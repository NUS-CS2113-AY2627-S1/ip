---
name: seedu-git-standard
description: Apply the SE-EDU Git conventions for commit messages and branch names when proposing, reviewing, or creating Git commits in this project.
---

# SE-EDU Git Standard

Use this skill when proposing, reviewing, or creating Git commits in this project.
Base decisions on the SE-EDU Git conventions:
https://se-education.org/guides/conventions/git.html

## Commit Subjects

- Write a clear subject line for every commit.
- Use the imperative mood, such as `Add task list support`.
- Capitalize the first letter.
- Do not end the subject with a period.
- Try to keep the subject under 50 characters. The hard limit is 72 characters.
- Add a short scope or category prefix only when it improves clarity.

## Commit Bodies

- Add a body for nontrivial commits.
- Separate the subject and body with a blank line.
- Wrap body lines at about 72 characters.
- Explain what changed and why. Leave low-level implementation details to the diff.
- Split changes into smaller commits when the body becomes too broad.

## Commit Scope

- Keep each commit to one logical change.
- For branch names, use meaningful kebab-case names, optionally prefixed by the issue number when relevant.
