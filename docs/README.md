# Eric User Guide

Eric is a command-line chatbot that keeps track of your tasks. You type a command and press Enter, and Eric replies. Your tasks are saved automatically, so they are still there the next time you start Eric.

* [Quick start](#quick-start)
* [Reading this guide](#reading-this-guide)
* [Features](#features)
  * [Adding a todo: `todo`](#adding-a-todo-todo)
  * [Adding a deadline: `deadline`](#adding-a-deadline-deadline)
  * [Adding an event: `event`](#adding-an-event-event)
  * [Listing tasks: `list`](#listing-tasks-list)
  * [Finding tasks: `find`](#finding-tasks-find)
  * [Marking and unmarking tasks: `mark` and `unmark`](#marking-and-unmarking-tasks-mark-and-unmark)
  * [Deleting a task: `delete`](#deleting-a-task-delete)
  * [Exiting: `bye`](#exiting-bye)
* [When something goes wrong](#when-something-goes-wrong)
* [Saving and loading](#saving-and-loading)
* [Command summary](#command-summary)
* [FAQ](#faq)

## Quick start

1. Make sure you have **Java 25** installed.
1. Build Eric as described in the project's [README](../README.md#building-and-running-a-jar-file). This creates `build/libs/eric.jar`.
1. In a terminal, go to the folder where you want your tasks to be saved, and start Eric with `java -jar <path to>/eric.jar`.
1. Eric greets you:
   ```
   ____________________________________________________________
   ███████╗██████╗ ██╗ ██████╗
   ██╔════╝██╔══██╗██║██╔════╝
   █████╗  ██████╔╝██║██║
   ██╔══╝  ██╔══██╗██║██║
   ███████╗██║  ██║██║╚██████╗
   ╚══════╝╚═╝  ╚═╝╚═╝ ╚═════╝

   Hello! I'm Eric.
   What can I do for you?
   ____________________________________________________________
   ```
1. Type a command and press Enter, for example `todo read book`. Type `bye` to exit.

## Reading this guide

* Words in `UPPER_CASE` are for you to fill in. In `todo DESCRIPTION`, you type something like `todo read book`.
* Eric shows every reply between two lines of underscores. The examples below leave those lines out, to save space.
* In a task such as `[D][X] return book (by: June 6th)`, the first box is the type of the task (`T` for todo, `D` for deadline, `E` for event), and the second box is `X` if the task is done and empty if it is not.

**Things that apply to every command**

* Command words are lower case and must be typed exactly: `todo` works, `TODO` does not.
* Spaces before and after what you type are ignored.
* `list` and `bye` take nothing after them, so `list now` is not understood.
* Dates and times are just text. Eric does not check them, so `/by Sunday` and `/by no idea :-p` are both fine.
* Task numbers start at 1 and are written as plain whole numbers, such as `2`. Forms like `+2` and `02` are not accepted.
* You cannot put ` | ` (a bar with a space on each side) in a task, because Eric uses it in the file where tasks are saved. A bar without spaces, like `a|b`, is fine.

## Features

### Adding a todo: `todo`

Adds a task with no date or time.

Format: `todo DESCRIPTION`

Example: `todo read book`

```
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
```

### Adding a deadline: `deadline`

Adds a task that must be done by a certain time.

Format: `deadline DESCRIPTION /by DATE`

* `/by` must be a separate word, with a space before and after it.

Example: `deadline return book /by June 6th`

```
 Got it. I've added this task:
   [D][ ] return book (by: June 6th)
 Now you have 2 tasks in the list.
```

### Adding an event: `event`

Adds a task that starts at one time and ends at another.

Format: `event DESCRIPTION /from START /to END`

* `/from` must come before `/to`, and both must be separate words.

Example: `event project meeting /from Aug 6th 2pm /to 4pm`

```
 Got it. I've added this task:
   [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
 Now you have 3 tasks in the list.
```

### Listing tasks: `list`

Shows all your tasks, in the order you added them, with their numbers.

Format: `list`

```
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[D][ ] return book (by: June 6th)
 3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
 4.[T][ ] join sports club
```

### Finding tasks: `find`

Shows the tasks whose description contains a keyword.

Format: `find KEYWORD`

* Upper and lower case are treated as the same, so `find BOOK` finds `read book`.
* Everything after `find` is the keyword, so it can have several words. `find read book` looks for those words together, in that order.
* Only the description is searched, not the dates and times.
* The keyword is plain text. Characters such as `.` and `*` mean just themselves.
* The results are numbered from 1 in the order of the results. These numbers are **not** the task numbers used by `mark`, `unmark` and `delete`. Use `list` to see those.

Example: `find book`

```
 Here are the matching tasks in your list:
 1.[T][X] read book
 2.[D][ ] return book (by: June 6th)
```

If no task matches, Eric says so:

```
 There are no matching tasks in your list.
```

### Marking and unmarking tasks: `mark` and `unmark`

Marks a task as done, or as not done yet.

Format: `mark INDEX` or `unmark INDEX`

* `INDEX` is the number of the task in the list, as shown by `list`.

Example: `mark 1`

```
 Nice! I've marked this task as done:
   [T][X] read book
```

Example: `unmark 2`

```
 OK, I've marked this task as not done yet:
   [D][ ] return book (by: June 6th)
```

### Deleting a task: `delete`

Removes a task from the list.

Format: `delete INDEX`

* `INDEX` is the number of the task in the list, as shown by `list`.
* The tasks after it move up, so their numbers change. Use `list` to see the new numbers.
* There is no undo. If you delete a task by mistake, add it again.

Example: `delete 3`

```
 Noted. I've removed this task:
   [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
 Now you have 3 tasks in the list.
```

### Exiting: `bye`

Closes Eric.

Format: `bye`

```
 Bye. Hope to see you again soon!
```

Your tasks are already saved, so there is nothing else to do before you exit.

## When something goes wrong

If Eric cannot carry out a command, it does not change your tasks. It says what is wrong and how to fix it, and you can simply type the next command. Eric does not close because of a mistake.

A few examples:

| You type | Eric replies |
|---|---|
| `todo` | `OOPS!!! The description of a todo is empty.` and `Type a description after "todo", e.g. todo read book` |
| `deadline return book` | `OOPS!!! A deadline needs a /by date, but I couldn't find one.` and `Use the format: deadline DESCRIPTION /by DATE, e.g. deadline return book /by Sunday` |
| `event party /from Mon` | `OOPS!!! An event needs a /to time, but I couldn't find one.` and `Use the format: event DESCRIPTION /from START /to END, e.g. event project meeting /from Mon 2pm /to 4pm` |
| `mark 99` | `OOPS!!! Task 99 doesn't exist.` and `Choose a number from 1 to 3. Type list to see the tasks.` |
| `mark abc` | `OOPS!!! "abc" is not a valid task number.` and `Use a plain whole number (no + sign or leading zeros), e.g. mark 2. Type list to see the task numbers.` |
| `delete` | `OOPS!!! The task number is missing.` and `Type the number of a task after "delete", e.g. delete 2` |
| `find` | `OOPS!!! The keyword to search for is missing.` and `Type a keyword after "find", e.g. find book` |
| `blah` | `OOPS!!! I don't know the command "blah".` and `Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.` |

## Saving and loading

Eric saves your tasks to the file `data/duke.txt` every time the list changes, and loads them again when it starts. The file is in the `data` folder next to where you run Eric. The folder and the file are created automatically, so nothing is needed on a first run.

Each line of the file is one task:

```
T | 1 | read book
D | 0 | return book | June 6th
E | 0 | project meeting | Mon 2pm | 4pm
```

* The first column is the type: `T` (todo), `D` (deadline) or `E` (event).
* The second column is `1` if the task is done and `0` if it is not.
* The remaining columns are the description, then the date for a deadline, or the start and end for an event.

If the file cannot be used, for example because a line is in the wrong format, the file is not text, or `data/duke.txt` is a folder, Eric does not load any of it. It tells you why (for a wrong format, every bad line and its reason) and starts with an empty list. **The file is replaced the next time your tasks change**, so to keep it, close Eric and fix or move the file first.

## Command summary

| Command | Format | Example | What it does |
|---|---|---|---|
| `todo` | `todo DESCRIPTION` | `todo read book` | Adds a task with no date. |
| `deadline` | `deadline DESCRIPTION /by DATE` | `deadline return book /by June 6th` | Adds a task that must be done by a date. |
| `event` | `event DESCRIPTION /from START /to END` | `event project meeting /from Aug 6th 2pm /to 4pm` | Adds a task that starts and ends at given times. |
| `list` | `list` | `list` | Shows all tasks, with their numbers. |
| `find` | `find KEYWORD` | `find book` | Shows the tasks whose description contains the keyword. |
| `mark` | `mark INDEX` | `mark 1` | Marks a task as done. |
| `unmark` | `unmark INDEX` | `unmark 2` | Marks a task as not done yet. |
| `delete` | `delete INDEX` | `delete 3` | Removes a task from the list. |
| `bye` | `bye` | `bye` | Closes Eric. |

## FAQ

**How do I move my tasks to another computer?**
Copy the file `data/duke.txt` into a `data` folder next to where you will run Eric on the other computer. Eric loads it when it starts.

**Can I edit the saved file by hand?**
Yes, it is a plain text file, but keep each line in the format shown in [Saving and loading](#saving-and-loading). If any line is wrong, Eric will not load the file and will tell you which lines are wrong.

**Eric says it couldn't load my saved tasks. What should I do?**
Read the message, which says what is wrong with the file. Close Eric, then fix the file, or move it somewhere safe if you want to keep it. If you do not, the file is replaced as soon as you add, change or delete a task.

**Is there a limit to how many tasks I can have?**
No. Eric keeps as many tasks as you add.

**Does Eric understand real dates, such as "tomorrow" or "6/6/2026"?**
No. A date or time is just text that Eric shows back to you. It does not check it, sort by it or remind you of it.

**Why is the number I see after `find` not the one I should use with `delete`?**
`find` numbers its results from 1, so they are not the task numbers in your full list. Type `list` to see the real numbers, then use those with `mark`, `unmark` and `delete`.

**Can I mark or delete several tasks at once?**
No. Each `mark`, `unmark` and `delete` works on one task. Type the command once for each task.

**Can I undo a `delete`?**
No. Add the task again with `todo`, `deadline` or `event`.

**Why was my command not understood?**
The most common reasons are a capital letter in the command word (`Todo` instead of `todo`), extra words after `list` or `bye`, or a missing `/by`, `/from` or `/to`. Eric's reply tells you what was wrong and shows an example of the right format.

**Can I use a `|` in my task?**
A `|` is fine unless it has a space on both sides. ` | ` is used in the save file, so Eric refuses it and tells you. For example, write `a|b` or use a comma instead.

**Do I need to save my tasks before I exit?**
No. Eric saves after every change. If the input ends without you typing `bye`, Eric still exits normally.

