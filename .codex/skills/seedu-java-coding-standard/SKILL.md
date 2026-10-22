---
name: seedu-java-coding-standard
description: Apply the SE-EDU Java coding standard for this project, including naming, layout, imports, packages, comments, and simple readable Java style.
---

# SE-EDU Java Coding Standard

Use this skill when writing, editing, or reviewing Java code in this project.
Base decisions on the SE-EDU Java coding standard:
https://se-education.org/guides/conventions/java/intermediate.html

For topics not covered by the SE-EDU guide, follow the Google Java Style Guide.

## Core Rules

- Put every Java class in a package. Use a lowercase root package based on the project name, such as `gpt`.
- Use PascalCase for class, enum, and interface names.
- Treat acronyms as ordinary words in identifiers, such as `Gpt`, not `GPT`.
- Use camelCase for variables and methods.
- Use names that communicate purpose. Collection variables should normally be plural.
- Use SCREAMING_SNAKE_CASE for constants.
- Use explicit imports. Do not use wildcard imports.
- Use 4 spaces for indentation and K&R braces.
- Keep lines under 120 characters, and prefer under 110 characters when practical.
- Put spaces around operators, after commas, and after Java reserved words.
- Use braces for all loop and conditional bodies, even single-line bodies.
- Declare variables in the smallest reasonable scope and initialize them where declared.
- Keep fields private unless there is a strong reason otherwise.

## Comments And Javadocs

- Write comments and Javadocs in English, using American spelling and avoiding local slang.
- Add Javadoc header comments for all public classes and public methods, except simple getters/setters, exact overrides, and test methods.
- The first Javadoc sentence should be a short summary that starts naturally, such as `Returns ...`, `Adds ...`, or `Marks ...`.
- Add `@param`, `@return`, and `@throws` only when they add useful information.
- Use inline comments sparingly, when they explain intent that is not obvious from the code.

## Review Focus

When reviewing Java changes, check package placement, naming, imports, layout, comments/Javadocs, visibility, line length, and whether the simplest sufficient design was used.
