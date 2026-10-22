---
name: test-ui
description: Run fail-fast end-to-end tests against this project's console UI using cases recorded in test/ui-test-plan.md. Use when asked to test command sequences, compare console responses with expected output, or show a console UI test transcript.
---

# Test UI

Test the `Potato` console application from the repository root. Treat the expected output as the specification; do not change it merely to make a failing test pass.

## Maintain the test plan

Use `test/ui-test-plan.md` as the source of truth for the launch details and test cases.

- When the user supplies test cases, record them in the plan before running them. Preserve unrelated existing cases unless the user asks to replace them.
- Give every case a stable ID and a concise aim. If an aim is omitted, infer one from the supplied commands.
- Record commands in execution order. Pair every input with the output expected immediately after that input.
- Preserve expected output verbatim in fenced `text` blocks, including indentation and blank lines. An empty block means that no application output is expected.
- Treat a flat list of commands and expected outputs as one ordered test case unless the user identifies separate cases.
- Update launch details when the project's actual build or entry point changes. Do not put generated transcripts or temporary build output in the test plan.

Each case must have this shape:

````markdown
## TC-1: Short name

- Aim: What behavior this case verifies.
- Inputs and expected outputs:

  1. Input

     ```text
     command entered by the user
     ```

     Expected output

     ```text
     exact application response
     ```
````

## Run the tests

1. Read the entire test plan and validate that every case has an aim, at least one input, and expected output for every input. Stop and report incomplete or ambiguous plan data rather than inventing expected text.
2. Confirm that the active JDK is Java 25, as required by this project. Compile using the setup command in the plan. A version or compilation failure is a test setup failure; report it without editing application code.
3. Run each test case in a fresh interactive terminal session using the plan's launch command. Capture startup output before sending any input and check it against the plan's expected startup output.
4. Send one input line at a time. Wait for that command's response, then compare it with the paired expected output before sending the next input.
5. Compare text exactly after normalizing only `CRLF` and `LF` line endings. Preserve leading and trailing spaces, punctuation, capitalization, ordering, and blank lines. Exclude the terminal's echo of the submitted input from the application output being compared.
6. If startup output or a command response differs, the process times out, or the application exits unexpectedly, fail the case and interrupt the running process immediately. Do not run remaining inputs or later cases.
7. After the last successful input, allow a normal application exit when the case caused one. Otherwise interrupt the idle test process so no Java process is left behind; this cleanup does not fail the case.

Do not modify production code while executing the plan. A test run observes the current program; fixing a discovered defect is a separate task unless the user explicitly asks for it.

## Report the session

Always show a complete, chronological console transcript after testing, including the startup output and every input that was actually sent. In the transcript, prefix user input with `>>> ` and leave application output verbatim; explain that the prefix is a transcript marker, not program output.

For a passing run, report the number of cases and command steps passed. For a failure, report the failing case and input step, show the actual and expected outputs in separate fenced blocks, state that the session was terminated immediately, and include the transcript only through the failure. Do not claim that unexecuted cases passed.
