# Bryte User Guide

<div align="center">
  <strong>Welcome to</strong>
  <pre>
██████╗ ██████╗ ██╗   ██╗████████╗███████╗
██╔══██╗██╔══██╗╚██╗ ██╔╝╚══██╔══╝██╔════╝
██████╔╝██████╔╝ ╚████╔╝    ██║   █████╗  
██╔══██╗██╔══██╗  ╚██╔╝     ██║   ██╔══╝  
██████╔╝██║  ██║   ██║      ██║   ███████╗
╚═════╝ ╚═╝  ╚═╝   ╚═╝      ╚═╝   ╚══════╝
  </pre>
</div>

**Bryte** is a lightweight, command-line based task management application (chatbot) designed for users who prefer interacting through a Command Line Interface (CLI). It helps you manage your daily tasks, deadlines, and events efficiently.

---

## Table of Contents

* [Quick Start](#quick-start)
* [Features](#features)
  * [1. Adding a Todo: `todo`](#1-adding-a-todo-todo)
  * [2. Adding a Deadline: `deadline`](#2-adding-a-deadline-deadline)
  * [3. Adding an Event: `event`](#3-adding-an-event-event)
  * [4. Listing all tasks: `list`](#4-listing-all-tasks-list)
  * [5. Marking a task as done: `mark`](#5-marking-a-task-as-done-mark)
  * [6. Marking a task as undone: `unmark`](#6-marking-a-task-as-undone-unmark)
  * [7. Deleting a task: `delete`](#7-deleting-a-task-delete)
  * [8. Finding tasks by keyword: `find`](#8-finding-tasks-by-keyword-find)
  * [9. Finding tasks by date: `schedule`](#9-finding-tasks-by-date-schedule)
  * [10. Exiting the program: `bye`](#10-exiting-the-program-bye)
* [FAQ](#faq)
* [Command Summary](#command-summary)

---

## Quick Start

1. Open a terminal and navigate to the folder containing `bryte.jar`.
2. Run the application using the command `java -jar bryte.jar`.
3. Type the command in the command box and press Enter to execute it.

---

## Features

<div markdown="block" class="alert alert-info">

**Notes about the command format:**<br>

* Words in `<UPPER_CASE>` are the parameters to be supplied by the user.<br>
  e.g. in `todo <DESCRIPTION>`, `DESCRIPTION` is a parameter which can be used as `todo read book`.
</div>

### 1. Adding a Todo: `todo`

Adds a simple task without any date or time constraints attached to it.

**Format:** `todo <description>`
- The `<description>` cannot be empty.

**Example:** `todo read book`
```text
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
```

### 2. Adding a Deadline: `deadline`

Adds a task that needs to be completed before a specific date and time.

**Format:** `deadline <description> /by <date/time>`
- The `<date/time>` must be in a recognizable format (e.g., `yyyy-MM-dd`, `yyyy-MM-dd HHmm`, `dd/MM/yyyy`).

**Example:** `deadline return book /by 2026-10-15`
```text
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Oct 15 2026)
 Now you have 2 tasks in the list.
____________________________________________________________
```

### 3. Adding an Event: `event`

Adds a task that starts at a specific time and ends at a specific time.

**Format:** `event <description> /from <start-time> /to <end-time>`
- Both `<start-time>` and `<end-time>` must be in a recognizable format (e.g., `yyyy-MM-dd HHmm`).

**Example:** `event project meeting /from 2026-10-02 1400 /to 2026-10-02 1600`
```text
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Oct 02 2026, 2:00 PM to: Oct 02 2026, 4:00 PM)
 Now you have 3 tasks in the list.
____________________________________________________________
```

### 4. Listing all tasks: `list`

Shows a list of all tasks currently stored in your task list.

**Format:** `list`

**Example:**
```text
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[D][ ] return book (by: Oct 15 2026)
 3.[E][ ] project meeting (from: Oct 02 2026, 2:00 PM to: Oct 02 2026, 4:00 PM)
____________________________________________________________
```

### 5. Marking a task as done: `mark`

Marks a specific task in the list as completed.

**Format:** `mark <index>`
- `<index>` must be a valid positive integer representing the task number in the list.

**Example:** `mark 1`
```text
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] read book
____________________________________________________________
```

### 6. Marking a task as undone: `unmark`

Marks a previously completed task as incomplete.

**Format:** `unmark <index>`
- `<index>` must be a valid positive integer representing the task number in the list.

**Example:** `unmark 1`
```text
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] read book
____________________________________________________________
```

### 7. Deleting a task: `delete`

Permanently removes a task from your list.

**Format:** `delete <index>`
- `<index>` must be a valid positive integer representing the task number in the list.

**Example:** `delete 3`
```text
____________________________________________________________
 Noted. I've removed this task:
   [E][ ] project meeting (from: Oct 02 2026, 2:00 PM to: Oct 02 2026, 4:00 PM)
 Now you have 2 tasks in the list.
____________________________________________________________
```

### 8. Finding tasks by keyword: `find`

Finds and lists all tasks whose description contains the specified keyword.

**Format:** `find <keyword>`
- The search is case-insensitive.

**Example:** `find book`
```text
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] read book
 2.[D][ ] return book (by: Oct 15 2026)
____________________________________________________________
```

### 9. Finding tasks by date: `schedule`

Finds and lists all deadlines and events that occur on a specific date.

**Format:** `schedule <date>`
- The `<date>` must be in a recognizable format (e.g., `yyyy-MM-dd`).

**Example:** `schedule 2026-10-15`
```text
____________________________________________________________
 Here are the tasks happening on Oct 15 2026:
 1.[D][ ] return book (by: Oct 15 2026)
____________________________________________________________
```

### 10. Exiting the program: `bye`

Exits the Bryte chatbot safely.

**Format:** `bye`

**Example:**
```text
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

---

## FAQ

**Q**: How is my data saved?<br>
**A**: Bryte automatically saves your tasks to your hard drive every time the task list is modified. You do not need to save manually! The data is stored in a text file located at `data/bryte.txt`.

**Q**: How do I transfer my data to another computer?<br>
**A**: Install the app on the other computer and overwrite the empty data file it creates with your `data/bryte.txt` file.

---

## Command Summary

| Action | Format, Examples |
|--------|------------------|
| **Todo** | `todo <description>` <br> e.g., `todo read book` |
| **Deadline** | `deadline <description> /by <date/time>` <br> e.g., `deadline return book /by 2026-10-15` |
| **Event** | `event <description> /from <start-time> /to <end-time>` <br> e.g., `event meeting /from 2026-10-02 1400 /to 2026-10-02 1600` |
| **List** | `list` |
| **Mark** | `mark <index>` <br> e.g., `mark 1` |
| **Unmark** | `unmark <index>` <br> e.g., `unmark 1` |
| **Delete** | `delete <index>` <br> e.g., `delete 3` |
| **Find** | `find <keyword>` <br> e.g., `find book` |
| **Schedule** | `schedule <date>` <br> e.g., `schedule 2026-10-15` |
| **Exit** | `bye` |