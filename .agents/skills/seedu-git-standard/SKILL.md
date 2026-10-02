---
name: seedu-git-standard
description: >-
  Enforces the SE-EDU Git conventions for commit messages, branches, and version control workflows.
  Use whenever proposing, drafting, creating Git commits, or managing branches.
---

# SE-EDU Git Standard

This skill defines the Git conventions based on the [SE-EDU Git Conventions Guide](https://se-education.org/guides/conventions/git.html).

---

## 1. Commit Message Subject Line

Every commit must have a well-written subject line following these rules:

* **Imperative Mood**: Use imperative verbs (e.g., `Add README.md`, `Fix parse error`, `Refactor UI layout`), NOT past tense (`Added`) or gerund (`Adding`).
* **Capitalization**: Capitalize the first letter of the subject line (e.g., `Update sample data`, not `update sample data`).
* **No Trailing Period**: Do not end the subject line with a period (`.`).
* **Length Limits**: Aim for 50 characters or fewer (hard limit: 72 characters).
* **Optional Prefix**: A `<scope>:` or `<category>:` prefix is permitted when helpful:
  * `Foodielover: Update greeting format`
  * `Parser: Add deadline command handling`
  * `bug fix: Prevent index out of bounds on mark`
  * `chore: Update dependency versions`

---

## 2. Commit Message Body

Non-trivial commits must include a body separated from the subject line by a single blank line.

* **Line Wrapping**: Hard-wrap the body text at **72 characters** per line.
* **Paragraph Separation**: Use single blank lines between paragraphs.
* **Content Focus (WHAT & WHY, not HOW)**:
  * Explain *what* the commit does and *why* the change is necessary.
  * The code diff shows *how*; the message provides rationale and context.
  * Minimize repeating details already stated in inline code comments.
* **Standard Body Structure**:
  1. **Current situation** (present tense, avoid words like "currently" or "originally").
  2. **Why it needs to change**.
  3. **What is being done** (use imperative mood; the phrase `Let's ...` can mark this section).
  4. **Why it is done that way** (design rationale/tradeoffs).
  5. **Any other relevant info** (issue references, links).
* **Bullet Points**: Use bulleted lists when itemizing multiple related changes within a single commit.

### Example Commit Message
```text
Foodielover: Implement task listing and mark command

The chatbot only displays a greeting and banner upon startup. Users have
no way to store tasks or view existing entries in memory.

Providing task storage and retrieval enables interactive task tracking
throughout the application session.

Let's,
* add in-memory array storage for up to 100 tasks
* implement the "list" command to print indexed task descriptions
* add "mark" command stub to process completion status updates

Using fixed arrays is sufficient for early iteration before transitioning
to dynamic collections.
```

---

## 3. Branch Naming Conventions

* Use meaningful `kebab-case` names (e.g., `refactor-ui-tests`, `add-deadline-command`).
* If associated with an issue tracker, prefix with the issue number:
  * `1234-fix-parser-crash`
  * `42-add-event-task-type`
