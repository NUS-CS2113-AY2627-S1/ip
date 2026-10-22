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

# Java coding standard

All Java code in this project must follow the project-specific `seedu-java-coding-standard`
skill at `.codex/skills/seedu-java-coding-standard/SKILL.md`. Apply it when creating,
modifying, or reviewing Java source, and preserve behavior while correcting violations.

# UI testing after code updates

After every code update, review `test/ui-test-plan.md` and add or revise test cases when
the changed behavior requires coverage. Then invoke the project-specific `$test-ui` skill
using that plan before considering the update complete. Preserve the skill's fail-fast
behavior and report any actual-versus-expected output mismatch.

# Project-specific requirements

## Java version:

Ensure that Java 25 is used when running the application or build tasks. On macOS, use `sdk use java 25.0.3.fx-zulu` to switch to Java 25 if needed.

## Git

Use lightweight tags unless the user requests an annotated tag.
All future commit messages and branch names must follow the project-specific
`seedu-git-standard` skill at `.codex/skills/seedu-git-standard/SKILL.md`. In particular,
commit subjects must be imperative, capitalized, period-free, and at most 72 characters;
non-trivial commits must include a blank-line-separated body wrapped at 72 characters that
explains what changed and why. Branch names must be meaningful kebab-case names.
Do not commit or push unless explicitly asked.
