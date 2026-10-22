# SE-EDU Java standard checklist

This checklist paraphrases the [SE-EDU Java coding standard (basic + intermediate)](https://se-education.org/guides/conventions/java/intermediate.html), accessed 2026-08-31. Apply every rule below unless a user instruction or stricter repository rule takes precedence. For topics not addressed here, use the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).

## Naming

- Write package names in lowercase. For a school project, begin the package hierarchy with the group or project name, followed by logical components such as `todobuddy.ui`; do not imply that NUS produced the code with names such as `edu.nus.comp.*`.
- Name classes and enums with English nouns in PascalCase, for example `AudioSystem`.
- Name variables in English camelCase.
- Name constants in `SCREAMING_SNAKE_CASE`. Constants belonging to one group should share a prefix, such as `COLOR_RED` and `COLOR_BLUE`.
- Name methods with English verbs in camelCase, for example `computeTotalWidth()`.
- Test methods may use `featureUnderTest_testScenario_expectedBehavior()`. The third component, or both the second and third components, may be omitted when the test's coverage makes them unnecessary.
- Treat an acronym as a normal word inside an identifier: prefer `exportHtmlSource()` and `openDvdPlayer()` over capitalized acronym blocks.
- Match name length to scope: use descriptive names for broad scopes and short scratch names only for a few nearby lines. Conventional scratch names include `i`, `j`, `k`, `m`, `n`, `c`, and `d`.
- Make boolean variables and methods read as predicates, preferably with prefixes such as `is`, `has`, `was`, `can`, or `should`. A boolean setter takes the form `setFound(boolean isFound)`.
- Use plural names for collections and arrays, such as `points` or `values`.
- Use `i` as the first loop or iterator variable; reserve `j`, `k`, and later letters for nested loops.

## Layout and whitespace

- Indent with 4 spaces, never tabs.
- Aim to keep lines below 110 characters and never exceed 120 characters. Wrap at a readable point rather than accepting an IDE's wrapping blindly.
- Indent a continuation 8 spaces beyond its parent line.
- When wrapping, normally break after a comma and before an operator. Treat `.`, the `&` in a type bound, and `|` in a multi-catch as operators for this purpose.
- Keep a method or constructor name attached to its opening `(`.
- Prefer a higher-level expression break over a break nested more deeply inside parentheses.
- Keep a short ternary on one line. For a wrapped ternary, place `?` and `:` on their own consistently indented continuation lines.
- Use K&R braces: the opening brace stays on the declaration or control-statement line, while `} else`, `} catch`, and `} finally` share a line.
- Put spaces around binary and ternary operators, after Java control keywords, after commas, and after semicolons in a `for` header. Surround a ternary colon with spaces; a switch label colon is exempt.
- Separate logical units inside a block with one blank line. Use comments only when they add useful intent.

## Statement forms

- Format methods as `public void someMethod() throws SomeException { ... }` using K&R braces.
- Put the bodies of `if`, `else if`, `else`, `for`, `while`, and `do` statements on separate lines and always enclose them in braces, even when the body has one statement.
- End a `do` loop as `} while (condition);`.
- Indent traditional `switch` labels one level inside the switch and their statements one further level. A switch rule or switch expression may use `case VALUE -> result`.
- Add an explicit `// Fallthrough` comment to every traditional switch case that intentionally reaches the next case without `break`.
- Format `try`, `catch`, and `finally` using K&R braces and keep each catch/finally clause attached to the preceding closing brace.

## Packages, imports, types, and variables

- Place every class in a package.
- Keep import ordering consistent with the rest of the project. Grouping commonly proceeds through static imports, `java`, `javax`, third-party libraries, and project-specific libraries, but consistency is the governing requirement.
- Import every used class explicitly; never use wildcard imports such as `java.util.*`. Keep imports minimal and remove unused ones.
- Attach array brackets to the type: use `int[] values`, not `int values[]`.
- Declare a variable in the smallest scope that needs it and initialize it at the declaration when a valid value is available. Leave it uninitialized rather than assigning a fake placeholder value.
- Do not expose class variables as `public`, except constants or fields of a behavior-free data class. Prefer encapsulation and access methods.

## Comments and Javadocs

- Write comments in English, using American spelling and avoiding local slang.
- Add descriptive Javadoc to every class and every public method. A public getter/setter, a test class or method, or an override whose inherited Javadoc applies exactly may omit a new comment.
- Begin a Javadoc block with `/**` on its own line. Align each subsequent `*`, include one space after it, and place the documented declaration immediately after the closing `*/`.
- Start a method Javadoc with a short summary sentence in third-person verb form, such as `Returns ...`, `Sends ...`, or `Adds ...`.
- Put a blank Javadoc line between the prose description and block tags. End each `@param`, `@return`, and `@throws` description with punctuation.
- Include either all `@param` tags or none. Omit them only when every parameter is already self-explanatory or explained in the prose.
- Omit `@return` for `void`, or when the return value is already obvious from the description.
- Use `{@inheritDoc}` when an override inherits documentation but needs additional or adjusted details.
- A field's Javadoc may be a single line, for example `/** Number of active connections. */`.
- Indent a comment to the same level as the code it describes. Trailing comments are allowed when clear and useful.

## Final review

Before completing Java work, check at minimum:

- Names follow their type-specific casing and grammatical form.
- Indentation is spaces-only, wrapping is readable, and no line exceeds 120 characters.
- Control-flow bodies use braces and intentional fallthrough is labeled.
- Imports are explicit, minimal, and consistently ordered.
- Variables use narrow scopes and valid initialization.
- Required class and public-method Javadocs are present and correctly formed.
