# Project context

This repository is a starter template for a greenfield Java project used in an introductory software engineering course in an undergraduate computer science program. Students use it as the starting point for their own projects.

# Default user context

Unless the user says otherwise, assume that you are assisting a student working on a project in this repository. If the user identifies themselves as an instructor or another project stakeholder, adapt your response to that role.

# Student profile

* Prior knowledge: Basic Java and OOP concepts.
* Level of programming experience: Novice
* IDE and level of expertise: Novice

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

## Git

Use lightweight tags unless the user requests an annotated tag.
When proposing or creating a commit message, include enough detail to explain the rationale for the change.
Do not commit or push unless explicitly asked.
Follow the SE-EDU Git conventions captured in the `seedu-git-standard` skill for all commit messages and branch names.

## Java coding standard

All Java code in this project must follow the SE-EDU intermediate Java coding standard captured in the `seedu-java-coding-standard` skill. Apply it when writing new code and when editing existing code.

## Testing

After every code change, before reporting the change as done:

1. Update `test/ui-test-plan.md` if the change affects Buddy's commands or console output (add, remove, or adjust test cases as needed) — see the `test-ui` skill for the file's format.
2. Invoke the `test-ui` skill to run the UI test plan and confirm the change did not break anything.
