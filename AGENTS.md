# Project context

This repository is a starter template for a greenfield Java project used in an introductory software engineering course in an undergraduate computer science program. Students use it as the starting point for their own projects.

# Default user context

Unless the user says otherwise, assume that you are assisting a student working on a project in this repository. If the user identifies themselves as an instructor or another project stakeholder, adapt your response to that role.

# Student profile

* Prior knowledge: Basic Java and OOP concepts.
* Level of programming experience: [Beginner]
* IDE and level of expertise: [IntelliJ, Beginner]

# Guidance for interacting with users

* Explain the rationale for significant actions: what you did and why.
* Keep explanations brief but instructive, supporting learning through responsible use of AI. For example:

  * When suggesting a Git command, briefly explain what it does.
  * Add explanatory Javadoc comments to all classes and to nontrivial methods and fields when their purpose or behavior is not obvious.
  * Make generated code as self-explanatory as possible, and include explanatory comments where they improve understanding.
  * When faced with a design choice, choose the simplest option that is sufficient for the requirements, while briefly explaining relevant more advanced alternatives.

# Mandatory Java coding standard

All Java code in this project must follow the [SE-EDU Java coding standard (basic + intermediate)](https://se-education.org/guides/conventions/java/intermediate.html).
Use the project-specific skill at `.codex/skills/seedu-java-coding-standard/SKILL.md` as the working checklist whenever Java code is created, edited, reviewed, or refactored. This includes package declarations, naming, layout, explicit imports, braces, field encapsulation, and Javadocs for public APIs.

# Project-specific requirements

## Java version:

Ensure that Java 25 is used when running the application or build tasks. On macOS, use `sdk use java 25.0.3.fx-zulu` to switch to Java 25 if needed.

## Git

Use lightweight tags unless the user requests an annotated tag.
When proposing or creating a commit message, include enough detail to explain the rationale for the change.
Do not commit or push unless explicitly asked.

All future commits in this project must follow the [SE-EDU Git conventions](https://se-education.org/guides/conventions/git.html).
Use `.codex/skills/seedu-git-standard/SKILL.md` as the mandatory checklist when creating commit messages or branch names. Commit subjects must be imperative, capitalized, period-free, and no longer than 72 characters; non-trivial commits must include a 72-column body explaining what and why.

# Mandatory UI testing workflow

After each code update:

1. Update `test/ui-test-plan.md` if the user-visible behavior or test coverage changed.
2. Invoke the project-specific `$test-ui` skill to run the listed UI tests.

The `$test-ui` skill must show each test's console input and output and stop at the first failed test, reporting the expected and actual output.
