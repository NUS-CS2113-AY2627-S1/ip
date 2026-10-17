# SE-EDU Git conventions checklist

Source of truth: [SE-EDU Git conventions](https://se-education.org/guides/conventions/git.html).

## Commit subject

- Give every commit a clear subject.
- Aim for at most 50 characters; never exceed 72 characters.
- Use the imperative mood, as if completing: "If applied, this commit will ...".
- Capitalize the first letter.
- Do not end with a period.
- Add an optional scope or category prefix only when it improves clarity.

## Commit body

- Add a body for non-trivial commits.
- Separate the subject and body with one blank line.
- Wrap body lines at 72 characters and separate paragraphs with blank lines.
- Explain WHAT changes and WHY the change is needed or designed that way. Let the diff show HOW.
- Describe the existing situation in the present tense and describe the action in the imperative mood.
- Avoid redundant implementation details and information already clear from code comments.
- Use bullets when they make multiple related points easier to scan.
- If the body becomes too long or covers unrelated reasons, split the work into finer-grained commits.

## Branch names

- Use meaningful keywords in kebab case, such as `refactor-ui-tests`.
- For work tied to an issue, use `issueNumber-relevant-keywords`, such as `1234-ui-freeze-error`.

## Final check

- Confirm the message describes exactly the staged diff.
- Confirm the commit contains one logical change.
- Confirm subject and body line lengths before proposing or creating the commit.
