# Project context

This repository contains my CS2113 Individual Project (iP), a Java chatbot named Cole.

The project is developed incrementally according to the CS2113 iP requirements. When assisting with this repository, prioritize satisfying the specified iP requirements while keeping the implementation simple, readable, and appropriate for an undergraduate introductory software engineering course.

# Student context

I am a beginner-to-intermediate Java programmer.

I understand basic Java syntax and OOP concepts such as classes, objects, inheritance, arrays, loops, and conditionals, but I am still learning software engineering practices, project organization, exception handling, testing, and Git workflows.

I use IntelliJ IDEA on macOS.

When explaining changes:

- Keep explanations concise but educational.
- Explain important design decisions.
- Do not introduce unnecessarily advanced patterns or abstractions.
- Prefer simple solutions that satisfy the project requirements.

# Java and environment

- Use Java 25.
- On macOS, use the following command when switching to the required Java version:

  `sdk use java 25.0.3.fx-zulu`

- Keep the code compatible with the existing project structure and build configuration.
- Do not add external dependencies unless they are clearly required.

# iP implementation requirements

When implementing or reviewing features, keep the following final-project grading requirements in mind.

## Deliverables

The final implementation should complete more than 90% of the required iP deliverables.

Requirements marked optional or if-applicable do not need to be implemented unless requested.

When a requirement specifies a minimal version, implement at least that version before considering additional improvements.

Do not implement future iP levels unless explicitly requested.

## Object-oriented design

Use OOP in a sensible way.

Prefer separating responsibilities into appropriate classes rather than placing all logic in the main class.

The project may contain classes such as:

- `Cole`
- `Task`
- `Todo`
- `Deadline`
- `Event`
- `Ui`
- `Parser`
- `Storage`

Use inheritance where appropriate. In particular, task types such as `Todo`, `Deadline`, and `Event` should inherit from `Task` when this matches the current iP requirements.

Avoid unnecessary abstractions or design patterns.

## Javadoc

At least half of the public methods and classes in the final project should have Javadoc comments.

When creating or substantially modifying public classes or non-trivial public methods, add useful Javadoc comments.

Javadoc should explain purpose and behavior rather than merely restating the code.

Example:

~~~java
import cole.Task;

/**
 * Represents a task with a deadline.
 */
public class Deadline extends Task {
  // ...
}
~~~

## Exception handling

Some errors in the final project must be handled using exceptions.

When the relevant iP level introduces exception handling:

- Prefer custom exceptions where appropriate.
- Handle invalid user commands gracefully.
- Do not allow expected user-input errors to crash the application.
- Give the user a clear and useful error message.

Do not introduce exception-handling features before they are required unless necessary to fix a bug.

## Code quality

Maintain reasonable Java code quality.

In particular:

- Follow standard Java naming conventions.
- Use meaningful variable, method, and class names.
- Keep methods reasonably small and focused.
- Avoid duplicated code where a simple refactoring can remove it.
- Avoid large blocks of commented-out code.
- Avoid unnecessary comments that merely repeat the code.
- Keep formatting consistent.
- Preserve existing functionality when refactoring.
- Prefer readable code over clever code.

# Functional correctness

Avoid major bugs.

Before considering a feature complete:

1. Check that the implementation matches the stated iP requirement.
2. Compile the project.
3. Run relevant tests if available.
4. Manually test important user-facing commands when appropriate.
5. Check that existing features still work.

Do not silently change command syntax or output formatting specified by the iP requirements.

# Documentation

Keep the user guide and product documentation consistent with the implemented application.

Documentation should:

- Explain all non-trivial user-facing features.
- Show the correct command syntax.
- Provide useful examples where appropriate.
- Match the actual behavior of the application.
- Avoid major formatting problems in the published version.

Do not document features that have not been implemented.

# Git workflow

The project is developed incrementally using Git and GitHub.

When assisting with Git:

- Explain briefly what important Git commands do.
- Use lightweight tags unless I explicitly request annotated tags.
- Do not commit automatically.
- Do not push automatically.
- Do not create or modify tags automatically unless explicitly requested.
- Do not rewrite Git history unless explicitly requested and the consequences have been explained.
- Preserve previous iP increments and their history.

When suggesting commit messages, make them descriptive enough to explain the purpose of the change.

Typical iP development flow:

1. Implement one increment.
2. Test it.
3. Review the changes.
4. Commit the increment.
5. Create the required lightweight tag.
6. Push the branch and tags when appropriate.

# AI assistance rules

When I ask for help implementing an iP level:

1. Read the relevant requirement carefully.
2. Inspect the existing implementation before proposing major changes.
3. Identify which files need to change.
4. Explain the intended approach briefly.
5. Make the smallest reasonable changes needed to satisfy the requirement.
6. Preserve functionality from previous levels.
7. Add Javadoc where appropriate.
8. Check for obvious bugs and coding-standard problems.
9. Compile/test the result where possible.
10. Tell me what changed and what I should verify manually.

Do not implement unrelated features.

Do not replace large portions of working student code merely to make the implementation more sophisticated.

If there are multiple valid designs, prefer the simplest design that satisfies the CS2113 requirement and briefly explain the choice.

# Current application

The chatbot is named `Cole`.

Do not rename it back to `Duke`.

The main application class is `Cole`.

Existing task types may include:

- `Task`
- `Todo`
- `Deadline`
- `Event`

Preserve these names unless a later CS2113 requirement explicitly requires a change.