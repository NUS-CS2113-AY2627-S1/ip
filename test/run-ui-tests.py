#!/usr/bin/env python3
"""Run the CortisolBot text-UI test cases recorded in test/ui-test-plan.md.

Each test case in the plan supplies a list of input lines and the console output
they are expected to produce. For every test case this script starts a fresh
copy of the program, feeds it the inputs, and compares the console output with
the expected output. The first mismatch ends the session immediately and prints
both outputs along with a diff.

The script uses only the Python standard library.

Usage (from the repository root):

    python test/run-ui-tests.py
    python test/run-ui-tests.py --only TC-03 --only TC-04
"""

import argparse
import difflib
import shutil
import subprocess
import sys
from pathlib import Path

# Subsections a test case may contain, written as they appear in the plan.
INPUT = "input"
EXPECTED_OUTPUT = "expected output"
DATA_FILE = "data file"
EXPECTED_DATA_FILE = "expected data file"
CASE_SUBSECTIONS = {INPUT, EXPECTED_OUTPUT, DATA_FILE, EXPECTED_DATA_FILE}

# Where the program keeps its saved task list, relative to its working folder.
SAVED_LIST = Path("data") / "cortisolbot.txt"

TAB = "\t"


class PlanError(Exception):
    """Raised when the test plan cannot be understood."""


class TestCase:
    """One test case read from the plan."""

    def __init__(self, case_id, title):
        self.case_id = case_id
        self.title = title
        self.aim = ""
        self.blocks = {}

    def block(self, name):
        """Returns the lines of the named block, or None if it was not given."""
        return self.blocks.get(name)


def parse_plan(plan_path):
    """Reads the plan and returns its snippets and its test cases.

    Snippets are reusable blocks of expected output, defined under a "## Snippets"
    heading and inserted wherever a line of expected output reads "{{name}}".
    """
    snippets = {}
    cases = []

    in_snippets = False
    in_aim = False
    case = None
    subsection = None
    target = None
    buffer = None

    for line_number, line in enumerate(plan_path.read_text(encoding="utf-8").splitlines(), 1):
        # A fenced block is being collected: everything up to the closing fence
        # is content, so no other line is interpreted while one is open.
        if buffer is not None:
            if line.startswith("```"):
                if target is not None:
                    target[0][target[1]] = buffer
                buffer = None
                target = None
            else:
                buffer.append(line)
            continue

        # An aim may be wrapped over several lines, so the lines that follow it
        # belong to it until a blank line or the next heading ends it.
        if in_aim:
            if line.strip() and not line.startswith(("#", "```")):
                case.aim = f"{case.aim} {line.strip()}"
                continue
            in_aim = False

        if line.startswith("## "):
            heading = line[3:].strip()
            subsection = None
            in_snippets = heading.lower() == "snippets"
            if heading.upper().startswith("TC-"):
                case = TestCase(heading.split()[0], heading)
                cases.append(case)
            else:
                # Any other "##" heading is narrative text and holds no cases.
                case = None
        elif line.startswith("### "):
            subsection = line[4:].strip()
        elif line.startswith("**Aim:**") and case is not None:
            case.aim = line[len("**Aim:**"):].strip()
            in_aim = True
        elif line.startswith("```"):
            buffer = []
            if in_snippets and subsection:
                target = (snippets, subsection)
            elif case is not None and subsection and subsection.lower() in CASE_SUBSECTIONS:
                if subsection.lower() in case.blocks:
                    raise PlanError(f"line {line_number}: {case.case_id} repeats "
                                    f"the '{subsection}' block")
                target = (case.blocks, subsection.lower())
            else:
                # A fenced block outside any test case, such as an example in
                # the plan's own explanation. Collected, then discarded.
                target = None

    if buffer is not None:
        raise PlanError("the plan ends inside an unclosed ``` block")
    if not cases:
        raise PlanError("the plan holds no test cases (headings of the form '## TC-01 ...')")

    for case in cases:
        for required in (INPUT, EXPECTED_OUTPUT):
            if case.block(required) is None:
                raise PlanError(f"{case.case_id} has no '{required}' block")

    return snippets, cases


