# Dash User Guide

**Dash** is a friendly, efficient command-line task manager designed to help you stay on top of your to-dos, deadlines, and events with minimal friction.

All your tasks are saved automatically to your hard disk (`./data/dash.txt`), so your data is always preserved across sessions.

---

## Quick Start

1. Ensure you have **Java 17 or higher** installed on your system.
2. Launch Dash in your terminal:
   ```bash
   java -jar dash.jar
   ```
3. When Dash welcomes you, type any command below and press **Enter**.
4. Type `bye` whenever you are ready to exit.

---

## Features

### 1. Adding a To-Do: `todo`
Adds a general task without any date or time attached.

- **Format**: `todo <description>`
- **Example**: `todo borrow book`
- **Expected Output**:
  ```text
  Got it. I've added this task:
    [T][ ] borrow book
  Now you have 1 tasks in the list.
  ```

### 2. Adding a Deadline: `deadline`
Adds a task that must be completed by a specific calendar date. The date must be provided in `yyyy-mm-dd` format (e.g., `2024-10-15`), and Dash will display it neatly as `MMM dd yyyy`.

- **Format**: `deadline <description> /by <yyyy-mm-dd>`
- **Example**: `deadline return book /by 2024-10-15`
- **Expected Output**:
  ```text
  Got it. I've added this task:
    [D][ ] return book (by: Oct 15 2024)
  Now you have 2 tasks in the list.
  ```

### 3. Adding an Event: `event`
Adds an event with start and end times or dates.

- **Format**: `event <description> /from <start> /to <end>`
- **Example (Time-based)**: `event project meeting /from Mon 2pm /to 4pm`
- **Example (Date-based)**: `event tech conference /from 2024-10-15 /to 2024-10-17`
- **Expected Output**:
  ```text
  Got it. I've added this task:
    [E][ ] project meeting (from: Mon 2pm to: 4pm)
  Now you have 3 tasks in the list.
  ```

### 4. Listing All Tasks: `list`
Displays all current tasks with their index number, task type (`[T]`, `[D]`, `[E]`), completion status (`[X]` for done, `[ ]` for not done), and details.

- **Format**: `list`
- **Expected Output**:
  ```text
  Here are the tasks in your list:
  1.[T][ ] borrow book
  2.[D][ ] return book (by: Oct 15 2024)
  3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
  ```

### 5. Marking a Task as Done: `mark`
Marks a specific task as completed.

- **Format**: `mark <task_number>`
- **Example**: `mark 1`
- **Expected Output**:
  ```text
  Nice! I've marked this task as done:
    [T][X] borrow book
  ```

### 6. Marking a Task as Incomplete: `unmark`
Reverts a completed task back to incomplete status.

- **Format**: `unmark <task_number>`
- **Example**: `unmark 1`
- **Expected Output**:
  ```text
  OK, I've marked this task as not done yet:
    [T][ ] borrow book
  ```

### 7. Deleting a Task: `delete`
Removes a task permanently from your list using its index number.

- **Format**: `delete <task_number>`
- **Example**: `delete 2`
- **Expected Output**:
  ```text
  Noted. I've removed this task:
    [D][ ] return book (by: Oct 15 2024)
  Now you have 2 tasks in the list.
  ```

### 8. Finding Tasks on a Specific Date: `on`
Shows all deadlines and events occurring on a given date. Dates must be in `yyyy-mm-dd` format.

- **Format**: `on <yyyy-mm-dd>`
- **Example**: `on 2024-10-15`
- **Expected Output**:
  ```text
  Here are the tasks occurring on Oct 15 2024:
  1.[D][ ] return book (by: Oct 15 2024)
  2.[E][ ] tech conference (from: 2024-10-15 to: 2024-10-17)
  ```

### 9. Finding Tasks by Keyword: `find`
Searches task descriptions for a keyword (case-insensitive) and lists all matches.

- **Format**: `find <keyword>`
- **Example**: `find book`
- **Expected Output**:
  ```text
  Here are the matching tasks in your list:
  1.[T][X] borrow book
  2.[D][ ] return book (by: Oct 15 2024)
  ```

### 10. Exiting the Application: `bye`
Exits Dash. Your tasks are automatically saved.

- **Format**: `bye`
- **Expected Output**:
  ```text
  Bye. Hope to see you again soon!
  ```

---

## Command Summary

| Action | Format | Example |
| :--- | :--- | :--- |
| **Add To-Do** | `todo <description>` | `todo borrow book` |
| **Add Deadline** | `deadline <description> /by <yyyy-mm-dd>` | `deadline return book /by 2024-10-15` |
| **Add Event** | `event <description> /from <start> /to <end>` | `event meeting /from Mon 2pm /to 4pm` |
| **List Tasks** | `list` | `list` |
| **Mark as Done** | `mark <task_number>` | `mark 1` |
| **Mark as Not Done** | `unmark <task_number>` | `unmark 1` |
| **Delete Task** | `delete <task_number>` | `delete 2` |
| **Find by Date** | `on <yyyy-mm-dd>` | `on 2024-10-15` |
| **Find by Keyword** | `find <keyword>` | `find book` |
| **Exit** | `bye` | `bye` |

---

## Data Storage
- Tasks are automatically loaded from `./data/dash.txt` on startup.
- Tasks are saved automatically whenever you add, delete, mark, or unmark tasks.
- You do not need to manually create the `./data/` folder or `dash.txt` file; Dash creates them automatically if they are missing.
