---
name: seedu-java-coding-standard
description: Apply the SE-EDU basic and intermediate Java coding standard when creating, editing, refactoring, or reviewing Java source and test code in this repository. Do not use for non-Java work.
---

# SE-EDU Java Coding Standard

Use the repository's Java conventions consistently without changing behavior merely to satisfy style.

## Required reference

Before writing or reviewing Java, read [references/java-standard.md](references/java-standard.md) completely. It is the project checklist distilled from the SE-EDU basic + intermediate standard.

For a Java topic the reference does not cover, follow the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html), as directed by the SE-EDU standard. Preserve any stricter project requirement stated by the user or repository instructions.

## Working approach

1. Inspect nearby code and relevant repository instructions before editing.
2. Apply the checklist to all new Java code and to lines materially changed by the task.
3. Keep style-only changes focused. Do not reformat unrelated code unless the user asks for a broader cleanup.
4. During a review, identify concrete violations with file and line references. Do not edit unless the user requested fixes.
5. After editing, run the project's formatter, linter, build, or tests when available and relevant. Manually inspect rules that tooling may miss, especially naming, Javadocs, line wrapping, explicit imports, braces, and intentional switch fallthrough comments.

When a necessary behavior or generated-code constraint conflicts with the convention, preserve correctness and explain the exception briefly.
