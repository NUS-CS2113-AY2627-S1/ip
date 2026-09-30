---
name: seedu-git-standard
description: Use when creating Git commits, commit messages, tags, or branches in a SE-EDU student project (CS2103/CS2103T iP or tP). Enforces the Git conventions from se-education.org/guides/conventions/git.html: commit subject line format, commit body structure, and branch naming.
---

# SE-EDU Git conventions

This skill encodes the SE-EDU Git conventions for student projects.
Source: https://se-education.org/guides/conventions/git.html

## Commit message: subject line

- Limit the subject to 50 characters (hard limit: 72 chars).
- Imperative mood: `Add README.md` good; `Added README.md`, `Adding README.md` bad.
- Capitalize the first letter.
- No period at the end.
- Optional `<scope>:` or `<category>:` prefix:
  - `Person class: Remove static imports`
  - `Main.java: Remove blank lines`
  - `bug fix: Add space after name`
  - `chore: Update release date`
- Do not include the SE-EDU (Git) standard body conditions in the subject.

## Commit message: body

Write a body for non-trivial commits.

- Blank line between subject and body.
- Wrap the body at 72 characters.
- Blank lines between paragraphs.
- Use bullet points when they help.
- Explain WHAT the commit does and WHY, not HOW — the diff shows the HOW.
  Do not repeat what code comments already say.
- Structure the body:

  ```
  {current situation}            -- present tense

  {why it needs to change}

  {what is being done about it}  -- imperative mood, often introduced with "Let's"

  {why it is done that way}

  {any other relevant info}
  ```

- Avoid `currently` / `originally`; the current situation is implied.
- If the description gets too long, split the commit into finer-grained pieces.

Example:

```
Person attributes classes: extract a parent class PersonAttribute

Person attribute classes (e.g. Name, Address, Age etc.) have some common
behaviors (e.g. isValid()).

The common behaviors across person attribute classes cause code
duplication.

Let's pull up behaviors common to all person attribute classes into a new
parent class named PersonAttribute.

Using inheritance is preferable over composition in this situation because
the common behaviors are not composable.
```

## Branch names

- Meaningful kebab-case keywords, e.g. `refactor-ui-tests`.
- Issue-related branches: `issueNumber-some-keywords-from-issue-title`,
  e.g. `1234-ui-freeze-error`.

## Procedure

1. Write the subject: imperative, capitalized, <= 50 chars, no trailing period.
2. Decide whether the commit is non-trivial; if so, add a body per the structure
   above, wrapped at 72 chars.
3. Validate the final message against every rule before proposing a commit.
4. Use lightweight tags for version/level markers unless told otherwise; tag
   naming mirrors the level/version, e.g. `Level-3`.