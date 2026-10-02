# Buddy User Guide

Buddy is a friendly command-line chatbot that helps you keep track of your
tasks — todos, deadlines, and events. Type commands, press enter, and
Buddy takes care of remembering everything for you between sessions.

```
____________________________________________________________
 ____   _   _  ____   ____  __   __
| __ ) | | | ||  _ \ |  _ \ \ \ / /
|  _ \ | | | || | | || | | | \ V /
| |_) || |_| || |_| || |_| |  | |
|____/  \___/ |____/ |____/   |_|

 Hello! I'm Buddy.
 What can I do for you?
____________________________________________________________
```

## Getting started

1. Make sure you have **Java 25** installed.
2. Download the latest `buddy.jar` from the project's GitHub releases.
3. Open a terminal in the folder containing `buddy.jar` and run:
   ```
   java -jar buddy.jar
   ```
4. Type a command at the prompt and press enter. Buddy saves your tasks
   automatically, so they'll still be there the next time you start it.

## Features

### Adding a todo: `todo`

Adds a simple task with just a description — no date or time attached.

Example: `todo read book`

```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
```

### Adding a deadline: `deadline`

Adds a task that needs to be done by a specific date and time.

Example: `deadline return book /by 2019-12-02 1800`

The date and time must be given as `yyyy-mm-dd HHmm` (24-hour time), e.g.
`2019-12-02 1800` for 2 December 2019, 6:00 PM.

```
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Dec 02 2019, 6:00 PM)
 Now you have 2 tasks in the list.
____________________________________________________________
```

### Adding an event: `event`

Adds a task that spans a period of time, from a start to an end.

Example: `event project meeting /from Mon 2pm /to 4pm`

```
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
```

### Listing all tasks: `list`

Shows every task currently stored, numbered from 1, in the order they
were added.

Example: `list`

```
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[D][ ] return book (by: Dec 02 2019, 6:00 PM)
 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
```

### Marking a task as done: `mark`

Marks the task at the given position (from `list`) as done.

Example: `mark 1`

```
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] read book
____________________________________________________________
```

### Marking a task as not done: `unmark`

Marks the task at the given position as not done.

Example: `unmark 1`

```
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] read book
____________________________________________________________
```

### Deleting a task: `delete`

Removes the task at the given position from the list.

Example: `delete 2`

```
____________________________________________________________
 Noted. I've removed this task:
   [D][ ] return book (by: Dec 02 2019, 6:00 PM)
 Now you have 2 tasks in the list.
____________________________________________________________
```

### Finding tasks: `find`

Shows only the tasks whose description contains the given keyword.

Example: `find book`

```
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] read book
 2.[D][ ] return book (by: Dec 02 2019, 6:00 PM)
____________________________________________________________
```

If nothing matches, Buddy lets you know instead of showing an empty list:

```
____________________________________________________________
 Buddy couldnt find any tasks with that keyword.
____________________________________________________________
```

### Exiting Buddy: `bye`

Ends the session. Your tasks have already been saved as you added,
marked, and deleted them, so nothing more needs to happen before you
close the terminal.

Example: `bye`

```
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## Command summary

| Action | Format | Example |
|---|---|---|
| Add a todo | `todo <description>` | `todo read book` |
| Add a deadline | `deadline <description> /by <yyyy-mm-dd HHmm>` | `deadline return book /by 2019-12-02 1800` |
| Add an event | `event <description> /from <start> /to <end>` | `event project meeting /from Mon 2pm /to 4pm` |
| List all tasks | `list` | `list` |
| Mark a task done | `mark <task number>` | `mark 1` |
| Mark a task not done | `unmark <task number>` | `unmark 1` |
| Delete a task | `delete <task number>` | `delete 2` |
| Find tasks by keyword | `find <keyword>` | `find book` |
| Exit Buddy | `bye` | `bye` |

