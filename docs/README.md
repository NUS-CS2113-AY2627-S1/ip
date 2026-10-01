# Goober User Guide

Goober is a command-line task manager for keeping track of things to do, deadlines, and events. Type a command and press **Enter**. Commands are case-insensitive. Task numbers shown by Goober are used to mark, unmark, or delete tasks.

## Quick start

Run Goober from IntelliJ by running `goober.Goober`, or build and run the project with Java 25. Goober creates `data/goober.txt` in its working directory and saves your tasks there; saved tasks are loaded the next time you start the app.

Type `help` at any time to see the available commands. Type `bye` to quit.

## Features

### Add tasks

Add a todo when there is no specific date or time:

```
todo <description>
```

Example: `todo Buy groceries`

Add a deadline with its due date and time:

```
deadline <description> /by <date> <time>
```

Example: `deadline Submit report /by 2/12/2026 1800`

Add an event with a start and end date and time:

```
event <description> /from <date> <time> /to <date> <time>
```

Example: `event Project meeting /from 2/12/2026 1400 /to 2/12/2026 1500`

Enter dates as `d/M/yyyy` and times as 24-hour `HHmm`. For example, `2/12/2026 0905` means December 2, 2026 at 9:05 AM. Deadlines and events display their date and time in a friendly format. Deadlines past their due time are marked **OVERDUE**; events show whether they are **UPCOMING**, **ONGOING**, or **ENDED**.

### View tasks

Use `list` to view all tasks. Goober groups them by type; deadlines are ordered by due time, and events by start time. To view one type, use `list todos`, `list deadlines`, or `list events`.

Use `list today` to see deadlines due today and events occurring today. To view a different date, use `list <date>`, for example `list 2/12/2026`. Date filters include an event if the selected date falls anywhere between its start and end dates.

Filtered lists keep each task's original task number, so the number can still be used with the commands below.

### Find tasks

Search task descriptions with `find <keyword>`, for example `find report`. Matching ignores capitalization and can find the keyword anywhere in a description. Goober shows the original task numbers for matches.

### Mark a task done or not done

Use the task number shown in a list or search result:

```
mark <task number>
unmark <task number>
```

For example, `mark 2` marks task 2 done, and `unmark 2` changes it back to not done. The task's status is saved automatically.

### Delete a task

Use `delete <task number>` to remove a task, for example `delete 2`. Goober confirms which task was removed and saves the updated list.

### Get help and quit

Type `help` to display the command summary. Type `bye` to close Goober. Your tasks are saved as you add, mark, unmark, or delete them, and are restored on the next launch.

If a command name is misspelled by one character, Goober may suggest the closest valid command. This suggestion uses Levenshtein distance: the number of single-character insertions, deletions, or substitutions needed to change one word into another. For example, `delet 2` may prompt you with `Did you mean "delete"?`; enter the corrected command yourself.

## Command summary

| Command | What it does |
| --- | --- |
| `todo <description>` | Add a task without a date. |
| `deadline <description> /by <d/M/yyyy HHmm>` | Add a task with a due date and time. |
| `event <description> /from <d/M/yyyy HHmm> /to <d/M/yyyy HHmm>` | Add an event with a start and end. |
| `list` | Show all tasks, grouped by type. |
| `list todos` / `list deadlines` / `list events` | Show only one type of task. |
| `list today` | Show deadlines and events occurring today. |
| `list <d/M/yyyy>` | Show deadlines and events on a date. |
| `find <keyword>` | Find tasks whose descriptions contain a keyword. |
| `mark <task number>` | Mark a task done. |
| `unmark <task number>` | Mark a task not done. |
| `delete <task number>` | Delete a task. |
| `help` | Show command help. |
| `bye` | Quit Goober. |
