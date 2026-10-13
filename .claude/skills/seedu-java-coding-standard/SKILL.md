---
description: "Enforce the SE-Education intermediate Java coding standard on all Java code in this project."
---

# SE-Education Java Coding Standard (Intermediate)

This skill enforces the coding conventions from
<https://se-education.org/guides/conventions/java/intermediate.html>
on every Java file written or modified in this project.

## When to apply

Apply these rules whenever you **create, modify, or review** Java code in
this repository. Before reporting a code task as complete, verify the
touched files against this checklist.

## Rules checklist

### Naming

1. **Packages** -- all lowercase (e.g. `aglio.task`, not `Aglio.Task`).
2. **Classes / enums** -- PascalCase nouns (e.g. `Task`, `AudioSystem`).
3. **Variables** -- camelCase (e.g. `taskCount`).
4. **Constants** -- SCREAMING_SNAKE_CASE (e.g. `MAX_TASKS`, `COLOR_RED`).
5. **Methods** -- camelCase verbs (e.g. `getName()`, `computeTotal()`).
   Test methods: `featureUnderTest_testScenario_expectedBehavior()`.
6. **Acronyms in names** -- not all-caps (e.g. `exportHtmlSource`, not
   `exportHTMLSource`).
7. **Language** -- all identifiers in English.
8. **Name length vs. scope** -- longer names for wider scope; `i`, `j`, `k`
   are fine for loop indices.
9. **Booleans** -- prefix with `is`, `has`, `was`, `can`, `should`
   (e.g. `isDone`, `hasData`).
10. **Collections** -- plural names (e.g. `points`, `values`).
11. **Iterators** -- `i`, `j`, `k` for loop counters.
12. **Related constants** -- share a common prefix (e.g. `COLOR_RED`,
    `COLOR_GREEN`).

### Layout

13. **Indentation** -- 4 spaces, never tabs.
14. **Line length** -- hard limit 120 characters, soft limit 110.
15. **Continuation indent** -- 8 spaces (double the normal indent).
16. **Line-break placement** -- break after commas, before operators
    (including `.`); keep method name attached to `(`.
17. **Ternary formatting** -- single line or three-line split.
18. **Brace style** -- K&R (opening brace on same line).
19. **Method format** -- `public void foo() throws E {`.
20. **if-else format** -- K&R style, `} else {` on one line.
21. **for-loop format** -- `for (init; cond; update) {`.
22. **while / do-while format** -- K&R style.
23. **switch format** -- K&R style; explicit `// Fallthrough` comment when
    no `break`; arrow-syntax is acceptable.
24. **try-catch format** -- K&R style.
25. **Whitespace** -- spaces around operators, after commas, after reserved
    words before `(`, after `;` in for-loops.
26. **Blank lines** -- one blank line between logical sections.

### Statements

27. **Package declaration** -- every class must belong to a named package.
28. **Import order** -- static imports first, then `java`, `javax`, `org`,
    `com`, others. Each group separated by a blank line.
29. **No wildcard imports** -- always import classes explicitly.
30. **Array brackets on type** -- `int[] a`, not `int a[]`.
31. **Declare where used** -- initialize variables at declaration; minimise
    scope.
32. **No public fields** -- unless the class is a pure data container
    (constants excluded).
33. **Loop body braces** -- always use `{ }`, even for single statements.
34. **Condition on own line** -- no `if (x) doIt();` one-liners.
35. **Conditional braces** -- always use `{ }`, even for single statements.

### Comments

36. **Comment language** -- English, American spelling.
37. **Javadoc on public API** -- required for all public classes and
    non-trivial public methods (getters/setters and overrides with
    identical semantics may omit).
38. **Javadoc format** -- `/** ... */`, first sentence is the summary,
    verb-phrase (`Returns ...`, `Sends ...`), `@param` / `@return` /
    `@throws` as needed, no blank line before the declaration.
39. **Comment indentation** -- align with surrounding code.

## How to use this skill

When reviewing or writing code, walk through each section above and
fix any violations. Common mistakes to watch for:

- Missing `package` declaration (Rule 27).
- Wildcard imports `import java.util.*` (Rule 29).
- Missing braces on single-line `if` / `for` (Rules 33--35).
- Boolean variables without `is`/`has`/`was` prefix (Rule 9).
- Acronyms in ALL CAPS within identifiers (Rule 6).
- Tabs instead of spaces (Rule 13).
- Lines exceeding 120 characters (Rule 14).
