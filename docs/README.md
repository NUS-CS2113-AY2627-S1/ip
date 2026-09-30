Jude is a desktop application for managing tasks, optimized for use through a Command Line Interface (CLI). With Jude, you can manage your todos, deadlines, and events easily and efficiently.

## Quick Start

1. Ensure you have Java 25 installed on your computer.
2. Run the application using your IDE or terminal.
3. Type your commands in the terminal and press Enter to execute them.

## Features

### Adding a Todo: `todo`

Adds a simple task without any date/time attached to it.

**Format:** `todo DESCRIPTION`

**Example of usage:**
`todo read book`

**Expected outcome:**
```
____________________________________________________________
Got it. I've added this task:
    [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
```

### Adding a Deadline: `deadline`

Adds a task that needs to be done before a specific date/time.

**Format:** `deadline DESCRIPTION /by DEADLINE`

**Example of usage:**
`deadline return book /by Sunday`

**Expected outcome:**
```
____________________________________________________________
Got it. I've added this task:
    [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
```

### Adding an Event: `event`

Adds a task that starts at a specific time and ends at a specific time.

**Format:** `event DESCRIPTION /from START /to END`

**Example of usage:**
`event project meeting /from Mon 2pm /to 4pm`

**Expected outcome:**
```
____________________________________________________________
Got it. I've added this task:
    [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
```

### Listing all tasks: `list`

Shows a list of all tasks in your task list.

**Format:** `list`

**Example of usage:**
`list`

**Expected outcome:**
```
____________________________________________________________
Here are the tasks in your list:
    1. [T][ ] read book
    2. [D][ ] return book (by: Sunday)
    3. [E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
```

### Marking a task as done: `mark`

Marks the specified task as completed.

**Format:** `mark INDEX`

* Marks the task at the specified `INDEX`.
* The index refers to the index number shown in the displayed task list.
* The index **must be a positive integer** 1, 2, 3, ...

**Example of usage:**
`mark 1`

**Expected outcome:**
```
____________________________________________________________
Nice! I've marked this task as done:
    [T][X] read book
____________________________________________________________
```

### Marking a task as not done: `unmark`

Marks the specified task as not completed.

**Format:** `unmark INDEX`

* Unmarks the task at the specified `INDEX`.
* The index refers to the index number shown in the displayed task list.
* The index **must be a positive integer** 1, 2, 3, ...

**Example of usage:**
`unmark 1`

**Expected outcome:**
```
____________________________________________________________
OK, I've marked this task as not done yet:
    [T][ ] read book
____________________________________________________________
```

### Deleting a task: `delete`

Deletes the specified task from the task list.

**Format:** `delete INDEX`

* Deletes the task at the specified `INDEX`.
* The index refers to the index number shown in the displayed task list.
* The index **must be a positive integer** 1, 2, 3, ...

**Example of usage:**
`delete 2`

**Expected outcome:**
```
____________________________________________________________
Noted. I've removed this task:
    [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
```

### Locating tasks by name: `find`

Finds tasks whose descriptions contain the given keyword.

**Format:** `find KEYWORD`

**Example of usage:**
`find book`

**Expected outcome:**
```
____________________________________________________________
Here are the matching tasks in your list:
    1. [T][ ] read book
____________________________________________________________
```

### Exiting the program: `bye`

Exits the program.

**Format:** `bye`

**Expected outcome:**
```
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Command Summary

* **Todo:** `todo DESCRIPTION`
* **Deadline:** `deadline DESCRIPTION /by DEADLINE`
* **Event:** `event DESCRIPTION /from START /to END`
* **List:** `list`
* **Mark:** `mark INDEX`
* **Unmark:** `unmark INDEX`
* **Delete:** `delete INDEX`
* **Find:** `find KEYWORD`
* **Bye:** `bye`