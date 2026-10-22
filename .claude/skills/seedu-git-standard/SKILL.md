---
name: seedu-git-standard
description: >-
  The SE-EDU Git conventions used in this course: commit-message format, commit
  granularity, and branch naming. Invoke before writing any commit message or
  creating a branch in this repository.
---

# SE-EDU Git Conventions

Source: https://se-education.org/guides/conventions/git.html

Apply these to **every** commit and branch created in this project.

---

## 1. Commit message: subject line

- **Length:** aim for <= 50 characters; hard limit 72.
- **Mood:** imperative, as if completing "This commit will ___".
  - Good: `Add README.md`
  - Bad: `Added README.md`, `Adding README.md`
- **Capitalization:** capitalize the first word.
  - Good: `Move index.html file to root`
  - Bad: `move index.html file to root`
- **Punctuation:** no trailing period.
  - Good: `Update sample data`
  - Bad: `Update sample data.`
- **Optional prefix:** a scope or category may lead the subject, e.g.
  `Person class: Remove static imports` or `chore: Update release date`.

---

## 2. Commit message: body

- Separate the subject from the body with **one blank line**.
- **Wrap the body at 72 characters.**
- Separate paragraphs with blank lines; use bullet points where they help.
- Explain **what** and **why**, not **how** — the diff already shows how.
- Give enough detail that a reader can judge the change without reading the code.
  Do not repeat what code comments already say.
- A body that keeps growing is a sign the commit should be split.
- Suggested flow for the body:
  1. Describe the current situation (present tense).
  2. Explain why it needs to change.
  3. State what the commit does (imperative mood).
  4. Say why this approach was chosen.
  5. Add any other relevant notes.
- Language: use present tense for existing conditions; avoid "currently" /
  "originally" (context implies them); "Let's ..." is a fine way to introduce
  the change.

### Example

```
Person class: Remove static imports

The Person class uses static imports for its collaborator classes,
which makes it unclear where each symbol comes from.

Let's remove the static imports and use fully qualified references
instead, matching the rest of the model package.
```

---

## 3. Commit granularity

- One commit = one logical change. Keep unrelated changes in separate commits.
- Commit working code; do not commit code that breaks the build.

---

## 4. Branch names

- Meaningful keywords in **kebab-case**: `refactor-ui-tests`.
- For an issue-linked branch: `issueNumber-keywords-from-issue-title`, e.g.
  `1234-ui-freeze-error`.

---

## 5. Tags

- Use lightweight tags unless an annotated tag is explicitly requested
  (project rule from `AGENTS.md`).

---

## 6. Checklist before committing

- [ ] Subject in imperative mood, capitalized, no period, <= 50 chars (<= 72 hard).
- [ ] Blank line between subject and body.
- [ ] Body wrapped at 72 chars, explains what/why.
- [ ] Commit is one logical change and the code builds.
- [ ] Do not commit or push unless the user explicitly asked.
