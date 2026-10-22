---
name: seedu-git-standard
description: SE-EDU Git conventions for commit messages and branch names. Use whenever proposing or creating a commit, writing a commit message, or naming a branch in this project.
---

# SE-EDU Git standard

Source: https://se-education.org/guides/conventions/git.html
Apply to every commit message and branch name. (Commit or push only when the user explicitly asks; see AGENTS.md.)

## Commit message subject
- Every commit has a well-written subject line.
- At most 50 characters (hard limit: 72).
- Imperative mood: `Add README.md`, not `Added README.md` or `Adding README.md`.
- Capitalize the first letter: `Move index.html file to root`.
- No period at the end: `Update sample data`.
- May add a `<scope>:` or `<category>:` prefix when applicable, e.g. `Person class: Remove static imports`, `Main.java: Remove blank lines`, `bug fix: Add space after name`, `chore: Update release date`.

## Commit message body
- Non-trivial commits need a body.
- Blank line between subject and body; blank lines between paragraphs.
- Wrap the body at 72 characters.
- Use bullet points where helpful instead of long paragraphs.
- Explain WHAT the commit is about and WHY it was done that way, not HOW (the diff shows how). A reader should be able to judge the change without reading the diff.
- Do not repeat information that is already in code comments in the same commit.
- Structure:
  1. Current situation (present tense).
  2. Why it needs to change.
  3. What is being done about it (imperative mood), introduced with "Let's".
  4. Why it is done that way.
  5. Any other relevant info.
- Avoid "currently" and "originally" when describing the current situation; they are implied.

Template:

```
Subject in imperative mood, max 50 chars

Describe the current situation in present tense. Explain why it
needs to change.

Let's do X, because Y.
- detail
- detail

Co-Authored-By trailer (if required by the session) goes last.
```

## Branch names
- Meaningful name made of relevant keywords in kebab-case: `refactor-ui-tests`.
- If related to an issue: `issueNumber-some-keywords-from-issue-title`, e.g. `1234-ui-freeze-error`.
