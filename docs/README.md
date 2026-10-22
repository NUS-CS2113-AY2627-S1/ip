# Vani User Guide

Vani is a command-line task manager that helps you keep track of to-dos, deadlines, and events.
You can mark tasks as completed, search their descriptions, and save your task list between sessions.

## Getting started

You need **Java 25** to run Vani.

1. Place `Vani.jar` in a folder where you want to keep your task data.
2. Open a terminal in that folder and run `java -version` to confirm Java 25 is being used.
3. Start Vani with the following command:

   ```text
   java -jar Vani.jar
   ```

4. When Vani greets you, type a command and press **Enter**.

If you are working from the source code, follow the [build instructions](../README.md#building-a-runnable-jar)
to create `build/libs/Vani.jar`. You can launch it from the project root with `java -jar build/libs/Vani.jar`.

## Command summary

Replace the uppercase words below with your own text or task number. Enter one command per line.
Command keywords and the `/by`, `/from`, and `/to` markers are case-sensitive; use them in lowercase.

| Action | Format | Example |
| --- | --- | --- |
| Add a to-do | `todo DESCRIPTION` | `todo read book` |
| Add a deadline | `deadline DESCRIPTION /by WHEN` | `deadline return book /by June 6th` |
| Add an event | `event DESCRIPTION /from START /to END` | `event project meeting /from Monday 2pm /to Monday 4pm` |
| List all tasks | `list` | `list` |
| Find tasks | `find KEYWORD` | `find book` |
| Mark a task as completed | `mark TASK_NUMBER` | `mark 1` |
| Mark a task as incomplete | `unmark TASK_NUMBER` | `unmark 1` |
| Delete a task | `delete TASK_NUMBER` | `delete 2` |
| Exit Vani | `bye` | `bye` |

Task numbers start at **1**. Use the numbers shown by `list` for `mark`, `unmark`, and `delete`.

The task display uses these symbols:

- `[T]`: a to-do.
- `[D]`: a deadline.
- `[E]`: an event.
- `[ ]`: incomplete.
- `[X]`: completed.

## Commands

The examples below assume an empty task list and are followed in order.
The decorative separator lines are omitted from the output examples.

### Adding a to-do

Add a task that needs only a description. New tasks start as incomplete.

Example: `todo read book`

```text
     Got it. I've added this todo: ^-^
        [T][ ] read book
     Now you have 1 tasks in the list. x_x
```

### Adding a deadline

Add a task with a due date. Include `/by` after the description, followed by the due date.

Example: `deadline return book /by June 6th`

```text
     Got it. I've added this deadline: ^-^
        [D][ ] return book (by: June 6th)
     Now you have 2 tasks in the list. x_x
```

Dates and times are stored as text. You can enter values such as `June 6th`, `Monday`, or `2026-10-02 18:00`.
Vani displays the text you enter without checking whether it is a valid date.

### Adding an event

Add a task with a start and an end. Include `/from` before `/to`, and provide text after each marker.

Example: `event project meeting /from Monday 2pm /to Monday 4pm`

```text
     Got it. I've added this event: ^-^
        [E][ ] project meeting (from: Monday 2pm to: Monday 4pm)
     Now you have 3 tasks in the list. x_x
```

Like deadlines, event dates and times are stored as text. Vani does not check whether the end is after the start.

### Listing tasks

Display all tasks in the order they were added, including completed tasks.

Example: `list`

```text
     Here are the tasks in your list: =^-^=
     1.[T][ ] read book
     2.[D][ ] return book (by: June 6th)
     3.[E][ ] project meeting (from: Monday 2pm to: Monday 4pm)
```

If the list is empty, Vani displays `Yay! You have no tasks in your list. >.<`.

### Marking a task as completed

Mark the task with the specified list number as completed. The task stays in the list.

Example: `mark 1`

```text
     Nice! I've marked this task as done: <3
       1.[T][X] read book
```

### Finding tasks

Search for text anywhere in a task description. Matching is **case-sensitive**:
`find book` matches `read book`, but does not match `Read Book`.
The search checks descriptions only, so dates and times are excluded.

Example: `find book`

```text
     Here are the matching tasks in your list: =^-^=
     1.[T][X] read book
     2.[D][ ] return book (by: June 6th)
```

You can also search for a phrase, such as `find read book`. The whole phrase must appear in the description.
If there are no matches, Vani displays `No matching tasks found. o_O`.

**Search results are numbered separately from the full task list.** Run `list` to get the correct task number
before using `mark`, `unmark`, or `delete` on a search result.

### Marking a task as incomplete

Reopen a completed task by changing its status back to incomplete.

Example: `unmark 1`

```text
     OK, I've marked this task as not done yet: -.-
       1.[T][ ] read book
```

### Deleting a task

Remove the task with the specified list number.

Example: `delete 2`

```text
     OK, I've deleted this task from the list: -.-
       2.[D][ ] return book (by: June 6th)
```

Deleting a task shifts the numbers of later tasks down by one.
In this example, `project meeting` becomes task 2. Run `list` again to see the updated numbers.

### Exiting Vani

End the session.

Example: `bye`

```text
     uwu Bye. Hope to see you again soon!
```

## Saving your tasks

Vani loads tasks from `data/vani.txt` when it starts and saves the list after you add, mark, unmark, or delete a task.
The path is relative to the folder from which Vani is launched, so use the project root as the working directory
to load the same list each time. The `data` folder is created automatically when saving if it does not exist.

If the data file does not exist, Vani starts with an empty list. Corrupted lines are skipped with a warning,
while valid tasks are loaded.

Avoid using the `|` character in descriptions or date and time text, because it separates fields in the saved file.

If Vani displays `Error loading tasks.`, it could not read the file and starts with an empty list.
If it displays `Error saving tasks.`, the latest change remains in the current session,
but the updated list may not have been saved correctly.
Check that the data file and its folder can be read and written before relying on the saved list.

## Invalid input

Vani displays an explanation when a command is unknown, required text is missing, or a task number is invalid.
You can enter another command afterward.

- Descriptions and search keywords must not be empty.
- Deadlines need a description and a due date after `/by`.
- Events need a description, a start after `/from`, and an end after `/to`.
- Task numbers must be integers from 1 to the current number of tasks.
- `list` and `bye` accept no arguments.

For example, entering `find` without a keyword displays:

```text
     The keyword for find cannot be empty. o_O
```
