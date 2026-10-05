---
name: test-ui
description: Run project-specific text UI tests from test/ui-test-plan.md and compare console output against expected output.
---

# Test UI

Use this skill when the user asks to run or maintain text UI tests for this project.
Also use this skill after each code update in this project. Before running it, check whether the update
changes text UI behavior; if it does, update `test/ui-test-plan.md` first.

The source of truth for UI test cases is `test/ui-test-plan.md`. Each test case must include:

- the aim of the test case;
- the console inputs, one command per line;
- the exact expected console output.

Use `scripts/run_ui_tests.py` to execute the test plan. It runs the program once per test case, sends that
test case's input lines to standard input, compares the actual output with the expected output, and stops
immediately when a test case fails.

## Test Plan Format

Write test cases in this form:

````markdown
## Test Case: <short name>

Aim: <what this test is checking>

### Input

```text
command 1
command 2
bye
```

### Expected Output

```text
full expected console output
```
````

Keep expected output exact. Whitespace, divider lines, and line breaks matter.
Banner-art-only lines are ignored by the runner so the test plan can focus on meaningful text output.

## Running Tests

Compile the program before running the UI tests. For the current project layout, this can be done with
`javac -d out` over the Java files under `src/main/java`.

Run the UI tests with:

```bash
python .codex/skills/test-ui/scripts/run_ui_tests.py --plan test/ui-test-plan.md -- java -cp out goatbot.GoatBot
```

If the program command is different, place the full command after `--`.

After testing, report the test session record shown by the script. The record includes each test case name,
the console input sent to the program, and the actual console output received from the program.

If a test case fails, report that the session stopped at the first failure and include both the expected and
actual outputs shown by the script.
