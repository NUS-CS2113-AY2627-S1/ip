---
name: seedu-git-standard
description: Apply the SE-EDU Git conventions when preparing, proposing, creating, or reviewing commits and commit messages, or when naming branches in this repository.
---

# SE-EDU Git Standard

Keep the repository's Git history readable and consistent without changing the
user's intended scope.

## Required reference

Before working on a commit, commit message, or branch name, read
[references/git-standard.md](references/git-standard.md) completely. It is the
project checklist distilled from the
[SE-EDU Git conventions](https://se-education.org/guides/conventions/git.html).

## Working approach

1. Inspect the relevant diff and repository state before drafting a message.
   Distinguish the user's changes from changes made for the current task.
2. Apply the subject rules to every proposed or created commit. Add a body for
   each non-trivial commit and use it to explain the change's purpose and
   rationale.
3. If a message needs an unusually long description, flag that the commit may
   contain multiple concerns. Do not split, amend, squash, or otherwise rewrite
   commits unless the user has authorized that action.
4. Apply the branch-name rules whenever proposing, creating, or renaming a
   branch.
5. Before creating a commit, check the complete message against the reference.
   After creating one, verify the recorded message when practical.

Do not stage files, create or rewrite commits, create or rename branches, or
push changes unless the user's request authorizes the corresponding action.
