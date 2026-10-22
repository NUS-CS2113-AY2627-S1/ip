# Pratbot User Guide

Pratbot is a desktop chatbot for managing your tasks — todos, deadlines, and events — using simple text commands. It's optimized for fast typists: if you can type quickly, Pratbot can manage your tasks faster than most graphical apps.

## Quick Start

1. Ensure you have Java 25 installed on your computer.
2. Download the latest `Duke.jar` from the [Releases](../../releases) page.
3. Copy the file into an empty folder you want to use as your Pratbot workspace.
4. Open a terminal in that folder and run: java -jar Duke.jar
5. Type a command and press Enter to try it out. Some examples you can try:
   - `todo read book` — adds a todo task
   - `list` — shows all your tasks
   - `bye` — exits the program

## Notes about the command format

* Words in `UPPER_CASE` are parameters to be supplied by you.
  For example, in `todo TASK_DESCRIPTION`, replace `TASK_DESCRIPTION` with a value such as `read book`.
* Task numbers used in `mark`, `unmark`, and `delete` refer to the position shown in the most recent `list` (starting from 1).

## Features

### Adding a todo: `todo`

Adds a task with no date or time attached.

Format: `todo TASK_DESCRIPTION`

Example: `todo read book`
Got it. I've added this task:
[T][ ] read book
Now you have 1 tasks in the list.


### Adding a deadline: `deadline`

Adds a task that needs to be done before a specific date/time.

Format: `deadline TASK_DESCRIPTION /by DUE_DATE`

Example: `deadline return book /by Sunday`
Got it. I've added this task:
[D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.


### Adding an event: `event`

Adds a task that starts and ends at specific date/times.

Format: `event TASK_DESCRIPTION /from START /to END`

Example: `event project meeting /from Mon 2pm /to 4pm`
Got it. I've added this task:
[E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.


### Listing all tasks: `list`

Shows every task currently saved, numbered in order.

Format: `list`
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)


### Marking a task as done: `mark`

Marks the specified task as done.

Format: `mark TASK_NUMBER`

Example: `mark 2`
Nice! I've marked this task as done:
[D][X] return book (by: Sunday)


### Unmarking a task: `unmark`

Marks the specified task as not done.

Format: `unmark TASK_NUMBER`

Example: `unmark 2`
OK, I've marked this task as not done yet:
[D][ ] return book (by: Sunday)


### Deleting a task: `delete`

Removes the specified task from the list.

Format: `delete TASK_NUMBER`

Example: `delete 1`
Noted. I've removed this task:
[T][ ] read book
Now you have 2 tasks in the list.


### Finding tasks: `find`

Finds all tasks whose description contains the given keyword.

Format: `find KEYWORD`

Example: `find book`
Here are the matching tasks in your list:
1.[D][X] return book (by: Sunday)


### Exiting the program: `bye`

Saves your tasks and exits Pratbot.

Format: `bye`

## Saving the data

Pratbot automatically saves your tasks to disk after every command that changes the list. There's no need to save manually. Data is stored in a `data` folder created next to `Duke.jar`, and is loaded automatically the next time you start Pratbot.

## FAQ

**Q: How do I transfer my data to another computer?**
A: Copy the `data` folder from your old Pratbot workspace into the same folder as `Duke.jar` on the new computer.

## Command Summary

| Action | Format | Example |
|---|---|---|
| Todo | `todo TASK_DESCRIPTION` | `todo read book` |
| Deadline | `deadline TASK_DESCRIPTION /by DUE_DATE` | `deadline return book /by Sunday` |
| Event | `event TASK_DESCRIPTION /from START /to END` | `event meeting /from Mon 2pm /to 4pm` |
| List | `list` | `list` |
| Mark | `mark TASK_NUMBER` | `mark 2` |
| Unmark | `unmark TASK_NUMBER` | `unmark 2` |
| Delete | `delete TASK_NUMBER` | `delete 1` |
| Find | `find KEYWORD` | `find book` |
| Exit | `bye` | `bye` |