# YODA User Guide

```
__   __  ___  ____    _
\ \ / / / _ \|  _ \  / \
 \ V / | | | | | | |/ _ \
  | |  | |_| | |_| / ___ \
  |_|   \___/|____/_/   \_\
```

YODA is a task management chatbot that helps you track your todos, deadlines, and events via a simple command-line interface. :)

---

## Quick Start

1. Ensure you have Java 25 or above installed.
2. Download the latest `yoda.jar` from the releases page.
3. Run the app with: `java -jar yoda.jar`
4. Type a command and press Enter to get started!

---

## Features

### Add a Todo
Adds a simple task with no date/time attached.

**Format:** `todo <description>`

**Example:** `todo Read a book`

Got it. I've added this task:
[T][ ] Read a book
Now you have 1 tasks in the list.


---

### Add a Deadline
Adds a task with a due date/time.

**Format:** `deadline <description> /by <time>`

**Example:** `deadline Submit report /by Monday 6pm`

Got it. I've added this task:
[D][ ] Submit report (by: Monday 6pm)
Now you have 2 tasks in the list.


---

### Add an Event
Adds a task with a start and end time.

**Format:** `event <description> /from <start> /to <end>`

**Example:** `event Team meeting /from Mon 2pm /to Mon 4pm`

Got it. I've added this task:
[E][ ] Team meeting (from: Mon 2pm to: Mon 4pm)
Now you have 3 tasks in the list.


---

### List All Tasks
Shows all tasks currently in your list.

**Format:** `list`

Here are the tasks in your list:
1.[T][ ] Read a book
2.[D][ ] Submit report (by: Monday 6pm)
3.[E][ ] Team meeting (from: Mon 2pm to: Mon 4pm)


---

### Mark a Task as Done
Marks the specified task as completed.

**Format:** `mark <task number>`

**Example:** `mark 1`

Nice! I've marked this task as done:
[T][X] Read a book


---

### Unmark a Task
Marks the specified task as not done.

**Format:** `unmark <task number>`

**Example:** `unmark 1`

OK, I've marked this task as not done yet:
[T][ ] Read a book


---

### Delete a Task
Removes the specified task from your list.

**Format:** `delete <task number>`

**Example:** `delete 2`

Noted. I've removed this task:
[D][ ] Submit report (by: Monday 6pm)
Now you have 2 tasks in the list.


---

### Exit the App
Exits YODA.

**Format:** `bye`

Bye. Hope to see you again soon!


---

## Task Types Summary

| Symbol | Type     |
|--------|----------|
| `[T]`  | Todo     |
| `[D]`  | Deadline |
| `[E]`  | Event    |

| Symbol | Status   |
|--------|----------|
| `[ ]`  | Not done |
| `[X]`  | Done     |

---

## Command Summary

| Command    | Format                                              |
|------------|-----------------------------------------------------|
| todo       | `todo <description>`                                |
| deadline   | `deadline <description> /by <time>`                 |
| event      | `event <description> /from <start> /to <end>`       |
| list       | `list`                                              |
| mark       | `mark <task number>`                                |
| unmark     | `unmark <task number>`                              |
| delete     | `delete <task number>`                              |
| bye        | `bye`                                               |
