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

## Java version:

Ensure that Java 25 is used when running the application or build tasks. On macOS, use `sdk use java 25.0.3.fx-zulu` to switch to Java 25 if needed.

## Coding Standard:

**MANDATORY**: Follow the [SE-EDU Java Coding Standard](https://se-education.org/guides/conventions/java/intermediate.html) (intermediate level) for all code in this project.

Use the `/seedu-java-coding-standard` skill to review and validate code against these standards.

**Key naming conventions:**
- **Package names**: all lowercase (e.g., `com.company.app`)
- **Class/Enum names**: PascalCase (e.g., `TaskList`, `Parser`)
- **Variable names**: camelCase (e.g., `taskList`, `userInput`)
- **Method names**: verbs in camelCase (e.g., `addTask()`, `getTasks()`)
- **Constants**: SCREAMING_SNAKE_CASE (e.g., `MAX_TASKS`, `DEFAULT_SEPARATOR`)
- **Boolean methods**: Prefix with `is`, `has`, `was`, `can`, `should` (e.g., `isDone()`, `hasNext()`)
- **Collections**: Use plural names (e.g., `tasks` not `taskList`)

**Javadoc requirements:**
- All classes require Javadoc comments
- All public methods (except getters/setters and overrides) require Javadoc
- Javadoc must start with a verb form (Returns, Sends, Adds, etc.)
- Include `@param` and `@return` tags for all parameters and return values

For other topics not covered, refer to the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).

## Git

**MANDATORY**: Follow the [SE-EDU Git Standard](https://se-education.org/guides/conventions/git.html) for all commits and branches.

Use the `/seedu-git-standard` skill to validate commit messages and branching practices.

**Commit message requirements:**
- Subject line: 50 characters (hard limit 72)
- Use imperative mood: "Add feature" not "Added feature"
- Start with capital letter, no period at end
- Optional scope prefix: `ClassName: Add method description`
- Body: Explain WHAT and WHY, not HOW
- Body width: 72 characters

**Branch naming:**
- Use kebab-case with meaningful keywords (e.g., `refactor-parser`)
- Issue-related: `issuenumber-keyword-from-title` (e.g., `42-add-todo-class`)

**Other requirements:**
- Use lightweight tags unless explicitly requested otherwise
- Do not commit or push unless explicitly asked by the user
- Always review staged files with `git status` before committing

## Implementation Grading [10 marks]

No major bugs. 
Reasonable use of OOP, e.g., at least some use of inheritance, with code divided into classes in a sensible way (e.g., Ui, Storage, Parser, Todo, Deadline, Event, etc.).
At least half of the public methods/classes have Javadoc comments.
Reasonable code quality:
  - No blatant violations of the coding standard (both Java and Git conventions). 
  - The code is neat, e.g., no chunks of commented-out code. 
  - At least some errors are handled using exceptions.

## Project Management [5 marks]

Submitted some deliverables in at least 4 out of the 6 iP weeks (i.e., week 2 to week 7)
Followed the other specified requirements (e.g., using Git/GitHub for each increment and doing peer reviews) in at least 4 weeks

## Documentation [5 marks]

The product website and the user guide:
  - Provide enough guidance to the user. Cover all non-trivial features. 
  - No major formatting errors in the published view.