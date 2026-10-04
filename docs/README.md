# Dook User Guide

Dook is a desktop application for managing tasks, optimized for use through a Command Line Interface (CLI)

## Features

> [!NOTE]
> Notes about the command format:
> * Words in `UPPER_CASE` are the parameters to be supplied by the user.
> * Every command is matched case-sensitively at the start of the line. Trailing whitespace is trimmed.
> * Extraneous parameters are not supported.
> * Multi-line inputs are not supported.

### Viewing help: `help`

Shows a message describing every available command and its format.

Format: `help`

### Adding a to-do task: `todo`

Adds a basic task with no time attachment to the task list.

Format: `todo DESCRIPTION`

* `DESCRIPTION` must be a non-empty phrase.
* If `DESCRIPTION` is missing, the input is not recognized as a `todo` and an unknown-command message is shown.

Examples:
* `todo read chapter 3`
* `todo buy groceries`

### Adding a deadline task: `deadline`

Adds a task with a due date/time to the task list.

Format: `deadline DESCRIPTION /by DATE_TIME`

* `DESCRIPTION` must be a non-empty phrase.
* `DATE_TIME` is parsed by a flexible date-time parser (see [Accepted date-time formats](#accepted-date-time-formats)).
* If the time is omitted, it defaults to `23:59:59` on the given date.
* If `DATE_TIME` cannot be parsed, the input is rejected.
* The `/by` keyword must be typed literally. Both `DESCRIPTION` and `DATE_TIME` may contain spaces.

Examples:
* `deadline submit report /by 2025-12-31 2359`
* `deadline lab quiz /by Fri 5pm`
* `deadline pay rent /by 1 Jan 2026`

### Adding an event task: `event`

Adds a task with a start and end time to the task list.

Format: `event DESCRIPTION /from START_DATE_TIME /to END_DATE_TIME`

* `DESCRIPTION` must be a non-empty phrase.
* Both `START_DATE_TIME` and `END_DATE_TIME` are parsed by the flexible date-time parser.
* If `END_DATE_TIME` has no date, the date of `START_DATE_TIME` is assumed.
* The event is rejected if `END_DATE_TIME` is strictly before `START_DATE_TIME`.
* If the start time has no time component, it defaults to `00:00:00`. If the end time has no time component, it defaults to `23:59:59`.

Examples:
* `event CS2103 lecture /from Mon 10am /to Mon 12pm`
* `event project meeting /from 2025-11-01 1400 /to 2025-11-01 1600`
* `event conference /from 2025-11-01 /to 2025-11-03`

### Listing all tasks: `list`

Shows a numbered list of all tasks.

Format: `list`

* Tasks are shown in insertion order.
* Task type is shown in square brackets: `[T]` for to-do, `[D]` for deadline, `[E]` for event.
* The completion status is shown next: `[X]` if done, `[ ]` if not done.
* Deadline and event tasks also show their date/time.
* If the task list is empty, an error message is shown instead of an empty list.

### Marking a task as done: `mark`

Marks the specified task as done.

Format: `mark INDEX`

* Marks the task at the specified `INDEX`. The index refers to the number shown in the displayed task list.
* The index must be a positive integer 1, 2, 3, ...
* If the task is already marked done, a special message is shown and no change is made.

Examples:
* `list` followed by `mark 2` marks the 2nd task as done.

### Marking a task as not done: `unmark`

Marks the specified task as not done.

Format: `unmark INDEX`

* Unmarks the task at the specified `INDEX`.
* The index must be a positive integer 1, 2, 3, ...
* If the task is already not done, a special message is shown and no change is made.

Examples:
* `unmark 3` marks the 3rd task as not done.

### Locating tasks by description: `find`

Finds tasks whose descriptions fuzzy-match the search phrase.

Format: `find SEARCH_PHRASE`

* The search is case-insensitive.
* The search is fuzzy: it uses the Damerau–Levenshtein (optimal string alignment) distance, with a maximum edit distance of 5.
* When an exact substring match is found (edit distance 0), only those tasks are returned.
* Otherwise, Dook finds the smallest edit distance at which at least one task matches and returns those tasks.
* Multi-word queries match if every query token is within the edit distance of some task token.
* If the task list is empty, an error message is shown.
* If no task matches, a "no such task" message is shown.

Examples:
* `find report` returns tasks containing `report`.
* `find repotr` returns tasks containing `report` (typo tolerated).
* `find cs2113 lecture` returns tasks containing both `cs2113` and `lecture` within the edit distance.

### Deleting a task: `delete`

Deletes the specified task from the task list.

Format: `delete INDEX`

* Deletes the task at the specified `INDEX`.
* The index refers to the number shown in the displayed task list.
* The index must be a positive integer 1, 2, 3, ...

Examples:
* `list` followed by `delete 2` deletes the 2nd task in the list.
* `find report` followed by `delete 1` deletes the 1st task in the results of the `find` command.

### Deleting all tasks: `delete all`

Clears the entire task list.

Format: `delete all`

### Deleting expired tasks: `delete expired`

Removes all tasks whose time has passed.

Format: `delete expired`

* A deadline task is expired if its deadline is before the current time.
* An event task is expired if its end time is before the current time.
* A to-do task never expires.

### Undoing the previous command: `undo`

Reverses the effect of the most recent command.

Format: `undo`

* Only the most recent command is undone. Repeated `undo` calls step further back through history.
* `undo` itself is not undoable.
* If there is no command to undo, a special message is shown.

### Viewing the monthly calendar: `calendar`

Displays a calendar of the current month with task density indicators.

Format: `calendar`

* Each day is shown with a density symbol based on the number of tasks on that day:
  * ` ` — no tasks
  * `░` — 1 task
  * `▒` — 2 tasks
  * `▓` — 3 tasks
  * `█` — 4 or more tasks
* Weeks start on Monday.

Example output:
```
> calendar
         OCTOBER 2026
  Mo  Tu  We  Th  Fr  Sa  Su
               1   2░  3   4░
   5▓  6░  7▒  8   9  10  11
  12  13░ 14  15▓ 16▒ 17  18
  19  20  21  22  23  24  25░
  26  27▒ 28▒ 29  30  31█
  ```

### Viewing today's tasks: `today`

Shows all tasks occurring today, sorted by time.

Format: `today`

* To-do tasks are always shown (they have no fixed date).
* Deadline tasks are shown if their deadline falls on today.
* Event tasks are shown if today falls within their start and end dates.

### Viewing tasks on a specific date: `date`

Shows all tasks occurring on the specified date, sorted by time.

Format: `date DATE`

* `DATE` is parsed by the flexible date-time parser. Only the date component is used.
* If no tasks occur on that date, a "no tasks found" message is shown.

Examples:
* `date 2025-12-31`
* `date 1 Jan 2026`
* `date Fri`

### Exiting the program: `bye`

Exits the program.

Format: `bye`

* Any input starting with `bye` is treated as the exit command.

### Saving the data

Dook saves data automatically after every command that changes the task list. You do not need to save manually.

Data is written atomically: a temporary file is created and then moved into place, so a crash mid-write will not corrupt your existing data.

### Editing the data file

Dook data is saved automatically as a plain-text file `[project root]/data/task.txt`, alongside a `data/metadata.txt` file that records the serialisation version. To transfer the data, copy the entire `data/` folder.

The format of Dook data is versioned, and backwards compatibility is supported. You can run Dook with data format that has been outdated.

Editing the file by hand is safe only if you keep the format consistent with the serialisation version recorded in `metadata.txt`. If the file is malformed, Dook will log an error and start with an empty task list at the next run.

### Viewing logs

A rolling log file is written to `[project root]/logs/dook.log` on every run. If something behaves unexpectedly, check that file for details.

---

## Accepted date-time formats

Dook uses a flexible parser so you can enter dates and times in a wide variety of ways. All formats below are accepted for both deadlines and events (with the defaults described above).

**Date only**
* ISO: `2025-12-31`
* Day-first with any of `-`, `/`, or `.`: `31-12-2025`, `31/12/2025`, `31.12.2025`
* Year-first: `2025-12-31`, `2025/12/31`, `2025.12.31`
* Month name: `31 Dec 2025`, `31 December 2025`, `Dec 31 2025`, `Dec 31, 2025`
* Two-digit year: `31/12/25`
* Day of week: `Fri`, `Friday`, `Fri 31 Dec 2025`

**Time only**
* 24-hour: `14:00`, `14`, `1400`
* 12-hour: `2pm`, `2 pm`, `2:00pm`, `2:00 PM`
* Seconds optional: `14:30:15`
* With or without `h`/`H` separator: `14h30`, `14H30`

**Combined**
* ISO date-time: `2025-12-31T14:30`
* With `T` or `at` or a space: `2025-12-31T14:30`, `2025-12-31 at 14:30`, `2025-12-31 14:30`
* Day of week first: `Fri 14:00`, `Friday 2pm`
* ISO offset / zoned / instant forms are also accepted.

**Defaults**

| Context | Missing part | Default |
|---|---|---|
| Start time (event, date command) | Time | `00:00:00` |
| End time (deadline, event) | Time | `23:59:59` |
| Any date-time | Year | Current year, or next year if the date has already passed |
| Any date-time | Date (e.g. just `14:00`) | Today |

---

## Command summary

Action | Format, Examples
--------|------------------
**Add to-do** | `todo DESCRIPTION`<br> e.g., `todo read chapter 3`
**Add deadline** | `deadline DESCRIPTION /by DATE_TIME`<br> e.g., `deadline submit report /by 2025-12-31 2359`
**Add event** | `event DESCRIPTION /from START_DATE_TIME /to END_DATE_TIME`<br> e.g., `event CS2103 lecture /from Mon 10am /to Mon 12pm`
**List** | `list`
**Mark done** | `mark INDEX`<br> e.g., `mark 2`
**Mark not done** | `unmark INDEX`<br> e.g., `unmark 3`
**Find** | `find SEARCH_PHRASE`<br> e.g., `find report`
**Delete** | `delete INDEX`<br> e.g., `delete 2`
**Delete all** | `delete all`
**Delete expired** | `delete expired`
**Undo** | `undo`
**Calendar** | `calendar`
**Today** | `today`
**Date** | `date DATE`<br> e.g., `date 2025-12-31`
**Help** | `help`
**Exit** | `bye`
