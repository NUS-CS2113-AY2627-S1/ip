---
name: seedu-java-coding-standard
description: Enforce the SE-EDU basic and intermediate Java coding standard for all Java code in this project, including new classes, refactors, reviews, and bug fixes. Use when creating, editing, or reviewing .java files in this repository.
---

# SE EDU Java Coding Standard

Apply this skill to every Java change in this project. Use the project reference at [references/intermediate-rules.md](references/intermediate-rules.md) for the checklist, and use the [SE-EDU standard](https://se-education.org/guides/conventions/java/intermediate.html) as the source of truth.

## Required workflow

1. Inspect the affected Java files and identify violations before editing.
2. Apply the naming, layout, statement, visibility, and documentation rules from the reference.
3. Preserve behavior unless the user requests a behavior change.
4. Compile and run relevant tests using Java 25.
5. Run `git diff --check` before reporting completion.

## Non-negotiable rules

- Put every class in a named lowercase package that matches its source directory.
- Use PascalCase nouns for classes, camelCase for variables and methods, and SCREAMING_SNAKE_CASE for constants.
- Use four spaces for indentation, K&R braces, explicit imports, and lines no longer than 120 characters.
- Initialize variables at declaration where practical and keep them in the smallest possible scope.
- Keep class fields private unless a stronger visibility is clearly justified; expose behavior through methods.
- Use braces for every loop and conditional body.
- Write descriptive English Javadocs for public classes and public methods.
- Keep comments concise, accurate, and in American English.
