# SE-EDU Git conventions checklist

This checklist is distilled from the
[SE-EDU Git conventions](https://se-education.org/guides/conventions/git.html).
Apply every relevant rule below.

## Commit subject

- Give every commit a clear, well-written subject.
- Aim for at most 50 characters. Never exceed 72 characters.
- Use the imperative mood, as in `Add parser error handling` rather than
  `Added parser error handling` or `Adding parser error handling`.
- Capitalize the first letter of the subject's description.
- Do not end the subject with a period.
- When useful, prefix the description with an applicable `<scope>:` or
  `<category>:`, such as `Parser: Handle empty input` or
  `chore: Update release date`.

## Commit body

Add a body for every non-trivial commit.

- Separate the subject and body with one blank line.
- Wrap body text at 72 characters.
- Separate paragraphs with blank lines. Use bullet points when they make the
  explanation clearer.
- Explain **what** the commit changes and **why** the change is appropriate.
  Leave implementation details that are evident from the diff out of the
  message.
- Give enough context for a reader to judge the purpose of the change without
  first reading the diff.
- Avoid repeating information already captured by code comments in the same
  commit.
- Treat an excessively long description as a signal that the commit may need
  to be split into smaller, coherent commits.

When applicable, organize the body in this order:

1. Describe the situation before the change in the present tense.
2. Explain why it needs to change.
3. State what the commit does using the imperative mood.
4. Explain why that approach was chosen.
5. Add other relevant context.

Do not use words such as `currently` or `originally` merely to introduce the
pre-change situation; that timing is already implied. `Let's` may introduce the
part that describes what the commit does, but it is optional.

## Branch names

- Choose a meaningful name made from relevant keywords in kebab case, such as
  `refactor-parser-tests`.
- For a branch tied to an issue, use
  `<issue-number>-<keywords-from-issue-title>`, such as
  `1234-empty-input-error`.
