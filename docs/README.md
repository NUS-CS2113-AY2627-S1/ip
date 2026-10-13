# Aglio User Guide

Aglio is a **command-line task manager** that helps you keep track of todos, deadlines, and events.

## Quick Start

1. Ensure you have **Java 25** installed.
2. Download the latest `aglio.jar` from the releases page.
3. Open a terminal, navigate to the folder containing `aglio.jar`, and run:

   ```shell
   java -jar aglio.jar
   ```

4. You should see the Aglio greeting. Type a command and press Enter.
5. Try these commands to get started:
   - `todo read book` — adds a todo
   - `list` — shows all tasks
   - `mark 1` — marks task 1 as done
   - `bye` — exits the app

Refer to the [Features](#features) section below for details on each command.

## Features

> **Notes about command format:**
> - Words in `UPPER_CASE` are parameters you supply.
>   e.g. in `todo DESCRIPTION`, `DESCRIPTION` is a parameter: `todo read book`.
>
> - Commands are case-sensitive and must be in lowercase.

### Listing all tasks: `list`

Shows all tasks in your list, numbered starting from 1.

Format: `list`

### Adding a todo: `todo`

Adds a task with no date attached.

Format: `todo DESCRIPTION`

Examples:

- `todo read book`
- `todo buy groceries`

### Adding a deadline: `deadline`

Adds a task with a due date.

Format: `deadline DESCRIPTION /by DATE`

- `DATE` must be in **yyyy-MM-dd** format (e.g. `2019-10-15`).
- The date is displayed in a friendlier format (e.g. `Oct 15 2019`).

Examples:

- `deadline return book /by 2019-10-15`
- `deadline submit report /by 2024-03-01`

### Adding an event: `event`

Adds a task with a start and end time.

Format: `event DESCRIPTION /from START /to END`

- `START` and `END` are free-form text (e.g. `Mon 2pm`, `Aug 6 4pm`).

Examples:

- `event project meeting /from Mon 2pm /to 4pm`
- `event concert /from Aug 6 7pm /to 10pm`

### Marking a task as done: `mark`

Marks the specified task as done.

Format: `mark TASK_NUMBER`

- `TASK_NUMBER` is the number shown in `list`.

Example: `mark 1`

### Marking a task as not done: `unmark`

Marks the specified task as not done.

Format: `unmark TASK_NUMBER`

Example: `unmark 1`

### Deleting a task: `delete`

Removes the specified task from the list. Remaining tasks are renumbered automatically.

Format: `delete TASK_NUMBER`

Example: `delete 2`

### Finding tasks by keyword: `find`

Searches for tasks whose descriptions contain the given keyword. The search is case-insensitive.

Format: `find KEYWORD`

Example: `find book` — matches "read book", "return book", etc.

### Exiting the program: `bye`

Exits Aglio.

Format: `bye`

### Saving data

Aglio automatically saves your tasks to `data/aglio.txt` after every change. There is no need to save manually.

On the next launch, your tasks are loaded automatically. If the save file is missing or corrupted, Aglio starts with an empty task list.

## Command Summary

| Action | Format |
| ------ | ------ |
| List | `list` |
| Todo | `todo DESCRIPTION` |
| Deadline | `deadline DESCRIPTION /by DATE` |
| Event | `event DESCRIPTION /from START /to END` |
| Mark | `mark TASK_NUMBER` |
| Unmark | `unmark TASK_NUMBER` |
| Delete | `delete TASK_NUMBER` |
| Find | `find KEYWORD` |
| Exit | `bye` |
