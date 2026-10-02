# SE-EDU Git Conventions

Source: <https://se-education.org/guides/conventions/git.html>.

## Commit subjects

- Every commit has a well-written subject.
- Prefer 50 characters or fewer; 72 characters is the hard limit.
- Write in imperative mood (`Add README.md`, not `Added README.md`).
- Capitalize the first letter and do not end the subject with a period.
- A meaningful optional scope or category may precede the subject, for example
  `Task.java: Add completion status` or `bug fix: Handle empty input`.

## Commit bodies

- Non-trivial commits require a detailed body after one blank line.
- Wrap body lines at 72 characters and use blank lines between paragraphs.
- Explain what changed and why; the diff explains how.
- A useful structure is: current situation in present tense, reason for the
  change, imperative description of what is being done, rationale, and any
  other relevant information.
- Use bullets when they improve clarity. Avoid duplicating code comments.
- If explaining the change needs an excessively long body, consider splitting
  the work into finer-grained, cohesive commits.

## Branch names

- Use meaningful keywords in kebab case, such as `refactor-ui-tests`.
- For issue-related branches, use `issueNumber-keywords-from-title`, such as
  `1234-ui-freeze-error`.
