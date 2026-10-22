# CS2113_Bot User Guide

CS2113_Bot is a command-line task manager for keeping track of todos, deadlines, and events. Type a command, press Enter, and the chatbot will confirm the result or explain how to correct an invalid command.

## Quick start

1. Run `CS2113_Bot` in your IDE.
2. Add a task with `todo read chapter 1`.
3. Enter `list` to view your tasks.
4. Enter `bye` when you are finished.

Successful task changes are saved automatically in `data/duke.txt` and loaded when you start the chatbot again.

## Features

### Add a todo

Adds a task without a date or time.

Format: `todo DESCRIPTION`

Example:

```text
todo read chapter 1
```

### Add a deadline

Adds a task with a due date or time.

Format: `deadline DESCRIPTION /by DATE`

Example:

```text
deadline submit assignment /by Friday 5pm
```

### Add an event

Adds a task that has a start and end time.

Format: `event DESCRIPTION /from START /to END`

Example:

```text
event project meeting /from Monday 2pm /to Monday 3pm
```

### List tasks

Shows all current tasks with their task numbers and completion status.

Format: `list`

### Mark or unmark a task

Marks a task as complete, or changes it back to incomplete. Replace `TASK_NUMBER` with the number shown by `list`.

Formats:

```text
mark TASK_NUMBER
unmark TASK_NUMBER
```

Examples:

```text
mark 1
unmark 1
```

### Delete a task

Removes a task permanently. Replace `TASK_NUMBER` with the number shown by `list`.

Format: `delete TASK_NUMBER`

Example:

```text
delete 2
```

### Exit the chatbot

Closes the chatbot. Your successful task changes have already been saved automatically.

Format: `bye`

## Handling errors

Use each command in the format shown above. CS2113_Bot will explain problems such as a missing task description, a missing deadline date, an invalid task number, or an unknown command. Correct the command and enter it again.
