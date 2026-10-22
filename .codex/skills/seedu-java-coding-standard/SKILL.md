---
name: seedu-java-coding-standard
description: Apply the SE-EDU basic and intermediate Java coding conventions to Java code in this project.
---

# Seedu Java Coding Standard

Apply this skill whenever creating, modifying, or reviewing Java code in this
repository. It is based on the [SE-EDU Java coding standard (basic +
intermediate)](https://se-education.org/guides/conventions/java/intermediate.html).
Use that page for details not summarized here.

## Required conventions

- Put every class in a lower-case project package. For this project, use the
  `aigis` package and keep source paths aligned with the package.
- Name classes and enums with PascalCase nouns. Name variables and methods in
  camelCase; use verbs for methods. Use lower-case forms for abbreviations and
  acronyms inside names.
- Name boolean variables and methods so they read as boolean values, using
  prefixes such as `is`, `has`, `was`, or `can`. Boolean setters use the form
  `setDone(boolean isDone)`.
- Use plural names for collections and arrays, and use descriptive names for
  variables whose scope is more than a few lines.
- Use four spaces for indentation, K&R braces, spaces around operators and
  after keywords, and braces for every conditional and loop body.
- Keep lines at or below 120 characters, wrapping long expressions at natural
  boundaries with wrapped lines indented by eight spaces.
- Initialize variables at declaration when practical and declare them in the
  smallest scope that needs them. Keep imports explicit, ordered consistently,
  and free of wildcard imports.
- Add descriptive Javadoc to every public class and public method. Getters,
  setters, and methods overriding a documented parent method may use the
  exceptions described by the source standard. Write comments in English with
  American spelling.

## Review checklist

Before handing off Java changes, check package declarations, naming, braces,
indentation, line lengths, import hygiene, variable scope, and public API
Javadocs. Compile and run the relevant behavior using Java 25.
