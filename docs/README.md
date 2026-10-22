# YuWei User Guide

**YuWei** is a chatbot that keeps track of your tasks from the command line. Type short commands to record to-dos, deadlines and events, tick them off as you finish them, and find them again later. Your tasks are saved automatically, so they are still there the next time you start YuWei.

```
    ____________________________________________________________
     Hello! I'm YuWei
     What can I do for you?
    ____________________________________________________________
```

- [Quick start](#quick-start)
- [Features](#features)
  - [Adding a to-do: `todo`](#adding-a-to-do-todo)
  - [Adding a deadline: `deadline`](#adding-a-deadline-deadline)
  - [Adding an event: `event`](#adding-an-event-event)
  - [Listing all tasks: `list`](#listing-all-tasks-list)
  - [Finding tasks: `find`](#finding-tasks-find)
  - [Marking a task as done: `mark`](#marking-a-task-as-done-mark)
  - [Marking a task as not done: `unmark`](#marking-a-task-as-not-done-unmark)
  - [Deleting a task: `delete`](#deleting-a-task-delete)
  - [Exiting: `bye`](#exiting-bye)
  - [Saving your tasks](#saving-your-tasks)
- [Command summary](#command-summary)

## Quick start

1. Make sure you have **Java 25** installed. You can check by running `java -version` in a terminal.
2. Download the latest `yuwei.jar` from the [releases page](https://github.com/Lim-Yu-Wei/ip/releases).
3. Put `yuwei.jar` in the folder where you want your tasks to be saved.
4. Open a terminal in that folder and run:
   ```
   java -jar yuwei.jar
   ```
5. Type a command and press Enter. Try `todo read book`, then `list`.

## Features

> **How to read the command formats below**
>
> - Words in `UPPER_CASE` are what you fill in. In `todo DESCRIPTION`, `DESCRIPTION` can be `read book`, giving `todo read book`.
> - `INDEX` is the task's number as shown by `list`, starting from 1.
> - Command words are case-sensitive: type `list`, not `List`.

### Adding a to-do: `todo`

Adds a task that has no date attached.

Format: `todo DESCRIPTION`

Example: `todo read book`

```
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
```

### Adding a deadline: `deadline`

Adds a task that must be done by a certain date.

Format: `deadline DESCRIPTION /by DATE`

- `DATE` must be written as `yyyy-mm-dd`, e.g. `2026-10-15` for 15 October 2026. YuWei shows it back to you as `Oct 15 2026`.

Example: `deadline return book /by 2026-10-15`

```
     Got it. I've added this task:
       [D][ ] return book (by: Oct 15 2026)
     Now you have 2 tasks in the list.
```

### Adding an event: `event`

Adds a task that takes place over a period of time.

Format: `event DESCRIPTION /from START /to END`

- `START` and `END` can be any text, e.g. `Mon 2pm` or `2026-10-15 1400`. They are shown exactly as you typed them.

Example: `event project meeting /from Mon 2pm /to 4pm`

```
     Got it. I've added this task:
       [E][ ] project meeting (from: Mon 2pm to: 4pm)
     Now you have 3 tasks in the list.
```

### Listing all tasks: `list`

Shows all your tasks, numbered.

Format: `list`

```
     Here are the tasks in your list:
     1. [T][ ] read book
     2. [D][ ] return book (by: Oct 15 2026)
     3. [E][ ] project meeting (from: Mon 2pm to: 4pm)
```

Each task shows its type and whether it is done:

| Symbol | Meaning |
|---|---|
| `[T]` | To-do |
| `[D]` | Deadline |
| `[E]` | Event |
| `[X]` | Done |
| `[ ]` | Not done yet |

### Finding tasks: `find`

Shows the tasks whose description contains a keyword.

Format: `find KEYWORD`

- The search ignores upper/lower case, so `find BOOK` also finds `read book`.
- Only descriptions are searched, not dates.
- The results are numbered 1, 2, 3... in the order found. To `mark` or `delete` one of them, use its number from `list`.

Example: `find book`

```
     Here are the matching tasks in your list:
     1. [T][ ] read book
     2. [D][ ] return book (by: Oct 15 2026)
```

### Marking a task as done: `mark`

Format: `mark INDEX`

Example: `mark 1`

```
     Nice! I've marked this task as done:
       [T][X] read book
```

### Marking a task as not done: `unmark`

Format: `unmark INDEX`

Example: `unmark 1`

```
     OK, I've marked this task as not done yet:
       [T][ ] read book
```

### Deleting a task: `delete`

Removes a task from your list permanently.

Format: `delete INDEX`

Example: `delete 3`

```
     Noted. I've removed this task:
       [E][ ] project meeting (from: Mon 2pm to: 4pm)
     Now you have 2 tasks in the list.
```

### Exiting: `bye`

Closes YuWei.

Format: `bye`

```
     Bye. Hope to see you again soon!
```

### Saving your tasks

There is no need to save manually: YuWei saves your tasks after every command. They are stored in `data/yuwei.txt`, inside the folder you started YuWei from.

> **Caution:** If you edit `data/yuwei.txt` by hand, keep each line in the same format. YuWei skips any line it cannot read and tells you which one when it starts. That line will then be removed the next time your tasks are saved.

## Command summary

| Action | Format | Example |
|---|---|---|
| Add to-do | `todo DESCRIPTION` | `todo read book` |
| Add deadline | `deadline DESCRIPTION /by yyyy-mm-dd` | `deadline return book /by 2026-10-15` |
| Add event | `event DESCRIPTION /from START /to END` | `event project meeting /from Mon 2pm /to 4pm` |
| List | `list` | `list` |
| Find | `find KEYWORD` | `find book` |
| Mark as done | `mark INDEX` | `mark 1` |
| Mark as not done | `unmark INDEX` | `unmark 1` |
| Delete | `delete INDEX` | `delete 3` |
| Exit | `bye` | `bye` |
