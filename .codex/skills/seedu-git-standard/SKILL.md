---
name: seedu-git-standard
description: Apply the SE-EDU Git conventions when creating, reviewing, or proposing commits and branch names in this project.
---

# Seedu Git Standard

Apply this skill whenever creating, reviewing, or proposing a commit or branch
in this repository. It is based on the [SE-EDU Git conventions](https://se-education.org/guides/conventions/git.html).
Use that page for details not summarized here.

## Commit subjects

- Write every subject in the imperative mood, with the first letter
  capitalized and no period at the end.
- Prefer 50 characters or fewer; never exceed 72 characters.
- Add a concise scope or category prefix when it improves clarity, such as
  `Task.java:`, `bug fix:`, or `chore:`.

Examples:

```text
Add task completion support
Task.java: Validate descriptions
```

## Commit bodies

Non-trivial commits must have a body separated from the subject by one blank
line. Wrap body lines at 72 characters and use blank lines between paragraphs.
Explain what changed and why it changed, rather than describing the
implementation steps. A useful order is:

1. Describe the situation in the present tense.
2. Explain why it needs to change.
3. State the change in the imperative mood.
4. Explain the rationale and any relevant additional information.

Use bullet points when they make several related changes easier to scan. Keep
the body detailed enough that a reviewer can understand the rationale without
reading the diff.

## Branch names

- Use meaningful, relevant keywords in kebab case, such as
  `refactor-ui-tests`.
- For issue-related branches, use
  `issueNumber-some-keywords-from-issue-title`, such as
  `1234-ui-freeze-error`.

## Review checklist

Before handing off a commit or commit message, check the subject length,
imperative mood, capitalization, punctuation, and scope. For non-trivial
changes, check the blank separator, 72-character body wrapping, WHAT/WHY
explanation, and branch naming. This skill does not authorize committing or
pushing; follow the repository's agent instructions for those actions.
