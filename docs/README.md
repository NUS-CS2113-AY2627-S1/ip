# Ernest User Guide

Ernest is a friendly command-line chatbot that keeps track of your to-dos,
deadlines, and events. Enter one command per line and press **Enter**. Ernest
automatically saves changes, so your tasks are restored the next time you
start it.

> [!TIP]
> Enter `help` at any time to see a quick command summary in Ernest.

## Getting started

1. Set up Java 25 and run `Ernest.main()` by following the
   [project setup instructions](../README.md#setting-up-in-intellij).
2. Type a command and press **Enter**.

## Quick reference

| What you want to do | Command |
| --- | --- |
| Add a to-do | `todo DESCRIPTION` |
| Add a deadline | `deadline DESCRIPTION /by DATE [TIME]` |
| Add an event | `event DESCRIPTION /from DATE [TIME] /to DATE [TIME]` |
| Show all tasks | `list` |
| Find tasks | `find KEYWORD` |
| Mark a task as done | `mark NUMBER` |
| Mark a task as not done | `unmark NUMBER` |
| Delete a task | `delete NUMBER` |
| Delete all tasks | `clear` |
| Show command help | `help` |
| Exit Ernest | `bye` |

`DESCRIPTION` is your task text. `NUMBER` is the number shown by `list`.
Dates use `yyyy-MM-dd`, and an optional time may use `HHmm` or `HH:mm` in
24-hour time. For example, `2026-10-09 1800` and `2026-10-09 18:00` are both
valid.

## Adding tasks

### To-dos

Use a to-do for a task without a date:

```text
todo Read the course notes
```

### Deadlines

Use `/by` to give a task a due date and, optionally, a time:

```text
deadline Submit the report /by 2026-10-09 18:00
```

### Events

Use `/from` and `/to` to record the start and end of an event:

```text
event Project meeting /from 2026-10-10 1400 /to 2026-10-10 15:30
```

Ernest can store up to 100 tasks. In the list, `[T]`, `[D]`, and `[E]` mean
to-do, deadline, and event. `[ ]` means not done, while `[X]` means done.

## Viewing and finding tasks

Enter `list` to see every task and its current number:

```text
list
```

Enter `find` followed by any part of a task description to show matching
tasks. The search is not case-sensitive:

```text
find report
```

## Updating tasks

Use the number displayed by `list` to update a task:

```text
mark 2
unmark 2
delete 2
```

Deleting a task renumbers the tasks after it, so run `list` again before using
another task number. To remove every task, enter:

```text
clear
```

> [!CAUTION]
> `clear` removes the entire task list immediately.

## Leaving Ernest

Enter `bye` to end your session:

```text
bye
```

Your latest task changes are saved in `data/ernest.txt` whenever you add,
mark, unmark, delete, or clear tasks. If Ernest warns that changes could not
be saved, those changes may not be available in your next session.