def expand_snippets(lines, snippets, case_id):
    """Replaces every "{{name}}" line with the lines of that snippet."""
    expanded = []
    for line in lines:
        stripped = line.strip()
        if stripped.startswith("{{") and stripped.endswith("}}"):
            name = stripped[2:-2].strip()
            if name not in snippets:
                raise PlanError(f"{case_id} refers to unknown snippet '{name}'")
            expanded.extend(snippets[name])
        else:
            expanded.append(line)
    return expanded


def normalise(lines):
    """Removes differences that cannot be seen on screen, so that they cannot
    fail a test case: trailing spaces on a line, and blank lines at the end."""
    normalised = [line.rstrip() for line in lines]
    while normalised and normalised[-1] == "":
        normalised.pop()
    return normalised


def to_lines(text):
    """Splits program output into lines, whatever line ending it used."""
    return text.replace("\r\n", "\n").replace("\r", "\n").split("\n")


def compile_program(source_folder, classes_folder):
    """Compiles the program afresh and returns the folder holding the classes."""
    sources = sorted(str(path) for path in source_folder.glob("*.java"))
    if not sources:
        raise SystemExit(f"No .java files found in {source_folder}")

    if classes_folder.exists():
        shutil.rmtree(classes_folder)
    classes_folder.mkdir(parents=True)

    result = subprocess.run(["javac", "-encoding", "UTF-8", "-d", str(classes_folder)]
                            + sources, capture_output=True, text=True)
    if result.returncode != 0:
        print("Compilation failed, so no test case was run:\n")
        print(result.stdout + result.stderr)
        raise SystemExit(1)
    return classes_folder


def run_case(case, snippets, classes_folder, runs_folder, timeout):
    """Runs one test case and returns (passed, report, transcript)."""
    # Each test case gets its own working folder, so that the task list saved by
    # one test case can never leak into the next one, nor into the real one.
    run_folder = runs_folder / case.case_id
    if run_folder.exists():
        shutil.rmtree(run_folder)
    run_folder.mkdir(parents=True)

    given_data = case.block(DATA_FILE)
    if given_data is not None:
        saved_list = run_folder / SAVED_LIST
        saved_list.parent.mkdir(parents=True, exist_ok=True)
        saved_list.write_text("\n".join(given_data) + "\n", encoding="utf-8")

    stdin_text = "\n".join(case.block(INPUT)) + "\n"

    try:
        result = subprocess.run(
            ["java", "-Dfile.encoding=UTF-8", "-Dstdout.encoding=UTF-8",
             "-cp", str(classes_folder), "CortisolBot"],
            input=stdin_text, capture_output=True, text=True, encoding="utf-8",
            cwd=run_folder, timeout=timeout)
    except subprocess.TimeoutExpired:
        report = (f"The program was still running after {timeout} seconds.\n"
                  "A test case must end with a 'bye' command, or the program "
                  "waits forever for more input.")
        return False, report, transcript_of(case, stdin_text, "(no output: timed out)")

    transcript = transcript_of(case, stdin_text, result.stdout)

    if result.returncode != 0:
        report = (f"The program exited with code {result.returncode}.\n\n"
                  f"Error output:\n{result.stderr or '(none)'}")
        return False, report, transcript

    expected = normalise(expand_snippets(case.block(EXPECTED_OUTPUT),
                                         snippets, case.case_id))
    actual = normalise(to_lines(result.stdout))
    if expected != actual:
        return False, comparison_report("console output", expected, actual), transcript

    expected_data = case.block(EXPECTED_DATA_FILE)
    if expected_data is not None:
        saved_list = run_folder / SAVED_LIST
        if not saved_list.exists():
            return False, f"No task list was saved at {SAVED_LIST}.", transcript
        actual_data = normalise(to_lines(saved_list.read_text(encoding="utf-8")))
        if normalise(expected_data) != actual_data:
            report = comparison_report(f"saved task list ({SAVED_LIST})",
                                       normalise(expected_data), actual_data)
            return False, report, transcript

    return True, "", transcript


