#!/usr/bin/env python3
"""Run fail-fast console UI tests described in a Markdown test plan."""

from __future__ import annotations

import argparse
import json
import os
import shlex
import subprocess
import sys
from pathlib import Path


def parse_arguments() -> argparse.Namespace:
    """Returns the command-line options for the test runner."""
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument(
        "--plan",
        default="test/ui-test-plan.md",
        help="Markdown plan containing an Executable test cases JSON block",
    )
    parser.add_argument(
        "--program",
        required=True,
        help="Program command to execute for each test case",
    )
    parser.add_argument(
        "--cwd",
        default=".",
        help="Working directory in which to run the program",
    )
    parser.add_argument(
        "--timeout",
        type=float,
        default=30.0,
        help="Maximum seconds allowed for one test case",
    )
    return parser.parse_args()


def load_test_cases(plan_path: Path) -> list[dict]:
    """Loads and validates executable test cases from the Markdown plan."""
    plan_text = plan_path.read_text(encoding="utf-8")
    block_start = plan_text.find("```json", plan_text.find("Executable test cases"))
    if block_start == -1:
        raise ValueError("The plan must contain an Executable test cases JSON block.")

    json_start = plan_text.find("\n", block_start) + 1
    json_end = plan_text.find("```", json_start)
    if json_end == -1:
        raise ValueError("The executable test-case JSON block is not closed.")

    cases = json.loads(plan_text[json_start:json_end])
    if not isinstance(cases, list) or not cases:
        raise ValueError("The executable test-case list must contain at least one case.")

    for index, case in enumerate(cases, start=1):
        if not isinstance(case, dict):
            raise ValueError(f"Test case {index} must be a JSON object.")
        for field in ("id", "aim", "inputs"):
            if not isinstance(case.get(field), str if field != "inputs" else list):
                raise ValueError(f"Test case {index} must contain a valid '{field}' field.")
        if not case["inputs"] or not all(isinstance(item, str) for item in case["inputs"]):
            raise ValueError(f"Test case {index} must contain a non-empty string inputs list.")
        has_output = "expected_output" in case or "expected_output_lines" in case
        if not has_output or ("expected_output" in case and "expected_output_lines" in case):
            raise ValueError(
                f"Test case {index} must contain exactly one expected-output field."
            )
        if "expected_output" in case and not isinstance(case["expected_output"], str):
            raise ValueError(f"Test case {index} has an invalid expected_output field.")
        if "expected_output_lines" in case:
            lines = case["expected_output_lines"]
            if not isinstance(lines, list) or not all(isinstance(line, str) for line in lines):
                raise ValueError(f"Test case {index} has invalid expected_output_lines.")
    return cases


def split_program_command(command: str) -> list[str]:
    """Splits a program command without invoking a shell."""
    parts = shlex.split(command, posix=os.name != "nt")
    if not parts:
        raise ValueError("The program command must not be empty.")
    return parts


def expected_output(case: dict) -> str:
    """Returns the exact expected output represented by a test case."""
    if "expected_output" in case:
        return case["expected_output"]
    return "\n".join(case["expected_output_lines"]) + "\n"


def normalize_line_endings(text: str) -> str:
    """Normalizes platform line endings while preserving all other output."""
    return text.replace("\r\n", "\n").replace("\r", "\n")


def display_session(case: dict, session_input: str, actual_output: str) -> None:
    """Prints the console input and output captured for a test case."""
    print(f"=== {case['id']}: {case['aim']} ===")
    print("--- Console input ---")
    print(session_input, end="" if session_input.endswith("\n") else "\n")
    print("--- Console output ---")
    print(actual_output, end="" if actual_output.endswith("\n") else "\n")


def run_case(case: dict, program: list[str], cwd: Path, timeout: float) -> bool:
    """Runs one test case and returns whether its output matches."""
    session_input = "\n".join(case["inputs"]) + "\n"
    try:
        result = subprocess.run(
            program,
            cwd=cwd,
            input=session_input,
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT,
            text=True,
            encoding="utf-8",
            errors="replace",
            timeout=timeout,
            check=False,
        )
        actual_output = result.stdout
        if result.returncode != 0:
            actual_output += f"\n[Process exited with code {result.returncode}]\n"
    except subprocess.TimeoutExpired as exception:
        partial_output = exception.stdout or ""
        if isinstance(partial_output, bytes):
            partial_output = partial_output.decode("utf-8", errors="replace")
        actual_output = partial_output + f"\n[Timed out after {timeout:g} seconds]\n"

    display_session(case, session_input, actual_output)
    actual = normalize_line_endings(actual_output)
    expected = normalize_line_endings(expected_output(case))
    if actual == expected:
        print("--- Result: PASS ---")
        return True

    print("--- Result: FAIL ---")
    print("--- Expected output ---")
    print(expected, end="" if expected.endswith("\n") else "\n")
    return False


def main() -> int:
    """Runs all test cases until the first failure."""
    arguments = parse_arguments()
    plan_path = Path(arguments.plan)
    cwd = Path(arguments.cwd).resolve()
    try:
        cases = load_test_cases(plan_path)
        program = split_program_command(arguments.program)
    except (OSError, ValueError, json.JSONDecodeError) as exception:
        print(f"Unable to load UI tests: {exception}", file=sys.stderr)
        return 2

    for case in cases:
        if not run_case(case, program, cwd, arguments.timeout):
            print(f"Stopped after failed test case {case['id']}.")
            return 1

    print(f"All {len(cases)} UI test cases passed.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
