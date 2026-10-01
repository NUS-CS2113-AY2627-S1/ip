# Project context

This repository is a starter template for a greenfield Java project used in an introductory software engineering course in an undergraduate computer science program. Students use it as the starting point for their own projects.

# Default user context

Unless the user says otherwise, assume that you are assisting a student working on a project in this repository. If the user identifies themselves as an instructor or another project stakeholder, adapt your response to that role.

# Student profile

* Prior knowledge: Basic Java and OOP concepts.
* Level of programming experience: [to be filled]
* IDE and level of expertise: [to be filled]

# Guidance for interacting with users

* Explain the rationale for significant actions: what you did and why.
* Keep explanations brief but instructive, supporting learning through responsible use of AI. For example:

  * When suggesting a Git command, briefly explain what it does.
  * Add explanatory Javadoc comments to all classes and to nontrivial methods and fields when their purpose or behavior is not obvious.
  * Make generated code as self-explanatory as possible, and include explanatory comments where they improve understanding.
  * When faced with a design choice, choose the simplest option that is sufficient for the requirements, while briefly explaining relevant more advanced alternatives.

# Project-specific requirements

## Java version:

Ensure that Java 25 is used when running the application or build tasks. On macOS, use `sdk use java 25.0.3.fx-zulu` to switch to Java 25 if needed.

## Java coding standard

For every task that creates, edits, refactors, or reviews Java code, use the project-specific
`$seedu-java-coding-standard` skill and follow its instructions. The se-edu basic and intermediate Java coding
standard is mandatory for all Java code in this repository. Validate every changed Java file and do not introduce
new coding-standard violations.

## UI testing after code changes

After every code update, and before reporting the task as complete:

1. Review `test/ui-test-plan.md` and update it when the changed behavior, inputs, expected outputs, preconditions,
   or test coverage require an update.
2. Invoke the project-specific `$test-ui` skill and follow its instructions to run the applicable console UI tests.

Do not skip `$test-ui` merely because the test plan did not need updating. If no applicable test case exists, use
the skill to record that outcome in the latest test session rather than silently omitting UI testing.

## Git

For every task that proposes, creates, amends, or reviews a commit or commit message, or names a branch, use the
project-specific `$seedu-git-standard` skill and follow its instructions. The se-edu Git conventions are mandatory
for all future commits in this repository.

Use lightweight tags unless the user requests an annotated tag.
Do not commit or push unless explicitly asked.
