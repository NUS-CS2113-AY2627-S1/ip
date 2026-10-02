#!/usr/bin/env python3
"""Run command-line UI tests described by a Markdown test plan."""

from __future__ import annotations

import argparse
import re
import shlex
import subprocess
import sys
from dataclasses import dataclass
from pathlib import Path


@dataclass
class TestCase:
    """A command-line test case parsed from the Markdown test plan."""

    name: str
    aim: str
    test_input: str
    expected_output: str


CASE_PATTERN = re.compile(
    r"^## (?P<name>.+?)\n"
    r"\nAim: (?P<aim>.+?)\n"
    r"\n### Input\n"
    r"\n```text\n(?P<input>.*?)\n```\n"
    r"\n### Expected output\n"
    r"\n```text\n(?P<expected>.*?)\n```",
    re.MULTILINE | re.DOTALL,
)


def normalize_output(text: str) -> str:
    """Returns text with platform-neutral line endings and one trailing newline."""
    return text.replace("\r\n", "\n").rstrip("\n") + "\n"


def parse_plan(plan_path: Path) -> list[TestCase]:
    """Returns all test cases declared in the supplied Markdown plan."""
    plan = plan_path.read_text()
    cases = [
        TestCase(
            match.group("name"),
            match.group("aim"),
            match.group("input") + "\n",
            match.group("expected") + "\n",
        )
        for match in CASE_PATTERN.finditer(plan)
    ]
    if not cases:
        raise ValueError("The plan contains no test cases in the required format.")
    return cases


def format_transcript(case: TestCase, actual_output: str, passed: bool) -> str:
    """Returns a readable record of one test case's console session."""
    result = "PASS" if passed else "FAIL"
    return (
        f"=== {case.name} ===\n"
        f"Aim: {case.aim}\n"
        "--- Console input ---\n"
        f"{case.test_input}"
        "--- Console output ---\n"
        f"{actual_output}"
        f"--- Result: {result} ---\n\n"
    )


def run_test_cases(cases: list[TestCase], command: str, transcript_path: Path) -> int:
    """Runs cases in order, writing a transcript and stopping at the first failure."""
    transcript_path.parent.mkdir(parents=True, exist_ok=True)
    with transcript_path.open("w") as transcript:
        for case in cases:
            result = subprocess.run(
                shlex.split(command),
                input=case.test_input,
                capture_output=True,
                text=True,
            )
            actual_output = normalize_output(result.stdout)
            expected_output = normalize_output(case.expected_output)
            passed = result.returncode == 0 and actual_output == expected_output
            transcript.write(format_transcript(case, actual_output, passed))

            if passed:
                print(f"PASS: {case.name}")
                continue

            print(f"FAIL: {case.name}")
            print("Expected output:")
            print(expected_output, end="")
            print("Actual output:")
            print(actual_output, end="")
            if result.stderr:
                print("Standard error:")
                print(result.stderr, end="")
            return 1
    return 0


def main() -> int:
    """Parses arguments and runs the UI test plan."""
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--plan", required=True, type=Path)
    parser.add_argument("--command", required=True)
    parser.add_argument("--transcript", required=True, type=Path)
    args = parser.parse_args()

    try:
        cases = parse_plan(args.plan)
    except (OSError, ValueError) as error:
        print(f"Unable to read test plan: {error}", file=sys.stderr)
        return 2

    return run_test_cases(cases, args.command, args.transcript)


if __name__ == "__main__":
    raise SystemExit(main())
