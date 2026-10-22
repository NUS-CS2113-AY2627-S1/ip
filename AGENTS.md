# Project context

This repository is a starter template for a greenfield Java project used in an introductory software engineering course in an undergraduate computer science program. Students use it as the starting point for their own projects.

# Default user context

Unless the user says otherwise, assume that you are assisting a student working on a project in this repository. If the user identifies themselves as an instructor or another project stakeholder, adapt your response to that role.

# Student profile

* Prior knowledge: Basic Java and OOP concepts.
* Level of programming experience: Intermediate
* IDE and level of expertise: IntelliJ, Beginner

# Guidance for interacting with users

* Explain the rationale for significant actions: what you did and why.
* Keep explanations brief but instructive, supporting learning through responsible use of AI. For example:

  * When suggesting a Git command, briefly explain what it does.
  * Add explanatory Javadoc comments to all classes and to nontrivial methods and fields when their purpose or behavior is not obvious.
  * Make generated code as self-explanatory as possible, and include explanatory comments where they improve understanding.
  * When faced with a design choice, choose the simplest option that is sufficient for the requirements, while briefly explaining relevant more advanced alternatives.

# Project-specific requirements

## Java coding standard:

All Java code in this project MUST follow the project-specific
`seedu-java-coding-standard` skill, based on the
[SE-EDU intermediate Java coding standard](https://se-education.org/guides/conventions/java/intermediate.html).
This includes package declarations, encapsulation, explicit imports, 4-space indentation,
K&R braces, a maximum line length of 120 characters, and Javadoc for public classes and methods.

## Java version:

Ensure that Java 25 is used when running the application or build tasks. On macOS, use `sdk use java 25.0.3.fx-zulu` to switch to Java 25 if needed.

## Git

All future commits and branch names in this project MUST follow the project-specific
`seedu-git-standard` skill, based on the
[SE-EDU Git conventions](https://se-education.org/guides/conventions/git.html).
Commit subjects must be imperative, capitalized, free of a trailing period, and at most
72 characters. Non-trivial commits must include a blank-line-separated body wrapped at
72 characters explaining what changed and why. Branch names must be meaningful kebab-case.

Use lightweight tags unless the user requests an annotated tag.
When proposing or creating a commit message, include enough detail to explain the rationale for the change.
Do not commit or push unless explicitly asked.
