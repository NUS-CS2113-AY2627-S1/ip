---
name: seedu-java-coding-standard
description: "Apply the SE-EDU basic and intermediate Java coding standard to Java code in this project."
---

# Seedu Java Coding Standard

Apply these rules whenever creating, editing, reviewing, or refactoring Java code in
this repository. The source standard is the [SE-EDU Java coding standard]
(https://se-education.org/guides/conventions/java/intermediate.html); use the
[Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) for
topics not covered here.

## Naming

- Put packages in lowercase. This project uses `ernest` as its root package;
  place classes in matching directories under `src/main/java`.
- Name classes and enums as PascalCase nouns.
- Name variables and methods in camelCase. Use verbs for method names.
- Name constants in SCREAMING_SNAKE_CASE and give related constants a common
  prefix where appropriate.
- Use English names and American spelling. Do not capitalize abbreviations or
  acronyms within names (`openDvdPlayer`, not `openDVDPlayer`).
- Use boolean names that read as boolean expressions (`isDone`, `hasData`),
  boolean setters of the form `setDone(boolean isDone)`, and plural names for
  collections. Short `i`, `j`, and `k` names are reserved for small iterator
  scopes and nested loops.

## Layout and whitespace

- Use four spaces for indentation, never tabs.
- Keep lines at 120 characters or fewer; prefer fewer than 110. Wrap at
  readable boundaries, indent wrapped lines by eight spaces beyond the parent,
  and keep a method/constructor name attached to its opening parenthesis.
- Use K&R braces for classes, methods, conditionals, loops, `switch`, and
  `try`/`catch` blocks.
- Put spaces around operators, after keywords and commas, and after `for`
  semicolons. Separate logical units in a block with one blank line.
- Use braces even for one-statement loop and conditional bodies. Keep
  single-line conditionals and loop bodies expanded onto separate lines.
- Put `case` labels and statements in the standard `switch` layout. Add an
  explicit `// Fallthrough` comment whenever a case intentionally lacks a
  `break`.

## Statements and declarations

- Put every class in a package.
- Order imports consistently and list imported classes explicitly; never use
  wildcard imports. Keep imports minimal and place static imports first when
  present.
- Attach array brackets to the type (`int[] values`).
- Initialize variables where declared whenever a valid value is available, and
  declare them in the smallest possible scope.
- Keep class fields non-public to preserve encapsulation, except for constants
  and intentionally behavior-free data classes.

## Documentation

- Write comments in English using American spelling and no local slang.
- Add a descriptive Javadoc header to every public class and public method.
  Getters/setters, test code, and overriding methods whose inherited Javadoc
  applies exactly are exceptions.
- Start a method summary with an action such as `Returns`, `Sends`, or `Adds`.
  Put a blank line before `@param`, `@return`, and `@throws` tags; end tag
  descriptions with punctuation. Keep `/**` on its own line, align `*`, and
  place no blank line between the Javadoc and its declaration.
- Indent comments with the code they describe.

Before finishing a Java change, check package placement, names, imports,
braces, whitespace, line lengths, variable scope, visibility, and required
Javadocs. Preserve behavior unless the requested change requires otherwise.
