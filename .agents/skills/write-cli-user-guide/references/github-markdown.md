# GitHub Markdown rules for user guides

Source: GitHub Docs, "Basic writing and formatting syntax":
https://docs.github.com/en/get-started/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax

Use these rules for repository documentation.

## Structure and navigation

- Use `#` through `######` for headings and do not skip levels without a reason.
- Keep heading text stable because GitHub derives section anchors from headings.
- Use unique headings. Duplicate generated anchors receive numeric suffixes and are easy to break.
- Use `[label](#section-anchor)` for section links.
- Prefer relative paths for repository files and images so links work across clones and branches.
- Keep link text on one source line.

## Commands and code

- Wrap command names, filenames, parameters, and short syntax in single backticks.
- Use fenced code blocks for multi-line commands, sessions, or output.
- Add a language identifier such as `bash`, `text`, `json`, or `java` when useful.
- Do not put an entire paragraph in code formatting.

## Lists, tables, and paragraphs

- Separate paragraphs with a blank line.
- Use ordered lists for sequences and unordered lists for non-sequential facts.
- Indent nested lists so their marker aligns under the parent item's text.
- Use tables for compact, scannable comparisons such as command summaries; do not force long prose into narrow cells.

## Links and images

- Write descriptive link text instead of "here" when the destination can be named.
- Embed an image as `![meaningful alt text](relative/path.png)`.
- Use alt text that conveys the image's purpose or information, not merely "screenshot".
- Prefer a relative image path for assets stored in the repository.

## Alerts and escaping

GitHub supports these alert markers: `NOTE`, `TIP`, `IMPORTANT`, `WARNING`, and `CAUTION`.

```markdown
> [!WARNING]
> Back up the data file before editing it manually.
```

Use at most one or two alerts in an article unless the content genuinely requires more. Do not nest alerts.

- Escape a Markdown control character with a backslash when it should appear literally.
- Use an HTML comment for source-only author notes: `<!-- hidden note -->`.
- Prefer Markdown over raw HTML unless GitHub Markdown lacks the needed structure.

## GitHub rendering checks

- Confirm internal anchors after changing headings.
- Confirm relative links from the Markdown file's directory, not the repository root unless the path begins with `/` intentionally.
- Check that code fences are balanced and that copied commands do not contain accidental line breaks.
- Preview tables and images on GitHub or with a compatible renderer.
