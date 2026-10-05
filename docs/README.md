# BigBrother User Guide

BigBrother is a command-line task manager that keeps track of todos,
deadlines, and events. It saves your tasks automatically, so they are available
again the next time you start the application.

## Quick start

1. Ensure Java 25 is installed.
2. Place `BigBrother.jar` in the folder where you want its data to be stored.
3. Open a terminal in that folder.
4. Run `java -jar "BigBrother.jar"`.
5. Enter one command at a time and press <kbd>Enter</kbd> after each command.

BigBrother creates `data/bigbrother.txt` automatically in the same working
folder. You do not need to create or edit this file yourself.

## Command format

- Words in `UPPER_CASE` are values that you supply.
- Type commands without the surrounding backticks.
- Task numbers are shown by the `list` command and start from `1`.

## Features

### Add a todo

Adds a task that has no deadline or event time.

```text
todo DESCRIPTION
```

Example: `todo borrow book`

### Add a deadline

Adds a task that must be completed by a particular date or time.

```text
deadline DESCRIPTION /by YYYY-MM-DD
deadline DESCRIPTION /by YYYY-MM-DD HHmm
```

Examples: `deadline submit report /by 2026-10-15` and
`deadline submit report /by 2026-10-15 1800`.
These display as `Oct 15 2026` and `Oct 15 2026 6:00 PM`, respectively.
Existing free-form deadlines such as `Friday 6pm` still load and display, but
only the formats above are interpreted as calendar dates and times.

### Add an event

Adds a task that takes place between a starting and ending time.

```text
event DESCRIPTION /from START /to END
```

Example: `event project meeting /from Monday 2pm /to 4pm`

### List all tasks

Displays every saved task and its task number.

```text
list
```

The task type is shown as `[T]` (todo), `[D]` (deadline), or `[E]` (event).
The status is `[ ]` when incomplete and `[X]` when completed.

### Find tasks

Displays tasks whose descriptions contain the given keyword. The search is
case-insensitive, so `find book` also matches `Book club`.

```text
find KEYWORD
```

Example: `find book`

Search results are numbered from `1` for display. To `mark`, `unmark`, or
`delete` a result, first run `list` and use its task number.
When nothing matches, BigBrother shows the matching-tasks heading with no
tasks underneath it.

### Mark a task as completed

Use the task number shown by `list`.

```text
mark TASK_NUMBER
```

Example: `mark 2`

### Mark a task as incomplete

Changes a completed task back to incomplete.

```text
unmark TASK_NUMBER
```

Example: `unmark 2`

### Delete a task

Permanently removes the selected task and renumbers the remaining tasks.

```text
delete TASK_NUMBER
```

Example: `delete 3`

### Exit BigBrother

```text
bye
```

All successful additions, status changes, and deletions are saved
automatically before the application exits.

## Command summary

| Purpose | Command |
| --- | --- |
| Add a todo | `todo DESCRIPTION` |
| Add a deadline | `deadline DESCRIPTION /by YYYY-MM-DD[ HHmm]` |
| Add an event | `event DESCRIPTION /from START /to END` |
| List tasks | `list` |
| Find tasks | `find KEYWORD` |
| Mark completed | `mark TASK_NUMBER` |
| Mark incomplete | `unmark TASK_NUMBER` |
| Delete a task | `delete TASK_NUMBER` |
| Exit | `bye` |

If a command is incomplete or invalid, BigBrother displays an error explaining
what needs to be corrected. Your existing tasks remain unchanged.
