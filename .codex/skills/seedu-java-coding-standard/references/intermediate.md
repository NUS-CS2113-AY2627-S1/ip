# SE-EDU Java Coding Standard: Intermediate Rules

Source: <https://se-education.org/guides/conventions/java/intermediate.html>.
Use the linked guide as the authority for nuances and topics not summarized
here; use the Google Java Style Guide for topics it does not cover.

## Naming

- Package names are lowercase. For student projects, use the project or group
  name as the root package; do not use NUS-owned namespaces.
- Classes and enums are PascalCase nouns. Methods are camelCase verbs.
- Variables are camelCase; constants are `UPPER_SNAKE_CASE`.
- Use English names. Use plural names for collections and `i`, `j`, or `k`
  only for short-lived loop indices.
- Boolean variables and methods start with forms such as `is`, `has`, `was`,
  `can`, or `should`. Boolean setters use `setX(boolean isX)`.
- Acronyms inside names are not all uppercase (for example, `exportHtmlSource`).

## Layout and statements

- Indent with four spaces. Keep lines at 120 characters or fewer, aiming for
  110 or fewer. Indent wrapped lines eight spaces beyond their parent.
- Use K&R braces. Put braces around every loop and conditional body, even when
  it has one statement.
- Put spaces around operators, after commas and semicolons in `for` clauses,
  and after reserved words. Separate logical units with one blank line.
- Break long lines after commas or before operators; prioritize readable,
  higher-level breaks. Keep a method name with its opening parenthesis.
- Put every class in a package. Keep import ordering consistent and import
  classes explicitly; never use wildcard imports.
- Attach array brackets to the type (`String[] names`), initialize variables
  at declaration where possible, and declare them in the smallest useful
  scope. Do not expose mutable fields publicly.
- Use an explicit `// Fallthrough` comment for an intentional switch
  fall-through.

## Comments and Javadoc

- Write comments in English, with American spelling and no local slang.
- Give every public class and public method a descriptive header comment,
  except simple getters/setters, exact overrides, and test code.
- Javadoc starts with a short third-person summary such as `Returns ...` or
  `Adds ...`. Use `@param`, `@return`, and `@throws` when they add useful
  information; describe parameters with sentence punctuation.
- Keep comments aligned with the code they describe. Prefer comments that
  explain intent or non-obvious decisions over comments that repeat code.
