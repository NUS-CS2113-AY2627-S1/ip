# Yao - User Guide

By: `Team Yao` &nbsp;&nbsp;&nbsp;&nbsp; Since: `Sep 2026` &nbsp;&nbsp;&nbsp;&nbsp; Licence: `MIT`

---

Yao is a **command-line task management application** designed to help you track your daily tasks, deadlines, and events through intuitive text commands. If you can type fast, Yao allows you to manage tasks much faster than traditional GUI-based task trackers.

* Table of Contents
{:toc}

----------------------------------------------------------------------------------------------------

## Quick Start

1. Ensure that you have **Java 25** or later installed on your computer.
2. Download or clone this repository:
   ```bash
   git clone https://github.com/bohao12/ip.git
   cd ip
   ```
3. Run the application using the Gradle wrapper:
   * **Windows:** `gradlew.bat run`
   * **macOS / Linux:** `./gradlew run`
4. Yao will launch and greet you with the welcome banner.
5. Type your command in the terminal and press **Enter** to execute it. Refer to the [Features](#features) section below for details of each command.

----------------------------------------------------------------------------------------------------

## Features

> **Notes about the command format:**
> * Words in `<angle brackets>` are the parameters to be supplied by the user.
>   * E.g. in `todo <description>`, `<description>` is a parameter such as `read book`.
> * Task numbers refer to the index displayed in the `list` command (e.g. `1`, `2`).
> * Tasks are automatically saved to disk after every modifying command (`todo`, `deadline`, `event`, `mark`, `unmark`, `delete`).

### Adding a todo task: `todo`

Adds a to-do task without any date or time to the list.

Format: `todo <description>`

Examples:
* `todo read book`
* `todo buy groceries`

### Adding a deadline task: `deadline`

Adds a task with a deadline date/time to the list.

Format: `deadline <description> /by <date or time>`

Examples:
* `deadline return book /by Sunday`
* `deadline submit homework /by 2026-10-15 23:59`

### Adding an event task: `event`

Adds an event with a start time and an end time to the list.

Format: `event <description> /from <start time> /to <end time>`

Examples:
* `event project meeting /from Mon 2pm /to 4pm`
* `event orientation camp /from Friday /to Sunday`

### Listing all tasks: `list`

Shows a list of all current tasks recorded in Yao, including their task type (`[T]`, `[D]`, `[E]`) and completion status (`[X]` for done, `[ ]` for not done).

Format: `list`

### Marking a task as done: `mark`

Marks the task at the specified index as completed.

Format: `mark <task_number>`

Examples:
* `mark 2` — Marks the 2nd task in the list as completed.

### Unmarking a task: `unmark`

Marks the task at the specified index as incomplete.

Format: `unmark <task_number>`

Examples:
* `unmark 2` — Reverts the 2nd task in the list back to incomplete.

### Finding tasks by keyword: `find`

Finds all tasks whose descriptions contain the given keyword.

Format: `find <keyword>`

Examples:
* `find book` — Returns all tasks containing "book" in their description (e.g. `read book`, `return book`).

### Deleting a task: `delete`

Deletes the task at the specified index from the list.

Format: `delete <task_number>`

Examples:
* `delete 1` — Removes the 1st task in the list.

### Exiting the program: `bye`

Exits Yao. All tasks are automatically saved to file storage.

Format: `bye`

### Saving the data

Yao task data is saved in the local file `data/Yao.txt` automatically after any command that modifies the list. There is no need to save manually.

----------------------------------------------------------------------------------------------------

## FAQ

**Q:** How do I transfer my data to another computer?  
**A:** Copy the `data/` folder (specifically `data/Yao.txt`) to the project directory on your new computer.

----------------------------------------------------------------------------------------------------

## Command Summary

| Action | Format | Examples |
|---|---|---|
| **Todo** | `todo <description>` | `todo read book` |
| **Deadline** | `deadline <description> /by <time>` | `deadline return book /by Sunday` |
| **Event** | `event <description> /from <start> /to <end>` | `event project meeting /from Mon 2pm /to 4pm` |
| **List** | `list` | `list` |
| **Mark** | `mark <task_number>` | `mark 1` |
| **Unmark** | `unmark <task_number>` | `unmark 1` |
| **Find** | `find <keyword>` | `find book` |
| **Delete** | `delete <task_number>` | `delete 2` |
| **Exit** | `bye` | `bye` |