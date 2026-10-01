# Floppy User Guide

Floppy is a command line task tracker with the personality of a 1.44 MB floppy disk: it
whirrs, it clicks, and it is delighted to be useful again after decades in a drawer.

It keeps three kinds of task — todos, deadlines and events — and saves them automatically,
so your list is still there the next time you start it.

```
    ____________________________________________________________
 _____ _
|  ___| | ___  _ __  _ __  _   _
| |_  | |/ _ \| '_ \| '_ \| | | |
|  _| | | (_) | |_) | |_) | |_| |
|_|   |_|\___/| .__/| .__/ \__, |
              |_|   |_|    |___/
     *click... whirr... clunk*
     Hello! I'm Floppy, 1.44 MB of pure determination.
     Tell me a task and I'll hold onto it:
       todo borrow book
       deadline return book /by 2019-10-15
       event project meeting /from Mon 2pm /to 4pm
     Then 'list', 'mark 1', 'unmark 1', 'delete 1',
     'find book', or 'bye'.
     What can I do for you?
    ____________________________________________________________
```

## Quick start

1. Make sure you have **Java 25** or newer installed. Check with `java -version`.
2. Download the latest `floppy.jar` from the [releases page](https://github.com/SanchitGarg08/ip/releases).
3. Put it in the folder you want Floppy to work in. Floppy saves your tasks beside the JAR.
4. Open a terminal, go to that folder, and run:

   ```
   java -jar floppy.jar
   ```
5. Type a command and press Enter. Try `todo read book`, then `list`.

## Features

Type one command per line. Command words are case-insensitive, so `TODO` and `todo` both
work. In the examples below, Floppy's replies are shown without the divider lines it
prints around each one.

### Adding a todo: `todo`

Adds a task with no date attached.

Format: `todo DESCRIPTION`

Example: `todo borrow book`

```
     *whirr-click* Got it. I've added this task:
       [T][ ] borrow book
     Now you have 1 task in the list.
```

### Adding a deadline: `deadline`

Adds a task that is due on a particular date.

Format: `deadline DESCRIPTION /by YYYY-MM-DD`

The date must be written as `YYYY-MM-DD`. Floppy displays it in a friendlier form.

Example: `deadline return book /by 2026-10-15`

```
     *chk-chk-chk* Got it. I've added this task:
       [D][ ] return book (by: Oct 15 2026)
     Now you have 2 tasks in the list.
```

### Adding an event: `event`

Adds a task that runs from a start time to an end time. The start and end are free text,
so you can write them however you like.

Format: `event DESCRIPTION /from START /to END`

Example: `event project meeting /from Mon 2pm /to 4pm`

```
     *seeking track 00* Got it. I've added this task:
       [E][ ] project meeting (from: Mon 2pm to: 4pm)
     Now you have 3 tasks in the list.
```

### Listing all tasks: `list`

Shows everything Floppy is holding, numbered. Use these numbers with `mark`, `unmark`
and `delete`.

Format: `list`

```
     *rattling through the index*
     Here are the tasks in your list:
     1.[T][ ] borrow book
     2.[D][ ] return book (by: Oct 15 2026)
     3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
```

Each task shows its type and whether it is done: `[T]` todo, `[D]` deadline, `[E]` event,
and `[X]` for done or `[ ]` for not done yet.

### Marking a task as done: `mark`

Format: `mark NUMBER`

Example: `mark 2`

```
     *clack* Nice! I've marked this task as done:
       [D][X] return book (by: Oct 15 2026)
```

### Marking a task as not done: `unmark`

Format: `unmark NUMBER`

Example: `unmark 2`

```
     *rewinds* OK, I've marked this task as not done yet:
       [D][ ] return book (by: Oct 15 2026)
```

### Deleting a task: `delete`

Removes a task for good. The remaining tasks are renumbered.

Format: `delete NUMBER`

Example: `delete 1`

```
     *bzzt, sector wiped* Noted. I've removed this task:
       [T][ ] borrow book
     Now you have 2 tasks in the list.
```

### Finding tasks: `find`

Shows every task whose description contains your keyword. The search ignores case, and
the keyword can be more than one word.

Format: `find KEYWORD`

Example: `find book`

```
     *seeking 'book'*
     Here are the matching tasks in your list:
     1.[T][ ] borrow book
     2.[D][X] return book (by: Oct 15 2026)
```

The numbers here count the matches, not positions in the full list. Run `list` before
using `mark`, `unmark` or `delete`.

### Exiting: `bye`

Format: `bye`

```
     *spinning down... ejecting*
     Bye. Hope to see you again soon!
     Please don't leave me in a drawer for another 20 years.
```

## Saving your tasks

Floppy saves after every command that changes your list, so there is no save command and
nothing to remember.

Your tasks live in `data/floppy.txt`, in the folder you ran Floppy from. You can edit that
file by hand if you like. If a line is not in the format Floppy expects, it skips that line,
tells you which line numbers it skipped, and copies the original to `data/floppy.txt.bak`
first, so nothing is lost.

## Getting it wrong is safe

Floppy explains mistakes instead of crashing. If you leave out a description, mistype a
date, ask for a task number that does not exist, or use a command it does not know, it
says so and your list stays as it was.

```
     *stutters* I couldn't read 'June 6th' as a date. Use yyyy-mm-dd, e.g. 2019-10-15.
```

## Command summary

| Action | Format | Example |
| --- | --- | --- |
| Add a todo | `todo DESCRIPTION` | `todo borrow book` |
| Add a deadline | `deadline DESCRIPTION /by YYYY-MM-DD` | `deadline return book /by 2026-10-15` |
| Add an event | `event DESCRIPTION /from START /to END` | `event project meeting /from Mon 2pm /to 4pm` |
| List all tasks | `list` | `list` |
| Mark as done | `mark NUMBER` | `mark 2` |
| Mark as not done | `unmark NUMBER` | `unmark 2` |
| Delete a task | `delete NUMBER` | `delete 1` |
| Find by keyword | `find KEYWORD` | `find book` |
| Exit | `bye` | `bye` |
