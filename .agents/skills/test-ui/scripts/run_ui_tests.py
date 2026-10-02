#!/usr/bin/env python3
"""
UI Test Runner for Foodielover (Duke chatbot).
Parses test cases from test/ui-test-plan.md, executes them against the compiled
Java application, compares actual output against expected output, and displays
interleaved console session records. Terminates immediately on first failure.
"""

import argparse
import os
import re
import subprocess
import sys

PROJECT_ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../../.."))
TEST_PLAN_PATH = os.path.join(PROJECT_ROOT, "test", "ui-test-plan.md")
BIN_DIR = os.path.join(PROJECT_ROOT, "bin")
SRC_DIR = os.path.join(PROJECT_ROOT, "src", "main", "java")
DATA_DIR = os.path.join(PROJECT_ROOT, "data")
DATA_FILE = os.path.join(DATA_DIR, "foodielover.txt")
MAIN_CLASS = "foodielover.Foodielover"


def compile_java():
    """Compiles all Java source files into the bin directory."""
    os.makedirs(BIN_DIR, exist_ok=True)
    java_files = []
    for root, _, files in os.walk(SRC_DIR):
        for f in files:
            if f.endswith(".java"):
                java_files.append(os.path.join(root, f))
    if not java_files:
        print(f"Error: No Java source files found in {SRC_DIR}", file=sys.stderr)
        sys.exit(1)

    compile_cmd = ["javac", "-d", BIN_DIR] + java_files
    result = subprocess.run(compile_cmd, capture_output=True, text=True)
    if result.returncode != 0:
        print("Compilation failed!", file=sys.stderr)
        print(result.stderr, file=sys.stderr)
        sys.exit(1)


def parse_test_plan(file_path):
    """
    Parses test cases from a markdown test plan file.
    Expected format for each test case:
    ### <ID>: <Title>
    - **Aim**: <Aim description>
    - **Inputs**:
    ```
    <input lines>
    ```
    - **Expected Output**:
    ```
    <expected output lines>
    ```
    """
    if not os.path.exists(file_path):
        print(f"Error: Test plan file not found at {file_path}", file=sys.stderr)
        sys.exit(1)

    with open(file_path, "r", encoding="utf-8") as f:
        content = f.read()

    test_cases = []
    # Split content by markdown level 3 headers
    sections = content.split("\n### ")
    for section in sections[1:]:
        header_line = section.split("\n", 1)[0].strip()
        title = header_line

        aim_match = re.search(r"-\s*\*\*Aim\*\*:\s*([^\n]+)", section)
        aim = aim_match.group(1).strip() if aim_match else ""

        initial_match = re.search(r"-\s*\*\*Initial Data File\*\*:\s*\n```[^\n]*\n(.*?)\n```", section, re.DOTALL)
        initial_data = initial_match.group(1) if initial_match else None

        input_match = re.search(r"-\s*\*\*Inputs?\*\*:\s*\n```[^\n]*\n(.*?)\n```", section, re.DOTALL)
        if not input_match:
            continue
        raw_inputs = input_match.group(1)

        output_match = re.search(r"-\s*\*\*Expected Output\*\*:\s*\n```[^\n]*\n(.*?)\n```", section, re.DOTALL)
        if not output_match:
            continue
        expected_output = output_match.group(1)

        inputs = [line.strip() for line in raw_inputs.strip().split("\n") if line.strip()]
        test_cases.append({
            "title": title,
            "aim": aim,
            "initial_data": initial_data,
            "inputs": inputs,
            "expected_output": expected_output.strip(),
        })

    return test_cases


def normalize_text(text):
    """Normalizes line breaks and trailing whitespace per line."""
    lines = [line.rstrip() for line in text.strip().splitlines()]
    return "\n".join(lines)


def run_session(inputs):
    """
    Runs the Java application with standard input and captures stdout.
    Returns (raw_stdout, interleaved_console_transcript).
    """
    # Ensure session ends with 'bye'
    session_inputs = list(inputs)
    if not session_inputs or session_inputs[-1] != "bye":
        session_inputs.append("bye")

    input_payload = "\n".join(session_inputs) + "\n"

    run_cmd = ["java", "-cp", BIN_DIR, MAIN_CLASS]
    result = subprocess.run(
        run_cmd,
        input=input_payload,
        capture_output=True,
        text=True,
    )

    stdout = result.stdout
    # Build interleaved transcript
    # Split stdout by DIVIDER_LINE blocks to interleave with inputs
    transcript_lines = []
    input_idx = 0

    stdout_lines = stdout.splitlines()
    in_banner_or_greeting = True
    divider_count = 0

    for line in stdout_lines:
        transcript_lines.append(line)
        if "____________________________________________________________" in line:
            divider_count += 1
            # First 2 dividers encompass the banner + greeting
            if divider_count >= 2:
                in_banner_or_greeting = False

            # After closing divider of previous command and if there are more inputs
            if not in_banner_or_greeting and divider_count % 2 == 0 and input_idx < len(session_inputs):
                user_cmd = session_inputs[input_idx]
                transcript_lines.append(f">>> {user_cmd}")
                input_idx += 1

    interleaved_transcript = "\n".join(transcript_lines)
    return stdout, interleaved_transcript


