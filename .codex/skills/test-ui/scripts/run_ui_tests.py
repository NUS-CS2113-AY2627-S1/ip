#!/usr/bin/env python3
"""Run command-line UI test cases recorded in a Markdown test plan."""

from __future__ import annotations

import argparse
import shlex
import subprocess
import sys
from dataclasses import dataclass
from pathlib import Path


@dataclass(frozen=True)
class TestCase:
    """A single UI test session and its expected stdout transcript."""

    name: str
    aim: str
    inputs: str
    expected_output: str


def parse_arguments() -> argparse.Namespace:
    """Return the command-line arguments for the test runner."""
    parser = argparse.ArgumentParser(
        description="Run exact-output CLI UI tests from a Markdown plan."
    )
    parser.add_argument("plan", type=Path, help="Markdown UI test plan path")
    parser.add_argument(
        "--program",
        default="java -cp out yanny.Yanny",
        help="Program command to run for each test case",
    )
    parser.add_argument(
        "--timeout",
        type=float,
        default=10.0,
        help="Maximum seconds allowed for each test case",
    )
    return parser.parse_args()


def parse_test_plan(plan_path: Path) -> list[TestCase]:
    """Parse structured test cases from a Markdown plan."""
    lines = plan_path.read_text(encoding="utf-8").splitlines()
    cases: list[dict[str, object]] = []
    current: dict[str, object] | None = None
    section = ""
    in_code_block = False
    code_lines: list[str] = []

    def finish_code_block() -> None:
        nonlocal in_code_block, code_lines
        if current is None:
            raise ValueError("Found a code block outside a test case.")
        if section not in {"inputs", "expected output"}:
            raise ValueError("Code blocks must be under Inputs or Expected output.")
        current[section] = "\n".join(code_lines) + "\n"
        in_code_block = False
        code_lines = []

    def finish_case() -> None:
        if current is None:
            return
        required_sections = {"aim", "inputs", "expected output"}
        missing_sections = required_sections - current.keys()
        if missing_sections:
            missing = ", ".join(sorted(missing_sections))
            raise ValueError(f"Test case '{current['name']}' is missing: {missing}.")
        cases.append(current.copy())

    for line in lines:
        if in_code_block:
            if line.startswith("```"):
                finish_code_block()
            else:
                code_lines.append(line)
            continue

        if line.startswith("## Test case:"):
            finish_case()
            current = {"name": line.removeprefix("## Test case:").strip()}
            section = ""
            continue

        if current is None:
            continue

        if line.startswith("### "):
            section = line.removeprefix("### ").strip().lower()
            continue

        if line.startswith("```") and section in {"inputs", "expected output"}:
            in_code_block = True
            code_lines = []
            continue

        if section == "aim":
            aim = str(current.get("aim", ""))
            current["aim"] = f"{aim} {line.strip()}".strip()

    if in_code_block:
        raise ValueError("The test plan contains an unclosed code block.")
    finish_case()

    if not cases:
        raise ValueError("The test plan does not contain any test cases.")

    return [
        TestCase(
            name=str(case["name"]),
            aim=str(case["aim"]),
            inputs=str(case["inputs"]),
            expected_output=str(case["expected output"]),
        )
        for case in cases
    ]


def write_transcript(label: str, transcript: str) -> None:
    """Print a labeled transcript while preserving its contents."""
    print(label)
    if transcript:
        print(transcript, end="" if transcript.endswith("\n") else "\n")
    else:
        print("<empty>")


def run_test_case(test_case: TestCase, program: list[str], timeout: float) -> bool:
    """Run one test case, print its record, and return whether it passed."""
    print(f"\nTest case: {test_case.name}")
    print(f"Aim: {test_case.aim}")
    write_transcript("Console input:", test_case.inputs)

    try:
        result = subprocess.run(
            program,
            input=test_case.inputs,
            capture_output=True,
            text=True,
            timeout=timeout,
            check=False,
        )
    except subprocess.TimeoutExpired as exception:
        actual_output = exception.stdout or ""
        if isinstance(actual_output, bytes):
            actual_output = actual_output.decode(errors="replace")
        print("Result: FAIL (program timed out)")
        write_transcript("Actual console output:", actual_output)
        write_transcript("Expected console output:", test_case.expected_output)
        return False
    except OSError as exception:
        print(f"Result: FAIL (could not start program: {exception})")
        write_transcript("Actual console output:", "")
        write_transcript("Expected console output:", test_case.expected_output)
        return False

    write_transcript("Actual console output:", result.stdout)
    if result.stderr:
        write_transcript("Program error output:", result.stderr)

    output_matches = result.stdout == test_case.expected_output
    exited_successfully = result.returncode == 0
    if output_matches and exited_successfully:
        print("Result: PASS")
        return True

    print("Result: FAIL")
    if not exited_successfully:
        print(f"Program exited with status {result.returncode}.")
    write_transcript("Expected console output:", test_case.expected_output)
    return False


def main() -> int:
    """Run all test cases until the first failure."""
    arguments = parse_arguments()
    try:
        test_cases = parse_test_plan(arguments.plan)
    except (OSError, ValueError) as exception:
        print(f"Could not read test plan: {exception}", file=sys.stderr)
        return 2

    program = shlex.split(arguments.program)
    if not program:
        print("The program command cannot be empty.", file=sys.stderr)
        return 2

    print(f"Loaded {len(test_cases)} UI test case(s).")
    for test_case in test_cases:
        if not run_test_case(test_case, program, arguments.timeout):
            print("\nTest session terminated after the first failure.")
            return 1

    print(f"\nAll {len(test_cases)} UI test case(s) passed.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
