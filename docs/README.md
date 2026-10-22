# Doug User Guide

Doug is a command-line chatbot that helps you keep track of todos, deadlines, and events. It saves changes automatically, so your tasks will still be available the next time you start it.

## Getting started

1. Start Doug using Java 25.
2. Type one command at a time and press <kbd>Enter</kbd>.
3. Enter `help` at any time to see the available commands.

In the command formats below, values in angle brackets such as `<description>` are placeholders. Replace them with your own text and do not type the angle brackets.

## Viewing your tasks: `list`

Shows every saved task and its number. Use these numbers with `mark`, `unmark`, and `delete`.

Format: `list`

Example:

```text
list
```

Each task begins with its type and status:

- `[T]` is a todo, `[D]` is a deadline, and `[E]` is an event.
- `[ ]` means the task is not done; `[X]` means it is done.

## Adding a todo: `todo`

Adds a task that does not have a date or time.

Format: `todo <description>`

Example:

```text
todo read chapter 5
```

Doug adds `[T][ ] read chapter 5` to the task list.

## Adding a deadline: `deadline`

Adds a task that must be completed by a particular date or time.

Format: `deadline <description> /by <date or time>`

Example:

```text
deadline submit report /by Friday 5pm
```

Doug adds `[D][ ] submit report (by: Friday 5pm)` to the task list. Dates and times are displayed exactly as you enter them.

## Adding an event: `event`

Adds an activity with a start and end date or time.

Format: `event <description> /from <start> /to <end>`

Example:

```text
event project meeting /from Monday 2pm /to Monday 3pm
```

Doug adds `[E][ ] project meeting (from: Monday 2pm to: Monday 3pm)` to the task list.

## Marking a task as done: `mark`

Marks a task as completed. Get the task number from `list` first.

Format: `mark <task number>`

Example:

```text
mark 2
```

The task's status changes from `[ ]` to `[X]`.

## Marking a task as not done: `unmark`

Returns a completed task to the not-done state.

Format: `unmark <task number>`

Example:

```text
unmark 2
```

The task's status changes from `[X]` to `[ ]`.

## Deleting a task: `delete`

Permanently removes a task. Get the task number from `list` first.

Format: `delete <task number>`

Example:

```text
delete 3
```

Task numbers may change after a deletion, so use `list` again before referring to another task.

## Finding tasks: `find`

Shows tasks whose descriptions contain the given keyword. The search is not case-sensitive.

Format: `find <keyword>`

Example:

```text
find report
```

The numbers in search results are for display only. Use `list` to find a task's current number before marking, unmarking, or deleting it.

## Viewing help: `help`

Displays a summary of Doug's task-management commands and their formats.

Format: `help`

## Exiting Doug: `bye`

Closes Doug. Any task changes have already been saved automatically.

Format: `bye`

## Command summary

| Action | Command |
| --- | --- |
| View all tasks | `list` |
| Add a todo | `todo <description>` |
| Add a deadline | `deadline <description> /by <date or time>` |
| Add an event | `event <description> /from <start> /to <end>` |
| Mark a task as done | `mark <task number>` |
| Mark a task as not done | `unmark <task number>` |
| Delete a task | `delete <task number>` |
| Find tasks | `find <keyword>` |
| View help | `help` |
| Exit Doug | `bye` |
