# Project context

This repository is a starter template for a greenfield Java project used in an introductory software engineering course in an undergraduate computer science program. Students use it as the starting point for their own projects.

# Default user context

Unless the user says otherwise, assume that you are assisting a student working on a project in this repository. If the user identifies themselves as an instructor or another project stakeholder, adapt your response to that role.

# Student profile

* Prior knowledge: Basic Java and OOP concepts.
* Level of programming experience: Intermediate (new to OOP)
* IDE and level of expertise: Intermediate

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

For every Java code creation, edit, refactor, or review in this repository, use the project-local `seedu-java-coding-standard` skill at `.codex/skills/seedu-java-coding-standard/SKILL.md` and follow it. This requirement is mandatory for all Java code in the project.

## Testing

After every code update in this repository:

1. Review `test/ui-test-plan.md` and update it when the change affects test aims, inputs, expected outputs, setup, build or launch commands, or other information needed for repeatable UI testing. Leave it unchanged when the existing plan still covers the change accurately.
2. Invoke the project-local `test-ui` skill at `.codex/skills/test-ui/SKILL.md` and follow it to run the relevant UI tests. This requirement is mandatory for every code update, even when the test plan does not need to change.

## Git

For every proposed or created commit message and every branch name in this repository, use the project-local `seedu-git-standard` skill at `.codex/skills/seedu-git-standard/SKILL.md` and follow it. This requirement is mandatory for all future commits.

Use lightweight tags unless the user requests an annotated tag.
Do not commit or push unless explicitly asked.
