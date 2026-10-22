# Wenwen User Guide

Wenwen is a friendly command-line task manager. It keeps todos, deadlines, and
events in one list, remembers them between sessions, and helps you find work by
keyword or date.

## Quick start

1. Install Java 25 or later.
2. Download `Wenwen.jar` and place it in a folder where Wenwen may create its
   data file.
3. Open a terminal in that folder.
4. Run `java -jar Wenwen.jar`.
5. Type a command and press <kbd>Enter</kbd>.

Wenwen saves changes automatically in `data/wenwen.txt`, relative to the folder
from which it is run. There is no need to save manually.

> **Date formats:** use `yyyy-MM-dd` for a date, such as `2026-10-02`. Use
> `yyyy-MM-dd HHmm` for an event date and 24-hour time, such as
> `2026-10-02 1830`. Wenwen displays these values in a friendlier format.

## Features

### Add a todo: `todo`

Adds a task without a date.

```text
todo read book
```

```text
Got it. I've added this task:
  [T][ ] read book
Now you have 1 task in the list.
```

### Add a deadline: `deadline`

Adds a task that must be completed by a date.

```text
deadline submit report /by 2026-10-02
```

```text
Got it. I've added this task:
  [D][ ] submit report (by: Oct 02 2026)
```

### Add an event: `event`

Adds an activity with a start and end date and time. The end must not be before
the start.

```text
event project demo /from 2026-10-02 1400 /to 2026-10-02 1530
```

```text
Got it. I've added this task:
  [E][ ] project demo (from: Oct 02 2026, 2:00PM to: Oct 02 2026, 3:30PM)
```

### View all tasks: `list`

Shows every task and its current number. Use that number with `mark`, `unmark`,
or `delete`.

```text
list
```

```text
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] submit report (by: Oct 02 2026)
```

`[T]`, `[D]`, and `[E]` mean todo, deadline, and event. `[X]` means done;
`[ ]` means not done.

### Mark or unmark a task: `mark`, `unmark`

Mark task 2 as done:

```text
mark 2
```

Change it back to not done:

```text
unmark 2
```

### Delete a task: `delete`

Deletes the task with the given number. Check `list` first if you are unsure of
the current number.

```text
delete 2
```

### Find tasks by keyword: `find`

Searches task descriptions for a word or phrase. Matching is not case-sensitive.

```text
find book
```

```text
Here are the matching tasks in your list:
1.[T][X] read book
2.[D][ ] return BOOK (by: Oct 02 2026)
```

### View tasks on a date: `on`

Shows deadlines due on the date and events that overlap the date. Todos are not
included because they have no date.

```text
on 2026-10-02
```

```text
Here are the tasks on Oct 02 2026:
1.[D][ ] submit report (by: Oct 02 2026)
2.[E][ ] project demo (from: Oct 02 2026, 2:00PM to: Oct 02 2026, 3:30PM)
```

### Exit Wenwen: `bye`

```text
bye
```

Wenwen saves each change as it happens, so it is safe to leave after the
farewell message.

## Command summary

| Action | Format |
| --- | --- |
| Add a todo | `todo DESCRIPTION` |
| Add a deadline | `deadline DESCRIPTION /by yyyy-MM-dd` |
| Add an event | `event DESCRIPTION /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm` |
| List all tasks | `list` |
| Mark a task done | `mark NUMBER` |
| Mark a task not done | `unmark NUMBER` |
| Delete a task | `delete NUMBER` |
| Find by description | `find KEYWORD` |
| View tasks on a date | `on yyyy-MM-dd` |
| Exit | `bye` |

## If something goes wrong

Wenwen explains invalid commands instead of stopping. Check the command spelling,
required separators such as `/by`, and date format, then try again. If the saved
data cannot be read, Wenwen reports the problem and starts with an empty list;
keep a copy of the existing `data/wenwen.txt` before replacing it.
