---
name: seedu-java-coding-standard
description: Apply the SE-EDU Java coding standard (basic and intermediate rules) when creating, editing, reviewing, or refactoring Java code in this project.
---

# SE-EDU Java Coding Standard

Follow the [SE-EDU Java coding standard (basic + intermediate)](https://se-education.org/guides/conventions/java/intermediate.html) for every Java change in this project. Treat that guide as authoritative. For topics it does not cover, follow the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html), as directed by the SE-EDU guide.

## Apply the standard

Before finishing a Java change, inspect every affected Java file and enforce these rules:

- Use English names. Use PascalCase nouns for classes and enums, camelCase verbs for methods, camelCase for variables, and SCREAMING_SNAKE_CASE for constants.
- Name booleans so they read as booleans, normally with `is`, `has`, `was`, `can`, or `should`. Use plural names for collections and arrays.
- Indent with four spaces and no tabs. Keep lines below 110 characters when practical and never exceed 120 characters.
- Use K&R braces. Put `else` and `catch` on the same line as the preceding closing brace. Always use braces for loops and conditionals, including single-statement bodies.
- Surround operators with spaces, put a space after commas and Java keywords, and separate logical blocks with one blank line.
- Wrap lines for readability: break after commas and before operators; indent continuation lines eight spaces beyond their parent line.
- Put every class in a lowercase package. List imports explicitly and use a consistent import order; never use wildcard imports.
- Attach array brackets to the type. Initialize variables when declared and declare them in the smallest useful scope. Do not expose class variables publicly unless they are constants or belong to a behavior-free data class.
- Write comments in English using American spelling. Indent comments with the code they describe and avoid comments that merely repeat clear code.
- Add descriptive Javadocs to every public class and public method, except self-explanatory getters/setters, exact overrides, and test code. Start with a concise third-person summary, document all parameters or none, and punctuate tag descriptions.

Compile and run relevant tests after formatting. Recheck changed lines for the 120-character hard limit before handing off.
