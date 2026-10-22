---
name: seedu-java-coding-standard
description: SE-EDU Java coding standard (intermediate). Use whenever writing, editing, reviewing or refactoring any Java code in this project, to keep naming, layout, statements and comments compliant.
---

# SE-EDU Java coding standard (intermediate)

Source: https://se-education.org/guides/conventions/java/intermediate.html
Apply every rule below to all Java code, and check existing code you touch.

## Naming
- Packages: all lower case (e.g. `eric`, `eric.task`).
- Classes/enums: nouns in PascalCase (`Task`, `AudioSystem`).
- Variables: camelCase. Constants: `SCREAMING_SNAKE_CASE`; related constants share a prefix (`COLOR_RED`, `COLOR_BLUE`).
- Methods: verbs in camelCase (`getName()`, `computeTotalWidth()`).
- Test methods: `featureUnderTest_testScenario_expectedBehavior()`, e.g. `sortList_emptyList_exceptionThrown()`.
- No uppercase abbreviations: `exportHtmlSource()`, not `exportHTMLSource()`.
- All names in English.
- Name length follows scope: large scope = long name; small scope = short name. Scratch variables: `i, j, k, m, n` for ints, `c, d` for chars.
- Booleans use affirmative prefixes: `isSet`, `isVisible`, `hasData`, `wasOpen`; methods `hasLicense()`, `canEvaluate()`; setters `setFound(boolean isFound)`.
- Collections have plural names (`points`, `values`).
- Loop iterators: `i`, then `j`, `k` for nested loops only.

## Layout
- Indent 4 spaces, never tabs.
- Line length: soft limit 110, hard limit 120.
- Wrapped lines are indented 8 spaces relative to the start of the statement.
- Break after commas; break before operators (including `.`, `&`, `|`); keep a method name attached to its `(`; prefer higher-level breaks.
- K&R / Egyptian braces: opening brace on the same line; `} else {` and `} else if (...) {` on one line.
- Spaces around operators (`a = (b + c) * d;`), after reserved words (`while (true) {`), after commas and after `;` in `for`.
- One blank line between logical blocks within methods.
- `switch`: add `// Fallthrough` when a case has no `break`; arrow form `case ABC -> method("1");` is fine.
- `try { ... } catch (Exception exception) { ... } finally { ... }` (name the caught variable `exception`).

## Statements
- Every class is in a package (none in the default package).
- Imports ordered: static, `java`, `javax`, `org`, `com`, `javafx`, other. No wildcard imports (`import java.util.*;`).
- Array brackets attach to the type: `int[] a`, not `int a[]`.
- Declare and initialise variables at declaration, in the smallest scope.
- Fields are never `public`, except in pure data classes.
- Loop bodies and `if`/`else` bodies always use braces, even for one statement, and the statement goes on its own line.

## Comments
- English only, American spelling, no slang.
- Every class and every public method needs a header comment. Optional for getters/setters, overriding methods whose parent doc applies exactly, and tests.
- Javadoc: multi-line form has `/**` on its own line, then ` * ` lines aligned with the first `*`; first sentence is a short summary; start method summaries with a third-person verb ("Returns ...", "Adds ...", "Sends ..."); blank line between description and tags; punctuation after each `@param` description; no blank line between the comment and the declaration; `@return` may be omitted if obvious; use `@param` for all parameters or none; use `{@inheritDoc}` for overrides that add to the parent doc.
- Single-line form for members: `/** Comment */ private int variable;` (on its own line above the member).
- Indent comments to the code they describe; trailing comments are allowed (`process(x); // why`).
