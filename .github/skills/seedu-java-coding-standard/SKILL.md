# seedu-java-coding-standard

Apply the SE-EDU intermediate Java coding standard from:
https://se-education.org/guides/conventions/java/intermediate.html

## Required rules

- Use lower camel case for variables and methods, upper camel case for classes and interfaces,
  and upper snake case for constants.
- Use descriptive names. Boolean variables and methods should read like boolean expressions
  (`isDone`, `hasItems`, `canExecute`).
- Use four spaces for indentation, never tabs.
- Put a space before an opening brace and keep braces on the same line as the declaration or
  control statement. Always use braces for conditionals and loops.
- Keep lines at 120 characters or fewer where practical. Wrap continuation lines with eight
  additional spaces relative to the parent line.
- Use consistent spacing around operators, after commas, and around control-statement keywords.
- Keep one statement per line and use blank lines to separate logical sections.
- Place package declarations before imports, and keep imports organized and free of unused entries.
- Write comments in English using American spelling. Prefer code that explains itself; comments
  should explain intent or non-obvious reasoning rather than restate code.
- Add descriptive Javadoc to every public class and public method. Javadoc may be omitted for
  getters/setters, exact overrides whose inherited documentation applies, and test code.
  Include `@param`, `@return`, and `@throws` tags when applicable.
- Use `@Override` for overridden methods and avoid unnecessary field qualification such as
  `this.` when it does not improve clarity.

## Application

Review all changed Java files against these rules before completing a task. Preserve behavior
unless a change is required to make the code conform to the standard.
