# Dexter User Guide

Dexter is a command-line task-management chatbot. It helps you keep track of basic tasks, deadlines, and events.

## Setting up in IntelliJ

Prerequisite: JDK 25.

1. Open IntelliJ. If another project is open, select `File` > `Close Project` first.
2. Click `Open` and select the project directory.
3. Configure the project to use **JDK 25**. In the same dialog, set the **Project language level** to `SDK default`.
4. Open `src/main/java/dexter/Dexter.java`.
5. Right-click the file and select `Run 'Dexter.main()'`.

Dexter will display a welcome message and wait for your command. Type `bye` to exit.

**Warning:** Keep the `src/main/java` folder as the root folder for Java files. Do not rename these folders or move Java files outside this folder path.

## Creating tasks

Create a basic task with `todo` followed by its description:

```
todo read chapter 3
```

Create a task with a deadline using `/by` and a date. Dexter accepts `yyyy-MM-dd` or `d/M/yyyy HHmm` formats:

```
deadline submit assignment /by 2026-10-05
deadline attend consultation /by 2/10/2026 1430
```

Create an event by specifying its start and end dates:

```
event software engineering meeting from 3/10/2026 1400 to 3/10/2026 1600
```

## Viewing and finding tasks

Display all tasks currently in your list:

```
list
```

Search for tasks whose descriptions contain a keyword. Searches are case-sensitive:

```
find assignment
```

## Updating and deleting tasks

Tasks are identified by their position in the list, starting from 1. Use that task number to update or delete a task:

```
mark 1
unmark 1
delete 1
```

`mark` marks a task as completed, `unmark` changes it back to incomplete, and `delete` removes it from the list.

## Exiting Dexter

Exit the chatbot with:

```
bye
```

Your tasks are saved automatically in `data/dexter.txt` whenever you add, mark, unmark, or delete a task. They are loaded again the next time Dexter starts.

## Command summary

| Command | Format | Purpose |
| --- | --- | --- |
| `todo` | `todo <description>` | Adds a basic task |
| `deadline` | `deadline <description> /by <date>` | Adds a task with a deadline |
| `event` | `event <description> from <start> to <end>` | Adds an event |
| `list` | `list` | Displays all tasks |
| `find` | `find <keyword>` | Finds matching task descriptions |
| `mark` | `mark <number>` | Marks a task as completed |
| `unmark` | `unmark <number>` | Marks a task as incomplete |
| `delete` | `delete <number>` | Deletes a task |
| `bye` | `bye` | Exits Dexter |

If a command is invalid, or a task number is missing or outside the list, Dexter displays an error message and continues waiting for input.
