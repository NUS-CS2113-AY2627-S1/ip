# CLI user guide pattern

This pattern is distilled from the supplied AddressBook Level 3 user guide. Adapt it to the product; do not copy AddressBook-specific facts.

## Default information architecture

1. **Product name and user-facing introduction**
   - Say what the application helps the user do.
   - Identify that it is CLI or CLI-first and state the main benefit.
2. **Contents**
   - Link to major sections and individual commands when the guide is long.
3. **Quick start**
   - State prerequisites and the exact supported runtime version.
   - Link to the release artifact.
   - Explain where to place it, how to launch it, what success looks like, and one safe first command.
4. **Features**
   - Begin with a compact explanation of syntax conventions.
   - Give one task-oriented section per command.
5. **Data and persistence**
   - Explain automatic saving, file location, transfer or backup steps, and manual-edit risks.
6. **FAQ**
   - Answer recurring user questions directly.
7. **Known issues**
   - State the trigger, observed behavior, and workaround for each verified issue.
8. **Command summary**
   - Provide a compact table of commands, formats, and representative examples.

## Syntax convention callout

Explain only conventions the application actually uses:

- `UPPER_CASE` represents a value supplied by the user.
- `[BRACKETS]` represent optional syntax.
- `...` represents repetition; state the permitted count.
- Prefixes such as `n/` or `by/` must be shown exactly as parsed.
- Argument-order freedom, ignored extra arguments, quoting, case sensitivity, and whitespace behavior must be verified from code or tests.

## Strong command example

```markdown
## Adding a deadline: `deadline`

Adds a task that must be completed by a specified date and time.

Format: `deadline DESCRIPTION /by DATE_TIME`

- `DESCRIPTION` must not be blank.
- `DATE_TIME` uses the format `yyyy-MM-dd HHmm`.

Example: `deadline submit report /by 2026-10-02 2359`

UniEnable adds "submit report" with a deadline of 2 October 2026 at 11:59 pm.
```

The expected result turns the example into a testable user promise. Omit it only when the effect is completely self-evident.

## Evidence checklist

For each documented command, verify:

- command word and aliases;
- exact parameter order and prefixes;
- required versus optional fields;
- accepted date, time, number, category, and text formats;
- index semantics and whether filtering changes indexes;
- search matching rules and sorting behavior;
- confirmation, overwrite, delete, or irreversible behavior;
- data persistence and automatic-save timing;
- output wording only when the wording matters to the user;
- version availability.

## Review checklist

- A new user can install and launch the application using only the quick start.
- Every current command appears once in the detailed features and once in the summary.
- Every example is accepted by the current parser.
- Planned commands are clearly labeled or removed from the current-feature sections.
- Internal implementation details appear only when they affect user behavior.
- FAQ and known-issue entries include actionable answers or workarounds.
