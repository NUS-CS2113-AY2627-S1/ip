---
name: seedu-java-coding-standard
description: Apply the SE-EDU basic and intermediate Java coding standard to Java code in this project.
---

# SE-EDU Java coding standard

Use this skill for all Java code in this project. Follow https://se-education.org/guides/conventions/java/intermediate.html and use Google Java Style for uncovered topics.

- Put every class in a lower-case project package; use PascalCase nouns for classes, camelCase verbs for methods and variables, and SCREAMING_SNAKE_CASE for constants.
- Name booleans with prefixes such as `is`, `has`, `can`, or `should`; use plural collection names and English/American spelling.
- Use four-space indentation, K&R braces, spaces around operators and after commas, blank lines between logical units, and lines no longer than 120 characters.
- Keep imports explicit and consistently ordered; attach array brackets to the type; initialize variables where practical and keep scope minimal.
- Keep mutable fields non-public. Always use braces for loops and conditionals, including single-statement bodies.
- Add descriptive Javadocs to public classes and public methods, except getters/setters, exact overrides, and tests. Start summaries with “Returns”, “Adds”, “Sends”, or another action.

Before finishing Java changes, review modified files against these rules and run available checks using Java 25.
