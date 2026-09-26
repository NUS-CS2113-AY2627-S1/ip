# MoistBot User Guide

MoistBot is a command-line personal task assistant that helps you record todos,
deadlines, and events. It can track whether tasks are complete, search task
descriptions, show dated tasks up to a chosen day, and save changes between
sessions.

## Contents

- [Quick start](#quick-start)
- [Reading the task list](#reading-the-task-list)
- [Command summary](#command-summary)
- [Adding tasks](#adding-tasks)
- [Viewing tasks](#viewing-tasks)
- [Finding tasks](#finding-tasks)
- [Updating tasks](#updating-tasks)
- [Deleting tasks](#deleting-tasks)
- [Exiting MoistBot](#exiting-moistbot)
- [Saving data](#saving-data)
- [Command tips](#command-tips)

## Quick start

### Requirements

- Java 25 or later

### Starting MoistBot

1. Download the JAR file from the
   [MoistBot v0.2 release](https://github.com/ka-how/ip/releases/tag/v0.2).
2. Move the JAR file into the folder where you want MoistBot to store its
   data.
3. Open a terminal in that folder and run:

   ```text
   java -jar MoistBot.jar
   ```

   If the downloaded JAR has a different name, replace `MoistBot.jar` with
   its actual file name.
4. Enter one command per line in the terminal.

To run MoistBot from source instead, clone the repository, open its root
folder in IntelliJ IDEA, and set the project SDK and language level to JDK 25.
Then run `moistbot.MoistBot.main()`.

When MoistBot is ready, it displays its name and greeting:

```text
 __  __   ___   ___ ____ _____ ____   ___ _____
|  \/  | / _ \ |_ _|/ ___|_   _| __ ) / _ \|_   _|
| |\/| || | | | | | \___ \ | | |  _ \| | | | | |
| |  | || |_| | | |  ___) || | | |_) | |_| | | |
|_|  |_| \___/ |___||____/ |_| |____/ \___/  |_|
Good day. I am MoistBot, at your service.
How may I assist you today?
```

Try this short session:

```text
todo buy milk
deadline submit report /by 2026-09-25 2359
list
mark 1
bye
```

## Reading the task list

MoistBot displays each task with a type, completion status, and description:

```text
1.[T][ ] buy milk
2.[D][X] submit report (by: Sep 25 2026, 11:59 PM)
3.[E][ ] project meeting (from: Sep 26 2026, 2:00 PM to: Sep 26 2026, 4:00 PM)
```

The symbols mean:

| Symbol | Meaning |
| --- | --- |
| `[T]` | Todo |
| `[D]` | Deadline |
| `[E]` | Event |
| `[ ]` | Incomplete |
| `[X]` | Complete |

The number at the start is the task number used by `mark`, `unmark`, and
`delete`.

## Command summary

Arguments in angle brackets, such as `<description>`, must be replaced with
your own text. Arguments in square brackets, such as `[HHmm]`, are optional.
Do not type the angle or square brackets.

| Action | Command format | Example |
| --- | --- | --- |
| Add a todo | `todo <description>` | `todo buy milk` |
| Add a deadline | `deadline <description> /by <date> [HHmm]` | `deadline submit report /by 2026-09-25 2359` |
| Add an event | `event <description> /from <date> [HHmm] /to <date> [HHmm]` | `event project meeting /from 2026-09-26 1400 /to 2026-09-26 1600` |
| View every task | `list` | `list` |
| View dated tasks up to a date | `list <date>` | `list 26/9/2026` |
| Find tasks by description | `find <search term>` | `find report` |
| Mark a task complete | `mark <task number>` | `mark 2` |
| Mark a task incomplete | `unmark <task number>` | `unmark 2` |
| Delete a task | `delete <task number>` | `delete 2` |
| Exit | `bye` | `bye` |

## Adding tasks

### Adding a todo: `todo`

Use a todo for a task without a specific date.

Format:

```text
todo <description>
```

Example:

```text
todo read chapter 4
```

MoistBot adds the task as incomplete and confirms the new number of tasks.

### Adding a deadline: `deadline`

Use a deadline for work that must be completed by a particular date or time.
The `/by` separator must appear as a separate word.

Format:

```text
deadline <description> /by <date> [HHmm]
```

Examples:

```text
deadline return library book /by 30/9/2026
deadline submit report /by 2026-09-25 2359
```

Both `d/M/yyyy` and `yyyy-MM-dd` dates are accepted. A time is optional and
must use the 24-hour `HHmm` format: for example, `0900` means 9:00 AM and
`2359` means 11:59 PM.

### Adding an event: `event`

Use an event for an activity with a start and an end. The `/from` and `/to`
separators must appear as separate words and in that order.

Format:

```text
event <description> /from <date> [HHmm] /to <date> [HHmm]
```

Examples:

```text
event recess week /from 21/9/2026 /to 27/9/2026
event project meeting /from 2026-09-26 1400 /to 2026-09-26 1600
```

If you provide a time for one endpoint, you must provide a time for both. The
end of an event cannot be earlier than its start.

## Viewing tasks

### Viewing all tasks: `list`

Enter `list` without an argument to display every task and its current task
number.

```text
list
```

Use these task numbers with `mark`, `unmark`, and `delete`.

### Viewing dated tasks up to a date: `list <date>`

Provide a date to display:

- deadlines due on or before that date; and
- events that start on or before that date.

The cutoff date is inclusive. Todos are not included in this filtered view.

```text
list 2026-09-26
```

The filtered view retains the tasks' original numbers, so its numbers can be
used directly with `mark`, `unmark`, and `delete`.

## Finding tasks

Use `find` to show tasks whose descriptions contain the search term.

Format:

```text
find <search term>
```

Example:

```text
find report
```

Searches are case-sensitive: `report` and `Report` are different terms. Only
the task description is searched; deadline and event dates are not searched.

Search results retain their original task numbers from `list`. You can use a
number shown by `find` directly with `mark`, `unmark`, or `delete`.

## Updating tasks

### Marking a task complete: `mark`

Format:

```text
mark <task number>
```

Example:

```text
mark 2
```

The task's status changes from `[ ]` to `[X]`.

### Marking a task incomplete: `unmark`

Format:

```text
unmark <task number>
```

Example:

```text
unmark 2
```

The task's status changes from `[X]` to `[ ]`.

## Deleting tasks

Use `delete` to remove a task permanently from the list.

Format:

```text
delete <task number>
```

Example:

```text
delete 2
```

MoistBot shows the deleted task and the new task count. Tasks after the deleted
task are renumbered, so enter `list` again before changing another task.

## Exiting MoistBot

Enter `bye` without any additional text to end the session.

```text
bye
```

## Saving data

MoistBot automatically saves the task list after every successful `todo`,
`deadline`, `event`, `mark`, `unmark`, and `delete` command. Saved tasks are
loaded automatically the next time MoistBot starts; no separate save command
is needed.

Data is stored relative to the folder from which MoistBot is run:

```text
data/moistbot.txt
```

Avoid editing this file manually. If MoistBot reports that it cannot read or
write the file, check that the `data` folder and file are accessible, then
restart MoistBot or retry the command as instructed by the error message.

## Command tips

- Command words are lowercase and case-sensitive. Use `todo`, not `Todo`.
- Enter one command per line.
- Dates must use `d/M/yyyy` or `yyyy-MM-dd`.
- Optional times must use four-digit, 24-hour `HHmm` format.
- Task numbers start at 1. Use `list` whenever you need to check them.
- A failed command does not close MoistBot. Follow the correction in its
  message and enter the command again.
