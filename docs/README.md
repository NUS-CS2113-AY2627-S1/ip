# Lumi User Guide

Lumi is a keyboard-driven task tracker for the command line. You type a line, Lumi answers,
and your list is saved to disk after every change — so you can close it mid-thought and pick
up exactly where you left off.

```
Hello from
 _    _   _ __  __ ___
| |  | | | |  \/  |_ _|
| |  | | | | |\/| || |
| |__| |_| | |  | || |
|_____\___/|_|  |_|___|
    ____________________________________________________________
     Hello! I'm Lumi
     What can I do for you?
    ____________________________________________________________
```

## Quick start

1. Make sure you have **Java 25** installed. Check with `java -version`.
1. Download `lumi.jar` from the [latest release](https://github.com/lulu-wen/ip/releases).
1. Put it in the folder you want Lumi to work from. Lumi keeps your tasks in a `data` folder
   next to wherever you run it.
1. Open a terminal in that folder and run:

   ```
   java -jar lumi.jar
   ```

1. Type a command and press Enter. Try `todo read book`, then `list`.
1. Type `bye` when you are done.

## Features

Lumi frames every reply between two horizontal lines, which the examples below leave out for
readability.

**A note on the command formats:**

* Words in `UPPER_CASE` are things you supply. In `todo DESCRIPTION`, `DESCRIPTION` is
  whatever you want to call the task.
* Command words are not fussy about case — `todo`, `Todo` and `TODO` all work.
* Task text cannot contain the `|` character, because Lumi uses it to separate fields in the
  save file.

### Adding a task with no date: `todo`

For something you need to do, with no particular deadline.

Format: `todo DESCRIPTION`

Example: `todo read book`

```
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
```

### Adding a task with a due date: `deadline`

For something due by a certain time. Write the date however you like — Lumi stores it as you
typed it.

Format: `deadline DESCRIPTION /by WHEN`

Example: `deadline return book /by Sunday`

```
 Got it. I've added this task:
   [D][ ] return book (by: Sunday)
 Now you have 2 tasks in the list.
```

### Adding a task that spans a period: `event`

For something with a start and an end.

Format: `event DESCRIPTION /from START /to END`

Example: `event project meeting /from Mon 2pm /to 4pm`

```
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 3 tasks in the list.
```

### Listing everything: `list`

Shows every task in the order you added it, numbered from 1. Those numbers are what `mark`,
`unmark` and `delete` expect.

Format: `list`

```
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[D][ ] return book (by: Sunday)
 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
```

The letter in the first box tells you the kind of task: `T` for a todo, `D` for a deadline,
`E` for an event. The second box holds an `X` once the task is done.

### Ticking a task off: `mark`

Format: `mark TASK_NUMBER`

Example: `mark 1`

```
 Nice! I've marked this task as done:
   [T][X] read book
```

### Putting a task back: `unmark`

Format: `unmark TASK_NUMBER`

Example: `unmark 1`

```
 OK, I've marked this task as not done yet:
   [T][ ] read book
```

### Removing a task: `delete`

Removes the task for good. The tasks after it move up, so their numbers change.

Format: `delete TASK_NUMBER`

Example: `delete 2`

```
 Noted. I've removed this task:
   [D][ ] return book (by: Sunday)
 Now you have 2 tasks in the list.
```

### Searching by keyword: `find`

Shows the tasks whose description contains the text you give. Matching is case-sensitive.

Format: `find KEYWORD`

Example: `find book`

```
 Here are the matching tasks in your list:
 1.[T][ ] read book
 2.[D][ ] return book (by: Sunday)
```

The numbers here count the matches, not positions in your full list. Run `list` before you
`mark` or `delete` anything.

### Leaving: `bye`

Format: `bye`

```
 Bye. Hope to see you again soon!
```

### Saving your tasks

There is nothing to do — Lumi writes `data/lumi.txt` every time your list changes, and reads
it back when it starts. The file is plain text, so you can open it if you are curious.

## FAQ

**Do I have to save before quitting?**
No. Every add, mark, unmark and delete is written to disk straight away.

**How do I move my tasks to another computer?**
Copy `data/lumi.txt` into the `data` folder next to `lumi.jar` on the other machine.

**I edited `data/lumi.txt` by hand and now a task is missing.**
Lumi tells you which line it could not understand and loads the rest. Compare the broken line
with a working one — each is `TYPE | DONE | DESCRIPTION`, where `TYPE` is `T`, `D` or `E` and
`DONE` is `0` or `1`, plus the dates for a deadline or event.

**Why does `find Book` miss `read book`?**
`find` is case-sensitive. Match the capitalisation you used when you added the task.

## Known issues

* **Dates are plain text.** Lumi keeps `/by Sunday` exactly as typed rather than
  understanding it as a date, so it cannot sort or filter by time.
* **`find` is case-sensitive** and matches whole substrings only, so `find boo` finds
  `book` but `find Book` does not.
* **The count always says "tasks"**, even when you have one.

## Command summary

| Action | Format | Example |
| --- | --- | --- |
| Add a todo | `todo DESCRIPTION` | `todo read book` |
| Add a deadline | `deadline DESCRIPTION /by WHEN` | `deadline return book /by Sunday` |
| Add an event | `event DESCRIPTION /from START /to END` | `event meeting /from Mon 2pm /to 4pm` |
| List everything | `list` | `list` |
| Mark as done | `mark TASK_NUMBER` | `mark 1` |
| Mark as not done | `unmark TASK_NUMBER` | `unmark 1` |
| Delete | `delete TASK_NUMBER` | `delete 2` |
| Search | `find KEYWORD` | `find book` |
| Exit | `bye` | `bye` |
