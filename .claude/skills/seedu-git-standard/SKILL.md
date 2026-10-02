---
name: seedu-git-standard
description: The SE-EDU Git conventions used by this project, covering commit message subjects, bodies, and branch names. Apply when proposing, writing, or reviewing any commit message or branch name in this repo.
---

# SE-EDU Git conventions

Source: https://se-education.org/guides/conventions/git.html

All commit messages in this project must follow these rules.

## Subject line

- Every commit must have a well-written subject line.
- Limit to **50 characters** (hard limit 72).
- **Imperative mood.**
  - Good: `Add README.md`
  - Bad: `Added README.md`, `Adding README.md`
- **Capitalize** the first letter.
  - Good: `Move index.html file to root`
  - Bad: `move index.html file to root`
- **No trailing period.**
  - Good: `Update sample data`
  - Bad: `Update sample data.`
- An optional `<scope>:` or `<category>:` prefix may be added when applicable:
  - `Person class: Remove static imports`
  - `Main.java: Remove blank lines`
  - `bug fix: Add space after name`
  - `chore: Update release date`

## Body

Non-trivial commits should have a body.

- Separate subject from body with a blank line.
- Wrap the body at **72 characters**.
- Separate paragraphs with blank lines.
- Use bullet points where they help more than prose.

### Explain WHAT and WHY, not HOW

The diff already shows how. The body explains what the commit is about and why it
was done that way, in enough detail that a reader can judge whether it was a good
idea without reading the diff.

If the description starts getting too long, that is a sign the commit should be
split into finer-grained pieces.

Avoid repeating information already present in the code comments of the same commit.

### Body structure

1. `{current situation}` — present tense
2. `{why it needs to change}`
3. `{what is being done about it}` — imperative mood
4. `{why it is done that way}`
5. `{any other relevant info}`

Avoid `currently` and `originally` when describing the current situation — they
are implied. The word `Let's` marks the start of the section describing the change.

### Example

```
Person attributes classes: extract a parent class PersonAttribute

Person attribute classes (e.g. Name, Address, Age etc.) have some common
behaviors (e.g. isValid()).

The common behaviors across person attribute classes cause code duplication.
Extracting the common behavior into a super class allows us to use
polymorphism when dealing with person attributes.

Let's pull up behaviors common to all person attribute classes into a new
parent class named PersonAttribute.

Using inheritance is preferable over composition in this situation because
the common behaviors are not composable.
```

## Branch names

- Meaningful keywords in kebab-case — `refactor-ui-tests`.
- For a branch tied to an issue: `issueNumber-some-keywords-from-issue-title`,
  e.g. `1234-ui-freeze-error`.

## Proposing commit messages

When asked to propose a commit message, inspect the actual staged/unstaged diff
first, then produce a subject that satisfies every rule above, adding a body when
the commit is non-trivial. State the subject's character count so it can be checked
against the 50-character guideline.
