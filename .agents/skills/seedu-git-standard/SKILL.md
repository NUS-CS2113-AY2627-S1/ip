---
name: seedu-git-standard
description: Craft, review, and validate Git commit messages and branch names according to the se-edu Git conventions. Use whenever proposing, creating, amending, or reviewing a commit or commit message, or when naming a branch in this repository.
---

# se-edu Git Standard

Apply the se-edu Git conventions to every commit message and branch name in this repository.

Before proposing, reviewing, or creating a commit, read [references/git-standard.md](references/git-standard.md). Treat the linked se-edu page as authoritative when the checklist does not cover an edge case. Preserve the user's scope and authorization: this skill does not authorize committing, amending, tagging, pushing, or creating a branch.

## Workflow

1. Inspect the changes intended for the commit. When changes are staged, use the staged diff as the source of truth.
2. Check whether the changes form one focused commit. Recommend splitting unrelated or excessively broad changes before committing.
3. Draft a subject that satisfies every subject rule in the reference. Add a body for every non-trivial commit.
4. Ensure the body explains what changed and why, matches the actual diff, and does not merely narrate implementation details.
5. Check subject and body line lengths before proposing or using the message. Report any deliberate exception.

When the user asks only for a commit message, return the complete ready-to-use message without running Git commands.
