# SE-EDU Java coding checklist

This project follows the SE-EDU "Java coding standard (basic + intermediate)" guide:
<https://se-education.org/guides/conventions/java/intermediate.html>

## Naming

- Package names are lower-case. Use the project/group name as the root package followed by logical subpackages.
- Class and enum names are nouns in PascalCase.
- Method names are verbs in camelCase.
- Variables use camelCase; constants use `SCREAMING_SNAKE_CASE`.
- Test methods may use `featureUnderTest_testScenario_expectedBehavior`.
- Do not capitalize abbreviations as words (`openDvdPlayer`, not `openDVDPlayer`).
- Names and comments use English. Use descriptive names for long-lived or wide-scope values; short names such as `i`, `j`, and `k` are for small-scope scratch or nested-loop indices.
- Boolean variables and methods should sound like predicates (`isDone`, `hasData`, `canEvaluate`). Boolean setters use `setFound(boolean isFound)` style.
- Collection names are plural (`tasks`, `values`). Related constants share a common prefix.

## Layout and whitespace

- Indent with four spaces, never tabs. Use K&R (Egyptian) braces.
- Keep every line no longer than 120 characters; aim for fewer than 110. Wrap long lines at readable boundaries: after commas, before operators, and at higher-level expressions. Keep a method/constructor name attached to its opening parenthesis. Continuation indentation is eight spaces beyond the parent line.
- Put method declarations in the form `public void method() throws Exception {`.
- Use standard brace layouts for `if`/`else`, `for`, `while`, `do`/`while`, `switch`, and `try`/`catch`/`finally`.
- Include `// Fallthrough` for an intentional switch fall-through. Arrow-style switch cases are also allowed.
- Put spaces around operators, after Java reserved words, after commas, around binary/ternary colons, and after semicolons in `for` headers.
- Separate logical units in a block with one blank line.

## Statements and declarations

- Put every class in a package.
- Keep import ordering consistent within the project, and import classes explicitly rather than using wildcard imports.
- Attach array brackets to the type (`int[] values`).
- Initialize variables where declared when a valid initial value is available, and declare them in the smallest scope possible. Leaving a value uninitialized is preferable to inventing a placeholder when initialization cannot yet be meaningful.
- Keep class fields non-public unless the class is a behavior-free data class; constants are exempt.
- Always use braces around loop and conditional bodies, including one-statement bodies. Put a conditional expression on its own line rather than writing `if (condition) statement;`.

## Comments and Javadocs

- Write comments in English using American spelling, without local slang.
- Add a descriptive header comment to every public class and public method. It may be omitted for getters/setters, overriding methods whose inherited documentation applies exactly, and test code.
- Use `/** ... */` Javadocs. Put the opening marker on its own line, align `*`, leave a blank line before tags, and do not put a blank line between the Javadoc and its declaration. The first sentence is a concise summary; method summaries should begin with "Returns", "Adds", "Sends", or another action verb. Add `@param`, `@return`, and `@throws` tags when they clarify behavior, and punctuate descriptions.
- Indent comments to match the code they describe.
