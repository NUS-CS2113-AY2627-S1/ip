# Foodielover - User Guide

**Foodielover** is a lightweight, desktop task management chatbot optimized for users who prefer working through a **Command Line Interface (CLI)**. Whether you are keeping track of course deadlines, club gatherings, or dinner reservations, Foodielover lets you manage your tasks faster than traditional point-and-click apps.

---

## Table of Contents

- [Quick Start](#quick-start)
- [Features](#features)
  - [Adding a ToDo task: `todo`](#adding-a-todo-task-todo)
  - [Adding a Deadline task: `deadline`](#adding-a-deadline-task-deadline)
  - [Adding an Event task: `event`](#adding-an-event-task-event)
  - [Listing all tasks: `list`](#listing-all-tasks-list)
  - [Marking a task as completed: `mark`](#marking-a-task-as-completed-mark)
  - [Marking a task as incomplete: `unmark`](#marking-a-task-as-incomplete-unmark)
  - [Finding tasks by keyword: `find`](#finding-tasks-by-keyword-find)
  - [Filtering tasks by date: `date` / `on`](#filtering-tasks-by-date-date--on)
  - [Deleting a task: `delete`](#deleting-a-task-delete)
  - [Exiting the program: `bye`](#exiting-the-program-bye)
  - [Automatic Data Persistence & Backup](#automatic-data-persistence--backup)
- [Command Summary](#command-summary)

---

## Quick Start

1. Ensure that you have **Java 25** installed on your computer.
2. Download the latest `foodielover.jar` from the [Releases](https://github.com/siewhean/ip/releases) page.
3. Place the JAR file in an empty folder you wish to use as Foodielover's home directory.
4. Open a terminal, navigate (`cd`) to that directory, and run:
   ```bash
   java -jar foodielover.jar
   ```
5. You will see the welcome banner:
   ```text
   ____________________________________________________________
    ______              _ _      _                            
   |  ____|            | (_)    | |                           
   | |__ ___   ___   __| |_  ___| | _____   _____ _ __        
   |  __/ _ \ / _ \ / _` | |/ _ \ |/ _ \ \ / / _ \ '__|       
   | | | (_) | (_) | (_| | |  __/ | (_) \ V /  __/ |          
   |_|  \___/ \___/ \__,_|_|\___|_|\___/ \_/ \___|_|          

   Hello! I'm Foodielover.
   What can I do for you?
   ____________________________________________________________
   ```
6. Type a command in the terminal and press **Enter** to execute it. For example, type `todo buy groceries` and press **Enter**.

---

## Features

> **Notes on Command Syntax:**
> - Words in `UPPER_CASE` represent parameters to be supplied by the user (e.g., in `todo DESCRIPTION`, `DESCRIPTION` is the task description).
> - Parameters must follow the specified flags (e.g., `/by`, `/from`, `/to`).
> - Commands are case-insensitive and tolerate leading/trailing whitespace (e.g., `LIST`, `list `, and `  list` all work).
> - The pipe character (`|`) is reserved as an internal storage separator and cannot be included in descriptions, dates, or times.

### Adding a ToDo task: `todo`

Adds a simple task without any deadline or specific time constraints.

- **Format:** `todo DESCRIPTION`
- **Example:**
  ```text
  todo buy baking ingredients
  ```
- **Expected Output:**
  ```text
  Got it. I've added this task:
    [T][ ] buy baking ingredients
  Now you have 1 task in the list.
  ```

### Adding a Deadline task: `deadline`

Adds a task that must be completed before a specified due date or time using the `/by` delimiter.

Foodielover recognizes standard date formats (such as `yyyy-MM-dd`, `d/M/yyyy`, `d-M-yyyy`, or `yyyy/M/d` with optional 24-hour time written as `HHmm`, `Hmm`, `HH:mm`, or `H:mm`, e.g. `1800`, `900`, `18:00`, or `9:00`), pretty-printing them upon display (e.g. `Oct 15 2026` or `Dec 02 2026, 6:00PM`). Natural phrases like `June 6th` or `tonight` are also accepted. Calendar dates are strictly validated, preventing impossible inputs like `2019-02-30`.

- **Format:** `deadline DESCRIPTION /by DUE_DATE_OR_TIME`
- **Examples:**
  ```text
  deadline submit lab report /by 2026-10-15
  deadline project milestone /by 2/12/2026 1800
  deadline return library book /by June 6th
  ```
- **Expected Output:**
  ```text
  Got it. I've added this task:
    [D][ ] submit lab report (by: Oct 15 2026)
  Now you have 2 tasks in the list.
  ```
  ```text
  Got it. I've added this task:
    [D][ ] project milestone (by: Dec 02 2026, 6:00PM)
  Now you have 3 tasks in the list.
  ```

### Adding an Event task: `event`

Adds a task that occurs within a specific time period using the `/from` and `/to` delimiters. When dates/times are provided for both, Foodielover checks that the end time is not earlier than the start time.

- **Format:** `event DESCRIPTION /from START_TIME /to END_TIME`
- **Examples:**
  ```text
  event international food fair /from 2026-10-15 0900 /to 2026-10-15 1700
  event cooking workshop /from 2026-10-14 /to 2026-10-16
  ```
- **Expected Output:**
  ```text
  Got it. I've added this task:
    [E][ ] international food fair (from: Oct 15 2026, 9:00AM to: Oct 15 2026, 5:00PM)
  Now you have 4 tasks in the list.
  ```

### Listing all tasks: `list`

Displays all current tasks in order with their type tag (`[T]`, `[D]`, `[E]`), completion status (`[X]` for done, `[ ]` for pending), and 1-based index numbers.

- **Format:** `list`
- **Example Output:**
  ```text
  Here are the tasks in your list:
  1.[T][ ] buy baking ingredients
  2.[D][ ] submit lab report (by: Oct 15 2026)
  3.[D][ ] project milestone (by: Dec 02 2026, 6:00PM)
  4.[E][ ] international food fair (from: Oct 15 2026, 9:00AM to: Oct 15 2026, 5:00PM)
  ```

### Marking a task as completed: `mark`

Marks the specified task as done (`[X]`).

- **Format:** `mark INDEX`
  - `INDEX` must be a positive integer corresponding to a valid 1-based index from the task list.
- **Example:** `mark 2`
- **Expected Output:**
  ```text
  Nice! I've marked this task as done:
    [D][X] submit lab report (by: Oct 15 2026)
  ```

### Marking a task as incomplete: `unmark`

Reverts the completion status of the specified task back to pending (`[ ]`).

- **Format:** `unmark INDEX`
- **Example:** `unmark 2`
- **Expected Output:**
  ```text
  OK, I've marked this task as not done yet:
    [D][ ] submit lab report (by: Oct 15 2026)
  ```

### Finding tasks by keyword: `find`

Searches task descriptions case-insensitively for the given keyword and displays all matching tasks.

- **Format:** `find KEYWORD`
- **Example:** `find report`
- **Expected Output:**
  ```text
  Here are the matching tasks in your list:
  1.[D][ ] submit lab report (by: Oct 15 2026)
  ```
- If no matching tasks are found, Foodielover informs you:
  ```text
  No matching tasks found in your list.
  ```

### Filtering tasks by date: `date` / `on`

Finds all deadlines and events that occur on or fall across a specific date. Both `date` and `on` keywords are supported.

- **Format:** `date DATE` or `on DATE`
  - Accepts dates in `yyyy-MM-dd`, `d/M/yyyy`, `d-M-yyyy`, or `yyyy/M/d` formats.
- **Example:** `date 2026-10-15`
- **Expected Output:**
  ```text
  Here are the tasks occurring on Oct 15 2026:
  1.[D][ ] submit lab report (by: Oct 15 2026)
  2.[E][ ] international food fair (from: Oct 15 2026, 9:00AM to: Oct 15 2026, 5:00PM)
  ```

### Deleting a task: `delete`

Removes a task from your list by its 1-based index number.

- **Format:** `delete INDEX`
- **Example:** `delete 1`
- **Expected Output:**
  ```text
  Noted. I've removed this task:
  [T][ ] buy baking ingredients
  Now you have 3 tasks in the list.
  ```

### Exiting the program: `bye`

Exits Foodielover cleanly.

- **Format:** `bye`
- **Expected Output:**
  ```text
  Bye. Hope to see you again soon!
  ```

### Automatic Data Persistence & Backup

- Foodielover automatically saves all additions, deletions, and status changes to disk at `./data/foodielover.txt`.
- Before the first save of each session, Foodielover creates a safe backup copy at `./data/foodielover.txt.bak` holding your data as it was when the app started, protecting it from subsequent overwrites during that run.
- When starting up, your tasks are loaded automatically. If any line is damaged or malformed, Foodielover issues a warning, skips the damaged entry, and safely loads the rest.

---

## Command Summary

| Action | Format | Examples |
|---|---|---|
| **Add ToDo** | `todo DESCRIPTION` | `todo buy groceries` |
| **Add Deadline** | `deadline DESCRIPTION /by DUE_DATE` | `deadline report /by 2026-10-15`<br>`deadline quiz /by 2/12/2026 1800` |
| **Add Event** | `event DESCRIPTION /from START /to END` | `event fair /from 2026-10-15 0900 /to 2026-10-15 1700` |
| **List Tasks** | `list` | `list` |
| **Mark Task** | `mark INDEX` | `mark 1` |
| **Unmark Task** | `unmark INDEX` | `unmark 1` |
| **Find Tasks** | `find KEYWORD` | `find report`, `find book` |
| **Filter by Date** | `date DATE`<br>`on DATE` | `date 2026-10-15`<br>`on 15/10/2026` |
| **Delete Task** | `delete INDEX` | `delete 2` |
| **Exit** | `bye` | `bye` |