---
name: seedu-git-standard
description: Apply the SE-EDU Git conventions when proposing or creating commits, writing commit messages, or naming branches in this project.
---

# SE-EDU Git Standard

Follow the [SE-EDU Git conventions](https://se-education.org/guides/conventions/git.html) for commit messages and branch names in this project. Treat that guide as authoritative.

## Commit messages

Inspect the complete staged diff before writing a commit message so the message accurately describes its scope and rationale.

- Give every commit a meaningful subject line.
- Aim for at most 50 characters; never exceed 72 characters.
- Write the subject in the imperative mood, capitalize its first letter, and do not end it with a period.
- Add a relevant `<scope>:` or `<category>:` prefix only when it improves clarity.
- For a non-trivial commit, separate the body from the subject with one blank line and wrap body lines at 72 characters.
- Explain what changed and why it was needed. Let the diff show how it was implemented.
- Use present tense for the situation and imperative mood for the change. Avoid redundant qualifiers such as `currently` and `originally`.
- Separate body paragraphs with blank lines and use bullet points when they improve readability.
- If the message requires an overly long explanation or covers unrelated rationales, propose splitting the work into focused commits.

Do not create a commit or push merely because this skill applies; obtain the authorization required by the project instructions.

## Branch names

- Use short, meaningful keywords in kebab-case, such as `refactor-ui-tests`.
- For issue-related branches, use `issueNumber-keywords-from-issue-title`, such as `1234-ui-freeze-error`.
