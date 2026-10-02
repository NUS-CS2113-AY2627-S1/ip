"""Compile Dog and check interactive command responses from a Markdown plan."""

import argparse
import difflib
import json
import os
from pathlib import Path
import queue
import re
import shutil
import subprocess
import sys
import tempfile
import threading
import time


class TestFailure(Exception):
    """Stop the complete test run without sending further commands."""


def check_output(label, expected, actual, record):
    if actual != expected:
        record(f"FAIL: {label}\nEXPECTED: {expected!r}\nACTUAL:   {actual!r}\n")
        record("".join(difflib.unified_diff(
            expected.splitlines(keepends=True), actual.splitlines(keepends=True),
            fromfile="expected", tofile="actual")))
        raise TestFailure(label)


def read_response(output_queue, expect_exit, timeout):
    """Read until the prompt or EOF, with a deadline even for a hung process."""
    deadline = time.monotonic() + timeout
    actual = ""
    while True:
        remaining = deadline - time.monotonic()
        if remaining <= 0:
            return actual, "timeout"
        try:
            character = output_queue.get(timeout=remaining)
        except queue.Empty:
            return actual, "timeout"
        if character is None:
            return actual, "exit"
        actual += character
        if not expect_exit and (actual == "> " or actual.endswith("\n> ")):
            return actual, "prompt"


def pump(stream, output_queue):
    try:
        while True:
            character = stream.read(1)
            if not character:
                break
            output_queue.put(character)
    finally:
        output_queue.put(None)


def load_plan(path):
    blocks = re.findall(r"```json\s*\n(.*?)\n```", path.read_text(encoding="utf-8"), re.S)
    if len(blocks) != 1:
        raise ValueError("The plan must contain exactly one JSON block.")
    plan = json.loads(blocks[0])
    if not isinstance(plan.get("startup"), str) or not plan.get("cases"):
        raise ValueError("The plan needs startup text and at least one case.")
    for case in plan["cases"]:
        if not case.get("name") or not case.get("aim") or not case.get("steps"):
            raise ValueError("Every case needs a name, aim, and steps.")
        if "data_file" in case and not isinstance(case["data_file"], str):
            raise ValueError("A case data_file must be a string.")
        if "startup" in case and not isinstance(case["startup"], str):
            raise ValueError("A case startup must be a string.")
        for index, step in enumerate(case["steps"]):
            if not isinstance(step.get("input"), str) or not isinstance(step.get("expected"), str):
                raise ValueError("Each step needs input and expected strings.")
            if "\n" in step["input"] or "\r" in step["input"]:
                raise ValueError("Each step must contain exactly one input line.")
            if "exit" in step and not isinstance(step["exit"], bool):
                raise ValueError("exit must be a boolean.")
            if step.get("exit", False) != (index == len(case["steps"]) - 1):
                raise ValueError("Only the final step must specify exit: true.")
    return plan


