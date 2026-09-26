---
name: seedu-git-standard
description: Apply the SE-EDU Git conventions when creating commits or recommending commit messages in this project.
---

# SE-EDU Git Standard

Apply these rules to all commits and commit-message recommendations in this
repository. The authoritative reference is the
[SE-EDU Git conventions](https://se-education.org/guides/conventions/git.html).

## Commit messages

- Give every commit a well-written subject line.
- Keep the subject within 72 characters, preferably within 50 characters.
- Use imperative mood, capitalize the first letter, and do not end with a
  period.
- Add a scope or category prefix only when it improves clarity.
- Add a body for every non-trivial commit. Separate it from the subject with a
  blank line and wrap it at 72 characters.
- Use the body to explain what changed and why, not how it was implemented.
- Structure the body around the current situation, why it needs to change,
  what to do, and why that approach is appropriate.

## Branches and related Git actions

- Use meaningful kebab-case branch names. If applicable, prefix them with the
  issue number.
- Do not commit or push unless the user explicitly requests it.
- Prefer lightweight tags unless an annotated tag is explicitly requested.

Before proposing a message, inspect the staged or working-tree diff so the
subject and body accurately describe the actual change.
