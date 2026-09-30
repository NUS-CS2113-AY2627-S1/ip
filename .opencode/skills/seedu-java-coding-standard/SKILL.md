---
name: seedu-java-coding-standard
description: Use when writing, editing, or reviewing Java code in a SE-EDU student project (CS2103/CS2103T iP or tP). Enforces the Java coding standard (basic + intermediate rules) from se-education.org/guides/conventions/java/intermediate.html: naming, layout, statements, imports, and Javadoc comments. Also use when checking whether existing Java code complies with the standard.
---

# SE-EDU Java coding standard (basic + intermediate)

This skill encodes the SE-EDU Java coding standard (basic + intermediate rules) for
student projects. Source: https://se-education.org/guides/conventions/java/intermediate.html
Use the [Google Java style guide](https://google.github.io/styleguide/javaguide.html)
for any topics not covered here.

## Naming

- Packages: all lower case; root name is the project/group name, e.g. `maple.ui`.
  Do NOT use `edu.nus.comp.*` or similar.
- Classes/enums: nouns in PascalCase, e.g. `Line`, `AudioSystem`.
- Variables: camelCase, e.g. `line`, `audioSystem`.
- Constants: SCREAMING_SNAKE_CASE, e.g. `MAX_ITERATIONS`, `COLOR_RED`. Associated
  constants share a common prefix.
- Methods: verbs in camelCase, e.g. `getMember()`, `computeTotalWidth()`.
  Test methods MAY use `featureUnderTest_testScenario_expectedBehavior()`.
- Abbreviations/acronyms are not uppercased inside names: `exportHtmlSource()` good,
  `exportHTMLSource()` bad.
- All names in English.
- Scope-appropriate name length: long names for large scope, short for small scope.
  Scratch vars: `i, j, k, m, n` for ints, `c, d` for chars.
- Booleans sound like booleans: `isSet`, `isVisible`, `hasData`, `wasOpen`;
  methods `hasLicense()`, `canEvaluate()`. Boolean setters: `void setFound(boolean isFound)`.
- Collections use plural names: `Collection<Point> points;`.
- Iterator variables `i, j, k`; `j, k` only for nested loops.

## Layout

- Indent 4 spaces (never tabs).
- Line length: soft limit 110 chars, hard limit 120 chars.
- Wrapped lines: indent 8 spaces more than the parent line.
- Break after a comma; break BEFORE an operator (also before `.`, `&` in type
  bounds, `|` in catch). A method name stays attached to its `(`, i.e. no break
  between name and paren.
- Prefer higher-level breaks over lower-level ones.
- Ternary acceptable forms:
  `alpha = (aLongBooleanExpression) ? beta : gamma;` or with each part on its own
  wrapped line.
- K&R (Egyptian) braces: opening brace at end of the same line.
- Whitespace: spaces around operators, after Java reserved words, after commas,
  around colons used as operators (not `switch x:`), after semicolons in `for`.
- Separate logical units within a block with one blank line.

## Statements

### Package and imports

- Every class must belong to a package, placed in the matching directory.
- Consistent import ordering (alphabetical within groups; static imports first).
- No wildcard imports — list every imported class explicitly.

### Types, variables

- Array specifier attaches to the type: `int[] a = new int[20];` (not `int a[]`).
- Initialize variables where declared; declare in the smallest scope possible.
- Class variables must never be public (applies to instance/member variables
  with behavior; constants are exempt).

### Loops and conditionals

- Always wrap loop bodies in braces, even single statements.
- Conditional on its own line with the body braced:
  `if (isDone) { doCleanup(); }` (never `if (isDone) doCleanup();`).

## Comments and Javadoc

- All comments in English (American spelling).
- Header Javadoc for ALL public classes and public methods. Exemptions:
  1. getters/setters
  2. overriding methods (when the parent Javadoc applies as-is)
  3. test classes/methods
- Javadoc form:
  - Opening `/**` on its own line; subsequent `*` aligned; space after each `*`.
  - First sentence is a short summary starting with a verb: `Returns ...`,
    `Sends ...`, `Adds ...` (not `Return`/`Returning`).
  - Empty line between description and parameter section.
  - Punctuation after each parameter description.
  - No blank line between the doc block and the member.
  - `@return` omitted if nothing returned or obvious; `@param`s either for all
    parameters or none (self-explanatory names may skip them).
- One-line class-member Javadoc allowed: `/** Number of connections */ private int c;`
- Comment indentation must match surrounding code; trailing comments allowed.

## Procedure

1. Check the code against every rule above.
2. Fix violations, keeping changes minimal.
3. Prefer the simplest compliant formulation; do not over-refactor code that
   already complies.