---
name: test-ui
description: Run scripted command-line UI tests from test/ui-test-plan.md, compare each case's actual stdout with its expected output, print the console input/output record, and stop immediately after the first failure.
---

# Test UI

Run the project's command-line chatbot against the test cases recorded in
`test/ui-test-plan.md`. Treat each test case as one fresh program session with
an ordered list of commands and an exact expected stdout transcript.

## Test-plan format

Write every case using this structure:

````markdown
## Test case: Descriptive name

### Aim
State what behavior the case verifies.

### Inputs
```text
first command
second command
bye
```

### Expected output
```text
The complete stdout transcript, including the final newline.
```
````

Keep the expected output synchronized with the program's user-visible output.
Do not include shell prompts or terminal input echo unless the program itself
prints them.

## Run the tests

1. Run from the repository root.
2. Confirm Java 25 is active.
3. Compile the application into the ignored `out/` directory:

   ```bash
   mkdir -p out
   javac -d out src/main/java/yanny/*.java
   ```

4. Run the bundled test runner:

   ```bash
   python3 .codex/skills/test-ui/scripts/run_ui_tests.py \
     test/ui-test-plan.md \
     --program "java -cp out yanny.Yanny"
   ```

The runner must print each case's console input and actual output. On the
first failure, stop without running later cases and print both the expected and
actual stdout transcripts. Return a non-zero exit status for a failure,
timeout, malformed test plan, or non-zero program exit.

## Update the plan

Add a test case whenever a user-visible command or validation rule is added.
Capture the complete output from a passing run, review it, and place it in the
case's Expected output block. Keep cases independent by including all setup
commands they need.

## Resource

Use `scripts/run_ui_tests.py` as the standard-library-only test runner. Do not
open a browser or modify application source code while running this skill.