def run_case(case, startup, java, classes, working_directory, timeout, record):
    record(f"\nCASE: {case['name']}\nAIM: {case['aim']}\n")
    process = subprocess.Popen(
        [java, "-cp", str(classes), "dog.Dog"], cwd=working_directory,
        stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.STDOUT,
        text=True, encoding="utf-8", errors="replace", bufsize=1)
    output_queue = queue.Queue()
    reader = threading.Thread(target=pump, args=(process.stdout, output_queue), daemon=True)
    reader.start()
    try:
        checks = [{"expected": startup}] + case["steps"]
        for step in checks:
            label = step.get("input", "startup")
            if "input" in step:
                record(f"\nINPUT: {step['input']}\n")
                try:
                    process.stdin.write(step["input"] + "\n")
                    process.stdin.flush()
                except (BrokenPipeError, OSError) as error:
                    record(f"EXPECTED: {step['expected']!r}\nACTUAL: process unavailable ({error})\n")
                    raise TestFailure(label) from error
            expect_exit = step.get("exit", False)
            expected = step["expected"] + ("" if expect_exit else "> ")
            actual, state = read_response(output_queue, expect_exit, timeout)
            record("OUTPUT:\n" + actual + "\n")
            check_output(label, expected, actual, record)
            if state != ("exit" if expect_exit else "prompt"):
                record(f"EXPECTED STATE: {'exit' if expect_exit else 'prompt'}; ACTUAL STATE: {state}\n")
                raise TestFailure(label)
            if expect_exit and process.wait(timeout=timeout) != 0:
                record(f"EXPECTED EXIT: 0; ACTUAL EXIT: {process.returncode}\n")
                raise TestFailure(label)
        record("PASS\n")
    finally:
        if process.poll() is None:
            process.kill()
        process.wait()
        reader.join(timeout=1)
        process.stdin.close()
        process.stdout.close()


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--repo", type=Path, default=Path.cwd())
    parser.add_argument("--plan", type=Path, default=Path("test/ui-test-plan.md"))
    parser.add_argument("--transcript", type=Path, default=Path("_temp/ui-test-session.txt"))
    parser.add_argument("--java-home", default=os.environ.get("JAVA_HOME"))
    parser.add_argument("--timeout", type=float, default=5)
    args = parser.parse_args()
    repo = args.repo.resolve()
    transcript = repo / args.transcript
    transcript.parent.mkdir(parents=True, exist_ok=True)
    with transcript.open("w", encoding="utf-8") as log:
        def record(text):
            print(text, end="", flush=True)
            log.write(text)
            log.flush()

        try:
            if args.timeout <= 0:
                raise ValueError("Timeout must be positive.")
            plan = load_plan(repo / args.plan)
            suffix = ".exe" if os.name == "nt" else ""
            commands = {}
            for name in ("java", "javac"):
                executable = (str(Path(args.java_home) / "bin" / (name + suffix))
                              if args.java_home else shutil.which(name))
                if not executable:
                    raise ValueError("JDK 25 is required; supply --java-home.")
                version = subprocess.run([executable, "-version"], capture_output=True,
                                         text=True, timeout=10)
                details = version.stdout + version.stderr
                if version.returncode or not re.search(r'(?:version\s+"?|javac\s+)25(?:[.\s"-]|$)', details):
                    raise ValueError(f"Expected JDK 25 for {name}, got: {details}")
                record(details + "\n")
                commands[name] = executable
            with tempfile.TemporaryDirectory(prefix="dog-ui-tests-") as temporary:
                temporary_root = Path(temporary)
                classes = temporary_root / "classes"
                classes.mkdir()
                sources = sorted((repo / "src/main/java").rglob("*.java"))
                if not sources:
                    raise ValueError("No production Java sources found.")
                compilation = subprocess.run(
                    [commands["javac"], "-encoding", "UTF-8", "-d", str(classes)]
                    + [str(source) for source in sources], capture_output=True,
                    text=True, timeout=30)
                record("COMPILE OUTPUT:\n" + compilation.stdout + compilation.stderr)
                if compilation.returncode:
                    raise TestFailure("Compilation failed; expected exit 0.")
                for index, case in enumerate(plan["cases"]):
                    working_directory = temporary_root / "cases" / str(index)
                    working_directory.mkdir(parents=True)
                    if "data_file" in case:
                        data_directory = working_directory / "data"
                        data_directory.mkdir()
                        (data_directory / "dog.txt").write_text(
                            case["data_file"], encoding="utf-8")
                    startup = case.get("startup", plan["startup"])
                    run_case(case, startup, commands["java"], classes,
                             working_directory, args.timeout, record)
            record(f"\nPASS: {len(plan['cases'])} cases. Transcript: {transcript}\n")
            return 0
        except (TestFailure, ValueError, OSError, subprocess.TimeoutExpired) as error:
            record(f"\nSTOPPED: {error}\nTranscript: {transcript}\n")
            return 1


if __name__ == "__main__":
    sys.exit(main())
