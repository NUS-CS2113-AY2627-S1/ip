# Shaheer User Guide

**Shaheer** is a chatbot that keeps track of your tasks from the command line. You type short commands to add todos, deadlines and events, tick them off, and search for them. Your tasks are saved automatically, so they are still there next time.

```
____________________________________________________________
 ____  _   _    _    _   _ _____ _____ ____
/ ___|| | | |  / \  | | | | ____| ____|  _ \
\___ \| |_| | / _ \ | |_| |  _| |  _| | |_) |
 ___) |  _  |/ ___ \|  _  | |___| |___|  _ <
|____/|_| |_/_/   \_\_| |_|_____|_____|_| \_\

Hello, I am Shaheer.
What can I do for you?
____________________________________________________________
```

## Quick start

1. Make sure you have **Java 25** installed. You can check by running `java -version` in a terminal.
2. Download the latest `shaheer.jar` from the [releases page](https://github.com/shaheerfarid/ip/releases).
3. Put the file in an empty folder of your choice. Shaheer saves your tasks in a `data` folder next to it.
4. Open a terminal in that folder and run:
   ```
   java -jar shaheer.jar
   ```
5. Type a command and press Enter. For example, try `todo read book`, then `list`. Type `bye` to exit.

## Features

> **About the command format**
> * Words in `UPPER_CASE` are what you fill in. In `todo DESCRIPTION`, `DESCRIPTION` could be `read book`.
> * Command words are not case-sensitive: `list`, `LIST` and `List` all work.
> * `TASK_NUMBER` is the number shown next to the task when you use `list`.

Each task is shown with its type and whether it is done:

| Symbol | Meaning |
|---|---|
| `[T]` | Todo |
| `[D]` | Deadline |
| `[E]` | Event |
| `[X]` | Done |
| `[ ]` | Not done yet |

If Shaheer cannot understand a command, it replies starting with `Hmmmm!`, explains what went wrong, and usually shows an example of the correct format.

### Adding a todo: `todo`

Adds a task that has no date or time.

Format: `todo DESCRIPTION`

Example: `todo read book`

```
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
```

### Adding a deadline: `deadline`

Adds a task that must be done by a certain time.

Format: `deadline DESCRIPTION /by WHEN`

Example: `deadline return book /by Sunday`

```
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
```

`WHEN` can be any text, such as `Sunday`, `June 6th` or `2pm tomorrow`.

### Adding an event: `event`

Adds a task that happens over a period of time.

Format: `event DESCRIPTION /from START /to END`

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
1.[T][ ] read book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
```

### Marking a task as done: `mark`

Format: `mark TASK_NUMBER`

Example: `mark 2`

```
Nice! I've marked this task as done:
  [D][X] return book (by: Sunday)
```

### Marking a task as not done: `unmark`

Format: `unmark TASK_NUMBER`

Example: `unmark 2`

```
OK, I've marked this task as not done yet:
  [D][ ] return book (by: Sunday)
```

### Deleting a task: `delete`

Removes a task from the list for good. The tasks after it move up one number, so use `list` again before your next `mark` or `delete`.

Format: `delete TASK_NUMBER`

Example: `delete 1`

```
Noted. I've removed this task:
  [T][ ] read book
Now you have 2 tasks in the list.
```

### Finding tasks: `find`

Shows the tasks whose description contains the keyword. Upper and lower case are treated the same, so `find BOOK` also finds `read book`.

Format: `find KEYWORD`

Example: `find book`

```
Here are the matching tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Sunday)
```

The numbers in the results only count the matches. To `mark` or `delete` a task you found, use its number from `list`.

### Exiting: `bye`

Saves your tasks and closes Shaheer.

Format: `bye`

```
Bye. Hope to see you back!
```

### Saving your tasks

Shaheer saves your tasks automatically when you exit, to `data/shaheer.txt` in the folder you ran it from. They are loaded again the next time you start Shaheer from the same folder.

To move your tasks to another computer, copy the `data` folder into the folder where you keep `shaheer.jar` there.

> **Caution:** you can edit `data/shaheer.txt` in a text editor, but Shaheer skips any line it cannot read, and that task is lost the next time it saves.

## Command summary

| Action | Format and example |
|---|---|
| Add a todo | `todo DESCRIPTION`<br>e.g. `todo read book` |
| Add a deadline | `deadline DESCRIPTION /by WHEN`<br>e.g. `deadline return book /by Sunday` |
| Add an event | `event DESCRIPTION /from START /to END`<br>e.g. `event project meeting /from Mon 2pm /to 4pm` |
| List all tasks | `list` |
| Mark as done | `mark TASK_NUMBER`<br>e.g. `mark 2` |
| Mark as not done | `unmark TASK_NUMBER`<br>e.g. `unmark 2` |
| Delete a task | `delete TASK_NUMBER`<br>e.g. `delete 1` |
| Find tasks | `find KEYWORD`<br>e.g. `find book` |
| Exit | `bye` |
