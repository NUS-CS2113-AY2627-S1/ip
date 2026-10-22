---
name: seedu-java-coding-standard
description: Apply the SE-EDU basic and intermediate Java coding standard when creating, reviewing, or refactoring Java code in this project.
---

# Seedu Java Coding Standard

Use this skill for every Java source change in this repository. Keep the code readable and consistent with the SE-EDU Java coding standard (basic + intermediate rules); use the linked standard as the authority when a detail is unclear:

<https://se-education.org/guides/conventions/java/intermediate.html>

Before finishing a Java change, check the following:

- Every class is in a lower-case project package; classes and enums are PascalCase nouns, methods are camelCase verbs, variables are camelCase, constants are `SCREAMING_SNAKE_CASE`, and boolean names read as predicates (`is...`, `has...`, `can...`, etc.). Use English and avoid all-capitalized abbreviations inside names.
- Use four spaces (never tabs), K&R braces, braces around every loop and conditional body, spaces around operators and after commas, and blank lines between logical units. Keep lines at or below 120 characters (prefer below 110); wrap continuation lines with an additional eight spaces and break at readable boundaries.
- Put imports in a consistent order, list imported classes explicitly, attach array brackets to the type, initialize variables at declaration when practical, and keep variables in the smallest useful scope. Do not expose mutable class fields publicly.
- Add descriptive English/American-English Javadocs to every public class and public method, except getters/setters, applicable overrides, and test code. Start method summaries with an action such as "Returns", "Adds", or "Sends"; include useful `@param`, `@return`, and `@throws` descriptions with punctuation.

For the complete checklist and examples, read [references/conventions.md](references/conventions.md) when reviewing or making a non-trivial change.
