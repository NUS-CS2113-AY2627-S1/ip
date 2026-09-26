---
name: seedu-java-coding-standard
description: Apply the SE-EDU basic and intermediate Java coding conventions to all Java code in this project.
---

# SE-EDU Java Coding Standard

Apply these rules to all Java code created or modified in this repository.
The authoritative reference is the [SE-EDU Java coding standard](https://se-education.org/guides/conventions/java/intermediate.html).
Use the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
for topics not covered by the SE-EDU guide.

## Required conventions

- Put every class in a meaningful lower-case package.
- Use PascalCase for classes, camelCase for variables and methods, and
  SCREAMING_SNAKE_CASE for constants.
- Name methods as verbs and boolean variables or methods so they read like
  boolean expressions.
- Use four spaces for indentation and keep lines within 120 characters.
- Use K&R braces, spaces around operators, and braces for every conditional
  and loop body.
- Keep imports explicit and consistently ordered. Do not use wildcard imports.
- Attach array brackets to the type, such as `String[] names`.
- Declare and initialize variables in the smallest practical scope.
- Keep class fields private unless a stronger reason justifies wider access.
- Add descriptive Javadocs to public classes and public methods. Getters,
  setters, and exact overrides may omit redundant Javadocs.
- Use `@Override` when overriding a superclass or interface method.
- Add an explicit `// Fallthrough` comment for intentional switch fallthrough.

## Review checklist

Before completing a Java change, inspect the modified files for naming,
layout, package/import, encapsulation, conditional/loop, and Javadoc issues.
Preserve existing behavior unless the user asks for a functional change.
