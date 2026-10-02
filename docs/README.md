# Nick User Guide

Nick is a friendly command-line chatbot that helps you keep track of your tasks:
todos, deadlines, and events. Your tasks are saved automatically, so they are
still there the next time you start Nick.

## Getting started

1. Make sure you have Java 25 installed.
2. Download `nick.jar` from the latest release.
3. Open a command window in the folder containing the JAR and run:

   ```
   java -jar "nick.jar"
   ```

4. Type a command after the greeting and press Enter.

Your tasks are stored in `data/nick.txt` (created automatically in the folder
you run Nick from).

## Command summary

| Action | Format | Example |
| --- | --- | --- |
| Add a todo | `todo DESCRIPTION` | `todo read book` |
| Add a deadline | `deadline DESCRIPTION /by WHEN` | `deadline return book /by 2019-12-02` |
| Add an event | `event DESCRIPTION /from START /to END` | `event meeting /from Mon 2pm /to 4pm` |
| List tasks | `list` | `list` |
| Mark as done | `mark NUMBER` | `mark 2` |
| Mark as not done | `unmark NUMBER` | `unmark 2` |
| Delete a task | `delete NUMBER` | `delete 3` |
| Find tasks | `find KEYWORD` | `find book` |
| Exit | `bye` | `bye` |

## Features

### Adding a todo: `todo`

Adds a task with no date attached.

Format: `todo DESCRIPTION`

Example: `todo read book`

```
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________
```

### Adding a deadline: `deadline`

Adds a task that must be done by a certain time.

Format: `deadline DESCRIPTION /by WHEN`

If `WHEN` is written as a date in `yyyy-MM-dd` form (e.g. `2019-12-02`), Nick
understands it as a real date and shows it in a friendlier format
(e.g. `Dec 02 2019`). Any other text is kept as-is.

Example: `deadline return book /by 2019-12-02`

```
    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] return book (by: Dec 02 2019)
     Now you have 2 tasks in the list.
    ____________________________________________________________
```

### Adding an event: `event`

Adds a task that spans a start and end time.

Format: `event DESCRIPTION /from START /to END`

Example: `event project meeting /from Mon 2pm /to 4pm`

```
    ____________________________________________________________
     Got it. I've added this task:
       [E][ ] project meeting (from: Mon 2pm to: 4pm)
     Now you have 3 tasks in the list.
    ____________________________________________________________
```

### Listing all tasks: `list`

Shows every task, numbered, with its type and done status.

Format: `list`

```
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read book
     2.[D][ ] return book (by: Dec 02 2019)
     3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
    ____________________________________________________________
```

The two boxes show the task type (`T`/`D`/`E`) and whether it is done (`X`) or
not (blank).

### Marking and unmarking: `mark`, `unmark`

Marks a task as done, or back to not done. Use the number shown by `list`.

Format: `mark NUMBER` / `unmark NUMBER`

Example: `mark 2`

```
    ____________________________________________________________
     Nice! I've marked this task as done:
       [D][X] return book (by: Dec 02 2019)
    ____________________________________________________________
```

### Deleting a task: `delete`

Removes a task from the list. Use the number shown by `list`.

Format: `delete NUMBER`

Example: `delete 3`

```
    ____________________________________________________________
     Noted. I've removed this task:
       [E][ ] project meeting (from: Mon 2pm to: 4pm)
     Now you have 2 tasks in the list.
    ____________________________________________________________
```

### Finding tasks: `find`

Lists all tasks whose description contains the given keyword (case-insensitive).

Format: `find KEYWORD`

Example: `find book`

```
    ____________________________________________________________
     Here are the matching tasks in your list:
     1.[T][ ] read book
     2.[D][ ] return book (by: Dec 02 2019)
    ____________________________________________________________
```

### Exiting: `bye`

Exits the chatbot.

Format: `bye`

```
    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________
```

## Saving the data

Nick saves your tasks to `data/nick.txt` automatically after every change, and
loads them when it starts. There is no need to save manually. If the file is
missing on first run, Nick starts with an empty list and creates the file.
