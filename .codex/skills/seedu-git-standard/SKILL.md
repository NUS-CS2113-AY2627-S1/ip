---
name: seedu-git-standard
description: Apply the SE-EDU Git conventions to commit messages and branch names in this project.
---

# SE-EDU Git Standard

Use this skill whenever creating, proposing, or reviewing commits or branch names in this project. Follow the [SE-EDU Git conventions](https://se-education.org/guides/conventions/git.html).

## Commit messages

- Write a clear subject in imperative mood, capitalize its first letter, and do not end it with a period.
- Keep the subject near 50 characters and never exceed 72 characters. An optional scope or category may precede it.
- For non-trivial commits, add a body after one blank line and wrap it at 72 characters.
- Use the body to explain what changed and why; the diff explains how. Describe the situation in present tense and the change in imperative mood, using paragraphs or bullets when useful.
- Split overly broad work into smaller commits when the message would otherwise become too long or unclear.

## Branch names

- Use meaningful kebab-case keywords, such as `refactor-ui-tests`.
- For issue-related branches, use `<issue-number>-<keywords-from-issue-title>`, such as `1234-ui-freeze-error`.

Do not create or push commits unless the user explicitly requests it. When a commit is requested, check the subject length, mood, capitalization, punctuation, body wrapping, and branch name before committing.
