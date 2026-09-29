"""Run the recorded console UI plan with Java 25, stopping at the first failure."""

from pathlib import Path
import os
import re
import subprocess
import sys
import tempfile
import threading


ROOT = Path(__file__).resolve().parents[1]
PLAN = ROOT / "test/ui-test-plan.md"
TRANSCRIPT = ROOT / "test/ui-test-transcript.md"
SEPARATOR = "_" * 60


def normalize(text):
    return text.replace("\r\n", "\n")


def read_block(case, label):
    """Read exact inline text or a repository-relative fixture from the plan."""
    match = re.search(rf"- {re.escape(label)}:\s*\n\n```text\n(.*?)```", case, re.S)
    if match:
        return match.group(1)
    match = re.search(rf"- {re.escape(label)} file: `([^`]+)`", case)
    if match:
        return (ROOT / match.group(1)).read_text(encoding="utf-8")
    raise ValueError(f"Missing {label} in test case")


def option(case, label, default=None):
    match = re.search(rf"^- {re.escape(label)}: `([^`]+)`", case, re.M)
    return match.group(1) if match else default


def storage_snapshot(workspace):
    """Capture only test-owned storage, including file bytes and directories."""
    data = workspace / "data"
    if not data.exists():
        return {}
    paths = [data, *data.rglob("*")] if data.is_dir() else [data]
    return {path.relative_to(workspace).as_posix(): None if path.is_dir() else path.read_bytes()
            for path in paths}


def setup_storage(case, workspace):
    setup = option(case, "Storage setup", "missing-folder")
    data = workspace / "data"
    saved = data / "chillguy.txt"
    if setup == "parent-is-file":
        data.write_text("Keep this existing file.\n", encoding="utf-8")
    elif setup == "file-is-directory":
        saved.mkdir(parents=True)
        (saved / "keep.txt").write_text("Keep this directory.\n", encoding="utf-8")
    elif setup in ("missing-file", "empty-file", "invalid-utf8"):
        data.mkdir()
        if setup == "empty-file":
            saved.write_bytes(b"")
        elif setup == "invalid-utf8":
            saved.write_bytes(b"T | 0 | valid\n\xff\xfe\n")
    elif setup != "missing-folder":
        raise ValueError(f"Unknown storage setup: {setup}")
    if re.search(r"- Initial saved tasks(?: file)?:", case):
        data.mkdir(exist_ok=True)
        saved.write_text(read_block(case, "Initial saved tasks"), encoding="utf-8", newline="\n")


def change_storage_block(workspace, kind, should_block):
    """Block writes only inside a newly created test sandbox, then restore its original file."""
    data = workspace / "data"
    saved = data / "chillguy.txt"
    original = data / "chillguy.original.txt"
    if kind == "folder":
        if should_block:
            data.write_text("Temporary test blocker.\n", encoding="utf-8")
        else:
            data.unlink()
    elif kind == "file":
        if should_block:
            saved.rename(original)
            saved.mkdir()
            (saved / "keep.txt").write_text("Prevent replacement.\n", encoding="utf-8")
        else:
            (saved / "keep.txt").unlink()
            saved.rmdir()
            original.rename(saved)
    else:
        raise ValueError(f"Unknown storage block: {kind}")


def run_case(arguments, inputs, workspace, case):
    checkpoints = re.findall(r"^- Saved tasks after command (\d+):", case, re.M)
    block_kind = option(case, "Storage block")
    if not checkpoints and not block_kind:
        return subprocess.run(arguments, input=inputs, capture_output=True, text=True,
                              encoding="utf-8", cwd=workspace, timeout=15), []

    # Read complete responses before checking disk state, while the app is still running.
    output = []
    checks = []
    with subprocess.Popen(arguments, stdin=subprocess.PIPE, stdout=subprocess.PIPE,
                          stderr=subprocess.PIPE, text=True, encoding="utf-8", cwd=workspace) as process:
        timer = threading.Timer(15, process.kill)
        timer.start()
        try:
            def read_response():
                separators = 0
                while separators < 2:
                    line = process.stdout.readline()
                    if not line:
                        raise RuntimeError("App ended before completing its response")
                    output.append(line)
                    separators += line.rstrip("\r\n") == SEPARATOR

            read_response()
            for number, command in enumerate(inputs.splitlines(keepends=True), 1):
                process.stdin.write(command)
                process.stdin.flush()
                read_response()
                if block_kind and str(number) == option(case, "Restore storage after command"):
                    change_storage_block(workspace, block_kind, False)
                if str(number) in checkpoints:
                    expected = read_block(case, f"Saved tasks after command {number}")
                    actual = (workspace / "data/chillguy.txt").read_text(encoding="utf-8")
                    if normalize(actual) != normalize(expected):
                        raise RuntimeError(f"Saved data mismatch after command {number}: "
                                           f"expected {expected!r}, actual {actual!r}")
                    checks.append(f"PASS: saved data after command {number} (app still running)")
                if block_kind and number == 1:
                    change_storage_block(workspace, block_kind, True)
            remaining, errors = process.communicate(timeout=15)
            output.append(remaining)
        except (OSError, RuntimeError, subprocess.TimeoutExpired) as exception:
            process.kill()
            remaining, errors = process.communicate()
            output.append(remaining)
            checks.append(f"FAIL: {exception}")
        finally:
            timer.cancel()
    return subprocess.CompletedProcess(arguments, process.returncode, "".join(output), errors), checks


