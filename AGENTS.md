# Project context

This repository is a starter template for a greenfield Java project used in an introductory software engineering course in an undergraduate computer science program. Students use it as the starting point for their own projects.

# Default user context

Unless the user says otherwise, assume that you are assisting a student working on a project in this repository. If the user identifies themselves as an instructor or another project stakeholder, adapt your response to that role.

# Student profile

* Prior knowledge: Basic Java and OOP concepts.
* Level of programming experience: Basic, has taken Data Structures and Algorithms and Intro to C
* IDE and level of expertise: Minimal, has used Visual Studio on occasion

# Guidance for interacting with users

* Explain the rationale for significant actions: what you did and why.
* Keep explanations brief but instructive, supporting learning through responsible use of AI. For example:

  * When suggesting a Git command, briefly explain what it does.
  * Add explanatory Javadoc comments to all classes and to nontrivial methods and fields when their purpose or behavior is not obvious.
  * Make generated code as self-explanatory as possible, and include explanatory comments where they improve understanding.
  * When faced with a design choice, choose the simplest option that is sufficient for the requirements, while briefly explaining relevant more advanced alternatives.

# Project-specific requirements

## Java coding standard

All Java code in this project must follow the project-specific
`.codex/skills/seedu-java-coding-standard/SKILL.md`, which is based on the
[SE-EDU Java coding standard (basic + intermediate)](https://se-education.org/guides/conventions/java/intermediate.html).
Apply it when creating, modifying, or reviewing Java code. In particular,
keep classes in the `aigis` package, use the required naming and formatting
conventions, add Javadocs for public APIs, and verify code with Java 25.

## Java version:

Ensure that Java 25 is used when running the application or build tasks. On macOS, use `sdk use java 25.0.3.fx-zulu` to switch to Java 25 if needed.

## Git

All future commits and branch names in this project must follow the
project-specific `.codex/skills/seedu-git-standard/SKILL.md`, which is based on
the [SE-EDU Git conventions](https://se-education.org/guides/conventions/git.html).
Use imperative, capitalized, non-period commit subjects; keep subjects within
72 characters; add a WHAT/WHY body for non-trivial commits wrapped at 72
characters; and use meaningful kebab-case branch names. This standard does not
override the requirement below that commits and pushes need explicit user
permission.

Use lightweight tags unless the user requests an annotated tag.
When proposing or creating a commit message, include enough detail to explain the rationale for the change.
Do not commit or push unless explicitly asked.
