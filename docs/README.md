# NGA User Guide

NGA (**No Goobers Allowed**) is a friendly command-line task manager for
todos, deadlines, and events.

## Getting started

NGA requires Java 25. From the project root, build and start the executable JAR:

```bash
gradle clean shadowJar
java -jar build/libs/nga-all.jar
```

You can also run `nga.Nga` directly from IntelliJ. Enter one command per line.
Type `bye` when you are finished.

## Commands

| Command | What it does |
| --- | --- |
| `todo <description>` | Adds a task without a date or time. |
| `deadline <description> /by <date or time>` | Adds a task with a deadline. |
| `event <description> /from <start> /to <end>` | Adds an event with a time range. |
| `list` | Displays all tasks in their current order. |
| `find <keyword>` | Finds tasks whose descriptions contain the keyword. |
| `mark <task number>` | Marks a task as done. |
| `unmark <task number>` | Marks a task as not done. |
| `delete <task number>` | Deletes one task. |
| `delete all` or `clear` | Deletes every task. |
| `help` | Shows the command summary. |
| `bye` | Exits NGA. |

Task numbers are the numbers shown by `list`, starting from 1.

## Examples

```text
todo read Java chapter
deadline submit report /by 2025-12-02 1800
event project meeting /from Monday 2pm /to Monday 3pm
list
find report
mark 1
unmark 1
delete 2
```

NGA displays tasks with a type and completion status:

```text
[T][ ] read Java chapter
[D][X] submit report (by: Dec 2 2025 6:00 PM)
[E][ ] project meeting (from: Monday 2pm to: Monday 3pm)
```

`T` means ToDo, `D` means deadline, and `E` means event. An `X` means the
task is complete.

## Deadline formats

Deadlines accept these formats:

```text
yyyy-MM-dd
d/M/yyyy HHmm
yyyy-MM-dd HHmm
yyyy-MM-dd HH:mm
```

For example:

```text
deadline return book /by 2025-12-02
deadline attend appointment /by 2/12/2025 1800
```

## Saving tasks

Tasks are saved automatically to `data/nga.txt`. Run the JAR from the project
root if you want the data file in the project folder; otherwise, `data/nga.txt`
is created relative to the folder from which you started the JAR.

Tasks are restored automatically the next time NGA starts. If a saved row is
invalid, NGA skips it, loads the other valid tasks, and displays a warning.

## Input rules

- Use spaces, not tabs, between a command and its arguments.
- Do not use the `|` character in task descriptions, deadline fields, or event
  fields.
- Empty descriptions, invalid task numbers, and unsupported deadline formats
  are rejected with a helpful message.
- Duplicate tasks are not added.
