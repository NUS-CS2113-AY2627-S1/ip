# Project context

This repository is a starter template for a greenfield Java project used in an introductory software engineering course in an undergraduate computer science program. Students use it as the starting point for their own projects.

# Default user context

Unless the user says otherwise, assume that you are assisting a student working on a project in this repository. If the user identifies themselves as an instructor or another project stakeholder, adapt your response to that role.

# Student profile

* Prior knowledge: Basic Java and OOP concepts.
* Level of programming experience: not very experienced, only started programming in NUS
* IDE and level of expertise: used VS code before but not a lot

# Guidance for interacting with users

* Explain the rationale for significant actions: what you did and why.
* Keep explanations brief but instructive, supporting learning through responsible use of AI. For example:

  * When suggesting a Git command, briefly explain what it does.
  * Add explanatory Javadoc comments to all classes and to nontrivial methods and fields when their purpose or behavior is not obvious.
  * Make generated code as self-explanatory as possible, and include explanatory comments where they improve understanding.
  * When faced with a design choice, choose the simplest option that is sufficient for the requirements, while briefly explaining relevant more advanced alternatives.

# Project-specific requirements

## Java coding standard

All Java code in this project MUST follow the project-local
[seedu-java-coding-standard](.agents/skills/seedu-java-coding-standard/SKILL.md)
skill, which is based on the [SE-EDU Java coding standard (basic + intermediate)](https://se-education.org/guides/conventions/java/intermediate.html).
Apply it to new code, edits, and refactors; preserve existing behavior unless the
user requests otherwise. In particular, every class must be in a lowercase package,
imports must be explicit and consistently ordered, braces are required for all loop
and conditional bodies, lines must not exceed 120 characters, and public classes and
methods require descriptive Javadoc subject to the standard's stated exceptions.

## Post-code-update UI verification

After every code update, invoke the project-local
[test-ui](.agents/skills/test-ui/SKILL.md) skill. Before invoking it, update
[test/ui-test-plan.md](test/ui-test-plan.md) when the change adds or changes
observable UI behavior, commands, inputs, or expected output. If the code change
does not affect the UI, the plan may remain unchanged, but the `test-ui` skill must
still be invoked. Follow the skill's fail-fast behavior and retain the console
input/output record in the response.

When updating expected console transcripts, copy the runner's output exactly,
including leading spaces on separators and response lines. Use the test runner's
record as the source of truth instead of manually reformatting transcript lines.

## Java version:

Ensure that Java 25 is used when running the application or build tasks. On macOS, use `sdk use java 25.0.3.fx-zulu` to switch to Java 25 if needed.

## Git coding standard

All future commits in this project MUST follow the project-local
[seedu-git-standard](.agents/skills/seedu-git-standard/SKILL.md) skill, which is based
on the [SE-EDU Git conventions](https://se-education.org/guides/conventions/git.html).
Before committing, use an imperative, capitalized, period-free subject; keep it to
50 characters where possible and never over 72 characters. Add a focused, 72-column
body explaining WHAT and WHY for non-trivial commits. Use meaningful kebab-case branch
names. Do not commit or push unless explicitly asked.

## Git

Use lightweight tags unless the user requests an annotated tag.
When proposing or creating a commit message, include enough detail to explain the rationale for the change.
Do not commit or push unless explicitly asked.
