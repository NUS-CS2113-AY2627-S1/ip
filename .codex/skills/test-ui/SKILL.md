---
name: test-ui
description: Run the text-UI test scenarios in test/ui-test-plan.md against the GPT chatbot, comparing console output against the expected output and halting on the first failure.
---

# Text-UI Testing

Use this skill to verify the GPT chatbot still behaves correctly after a code
change. The test cases live in `test/ui-test-plan.md`; this skill runs them.

## Test plan format

Each scenario in `test/ui-test-plan.md` has:

* a heading of the form `## TC-nn: <short title>`,
* an **Aim** describing what the scenario verifies,
* an **Input** code block: commands sent to the program, one per line,
* an **Expected output** code block: the console output to compare against.

## Procedure

Run the scenarios in the order they appear in the plan.

1. **Compile.** Build to a scratch directory so the IDE's `out/` is untouched:

   ```
   javac -d _temp/classes src/main/java/gpt/*.java
   ```

   `_temp/` is already listed in `.gitignore`, so nothing here is committed.
   If compilation fails, stop and report the compiler errors. Do not run any
   scenario against a stale build.

2. **For each scenario**, write its Input block to `_temp/<tc-id>-input.txt`,
   run the program with that file on standard input, and capture everything the
   program prints:

   ```
   java -cp _temp/classes gpt.Gpt < _temp/tc-01-input.txt > _temp/tc-01-actual.txt 2>&1
   ```

   In PowerShell there is no `<` operator; pipe the file in instead:

   ```
   Get-Content _temp/tc-01-input.txt | java -cp _temp/classes gpt.Gpt > _temp/tc-01-actual.txt
   ```

3. **Compare.** The Expected output block must match the **end** of the actual
   output. TC-01 covers the startup banner explicitly, so its expected block is
   the complete output; later scenarios omit the banner and match as a suffix.

   Normalize both sides before comparing:

    * Strip carriage returns. On Windows the program emits CRLF line endings
      while the plan is stored with LF, and that difference is not a defect.
    * Strip trailing whitespace from every line. Trailing spaces are invisible
      in the plan and easy to lose when editing it.

   Compare everything else exactly, including blank lines and separator lines.

4. **On failure, stop immediately.** Do not run the remaining scenarios. Report:

    * the failing scenario's ID and Aim,
    * the first line where the two outputs diverge,
    * the expected text and the actual text at that point.

5. **On success, continue** to the next scenario.

## Reporting

After the session, always show a record of the console input and output so the
test session can be reviewed. For each scenario that ran, show the commands
that were sent and the output the program produced.

Finish with a summary line, for example
`4 of 5 scenarios passed; TC-05 failed.`

Report results faithfully. If a scenario fails, say so plainly and show the
real output. Never describe a scenario as passing without having run it.

## Keeping the plan current

If a code change intentionally alters the console output, or adds a command or
task type that no scenario covers, update `test/ui-test-plan.md` first, then run
this skill. A test plan that no longer matches intended behavior is worse than
no plan.

When adding a scenario, give it the next free `TC-nn` number and state its Aim
in terms of the behavior being verified, not the code being executed.