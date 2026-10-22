# Cole User Guide

**Cole** is a chatbot that helps you keep track of your tasks right from the command line.
Type a short command, and Cole remembers your todos, deadlines and events for you, even after you close it.

```
_____________________________________________________________

  ____      _
 / ___|___ | | ___
| |   / _ \| |/ _ \
| |__| (_) | |  __/
 \____\___/|_|\___|

Hello! I'm Cole.

What can I do for you?
_____________________________________________________________
```

- [Quick start](#quick-start)
- [Features](#features)
  - [Adding a todo: `todo`](#adding-a-todo-todo)
  - [Adding a deadline: `deadline`](#adding-a-deadline-deadline)
  - [Adding an event: `event`](#adding-an-event-event)
  - [Listing all tasks: `list`](#listing-all-tasks-list)
  - [Marking a task as done: `mark`](#marking-a-task-as-done-mark)
  - [Marking a task as not done: `unmark`](#marking-a-task-as-not-done-unmark)
  - [Deleting a task: `delete`](#deleting-a-task-delete)
  - [Finding tasks by keyword: `find`](#finding-tasks-by-keyword-find)
  - [Exiting Cole: `bye`](#exiting-cole-bye)
  - [Saving the data](#saving-the-data)
- [FAQ](#faq)
- [Command summary](#command-summary)

---

## Quick start

1. Make sure you have **Java 25** installed. You can check by running `java -version` in a terminal.
2. Download the latest `cole.jar` from the [Releases page](https://github.com/ducthien-k24/ip/releases).
3. Copy `cole.jar` into the folder where you want Cole to keep your tasks.
4. Open a terminal in that folder and run:
   ```
   java -jar cole.jar
   ```
5. Cole greets you. Type a command and press Enter. Try these to get started:
   - `todo read book` adds a todo.
   - `list` shows all your tasks.
   - `bye` exits Cole.
6. See [Features](#features) below for every command.

---

## Features

> **Notes about the command format**
>
> - Words in `UPPER_CASE` are what you fill in.<br>
>   e.g. in `todo DESCRIPTION`, `DESCRIPTION` can be `read book`, so you type `todo read book`.
> - Command words are lowercase, e.g. type `todo`, not `Todo`. (`list` and `bye` also work in any case.)
> - `INDEX` is the number shown next to the task in `list`, starting from 1.
> - Each task is shown with two boxes: the task type (`[T]` todo, `[D]` deadline, `[E]` event) and its status (`[X]` done, `[ ]` not done).
> - The `|` character cannot be used in any command, because Cole uses it when saving your tasks.

### Adding a todo: `todo`

Adds a task that has no date or time.

Format: `todo DESCRIPTION`

Example: `todo read book`

```
Got it. I've added this task:
 [T][ ] read book
Now you have 1 task in the list.
```

### Adding a deadline: `deadline`

Adds a task that must be done by a certain date.

Format: `deadline DESCRIPTION /by DATE`

- `DATE` must be written as `yyyy-mm-dd`, e.g. `2019-10-15`.
- Cole shows the date in an easier-to-read form, e.g. `Oct 15 2019`.

Example: `deadline return book /by 2019-10-15`

```
Got it. I've added this task:
 [D][ ] return book (by: Oct 15 2019)
Now you have 2 tasks in the list.
```

> **Tip:** Dates such as `Sunday` or `15/10/2019` are not accepted. Cole will remind you to use `yyyy-mm-dd`.

### Adding an event: `event`

Adds a task that happens over a period of time.

Format: `event DESCRIPTION /from START /to END`

- `START` and `END` can be any text, e.g. `Mon 2pm`, `4pm`.
- `/from` must come before `/to`.

Example: `event project meeting /from Mon 2pm /to 4pm`

```
Got it. I've added this task:
 [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
```

### Listing all tasks: `list`

Shows all your tasks, numbered from 1.

Format: `list`

```
Here are the tasks in your list:
1. [T][ ] read book
2. [D][ ] return book (by: Oct 15 2019)
3. [E][ ] project meeting (from: Mon 2pm to: 4pm)
```

### Marking a task as done: `mark`

Marks the task at the given position as done.

Format: `mark INDEX`

Example: `mark 1`

```
Nice! I've marked this task as done:
 [T][X] read book
```

### Marking a task as not done: `unmark`

Marks the task at the given position as not done yet.

Format: `unmark INDEX`

Example: `unmark 1`

```
OK, I've marked this task as not done yet:
 [T][ ] read book
```

### Deleting a task: `delete`

Removes the task at the given position. The tasks after it move up by one.

Format: `delete INDEX`

Example: `delete 3`

```
Noted. I've removed this task:
 [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 2 tasks in the list.
```

> **Warning:** Deleting cannot be undone. Use `list` first to check the number.

### Finding tasks by keyword: `find`

Shows every task whose description contains the keyword.

Format: `find KEYWORD`

- The search ignores upper/lower case, e.g. `find BOOK` also finds `read book`.
- Part of a word also matches, e.g. `find boo` finds `read book`.
- The results are numbered from 1 again. These numbers are **not** the ones to use with `mark`, `unmark` or `delete`. Use `list` to see the real numbers.

Example: `find book`

```
Here are the matching tasks in your list:
1. [T][ ] read book
2. [D][ ] return book (by: Oct 15 2019)
```

### Exiting Cole: `bye`

Closes Cole.

Format: `bye`

```
Bye. Hope to see you again soon!
```

### Saving the data

Cole saves your tasks automatically after every change, so there is no need to save manually.
Next time you start Cole from the same folder, your tasks are loaded back.

The tasks are stored in `data/cole.txt`, inside the folder where you ran `cole.jar`.

> **Caution:** Editing `data/cole.txt` by hand is not recommended. If a line in the file cannot be read (e.g. a deadline date that is not in `yyyy-mm-dd` format), Cole skips that line when it starts.

---

## FAQ

**Q: How do I move my tasks to another computer?**<br>
A: Copy the `data` folder (with `cole.txt` inside) into the folder where you run `cole.jar` on the other computer.

**Q: Cole says `OOPS!!!`. What went wrong?**<br>
A: The command is missing something or written in the wrong format. The message tells you what to fix, often with an example. For example:

```
OOPS!!! Please write the deadline date as yyyy-mm-dd, e.g. "deadline return book /by 2019-10-15".
```

**Q: Cole says it has no idea what my command means.**<br>
A: Check the spelling of the command word against the [Command summary](#command-summary) below.

---

## Command summary

| Action | Format | Example |
|---|---|---|
| Add todo | `todo DESCRIPTION` | `todo read book` |
| Add deadline | `deadline DESCRIPTION /by yyyy-mm-dd` | `deadline return book /by 2019-10-15` |
| Add event | `event DESCRIPTION /from START /to END` | `event project meeting /from Mon 2pm /to 4pm` |
| List | `list` | `list` |
| Mark as done | `mark INDEX` | `mark 1` |
| Mark as not done | `unmark INDEX` | `unmark 1` |
| Delete | `delete INDEX` | `delete 3` |
| Find | `find KEYWORD` | `find book` |
| Exit | `bye` | `bye` |
