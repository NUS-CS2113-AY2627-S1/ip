# Potato User Guide

Potato is a command-line task manager that helps you keep track of to-dos, deadlines, and events. Enter one command at a time and press **Enter** to run it.

Potato saves every change automatically and reloads your tasks the next time it starts. In task listings, `[T]`, `[D]`, and `[E]` identify to-dos, deadlines, and events respectively, while `[X]` marks a completed task and `[ ]` marks an incomplete task.

## Table of contents

- [Command format conventions](#command-format-conventions)
- [Features](#features)
  - [Adding a to-do: `todo`](#adding-a-to-do-todo)
  - [Adding a deadline: `deadline`](#adding-a-deadline-deadline)
  - [Adding an event: `event`](#adding-an-event-event)
  - [Listing all tasks: `list`](#listing-all-tasks-list)
  - [Finding tasks: `find`](#finding-tasks-find)
  - [Marking a task as done: `mark`](#marking-a-task-as-done-mark)
  - [Marking a task as not done: `unmark`](#marking-a-task-as-not-done-unmark)
  - [Deleting a task: `delete`](#deleting-a-task-delete)
  - [Exiting Potato: `bye`](#exiting-potato-bye)
  - [Saving the data](#saving-the-data)
  - [Editing the data file](#editing-the-data-file)

## Command format conventions

- Words in `UPPER_CASE` are parameters that you must replace with your own values.
- Dates must use `DD-MM-YYYY` or `DD/MM/YYYY`, for example `31-10-2026` or `31/10/2026`.
- Optional times must appear after their date and use 24-hour `HH:mm`, for example `18:30`.
- Potato does not display a time of `00:00`, because a date without a displayed time is treated as midnight on that date.
- `TASK_NUMBER` is the positive integer shown beside a task by the `list` command.
- Command words are not case-sensitive. Enter `/by`, `/from`, and `/to` exactly as shown in the command formats.
- Descriptions are stored exactly as entered, and description searches are case-sensitive.

## Features

### Adding a to-do: `todo`

Adds a task that does not have an associated date.

Format: `todo DESCRIPTION`

- `DESCRIPTION` must not be blank.
- The new to-do is added to the end of the task list as incomplete.

Examples:

- `todo read book` adds a to-do named `read book`.
- `todo buy groceries` adds a to-do named `buy groceries`.

### Adding a deadline: `deadline`

Adds a task that must be completed by a particular date.

Format: `deadline DESCRIPTION /by DATE [TIME]`

- `DESCRIPTION` and `DATE` must not be blank.
- `DATE` must be a valid calendar date in one of Potato's supported date formats.
- `TIME` is optional. When provided, it must use the 24-hour `HH:mm` format.
- Entering `00:00` has the same meaning as omitting `TIME`: "by a particular date" means by midnight on that date, so `00:00` is not displayed.
- Potato displays saved dates as `DD MMM YYYY` and appends the time when provided.

Examples:

- `deadline return book /by 31-10-2026` adds a deadline due on 31 October 2026.
- `deadline submit report /by 15/11/2026 23:59` adds a deadline due at 23:59 on 15 November 2026.

### Adding an event: `event`

Adds a task with a start date and an end date.

Format: `event DESCRIPTION /from START_DATE [START_TIME] /to END_DATE [END_TIME]`

- `DESCRIPTION`, `START_DATE`, and `END_DATE` must not be blank.
- Both dates must be valid calendar dates in one of Potato's supported date formats.
- `START_TIME` and `END_TIME` are independently optional and must use 24-hour `HH:mm` when provided.
- Potato displays each saved date as `DD MMM YYYY` and appends its time when provided.

Examples:

- `event project meeting /from 01/11/2026 09:00 /to 02/11/2026 17:30` adds a timed project meeting.
- `event study break /from 10-12-2026 /to 12-12-2026` adds an event from 10 to 12 December 2026.

### Listing all tasks: `list`

Shows every saved task in the order it was added, together with its task number, type, completion state, and dates where applicable.

Format: `list`

- Use the displayed task numbers with `mark`, `unmark`, and `delete`.
- Task numbers can change after a task is deleted, so run `list` again before using a number if you are unsure.

Example:

- `list` displays all saved tasks.

### Finding tasks: `find`

Shows tasks whose descriptions contain the search text, or whose deadline, start date, or end date matches the search date.

Format: `find SEARCH_TEXT`

- `SEARCH_TEXT` must not be blank.
- Description matching is case-sensitive and can match part of a description.
- To search by date, enter the complete date in one of Potato's supported date formats.
- Search results are temporary and do not change the saved task list.
- Numbers in the search results are result positions only. Use `list` to obtain task numbers for `mark`, `unmark`, or `delete`.

Examples:

- `find project` finds tasks containing the text `project` in their descriptions.
- `find 02/11/2026` finds deadlines due on that date and events that start or end on that date.

### Marking a task as done: `mark`

Marks the specified task as completed.

Format: `mark TASK_NUMBER`

- `TASK_NUMBER` must be a positive integer that identifies an existing task in the full list.
- The completion indicator changes from `[ ]` to `[X]`.

Examples:

- `list` followed by `mark 2` marks the second task in the full task list as done.
- `list` followed by `mark 1` marks the first task in the full task list as done.

### Marking a task as not done: `unmark`

Marks the specified task as incomplete again.

Format: `unmark TASK_NUMBER`

- `TASK_NUMBER` must be a positive integer that identifies an existing task in the full list.
- The completion indicator changes from `[X]` to `[ ]`.

Examples:

- `list` followed by `unmark 2` marks the second task in the full task list as not done.
- `list` followed by `unmark 1` marks the first task in the full task list as not done.

### Deleting a task: `delete`

Permanently removes the specified task from Potato.

Format: `delete TASK_NUMBER`

- `TASK_NUMBER` must be a positive integer that identifies an existing task in the full list.
- Tasks after the deleted task are renumbered automatically.

Examples:

- `list` followed by `delete 2` deletes the second task in the full task list.
- `list` followed by `delete 1` deletes the first task in the full task list.

### Exiting Potato: `bye`

Closes Potato after displaying a farewell message. Your task changes have already been saved automatically.

Format: `bye`

Example:

- `bye` exits Potato.

### Saving the data

Potato automatically saves your task list after every command that adds, marks, unmarks, or deletes a task. You do not need to save your data manually.

### Editing the data file

Potato stores your task list in the text file `data/potato.txt`, relative to the folder from which you run the application. Advanced users may edit this file directly while Potato is not running.

> [!CAUTION]
> Back up `data/potato.txt` before editing it. If a line has an invalid format, Potato displays a warning and skips that line when it next starts. The skipped task will be lost from the file after the next command that changes the task list. Edit the data file only if you are confident that you can preserve its format correctly.
