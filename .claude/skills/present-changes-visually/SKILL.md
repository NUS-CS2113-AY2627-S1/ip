---
name: present-changes-visually
description: Generate a self-contained, GitHub-style split-view HTML page that visually presents changes in the current Git repository. Use when asked to show, review, share, or inspect code changes visually; compare revisions, branches, commits, or the worktree; or create an HTML diff.
---

# Present Changes Visually

Generate one interactive HTML page containing every changed file as a side-by-side before/after diff. The page folds long unchanged runs, highlights changed words within modified lines, lets readers filter files, and includes collapsed panels for unchanged files.

Use this at the end of each increment so the change can be reviewed before moving on.

## Generate the page

1. Treat the current repository as the target unless the user identifies another repository.
2. Use `HEAD` as the before point and `WORKTREE` as the after point unless the user specifies comparison points. `WORKTREE` includes staged, unstaged, and untracked (but not ignored) files.
3. Write to `_temp/visual-diff.html` unless the user supplies an output path. `_temp/` is gitignored in this repository, so generated pages never enter a commit.
4. Run the bundled generator from the repository root:

   ```powershell
   python .claude/skills/present-changes-visually/scripts/generate-split-view-diff.py . HEAD WORKTREE _temp/visual-diff.html
   ```

   Replace `HEAD`, `WORKTREE`, and the output path with the requested values. The comparison points can be any Git commit-ish such as `HEAD~1`, a tag, a branch, or a commit SHA. Use `WORKTREE` for the current files.

   On this machine both `python` and `python3` resolve to Python 3.14 in PowerShell and in Git Bash. The generator uses only the standard library, so no packages need installing.

5. Confirm the command succeeded and report the path to the generated page. Do not open a browser unless the user asks — pass `--open` only on request.

### Useful variations

- Compare the last commit with the one before it: `. HEAD~1 HEAD`
- Compare a tagged increment with the current work: `. A-Jar WORKTREE`
- Drop the collapsed panels for untouched files: add `--no-unchanged`
- Name the output after the comparison, e.g. `_temp/with-and-without-tasks-class.html`, when the page is meant to be kept or shared.

## Verify output

Check that the page exists and that the generator's summary reports the expected changed-file count. If the count looks wrong, confirm the comparison points with `git status` or `git log --oneline` before regenerating.

## Commit messages

This skill does not commit anything. If a commit is wanted for the reviewed changes, follow the `seedu-git-standard` skill, which encodes this project's rules — a one-sentence subject line and no body.

Note for anyone comparing against the upstream skill: the original referred to a `craft-commit-message` skill that mandated a detailed body. That skill is not installed here, and a detailed body contradicts this project's one-sentence rule, so the reference was replaced.

## Resource

`scripts/generate-split-view-diff.py` is the bundled standard-library-only generator, taken unmodified from https://github.com/se-edu/skill-present-changes-visually. Keep the page self-contained except for optional syntax-highlighting resources loaded by the page.
