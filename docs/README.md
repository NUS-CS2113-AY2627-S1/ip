# Anson User Guide

Anson is a simple desktop chatbot that helps you keep track of your tasks. You type commands, and Anson adds, lists, finds and updates your tasks. Your tasks are saved automatically, so they are still there the next time you start Anson.

* [Quick start](#quick-start)
* [Features](#features)
    * [Adding a todo: `todo`](#adding-a-todo-todo)
    * [Adding a deadline: `deadline`](#adding-a-deadline-deadline)
    * [Adding an event: `event`](#adding-an-event-event)
    * [Listing all tasks: `list`](#listing-all-tasks-list)
    * [Marking a task as done: `mark`](#marking-a-task-as-done-mark)
    * [Marking a task as not done: `unmark`](#marking-a-task-as-not-done-unmark)
    * [Deleting a task: `delete`](#deleting-a-task-delete)
    * [Finding tasks: `find`](#finding-tasks-find)
    * [Exiting Anson: `bye`](#exiting-anson-bye)
    * [Saving your tasks](#saving-your-tasks)
* [FAQ](#faq)
* [Command summary](#command-summary)

## Quick start

1. Make sure you have Java 25 or newer installed. You can check by running `java -version` in a command window.
2. Download `Anson.jar` from the [latest release](https://github.com/neil0618/ip/releases).
3. Copy `Anson.jar` into an empty folder.
4. Open a command window in that folder and run `java -jar "Anson.jar"`.
5. You should see the Anson banner and a greeting. Type a command and press Enter. Try these:
    * `todo read book` adds a todo.
    * `list` shows all your tasks.
    * `bye` exits Anson.

## Features

**Notes about the command format:**

* Words in UPPER_CASE are values you supply. For example, in `todo DESCRIPTION`, you can type `todo read book`.
* Command words are case-sensitive and must be typed in lower case, for example `todo`, not `Todo`.
* Dates and times are plain text, so you can write them however you like, for example `Sunday`, `June 6th` or `Mon 2pm`.
* The `|` character cannot be used anywhere in a command.
* Task numbers refer to the numbers shown by the `list` command.

### Adding a todo: `todo`

Adds a task with no date or time.

Format: `todo DESCRIPTION`

Example: `todo read book`

```
Got it! I've added this task:
  [T][ ] read book
Number of tasks in the list: 1
```

### Adding a deadline: `deadline`

Adds a task that needs to be done by a certain date or time.

Format: `deadline DESCRIPTION /by DATE_TIME`

Example: `deadline return book /by June 6th`

```
Got it! I've added this task:
  [D][ ] return book (by: June 6th)
Number of tasks in the list: 2
```

### Adding an event: `event`

Adds a task that starts and ends at specific times.

Format: `event DESCRIPTION /from START /to END`

Example: `event project meeting /from Aug 6th 2pm /to 4pm`

```
Got it! I've added this task:
  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
Number of tasks in the list: 3
```

### Listing all tasks: `list`

Shows every task in your list, in the order you added them.

Format: `list`

```
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: June 6th)
3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
```

In the task display, `[T]`, `[D]` and `[E]` show the task type (todo, deadline, event). `[X]` means the task is done, and `[ ]` means it is not done yet.

### Marking a task as done: `mark`

Marks the task with the given number as done.

Format: `mark TASK_NUMBER`

Example: `mark 1`

```
I have marked this task as done:
[T][X] read book
```

### Marking a task as not done: `unmark`

Marks the task with the given number as not done yet.

Format: `unmark TASK_NUMBER`

Example: `unmark 1`

```
I have marked this task as not done yet:
[T][ ] read book
```

### Deleting a task: `delete`

Removes the task with the given number from your list. The remaining tasks are renumbered.

Format: `delete TASK_NUMBER`

Example: `delete 2`

```
Noted. I've removed this task:
  [D][ ] return book (by: June 6th)
Now you have 2 tasks in the list.
```

### Finding tasks: `find`

Shows the tasks whose descriptions contain a keyword.

Format: `find KEYWORD`

* The search is not case-sensitive, so `book` matches `Book`.
* Only the description is searched, not dates or times.
* A task matches if its description contains the keyword anywhere, so `meet` matches `project meeting`.
* The matching tasks are numbered from 1 in the results. These numbers are not the same as the numbers used by `mark`, `unmark` and `delete`. Use `list` to see those.

Example: `find book`

```
Here are the matching tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: June 6th)
```

If nothing matches, Anson says `No matching tasks found.`

### Exiting Anson: `bye`

Exits the chatbot.

Format: `bye`

### Saving your tasks

Anson saves your tasks automatically after every change. There is nothing you need to do.

* Tasks are stored in a text file at `data/anson.txt`, inside the folder where you run Anson. Anson creates the `data` folder and file for you if they do not exist yet.
* The next time you start Anson in the same folder, your tasks are loaded automatically.
* You can edit the file by hand, but be careful. If a line in the file is not in the expected format, Anson skips it, shows a warning when it starts, and drops that line the next time it saves. If you want to keep a copy of your data, back up `data/anson.txt` before editing it.

## FAQ

**Q: How do I move my tasks to another computer?**

A: Copy the `data/anson.txt` file into a `data` folder next to `Anson.jar` on the other computer.

**Q: Why does Anson say "Sorry, the `|` character can't be used in a command!"?**

A: Anson uses `|` internally to separate fields in the save file, so it cannot be part of a task. Use another character instead.

**Q: I typed `mark 5` and got an error. Why?**

A: The task number must be between 1 and the number of tasks in your list. Use `list` to check the numbers.

**Q: Why does Anson say it does not know what I mean?**

A: The command word was not recognized. Check the spelling, and make sure it is in lower case. See the command summary below.

## Command summary

| Action | Format | Example |
|--------|--------|---------|
| Add todo | `todo DESCRIPTION` | `todo read book` |
| Add deadline | `deadline DESCRIPTION /by DATE_TIME` | `deadline return book /by June 6th` |
| Add event | `event DESCRIPTION /from START /to END` | `event meeting /from Mon 2pm /to 4pm` |
| List | `list` | `list` |
| Mark done | `mark TASK_NUMBER` | `mark 1` |
| Mark not done | `unmark TASK_NUMBER` | `unmark 1` |
| Delete | `delete TASK_NUMBER` | `delete 2` |
| Find | `find KEYWORD` | `find book` |
| Exit | `bye` | `bye` |