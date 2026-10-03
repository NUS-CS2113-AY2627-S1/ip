---
name: test-ui
description: Run command-line UI tests for this Java project from test/ui-test-plan.md, compare actual output with expected output, stop at the first failure, and report the console transcript. Use after every code update and when asked to test Clammy's text UI or verify command/output examples.
---

# Test UI

Use [`test/ui-test-plan.md`](../../../test/ui-test-plan.md) as the test specification and record of relevant test information.

## Prepare the test plan

- After any code update, review the plan before testing. Update it when user-visible behavior or necessary coverage
  has changed; otherwise leave it unchanged and proceed with the existing cases.
- If the user supplies test cases, add or update them in the plan before running them.
- Give every test case a unique name and record its aim, console input, and complete expected console output.
- Preserve spaces, blank lines, punctuation, and command order because console output is compared exactly.
- If required information is missing and cannot be inferred safely, ask for it before testing.

## Run the tests

1. Read `AGENTS.md` and the whole test plan.
2. From the repository root, select Java 25 with `sdk use java 25.0.3.fx-zulu`.
3. Compile all Java source files into the ignored `out/` directory. Treat a compilation failure as a failed test session and report the compiler output.
4. Run each test case in plan order in a fresh Java process so state does not leak between cases.
5. Send that case's input lines to the program in their recorded order.
6. Capture standard output and standard error. Compare the complete actual output with the expected output exactly, including whitespace and line breaks.
7. Stop immediately on the first mismatch or nonzero process exit. Do not run later test cases.

Choose the program's fully qualified main class from its package declaration. For the current project, use `clammy.Clammy` when `Clammy.java` declares `package clammy;`.

## Report the session

For each passing case, show its name and a console transcript that clearly distinguishes input from program output. Do not include shell setup commands in the transcript.

On failure, report:

- the failed test case and aim;
- the input supplied;
- the complete expected output;
- the complete actual output, including standard error when relevant;
- that execution stopped and which cases were not run.

Do not change application code merely to make a test pass unless the user separately asks for a fix.
