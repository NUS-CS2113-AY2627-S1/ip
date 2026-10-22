# SE-EDU Java Coding Standard (Basic + Intermediate)

Source: https://se-education.org/guides/conventions/java/intermediate.html

## Naming

- Use lowercase package names organized by project and logical grouping.
- Use PascalCase noun names for classes and enums.
- Use camelCase verb names for methods and camelCase names for variables.
- Use SCREAMING_SNAKE_CASE for constants; give related constants a common prefix.
- Keep names in English. Do not capitalize abbreviations inside names.
- Give wide-scope variables descriptive names; short scratch names are acceptable only in small scopes.
- Prefix boolean names with words such as `is`, `has`, `was`, `can`, or `should`.
- Use plural names for collections and arrays.
- Use `i` for the outer loop and `j`, `k`, and so on only for nested loops.
- Test names may use `featureUnderTest_testScenario_expectedBehavior`.

## Layout

- Indent with four spaces, never tabs.
- Keep lines under 120 characters and preferably under 110.
- Indent wrapped lines eight spaces beyond their parent line.
- Break after commas and before operators; favor high-level expression breaks.
- Keep a method or constructor name attached to its opening parenthesis.
- Use K&R braces for methods, conditionals, loops, `switch`, and `try` statements.
- Put spaces around operators, after keywords, after commas, and after semicolons in `for` headers.
- Separate logical units inside a block with a blank line.

## Statements and declarations

- Put every class in a suitable package when the project's source layout and launch contract support packages.
- Keep import ordering consistent, list imports explicitly, and remove unused imports.
- Attach array brackets to the type, such as `String[] tasks`.
- Initialize variables at declaration and declare them in the smallest practical scope.
- Do not expose class variables publicly unless the class is a behavior-free data class; constants are exempt.
- Always use braces around loop and conditional bodies.
- Put conditional bodies on lines separate from their conditions.
- Mark intentional `switch` fall-through with `// Fallthrough`.

## Comments

- Write comments in English using American spelling and no local slang.
- Add descriptive Javadoc to public classes and public methods, except getters/setters, exact overrides, and test code.
- Begin Javadoc with a short summary sentence such as `Returns ...` or `Marks ...`.
- Use standard aligned Javadoc formatting and punctuation in tag descriptions.
- Indent comments with the code they describe.

For any Java-style topic not covered here, follow the Google Java Style Guide.
