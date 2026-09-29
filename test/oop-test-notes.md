# A-MoreOOP regression verification

- Runtime and compiler: Java 25.0.4.
- Command: `python test/run-ui-tests.py`, with PowerShell environment variable
  `GRADLE_USER_HOME` set to `C:\Users\zepht\.gradle` for the existing cache.
- Before the refactor: all 38 recorded cases passed.
- After the refactor: all 38 recorded cases passed on the final run, including the fat JAR,
  command validation, task types, list growth, restart persistence, corrupt files, and save rollback.
- The complete final inputs, actual outputs, exit codes, stderr, and storage checks are in
  [ui-test-transcript.md](ui-test-transcript.md). Expected outputs were not changed.

## Earlier stopped run

The first post-refactor run stopped at **Grow Task List Beyond 100 Tasks**, after nine passing cases.
The command was `java -cp out chillguy.Chillguy` in a disposable test directory; the runner added
UTF-8 output flags. Input is recorded in [capacity.input.txt](fixtures/capacity.input.txt), and
the complete expected output is in [capacity.expected.txt](fixtures/capacity.expected.txt).

For `todo task 100`, the expected response was:

```text
Got it. I've added this task:
  [T][ ] task 100
Now you have 100 tasks in the list.
```

The actual response was:

```text
Sorry, I couldn't save tasks to data/chillguy.txt. No changes were made. Check that the data folder is writable and the file is not in use.
```

The task was rolled back, so subsequent task counts and indices differed from the expected output.
Exit code was 0, stderr was empty, and no temporary save files remained.
The full suite passed when rerun without code changes. The file-system failure did not recur;
its underlying cause was not established. The pre-existing committed transcript also records a
save failure during an unrelated task-deletion case, before this refactor.

The initial baseline attempt also stopped before test execution because Gradle tried to create a
cache lock under `C:\.gradle`. Using the existing user cache resolved that build setup failure.

## Source checks

The new and modified Java sources have package declarations, explicit imports, class and public
method documentation, no tabs, and no lines over 120 characters. The only whitespace warnings
in the final diff are intentional trailing spaces in the transcript's invalid-input test cases.
