# Project context

This repository is a starter template for a greenfield Java project used in an introductory software engineering course in an undergraduate computer science program. Students use it as the starting point for their own projects.

# Default user context

Unless the user says otherwise, assume that you are assisting a student working on a project in this repository. If the user identifies themselves as an instructor or another project stakeholder, adapt your response to that role.

# Student profile

* Prior knowledge: Basic Java and OOP concepts.
* Level of programming experience: Intemediate
* IDE and level of expertise: Intemediate

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

## Coding Standards:

All Java code in this project MUST strictly follow the project's `seedu-java-coding-standard` skill based on the [SE-EDU Java Coding Standard (Intermediate)](https://se-education.org/guides/conventions/java/intermediate.html). Key rules include:
* Mandatory Javadoc comments for all classes, public methods, and non-trivial fields.
* Method summaries must start with third-person present tense verbs (e.g., `Returns ...`, `Runs ...`).
* PascalCase for classes/enums, camelCase for variables/methods, SCREAMING_SNAKE_CASE for constants.
* 4-space indentation, 120-character line length limit, K&R (Egyptian) brace style.
* Control statements (`if`, `while`, `for`, `do-while`) MUST always use braces `{ }`.
* Explicit imports only (never use wildcard `*` imports).

## Git Conventions:

All Git commits, messages, and branches in this project MUST strictly adhere to the `seedu-git-standard` skill based on the [SE-EDU Git Conventions Guide](https://se-education.org/guides/conventions/git.html). Key rules include:
* **Subject line**: Imperative mood (e.g., `Add ...`, `Fix ...`, not `Added`/`Adding`), capitalized, no trailing period, $\le$ 50 characters (hard limit: 72 characters).
* **Commit body**: Required for non-trivial commits, hard-wrapped at 72 characters, separated from the subject by a blank line.
* **Content focus**: Explain WHAT and WHY, not HOW. Structure: `{current situation} -> {why change} -> {what is being done (Let's ...)} -> {rationale/tradeoffs}`.
* **Branches**: Use `kebab-case` (e.g., `refactor-ui-tests`) or `issueNumber-keywords` (e.g., `123-fix-bug`).
* **Tags**: Use lightweight tags unless annotated tag is explicitly requested.
* **Safety**: Do not commit or push unless explicitly asked.

## UI Testing & Verification:

After EVERY code update, you MUST:
1. Update `test/ui-test-plan.md` if the code change introduces new commands, modifies expected output formats, or adds/updates test cases.
2. Invoke the `test-ui` skill (e.g., run `python3 .agents/skills/test-ui/scripts/run_ui_tests.py`) to verify the implementation against the test plan and ensure all tests pass before completing the turn.
