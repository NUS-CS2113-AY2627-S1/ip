---
name: test-ui
description: Execute and verify scripted stdin/console UI tests for this Java project, using the cases recorded in test/ui-test-plan.md.
---

# Test UI

Use this skill when validating the project's command-line user interface with
scripted input. The test plan is the source of truth: read
`test/ui-test-plan.md` before testing and preserve its aims, inputs, and
expected outputs.

## Workflow

1. Check that Java 25 is active. If it is not, stop and report the runtime
   mismatch; do not silently test with another Java version.
2. Review the program target and test cases in `test/ui-test-plan.md`.
3. Run the bundled runner from the repository root:

   ```powershell
   python .codex/skills/test-ui/scripts/run_ui_tests.py
   ```

   The runner compiles the Java sources with `javac --release 25`, starts a
   fresh program process for each test case, sends the listed input lines, and
   compares the complete console output with the expected output. Use the
   runner's options when the plan specifies a different source directory or
   main class.
4. Treat output comparison as exact after converting Windows line endings to
   LF. Do not ignore extra output, missing output, ordering, or whitespace.
5. The runner prints a console transcript for every case. Include that
   transcript in the test report. If a case fails, stop immediately, report
   the actual and expected outputs (and the first useful diff if available),
   and do not execute later cases.

## Test-plan format

Each case in `test/ui-test-plan.md` must have this shape:

```markdown
## Test case: Short name

Aim: What behavior this case verifies.

Inputs:
```text
first command
second command
bye
```

Expected output:
```text
the complete console output, including the final newline
```
```

Use one command per input line. A case may contain setup commands followed by
the behavior under test; all of its commands run in one fresh process so state
is isolated between cases. Keep expected output complete and deterministic.
Do not put an unescaped triple-backtick sequence inside an input or expected
output block.

The plan should also document the program entry point, Java version, execution
assumptions, and comparison rules. Update the plan when the UI contract
changes; do not weaken the runner's comparison to make a failing case pass.
