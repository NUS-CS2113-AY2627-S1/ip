# SE-EDU Git Conventions

Source: https://se-education.org/guides/conventions/git.html

## Commit subject

- Write a clear subject for every commit.
- Aim for at most 50 characters; never exceed 72 characters.
- Use imperative mood, such as `Add README.md`, not `Added README.md` or `Adding README.md`.
- Capitalize the first letter.
- Do not end with a period.
- Optionally prefix an applicable scope or category, such as `Main.java: Remove blank lines` or `chore: Update release date`.

## Commit body

- Add a body for every non-trivial commit.
- Separate the subject and body with one blank line.
- Wrap body text at 72 characters.
- Separate paragraphs with blank lines and use bullet points when they improve clarity.
- Explain what changed and why; leave implementation details to the diff.
- Provide enough context for a reviewer to judge the change without reading the diff.
- Describe the existing situation in present tense, explain why it needs to change, describe the change in imperative mood, and explain the chosen approach when relevant.
- Avoid redundant terms such as `currently` and `originally` when describing the existing situation.
- Avoid repeating information already captured in code comments.
- Split the work into smaller commits if the message becomes too long or covers unrelated concerns.

## Branch names

- Use a meaningful kebab-case name made from relevant keywords, such as `refactor-ui-tests`.
- For issue-related work, use `issueNumber-keywords-from-issue-title`, such as `1234-ui-freeze-error`.
