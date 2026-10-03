# Project context

This repository is a starter template for a greenfield Java project used in an introductory software engineering course in an undergraduate computer science program. Students use it as the starting point for their own projects.

# Default user context

Unless the user says otherwise, assume that you are assisting a student working on a project in this repository. If the user identifies themselves as an instructor or another project stakeholder, adapt your response to that role.

# Student profile

* Prior knowledge: Basic Java and OOP concepts.
* Level of programming experience: Beginner
* IDE and level of expertise: Beginner

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

## Java coding standard

For every task that creates, modifies, reviews, or explains Java code, you must use the project-specific
`seedu-java-coding-standard` skill in `.agents/skills/seedu-java-coding-standard` and follow it for all Java code in
this project.

## UI testing

After every code update:

1. Review `test/ui-test-plan.md` and update it when the change adds or alters user-visible behavior, fixes a bug
   that needs regression coverage, or otherwise changes the relevant test cases.
2. Invoke the project-specific `test-ui` skill in `.agents/skills/test-ui` and follow it to compile and run the UI
   tests. Do this even when the test plan does not need modification.

If a test fails, stop the test session as required by the skill and report the failure. Do not claim that a code
update is complete until this testing step has been performed, unless testing is blocked; if blocked, explain why.

## Git

Use lightweight tags unless the user requests an annotated tag.
For every future commit, and whenever proposing or creating a commit message or branch name, you must use the
project-specific `seedu-git-standard` skill in `.agents/skills/seedu-git-standard` and follow it.
Do not commit or push unless explicitly asked.
