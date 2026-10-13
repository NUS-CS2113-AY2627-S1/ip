---
description: "Generate a self-contained, interactive HTML page that presents changed files as a GitHub-style side-by-side diff."
---

# Present Changes Visually

This skill generates an interactive, self-contained HTML page displaying Git repository changes in a GitHub-style split-view format.

## Key Capabilities

The tool creates a single HTML file that:
- Shows side-by-side before/after diffs for all modified files
- Collapses lengthy unchanged sections
- Highlights word-level changes within modified lines
- Provides file filtering options
- Includes collapsible panels for unchanged files

## Workflow

**Setup:**
- Target the current repository by default
- Compare `HEAD` (before) against `WORKTREE` (after) unless specified otherwise
- Output to `_temp/visual-diff.html` unless redirected

**Execution:**
Run this command from the repository root:
```bash
py .claude/skills/present-changes-visually/generate-split-view-diff.py \
  . HEAD WORKTREE _temp/visual-diff.html
```

Substitute comparison points with any Git reference (commit SHAs, branches, tags, or `WORKTREE` for current files).

**Verification:**
Confirm successful generation and note the output path. Open the HTML file in a browser only upon user request.

## Commit Messages

When proposing commits for reviewed changes, reference the `craft-commit-message` skill to ensure messages include an imperative subject line and detailed explanation of modifications and rationale.
