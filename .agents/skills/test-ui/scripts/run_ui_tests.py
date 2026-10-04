"""Runs fail-fast command-line UI tests recorded in the Markdown test plan."""

from __future__ import annotations

import argparse
from dataclasses import dataclass
from pathlib import Path
import re
import shutil
import subprocess
import sys
import tempfile


MAIN_CLASS = "luke.Luke"
JAVA_MAJOR_VERSION = 25
DEFAULT_TIMEOUT_SECONDS = 10
TEST_HEADING_PATTERN = re.compile(
    r"^###\s+(?P<identifier>[A-Za-z]+-\d+):\s+(?P<title>.+?)\s*$"
)


class PlanError(ValueError):
    """Indicates that the UI test plan does not follow the required format."""


@dataclass(frozen=True)
class TestCase:
    """Stores one independent console test session."""

    identifier: str
    title: str
    aim: str
    inputs: str
    expected_output: str


def parse_arguments() -> argparse.Namespace:
    """Parses command-line options for the UI test runner."""
    project_root = Path(__file__).resolve().parents[4]
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument(
        "--project-root",
        type=Path,
        default=project_root,
        help="project root containing src/main/java (default: detected root)",
    )
    parser.add_argument(
        "--plan",
        type=Path,
        default=Path("test/ui-test-plan.md"),
        help="test plan path, relative to the project root by default",
    )
    parser.add_argument(
        "--timeout",
        type=float,
        default=DEFAULT_TIMEOUT_SECONDS,
        help="maximum seconds allowed for each test case",
    )
    return parser.parse_args()


def extract_fenced_block(lines: list[str], label: str, case_id: str) -> str:
    """Extracts a fenced text block following a named field."""
    marker = f"**{label}:**"
    try:
        marker_index = next(
            index for index, line in enumerate(lines) if line.strip() == marker
        )
    except StopIteration as error:
        raise PlanError(f"{case_id} is missing {marker}") from error

    fence_index = marker_index + 1
    while fence_index < len(lines) and not lines[fence_index].strip():
        fence_index += 1

    if fence_index >= len(lines) or not lines[fence_index].strip().startswith("```"):
        raise PlanError(f"{case_id} must place a fenced block after {marker}")

    closing_index = fence_index + 1
    while closing_index < len(lines) and lines[closing_index].strip() != "```":
        closing_index += 1

    if closing_index >= len(lines):
        raise PlanError(f"{case_id} has an unclosed {label} block")

    content_lines = lines[fence_index + 1 : closing_index]
    if not content_lines:
        raise PlanError(f"{case_id} has an empty {label} block")

    return "\n".join(content_lines) + "\n"


def parse_test_plan(plan_path: Path) -> list[TestCase]:
    """Parses all test cases from the Markdown test plan."""
    if not plan_path.is_file():
        raise PlanError(f"Test plan does not exist: {plan_path}")

    lines = plan_path.read_text(encoding="utf-8").splitlines()
    headings: list[tuple[int, re.Match[str]]] = []
    for index, line in enumerate(lines):
        match = TEST_HEADING_PATTERN.match(line)
        if match:
            headings.append((index, match))

    if not headings:
        raise PlanError(f"No UI test cases found in {plan_path}")

    test_cases: list[TestCase] = []
    identifiers: set[str] = set()
    for heading_number, (start_index, match) in enumerate(headings):
        end_index = (
            headings[heading_number + 1][0]
            if heading_number + 1 < len(headings)
            else len(lines)
        )
        block = lines[start_index + 1 : end_index]
        identifier = match.group("identifier")
        title = match.group("title")

        if identifier in identifiers:
            raise PlanError(f"Duplicate test case ID: {identifier}")
        identifiers.add(identifier)

        aim_line = next(
            (line for line in block if line.strip().startswith("**Aim:**")),
            None,
        )
        if aim_line is None:
            raise PlanError(f"{identifier} is missing **Aim:**")
        aim = aim_line.strip()[len("**Aim:**") :].strip()
        if not aim:
            raise PlanError(f"{identifier} has an empty aim")

        inputs = extract_fenced_block(block, "Inputs", identifier)
        expected_output = extract_fenced_block(
            block, "Expected output", identifier
        )
        commands = inputs.removesuffix("\n").split("\n")
        if commands[-1] != "bye":
            raise PlanError(f"{identifier} must end its input list with bye")

        test_cases.append(
            TestCase(identifier, title, aim, inputs, expected_output)
        )

    return test_cases


def find_java_25_executable(name: str) -> str:
    """Returns a Java executable after confirming that its major version is 25."""
    executable = shutil.which(name)
    if executable is None:
        raise RuntimeError(f"Required executable is not available: {name}")

    completed = subprocess.run(
        [executable, "-version"],
        capture_output=True,
        text=True,
        check=False,
    )
    version_text = completed.stdout + completed.stderr
    match = re.search(r"(?:version\s+\"?|javac\s+)(\d+)", version_text)
    if completed.returncode != 0 or match is None:
        raise RuntimeError(f"Could not determine {name} version:\n{version_text}")
    if int(match.group(1)) != JAVA_MAJOR_VERSION:
        raise RuntimeError(
            f"{name} must use Java {JAVA_MAJOR_VERSION}, but reported:\n"
            f"{version_text.strip()}"
        )

    return executable


