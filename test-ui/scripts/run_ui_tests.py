#!/usr/bin/env python3
"""Run the Dandelion console UI test plan and stop at the first failure."""

from __future__ import annotations

import argparse
import re
import subprocess
import sys
import tempfile
from pathlib import Path


CASE_PATTERN = re.compile(r"^## Test case: (.+)$", re.MULTILINE)
BLOCK_PATTERN = re.compile(
    r"\*\*(Input|Restart input|Initial data|Expected output):\*\*\s*```(?:text)?\n(.*?)```",
    re.DOTALL,
)


def parse_test_plan(plan_path: Path) -> list[dict[str, str]]:
    """Parse test cases and their input/output blocks from a Markdown plan."""
    plan = plan_path.read_text(encoding="utf-8")
    headings = list(CASE_PATTERN.finditer(plan))
    cases = []

    for index, heading in enumerate(headings):
        section_end = headings[index + 1].start() if index + 1 < len(headings) else len(plan)
        section = plan[heading.end():section_end]
        blocks = {name: content.strip("\n") for name, content in BLOCK_PATTERN.findall(section)}
        if "Input" not in blocks or "Expected output" not in blocks:
            raise ValueError(
                f"Test case '{heading.group(1)}' must contain Input and Expected output blocks."
            )
        if "bye" not in blocks["Input"].splitlines():
            raise ValueError(f"Test case '{heading.group(1)}' must include bye as an input command.")
        if "Restart input" in blocks and "bye" not in blocks["Restart input"].splitlines():
            raise ValueError(f"Restart input for '{heading.group(1)}' must include bye as a command.")
        cases.append({"name": heading.group(1), **blocks})

    if not cases:
        raise ValueError(f"No test cases found in {plan_path}.")
    return cases


def compile_application(source_root: Path, output_dir: Path) -> None:
    """Compile all project Java sources into a temporary output directory."""
    sources = sorted(source_root.rglob("*.java"))
    if not sources:
        raise RuntimeError(f"No Java source files found under {source_root}.")
    result = subprocess.run(
        ["javac", "-d", str(output_dir), *(str(source) for source in sources)],
        capture_output=True,
        text=True,
    )
    if result.returncode != 0:
        raise RuntimeError("Compilation failed:\n" + result.stdout + result.stderr)


def prepare_case_data(case: dict[str, str], working_dir: Path) -> None:
    """Write optional initial saved data for one test case."""
    if "Initial data" not in case:
        return

    data_file = working_dir / "data" / "dandelion.txt"
    data_file.parent.mkdir(parents=True)
    data_file.write_text(case["Initial data"] + "\n", encoding="utf-8")


def run_session(test_input: str, classes_dir: Path, working_dir: Path) -> tuple[str, int]:
    """Run one chatbot session and return its output and exit status."""
    result = subprocess.run(
        ["java", "-cp", str(classes_dir), "dandelion.Dandelion"],
        input=test_input + "\n",
        capture_output=True,
        text=True,
        timeout=10,
        cwd=working_dir,
    )
    output = result.stdout + result.stderr
    return output, result.returncode


def run_case(
    case: dict[str, str],
    classes_dir: Path,
    working_dir: Path,
) -> tuple[str, str, int]:
    """Run one or two chatbot sessions and return their transcript and exit status."""
    prepare_case_data(case, working_dir)

    first_input = case["Input"]
    first_output, first_exit_status = run_session(first_input, classes_dir, working_dir)
    if first_exit_status != 0 or "Restart input" not in case:
        return first_input + "\n", first_output, first_exit_status

    restart_input = case["Restart input"]
    restart_output, restart_exit_status = run_session(restart_input, classes_dir, working_dir)
    combined_input = "Session 1:\n" + first_input + "\nSession 2:\n" + restart_input + "\n"
    combined_output = "Session 1:\n" + first_output + "\nSession 2:\n" + restart_output
    return combined_input, combined_output, restart_exit_status


def assert_expected_output(output: str, expected: str) -> None:
    """Check that expected output fragments occur in order in the actual output."""
    search_start = 0
    for fragment in expected.splitlines():
        if not fragment.strip():
            continue
        match_start = output.find(fragment, search_start)
        if match_start == -1:
            raise AssertionError(
                f"Expected output fragment not found after character {search_start}: {fragment!r}"
            )
        search_start = match_start + len(fragment)


def print_transcript(test_input: str, output: str) -> None:
    """Print a readable record of one console session."""
    print("Console input:")
    print(test_input, end="" if test_input.endswith("\n") else "\n")
    print("Console output:")
    print(output, end="" if output.endswith("\n") else "\n")


def main() -> int:
    """Compile the application and execute the test plan."""
    argument_parser = argparse.ArgumentParser(description=__doc__)
    argument_parser.add_argument(
        "--plan",
        type=Path,
        default=Path("test/ui-test-plan.md"),
        help="Markdown test plan to execute.",
    )
    argument_parser.add_argument(
        "--source-root",
        type=Path,
        default=Path("src/main/java"),
        help="Root directory containing Java sources.",
    )
    arguments = argument_parser.parse_args()

    try:
        cases = parse_test_plan(arguments.plan)
        with tempfile.TemporaryDirectory(prefix="dandelion-ui-") as temporary_dir:
            classes_dir = Path(temporary_dir)
            compile_application(arguments.source_root, classes_dir)

            for case_number, case in enumerate(cases, start=1):
                print(f"\n=== Test case {case_number}: {case['name']} ===")
                case_directory = classes_dir / f"case-{case_number}"
                case_directory.mkdir()
                test_input, output, exit_status = run_case(case, classes_dir, case_directory)
                print_transcript(test_input, output)
                if exit_status != 0:
                    print("TEST FAILED")
                    print(f"Actual output: program exited with status {exit_status}.")
                    print("Expected output fragments:")
                    print(case["Expected output"])
                    return 1
                try:
                    assert_expected_output(output, case["Expected output"])
                except AssertionError as error:
                    print("TEST FAILED")
                    print("Expected output fragments:")
                    print(case["Expected output"])
                    print(f"Actual-output detail: {error}")
                    return 1
                print("TEST PASSED")

        print(f"\nAll {len(cases)} UI test cases passed.")
        return 0
    except (AssertionError, OSError, RuntimeError, ValueError, subprocess.TimeoutExpired) as error:
        print(f"TEST SESSION FAILED: {error}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    sys.exit(main())
