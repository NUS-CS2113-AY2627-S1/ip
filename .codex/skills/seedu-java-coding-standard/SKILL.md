---
name: seedu-java-coding-standard
description: Apply the SE-EDU basic and intermediate Java coding standard to Java code in this project.
---

# SE-EDU Java coding standard

Use this skill for all Java creation, editing, refactoring, and review in this project.

- Use lowercase package names and place every class in a package.
- Use PascalCase nouns for classes/enums, camelCase for variables and verb-based methods, and SCREAMING_SNAKE_CASE for constants.
- Use English/American spelling; use `is`, `has`, `was`, or `can` prefixes for booleans and plural names for collections.
- Use four spaces, never tabs, K&R braces, and a maximum line length of 120 characters (prefer below 110).
- Wrap long lines at readable boundaries with eight-space continuation indentation.
- Use consistent, explicit imports; never wildcard imports.
- Attach array brackets to the type; initialize variables at declaration where practical and use the smallest scope possible.
- Keep fields non-public except constants or simple data classes; preserve encapsulation.
- Always brace loops and conditionals, including one-line bodies; put conditional bodies on separate lines.
- Mark intentional switch fallthrough with `// Fallthrough`.
- Add descriptive Javadocs to public classes and public methods, except getters/setters, exact overrides, and test code.

For topics not covered here, follow the Google Java Style Guide.

Before completing changes, check modified Java files for naming, imports, line length, indentation, braces, visibility, variable scope, and required Javadocs, then run the available Java 25 build/tests.
