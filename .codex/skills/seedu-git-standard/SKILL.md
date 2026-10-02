---
name: seedu-git-standard
description: Apply the SE-EDU Git conventions when proposing commit messages, creating commits, naming branches, or reviewing Git history in this project.
---

# SE-EDU Git Standard

Follow the current [SE-EDU Git conventions](https://se-education.org/guides/conventions/git.html).

For commit messages:

- Write a clear subject in imperative mood.
- Capitalize the subject and do not end it with a period.
- Aim for 50 characters and never exceed 72 characters.
- Add an optional scope or category prefix only when it improves clarity.
- For non-trivial commits, separate the body with a blank line and wrap body lines at 72 characters.
- Explain what situation motivates the change, why it matters, what the commit changes, and why that approach is suitable. Let the diff explain implementation details.
- Split unrelated or overly broad changes into focused commits.

For branch names, use meaningful kebab-case words. Prefix issue-related branches with the issue number when applicable.

Before committing, inspect the staged diff, confirm it contains one coherent change, and check the complete message against these rules.
