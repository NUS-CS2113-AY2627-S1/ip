# CLANKER User Guide

**CLANKER** is a command-line task manager that helps you keep track of things to do, deadlines, and events. Add tasks, mark them as done, and find them by description. Your tasks are saved automatically after each successful change and restored when you next start the app.

## Contents

- [Getting started](#getting-started)
- [Command summary](#command-summary)
- [Adding tasks](#adding-tasks)
- [Viewing tasks](#viewing-tasks)
- [Marking and unmarking tasks](#marking-and-unmarking-tasks)
- [Finding tasks](#finding-tasks)
- [Deleting tasks](#deleting-tasks)
- [Help and exit](#help-and-exit)
- [Saving your tasks](#saving-your-tasks)
- [Troubleshooting](#troubleshooting)

## Getting started

1. Install **JDK 25** and configure your IDE or terminal to use it. In a terminal, run `java -version` to check the version.
2. Open the project in IntelliJ IDEA and set the project SDK to JDK 25.
3. Open `src/main/java/Clanker.java` and run `Clanker.main()`.
4. Enter commands in the Run console, pressing **Enter** after each command.

You can also start the app from a terminal in the project root, which is the folder containing `build.gradle`.

**Windows PowerShell:**

```powershell
.\gradlew.bat run --console=plain
```

**macOS or Linux:**

```sh
sh gradlew run --console=plain
```

The Gradle wrapper downloads the required build tools on its first run, so an internet connection is needed for that setup.

On startup, CLANKER displays a welcome message, its command guide, and any saved tasks. If your list is empty, it displays `Add tasks first`.

Try these commands one at a time:

```text
todo read book
deadline return book /by June 6th
event study group /from 2pm /to 4pm
list
mark 1
find book
bye
```

## Command summary

Replace values in angle brackets, such as `<description>`, with your own text. Do not type the angle brackets or add quotation marks around your text.

| Action | Format | Example |
| --- | --- | --- |
| Add a to-do | `todo <description>` | `todo read book` |
| Add a deadline | `deadline <description> /by <date>` | `deadline return book /by June 6th` |
| Add an event | `event <description> /from <start> /to <end>` | `event study group /from 2pm /to 4pm` |
| Show all tasks | `list` | `list` |
| Mark a task as done | `mark <number>` | `mark 1` |
| Mark a task as not done | `unmark <number>` | `unmark 1` |
| Find tasks | `find <keyword>` | `find book` |
| Delete a task | `delete <number>` | `delete 2` |
| Show the command guide | `help` | `help` |
| Exit the app | `bye` | `bye` |

Command names are case-insensitive: `LIST` and `list` both work. Use lowercase `/by`, `/from`, and `/to` in deadline and event commands, with spaces before and after each marker. Search text is case-sensitive.

## Adding tasks

### To-dos

Use `todo` for a task without a date or time.

```text
todo read book
```

CLANKER adds the task at the end of your list, displays it, and reports the new total number of tasks. New tasks start as not done.

### Deadlines

Use `deadline` with `/by` to record when a task is due.

```text
deadline return book /by June 6th
```

The task is displayed with its deadline:

```text
  [D][ ]return book (by: June 6th)
```

Both the description and the text after `/by` are required.

### Events

Use `event` with `/from` and `/to` to record a start and end.

```text
event study group /from 2pm /to 4pm
```

The task is displayed with both times:

```text
  [E][ ]study group (from: 2pm to: 4pm)
```

The description, start, and end are all required. Place `/from` before `/to`.

> **Note:** Dates and times are currently stored as text. You can enter values such as `June 6th` or `29/09/2026 5pm`. CLANKER does not validate calendar dates or check that an event ends after it starts.

## Viewing tasks

Enter `list` to display every task in the order it was added. For example, after adding the three tasks above:

```text
Here is your list:
1.   [T][ ]read book
2.   [D][ ]return book (by: June 6th)
3.   [E][ ]study group (from: 2pm to: 4pm)
```

- `[T]` means a to-do, `[D]` means a deadline, and `[E]` means an event.
- `[ ]` means not done; `[X]` means done.
- Task numbers begin at **1**.

## Marking and unmarking tasks

Use the task's number from `list` to change its completion status.

```text
mark 1
```

This marks the first task as done. Its status becomes `[X]`.

```text
unmark 1
```

This marks the first task as not done. Its status becomes `[ ]`. Both commands keep the task in your list.

## Finding tasks

Use `find` to show tasks whose descriptions contain your search text.

```text
find book
```

If both book tasks have been marked as done, the matching list looks like this:

```text
Here are the matching tasks in your list:
1.   [T][X]read book
2.   [D][X]return book (by: June 6th)
```

Matching is **case-sensitive**: `book` matches `read book` and `bookshelf`, but not `Book`. You can also search for a phrase, such as `find return book`. Only descriptions are searched; dates and times are not included.

If nothing matches, CLANKER displays `No matching tasks found.` Searching does not change your tasks.

> **Note:** Search results are numbered from 1 within the matching list. Before using `mark`, `unmark`, or `delete`, run `list` to get the task's number in the full list.

## Deleting tasks

Use `delete` followed by the task's number from `list`.

```text
delete 2
```

This removes the second task and saves the updated list. Later tasks move up one number, so run `list` again before choosing another task. There is no undo command.

## Help and exit

Enter `help` to display the command guide.

Enter `bye` to close CLANKER. The app displays:

```text
Bye! See you soon
```

## Saving your tasks

CLANKER saves your list after each successful add, mark, unmark, or delete command. You do not need a separate save command. Listing and searching do not modify the save file.

Tasks are stored in `data/tasks.txt`, relative to the app's working directory. When you run the app from the project root, this is the `data` folder inside your project. Start the app with the same working directory each time to load the same saved list.

## Troubleshooting

| Problem | What to do |
| --- | --- |
| A command is not recognised | Enter `help` and check the command's spelling. |
| A task description or date is missing | Include all required parts shown in the command summary. |
| A task number is invalid or out of bounds | Run `list`, then use a whole number between 1 and the number of tasks shown. |
| `find` asks for a keyword | Add search text, such as `find book`. |
| A search returns no matches | Check the spelling and capitalisation in the task description. |
| Previously saved tasks are missing | Check that you started the app with the same working directory as before. |
| CLANKER reports that it could not save tasks | Check that the working directory is writable. Changes are only in memory until a save succeeds. |
| CLANKER reports that it could not load tasks | Check access to `data/tasks.txt`. If it reports invalid task data, back up the file before correcting the reported line. The app stops when loading fails. |
