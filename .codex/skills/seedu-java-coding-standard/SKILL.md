---
name: seedu-java-coding-standard
description: Apply and review this project's Java code against the SE-EDU Java coding standard (basic and intermediate rules). Use whenever creating, editing, refactoring, or reviewing Java source or test code in this repository.
---

# SE-EDU Java Coding Standard

Apply the SE-EDU basic and intermediate Java rules to all Java code in this project.

Before changing or reviewing Java, read [references/standard.md](references/standard.md) completely. Treat every rule there as mandatory. For a topic it does not cover, follow the Google Java Style Guide.

## Workflow

1. Inspect every Java file affected by the requested change, including tests.
2. Make the requested behavior change while applying the standard to all touched code.
3. Correct pre-existing violations in the directly affected code when doing so is safe and does not change behavior.
4. Check package paths, imports, names, indentation, line lengths, braces, whitespace, variable scope, and Javadocs before finishing.
5. Compile and run relevant tests with Java 25. Report any violation that cannot be corrected within the user's requested scope.

Preserve behavior when a request is only a style or compliance update. Prefer clear, self-explanatory code; comments should explain purpose or non-obvious reasoning rather than narrate syntax.
