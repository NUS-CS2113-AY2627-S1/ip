---
name: test-ui
description: Run command-driven tests for a text user interface and record a
  verifiable console transcript.
---

# Test UI

Use this skill after an application code update changes user-visible
behaviour, or when the user asks for a UI regression check. The test cases and
their purpose belong in test/ui-test-plan.md.

## Test case contract

Each test case must state:

- its aim;
- the ordered list of commands to send to the program; and
- one expected output fragment for each command, in the same order.

Keep expected outputs specific to observable behaviour. Do not compare
unstable details such as process IDs or timestamps.

## Running the tests

1. Read test/ui-test-plan.md and update the cases when the behaviour or
   required coverage has changed.
2. Build the application with Java 25 and identify the executable, arguments,
   and output line that terminates each command response. For Ted, the
   separator line is the response boundary and occurs twice per command.
3. Invoke scripts/run_ui_tests.ps1, passing the executable, argument list,
   command list, expected output list, separator, and transcript path. Keep
   commands and expected outputs paired by index.
4. The runner must stop at the first failed case. Report the command, the
   expected fragment, and the actual response chunk. Do not run later cases
   after a failure.
5. Keep the latest complete console record in test/ui-test-session.txt and
   link it from the test plan when reporting results.

Example for Ted:

~~~powershell
$uiCommands = @(
    'todo borrow book',
    'deadline return book /by Sunday',
    'event project meeting /from Mon 2pm /to 4pm',
    'list',
    'bye'
)
$uiExpected = @(
    '[T][ ] borrow book',
    '[D][ ] return book (by: Sunday)',
    '[E][ ] project meeting (from: Mon 2pm to: 4pm)',
    '1.[T][ ] borrow book',
    'Bye. Hope to see you again soon!'
)
$runnerArguments = @{
    Executable = 'C:/Program Files/Java/jdk-25/bin/java.exe'
    ArgumentList = @('-cp', '_temp/ui-test', 'ted.Ted')
    Commands = $uiCommands
    ExpectedOutputs = $uiExpected
    EndMarker = '    ____________________________________________________________'
    TranscriptPath = 'test/ui-test-session.txt'
}
& .codex/skills/test-ui/scripts/run_ui_tests.ps1 @runnerArguments
~~~
