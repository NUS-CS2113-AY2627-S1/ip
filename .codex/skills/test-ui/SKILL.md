---
name: test-ui
description: Run console UI test cases from test/ui-test-plan.md, compare actual and expected output, and stop at the first failure.
---

# Test UI

Use this skill when testing the project's command-line interface with scripted input and
expected output. The test plan is the source of truth for the cases and must remain a
human-readable record of what was tested.

## Test-plan format

Read `test/ui-test-plan.md` before running tests. Record every requested or newly created
test case there with:

- a unique case ID;
- the aim of the test;
- an ordered `inputs` list containing the console commands for one fresh process; and
- the expected console output.

Keep the executable cases in the single `json` block labelled `Executable test cases` in
the plan. Each object must have this shape:

```json
{
  "id": "TC-001",
  "aim": "Describe what this test verifies",
  "inputs": ["first command", "second command"],
  "expected_output_lines": ["first output line", "second output line"]
}
```

Use `expected_output` instead of `expected_output_lines` when a literal multiline string
is easier to maintain. Expected output is compared exactly after normalizing line endings;
do not silently trim banners, prompts, or trailing output.

## Run the tests

1. Compile or otherwise prepare the program using the project's normal Java 25 workflow.
2. Run the bundled `scripts/run_ui_tests.py` from the repository root, passing the plan
   and the program command. For example:

   ```text
   python .codex/skills/test-ui/scripts/run_ui_tests.py \
     --plan test/ui-test-plan.md \
     --program "java -cp build/classes Main"
   ```

3. The runner starts a fresh process for each case, sends its `inputs` with a final newline,
   captures stdout and stderr as the console session, and compares it with the expected
   output.
4. Show the complete console input and output record for each executed case. If a case
   fails, stop immediately, show the actual output and expected output, and do not run any
   later cases.
5. Report the pass/fail result and preserve the executed case record in the test plan.

Do not weaken a failing comparison by trimming or matching only a convenient fragment unless
the test plan explicitly defines that behavior.
