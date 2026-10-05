---
name: test-ui
description: Run the project's console UI test cases from test/ui-test-plan.md, compare actual and expected output, and stop at the first failure.
---

# Test UI

Use this skill after a code update that changes the chatbot's user-visible behavior, and whenever the user explicitly asks to run UI tests.

## Test source

Read `test/ui-test-plan.md` before running tests. Each test case must record:

- the aim of the test;
- the exact console input, with one command per line; and
- the expected output for that input.

If the requested behavior is not represented, update the test plan before testing. Keep existing cases unless the behavior they describe intentionally changed.

When the user supplies an explicit list of commands and expected outputs, use
that list for the current run and add or update the corresponding test-plan
case when it represents new or changed behavior.

## Execution

1. Confirm that Java 25 is being used.
2. Compile the Java sources into a temporary output directory without changing
   tracked project files.
3. Run each test case separately, passing its listed commands to
   `bigbrother.BigBrother` in the recorded order.
4. Capture the complete console transcript, including the commands sent to the
   program and all output it produces.
5. Compare the expected output lines with the actual output in the same order,
   matching each expected line exactly after normalizing only line endings.
   The plan records important response lines rather than the startup banner
   and separator lines. Do not ignore a mismatch in wording, task type, status,
   count, or formatting.
6. Print the console input and output for every completed test case.
7. If a test fails, stop immediately. Report the test case name, expected
   output, actual output, and the console transcript. Do not run later cases.

## Reporting

For a successful run, report every test case as passed and include its full
input/output transcript. For a failed run, report only the cases run up to and
including the first failure, with the expected and actual output clearly
separated.

Do not modify application code merely to make a test pass. If a test reveals a
real behavior problem, report it and wait for the user's instruction before
changing the application.
