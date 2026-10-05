---
name: seedu-git-standard
description: "Apply the SE-EDU Git conventions when preparing commit messages and branch names in this project."
---

# Seedu Git Standard

Apply these rules when preparing, reviewing, or creating Git commits and
branches in this repository. The source standard is the [SE-EDU Git
conventions](https://se-education.org/guides/conventions/git.html).

## Commit subject

- Write a meaningful subject for every commit.
- Keep the subject near 50 characters and never exceed 72 characters.
- Use the imperative mood (`Add README.md`, not `Added README.md`).
- Capitalize the first letter and do not end the subject with a period.
- Add a relevant `<scope>:` or `<category>:` prefix when it improves clarity.

## Commit body

- Give every non-trivial commit a body separated from the subject by one
  blank line.
- Wrap body lines at 72 characters and use blank lines between paragraphs.
- Explain WHAT the commit changes and WHY the change is needed; the diff
  already communicates HOW.
- Structure the explanation in this order when useful: current situation in
  present tense, why it needs to change, what to do in imperative mood, why
  that approach is appropriate, and any other relevant information.
- Avoid redundant explanations already present in code comments. Use bullets
  when they make several related changes easier to scan.
- Avoid words such as `currently` and `originally`; the commit context makes
  them implicit.

## Branch names

- Use meaningful kebab-case names containing relevant keywords, such as
  `refactor-ui-tests`.
- For issue-related branches, use
  `issueNumber-some-keywords-from-issue-title`, such as
  `1234-ui-freeze-error`.

Use this standard for the commit message whenever a commit is explicitly
authorized. Do not create, amend, or push commits without that authorization.
