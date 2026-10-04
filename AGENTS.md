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
  * Make generated code as self-explanatory as possible, and include explanatory comments where they improve understanding.
  * When faced with a design choice, choose the simplest option that is sufficient for the requirements, while briefly explaining relevant more advanced alternatives.

# Project-specific requirements

## Java version:

Ensure that Java 25 is used when running the application or build tasks. On macOS, use `sdk use java 25.0.3.fx-zulu` to switch to Java 25 if needed.

## Java coding standard

All Java code in this project, including production and test code, must follow the project skill at
`.agents/skills/seedu-java-coding-standard/SKILL.md`.

Before creating, editing, refactoring, or reviewing any Java code, load and follow that skill. Treat its rules as
mandatory for the entire Java codebase, using the Google Java Style Guide only for topics the skill does not cover.

## UI regression testing

After every production code change:

1. Review `test/ui-test-plan.md` and update it when the change adds or alters user-visible behavior.
2. Invoke the project skill at `.agents/skills/test-ui/SKILL.md` and run the complete UI test plan.

Preserve the test skill's fail-fast behavior. Do not weaken expected output or change application behavior merely to
make a failing test pass.

## Git

Before creating commits or branches, load and follow the project skill at
`.agents/skills/seedu-git-standard/SKILL.md`.

Use lightweight tags unless the user requests an annotated tag.
When proposing or creating a commit message, include enough detail to explain the rationale for the change.
Do not commit or push unless explicitly asked.
