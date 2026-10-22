#!/usr/bin/env python3
"""Run the ordered console UI sessions described in an Aigis test plan."""

from __future__ import annotations

import argparse
import re
import shutil
import subprocess
import sys
import tempfile
from dataclasses import dataclass
from pathlib import Path


CASE_PATTERN = re.compile(
    r"^##\s+Test case\s+\d+:\s*(?P<name>.+?)\s*$"
    r"(?P<body>.*?)(?=^##\s+Test case\s+\d+:|\Z)",
    re.MULTILINE | re.DOTALL,
)


@dataclass(frozen=True)
class TestCase:
    """A single independent Aigis console session."""

    name: str
    aim: str
    inputs: str
    expected_output: str
    initial_data: str | None


def normalize_newlines(value: str) -> str:
    """Normalize platform line endings without changing any other whitespace."""
    return value.replace("\r\n", "\n").replace("\r", "\n")


def extract_block(body: str, label: str, case_name: str) -> str:
    """Extract a labelled Markdown text block or raise a useful plan error."""
    pattern = re.compile(
        rf"^{re.escape(label)}:\s*$\n```(?:text)?\s*\n(?P<content>.*?)^```\s*$",
        re.MULTILINE | re.DOTALL,
    )
    match = pattern.search(body)
    if match is None:
        raise ValueError(f"{case_name}: missing '{label}' fenced text block")
    return match.group("content")


def extract_optional_block(body: str, label: str) -> str | None:
    """Extract an optional labelled Markdown text block."""
    pattern = re.compile(
        rf"^{re.escape(label)}:\s*$\n```(?:text)?\s*\n(?P<content>.*?)^```\s*$",
        re.MULTILINE | re.DOTALL,
    )
    match = pattern.search(body)
    return match.group("content") if match is not None else None


def parse_plan(plan_path: Path) -> tuple[str, list[TestCase]]:
    """Read the main class and ordered test cases from a Markdown plan."""
    contents = normalize_newlines(plan_path.read_text(encoding="utf-8"))
    main_class_match = re.search(r"^- Main class:\s*`([^`]+)`\s*$", contents, re.MULTILINE)
    main_class = main_class_match.group(1) if main_class_match else "aigis.Aigis"

    cases = []
    for match in CASE_PATTERN.finditer(contents):
        name = match.group("name")
        body = match.group("body")
        aim_match = re.search(r"^Aim:\s*(.+?)\s*$", body, re.MULTILINE)
        if aim_match is None:
            raise ValueError(f"{name}: missing Aim line")
        cases.append(
            TestCase(
                name=name,
                aim=aim_match.group(1),
                inputs=extract_block(body, "Inputs", name),
                expected_output=extract_block(body, "Expected output", name),
                initial_data=extract_optional_block(body, "Initial data"),
            )
        )

    if not cases:
        raise ValueError("the plan does not contain any '## Test case N:' sections")
    return main_class, cases


def java_major_version(executable: str) -> str:
    """Return the major version reported by a Java executable."""
    result = subprocess.run(
        [executable, "-version"], capture_output=True, text=True, encoding="utf-8"
    )
    version_text = result.stdout + result.stderr
    match = re.search(r'version\s+"(\d+)', version_text)
    if match is None:
        match = re.search(r"(?:javac\s+)?(\d+)", version_text)
    return match.group(1) if match else "unknown"


def require_java_25() -> tuple[str, str]:
    """Locate Java tools and require the project-mandated major version."""
    java = shutil.which("java")
    javac = shutil.which("javac")
    if java is None or javac is None:
        raise RuntimeError("both 'java' and 'javac' must be available on PATH")
    versions = java_major_version(java), java_major_version(javac)
    if versions != ("25", "25"):
        raise RuntimeError(
            f"Java 25 is required; found java {versions[0]} and javac {versions[1]}"
        )
    return java, javac


def compile_sources(project_root: Path, javac: str, classes_dir: Path) -> None:
    """Compile every project Java source into the supplied temporary directory."""
    sources = sorted((project_root / "src" / "main" / "java").rglob("*.java"))
    if not sources:
        raise RuntimeError("no Java sources found under src/main/java")
    result = subprocess.run(
        [javac, "-d", str(classes_dir), *(str(source) for source in sources)],
        cwd=project_root,
        capture_output=True,
        text=True,
        encoding="utf-8",
    )
    if result.returncode != 0:
        raise RuntimeError("Java compilation failed:\n" + (result.stderr or result.stdout))


def print_transcript(test_case: TestCase, actual: str) -> None:
    """Print a readable record of one test session."""
    print(f"\n=== {test_case.name} ===")
    print(f"Aim: {test_case.aim}")
    print("--- Console input ---")
    print(test_case.inputs, end="" if test_case.inputs.endswith("\n") else "\n")
    print("--- Console output ---")
    print(actual, end="" if actual.endswith("\n") else "\n")


def run_case(
    java: str,
    classes_dir: Path,
    session_dir: Path,
    main_class: str,
    test_case: TestCase,
) -> int:
    """Run one case, print its transcript, and return its process exit code."""
    expected = normalize_newlines(test_case.expected_output)
    if test_case.initial_data is not None:
        data_file = session_dir / "data" / "aigis.txt"
        data_file.parent.mkdir()
        data_file.write_text(test_case.initial_data, encoding="utf-8")
    try:
        result = subprocess.run(
            [java, "-cp", str(classes_dir), main_class],
            cwd=session_dir,
            input=test_case.inputs,
            capture_output=True,
            text=True,
            encoding="utf-8",
            timeout=30,
        )
        actual = normalize_newlines(result.stdout)
    except subprocess.TimeoutExpired as error:
        actual = normalize_newlines(error.stdout or "")
        print_transcript(test_case, actual)
        print("FAIL: process exceeded the 30-second timeout.")
        print("--- Expected output ---")
        print(expected, end="" if expected.endswith("\n") else "\n")
        print("--- Actual output ---")
        print(actual, end="" if actual.endswith("\n") else "\n")
        return 1

    print_transcript(test_case, actual)
    if result.returncode != 0:
        print(f"FAIL: process exited with status {result.returncode}.")
        print("--- Expected output ---")
        print(expected, end="" if expected.endswith("\n") else "\n")
        return 1
    if actual != expected:
        print("FAIL: actual output differs from expected output.")
        print("--- Expected output ---")
        print(expected, end="" if expected.endswith("\n") else "\n")
        print("--- Actual output ---")
        print(actual, end="" if actual.endswith("\n") else "\n")
        return 1
    print("PASS")
    return 0


def main() -> int:
    """Parse arguments, compile Aigis, and stop at the first failed case."""
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--plan", type=Path, default=Path("test/ui-test-plan.md"))
    args = parser.parse_args()
    project_root = Path.cwd()
    plan_path = args.plan if args.plan.is_absolute() else project_root / args.plan

    try:
        main_class, cases = parse_plan(plan_path)
        java, javac = require_java_25()
        with tempfile.TemporaryDirectory(prefix="aigis-ui-test-") as temp_dir:
            classes_dir = Path(temp_dir)
            compile_sources(project_root, javac, classes_dir)
            for case_number, test_case in enumerate(cases, start=1):
                session_dir = classes_dir / f"session-{case_number}"
                session_dir.mkdir()
                if run_case(java, classes_dir, session_dir, main_class, test_case) != 0:
                    print("\nTest session stopped after the first failure.")
                    return 1
    except (OSError, RuntimeError, ValueError, subprocess.TimeoutExpired) as error:
        print(f"ERROR: {error}", file=sys.stderr)
        return 2

    print(f"\nAll {len(cases)} UI test cases passed.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
