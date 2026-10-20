# GigaBot User Guide

GigaBot is a fast, interactive conversational assistant that helps you track and manage your daily tasks, deadlines, and events right from your command line.

## Features

### 1. Adding Tasks
* **Todo:** Adds a basic task without any date attached.
    * **Format:** `todo <description>`
    * **Example:** `todo borrow book`
* **Deadline:** Adds a task that needs to be done before a specific date.
    * **Format:** `deadline <description> /by yyyy-mm-dd`
    * **Example:** `deadline return book /by 2026-10-15`
* **Event:** Adds an event that starts and ends at specific dates.
    * **Format:** `event <description> /from yyyy-mm-dd /to yyyy-mm-dd`
    * **Example:** `event project meeting /from 2026-10-16 /to 2026-10-17`

### 2. Managing Tasks
* **List:** Displays all tasks currently in your list.
    * **Format:** `list`
* **Mark:** Marks a specific task as completed.
    * **Format:** `mark <task_number>`
* **Unmark:** Marks a specific task as not completed yet.
    * **Format:** `unmark <task_number>`
* **Delete:** Permanently removes a task from your list.
    * **Format:** `delete <task_number>`

### 3. Searching Data
* **Find:** Searches for tasks containing a specific keyword in their description.
    * **Format:** `find <keyword>`
    * **Example:** `find book`

### 4. Exiting the Program
* **Bye:** Safely shuts down the chatbot.
    * **Format:** `bye`

## Data Storage
GigaBot automatically saves all your tasks to your hard disk instantly after every command. There is no need to save manually!