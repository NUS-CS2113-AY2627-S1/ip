# Buddy User Guide

Buddy is a simple chatbot that helps you track tasks — todos, deadlines, and
events — from the command line. Type a command, press Enter, and Buddy takes
care of the rest, including remembering your tasks the next time you start it.

## Quick Start

1. Ensure you have Java 25 installed.
2. Download the latest `buddy.jar` from the [releases page](https://github.com/MohamedFarsat/ip/releases).
3. Copy it into an empty folder (Buddy will create a `data` folder next to it to save your tasks).
4. Open a terminal in that folder and run:
   ```
   java -jar buddy.jar
   ```
5. You should see Buddy's greeting. Type a command and press Enter to try it out, e.g. `todo read book`.

> **Notation used in this guide:** words in `<angle brackets>` are parameters you
> supply, e.g. in `todo <description>`, `<description>` is replaced with your own text.

## Features

### Adding a todo: `todo`

Adds a task with no date or time attached.

Format: `todo <description>`

Example: `todo read book`

```text
Got it. I've added this task:
  [T][ ] read book
Now you have 1 task in the list.
```

### Adding a deadline: `deadline`

Adds a task that needs to be done by a specific date or time.

Format: `deadline <description> /by <date or time>`

Example: `deadline return book /by June 6th`

```text
Got it. I've added this task:
  [D][ ] return book (by: June 6th)
Now you have 2 tasks in the list.
```

### Adding an event: `event`

Adds a task that happens during a specific period.

Format: `event <description> /from <start> /to <end>`

Example: `event project meeting /from Aug 6th 2pm /to 4pm`

```text
Got it. I've added this task:
  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
Now you have 3 tasks in the list.
```

### Listing all tasks: `list`

Shows every task currently in your list, numbered from 1.

Format: `list`

```text
Here are the tasks in your list:
1. [T][ ] read book
2. [D][ ] return book (by: June 6th)
3. [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
```

### Marking a task as done: `mark`

Marks the given task as done. `[ ]` in the task's display becomes `[X]`.

Format: `mark <task number>`

Example: `mark 1`

```text
Nice! I've marked this task as done:
  [T][X] read book
```

### Marking a task as not done: `unmark`

Marks the given task as not done. `[X]` in the task's display becomes `[ ]`.

Format: `unmark <task number>`

Example: `unmark 1`

```text
OK, I've marked this task as not done yet:
  [T][ ] read book
```

### Deleting a task: `delete`

Removes the given task from the list.

Format: `delete <task number>`

Example: `delete 3`

```text
Noted. I've removed this task:
  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
Now you have 2 tasks in the list.
```

### Finding tasks: `find`

Shows every task whose description contains the given keyword. The search
is case-insensitive and matches keywords found anywhere in the description.

Format: `find <keyword>`

Example: `find book`

```text
Here are the matching tasks in your list:
1. [T][ ] read book
2. [D][ ] return book (by: June 6th)
```

### Exiting: `bye`

Exits Buddy.

Format: `bye`

### Saving data

Buddy automatically saves your task list to `./data/buddy.txt` (relative to
wherever you run it from) after every command that changes it, and loads it
back in the next time Buddy starts. There's nothing you need to do manually,
and no save file is created until your first change.

## Command Summary

| Action | Format | Example |
|---|---|---|
| Add a todo | `todo <description>` | `todo read book` |
| Add a deadline | `deadline <description> /by <date/time>` | `deadline return book /by June 6th` |
| Add an event | `event <description> /from <start> /to <end>` | `event meeting /from Mon 2pm /to 4pm` |
| List tasks | `list` | `list` |
| Mark a task done | `mark <task number>` | `mark 1` |
| Unmark a task | `unmark <task number>` | `unmark 1` |
| Delete a task | `delete <task number>` | `delete 3` |
| Find tasks | `find <keyword>` | `find book` |
| Exit | `bye` | `bye` |
