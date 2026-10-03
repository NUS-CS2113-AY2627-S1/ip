#!/usr/bin/env python3
"""Runs the UI plan in isolated folders and stops at the first failure."""

import argparse
import json
import os
from pathlib import Path
import re
import shutil
import subprocess
import sys
import tempfile
import time


ROOT = Path(__file__).resolve().parents[1]
REPORT = ROOT / "_temp" / "ui-test-transcript.md"
JAVA = os.environ.get("JAVA_HOME", "")
JAVA = str(Path(JAVA) / "bin" / "java") if JAVA else "java"


def snapshot(directory):
    """Records file bytes, directories, and symbolic links without following links."""
    result = {}
    for path in directory.rglob("*"):
        name = str(path.relative_to(directory))
        if path.is_symlink():
            result[name] = ("symlink", os.readlink(path))
        elif path.is_dir():
            result[name] = ("directory",)
        else:
            result[name] = ("file", path.read_bytes())
    return result


def prepare(directory, setup):
    """Builds the plan's filesystem fixture before applying permission changes."""
    for name in setup.get("directories", []):
        (directory / name).mkdir(parents=True, exist_ok=True)
    for name, content in setup.get("files", {}).items():
        path = directory / name
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_bytes(content.encode("utf-8"))
    for name, content in setup.get("hex_files", {}).items():
        path = directory / name
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_bytes(bytes.fromhex(content))
    for name, target in setup.get("symlinks", {}).items():
        (directory / name).symlink_to(target)
    initial = snapshot(directory)
    for name, mode in setup.get("modes", {}).items():
        (directory / name).chmod(int(mode, 8))
    return initial


def restore_permissions(directory, setup):
    """Allows inspection and cleanup of unreadable or read-only test fixtures."""
    for name in sorted(setup.get("modes", {}), key=len):
        path = directory / name
        path.chmod(0o700 if path.is_dir() else 0o600)


def execute_session(directory, inputs, checkpoints, jar_path):
    """Optionally checks disk contents after commands while the process stays open."""
    command = ([JAVA, "-jar", jar_path.name] if jar_path
               else [JAVA, "-cp", str(ROOT / "out"), "clammy.Clammy"])
    if not checkpoints:
        return subprocess.run(command, input=inputs, text=True, encoding="utf-8",
                              capture_output=True, cwd=directory, timeout=15)
    process = subprocess.Popen(command, stdin=subprocess.PIPE, stdout=subprocess.PIPE,
                               stderr=subprocess.PIPE, text=True, encoding="utf-8", cwd=directory)
    lines = inputs.splitlines(keepends=True)
    sent = 0
    try:
        for checkpoint in checkpoints:
            through = checkpoint["after_command"]
            process.stdin.write("".join(lines[sent:through]))
            process.stdin.flush()
            sent = through
            deadline = time.monotonic() + 5
            saved_path = directory / "data" / "clammy.txt"
            expected = checkpoint["data"].encode("utf-8")
            while not saved_path.is_file() or saved_path.read_bytes() != expected:
                if process.poll() is not None or time.monotonic() >= deadline:
                    raise AssertionError(f"Data was not saved after command {through} while still running.")
                time.sleep(0.01)
            if process.poll() is not None:
                raise AssertionError("Clammy exited before the checkpoint.")
        stdout, stderr = process.communicate("".join(lines[sent:]), timeout=15)
        return subprocess.CompletedProcess(command, process.returncode, stdout, stderr)
    finally:
        if process.poll() is None:
            process.kill()
        process.wait()


