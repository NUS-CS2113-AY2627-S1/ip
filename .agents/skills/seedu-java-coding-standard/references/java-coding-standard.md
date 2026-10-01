# Java coding standard checklist

Source: [se-edu Java coding standard (basic + intermediate)](https://se-education.org/guides/conventions/java/intermediate.html). For subjects the source does not cover, consult the Google Java Style Guide linked there.

## Naming

- Use lower-case package names. For a school project, start the package hierarchy with the project or group name.
- Name classes and enums with noun-based PascalCase names.
- Name variables in camelCase, constants in SCREAMING_SNAKE_CASE, and methods with verb-based camelCase names.
- In compound names, write acronyms like ordinary words rather than all-uppercase fragments.
- Use English names. Give wide-scope variables descriptive names; reserve short names such as `i` and `j` for small loop scopes.
- Name boolean values and queries so they read as conditions, normally with prefixes such as `is`, `has`, `was`, `can`, or `should`.
- Use plural names for collections and arrays. Use `i` for the outer loop and later iterator letters for nested loops.
- Test methods may use `featureUnderTest_scenario_expectedBehavior`, with later portions omitted when unnecessary.

## Layout and whitespace

- Indent with four spaces and never with tabs.
- Aim for at most 110 characters per line and never exceed 120. Indent continuation lines eight spaces beyond their parent indentation.
- When wrapping, normally break after commas and before operators or method-chain dots. Keep a method name with its opening parenthesis and prefer high-level expression breaks.
- Use K&R braces: opening braces stay on the statement or declaration line.
- Put spaces around operators, after Java keywords and commas, and after semicolons in `for` headers. Surround ternary colons with spaces.
- Separate distinct logical units inside a block with one blank line.
- Use the standard multiline layouts for methods, `if`/`else`, loops, `switch`, and `try`/`catch`/`finally` blocks.
- Mark intentional traditional-switch fallthrough with `// Fallthrough`.

## Packages, imports, declarations, and control flow

- Put every class in a package.
- Keep imports ordered consistently, explicit rather than wildcarded, minimal, and free of unused entries.
- Attach array brackets to the type, for example `int[] values`.
- Declare variables in the narrowest useful scope and initialize them at declaration when a genuine initial value is available.
- Keep mutable class fields non-public unless the class is deliberately a behavior-free data holder. Constants may be public when appropriate.
- Always use braces around loop and conditional bodies, including one-statement bodies. Put the body on a separate line.

## Comments and Javadocs

- Write comments in clear English using American spelling, without local slang.
- Add descriptive Javadocs to every public class and public method. Javadocs may be omitted for straightforward getters/setters, test code, and overrides whose inherited documentation remains fully accurate.
- Start a Javadoc with a short summary sentence describing what the API does. Use complete, punctuated parameter descriptions when parameter tags add value; either document every parameter or omit all redundant parameter tags.
- Include `@return` and `@throws` when they add information not already obvious from the summary.
- Align and indent comments with the code they describe. Prefer comments that explain purpose or reasoning instead of restating operations.
