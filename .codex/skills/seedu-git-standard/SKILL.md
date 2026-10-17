---
name: seedu-git-standard
description: Propose, review, and create Git commit messages and branch names that comply with the SE-EDU Git conventions. Use for commit-message or branch-name work in this project. Invoking this skill does not authorize staging, committing, tagging, or pushing.
---

# SE-EDU Git Standard

Apply the official [SE-EDU Git conventions](https://se-education.org/guides/conventions/git.html) to Git messages and branch names in this repository.

Before proposing, reviewing, or creating a commit message, read [references/git-standard.md](references/git-standard.md) completely.

## Workflow

1. Inspect the staged diff when available. Distinguish staged changes from unstaged and untracked files.
2. Confirm the staged content represents one logical change. Recommend separate commits when independent changes are mixed.
3. Write a concise imperative subject and, for a non-trivial change, a body that explains what changed and why.
4. Check capitalization, punctuation, line lengths, blank-line separation, and body wrapping against the reference.
5. When reviewing an existing message, name each violation and provide a corrected ready-to-use message.
6. When branch naming is requested, use meaningful kebab-case keywords and include an issue number when applicable.

## Boundaries

- A request to propose or review a message does not authorize `git add`, `git commit`, `git commit --amend`, tag creation, or push.
- Before an explicitly authorized commit, verify the exact staged paths and do not include unrelated changes.
- Explain what and why in the message; leave implementation details to the diff unless they are needed to understand the decision.
