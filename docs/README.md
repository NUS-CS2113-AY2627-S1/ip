# Dobby User Guide

Dobby is a command-line task manager for keeping track of everyday quests. Add simple ToDos, deadlines, and events; then list, search, complete, or remove them with short commands.

## Quick start

### Prerequisites

Install [JDK 25](https://www.oracle.com/java/technologies/downloads/).

### Run Dobby

From the project root, build the executable JAR:

```powershell
.\gradlew.bat shadowJar
```

Then start Dobby with:

```powershell
java -jar build\libs\dobby.jar
```

When Dobby starts, type one command per line and press `Enter`. Type `bye` when you are finished.

> [!IMPORTANT]
> Dobby keeps tasks in memory for the current run only. Starting a new run begins with an empty task list.

## Command format

The examples below use these conventions:

- Replace text in angle brackets, such as `<description>`, with your own text.
- Type command names in lowercase.
- Keep the spaces around `/by`, `/from`, and `/to` exactly as shown.
- A task number is the number shown by `list`, starting from `1`.

## Features

### Adding a ToDo: `todo`

Adds a task without a date or time.

Format: `todo <description>`

Example:

```text
todo read chapter 3
```

Dobby adds the task and displays it with a `[T]` marker:

```text
[T][ ] read chapter 3
```

The description cannot be empty.

### Adding a deadline: `deadline`

Adds a task that should be completed by a specified date or time.

Format: `deadline <description> /by <date/time>`

Examples:

```text
deadline submit report /by 2019-10-15
deadline call bank /by 2019-10-15 1730
```

Dobby displays deadlines with a `[D]` marker. Recognized dates and date-times are formatted for readability; unrecognized deadline text is kept as entered.

```text
[D][ ] submit report (by: Oct 15 2019)
```

Recommended date formats are `yyyy-MM-dd` and `d/M/yyyy`. For a date and time, use `yyyy-MM-dd HHmm` or `d/M/yyyy HHmm`.

### Adding an event: `event`

Adds a task that spans a start and end date or time.

Format: `event <description> /from <start> /to <end>`

Example:

```text
event project meeting /from 2019-10-14 /to 2019-10-16
```

Dobby displays events with an `[E]` marker:

```text
[E][ ] project meeting (from: 2019-10-14 to: 2019-10-16)
```

Both `/from` and `/to` values are required. Put a space before and after each marker.

### Listing tasks: `list`

Shows every task in the order it was added.

Format: `list`

Example:

```text
list
```

The result includes each task's number, type marker, completion status, description, and any deadline or event details. An unfinished task uses `[ ]`; a completed task uses `[X]`.

### Marking a task as done: `mark`

Marks a task as completed.

Format: `mark <number>`

Example:

```text
mark 2
```

The selected task changes from `[ ]` to `[X]`.

### Marking a task as not done: `unmark`

Returns a completed task to the unfinished state.

Format: `unmark <number>`

Example:

```text
unmark 2
```

### Deleting a task: `delete`

Removes a task permanently from the current session.

Format: `delete <number>`

Example:

```text
delete 3
```

The remaining tasks are renumbered in their original order. Use `list` before deleting if you are unsure which number to select.

### Finding tasks by keyword: `find`

Finds tasks whose descriptions contain a keyword. The search is case-insensitive and matches part of a description.

Format: `find <keyword>`

Example:

```text
find report
```

The results keep the original task numbers, so you can identify the matching task in the full list. A keyword is required.

### Finding tasks on a date: `on`

Shows deadlines due on a date and events that include a date within their start-to-end range. To avoid ambiguity, use the `yyyy-MM-dd` format.

Format: `on <date>`

Example:

```text
on 2019-10-15
```

To be found by `on`, a deadline must contain a recognized date, and both ends of an event must be recognized dates. ToDos are not included in date results.

### Exiting Dobby: `bye`

Ends the current Dobby session.

Format: `bye`

Example:

```text
bye
```

## Typical workflow

The following sequence adds three kinds of tasks, checks the list, completes one task, searches for a keyword, and exits:

```text
todo borrow book
deadline return book /by 2019-10-15
event project meeting /from 2019-10-14 /to 2019-10-16
list
mark 1
find book
bye
```

## Troubleshooting

- If Dobby reports an unknown command, check the [command summary](#command-summary) and use one of the supported commands.
- If a command needs a task number, use a number between `1` and the number of tasks currently listed.
- If a command is malformed, check that its description is present and that `/by`, `/from`, and `/to` have the required values.
- Dobby accepts up to 100 tasks in one session. Start a new session after reaching the limit.

## Command summary

| Action | Format |
| --- | --- |
| Add a ToDo | `todo <description>` |
| Add a deadline | `deadline <description> /by <date/time>` |
| Add an event | `event <description> /from <start> /to <end>` |
| List tasks | `list` |
| Mark done | `mark <number>` |
| Mark not done | `unmark <number>` |
| Delete a task | `delete <number>` |
| Find by keyword | `find <keyword>` |
| Find by date | `on <date>` |
| Exit | `bye` |
