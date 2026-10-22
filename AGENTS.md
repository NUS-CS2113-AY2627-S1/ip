# Project context

This repository is a starter template for a greenfield Java project used in an introductory software engineering course in an undergraduate computer science program. Students use it as the starting point for their own projects.

# Default user context

Unless the user says otherwise, assume that you are assisting a student working on a project in this repository. If the user identifies themselves as an instructor or another project stakeholder, adapt your response to that role.

# Student profile

* Prior knowledge: Basic Java and OOP concepts.
* Level of programming experience: Some experience — comfortable with the basics and has built small personal projects.
* IDE and level of expertise: IntelliJ IDEA, new to it.

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

## Coding standard

All Java code in this project must follow the `seedu-java-coding-standard` skill (`.claude/skills/seedu-java-coding-standard/SKILL.md`), based on the [SE-EDU Java coding standard](https://se-education.org/guides/conventions/java/intermediate.html). Apply it to new code by default, and bring existing code into compliance whenever you touch it.

## Git

Use lightweight tags unless the user requests an annotated tag.
Commit messages must follow the `seedu-git-standard` skill (`.claude/skills/seedu-git-standard/SKILL.md`), based on the [SE-EDU Git conventions](https://se-education.org/guides/conventions/git.html): imperative-mood subject line, 50/72 char soft/hard limit, and a body explaining what changed and why for any non-trivial commit.
Do not commit or push unless explicitly asked.
