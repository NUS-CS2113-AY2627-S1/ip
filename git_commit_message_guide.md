# Git Commit Message Guide

Use this as a quick reference when writing commits for the project.

## 1. Subject line format

Use:

```text
<Scope>: <Imperative verb> <change>
```

Examples:

```text
Auth: Add password validation
Storage: Remove duplicate records
Parser: Handle malformed input
Tests: Add parser unit tests
Docs: Update setup instructions
Bug fix: Prevent duplicate usernames
Refactor: Extract validation logic
Chore: Remove unused files
```

### Subject rules

- Try to keep the subject within **50 characters**.
- Never exceed **72 characters**.
- Use the **imperative mood**.
- Capitalize the first letter.
- Do **not** end with a period.
- Add a meaningful scope or category when useful.

Good:

```text
Auth: Add password validation
Parser: Handle empty input
Main.java: Remove unused import
```

Bad:

```text
auth: added password validation.
Fixed some bugs
Updating parser
```

### Useful imperative verbs

```text
Add
Remove
Fix
Update
Extract
Rename
Move
Prevent
Handle
Validate
Refactor
Replace
Introduce
Simplify
```

A useful question for the subject is:

> What did I tell the codebase to do?

---

## 2. Small or trivial commits

A body is not necessary for very small commits.

Examples:

```text
Docs: Fix README typo
```

```text
Tests: Add missing parser test
```

```text
Main.java: Remove unused import
```

---

## 3. Standard commit template

Use this for most non-trivial commits:

```text
<Scope>: <Imperative summary>

<Describe the situation or problem in present tense>

<Explain why it needs to change>

Let's,
* <imperative description of change 1>
* <imperative description of change 2>
* <imperative description of change 3>

<Explain why this approach is appropriate>

<Optional: issue, limitation, follow-up, or reference>
```

### Example

```text
Auth service: Validate user credentials

The login flow accepts credentials without checking whether the supplied
username and password match an existing account.

Invalid credentials must be rejected so that unauthorized users cannot
access protected features.

Let's,
* validate credentials against stored account data
* return an authentication failure for invalid credentials
* add tests for successful and unsuccessful login attempts

Keeping authentication logic in the auth service keeps credential
validation separate from the user interface.
```

---

## 4. Shorter feature template

Use this when the change is meaningful but does not need a long explanation:

```text
<Scope>: <Imperative summary>

<What problem or limitation exists>

<Why it needs to change>

Let's,
* <change>
* <change>
```

---

## 5. Bug fix template

```text
Bug fix: <Imperative summary>

<Describe the incorrect behavior in present tense>

<Explain why this behavior is a problem>

Let's,
* <fix the problematic behavior>
* <add or update tests that cover the bug>

<Optional: explain why this fix was chosen>
```

### Example

```text
Bug fix: Reject duplicate usernames

Account creation allows multiple users to register the same username.

Usernames identify accounts and must therefore remain unique.

Let's,
* reject registration when the username already exists
* add tests for duplicate username registration
```

---

## 6. Refactor template

```text
<Component>: <Imperative refactoring summary>

<Describe the code-quality problem>

<Explain why the existing structure is undesirable>

Let's,
* <refactoring step>
* <refactoring step>

<Explain why the new structure is preferable>
```

### Example

```text
Parser: Extract command validation

Command parsing and validation are handled in the same method.

Combining both responsibilities makes the parser harder to understand
and test independently.

Let's,
* extract validation into a dedicated method
* reuse the validation logic across command types

Separating validation from parsing keeps each method focused on one
responsibility.
```

---

## 7. Quick template

Keep this beside you while coding:

```text
<Scope>: <Imperative verb> <change>

<Situation or problem>

<Why it matters>

Let's,
* <what is changed>
* <what is changed>

<Why this approach>
```

---

## 8. What to write in the body

Focus on **WHAT** and **WHY**, not **HOW**.

The diff already shows how the implementation works.

Use the body to explain:

1. What problem, limitation, or situation exists?
2. Why does it matter?
3. What does this commit change?
4. Why is this approach appropriate?
5. Is there anything else a reviewer should know?

Avoid repeating details that are already obvious from the code.

---

## 9. Body formatting rules

- Separate the subject and body with a blank line.
- Wrap body lines at about **72 characters**.
- Use blank lines between paragraphs.
- Use bullet points when they improve readability.
- Describe the situation in **present tense**.
- Describe changes using the **imperative mood**.
- Avoid unnecessary words such as `currently` or `originally` when the
  context already makes the timing clear.

---

## 10. Branch naming reminder

Use meaningful **kebab-case** branch names.

Examples:

```text
add-user-authentication
refactor-command-parser
fix-duplicate-usernames
update-storage-tests
```

If the branch relates to an issue, use:

```text
<issue-number>-<keywords-from-issue-title>
```

Example:

```text
1234-ui-freeze-error
```

---

## Final checklist before committing

- [ ] Subject describes one clear change.
- [ ] Subject uses an imperative verb.
- [ ] Subject begins with a capital letter.
- [ ] Subject has no trailing period.
- [ ] Subject is preferably 50 characters or fewer.
- [ ] Non-trivial commit has a body.
- [ ] Body explains WHAT and WHY rather than implementation details.
- [ ] Subject and body are separated by a blank line.
- [ ] Body is wrapped at about 72 characters.
- [ ] Commit contains one logical unit of work.
