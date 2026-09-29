# Chillguy User Guide

Chillguy is a task manager you use by typing commands in a terminal. Keep track of todos,
deadlines, and events, find tasks by description, and mark them done. Your tasks are saved
automatically and loaded the next time you start the app.

## Contents

- [Quick start](#quick-start)
- [Command format](#command-format)
- [Features](#features)
- [Saving and restoring tasks](#saving-and-restoring-tasks)
- [Troubleshooting](#troubleshooting)
- [Command summary](#command-summary)

## Quick start

1. Install **Java 25**. Open a terminal and run `java -version` to check that it reports version 25.
2. Put `chillguy-all.jar` in a folder where you want to keep your tasks. If you have the project
   source instead of a JAR, follow [Building from source](#building-from-source) below first.
3. Open a terminal in that folder and run:

   ```sh
   java -jar chillguy-all.jar
   ```

4. When you see `Hello! I'm Chillguy.` and `What can I do for you?`, type a command and press
   **Enter**. Try these commands one at a time:

   ```text
   todo read book
   list
   mark 1
   bye
   ```

   In a new task list, this adds your first todo, displays it, marks it done, and exits.
   Starting the app again and entering `list` shows the saved task as `[T][X] read book`.

Use the same launch folder each time: Chillguy looks for `data/chillguy.txt` inside the
terminal's current folder. This location can differ from the folder containing the JAR.

### Building from source

Install **JDK 25**, which includes the Java compiler. Both `java -version` and `javac -version`
should report version 25. If `JAVA_HOME` is set, point it to that JDK.
Open a terminal in the project root (the folder containing `build.gradle`).

On Windows PowerShell:

```powershell
.\gradlew.bat shadowJar
```

On macOS or Linux:

```sh
sh gradlew shadowJar
```

The first build needs internet access. After `BUILD SUCCESSFUL`, copy
`build/libs/chillguy-all.jar` to your chosen folder and continue with step 3 above.

## Command format

- Type commands in lowercase, such as `list`. `LIST` is not recognized.
- Replace uppercase placeholders with your own text: `todo DESCRIPTION` becomes `todo read book`.
  All fields shown in a command's format are required.
- Enter one command per line. Descriptions and dates can contain spaces; quotation marks are
  unnecessary and are treated as part of the text.
- Keep spaces around `/by`, `/from`, and `/to`, and use them in the order shown. Use `/by` once
  in a deadline, and `/from` and `/to` once each in an event; these standalone tokens separate fields.
- `INDEX` is a whole number starting at 1, taken from the full `list` output.
  Run `list` again after deleting a task or searching.
- `list` and `bye` take no extra input.

## Features

The examples below form one walkthrough, starting with an empty list. Task counts and numbers
will differ if you already have saved tasks. Output excerpts omit the horizontal separator lines.

### Adding a todo

Use a todo for a task without a date.

**Format:** `todo DESCRIPTION`<br>
**Example:** `todo read book`

```text
Got it. I've added this task:
  [T][ ] read book
Now you have 1 task in the list.
```

New tasks start as not done. Each successful addition goes to the end of the list, even if
another task has the same description.

### Adding a deadline

Use a deadline for a task with a due date or time.

**Format:** `deadline DESCRIPTION /by DATE`<br>
**Example:** `deadline return book /by June 6th`

```text
Got it. I've added this task:
  [D][ ] return book (by: June 6th)
Now you have 2 tasks in the list.
```

Dates are stored as text. You can enter `June 6th`, `2026-10-01`, or `tomorrow 5pm`.
Chillguy displays the text you supplied; it does not check calendar dates, resolve `tomorrow`,
sort tasks by date, or send reminders.

### Adding an event

Use an event for something with a start and an end.

**Format:** `event DESCRIPTION /from START /to END`<br>
**Example:** `event project meeting /from Aug 6th 2pm /to 4pm`

```text
Got it. I've added this task:
  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
Now you have 3 tasks in the list.
```

Both start and end are required. Like deadlines, they are stored as text; the app does not
check that the end is after the start.

### Listing tasks

**Format and example:** `list`

Shows all tasks, including completed ones, in their stored order:

```text
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: June 6th)
3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
```

`[T]` means todo, `[D]` means deadline, and `[E]` means event.
`[ ]` means not done; `[X]` means done. An empty list displays only the heading.

### Finding tasks

**Format:** `find KEYWORD`<br>
**Example:** `find book`

```text
Here are the matching tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: June 6th)
```

- Searches task descriptions only, including completed tasks. Dates, times, and status icons
  are not searched.
- Matching is **case-sensitive**: `find Book` does not match `read book`.
- Partial words match: `find boo` matches both book tasks above.
- Multiple words form one exact phrase: `find read book` matches `read book`.
- No matches, including on an empty list, produces `No matching tasks found.`
- Searching leaves your tasks and saved data unchanged.

> **Before changing a search result, run `list`.** Search results are numbered from 1 within
> the results, but `mark`, `unmark`, and `delete` always use full-list numbers.
> For example, `find meeting` shows the event as result 1 in this walkthrough;
> its full-list number is 3, so use `mark 3` to complete it.

### Marking a task done

**Format:** `mark INDEX`<br>
**Example:** `mark 1`

```text
Nice! I've marked this task as done:
  [T][X] read book
```

The task stays in the list. Marking an already completed task leaves it completed.

### Marking a task not done

**Format:** `unmark INDEX`<br>
**Example:** `unmark 1`

```text
OK, I've marked this task as not done yet:
  [T][ ] read book
```

Use this to reopen a completed task. Unmarking an incomplete task leaves it incomplete.

### Deleting a task

**Format:** `delete INDEX`<br>
**Example:** `delete 2`

```text
Noted. I've removed this task:
  [D][ ] return book (by: June 6th)
Now you have 2 tasks in the list.
```

Deletion is saved immediately, with no confirmation prompt or undo command. Run `list` first
to check the number. Remaining tasks shift up: the event above becomes task 2 after this deletion.
To change a task's description or dates, delete it and add the corrected task.

### Exiting

**Format and example:** `bye`

```text
Bye. Hope to see you again soon!
```

This closes Chillguy. Successful task changes have already been saved.

## Saving and restoring tasks

Chillguy automatically saves successful additions, deletions, and changes in completion status
to `data/chillguy.txt` relative to your launch folder. No save command is needed.
The folder and file are created on the first successful task change. A missing or empty file
starts an empty list.

- **Back up:** Exit Chillguy, then copy `data/chillguy.txt` to a safe location.
- **Restore or transfer:** Exit all instances, install Java 25 on the destination computer if
  needed, and put the JAR and the backed-up `data` folder in your chosen launch folder.
  Restoring the file replaces the tasks at the destination, so back those up first.
- **Avoid conflicting changes:** Run one Chillguy instance per data file and manage tasks through
  the app. Manual file edits can prevent it from loading.

If a save fails, Chillguy reports the error and undoes the attempted change. If loading fails,
the app stops and leaves the saved file unchanged.

## Troubleshooting

| Problem | What to do |
| --- | --- |
| `java` is not recognized, or Java reports an unsupported class version | Install Java 25, reopen the terminal, and check `java -version`. |
| `Unable to access jarfile` | Open the terminal in the folder containing `chillguy-all.jar` and check the filename. |
| `ERROR: Unknown command.` | Use a lowercase command from the summary below. For help, refer to this guide; there is no `help` command. |
| A command reports missing fields or an invalid format | Supply all fields and the separators shown in the command format. For example, use `deadline return book /by June 6th`. |
| A task number is rejected | Run `list` and choose an existing positive whole number. Add a task first if the list is empty. |
| Search finds nothing | Check capitalization and search for text in the description. Multiple words must occur together in that order. |
| Tasks appear to be missing after restarting | Launch from the same folder as before and check its `data/chillguy.txt`. |
| Chillguy cannot load tasks or reports invalid task data | Back up the file. Check that `data/chillguy.txt` is a readable UTF-8 file; restore a valid backup or correct the reported line, then restart. |
| Chillguy cannot save tasks | Check that the data folder is writable and close programs locking the file, then retry. If the error mentions atomic file replacement, move the JAR and data to a local folder on a file system that supports it. |

## Command summary

| Action | Format | Example |
| --- | --- | --- |
| Add a todo | `todo DESCRIPTION` | `todo read book` |
| Add a deadline | `deadline DESCRIPTION /by DATE` | `deadline return book /by June 6th` |
| Add an event | `event DESCRIPTION /from START /to END` | `event project meeting /from Aug 6th 2pm /to 4pm` |
| List all tasks | `list` | `list` |
| Search descriptions | `find KEYWORD` | `find book` |
| Mark done | `mark INDEX` | `mark 1` |
| Mark not done | `unmark INDEX` | `unmark 1` |
| Delete a task | `delete INDEX` | `delete 2` |
| Exit | `bye` | `bye` |

Use numbers from `list` when marking, unmarking, or deleting tasks.