def run_case(directory, section, report, jar_path):
    """Checks console output and saved data for every recorded process session."""
    if jar_path:
        copied_jar = directory / "Clammy [release].jar"
        shutil.copyfile(jar_path, copied_jar)
        jar_path = copied_jar
    setup_match = re.search(r"\*\*Setup:\*\*\s+```json\n(.*?)```", section, re.S)
    setup = json.loads(setup_match[1]) if setup_match else {}
    if setup.get("modes") and (os.name != "posix" or os.geteuid() == 0):
        raise AssertionError("Permission tests need a non-root macOS or Linux account.")
    initial = prepare(directory, setup)
    sessions = section.split("**Input:**")[1:]
    if not sessions:
        raise AssertionError("No input recorded in the test plan.")
    try:
        for number, session in enumerate(sessions, 1):
            blocks = re.findall(r"```text\n(.*?)```", session, re.S)
            inputs, expected = blocks[:2]
            checkpoint_match = re.search(r"\*\*Save checkpoints:\*\*\s+```json\n(.*?)```", session, re.S)
            checkpoints = json.loads(checkpoint_match[1]) if checkpoint_match else []
            completed = execute_session(directory, inputs, checkpoints, jar_path)
            report.extend([
                f"### Session {number}\n",
                f"**Input:**\n\n```text\n{inputs}```\n",
                f"**Program output:**\n\n```text\n{completed.stdout}```\n",
                "**Standard error:**\n\n" + (
                    f"```text\n{completed.stderr}```\n" if completed.stderr else "(empty)\n"
                ),
            ])
            if completed.returncode or completed.stderr or completed.stdout != expected:
                report.append(f"**Expected output:**\n\n```text\n{expected}```\n")
                raise AssertionError(f"Console mismatch or exit code {completed.returncode}.")
            saved_path = directory / "data" / "clammy.txt"
            if "**Expected saved data:** absent" in session:
                if saved_path.exists() or saved_path.is_symlink():
                    raise AssertionError("Expected the saved data file to be absent.")
            elif "**Expected saved data:** unchanged" in session:
                if "**Data unchanged:** yes" not in section:
                    raise AssertionError("An unchanged-data expectation needs a fixture comparison.")
            elif "**Expected saved data:**" in session:
                expected_data = blocks[2].encode("utf-8")
                actual_data = saved_path.read_bytes()
                if actual_data != expected_data:
                    report.append(f"Expected saved bytes: {expected_data!r}\n\nActual: {actual_data!r}\n")
                    raise AssertionError("Saved data does not match the plan.")
            if list((directory / "data").glob("clammy-*.tmp")):
                raise AssertionError("A temporary save file was left behind.")
    finally:
        restore_permissions(directory, setup)
    if "**Data unchanged:** yes" in section and snapshot(directory) != initial:
        raise AssertionError("The original filesystem fixture changed.")


def main():
    """Checks the runtime, runs cases in order, and writes full console transcripts."""
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--jar", type=Path, help="Run the UI cases against this executable JAR.")
    args = parser.parse_args()
    jar_path = args.jar.resolve() if args.jar else None
    if jar_path and not jar_path.is_file():
        parser.error(f"JAR does not exist: {jar_path}. Build it with ./gradlew shadowJar first.")
    version = subprocess.run([JAVA, "-version"], capture_output=True, text=True, check=True)
    if not re.search(r'version "25[.\"]', version.stderr + version.stdout):
        sys.exit("Select Java 25 before running these tests.")
    plan = (ROOT / "test" / "ui-test-plan.md").read_text(encoding="utf-8")
    cases = re.findall(r"### (TC-\d+: [^\n]+)\n(.*?)(?=\n### TC-|\Z)", plan, re.S)
    names = [name.split(":")[0] for name, _ in cases]
    if not cases or len(set(names)) != len(names):
        sys.exit("The plan must contain uniquely numbered cases.")
    REPORT.parent.mkdir(exist_ok=True)
    report = ["# UI test transcript\n", f"Runtime: {version.stderr.splitlines()[0]}\n",
              f"Application: {jar_path or 'out/ compiled classes'}\n"]
    for index, (name, section) in enumerate(cases):
        report.append(f"## {name}\n")
        try:
            with tempfile.TemporaryDirectory(prefix="clammy-ui-") as folder:
                run_case(Path(folder), section, report, jar_path)
        except (AssertionError, OSError, subprocess.SubprocessError, ValueError) as error:
            report.append(f"**FAIL:** {error}\n")
            remaining = [name for name, _ in cases[index + 1:]]
            report.append("Execution stopped. Cases not run:\n\n" + "\n".join(remaining))
            REPORT.write_text("\n".join(report), encoding="utf-8")
            print(f"FAIL: {name}: {error}\nTranscript: {REPORT}")
            return 1
        report.append("**PASS**\n")
        print(f"PASS: {name}")
    REPORT.write_text("\n".join(report), encoding="utf-8")
    print(f"All {len(cases)} cases passed. Transcript: {REPORT}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
