---
name: test-ui
description: Run command-driven console UI tests from test/ui-test-plan.md, compare each response with its expected output, stop at the first mismatch, and report a complete input/output transcript. Use for testing this project's interactive text UI.
---

# Test UI

Use `test/ui-test-plan.md` as the source of truth for the program launch details,
shared startup output, and ordered test cases.

## Prepare the test plan

- Accept one or more test cases supplied by the user as lists of console commands
  and expected responses.
- Before running them, add or update those cases in `test/ui-test-plan.md`.
  Preserve unrelated existing cases.
- Every case must state its aim and pair each input with the exact expected output.
  Do not invent an expected output that the user has not supplied unless the user
  explicitly asks for help designing the case.
- Record execution details needed to reproduce the test, including build and run
  commands, required runtime version, whether cases share state, and any permitted
  output normalization.
- If a case lacks an aim, input, expected output, or required execution detail,
  report the missing information instead of running an unverifiable test.

## Run the tests

1. Read the whole test plan before starting. Follow its case order.
2. Verify that both `java` and `javac` use Java 25. Do not silently test with a
   different Java version.
3. Run the build command once. Treat a build or launch error as a failed test
   setup: show the command and diagnostic output, then stop.
4. Start a fresh program process for each test case unless the plan explicitly
   says that cases share a session.
5. Verify the startup output when the plan specifies it. Then send one listed
   console input at a time, wait for its complete response, and compare that
   response with the paired expected output before sending the next input.
6. Compare text exactly after only the normalizations allowed by the plan. By
   default, treat CRLF and LF as equivalent and ignore input echo added by the
   terminal transport. Preserve spaces, blank lines, case, punctuation, and all
   program-produced text.
7. End a successful session cleanly using its listed exit command. If the process
   remains alive after a mismatch or setup error, terminate only that test process.

Run cases sequentially because the required stop-on-first-failure behavior makes
parallel execution unsuitable.

## Report the session

After testing, show a console-style transcript for every session that ran. The
transcript must include the launch command, startup output, every entered input,
and every program response, in observed order. Clearly label input annotations so
they are not mistaken for program output.

For a successful run, report each case as passed and state that all listed output
matched exactly under the plan's normalization rules.

At the first mismatch:

- stop the program immediately and do not run later commands or cases;
- identify the case and input step that failed;
- show the actual and expected output in separate fenced blocks without trimming
  meaningful whitespace;
- include the transcript up to the failure; and
- list the remaining cases as not run.
