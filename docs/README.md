# Yanny User Guide

Yanny is a command-line task manager for Todos, Deadlines, and Events. It saves
your tasks automatically so they are available the next time you start it.

## Get started

Yanny requires Java 25. Follow the [build instructions](https://github.com/Pang2004/ip#building-and-running-the-executable-jar)
to create `yanny.jar`. Copy it into a folder where you want to keep your tasks,
open a terminal there, and run:

```bash
java -jar yanny.jar
```

When you see `SYSTEM READY. AWAITING COMMAND...`, enter one command per line.
Replace placeholders such as `<description>` and `<number>` with your own values.
Commands and markers such as `/by`, `/from`, and `/to` ignore letter case.

## Add tasks

### Todo

Use `todo <description>` for a task without a date or time:

```text
todo borrow a book
```

Yanny displays a Todo with `[T]`.

### Deadline

Use `deadline <description> /by <date>` for a task due on a date. Enter a date
as `yyyy-MM-dd`, or include a 24-hour time as `d/M/yyyy HHmm`. For example,
`2/12/2030 1800` means 2 December 2030 at 6:00 PM.

```text
deadline submit report /by 2030-10-15
deadline return book /by 2/12/2030 1800
```

Yanny displays these dates as `Oct 15 2030` and `Dec 02 2030 6:00 PM`.
Invalid dates and times are rejected. Older free-text deadlines already in your
saved tasks still display as written.

### Event

Use `event <description> /from <start> /to <end>` for an activity with a start
and end:

```text
event project meeting /from Monday 2pm /to Monday 3pm
```

Yanny displays an Event with `[E]`. Start and end values appear as entered.

## View and find tasks

```text
list
find book
```

`list` shows every task. `find` searches task descriptions for a word or phrase,
ignoring letter case. Search results keep their original task numbers; a task
shown as number 3 can still be used with `mark 3` or `delete 3`.

`[T]`, `[D]`, and `[E]` mean Todo, Deadline, and Event. `[ ]` means incomplete;
`[X]` means completed. Task numbers begin at 1 and change after deletion.

## Update tasks

Use a task number from `list` or `find`:

| Command | Result |
| --- | --- |
| `mark 2` | Mark task 2 as completed. |
| `unmark 2` | Mark task 2 as incomplete again. |
| `delete 2` | Permanently remove task 2 and renumber the list. |
| `remove 2` | Do the same as `delete 2`. |

## Exit and saved tasks

Enter `bye` to close Yanny. It saves tasks in `data/yanny.txt` in the directory
from which you run it, and loads them when you start it there again.
Descriptions and Event start/end values cannot contain `|`.
