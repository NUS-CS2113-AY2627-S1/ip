---
name: test-ui
description: Run command-line UI tests from a Markdown plan containing commands and expected output, then save a console transcript.
---

# Test UI

Use this skill to verify interactive command-line programs in this repository.

Store test cases in `test/ui-test-plan.md`, using the format documented in
[the plan format reference](references/plan-format.md). Each case must state
its aim, console input, and exact expected program output.

Run the plan with the standard-library runner:

```bash
python3 .codex/skills/test-ui/scripts/run_ui_tests.py \
  --plan test/ui-test-plan.md \
  --command '<program command>' \
  --transcript _temp/ui-test-record.txt
```

For this Java project, compile with Java 25 into a temporary directory before
running the command. Pass the resulting `java -cp ... dash.Dash` command to
the runner.

The runner compares the program's stdout with each expected-output block,
executes cases in plan order, and records console input and output in the
transcript. It stops at the first failure and prints both the expected and
actual output. Do not continue with later cases after a failure.
