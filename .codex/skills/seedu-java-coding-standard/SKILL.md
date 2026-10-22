---
name: seedu-java-coding-standard
description: Apply the SE-EDU basic and intermediate Java coding conventions to all Java code in this project.
---

# Seedu Java Coding Standard

Apply this skill whenever you write, modify, or review Java code in this project. The
authoritative reference is the [SE-EDU Java coding standard](https://se-education.org/guides/conventions/java/intermediate.html).
Use the Google Java Style Guide for topics not covered by that reference.

## Required conventions

- Put every class in a lower-case package; keep class and enum names in PascalCase.
- Use camelCase for variables and methods, SCREAMING_SNAKE_CASE for constants, and
  names that make boolean values read naturally (`isDone`, `hasData`, and similar).
- Use plural names for collections and descriptive names for variables with wider scope.
- Use four spaces for indentation, K&R braces, spaces around operators and after commas,
  and braces around every loop and conditional body.
- Keep source lines at or below 120 characters. Wrap long expressions after commas or
  before operators, using eight spaces of continuation indentation.
- Keep imports explicit and consistently ordered. Attach array brackets to the type.
- Initialize variables at declaration when a valid initial value is available and keep
  declarations in the smallest practical scope.
- Keep fields private unless there is a strong reason otherwise; expose behavior through
  methods rather than public mutable state.
- Add descriptive Javadocs to public classes and public methods. Getters, setters, and
  exact overrides may omit redundant documentation. Write comments in English using
  American spelling.

## Workflow

Preserve behavior while applying the conventions. Before finishing, inspect all changed
Java files, compile and run the relevant tests with Java 25, and check for line-length,
naming, visibility, import, brace, and documentation violations. Intentional fixed-width
output such as ASCII-art banners may remain visually long when wrapping it would alter the
program's output.
