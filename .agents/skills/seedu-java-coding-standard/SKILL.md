---
name: seedu-java-coding-standard
description: Apply the SE-EDU intermediate Java coding standard when creating, editing, or reviewing Java code in this project.
---

# SE-EDU Java Coding Standard

Follow the [SE-EDU Java coding standard (basic + intermediate)](https://se-education.org/guides/conventions/java/intermediate.html) for all Java code in this project. For topics it does not cover, use the Google Java Style Guide as directed by that standard.

Before completing Java changes, check that the code follows these rules:

- Put every class in an appropriately named, all-lowercase package. Use nouns in PascalCase for classes, verbs in camelCase for methods, and camelCase for variables. Use boolean names that read as booleans and plural names for collections.
- Use 4-space indentation, K&R braces, explicit imports, and lines no longer than 120 characters. Wrap long lines at readable, higher-level boundaries with an 8-space continuation indent.
- Declare and initialize variables in the smallest practical scope. Keep class fields non-public unless the class is a behavior-free data class.
- Put braces around every conditional and loop body, including single-statement bodies. Keep logical units separated by one blank line.
- Write English, American-English comments. Add descriptive Javadocs to public classes and public methods except obvious getters/setters; start the first sentence with a verb such as Returns, Adds, or Marks.

Do not make unrelated structural changes solely to satisfy a preference. When a user requirement conflicts with this standard, follow the user requirement and note the conflict.
