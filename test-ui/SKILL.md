# test-ui

## Purpose

This is a project-specific UI testing skill for SamSquare.

The skill tests SamSquare through its actual console interface using the test
cases defined in `test/ui-test-plan.md`.

## Instructions

1. Read `test/ui-test-plan.md` before starting the test session.
2. Run the SamSquare program using the project's normal method.
3. Execute the test cases from `test/ui-test-plan.md` in the order they are listed.
4. For each test case:

    * enter the specified console input;
    * capture the actual console output;
    * compare the actual output against the expected output in the test plan.
5. Treat the expected output in `test/ui-test-plan.md` as the specification.
6. Do not change the expected output just to make a test pass.
7. After each test case, record the console input and actual console output.
8. If a test case fails:

    * stop the test session immediately;
    * do not continue to later test cases;
    * report the name or number of the failed test case;
    * report the actual output;
    * report the expected output.
9. After a failing test has been fixed, run the UI test session again from the
   beginning, unless there is a clear project-specific reason to run a smaller
   relevant set of tests.
10. If all test cases pass, report that the UI test session passed and show the
    console input and output from the session.

## Test Plan Maintenance

When user-facing behaviour changes:

1. Check whether `test/ui-test-plan.md` needs to be updated.
2. Add or modify test cases when new behaviour is introduced.
3. Keep the test plan consistent with the current SamSquare requirements.
4. Add both positive and negative cases for each command where the requirements
   define invalid input behaviour.
5. Interleave positive and negative cases when state can carry across commands.
   After rejected input, run `list` or another observable follow-up operation to
   confirm that no task was added, removed, marked, or unmarked accidentally.
6. Include boundary cases such as the first and last valid task number, zero,
   negative numbers, numbers beyond the list size, non-numeric arguments, empty
   descriptions, missing command components, and significant whitespace when
   applicable.
7. Every test case must state its starting state. Say explicitly whether it
   starts a fresh SamSquare process or continues the state produced by named
   earlier cases.
8. Do not infer expected behaviour from the current implementation. Derive it
   from the project requirements, and flag an unclear requirement instead of
   encoding an implementation accident into the test plan.

## Testing Scope

The tests should cover the actual console behaviour of SamSquare, including:

* adding ToDos;
* adding Deadlines;
* adding Events;
* listing tasks;
* marking tasks as done;
* unmarking tasks;
* accepting valid ISO deadline dates and displaying English month names;
* rejecting invalid deadline dates without changing state;
* filtering deadlines by date, including completed tasks and excluding other task types;
* preserving arbitrary event time strings;
* handling multiple task types together;
* exiting with `bye`.

The scope must also include incorrect and incomplete input for the supported
commands, plus checks that rejected commands leave task state unchanged.

## Test Design Review

Before running a session:

0. Establish the documented storage state as well as the in-memory state.
   The earlier plan assumed a fresh process had no tasks, but `Storage.load`
   reads existing saved tasks. For empty-list cases, use a fresh working
   directory with no task file; never overwrite the user's saved tasks.
   Capture and compare the greeting and its separator against the test plan
   before sending the first command, since command-only comparisons miss
   startup display regressions.
1. Map each user-facing command and validation rule to at least one test case.
2. Identify important branches that have only a successful case or only a
   failing case, and add the missing counterpart when the requirement is known.
   The original parsing tests omitted whitespace-only arguments and integer
   overflow. For parser refactoring, test bare commands separately from
   commands followed by spaces, test numbers outside the integer range, and
   verify that command-name prefixes or extra arguments do not trigger an
   unintended action. Follow rejected commands with a state check.
3. Check that expected task counts and list contents follow from the documented
   starting state and every preceding accepted command.
4. Check that the test sequence can expose state corruption rather than merely
   printing an error message.
5. The earlier sessions never restarted the application, so they could not
   detect lost saved tasks or completion status. When changing task ownership
   or storage integration, restart using the same isolated task file and
   compare all task types, order, and status. Modify loaded tasks and reload
   again, including after deleting the final task. Check invalid task numbers
   on an empty list and numbers made invalid by a prior deletion.
6. Earlier reload checks allowed a later mutation to save an earlier change,
   masking a missing save in the earlier command. When moving persistence
   into command classes, restart immediately after each distinct mutation
   (add, mark, unmark, delete) and compare the loaded state before any further
   mutation can save it.
7. Deadline representation changes must be checked through both input and
   storage loading. Include a valid leap day, an invalid leap day, an invalid
   month/day, a wrong format, and a reload of the saved ISO date. For legacy
   invalid dates, verify that a save preserves an exact backup of the original
   file. Do not test only new commands: old saved data uses a separate path.
8. The runner previously recognized only an exact `bye` when checking process
   termination. When surrounding command whitespace is accepted, send the
   original padded input unchanged and check termination for padded `bye` too.
   Cover leading-only, trailing-only, and combined padding, plus preserved
   internal description spaces and rejected extra arguments.
9. A previous audit passed source tests while an older packaged JAR lacked
   features in the User Guide. When checking a completed JAR or documenting a
   release, test that exact artifact using the runner's `--jar` option, record
   its path and hash, and do not rebuild it before testing. Locate the newly
   created artifact rather than assuming the previous output directory.
10. A previous JAR smoke check sent all commands before inspecting responses.
    Use the same per-command comparisons for JAR tests as for source tests:
    compare each response before sending the next command, and stop at the
    first mismatch. Do not pipe an entire session into the app unchecked.

## Repeatable Runner

Run `python test/run-ui-tests.py <session-name>` with Java 25 available on PATH.
The runner reads this plan, compares console responses, and stops at the first
failure. It uses an isolated temporary data directory and writes the transcript
to `_temp/<session-name>/console-record.txt`. Keep the runner in version control:
earlier verification scripts were only in ignored `_temp`, preventing a fresh
checkout from reproducing the same automatic comparisons.

To test an existing JAR, append `--jar <path-to-jar>` to the runner command.
This copies the JAR into the isolated directory and uses `java -jar ip.jar`
without compiling or changing the original artifact.

## Optional Mutation Check

Use a mutation check only when the user requests or authorises an assessment of
test effectiveness.

1. Record the clean or pre-existing working-tree diff before editing.
2. Make one minimal, targeted temporary production-code change representing a
   plausible defect in behaviour covered by the test plan.
3. Run the relevant UI tests and require them to fail for the intended reason.
4. Stop immediately if they do not fail; report the surviving mutation as a
   test gap and improve the test plan before trying again.
5. Restore only the exact temporary change, preserving all pre-existing user
   edits.
6. Rerun the relevant UI tests against the restored code and inspect the diff to
   verify that no mutation remains.
7. Never commit, tag, or leave a temporary defect in the working tree.

## Failure Handling

A test failure must immediately stop the test session.

The failure report must contain:

### Failed Test

The test case that failed.

### Actual Output

The output produced by SamSquare.

### Expected Output

The output specified in `test/ui-test-plan.md`.

Do not hide, skip, or ignore failures.

## Completion Criteria

A code update affecting user-facing behaviour is not considered complete until
the relevant UI tests have been run.

All relevant UI tests must pass before the testing task is considered complete.
