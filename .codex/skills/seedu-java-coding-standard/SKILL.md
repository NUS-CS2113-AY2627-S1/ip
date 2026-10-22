---
name: seedu-java-coding-standard
description: Apply the SE-EDU basic and intermediate Java coding standard to Java code in this project.
---

# SE-EDU Java Coding Standard

Use this skill for all Java code in this project. Follow the [SE-EDU Java coding standard](https://se-education.org/guides/conventions/java/intermediate.html), which incorporates the basic and intermediate rules. For topics not covered there, use the Google Java Style Guide.

## Required rules

- Put every class in a lower-case project package; use `ip` for this starter project unless an established project package exists.
- Use PascalCase nouns for classes and enums, camelCase verbs for methods, camelCase variables, and `SCREAMING_SNAKE_CASE` constants. Keep acronyms in normal case and use English names.
- Name booleans with prefixes such as `is`, `has`, `was`, or `can`; use plural names for collections.
- Use 4 spaces, K&R braces, spaces around operators and after commas, and blank lines between logical units. Keep lines at or below 120 characters and indent wrapped lines by 8 additional spaces.
- Use explicit, consistently ordered imports; never wildcard imports. Attach array brackets to the type (`String[] args`).
- Initialize variables at declaration when possible and keep them in the smallest scope. Do not expose mutable class fields publicly; prefer private fields and methods for access.
- Always use braces for loops and conditionals, even for one statement. Put conditional bodies on separate lines. Mark intentional switch fall-through with `// Fallthrough`.
- Write English comments using American spelling. Add descriptive Javadoc to all classes and public methods, except getters/setters, exact overrides, and test code. Include `@param`, `@return`, and `@throws` when they add value.

Before finishing Java changes, inspect the diff for these rules and preserve existing behavior unless the task requires otherwise.
