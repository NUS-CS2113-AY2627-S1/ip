# ATHENA User Guide

ATHENA is a friendly, text-based task manager that helps you keep track of to-dos, deadlines, and events. You need Java 25 installed to use ATHENA. Enter a command and press Enter; ATHENA will confirm the result and save your task list automatically.

## Contents

- [Quick start](#quick-start)
- [Understanding the command format](#understanding-the-command-format)
- [Features](#features)
  - [Adding a to-do](#adding-a-to-do-todo)
  - [Adding a deadline](#adding-a-deadline-deadline)
  - [Adding an event](#adding-an-event-event)
  - [Viewing all tasks](#viewing-all-tasks-list)
  - [Finding tasks](#finding-tasks-find)
  - [Marking a task as done](#marking-a-task-as-done-mark)
  - [Marking a task as not done](#marking-a-task-as-not-done-unmark)
  - [Deleting a task](#deleting-a-task-delete)
  - [Exiting ATHENA](#exiting-athena-bye)
- [Saving your tasks](#saving-your-tasks)
- [Command summary](#command-summary)

## Quick start

1. Make sure Java 25 is installed.
2. Download the `athena.jar` file.

> [!TIP]
> Try the commands in the [command summary](#command-summary) first. Command words and markers such as `todo` and `/by` must be typed in lowercase.

## Understanding the command format

- Words in `UPPER_CASE` are values you should replace. For example, replace `DESCRIPTION` with `read a book`.
- Task numbers refer to the numbers shown by `list`, starting from `1`.
- Dates and times are stored as written, so you can use a format that is meaningful to you, such as `Friday 5pm` or `2026-10-15`.

## Features

### Adding a to-do: `todo`

Adds a task without a deadline or scheduled time.

Format: `todo DESCRIPTION`

Example: `todo read a book`

### Adding a deadline: `deadline`

Adds a task that must be completed by a specified date or time.

Format: `deadline DESCRIPTION /by DATE_OR_TIME`

Example: `deadline submit report /by Friday 5pm`

### Adding an event: `event`

Adds a task that takes place between a start and end time.

Format: `event DESCRIPTION /from START /to END`

Example: `event project meeting /from Monday 2pm /to Monday 4pm`

### Viewing all tasks: `list`

Shows every saved task and its task number. `[ ]` means the task is incomplete, while `[X]` means it is complete. The letters `[T]`, `[D]`, and `[E]` identify to-dos, deadlines, and events respectively.

Format: `list`

Example output:

```text
1.[T][ ] read a book
2.[D][X] submit report (by: Friday 5pm)
3.[E][ ] project meeting (from: Monday 2pm to: Monday 4pm)
```

### Finding tasks: `find`

Shows tasks whose descriptions contain the given keyword, even when the description includes other words. You do not need to enter the full description. The search is case-sensitive and uses the whole text after `find` as one keyword; for example, `find book` matches both `read a book` and `return book`, but `find Book` does not.

Format: `find KEYWORD`

Example: `find book`

Example output:

```less
Here are the matching tasks in your list:
     1.[T][X] read book
     2.[D][X] return book (by: June 6th)
    _______________________________________
```

### Marking a task as done: `mark`

Marks the selected task as complete.

Format: `mark TASK_NUMBER`

Example: `mark 2`

### Marking a task as not done: `unmark`

Marks the selected task as incomplete again.

Format: `unmark TASK_NUMBER`

Example: `unmark 2`

### Deleting a task: `delete`

Permanently removes the selected task. Run `list` first if you are unsure of its number; the remaining tasks are renumbered after deletion.

Format: `delete TASK_NUMBER`

Example: `delete 3`

### Exiting ATHENA: `bye`

Closes ATHENA safely.

Format: `bye`

## Saving your tasks

ATHENA saves changes automatically whenever you add, mark, unmark, or delete a task. Your tasks are restored the next time ATHENA starts, so no separate save command is needed.

The data is kept in `data/athena.txt`, relative to the folder from which ATHENA is run. Avoid editing this file manually, as invalid data can prevent ATHENA from starting.

## Command summary

| Action | Command | Example |
| --- | --- | --- |
| Add a to-do | `todo DESCRIPTION` | `todo read a book` |
| Add a deadline | `deadline DESCRIPTION /by DATE_OR_TIME` | `deadline submit report /by Friday 5pm` |
| Add an event | `event DESCRIPTION /from START /to END` | `event meeting /from 2pm /to 4pm` |
| View all tasks | `list` | `list` |
| Find tasks | `find KEYWORD` | `find book` |
| Mark as done | `mark TASK_NUMBER` | `mark 2` |
| Mark as not done | `unmark TASK_NUMBER` | `unmark 2` |
| Delete a task | `delete TASK_NUMBER` | `delete 3` |
| Exit ATHENA | `bye` | `bye` |

If ATHENA reports an error, check that the command word and markers are lowercase, all required details are present, and the task number appears in the latest `list` output.
