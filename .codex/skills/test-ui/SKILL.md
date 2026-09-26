---
name: test-ui
description: Run command-line UI test cases for this Java project from test/ui-test-plan.md, compare actual and expected output, and record the console session.
---

# UI testing

Use this skill when testing the application's text-based user interface.

## Test plan

Read `test/ui-test-plan.md` before testing. Each test case must document:

- its aim;
- the complete console input/commands;
- the expected output.

If the plan is missing or incomplete, update it before running tests. Preserve existing cases unless the user asks to change them.

## Execution

1. Determine the project's documented Java 25 run command and use Java 25 for the test session.
2. Run each test case in the order listed, supplying its inputs exactly as written.
3. Compare the complete observed output with the expected output, allowing only differences explicitly permitted by the test plan (such as platform-specific line endings).
4. Stop immediately at the first failed test. Report the test case, expected output, and actual output; do not continue with later cases.
5. After a successful session, show a transcript containing every console input and output. For a failed session, show the transcript up to the failure and clearly mark the mismatch.

Do not silently alter expected output to make a test pass. If the application cannot be launched, treat the session as failed and report the launch command and error output.
