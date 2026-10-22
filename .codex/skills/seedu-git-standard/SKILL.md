---
name: seedu-git-standard
description: Apply the SE-EDU Git conventions when planning, proposing, reviewing, or creating commits and branches in this project. Use for every commit message, commit operation, branch creation, or Git-history review.
---

# SE-EDU Git Standard

Follow [references/git-conventions.md](references/git-conventions.md) for every Git commit and branch.

## Commit workflow

1. Inspect the complete staged diff before committing.
2. Keep the commit focused; split unrelated changes into separate commits.
3. Draft the subject according to the reference.
4. Add a body for every non-trivial commit. Explain what changed and why, not implementation details visible in the diff.
5. Check subject and body line lengths before committing.
6. Review the final commit message and staged file list.

Do not create a commit whose message violates the reference. If the user supplies a non-compliant message, improve it and explain the correction briefly.

## Branch workflow

Use meaningful kebab-case names. Prefix issue-related branches with the issue number, such as `1234-ui-freeze-error`.
