# Usagi User Guide

Usagi is a chatbot that keeps track of your tasks. You type short commands,
and Usagi remembers your todos, deadlines and events for you. ("Usagi" means
rabbit in Japanese, which is why a rabbit greets you when it starts.)

Your tasks are saved automatically, so they are still there the next time you
start Usagi.

- [Quick start](#quick-start)
- [Features](#features)
  - [Adding a todo: `todo`](#adding-a-todo-todo)
  - [Adding a deadline: `deadline`](#adding-a-deadline-deadline)
  - [Adding an event: `event`](#adding-an-event-event)
  - [Listing all tasks: `list`](#listing-all-tasks-list)
  - [Marking a task as done: `mark`](#marking-a-task-as-done-mark)
  - [Marking a task as not done: `unmark`](#marking-a-task-as-not-done-unmark)
  - [Finding tasks: `find`](#finding-tasks-find)
  - [Deleting a task: `delete`](#deleting-a-task-delete)
  - [Exiting: `bye`](#exiting-bye)
  - [Saving your tasks](#saving-your-tasks)
- [Command summary](#command-summary)

## Quick start

1. Make sure you have Java 25 installed. You can check by running
   `java -version` in a terminal.
2. Download the latest `usagi.jar` from the
   [releases page](https://github.com/yy261/ip/releases).
3. Copy the file to the folder you want Usagi to keep its data in.
4. Open a terminal, go to that folder, and run:
   ```
   java -jar usagi.jar
   ```
5. Type a command and press Enter. Try `todo read book`, then `list`.

> **Tip:** If the rabbit shows up as question marks or odd symbols on Windows,
> your terminal is not using UTF-8. Run `chcp 65001` first, then start Usagi
> again. Everything else works either way.

## Features

**About the command format**

- Words in `UPPER_CASE` are for you to fill in. For example, in
  `todo DESCRIPTION`, you could type `todo read book`.
- Command words are in lower case: `list` works, `List` does not.
- `TASK_NUMBER` is the number shown next to the task by the `list` command.

Every task is shown in the form `[T][X] read book`:

- The first box shows the type: `T` for todo, `D` for deadline, `E` for event.
- The second box shows an `X` if the task is done, and is empty if it is not.

### Adding a todo: `todo`

Adds a task that has no date attached.

Format: `todo DESCRIPTION`

Example: `todo read book`

```
____________________________________________________________
Puru Yaha->(Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.)
____________________________________________________________
```

### Adding a deadline: `deadline`

Adds a task that must be done by a certain time.

Format: `deadline DESCRIPTION /by DUE_DATE`

- `DUE_DATE` can be any text, e.g. `Sunday` or `2 Oct, 6pm`.

Example: `deadline return book /by Sunday`

```
____________________________________________________________
Puru Yaha->(Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.)
____________________________________________________________
```

### Adding an event: `event`

Adds a task that starts and ends at certain times.

Format: `event DESCRIPTION /from START /to END`

- `START` and `END` can be any text.
- `/from` must come before `/to`.

Example: `event project meeting /from Mon 2pm /to 4pm`

```
____________________________________________________________
Puru Yaha->(Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.)
____________________________________________________________
```

### Listing all tasks: `list`

Shows every task, with its task number.

Format: `list`

```
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
```

### Marking a task as done: `mark`

Marks the task with the given number as done.

Format: `mark TASK_NUMBER`

Example: `mark 1`

```
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read book
____________________________________________________________
```

### Marking a task as not done: `unmark`

Marks the task with the given number as not done, e.g. if you marked it by
mistake.

Format: `unmark TASK_NUMBER`

Example: `unmark 1`

```
____________________________________________________________
OK, I've marked this task as not done yet:
  [T][ ] read book
____________________________________________________________
```

### Finding tasks: `find`

Shows the tasks whose description contains the given keyword.

Format: `find KEYWORD`

- The search ignores upper and lower case: `find book` also finds `Book club`.
- `KEYWORD` can be more than one word, e.g. `find return book`.
- Only the description is searched, not the dates.

Example: `find book`

```
____________________________________________________________
Here are the matching tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Sunday)
____________________________________________________________
```

> **Note:** The numbers shown by `find` only count the matching tasks. To
> `mark`, `unmark` or `delete` a task, use its number from the `list` command.

### Deleting a task: `delete`

Removes the task with the given number. The tasks after it move up by one
number.

Format: `delete TASK_NUMBER`

Example: `delete 3`

```
____________________________________________________________
HaAAA->(Noted. I've removed this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 2 tasks in the list.)
____________________________________________________________
```

### Exiting: `bye`

Closes Usagi.

Format: `bye`

### Saving your tasks

Usagi saves your tasks after every change, so there is no save command. They
are kept in the file `data/tasks.txt`, inside the folder you started Usagi
from, and are loaded again the next time you start it there.

> **Note:** Avoid typing ` | ` (a bar with a space on each side) in a
> description or date. Usagi uses it to separate the parts of a task in the
> save file, so such a task will not be loaded back correctly.

## Command summary

| Action | Format | Example |
|--------|--------|---------|
| Add a todo | `todo DESCRIPTION` | `todo read book` |
| Add a deadline | `deadline DESCRIPTION /by DUE_DATE` | `deadline return book /by Sunday` |
| Add an event | `event DESCRIPTION /from START /to END` | `event project meeting /from Mon 2pm /to 4pm` |
| List all tasks | `list` | `list` |
| Mark as done | `mark TASK_NUMBER` | `mark 1` |
| Mark as not done | `unmark TASK_NUMBER` | `unmark 1` |
| Find tasks | `find KEYWORD` | `find book` |
| Delete a task | `delete TASK_NUMBER` | `delete 3` |
| Exit | `bye` | `bye` |
