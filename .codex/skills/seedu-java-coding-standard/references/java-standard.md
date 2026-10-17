# SE-EDU Java standard checklist

Source of truth: [SE-EDU Java coding standard (basic + intermediate)](https://se-education.org/guides/conventions/java/intermediate.html). Use the Google Java Style Guide only for topics not covered there.

## Naming

- Use lower-case package names organized by project and logical component.
- Name classes and enums with nouns in PascalCase.
- Name variables in camelCase and methods with verbs in camelCase.
- Name constants in SCREAMING_SNAKE_CASE.
- Keep abbreviations and acronyms lower case when they form part of a name, such as `exportHtmlSource`.
- Write names in English. Give wider-scope variables more descriptive names; short scratch names such as `i` are acceptable for very small scopes.
- Name booleans so they read as booleans, preferably with prefixes such as `is`, `has`, `was`, `can`, or `should`.
- Use plural names for collections and arrays of objects.

## Layout

- Indent with four spaces, never tabs.
- Keep lines below the 120-character hard limit and aim for fewer than 110 characters.
- Indent wrapped lines eight spaces beyond the parent line. Break after commas and before operators, and prefer higher-level breaks.
- Keep a method or constructor name attached to its opening parenthesis.
- Use K&R braces: place the opening brace on the declaration or control-statement line.
- Always use braces for loop and conditional bodies, including single statements.
- Surround operators with spaces; put spaces after Java keywords, commas, and `for` semicolons.
- Separate logical units in a block with one blank line.
- Format `if`/`else`, loops, `switch`, and `try`/`catch` consistently. Mark intentional switch fall-through with `// Fallthrough`.

## Packages, imports, types, and variables

- Put every class in a package and keep its path consistent with the package declaration.
- Keep imports ordered consistently, explicit, minimal, and free of wildcard imports.
- Attach array brackets to the type, such as `int[] values`.
- Declare variables in the smallest useful scope and initialize them where declared when a valid initial value exists.
- Do not expose mutable class variables as `public`; use encapsulation. Constants are exempt.

## Comments and Javadocs

- Write comments in English using American spelling and avoid local slang.
- Write descriptive header comments for every class and public method, except getters/setters, tests, and overrides whose inherited Javadoc applies exactly.
- Use `/**` on its own line and start with a short summary sentence in the correct verb form, such as `Returns`, `Adds`, or `Creates`.
- Leave one blank Javadoc line between the description and tags. Align stars and keep one space after each `*`.
- End parameter and return descriptions with punctuation.
- Include `@param` for all parameters or omit all of them when every parameter is already self-explanatory.
- Use `@inheritDoc` when an override needs to reuse and extend inherited documentation.
- Indent comments with the code they describe. Do not add comments that merely translate obvious code into English.

## Review outcome

- Check every modified Java line and enough surrounding code to catch structural violations.
- Verify source paths after package changes.
- Compile with the project's required Java version and run focused tests.
- Distinguish mandatory SE-EDU rules from optional readability improvements in the review report.
