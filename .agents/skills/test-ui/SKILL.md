---
name: test-ui
description: Run this project's scripted console UI test plan, compare each session with expected output, and stop on the first failure.
---

# Test UI

Use this skill for the line-oriented console UI in this project. The source of
truth is [test/ui-test-plan.md](../../../test/ui-test-plan.md).

## Test-plan contract

Read the plan before testing. Each test case must contain:

- an `Aim` describing the behavior under test;
- an `Inputs` fenced block, with one console command per line; and
- an `Expected output` fenced block containing the complete stdout transcript
  for that independent session.

The input block is the ordered command list for one program invocation. Use a
single block for stateful flows such as adding a task and then listing it. Keep
the expected transcript exact; preserve indentation, symbols, and spaces inside
the output. The runner ignores only CRLF versus LF line endings and a final
newline.

## Execution workflow

1. Read `test/ui-test-plan.md` and execute test cases from top to bottom.
2. Confirm that both `java` and `javac` report Java 25 before compiling. Compile
   into a temporary directory so the repository is not changed.
3. Run the program once for each test case using the run command recorded in
   the plan. Use the plan's input block as stdin exactly, including the final
   newline needed by the last command.
4. Use `scripts/run_ui_tests.ps1` to capture stdout and stderr, compare the
   session with the expected transcript, and print the console input/output
   record. Pass the executable and arguments separately; do not use shell
   operators in the arguments.
5. If a case fails, stop immediately. The failure record must include the test
   case, the actual output, the expected output, the exit code, and any stderr.
   Do not continue to later cases or silently revise the plan.
6. When all cases pass, report the pass summary and retain the printed session
   records in the response.

If the user supplies additional command/expected-output pairs, add them to
`test/ui-test-plan.md` with an aim before running them. Do not invent an
expected output when the user has not supplied one.

## Runner example

From the project root, after compiling to `$buildDir`:

```powershell
& .agents\skills\test-ui\scripts\run_ui_tests.ps1 `
    -PlanPath test\ui-test-plan.md `
    -RunExecutable java `
    -RunArgument @('-cp', $buildDir, 'dobby.Dobby')
```

The runner is deliberately fail-fast: its first mismatch or process error
returns a non-zero exit code and prevents later sessions from running.
