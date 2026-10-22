---
name: seedu-java-coding-standard
description: Apply the SE-EDU Java coding standard (naming, layout, statements, comments) to Java code in this project. Use whenever writing, editing, or reviewing any .java file, or when asked to check/tweak the project's coding standard.
---

# SE-EDU Java Coding Standard

Source: [SE-EDU Java coding standard — Basic + Intermediate rules](https://se-education.org/guides/conventions/java/intermediate.html). Fall back to the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) for anything not covered here.

Apply this to every `.java` file in this project by default — don't wait to be asked. When editing existing code, bring the lines you touch into compliance; don't do a drive-by reformat of unrelated code in the same file.

## Naming

- Packages: all lowercase (e.g. `todobuddy.ui`).
- Classes/enums: `PascalCase` nouns (`Task`, `TaskList`).
- Variables/methods: `camelCase`. Methods are verbs (`getName()`, `computeTotal()`).
- Constants: `SCREAMING_SNAKE_CASE` (`MAX_ITERATIONS`). Related constants share a prefix (`COLOR_RED`, `COLOR_GREEN`).
- Don't uppercase acronyms inside names: `exportHtmlSource()`, not `exportHTMLSource()`.
- All identifiers in English.
- Scope-size naming: short names (`i`, `j`, `k`, `c`, `d`) only for small-scope scratch variables/loop indices; wider-scope variables get descriptive names.
- Boolean vars/methods read as booleans: `isSet`, `hasData`, `boolean hasLicense()`. Boolean setters: `void setFound(boolean isFound)`.
- Collections are named in the plural: `List<Point> points`.

## Layout

- Indent with 4 spaces, no tabs.
- Line length: soft limit 110 chars, hard limit 120.
- Wrapped lines: indent 8 spaces relative to the parent line; break after a comma, before an operator/`.`/`|`; keep a method name attached to its `(`.
- K&R ("Egyptian") braces — opening brace on the same line as the statement, e.g.:
  ```java
  while (!done) {
      doSomething();
  }
  ```
- One space around operators, after Java keywords, after commas, around `for`-loop semicolons.
- Separate logical blocks within a method with one blank line (often preceded by a comment).
- Standard K&R forms for `if`/`if-else`/`for`/`while`/`do-while`/`switch`/`try-catch`. Always brace loop and conditional bodies, even single-statement ones.

## Statements

- Every class belongs to a package (skip this only where the project has deliberately stayed in the default package for early increments — flag it rather than silently restructuring).
- Keep import ordering consistent within the project; never use wildcard imports (`import java.util.*;`).
- Array brackets attach to the type, not the variable: `int[] a`, not `int a[]`.
- Declare and initialize variables in the smallest scope possible, at the point of first use.
- Class fields are non-public unless the class is a pure data class with no behavior; constants are exempt.

## Comments

- English, American spelling, no slang.
- Every public class and public method needs a header Javadoc comment, **except**: getters/setters, method overrides whose parent Javadoc still applies exactly, and classes/methods used only for testing.
- Javadoc form:
  - Opening `/**` on its own line.
  - First sentence is a short summary starting with a verb (`Returns …`, `Adds …`), since Javadoc uses it as the method summary.
  - Blank line between the description and the `@param`/`@return`/`@throws` block.
  - Punctuation after each `@param` description.
  - `@return` can be omitted if the method returns nothing or the return value is obvious from the description.
  - `@param` is either present for every parameter or omitted entirely — don't document some parameters and skip others.
  - Single-line form for simple field comments: `/** Number of connections to this database */`.
- Comments are indented to match the surrounding code; trailing comments on the same line as code are fine.

## Applying this

1. Write new code compliant with the above from the start.
2. When asked to bring existing code up to standard, review each `.java` file against every section above and fix what's found — don't just skim for one category of issue.
3. Recompile/retest after changes; formatting and Javadoc edits should never change program behavior.
