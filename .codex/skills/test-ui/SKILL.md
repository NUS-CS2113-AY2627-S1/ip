---
name: test-ui
description: Run fail-fast console UI tests from test/ui-test-plan.md when checking interactive command input and exact expected output.
---

# Test UI

Use `test/ui-test-plan.md` as the source of truth. Record the program command and
every test case there before testing. Each test case must contain:

- a unique name and aim;
- the console commands to enter, in an `Inputs` fenced text block; and
- the complete expected console output, in an `Expected output` fenced text block.

When the plan declares a `Data file`, the runner removes it before cases that
do not declare an `Initial data` block. For a loading test, add an `Initial
data` fenced text block containing the exact file fixture.

Run all recorded cases from the repository root:

```bash
python3 .codex/skills/test-ui/scripts/run_ui_tests.py test/ui-test-plan.md
```

The runner starts a fresh program process for each case, sends the listed inputs
to standard input, and compares standard output exactly after normalizing only
Windows line endings. It prints the input and actual output as a console-session
record. On the first failure, it prints the expected and actual outputs, exits
non-zero, and does not run later cases.

If the plan's program command does not work in the current environment, report
that blocker without claiming that the tests passed. Do not silently alter
expected output to match a failure; change it only when the requirement changes.
