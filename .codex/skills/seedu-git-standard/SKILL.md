---
name: seedu-git-standard
description: Apply the SE-EDU Git conventions when proposing or creating commits and branch names in this project.
---

# SE-EDU Git Standard

Use this skill whenever proposing, drafting, reviewing, or creating Git commits in this project. Follow the SE-EDU Git conventions at https://se-education.org/guides/conventions/git.html.

## Commit Subject

- Every commit must have a clear subject line.
- Keep the subject near 50 characters when practical. Do not exceed 72 characters.
- Use imperative mood, for example `Add parser tests`, not `Added parser tests`.
- Capitalize the first letter of the subject.
- Do not end the subject with a period.
- Add a meaningful `<scope>:` or `<category>:` prefix when it improves clarity, for example `Parser: Handle empty input` or `chore: Update release date`.

## Commit Body

- Add a body for non-trivial commits.
- Separate the subject from the body with one blank line.
- Wrap body lines at 72 characters.
- Use blank lines between paragraphs.
- Explain what the change is and why it is useful. Do not spend the body explaining how the diff implements it unless that context is needed.
- Make the explanation detailed enough that a reader can judge the change without reading the diff.
- Avoid repeating information already obvious from code comments in the same commit.
- If the body gets too long, consider whether the work should be split into smaller commits.

For substantial commits, structure the body in this order when it fits:

1. Present-tense description of the existing situation.
2. Why it needs to change.
3. What is being done about it, using imperative mood where natural.
4. Why this approach is appropriate.
5. Any other relevant context.

Avoid words such as `currently` and `originally` when describing the existing situation; present tense already signals that context. It is acceptable to use `Let's` to introduce the concrete change being made.

## Branch Names

- Use meaningful branch names made from relevant keywords in kebab case, for example `refactor-ui-tests`.
- If the branch is tied to an issue, use `issueNumber-some-keywords-from-issue-title`, for example `1234-ui-freeze-error`.
- Preserve any repository-specific branch prefix required by higher-priority instructions, such as `codex/`, while keeping the descriptive part kebab-cased.

## Verification

- Before proposing or creating a commit, inspect the staged and unstaged changes enough to write an accurate message.
- Check the subject length, imperative mood, capitalization, and missing-period rule.
- For non-trivial commits, check that the body has a blank line after the subject and wraps at 72 characters.
