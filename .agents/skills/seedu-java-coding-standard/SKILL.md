---
name: seedu-java-coding-standard
description: Apply the SE-EDU basic and intermediate Java coding rules when writing, reviewing, or refactoring Java code in this project.
---

# Seedu Java Coding Standard

Use this skill for every Java source change in this project. Preserve behavior unless
the user requests a behavior change, and fix style issues in code that the requested
change touches.

## Source of truth

Follow the SE-EDU [Java coding standard (basic + intermediate)](https://se-education.org/guides/conventions/java/intermediate.html).
For a topic that the page does not cover, use the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).

## Rules to apply

- Put every class in a lowercase package. Use the project name as the root package
  (for this project, `dobby`), followed by logical subpackages where needed. Keep
  package declarations, source directories, and imports consistent.
- Use noun-based `PascalCase` for classes and enums, `camelCase` for variables and
  verb-based methods, and `SCREAMING_SNAKE_CASE` for constants. Keep acronyms in
  normal mixed case (`exportHtml`, not `exportHTML`). Use English and American
  spelling for names and comments.
- Name booleans to read as predicates (`isDone`, `hasData`, `canEvaluate`) and use
  `setName(boolean isName)` for boolean setters. Use plural names for collections.
  Keep short iterator names such as `i`, `j`, and `k` to small loop scopes only.
- Indent with four spaces, use K&R braces, and keep lines at or below 120 characters
  (prefer below 110). Wrapped lines use an additional eight spaces of indentation.
  Put spaces around operators, after commas, after Java keywords, and after `for`
  semicolons. Separate logical units in a block with one blank line.
- Put method and constructor names directly next to their opening parenthesis. Break
  long expressions at readable higher-level boundaries, normally after commas or
  before operators.
- Use explicit, consistently ordered imports; never use wildcard imports. Attach
  array brackets to the type (`String[] names`). Initialize variables at declaration
  when possible and declare them in the smallest scope. Do not expose class fields
  publicly, except for constants or behavior-free data classes.
- Always use braces around loop and conditional bodies, including one-line bodies.
  Put conditional bodies on separate lines. Mark intentional switch fall-through
  with `// Fallthrough`.
- Write descriptive Javadoc for every public class and public method, except
  getters/setters, exact overrides, and test code. Start the summary with a present-
  tense verb such as `Returns`, `Adds`, or `Sends`; document parameters and thrown
  exceptions when useful, end parameter descriptions with punctuation, and keep the
  documentation block immediately above its declaration. Keep all comments aligned
  with the code and written in English.
- For tests, underscores are allowed in the three-part form
  `featureUnderTest_testScenario_expectedBehavior`.

## Review workflow

Before finishing a Java change, inspect the complete affected file for violations,
check line lengths and package/import structure, then compile or run the relevant
tests with Java 25. Do not introduce a formatter or dependency solely for this
review; make the smallest clear source change that satisfies the standard.
