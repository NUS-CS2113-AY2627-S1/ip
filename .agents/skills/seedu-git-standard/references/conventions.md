# SE-EDU Git conventions checklist

This project follows the SE-EDU Git conventions:
<https://se-education.org/guides/conventions/git.html>

## Commit subject

- Every commit has a meaningful subject line.
- Aim for 50 characters or fewer; never exceed 72 characters.
- Use imperative mood (`Add README.md`, not `Added README.md`).
- Capitalize the first letter.
- Do not end the subject with a period.
- Add a concise `<scope>:` or `<category>:` prefix when it adds useful context, such as `Task class: Add completion state` or `chore: Update release date`.

## Commit body

- Non-trivial commits include a body separated from the subject by one blank line.
- Wrap body lines at 72 characters and use blank lines between paragraphs.
- Explain what changed and why it was needed; the diff provides the implementation details.
- Describe the current situation in the present tense, then state the change in imperative mood. Include the rationale and any other relevant information.
- Avoid filler terms such as `currently` and `originally`. Minimize repetition of code comments.
- Use bullets when they make a list of changes easier to scan. If the message becomes too long, split the work into smaller commits.

## Branch names

- Use meaningful keywords in kebab case, for example `refactor-ui-tests`.
- For issue-related branches, use `issueNumber-keywords-from-issue-title`, for example `1234-ui-freeze-error`.
