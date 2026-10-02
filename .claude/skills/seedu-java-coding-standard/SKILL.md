---
name: seedu-java-coding-standard
description: Java coding conventions for this project, based on the SE-EDU intermediate Java coding standard (https://se-education.org/guides/conventions/java/intermediate.html). Use whenever writing, editing, or reviewing any .java file in this repository — naming, layout, statement, and comment/Javadoc rules to follow.
---

# SE-EDU Java Coding Standard (Intermediate)

Source: https://se-education.org/guides/conventions/java/intermediate.html
For anything not covered here, defer to the Google Java Style Guide.

Apply these rules whenever writing new Java code or editing existing Java
code in this project.

## Naming

- **Packages**: all lower case, e.g. `todobuddy.ui`.
- **Classes/enums**: nouns in PascalCase, e.g. `Line`, `AudioSystem`.
- **Variables**: camelCase, e.g. `line`, `audioSystem`.
- **Constants**: ALL_UPPERCASE with underscores, e.g. `MAX_ITERATIONS`.
- **Methods**: verbs in camelCase, e.g. `getName()`, `computeTotalWidth()`.
  Test methods: `featureUnderTest_testScenario_expectedBehavior()`, e.g.
  `sortList_emptyList_exceptionThrown()` (later parts may be omitted).
- **Abbreviations/acronyms**: not all-uppercase within a name.
  Good: `exportHtmlSource()`, `openDvdPlayer()`.
  Bad: `exportHTMLSource()`, `openDVDPlayer()`.
- **Language**: all names in English.
- **Scope vs. length**: large-scope variables get long, descriptive names;
  small-scope/scratch variables can be short (`i`, `j`, `k`, `n`, `c`, `d`).
- **Booleans**: name so they read like a boolean, e.g. `isSet`,
  `isVisible`, `hasData`, `wasOpen`; methods `hasLicense()`,
  `canEvaluate()`; setter form `void setFound(boolean isFound)`.
- **Collections**: plural form, e.g. `Collection<Point> points`,
  `int[] values`.
- **Iterators**: `i`, `j`, `k`; use `j`/`k` only for nested loops.
- **Associated constants**: share a common prefix, e.g. `COLOR_RED`,
  `COLOR_GREEN`, `COLOR_BLUE`.

## Layout

- **Indentation**: 4 spaces, never tabs.
- **Line length**: soft limit 110 chars, hard limit 120 chars — wrap
  longer lines.
- **Wrapped lines**: indent continuation lines 8 spaces further than the
  statement's own indentation:
  ```java
  setText("Long line split"
          + "into two parts.");
  ```
- **Where to break**: break after commas; break before operators
  (including `.`); prefer breaking at higher syntactic levels over lower
  ones; keep a method/constructor name attached to its opening `(`.
- **Ternary**: either on one line, or with `?`/`:` each starting a
  continuation line.
- **Braces**: K&R (Egyptian) style — opening brace on the same line:
  ```java
  while (!done) {
      doSomething();
  }
  ```
- **Switch**: traditional form needs an explicit `// Fallthrough` comment
  on any case without `break`; arrow (`->`) form and switch-expressions
  are also acceptable.
- **Whitespace**: space around binary operators (`a = (b + c) * d;`),
  space after reserved words before `(` (`while (true) {`), space after
  commas, space around `;` in `for` headers.
- **Blank lines**: separate logical units within a block with one blank
  line (with a comment introducing the next unit where helpful).

## Statements

- **Package**: every class belongs in a package. This project uses
  `buddy` as its root package, with subpackages by role, e.g.
  `buddy.task`, `buddy.storage`, `buddy.exception`.
- **Imports**: list explicitly, never wildcard (`import java.util.*;` is
  disallowed). Keep ordering consistent (e.g. static imports, then JDK,
  then third-party, then project packages, each group separated by a
  blank line).
- **Array specifiers**: attach to the type, not the variable.
  Good: `int[] a = new int[20];` Bad: `int a[] = new int[20];`
- **Variables**: initialize where declared; declare in the smallest scope
  possible.
- **Public fields**: class fields should never be `public` unless the
  class is a pure data class with no behavior (constants are exempt).
- **Loops**: always brace the body, even for a single statement.
- **Conditionals**: the condition's body goes on its own line and is
  always braced, even for a single statement:
  ```java
  if (isDone) {
      doCleanup();
  }
  ```

## Comments

- **Language**: English, American spelling.
- **Header comments**: mandatory for all classes and all public methods,
  except getters/setters, overridden methods whose parent Javadoc already
  applies, and test code.
- **Javadoc format**:
  ```java
  /**
   * Returns lateral location of the specified position.
   * If the position is unset, NaN is returned.
   *
   * @param x X coordinate of position.
   * @param y Y coordinate of position.
   * @return Lateral location.
   * @throws IllegalArgumentException If zone is <= 0.
   */
  public double computeLocation(double x, double y) throws IllegalArgumentException {
  ```
  - First sentence is a short summary, starting with a verb ("Returns...",
    not "Return...").
  - Blank line between the description and the `@param`/`@return`/`@throws`
    block; no blank line between the Javadoc block and the class/method.
  - `@return` may be omitted if the method returns nothing or the return
    value is obvious; `@param` should be included for all parameters or
    none.
  - Single-line form is fine for simple members:
    `/** Number of connections to this database */`
- **Comment indentation**: match the indentation of the surrounding code.
  Trailing comments on the same line as code are fine, e.g.
  `process('ABC'); // process a dummy String first`.
