---
name: seedu-git-standard
description: The SE-EDU Git conventions for commit messages and branch names that this project must follow. Load before writing any commit message or creating a branch in this repository.
---

# SE-EDU Git conventions

Source: https://se-education.org/guides/conventions/git.html

Every commit made in this project must follow the rules below.

## Subject line

- Limit to **50 characters** where possible; **72 is the hard limit**.
- Write in the **imperative mood**: "Add README.md", not "Added README.md" or
  "Adds README.md". A useful test: the subject should complete the sentence
  "If applied, this commit will ...".
- **Capitalise** the first letter.
- **Do not end with a period.**
- An optional scope or category prefix is allowed:
  `Person class: Remove static imports`, `chore: Update release date`.

## Body

- Separate the subject from the body with a **blank line**.
- Wrap the body at **72 characters**.
- Separate paragraphs with blank lines.
- Use bullet points where they aid clarity.
- Explain **what and why, not how** — the diff already shows how.
- Do not repeat what code comments already say.

Recommended flow for the body:

1. Describe the current situation, in **present tense**.
2. Explain why a change is needed.
3. State what is being done, in **imperative mood**.
4. Explain why it is done that way.
5. Add any other relevant information.

## Example

```
Person class: Remove static imports

Static imports of the Assert methods make the test code harder to
read for a newcomer, as it is not obvious where those methods come
from.

Import the Assert class instead and qualify each call, so that the
origin of every assertion is visible at the call site.
```

## Branch names

- Use a meaningful name of relevant keywords in **kebab-case**:
  `refactor-ui-tests`.
- For a branch addressing an issue, prefix with the issue number:
  `1234-ui-freeze-error`.

## Notes for this project

- The guide does not specify tag naming. This project uses the tag names given
  by the course increments (`Level-3`, `A-CodingStandard`, ...), created as
  lightweight tags unless an annotated tag is requested.
- Do not commit or push unless the user explicitly asks.
