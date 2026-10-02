---
name: seedu-git-standard
description: Git commit message and branch naming conventions for this project, based on the SE-EDU Git conventions (https://se-education.org/guides/conventions/git.html). Use whenever drafting a commit message, creating a branch, or reviewing a commit for style.
---

# SE-EDU Git Standard

Source: https://se-education.org/guides/conventions/git.html

Apply these rules whenever proposing or creating a commit message, or
naming a branch, in this project.

## Commit subject

- Limit the subject line to 50 characters (hard limit: 72).
- Use imperative mood.
  Good: `Add README.md` Bad: `Added README.md`, `Adding README.md`
- Capitalize the first letter.
  Good: `Move index.html file to root` Bad: `move index.html file to root`
- Do not end the subject line with a period.
  Good: `Update sample data` Bad: `Update sample data.`
- An optional scope/category prefix is fine, e.g.
  `Person class: Remove static imports`, `bug fix: Add space after name`,
  `chore: Update release date`.
- The Conventional Commits format is an acceptable, more elaborate
  alternative if the project wants that structure.

## Commit body

- Separate the subject from the body with a blank line.
- Wrap the body at 72 characters.
- Use blank lines to separate paragraphs.
- Use bullet points where that's clearer than prose.
- Explain WHAT and WHY, not HOW — the diff already shows how; give enough
  context for a reader to judge whether the change was necessary without
  reading the code.
- Minimize repeating information already given in code comments.
- Useful structure to follow: current situation (present tense), why the
  change is needed, what is being done (imperative mood), why it's done
  that way, other relevant info. Avoid words like "currently" or
  "originally" when describing the prior state — say what it is/was
  directly.

## Branch names

- Use a meaningful name made of relevant keywords, in kebab-case, e.g.
  `refactor-ui-tests`.
- For issue-related branches, use
  `issueNumber-some-keywords-from-issue-title`, e.g. `1234-ui-freeze-error`.
