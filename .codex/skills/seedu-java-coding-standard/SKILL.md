---
name: seedu-java-coding-standard
description: Apply the SE-EDU Java coding standard for this project's Java code.
---

# SE-EDU Java Coding Standard

Follow the SE-EDU Java coding standard, basic plus intermediate rules:
https://se-education.org/guides/conventions/java/intermediate.html

When editing Java code in this project, apply these rules unless a course
increment explicitly requires otherwise:

- Use the Google Java Style Guide for topics not covered by SE-EDU.
- Put every class in a package that matches its source path.
- Use explicit imports only; do not use wildcard imports.
- Use PascalCase for classes, camelCase for methods and variables, and
  SCREAMING_SNAKE_CASE for constants.
- Name booleans so they read as booleans, such as `isDone` or `hasData`.
- Use plural names for collections or arrays, such as `tasks`.
- Use 4 spaces for indentation and no tabs.
- Keep lines no longer than 120 characters, with 110 characters as a soft
  target.
- Use K&R braces: opening braces stay on the same line.
- Put spaces around operators and after Java reserved words.
- Always use braces for loops and conditionals, even for single statements.
- Declare variables in the smallest reasonable scope and initialize them where
  they are declared.
- Keep class fields non-public unless they are constants or true data-class
  fields with no behavior.
- Write descriptive Javadocs for all public classes and public methods, except
  simple getters/setters, inherited overrides whose parent Javadoc applies, and
  test methods.
- When a documented method lists `@param` or `@throws` tags, keep the tags in
  sync with the actual method signature.
- Keep imports explicit and grouped consistently: Java standard library imports
  first, then a blank line before project imports.
- Write comments in English using American spelling, and add comments only when
  they clarify purpose or non-obvious behavior.

Before finishing Java edits, inspect the changed Java files for these points and
run the available compile or test command for the project.
