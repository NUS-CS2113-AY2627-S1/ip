---
name: seedu-git-standard
description: Apply the SE-EDU Git commit message standard when writing or proposing any commit message. Use whenever committing, drafting a commit message, or reviewing one for compliance.
---

# SE-EDU Git Commit Message Standard

Source: [SE-EDU Git conventions](https://se-education.org/guides/conventions/git.html).

Apply this to every commit message in this project by default — don't wait to be asked.

## Subject line

- Aim for 50 characters; 72 is a hard limit.
- Imperative mood: `Add README.md`, not `Added README.md` or `Adding README.md`.
- Capitalize the first letter.
- No trailing period.
- An optional `<scope>:`/`<category>:` prefix is fine when it adds clarity, e.g. `Person class: Remove static imports`, `bug fix: Add space after name`.

## Body

- Required for any non-trivial commit; a one-line subject is fine only for genuinely trivial changes.
- Blank line between subject and body.
- Wrap body lines at 72 characters.
- Blank lines between paragraphs; bullet points where a list is clearer than prose.
- Explain **what** the commit does and **why** — not **how**; the diff already shows how. If the explanation is getting long, that's a sign the commit should be split into smaller ones.
- Don't repeat what's already said in code comments added by the same commit.
- Suggested structure (adapt as needed, skip parts that don't apply):
  1. Current situation (present tense, skip words like "currently"/"originally" — they're implied).
  2. Why it needs to change.
  3. What is being done about it (imperative mood; `Let's …` works well to introduce this part).
  4. Why it's done that way, and any other relevant info (links, references).

## Branch names

- Meaningful, kebab-case: `refactor-ui-tests`.
- If tied to an issue: `issueNumber-some-keywords-from-issue-title`, e.g. `1234-ui-freeze-error`.

## Applying this

1. Draft every commit message — including ones you propose for the user to approve — following the rules above.
2. Never rewrite or amend a commit that's already been made just to fix a message that's a soft-limit miss (e.g. a subject a few characters over 50 but under 72) — that risks orphaning tags or losing work. Apply the standard going forward instead, and flag existing non-compliant commits to the user rather than silently rewriting history.
