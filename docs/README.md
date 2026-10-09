# Nimbus User Guide

**Sunglasses on. Briefcase loaded. Let's do business.**

Nimbus is a command-line task manager with main character energy.
Add todos, deadlines, and events; find tasks; and track what you
have completed. Your tasks are saved between sessions.

## Quick start

1. Install **Java 25**.
2. Download `ip.jar` from the
   [latest release](https://github.com/Rohita-29/ip/releases/latest).
3. Place the JAR in a folder where you have permission to create files.
4. Open a terminal in that folder and run:

   ```bash
   java -jar ip.jar
   ```

5. Enter one command at a time.
6. Type `bye` to exit.

## Command summary

| Command | Format |
|---------|--------|
| Add a todo | `todo <description>` |
| Add a deadline | `deadline <description> /by <date/time>` |
| Add an event | `event <description> /from <date/time> /to <date/time>` |
| List tasks | `list` |
| Find tasks | `find <keyword>` |
| Mark completed | `mark <task number>` |
| Mark incomplete | `unmark <task number>` |
| Delete a task | `delete <task number>` |
| Exit | `bye` |

Replace values in angle brackets with your own information.
Do not type the angle brackets. Command names are lowercase.

## Date and time formats

New deadlines and events accept these formats:

| Format | Example | Meaning |
|--------|---------|---------|
| `yyyy-MM-dd` | `2026-10-03` | 3 October 2026 at midnight |
| `yyyy-MM-dd HHmm` | `2026-10-03 1800` | 3 October 2026 at 6:00 PM |

Use four digits for the year, two for the month and day, and four
for the time. `HHmm` uses the 24-hour clock.

Date-only input means **midnight at the start of that date**.
For an end-of-day deadline, enter a time explicitly, such as
`2026-10-03 2359`.

Nimbus displays dates in a readable format:

```text
Oct 03 2026, 6:00 PM
```

Invalid dates and times, such as `2026-02-30` or `2500`, are rejected.

Older saved tasks with text-based dates remain readable. New
commands require the formats above. Nimbus does not send reminders
or perform timezone conversion.

## Adding a todo

A todo is a task without an attached date or time.

**Format:**

```text
todo <description>
```

**Example:**

```text
todo read book
```

Nimbus adds the task and displays a confirmation:

```text
Booked. Briefcase business:
  [T][ ] read book
Tasks under management: 1
```

The description cannot be empty.

## Adding a deadline

A deadline is a task with a due date and time.

**Format:**

```text
deadline <description> /by <date/time>
```

**Example:**

```text
deadline submit assignment /by 2026-10-03 1800
```

The added task appears as:

```text
[D][ ] submit assignment (by: Oct 03 2026, 6:00 PM)
```

You can also enter a date without a time:

```text
deadline pay fees /by 2026-10-04
```

This means midnight at the start of 4 October 2026.

The description and deadline must not be empty. Keep spaces
around `/by`.

## Adding an event

An event has a start and an end.

**Format:**

```text
event <description> /from <date/time> /to <date/time>
```

**Example:**

```text
event project meeting /from 2026-10-03 1400 /to 2026-10-03 1600
```

The added task appears as:

```text
[E][ ] project meeting (from: Oct 03 2026, 2:00 PM to: Oct 03 2026, 4:00 PM)
```

The description, start, and end must all be provided. Keep spaces
around `/from` and `/to`.

Both date/time values are validated. An event cannot end before
it starts. Equal start and end times are allowed.

## Listing tasks

**Command:**

```text
list
```

Nimbus displays your tasks, numbered from 1:

```text
Here's the agenda:
1. [T][ ] read book
2. [D][X] submit assignment (by: Oct 03 2026, 6:00 PM)
3. [E][ ] project meeting (from: Oct 03 2026, 2:00 PM to: Oct 03 2026, 4:00 PM)
```

The symbols mean:

| Symbol | Meaning |
|--------|---------|
| `[T]` | Todo |
| `[D]` | Deadline |
| `[E]` | Event |
| `[ ]` | Incomplete |
| `[X]` | Completed |

If there are no tasks, Nimbus displays an empty-list message.

## Finding tasks

**Format:**

```text
find <keyword>
```

**Example:**

```text
find book
```

Nimbus searches task descriptions for the supplied text.

Matching ignores case, so `find BOOK` also matches `read book`.
A search can contain several words, which are matched as one
continuous phrase. Dates and times are not searched.

Results retain their **original task numbers** from `list`.
Use those numbers when marking or deleting a matching task.

An empty keyword produces an error. If nothing matches, Nimbus
displays a no-matches message.

## Marking tasks as completed

**Format:**

```text
mark <task number>
```

**Example:**

```text
mark 1
```

Nimbus marks task 1 as completed:

```text
Another one handled. Naturally:
  [T][X] read book
```

The task remains in the list.

## Marking tasks as incomplete

**Format:**

```text
unmark <task number>
```

**Example:**

```text
unmark 1
```

Nimbus marks task 1 as incomplete:

```text
We're reopening the case:
  [T][ ] read book
```

Use a valid task number shown by `list` or `find`.

## Deleting tasks

**Format:**

```text
delete <task number>
```

**Example:**

```text
delete 1
```

Nimbus permanently removes task 1 and displays the number of
remaining tasks.

**There is no undo command.** Later tasks are renumbered after
deletion. Run `list` again before choosing another task number.

## Saving and loading

Nimbus automatically saves after adding, marking, unmarking,
or deleting a task.

Tasks are stored in:

```text
data/nimbus.txt
```

This path is relative to the folder from which you run the app.
Nimbus creates the data folder when needed and loads saved tasks
when it starts.

New dates and times are saved in a consistent format and restored
as date/time objects when the app loads them.

Running the app from another folder uses a different data file.
Keep the same working folder to continue using your existing list.

If saving fails, Nimbus reports that your latest change exists only
in memory. Check that the data file and folder are writable.
A later successful task change saves the current list.
Exiting before a successful save loses unsaved changes.

## Errors and input restrictions

Nimbus displays an error message when a command is invalid.

For example:

```text
delete 999
```

If task 999 does not exist:

```text
Wait a second...
That task doesn't exist. Even I can't manage imaginary business.
```

Remember:

- Command names are lowercase.
- Task numbers must be integers corresponding to existing tasks.
- Descriptions and required date/time fields cannot be empty.
- New dates must use `yyyy-MM-dd` or `yyyy-MM-dd HHmm`.
- Events cannot end before they start.
- Task fields cannot contain `|` because it is reserved for storage.
- Keep spaces around `/by`, `/from`, and `/to`.
- If a saved record is malformed, Nimbus reports its line number and
  stops without changing the data file. Back up `data/nimbus.txt`,
  then repair the reported line or restore a working backup.

If a command fails validation, correct it and enter it again.

## Exiting

**Command:**

```text
bye
```

Nimbus displays its exit message and closes:

```text
[mic drop]

Instructions handled. Exit executed. Nimbus rocked!
Too much work done today. Need my beauty sleep. BYEEEEEE!!!
```

Unfinished tasks remain available when you next run Nimbus from
the same folder.