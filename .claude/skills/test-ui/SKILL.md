---
name: test-ui
description: Run the UI test cases in test/ui-test-plan.md against GLaDOS and check the console output matches. Use after any code change to the command loop or output formatting, and whenever the user asks to test the UI or run the UI tests.
---

# test-ui

Compiles GLaDOS and its supporting classes, then runs each test case in
[test/ui-test-plan.md](../../../test/ui-test-plan.md) as a full console
session, comparing the actual output to the expected output recorded there.

## Usage

Run from the repository root:

```bash
python .claude/skills/test-ui/scripts/run_ui_tests.py
```

Optionally pass a different plan file:

```bash
python .claude/skills/test-ui/scripts/run_ui_tests.py path/to/other-plan.md
```

## What it does

For each test case, the script:
1. Prints the test's name and aim.
2. Creates a fresh, empty working folder for GLaDOS to run in, so saved
   data never carries over between tests or into the repository. If the
   test has a **Data file before** block, writes it to `data/glados.txt`
   in that folder first.
3. Feeds its **Input** block to GLaDOS on stdin, as if typed by a user.
4. Prints the console input and the actual console output, so the full
   session is visible.
5. Compares the actual output to the test's **Expected output** block
   (Windows/Unix line-ending differences are ignored, everything else
   must match exactly). If the test has a **Data file after** block, also
   compares it to the contents of `data/glados.txt` after the run.
6. **Stops immediately at the first failing test case** and reports both
   the expected and actual output (or data file) for that case, rather
   than continuing on to the rest.

If every test case passes, it prints a final summary line.

## Adding or updating test cases

Test cases live in test/ui-test-plan.md, one per `## Test N: <name>` section,
each with a `**Aim:**` line and one `**Input:**` and one `**Expected output:**`
fenced ` ```text ` block. Optional `**Data file before:**` and
`**Data file after:**` blocks set up and check `data/glados.txt`. See the
existing test cases in that file for the exact format the parser expects.

`**Data file before:**` is written as UTF-8. To test a data file saved in
another encoding, use `**Data file before (Windows-1252):**` (as saved by
older Windows text editors) or `**Data file before (UTF-8 with BOM):**`
instead. A test may have only one of these three blocks.

**Whenever a code change alters a command's input format or reply wording**,
update the affected test case(s) in test/ui-test-plan.md to match, then
re-run this skill — do not leave the plan out of sync with actual behaviour.

## When to invoke this skill

Invoke it after making a code change to the command loop, task formatting,
or any user-visible output — and whenever the user asks to test the UI,
run the UI tests, or verify the program still behaves correctly.
