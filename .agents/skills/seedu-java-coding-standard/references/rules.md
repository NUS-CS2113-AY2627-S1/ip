# SE-EDU Java coding rules: basic and intermediate

This is an operational summary of the
[authoritative basic + intermediate standard](https://se-education.org/guides/conventions/java/intermediate.html).
Consult the authoritative page if a rule's application is ambiguous. Use the Google Java Style Guide for topics
not covered here.

## Naming

- Use lowercase package names. For a school project, start package names with the project or group name, followed
  by logical group names; do not imply that NUS produced the code.
- Use English noun names in `PascalCase` for classes and enums.
- Use English verb names in `camelCase` for methods.
- Use `camelCase` for variables and `SCREAMING_SNAKE_CASE` for constants.
- Keep acronyms lowercase within identifiers, such as `exportHtmlSource`, not `exportHTMLSource`.
- Give wide-scope variables descriptive names; short scratch names such as `i`, `j`, and `k` are acceptable in a
  small scope. Reserve `j` and `k` for nested loops.
- Name booleans to read as booleans, preferably with prefixes such as `is`, `has`, `was`, `can`, or `should`.
- Use plural names for collections and arrays.
- Give associated constants a common prefix.
- Test methods may use `featureUnderTest_testScenario_expectedBehavior`, omitting later parts when unnecessary.

## Layout

- Indent using four spaces, never tabs.
- Prefer lines shorter than 110 characters and never exceed 120 characters. Indent continuation lines eight spaces
  beyond their parent line.
- Wrap for readability: break after commas and before operators, including `.`, `&`, and `|`. Keep a method or
  constructor name attached to its opening parenthesis and prefer higher-level expression breaks.
- Use K&R braces. Always use braces for loop and conditional bodies, even for one statement, and put the body on a
  separate line.
- Put spaces around operators, after Java keywords and commas, around ternary colons, and after semicolons in
  `for` headers.
- Separate logical units within a block with one blank line.
- Follow the standard forms for methods, `if`/`else`, loops, `switch`, and `try`/`catch`. Add `// Fallthrough` when
  a colon-style switch case intentionally falls through.

## Packages, imports, types, and variables

- Put every class in a package.
- Keep import ordering consistent. List each imported class explicitly; never use wildcard imports.
- Attach array brackets to the type, such as `Task[] tasks`.
- Initialize variables where declared and declare them in the smallest possible scope. If no valid initial value
  exists, leave the variable uninitialized rather than using a fake value.
- Do not make class variables public unless the class is a behavior-free data class; constants are exempt.

## Comments and Javadoc

- Write comments in English using American spelling, and indent them with the surrounding code.
- Add descriptive Javadoc to every public class and public method, except getters/setters, test code, and overrides
  whose inherited documentation applies exactly.
- Start Javadoc with a short summary sentence using third-person verbs such as `Returns`, `Adds`, or `Sends`.
- Use standard Javadoc layout: `/**` on its own line, aligned `*` characters, no blank line before the declaration,
  and a blank line between the description and tags.
- End parameter descriptions with punctuation. Include `@param` for either all parameters or none; omit all when
  every parameter is self-explanatory or already covered by the description.
- Omit `@return` for `void` methods or when the return value is already obvious from the description.
