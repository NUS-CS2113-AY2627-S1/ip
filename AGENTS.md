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

# Project-specific requirements

## Git standard:

All future commits in this project must follow the project-specific
`.codex/skills/seedu-git-standard/SKILL.md`, which is based on the
[SE-EDU Git conventions](https://se-education.org/guides/conventions/git.html).
Use an imperative, capitalized subject without a trailing period, keep it
within the 72-character hard limit, and wrap commit bodies at 72 characters.
Non-trivial commits must explain what changed and why, separated from the
subject by a blank line. Use meaningful kebab-case branch names. Keep the
existing requirement that commits and pushes require explicit authorization.

## Java coding standard:

All Java code in this project must follow the project-specific
`.codex/skills/seedu-java-coding-standard/SKILL.md`, which is based on the
[SE-EDU Java coding standard](https://se-education.org/guides/conventions/java/intermediate.html).
Apply it when creating, editing, reviewing, or refactoring Java code. In
particular, keep packages, source directories, names, imports, formatting,
braces, visibility, and required Javadocs consistent with that skill.

## Java version:

Ensure that Java 25 is used when running the application or build tasks. On macOS, use `sdk use java 25.0.3.fx-zulu` to switch to Java 25 if needed.

## UI testing after code updates:

After every code update, review `test/ui-test-plan.md` before considering the
change complete. If the update changes observable command-line behavior, add
or revise test cases so each affected behavior has an aim, inputs, and the
complete expected output. Then invoke the project-specific `test-ui` skill
(`$test-ui`) and run the plan from the repository root:

```powershell
python .codex/skills/test-ui/scripts/run_ui_tests.py
```

Do not skip the skill invocation merely because the plan did not need an
update. Preserve the skill's exact output comparison, console transcript, and
stop-on-first-failure behavior. Report any failure with the actual and
expected output before making further code changes.

## Git

Use lightweight tags unless the user requests an annotated tag.
When proposing or creating a commit message, include enough detail to explain the rationale for the change.
Do not commit or push unless explicitly asked.
