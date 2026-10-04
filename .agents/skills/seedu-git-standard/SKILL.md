---
name: seedu-git-standard
description: Apply or review the official SE-EDU Git conventions for commits and branch names in this repository.
---

# SE-EDU Git Standard

Use the official [SE-EDU Git conventions](https://se-education.org/guides/conventions/git.html) whenever proposing or
creating commits or branches in this repository.

## Commit subjects

- Write a meaningful subject in the imperative mood.
- Capitalize the first letter and do not end the subject with a period.
- Aim for no more than 50 characters. Never exceed the 72-character hard limit.
- Add an optional `<scope>:` or `<category>:` prefix only when it improves clarity.

## Commit bodies

- Add a body for every non-trivial commit, separated from the subject by a blank line.
- Wrap body lines at 72 characters and use blank lines between paragraphs. Use bullet points when they improve clarity.
- Explain what the change does and why it is needed; leave implementation mechanics to the diff.
- Describe the existing situation in the present tense and the action being taken in the imperative mood.

## Commit structure

- Keep commits logical and atomic: each commit should represent one coherent change and remain reviewable on its own.
- Split a change when its commit description becomes too long or combines independent concerns.
- Review the exact staged diff before committing so unrelated files or changes are not included.

## Branch names

- Use a meaningful kebab-case name made from relevant keywords, such as `refactor-ui-tests`.
- For issue-related branches, use `issueNumber-keywords-from-issue-title`, such as `1234-ui-freeze-error`.
