---
name: seedu-java-coding-standard
description: Apply the SE-EDU Java coding standard for Java code in this project, including naming, layout, statements, imports, packages, and Javadocs.
---

# SE-EDU Java Coding Standard

Use this skill whenever creating, editing, or reviewing Java code in this project. Follow the SE-EDU Java coding standard (basic + intermediate) at https://se-education.org/guides/conventions/java/intermediate.html. For topics not covered there, use the Google Java Style Guide.

## Project Defaults

- Put every Java class in a package. For this project, prefer package names rooted at the project name, such as `chillguy`, with lower-case logical subpackages as the codebase grows.
- Keep imports explicit and consistently ordered. Do not use wildcard imports.
- Use Java 25 when compiling or running project code.

## Naming

- Use PascalCase nouns for classes and enums.
- Use camelCase verbs for methods.
- Use camelCase for variables.
- Use SCREAMING_SNAKE_CASE for constants.
- Use lower-case names for packages.
- Use English names. Acronyms inside names should not be all uppercase, for example `exportHtmlSource`, not `exportHTMLSource`.
- Name booleans so they read as booleans, using prefixes such as `is`, `has`, `was`, `can`, or `should` where practical.
- Use plural names for collections and arrays.
- Use short scratch variables only for small scopes. Reserve `j`, `k`, and later letters for nested loops.

## Layout And Statements

- Indent with 4 spaces, not tabs.
- Keep lines under 120 characters, and try to stay under 110 characters.
- When wrapping lines, use an 8-space continuation indent, break after commas, and break before operators.
- Use K&R braces for classes, methods, loops, conditionals, switch blocks, try-catch, and try-finally.
- Always use braces for loop and conditional bodies, even when the body has one statement.
- Put each conditional body on its own line; do not write single-line `if` bodies.
- Surround operators and ternary colons with spaces. Put a space after Java reserved words, commas, and semicolons in `for` headers.
- Separate logical units within a block with one blank line.
- In `switch` statements, add an explicit `// Fallthrough` comment for any intentional fallthrough.

## Variables And Types

- Attach array specifiers to the type, for example `int[] values`.
- Declare variables in the smallest reasonable scope and initialize them where declared when possible.
- Do not expose class variables as `public` unless the class is a data class with no behavior. This does not apply to constants.

## Javadocs And Comments

- Write comments in English using American spelling and no local slang.
- Write descriptive Javadocs for all classes and public methods, except simple getters/setters, test code, and overrides whose inherited documentation applies exactly.
- For method Javadocs, start the first sentence with an active third-person verb such as `Returns`, `Sends`, `Adds`, `Creates`, or `Marks`.
- Include `@param`, `@return`, and `@throws` tags only when they add useful information. If using `@param`, document all parameters.
- End Javadoc parameter descriptions with punctuation.
- Do not leave a blank line between a Javadoc block and the declaration it documents.
- Indent comments relative to their position in the code.

## Verification

- Before finishing Java edits, scan modified Java files for package declarations, explicit imports, public members needing Javadocs, tabs, overlong lines, missing braces, wildcard imports, and obvious naming violations.
- Compile or run the relevant project checks when available.
