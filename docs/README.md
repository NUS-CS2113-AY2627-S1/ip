# Alfred User Guide

Alfred is a command-line chatbot that helps you manage todos, deadlines, and events. It is designed for fast keyboard-based use and saves your tasks automatically between sessions.

- [Quick start](#quick-start)
- [Features](#features)
  - [Add a task: `todo`](#add-a-task-todo)
  - [Add a deadline: `deadline`](#add-a-deadline-deadline)
  - [Add an event: `event`](#add-an-event-event)
  - [List all tasks: `list`](#list-all-tasks-list)
  - [Mark a task as done: `mark`](#mark-a-task-as-done-mark)
  - [Mark a task as not done: `unmark`](#mark-a-task-as-not-done-unmark)
  - [Delete a task: `delete`](#delete-a-task-delete)
  - [View a schedule: `schedule`](#view-a-schedule-schedule)
  - [Find tasks: `find`](#find-tasks-find)
  - [Exit Alfred: `bye`](#exit-alfred-bye)
  - [Saving data](#saving-data)
- [FAQ](#faq)
- [Known issues](#known-issues)
- [Command summary](#command-summary)

---

## Quick start

1. Ensure that Java 25 is installed on your computer.
2. Download the latest `alfred.jar` from the [repository's Releases page](https://github.com/DaynaG3/ip/releases).
3. Move `alfred.jar` into the folder you want to use as Alfred's home folder.
4. Open a terminal in that folder and run:

   ```shell
   java -jar alfred.jar
   ```

5. Type a command and press <kbd>Enter</kbd> to execute it. Here are a few commands to try:

   - `todo read book` — adds a todo.
   - `deadline return book /by 2026-10-02 17:30` — adds a deadline with a time.
   - `list` — displays all tasks.
   - `mark 1` — marks the first task as done.
   - `bye` — exits Alfred.

6. Refer to [Features](#features) for details of every command.

---

## Features

Notes about the command formats used in this guide:

- Words in `UPPER_CASE` are values that you supply. For example, replace `DESCRIPTION` with `read book`.
- Items in square brackets are optional. For example, `[HH:mm]` means that a time may be omitted.
- Dates must use `yyyy-MM-dd`, such as `2026-10-02`.
- Times must use the 24-hour `HH:mm` format, such as `09:05` or `17:30`.
- `TASK_NUMBER` is the number shown beside a task by the `list` command. Task numbers start at `1`.
- Command words are case-sensitive and must be typed in lowercase.

### Add a task: `todo`

Adds a todo without a date or time.

Format: `todo DESCRIPTION`

Example: `todo read book`

### Add a deadline: `deadline`

Adds a task that must be completed by a particular date, with an optional time.

Formats:

- `deadline DESCRIPTION /by yyyy-MM-dd`
- `deadline DESCRIPTION /by yyyy-MM-dd HH:mm`

Examples:

- `deadline return book /by 2026-10-02`
- `deadline submit report /by 2026-10-02 17:30`

> [!NOTE]
> Use `/by` exactly once, after the description.

### Add an event: `event`

Adds an event with a start and an end. The endpoints may both include a time or both omit it.

Formats:

- `event DESCRIPTION /from yyyy-MM-dd /to yyyy-MM-dd`
- `event DESCRIPTION /from yyyy-MM-dd HH:mm /to yyyy-MM-dd HH:mm`

Examples:

- `event conference /from 2026-10-02 /to 2026-10-04`
- `event team meeting /from 2026-10-02 14:00 /to 2026-10-02 16:00`

> [!NOTE]
> `/from` must appear before `/to`, and the event cannot end before it starts.

### List all tasks: `list`

Displays every task together with its task number and completion status.

Format: `list`

### Mark a task as done: `mark`

Marks the specified task as completed.

Format: `mark TASK_NUMBER`

Example: `mark 2`

### Mark a task as not done: `unmark`

Marks the specified task as incomplete.

Format: `unmark TASK_NUMBER`

Example: `unmark 2`

### Delete a task: `delete`

Permanently removes the specified task. The remaining tasks are renumbered automatically.

Format: `delete TASK_NUMBER`

Example: `delete 3`

> [!TIP]
> Run `list` before `delete` to check the task number.

### View a schedule: `schedule`

Displays deadlines due on a date and events occurring on that date. An event is included on every date from its start through its end, inclusive. Todos are not included.

Format: `schedule yyyy-MM-dd`

Example: `schedule 2026-10-02`

### Find tasks: `find`

Displays tasks whose descriptions contain the given search text.

Format: `find SEARCH_TEXT`

Examples:

- `find book`
- `find team meeting`

The search:

- is case-insensitive, so `BOOK` matches `book`;
- matches partial text, so `read` matches `reading`;
- accepts multiple words as one search phrase;
- checks descriptions only, not dates or times.

### Exit Alfred: `bye`

Exits Alfred.

Format: `bye`

### Saving data

Alfred automatically saves changes after adding, marking, unmarking, or deleting a task. Data is stored in `data/alfred.txt`, relative to the folder from which Alfred was started. There is no manual save command.

> [!CAUTION]
> Avoid editing `data/alfred.txt` manually. Invalid records cannot be loaded, and the text ` | ` is reserved as Alfred's field separator and cannot be used in task descriptions.

---

## FAQ

**Q: How do I transfer my tasks to another computer?**

A: Install Alfred on the other computer, then copy `data/alfred.txt` from the old Alfred home folder into the new one's `data` folder.

**Q: Where did Alfred save my tasks?**

A: Alfred saves them in `data/alfred.txt`, relative to the folder where you ran `java -jar alfred.jar`.

**Q: How do I add a deadline or event without a time?**

A: Enter only the date. For example, use `deadline submit report /by 2026-10-02`.

**Q: Why does `schedule` not show my todo?**

A: Todos do not have dates. `schedule` displays only deadlines and events associated with the requested date.

**Q: Is deleting a task reversible?**

A: No. Check the task number with `list` before using `delete`.

---

## Known issues

- Command words must use lowercase. For example, `list` works but `List` is treated as an unknown command.
- Alfred stores its data relative to the terminal's current folder. Starting the JAR from a different folder creates or loads a different `data/alfred.txt` file.

---

## Command summary

| Action | Format | Example |
| --- | --- | --- |
| Add task | `todo DESCRIPTION` | `todo read book` |
| Add deadline | `deadline DESCRIPTION /by yyyy-MM-dd [HH:mm]` | `deadline return book /by 2026-10-02 17:30` |
| Add event | `event DESCRIPTION /from yyyy-MM-dd [HH:mm] /to yyyy-MM-dd [HH:mm]` | `event meeting /from 2026-10-02 14:00 /to 2026-10-02 16:00` |
| List tasks | `list` | `list` |
| Mark as done | `mark TASK_NUMBER` | `mark 2` |
| Mark as not done | `unmark TASK_NUMBER` | `unmark 2` |
| Delete task | `delete TASK_NUMBER` | `delete 3` |
| View schedule | `schedule yyyy-MM-dd` | `schedule 2026-10-02` |
| Find tasks | `find SEARCH_TEXT` | `find book` |
| Exit | `bye` | `bye` |
