#!/usr/bin/env python3
"""Run the UI test cases in test/ui-test-plan.md against the compiled program.

Usage:
    run_ui_tests.py [plan_file]

Compiles every .java file under src/main/java, then for each test case in
the plan feeds its Input block to GLaDOS on stdin and compares the full
console output to its Expected output block. Stops at the first failure.

Each test runs in its own fresh, empty working folder, so data files saved by
one test never leak into another (or into the repository). A test may also
give a "Data file before" block (written to data/glados.txt before the run)
and a "Data file after" block (compared against data/glados.txt after it).

"Data file before" is written as UTF-8. To test a file saved in another
encoding, label it "Data file before (Windows-1252)" or
"Data file before (UTF-8 with BOM)" instead.
"""

import re
import subprocess
import sys
import tempfile
from pathlib import Path

REPO_ROOT = Path(__file__).resolve().parents[4]
SRC_DIR = REPO_ROOT / "src" / "main" / "java"
BUILD_DIR = REPO_ROOT / "_temp" / "ui-test-classes"
MAIN_CLASS = "glados.GLaDOS"
DATA_FILE = Path("data") / "glados.txt"

TEST_HEADER_RE = re.compile(r"^## (.+)$", re.MULTILINE)
AIM_RE = re.compile(r"\*\*Aim:\*\*\s*(.+?)\n\n", re.DOTALL)
CODE_BLOCK_RE = re.compile(r"```text\n(.*?)```", re.DOTALL)
LABELED_BLOCK_RE = re.compile(r"\*\*([^*]+):\*\*\s*\n```text\n(.*?)```", re.DOTALL)
DATA_BEFORE_ENCODINGS = {
    "Data file before": "utf-8",
    "Data file before (Windows-1252)": "cp1252",  # e.g. saved by older Windows text editors
    "Data file before (UTF-8 with BOM)": "utf-8-sig",  # UTF-8 with a byte order mark first
}
KNOWN_LABELS = {"Input", "Expected output", "Data file after", *DATA_BEFORE_ENCODINGS}
NO_FILE = "(data file does not exist)"


def normalize(text):
    """Ignore Windows/Unix line-ending differences and a trailing blank line."""
    return text.replace("\r\n", "\n").rstrip("\n")


def parse_plan(plan_text):
    """Split the plan into test cases: (name, aim, blocks), blocks keyed by label."""
    sections = TEST_HEADER_RE.split(plan_text)[1:]  # drop text before first "## "
    tests = []
    for i in range(0, len(sections), 2):
        name = sections[i].strip()
        body = sections[i + 1]

        aim_match = AIM_RE.search(body)
        aim = " ".join(aim_match.group(1).split()) if aim_match else "(no aim given)"

        labeled = LABELED_BLOCK_RE.findall(body)
        blocks = dict(labeled)
        unknown = set(blocks) - KNOWN_LABELS
        data_before_count = len(set(blocks) & set(DATA_BEFORE_ENCODINGS))
        if (len(labeled) != len(blocks) or unknown or data_before_count > 1
                or len(labeled) != len(CODE_BLOCK_RE.findall(body))
                or "Input" not in blocks or "Expected output" not in blocks):
            print(f"error: test '{name}' needs exactly one **Input:** and one "
                  f"**Expected output:** ```text``` block, plus optional "
                  f"**Data file before:** (or one of its encoding variants) / "
                  f"**Data file after:** blocks",
                  file=sys.stderr)
            sys.exit(2)

        tests.append((name, aim, blocks))
    return tests


def compile_sources():
    BUILD_DIR.mkdir(parents=True, exist_ok=True)
    java_files = [str(p) for p in SRC_DIR.rglob("*.java")]
    result = subprocess.run(
        ["javac", "-d", str(BUILD_DIR), *java_files],
        capture_output=True, text=True,
    )
    if result.returncode != 0:
        print("Compilation failed:\n" + result.stderr, file=sys.stderr)
        sys.exit(1)


def get_data_before(blocks):
    """Return (contents, encoding) of the test's data-file-before block, or (None, None)."""
    for label, encoding in DATA_BEFORE_ENCODINGS.items():
        if label in blocks:
            return blocks[label], encoding
    return None, None


def run_program(input_text, data_before, encoding="utf-8"):
    """Run GLaDOS in a fresh folder; return (stdout, data file contents afterwards).

    data_before, if given, is written to the data file in the given encoding.
    """
    with tempfile.TemporaryDirectory() as work_dir:
        data_path = Path(work_dir) / DATA_FILE
        if data_before is not None:
            data_path.parent.mkdir(parents=True)
            data_path.write_text(data_before, encoding=encoding)

        result = subprocess.run(
            ["java", "-cp", str(BUILD_DIR), MAIN_CLASS],
            input=input_text, capture_output=True, text=True, cwd=work_dir,
        )

        if data_path.is_file():
            data_after = data_path.read_text(encoding="utf-8")
        else:
            data_after = NO_FILE
    return result.stdout, data_after


def fail(name, what, expected, actual):
    print(f"\nFAIL: {name} ({what} differs)")
    print(f"--- expected {what} ---")
    print(expected.rstrip("\n"))
    print(f"--- actual {what} ---")
    print(actual.rstrip("\n"))
    print(f"\nTest session terminated at first failure: {name}")
    return 1


def main():
    plan_path = Path(sys.argv[1]) if len(sys.argv) > 1 else REPO_ROOT / "test" / "ui-test-plan.md"
    if not plan_path.is_file():
        print(f"error: no such file: {plan_path}", file=sys.stderr)
        return 2

    tests = parse_plan(plan_path.read_text(encoding="utf-8"))
    if not tests:
        print(f"error: no test cases found in {plan_path}", file=sys.stderr)
        return 2

    print(f"Compiling {SRC_DIR} ...")
    compile_sources()

    for name, aim, blocks in tests:
        print(f"\n=== {name} ===")
        print(f"Aim: {aim}")
        data_before, encoding = get_data_before(blocks)
        if data_before is not None:
            print(f"--- data file before ({encoding}) ---")
            print(data_before.rstrip("\n"))
        print("--- console input ---")
        print(blocks["Input"].rstrip("\n"))

        actual_text, data_after = run_program(blocks["Input"], data_before, encoding)
        print("--- console output ---")
        print(actual_text.rstrip("\n"))

        if normalize(actual_text) != normalize(blocks["Expected output"]):
            return fail(name, "output", blocks["Expected output"], actual_text)

        expected_data = blocks.get("Data file after")
        if expected_data is not None:
            print("--- data file after ---")
            print(data_after.rstrip("\n"))
            if normalize(data_after) != normalize(expected_data):
                return fail(name, "data file", expected_data, data_after)

        print(f"PASS: {name}")

    print(f"\nAll {len(tests)} test case(s) passed.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
