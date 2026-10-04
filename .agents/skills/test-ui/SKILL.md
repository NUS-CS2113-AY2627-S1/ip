---
name: test-ui
description: Run fail-fast command-line UI regression tests for this Java project from test cases stored in test/ui-test-plan.md. Use when the user supplies UI commands and expected console output, asks to execute or update UI tests, or invokes $test-ui.
---

# Test UI

Treat `test/ui-test-plan.md` as the single source of truth for UI test
cases. Run commands from the repository root.

## Record test cases

Before testing, add or update the user-provided cases in
`test/ui-test-plan.md`. Preserve existing cases unless the user asks to
replace or remove them. Assign sequential IDs such as `UI-002` when the
user does not provide IDs.

Each case must contain:

- a concise aim explaining the behavior under test;
- an `Inputs` fenced text block with one console command per line; and
- an `Expected output` fenced text block containing the complete stdout
  for that program run.

Every input list must end with `bye` so the program exits normally. A test
case can contain multiple commands when later commands depend on state set
up earlier in that same session. Each test case starts a fresh program
process and therefore cannot depend on another case.

Use the exact Markdown structure already demonstrated in the test plan.
Do not duplicate the cases in another tracked data file. The runner
normalizes only CRLF versus LF line endings; spaces, blank lines, text,
and the final newline remain significant.

## Run tests

1. Confirm that both `java` and `javac` report major version 25.
2. Use an available Python 3 executable to run
   `scripts/run_ui_tests.py` from the repository root. Pass `--plan` only
   when the user explicitly requests another plan.
3. Let the runner compile all files below `src/main/java` in a temporary
   directory, then execute the cases in plan order.
4. Stop at the first failed case. Do not run later cases, edit application
   code, or change expected output merely to make a failure pass unless the
   user separately asks for those changes.

The ordinary command is:

```text
python .agents/skills/test-ui/scripts/run_ui_tests.py
```

If the sandbox prevents Java from writing temporary class files, rerun the
same command with the required approval rather than changing the test.

## Report results

Show the complete transcript emitted by the runner so the user can inspect
the console input and output for every executed case. On success, report the
number of passing cases. On failure, identify the failed case and show its
console input, actual output, expected output, exit status, and stderr when
present. State explicitly that remaining cases were not run.
