#!/usr/bin/env python3
"""Run ordered console UI tests and stop at the first output mismatch."""

import argparse
import json
import subprocess
import sys
from pathlib import Path


def normalize(text: str) -> str:
    """Normalize platform line endings without changing other output."""
    return text.replace("\r\n", "\n").replace("\r", "\n")


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("test_file", type=Path, help="JSON file containing an ordered tests list")
    args = parser.parse_args()
    data = json.loads(args.test_file.read_text(encoding="utf-8"))
    tests = data.get("tests")
    if not isinstance(tests, list):
        raise ValueError("test file must contain a 'tests' list")

    for index, test in enumerate(tests, start=1):
        name = test["name"]
        command = test["command"]
        expected = test["expected_output"]
        result = subprocess.run(
            command,
            input=test.get("input", ""),
            text=True,
            capture_output=True,
            shell=True,
            check=False,
        )
        actual = result.stdout + result.stderr
        print(f"=== Test {index}: {name} ===")
        print(f"$ {command}")
        if test.get("input", ""):
            print("[stdin]")
            print(test["input"], end="" if test["input"].endswith("\n") else "\n")
        print("[output]")
        print(actual, end="" if actual.endswith("\n") else "\n")
        if normalize(actual) != normalize(expected):
            print("RESULT: FAILED")
            print("--- expected output ---")
            print(expected, end="" if expected.endswith("\n") else "\n")
            print("--- actual output ---")
            print(actual, end="" if actual.endswith("\n") else "\n")
            return 1
        print("RESULT: PASSED")
    print(f"ALL TESTS PASSED ({len(tests)})")
    return 0


if __name__ == "__main__":
    try:
        sys.exit(main())
    except (KeyError, json.JSONDecodeError, OSError, ValueError) as error:
        print(f"TEST SETUP ERROR: {error}", file=sys.stderr)
        sys.exit(2)
