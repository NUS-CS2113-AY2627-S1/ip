---
name: seedu-git-standard
description: Apply the SE-EDU Git conventions when preparing, reviewing, or naming commits and branches in this project.
---

# Seedu Git Standard

Use this skill for every future commit and branch-name decision in this project.
Preserve the user's requested scope, and do not commit or push unless the user
explicitly authorizes that action.

## Source of truth

Follow the SE-EDU [Git conventions](https://se-education.org/guides/conventions/git.html).
Use the project's existing Git instructions for any additional local constraints.

## Commit subject

- Write a well-formed subject for every commit.
- Use imperative mood, capitalize the first letter, and do not end with a period.
- Prefer 50 characters or fewer; never exceed the 72-character hard limit.
- Add a relevant `<scope>:` or `<category>:` prefix only when it improves clarity.

Examples: `Add README.md`, `Task class: Validate input`, `chore: Update release date`.

## Commit body

- Add a body for every non-trivial commit, separated from the subject by one blank
  line and wrapped at 72 characters.
- Explain WHAT changed and WHY. Do not spend the body explaining HOW; the diff shows
  the implementation.
- Structure the body in present tense as the current situation and reason for change,
  then use imperative mood for the change and its rationale. Use blank lines between
  paragraphs and bullets when they make the explanation clearer.
- Keep the message focused. If the explanation becomes too long, split the work into
  smaller commits.
- Avoid redundant restatement of comments already present in the changed code.

## Branch names

- Use meaningful kebab-case names containing relevant keywords, such as
  `refactor-ui-tests`.
- For issue-related branches, use `issueNumber-keywords-from-issue-title`, such as
  `1234-ui-freeze-error`.

## Commit workflow

Before creating a commit, inspect the staged diff and confirm that the subject and
body meet these rules. Use lightweight tags unless the user requests an annotated
tag. Never push unless explicitly requested.
