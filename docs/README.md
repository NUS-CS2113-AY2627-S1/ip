```
 _   _
| \ | | ___  ___
|  \| |/ _ \/ _ \
| |\  |  __/ (_) |
|_| \_|\___|\___/
```

# Neo User Guide

Neo is a fast, command-line-based task management assistant designed to help you keep track of your daily tasks, deadlines, and events efficiently.

## Quick Start

1. Ensure you have Java 17 or above installed on your computer.
2. Download the latest `neo.jar` from the releases tab.
3. Open a command terminal, navigate to the folder containing the jar file, and run the following command:
   `java -jar neo.jar`

---

## Features

### 1. Adding a basic task: `todo`

Adds a standard task with no specific date or time attached to it.

- **Format:** `todo <task_description>`
- **Example:** `todo read textbook chapter 3`

### 2. Adding a deadline: `deadline`

Adds a task that needs to be done before a specific date/time.

- **Format:** `deadline <task_description> /by <date/time>`
- **Example:** `deadline submit IP PR /by Sunday 2359`

### 3. Adding an event: `event`

Adds an event that starts and ends at a specific time.

- **Format:** `event <event_name> /from <start_time> /to <end_time>`
- **Example:** `event project meeting /from Monday 2pm /to 4pm`

### 4. Listing all tasks: `list`

Displays a fully indexed list of all the tasks currently in your task manager.

- **Format:** `list`

### 5. Searching for tasks: `find`

Finds and lists all tasks that contain a specific keyword in their description.

- **Format:** `find <keyword>`
- **Example:** `find book`

### 6. Marking a task as done: `mark`

Marks a specific task in your list as completed.

- **Format:** `mark <task_index>`
- **Example:** `mark 2` (Marks the 2nd task in the list as done)

### 7. Unmarking a task: `unmark`

Marks a specific completed task as not done yet.

- **Format:** `unmark <task_index>`
- **Example:** `unmark 2`

### 8. Deleting a task: `delete`

Removes a task permanently from your list.

- **Format:** `delete <task_index>`
- **Example:** `delete 3` (Deletes the 3rd task in the list)

### 9. Exiting the application: `bye`

Saves your tasks and exits Neo.

- **Format:** `bye`

## Editing saved tasks

Tasks are saved in `data/neo.txt` as readable UTF-8 text. Close Neo before editing
the file, then restart it to load your changes. For example:

```text
T | 0 | read book
D | 0 | return book | June 6th
E | 1 | meet friends | 10 am | 9 pm
```

Keep the `|` separators. `T`, `D`, and `E` mean todo,
deadline, and event; `0` means incomplete and `1` means complete. Edit the task
description and dates directly. Within a field, write `\|` for a literal pipe,
`\\` for a backslash, `\n` for a line break, and `\r` for a carriage return.

Each task addition, deletion, or completion change saves the entire list in this format.
