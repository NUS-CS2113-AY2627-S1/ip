# TwitchChat User Guide

TwitchChat is a simple command-line task manager. Add tasks, set deadlines, record events, mark tasks as complete, search your tasks, and remove tasks.

## Getting started

### Requirements

- Java 25

### Run TwitchChat

1. Download the latest TwitchChat JAR file from the repository's **Releases** page.
2. Open a terminal in the folder containing the downloaded JAR file.
3. Run the following command:

   ```text
   java -jar TwitchChat.jar
   ```

Your tasks are saved automatically in `data/tasks.txt` relative to the folder where you run the JAR. They are loaded the next time you start TwitchChat.

## Commands

Commands must be entered in lowercase.

| Command | Format | Example | Purpose |
| --- | --- | --- | --- |
| `hi` | `hi` | `hi` | Displays a greeting. |
| `todo` | `todo DESCRIPTION` | `todo buy groceries` | Adds a task without a date. |
| `deadline` | `deadline DESCRIPTION /by DATE` | `deadline submit report /by Friday` | Adds a task with a deadline. |
| `event` | `event DESCRIPTION /from START /to END` | `event team meeting /from Monday /to Tuesday` | Adds an event with a start and end time. |
| `list` | `list` | `list` | Shows all tasks. |
| `mark` | `mark TASK_ID` | `mark 1` | Marks a task as done. |
| `unmark` | `unmark TASK_ID` | `unmark 1` | Marks a task as not done. |
| `delete` | `delete TASK_ID` | `delete 1` | Removes a task. |
| `find` | `find KEYWORD` | `find report` | Shows tasks containing the keyword. |
| `bye` | `bye` | `bye` | Exits TwitchChat. |

## Task IDs

Tasks are numbered starting from `1` when you use `list`. Use these numbers with `mark`, `unmark`, and `delete`.

For example:

```text
list
1.[T][ ] buy groceries
2.[D][ ] submit report (by: Friday)

mark 1
delete 2
```

## Helpful notes

- Task descriptions and dates can contain spaces.
- The words `/by`, `/from`, and `/to` separate the parts of deadline and event commands.
- If a command is incomplete or invalid, TwitchChat explains what needs to be corrected.
- Use `find` to search task descriptions without worrying about letter case.
- Type `bye` when you are finished so TwitchChat can exit cleanly.
