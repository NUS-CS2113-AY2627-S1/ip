---
name: seedu-java-coding-standard
description: The SE-EDU Java coding standard (intermediate level) that all Java code in this project must follow. Load before writing, editing, or reviewing any .java file in this repository.
---

# SE-EDU Java coding standard (intermediate)

Source: https://se-education.org/guides/conventions/java/intermediate.html

All Java code in this project must comply with the rules below. Apply them to new
code as it is written, not as a clean-up pass afterwards.

## Naming

| Element | Rule | Example |
|---|---|---|
| Package | all lower case | `todobuddy.ui` |
| Class / enum | noun, PascalCase | `Line`, `AudioSystem` |
| Variable | camelCase | `line`, `audioSystem` |
| Constant | UPPER_CASE with underscores | `MAX_ITERATIONS`, `COLOR_RED` |
| Method | verb, camelCase | `getName()`, `computeTotalWidth()` |

- All names are written in English.
- Variables with a large scope get long names; small-scope variables may be short.
- Scratch variables may be `i, j, k, m, n` (integers) and `c, d` (characters);
  `j` and `k` are reserved for nested loops.
- Collections take a plural name: `Collection<Point> points;`
- Associated constants share a common prefix.
- Booleans sound like booleans, using an `is` / `has` / `was` / `can` / `should`
  prefix: `isVisible`, `hasData`, `wasOpen`, `canEvaluate()`, `shouldAbort`.
  A setter takes the form `void setFound(boolean isFound);`
- Abbreviations and acronyms are **not** uppercased inside a name:
  `exportHtmlSource()`, `openDvdPlayer()` — not `exportHTMLSource()`.
- Test methods use `featureUnderTest_testScenario_expectedBehavior()`, e.g.
  `sortList_emptyList_exceptionThrown()`. Parts may be omitted where the scope
  makes them obvious.

## Layout

- Indent with **4 spaces**, never tabs.
- Indent wrapped lines by **8 spaces** (double the normal indent):

  ```java
  setText("Long line split"
          + "into two parts.");
  ```

- Break **after** a comma and **before** an operator (including `.`, `&` in type
  bounds, and `|` in catch blocks). Prefer higher-level breaks to lower-level ones.
- A method or constructor name stays attached to its opening `(`.
- Line length: soft limit 110 characters, hard limit **120**.
- Use K&R ("Egyptian") braces — the opening brace ends the line that opens the block:

  ```java
  while (!done) {
      doSomething();
      done = moreToDo();
  }
  ```

- `if` / `else if` / `else`, `for`, `while`, `do-while`, `try-catch-finally` and
  `switch` all follow the same brace style.
- In a traditional `switch`, a `case` without a `break` carries an explicit
  `// Fallthrough` comment.

## Whitespace

- Spaces around operators: `a = (b + c) * d;` not `a=(b+c)*d;`
- Space after a reserved word: `while (true) {` not `while(true){`
- Space after a comma: `doSomething(a, b, c, d);`
- Spaces around the parts of a `for` header: `for (i = 0; i < 10; i++)`
- Separate logical units within a block with a single blank line.

## Statements

- **Packages:** put every class in a package.
- **Imports:** list imported classes explicitly — `import java.util.List;`, never
  `import java.util.*;`. Keep import order consistent: static imports, `java.*`,
  `javax.*`, third-party, then project imports.
- **Arrays:** attach the specifier to the type — `int[] a` not `int a[]`.
- **Variables:** initialise where declared, and declare in the smallest possible
  scope.
- Class variables are never `public` unless the class is a data class with no
  behaviour; constants are exempt.
- **Loops and conditionals:** always wrap the body in curly brackets, even for a
  single statement, and put the conditional on its own line — never
  `if (isDone) doCleanup();`.

## Comments

- Write all comments in English, using American spelling; avoid slang.
- Write descriptive header comments for all public classes and public methods.
  They may be omitted for getters/setters, for overridden methods whose parent
  Javadoc applies exactly, and for test classes/methods.
- Javadoc format:
  - `/**` sits on its own line, with subsequent `*` aligned and one space after each.
  - The first sentence is a short summary written in the third person —
    `Returns ...`, `Sends ...`, `Adds ...` — not the imperative form.
  - An empty line separates the description from the `@param` / `@return` /
    `@throws` block; there is no blank line between the Javadoc and the declaration.
  - Parameter descriptions end with punctuation.
  - `@return` may be omitted when nothing is returned or the value is obvious.
  - `@param` may be omitted only when every parameter is self-explanatory —
    apply it to all parameters or none.
  - Use `{@inheritDoc}` to reuse a parent's documentation.

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
  ```

- A single-line member comment is written as
  `/** Number of connections to this database */`.
- Indent comments to match the code they describe. Trailing comments are allowed:
  `process('ABC'); // process a dummy String first`

## Project note on packages

The **A-Packages** increment has been done, so the "put every class in a
package" rule now applies in full: no class goes in the default package. The
source root stays `src/main/java`, and `src`, `main` and `java` are *not*
package names.

| Package | Holds | Role |
|---|---|---|
| `turing` | `Turing`, `Command`, `TuringException` | The layer the user talks to: reading input, the vocabulary it accepts, and how a refusal is explained. |
| `turing.task` | `Task`, `Todo`, `Deadline`, `Event`, `TaskList` | What the user is tracking. Knows nothing about commands or printing. |

Put a new class in the package whose job it shares. Add a further package only
once it would hold more than one class — a package built around a single class
buys nothing. Run the app with `java -cp <classes-dir> turing.Turing`.
