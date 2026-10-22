# JamaL User Guide

JamaL is a command-line task manager with no patience. You type a command, it does the thing, it tells you it did the thing. Your tasks are saved automatically after every change.

```
____________________________________________________________
     ____.                     .____
    |    |____    _____ _____  |    |
    |    \__  \  /     \\__  \ |    |
/\__|    |/ __ \|  Y Y  \/ __ \|    |___
\________(____  /__|_|  (____  /_______ \
              \/      \/     \/        \/
It's JamaL.
What do you want.
____________________________________________________________
```

## Quick start

1. Make sure you have **Java 17 or above** installed (`java -version` to check).
2. Download the latest `jamal.jar` from the [Releases page](https://github.com/zechby/ip/releases).
3. Put it in an empty folder, open a terminal in that folder, and run:
   ```
   java -jar jamal.jar
   ```
4. Type a command and press Enter. Type `bye` to exit.

## Command summary

| Command | Format | Example |
|---|---|---|
| Add todo | `todo DESCRIPTION` | `todo read book` |
| Add deadline | `deadline DESCRIPTION /by WHEN` | `deadline return book /by Sunday` |
| Add event | `event DESCRIPTION /from START /to END` | `event project meeting /from Mon 2pm /to 4pm` |
| List | `list` | `list` |
| Mark done | `mark INDEX` | `mark 2` |
| Unmark | `unmark INDEX` | `unmark 2` |
| Delete | `delete INDEX` | `delete 3` |
| Find | `find KEYWORD` | `find book` |
| Exit | `bye` | `bye` |

Notes on the format:
* Words in `UPPER_CASE` are what you fill in.
* `INDEX` is the number shown next to the task in `list`, starting from 1.
* `WHEN`, `START` and `END` are free text. JamaL stores them exactly as you type them.
* Task text can't contain `|`, because the save file uses it to separate fields.

## Adding a todo: `todo`

Adds a task with no date attached.

Format: `todo DESCRIPTION`

Example: `todo read book`

```
____________________________________________________________
added:
  [T][ ] read book
you got 1 task now.
____________________________________________________________
```

## Adding a deadline: `deadline`

Adds a task that has to be done by a certain time.

Format: `deadline DESCRIPTION /by WHEN`

Example: `deadline return book /by Sunday`

```
____________________________________________________________
added:
  [D][ ] return book (by: Sunday)
you got 2 tasks now.
____________________________________________________________
```

## Adding an event: `event`

Adds a task that starts and ends at certain times.

Format: `event DESCRIPTION /from START /to END`

Example: `event project meeting /from Mon 2pm /to 4pm`

```
____________________________________________________________
added:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
you got 3 tasks now.
____________________________________________________________
```

## Listing all tasks: `list`

Shows every task, numbered. The first bracket is the type (`T`odo, `D`eadline, `E`vent); the second shows `X` if the task is done.

Format: `list`

```
____________________________________________________________
1.[T][ ] read book
2.[D][X] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
```

## Marking and unmarking: `mark`, `unmark`

Marks a task as done, or back to not done.

Format: `mark INDEX`, `unmark INDEX`

Example: `mark 2`

```
____________________________________________________________
task 2 is marked
  [D][X] return book (by: Sunday)
____________________________________________________________
```

## Deleting a task: `delete`

Removes a task. Tasks after it move up one number.

Format: `delete INDEX`

Example: `delete 1`

```
____________________________________________________________
gone:
  [T][ ] read book
you got 2 tasks left.
____________________________________________________________
```

## Finding tasks: `find`

Shows every task whose description contains the keyword. Case doesn't matter, and partial words match (`book` finds `Book` and `notebook`).

Format: `find KEYWORD`

Example: `find book`

```
____________________________________________________________
found these:
1.[D][X] return book (by: Sunday)
____________________________________________________________
```

The numbers in the results are just for this list. Use `list` to get the index for `mark`, `unmark` or `delete`.

## Exiting: `bye`

Format: `bye`

```
____________________________________________________________
alright.
____________________________________________________________
```

## Saving your data

Tasks are saved to `data/jamal.txt`, inside the folder you ran JamaL from, after every change. There's no save command.

You can edit the file by hand, one task per line:

```
T | 1 | read book
D | 0 | return book | Sunday
E | 0 | project meeting | Mon 2pm | 4pm
```

The second field is `1` if done, `0` if not. If a line is broken, JamaL skips it on startup and tells you how many it skipped. The rest still load.

## Errors

If you get a command wrong, JamaL tells you what it wanted:

| You typed | JamaL says |
|---|---|
| `todo` | `todo what.` |
| `deadline essay` | `deadline what /by when.` |
| `mark abc` | `that's not a number.` |
| `mark 99` | `no task 99.` |
| `dance` | `I'm not doing whatever dance is.` |
