---
name: test-ui
description: Compile and test the Noah command-line UI against ordered commands and expected output blocks. Use after source changes that can affect console behavior or when asked to run or update the project's UI tests.
---

# Test UI

Run reproducible console tests for Noah with Java 25. Use
`test/ui-test-plan.md` as the default source of test cases.

## Inputs

Each test case must provide:

- a short name and aim;
- an ordered list of console commands ending with `bye`; and
- one expected output block for each command.

The runner accepts either the default test plan or ad hoc `Commands` and
`ExpectedOutputs` arrays. Expected blocks are matched exactly, in order,
within the complete console output. Startup art and separators do not need to be
repeated in every expected block.

## Workflow

1. Read `test/ui-test-plan.md` before testing.
2. If intended console behavior changed and file edits are authorized, update
   the test plan first. Never weaken an expectation merely to make a failure
   pass.
3. Run the default suite from the repository root:

   ```powershell
   powershell.exe -NoProfile -ExecutionPolicy Bypass -File .codex/skills/test-ui/scripts/run-ui-tests.ps1
   ```

4. For an ad hoc case, invoke the script from PowerShell and pass equally sized
   `-Commands` and `-ExpectedOutputs` arrays. The last command must be
   `bye`.
5. Show the script's console input/output transcript in the result.
6. Stop at the first failure. Report the failing test name and command together
   with the actual output and expected block.

The runner verifies Java 25, compiles sources into a uniquely named system
temporary directory, runs every test case in a fresh Noah process, and removes
the temporary directory afterward.
