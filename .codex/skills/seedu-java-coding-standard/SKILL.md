---
name: seedu-java-coding-standard
description: Apply the SE-EDU intermediate Java coding conventions to all Java code in this project.
---

# SE-EDU Java coding standard

Use this skill for every Java code change in this repository. The authoritative
rules are documented at:
https://se-education.org/guides/conventions/java/intermediate.html

Apply the rules consistently: use lowercase packages and explicit consistently
ordered imports; descriptive names and `is`/`has` boolean names; four-space
indentation, K&R braces, spaces around operators and after commas, logical blank
lines, and a 120-character hard line limit; type-attached array brackets;
initialized variables in the smallest scope; no public mutable fields; braces
for every loop and conditional; `// Fallthrough` for intentional switch
fall-through; and English/American-spelling comments.

Write descriptive Javadocs for public classes and public methods, including
`@param`, `@return`, and `@throws` when useful. Getters/setters and correctly
inherited overrides may omit redundant Javadocs. Preserve behavior unless the
requested change requires otherwise, inspect the final diff for violations, and
compile with Java 25 when available.
