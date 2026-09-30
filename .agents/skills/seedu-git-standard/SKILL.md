---
name: seedu-git-standard
description: Apply the SE-EDU Git conventions when proposing or creating commits and branches in this project.
---

# SE-EDU Git Standard

Follow the [SE-EDU Git conventions](https://se-education.org/guides/conventions/git.html) whenever proposing or creating a commit or branch for this project.

## Commit messages

- Write a well-formed subject in imperative mood. Capitalize its first letter, do not end it with a period, and aim for 50 characters (never exceed 72). Add an optional scope or category prefix only when it improves clarity.
- Add a body for non-trivial commits. Leave one blank line after the subject, wrap body lines at 72 characters, and use blank lines or bullets to make the explanation readable.
- Explain what is changing and why; use the diff for implementation detail. A useful body describes the current situation, why it should change, what the commit does, and why that approach was chosen.
- If the explanation is too long for one coherent commit, recommend splitting the work into smaller commits.

## Branches

- Name branches with meaningful, relevant kebab-case keywords, such as `refactor-ui-tests`.
- For issue-related branches, use `issueNumber-keywords-from-title`, such as `1234-ui-freeze-error`.

Follow existing repository authorization rules: do not create commits, branches, or pushes unless the user has explicitly requested the action.
