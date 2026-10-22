# Clanky User Guide

```
  ____   _          _      _   _   _  __ __   __
 / ___| | |        / \    | \ | | | |/ / \ \ / /
| |     | |       / _ \   |  \| | | ' /   \ V / 
| |___  | |___   / ___ \  | |\  | | . \    | |  
 \____| |_____| /_/   \_\ |_| \_| |_|\_\   |_|
```

Clanky is a command-line task manager that helps you keep track of **todos**, **deadlines**, and **events** — and it remembers your tasks between sessions automatically.

- [Quick start](#quick-start)
- [Features](#features)
    - [Listing all tasks: `list`](#listing-all-tasks-list)
    - [Adding a todo: `todo`](#adding-a-todo-todo)
    - [Adding a deadline: `deadline`](#adding-a-deadline-deadline)
    - [Adding an event: `event`](#adding-an-event-event)
    - [Marking a task as done: `mark`](#marking-a-task-as-done-mark)
    - [Unmarking a task: `unmark`](#unmarking-a-task-unmark)
    - [Deleting a task: `delete`](#deleting-a-task-delete)
    - [Finding tasks: `find`](#finding-tasks-find)
    - [Exiting the program: `bye`](#exiting-the-program-bye)
    - [Saving and loading data](#saving-and-loading-data)
- [Command summary](#command-summary)

---

## Quick start

1. Ensure you have Java 25 or above installed on your computer.
2. Download the latest `clanky.jar` from the releases page.
3. Open a terminal, navigate to the folder containing the jar, and run:

   ```
   java -jar clanky.jar
   ```
4. Type a command and press Enter. Refer to the [Features](#features) section below for the full list of commands.

---

## Features

> 💡 **Notes on command format**
>
> - Words in `<angle brackets>` are parameters you supply.
> - Task numbers refer to the position shown by the `list` command, starting from 1.
> - Dates must be entered in `yyyy-mm-dd` format, e.g. `2019-12-02`.

### Listing all tasks: `list`

Shows every task currently in your list, along with its type, completion status, and any dates.

Example: `list`

```
=================================================
1.[T][X] read book
2.[D][ ] return book (by: Dec 2 2019)
3.[E][ ] project meeting (from: Aug 06 2026 to: Aug 06 2026)
=================================================
```

### Adding a todo: `todo`

Adds a simple task with no date attached.

Example: `todo read book`

```
=================================================
Got it. I've added this task:
  [T][ ] read book
Now you have 1 task in the list.
=================================================
```

### Adding a deadline: `deadline`

Adds a task that must be completed by a specific date.

Example: `deadline return book /by 2019-12-02`

```
=================================================
Got it. I've added this task:
  [D][ ] return book (by: Dec 2 2019)
Now you have 2 tasks in the list.
=================================================
```

### Adding an event: `event`

Adds a task that spans a start and end date.

Example: `event project meeting /from 2026-08-06 /to 2026-08-06`

```
=================================================
Got it. I've added this task:
  [E][ ] project meeting (from: Aug 6 2026 to: Aug 6 2026)
Now you have 3 tasks in the list.
=================================================
```

### Marking a task as done: `mark`

Marks the specified task as completed.

Example: `mark 1`

```
=================================================
Nice! I've marked this task as done:
[T][X] read book
=================================================
```

### Unmarking a task: `unmark`

Marks the specified task as not yet completed.

Example: `unmark 1`

```
=================================================
OK, I've marked this task as not done yet:
[T][ ] read book
=================================================
```

### Deleting a task: `delete`

Removes the specified task from your list permanently.

Example: `delete 2`

```
=================================================
Noted. I've removed this task:
  [D][ ] return book (by: Dec 2 2019)
Now you have 2 tasks in the list.
=================================================
```

### Finding tasks: `find`

Searches your task list for descriptions containing the given keyword.

Example: `find book`

```
=================================================
Here are the matching tasks in your list:
1. [T][X] read book
=================================================
```

### Exiting the program: `bye`

Ends the session and closes Clanky.

Example: `bye`

```
=================================================
Bye. Hope to see you again soon!
=================================================
```

### Saving and loading data

Clanky automatically saves your tasks to disk after every change, and loads them back the next time you start the program — no manual save command needed. Data is stored in a hidden folder in your home directory, so it works consistently no matter where you run Clanky from.

---

## Command summary

| Action | Format | Example |
| --- | --- | --- |
| List tasks | `list` | `list` |
| Add todo | `todo <description>` | `todo read book` |
| Add deadline | `deadline <description> /by <yyyy-mm-dd>` | `deadline return book /by 2019-12-02` |
| Add event | `event <description> /from <yyyy-mm-dd> /to <yyyy-mm-dd>` | `event project meeting /from 2026-08-06 /to 2026-08-06` |
| Mark done | `mark <task number>` | `mark 1` |
| Unmark | `unmark <task number>` | `unmark 1` |
| Delete | `delete <task number>` | `delete 2` |
| Find | `find <keyword>` | `find book` |
| Exit | `bye` | `bye` |