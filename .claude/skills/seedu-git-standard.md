---
name: seedu-git-standard
description: SE-EDU Git standard for commits and branching
---

# SE-EDU Git Standard

Follow these rules for all Git commits and branches in this project.

## Commit Message Format

### Subject Line
- **Length**: Aim for 50 characters; hard limit 72 characters
- **Mood**: Use imperative mood (e.g., "Add feature" not "Added feature")
- **Capitalization**: Start with capital letter
- **Punctuation**: Do not end with period
- **Optional prefix**: Include `<scope>:` or `<category>:` when applicable
  - Examples: `Task class: Add toString() method`, `Parser: Fix edge case handling`

### Message Body
- **Structure**: Blank line between subject and body
- **Width**: Wrap at 72 characters
- **Content**: Explain WHAT changed and WHY, not HOW
- **Organization**: 
  - Present the current situation
  - Explain why change is needed
  - Describe what's being done
  - Add relevant context or trade-offs
- **Format**: Use bullet points when helpful for clarity
- **Avoid redundancy**: Don't repeat information already in code comments

### Attribution
End all commit messages with:
```
Co-Authored-By: Claude Haiku 4.5 <noreply@anthropic.com>
Claude-Session: <session-url>
```

## Branch Naming

- **Format**: Use kebab-case with meaningful keywords (e.g., `refactor-parser-logic`)
- **Issue-related**: Follow `issueNumber-keywords-from-title` (e.g., `123-fix-deadline-parsing`)

## Commit Checklist

Before committing:
1. ✓ Subject line: Imperative mood, 50 chars or less
2. ✓ First letter capitalized, no period at end
3. ✓ Body explains WHY and WHAT
4. ✓ No redundancy with code comments
5. ✓ All staged files are intended (use `git status` to verify)
6. ✓ No secrets or sensitive data
