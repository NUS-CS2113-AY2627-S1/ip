---
name: seedu-java-coding-standard
description: Follow the SE-EDU intermediate Java coding standard when creating, reviewing, or editing Java code in this project.
---

# SE-EDU Java Coding Standard

Follow the SE-EDU intermediate Java coding standard for all Java code in this project.
Use this skill whenever you create, edit, review, or suggest Java code here.

Source: https://se-education.org/guides/conventions/java/intermediate.html

## Required Style

- Put every class in a package.
- Use explicit imports; do not use wildcard imports.
- Use K&R brace style.
- Use 4 spaces for indentation. Do not use tabs.
- Keep line length reasonable, with 110 characters as the soft limit.
- Use lowerCamelCase for variables, methods, and parameters.
- Use UpperCamelCase for classes, interfaces, enums, and annotations.
- Use SCREAMING_SNAKE_CASE for constants.
- Name boolean variables and methods so they read naturally, such as `isDone`, `hasTasks`, or `canExit`.
- Prefer private fields unless a wider access level is needed.
- Avoid dead code, commented-out old code, unnecessary comments, and magic numbers.

## Documentation

- Add Javadoc comments to public classes.
- Add Javadoc comments to nontrivial public methods and constructors.
- Simple getters and setters do not need Javadocs when their meaning is obvious.
- Write comments to explain why code exists or how non-obvious logic works, not to restate simple statements.

## Formatting Checks

Before finishing Java edits, inspect the changed code for:

- package declaration before imports;
- blank line between package, imports, class Javadoc, and class declaration;
- no wildcard imports;
- no stale commented-out code;
- no unnecessary access widening;
- readable wrapping for long lines.
