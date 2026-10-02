---
name: test-ui
description: Test the Dog command-line UI against command sequences and expected outputs, stop at the first failure, and report a console transcript. Use after project code changes or when asked to test chatbot commands.
---

# Test UI

Read `test/ui-test-plan.md` before testing. Accept user-supplied command and
expected-output lists by recording them as cases in that plan. Each case needs
an aim and ordered steps. Keep related commands in one case to preserve task
state; separate cases start fresh processes.

Update the plan when intended behavior changes. Do not change expectations just
to make a failing implementation pass. Explain any mismatch with requirements.

Run from the project root using Python 3 and JDK 25:

```text
python .codex/skills/test-ui/scripts/run_ui_tests.py --java-home "<JDK 25 directory>"
```

The runner also accepts `--repo`, `--plan`, `--transcript`, and `--timeout`.
Without `--java-home`, it checks JAVA_HOME, then java/javac on PATH. Use the
available bundled Python runtime when Python is not on PATH. Request permission
if compilation or transcript writing is blocked; do not bypass permissions.

The runner compiles all production Java sources into a fresh temporary directory,
then compares complete startup and command responses. Only CRLF/LF differences
are normalized. It sends one command at a time and checks before sending the next.
Every case must explicitly end with an exit step, normally `bye`.

On the first mismatch, timeout, compilation error, or unexpected process exit,
stop the entire session and report the case, command, expected output, actual
output, and transcript path. Do not continue to later cases. Correct code only
when authorized; rerun the entire plan after an approved correction.

After testing, show a console input/output excerpt, report the result, and link
the full `_temp/ui-test-session.txt` transcript. Also perform a short independent
interactive check when requested; clearly distinguish it from automated results.
Never claim that tests prove absence of bugs. Do not stage, commit, or push
application code merely because tests passed.
