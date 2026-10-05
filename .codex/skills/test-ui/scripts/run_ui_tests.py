"""Run the project's scripted command-line UI test plan."""

from __future__ import annotations

import argparse
import difflib
import re
import subprocess
import sys
import tempfile
from dataclasses import dataclass
from pathlib import Path


@dataclass(frozen=True)
class TestCase:
    """A named UI session with input and its complete expected output."""

    name: str
    aim: str
    inputs: str
    expected_output: str
    initial_data: str
    expected_saved_data: str
    data_directory_is_file: bool
    data_file_is_directory: bool


def parse_args() -> argparse.Namespace:
    """Parse runner options while keeping the normal project invocation short."""
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument(
        "--plan",
        type=Path,
        default=Path("test/ui-test-plan.md"),
        help="Markdown test plan (default: test/ui-test-plan.md)",
    )
    parser.add_argument(
        "--source-dir",
        type=Path,
        default=Path("src/main/java"),
        help="Java source directory to compile",
    )
    parser.add_argument(
        "--main-class",
        default="ernest.Ernest",
        help="Fully qualified Java main class (default: ernest.Ernest)",
    )
    parser.add_argument(
        "--timeout",
        type=float,
        default=30.0,
        help="Maximum seconds allowed for one test case (default: 30)",
    )
    return parser.parse_args()


def normalize_newlines(value: str) -> str:
    """Make output comparison independent of the host operating system."""
    return value.replace("\r\n", "\n").replace("\r", "\n")


def extract_block(body: str, label: str, case_name: str) -> str:
    """Extract the first labelled Markdown text fence from a test case."""
    pattern = rf"(?ms)^\s*{re.escape(label)}:\s*\n```[^\n]*\n(.*?)^```\s*$"
    match = re.search(pattern, body)
    if match is None:
        raise ValueError(f"{case_name}: missing {label} code block")
    return match.group(1)


def extract_optional_block(body: str, label: str) -> str:
    """Extract an optional labelled Markdown text fence from a test case."""
    pattern = rf"(?ms)^\s*{re.escape(label)}:\s*\n```[^\n]*\n(.*?)^```\s*$"
    match = re.search(pattern, body)
    return "" if match is None else match.group(1)


def parse_plan(plan_path: Path) -> list[TestCase]:
    """Parse the required test-case fields from the Markdown plan."""
    plan = plan_path.read_text(encoding="utf-8")
    matches = list(re.finditer(r"(?m)^## Test case:\s*(.+?)\s*$", plan))
    if not matches:
        raise ValueError(f"{plan_path}: no '## Test case:' sections found")

    cases = []
    for index, match in enumerate(matches):
        name = match.group(1).strip()
        end = matches[index + 1].start() if index + 1 < len(matches) else len(plan)
        body = plan[match.end():end]
        aim_match = re.search(r"(?m)^Aim:\s*(.+?)\s*$", body)
        if aim_match is None or not aim_match.group(1).strip():
            raise ValueError(f"{name}: missing Aim")
        inputs = extract_block(body, "Inputs", name)
        expected = normalize_newlines(extract_block(body, "Expected output", name))
        if not inputs.strip():
            raise ValueError(f"{name}: Inputs must contain at least one command")
        if not expected:
            raise ValueError(f"{name}: Expected output must not be empty")
        initial_data = normalize_newlines(extract_optional_block(body, "Initial data"))
        expected_saved_data = normalize_newlines(extract_optional_block(body, "Expected saved data"))
        data_directory_is_file = bool(re.search(r"(?m)^Data directory is a file:\s*$", body))
        data_file_is_directory = bool(re.search(r"(?m)^Data file is a directory:\s*$", body))
        cases.append(TestCase(name, aim_match.group(1).strip(), inputs, expected, initial_data,
                              expected_saved_data, data_directory_is_file, data_file_is_directory))
    return cases


def check_java_25() -> None:
    """Require both Java tools to report major version 25."""
    java_version = subprocess.run(
        ["java", "-version"], capture_output=True, text=True, check=False
    )
    javac_version = subprocess.run(
        ["javac", "-version"], capture_output=True, text=True, check=False
    )
    java_text = java_version.stdout + java_version.stderr
    javac_text = javac_version.stdout + javac_version.stderr
    if java_version.returncode != 0 or javac_version.returncode != 0:
        raise RuntimeError("Java 25 is required, but java/javac could not be run")
    if not re.search(r'\bversion "25(?:[.]|\b)', java_text):
        raise RuntimeError(f"Java 25 is required; java reports: {java_text.strip()}")
    if not re.search(r"\bjavac\s+25(?:[.]|\b)", javac_text):
        raise RuntimeError(f"Java 25 is required; javac reports: {javac_text.strip()}")