def main():
    transcript = ["# UI Test Transcript\n"]
    for executable in ("java", "javac"):
        result = subprocess.run([executable, "-version"], capture_output=True, text=True, cwd=ROOT)
        version = result.stdout + result.stderr
        transcript.append(f"```text\n{executable} -version\n{version}```\n")
        if result.returncode or not re.search(r'(?:version "|javac )25(?:\.|\")', version):
            raise RuntimeError(f"Java 25 required: {version}")

    sources = sorted(str(path) for path in (ROOT / "src/main/java").rglob("*.java"))
    build = subprocess.run(["javac", "-d", "out", *sources], capture_output=True, text=True, cwd=ROOT)
    source_paths = " ".join(Path(source).relative_to(ROOT).as_posix() for source in sources)
    transcript.append(f"Build: `javac -d out {source_paths}`\n")
    if build.returncode or build.stdout or build.stderr:
        transcript.append(f"```text\n{build.stdout}{build.stderr}```\n")
        TRANSCRIPT.write_text("\n".join(transcript), encoding="utf-8")
        raise RuntimeError("Compilation failed or produced unexpected output; see transcript")

    wrapper = [str(ROOT / "gradlew.bat")] if os.name == "nt" else ["sh", str(ROOT / "gradlew")]
    jar_build = subprocess.run([*wrapper, "--console=plain", "shadowJar"], capture_output=True,
                               text=True, encoding="utf-8", cwd=ROOT)
    transcript.append(f"Fat JAR build: `{subprocess.list2cmdline(wrapper)} --console=plain shadowJar`\n\n"
                      f"```text\n{jar_build.stdout}{jar_build.stderr}```\n\n"
                      f"Exit code: {jar_build.returncode}\n")
    if jar_build.returncode:
        TRANSCRIPT.write_text("\n".join(transcript), encoding="utf-8")
        raise RuntimeError("Fat JAR build failed; see transcript")

    cases = re.split(r"^### ", PLAN.read_text(encoding="utf-8"), flags=re.M)[1:]
    if not cases:
        raise ValueError("No test cases found")
    sandbox_root = ROOT / "_temp"
    sandbox_root.mkdir(exist_ok=True)
    with tempfile.TemporaryDirectory(prefix="ui-tests-", dir=sandbox_root) as directory:
        return run_cases(cases, transcript, Path(directory))


def run_cases(cases, transcript, sandbox_root):
    sessions = {}
    for index, case in enumerate(cases):
        name = case.splitlines()[0]
        command = re.search(r"- Command: `([^`]+)`", case).group(1)
        if command == "java -cp out chillguy.Chillguy":
            launch_arguments = ["-cp", str(ROOT / "out"), "chillguy.Chillguy"]
        elif command == "java -jar build/libs/chillguy-all.jar":
            launch_arguments = ["-jar", str(ROOT / "build/libs/chillguy-all.jar")]
        else:
            raise ValueError(f"Unsupported command: {command}")
        inputs = read_block(case, "Input")
        expected = read_block(case, "Expected output")
        session = option(case, "Storage session", str(index))
        if session not in sessions:
            workspace = sandbox_root / str(index)
            workspace.mkdir()
            sessions[session] = workspace
            setup_storage(case, workspace)
        workspace = sessions[session]
        before = storage_snapshot(workspace)
        arguments = ["java", "-Dstdout.encoding=UTF-8", "-Dstderr.encoding=UTF-8",
                     *launch_arguments]
        actual, checks = run_case(arguments, inputs, workspace, case)
        if re.search(r"- Expected saved tasks(?: file)?:", case):
            saved = workspace / "data/chillguy.txt"
            expected_saved = read_block(case, "Expected saved tasks")
            actual_saved = saved.read_text(encoding="utf-8") if saved.is_file() else None
            if actual_saved is not None and normalize(actual_saved) == normalize(expected_saved):
                checks.append("PASS: saved file contents")
            else:
                checks.append(f"FAIL: saved data expected {expected_saved!r}, actual {actual_saved!r}")
        if option(case, "Expected storage unchanged") == "yes":
            checks.append(f"{'PASS' if before == storage_snapshot(workspace) else 'FAIL'}: "
                          "storage bytes and paths unchanged")
        leftovers = list((workspace / "data").glob("chillguy-*.tmp"))
        checks.append(f"{'FAIL' if leftovers else 'PASS'}: no temporary save files remain")
        passed = (actual.returncode == 0 and not actual.stderr
                  and normalize(actual.stdout) == normalize(expected)
                  and all(check.startswith("PASS:") for check in checks))
        transcript.append(f"## {name}: {'PASS' if passed else 'FAIL'}\n\n"
                          f"Command: `{subprocess.list2cmdline(arguments)}`\n\n"
                          f"Working directory: isolated sandbox `{index}`, storage session `{session}`.\n\n"
                          f"Input:\n\n```text\n{inputs}```\n\n"
                          f"Actual output:\n\n```text\n{actual.stdout}```\n\n"
                          f"Exit code: {actual.returncode}\n\n"
                          f"Stderr: {actual.stderr!r}\n\n"
                          + "\n".join(f"- {check}" for check in checks) + "\n")
        if not passed:
            transcript.append(f"Expected output:\n\n```text\n{expected}```\n")
        TRANSCRIPT.write_text("\n".join(transcript), encoding="utf-8")
        print(f"{'PASS' if passed else 'FAIL'}: {name}", flush=True)
        if not passed:
            print(f"Stopped at first failure. See {TRANSCRIPT}")
            return 1
    print(f"All {len(cases)} cases passed. Transcript: {TRANSCRIPT}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
