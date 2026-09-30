# Maple — User Guide

## About Us

Maple is a **desktop app for managing tasks**, optimized for people who
prefer typing commands over clicking buttons. Your task list is saved
automatically, so it is still there the next time you start Maple.

## Quick Start

1. Ensure you have Java 25 (or later) installed.
2. Download `maple.jar` from the [latest release](https://github.com/officialasyrafrahim/ip/releases).
3. Copy the JAR file into an empty folder, open a terminal in that
   folder, and run:
   ```sh
   java -jar maple.jar
   ```
4. Type a command into the terminal and press `Enter`.

Maple remembers your tasks in `data/maple.txt` (created next to the
JAR on first exit).

## Features

### Adding a todo — `todo`

Creates a task with just a description.

Format: `todo DESCRIPTION`

Example: `todo borrow book`

Expected output:

```
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
```

### Adding a deadline — `deadline`

Creates a task that must be finished by a given date or time.

Format: `deadline DESCRIPTION /by DATE_TIME`

Example: `deadline return book /by Sunday`

Expected output:

```
 Got it. I've added this task:
   [D][ ] return book (by: Sunday)
 Now you have 1 tasks in the list.
```

### Adding an event — `event`

Creates a task that takes place between a start and an end time.

Format: `event DESCRIPTION /from START /to END`

Example: `event project meeting /from Mon 2pm /to 4pm`

Expected output:

```
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 1 tasks in the list.
```

### Listing all tasks — `list`

Shows every task with its number, type and completion status.

Format: `list`

Example: `list`

Expected output:

```
 Here are the tasks in your list:
 1.[T][ ] borrow book
```

### Marking a task as done — `mark`

Format: `mark TASK_NUMBER`

Example: `mark 1`

Expected output:

```
 Nice! I've marked this task as done:
   [T][X] borrow book
```

### Marking a task as not done — `unmark`

Format: `unmark TASK_NUMBER`

Example: `unmark 1`

Expected output:

```
 OK, I've marked this task as not done yet:
   [T][ ] borrow book
```

### Deleting a task — `delete`

Removes the task with the given number from the list.

Format: `delete TASK_NUMBER`

Example: `delete 1`

Expected output:

```
 Noted. I've removed this task:
   [T][ ] borrow book
 Now you have 0 tasks in the list.
```

### Finding tasks — `find`

Lists every task whose description contains the keyword. The search
ignores case, and a multi-word keyword matches that exact phrase.

Format: `find KEYWORD`

Example: `find book`

Expected output:

```
 Here are the matching tasks in your list:
 1.[T][ ] borrow book
```

### Exiting — `bye`

Saves your task list and quits Maple.

Format: `bye`

Expected output:

```
 Bye. Hope to see you again soon!
```

## Command Summary

| Action | Format | Example |
| --- | --- | --- |
| Add todo | `todo DESCRIPTION` | `todo borrow book` |
| Add deadline | `deadline DESCRIPTION /by DATE_TIME` | `deadline return book /by Sunday` |
| Add event | `event DESCRIPTION /from START /to END` | `event project meeting /from Mon 2pm /to 4pm` |
| List tasks | `list` | `list` |
| Mark done | `mark N` | `mark 1` |
| Mark not done | `unmark N` | `unmark 1` |
| Delete task | `delete N` | `delete 1` |
| Find tasks | `find KEYWORD` | `find book` |
| Exit | `bye` | `bye` |

`N` is the task number shown by `list`. Invalid commands are reported
with an `Oops!` message and never crash the app or lose your data.
