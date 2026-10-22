# Extant User Guide

Extant is a command-line task manager. It supports todo, deadline, and event tasks, and saves changes in `data/Extant.txt`.

## Starting Extant

Starting from release version 0.2, precompiled releases can be downloaded from the release page [https://github.com/NUS-Nbnq/ip/releases/tag/Release]

If compiling from scratch, cd into the root folder and run:

```bash
gradle shadowjar
```

## Commands

### Add tasks

```text
todo <description>
deadline <description> /by <date>
event <description> /from <start> /to <end>
```

Examples:

```text
todo read book
deadline return book /by June 6th
event project meeting /from Monday 10am /to Monday 11am
```

Example output:

```text
added: read book
added: return book (by: June 6th)
added: project meeting (from: Monday 10am to Monday 11am)
```

### List tasks

```text
list
```

Example output:

```text
1. [T][ ] read book
2. [D][X] return book (by: June 6th)
You have 2 tasks in total.
```

`T`, `D`, and `E` identify todo, deadline, and event tasks. `[X]` means
complete and `[ ]` means incomplete.

### Find tasks

```text
find <search text>
```

Search is case-insensitive, checks task bodies, and preserves task order.

```text
find book
```

Output:

```text
Here are the matching tasks in your list:
1. [T][X] read book
2. [D][X] return book (by: June 6th)
```

If there are no matches:

```text
No matching tasks found.
```

### Change task status

```text
mark <task number>
unmark <task number>
```

Task numbers are one-based, as shown by `list`.

Example:

```text
mark 1
```

Output:

```text
Nice! I've marked this task as done:
  read book
```

### Delete a task

```text
delete <task number>
```

Example:

```text
delete 1
```

Output:

```text
Deleting 1:
  read book
```

### Exit

```text
bye
```

Output:

```text
Farewell. Until our paths cross again.
```

## Errors and exceptions

Extant catches these exceptions while processing commands:

| Exception | Meaning | Response |
| --- | --- | --- |
| `IllegalEventException` | A task or search command has invalid or missing arguments. | Displays a usage or format message. |
| `IllegalKeywordException` | The command keyword is not recognized. | Displays a command-unrecognized message. |
| `NumberFormatException` | A task number is missing or is not positive. | `Invalid index format. Please provide a valid number.` |
| `IndexOutOfBoundsException` | The task number does not exist. | `Invalid index. Please provide a valid task number.` |
| `Exception` | An unexpected error occurred. | Displays the exception message. |

Examples:

```text
todo
Usage: todo <description>
```

```text
deadline return book
Format invalid, do <body> /by <dateEnd>
```

```text
mark abc
Invalid index format. Please provide a valid number.
```

## Persistence

Tasks are loaded from `data/Extant.txt` at startup. Adding, deleting, marking,
and unmarking tasks saves the updated list automatically.
