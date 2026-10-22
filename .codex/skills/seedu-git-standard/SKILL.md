---
name: seedu-git-standard
description: Prepare and review Git commits and branch names for this project using the SE-EDU Git conventions. Use when proposing, writing, reviewing, or creating a commit, and when naming a branch; loading this skill does not itself authorize a commit, push, or branch change.
---

# SE-EDU Git Standard

Follow the [SE-EDU Git conventions](https://se-education.org/guides/conventions/git.html) for this project.

## Commit workflow

1. Inspect the complete staged diff before writing or approving a commit message.
2. Check that the staged changes form one coherent, reasonably sized change. If the explanation becomes long or covers unrelated concerns, recommend or create finer-grained commits within the user's authorization.
3. Write and verify the subject and, for every non-trivial commit, the body using the rules below.
4. Do not commit or push unless the user has explicitly authorized that action.

## Subject

- Write a meaningful summary in the imperative mood: use `Add README.md`, not `Added README.md` or `Adding README.md`.
- Capitalize the first letter.
- Do not end with a period.
- Aim for 50 characters or fewer; never exceed 72 characters.
- An applicable `<scope>:` or `<category>:` prefix is allowed, for example `Main.java: Remove blank lines` or `chore: Update release date`.

## Body

- Include a body for every non-trivial commit. Separate it from the subject with one blank line.
- Wrap body text at 72 characters and separate paragraphs with blank lines. Use bullet points when they improve clarity.
- Explain what the change is and why it is needed or designed that way. Leave implementation mechanics to the diff and avoid repeating code comments.
- Give enough context for a reviewer to judge the change without first reading the diff.
- When useful, structure the explanation as: present-tense current situation, why it needs to change, imperative description of what the commit does, why that approach was chosen, and other relevant information.
- Avoid redundant time qualifiers such as `currently` and `originally`. `Let's` may introduce the description of the change.

## Branch names

- Use a meaningful kebab-case name made from relevant keywords, such as `refactor-ui-tests`.
- For an issue-related branch, use `issueNumber-keywords-from-issue-title`, such as `1234-ui-freeze-error`.
