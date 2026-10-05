---
name: seedu-git-standard
description: Enforce the SE-EDU Git conventions for commit subjects, commit bodies, and branch names in this project. Use when creating, reviewing, or proposing commits, commit messages, or branches.
---

# SE EDU Git Standard

Apply this skill to every commit message and branch name in this project. Use the project reference at [references/git-rules.md](references/git-rules.md) for the checklist, and use the [SE-EDU Git conventions](https://se-education.org/guides/conventions/git.html) as the source of truth.

## Required workflow

1. Inspect the requested diff and identify the change's purpose before drafting a commit message.
2. Write a subject that follows the subject rules.
3. Add a body for every non-trivial commit, explaining what changed and why.
4. Use a meaningful kebab-case branch name.
5. Do not create or push a commit unless the user explicitly asks for it.

## Commit subject rules

- Use imperative mood.
- Capitalize the first letter.
- Do not end the subject with a period.
- Prefer 50 characters or fewer; never exceed 72 characters.
- Add a scope or category prefix only when it improves clarity.

## Commit body rules

- Separate the subject and body with a blank line.
- Wrap body lines at 72 characters.
- Explain what the change does and why it is needed, not implementation mechanics.
- For non-trivial changes, describe the current situation, motivation, change, and rationale.
- Use blank lines or bullets to organize multiple points.
