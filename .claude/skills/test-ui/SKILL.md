---
name: test-ui
description: Run the Eric command-line UI test plan (test/ui-test-plan.md), check actual output against expected output, show the console session, and stop at the first failure. Use when asked to run UI tests, or after changing behavior visible in the console.
---

# test-ui

Runs the UI test cases recorded in `test/ui-test-plan.md` using `test/run-ui-tests.py`.

## Test plan format (`test/ui-test-plan.md`)
Each test case is a section with its aim, the input lines, and the exact expected output (after the startup banner):

````
## TC1: Title

**Aim:** What this test checks.

**Input:**
```
todo read book
bye
```

**Expected output:**
```
...exact console output after the banner...
```
````

Optionally, right after the aim, add the contents of a save file that exists before Eric starts (to test loading). Without it, no file exists at startup:

````
**Initial file (data/duke.txt):**
```
T | 1 | read book
D | 0 | return book | June 6th
```
````

Instead of file contents, an initial file block can hold one special line to create an unusual starting state:

- `(data folder only)`: the `data` folder exists, but the file does not.
- `(folder)`: a folder, not a file, exists where the file should be.
- `(invalid utf-8)`: the file holds bytes that are not valid text.
- `(windows line endings)` as the first line: the remaining lines are written with CRLF line endings.

Optionally, after the expected output, add the expected contents of the file Eric saves (one line per task):

````
**Expected file (data/duke.txt):**
```
T | 1 | read book
D | 0 | return book | June 6th
```
````

Use the single line `(file not created)` when no file should exist. Without this block the file is not checked.

To add a test case, append a section of this shape. Keep `bye` as the last input line.

## Procedure
1. Make sure Java 25 is active (see AGENTS.md; `sdk use java 25.0.3.fx-zulu` on macOS if needed).
2. From the project root run `python3 test/run-ui-tests.py`. The script compiles `src/main/java`, then runs each test case in a fresh `eric.Eric` process, feeding it the case's input.
3. Show the user the record of the test session: for every test case the script prints its title, aim, console input and console output (and the saved file, if the case checks one), followed by PASS. Reproduce this output in your reply (trim nothing the user would need to follow the session).
4. If a test case fails, the script stops immediately (no later cases run), exits non-zero and prints the expected and actual output. Report both to the user, and do not continue with the remaining cases.
5. If the user gives new commands and expected outputs, first record them as test cases in `test/ui-test-plan.md`, then run the script.

## Notes
- Each test case runs in its own empty temporary folder, so `data/duke.txt` never touches real data. It starts out missing unless the case gives an initial file.
- The comparison is exact line by line, ignoring trailing whitespace; the banner and greeting are stripped first.
- If a failure is caused by an intended behavior change, update the expected output in the plan (after confirming with the user), then re-run.
