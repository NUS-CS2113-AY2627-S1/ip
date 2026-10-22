---
name: seedu-git-standard
description: The se-education Git conventions for commit messages and branch names that all commits in this project must follow. Use when proposing, writing, or reviewing any commit message, when naming a branch, or when checking whether a commit message is compliant.
---

# seedu Git Standard

Source: https://se-education.org/guides/conventions/git.html
All commits in this repository must follow these rules.

## Subject line

- **Length**: aim for 50 characters, hard limit 72. Count before proposing.
- **Imperative mood** — write as an instruction, not a report.
  - Good: `Add README.md`
  - Bad: `Added README.md`, `Adding README.md`
- **Capitalize the first letter.**
  - Good: `Move index.html file to root`
  - Bad: `move index.html file to root`
- **No trailing period.**
  - Good: `Update sample data`
  - Bad: `Update sample data.`
- **An optional scope or category prefix** may lead the subject. The standard's own examples:
  - `Person class: Remove static imports`
  - `Main.java: Remove blank lines`
  - `bug fix: Add space after name`
  - `chore: Update release date`

## Body

- Separate the subject from the body with a blank line.
- Wrap the body at 72 characters.
- Separate paragraphs with blank lines; bullet points are fine.
- Explain **what** and **why**, not **how** — the diff already shows how.
- Suggested flow: current situation (present tense) → why a change is needed → what is being done
  (imperative) → why this approach → anything else relevant.

## Branch names

- Meaningful keywords in **kebab-case**: `refactor-ui-tests`.
- For issue-linked branches: `issueNumber-keywords-from-title`, e.g. `1234-ui-freeze-error`.

## This project's rules

Two project-specific decisions sit on top of the standard. Both take precedence.

1. **Subject line only — no body.** The repo owner has asked for commit messages that are a single
   sentence. The body rules above still apply *if* a body is ever requested, but the default is a
   bare subject line. Rationale for a change goes into the chat explanation and the code's comments.
   This is a deliberate departure from the standard's preference for an explanatory body.

2. **Conventional-Commits-style lowercase category prefixes**, as already used throughout this
   repo's history: `feat:`, `refactor:`, `style:`, `chore:`, and `fix:` / `docs:` when they apply.
   This is compatible with the standard, which permits a category prefix and itself gives
   `chore: Update release date` as an example.

Combining the two, the required shape is:

```
<category>: <Capitalized imperative phrase, no trailing period>
```

Compliant examples for this project:

```
refactor: Extract user interaction into a Ui class
feat: Add find command for searching tasks
style: Apply seedu Java coding standard
docs: Document the butler voice in AGENTS.md
```

Non-compliant, with the reason:

```
feat: add deadline, todo, event classes          # 'add' not capitalized
refactor: Extracted the Ui class                 # past tense, not imperative
style: Apply the coding standard.                # trailing period
docs: align AGENTS.md with the repo's real state, butler voice, and git workflow
                                                 # 78 chars, over the 72 hard limit
```

Note that commits made before this standard was adopted use a lowercase word after the prefix. Do
not rewrite already-pushed history to fix them; just comply from now on.

## Branch naming in this project

Increment branches are named `branch-<Increment>` (e.g. `branch-A-MoreOOP`) because the course
requires that form. This overrides the kebab-case rule for increment branches. Use kebab-case for
any other branch.

## Checklist before proposing a commit message

1. Is it a single sentence, subject only?
2. Does it start with a lowercase category prefix and a colon?
3. Is the first word after the colon capitalized and imperative?
4. Is there no trailing period?
5. Is it 72 characters or fewer — ideally 50? **Count the characters.**
6. Does it say what changed, not how?
7. Are unrelated changes split into separate commits? One commit per standalone change; agent-file
   changes are separate commits from code changes.

Then present the command for the user to run — per AGENTS.md, never run `git commit` yourself.
