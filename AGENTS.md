# Project context

This repository is a starter template for a greenfield Java project used in an introductory software engineering course in an undergraduate computer science program. Students use it as the starting point for their own projects.

# Default user context

Unless the user says otherwise, assume that you are assisting a student working on a project in this repository. If the user identifies themselves as an instructor or another project stakeholder, adapt your response to that role.

# Student profile

* Prior knowledge: Basic Java and OOP concepts.
* Level of programming experience: Year 4 university student
* IDE and level of expertise: Year 4 university student

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

## Coding standard

All Java code in this project (new and modified) MUST follow the SE-EDU Java coding standard (intermediate), as captured in the project skill `seedu-java-coding-standard` (`.claude/skills/seedu-java-coding-standard/SKILL.md`). Invoke that skill before writing or changing Java code, and fix any violations you find in code you touch. If this standard conflicts with another instruction here, the standard wins for code style.

## UI testing

After every code update (any change to Java code under `src/`), you MUST:

1. Update `test/ui-test-plan.md` if the change affects console behavior (add, change or remove test cases as needed; ask before changing an expected output to make a failing test pass).
2. Invoke the project skill `test-ui` (`.claude/skills/test-ui/SKILL.md`) to run the plan, and show the user the test session record.

If a test case fails, stop and report the expected and actual output, as the skill describes.

## Git

All commits (messages and branch names) MUST follow the SE-EDU Git standard, as captured in the project skill `seedu-git-standard` (`.claude/skills/seedu-git-standard/SKILL.md`). Invoke that skill before proposing or creating any commit message or branch. If it conflicts with another instruction here about commit message format, the standard wins; the required `Co-Authored-By` trailer is still appended last.

Use lightweight tags unless the user requests an annotated tag.
When proposing or creating a commit message, include enough detail to explain the rationale for the change.
Do not commit or push unless explicitly asked.
