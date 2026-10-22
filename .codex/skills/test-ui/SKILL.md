---
name: test-ui
description: Run scripted console UI test cases from test/ui-test-plan.md, compare each actual output with its expected output, and stop at the first failure.
---

# Console UI testing

Use this skill when the user provides or requests command-line UI test cases for this project. The test plan is the source of truth and must be maintained at `test/ui-test-plan.md`.

## Test-plan requirements

Record each test case in `test/ui-test-plan.md`. Every case must include:

- an aim;
- the exact console inputs/commands, in order; and
- the expected output, preserving meaningful whitespace and line breaks.

Also record the program launch command, working directory, Java version (use Java 25), and any environment assumptions. Keep commands and expected output in fenced code blocks so they can be copied without interpretation.

## Running tests

Translate the plan into a JSON file containing an ordered `tests` list. Each item must have `name`, `command`, and `expected_output`; `command` is the complete launch command and any required stdin, while `expected_output` is the complete output to compare. Run the bundled `scripts/run_ui_tests.py` with that JSON file.

The runner executes cases in order and compares normalized line endings only. It prints the console input and output for every executed case. If a case fails, it immediately stops, reports both actual and expected output, and does not execute later cases. Do not hide or summarize the transcript.

After a run, update `test/ui-test-plan.md` with the date, result, and the complete transcript produced by the runner. Do not call a test passed unless the actual output matches the expected output exactly under the runner's line-ending normalization.
