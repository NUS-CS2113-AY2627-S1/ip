"""Run console UI tests described in test/ui-test-plan.md."""

from __future__ import annotations

import argparse
import re
import subprocess
import sys
from dataclasses import dataclass
from datetime import datetime
from pathlib import Path


CASE_PATTERN = re.compile(
    r"^## Test case: (?P<name>[^\n]+)\n(?P<body>.*?)(?=^## Test case: |^## Latest test session|\Z)",
    re.MULTILINE | re.DOTALL,
)
SESSION_HEADING = "## Latest test session"


@dataclass
class TestCase:
    """Represents one console UI test from the Markdown plan."""

    name: str
    aim: str
    inputs: str
    expected_output: str


def parse_arguments() -> argparse.Namespace:
    """Returns command-line arguments for the UI test runner."""
    parser = argparse.ArgumentParser(description="Run console UI tests from a Markdown plan.")
    parser.add_argument(
        "--plan",
        type=Path,
        default=Path("test/ui-test-plan.md"),
        help="Path to the Markdown UI test plan.",
    )
    return parser.parse_args()


def extract_fenced_section(text: str, heading: str) -> str:
    """Returns the content of the code block following a Markdown heading."""
    pattern = re.compile(
        rf"^### {re.escape(heading)}\n(?:\n)*```(?:text)?\n(?P<content>.*?)^```\n?",
        re.MULTILINE | re.DOTALL,
    )
    match = pattern.search(text)
    if match is None:
        raise ValueError(f"Missing '{heading}' code block.")
    return match.group("content")


def extract_aim(text: str) -> str:
    """Returns the prose in a test case's Aim section."""
    match = re.search(r"^### Aim\n(?P<content>.*?)(?=^### |\Z)", text, re.MULTILINE | re.DOTALL)
    if match is None:
        raise ValueError("Missing Aim section.")
    return match.group("content").strip()


def parse_plan(plan_text: str) -> tuple[str, str, list[TestCase]]:
    """Returns the build command, program command, and test cases in a plan."""
    setup_text = plan_text.split("## Test case:", maxsplit=1)[0]
    build_command = extract_fenced_section(setup_text, "Build command").strip()
    program_command = extract_fenced_section(setup_text, "Program command").strip()
    test_cases = []

    for match in CASE_PATTERN.finditer(plan_text):
        body = match.group("body")
        test_cases.append(
            TestCase(
                name=match.group("name").strip(),
                aim=extract_aim(body),
                inputs=extract_fenced_section(body, "Inputs"),
                expected_output=extract_fenced_section(body, "Expected output"),
            )
        )

    if not test_cases:
        raise ValueError("The plan does not contain any test cases.")
    return build_command, program_command, test_cases


def normalize_output(output: str) -> str:
    """Normalizes line endings before an exact console-output comparison."""
    return output.replace("\r\n", "\n").replace("\r", "\n")


def run_command(command: str, inputs: str = "") -> subprocess.CompletedProcess[str]:
    """Runs a shell command and captures its combined console output."""
    return subprocess.run(
        command,
        input=inputs,
        text=True,
        stdout=subprocess.PIPE,
        stderr=subprocess.STDOUT,
        shell=True,
        check=False,
    )


def format_code_block(content: str) -> str:
    """Formats console text as a Markdown code block without losing final output."""
    if content and not content.endswith("\n"):
        content += "\n"
    return f"```text\n{content}```"


def format_case_record(
    test_case: TestCase,
    actual_output: str,
    result: str,
    expected_output: str | None = None,
) -> str:
    """Returns a Markdown record for one executed test case."""
    record = [
        f"### {test_case.name}",
        "",
        f"**Result:** {result}",
        "",
        f"**Aim:** {test_case.aim}",
        "",
        "#### Console input",
        format_code_block(test_case.inputs),
        "",
        "#### Console output",
        format_code_block(actual_output),
    ]
    if expected_output is not None:
        record.extend(
            [
                "",
                "#### Expected output",
                format_code_block(expected_output),
            ]
        )
    return "\n".join(record)


def write_session_record(plan_path: Path, plan_text: str, record: str) -> None:
    """Replaces the plan's latest-session section with the supplied transcript."""
    heading_match = re.search(r"^## Latest test session\n?", plan_text, re.MULTILINE)
    if heading_match is None:
        updated_plan = plan_text.rstrip() + f"\n\n{SESSION_HEADING}\n\n{record.rstrip()}\n"
    else:
        updated_plan = plan_text[:heading_match.end()].rstrip() + f"\n\n{record.rstrip()}\n"
    plan_path.write_text(updated_plan, encoding="utf-8")


def fail_test(
    plan_path: Path,
    plan_text: str,
    records: list[str],
    test_case: TestCase,
    actual_output: str,
) -> int:
    """Records and reports a failed test, then returns a failing exit status."""
    records.append(
        format_case_record(
            test_case,
            actual_output,
            "FAIL",
            test_case.expected_output,
        )
    )
    session_record = "\n\n".join(records)
    write_session_record(plan_path, plan_text, session_record)

    print(f"FAIL: {test_case.name}")
    print(session_record)
    print("Expected output:")
    print(test_case.expected_output, end="" if test_case.expected_output.endswith("\n") else "\n")
    print("Actual output:")
    print(actual_output, end="" if actual_output.endswith("\n") else "\n")
    return 1


def main() -> int:
    """Runs every planned test until one fails or all tests pass."""
    arguments = parse_arguments()
    plan_path = arguments.plan.resolve()
    plan_text = plan_path.read_text(encoding="utf-8")

    try:
        build_command, program_command, test_cases = parse_plan(plan_text)
    except ValueError as error:
        print(f"Invalid UI test plan: {error}", file=sys.stderr)
        return 2

    build_result = run_command(build_command)
    build_output = normalize_output(build_result.stdout)
    if build_result.returncode != 0:
        record = "\n".join(
            [
                f"Run started: {datetime.now().astimezone().isoformat(timespec='seconds')}",
                "",
                "### Build command",
                "",
                "**Result:** FAIL",
                "",
                "#### Console output",
                format_code_block(build_output),
            ]
        )
        write_session_record(plan_path, plan_text, record)
        print("FAIL: Build command")
        print(record)
        print(build_output, end="" if build_output.endswith("\n") else "\n")
        return 1

    records = [f"Run started: {datetime.now().astimezone().isoformat(timespec='seconds')}"]
    for test_case in test_cases:
        result = run_command(program_command, test_case.inputs)
        actual_output = normalize_output(result.stdout)
        expected_output = normalize_output(test_case.expected_output)

        if result.returncode != 0 or actual_output != expected_output:
            return fail_test(plan_path, plan_text, records, test_case, actual_output)

        records.append(format_case_record(test_case, actual_output, "PASS"))

    session_record = "\n\n".join(records)
    write_session_record(plan_path, plan_text, session_record)
    print(session_record)
    print(f"\nPASS: {len(test_cases)} UI test case(s)")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
