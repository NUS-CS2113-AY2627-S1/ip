---
name: seedu-git-standard
description: Apply the SE-EDU Git conventions when preparing commits, commit messages, or branch names in this project.
---

# Seedu Git Standard

Use this skill for every commit or branch operation in this repository. Follow the SE-EDU Git conventions (commit subjects, commit bodies, and branch names); use the linked standard as the authority when a detail is unclear:

<https://se-education.org/guides/conventions/git.html>

Before creating a commit, check that:

- The subject is meaningful, starts with a capital letter, uses imperative mood, has no final period, and is at most 72 characters (prefer 50 or fewer). A concise `<scope>:` or `<category>:` prefix is allowed when useful.
- A non-trivial commit has a blank line after the subject and a body wrapped at 72 characters. The body explains what changed and why, using the present tense for the situation and imperative mood for the change; the diff explains how. Use paragraphs or bullets when they improve clarity and split work into smaller commits if the message becomes unwieldy.
- Branch names use meaningful kebab-case keywords, or `issueNumber-keywords-from-issue-title` when tied to an issue.

Read [references/conventions.md](references/conventions.md) for the complete checklist and examples before drafting a non-trivial commit message.
