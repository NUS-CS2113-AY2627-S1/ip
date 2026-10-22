---
name: seedu-java-coding-standard
description: Enforce the SE-EDU Java coding standard (basic + intermediate) for all Java code in this project.
---

# SE-EDU Java coding standard

Follow the rules in https://se-education.org/guides/conventions/java/intermediate.html for every Java file in this repository.

## Required rules

- Use English names only.
- Use PascalCase for class names, camelCase for methods and variables, and UPPER_SNAKE_CASE for constants.
- Use boolean names that read like booleans, such as `isDone`, `hasData`, and `wasOpen`.
- Prefer descriptive names and keep scopes as small as possible.
- Use 4 spaces for indentation, never tabs.
- Use K&R/Egyptian style braces and always wrap loop and conditional bodies in braces.
- Break long lines at sensible points, keeping lines under 120 characters when possible.
- Keep imports explicit, minimal, and ordered consistently.
- Put every class in a package when the project grows beyond a minimal starter template.
- Write Javadoc for public classes and public methods.
- Use clear, consistent whitespace around operators, commas, and colons.
- Separate logical units inside a block with a blank line where helpful.
- Prefer encapsulation and avoid exposing mutable fields directly.

## Project-specific reminder

All code generated or modified in this project must follow the SE-EDU standard even when a shorter solution would work. If a change is ambiguous, prefer the clearer and more readable Option that matches the SE-EDU Java conventions.

## Review checklist

Before finishing any Java change, check:

1. Names match the required style.
2. Braces and indentation match the project standard.
3. Javadoc is present where required.
4. The code is readable and explicit rather than clever.
5. The result remains consistent with the rest of the project.
