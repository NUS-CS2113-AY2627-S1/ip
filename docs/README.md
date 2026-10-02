---
title: Dog User Guide
permalink: /
---

# Dog User Guide

Woof! Dog is a command-line task tracker. Add todos, deadlines, and events,
check off completed tasks, and find what you need with a keyword.

## Getting started

1. Install **Java 25**. Check your installation with `java -version`.
2. Put the supplied `Dog.jar` in a folder of your choice.
3. Open a terminal in that folder and run:

   ```text
   java -jar "Dog.jar"
   ```

4. Type a command after the `>` prompt and press Enter.
   Try `todo read book`, then `list`. Do not type the `>` itself.

This guide describes the current source version, including `find`. An older
JAR may need to be replaced with a build of the current version.

## Commands at a glance

Replace angle-bracketed values with your own text; omit the brackets.
Command words are case-insensitive. Use lowercase `/by`, `/from`, and `/to`,
with spaces around each delimiter.

| Action | Format | Example |
| --- | --- | --- |
| Add a todo | `todo <description>` | `todo read book` |
| Add a deadline | `deadline <description> /by <when>` | `deadline return book /by Sunday` |
| Add an event | `event <description> /from <start> /to <end>` | `event meeting /from Mon 2pm /to 4pm` |
| Show all tasks | `list` | `list` |
| Mark done | `mark <number>` | `mark 1` |
| Mark not done | `unmark <number>` | `unmark 1` |
| Find tasks | `find <keyword>` | `find book` |
| Delete a task | `delete <number>` | `delete 2` |
| Exit | `bye` | `bye` |

## Adding tasks

Use `todo` for a task without a date, `deadline` for a due time, and
`event` for a start and end time. Descriptions and required time fields
must not be empty.

```text
todo read book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
```

Dog confirms each addition and shows the new task count. Dates and times
are free-form text: `Sunday`, `11/10/2019 5pm`, and `no idea :-p` all work.
Dog does not validate dates, check time order, or send reminders.

## Viewing and completing tasks

Enter `list` to see tasks in their saved order. For example:

```text
1.[T][X] read book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
```

`[T]` means todo, `[D]` deadline, and `[E]` event.
`[X]` means done; `[ ]` means not done. Use `mark 2` to complete
the second task and `unmark 2` to reopen it. Completed tasks stay in the list.

## Finding tasks

`find book` searches **descriptions** for the text `book`, including
`read Book` and `buy notebook`. Matching ignores letter case.
You can also search for a phrase, such as `find read book`.

Dates, times, and status markers are not searched. If nothing matches,
Dog displays `No matching tasks found.`

**Search results have their own numbering.** Before using `mark`,
`unmark`, or `delete`, run `list` and use the number from that full list.

## Deleting tasks

Run `list`, then `delete <number>`. Dog displays the removed task and
remaining count. Later tasks are renumbered, so check `list` before another
deletion. Deletion is immediate and has no undo command; add the task again
if you removed it by mistake.

## Saving and exiting

Dog automatically saves after adding, deleting, marking, or unmarking.
Enter `bye` to exit; your tasks load when you next start Dog.

Data is stored in `data/dog.txt` relative to the folder where you run the app.
Dog creates the folder and file when it first saves. Always launch from the
same folder to use the same list. When moving the app, copy the `data`
folder too. Back it up while Dog is closed and avoid editing its contents.

## Troubleshooting

- **Java is not recognized:** install Java 25 and ensure it is on your PATH,
  then reopen the terminal.
- **Unable to access the JAR:** open the terminal in its folder and use its
  exact filename inside quotes.
- **Unknown command or missing information:** check the command table and
  try again. Task numbers must be positive integers shown by `list`.
- **Tasks appear to be missing:** check the folder you launched from and
  whether its `data` folder is still there.
- **Loading or saving fails:** check folder permissions. After a save error,
  changes may exist only in memory. A load error starts an empty in-memory list.
- **Corrupted data warning:** Dog skips unreadable records and loads valid ones.
  Back up the file before making changes: the next save replaces it with
  the tasks currently loaded.

**Thank you you have reached the end** :)
