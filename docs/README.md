# Gizmo User Guide

Gizmo is a simple command-line task manager. Use it to add, view, search, complete, and remove tasks.

## Quick start

1. Start Gizmo.
2. Type a command and press Enter.
3. Use `list` whenever you need to see the current task numbers.
4. Type `bye` when you are finished.

Gizmo saves changes automatically in `data/gizmo.txt`.

## Commands

### Add a todo

Use a todo for a task without a date or time.

Format:

```text
todo <DESCRIPTION>
```

Example:

```text
todo read textbook
```

### Add a deadline

Use a deadline for a task that must be completed by a particular date or time.

Format:

```text
deadline <DESCRIPTION> /by <DATE_OR_TIME>
```

Example:

```text
deadline submit project report /by Friday
```

### Add an event

Use an event for a task with a start and end time.

Format:

```text
event <DESCRIPTION> /from <START> /to <END>
```

Example:

```text
event project meeting /from Monday 2pm /to Monday 3pm
```

### List all tasks

Shows every task and its current one-based task number.

```text
list
```

Use these numbers with `mark`, `unmark`, and `delete`.

### Mark a task as done

```text
mark <TASK_NUMBER>
```

Example:

```text
mark 2
```

### Mark a task as not done

```text
unmark <TASK_NUMBER>
```

Example:

```text
unmark 2
```

### Find tasks

Searches task descriptions for the supplied keyword. Matching is case-sensitive.

```text
find <KEYWORD>
```

Example:

```text
find book
```

Gizmo displays the matching tasks in their original order. If there are no matches, it reports that no matching tasks were found.

### Delete a task

Removes the task with the specified task number.

```text
delete <TASK_NUMBER>
```

Example:

```text
delete 3
```

### Exit Gizmo

Closes the chatbot.

```text
bye
```

## Helpful reminders

- Task numbers start at `1`, not `0`.
- Use the exact markers `/by`, `/from`, and `/to` for deadlines and events.
- Adding, marking, unmarking, and deleting tasks are saved automatically.
- `list` and `find` only display information; they do not change your task list.
