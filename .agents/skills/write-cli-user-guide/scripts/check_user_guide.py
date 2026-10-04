#!/usr/bin/env python3
"""Advisory checks for a GitHub-rendered CLI UserGuide.md."""

from __future__ import annotations

import argparse
import re
import sys
from collections import Counter
from pathlib import Path


def github_anchor(heading: str) -> str:
    heading = re.sub(r"<[^>]+>", "", heading)
    heading = re.sub(r"[*_~`]", "", heading).strip().lower()
    heading = re.sub(r"\s+", "-", heading)
    return re.sub(r"[^\w\-]", "", heading, flags=re.UNICODE)


def check(path: Path) -> tuple[list[str], list[str]]:
    errors: list[str] = []
    warnings: list[str] = []

    if not path.is_file():
        return [f"file does not exist: {path}"], warnings

    text = path.read_text(encoding="utf-8")
    lines = text.splitlines()

    if text.count("```") % 2:
        errors.append("unbalanced fenced code blocks")

    headings: list[tuple[int, str, int]] = []
    in_fence = False
    for number, line in enumerate(lines, 1):
        if line.lstrip().startswith("```"):
            in_fence = not in_fence
            continue
        if in_fence:
            continue
        match = re.match(r"^(#{1,6})\s+(.+?)\s*$", line)
        if match:
            headings.append((len(match.group(1)), match.group(2), number))

    for previous, current in zip(headings, headings[1:]):
        if current[0] > previous[0] + 1:
            warnings.append(
                f"line {current[2]}: heading jumps from H{previous[0]} to H{current[0]}"
            )

    anchors = [github_anchor(title) for _, title, _ in headings]
    duplicates = sorted(anchor for anchor, count in Counter(anchors).items() if anchor and count > 1)
    if duplicates:
        warnings.append("duplicate generated heading anchors: " + ", ".join(duplicates))

    for match in re.finditer(r"!\[([^\]]*)\]\(([^)]+)\)", text):
        if not match.group(1).strip():
            line = text.count("\n", 0, match.start()) + 1
            warnings.append(f"line {line}: image has empty alt text")

    for number, line in enumerate(lines, 1):
        if re.match(r"^\s*(?:\*\*)?Format(?:\*\*)?:", line, flags=re.IGNORECASE):
            if "`" not in line:
                warnings.append(f"line {number}: format syntax is not in inline code")

    if not any(title.lower().strip("` ") == "quick start" for _, title, _ in headings):
        warnings.append("no Quick start heading found")
    if not any("command summary" in title.lower() for _, title, _ in headings):
        warnings.append("no Command summary heading found")

    return errors, warnings


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("markdown_file", type=Path)
    args = parser.parse_args()

    errors, warnings = check(args.markdown_file)
    for message in errors:
        print(f"ERROR: {message}")
    for message in warnings:
        print(f"WARN: {message}")
    if not errors and not warnings:
        print("OK: no issues found")
    else:
        print(f"Summary: {len(errors)} error(s), {len(warnings)} warning(s)")
    return 1 if errors else 0


if __name__ == "__main__":
    sys.exit(main())
