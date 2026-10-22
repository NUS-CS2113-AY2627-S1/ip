# GPT User Guide

GPT is a command-line task manager that keeps track of todos, deadlines, and
events. It saves your tasks automatically so that they are available the next
time you start the application.

## Quick Start

1. Install Java 25 or later.
2. Put `Gpt.jar` in a folder where GPT can create its `data` folder.
3. Open a terminal in that folder.
4. Run:

   ```text
   java -jar Gpt.jar
   ```

5. Enter commands into the terminal and press Enter after each command.

Words written in uppercase below, such as `DESCRIPTION`, are values that you
should replace. Do not type the uppercase placeholders.

## Command Summary

| Action | Command |
| --- | --- |
| Add a todo | `todo DESCRIPTION` |
| Add a deadline | `deadline DESCRIPTION /by yyyy-MM-dd` |
| Add an event | `event DESCRIPTION /from START /to END` |
| List tasks | `list` |
| Find tasks | `find KEYWORD` |
| Mark a task | `mark NUMBER` |
| Unmark a task | `unmark NUMBER` |
| Delete a task | `delete NUMBER` |
| Exit GPT | `bye` |

## Adding a Todo

Use `todo` for a task without a date or time.

```text
todo borrow book
```

GPT adds the task as an incomplete todo:

```text
[T][ ] borrow book
```

## Adding a Deadline

Use `deadline` for a task that must be completed by a particular date. Enter
the date in `yyyy-MM-dd` format.

```text
deadline submit report /by 2026-10-15
```

GPT displays the date in a more readable format:

```text
[D][ ] submit report (by: Oct 15 2026)
```

## Adding an Event

Use `event` for a task with a start and an end. The start and end values may be
written as descriptive text.

```text
event project meeting /from Monday 2pm /to Monday 4pm
```

```text
[E][ ] project meeting (from: Monday 2pm to: Monday 4pm)
```

## Listing Tasks

Use `list` to display all tasks and their numbers.

```text
list
```

```text
Here are the tasks in your list:
1.[T][ ] borrow book
2.[D][ ] submit report (by: Oct 15 2026)
```

The task number is used by the `mark`, `unmark`, and `delete` commands.

## Finding Tasks

Use `find` to display tasks whose descriptions contain a keyword. The search
is not case-sensitive.

```text
find book
```

```text
Here are the matching tasks in your list:
1.[T][ ] borrow book
```

## Marking a Task

Use `mark` followed by the task number shown by `list`.

```text
mark 1
```

The status changes from `[ ]` to `[X]`.

## Unmarking a Task

Use `unmark` to mark a completed task as incomplete again.

```text
unmark 1
```

The status changes from `[X]` to `[ ]`.

## Deleting a Task

Use `delete` followed by the task number shown by `list`.

```text
delete 2
```

GPT removes that task and renumbers the remaining tasks.

## Exiting GPT

Use `bye` to close the application.

```text
bye
```

## Saving Tasks

GPT saves changes automatically in `data/gpt.txt`, relative to the folder from
which the application is run. You do not need to save manually.

If a command is incomplete or invalid, GPT displays an explanation and waits
for another command instead of closing.
