# AgentCT Chatbot User Guide

AgentCT is a command-line task manager that helps you organise todos,
deadlines, and events.

## Features

AgentCT supports the following commands:

| Command | Description |
| --- | --- |
| `todo <description>` | Adds a todo task |
| `deadline <description> /by <time>` | Adds a task with a deadline |
| `event <description> /from <start> /to <end>` | Adds an event |
| `list` | Displays all tasks |
| `find <keyword>` | Finds tasks containing a keyword |
| `mark <task number>` | Marks a task as completed |
| `unmark <task number>` | Marks a task as not completed |
| `delete <task number>` | Deletes a task |
| `bye` | Exits AgentCT |

## Adding tasks

### Adding a todo

Use:

```text
todo <description>
```

Example:

```text
todo Read chapter 3
```

AgentCT adds the task to your task list.

### Adding a deadline

Use:

```text
deadline <description> /by <time>
```

Example:

```text
deadline Submit assignment /by Friday 11:59pm
```

### Adding an event

Use:

```text
event <description> /from <start time> /to <end time>
```

Example:

```text
event Team meeting /from Monday 2pm /to Monday 3pm
```

## Viewing and searching tasks

To view all tasks, enter:

```text
list
```

To search for tasks containing a keyword, enter:

```text
find <keyword>
```

Example:

```text
find assignment
```

Task numbers are shown when tasks are displayed. Use these numbers when
marking, unmarking, or deleting tasks.

## Updating and deleting tasks

To mark a task as completed:

```text
mark <task number>
```

To mark a task as not completed:

```text
unmark <task number>
```

To delete a task:

```text
delete <task number>
```

For example:

```text
delete 2
```

removes the second task in the list.

## Exiting the program

Enter:

```text
bye
```

to close AgentCT. Your tasks are saved automatically and restored the next
time you start the program.
