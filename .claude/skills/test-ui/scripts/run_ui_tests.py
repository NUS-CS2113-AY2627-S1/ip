#!/usr/bin/env python3
"""Compile Buddy, replay the test cases in test/ui-test-plan.md as one
continuous console session, and check the real output against each test
case's expected output. Stops at the first mismatch.
"""
import re
import shutil
import subprocess
import sys
import tempfile
from dataclasses import dataclass
from pathlib import Path

# scripts -> test-ui -> skills -> .claude -> repo root
REPO_ROOT_DEPTH = 4
MAIN_CLASS = "buddy.Buddy"
SRC_DIR = "src/main/java"
TEST_PLAN_PATH = "test/ui-test-plan.md"
DATA_DIR = "data"

CASE_PATTERN = re.compile(r"^## (.+)$", re.MULTILINE)
AIM_PATTERN = re.compile(r"\*\*Aim:\*\*\s*(.+)")
INPUT_PATTERN = re.compile(r"\*\*Input:\*\*\s*```[a-zA-Z]*\n(.*?)\n```", re.S)
OUTPUT_PATTERN = re.compile(r"\*\*Expected Output:\*\*\s*```[a-zA-Z]*\n(.*?)\n```", re.S)


@dataclass
class TestCase:
    title: str
    aim: str
    input_lines: list
    expected_lines: list


def fail(message):
    print(f"ERROR: {message}")
    sys.exit(1)


def find_repo_root():
    if len(sys.argv) > 1:
        return Path(sys.argv[1]).resolve()
    return Path(__file__).resolve().parents[REPO_ROOT_DEPTH]


def parse_test_plan(path):
    text = path.read_text(encoding="utf-8").replace("\r\n", "\n")
    headings = list(CASE_PATTERN.finditer(text))
    if not headings:
        fail(f"No '## ' test case headings found in {path}")

    test_cases = []
    for i, heading in enumerate(headings):
        title = heading.group(1).strip()
        start = heading.end()
        end = headings[i + 1].start() if i + 1 < len(headings) else len(text)
        section = text[start:end]

        aim_match = AIM_PATTERN.search(section)
        input_match = INPUT_PATTERN.search(section)
        output_match = OUTPUT_PATTERN.search(section)
        if not aim_match:
            fail(f"Test case '{title}' is missing a '**Aim:** ...' line")
        if not input_match:
            fail(f"Test case '{title}' is missing an '**Input:**' code block")
        if not output_match:
            fail(f"Test case '{title}' is missing an '**Expected Output:**' code block")

        test_cases.append(TestCase(
            title=title,
            aim=aim_match.group(1).strip(),
            input_lines=input_match.group(1).split("\n"),
            expected_lines=output_match.group(1).split("\n"),
        ))
    return test_cases


def compile_java(repo_root):
    src_dir = repo_root / SRC_DIR
    java_files = sorted(str(p) for p in src_dir.rglob("*.java"))
    if not java_files:
        fail(f"No .java files found in {src_dir}")

    out_dir = Path(tempfile.mkdtemp(prefix="buddy-ui-test-"))
    try:
        result = subprocess.run(
            ["javac", "-d", str(out_dir), *java_files],
            capture_output=True, text=True,
        )
    except FileNotFoundError:
        fail("Could not find 'javac' on PATH. Make sure Java 25 is installed "
             "and on PATH (see AGENTS.md's Java version requirement).")

    if result.returncode != 0:
        print("COMPILATION FAILED")
        print(result.stdout)
        print(result.stderr)
        sys.exit(1)
    return out_dir


def run_buddy(classes_dir, repo_root, stdin_text):
    # Each call replays the *entire* cumulative session from scratch in a
    # fresh process, so Buddy's saved-data file must be cleared first -
    # otherwise tasks it loads from a previous call's save file would be
    # double-counted on top of the replayed input.
    shutil.rmtree(repo_root / DATA_DIR, ignore_errors=True)
    try:
        return subprocess.run(
            ["java", "-classpath", str(classes_dir), MAIN_CLASS],
            input=stdin_text, capture_output=True, text=True,
            cwd=repo_root, timeout=10,
        )
    except FileNotFoundError:
        fail("Could not find 'java' on PATH. Make sure Java 25 is installed "
             "and on PATH (see AGENTS.md's Java version requirement).")
    except subprocess.TimeoutExpired:
        fail("Buddy did not exit within 10 seconds - possible infinite loop "
             "or blocked read.")


def print_transcript(transcript):
    print("\n===== Console session transcript =====")
    for entry in transcript:
        print(f"\n--- {entry['title']} [{entry['status']}] ---")
        print(f"Aim: {entry['aim']}")
        print("Input:")
        for line in entry["input"]:
            print(f"  {line}")
        print("Output:")
        for line in entry["actual"]:
            print(f"  {line}")


def report_failure(test_case, actual_slice, proc):
    print(f"\nFAILURE in test case: {test_case.title}")
    print(f"Aim: {test_case.aim}\n")
    print("--- Expected ---")
    print("\n".join(test_case.expected_lines))
    print("\n--- Actual ---")
    print("\n".join(actual_slice))
    if proc.stderr:
        print("\n--- stderr ---")
        print(proc.stderr)
    if proc.returncode != 0:
        print(f"\n--- exit code: {proc.returncode} ---")


def main():
    repo_root = find_repo_root()
    buddy_source = repo_root / SRC_DIR / (MAIN_CLASS.replace(".", "/") + ".java")
    test_plan_path = repo_root / TEST_PLAN_PATH
    if not buddy_source.exists():
        fail(f"Expected {buddy_source} to exist - is this the Buddy repo root?")
    if not test_plan_path.exists():
        fail(f"Expected {test_plan_path} to exist - nothing to test.")

    test_cases = parse_test_plan(test_plan_path)
    classes_dir = compile_java(repo_root)

    cumulative_input = []
    consumed = 0
    transcript = []

    for test_case in test_cases:
        cumulative_input.extend(test_case.input_lines)
        stdin_text = "\n".join(cumulative_input) + "\n"
        proc = run_buddy(classes_dir, repo_root, stdin_text)
        actual_lines = proc.stdout.splitlines()

        expected_len = len(test_case.expected_lines)
        actual_slice = actual_lines[consumed:consumed + expected_len]
        passed = actual_slice == test_case.expected_lines

        transcript.append({
            "title": test_case.title,
            "aim": test_case.aim,
            "input": test_case.input_lines,
            "actual": actual_slice,
            "status": "PASS" if passed else "FAIL",
        })

        if not passed:
            print_transcript(transcript)
            report_failure(test_case, actual_slice, proc)
            sys.exit(1)

        consumed += expected_len

    print_transcript(transcript)
    print(f"\nALL TESTS PASSED ({len(test_cases)}/{len(test_cases)})")
    sys.exit(0)


if __name__ == "__main__":
    main()
