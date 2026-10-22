# King User Guide

King is a text-based task manager for todos, deadlines, and events. Type a
command, press Enter, and King keeps your task list ready for your next visit.

## Getting started

This guide describes the current source version. The downloadable
[v0.1 release](https://github.com/YamYamui/ip/releases/tag/v0.1) is an older
version without deadline date validation or `find`.

To use every feature below on Windows:

1. Install **JDK 25** and check that `java -version` and `javac -version` both show 25.
2. [Download the current source](https://github.com/YamYamui/ip/archive/refs/heads/master.zip)
   and unzip it. Open PowerShell in the extracted project folder (the folder containing `src`).
3. Build the application:

   ```powershell
   New-Item -ItemType Directory -Force target/classes | Out-Null
   $sources = @(Get-ChildItem src/main/java -Recurse -Filter *.java | ForEach-Object FullName)
   javac --release 25 -encoding UTF-8 -d target/classes $sources
   jar --create --file target/King.jar --main-class king.King -C target/classes .
   ```

4. Copy `target/King.jar` into a folder where you want to keep your tasks.
   Open a terminal in that folder and run:

   ```text
   java -jar King.jar
   ```

5. Try `todo read book`, then `list`. Enter `bye` to leave.

The JAR also runs on macOS or Linux with Java 25. Always start it from the
same folder to load the same saved tasks.

## Commands at a glance

Type one command per line. Replace text in `<angle brackets>` with your own
values; do not type the brackets. Command names ignore letter case, but use
lowercase clause markers such as `/by`, `/from`, and `/to`.

| Action | Command | Example |
| --- | --- | --- |
| Add a todo | `todo <description>` | `todo read book` |
| Add a deadline | `deadline <description> /by <yyyy-MM-dd>` | `deadline return book /by 2026-10-15` |
| Add an event | `event <description> /from <start> /to <end>` | `event study group /from Monday 2pm /to Monday 4pm` |
| Show all tasks | `list` | `list` |
| Search descriptions | `find <keyword>` | `find book` |
| Mark done | `mark <number>` | `mark 1` |
| Mark not done | `unmark <number>` | `unmark 1` |
| Delete a task | `delete <number>` | `delete 2` |
| Exit | `bye` | `bye` |

## Adding tasks

Descriptions are required and can contain spaces. King confirms each new task
and reports the number of tasks in your list.

Deadlines need a real date in **yyyy-MM-dd** format, with a two-digit month
and day. For example:

```text
deadline return book /by 2026-10-15
```

King displays the deadline as `[D][ ] return book (by: Oct 15 2026)`.
Dates such as `2026-02-30` and text such as `Friday` are rejected. Deadline
times, such as `2026-10-15 1800`, are not supported.

Events need both `/from` and `/to`. Their start and end values are free-form
text: King displays them as entered and does not validate their dates or order.
Put spaces around each clause marker and use each required marker once.

## Viewing and updating tasks

`list` shows tasks in their stored order:

```text
Here are the tasks in your list:
1. [T][ ] read book
2. [D][X] return book (by: Oct 15 2026)
3. [E][ ] study group (from: Monday 2pm to: Monday 4pm)
```

`T`, `D`, and `E` mean todo, deadline, and event. `[X]` means done; `[ ]`
means not done. Use the number from `list` with `mark`, `unmark`, or `delete`.
Deletion removes the task immediately, and later tasks are renumbered.
There is no undo command, so check the number before deleting.

## Finding tasks

`find book` searches the original description of every task, including completed
tasks. Matching is **case-sensitive**: `book` matches `read book` and `notebook`,
but not `Book`. Multiple words are treated as one phrase. Dates, event times,
and status icons are not searched unless they are part of the description itself.

```text
Here are the matching tasks in your list:
1. [T][ ] read book
2. [D][X] return book (by: Oct 15 2026)
```

Results are numbered from 1 within the search results. **Run `list` before
marking or deleting**, because those commands use the full list's numbers.
If nothing matches, King says `No matching tasks found.` Searching does not
change or save your tasks.

## Saving and troubleshooting

King saves automatically after adding, marking, unmarking, or deleting a task,
and reloads tasks when it starts. There is no separate save command.
The save file is `data/king.txt` inside the folder where you launch King.
To move or back up your tasks, close King and copy that file; keep the `data`
folder beside the JAR when moving your application folder.

- **Invalid command or missing details:** follow King's error message and try again.
  `list` and `bye` take no extra arguments.
- **Tasks seem missing:** launch King from the folder containing your original `data` folder.
- **Cannot save:** changes remain in memory. Fix the folder permissions or path;
  the next task change retries saving. Do not exit until saving succeeds.
- **Cannot load or invalid saved data:** King stops and leaves the file untouched.
  Back it up before repairing it. Older free-form deadlines such as `Friday`
  cannot be loaded as dates. To start fresh, move the old file aside and re-enter
  your tasks with valid dates. Avoid editing the encoded save file directly.
