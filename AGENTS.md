# Project context

This repository is a starter template for a greenfield Java project used in an introductory software engineering course in an undergraduate computer science program. Students use it as the starting point for their own projects.

# Default user context

Unless the user says otherwise, assume that you are assisting a student working on a project in this repository. If the user identifies themselves as an instructor or another project stakeholder, adapt your response to that role.

# Student profile

* Prior knowledge: Basic Java and OOP concepts.
* Level of programming experience: Intermediate. Did well in National University of Singapore's Introduction to Programming Methodology module (CS1010). Understands basic data structures and algorithms. 
* IDE and level of expertise: IDE is VSCode. Level of expertise is intermediate. Have done a lot of microcontroller programming, from Arduino and ESP32 to STM32 boards. Made a Unity PC game like Wii Tennis where we use a mobile phone's IMU to control an onscreen tennis racket.

# Guidance for interacting with users

* Explain the rationale for significant actions: what you did and why.
* Keep explanations brief but instructive, supporting learning through responsible use of AI. For example:

  * When suggesting a Git command, briefly explain what it does.
  * Add explanatory Javadoc comments to all classes and to nontrivial methods and fields when their purpose or behavior is not obvious.
  * Make generated code as self-explanatory as possible, and include explanatory comments where they improve understanding.
  * When faced with a design choice, choose the simplest option that is sufficient for the requirements, while briefly explaining relevant more advanced alternatives.

# Project-specific requirements

## Java coding standard

All Java source and test code in this project must follow the SE-EDU basic and intermediate Java coding standard.

Before creating, modifying, refactoring, or reviewing Java code, agents must use the project-specific `seedu-java-coding-standard` skill and read its required reference completely. Apply the standard to all new Java code and to every line materially changed by the task. Do not reformat unrelated code solely for style unless the user requests a broader cleanup.

Packages have been introduced in the course. Place every Java class in an appropriate package and keep its source file in the matching package directory.

## Java version:

Ensure that Java 25 is used when running the application or build tasks. On macOS, use `sdk use java 25.0.3.fx-zulu` to switch to Java 25 if needed.

## Console UI testing

After every code update:

1. Review `test/ui-test-plan.md` and update it when the change adds, removes, or alters console commands, expected output, state transitions, or relevant test coverage. Do not change expected output merely to make a regression pass.
2. Invoke the project-specific `test-ui` skill and run the applicable UI test cases. Do not consider the code update complete until the tests pass or the failure and its actual and expected outputs have been reported to the user.

## Git

All commits and branch names in this project must follow the SE-EDU Git conventions.

Before proposing, creating, amending, squashing, or reviewing a commit or commit message, and before naming or renaming a branch, agents must use the project-specific `seedu-git-standard` skill and read its required reference completely. Apply its subject and body rules to every future commit, including commits created through automated workflows.

Use lightweight tags unless the user requests an annotated tag.
When proposing or creating a commit message, include enough detail to explain the rationale for the change.
Do not commit or push unless explicitly asked.