def compile_application(project_root: Path, classes_dir: Path, javac: str) -> None:
    """Compiles all production Java sources into a temporary directory."""
    source_root = project_root / "src" / "main" / "java"
    source_files = sorted(source_root.rglob("*.java"))
    if not source_files:
        raise RuntimeError(f"No Java source files found below {source_root}")

    completed = subprocess.run(
        [
            javac,
            "--release",
            str(JAVA_MAJOR_VERSION),
            "-Xlint:all",
            "-d",
            str(classes_dir),
            *(str(source_file) for source_file in source_files),
        ],
        cwd=project_root,
        capture_output=True,
        text=True,
        check=False,
        timeout=60,
    )
    if completed.returncode != 0:
        raise RuntimeError(
            "Compilation failed.\n"
            f"stdout:\n{completed.stdout}"
            f"stderr:\n{completed.stderr}"
        )


def normalize_line_endings(text: str) -> str:
    """Normalizes platform line endings without changing other output."""
    return text.replace("\r\n", "\n").replace("\r", "\n")


def decode_captured_output(output: str | bytes | None) -> str:
    """Converts captured subprocess output to normalized text."""
    if isinstance(output, bytes):
        output = output.decode("utf-8", errors="replace")
    return normalize_line_endings(output or "")


def print_text(text: str) -> None:
    """Prints captured text while preserving its final-newline state."""
    if not text:
        print("<no output>")
        return
    print(text, end="" if text.endswith("\n") else "\n")


def print_input(inputs: str) -> None:
    """Prints commands as an explicit console-input transcript."""
    for command in inputs.removesuffix("\n").split("\n"):
        print(f"> {command}")


def report_pass(test_case: TestCase, actual_output: str) -> None:
    """Prints the transcript for a passing test case."""
    print(f"=== {test_case.identifier}: {test_case.title} [PASS] ===")
    print(f"Aim: {test_case.aim}")
    print("Console input:")
    print_input(test_case.inputs)
    print("Console output:")
    print_text(actual_output)


def report_failure(
    test_case: TestCase,
    actual_output: str,
    expected_output: str,
    stderr: str,
    reason: str,
) -> None:
    """Prints the transcript and comparison for a failed test case."""
    print(f"=== {test_case.identifier}: {test_case.title} [FAIL] ===")
    print(f"Aim: {test_case.aim}")
    print(f"Reason: {reason}")
    print("Console input:")
    print_input(test_case.inputs)
    print("Actual output:")
    print_text(actual_output)
    print("Expected output:")
    print_text(expected_output)
    if stderr:
        print("Standard error:")
        print_text(stderr)
    print("Test session terminated; remaining cases were not run.")


def run_test_case(
    test_case: TestCase,
    project_root: Path,
    classes_dir: Path,
    java: str,
    timeout_seconds: float,
) -> bool:
    """Runs one test case and returns whether it passed."""
    with tempfile.TemporaryDirectory(prefix="luke-ui-case-") as case_dir:
        try:
            completed = subprocess.run(
                [java, "-cp", str(classes_dir), MAIN_CLASS],
                cwd=case_dir,
                input=test_case.inputs,
                capture_output=True,
                text=True,
                check=False,
                timeout=timeout_seconds,
            )
        except subprocess.TimeoutExpired as error:
            actual_output = decode_captured_output(error.stdout)
            stderr = decode_captured_output(error.stderr)
            report_failure(
                test_case,
                actual_output,
                test_case.expected_output,
                stderr,
                f"program exceeded the {timeout_seconds:g}-second timeout",
            )
            return False

    actual_output = normalize_line_endings(completed.stdout)
    stderr = normalize_line_endings(completed.stderr)
    expected_output = normalize_line_endings(test_case.expected_output)
    passed = (
        completed.returncode == 0
        and not stderr
        and actual_output == expected_output
    )
    if passed:
        report_pass(test_case, actual_output)
        return True

    reasons: list[str] = []
    if completed.returncode != 0:
        reasons.append(f"program exited with status {completed.returncode}")
    if stderr:
        reasons.append("program wrote to stderr")
    if actual_output != expected_output:
        reasons.append("actual stdout differs from expected output")
    report_failure(
        test_case,
        actual_output,
        expected_output,
        stderr,
        "; ".join(reasons),
    )
    return False


def main() -> int:
    """Runs the test plan in order and stops after the first failure."""
    arguments = parse_arguments()
    project_root = arguments.project_root.resolve()
    plan_path = arguments.plan
    if not plan_path.is_absolute():
        plan_path = project_root / plan_path

    try:
        test_cases = parse_test_plan(plan_path)
        java = find_java_25_executable("java")
        javac = find_java_25_executable("javac")
        with tempfile.TemporaryDirectory(prefix="luke-ui-tests-") as temp_dir:
            classes_dir = Path(temp_dir)
            compile_application(project_root, classes_dir, javac)
            for test_case in test_cases:
                if not run_test_case(
                    test_case,
                    project_root,
                    classes_dir,
                    java,
                    arguments.timeout,
                ):
                    return 1
    except (OSError, PlanError, RuntimeError, subprocess.TimeoutExpired) as error:
        print(f"UI test setup failed: {error}", file=sys.stderr)
        return 2

    print(f"Passed {len(test_cases)}/{len(test_cases)} UI test cases.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
