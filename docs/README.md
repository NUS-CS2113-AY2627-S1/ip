# Atlas User Guide

Atlas is a desktop task management application optimized for use via a Command Line Interface (CLI). It helps you organize to-dos, deadlines, and events through intuitive text commands, backed by automatic persistent storage.

---

## Setting Up

### Prerequisites
* **Java 17 or higher** (JDK 25 recommended).

### Running via Gradle / IntelliJ
1. Clone or open the repository folder in IntelliJ IDEA or VS Code.
2. In the terminal at the project root directory (`ip/`), run:
   ```bash
   ./gradlew run
   ```
   *(On Windows PowerShell, you can also use `.\gradlew.bat run`)*

### Running via Standalone JAR
1. Download the latest `atlas.jar` release.
2. Place `atlas.jar` into an empty directory where you want to keep your task data.
3. Open a terminal in that directory and run:
   ```bash
   java -jar atlas.jar
   ```

---

## Features & Usage

### Parameter Format Notes
* Words in `UPPER_CASE` represent parameters you supply (e.g., `todo DESCRIPTION`).
* Date values must be formatted as `yyyy-mm-dd` (e.g., `2026-10-15`).
* Parameters must follow the specified order.

---

### 1. Adding a To-Do Task: `todo`
Adds a task without any date or time constraints.

* **Format:** `todo DESCRIPTION`
* **Example:** `todo read CS2113 textbook`
* **Expected Output:**
  ```text
  ____________________________________________________________
  Added task:
  [T][ ] read CS2113 textbook
  There is 1 item in the list
  ____________________________________________________________
  ```

---

### 2. Adding a Deadline Task: `deadline`
Adds a task that must be completed by a specific date.

* **Format:** `deadline DESCRIPTION /by YYYY-MM-DD`
* **Example:** `deadline submit assignment /by 2026-10-25`
* **Expected Output:**
  ```text
  ____________________________________________________________
  Added task:
  [D][ ] submit assignment (by: Oct 25 2026)
  There are 2 items in the list
  ____________________________________________________________
  ```

---

### 3. Adding an Event Task: `event`
Adds a task spanning a specific start and end date.

* **Format:** `event DESCRIPTION /from YYYY-MM-DD /to YYYY-MM-DD`
* **Example:** `event career fair /from 2026-11-01 /to 2026-11-03`
* **Expected Output:**
  ```text
  ____________________________________________________________
  Added task:
  [E][ ] career fair (from: Nov 01 2026 to: Nov 03 2026)
  There are 3 items in the list
  ____________________________________________________________
  ```

---

### 4. Listing All Tasks: `list`
Displays all current tasks with their completion status and index numbers.

* **Format:** `list`
* **Expected Output:**
  ```text
  ____________________________________________________________
  1. [T][ ] read CS2113 textbook
  2. [D][ ] submit assignment (by: Oct 25 2026)
  3. [E][ ] career fair (from: Nov 01 2026 to: Nov 03 2026)
  ____________________________________________________________
  ```

---

### 5. Marking a Task as Done: `mark`
Marks a specific task from your list as completed.

* **Format:** `mark INDEX`
* **Example:** `mark 2`
* **Expected Output:**
  ```text
  ____________________________________________________________
  Marked task as DONE
  [D][X] submit assignment (by: Oct 25 2026)
  ____________________________________________________________
  ```

---

### 6. Marking a Task as Not Done: `unmark`
Reverts a completed task back to incomplete status.

* **Format:** `unmark INDEX`
* **Example:** `unmark 2`
* **Expected Output:**
  ```text
  ____________________________________________________________
  Marked task as NOT DONE
  [D][ ] submit assignment (by: Oct 25 2026)
  ____________________________________________________________
  ```

---

### 7. Deleting a Task: `delete`
Removes a task permanently from the list.

* **Format:** `delete INDEX`
* **Example:** `delete 1`
* **Expected Output:**
  ```text
  ____________________________________________________________
  The tasks has been removed.
     [T][ ] read CS2113 textbook
  There are 2 tasks remaining in the list.
  ____________________________________________________________
  ```

---

### 8. Finding Tasks by Keyword: `find`
Filters and displays tasks whose description contains the search keyword (case-insensitive).

* **Format:** `find KEYWORD`
* **Example:** `find assignment`
* **Expected Output:**
  ```text
  ____________________________________________________________
  Here are the matching tasks in your list:
  1. [D][ ] submit assignment (by: Oct 25 2026)
  ____________________________________________________________
  ```

---

### 9. Exiting the Application: `bye`
Exits the application.

* **Format:** `bye`
* **Expected Output:**
  ```text
  ____________________________________________________________
  Bye. Happy to be of service!
  ____________________________________________________________
  ```

---

## Data Storage
Atlas automatically manages local persistence:
* Task changes are saved immediately to `./data/atlas.txt`.
* When launched, saved tasks are loaded automatically from disk.
* You do not need to issue any manual save command.

---

## Command Summary

| Action | Format | Example |
|---|---|---|
| **Todo** | `todo DESCRIPTION` | `todo read book` |
| **Deadline** | `deadline DESCRIPTION /by YYYY-MM-DD` | `deadline return book /by 2026-10-15` |
| **Event** | `event DESCRIPTION /from YYYY-MM-DD /to YYYY-MM-DD` | `event camp /from 2026-10-20 /to 2026-10-22` |
| **List** | `list` | `list` |
| **Mark** | `mark INDEX` | `mark 1` |
| **Unmark** | `unmark INDEX` | `unmark 1` |
| **Delete** | `delete INDEX` | `delete 1` |
| **Find** | `find KEYWORD` | `find book` |
| **Exit** | `bye` | `bye` |