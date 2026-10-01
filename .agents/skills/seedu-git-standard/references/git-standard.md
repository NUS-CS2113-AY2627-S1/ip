# Git standard checklist

Source: [se-edu Git conventions](https://se-education.org/guides/conventions/git.html).

## Commit subject

- Give every commit a clear subject.
- Aim for 50 characters or fewer; 72 characters is the absolute maximum.
- Use the imperative mood, as if completing the phrase "This commit will ...".
- Capitalize the first word of the subject and do not end it with a period.
- Describe the result precisely. Avoid vague subjects such as "Update files" or "Fix stuff".
- An optional scope or category prefix may be used when it adds clarity, for example `Parser: ...` or `chore: ...`.

## Commit body

- Include a body for any non-trivial commit, including behavior changes, refactors, configuration changes, and coordinated edits across files. A small, self-evident change may use only a subject.
- Separate the subject and body with one blank line.
- Wrap every body line at 72 characters. Separate paragraphs with blank lines and use bullet points when they make the explanation clearer.
- Explain what the change accomplishes and why it is needed or designed that way. Leave low-level implementation mechanics to the diff unless they are important context.
- Describe the pre-change situation in the present tense. Describe the action taken in the imperative mood where practical.
- Avoid redundant wording and unnecessary temporal qualifiers such as "currently" or "originally".
- If the body becomes difficult to explain concisely, check whether the changes should be split into smaller, focused commits.

## Branch names

- Use a meaningful kebab-case name made from relevant keywords, such as `refactor-ui-tests`.
- For work tied to an issue, start with the issue number followed by issue-title keywords, such as `1234-ui-freeze-error`.

## Pre-commit review

- Inspect the exact staged changes and confirm that the message describes all and only those changes.
- Confirm the commit is focused and does not mix unrelated concerns.
- Check spelling, subject length, body wrapping, imperative mood, capitalization, and punctuation.
- Follow repository-specific authorization, tagging, and pushing rules in `AGENTS.md` in addition to this standard.
