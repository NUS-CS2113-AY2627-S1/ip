# Cracker User Guide

Cracker is a command-line task manager that helps you keep track of to-dos,
deadlines, and events. Your tasks are saved automatically, so they are available
the next time you start the application.

## Quick start

1. Ensure that Java 25 is installed.
1. Build the application from the project root:

   ```text
   .\gradlew.bat shadowJar
   ```

1. Run the generated JAR:

   ```text
   java -jar build/libs/cracker.jar
   ```

1. Enter a command, then press Enter. For example:

   ```text
   todo read book
   ```

1. Enter `bye` to exit.

## Features

### Add a to-do: `todo`

Adds a task without a date or time.

**Format:** `todo DESCRIPTION`

**Example:** `todo read book`

### Add a deadline: `deadline`

Adds a task with a due time. Cracker preserves the due time as the text you
enter, so you can use any clear format.

**Format:** `deadline DESCRIPTION /by DUE_TIME`

**Example:** `deadline return book /by Sunday`

### Add an event: `event`

Adds a task with a start and end time. Times are stored as the text you enter.

**Format:** `event DESCRIPTION /from START_TIME /to END_TIME`

**Example:** `event project meeting /from Mon 2pm /to 4pm`

### List tasks: `list`

Displays every saved task. Tasks are numbered starting from 1. `[X]` marks a
completed task and `[ ]` marks an incomplete task; `[T]`, `[D]`, and `[E]`
identify to-dos, deadlines, and events respectively.

**Format:** `list`

### Mark a task as done: `mark`

Marks the specified task as completed.

**Format:** `mark TASK_NUMBER`

**Example:** `mark 2`

### Mark a task as not done: `unmark`

Changes a completed task back to incomplete.

**Format:** `unmark TASK_NUMBER`

**Example:** `unmark 2`

### Delete a task: `delete`

Removes the specified task. The remaining tasks are renumbered.

**Format:** `delete TASK_NUMBER`

**Example:** `delete 3`

### Find tasks: `find`

Shows tasks whose descriptions contain the given keyword. Searches are
case-insensitive.

**Format:** `find KEYWORD`

**Example:** `find book`

### Exit Cracker: `bye`

Ends the application. Tasks are already saved when you add, mark, unmark, or
delete them.

**Format:** `bye`

## Saving data

Cracker saves tasks to `data/duke.txt` relative to the directory where you run
the application. It reloads this file on startup. If a saved line is corrupted,
Cracker ignores that line and continues loading the remaining valid tasks.

## Command summary

| Command | Purpose |
| --- | --- |
| `todo DESCRIPTION` | Add a to-do task. |
| `deadline DESCRIPTION /by DUE_TIME` | Add a deadline. |
| `event DESCRIPTION /from START_TIME /to END_TIME` | Add an event. |
| `list` | Display all tasks. |
| `mark TASK_NUMBER` | Mark a task as done. |
| `unmark TASK_NUMBER` | Mark a task as not done. |
| `delete TASK_NUMBER` | Delete a task. |
| `find KEYWORD` | Find tasks by description. |
| `bye` | Exit the application. |
