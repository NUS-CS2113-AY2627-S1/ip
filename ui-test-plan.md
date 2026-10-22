# Nga UI Test Plan

## Persistence test: automatic saving

Compile and run Nga with Java 25 from the project root. Before starting, remove
the existing `data/nga.txt` file so the test begins with a clean output file.

Enter:

```text
todo read book
deadline return book /by 2019-06-06 1800
event project meeting /from Aug 6th 2pm /to 4pm
mark 1
unmark 1
bye
```

Expected result:

- Nga confirms each task addition.
- Nga confirms both status changes.
- `data/nga.txt` is created automatically.
- The file contains one line per task in this format:

```text
T | 0 | read book
D | 0 | return book | 2019-06-06T18:00:00
E | 0 | project meeting | Aug 6th 2pm | 4pm
```

- The file is rewritten after every successful add, mark, or unmark command.
- Starting Nga again loads the saved tasks in the same order and with the same
  completion statuses.

## Persistence test: automatic loading

After completing the saving test, start Nga again and enter:

```text
list
bye
```

Expected result:

- Nga displays the three tasks saved by the previous session.
- The task types, details, order, and completion statuses are preserved.

## UI regression checks

- `list` still displays tasks in their insertion order.
- `mark` displays `[X]` and `unmark` displays `[ ]`.
- Invalid task numbers do not rewrite the file.
- `bye` ends the session normally.

## Error-handling checks

Manually edit `data/nga.txt` to include blank lines, unknown task types, invalid
status values, and records with missing fields. Restart Nga and enter `list`.

Expected result:

- Nga starts without crashing.
- Valid records are loaded.
- Invalid records are skipped.
- Commands with missing or non-numeric task numbers return a helpful message.
- Empty input and end-of-file input do not crash Nga.
- If the data file cannot be written, Nga keeps the task in memory and warns
  that the task list could not be saved.
