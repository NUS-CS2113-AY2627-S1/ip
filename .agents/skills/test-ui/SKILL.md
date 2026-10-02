---
name: test-ui
description: >-
  Executes UI testing for the chatbot by running commands against the Java application and verifying output.
  Use whenever testing command interactions, validating UI regression test cases from test/ui-test-plan.md,
  or inspecting the console session transcript.
---

# UI Testing Skill (`test-ui`)

This skill automates UI acceptance and regression testing for the chatbot. It executes command sequences, validates actual program outputs against expected outputs, presents an interleaved console session record, and halts immediately upon encountering any test failure.

---

## 1. Test Plan Organization (`test/ui-test-plan.md`)

All standard UI test cases and their specifications are documented in [`test/ui-test-plan.md`](file:///Users/Siew%20Hean/Documents/GitHub/ip/test/ui-test-plan.md).

Each test case adheres to the following specification format:

```markdown
### <ID>: <Test Case Title>
- **Aim**: <Objective of the test case>
- **Inputs**:
```
<command 1>
<command 2>
bye
```
- **Expected Output**:
```
<expected output block or text snippet>
```
```

### Key Elements of Every Test Case:
1. **Aim**: Specific goal and behavior being verified.
2. **Inputs**: The sequence of user commands sent to the application.
3. **Expected Output**: The expected text response against which the program's output is verified.

---

## 2. Test Execution Workflow

The automated test runner script is located at:
[`.agents/skills/test-ui/scripts/run_ui_tests.py`](file:///Users/Siew%20Hean/Documents/GitHub/ip/.agents/skills/test-ui/scripts/run_ui_tests.py)

### Running All Test Cases
To execute all test cases defined in `test/ui-test-plan.md`:
```bash
python3 .agents/skills/test-ui/scripts/run_ui_tests.py
```

### Running Filtered Test Cases
To run a specific test case (e.g., `TC-02` or `Todo`):
```bash
python3 .agents/skills/test-ui/scripts/run_ui_tests.py --filter "TC-02"
```

### Running Ad-Hoc Commands
To test a custom command sequence without modifying the test plan:
```bash
python3 .agents/skills/test-ui/scripts/run_ui_tests.py --commands "todo borrow book" --expected "[T][ ] borrow book"
```

---

## 3. Test Behavior & Guarantees

1. **Compilation**: Automatically compiles Java sources (`src/main/java/*.java` into `bin/`) before testing.
2. **Console Session Recording**:
   After testing, an interleaved record of console input (prefixed by `>>> `) and chatbot output is displayed so the full interaction session is visible.
3. **Immediate Failure Termination**:
   If any test case fails:
   * The test runner terminates the session **immediately** (stops execution of subsequent test cases).
   * It outputs the exact `[EXPECTED OUTPUT]` and `[ACTUAL OUTPUT]` with the test aim.
   * Exits with non-zero exit code (`1`).
