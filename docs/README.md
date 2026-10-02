# Dandelion User Guide

Dandelion is a command-line task manager for creating, tracking, searching, and
maintaining tasks.

## Contents

- [Getting started](#getting-started)
- [Command reference](#command-reference)
- [Task status](#task-status)
- [Searching tasks](#searching-tasks)
- [Data persistence](#data-persistence)
- [Validation messages](#validation-messages)

## Getting started

### Requirements

- **JDK 25**
- A terminal opened at the repository root

### Run Dandelion

Start the application with Gradle:

```bash
./gradlew run
```

To build and run the executable JAR instead:

```bash
./gradlew shadowJar
java -jar build/libs/dandelion-1.0.0-all.jar
```

When Dandelion starts, it loads saved tasks before displaying the command prompt.

## Command reference

| Command | Format | Description |
| --- | --- | --- |
| `todo` | `todo DESCRIPTION` | Adds a task without a deadline or time period. |
| `deadline` | `deadline DESCRIPTION /by yyyy-MM-dd` | Adds a task with a date deadline. |
| `event` | `event DESCRIPTION /from START /to END` | Adds a task with a time period. |
| `list` | `list` | Displays all tasks in their current order. |
| `mark` | `mark NUMBER` | Marks a task as done. |
| `unmark` | `unmark NUMBER` | Marks a task as not done. |
| `delete` | `delete NUMBER` | Deletes a task. |
| `find` | `find KEYWORD` | Displays tasks whose descriptions contain the keyword. |
| `bye` | `bye` | Exits the application. |

### Add tasks

Use `todo`, `deadline`, or `event` with the required arguments:

```text
todo read book
deadline return book /by 2019-06-06
event team meeting /from Monday 10am /to Monday 11am
```

Deadline input must use `yyyy-MM-dd` format. Dandelion displays the date as
`MMM d yyyy`.

Dandelion confirms each successful addition:

```text
  bot  › added: [T][ ] read book
  bot  › added: [D][ ] return book (by: Jun 6 2019)
  bot  › added: [E][ ] team meeting (from: Monday 10am to: Monday 11am)
```

### List tasks

Enter:

```text
list
```

Example output:

```text
  bot  › 1.[T][ ] read book
         2.[D][ ] return book (by: Jun 6 2019)
         3.[E][ ] team meeting (from: Monday 10am to: Monday 11am)
```

### Mark, unmark, and delete tasks

Commands use the task number shown by `list`:

```text
mark 1
unmark 1
delete 3
```

The task list is renumbered automatically after a deletion.

## Task status

The status marker appears after the task type:

- `[ ]` means the task is not done.
- `[X]` means the task is done.

For example:

```text
1.[T][ ] read book
```

After `mark 1`, the same task appears as:

```text
1.[T][X] read book
```

## Searching tasks

Use `find` to search task descriptions. The search is case-insensitive and can
contain more than one word:

```text
find book
```

Example output:

```text
    ____________________________________________________________
     Here are the matching tasks in your list:
     1.[T][X] read book
     2.[D][X] return book (by: Jun 6 2019)
    ____________________________________________________________
```

Only task descriptions are searched; deadline and event details are displayed
but are not search fields.

## Data persistence

Dandelion stores tasks in `data/dandelion.txt`.

- Tasks are loaded when the application starts.
- New tasks are saved after they are added.
- Status changes are saved after `mark` and `unmark`.
- Deletions are saved after `delete`.

> [!NOTE]
> The `data` directory and task file are created automatically when Dandelion
> saves the first task.

## Validation messages

Dandelion keeps the session running when a command is invalid. For example:

```text
todo
  bot  › Todo description cannot be empty.

mark abc
  bot  › Please enter a task number after the command.

find
  bot  › Please enter a keyword after the command.
```
