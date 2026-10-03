---
name: seedu-git-standard
description: Apply the SE-EDU Git conventions when proposing or creating commits, writing commit messages, or naming branches in this project.
---

# SE-EDU Git Standard

Follow the [SE-EDU Git conventions](https://se-education.org/guides/conventions/git.html) whenever proposing or
creating a commit, writing a commit message, or naming a branch in this project.

This skill controls formatting and content only. It does not authorize creating a commit, pushing changes, or
creating a branch. Obtain any authorization required by the project instructions or user request.

## Commit subject

- Write a meaningful subject for every commit.
- Aim for at most 50 characters; never exceed 72 characters.
- Use the imperative mood, such as `Add README.md` rather than `Added README.md`.
- Capitalize the first letter.
- Do not end with a period.
- Add a useful `<scope>:` or `<category>:` prefix when it improves clarity.

## Commit body

For every non-trivial commit, include a body that:

- is separated from the subject by one blank line;
- wraps at 72 characters;
- uses blank lines between paragraphs and bullet points where they improve readability;
- explains what changed and why, leaving implementation details to the diff;
- gives enough context for a reviewer to judge the change without reading the diff; and
- avoids repeating information already captured in code comments.

When a longer explanation is useful, organize it around the present situation, why it needs to change, what the
commit does, why that approach was chosen, and any other relevant context. If the body becomes unwieldy, consider
whether the change should be split into smaller coherent commits.

Before proposing or creating the message, inspect the actual staged or intended diff so every claim matches the
commit content. Do not include unrelated changes in the message.

## Branch names

- Use meaningful keywords in kebab-case, such as `refactor-ui-tests`.
- For an issue-related branch, use `issueNumber-keywords-from-title`, such as `1234-ui-freeze-error`.
- Preserve any branch prefix required by the execution environment; apply these rules to the part after the prefix.
