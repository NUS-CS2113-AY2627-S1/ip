# Aigis

Aigis is a command-line chatbot for managing tasks, deadlines, and events.
You can add tasks, update their completion status, search for tasks, and
save your task list locally between sessions.

## Getting Started

### Prerequisites

- [JDK 25](https://www.oracle.com/java/technologies/downloads/)
- IntelliJ IDEA or another Java IDE

### Running Aigis in IntelliJ

1. Open IntelliJ IDEA.
2. Select **File** > **Open** and choose the project directory.
3. Configure the project to use **JDK 25**. Set the project language level to
   **SDK default**.
4. Open `src/main/java/aigis/Aigis.java`.
5. Right-click the file and select **Run `Aigis.main()`**.

When Aigis starts, it displays a welcome message and waits for commands.
Tasks are saved in `data/aigis.txt` when the application exits.

## Command Summary

| Command | Purpose |
| --- | --- |
| `todo <description>` | Add a task without a date or time |
| `deadline <description> /by <date>` | Add a task with a due date |
| `event <description> /from <start> /to <end>` | Add a task for a time period |
| `list` | Display all tasks |
| `find <keyword>` | Display tasks whose names contain the keyword |
| `mark <task number>` | Mark a task as completed |
| `unmark <task number>` | Mark a task as not completed |
| `delete <task number>` | Remove a task |
| `bye` | Exit Aigis and save the task list |

Task numbers are shown by the `list` command and start from `1`.

## Adding Tasks

### Add a todo

Use `todo` for a task that has no deadline or event time:

```text
todo read chapter 3
```

Aigis adds the task and displays:

```text
New objective: read chapter 3
```

### Add a deadline

Use `deadline` with the `/by` marker. Dates must use the
`yyyy-MM-dd` format:

```text
deadline submit report /by 2026-09-30
```

Aigis displays the deadline in a readable format:

```text
New objective: submit report ( by: Sep 30 2026 )
```

### Add an event

Use `event` with both `/from` and `/to` markers. Times are entered as text,
so you can use values such as `9am`, `14:00`, or `Monday morning`:

```text
event team meeting /from 9am /to 10am
```

Aigis displays:

```text
New objective: team meeting( from: 9am to: 10am )
```

## Viewing and Searching Tasks

### List all tasks

Use `list` to display every stored task:

```text
list
```

Example output:

```text
1. [T] [ ] read chapter 3
2. [D] [ ] submit report ( by: Sep 30 2026 )
3. [E] [ ] team meeting( from: 9am to: 10am )
```

`[ ]` means that a task is incomplete, while `[X]` means that it is
completed.

### Find tasks

Use `find` followed by a keyword to display tasks whose names contain that
keyword:

```text
find report
```

The search checks the task description and is case-sensitive. If no task
matches, Aigis displays no task entries.

## Updating Tasks

### Mark a task as completed

Use the task's number from the task list:

```text
mark 2
```

### Mark a task as incomplete

Use `unmark` followed by the task number:

```text
unmark 2
```

### Delete a task

Use `delete` followed by the task number:

```text
delete 2
```

Aigis confirms the removed task.

## Exiting Aigis

Use `bye` to exit the chatbot:

```text
bye
```

Aigis saves the current task list to `data/aigis.txt` before closing.

## Invalid Commands

Aigis reports an error when a command is incomplete, a date is invalid, or a
task number does not exist. For example:

```text
deadline submit report /by tomorrow
```

```text
Please use: deadline <description> /by <date>
```

Unknown commands are also rejected with:

```text
I don't understand that command.
```
