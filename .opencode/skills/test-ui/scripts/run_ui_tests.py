#!/usr/bin/env python3
"""Compiles Maple and runs the UI cases in test/ui-test-plan.md."""

import datetime
import re
import shutil
import subprocess
import sys
import tempfile
from pathlib import Path

PLAN_PATH = Path("test/ui-test-plan.md")
SOURCE_ROOT = Path("src/main/java")
OUTPUT_ROOT = Path("out")
MAIN_CLASS = "maple.Maple"
CASE_PATTERN = re.compile(
    r"### (TC\d+ [^\n]+)\n(.*?)(?=\n### TC\d+ |\Z)",
    re.DOTALL,
)


def normalize(output):
    """Returns output without insignificant surrounding whitespace."""
    lines = [line.rstrip() for line in output.splitlines()]
    while lines and not lines[0]:
        lines.pop(0)
    while lines and not lines[-1]:
        lines.pop()
    return lines


def extract_fenced_block(case_body, label):
    """Returns the fenced block that follows the given case label."""
    pattern = rf"{label}:\n\n```\n(.*?)\n```"
    match = re.search(pattern, case_body, re.DOTALL)
    if not match:
        raise ValueError(f"Missing {label.lower()} block")
    return normalize(match.group(1))


def load_cases(plan):
    """Returns the test cases defined before the session record."""
    if "## Test cases" not in plan or "## Test session" not in plan:
        raise ValueError("The plan needs Test cases and Test session sections")

    test_section = plan.split("## Test cases", 1)[1].split("## Test session", 1)[0]
    cases = []
    for title, case_body in CASE_PATTERN.findall(test_section):
        aim_match = re.search(r"Aim: (.*?)\n\nInputs:", case_body, re.DOTALL)
        if not aim_match:
            raise ValueError(f"Missing aim for {title}")
        aim = " ".join(aim_match.group(1).splitlines())
        inputs = extract_fenced_block(case_body, "Inputs")
        expected = extract_fenced_block(case_body, "Expected output")
        cases.append((title, aim, inputs, expected))
    return cases


def check_java_version():
    """Stops the test run unless Java 25 is available."""
    result = subprocess.run(
        ["javac", "-version"],
        capture_output=True,
        text=True,
    )
    version = (result.stdout or result.stderr).strip()
    if result.returncode != 0 or not re.match(r"javac 25(?:\.|$)", version):
        print(f"Java 25 is required, but found: {version or 'javac unavailable'}")
        sys.exit(1)


def compile_program():
    """Performs a clean compilation of every Java source file."""
    source_files = sorted(str(path) for path in SOURCE_ROOT.rglob("*.java"))
    if not source_files:
        print(f"No Java source files found under {SOURCE_ROOT}.")
        sys.exit(1)

    shutil.rmtree(OUTPUT_ROOT, ignore_errors=True)
    OUTPUT_ROOT.mkdir(parents=True)
    result = subprocess.run(
        ["javac", "-Xlint:all", "-d", str(OUTPUT_ROOT), *source_files],
        capture_output=True,
        text=True,
    )
    if result.returncode != 0:
        print("Compilation failed.")
        print(result.stderr)
        sys.exit(result.returncode)


def run_case(inputs):
    """Runs Maple in an isolated working dir so cases do not share saved state."""
    with tempfile.TemporaryDirectory() as workdir:
        result = subprocess.run(
            ["java", "-cp", str(OUTPUT_ROOT.resolve()), MAIN_CLASS],
            input="\n".join(inputs) + "\n",
            capture_output=True,
            text=True,
            cwd=workdir,
        )
    return normalize(result.stdout)


def show_failure(title, aim, expected, actual):
    """Prints the first failed test and its output difference."""
    print(f"FAIL {title}")
    print(f"Aim: {aim}")
    print("\nExpected output:\n")
    print("\n".join(expected))
    print("\nActual output:\n")
    print("\n".join(actual))


def format_session(sessions):
    """Returns a Markdown record of the successful test session."""
    lines = [f"Run on {datetime.date.today().isoformat()}. All {len(sessions)} cases passed."]
    for title, inputs, actual in sessions:
        lines.extend([
            "",
            f"### {title}",
            "",
            "Console input:",
            "",
            "```",
            *inputs,
            "```",
            "",
            "Console output:",
            "",
            "```",
            *actual,
            "```",
            "",
            "Result: PASS",
        ])
    return "\n".join(lines)


def save_session(plan, session_record):
    """Replaces the previous test session record in the plan."""
    plan_without_session = plan.split("## Test session", 1)[0].rstrip()
    PLAN_PATH.write_text(
        plan_without_session + "\n\n## Test session\n\n" + session_record + "\n"
    )


def main():
    """Runs every planned case and stops immediately on a failure."""
    plan = PLAN_PATH.read_text()
    try:
        cases = load_cases(plan)
    except ValueError as exception:
        print(f"Invalid test plan: {exception}")
        sys.exit(1)

    check_java_version()
    compile_program()
    sessions = []
    for title, aim, inputs, expected in cases:
        actual = run_case(inputs)
        if actual != expected:
            show_failure(title, aim, expected, actual)
            sys.exit(1)
        print(f"PASS {title}")
        sessions.append((title, inputs, actual))

    session_record = format_session(sessions)
    print(f"\nAll {len(sessions)} cases passed.\n")
    print(session_record)
    save_session(plan, session_record)


if __name__ == "__main__":
    main()
