# Project context

This repository is a starter template for a greenfield Java project used in an introductory software engineering course in an undergraduate computer science program. Students use it as the starting point for their own projects.

# Default user context

Unless the user says otherwise, assume that you are assisting a student working on a project in this repository. If the user identifies themselves as an instructor or another project stakeholder, adapt your response to that role.

# Student profile

* Prior knowledge: Basic Java and OOP concepts.
* Level of programming experience: [to be filled]
* IDE and level of expertise: [to be filled]

# Guidance for interacting with users

* Explain the rationale for significant actions: what you did and why.
* Keep explanations brief but instructive, supporting learning through responsible use of AI. For example:

  * When suggesting a Git command, briefly explain what it does.
  * Add explanatory Javadoc comments to all classes and to nontrivial methods and fields when their purpose or behavior is not obvious.
  * Make generated code as self-explanatory as possible, and include explanatory comments where they improve understanding.
  * When faced with a design choice, choose the simplest option that is sufficient for the requirements, while briefly explaining relevant more advanced alternatives.

## Using reference documents

A previous User Guide revision copied a friend's section order and examples
too closely. When a reference is supplied for quality guidance, create an
independent outline and example scenario unless the user requests its layout.
Before presenting the revision, compare the two outlines and ensure the
section sequence and walkthrough examples are distinct. Verify feature claims
against SamSquare rather than transferring the reference application's rules.

## Coding standards

All Java code in this repository must follow the project-specific
`seedu-java-coding-standard`.

All Git commit messages must follow the project-specific
`seedu-git-standard`.

When generating or modifying Java code:
- Follow the Java coding standard before presenting the code.
- Preserve the project's existing architecture unless the requirement
  calls for a structural change.
- Do not introduce unnecessary classes, methods, or abstractions.
- Ensure code remains compatible with Java 25.

When proposing or creating commits:
- Follow the Git commit standard.
- Propose the commit message before committing when requested.
- Keep unrelated changes in separate commits.

# Project-specific requirements

## Java version:

Ensure that Java 25 is used when running the application or build tasks. On macOS, use `sdk use java 25.0.3.fx-zulu` to switch to Java 25 if needed.

## Git

Use lightweight tags unless the user requests an annotated tag.
When proposing or creating a commit message, include enough detail to explain the rationale for the change.
Do not commit or push unless explicitly asked.

# Project-specific skill

## SEEDU Java Coding Standard:

All Java code in this project must follow the SE-EDU Java coding conventions.

## Formatting

- Use 4 spaces for indentation. Do not use tabs.
- Keep lines at or below 120 characters.
- Use one statement per line.
- Use braces consistently for control structures.
- Keep related code together and use blank lines to separate logical sections.
- Do not leave unnecessary trailing whitespace.

## Naming

- Classes and interfaces use PascalCase.
- Methods and variables use camelCase.
- Constants use UPPER_SNAKE_CASE.
- Names should be descriptive and avoid unnecessary abbreviations.

## Comments and Javadoc

- Write comments in English.
- Public classes and public methods should have Javadoc where required by the convention.
- Comments should explain intent or rationale rather than simply restating code.
- Avoid unnecessary comments.

## Classes

- Keep each top-level class in its own file.
- The filename must match the public class name.
- Use appropriate access modifiers.
- Keep fields private/protected only when justified by the design.
- Prefer encapsulation through methods rather than exposing mutable state directly.

## Packages

- Java source files should declare an appropriate package.
- The package declaration should match the project's directory structure.

## General

- Follow the official SE-EDU Java Intermediate Coding Standard:
  https://se-education.org/guides/conventions/java/intermediate.html
- When a project-specific requirement conflicts with a general style rule, satisfy the project requirement while keeping the rest of the code compliant.

## SEEDU Git Commit Standard:

All Git commits in this project must follow the SE-EDU Git conventions.

## Commit subject

- Use an imperative mood.
- Capitalize the first word.
- Do not end the subject with a period.
- Keep the subject line preferably within 50 characters.
- Never exceed 72 characters.
- The subject should clearly describe the change.

## Commit body

- For non-trivial changes, include a body explaining what changed and why.
- Separate the subject from the body with a blank line.
- Wrap body lines at approximately 72 characters.
- Focus on rationale and relevant context rather than repeating the diff.

## Commit scope

- Each commit should represent one logical change.
- Do not combine unrelated changes into one commit.
- Keep refactoring separate from functional changes when practical.

## Project-specific Git requirements

- Use lightweight tags unless an annotated tag is explicitly requested.
- Do not commit or push unless explicitly requested by the user.
- When completing an assignment increment, tag the commit with the exact increment ID as required by the assignment.

## Coding Standard Reference

Follow:
https://se-education.org/guides/conventions/git.html

## UI Testing Requirement

After every code update:

1. Update `test/ui-test-plan.md` if the affected behaviour or test cases have changed.
2. Invoke the project-specific `test-ui` skill.
3. Do not consider the code update complete until the UI tests have been run.
4. The `test-ui` skill must test SamSquare through its actual console user interface using the test cases documented in `test/ui-test-plan.md`.
5. For each test case, compare the actual program output against the expected output recorded in `test/ui-test-plan.md`.
6. After testing, show a record of the console input and output for the test session.
7. If a test case fails, terminate the test session immediately.
8. When a test fails, report:
  - the test case that failed,
  - the actual output,
  - the expected output.
9. Do not skip a failing test or modify the expected output merely to make the test pass.
10. After fixing a failed test, invoke the `test-ui` skill again and rerun the tests.
11. Continue this process until all relevant UI tests pass.
12. Any new or modified user-facing behaviour should have a corresponding test case in `test/ui-test-plan.md`.

## Continuous AI Improvement

For User Guide changes, check the deployed GitHub Pages HTML as well as the
Markdown source. Inline code with spaces just inside backticks rendered as
literal backticks on Pages even though a local GFM preview parsed it as code.
Describe surrounding spaces in prose or use a fenced code block instead. A
local Markdown preview alone does not verify the deployed Pages rendering.

Treat the repository's AI guidance as a living part of the project.

1. During each task, note concrete cases where AI-generated work, review, or
   testing missed a requirement, made an incorrect assumption, or failed to
   detect a defect.
2. When a recurring or generalisable weakness is found, update the most
   relevant AI guidance file (for example, this file or `test-ui/SKILL.md`) in
   the same change. State the observed failure and turn it into a specific,
   verifiable instruction rather than adding vague advice.
3. Do not weaken requirements, expected outputs, or tests to accommodate the
   current implementation.
4. Expand UI tests beyond happy paths. Cover boundary values, malformed and
   incomplete commands, invalid task numbers, empty descriptions, command
   whitespace, and operations on empty or previously modified task lists where
   those behaviours are supported by the current requirements.
5. Interleave successful and unsuccessful commands in stateful test sessions.
   After each rejected command, use a suitable follow-up command such as
   `list` to verify that the application's internal state was not changed.
6. Keep each test case deterministic. Document its starting state and whether
   it continues an earlier session or starts a fresh process.
7. Mutation checks may be used to assess test quality: introduce one small,
   targeted temporary defect, confirm that an existing test fails for the
   intended reason, and then restore the original code. Never commit or leave
   the temporary defect in the working tree. Do not change expected output to
   make the mutation pass.
8. Before a mutation check, record the files and exact code being changed so
   the original state can be restored without overwriting unrelated user work.
   After restoration, rerun the relevant tests and verify the working-tree diff.
