---
name: seedu-java-coding-standard
description: >-
  Enforces the SE-EDU Java coding standard (basic and intermediate rules) for all Java code in this project.
  Use whenever writing, modifying, refactoring, or reviewing Java source code.
---

# SE-EDU Java Coding Standard (Basic + Intermediate)

This skill provides guidelines and rules from the [SE-EDU Java Coding Standard](https://se-education.org/guides/conventions/java/intermediate.html). For topics not covered here, follow the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).

---

## 1. Naming Conventions

* **Packages**: All lowercase alphanumeric characters without underscores (e.g., `foodielover.ui`, `foodielover.command`). For project code, use your project/group name as root (never `edu.nus.comp.*`).
* **Classes & Enums**: Nouns in `PascalCase` (e.g., `Foodielover`, `TaskManager`, `PriorityLevel`).
* **Variables & Fields**: Nouns or noun phrases in `camelCase` (e.g., `totalCount`, `taskDescription`).
* **Constants**: All uppercase with underscores `SCREAMING_SNAKE_CASE` (e.g., `MAX_CAPACITY`, `DIVIDER_LINE`). Associated constants must share a common prefix (e.g., `COLOR_RED`, `COLOR_BLUE`).
* **Methods**: Verbs in `camelCase` (e.g., `getName()`, `printWelcomeMessage()`, `addTask()`).
* **Booleans**:
  * Names should sound like booleans with prefixes such as `is`, `has`, `can`, `was`, `should` (e.g., `isDone`, `hasChildren`, `canExecute()`).
  * Boolean setters must follow `void setFound(boolean isFound)`.
* **Collections / Arrays**: Use plural forms (e.g., `List<Task> tasks`, `int[] values`).
* **Loop Iterators**: Standard scratch names `i`, `j`, `k`. Nested loops only for `j`, `k`.
* **Abbreviations & Acronyms**: Treat as regular words, not all-caps (e.g., `exportHtmlSource()`, `parseXml()`, not `exportHTMLSource()`).
* **Test Methods**: Use 3-part underscore convention: `featureUnderTest_testScenario_expectedBehavior()` (e.g., `addDeadline_emptyDescription_exceptionThrown()`).
* **Language**: All identifiers must be in English.

---

## 2. Layout & Formatting

* **Indentation**: 4 spaces per level (never tabs).
* **Line Length**: Soft limit of 110 characters, hard limit of 120 characters.
* **Line Wrapping**:
  * Indent continuation lines by 8 spaces (twice normal indentation).
  * Break **after** commas.
  * Break **before** operators (`+`, `-`, `.`, `&&`, etc.).
  * Keep method/constructor name attached to opening parenthesis `(`.
  * Prefer higher-level breaks over lower-level nested breaks.
* **Brace Style**: K&R / Egyptian style brackets (`{` at the end of statement line):
  ```java
  if (condition) {
      statements;
  } else {
      statements;
  }
  ```
* **Control Structures**:
  * Single-statement conditionals and loops **MUST** be enclosed in braces `{ }`.
  * The conditional expression must be on a separate line from statements.
  * `switch` statements must include an explicit `// Fallthrough` comment when a `case` intentionally falls through.
* **Whitespace**:
  * Binary and ternary operators must be surrounded by a single space (e.g., `a = (b + c) * d;`).
  * Keywords (`if`, `while`, `for`, `switch`, `catch`) must be followed by a single space before `(`.
  * Commas and semicolons (in `for` loops) must be followed by a space.
  * Separate logical units inside methods with a single blank line.
* **Array Specifiers**: Attach brackets to type, not variable name (`int[] numbers;`, NOT `int numbers[];`).

---

## 3. Statements & Declarations

* **Packages & Imports**:
  * Put every class in a named package.
  * List imports explicitly; **never** use wildcard imports (e.g., use `import java.util.List;`, not `import java.util.*;`).
  * Maintain consistent import ordering (e.g., static imports first, standard library, third-party, project packages).
* **Variables**:
  * Declare variables in the smallest possible scope and initialize where declared.
  * Class fields should never be declared `public` (except `public static final` constants or pure data classes with no behavior).

---

## 4. Comments & Javadoc

* **Language**: English with American spelling. Avoid slang.
* **Mandatory Header Comments**:
  * Required for all classes and public methods/constructors.
  * May be omitted only for trivial getters/setters, `@Override` methods where parent javadoc applies verbatim, and test classes.
* **Javadoc Format**:
  ```java
  /**
   * Returns lateral location of the specified position.
   * If the position is unset, NaN is returned.
   *
   * @param x X coordinate of position.
   * @param y Y coordinate of position.
   * @return Lateral location.
   * @throws IllegalArgumentException If coordinates are invalid.
   */
  public double computeLocation(double x, double y) throws IllegalArgumentException {
      // ...
  }
  ```
  * Opening `/**` on its own line.
  * First sentence is a concise summary starting in third-person present tense (e.g., `Returns ...`, `Adds ...`, `Runs ...`, `Parses ...` — NOT `Return` or `Returning`).
  * Blank line between description and `@param` / `@return` / `@throws` tags.
  * Parameter descriptions end with punctuation (period).
  * No blank line between closing `*/` and class/method declaration.
  * Single-line field Javadoc: `/** Number of connections to this database. */`.
  * Inline comments must be indented relative to their position in code.
