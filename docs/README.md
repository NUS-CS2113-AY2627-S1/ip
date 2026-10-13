# Socrates User Guide

Socrates is a command-line chatbot that helps you track your tasks — todos, deadlines, and events — through simple text commands, speaking (loosely) in the voice of the philosopher.

## Quick Start

1. Ensure you have Java 25 installed.
2. Download the latest `socrates.jar`.
3. Open a terminal, `cd` into the folder containing the jar, and run:
   ```
   java -jar socrates.jar
   ```
4. Type a command and press Enter. Type `help` at any time to see the full command list.

Your tasks are saved automatically to `data/socrates.txt` (created next to wherever you run the jar from) and reloaded the next time you start the program.

## Features

### Listing all tasks: `list`

Shows every task currently in your list, numbered.

Example: `list`
```
1.[T][ ] read book
2.[D][ ] submit form (by: Oct 15 2026)
3.[E][ ] meeting (from: Oct 20 2026 to: Oct 21 2026)
```

### Adding a todo: `todo`

Adds a simple task with no associated date.

Example: `todo read book`
```
Got it. Ive added this task:
   [T][ ] read book
Now you have 1 tasks in the list
```

### Adding a deadline: `deadline`

Adds a task that must be completed by a given date.

Example: `deadline submit form /by 2026-10-15`
```
Got it. Ive added this task:
   [D][ ] submit form (by: Oct 15 2026)
Now you have 1 tasks in the list
```

Dates must be given as `yyyy-MM-dd`.

### Adding an event: `event`

Adds a task that spans a start and end date.

Example: `event project meeting /from 2026-10-20 /to 2026-10-21`
```
Got it. Ive added this task:
   [E][ ] project meeting (from: Oct 20 2026 to: Oct 21 2026)
Now you have 1 tasks in the list
```

Dates must be given as `yyyy-MM-dd`.

### Marking a task as done: `mark`

Marks the task at the given position (as shown by `list`) as done.

Example: `mark 1`
```
Nice! I've marked this task as done:
   [T][X] read book
```

### Unmarking a task: `unmark`

Marks the task at the given position as not done.

Example: `unmark 1`
```
Okay, I've marked this task as not done yet:
   [T][ ] read book
```

### Deleting a task: `delete`

Removes the task at the given position from your list.

Example: `delete 1`
```
Noted. I've removed this task:
   [T][ ] read book
now you have 0 tasks in the list
```

### Finding tasks: `find`

Lists all tasks whose description contains the given keyword (case-insensitive).

Example: `find book`
```
Here are the matching tasks in your list:
1.[T][ ] read book
```

### Viewing help: `help`

Shows the full list of commands.

Example: `help`

### Exiting: `bye`

Ends the conversation. Your tasks have already been saved automatically before this point, so nothing further is needed.

Example: `bye`
```
Farewell, friend. Remember - the unexamined task list is not worth keeping!
```

## Saving the data

Your task list is saved automatically to `data/socrates.txt` after every command that changes it — there's no need to save manually. The file is created relative to wherever you run the jar from.

## FAQ

**Q: How do I transfer my data to another computer?**

A: Copy the `data/socrates.txt` file to the same relative location next to `socrates.jar` on the other computer.

**Q: What happens if I edit `data/socrates.txt` by hand and make a mistake?**

A: Any line Socrates can't understand is skipped with a warning when the program next starts, rather than crashing — but it's safest to let the program manage the file itself.

## Command Summary

| Action | Format | Example |
|---|---|---|
| List | `list` | `list` |
| Todo | `todo {description}` | `todo read book` |
| Deadline | `deadline {description} /by {yyyy-MM-dd}` | `deadline submit form /by 2026-10-15` |
| Event | `event {description} /from {yyyy-MM-dd} /to {yyyy-MM-dd}` | `event meeting /from 2026-10-20 /to 2026-10-21` |
| Mark | `mark {index}` | `mark 1` |
| Unmark | `unmark {index}` | `unmark 1` |
| Delete | `delete {index}` | `delete 1` |
| Find | `find {keyword}` | `find book` |
| Help | `help` | `help` |
| Exit | `bye` | `bye` |
