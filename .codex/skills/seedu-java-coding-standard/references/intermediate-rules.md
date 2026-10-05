# SE-EDU Java basic and intermediate rules

Source: <https://se-education.org/guides/conventions/java/intermediate.html>

Use this checklist when writing or reviewing Java code in this project. For topics
not covered here, follow the Google Java Style Guide as directed by the source.

## Naming

- Use lowercase package names.
- Use PascalCase nouns for class and enum names.
- Use camelCase for variables and verb-based methods.
- Use SCREAMING_SNAKE_CASE for constants.
- Keep acronyms in normal casing within names, such as `exportHtmlSource`.
- Use English names and American spelling in comments.
- Name booleans with prefixes such as `is`, `has`, `was`, or `should`.
- Use plural names for collections.
- Use descriptive names for variables with a large scope.

## Layout

- Indent with four spaces, never tabs.
- Keep lines at or below 120 characters; prefer below 110.
- Indent wrapped lines by eight spaces relative to the parent line.
- Use K&R braces.
- Separate logical units in a block with one blank line.
- Surround operators, commas, and relevant keywords with appropriate spaces.

## Statements and variables

- Put every class in a package and use explicit, consistently ordered imports.
- Attach array brackets to the type, such as `int[] values`.
- Initialize variables where declared and keep them in the smallest possible scope.
- Keep class variables private and expose behavior through methods.
- Wrap every loop and conditional body in braces.
- Put conditional bodies on separate lines.

## Comments and Javadocs

- Write descriptive header comments for all public classes and public methods.
- Start Javadoc summaries with verbs such as `Returns`, `Adds`, or `Computes`.
- Include useful `@param`, `@return`, and `@throws` tags.
- Keep comments accurate, concise, and properly indented.
