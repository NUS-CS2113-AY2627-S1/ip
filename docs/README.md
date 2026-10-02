# Nico User Guide

Nico is a command-line chatbot that keeps track of your todos, deadlines and events. Type short commands, and Nico saves your list automatically so it's still there next time you open it.

## Quick start

1. Make sure you have Java 25 installed.
2. Download the latest `nico.jar` from the [Releases page](https://github.com/Nickyi29/ip/releases).
3. Copy it into an empty folder, open a terminal in that folder, and run:
```
   java -jar nico.jar
```
4. Type a command and press Enter. Type `bye` to exit.

## Features

> **Notes about the command format**
> - Words in `UPPER_CASE` are details you supply, e.g. in `todo DESCRIPTION`, `DESCRIPTION` could be `read book`.
> - `TASK_NUMBER` is the number shown next to the task in `list`.
> - Command words are not case-sensitive (`LIST` works the same as `list`).

### Adding a todo: `todo`

Adds a task with no date attached.

Format: `todo DESCRIPTION`

Example: `todo read book`

```
Got it. I've added this task:
  [T][ ] read book
Now you have 1 task(s) in the list.
```

### Adding a deadline: `deadline`

Adds a task that must be done by a certain time.

Format: `deadline DESCRIPTION /by DATE`

- Write `DATE` as `yyyy-mm-dd` (e.g. `2026-10-15`), optionally followed by a 24-hour time (e.g. `2026-10-15 1800`). Nico then shows it as `Oct 15 2026` or `Oct 15 2026, 6:00PM`.
- Any other text (e.g. `Sunday`) is also accepted, and shown exactly as typed.

Examples:
- `deadline return book /by 2026-10-15`
- `deadline submit report /by 2026-10-15 1800`

```
Got it. I've added this task:
  [D][ ] return book (by: Oct 15 2026)
Now you have 2 task(s) in the list.
```

### Adding an event: `event`

Adds a task with a start and end time.

Format: `event DESCRIPTION /from START /to END`

`START` and `END` follow the same rules as a deadline's `DATE`. If both are dates, the end cannot be before the start.

Example: `event hackathon /from 2026-10-14 /to 2026-10-16`

```
Got it. I've added this task:
  [E][ ] hackathon (from: Oct 14 2026 to: Oct 16 2026)
Now you have 3 task(s) in the list.
```

### Listing all tasks: `list`

Shows every task. `[X]` means the task is done.

Format: `list`

```
Here are the tasks in your list:
1.[T][X] read book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
```

### Marking a task as done: `mark`

Format: `mark TASK_NUMBER`

Example: `mark 1`

```
Nice! I've marked this task as done:
  [T][X] read book
```

### Marking a task as not done: `unmark`

Format: `unmark TASK_NUMBER`

Example: `unmark 1`

```
OK, I've marked this task as not done yet:
  [T][ ] read book
```

### Deleting a task: `delete`

Format: `delete TASK_NUMBER`

Example: `delete 3`

```
Noted. I've removed this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 2 task(s) in the list.
```

### Finding tasks: `find`

Shows tasks whose description contains the keyword. The search ignores upper and lower case.

Format: `find KEYWORD`

Example: `find book`

```
Here are the matching tasks in your list:
1.[T][X] read book
2.[D][ ] return book (by: Sunday)
```

### Showing tasks on a date: `on`

Shows every deadline due, and every event happening, on a given date. An event spanning several days appears on each of those days.

Format: `on DATE`, where `DATE` is `yyyy-mm-dd`

Example: `on 2026-10-15`

```
Here are your deadlines and events on Oct 15 2026:
1.[D][ ] return book (by: Oct 15 2026)
2.[E][ ] hackathon (from: Oct 14 2026 to: Oct 16 2026)
```

### Exiting: `bye`

Format: `bye`

### Saving your data

Your tasks are saved automatically after every change to `data/nico.txt`, in the folder you ran Nico from. There is no need to save manually.

## Command summary

| Command | Format | Example |
|---|---|---|
| Todo | `todo DESCRIPTION` | `todo read book` |
| Deadline | `deadline DESCRIPTION /by DATE` | `deadline return book /by 2026-10-15` |
| Event | `event DESCRIPTION /from START /to END` | `event hackathon /from 2026-10-14 /to 2026-10-16` |
| List | `list` | `list` |
| Mark | `mark TASK_NUMBER` | `mark 1` |
| Unmark | `unmark TASK_NUMBER` | `unmark 1` |
| Delete | `delete TASK_NUMBER` | `delete 3` |
| Find | `find KEYWORD` | `find book` |
| On | `on DATE` | `on 2026-10-15` |
| Exit | `bye` | `bye` |