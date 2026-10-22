# SE-EDU Java coding standard: basic and intermediate rules

This project follows the [SE-EDU Java coding standard (basic + intermediate)](https://se-education.org/guides/conventions/java/intermediate.html), accessed 28 August 2026. Use the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) for topics not covered here.

## Naming

- Use lowercase package names. For a school project, begin with the group or project name and add logical subpackages; do not use an `edu.nus.comp` namespace.
- Name classes and enums with English PascalCase nouns.
- Name variables in English camelCase. Use longer names for wider scopes; short scratch names such as `i`, `j`, and `k` are suitable only for small scopes, with later letters used for nested loops.
- Name methods with English camelCase verbs.
- Name constants in `SCREAMING_SNAKE_CASE`; give related constants a common prefix.
- In identifiers, treat abbreviations and acronyms as words: use `exportHtmlSource`, not `exportHTMLSource`.
- Name booleans so they read as booleans, preferably with `is`, `has`, `was`, `can`, or `should`. A boolean setter takes a similarly named parameter, such as `setFound(boolean isFound)`.
- Use plural names for collections and arrays.
- Test methods may use `featureUnderTest_testScenario_expectedBehavior`, omitting the second or third portion when appropriate.

## Layout

- Indent with four spaces, never tabs.
- Keep lines below 110 characters when practical and never exceed 120 characters.
- Indent continuation lines eight spaces beyond the parent line. Break after commas and before operators (including `.`, `&` in type bounds, and `|` in multi-catch). Keep a method or constructor name attached to its opening parenthesis and prefer higher-level breaks.
- Use K&R braces: the opening brace remains on the declaration or control-statement line.
- Format `if`/`else`, `for`, `while`, `do`/`while`, `switch`, and `try`/`catch`/`finally` consistently with K&R style.
- In a colon-form switch, indent case statements one level and include `// Fallthrough` whenever a case intentionally has no `break`. Arrow-form switch cases and switch expressions are permitted.
- Put spaces around binary and ternary operators, after Java control keywords, after commas, around ternary colons, and after semicolons in `for` headers.
- Separate logical units within a block with one blank line. Avoid redundant blank lines.

## Packages, imports, types, and variables

- Put every class in a package whose directory structure matches its package name.
- Keep import ordering consistent, minimal, and explicit. Never use wildcard imports.
- Attach array brackets to the type (`int[] values`), not the variable.
- Initialize variables where they are declared and declare them in the smallest practical scope. If no valid initial value exists, leave the variable uninitialized instead of inventing a placeholder.
- Do not expose class variables as `public` unless the class is a behavior-free data class. Constants are exempt.

## Statements

- Always use braces for loop and conditional bodies, even for one statement.
- Put a conditional and its body on separate lines.

## Comments and Javadocs

- Write comments in English, using American spelling and no local slang.
- Add descriptive Javadocs to every class and public method. They may be omitted for getters/setters, tests, and overrides whose inherited documentation applies exactly.
- Start a Javadoc with `/**` on its own line. Begin with a short summary sentence phrased in third-person form such as “Returns”, “Sends”, or “Adds”. Align `*`, include one space after it, and place no blank line between the Javadoc and declaration.
- Put a blank Javadoc line between the description and block tags. End parameter, return, and throws descriptions with punctuation.
- Include `@param` for either all parameters or none; omit them only when every parameter is self-explanatory or already explained. Omit `@return` for `void` or when the return value is obvious from the main description. Use `{@inheritDoc}` when inherited documentation needs additions.
- A short member Javadoc may be written on one line.
- Indent comments with the code they describe. Trailing comments are allowed, but comments should add useful information.
