# Wallis User Guide
By Satya Venkat Pranav Gandham

Wallis is a desktop chatbot application used for managing daily tasks, deadlines, and events, optimized for use via a Command Line Interface (CLI).

## Features

### Adding a Todo Task
Adds a basic task without a specific date attached.
*   **Format:** `todo [description]`
*   **Example:** `todo read software engineering textbook`

### Adding a Deadline
Adds a task that needs to be completed before a specific date. Dates should be entered in `yyyy-MM-dd` format.
*   **Format:** `deadline [description] /by [yyyy-mm-dd]`
*   **Example:** `deadline submit assignment /by 2026-10-15`

### Adding an Event
Adds a task that occurs between a start and end date.
*   **Format:** `event [description] /from [yyyy-mm-dd] /to [yyyy-mm-dd]`
*   **Example:** `event team meeting /from 2026-10-20 /to 2026-10-21`

### Listing Tasks
Displays a numbered list of all currently tracked tasks.
*   **Format:** `list`

### Finding Tasks
Searches for tasks containing a specific keyword in their description.
*   **Format:** `find [keyword]`
*   **Example:** `find meeting`

### Marking a Task as Done
Marks a specific task in the list as completed.
*   **Format:** `mark [task_number]`
*   **Example:** `mark 1`

### Unmarking a Task
Marks a previously completed task as not done yet.
*   **Format:** `unmark [task_number]`
*   **Example:** `unmark 1`

### Deleting a Task
Permanently removes a task from your list.
*   **Format:** `delete [task_number]`
*   **Example:** `delete 2`

### Exiting the Application
Saves all data and closes the chatbot.
*   **Format:** `bye`