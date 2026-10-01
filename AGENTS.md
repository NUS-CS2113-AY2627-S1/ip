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

All Java code in this project must follow the SE-EDU Java coding standard at the
**intermediate** level: https://se-education.org/guides/conventions/java/intermediate.html

This is mandatory for all new and modified code, not just the code that existed when the
standard was adopted. The points that come up most often in this project:

* Names: PascalCase classes, camelCase methods and variables, SCREAMING_SNAKE_CASE constants.
  Boolean names take an `is`/`has`/`was`/`can`/`should` prefix. Groups of related constants
  share a common *prefix* (`COMMAND_MARK`, `COMMAND_UNMARK`), not a common suffix.
* Layout: 4-space indentation, no tabs, K&R braces, 110-character soft line limit
  (120 hard), wrapped lines indented 8 spaces, break before operators and after commas.
* Braces are required on every `if`, `else`, `for`, and `while`, even single-statement ones.
* No wildcard imports.
* Javadoc on every class and public method, opening with a third-person verb
  ("Returns...", "Prints..."), with `@param` for all parameters or none, each ending in
  a full stop.
* Prefer a named constant over a repeated or unexplained literal.
* Every class belongs to a package. `floppy` is the root package, with the task
  hierarchy in `floppy.task` and the command classes in `floppy.command`.
  `src/main/java` stays the source root.

## Git

Use lightweight tags unless the user requests an annotated tag.
When proposing or creating a commit message, include enough detail to explain the rationale for the change.
Do not commit or push unless explicitly asked.
