# Friday User Guide

Friday is a desktop chatbot for keeping track of your to-dos, deadlines, and events, operated entirely by typing commands into a Command Line Interface (CLI). If you can type fast, Friday gets your task list updated faster than any mouse-driven app.

## Quick start

1. Ensure you have **Java 25** or above installed on your computer.
2. Download the latest `friday.jar` (or build it yourself: run `./gradlew shadowJar` from the project root, and find the jar at `build/libs/friday.jar`).
3. Open a terminal in the folder containing the jar and run:
   ```
   java -jar friday.jar
   ```
4. Type a command into the terminal and press Enter to run it. Some things to try first:
   * `todo read book` — adds a to-do.
   * `list` — shows everything on your list.
   * `bye` — exits the program.
5. Refer to the [Features](#features) section below for details of each command.

Friday automatically saves your task list to disk after every change, and reloads it the next time you start the program — there's no separate save command.

## Features

> **Notes on the command format**
> * Words in `UPPER_CASE` are parameters to be supplied by you, e.g. in `todo DESCRIPTION`, `DESCRIPTION` could be `todo read book`.
> * Task numbers referred to below (e.g. in `mark INDEX`) are the numbers shown by the `list` command, starting from 1.
> * A task's description cannot contain the `|` character, since Friday uses it internally as a separator when saving tasks to disk.

### Adding a to-do: `todo`

Adds a simple task with no date/time attached.

Format: `todo DESCRIPTION`

Example: `todo read book`

```
____________________________________________________________
todo read book
____________________________________________________________
```

### Adding a deadline: `deadline`

Adds a task that needs to be done by a specific date.

Format: `deadline DESCRIPTION /by DATE`

`DATE` must be in `yyyy-MM-dd` format (e.g. `2019-12-02`). Friday displays it back in a friendlier `MMM dd yyyy` format.

Example: `deadline return book /by 2019-12-02`

```
____________________________________________________________
deadline return book /by 2019-12-02
____________________________________________________________
```

The task then appears in `list` as:
```
2.[D][ ] return book (by: Dec 02 2019)
```

### Adding an event: `event`

Adds a task that spans a start and end time.

Format: `event DESCRIPTION /from START /to END`

`START` and `END` are free text (e.g. a day and time), not restricted to a particular date format.

Example: `event project meeting /from Mon 2pm /to Mon 4pm`

```
____________________________________________________________
event project meeting /from Mon 2pm /to Mon 4pm
____________________________________________________________
```

### Listing all tasks: `list`

Shows every task currently on your list, numbered in the order they were added.

Format: `list`

```
1.[T][ ] read book

2.[D][ ] return book (by: Dec 02 2019)

3.[E][ ] project meeting (from: Mon 2pm to: Mon 4pm)

____________________________________________________________
```

`[T]`, `[D]`, and `[E]` mark a to-do, deadline, and event respectively. The second bracket shows `X` if the task is done, or a blank space if it isn't.

### Marking a task as done: `mark`

Format: `mark INDEX`

Example: `mark 1` marks the 1st task in the list as done.

```
____________________________________________________________
The task below has been marked as done
[X] read book
____________________________________________________________
```

### Marking a task as not done: `unmark`

Format: `unmark INDEX`

Example: `unmark 1` marks the 1st task in the list as not done.

```
____________________________________________________________
The task below has been marked as undone
[ ] read book
____________________________________________________________
```

### Deleting a task: `delete`

Removes a task from the list permanently.

Format: `delete INDEX`

Example: `delete 2` removes the 2nd task in the list.

```
____________________________________________________________
Noted. I've removed this task:
[D][ ] return book (by: Dec 02 2019)
Now you have 2 tasks in the list.
____________________________________________________________
```

### Finding tasks: `find`

Finds tasks whose description contains a given keyword. Matching is case-sensitive, and the whole text after `find` is treated as the keyword (so a keyword can be more than one word).

Format: `find KEYWORD`

Example: `find book`

```
____________________________________________________________
Here are the matching tasks in your list:
1.[T][ ] read book

2.[D][ ] return book (by: Dec 02 2019)

____________________________________________________________
```

### Exiting the program: `bye`

Format: `bye`

```
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Saving the data

Friday's task list is saved automatically to `./data/friday.txt` after every command that changes it (adding, deleting, marking, unmarking) — there's no need to save manually. It's loaded back automatically the next time you start Friday from the same folder.

## Command summary

| Action | Format | Example |
|---|---|---|
| Add a to-do | `todo DESCRIPTION` | `todo read book` |
| Add a deadline | `deadline DESCRIPTION /by DATE` | `deadline return book /by 2019-12-02` |
| Add an event | `event DESCRIPTION /from START /to END` | `event meeting /from Mon 2pm /to Mon 4pm` |
| List all tasks | `list` | `list` |
| Mark a task as done | `mark INDEX` | `mark 1` |
| Mark a task as not done | `unmark INDEX` | `unmark 1` |
| Delete a task | `delete INDEX` | `delete 2` |
| Find tasks by keyword | `find KEYWORD` | `find book` |
| Exit | `bye` | `bye` |
