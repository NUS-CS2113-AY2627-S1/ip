# Git standard

This project checklist is derived from the [SE-EDU Git conventions](https://se-education.org/guides/conventions/git.html). The linked page is authoritative.

## Commit subject

Every commit must have a clear subject that follows these rules:

- Aim for at most 50 characters. Never exceed 72 characters.
- Use the imperative mood, such as `Add validation for task names`.
- Capitalise the first letter.
- Do not end with a full stop.
- Add an optional `<scope>:` or `<category>:` prefix only when it helps identify the affected area or kind of change.

## Commit body

Add a body for every non-trivial commit.

- Separate the subject from the body with one blank line.
- Wrap every body line at 72 characters.
- Use blank lines between paragraphs.
- Use bullet points when they make the content clearer.
- Explain what changed and why. Leave implementation details that are clear from the diff out of the message.
- Give enough context for a reader to judge the purpose of the change without reading the diff.
- Minimise repetition of information already stated in code comments.

Use this order when the information applies:

1. State the existing situation in the present tense.
2. Explain why it needs to change.
3. State what the commit does in the imperative mood.
4. Explain why that approach was chosen.
5. Add other relevant context.

Do not use `currently` or `originally` to introduce the existing situation because the tense already conveys that meaning.

If the body becomes too long or describes separate concerns, propose smaller focused commits. Do not split or create commits without the user's authority.

## Branch names

- Use meaningful keywords that describe the branch purpose.
- Write the name in kebab case, such as `refactor-ui-tests`.
- For a branch tied to an issue, use `issueNumber-keywords-from-title`, such as `1234-ui-freeze-error`.
