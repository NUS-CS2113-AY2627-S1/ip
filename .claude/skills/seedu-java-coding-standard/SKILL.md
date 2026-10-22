---
name: seedu-java-coding-standard
description: >-
  The SE-EDU Java coding standard (Basic + Intermediate levels) used in this
  course. Invoke before writing, reviewing, or refactoring any Java code in this
  repository so that naming, layout, statements, and Javadoc all conform.
---

# SE-EDU Java Coding Standard (Basic + Intermediate)

Source: https://se-education.org/guides/conventions/java/intermediate.html

Apply every rule below to **all** Java code in this project: new code, edits, and
refactors. When something is not covered here, fall back to the
[Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).

---

## 1. Naming

| Element | Rule | Example |
| --- | --- | --- |
| Package | all lower case, logically grouped | `ducky.ui`, `ducky.storage` |
| Class / enum / interface | noun, `PascalCase` | `Chore`, `AudioSystem` |
| Method | verb, `camelCase` | `getName()`, `computeTotalWidth()` |
| Variable | `camelCase` | `line`, `audioSystem` |
| Constant (`static final`) | `UPPER_CASE_WITH_UNDERSCORES` | `MAX_ITERATIONS` |
| Test method | `featureUnderTest_testScenario_expectedBehavior()` | `parse_emptyInput_exceptionThrown()` |

- All names are in **English**.
- No all-caps acronyms in names: `exportHtmlSource()`, not `exportHTMLSource()`.
- Boolean variables/methods take an `is` / `has` / `was` / `can` prefix:
  `isDone`, `hasNext`, `canEvaluate()`. Setter: `void setFound(boolean isFound)`.
- Collections use the **plural**: `Collection<Point> points`.
- Large scope -> descriptive name; tiny scope -> short name is fine
  (`i`, `j`, `k` for loop counters, with `j`, `k` only for nested loops).
- Associated constants share a prefix: `COLOR_RED`, `COLOR_GREEN`, `COLOR_BLUE`.

---

## 2. Layout & formatting

- **Indentation:** 4 spaces, never tabs.
- **Line length:** hard limit 120 chars; aim for <= 110.
- **Wrapped-line indentation:** 8 spaces (double the normal indent).
- **Braces:** K&R / "Egyptian" style — opening brace on the same line, preceded
  by a space; closing brace on its own line.

  ```java
  public void someMethod() throws SomeException {
      if (condition) {
          statements;
      } else if (condition) {
          statements;
      } else {
          statements;
      }
  }
  ```

- **Line-wrapping principles:** break after a comma; break *before* an operator
  (including `.`); keep a method/constructor name attached to its `(`; prefer
  higher-level breaks. Ternary is either one line or a clean three-line form.
- **Whitespace:**
  - binary/ternary operators are surrounded by spaces: `a = (b + c) * d;`
  - a reserved word is followed by a space: `while (true) {`, `if (x) {`
  - a comma is followed by a space: `doSomething(a, b, c);`
  - **no** space between a method name and its `(`: `println("hi")`, not `println ("hi")`
  - `for (i = 0; i < 10; i++) {`
- **Blank lines:** separate logical units inside a block with a single blank line;
  do not pile up multiple blank lines.

---

## 3. Statements

- Every class belongs to a **package**. (Default package is tolerated only at the
  earliest project levels before a build tool exists; migrate to real packages
  as soon as the project grows.)
- **Imports:** list classes explicitly — no wildcard (`import java.util.*`)
  imports. Keep import order consistent: static imports, then `java.*`,
  `javax.*`, `org.*`, `com.*`, then project packages.
- Array brackets attach to the **type**: `int[] a`, not `int a[]`.
- Declare a variable in the **smallest scope** possible and initialize it where
  it is declared.
- A class variable is never `public` unless the class is a pure data class with
  no behaviour (`public static final` constants are exempt).
- **Every** loop body and **every** conditional branch is wrapped in curly
  braces, even one-liners.
- In a `switch`, mark an intentional missing `break` with a `// Fallthrough`
  comment.

---

## 4. Comments & Javadoc

- Comments and Javadoc are in **English**, using **British spelling**
  (e.g. "behaviour", "initialise", "colour"). This overrides the SE-EDU
  standard's American-spelling rule, by project decision. Identifiers still use
  whatever spelling their API requires (e.g. `Color`, `initialize` from the JDK).
- Every public class and public/protected method has a header Javadoc comment.
  Exceptions: trivial getters/setters, methods that fully inherit a parent
  Javadoc (`{@inheritDoc}`), and test classes/methods.
- Javadoc block form:

  ```java
  /**
   * Returns the lateral location of the specified position.
   * If the position is unset, NaN is returned.
   *
   * @param x X coordinate of the position.
   * @param y Y coordinate of the position.
   * @return The lateral location.
   * @throws IllegalArgumentException If the zone is <= 0.
   */
  ```

  - `/**` on its own line; aligned ` *` with a space after each `*`.
  - First sentence is a short summary starting with a verb in the third person
    ("Returns...", "Adds...", "Sends...").
  - One empty line between the description and the first `@` tag.
  - Each `@param` / `@return` / `@throws` description ends with punctuation.
  - No blank line between the Javadoc block and the thing it documents.
  - `@return` may be omitted when the method is `void` or the return is obvious;
    `@param` tags are omitted only when *every* parameter is self-explanatory.
- Single-line member Javadoc is allowed:

  ```java
  /** Number of connections to this database. */
  private int connectionCount;
  ```

---

## 5. Quick checklist before committing Java

- [ ] Class names `PascalCase`; methods/variables `camelCase`; constants `UPPER_CASE`.
- [ ] 4-space indent, no tabs; lines <= 120 chars.
- [ ] Opening braces on the same line, space before `{`, space after keywords.
- [ ] No space between a method name and `(`.
- [ ] Every `if` / `for` / `while` body has braces.
- [ ] No wildcard imports; imports ordered.
- [ ] Public classes and non-trivial methods have Javadoc in the correct form.
