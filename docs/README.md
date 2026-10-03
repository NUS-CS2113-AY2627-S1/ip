# Clammy User Guide

Clammy is a task manager you use by typing commands in a terminal. Keep track of todos,
deadlines, and events, find tasks, and mark them done. Your changes are saved automatically.

## Quick start

1. Install **Java 25**. Run `java -version` in a terminal to check your version.
2. Download `clammy-all.jar` from the [releases page](https://github.com/J-unn-y/ip/releases).
3. Put the JAR in a folder of your choice and open a terminal in that folder.
4. Run `java -jar "clammy-all.jar"`.
5. Type `todo borrow book` and press **Enter**, then try `list` to see your task.

Always start Clammy from the same folder so it can find your saved tasks.
To build or run from source, see the [project README](../README.md).

## Features

Replace words such as `DESCRIPTION` and `NUMBER` with your own values; do not type the
placeholder names. Enter one command per line. Command names are case-insensitive.
Descriptions and other required values must not be blank.

### Add a todo: `todo`

Adds a task without a date or time.

**Format:** `todo DESCRIPTION`

**Example:** `todo borrow book`

When this is your first task, Clammy replies as follows (separator lines omitted):

```text
Got it. I've added this task:
[T][ ] borrow book
Now you have 1 task in the list.
```

### Add a deadline: `deadline`

Adds a task with a due date and an optional time.

**Format:** `deadline DESCRIPTION /by DATE_OR_TIME`

**Examples:**

- `deadline return book /by 2019-12-02`
- `deadline return book /by 2/12/2019 1800`

Use `yyyy-MM-dd` (year-month-day) or `d/M/yyyy` (day/month/year). You can add a
24-hour time in `HHmm` format: `1800` means 6:00 PM. The second example displays as:

```text
[D][ ] return book (by: Dec 02 2019, 6:00 PM)
```

Dates must be valid. Text such as `Sunday`, times such as `18:00`, and impossible dates
such as `2019-02-29` are rejected. Omit the time if you only need a date.

### Add an event: `event`

Adds a task with a start and end. Unlike deadlines, event times accept free-form text.
Include both `/from` and `/to`, in that order.

**Format:** `event DESCRIPTION /from START /to END`

**Example:** `event project meeting /from Mon 2pm /to 4pm`

The added task displays as:

```text
[E][ ] project meeting (from: Mon 2pm to: 4pm)
```

### List all tasks: `list`

Enter `list` to see your tasks in the order you added them. For example:

```text
Here are the tasks in your list:
1.[T][ ] borrow book
2.[D][ ] return book (by: Dec 02 2019)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
```

`[T]`, `[D]`, and `[E]` mean todo, deadline, and event. `[ ]` means incomplete;
`[X]` means done. An empty list shows only the heading.

### Find tasks: `find`

Searches task descriptions for text, ignoring case. Partial words match too:
`find book` matches both `Book` and `notebook`. Dates and event times are not searched.

**Format:** `find KEYWORD`

**Examples:** `find book`, `find return book`

A phrase such as `return book` must appear together, with the same spaces. Matches are
shown under `Here are the matching tasks in your list:`. If nothing matches, only that
heading appears.

> **Task numbers:** Search results are numbered separately. Always use `list` to get
> the full-list number before marking, unmarking, or deleting a task.

### Mark or unmark a task: `mark`, `unmark`

Changes the completion status of a task. Use a positive task number from `list`.

| Action | Format | Example | Result |
| --- | --- | --- | --- |
| Mark done | `mark NUMBER` | `mark 1` | Task 1 shows `[X]`. |
| Mark incomplete | `unmark NUMBER` | `unmark 1` | Task 1 shows `[ ]`. |

### Delete a task: `delete`

Removes a task and shows the remaining task count.

**Format:** `delete NUMBER`

**Example:** `delete 2` removes the second task in the full list.

Deletion takes effect immediately and has no undo command. Task numbers shift after
deletion, so use `list` again before changing another task.

### Exit: `bye`

Enter `bye` to close Clammy. It replies `Bye. Hope to see you again soon!`.
The `list` and `bye` commands do not accept extra arguments.

### Save and restore tasks

Clammy saves to `data/clammy.txt` in the terminal's working folder after each successful
addition, status change, or deletion. It creates the folder and file on the first save
and loads your tasks when you next start it. No save command is needed.

If Clammy cannot load your data, it leaves the file unchanged and asks you to fix the
problem before restarting. If saving fails, your changes remain in memory only: fix the
reported problem, then change a task again to retry saving before you exit.
See [saved tasks](storage.md) for recovery details.

## Command summary

| Action | Command |
| --- | --- |
| Add a todo | `todo DESCRIPTION` |
| Add a deadline | `deadline DESCRIPTION /by DATE_OR_TIME` |
| Add an event | `event DESCRIPTION /from START /to END` |
| List all tasks | `list` |
| Find tasks | `find KEYWORD` |
| Mark done | `mark NUMBER` |
| Mark incomplete | `unmark NUMBER` |
| Delete a task | `delete NUMBER` |
| Exit | `bye` |

Mistyped a command? Clammy shows an error message so you can correct it and try again.
