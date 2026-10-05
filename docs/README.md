# Goat Bot User Guide

Goat Bot is a command-line task manager for keeping track of todos, deadlines, and events. It saves every change automatically and restores your tasks the next time it starts.

## Quick start

1. Install Java 25.
2. Download `goatbot.jar` and place it in a folder of your choice.
3. Open a terminal in that folder and run:

   ```shell
   java -jar goatbot.jar
   ```

4. Type a command and press <kbd>Enter</kbd>.

**Note:** Commands must be entered in lowercase. Task numbers are shown by the `list` command.

## Date and time format

Use `d/M/yyyy HHmm` for a date and time. For example, `2/12/2019 1800` means 2 December 2019 at 6:00 PM.

Use `d/M/yyyy` when searching by date. For example, `2/12/2019` means 2 December 2019.

## Commands

### Add a todo

Adds a task without a date or time.

```text
todo DESCRIPTION
```

Example: `todo read book`

### Add a deadline

Adds a task that must be completed by a specific time.

```text
deadline DESCRIPTION /by DATE_TIME
```

Example: `deadline return book /by 2/12/2019 1800`

### Add an event

Adds an event with a start and end time. The end time cannot be before the start time.

```text
event DESCRIPTION /from START_DATE_TIME /to END_DATE_TIME
```

Example: `event project meeting /from 6/8/2026 1400 /to 6/8/2026 1600`

### View all tasks

Displays every task and its task number.

```text
list
```

### Mark a task as done

```text
mark TASK_NUMBER
```

Example: `mark 2`

### Mark a task as not done

```text
unmark TASK_NUMBER
```

Example: `unmark 2`

### Delete a task

```text
delete TASK_NUMBER
```

Example: `delete 2`

### Find tasks by keyword

Displays tasks whose descriptions contain the given keyword. The search is case-sensitive.

```text
find KEYWORD
```

Example: `find book`

### Find tasks by date

Displays deadlines due on the given date and events occurring on that date.

```text
on DATE
```

Example: `on 2/12/2019`

### Exit Goat Bot

```text
bye
```

Your tasks are stored automatically in `data/goatbot.txt` whenever you add, mark, unmark, or delete a task.
