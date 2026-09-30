---
name: test-ui
description: Runs UI tests for the Maple chatbot by feeding scripted commands through stdin and comparing the printed output against expected output. Use when the user asks to run UI tests, test the chatbot UI, add UI test cases, or review the test session record in test/ui-test-plan.md.
---

# Maple UI Testing

Run UI tests for the Maple chatbot. Feed scripted commands through
stdin, capture the printed output, and compare it with the expected
output.

## Test cases

The test cases live in `test/ui-test-plan.md`. Each case records its
aim, the inputs, and the expected output. If the user supplies extra
test cases in the request, run those after the plan cases.

## Procedure

1. Run the project test runner from the repository root.

   ```bash
   python3 .opencode/skills/test-ui/scripts/run_ui_tests.py
   ```

   The runner verifies Java 25, removes stale classes from `out`, and
   recursively compiles every Java source file under `src/main/java`
   before it starts the test cases.

2. The runner reads `test/ui-test-plan.md` and extracts every test
   case (id, aim, inputs, expected output).

3. For each test case, in order:

   a. Feed the listed commands to the program through stdin.
   b. Capture stdout.
   c. Compare the captured output with the expected output. The
      program reads stdin until `bye`, so every case must end with
      `bye`. Compare after trimming leading and trailing blank lines
      from both sides and trailing whitespace on each line. The
      banner and separators are part of the expected output.

4. If a case fails, STOP immediately. Do not run the remaining cases.
   Report the failed case id, its aim, the expected output, and the
   actual output, side by side.

5. If every case passes, show the full test session. For each case,
   display the inputs fed to the program and the output it printed,
   exactly as a user would see it. Replace the record in the "Test
   session" section of `test/ui-test-plan.md` with this dated run.

## Notes

- Expected output must be captured verbatim from a passing run before
  a test case is added to the plan. Copy it from the actual program
  output, not from memory.
- When new commands are added to the program, add matching test cases
  to the plan before running the suite.
