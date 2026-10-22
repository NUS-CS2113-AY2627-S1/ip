---
name: seedu-git-standard
description: Apply the SE-EDU Git conventions for this project's commits and branches.
---

# SE-EDU Git Standard

Follow the SE-EDU Git conventions:
https://se-education.org/guides/conventions/git.html

For commit messages:

- Write a clear subject line.
- Keep the subject around 50 characters where practical, and never over 72
  characters.
- Use the imperative mood in the subject, such as `Add parser support`.
- Capitalize the first letter of the subject.
- Do not end the subject with a period.
- Add a scope or category prefix only when it improves clarity.
- For non-trivial commits, include a body separated from the subject by a blank
  line.
- Wrap body lines at 72 characters.
- Explain what changed and why; avoid repeating how the diff implements it.
- Use the body structure where useful: current situation, why it needs to
  change, what is being done, why that approach is used, and any other relevant
  information.

For branch names:

- Use meaningful kebab-case names, such as `refactor-ui-tests`.
- If tied to an issue, prefer `issueNumber-some-keywords`.
- For course increments, preserve the required grading names exactly, such as
  `branch-Level-5`, `branch-A-Packages`, and their matching tags.
