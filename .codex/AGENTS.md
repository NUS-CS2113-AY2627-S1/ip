# Project agent instructions

All Java code in this project must follow `.codex/skills/seedu-java-coding-standard/SKILL.md`, based on the SE-EDU basic + intermediate Java coding standard.

Apply the standard when adding, editing, reviewing, or refactoring Java code. Preserve behavior while correcting violations, and run the available build or tests after changes.

## Code-update testing workflow

After every code update, review `test/ui-test-plan.md` and update it if the change affects UI behavior, then invoke the project-specific `test-ui` skill to run the UI test cases. Stop at the first failure and show the console input and output.
