# Java coding standard

This is the project checklist derived from the [SE-EDU Java coding standard, basic and intermediate rules](https://se-education.org/guides/conventions/java/intermediate.html). The linked page is authoritative. Use the Google Java Style Guide for topics it does not cover.

## Naming

- Write package names in lower case. For a school project, start with the group or project name, followed by logical package names. Do not use `edu.nus.comp` or a similar name.
- Name classes and enums with English nouns in PascalCase.
- Name variables in English using camelCase.
- Name constants using SCREAMING_SNAKE_CASE. Give associated constants a common prefix.
- Name methods with English verbs in camelCase.
- Test methods may use `featureUnderTest_testScenario_expectedBehavior()`. The last part, or the last two parts, may be omitted when they add no useful detail.
- Treat abbreviations and acronyms as words inside names, such as `exportHtmlSource()` and `openDvdPlayer()`.
- Use longer names for larger scopes. Short scratch names such as `i`, `j`, `k`, `m`, `n`, `c`, and `d` are suitable only for small scopes.
- Name booleans so they read as booleans. Prefer prefixes such as `is`, `has`, `was`, `can`, and `should`.
- Name a boolean setter and its parameter in the form `setFound(boolean isFound)`.
- Use plural names for collections and arrays.
- Use `i` for an iterator or loop index. Reserve `j`, `k`, and later letters for nested loops.

## Layout

- Indent with four spaces and do not use tabs.
- Keep lines below the 120-character hard limit and aim for less than 110 characters.
- Indent a wrapped continuation by eight spaces more than its parent line.
- Choose line breaks for readability. Break after a comma and before an operator, including `.`, `&` in a type bound, and `|` in a catch clause.
- Keep a method or constructor name attached to its opening parenthesis.
- Prefer a higher-level expression break over a lower-level break.
- Keep a short ternary expression on one line. For a wrapped ternary, put `?` and `:` on separate continuation lines.
- Use K&R braces: put the opening brace at the end of the declaration or control-statement line.
- Format method declarations and `if`, `else`, `for`, `while`, `do-while`, `switch`, `try`, `catch`, and `finally` blocks consistently with K&R style.
- In a colon-style `switch`, indent case labels once and their statements twice. Add `// Fallthrough` whenever execution intentionally continues into the next case.
- Put spaces around operators, after Java reserved words, after commas, around a ternary colon, and after each semicolon in a `for` header.
- Separate logical units within a block with one blank line.

## Packages, imports, types, and variables

- Put every class in a package.
- Keep import ordering consistent. Group static imports, `java`, `javax`, third-party, project, and other imports consistently, with blank lines between groups when present.
- Import each class explicitly. Do not use wildcard imports.
- Attach array brackets to the type, such as `int[] values`, not to the variable name.
- Initialise variables where declared and declare them in the smallest practical scope. If no valid initial value exists, leave the variable uninitialised rather than assigning a false placeholder.
- Do not expose class variables as `public`, except constants or fields in a data class with no behaviour.

## Loops and conditionals

- Wrap every loop body in braces, including a one-statement body.
- Put a conditional body on the line after its condition.
- Wrap every conditional body in braces, including a one-statement body.

## Comments and Javadoc

- Write comments in English using American spelling and no local slang.
- Add descriptive Javadoc to every class and public method. Javadoc may be omitted for getters and setters, exact overrides whose inherited documentation applies, and test classes or methods.
- Start a method's first Javadoc sentence with a third-person verb such as `Returns`, `Sends`, or `Adds`. Make it a short summary.
- Put `/**` and `*/` on their own lines for class and method Javadoc. Align each `*` and put one space after it.
- Separate the description from Javadoc tags with one blank line. Do not add a blank line between the Javadoc block and its declaration.
- Capitalise and punctuate every parameter, return, and exception description.
- Include either all `@param` tags or none. Omit them only when every parameter is self-explanatory or already explained in the main description.
- Omit `@return` for `void` methods or when the return value is already obvious from the description.
- Use `{@inheritDoc}` when inherited documentation needs additions or behaviour-specific changes.
- A field Javadoc comment may use one line when suitable.
- Indent comments to match the code they describe. Trailing comments are allowed when they remain clear.
