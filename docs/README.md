# Sandy User Guide

Sandy is a command-line task manager for keeping track of todos, deadlines, and events.

## Getting started

Sandy requires Java 25. From the project folder, compile and start it in PowerShell:

```powershell
javac -d out (Get-ChildItem -Path src/main/java -Filter *.java -Recurse).FullName
java -cp out sandy.Sandy
```

Enter a command at the prompt. Sandy saves your tasks in `data/sandy.txt` and loads them the next time it starts.

## Commands

| What you want to do | Command | Example |
| --- | --- | --- |
| Add a todo | `todo DESCRIPTION` | `todo read Dune` |
| Add a deadline | `deadline DESCRIPTION /by DATE` | `deadline return book /by 2019-12-02` |
| Add an event | `event DESCRIPTION /from START /to END` | `event team meeting /from Monday 2pm /to Monday 3pm` |
| Show all tasks | `list` | `list` |
| Mark a task done | `mark NUMBER` | `mark 1` |
| Mark a task not done | `unmark NUMBER` | `unmark 1` |
| Delete a task | `delete NUMBER` | `delete 1` |
| Find tasks by description | `find KEYWORD` | `find book` |
| Exit Sandy | `bye` | `bye` |

Replace `DESCRIPTION`, `DATE`, and other capitalized placeholders with your own values. For `mark`, `unmark`, and `delete`, use the task number shown by `list`.

### Deadline date formats

Use `yyyy-MM-dd`, such as `2019-12-02`, or include a time. Sandy also accepts `d/M/yyyy HHmm`, where the date is day/month/year and the time uses 24-hour hours and minutes. For example, `2/12/2019 1800` means 2 December 2019 at 6:00 PM. Sandy displays dates in a friendly format, such as `Dec 02 2019`.

### Events

The event start and end values are stored and displayed as entered; Sandy does not interpret them as dates or times. Include `/from` and `/to` in the command.

### Finding tasks

`find` searches task descriptions for a case-insensitive keyword and shows matching tasks. The numbers in search results are positions within those results, not the task numbers used by `mark`, `unmark`, or `delete`; use `list` to get the task number for those commands.
