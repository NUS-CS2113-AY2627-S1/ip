---
name: seedu-java-coding-standard
description: Apply and review the se-edu basic and intermediate Java coding standard in this repository. Use whenever creating, editing, refactoring, or reviewing Java code, and when diagnosing Java style or naming issues.
---

# se-edu Java Coding Standard

Apply the se-edu basic and intermediate Java conventions to all Java code in this repository.

Before changing or reviewing Java code, read [references/java-coding-standard.md](references/java-coding-standard.md). Treat its linked se-edu page as authoritative when an edge case is not covered by the summary. Follow more specific assignment requirements when they conflict, and report the exception.

## Workflow

1. Inspect the Java files in scope before editing. For a repository-wide compliance request, inspect every Java file.
2. Apply the naming, layout, statement, and documentation rules in the reference. Keep behavioral changes separate from style corrections unless the user requests both.
3. Preserve surrounding code that already complies; do not reformat unrelated files merely to create churn.
4. Check changed Java files for tabs and lines longer than 120 characters. Compile and run the relevant tests with Java 25.
5. Report any rule that could not be followed and explain the conflicting requirement.