def transcript_of(case, stdin_text, stdout_text):
    """Builds the record of one test session, for the user to read afterwards."""
    lines = [f"=== {case.title} ==="]
    if case.aim:
        lines.append(f"Aim: {case.aim}")
    lines.append("--- input typed ---")
    lines.append(stdin_text.rstrip("\n"))
    lines.append("--- console output ---")
    lines.append(stdout_text.rstrip("\n"))
    lines.append("")
    return "\n".join(lines)


def comparison_report(what, expected, actual):
    """Describes a mismatch, showing both sides and then the differing lines."""
    diff = difflib.unified_diff(expected, actual, fromfile="expected",
                                tofile="actual", lineterm="", n=2)
    return "\n".join([
        f"The {what} did not match.",
        "",
        "--- EXPECTED ---",
        *numbered(expected),
        "",
        "--- ACTUAL ---",
        *numbered(actual),
        "",
        "--- DIFFERENCES ---",
        *diff,
    ])


def numbered(lines):
    """Numbers lines and makes tabs visible, since indentation is part of the
    expected output and an invisible tab is impossible to spot in a report."""
    return [f"{number:>3} | " + line.replace(TAB, "\\t")
            for number, line in enumerate(lines, 1)]


def main():
    default_root = Path(__file__).resolve().parents[1]

    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--root", type=Path, default=default_root,
                        help="repository root (default: the folder holding test/)")
    parser.add_argument("--plan", type=Path, default=None,
                        help="test plan to run (default: <root>/test/ui-test-plan.md)")
    parser.add_argument("--only", action="append", default=[], metavar="TC-ID",
                        help="run only this test case; may be given more than once")
    parser.add_argument("--timeout", type=int, default=20,
                        help="seconds to allow one test case (default: 20)")
    args = parser.parse_args()

    root = args.root.resolve()
    plan_path = args.plan or root / "test" / "ui-test-plan.md"
    if not plan_path.exists():
        raise SystemExit(f"No test plan at {plan_path}")

    for tool in ("javac", "java"):
        if shutil.which(tool) is None:
            raise SystemExit(f"'{tool}' is not on the PATH. Java 25 is required.")

    try:
        snippets, cases = parse_plan(plan_path)
    except PlanError as error:
        raise SystemExit(f"{plan_path}: {error}")

    if args.only:
        wanted = {case_id.upper() for case_id in args.only}
        cases = [case for case in cases if case.case_id.upper() in wanted]
        if not cases:
            raise SystemExit(f"No test case matches {', '.join(sorted(wanted))}")

    classes_folder = compile_program(root / "src" / "main" / "java",
                                     root / "_temp" / "ui-test-classes")
    runs_folder = root / "_temp" / "ui-test-runs"
    transcript_path = root / "_temp" / "ui-test-session.txt"
    transcript_path.parent.mkdir(parents=True, exist_ok=True)

    print(f"Test plan: {plan_path}")
    print(f"Test cases: {len(cases)}\n")

    transcripts = []
    for position, case in enumerate(cases, 1):
        passed, report, transcript = run_case(case, snippets, classes_folder,
                                              runs_folder, args.timeout)
        transcripts.append(transcript)
        print(transcript)

        if not passed:
            transcript_path.write_text("\n".join(transcripts), encoding="utf-8")
            print(f"FAILED  {case.title}\n")
            print(report)
            print(f"\nTest session stopped at test case {position} of "
                  f"{len(cases)}. Transcript: {transcript_path}")
            return 1

        print(f"PASSED  {case.title}\n")

    transcript_path.write_text("\n".join(transcripts), encoding="utf-8")
    print(f"All {len(cases)} test cases passed. Transcript: {transcript_path}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
