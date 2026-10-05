# Project context

This repository is a starter template for a greenfield Java project used in an introductory software engineering course in an undergraduate computer science program. Students use it as the starting point for their own projects.

# Default user context

Unless the user says otherwise, assume that you are assisting a student working on a project in this repository. If the user identifies themselves as an instructor or another project stakeholder, adapt your response to that role.

# Student profile

* Prior knowledge: Basic Java and OOP concepts.
* Level of programming experience: 1 year of programming experience
* IDE and level of expertise: VSCode, nvim

# Guidance for interacting with users

* Explain the rationale for significant actions: what you did and why.
* Keep explanations brief but instructive, supporting learning through responsible use of AI. For example:

  * When suggesting a Git command, briefly explain what it does.
  * Add explanatory Javadoc comments to all classes and to nontrivial methods and fields when their purpose or behavior is not obvious.
  * Make generated code as self-explanatory as possible, and include explanatory comments where they improve understanding.
  * When faced with a design choice, choose the simplest option that is sufficient for the requirements, while briefly explaining relevant more advanced alternatives.

# Project-specific requirements

## Java coding standard

All Java code in this project must follow the project-specific
`seedu-java-coding-standard` skill, based on the
[SE-EDU Java basic and intermediate coding standard](https://se-education.org/guides/conventions/java/intermediate.html).
Use the skill when creating, editing, reviewing, or refactoring any `.java` file.

## Java version:

Ensure that Java 25 is used when running the application or build tasks. On macOS, use `sdk use java 25.0.3.fx-zulu` to switch to Java 25 if needed.

## Package organization

Keep `src/main/java` as the Java source root. Every package directory below it
must match the package declaration at the top of each Java file.

Yanny uses the following package structure:

* `yanny.ui` contains the `Yanny` application entry point and CLI display logic.
* `yanny.command` contains `CommandProcessor`, which parses and executes commands.
* `yanny.task` contains `Task`, `Todo`, `Deadline`, and `Event` domain classes.
* `yanny.exception` contains `YannyException` for expected user-input errors.

`CommandProcessor` stores tasks in a dynamically sized list, so Yanny does not
impose an artificial maximum number of tasks.

Compile all source files from the repository root with:

```bash
mkdir -p out
javac -d out $(find src/main/java -name '*.java')
```

Run Yanny with:

```bash
java -cp out yanny.ui.Yanny
```

## Yanny command output format

Keep user-visible command errors consistent with Yanny's terminal style:

* Keep the `| YANNY_OS :: COMMAND REJECTED` status line for rejected commands.
* Put the specific error on the next line using `| ERROR > ` followed by an uppercase,
  actionable message.
* Begin an empty-todo error with `TODO DESCRIPTION CANNOT BE EMPTY` and include the
  correct `TODO <DESCRIPTION>` syntax.
* Begin an unsupported-input error with `UNKNOWN COMMAND DETECTED` and list the
  supported commands.
* Represent expected user-input failures with `YannyException`. Catch it at the
  command-loop boundary and do not print stack traces to the user.

## UI testing after code changes

After every code update, inspect `test/ui-test-plan.md` and update it when the
change adds or modifies user-visible behaviour, commands, validation, or
output. Then invoke the project-specific `test-ui` skill to run the documented
CLI test cases and display the console input/output record. Do this before
handing the change back to the user, and report any failed test with its actual
and expected output.

Use the skill's standard runner from the repository root:

```bash
python3 .codex/skills/test-ui/scripts/run_ui_tests.py \
  test/ui-test-plan.md \
  --program "java -cp out yanny.ui.Yanny"
```

Compile the application with Java 25 before invoking the runner when the
compiled output is not current.

## Git

All future commit messages and branch names must follow the project-specific
`seedu-git-standard` skill, based on the
[SE-EDU Git conventions](https://se-education.org/guides/conventions/git.html).
Use the skill whenever proposing, creating, or reviewing a commit or branch.
Use lightweight tags unless the user requests an annotated tag.
When proposing or creating a commit message, include enough detail to explain the rationale for the change.
Do not commit or push unless explicitly asked.
