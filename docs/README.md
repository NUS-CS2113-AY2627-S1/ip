# Dook User Guide

> **Dook** is a lightweight command-line task manager that helps you keep track of your todos, deadlines, and events.

## Getting Started

### Running Dook

Make sure you have **Java 25** installed on your computer.

1. Download `Dook.jar`.
2. Open a terminal and navigate to the folder containing `Dook.jar`.
3. Run:

```bash
java -jar Dook.jar
```

Dook will start with the following greeting:

```text
	____________________________________________________________
	 ____   ___   ___  _  __
	|  _ \ / _ \ / _ \| |/ /
	| | | | | | | | | | ' /
	| |_| | |_| | |_| | . \
	|____/ \___/ \___/|_|\_\

	Hello! I'm Dook.
	What can I do for you?
	____________________________________________________________
```

You can now start entering commands.

## Features

### Adding a todo: `todo`

Adds a simple task to your task list.

**Format:**

```text
todo DESCRIPTION
```

**Example:**

```text
todo Buy groceries
```

Dook will add the task to your list:

```text
Got it. I've added this task:
  [T][ ] Buy groceries
Now you have 1 tasks in the list.
```

---

### Adding a deadline: `deadline`

Adds a task with a deadline.

**Format:**

```text
deadline DESCRIPTION /by DATE
```

**Example:**

```text
deadline Submit report /by Friday 11:59pm
```

The `/by` keyword separates the task description from its deadline.

---

### Adding an event: `event`

Adds an event with a start and end time.

**Format:**

```text
event DESCRIPTION /from START /to END
```

**Example:**

```text
event Project meeting /from Monday 2pm /to Monday 3pm
```

The `/from` and `/to` keywords specify when the event starts and ends.

---

### Viewing your tasks: `list`

Displays all tasks currently in your task list.

**Format:**

```text
list
```

Tasks are numbered starting from `1`.

**Example:**

```text
Here are the tasks in your list:
1.[T][ ] Buy groceries
2.[D][X] Submit report (by: Friday 11:59pm)
3.[E][ ] Project meeting (from: Monday 2pm to Monday 3pm)
```

---

### Marking a task as done: `mark`

Marks a task as completed.

**Format:**

```text
mark TASK_NUMBER
```

**Example:**

```text
mark 1
```

Dook will confirm the change:

```text
Nice! I've marked this task as done:
  [T][X] Buy groceries
```

---

### Marking a task as not done: `unmark`

Marks a completed task as incomplete.

**Format:**

```text
unmark TASK_NUMBER
```

**Example:**

```text
unmark 1
```

Dook will confirm the change:

```text
OK, I've marked this task as not done yet:
  [T][ ] Buy groceries
```

---

### Deleting a task: `delete`

Removes a task from your task list.

**Format:**

```text
delete TASK_NUMBER
```

**Example:**

```text
delete 2
```

Dook will confirm the deletion and show the number of remaining tasks:

```text
Noted. I've removed this task:
  [D][X] Submit report (by: Friday 11:59pm)
Now you have 2 tasks in the list.
```

Task numbers refer to the numbers displayed by the `list` command.

---

### Finding tasks: `find`

Searches for tasks whose **description** contains a specified keyword. The search is case-insensitive.

**Format:**

```text
find KEYWORD
```

**Example:**

```text
find meeting
```

Dook will display the matching tasks:

```text
Here are the matching tasks in your list:
1.[E][ ] Project meeting (from: Monday 2pm to Monday 3pm)
```

> **Tip:** `find` searches task descriptions only. It does not search deadline or event date/time information.

---

### Exiting Dook: `bye`

Closes Dook.

**Format:**

```text
bye
```

Dook will display:

```text
Bye. Hope to see you again soon!
```

## Command Summary

| Command    | Format                                  | Description               |
| ---------- | --------------------------------------- | ------------------------- |
| `todo`     | `todo DESCRIPTION`                      | Add a todo                |
| `deadline` | `deadline DESCRIPTION /by DATE`         | Add a deadline            |
| `event`    | `event DESCRIPTION /from START /to END` | Add an event              |
| `list`     | `list`                                  | View all tasks            |
| `mark`     | `mark TASK_NUMBER`                      | Mark a task as done       |
| `unmark`   | `unmark TASK_NUMBER`                    | Mark a task as not done   |
| `delete`   | `delete TASK_NUMBER`                    | Delete a task             |
| `find`     | `find KEYWORD`                          | Find tasks by description |
| `bye`      | `bye`                                   | Exit Dook                 |

## Understanding Task Status

Dook uses the following notation when displaying tasks:

* `[T]` — Todo
* `[D]` — Deadline
* `[E]` — Event
* `[ ]` — Task is not completed
* `[X]` — Task is completed

For example:

```text
[T][ ] Buy groceries
[D][X] Submit report (by: Friday 11:59pm)
[E][ ] Project meeting (from: Monday 2pm to Monday 3pm)
```

## Tips

* Task numbers start from **1**, not 0.
* Commands must begin with one of Dook's supported commands.
* When using `mark`, `unmark`, or `delete`, use the task number shown by `list`.
* Deadlines require exactly one `/by`.
* Events require exactly one `/from` and one `/to`.
* Descriptions and search keywords cannot be empty.
* Date and time information can be entered as free-form text, such as `Friday 11:59pm` or `Monday 2pm`.
* Your tasks are saved automatically when you add, modify, or delete them. There is no separate save command.
