---
name: test-ui
description: Run CortisolBot against the text-UI test cases in test/ui-test-plan.md, comparing each test case's console output with its expected output and stopping at the first failure. Use after any change to the Java sources, when asked to test the bot, check its output, or verify that a refactoring changed nothing the user can see.
---

# Text-UI Testing

CortisolBot has no unit tests. This skill is the only regression safety net, so run it after **every**
change to `src/main/java`, including changes meant to be invisible such as a refactoring.

## Run the test session

From the repository root:

```powershell
python test/run-ui-tests.py
```

The script compiles the sources afresh into `_temp/ui-test-classes`, then for each test case in
`test/ui-test-plan.md` starts a new copy of the program in its own empty folder under
`_temp/ui-test-runs/`, types the test case's input lines, and compares everything printed against the
test case's expected output. `_temp/` is gitignored, so nothing it writes can enter a commit, and the
user's real `data/cortisolbot.txt` is never read or written.

**The script exits non-zero when a test case fails.** Check the exit status rather than trusting the
last lines of output: piping through `tail` or `head` discards the status and can make a failed
session look finished. In Git Bash, `python test/run-ui-tests.py; echo "EXIT: $?"`, or
`${PIPESTATUS[0]}` when piping.

Useful variations:

* One test case while fixing it: `python test/run-ui-tests.py --only TC-03`
* Several: `--only TC-03 --only TC-07`

## Report the result

Show the user the record of the session, not just a verdict — the point of the exercise is that the
input and output can be inspected. The script prints, for every test case, the lines typed and the
console output they produced, and writes the same record to `_temp/ui-test-session.txt`.

The full record is now several hundred lines, most of it the banner repeated once per test case, so
paste it only when it is short enough to be read:

* **All test cases passed** — say so with the number of test cases, show the list of `PASSED` lines,
  and point to `_temp/ui-test-session.txt`. Quote one test case in full only if it is the one the
  user is interested in.
* **A test case failed** — the session stops there by design and later test cases do not run. Show
  that test case's aim, its input, and the expected and actual outputs the script printed, then
  diagnose. Do not re-run the suite hoping for a different answer, and never edit the expected output
  to match broken behaviour.

A failure means one of two things, and they are settled differently:

1. **The code is wrong.** Fix the code, then run the session again.
2. **The behaviour changed on purpose.** Update the affected expected output in
   `test/ui-test-plan.md`, in the same commit as the code change, so the plan and the code never
   disagree in the repository's history.

## Add a test case when behaviour is added

A new command, a new error message, or a new output format needs a test case before the increment is
finished. Add it to `test/ui-test-plan.md` in the same commit as the code.

The plan's own "How a test case works" section is the reference; in short, a test case is an `## TC-nn
Title` heading holding an `**Aim:**`, an `### Input` block, an `### Expected output` block, and
optionally a `### Data file` block written before the program starts and an `### Expected data file`
block compared after it exits. Points worth repeating:

* The last input line must be `bye`, or the program waits forever for more input.
* `{{greeting}}` and `{{farewell}}` stand in for the banner and the closing lines.
* Write the expected output by **reasoning from the source and from the voice and formatting rules in
  AGENTS.md** — a 55-hyphen separator after each exchange, a leading space on error lines, a leading
  tab on task lines. Never paste the program's actual output into the plan as the expected output: a
  test case that is a copy of what the program happens to do cannot fail, and proves nothing.
* Give each test case an aim that says what is being checked, not what is being typed.

## Writing test cases that can actually fail

These rules come from injecting deliberate bugs and seeing which ones the suite let through. Each one
corresponds to a bug that escaped until the rule was applied.

* **Interleave the good with the bad.** After every command the bot refuses, display the state and
  check it is untouched — `list`, and an `### Expected data file` block at the end. A command that
  corrupts the list *before* noticing the input was wrong produces a perfectly correct error message,
  so the error message alone proves nothing. TC-12 exists for this.
* **Test every boundary of a numeric argument**: the first (`1`), the last (`n`), **one past the last
  (`n + 1`)**, zero, a negative number, something that is not a number at all, and something too
  large for `int`. `n + 1` is the one everybody forgets, and it is the one that turns a polite
  refusal into a crash. TC-13 and TC-14 cover these.
* **Put something after whatever is under test.** Trailing spaces are ignored when outputs are
  compared, so a stray space at the end of a line is invisible. Check a description's boundaries
  through a deadline or an event, where the description is followed by ` (by: ...)` on screen and
  ` | ` in the data file and a stray space shows up as a doubled space mid-line. TC-19 exists for
  this.
* **Separate words with a tab, not only spaces, in at least one test case.** Code that splits on a
  run of whitespace and code that splits on a single space behave identically for every
  space-separated input, so only a tab tells them apart. TC-22 exists for this.
* **Exercise both directions of anything that is saved.** Writing the file and reading it back are
  separate code paths: a test case that saves proves nothing about loading. Pair an
  `### Expected data file` test case with a `### Data file` one, as TC-17 and TC-18 do.

## Checking that the test cases still bite

A suite that cannot fail is worse than no suite, because it is trusted. When the plan has grown, or
before relying on it for a large refactoring, prove it still bites: back up `src/main/java`, introduce
one deliberate bug, run the session, confirm it is caught, and restore. One bug at a time, because the
session stops at the first failure.

Bugs worth trying, all of which the current plan catches: using the task number as the list index;
making the upper bound one too generous; having `unmark` call `markAsDone()`; adding a task before
validating its description; numbering `list` from zero; reporting the count before a deletion rather
than after; dropping a separator in `toFileFormat()`; ignoring the done-status when loading; leaving a
description untrimmed; and splitting input on `" "` instead of `"\\s+"`.

Restore every file afterwards with a plain file copy. **Do not use `git checkout` or `git stash` to
undo the bugs** — per AGENTS.md the user runs all Git commands. Keep the backup under `_temp/`, which
is gitignored, and confirm with `git status` that the working tree is clean before proposing a commit.

## Commit messages

Changes to the plan or the runner are `chore:` work — `test:` is not one of this repository's
categories — and belong in their own commit, separate from the code change whose behaviour they
describe. The one exception is the plan being updated *because* the code changed: then both go in a
single commit, so the plan and the code never disagree in the repository's history. Follow the
`seedu-git-standard` skill, and leave the commands for the user to run.
