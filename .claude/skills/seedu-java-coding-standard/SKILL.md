---
name: seedu-java-coding-standard
description: The SE-EDU Java coding standard (basic + intermediate rules) used by this project. Apply when writing, reviewing, or tweaking any Java code in this repo, and when asked to check code against the coding standard.
---

# SE-EDU Java coding standard (basic + intermediate)

Source: https://se-education.org/guides/conventions/java/intermediate.html
For anything not covered here, fall back to the Google Java style guide.

All Java code in this project must follow these rules.

## Naming

- Packages: all lower case. For school projects use the project name plus logical
  groupings, e.g. `todobuddy.ui`, `todobuddy.file`. Never `edu.nus.comp.*`.
- Classes/enums: nouns in `PascalCase` — `Line`, `AudioSystem`.
- Variables: `camelCase` — `line`, `audioSystem`.
- Constants: `SCREAMING_SNAKE_CASE` — `MAX_ITERATIONS`, `COLOR_RED`.
- Methods: verbs in `camelCase` — `getName()`, `computeTotalWidth()`.
- Test methods may use `featureUnderTest_testScenario_expectedBehavior()`;
  the third part, or both second and third, may be omitted.
- Abbreviations/acronyms are not uppercase inside a name:
  `exportHtmlSource()` not `exportHTMLSource()`.
- All names in English.
- Large scope gets long names; small scope may use short ones. Scratch/index
  variables may be `i`, `j`, `k`, `m`, `n`, and `c`, `d` for characters.
- Booleans read like booleans, prefixed `is`/`has`/`was`/`can`/`should` —
  `isVisible`, `hasData`, `canEvaluate()`. Boolean setters take the form
  `void setFound(boolean isFound);`.
- Collections take plural names — `Collection<Point> points`, `int[] values`.
- Iterator variables may be `i`, `j`, `k`; use `j`/`k` only for nested loops.
- Associated constants share a common prefix — `COLOR_RED`, `COLOR_GREEN`.

## Layout

- Indent 4 spaces. Never tabs.
- Line length: soft limit 110 chars, hard limit 120.
- Wrapped lines indent **8** spaces (twice normal) from the parent line.
- Break after a comma; break *before* an operator (including `.`, `&`, `|`).
- A method/constructor name stays attached to its opening `(`.
- Prefer higher-level breaks to lower-level ones.
- K&R (Egyptian) braces — opening brace on the same line, never its own line.
- Surround operators with spaces; follow reserved words and commas with a space.
- Separate logical units within a block by one blank line.

Standard statement forms:

```java
public void someMethod() throws SomeException {
    ...
}

if (condition) {
    statements;
} else if (condition) {
    statements;
} else {
    statements;
}

for (initialization; condition; update) {
    statements;
}

while (condition) {
    statements;
}

do {
    statements;
} while (condition);

try {
    statements;
} catch (Exception exception) {
    statements;
} finally {
    statements;
}
```

In a `switch`, add an explicit `// Fallthrough` comment for any `case` without a `break`.

## Statements

- Put every class in a package.
- Keep import ordering consistent.
- List imported classes explicitly — never `import java.util.*;`.
- Array specifiers attach to the type: `int[] a` not `int a[]`.
- Initialize variables where declared, in the smallest scope possible.
- Class variables are never `public` unless the class is a data class with no
  behavior. Constants are exempt.
- Always wrap loop bodies in braces, however few the statements.
- Put the conditional on its own line — never `if (isDone) doCleanup();`.
- Always wrap conditional bodies in braces, even single statements.

## Comments

- English, American spelling, no local slang.
- **Write descriptive header comments for all classes and public methods.**
  May be omitted for: getters/setters, overriding methods where the parent's
  Javadoc applies as-is, and test classes/methods.
- Javadoc form:

```java
/**
 * Returns lateral location of the specified position.
 * If the position is unset, NaN is returned.
 *
 * @param x X coordinate of position.
 * @param zone Zone of position.
 * @return Lateral location.
 * @throws IllegalArgumentException If zone is <= 0.
 */
public double computeLocation(double x, int zone) throws IllegalArgumentException {
    // ...
}
```

  - Opening `/**` on its own line; subsequent `*` aligned; a space after each `*`.
  - First sentence is a short summary, starting `Returns ...`, `Sends ...`,
    `Adds ...` — not `Return` or `Returning`.
  - Blank line between description and the parameter section.
  - Punctuation after each parameter description.
  - No blank line between the Javadoc block and the class/method.
  - `@return` may be omitted when the method returns nothing or the return value
    is obvious from the description.
  - `@param` may be omitted when every parameter name is self-explanatory — it is
    all params or none.
  - Use `@inheritDoc` to reuse a parent method's comment.
- Class members may use single-line Javadoc: `/** Number of connections */`.
- Indent comments to match the code they describe.
- Trailing comments are allowed: `process("ABC"); // process a dummy String first`.

## Checking existing code

When asked to bring code up to standard, walk the rules above in order and report
what changed. Do not restructure the design beyond what the standard requires —
architecture changes belong to separate increments.
