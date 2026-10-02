---
name: seedu-java-coding-standard
description: Apply the SE-EDU basic and intermediate Java coding standard when writing, editing, refactoring, or reviewing Java code in this project.
---

# SE-EDU Java Coding Standard

Follow the current [SE-EDU Java coding standard](https://se-education.org/guides/conventions/java/intermediate.html). Use Google Java Style only for topics the SE-EDU guide does not cover.

Apply these project-relevant rules:

- Put every class in a package and keep the directory structure consistent with the package.
- Use nouns in PascalCase for classes, verbs in camelCase for methods, camelCase for variables, and SCREAMING_SNAKE_CASE for constants.
- Give boolean variables and methods names that read as booleans, such as `isDone` or `hasTasks`.
- Use plural names for collections and arrays.
- Indent with 4 spaces. Keep lines below 120 characters, preferably below 110, and indent wrapped lines by 8 additional spaces.
- Use K&R braces, spaces around operators, spaces after commas, and braces for every loop and conditional body.
- Separate logical blocks with blank lines.
- Use explicit imports rather than wildcard imports, and keep import ordering consistent.
- Attach array brackets to the type. Initialize variables at declaration and declare them in the smallest useful scope.
- Keep fields non-public except constants.
- Write English comments using American spelling. Use comments to explain intent, not obvious mechanics.
- Write descriptive Javadocs for every public class and public method, except getters, setters, overrides whose inherited documentation applies, and test code. Use a multi-line Javadoc form for public APIs.

Before finishing, compile or test the affected Java code and review changed lines for these rules.
