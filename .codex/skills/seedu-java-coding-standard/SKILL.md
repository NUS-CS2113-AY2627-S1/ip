---
name: seedu-java-coding-standard
description: Apply the SE-EDU basic and intermediate Java coding standard when creating, editing, or reviewing Java code in this project.
---

# SE-EDU Java Coding Standard

Use this skill for every Java code change or Java code review in this repository.

Follow the rules in [the project reference](references/intermediate.md). They
are a focused, local transcription of the [SE-EDU Java coding standard]
(https://se-education.org/guides/conventions/java/intermediate.html), which
is authoritative when a detail is unclear.

Before completing a Java change, check that:

- every class is in a lowercase, project-appropriate package;
- names, declarations, imports, braces, wrapping, and whitespace follow the
  convention;
- public classes and public methods have descriptive English Javadoc unless a
  documented exception applies; and
- fields preserve encapsulation and local variables have the narrowest useful
  scope.

Keep the code's behavior unchanged unless the user's request calls for a
behavioral change. Apply the standard pragmatically: do not add abstractions
or comments that do not improve the code.
