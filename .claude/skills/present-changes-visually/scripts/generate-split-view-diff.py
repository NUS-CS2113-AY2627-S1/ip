#!/usr/bin/env python3
"""Generate a self-contained side-by-side HTML diff for a Git repository.

Usage:
    generate-split-view-diff.py <repo> <base> <target> <output.html>

<base> and <target> are Git revisions (e.g. HEAD, HEAD~1, a tag, a SHA).
<target> may also be the literal WORKTREE, meaning "the files as they are on
disk right now", which includes uncommitted and untracked files.
"""

import difflib
import html
import subprocess
import sys
from pathlib import Path

WORKTREE = "WORKTREE"


def git(repo, *args):
    """Run a git command inside repo and return its stdout (empty on failure)."""
    result = subprocess.run(
        ["git", "-C", repo, *args],
        capture_output=True,
        text=True,
        errors="replace",
    )
    if result.returncode != 0:
        return ""
    return result.stdout


def changed_files(repo, base, target):
    """Return the sorted list of paths that differ between base and target."""
    if target == WORKTREE:
        tracked = git(repo, "diff", "--name-only", base)
        untracked = git(repo, "ls-files", "--others", "--exclude-standard")
    else:
        tracked = git(repo, "diff", "--name-only", base, target)
        untracked = ""

    paths = set()
    for chunk in (tracked, untracked):
        for path in chunk.splitlines():
            if path.strip():
                paths.add(path.strip())
    return sorted(paths)


def file_lines(repo, rev, path):
    """Return the lines of path at revision rev, or [] if it does not exist."""
    if rev == WORKTREE:
        full_path = Path(repo) / path
        if not full_path.is_file():
            return []
        text = full_path.read_text(encoding="utf-8", errors="replace")
    else:
        text = git(repo, "show", f"{rev}:{path}")
    return text.splitlines()


CSS = """
:root { color-scheme: light dark; }
* { box-sizing: border-box; }
body {
  margin: 0; padding: 2rem 1.5rem;
  font: 15px/1.6 -apple-system, "Segoe UI", Roboto, sans-serif;
  background: #f6f8fa; color: #1f2328;
}
h1 { font-size: 1.4rem; margin: 0 0 .3rem; }
.meta { color: #656d76; font-size: .9rem; margin-bottom: 2rem; }
.file { background: #fff; border: 1px solid #d0d7de; border-radius: 8px;
        margin-bottom: 1.5rem; overflow: hidden; }
.file > h2 { font-size: .95rem; margin: 0; padding: .7rem 1rem;
             background: #f6f8fa; border-bottom: 1px solid #d0d7de;
             font-family: ui-monospace, "Cascadia Code", Consolas, monospace; }
.scroll { overflow-x: auto; }
table.diff { width: 100%; border-collapse: collapse;
             font-family: ui-monospace, "Cascadia Code", Consolas, monospace;
             font-size: 12.5px; }
table.diff td { padding: 1px 6px; vertical-align: top;
                white-space: pre-wrap; word-break: break-word; }
table.diff .diff_header { background: #f6f8fa; color: #8c959f;
                          text-align: right; user-select: none;
                          width: 1%; white-space: nowrap; }
td.diff_next { display: none; }
.diff_add { background: #d1f8d9; }
.diff_sub { background: #ffd7d5; }
.diff_chg { background: #fff5b1; }
.empty { color: #656d76; padding: 1rem; }
@media (prefers-color-scheme: dark) {
  body { background: #0d1117; color: #e6edf3; }
  .file { background: #161b22; border-color: #30363d; }
  .file > h2 { background: #0d1117; border-color: #30363d; }
  table.diff .diff_header { background: #0d1117; color: #6e7681; }
  .diff_add { background: #12261e; }
  .diff_sub { background: #341a1f; }
  .diff_chg { background: #2e2a12; }
  .meta, .empty { color: #8b949e; }
}
"""


def build_page(repo, base, target, paths):
    """Return the complete HTML page comparing base and target."""
    differ = difflib.HtmlDiff(tabsize=4, wrapcolumn=90)
    sections = []

    for path in paths:
        old_lines = file_lines(repo, base, path)
        new_lines = file_lines(repo, target, path)
        table = differ.make_table(
            old_lines, new_lines, fromdesc=base, todesc=target, context=True, numlines=4
        )
        sections.append(
            f'<div class="file"><h2>{html.escape(path)}</h2>'
            f'<div class="scroll">{table}</div></div>'
        )

    if not sections:
        sections.append('<div class="file"><p class="empty">No changes found.</p></div>')

    return (
        "<!doctype html>\n<html><head><meta charset='utf-8'>"
        "<meta name='viewport' content='width=device-width, initial-scale=1'>"
        f"<title>Visual diff: {html.escape(base)} to {html.escape(target)}</title>"
        f"<style>{CSS}</style></head><body>"
        f"<h1>Visual diff</h1>"
        f'<p class="meta">Comparing <strong>{html.escape(base)}</strong> '
        f"to <strong>{html.escape(target)}</strong> &mdash; "
        f"{len(paths)} file(s) changed.</p>"
        + "\n".join(sections)
        + "</body></html>\n"
    )


def main():
    if len(sys.argv) != 5:
        print(__doc__, file=sys.stderr)
        return 2

    repo, base, target, output = sys.argv[1:5]

    if not git(repo, "rev-parse", "--git-dir"):
        print(f"error: {repo} is not a Git repository", file=sys.stderr)
        return 1

    paths = changed_files(repo, base, target)
    page = build_page(repo, base, target, paths)

    out_path = Path(output)
    out_path.parent.mkdir(parents=True, exist_ok=True)
    out_path.write_text(page, encoding="utf-8")

    print(f"Wrote {out_path} ({len(paths)} file(s) changed).")
    return 0


if __name__ == "__main__":
    sys.exit(main())
