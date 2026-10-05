---
name: seedu-git-standard
description: Follow the SE-EDU Git conventions when naming branches or creating, proposing, reviewing, or editing commit messages in this project.
---

# SE-EDU Git Standard

Follow the SE-EDU Git conventions for all Git-related work in this project,
especially future commits and branch names.

Source: https://se-education.org/guides/conventions/git.html

## Commit Message Subject

- Every commit must have a well-written subject line.
- Try to limit the subject to 50 characters. The hard limit is 72 characters.
- Use the imperative mood, such as `Add Task class`, not `Added Task class`.
- Capitalize the first letter of the subject.
- Do not end the subject with a period.
- Add a `<scope>:` or `<category>:` prefix only when it helps clarify the change.

## Commit Message Body

- For non-trivial commits, include a body.
- Separate the subject and body with a blank line.
- Wrap body lines at 72 characters.
- Use blank lines to separate paragraphs.
- Explain what changed and why it changed; leave detailed how-it-changed
  information to the diff unless it is important context.
- Use bullet points only when they make the body clearer.
- Avoid unnecessary words such as "currently" or "originally" when describing
  the current situation.
- If the body becomes too long, consider whether the work should be split into
  smaller commits.

## Body Structure

When a commit body is needed, prefer this shape:

```text
{current situation, in present tense}

{why it needs to change}

{what is being done about it, in imperative mood}

{why it is done that way}

{any other relevant information}
```

## Branch Names

- Use meaningful names made from relevant keywords.
- Use kebab case, such as `refactor-ui-tests`.
- If the branch relates to an issue, prefer
  `issueNumber-some-keywords-from-issue-title`, such as
  `1234-ui-freeze-error`.
