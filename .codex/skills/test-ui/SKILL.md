---
name: test-ui
description: Run scripted acceptance tests against this project's interactive console UI, comparing the output after each command with expectations recorded in test/ui-test-plan.md. Use when asked to test command sequences, verify console behavior, or execute the UI test plan.
---

# Test UI

Run the project's interactive console application against ordered commands and expected outputs. Treat `test/ui-test-plan.md` as the persistent source of truth for the test cases and test setup.

## Prepare the test plan

1. Read `AGENTS.md`, `README.md`, and `test/ui-test-plan.md` if it exists.
2. Accept test cases supplied by the user as lists of commands and expected outputs. Before running them, create or update `test/ui-test-plan.md` so every test case records:
   - a unique ID and short name;
   - the aim;
   - any preconditions or required initial state;
   - the ordered console inputs, including a command that exits cleanly when relevant; and
   - the exact output expected after each input.
3. Record the application build and launch commands, working directory, Java version requirement, comparison rules, and any setup or cleanup needed for repeatable tests in the same file.
4. Preserve existing test cases that the user did not ask to change. If required information cannot be inferred without changing what the test means, ask the user for it before executing that case.

Use fenced text blocks for inputs and outputs so whitespace remains visible. Do not place volatile test results in the test plan; report the session transcript in the response after testing.

## Run the tests

1. Confirm that Java 25 is active with `java -version`. Use Java 25 for all compilation and launch commands. If Java 25 is unavailable, stop and report the blocker without running on another version.
2. Perform the documented setup, then compile the current sources before testing. A build or launch failure is a test-session failure: stop and show the command and error output.
3. Run each test case in its own fresh application process unless its documented preconditions explicitly require a shared process or persisted state.
4. Use an interactive terminal session. For each test case:
   - capture startup output separately;
   - send one documented input at a time;
   - wait until the complete response is available;
   - capture the application output produced in response; and
   - compare it immediately with that command's expected output before sending the next input.
5. Compare exact text after normalizing only platform line endings (`CRLF` and `LF`). Preserve blank lines, spaces, punctuation, capitalization, and output order. Exclude terminal input echo from the application output comparison, but retain it in the session transcript. Apply any other normalization only when it is explicitly documented in the test plan.
6. On the first mismatch, timeout, unexpected termination, or nonzero exit:
   - do not send another test command and do not run later test cases;
   - terminate the application process if it is still running; and
   - report the failed test case and command, plus the complete expected and actual output in separate fenced text blocks.
7. When a test case passes, close its process cleanly before starting the next case. Do not modify application data merely to make a failed test pass; only perform cleanup documented by the test plan.

## Report the session

Always show a chronological console transcript containing the launch command, console inputs, application outputs, and build/runtime errors. Clearly distinguish input from output without altering the captured text. Then summarize which test cases passed. If the session stopped on a failure, say that the remaining cases were not run and show the expected-versus-actual output for the first failure.

