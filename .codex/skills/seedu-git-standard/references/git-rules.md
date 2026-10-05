# SE-EDU Git conventions

Source: <https://se-education.org/guides/conventions/git.html>

Use this checklist whenever proposing, reviewing, or creating commits and
branches in this project.

## Commit subject

- Write a clear subject for every commit.
- Prefer 50 characters or fewer and never exceed 72 characters.
- Use the imperative mood, such as `Add README.md`.
- Capitalize the first letter.
- Do not end the subject with a period.
- Add a scope or category prefix only when it helps clarity.

## Commit body

- Add a body for non-trivial commits.
- Separate the subject and body with one blank line.
- Wrap body lines at 72 characters.
- Use blank lines or bullet points to separate ideas.
- Explain what the commit changes and why, not how the code was implemented.
- Describe the current situation, why it needs to change, what to do, and the
  rationale for the chosen change.
- Avoid repeating information already clear from code comments.

## Branch names

- Use meaningful names made from relevant keywords.
- Use kebab case, such as `refactor-ui-tests`.
- For issue-related branches, use `issueNumber-keywords-from-title`.
