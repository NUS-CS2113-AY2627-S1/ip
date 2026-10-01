---
name: test-ui
description: Run repeatable console UI test cases from lists of program inputs and expected outputs, maintain test/ui-test-plan.md, stop at the first mismatch, and present the captured test session. Use for manual or scripted text-UI acceptance testing in this repository; do not use for unit tests.
---

# Test UI

Test the program as a user would through its console interface. Treat the commands in a test case as standard-input lines for the program, never as shell commands.

## Test-case input

Accept test cases written as a numbered list, table, or parallel lists of commands and expected outputs. Pair parallel-list entries by position. Each test case must ultimately contain:

- an aim that states the behavior being checked;
- one or more exact console input lines; and
- the exact complete console output expected from a fresh program run.

Preserve supplied whitespace and blank lines. If an aim is omitted but the behavior is clear, write a concise aim such as `Verify the response to "list"`; ask only when a missing or ambiguous value prevents a meaningful comparison.

## Test plan

Before running any test, create or update `test/ui-test-plan.md`. Keep previously recorded cases unless the user asks to replace them. Record:

- the program launch/build commands, working directory, required Java version, and relevant preconditions;
- the comparison policy;
- every test case's stable ID, aim, inputs, and expected output; and
- the latest session's timestamp, result, per-case status, and console transcript.

Use fenced text blocks for inputs, expected outputs, actual outputs, and transcripts so spacing remains inspectable. Never replace an expected output with the observed output after a failure.

## Execution

1. Read the repository instructions and available build/run documentation. Use Java 25 for this project. Discover the launch command from the project rather than assuming one, and record it in the plan.
2. Build once before the session when needed. Treat a build or launch error as an immediate failed session and capture its console output.
3. Run test cases in plan order. Start a fresh program process for each case, feed only that case's input lines exactly as recorded, and capture stdout, stderr, and the exit code. Do not add convenience commands such as `bye` unless they are part of the recorded inputs.
4. Compare the complete stdout with the expected output. Normalize only line endings (`CRLF` and `LF`) before comparison; preserve all other characters, including leading/trailing spaces and blank lines. Any unexpected stderr, nonzero exit code, missing output, extra output, or text mismatch fails the case unless the test case explicitly expects that condition.
5. On the first failure, terminate the current program if it is still running, do not run later cases, and mark later cases `NOT RUN` in the plan.

If the application cannot terminate after the supplied input, use a bounded timeout appropriate to the program. A timeout is a failure, and its partial output must be retained.

## Reporting

After a successful session, report the number of passed cases and show the captured console input and output for every case.

After a failed session, report the failed case ID and aim, then show:

- the console input;
- the actual stdout and stderr;
- the expected output;
- the exit code or timeout; and
- which remaining cases were not run.

In both outcomes, include the session transcript in the response and save the same record under `Latest test session` in `test/ui-test-plan.md`.
