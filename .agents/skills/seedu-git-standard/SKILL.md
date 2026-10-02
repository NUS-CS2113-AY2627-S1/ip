---
name: seedu-git-standard
description: Git conventions and commit message standards based on SE-EDU conventions. Use when creating, proposing, or formatting Git commit messages and branch names in this project.
---

# SE-EDU Git Standard

This guide defines the Git conventions and commit standards for this project, based on the [SE-EDU Git Conventions](https://se-education.org/guides/conventions/git.html).

---

## 1. Commit Message: Subject Line

Every commit must have a well-written commit message subject line adhering to the following rules:

* **Character Limit**:
  * Soft limit: **50 characters**.
  * Hard limit: **72 characters**.
  * *Rationale*: Git tools and repository viewers display only a limited number of characters from the subject line.
* **Imperative Mood**:
  * Use the imperative mood in the subject line (e.g., command style).
  * `Good`: `Add README.md`, `Fix null pointer in TaskList`
  * `Bad`: `Added README.md`, `Adding README.md`, `Fixes null pointer`
* **Capitalization**:
  * Capitalize the first letter of the subject line.
  * `Good`: `Move index.html file to root`
  * `Bad`: `move index.html file to root`
* **No Trailing Period**:
  * Do not end the subject line with a period.
  * `Good`: `Update sample data`
  * `Bad`: `Update sample data.`
* **Scope / Category Prefix (Optional)**:
  * You may add `<scope>:` or `<category>:` at the beginning when applicable.
  * Examples:
    * `Person class: Remove static imports`
    * `Main.java: Remove blank lines`
    * `bug fix: Add space after name`
    * `chore: Update release date`

---

## 2. Commit Message: Body

Non-trivial commits must include a message body providing details about the change.

### Formatting Rules

* **Separation**: Separate the subject line from the body with **one blank line**.
* **Line Wrapping**: Wrap body lines at **72 characters**.
* **Paragraphs**: Separate distinct paragraphs with a blank line.
* **Bullet Points**: Use bullet lists where appropriate instead of dense blocks of text.

### Content Guidelines

* **Explain WHAT and WHY, not HOW**:
  * Explain what the commit changes and why it was done that way.
  * The reader can consult the code diff to see *how* it was done.
  * Provide enough explanation so the reader can evaluate the change without needing to inspect the diff.
  * If the description becomes excessively long, split the commit into smaller, finer-grained commits.
* **Avoid Redundancy**:
  * Minimize repeating details already covered in code comments within the commit.
* **Avoid Temporal Fillers**:
  * Avoid words like "currently" or "originally" when describing the current situation (they are already implied).

### Body Structure

Structure the body following this logical sequence:

```text
{current situation}      -- present tense
{why it needs to change}
{what is being done}     -- imperative mood (often starts with "Let's")
{why it is done that way}
{any other relevant info}
```

* **The "Let's" Convention**: Use the word `Let's` to introduce the section detailing the changes made by the commit.

---

## 3. Examples of Commit Messages

### Example 1: Multi-commit / General Feature Change
```text
Unify variations of toSet() methods

There are several methods that convert a collection to a set. In some
cases the conversion is in-lined as a code block in another method.
Unifying all those duplicated code improves the code quality.

As a step towards such unification, let's extract those duplicated code
blocks into separate methods in their respective classes. Doing so will
make the subsequent unification easier.
```

### Example 2: Bug Fix with Bullet Points
```text
Find command: make matching case-insensitive

Find command is case-sensitive.
A case-insensitive find is more user-friendly because users cannot be
expected to remember the exact case of the keywords.

Let's,
* update the search algorithm to use case-insensitive matching
* add a script to migrate stress tests to the new format
```

### Example 3: Refactoring with Technical Rationale & References
```text
Person attributes classes: extract a parent class PersonAttribute

Person attribute classes (e.g. Name, Address, Age etc.) have some common
behaviors (e.g. isValid()).
The common behaviors across person attribute classes cause code duplication.

Extracting the common behavior into a super class allows us to use
polymorphism when dealing with person attributes. For example, validity
checking can be done for all attributes of a person in one loop.

Let's pull up behaviors common to all person attribute classes into a new
parent class named PersonAttribute.

Using inheritance is preferable over composition in this situation
because the common behaviors are not composable.

Refer to this S/O discussion on dealing with attributes
http://stackoverflow.com/some/question
```

---

## 4. Branch Naming Conventions

* **Format**: Use kebab-case with concise, descriptive keywords.
  * Example: `refactor-ui-tests`
* **Issue-linked Branches**: When a branch addresses an issue, prefix with the issue number:
  * `<issueNumber>-<keywords-from-issue-title>`
  * Example: `1234-ui-freeze-error`
