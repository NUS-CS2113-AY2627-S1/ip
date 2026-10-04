# Luke User Guide

Luke is a command-line task manager that helps you record todos, deadlines, and events in a persistent task list.

## Contents

- [Quick start](#quick-start)
- [Starting Luke](#starting-luke)
- [Reading the task list](#reading-the-task-list)
- [Command syntax](#command-syntax)
- [Adding a todo: `todo`](#adding-a-todo-todo)
- [Adding a deadline: `deadline`](#adding-a-deadline-deadline)
- [Adding an event: `event`](#adding-an-event-event)
- [Listing tasks: `list`](#listing-tasks-list)
- [Marking a task as done: `mark`](#marking-a-task-as-done-mark)
- [Marking a task as not done: `unmark`](#marking-a-task-as-not-done-unmark)
- [Deleting a task: `delete`](#deleting-a-task-delete)
- [Finding tasks: `find`](#finding-tasks-find)
- [Exiting Luke: `bye`](#exiting-luke-bye)
- [Data and persistence](#data-and-persistence)
- [FAQ](#faq)
- [Known issues](#known-issues)
- [Command summary](#command-summary)

## Quick start

1. Install Java 25.
2. Download `Luke.jar` from the [latest GitHub release](https://github.com/lukelouyu/ip/releases/latest).
3. Place `Luke.jar` in a folder where Luke can keep its data.
4. Open a terminal in that folder.
5. Run:

   ```bash
   java -jar Luke.jar
   ```

6. When Luke asks what it can do for you, enter a command such as:

   ```text
   todo read a book
   ```

7. Enter `bye` when you are ready to exit.

## Starting Luke

After launching `Luke.jar`, Luke displays this welcome screen and waits for your command:

```text
____________________________________________________________
Hello! I'm Luke
 _          _
| |   _   _| | _____
| |  | | | | |/ / _ \
| |__| |_| |   <  __/
|_____\__,_|_|\_\___|

What can I do for you?
____________________________________________________________
```

## Reading the task list

Luke displays each task with a type icon and a completion icon:

| Icon | Meaning |
|---|---|
| `[T]` | Todo |
| `[D]` | Deadline |
| `[E]` | Event |
| `[ ]` | Not done |
| `[X]` | Done |

For example, `[D][X] submit report (by: Oct 02 2026)` is a completed deadline.

The number shown before a task by `list` identifies that task for `mark`, `unmark`, and `delete` commands.

## Command syntax

- Words in `UPPER_CASE` are values you must supply.
- Command words and prefixes are case-sensitive. Enter them in lowercase exactly as shown.
- Enter dates as `yyyy-MM-dd`, for example `2026-10-02`.
- Enter date-times as `yyyy-MM-dd HH:mm` using the 24-hour clock, for example `2026-10-01 14:00`.
- Do not include the pipe character (`|`) in task descriptions. See [Known issues](#known-issues).

## Adding a todo: `todo`

Adds a task that has no deadline or scheduled time.

Format: `todo DESCRIPTION`

- `DESCRIPTION` must not be blank.
- Luke saves the new todo automatically.

Example: `todo borrow a library book`

Luke adds an incomplete todo named "borrow a library book".

```text
____________________________________________________________
Got it. I've added this task:
  [T][ ] borrow a library book
Now you have 1 tasks in the list.
____________________________________________________________
```

## Adding a deadline: `deadline`

Adds a task that must be completed by a specified date.

Format: `deadline DESCRIPTION /by DATE`

- `DESCRIPTION` must not be blank.
- `DATE` must be a valid calendar date in `yyyy-MM-dd` format.
- The `/by` prefix and argument order are required.
- Luke saves the new deadline automatically.

Example: `deadline return library book /by 2026-10-02`

Luke adds an incomplete deadline due on 2 October 2026.

```text
____________________________________________________________
Got it. I've added this task:
  [D][ ] return library book (by: Oct 02 2026)
Now you have 1 tasks in the list.
____________________________________________________________
```

## Adding an event: `event`

Adds a task scheduled between a start and end time.

Format: `event DESCRIPTION /from START_DATE_TIME /to END_DATE_TIME`

- `DESCRIPTION` must not be blank.
- Both date-times must use `yyyy-MM-dd HH:mm` format.
- `END_DATE_TIME` must be later than `START_DATE_TIME`.
- The `/from` and `/to` prefixes and their order are required.
- Luke saves the new event automatically.

Example: `event project meeting /from 2026-10-01 14:00 /to 2026-10-01 15:00`

Luke adds an incomplete event scheduled from 2:00 pm to 3:00 pm on 1 October 2026.

```text
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Oct 01 2026 14:00 to: Oct 01 2026 15:00)
Now you have 1 tasks in the list.
____________________________________________________________
```

## Listing tasks: `list`

Shows every stored task in its current order.

Format: `list`

- Luke reports when the task list is empty.
- The displayed task numbers are used by `mark`, `unmark`, and `delete`.
- This command does not change the task list.

Example: `list`

Luke displays output similar to:

```text
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] borrow a library book
2. [D][X] return library book (by: Oct 02 2026)
3. [E][ ] project meeting (from: Oct 01 2026 14:00 to: Oct 01 2026 15:00)
____________________________________________________________
```

## Marking a task as done: `mark`

Marks a task as completed.

Format: `mark TASK_NUMBER`

- `TASK_NUMBER` must be a whole number shown by `list`.
- Luke saves the updated completion status automatically.

Example: `mark 2`

Assuming task 2 is the deadline from the earlier example, Luke marks it as done and displays it with `[X]`.

```text
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] return library book (by: Oct 02 2026)
____________________________________________________________
```

## Marking a task as not done: `unmark`

Marks a completed task as incomplete.

Format: `unmark TASK_NUMBER`

- `TASK_NUMBER` must be a whole number shown by `list`.
- Luke saves the updated completion status automatically.

Example: `unmark 2`

Assuming task 2 is the completed deadline from the earlier example, Luke marks it as not done and displays it with
`[ ]`.

```text
____________________________________________________________
OK, I've marked this task as not done yet:
  [D][ ] return library book (by: Oct 02 2026)
____________________________________________________________
```

## Deleting a task: `delete`

Permanently removes a task from the task list.

Format: `delete TASK_NUMBER`

- `TASK_NUMBER` must be a whole number shown by `list`.
- Task numbers after the deleted task move up by one.
- Luke saves the updated task list automatically.

Example: `delete 1`

Luke removes task 1 and reports the number of remaining tasks.

```text
____________________________________________________________
Noted. I've removed this task:
  [T][ ] borrow a library book
Now you have 2 tasks in the list.
____________________________________________________________
```

## Finding tasks: `find`

Shows tasks whose descriptions contain a keyword.

Format: `find KEYWORD`

- `KEYWORD` must not be blank.
- Matching is case-insensitive.
- A partial-word match is accepted. For example, `book` matches `bookshelf`.
- Matches remain in the same order as the main task list.
- Searching does not change any tasks.
- Use `list` to confirm the main-list number before modifying a found task.

Example: `find book`

Luke displays every task whose description contains "book", regardless of capitalization.

```text
____________________________________________________________
Here are the matching tasks in your list:
1. [T][ ] borrow a library book
2. [D][ ] return library book (by: Oct 02 2026)
____________________________________________________________
```

## Exiting Luke: `bye`

Ends the current Luke session.

Format: `bye`

Example: `bye`

Luke displays its goodbye message and exits.

```text
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Data and persistence

Luke automatically saves the task list after you add, mark, unmark, or delete a task. The data is stored at
`data/luke.txt`, relative to the folder from which you launch `Luke.jar`. Luke loads this file the next time it starts
from the same folder.

To transfer or back up your tasks, exit Luke and copy the entire `data` folder. Keep the backup together with the
corresponding version of Luke.

> [!WARNING]
> Do not edit `data/luke.txt` while Luke is running. Invalid or incomplete data can prevent all tasks from loading.
> Back up the file before attempting any manual edit.

## FAQ

### Why does Luke show a warning instead of running my command?

Check that the command word is lowercase, all required prefixes are present, and every date or date-time follows the
format shown in this guide. Luke remains open after most invalid commands, so you can correct the command and try
again.

### Why are my previous tasks missing?

Luke looks for `data/luke.txt` relative to the folder where you launch the JAR. Start Luke from the same folder you
used previously. If you moved the JAR, move its `data` folder with it or launch it from the original folder.

### How do I identify the number of a task?

Run `list`. Use the number shown before the task with `mark`, `unmark`, or `delete`. Run `list` again after deleting a
task because the remaining numbers may have changed.

### Is task searching case-sensitive?

No. For example, `find BOOK` matches descriptions containing `book`, `Book`, or `BOOK`.

## Known issues

### Pipe characters in descriptions can damage saved data

Entering `|` in a task description conflicts with Luke's storage format. The task may display correctly during the
current session but load incorrectly the next time Luke starts.

Workaround: Do not use `|` in task descriptions. Use a hyphen (`-`), slash (`/`), or colon (`:`) instead.

### A damaged data file stops the remaining tasks from loading

If `data/luke.txt` contains an invalid line, Luke displays a load warning. Tasks after the damaged line are not loaded.

Workaround: Exit Luke, restore the `data` folder from a backup, and restart the application.

## Command summary

| Action | Format | Example |
|---|---|---|
| Add a todo | `todo DESCRIPTION` | `todo borrow a library book` |
| Add a deadline | `deadline DESCRIPTION /by DATE` | `deadline return book /by 2026-10-02` |
| Add an event | `event DESCRIPTION /from START_DATE_TIME /to END_DATE_TIME` | `event meeting /from 2026-10-01 14:00 /to 2026-10-01 15:00` |
| List tasks | `list` | `list` |
| Mark a task done | `mark TASK_NUMBER` | `mark 2` |
| Mark a task not done | `unmark TASK_NUMBER` | `unmark 2` |
| Delete a task | `delete TASK_NUMBER` | `delete 1` |
| Find tasks | `find KEYWORD` | `find book` |
| Exit Luke | `bye` | `bye` |

