# Project context

This repository is a starter template for a greenfield Java project used in an introductory software engineering course in an undergraduate computer science program. Students use it as the starting point for their own projects.

# Default user context

Unless the user says otherwise, assume that you are assisting a student working on a project in this repository. If the user identifies themselves as an instructor or another project stakeholder, adapt your response to that role.

# Student profile

* Prior knowledge: Basic Java and OOP concepts.
* Level of programming experience: Decent
* IDE and level of expertise: Low

# Guidance for interacting with users

* Explain the rationale for significant actions: what you did and why.
* Keep explanations brief but instructive, supporting learning through responsible use of AI. For example:

  * When suggesting a Git command, briefly explain what it does.
  * Add explanatory Javadoc comments to all classes and to nontrivial methods and fields when their purpose or behavior is not obvious.
  * Make generated code as self-explanatory as possible, and include explanatory comments where they improve understanding.
  * When faced with a design choice, choose the simplest option that is sufficient for the requirements, while briefly explaining relevant more advanced alternatives.

# Project-specific requirements

## Coding standard (mandatory)

All Java code in this repository — new code, edits, and refactors — must follow
the SE-EDU Java coding standard (Basic + Intermediate levels), documented at
https://se-education.org/guides/conventions/java/intermediate.html and captured
in the project skill `seedu-java-coding-standard`
(`.claude/skills/seedu-java-coding-standard/SKILL.md`).

Invoke that skill before writing or reviewing Java code, and do not propose a
Java change that violates it. Key points: `PascalCase` class names, `camelCase`
methods/variables, `UPPER_CASE` constants; 4-space indentation and K&R braces;
braces around every loop body and conditional branch; explicit (non-wildcard)
ordered imports; and Javadoc on every public class and non-trivial method.

Project override: comments and Javadoc use **British spelling** (e.g.
"behaviour", "initialise"), not the American spelling the SE-EDU standard
specifies.

## Java version:

Ensure that Java 25 is used when running the application or build tasks. On macOS, use `sdk use java 25.0.3.fx-zulu` to switch to Java 25 if needed.

## Git

Every commit and branch created in this repository must follow the SE-EDU Git
conventions, documented at https://se-education.org/guides/conventions/git.html
and captured in the project skill `seedu-git-standard`
(`.claude/skills/seedu-git-standard/SKILL.md`). Invoke that skill before writing
any commit message or creating a branch.

Key points: subject line in the imperative mood, capitalized, no trailing
period, <= 50 chars (72 hard limit); blank line before the body; body wrapped at
72 chars explaining what and why (not how); one logical change per commit;
branch names in kebab-case.

Use lightweight tags unless the user requests an annotated tag.
When proposing or creating a commit message, include enough detail to explain the rationale for the change.
Do not commit or push unless explicitly asked.
