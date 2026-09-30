# XxLilChatxX User Guide

XxLilChatxX is a desktop chatbot app for managing your tasks, optimized for use through a Command Line Interface (CLI). If you type quickly, XxLilChatxX can help you track your tasks faster than a traditional GUI app.

* [Quick start](#quick-start)
* [Features](#features)
   * [Adding a todo: `todo`](#adding-a-todo-todo)
   * [Adding a deadline: `deadline`](#adding-a-deadline-deadline)
   * [Adding an event: `event`](#adding-an-event-event)
   * [Listing all tasks: `list`](#listing-all-tasks-list)
   * [Marking a task as done: `mark`](#marking-a-task-as-done-mark)
   * [Unmarking a task: `unmark`](#unmarking-a-task-unmark)
   * [Deleting a task: `delete`](#deleting-a-task-delete)
   * [Finding tasks: `find`](#finding-tasks-find)
   * [Exiting the program: `bye`](#exiting-the-program-bye)
* [Saving the data](#saving-the-data)
* [Editing the data file](#editing-the-data-file)
* [FAQ](#faq)
* [Command summary](#command-summary)

## Quick start

1. Ensure you have Java 25 installed on your computer.
2. Download the latest `.jar` file from [here](https://github.com/Kaishhhh/ip/releases).
3. Copy the file to the folder you want to use as the home folder for XxLilChatxX.
4. Open a terminal, `cd` into that folder, and run:

java -jar XxLilChatxX.jar

5. Type a command and press Enter. Some example commands you can try:

   * `todo read book` : Adds a todo task named "read book".
   * `list` : Shows all your tasks.
   * `delete 1` : Deletes the 1st task shown in the current list.
   * `bye` : Exits the app.

Refer to the [Features](#features) section below for details of each command.

## Features

:information_source: **Notes about the command format:**

* Words in `UPPER_CASE` are parameters supplied by you.
  For example, in `todo DESCRIPTION`, `DESCRIPTION` should be replaced, e.g. `todo read book`.
* `INDEX` refers to the task number shown in the `list` output, starting from 1. The index must be a positive integer 1, 2, 3, …​
* Extraneous parameters for commands that take no parameters, such as `list` and `bye`, are ignored. For example, `list 123` is interpreted as `list`.

### Adding a todo: `todo`

Adds a task with no date or time attached.

Format: `todo DESCRIPTION`

Example:

todo read book

Got it. I've added this task:
[T][ ] read book
Now you have 1 tasks in the list.


### Adding a deadline: `deadline`

Adds a task that needs to be done by a specific date/time.

Format: `deadline DESCRIPTION /by DATE`

Example:

deadline return book /by Sunday

Got it. I've added this task:
[D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.


### Adding an event: `event`

Adds a task that starts and ends at specific times.

Format: `event DESCRIPTION /from START /to END`

Example:

event project meeting /from Mon 2pm /to 4pm

Got it. I've added this task:
[E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.


### Listing all tasks: `list`

Shows all tasks currently in your list, along with their status (done or not done).

Format: `list`

### Marking a task as done: `mark`

Marks the specified task as done.

Format: `mark INDEX`

Example: `mark 2` marks the 2nd task in the list as done.

### Unmarking a task: `unmark`

Marks the specified task as not done.

Format: `unmark INDEX`

Example: `unmark 2` marks the 2nd task in the list as not done.

### Deleting a task: `delete`

Removes the specified task from the list.

Format: `delete INDEX`

Example: `delete 3` removes the 3rd task in the list.

### Finding tasks: `find`

Finds tasks whose description contains the given keyword.

Format: `find KEYWORD`

* The search matches any part of the task description.
* Only one keyword is supported at a time.

Example:

find book

Here are the matching tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Sunday)


### Exiting the program: `bye`

Exits XxLilChatxX.

Format: `bye`

## Saving the data

XxLilChatxX automatically saves your tasks to disk after every command that changes the task list (adding, deleting, marking, or unmarking a task). You do not need to save manually.

## Editing the data file

XxLilChatxX data is saved automatically at `[JAR file location]/data/xxlilchatxx.txt`. Advanced users are welcome to update data directly by editing that data file.

:exclamation: **Caution:** If your edits make a line in the data file invalid, XxLilChatxX will skip that line (with a warning) the next time it loads, rather than crashing. Still, we recommend backing up the file before editing it directly.

## FAQ

**Q: How do I transfer my data to another computer?**

A: Install XxLilChatxX on the other computer and copy over your `data/xxlilchatxx.txt` file into the same relative location (a `data` folder next to the JAR file).

## Command summary

| Action | Format | Example |
|---|---|---|
| Todo | `todo DESCRIPTION` | `todo read book` |
| Deadline | `deadline DESCRIPTION /by DATE` | `deadline return book /by Sunday` |
| Event | `event DESCRIPTION /from START /to END` | `event meeting /from Mon 2pm /to 4pm` |
| List | `list` | `list` |
| Mark | `mark INDEX` | `mark 2` |
| Unmark | `unmark INDEX` | `unmark 2` |
| Delete | `delete INDEX` | `delete 3` |
| Find | `find KEYWORD` | `find book` |
| Exit | `bye` | `bye` |