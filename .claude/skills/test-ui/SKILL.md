---
description: "Run UI tests by piping commands into the chatbot and comparing actual output against expected output from the test plan."
---

# UI Testing

This skill runs the Aglio chatbot with predefined inputs and checks
that the actual console output matches the expected output.

## When to apply

Run this skill whenever you want to verify that the chatbot's
interactive behaviour has not regressed after code changes.

## Workflow

### 1. Read the test plan

Read the file `test/ui-test-plan.md`. Each test case specifies:
- **Aim** -- what the test verifies.
- **Inputs** -- the commands to pipe in (one per line, in a fenced
  code block tagged `input`).
- **Expected output** -- the exact full console output (in a fenced
  code block tagged `expected`).

### 2. Compile the project

```bash
javac -d out src/main/java/aglio/*.java
```

If compilation fails, stop immediately and report the error.

### 3. Run each test case in order

For each test case:

1. Pipe the input lines into the program:
   ```bash
   echo -e "line1\nline2\n..." | java -cp out aglio.Aglio
   ```
2. Capture the full console output (both stdout and stderr).
3. Compare the actual output against the expected output,
   **line by line, ignoring trailing whitespace on each line**.
4. **If the outputs do not match**:
   - Print `FAIL: <test case name>`.
   - Show the expected output under an `Expected:` header.
   - Show the actual output under an `Actual:` header.
   - **Stop immediately** -- do not run any remaining test cases.

### 4. Report results

If all test cases pass:
- For each test case, show its name and the full console session
  (the actual input and output).
- Print a summary: `All <N> test(s) passed.`