def match_output(actual_stdout, expected_output):
    """
    Checks whether expected output is satisfied by actual stdout.
    Accepts exact match, or substring match if expected_output is a subset snippet.
    """
    norm_actual = normalize_text(actual_stdout)
    norm_expected = normalize_text(expected_output)

    if norm_expected in norm_actual:
        return True, norm_actual, norm_expected
    return False, norm_actual, norm_expected


def run_test(test_case):
    """Runs a single test case. Returns True on success, False on failure."""
    title = test_case["title"]
    aim = test_case["aim"]
    inputs = test_case["inputs"]
    expected_output = test_case["expected_output"]

    print(f"\n{'='*70}")
    print(f"RUNNING: {title}")
    if aim:
        print(f"AIM: {aim}")
    print(f"INPUTS: {', '.join(inputs)}")
    print(f"{'='*70}")

    if os.path.exists(DATA_FILE):
        os.remove(DATA_FILE)

    if test_case.get("initial_data") is not None:
        os.makedirs(DATA_DIR, exist_ok=True)
        with open(DATA_FILE, "w", encoding="utf-8") as f:
            f.write(test_case["initial_data"].strip() + "\n")

    actual_stdout, transcript = run_session(inputs)

    if os.path.exists(DATA_FILE):
        os.remove(DATA_FILE)

    matched, norm_actual, norm_expected = match_output(actual_stdout, expected_output)

    print("\n--- [CONSOLE SESSION RECORD] ---")
    print(transcript)
    print("--------------------------------\n")

    if not matched:
        print(f"\n❌ FAILED: {title}", file=sys.stderr)
        if aim:
            print(f"Aim: {aim}", file=sys.stderr)
        print("\n[EXPECTED OUTPUT]:", file=sys.stderr)
        print(norm_expected, file=sys.stderr)
        print("\n[ACTUAL OUTPUT]:", file=sys.stderr)
        print(norm_actual, file=sys.stderr)
        return False

    print(f"✅ PASSED: {title}")
    return True


def main():
    parser = argparse.ArgumentParser(description="Run UI test plan for Foodielover chatbot.")
    parser.add_argument(
        "--test-plan",
        default=TEST_PLAN_PATH,
        help="Path to ui-test-plan.md file",
    )
    parser.add_argument(
        "--filter",
        help="Filter test cases by title substring",
    )
    parser.add_argument(
        "--commands",
        nargs="+",
        help="Directly run an ad-hoc list of commands",
    )
    parser.add_argument(
        "--expected",
        help="Expected output snippet for ad-hoc commands",
    )

    args = parser.parse_args()

    print("Compiling Java source code...")
    compile_java()
    print("Compilation successful.")

    # Ad-hoc command mode
    if args.commands:
        expected = args.expected or ""
        test_case = {
            "title": "Ad-hoc Command Execution",
            "aim": "Verify ad-hoc input commands",
            "inputs": args.commands,
            "expected_output": expected,
        }
        passed = run_test(test_case)
        sys.exit(0 if passed else 1)

    # Test plan mode
    test_cases = parse_test_plan(args.test_plan)
    if not test_cases:
        print(f"No test cases found in {args.test_plan}!", file=sys.stderr)
        sys.exit(1)

    if args.filter:
        test_cases = [tc for tc in test_cases if args.filter.lower() in tc["title"].lower()]
        if not test_cases:
            print(f"No test cases matched filter: '{args.filter}'", file=sys.stderr)
            sys.exit(1)

    total = len(test_cases)
    passed_count = 0

    print(f"Loaded {total} test case(s) from {args.test_plan}.\n")

    for tc in test_cases:
        success = run_test(tc)
        if not success:
            print(f"\n[TERMINATION] Test session terminated immediately due to failure in '{tc['title']}'.")
            print(f"Summary: {passed_count}/{total} passed before termination.")
            sys.exit(1)
        passed_count += 1

    print(f"\n{'='*70}")
    print(f"ALL TESTS PASSED: {passed_count}/{total} test cases succeeded.")
    print(f"{'='*70}\n")
    sys.exit(0)


if __name__ == "__main__":
    main()
