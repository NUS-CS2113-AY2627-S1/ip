---
name: write-cli-user-guide
description: Create, revise, or review a GitHub-rendered Markdown user guide for a command-line or CLI-first application. Use for UserGuide.md work, CS2113-style project documentation, command reference sections, quick-start instructions, command-format notation, screenshots, FAQs, known issues, and command-summary tables; also use when converting an existing guide or implemented CLI behavior into clear end-user documentation.
---

# Write CLI User Guide

Produce a task-oriented `UserGuide.md` that reflects implemented behavior and renders correctly on GitHub. Treat the application, tests, and current project documentation as the factual source; never invent commands, parameters, defaults, validation rules, or future features.

## Load the references

- Read [references/user-guide-pattern.md](references/user-guide-pattern.md) before drafting or restructuring a guide.
- Read [references/github-markdown.md](references/github-markdown.md) when formatting links, images, tables, alerts, anchors, lists, or code.

## Workflow

1. **Ground the behavior.** Inspect the current `UserGuide.md`, command parser, command classes, tests, sample data, release version, and relevant screenshots. Ask for missing project-specific facts only when they cannot be verified.
2. **Build a command inventory.** For every user-facing command, record its purpose, exact syntax, required and optional arguments, constraints, behavior, side effects, examples, and error-prone cases.
3. **Choose the structure.** Use the pattern reference as the default, but omit irrelevant sections and preserve project-required headings.
4. **Write for a first-time user.** Lead with the goal and outcome. Use short steps, concrete values, and copyable commands. Explain domain terms on first use.
5. **Format for GitHub.** Use semantic headings, inline code for command names and short syntax, fenced blocks for multi-line sessions, relative repository links, meaningful image alt text, and tables only for compact comparisons or summaries.
6. **Reconcile the whole guide.** Update the table of contents, section anchors, screenshots, command summary, version labels, FAQ, and known issues whenever behavior changes.
7. **Validate.** Preview the rendered Markdown if possible, check every documented example against the application or tests, and run:

   ```bash
   python3 scripts/check_user_guide.py path/to/UserGuide.md
   ```

   Resolve all errors. Review warnings deliberately; they are advisory and may be acceptable for a project-specific format.

## Command section contract

Write each command section in this order unless the command needs a clearer variant:

1. Task-oriented heading containing the command, such as `## Adding a task: add`.
2. One-sentence user outcome.
3. Exact format line with the full syntax in inline code.
4. Bullets for constraints, defaults, ordering rules, and side effects.
5. One or more realistic examples, each followed by its expected result when that result is not obvious.
6. A screenshot only when it adds information that text cannot convey efficiently.

Use `UPPER_CASE` for user-supplied placeholders, `[OPTIONAL]` for optional items, and `...` only after explaining whether repetition means zero-or-more or one-or-more. Do not claim arguments may appear in any order unless the parser permits it.

## Quality bar

- Document the released behavior, not the intended design.
- Keep terminology, capitalization, prefixes, and example values consistent across all sections.
- Make commands safe to copy from Markdown and PDF exports; avoid breaking one command across lines when possible.
- Prefer one primary way to complete a task, then state alternatives.
- Distinguish current features from planned features explicitly; do not mix roadmap material into current command instructions.
- State persistence location, automatic-save behavior, and risks of manual data edits when applicable.
- Use alerts sparingly and only for information that materially affects success or safety.
- Ensure screenshots match the current UI, contain no private data, and have useful alt text.
- Keep the command summary concise and mechanically consistent with the detailed sections.

## Deliverable

Edit or create the repository's canonical Markdown guide unless the user requests another format. If a PDF is requested, treat Markdown as the source of truth and use the PDF workflow to render and visually verify the exported document.
