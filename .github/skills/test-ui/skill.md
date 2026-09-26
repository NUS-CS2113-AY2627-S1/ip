---
name: test-ui
description: Run a console UI test session for MoistBot by feeding commands and checking output against expected results.
---

# test-ui

Use this skill to validate the interactive console output of `MoistBot`.

## Goal

Given a list of commands and a matching list of expected outputs, the skill should:

- compile the Java sources
- run the app once per test case
- capture the console input and output
- compare the actual output with the expected output
- stop immediately on the first failure and report the actual and expected output

After any code update, this skill must be invoked as part of the verification workflow. If the edited behaviour changes the UI or command output, update `test/ui-test-plan.md` first when needed so the plan reflects the new expectations.

## Test-plan location

The canonical list of cases is stored in `test/ui-test-plan.md`.

## Example usage

```powershell
./test/test-ui.ps1 -Commands @(
  'bye'
) -ExpectedOutputs @(
  "____________________________________________________________`n __  __   ___   ___ ____ _____ ____   ___ _____`n|  \/  | / _ \\ |_ _|/ ___|_   _| __ ) / _ \\|_   _|`n| |\\/| || | | | | | \\___ \\ | | |  _ \\| | | | | |`n| |  | || |_| | | |  ___) || | | |_) | |_| | | |`n|_|  |_| \\___/ |___||____/ |_| |____/ \\___/  |_|`nHello! I'm MoistBot`nHow can I help you today?`n____________________________________________________________`n____________________________________________________________`nBye! Have a nice day!`n____________________________________________________________"
)
```

## Failure behaviour

If a mismatch is detected, terminate the test session immediately and display:

- the failing case name
- the console input used
- the actual output
- the expected output
