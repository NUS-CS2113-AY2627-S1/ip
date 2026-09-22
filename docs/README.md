# Jody User Guide

Jody is a command-line chatbot that helps you manage todos, deadlines, and events. Add tasks, find what you need, and mark tasks as completed.

## Using commands

Type a command and press **Enter**.

- Replace uppercase placeholders such as `DESCRIPTION` with your own text.
- Descriptions can contain spaces. Quotation marks are not needed.
- Command words are case-insensitive, but use lowercase `/by`, `/from`, and `/to`.
- Task numbers start at **1**. Run `list` to check a task's number before marking, unmarking, or deleting it.

## Adding todos

Adds a task without a date or time.

**Format:** `todo DESCRIPTION`

**Example:**

```text
todo read a book
```

The new task appears as:

```text
[T][ ] read a book
```

A description is required.

## Adding deadlines

Adds a task with a due date and an optional time.

**Format:** `deadline DESCRIPTION /by DATE_TIME`

**Example:**

```text
deadline return book /by 2/12/2026 1800
```

The new task appears as:

```text
[D][ ] return book (by: Dec 02 2026, 6:00 PM)
```

Include a description and exactly one `/by` separator.

## Adding events

Adds a task with a start and end date/time.

**Format:** `event DESCRIPTION /from START /to END`

**Example:**

```text
event project meeting /from 2/12/2026 1400 /to 2/12/2026 1600
```

The new task appears as:

```text
[E][ ] project meeting (from: Dec 02 2026, 2:00 PM to: Dec 02 2026, 4:00 PM)
```

Both dates are required. The end cannot be earlier than the start. Events can span multiple days.

## Supported dates and times

The following date formats are accepted:

| Format | Example |
|---|---|
| Year-month-day | `2026-12-02` |
| Day/month/year | `2/12/2026` |
| Full English month | `2 December 2026` |
| Abbreviated English month | `2 Dec 2026` |
| Day with an ordinal suffix | `2nd December 2026` |

Slash dates use **day/month/year**, so `2/12/2026` means **2 December 2026**.

For deadlines and events, add a time after the date:

| Time format | Example |
|---|---|
| Compact 24-hour time | `1800` |
| 24-hour time with a colon | `18:00` |
| 12-hour time | `6pm` |
| 12-hour time with minutes | `6:30pm` |

For example:

```text
deadline submit report /by 2nd December 2026 6pm
```

Month names and AM/PM are case-insensitive. A date without a time means **midnight at the start of that date**. Phrases such as `tomorrow` and `next Friday` are not supported.

## Listing tasks

Displays all tasks, including completed tasks.

**Command:**

```text
list
```

For a list containing a todo, a deadline, and an event, the task entries look like:

```text
1.[T][ ] read a book
2.[D][ ] return book (by: Dec 02 2026, 6:00 PM)
3.[E][ ] project meeting (from: Dec 02 2026, 2:00 PM to: Dec 02 2026, 4:00 PM)
```

- `[T]`: Todo
- `[D]`: Deadline
- `[E]`: Event
- `[ ]`: Incomplete
- `[X]`: Completed

## Marking tasks as completed

Marks a task as completed, changing its status to `[X]`.

**Format:** `mark NUMBER`

**Example:**

```text
mark 1
```

If task 1 is `read a book`, it becomes:

```text
[T][X] read a book
```

## Marking tasks as incomplete

Changes a task's status back to `[ ]`.

**Format:** `unmark NUMBER`

**Example:**

```text
unmark 1
```

Task 1 becomes incomplete again.

## Deleting tasks

Removes a task from the list.

**Format:** `delete NUMBER`

**Example:**

```text
delete 2
```

This removes task 2 from the full list.

Deletion is immediate and cannot be undone through a command. Remaining tasks are renumbered, so run `list` again before deleting another task.

## Finding tasks

Finds tasks whose descriptions contain the given text.

**Format:** `find TEXT`

**Example:**

```text
find book
```

This matches descriptions such as `read a book` and `return books`.

- Searching is **case-sensitive**: `book` does not match `Book`.
- Multiple words are matched as one continuous phrase.
- Entering `find` without search text displays all tasks.
- If nothing matches, only the matching-tasks heading is displayed.

**Important:** Search results are numbered separately from the full list. Run `list` before using `mark`, `unmark`, or `delete`, because these commands use the full list's task numbers.

## Viewing tasks on a date

Displays deadlines due on a date and events spanning that date.

**Format:** `on DATE`

**Example:**

```text
on 2/12/2026
```

- Enter a date without a time.
- Events are included on both their start and end dates.
- Completed tasks are included; todos are excluded.
- Results retain their original task numbers and are sorted by deadline time or event start time.

If no tasks match, Jody displays:

```text
No deadlines or events on this date.
```

## Saving tasks

Jody automatically saves task changes to `data/jody.txt` and loads saved tasks when it starts.

The file is stored relative to the folder Jody is launched from. Launch Jody from the same folder each time to access the same task list.

No manual save command is needed. To back up your tasks, close Jody and copy `data/jody.txt`.

If Jody reports a save error, your latest changes remain in the current session but have not been saved. Fix the folder's write permissions, then enter `find` to retry saving in this version.

## Exiting Jody

Closes the application.

**Command:**

```text
bye
```

Jody displays:

```text
Bye. Hope to see you again soon!
```

## Handling errors

If a command is invalid, Jody displays an explanation. Correct the input and try again.

Check that:

- New tasks have a description.
- Deadlines include `/by`.
- Events include `/from` and `/to` in that order.
- Dates and times use a supported format.
- Task numbers exist in the full list.
- `list` and `bye` are entered without extra arguments.

If Jody reports an invalid saved task on startup, back up `data/jody.txt` before correcting the reported line or restoring a known-good copy.

## Command summary

| Action | Command |
|---|---|
| Add a todo | `todo DESCRIPTION` |
| Add a deadline | `deadline DESCRIPTION /by DATE_TIME` |
| Add an event | `event DESCRIPTION /from START /to END` |
| List all tasks | `list` |
| Complete a task | `mark NUMBER` |
| Mark a task as incomplete | `unmark NUMBER` |
| Delete a task | `delete NUMBER` |
| Search descriptions | `find TEXT` |
| View tasks on a date | `on DATE` |
| Exit | `bye` |
