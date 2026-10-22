---
name: test-ui
description: Run command-line UI test cases for this project from a command-and-expected-output list, record the plan, and show the console session.
---

# UI testing

Use this skill when the user provides or requests command-line UI test cases for the project.

## Workflow

1. Read `test/ui-test-plan.md`. Preserve existing test cases and add or update cases requested by the user.
2. Each test case must record:
   - Aim
   - Inputs: the commands sent to standard input, in order
   - Expected output
3. Run the program using Java 25 and feed each test case's commands to standard input. Compare the observed output with the expected output. Treat output comparison as exact unless the test plan explicitly marks a value as variable.
4. Stop immediately at the first failed test case. Do not run later cases after a failure.
5. After testing, show a console-session record containing the input and actual output for every case run.
6. For a failure, report the test case, actual output, and expected output clearly, then stop. For passing cases, report that they passed and include their session records.

Keep test execution scoped to this repository. Do not alter application source code while using this skill. If the test plan is missing, create it before running tests.
