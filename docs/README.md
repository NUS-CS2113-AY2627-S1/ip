# Echo User Guide

**Echo** is a command-line chatbot that keeps track of your to-dos, deadlines and events. You type a command, Echo replies, and your tasks are saved automatically, so they are still there the next time you start it.

* [Quick start](#quick-start)
* [Features](#features)
* [Saving your data](#saving-your-data)
* [Command summary](#command-summary)

## Quick start

1. Make sure you have **Java 25** installed. You can check by running `java -version` in a terminal.
2. Download the latest `echo.jar` from the [Releases page](https://github.com/nabijonzzz/ip/releases).
3. Copy `echo.jar` into an empty folder of your choice. Echo keeps its data file in this folder.
4. Open a terminal in that folder and run `java -jar echo.jar`.
5. Type a command and press Enter. For example, try `todo read book` and then `list`.
6. Type `bye` to exit.

When Echo starts, it greets you like this:

```
____________________________________________________________
 _____ ____ _   _  ___
| ____/ ___| | | |/ _ \
|  _|| |   | |_| | | | |
| |__| |___|  _  | |_| |
|_____\____|_| |_|\___/

Hello! I'm Echo.
What can I do for you?
____________________________________________________________
```

## Features

**Notes about the command format:**

* Words in `UPPER_CASE` are values you supply. In `todo DESCRIPTION`, for example, `DESCRIPTION` can be `read book`.
* Dates are written as `yyyy-mm-dd`, for example `2019-10-15`. Echo shows them as `Oct 15 2019`.
* `TASK_NUMBER` is the number shown next to a task by the `list` command.
* Commands are lowercase: `list` works, `List` does not.
* A task cannot contain the `|` character, because Echo uses it in its data file.

The examples below follow on from each other, starting from an empty list.

### Adding a to-do: `todo`

Adds a task that has no date.

Format: `todo DESCRIPTION`

Example: `todo read book`

```
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
```

### Adding a deadline: `deadline`

Adds a task that must be done by a certain date.

Format: `deadline DESCRIPTION /by DATE`

Example: `deadline return book /by 2019-10-15`

```
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Oct 15 2019)
Now you have 2 tasks in the list.
____________________________________________________________
```

### Adding an event: `event`

Adds a task that runs from a start date to an end date.

Format: `event DESCRIPTION /from START_DATE /to END_DATE`

* `END_DATE` cannot be before `START_DATE`. For a one-day event, use the same date for both.

Example: `event project fair /from 2019-10-14 /to 2019-10-16`

```
____________________________________________________________
Got it. I've added this task:
  [E][ ] project fair (from: Oct 14 2019 to: Oct 16 2019)
Now you have 3 tasks in the list.
____________________________________________________________
```

### Listing all tasks: `list`

Shows every task in your list, numbered from 1.

Format: `list`

* The first box shows the type of task: `T` for a to-do, `D` for a deadline, `E` for an event.
* The second box shows `X` if the task is done, and is empty if it is not.

Example: `list`

```
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Oct 15 2019)
3.[E][ ] project fair (from: Oct 14 2019 to: Oct 16 2019)
____________________________________________________________
```

### Marking a task as done: `mark`

Marks a task as done.

Format: `mark TASK_NUMBER`

Example: `mark 1`

```
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read book
____________________________________________________________
```

### Marking a task as not done: `unmark`

Marks a task as not done yet.

Format: `unmark TASK_NUMBER`

Example: `unmark 1`

```
____________________________________________________________
OK, I've marked this task as not done yet:
  [T][ ] read book
____________________________________________________________
```

### Finding tasks by keyword: `find`

Shows the tasks whose description contains a keyword.

Format: `find KEYWORD`

* The search is case-sensitive: `find Book` does not match `read book`.
* Matching tasks are numbered from 1 in the order they appear in your list. These numbers are positions in the search result, not in your full list, so use `list` to see the numbers to give to `mark`, `unmark` or `delete`.

Example: `find book`

```
____________________________________________________________
Here are the matching tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Oct 15 2019)
____________________________________________________________
```

### Seeing what is on a date: `on`

Shows the deadlines due on a date and the events taking place on it.

Format: `on DATE`

* An event is included on every day from its start date to its end date, both included.
* To-dos have no date, so they are never included.

Example: `on 2019-10-15`

```
____________________________________________________________
Here are the tasks on Oct 15 2019:
1.[D][ ] return book (by: Oct 15 2019)
2.[E][ ] project fair (from: Oct 14 2019 to: Oct 16 2019)
____________________________________________________________
```

### Deleting a task: `delete`

Removes a task from your list. The tasks after it move up by one number.

Format: `delete TASK_NUMBER`

Example: `delete 3`

```
____________________________________________________________
Noted. I've removed this task:
  [E][ ] project fair (from: Oct 14 2019 to: Oct 16 2019)
Now you have 2 tasks in the list.
____________________________________________________________
```

### Exiting: `bye`

Closes Echo.

Format: `bye`

```
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### When something goes wrong

If Echo cannot understand a command, it replies with a message starting with `OOPS!!!` that explains what is wrong, often with an example of the correct format. Your tasks are not changed. For example, `deadline return book` gives:

```
____________________________________________________________
OOPS!!! A deadline needs a due date, e.g. "deadline return book /by 2019-10-15".
____________________________________________________________
```

## Saving your data

* Echo saves your tasks automatically after every change. There is no need to save manually.
* The tasks are stored in `data/echo.txt`, inside the folder you run Echo from. Echo creates the file the first time you add a task.
* If a line in the file cannot be read (for example, after it was edited by hand into a different format), Echo skips that line and loads the rest.

## Command summary

| Action | Format | Example |
|---|---|---|
| Add a to-do | `todo DESCRIPTION` | `todo read book` |
| Add a deadline | `deadline DESCRIPTION /by DATE` | `deadline return book /by 2019-10-15` |
| Add an event | `event DESCRIPTION /from START_DATE /to END_DATE` | `event project fair /from 2019-10-14 /to 2019-10-16` |
| List all tasks | `list` | `list` |
| Mark as done | `mark TASK_NUMBER` | `mark 1` |
| Mark as not done | `unmark TASK_NUMBER` | `unmark 1` |
| Find by keyword | `find KEYWORD` | `find book` |
| See what is on a date | `on DATE` | `on 2019-10-15` |
| Delete a task | `delete TASK_NUMBER` | `delete 3` |
| Exit | `bye` | `bye` |
