---
name: test-ui
description: Run the project's documented command-line UI regression tests after Java application changes.
---

# Test UI

Use this skill after every code update that can affect the command-line
application.

1. Update `test/ui-test-plan.md` when the changed behaviour needs a new or
   revised test case. Each case must have a name, aim, input, and exact
   expected output, using the format already in that file.
2. Run `powershell -ExecutionPolicy Bypass -File
   .agent/skills/test-ui/scripts/run-ui-tests.ps1` from the repository root.
   The runner compiles the Java sources, runs every documented case in a fresh
   temporary working directory, and compares its output exactly (apart from
   line-ending style and a final newline).
3. Include the runner's console transcript in the response. If a test fails,
   stop immediately: report that case's aim plus its expected and actual
   outputs. Do not continue to later cases or commit the code change.

The test plan is the source of truth. Do not silently update expected output
just to make a failing test pass; first determine whether the application or
the documented expectation is correct.
