# MimiMeow User Guide

MimiMeow is a command-line task manager that helps you record todos,
deadlines, and events. It can also show what is happening today, what is
coming up, and which deadlines are overdue.

## Quick start

1. Ensure that Java 25 is installed.
2. Open the project in IntelliJ IDEA.
3. Run `mimimeow.MimiMeow`.
4. Type a command and press <kbd>Enter</kbd>.

MimiMeow saves your tasks automatically in `data/mimimeow.txt`. Your saved
tasks are loaded the next time the application starts. The `data` folder and
file are created automatically when you first make a change.

> [!CAUTION]
> Do not edit `data/mimimeow.txt` while MimiMeow is running. Invalid data can
> prevent MimiMeow from loading or saving tasks.

## Command summary

| Action | Format |
| --- | --- |
| Add a todo | `todo DESCRIPTION` |
| Add a deadline | `deadline DESCRIPTION /by DATE_TIME` |
| Add an event | `event DESCRIPTION /from START /to END` |
| List all tasks | `list` |
| Show today's scheduled tasks | `today` |
| Show upcoming tasks | `upcoming` |
| Show overdue deadlines | `overdue` |
| Mark a task as complete | `mark TASK_NUMBER` |
| Mark a task as incomplete | `unmark TASK_NUMBER` |
| Delete a task | `delete TASK_NUMBER` |
| Exit MimiMeow | `bye` |

`DESCRIPTION` must contain at least one non-space character. Dates and times
must use the format `d/M/yyyy HHmm`, such as `2/12/2027 1800`.

## Understanding the task display

MimiMeow uses the following symbols:

- `[T]` identifies a todo.
- `[D]` identifies a deadline.
- `[E]` identifies an event.
- `[ ]` means that a task is incomplete.
- `[X]` means that a task is complete.

For example:

```text
1. [T][ ] prepare quotation
2. [D][X] submit report (by: 2 Dec 2027, 6:00 PM)
3. [E][ ] client meeting (from: 3 Dec 2027, 2:00 PM to: 3 Dec 2027, 3:00 PM)
```

Task numbers are the numbers shown by the `list` command. They can change
after a task is deleted, so run `list` again before using a task number if you
are unsure.

## Adding a todo: `todo`

Adds a task that does not have a specific date or time.

Format: `todo DESCRIPTION`

Example:

```text
todo prepare quotation
```

Expected task:

```text
[T][ ] prepare quotation
```

## Adding a deadline: `deadline`

Adds a task that must be completed by a specific date and time.

Format: `deadline DESCRIPTION /by DATE_TIME`

Example:

```text
deadline submit report /by 2/12/2027 1800
```

Expected task:

```text
[D][ ] submit report (by: 2 Dec 2027, 6:00 PM)
```

## Adding an event: `event`

Adds an activity with a start and end date and time. The end cannot be before
the start.

Format: `event DESCRIPTION /from START /to END`

Example:

```text
event client meeting /from 3/12/2027 1400 /to 3/12/2027 1500
```

Expected task:

```text
[E][ ] client meeting (from: 3 Dec 2027, 2:00 PM to: 3 Dec 2027, 3:00 PM)
```

Events may span more than one date. An event that starts on 3 December and
ends on 4 December is shown by `today` on either date.

## Listing all tasks: `list`

Displays every task and its current task number.

Format: `list`

Example output:

```text
Here are the tasks in your list:
1. [T][ ] prepare quotation
2. [D][X] submit report (by: 2 Dec 2027, 6:00 PM)
```

## Showing today's tasks: `today`

Displays deadlines and events occurring on the current date, ordered by time.
Both complete and incomplete tasks are included. Todos are not included
because they do not have a date.

Format: `today`

## Showing upcoming tasks: `upcoming`

Displays incomplete deadlines and events that are due or start at the current
time or later. Results are ordered chronologically.

Format: `upcoming`

## Showing overdue deadlines: `overdue`

Displays incomplete deadlines whose due date and time have passed. Completed
deadlines and events are not included.

Format: `overdue`

## Marking a task as complete: `mark`

Marks the task at the given task number as complete.

Format: `mark TASK_NUMBER`

Example:

```text
mark 2
```

## Marking a task as incomplete: `unmark`

Marks the task at the given task number as incomplete.

Format: `unmark TASK_NUMBER`

Example:

```text
unmark 2
```

## Deleting a task: `delete`

Permanently removes the task at the given task number.

Format: `delete TASK_NUMBER`

Example:

```text
delete 2
```

> [!TIP]
> Use `list` to check the task number before deleting a task.

## Exiting the application: `bye`

Exits MimiMeow. Changes are already saved whenever the task list is updated,
so no separate save command is needed.

Format: `bye`

## Troubleshooting

### MimiMeow rejects a date or time

Use `d/M/yyyy HHmm`. The day and month may use one or two digits, while the
time must use the 24-hour clock with four digits.

Valid examples include:

- `2/12/2027 1800`
- `02/12/2027 0905`

### MimiMeow cannot find a task number

Run `list`, then enter a positive whole-number task number that currently
appears in the list.

### MimiMeow reports that saved data is invalid

Close MimiMeow and fix or replace `data/mimimeow.txt` before restarting the
application. MimiMeow will not overwrite data that it could not load.
