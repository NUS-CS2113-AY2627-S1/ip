---
name: test-ui
description: "Run scripted console UI tests for the Aigis Java application from test/ui-test-plan.md, comparing each session with its expected output."
---

# Test Aigis UI

Use this skill when the user supplies, edits, or asks to execute command-line UI test cases for Aigis.

## Test-plan contract

Keep all test cases and execution details in [test/ui-test-plan.md](../../../test/ui-test-plan.md). Each test case must contain:

- an `Aim:` line describing what behavior is being checked;
- an `Inputs:` fenced `text` block containing one command per line, in order; and
- an `Expected output:` fenced `text` block containing the complete expected stdout transcript, including the startup banner and closing message.

The input block is sent to a fresh Aigis process for that test case. The expected-output block is compared exactly after normalizing only Windows versus Unix line endings; do not trim whitespace or blank lines. This makes spacing in the UI part of the test.

If the user provides new commands and expected output, record them in the plan before running the tests. Preserve existing cases unless the user asks to replace them.

## Running the tests

From the project root, run:

```text
python .codex/skills/test-ui/scripts/run_ui_tests.py --plan test/ui-test-plan.md
```

The runner compiles the Java sources under `src/main/java` into a temporary directory, starts a new `aigis.Aigis` process for each test case, and prints the console input and output for every executed case. It requires both `java` and `javac` to be Java 25.

Do not continue to later cases after a failure. The runner exits immediately and reports the failing case's actual and expected output. In the final response, state the first failing case and include the runner's transcript or summarize it only if the user asks for a shorter report.
