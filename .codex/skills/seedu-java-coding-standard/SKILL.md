---
name: seedu-java-coding-standard
description: Apply the SE-EDU basic and intermediate Java coding standard when creating, editing, reviewing, or refactoring Java code in this project.
---

# SE-EDU Java Coding Standard

Apply the [SE-EDU Java coding standard](https://se-education.org/guides/conventions/java/intermediate.html) to every Java file in this project. Preserve the requested behavior while correcting style issues that the standard covers.

## Required conventions

- Put every class in a lower-case package that matches the project structure.
- Use PascalCase nouns for classes, camelCase for variables and verb-based methods, and `SCREAMING_SNAKE_CASE` for constants.
- Use boolean names that read naturally, such as `isDone` or `hasTasks`; use plural names for collections.
- Use four spaces for indentation, K&R braces, explicit imports, and array brackets attached to the type.
- Keep lines at or below 120 characters, preferably below 110. Wrap long lines with continuation indentation of eight spaces beyond the parent line.
- Initialize variables where declared and keep them in the smallest practical scope. Separate logical units with one blank line.
- Use braces for every loop and conditional body, including single-statement bodies. Keep operators, commas, and control-keyword spacing consistent with the guide.
- Keep class fields encapsulated; expose behavior through methods rather than public mutable fields.
- Write English comments using American spelling. Add descriptive Javadocs to public classes and public methods; getters, setters, and correctly overridden methods may omit them.

## Review checklist

Before finishing a Java change, check package declarations, imports, naming, indentation, line length, braces, field visibility, initialization scope, comments, and public API Javadocs. Use the official guide for rules not summarized here.