def compile_sources(source_dir: Path, output_dir: Path) -> None:
    """Compile every Java source under source_dir for an isolated test run."""
    sources = sorted(source_dir.rglob("*.java"))
    if not sources:
        raise RuntimeError(f"No Java sources found under {source_dir}")
    result = subprocess.run(
        ["javac", "--release", "25", "-d", str(output_dir), *map(str, sources)],
        capture_output=True,
        text=True,
        check=False,
    )
    if result.returncode != 0:
        details = normalize_newlines(result.stdout + result.stderr).strip()
        raise RuntimeError(f"Compilation failed:\n{details}")


def show_transcript(case: TestCase, actual: str) -> None:
    """Print the input and output needed to reproduce this test session."""
    print(f"\n=== {case.name} ===")
    print(f"Aim: {case.aim}")
    print("--- Console input ---")
    print(case.inputs, end="" if case.inputs.endswith("\n") else "\n")
    print("--- Console output ---")
    print(actual, end="" if actual.endswith("\n") else "\n")


def report_failure(case: TestCase, actual: str) -> None:
    """Report the exact expected/actual output and a compact unified diff."""
    print("FAIL")
    print("--- Expected output ---")
    print(case.expected_output, end="" if case.expected_output.endswith("\n") else "\n")
    print("--- Actual output ---")
    print(actual, end="" if actual.endswith("\n") else "\n")
    diff = difflib.unified_diff(
        case.expected_output.splitlines(keepends=True),
        actual.splitlines(keepends=True),
        fromfile="expected",
        tofile="actual",
    )
    print("--- Diff ---")
    sys.stdout.writelines(diff)


def report_saved_data_failure(case: TestCase, actual_saved_data: str) -> None:
    """Report a mismatch between the expected and actual saved data file."""
    print("FAIL")
    print("--- Expected saved data ---")
    print(case.expected_saved_data, end="" if case.expected_saved_data.endswith("\n") else "\n")
    print("--- Actual saved data ---")
    print(actual_saved_data, end="" if actual_saved_data.endswith("\n") else "\n")
    diff = difflib.unified_diff(
        case.expected_saved_data.splitlines(keepends=True),
        actual_saved_data.splitlines(keepends=True),
        fromfile="expected saved data",
        tofile="actual saved data",
    )
    print("--- Diff ---")
    sys.stdout.writelines(diff)


def run_case(case: TestCase, classes_dir: Path, main_class: str, timeout: float) -> bool:
    """Run one isolated case and stop its caller on the first mismatch."""
    input_text = case.inputs
    if not input_text.endswith("\n"):
        input_text += "\n"
    with tempfile.TemporaryDirectory(prefix="test-ui-case-") as temporary_dir:
        working_dir = Path(temporary_dir)
        data_file = working_dir / "data" / "ernest.txt"
        if case.data_directory_is_file:
            data_file.parent.write_text("", encoding="utf-8")
        elif case.data_file_is_directory:
            data_file.mkdir(parents=True)
        elif case.initial_data:
            data_file.parent.mkdir(parents=True)
            data_file.write_text(case.initial_data, encoding="utf-8")
        try:
            result = subprocess.run(
                ["java", "-cp", str(classes_dir), main_class],
                cwd=working_dir,
                input=input_text,
                capture_output=True,
                text=True,
                timeout=timeout,
                check=False,
            )
        except subprocess.TimeoutExpired as exception:
            actual = normalize_newlines((exception.stdout or "") + (exception.stderr or ""))
            show_transcript(case, actual)
            print(f"FAIL: process exceeded the {timeout:g}-second timeout")
            report_failure(case, actual)
            return False

        actual_saved_data = ""
        if case.expected_saved_data:
            try:
                actual_saved_data = normalize_newlines(data_file.read_text(encoding="utf-8"))
            except OSError:
                actual_saved_data = "<saved data file is missing>\n"

    actual = normalize_newlines(result.stdout + result.stderr)
    show_transcript(case, actual)
    if result.returncode != 0:
        print(f"FAIL: process exited with status {result.returncode}")
        report_failure(case, actual)
        return False
    if actual != case.expected_output:
        report_failure(case, actual)
        return False
    if case.expected_saved_data and actual_saved_data != case.expected_saved_data:
        report_saved_data_failure(case, actual_saved_data)
        return False
    print("PASS")
    return True


def main() -> int:
    """Run the plan in order and stop immediately after the first failure."""
    args = parse_args()
    try:
        check_java_25()
        cases = parse_plan(args.plan)
        with tempfile.TemporaryDirectory(prefix="test-ui-") as temporary_dir:
            classes_dir = Path(temporary_dir)
            compile_sources(args.source_dir, classes_dir)
            for case in cases:
                if not run_case(case, classes_dir, args.main_class, args.timeout):
                    print("\nTest session stopped after the first failure.")
                    return 1
    except (OSError, ValueError, RuntimeError) as exception:
        print(f"ERROR: {exception}", file=sys.stderr)
        return 2
    print(f"\nAll {len(cases)} UI test case(s) passed.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
