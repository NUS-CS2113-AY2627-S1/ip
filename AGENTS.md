# Project context

This repository is a starter template for a greenfield Java project used in an introductory software engineering course in an undergraduate computer science program. Students use it as the starting point for their own projects.

# Default user context

Unless the user says otherwise, assume that you are assisting a student working on a project in this repository. If the user identifies themselves as an instructor or another project stakeholder, adapt your response to that role.

# Student profile

* Prior knowledge: Basic Java and OOP concepts.
* Level of programming experience: Basic
* IDE and level of expertise: IntelliJ 1/10

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

Use lightweight tags unless the user requests an annotated tag.
When proposing or creating a commit message, include enough detail to explain the rationale for the change.
Do not commit or push unless explicitly asked.


## Mandatory Skill & Style Guidelines

### 1. Java Coding Standard
All Java code generated, modified, or refactored **must strictly follow** the project skill defined in `.agent/skills/seedu-java-coding-standard.md` (based on [SE-EDU Intermediate Java Conventions](https://se-education.org/guides/conventions/java/intermediate.html)).
- **Key Constraints**:
  - Strict naming conventions (PascalCase classes, camelCase methods/variables, UPPER_SNAKE constants).
  - Explicit Javadoc comments on all classes and non-trivial methods/fields.
  - Guard clauses over deep nesting.
  - No wildcard imports (`import java.util.*;`).

### 2. Git Commit Standard
All commit messages proposed or executed **must strictly follow** the project skill defined in `.agent/skills/seedu-git-standard.md` (based on [SE-EDU Git Conventions](https://se-education.org/guides/conventions/git.html)).
- **Key Constraints**:
  - Imperative mood in the subject line (e.g., `Add feature X`, not `Added feature X`).
  - Maximum 50 characters for subject line; wrap body lines at 72 characters.
  - Explain *what* and *why*, not just *how*.
  - Keep Java code refactoring and agent/config updates in separate, isolated commits.

### 3. Command-line UI Testing
After every code update that can affect command-line behaviour:

1. Review and update `test/ui-test-plan.md` when the change affects a
   documented behaviour or needs regression coverage.
2. Invoke the project-specific `.agent/skills/test-ui` skill before committing.
3. Include its console input/output transcript in the completion report.
   On the first failing case, stop and report the expected and actual output;
   do not commit the failing change.
