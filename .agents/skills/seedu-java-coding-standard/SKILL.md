---
name: seedu-java-coding-standard
description: Apply and review the SE-EDU basic and intermediate Java coding standard for every Java file in this repository. Use whenever Java code is created, edited, refactored, or reviewed.
---

# SE-EDU Java Coding Standard

Use the official SE-EDU Java coding standard (basic + intermediate) for all production and test Java code in this
repository. For topics the standard does not cover, use the Google Java Style Guide as directed by the official
standard. Follow the CS2113 course convention for `switch` indentation as the project-specific exception described
below.

Official standard: <https://se-education.org/guides/conventions/java/intermediate.html>

## Apply the standard

- Put every class in a package. Use lowercase package names rooted at the project name, `luke`, followed by logical
  subgroup names when the code is split into components.
- Name classes and enums with PascalCase nouns, methods with camelCase verbs, variables with camelCase English names,
  booleans to read as booleans, collections with plural nouns, and constants with SCREAMING_SNAKE_CASE. Keep acronyms
  lowercase except for their first letter when they form part of a name, such as `exportHtmlSource`.
- Prefer `is`, `has`, `was`, `can`, or `should` prefixes for booleans. Give wider-scope variables more descriptive
  names, use `i` for a single loop index, and reserve `j`, `k`, and similar names for nested loops. Give related
  constants a common prefix. Test methods may use `featureUnderTest_testScenario_expectedBehavior` names.
- Use four spaces, never tabs. Keep lines below the 120-character hard limit and aim for fewer than 110 characters.
  Indent wrapped lines eight spaces beyond their parent line and wrap at readable, high-level boundaries.
- Use K&R braces. Always brace loop and conditional bodies, and put conditional bodies on separate lines.
- Follow the CS2113 course convention for `switch` statements: align `case` and `default` labels with their enclosing
  `switch`, and indent their statements one level further. End each traditional case with `break` or use an explicit
  `// Fallthrough` comment when fallthrough is intentional. Arrow-style cases and switch expressions are allowed.
- Put spaces around operators and after Java keywords, commas, and `for`-loop semicolons. Separate logical units with
  one blank line.
- Attach array brackets to the type. Initialize variables at declaration when practical and declare them in the
  smallest useful scope. Do not expose non-constant class variables publicly unless the class is a behavior-free data
  class.
- List imports explicitly, remove unused imports, and keep import ordering consistent with neighboring files. Never use
  wildcard imports.
- Write English comments using American spelling and indent them with the code they describe. Add descriptive Javadoc
  header comments to every class and public method, except getters/setters, exact overrides, and test code where the
  official standard allows omission.
- Start method summaries with a third-person verb such as `Returns`, `Adds`, or `Marks`. Put `/**` on its own line,
  align each `*`, separate the summary from tags with a blank line, and end parameter descriptions with punctuation.
  Include `@param` tags for all parameters or none; document return values and thrown exceptions when doing so adds
  information.
- Give each class a logical, explainable member order rather than appending members chronologically. Keep overloads of
  the same method or constructor together.

## Preserve behavior

When applying this skill as a style-only task, do not change command semantics, output text, data flow, or add future
features. Compile with Java 25 after editing. For production changes, review `test/ui-test-plan.md` and invoke the
`test-ui` skill as required by `AGENTS.md`. Review the final diff for standard violations and unrelated changes before
reporting completion.
