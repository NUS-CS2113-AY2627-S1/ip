---
name: seedu-git-standard
description: Apply the SE-EDU Git conventions when creating, reviewing, or proposing commits and branch names in this project.
---

# Seedu Git Standard

Apply this skill whenever a commit is being prepared, reviewed, or proposed, or when a
branch name is being chosen. The authoritative reference is the [SE-EDU Git conventions]
(https://se-education.org/guides/conventions/git.html).

## Commit subject

- Every commit must have a well-written subject line.
- Use the imperative mood, capitalize the first letter, and do not end with a period.
- Keep the subject at 50 characters when practical; never exceed 72 characters.
- Add a meaningful scope or category prefix only when it improves clarity.

## Commit body

- Non-trivial commits must have a body separated from the subject by one blank line.
- Wrap body lines at 72 characters and use blank lines between paragraphs.
- Explain what changed and why it changed, rather than narrating implementation details.
- Describe the situation and motivation in present tense, then describe the change in
  imperative mood. Include relevant rationale or other context when it helps reviewers.
- Use bullet points when they make several related changes easier to scan.

## Branch names

- Use meaningful kebab-case names containing relevant keywords.
- For issue-related branches, use `issueNumber-keywords-from-issue-title`.

## Workflow

Before proposing or creating a commit, inspect the staged diff and verify that the subject
and body satisfy these rules. Do not commit or push unless the user explicitly authorizes
it. Keep unrelated changes out of the commit.
