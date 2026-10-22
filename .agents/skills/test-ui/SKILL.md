---
name: test-ui
description: Run planned console UI tests, compare exact output, and record each test session in test/ui-test-plan.md.
---

# Test UI

Use this skill to test the project's command-line user interface. The test plan at `test/ui-test-plan.md` is the source of truth for the build command, program command, test aims, console inputs, and expected outputs.

Before running tests, add or update each test case in the plan. Every case must contain:

- A descriptive test-case name and an `Aim` section.
- An `Inputs` code block containing the complete sequence sent to standard input.
- An `Expected output` code block containing the complete expected console output.

Run the plan from the repository root with:

```text
python .agents/skills/test-ui/scripts/run_ui_tests.py
```

The runner builds the program once, launches it once per test case, and compares the complete console output exactly after normalizing Windows and Unix line endings. It records the console input and output for every executed test in the plan's `Latest test session` section and also prints that record to the console.

On the first failed case, the runner stops immediately. It records the failed case and prints its expected and actual outputs; do not continue to later cases or replace the expected output merely to make the test pass. Review any command in the plan before running it because the runner executes the build and program commands through the local system shell.

See [references/plan-format.md](references/plan-format.md) when adding cases or changing the test setup.
