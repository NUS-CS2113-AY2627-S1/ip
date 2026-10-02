---
name: test-ui
description: Run Buddy's CLI test plan (test/ui-test-plan.md) against the compiled program and report pass/fail with a full console transcript. Use whenever asked to test Buddy's command-line behavior, verify UI/output changes, add a new UI test case, or check the program still behaves as documented before/after a code change.
---

# Test UI

Replay the test cases recorded in `test/ui-test-plan.md` against the real,
freshly-compiled `Buddy` program and check the actual console output
against what's recorded there, stopping at the first mismatch. This
formalizes the same compile-pipe-commands-and-check-output verification
that would otherwise be done by hand.

## Test plan format

`test/ui-test-plan.md` holds one `## `-level section per test case:

```markdown
## <Title describing what this test case checks>

**Aim:** <one sentence: what behavior this test case verifies>

**Input:**
```text
<one Buddy command per line>
```

**Expected Output:**
```text
<exact console lines produced by just this test case's input>
```
```

All test cases run as **one continuous Buddy session**: each test case's
`Input` lines are appended to every earlier test case's input before Buddy
is re-run, so task state carries over between test cases exactly like a
real user's session. A test case's `Expected Output` is only the **new**
output its own input produces — not a repeat of earlier test cases'
output. Exception: Buddy prints a one-time startup banner before reading
any input, so only the very first test case's `Expected Output` needs to
include that banner block.

## Adding a test case

Append a new `## ` section at the end of `test/ui-test-plan.md` following
the template above. Get the exact `Expected Output` text by actually
running the program (see below) rather than guessing it, since leading
spaces and punctuation in Buddy's output are significant and easy to get
wrong by eye.

## Running the tests

Run the bundled script from the repository root:

```powershell
python .claude/skills/test-ui/scripts/run_ui_tests.py
```

```bash
python3 .claude/skills/test-ui/scripts/run_ui_tests.py
```

(On Windows use `python`; this project's `python3` is a Microsoft Store
stub. On macOS/Linux use `python3`.) The script compiles
`src/main/java/*.java` fresh into a temp directory, then runs the test
cases in order.

## Reporting results

Relay to the user, verbatim (don't paraphrase or summarize away the
detail):
- The full console-session transcript the script prints (input and actual
  output per test case, tagged PASS/FAIL).
- A clear overall pass/fail summary.
- On failure: the Expected vs. Actual block for the failing test case, and
  its `stderr`/exit code if the script printed them (this happens when,
  e.g., a malformed command crashes the JVM instead of just producing
  wrong output).

## Assumptions

The script assumes the current project layout: no package declaration
(classes live directly in `src/main/java`, an intentional documented
deviation — see the `seedu-java-coding-standard` skill), a main class
named `Buddy`, and `javac`/`java` (Java 25, per AGENTS.md) available on
PATH. If the project later adds a package, moves to Gradle, or renames the
main class, update the `SRC_DIR`/`MAIN_CLASS` constants and the compile
step at the top of `scripts/run_ui_tests.py`.
