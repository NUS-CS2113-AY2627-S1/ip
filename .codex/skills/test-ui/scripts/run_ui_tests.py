#!/usr/bin/env python3
"""Run text UI tests recorded in a Markdown test plan."""

from __future__ import annotations

import argparse
import re
import subprocess
import sys
from dataclasses import dataclass
from pathlib import Path


TEST_CASE_PATTERN = re.compile(
    r"^## Test Case:\s*(?P<name>.+?)\s*$"
    r"(?P<body>.*?)(?=^## Test Case:|\Z)",
    re.MULTILINE | re.DOTALL,
)
FENCED_BLOCK_PATTERN = re.compile(
    r"^### (?P<label>Input|Expected Output)\s*"
    r"```(?:text)?\s*\n(?P<content>.*?)\n```\s*",
    re.MULTILINE | re.DOTALL,
)
ASCII_BANNER_LINES = {
    "2222222      3333333",
    "22            33",
    "2222222       333333",
    "22                 33",
}


@dataclass(frozen=True)
class UiTestCase:
    """A single text UI test case."""

    name: str
    aim: str
    input_text: str
    expected_output: str


def main() -> int:
    args = parse_args()
    test_cases = load_test_cases(args.plan)
    if not test_cases:
        print(f"No test cases found in {args.plan}.", file=sys.stderr)
        return 1

    for test_case in test_cases:
        actual_output = run_program(args.program_command, test_case.input_text)
        comparable_actual_output = remove_banner_art(actual_output)
        comparable_expected_output = remove_banner_art(test_case.expected_output)
        print_session_record(test_case, comparable_actual_output)
        if normalize(comparable_actual_output) != normalize(comparable_expected_output):
            print_failure(test_case, comparable_expected_output, comparable_actual_output)
            return 1

    print(f"All {len(test_cases)} UI test case(s) passed.")
    return 0


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Run text UI tests from a Markdown test plan.")
    parser.add_argument("--plan", type=Path, default=Path("test/ui-test-plan.md"))
    parser.add_argument("program_command", nargs=argparse.REMAINDER)
    args = parser.parse_args()

    if args.program_command and args.program_command[0] == "--":
        args.program_command = args.program_command[1:]

    if not args.program_command:
        parser.error("provide the program command after --")

    return args


def load_test_cases(plan_path: Path) -> list[UiTestCase]:
    plan_text = plan_path.read_text(encoding="utf-8")
    test_cases = []
    for match in TEST_CASE_PATTERN.finditer(plan_text):
        name = match.group("name").strip()
        body = match.group("body")
        aim = parse_aim(body)
        blocks = parse_blocks(body)
        missing_sections = [label for label in ("Input", "Expected Output") if label not in blocks]
        if missing_sections:
            raise ValueError(f"{name} is missing: {', '.join(missing_sections)}")
        test_cases.append(UiTestCase(name, aim, blocks["Input"], blocks["Expected Output"]))
    return test_cases


def parse_aim(body: str) -> str:
    aim_match = re.search(r"^Aim:\s*(?P<aim>.+?)\s*$", body, re.MULTILINE)
    return aim_match.group("aim").strip() if aim_match else ""


def parse_blocks(body: str) -> dict[str, str]:
    blocks = {}
    for match in FENCED_BLOCK_PATTERN.finditer(body):
        blocks[match.group("label")] = match.group("content")
    return blocks


def run_program(program_command: list[str], input_text: str) -> str:
    completed_process = subprocess.run(
        program_command,
        input=ensure_trailing_newline(input_text),
        capture_output=True,
        text=True,
        check=False,
    )
    if completed_process.stderr:
        return completed_process.stdout + completed_process.stderr
    return completed_process.stdout


def ensure_trailing_newline(text: str) -> str:
    return text if text.endswith("\n") else text + "\n"


def normalize(text: str) -> str:
    return text.replace("\r\n", "\n").rstrip()


def remove_banner_art(text: str) -> str:
    return "\n".join(line for line in text.splitlines() if not is_banner_art_line(line))


def is_banner_art_line(line: str) -> bool:
    stripped_line = line.strip()
    if not stripped_line:
        return False
    if stripped_line in ASCII_BANNER_LINES:
        return True
    if set(stripped_line) == {"?"}:
        return True
    if all(character in {"@", " "} for character in stripped_line):
        return True
    return all(not character.isascii() for character in stripped_line)


def print_session_record(test_case: UiTestCase, actual_output: str) -> None:
    print(f"=== Test Case: {test_case.name} ===")
    if test_case.aim:
        print(f"Aim: {test_case.aim}")
    print("--- Console Input ---")
    print(test_case.input_text)
    print("--- Console Output ---")
    print(actual_output, end="" if actual_output.endswith("\n") else "\n")


def print_failure(test_case: UiTestCase, expected_output: str, actual_output: str) -> None:
    print(f"FAILED: {test_case.name}")
    print("--- Expected Output ---")
    print(expected_output)
    print("--- Actual Output ---")
    print(actual_output, end="" if actual_output.endswith("\n") else "\n")


if __name__ == "__main__":
    raise SystemExit(main())
