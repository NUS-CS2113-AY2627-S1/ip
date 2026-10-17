---
name: seedu-java-coding-standard
description: Review, write, and refactor Java code to comply with the SE-EDU basic and intermediate Java coding standard. Use whenever Java source is created, modified, formatted, or reviewed in this project. Do not use it for Git commit-message conventions.
---

# SE-EDU Java Coding Standard

Apply the official [SE-EDU Java coding standard](https://se-education.org/guides/conventions/java/intermediate.html) to Java work in this repository.

Before writing or reviewing Java, read [references/java-standard.md](references/java-standard.md) completely. It is the project checklist for the basic and intermediate rules. For topics the SE-EDU guide does not cover, follow the Google Java Style Guide as directed by the official page.

## Workflow

1. Identify the Java files in scope, their packages, and the configured Java version.
2. Review both the requested changes and the surrounding code needed to judge naming, layout, imports, declarations, control flow, and comments.
3. Preserve behavior while correcting standard violations. Keep unrelated design or feature changes out of a coding-standard update.
4. Treat IDE formatting as a helper, then manually verify the checklist, especially continuation indentation, line length, Javadocs, and package placement.
5. Compile with the project's required Java version and run focused behavior checks after changes.
6. Report the files checked, violations corrected, and any unresolved rule conflict.

## Boundaries

- Modify only files and scope authorized by the user.
- Before moving a class into a package, check source layout, build configuration, launch instructions, tests, and documentation; update directly affected references together.
- If project instructions conflict with a general fallback convention, follow the more specific project instruction and state the conflict.
- This skill does not govern commit messages, staging, commits, tags, or pushes.
