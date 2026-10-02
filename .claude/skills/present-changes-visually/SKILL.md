---
name: present-changes-visually
description: Generate a side-by-side HTML diff page showing what changed in this repo. Use when the user wants to review or examine code changes visually, compare the working tree against the last commit, or compare two commits/tags.
---

# Present changes visually

Produces `_temp/visual-diff.html`, a self-contained side-by-side diff page for
reviewing changes before committing them.

`_temp/` is already listed in `.gitignore`, so generated pages are never committed.

## Usage

Run from the repository root:

```bash
python .claude/skills/present-changes-visually/scripts/generate-split-view-diff.py . HEAD WORKTREE _temp/visual-diff.html
```

Arguments are `<repo> <base> <target> <output>`:

- `<base>` / `<target>` are Git revisions — `HEAD`, `HEAD~1`, a tag such as `Level-2`, or a SHA.
- `<target>` may be the literal `WORKTREE`, meaning the files on disk right now.
  This includes uncommitted **and untracked** files, so newly added files show up.

After generating the page, send it to the user with SendUserFile so they can open it.

## Common comparisons

Uncommitted work against the last commit (the default case):

```bash
python .claude/skills/present-changes-visually/scripts/generate-split-view-diff.py . HEAD WORKTREE _temp/visual-diff.html
```

The most recent commit against the one before it:

```bash
python .claude/skills/present-changes-visually/scripts/generate-split-view-diff.py . HEAD~1 HEAD _temp/visual-diff.html
```

Everything that changed across an increment, using tags:

```bash
python .claude/skills/present-changes-visually/scripts/generate-split-view-diff.py . Level-2 Level-3 _temp/visual-diff.html
```

## Notes

- Uses only the Python standard library — no packages to install.
- Write the output somewhere under `_temp/`. Use a descriptive filename when
  generating more than one page, e.g. `_temp/with-and-without-task-class.html`.
- On Windows the command is `python`; on macOS/Linux use `python3`.
