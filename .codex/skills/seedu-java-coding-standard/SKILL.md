---
name: seedu-java-coding-standard
description: Comprehensive Java coding standard based on SE-EDU conventions (basic + intermediate rules) used for software engineering student projects. Use when writing, modifying, reviewing, or refactoring Java code in this project.
---

# SE-EDU Java Coding Standard (Basic + Intermediate)

This guide defines the Java coding standard for this project, based on the [SE-EDU Java Coding Standard (Basic + Intermediate)](https://se-education.org/guides/conventions/java/intermediate.html). For topics not explicitly covered here, refer to the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).

---

## 1. Naming Conventions

* **Packages**: All lower case, representing logical groups (e.g., `dandelion.ui`, `dandelion.task`). For school projects, root name should be project or group name, never `edu.nus.comp.*`.
* **Classes & Enums**: Nouns in `PascalCase` (e.g., `Task`, `Dandelion`, `AudioSystem`).
* **Variables & Method Parameters**: `camelCase` (e.g., `taskCount`, `linePrefix`, `command`).
* **Constants**: All uppercase with underscores (`SCREAMING_SNAKE_CASE`), e.g., `MAX_TASKS`, `COLOR_RED`. Associated constants must share a common prefix (e.g., `COLOR_RED`, `COLOR_GREEN`).
* **Methods**: Verbs in `camelCase` (e.g., `markAsDone()`, `computeTotalWidth()`, `getStatusIcon()`).
* **Test Methods**: May use underscores following `featureUnderTest_testScenario_expectedBehavior()` (e.g., `sortList_emptyList_exceptionThrown()`).
* **Acronyms & Abbreviations**: Treat as words in camelCase/PascalCase, not all uppercase (e.g., `exportHtmlSource()`, `openDvdPlayer()`, `XmlParser`, not `exportHTMLSource` or `XMLParser`).
* **Language**: All names must be written in English.
* **Scope & Length**:
  * Large scope: descriptive, longer names.
  * Small scope / scratch / loop indices: short names (`i`, `j`, `k`, `c`, `d`).
* **Booleans**:
  * Must sound like booleans, using prefixes like `is`, `has`, `was`, `can`, `should` (e.g., `isDone`, `isVisible`, `hasLicense()`, `canEvaluate()`).
  * Boolean setters must follow `setFound(boolean isFound)`.
* **Collections & Arrays**: Use plural nouns (e.g., `Task[] tasks`, `List<Point> points`).
* **Iterators**: `i` for outer loops, `j`, `k` for nested loops only.

---

## 2. Layout & Formatting

* **Indentation**: Exactly 4 spaces (never tabs).
* **Line Length**:
  * Soft limit: 110 characters.
  * Hard limit: 120 characters.
* **Line Wrapping**:
  * Indent wrapped continuation lines by **8 spaces** (twice the normal 4 spaces).
  * Break **after a comma**.
  * Break **before an operator** (including `.`, `&` in type bounds, `|` in catch blocks).
  * Method/constructor name stays attached to the opening parenthesis `(`.
  * Prefer higher-level breaks to lower-level breaks.
* **Braces (K&R / Egyptian style)**:
  * Open brace `{` at the end of the line that starts the statement/class/method.
  * Close brace `}` on a new line aligned with the statement start.
* **Control Structures**:
  ```java
  if (condition) {
      statements;
  } else if (condition) {
      statements;
  } else {
      statements;
  }

  for (int i = 0; i < count; i++) {
      statements;
  }

  while (condition) {
      statements;
  }

  try {
      statements;
  } catch (SpecificException exception) {
      statements;
  } finally {
      statements;
  }

  switch (condition) {
  case ABC:
      statements;
      // Fallthrough
  case DEF:
      statements;
      break;
  default:
      statements;
      break;
  }
  ```
  * Note: Explicit `// Fallthrough` comment is required if a `case` deliberately falls through without a `break`.
* **Whitespace within Statements**:
  * Operators surrounded by a single space (`a = (b + c) * d;`).
  * Java keywords followed by a space (`if (...)`, `for (...)`, `while (...)`, `try (...)`, `catch (...)`).
  * Commas followed by a space (`doSomething(a, b, c);`).
  * Colons surrounded by space when used as binary/ternary operators (`a ? b : c`).
  * Semicolons in `for` headers followed by a space (`for (int i = 0; i < 10; i++)`).
* **Logical Units**: Separate logical blocks within a method body with **one blank line**.

---

## 3. Statements & Declarations

* **Packages & Imports**:
  * Put every class in a named package.
  * Explicit imports only (never wildcard imports like `import java.util.*;`).
  * Consistent ordering of import statements.
* **Types & Arrays**:
  * Array brackets attached to type, not the variable (`int[] numbers = new int[10];`, not `int numbers[]`).
* **Variables & Scope**:
  * Initialize variables where they are declared whenever possible.
  * Declare variables in the smallest scope possible.
  * Class fields/variables must never be `public` (except constants or data classes with no behavior). Use encapsulation and accessors.
* **Loops & Conditionals**:
  * Loop bodies must always be wrapped in curly braces `{}` even if single-line.
  * Conditionals must always be on a separate line and wrapped in curly braces `{}` even for single-line bodies.

---

## 4. Comments & Javadoc

* **Language**: English with American spelling; avoid slang.
* **Header Comments**:
  * Required for all public classes and public methods.
  * Optional for trivial getters/setters, test methods, and standard `@Override` methods where behavior is unchanged.
* **Javadoc Format**:
  ```java
  /**
   * Returns lateral location of the specified position.
   * If the position is unset, NaN is returned.
   *
   * @param x X coordinate of position.
   * @param y Y coordinate of position.
   * @param zone Zone of position.
   * @return Lateral location.
   * @throws IllegalArgumentException If zone is <= 0.
   */
  public double computeLocation(double x, double y, int zone) throws IllegalArgumentException {
      // ...
  }
  ```
  * Opening `/**` on a separate line.
  * First sentence is a short summary in 3rd-person singular present tense (`Returns ...`, `Marks ...`, `Runs ...`, `Creates ...`).
  * Subsequent `*` aligned with one space after `*`.
  * Empty line between summary description and `@param` / `@return` / `@throws` section.
  * Punctuation at the end of each `@param` / `@return` / `@throws` description.
  * No blank line between the Javadoc comment and the declaration.
  * Class fields can use a single-line Javadoc: `/** Number of connections to this database. */`.
* **Inline Comments**:
  * Indent relative to their position in the code.
