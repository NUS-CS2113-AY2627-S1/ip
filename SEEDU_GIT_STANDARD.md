# SE-Edu Git Standard (seedu-git-standard)

**Project-specific skill**: This skill defines Git commit conventions for this project, based on https://se-education.org/guides/conventions/git.html

## Commit Message: Subject Line

**Basic Rules** (mandatory):

1. **Well-written subject line**: Every commit must have a clear, descriptive subject.
2. **Length**: Limit to 50 characters (hard limit: 72 characters).
3. **Imperative mood**: Use commands as if instructing the codebase to do something.
   - ✅ Good: `Add README.md`, `Fix NPE in Parser`, `Update version number`
   - ❌ Bad: `Added README.md`, `Fixed NPE`, `Updating version`
4. **Capitalize first letter**: Start with a capital letter.
   - ✅ Good: `Move index.html file to root`
   - ❌ Bad: `move index.html file to root`
5. **No period at end**: Do not end the subject line with punctuation.
   - ✅ Good: `Update sample data`
   - ❌ Bad: `Update sample data.`

**Optional Enhancements** (recommended):
- Add a `<scope>:` or `<category>:` prefix when applicable (e.g., `Person class: Remove static imports`, `bug fix: Add space after name`, `chore: Update release date`)

## Commit Message: Body

**Basic Rules** (mandatory for non-trivial commits):

1. **Separate subject and body**: Use a blank line between the subject and body.
2. **Line wrapping**: Wrap body text at 72 characters.
3. **Paragraph separation**: Use blank lines to separate logical paragraphs.

**Intermediate Rules** (recommended):
1. **Explain WHAT and WHY, not HOW**: 
   - Use the body to explain what the commit accomplishes and why it was necessary.
   - The reader can examine the diff to understand how the change was implemented.
   - Give enough detail so readers can judge if the change is good without reading the diff.
   - Minimize repeating information already in code comments.

2. **Use bullet points**: Organize information with bullet lists when helpful.

Example of a well-structured commit message:

```
Unify variations of toSet() methods

There are several methods that convert a collection to a set. In some
cases the conversion is in-lined as a code block in another method.

Unifying all those duplicated code improves the code quality.

As a step towards such unification, let's extract those duplicated code
blocks into separate methods in their respective classes. Doing so will
make the subsequent unification easier.
```

## Enforcement

**All commits to this repository must follow these standards.**

When proposing commits, ensure:
- Subject line follows the imperative mood and capitalization rules
- Subject line is concise (50 chars preferred, max 72)
- Non-trivial commits include a detailed body explaining WHAT and WHY
- Proper blank line separation between subject and body
- Body is wrapped at 72 characters

When reviewing commits, verify compliance with these rules before merging.

## References

- [SE-Edu Git Conventions](https://se-education.org/guides/conventions/git.html)
- [Conventional Commits](https://www.conventionalcommits.org/) (alternative format)
- [Git Documentation on Commit Messages](https://git-scm.com/docs/git-commit#_discussion)
