---
name: test-ui
description: Run planned console UI tests, compare each command sequence with its exact expected output, and report the complete test transcript.
---

# Test UI

Use this skill to test the project's console user interface. Keep the test cases and the latest
test-session record in [`test/ui-test-plan.md`](../../../test/ui-test-plan.md).

Invoke this skill after every code update. Before running the tests, decide whether that update changes
user-visible console behaviour and update the plan when a test case needs to be added or revised. For Java
updates, ensure the `seedu-java-coding-standard` skill has been applied first.

## Plan format

For every case, record a descriptive heading followed by:

- **Aim** — the user behaviour or UI rule being verified.
- **Inputs** — the commands supplied to one fresh program run, in a `text` code block, one command per line.
- **Expected output** — the complete console output, in a `text` code block. Preserve spaces, blank lines,
  and separators when they are meaningful to the UI.

Use a new process for each test case so that no task state leaks between cases. Add, revise, or remove
test cases only when that change is within the task being performed.

## Run and compare

1. Read the test configuration and cases in `test/ui-test-plan.md`. Build the program with the plan's
   build command, using Java 25. Do not run tests if the build fails; report that failure instead.
2. For each case in order, start the plan's run command, feed the listed inputs exactly, and capture standard
   output. Compare it with the listed expected output after normalizing only line endings (`CRLF` versus `LF`).
   Do not trim whitespace or otherwise loosen the comparison.
3. On success, continue with the next case. On the first mismatch, stop immediately. Report the case name,
   its input transcript, and clearly labelled **Expected output** and **Actual output** blocks. Do not run
   later cases.
4. After a completed or failed session, update the **Latest test session** section of
   `test/ui-test-plan.md` and show the same console input/output record in the response. Include the Java
   version, test result, and failed-case details when applicable.

When the requested behaviour has no existing test case, add a focused case to the plan before running it.
