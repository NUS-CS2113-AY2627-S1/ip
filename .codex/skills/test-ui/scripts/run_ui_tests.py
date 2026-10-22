#!/usr/bin/env python3
"""Run fail-fast console UI tests described in a Markdown test plan."""

import argparse
import re
import shlex
import subprocess
import sys
from dataclasses import dataclass
from pathlib import Path


@dataclass(frozen=True)
class TestCase:
    """A console UI test case parsed from the test plan."""

    name: str
    aim: str
    inputs: str
    expected_output: str
    initial_data: str | None


def extract_block(section: str, label: str) -> str:
    """Return the fenced text block following a specified heading."""
    pattern = rf"### {re.escape(label)}\s*\n```(?:text)?\n(.*?)```"
    match = re.search(pattern, section, flags=re.DOTALL)
    if not match:
        raise ValueError(f"missing '{label}' fenced text block")
    return match.group(1)


def parse_plan(plan_path: Path) -> tuple[list[str], Path | None, list[TestCase]]:
    """Parse the program command and test cases from a Markdown plan."""
    content = plan_path.read_text(encoding="utf-8")
    command_match = re.search(r"^Program command: `(.+)`$", content, re.MULTILINE)
    if not command_match:
        raise ValueError("missing program command")

    data_file_match = re.search(r"^Data file: `(.+)`$", content, re.MULTILINE)
    data_file = Path(data_file_match.group(1)) if data_file_match else None

    sections = re.split(r"^## Test case: ", content, flags=re.MULTILINE)[1:]
    if not sections:
        raise ValueError("no test case sections found")

    test_cases = []
    for section in sections:
        name, separator, body = section.partition("\n")
        if not separator or not name.strip():
            raise ValueError("test case has no name")
        aim_match = re.search(r"^Aim: (.+)$", body, re.MULTILINE)
        if not aim_match:
            raise ValueError(f"test case '{name}' has no aim")
        test_cases.append(TestCase(
            name=name.strip(),
            aim=aim_match.group(1).strip(),
            inputs=extract_block(body, "Inputs"),
            expected_output=extract_block(body, "Expected output"),
            initial_data=(
                extract_block(body, "Initial data")
                if "### Initial data" in body
                else None
            ),
        ))

    return shlex.split(command_match.group(1)), data_file, test_cases


def normalize_output(output: str) -> str:
    """Normalize platform line endings while preserving all other text."""
    return output.replace("\r\n", "\n").replace("\r", "\n")


def show_block(label: str, value: str) -> None:
    """Print a clearly delimited transcript block."""
    print(f"--- {label} ---")
    print(value, end="" if value.endswith("\n") else "\n")
    print(f"--- end {label} ---")


def prepare_data_file(data_file: Path | None, initial_data: str | None) -> None:
    """Prepare a test's data fixture or remove data left by an earlier test."""
    if data_file is None:
        return
    if initial_data is None:
        data_file.unlink(missing_ok=True)
        return

    data_file.parent.mkdir(parents=True, exist_ok=True)
    data_file.write_text(initial_data, encoding="utf-8")


def run_test(
        command: list[str],
        data_file: Path | None,
        test_case: TestCase,
) -> bool:
    """Run one test case and return whether its output matched exactly."""
    print(f"\nTEST: {test_case.name}")
    print(f"Aim: {test_case.aim}")
    show_block("console input", test_case.inputs)

    try:
        prepare_data_file(data_file, test_case.initial_data)
        result = subprocess.run(
            command,
            input=test_case.inputs,
            capture_output=True,
            text=True,
            check=False,
        )
    except OSError as error:
        print(f"FAILED: could not start program: {error}")
        return False

    actual = normalize_output(result.stdout)
    expected = normalize_output(test_case.expected_output)
    show_block("console output", actual)

    if result.returncode != 0:
        print(f"FAILED: program exited with status {result.returncode}")
        if result.stderr:
            show_block("standard error", normalize_output(result.stderr))
        show_block("expected output", expected)
        show_block("actual output", actual)
        return False

    if actual != expected:
        print("FAILED: actual output differs from expected output")
        show_block("expected output", expected)
        show_block("actual output", actual)
        return False

    print("PASSED")
    return True


def main() -> int:
    """Run test cases in order and stop immediately on the first failure."""
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("plan", type=Path, help="path to the Markdown test plan")
    args = parser.parse_args()

    try:
        command, data_file, test_cases = parse_plan(args.plan)
    except (OSError, ValueError) as error:
        print(f"Invalid test plan: {error}", file=sys.stderr)
        return 2

    print(f"Program command: {shlex.join(command)}")
    for test_case in test_cases:
        if not run_test(command, data_file, test_case):
            print("Test session terminated after first failure.")
            return 1

    print(f"\nAll {len(test_cases)} test case(s) passed.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
