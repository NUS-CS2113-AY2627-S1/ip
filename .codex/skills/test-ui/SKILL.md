---
name: test-ui
description: Run fail-fast black-box tests for a console UI from commands, inputs, and expected outputs recorded in test/ui-test-plan.md.
metadata:
  short-description: Test a console UI from a Markdown plan
---

# Test UI

Use this project-specific skill when a user asks to test the Java program through its console UI, or provides a list of commands and expected outputs.

## Workflow

1. Read `test/ui-test-plan.md` before running anything. The plan is the source of truth for the test cases.
2. Confirm that every test case has an aim, a program command, an inputs block, and an expected output block. Inputs are the exact lines sent to standard input; the program command launches a fresh process for that test case.
3. Run the plan from the repository root with:

   ```powershell
   pwsh -NoProfile -File .codex/skills/test-ui/scripts/run-ui-tests.ps1 -PlanPath test/ui-test-plan.md
   ```

   On a machine where `pwsh` is unavailable, run the same script with Windows PowerShell using `powershell -NoProfile -File ...`.

4. The runner normalizes only line endings and the final newline before comparing output. Otherwise, comparison is exact, including spaces and blank lines. It also fails a test whose process exits with a non-zero code.
5. The runner starts each test case in its own process and stops immediately at the first failure. Do not continue manually after a failure unless the user asks for diagnostic investigation.
6. In the final response, show the console session record printed by the runner. If a test fails, report the failing test case, actual output, expected output, exit code, and any process error output.

## Plan format

Keep one section per test case in `test/ui-test-plan.md`:

```markdown
## Test case 1: short name

### Aim
What behavior this test checks.

### Program command
```text
java -cp out/production/ip Dexter
```

### Inputs
```text
todo buy milk
list
bye
```

### Expected output
```text
...exact output, excluding the console input echo...
```
```

The program command must be a single executable followed by arguments; shell pipelines, redirection, and multiple commands are intentionally unsupported so that the test session is reproducible. Use one input line per console command. If a test does not need input, leave the inputs block empty.

For this Java project, use Java 25. The runner checks the installed Java version before executing the plan and aborts before the first test if it is not Java 25.
