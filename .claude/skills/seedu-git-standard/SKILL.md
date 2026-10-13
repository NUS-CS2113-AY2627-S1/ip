---
description: "Enforce the SE-Education Git conventions on all commits, branches, and messages in this project."
---

# SE-Education Git Convention

This skill enforces the Git conventions from
<https://se-education.org/guides/conventions/git.html>
on every commit and branch created in this project.

## When to apply

Apply these rules whenever you **create a commit, write a commit message,
or name a branch** in this repository.

## Rules checklist

### Commit message -- subject line

1. **Length** -- 50 characters or fewer (hard limit 72).
2. **Imperative mood** -- write as a command, not a description.
   Good: `Add README.md`. Bad: `Added README.md`, `Adding README.md`.
3. **Capitalize first letter** -- e.g. `Move index.html file to root`.
4. **No trailing period** -- e.g. `Update sample data` (no `.`).
5. **Optional scope prefix** -- use `Scope: Message` format when helpful.
   Examples:
   - `Person class: Remove static imports`
   - `Main.java: Remove blank lines`
   - `bug fix: Add space after name`
   - `chore: Update release date`

### Commit message -- body

6. **Include a body for non-trivial commits** -- anything beyond a
   one-line rename or typo fix deserves an explanation.
7. **Blank line between subject and body** -- mandatory separator.
8. **Wrap body at 72 characters** -- keep lines within this width.
9. **Blank lines between paragraphs** -- for readability.
10. **Explain WHAT and WHY, not HOW** -- let the diff show implementation
    details; the message should provide enough context for a reader to
    judge the change without examining the code.
11. **Structure the body logically** -- suggested order:
    - Current situation (present tense)
    - Why a change is needed
    - What is being done (imperative mood)
    - Why it is done this way
    - Other relevant information
12. **Use bullet points when helpful** -- for lists of changes or reasons.

### Branch names

13. **kebab-case with meaningful keywords** -- e.g. `refactor-ui-tests`.
14. **Include issue number when applicable** --
    format `issueNumber-some-keywords`, e.g. `1234-ui-freeze-error`.

## Quick examples

**Good commit message (simple):**
```
Add user search feature
```

**Good commit message (with body):**
```
Add user search feature

The app currently has no way to look up users by name.
Add a search bar to the main page that filters the user
list as the user types.

This uses a simple substring match rather than fuzzy
search, which is sufficient for the current data size.
```

**Good branch name:**
```
42-add-delete-command
```

**Bad examples:**
```
added stuff.                     # past tense, vague, trailing period
fix                              # too vague, no context
Updated the main class to add new feature  # past tense, too long
```
