---
name: test-ui
description: Run console UI tests recorded in test/ui-test-plan.md, compare actual output with expected output, and stop on the first failure.
metadata:
  short-description: Test console UI output from a recorded plan
---

# Test UI

Use this skill after each code update in this project, and whenever the user asks to test console UI behavior with commands and expected outputs.

## Source of Truth

Use `test/ui-test-plan.md` as the test plan. Keep it updated when a code change affects console behavior, command invocation, inputs, or expected output. Do not invent unrelated test cases; add or revise cases only when they are relevant to the change or when the user supplies new commands and expected outputs.

When the user supplies a list of commands and expected outputs, record that list as test cases in `test/ui-test-plan.md` before running the tests. If an aim is not supplied for a case, infer a brief aim from the command and expected behavior.

Each test case in the plan should specify:

- The aim of the test case.
- The command to run from the repository root.
- The console input to provide, if any.
- The expected console output.
- Any relevant setup, cleanup, or assumptions.

## Running Tests

For each test case, run the program in a fresh process from the repository root unless the plan explicitly says that cases share state. Use Java 25 for Java build and run tasks in this project.

Compare the actual output with the expected output recorded in `test/ui-test-plan.md`. Treat output as exact after normalizing line endings. Do not ignore extra lines, missing lines, changed prompts, stderr, or a nonzero exit code unless the test case explicitly says those differences are expected.

## Reporting

After testing, show a record of the console session so the user can see the command, provided input, and actual output. If the transcript is long, save it in `test/` and link to it, while still summarizing the commands and result in the response.

If a test case fails, stop the test session immediately. Report:

- The failed test case.
- The command that was run.
- The console input that was provided.
- The expected output.
- The actual output.
- The exit code, if it is relevant.

Do not continue to later test cases after the first failure.
