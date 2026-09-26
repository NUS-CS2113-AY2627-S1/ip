# Project context

This repository is a starter template for a greenfield Java project used in an introductory software engineering course in an undergraduate computer science program. Students use it as the starting point for their own projects.

# Default user context

Unless the user says otherwise, assume that you are assisting a student working on a project in this repository. If the user identifies themselves as an instructor or another project stakeholder, adapt your response to that role.

# Student profile

* Prior knowledge: Basic Java and OOP concepts.
* Level of programming experience: Year 2 NUS CEG undergrad
* IDE and level of expertise: Year 2 NUS CEG undergrad

# Guidance for interacting with users

* Explain the rationale for significant actions: what you did and why.
* Keep explanations brief but instructive, supporting learning through responsible use of AI. For example:

  * When suggesting a Git command, briefly explain what it does.
  * Add explanatory Javadoc comments to all classes and to nontrivial methods and fields when their purpose or behavior is not obvious.
  * Make generated code as self-explanatory as possible, and include explanatory comments where they improve understanding.
  * When faced with a design choice, choose the simplest option that is sufficient for the requirements, while briefly explaining relevant more advanced alternatives.

# Project-specific requirements

## Java version:

Ensure that Java 25 is used when running the application or build tasks. On macOS, use `sdk use java 25.0.3.fx-zulu` to switch to Java 25 if needed.

## Git

**Project-specific skill**: Follow the **seedu-git-standard** (defined in `SEEDU_GIT_STANDARD.md`).

All commits must adhere to SE-Edu Git conventions:
- **Subject**: Imperative mood, capitalized, 50 chars (max 72), no period
- **Body** (for non-trivial changes): Blank line after subject, wrapped at 72 chars, explain WHAT and WHY

Additional guidelines:
- Use lightweight tags unless the user requests an annotated tag
- When proposing or creating a commit message, include enough detail to explain the rationale for the change
- Do not commit or push unless explicitly asked

For complete standards, see `SEEDU_GIT_STANDARD.md`.

## Java Code

**Project-specific skill**: Follow the **seedu-java-coding-standard** (defined in `SEEDU_JAVA_CODING_STANDARD.md`).

All Java code must adhere to SE-Edu Java conventions including:
- **Naming**: PascalCase for classes, camelCase for variables/methods, SCREAMING_SNAKE_CASE for constants
- **Abbreviations**: Use mixed case (e.g., `exportHtmlSource()` not `exportHTMLSource()`)
- **Booleans**: Use meaningful prefixes like `is`, `has`, `was`, `can` (e.g., `isFound`, `hasLicense()`)
- **Comments**: Javadoc for all public classes/methods, explain WHY not WHAT
- **Formatting**: 4-space indentation, max 120 chars per line, standard Java style (braces on same line)

For complete standards, see `SEEDU_JAVA_CODING_STANDARD.md` or https://se-education.org/guides/conventions/java/intermediate.html

## User-facing messages

All new or revised messages shown by MoistBot must use a courteous,
professional butler voice. Keep the wording clear and concise: acknowledge
successful requests calmly, begin corrective feedback with an appropriate
polite phrase such as "Please" or "My apologies", and always explain the next
action the user can take. Preserve this tone consistently across UI output,
validation errors, and unexpected-error fallbacks.

## Post-update verification

After every code update, agents **must** complete both steps below before
finalising the work. If a test case fails, stop the test session immediately
and report its actual and expected outputs.

1. Update `test/ui-test-plan.md` if needed to match the new or changed behaviour.
   - Add or modify relevant cases whenever a change introduces, fixes, or refactors observable behaviour, including error handling. Each case must state its aim, inputs, and expected output.
   - If a change is not observable through the UI, add or update the closest automated test that verifies the changed code path, and record why no UI test-plan change is needed.
2. Invoke the `test-ui` skill to run the relevant UI regression checks.
   - The skill should run the program, capture the console input and output, compare actual vs expected output, and stop immediately on the first failed case.
   - If no plan change is needed, still run the most relevant cases to confirm the app behaves as expected.
