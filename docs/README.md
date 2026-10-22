# <span style="color:#006064">Dawn User Guide</span>

## Table of Contents
- [Introduction](#introduction)
- [Quick Start](#quick-start)
- [Features](#features)
  - [Command Format & Syntax Rules](#command-format--syntax-rules)
  - [Task Display Notation & Status Icons](#task-display-notation--status-icons)
  - [Supported Date & Time Formats](#supported-date--time-formats)
  - [Adding a todo task: `todo`](#adding-a-todo-task-todo)
  - [Adding a deadline task: `deadline`](#adding-a-deadline-task-deadline)
  - [Adding an event task: `event`](#adding-an-event-task-event)
  - [Listing all tasks: `list`](#listing-all-tasks-list)
  - [Marking a task as done: `mark`](#marking-a-task-as-done-mark)
  - [Unmarking a task: `unmark`](#unmarking-a-task-unmark)
  - [Deleting a task: `delete`](#deleting-a-task-delete)
  - [Searching for tasks by keyword: `find`](#searching-for-tasks-by-keyword-find)
  - [Viewing tasks by date: `view`](#viewing-tasks-by-date-view)
  - [Viewing help: `help`](#viewing-help-help)
  - [Exiting the application: `bye`](#exiting-the-application-bye)
- [Command Summary](#command-summary)
- [FAQ](#faq)

---

## <span style="color:#006064">Introduction</span>

**Dawn** is a desktop Command Line Interface (CLI) task management chatbot tailored to help  keep track of your daily tasks, deadlines, and events. 
Named after the Pokémon trainer, _Dawn_ allows you to manage your schedule seamlessly and efficiently through  text commands.

---

## <span style="color:#006064">Quick Start</span>

1. Ensure you have **Java 25** or above installed on your computer.
2. Download the latest [`dawn.jar`](https://github.com/naieel04/ip/releases) release from the repository [here](https://github.com/naieel04/ip/releases).
3. Open a command prompt or terminal in the folder where the `.jar` file is located.
4. Start the application by running the following command:
   ```bash
   java -jar dawn.jar
   ```
5. You will be greeted by Dawn's welcome banner. Start entering commands to begin managing your day!

---

## <span style="color:#006064">Features</span>

### Command Format & Syntax Rules

Before using Dawn, take note of the following general command rules:

- **Words in `[brackets]` are parameters:** Parameters enclosed in square brackets are required arguments to be provided by the user.
  *Example:* In `todo [description]`, `[description]` is a parameter (`todo read a book`).
- **Command keywords are lowercase:** Commands like `todo`, `list`, and `help` must be entered in lowercase.
- **Parameterless commands reject extra arguments:** Commands that take no arguments (`list`, `help`, `bye`) will show an error if additional characters or arguments are typed (e.g., `list 123` or `bye now` will be rejected).
- **Task numbers are 1-based positive integers:** For `mark`, `unmark`, and `delete`, task numbers refer to the index displayed in the `list` command and must be positive integers (e.g., `1`, `2`, `3`).
- **Marker order in events:** For `event` commands, the `/from` clause must precede the `/to` clause.
- **Keyword searches are case-insensitive:** The `find` command will match tasks regardless of uppercase or lowercase (e.g., `find book` matches `Book` and `BOOK`).

---

### Task Display Notation & Status Icons

When viewing tasks through `list`, `find`, or `view`, each task entry is formatted with concise status and type tags:

| Symbol / Tag | Meaning                                                   | Example                                                              |
|:------------:|:----------------------------------------------------------|:---------------------------------------------------------------------|
|    `[T]`     | **ToDo task:** A simple task without dates or times.      | `[T][ ] read a book`                                                 |
|    `[D]`     | **Deadline task:** A task due by a specific date or time. | `[D][ ] submit assignment (by: 20 Nov 2026 23:59)`                   |
|    `[E]`     | **Event task:** An event spanning a start and end time.   | `[E][ ] orientation (from: 12 Oct 2026 14:00 to: 12 Oct 2026 16:00)` |
|    `[ ]`     | **Pending status:** The task has not been completed yet.  | `[T][ ] read a book`                                                 |
|    `[X]`     | **Completed status:** The task has been marked as done.   | `[T][X] read a book`                                                 |

*Note: Dates and times with recognized calendar formats are automatically displayed in clean English format (`dd MMM yyyy` or `dd MMM yyyy HH:mm`, e.g., `12 Oct 2026 14:00`).*

---

### Supported Date & Time Formats

For commands that use calendar dates (`deadline`, `event`, and `view`), Dawn recognizes these formats. Each may include an optional four-digit `HHmm` time, such as `1800` for 18:00:

- **yyyy-MM-dd:** `2026-10-12`
- **yyyy/MM/dd:** `2026/10/12`
- **d/M/yyyy:** `12/10/2026` or `2/1/2026`
- **dd-MM-yyyy:** `12-10-2026`
- **d MMM yyyy:** `12 Oct 2026` or `2 Oct 2026` (English month abbreviations)

> [!TIP]
> **Flexible text fallback:** `deadline` and `event` also accept text such as `Monday 10am` or `tomorrow night`. Dawn rejects invalid calendar-like dates such as `31 Feb 2026` instead of saving them as text. Tasks with free-form dates cannot be found using `view`.

---

### Adding a todo task: `todo`

Adds a standard task without any specific date attached to it.

- **Format:** `todo [description]`
- **Examples:**
  - `todo read a book`
  - `todo wash the dishes`
- **Expected Output:**
  ```text
  Pip! Added this task: read a book
  ```

---

### Adding a deadline task: `deadline`

Adds a task that needs to be done by a specific deadline. 

- **Format:** `deadline [description] /by [due date]`
- **Examples:**
  - `deadline submit assignment /by 2026-11-20 2359`
  - `deadline return library book /by tomorrow night`
- **Expected Output:**
  ```text
  Pip! Added this task: submit assignment (by: 20 Nov 2026 23:59)
  ```

---

### Adding an event task: `event`

Adds an event task that spans a duration defined by a start time and an end time.

- **Format:** `event [description] /from [start time] /to [end time]`
- **Examples:**
  - `event project meeting /from 12/10/2026 1400 /to 12/10/2026 1600`
  - `event career fair /from Monday 10am /to Wednesday 5pm`
- **Expected Output:**
  ```text
  Pip! Added this task: project meeting (from: 12 Oct 2026 14:00 to: 12 Oct 2026 16:00)
  ```

---

### Listing all tasks: `list`

Displays all the tasks currently saved in your task list, alongside their completion statuses.

- **Format:** `list`
- **Expected Output:**
  ```text
  Here's your task list:
  1.[T][ ] read a book
  2.[D][ ] submit assignment (by: 20 Nov 2026 23:59)
  3.[E][ ] project meeting (from: 12 Oct 2026 14:00 to: 12 Oct 2026 16:00)
  ```

---

### Marking a task as done: `mark`

Marks the task at the specified numerical index in the list as completed.

- **Format:** `mark [task number]`
- **Examples:**
  - `mark 1` *(Marks the 1st task as done).*
- **Expected Output:**
  ```text
  Nice! Marked this task as done:
      [T][X] read a book
  ```

---

### Unmarking a task: `unmark`

Marks a previously completed task at the specified index as uncompleted.

- **Format:** `unmark [task number]`
- **Examples:**
  - `unmark 1` *(Unmarks the 1st task).*
- **Expected Output:**
  ```text
  Back on the list. Marked this task as not done yet:
      [T][ ] read a book
  ```

---

### Deleting a task: `delete`

Permanently deletes the task at the specified index from your tracker.

> [!WARNING]
> This action cannot be undone.

- **Format:** `delete [task number]`
- **Examples:**
  - `delete 3` *(Deletes the 3rd task in the list).*
- **Expected Output:**
  ```text
  Removed this task. You have 2 tasks left:
      [E][ ] project meeting (from: 12 Oct 2026 14:00 to: 12 Oct 2026 16:00)
  ```

---

### Searching for tasks by keyword: `find`

Retrieves and displays all tasks whose description contains the specified keyword. This search is case-insensitive.

- **Format:** `find [keyword]`
- **Examples:**
  - `find meeting`
  - `find BOOK`
- **Expected Output:**
  ```text
  I found these tasks in your list:
  1.[E][ ] project meeting (from: 12 Oct 2026 14:00 to: 12 Oct 2026 16:00)
  ```

---

### Viewing tasks by date: `view`

Shows all deadline and event tasks that occur on a particular calendar date. Use one of the structured formats above. If you include a time, Dawn still checks the entire day.

- **Format:** `view [date]`
- **Examples:**
  - `view 2026-10-12`
  - `view 12/10/2026`
  - `view 12 Oct 2026`
- **Expected Output:**
  ```text
  Here's your plan for 12 Oct 2026:
  1.[E][ ] project meeting (from: 12 Oct 2026 14:00 to: 12 Oct 2026 16:00)
  ```

---

### Viewing help: `help`

Displays the command syntax guide table on demand without having to restart the application.

- **Format:** `help`
- **Expected Output:**
  ```text
  Here are the commands you can use:
  +---------------------------------------------+-----------------------+
  | Command / format                            | Description           |
  +---------------------------------------------+-----------------------+
  | todo [description]                          | Add a todo            |
  | deadline [description] /by [due date]       | Add a deadline        |
  | event [description] /from [start] /to [end] | Add an event          |
  | list                                        | List all tasks        |
  | mark [task number]                          | Mark a task done      |
  | unmark [task number]                        | Mark a task not done  |
  | delete [task number]                        | Delete a task         |
  | view [date]                                 | View tasks on a date  |
  | find [keyword]                              | Find tasks by keyword |
  | help                                        | Show commands         |
  | bye                                         | Exit Dawn             |
  +---------------------------------------------+-----------------------+
  ```

---

### Exiting the application: `bye`

Safely exits the chatbot interface.

- **Format:** `bye`
- **Expected Output:**
  ```text
  Pip! No need to worry, everything is saved. See you next time!
  ```

---

## <span style="color:#006064">Command Summary</span>

| Action | Format | Examples |
|--------|--------|----------|
| **Add ToDo** | `todo [description]` | `todo read a book` |
| **Add Deadline** | `deadline [description] /by [due date]` | `deadline submit assignment /by 2026-11-20 2359` |
| **Add Event** | `event [description] /from [start] /to [end]` | `event project meeting /from 2026-10-12 1400 /to 2026-10-12 1600` |
| **List** | `list` | `list` |
| **Mark** | `mark [task number]` | `mark 1` |
| **Unmark** | `unmark [task number]` | `unmark 1` |
| **Delete** | `delete [task number]` | `delete 3` |
| **Find** | `find [keyword]` | `find meeting` |
| **View** | `view [date]` | `view 2026-10-12` |
| **Help** | `help` | `help` |
| **Exit** | `bye` | `bye` |

---

## <span style="color:#006064">FAQ</span>

**Q**: How do I save my data? Do I need to run a save command?  
**A**: There is no manual save command. Dawn **automatically saves** your tasks after commands that create or modify the list (e.g., adding, marking, or deleting a task). When you start the application again, your saved tasks are loaded into the session.

**Q**: Where is my data saved?  
**A**: Your data is saved in `data/dawn.txt`, relative to the directory from which you launch Dawn.

**Q**: Can I manually edit the `data/dawn.txt` file?  
**A**: Yes, advanced users can edit `data/dawn.txt` directly. However, be cautious:
- Records use a versioned format (`V2 | [Type] | [Status] | [Description]...`).
- If any line is damaged, missing required fields, or formatted incorrectly, Dawn will print a warning on startup (`Pip?! I skipped a damaged saved task...`) and safely skip only the corrupted record while preserving all valid tasks.
- If the file is completely unreadable or missing, Dawn starts with a fresh empty list.
- It is recommended to make a backup copy of `data/dawn.txt` before making manual edits.
