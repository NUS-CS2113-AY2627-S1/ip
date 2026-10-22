# Bob's Task Outpost - User Guide

**Bob's Task Outpost** is a friendly, cowboy-themed chatbot that helps you add, track, find, and complete tasks from the command line.

## Quick start

1. Ensure that Java 25 or later is installed.
2. Download the latest Bob `.jar` file and place it in a folder of your choice.
3. Open a terminal in that folder.
4. Run the application, replacing `bob.jar` with the actual file name if necessary:

   ```bash
   java -jar bob.jar
   ```

5. Enter a command and press <kbd>Enter</kbd>. Try `todo Read a book` to create your first task.

Bob saves task changes automatically, so no separate save command is needed.

## Reading your task list

Bob uses two symbols to show each task's type and completion status:

| Symbol | Meaning |
|---|---|
| `[T]` | Todo |
| `[D]` | Deadline |
| `[E]` | Event |
| `[ ]` | Not completed |
| `[X]` | Completed |

For example:

```text
1. [T][X] Read a book
2. [D][ ] Submit report (by: 31/12/2099 1800)
3. [E][ ] Team meeting (from: Monday 2pm to: Monday 3pm)
```

## Command format

- Words in `UPPER_CASE` are values you must replace. For example, replace `DESCRIPTION` with `Read a book`.
- Commands must be entered in lowercase, exactly as shown.
- Task numbers refer to the numbered positions shown by the `list` command.
- Do not type placeholder words such as `DESCRIPTION` or `TASK_NUMBER` literally.

## Features

### Adding a todo: `todo`

Adds a task without a date or time.

Format: `todo DESCRIPTION`

Example:

```text
todo Read a book
```

Bob adds `Read a book` as a new todo.

### Adding a deadline: `deadline`

Adds a task that must be completed by a future date and time.

Format: `deadline DESCRIPTION /by DD/MM/YYYY HHMM`

Example:

```text
deadline Submit report /by 31/12/2099 1800
```

The date must:

- use the exact `DD/MM/YYYY HHMM` format;
- contain a valid calendar date;
- use 24-hour time, such as `0900` or `1830`; and
- be later than the current date and time.

Bob also tells you how many days and hours remain until the deadline.

### Adding an event: `event`

Adds a task that takes place between a start and an end.

Format: `event DESCRIPTION /from START /to END`

Example:

```text
event Team meeting /from Monday 2pm /to Monday 3pm
```

Both `/from` and `/to` are required, and `/from` must appear before `/to`. Event times can be written in any clear text format.

### Listing tasks: `list`

Shows every task and its current number.

Format: `list`

Run `list` before using `mark`, `unmark`, or `delete` if you are unsure of a task's number.

### Finding tasks: `find`

Shows tasks whose descriptions contain the given keyword.

Format: `find KEYWORD`

Example:

```text
find book
```

The search:

- checks task descriptions only;
- matches partial words, so `book` matches `Return library book`; and
- is case-sensitive, so `book` and `Book` are treated differently.

The numbers in search results only number the matches. Use `list` to check the task number before changing or deleting a task.

### Marking a task as completed: `mark`

Marks the task at the specified number as completed.

Format: `mark TASK_NUMBER`

Example:

```text
mark 2
```

This marks the second task shown by `list` with `[X]`.

### Marking a task as not completed: `unmark`

Returns a completed task to the not-completed state.

Format: `unmark TASK_NUMBER`

Example:

```text
unmark 2
```

This changes the second task's status back to `[ ]`.

### Deleting a task: `delete`

Permanently removes the task at the specified number.

Format: `delete TASK_NUMBER`

Example:

```text
delete 3
```

Task numbers may change after deletion. Run `list` again before working with another numbered task.

### Exiting Bob: `bye`

Closes Bob's Task Outpost.

Format: `bye`

## Saving data

Bob automatically saves changes after you add, mark, unmark, or delete a task. Saved tasks are loaded the next time you start the application from the same folder.

Task data is stored in `data/bob.txt`, relative to the folder from which Bob is run. Avoid editing this file manually. If the file contains invalid data, Bob starts that session with an empty task list and warns you that saving is unavailable.

To move your tasks to another computer, copy both the application and the `data/bob.txt` file while keeping the same folder structure.

## Command summary

| Action | Command | Example |
|---|---|---|
| Add a todo | `todo DESCRIPTION` | `todo Read a book` |
| Add a deadline | `deadline DESCRIPTION /by DD/MM/YYYY HHMM` | `deadline Submit report /by 31/12/2099 1800` |
| Add an event | `event DESCRIPTION /from START /to END` | `event Meeting /from Monday 2pm /to Monday 3pm` |
| List tasks | `list` | `list` |
| Find tasks | `find KEYWORD` | `find book` |
| Mark completed | `mark TASK_NUMBER` | `mark 2` |
| Mark not completed | `unmark TASK_NUMBER` | `unmark 2` |
| Delete a task | `delete TASK_NUMBER` | `delete 3` |
| Exit | `bye` | `bye` |
