# PR Reviewing Guide

Use this guide when helping review pull requests in this project.

Source: https://se-education.org/guides/guidelines/PRs-reviewing.html

## Review Comment Style

- Add comments at the relevant lines or small code blocks instead of giving only one broad PR-level comment.
- Phrase suggestions as questions where possible.
- Prefer "I like", "I am not sure I like", or "Could..." over absolute judgments such as "good", "bad", "wrong", or direct commands.
- Ask for clarification when the author's reason is unclear.
- Suggest alternatives for the author to consider, without implying that only one answer is acceptable.
- Compliment clear or thoughtful code when there is something genuinely worth pointing out.
- Avoid overusing "please"; it can accidentally sound condescending in review comments.
- Do not repeat the same comment many times. Comment on a few examples, then mention that the same issue appears elsewhere.
- Write comments in clear, regular English for future readers too.

## Useful Comment Patterns

- Could this be extracted into a helper method to make the main flow easier to read?
- Any reason why this is handled here instead of in a more specific class?
- Would a more descriptive variable name make this easier to understand?
- I like how this separates the parsing logic from the task behavior.
- I am not sure I like this approach because it makes the method handle two different responsibilities.
- Could this be shortened or wrapped to stay within the coding standard?
- I noticed this same spacing issue in a few other places too.

## Review Checklist

- Check correctness first: crashes, wrong outputs, edge cases, and behavior regressions.
- Check whether the change matches the stated requirement.
- Check whether errors are handled in a user-friendly way.
- Check whether names reveal intent.
- Check whether methods/classes have a single clear responsibility.
- Check whether public classes and nontrivial public methods have useful Javadocs.
- Check Java convention issues: package declarations, explicit imports, K&R braces, 4-space indentation, naming conventions, private fields by default, and reasonable line length.
- Check for stale commented-out code, unnecessary comments, magic numbers, and repeated logic.
- Keep the review concise and specific.

## If Reviewing As A Peer

- Be collaborative, not authoritative.
- Ask questions before assuming the author is wrong.
- Explain why a suggestion matters when it is not obvious.
- Avoid turning preference comments into blockers.
- Thank the author or acknowledge good parts when appropriate.
