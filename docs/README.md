# Hedy

Hedy is a chatbot that keeps a list of todos, deadlines, and events. You type a command, and Hedy replies between two lines.

To run the downloaded program, open a terminal in the folder that contains `Hedy.jar` and use Java 25:

```
java -jar Hedy.jar
```

Hedy saves your list in `data/duke.txt` in that same folder. The first time you run it, that folder does not need to exist yet.

## Commands

Type the command and press Enter. A blank line is ignored.

| Command | What it does |
| --- | --- |
| `list` | Shows every task |
| `todo DESCRIPTION` | Adds a todo |
| `deadline DESCRIPTION /by yyyy-mm-dd` | Adds a deadline on that date |
| `event DESCRIPTION /from START /to END` | Adds an event |
| `mark NUMBER` | Marks task NUMBER as done |
| `unmark NUMBER` | Marks task NUMBER as not done |
| `delete NUMBER` | Removes task NUMBER |
| `find KEYWORD` | Shows tasks whose description contains KEYWORD |
| `on yyyy-mm-dd` | Shows deadlines due on that date |
| `bye` | Says goodbye and stops |

Task numbers start at 1. They match the numbers shown by `list`.

## Adding a todo

```
todo read book
```

```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 task in the list.
____________________________________________________________
```

`todo` on its own is rejected, because a todo needs a description.

## Adding a deadline

Give the date as `yyyy-mm-dd`. Hedy stores that date and prints it in a longer form, such as `Oct 15 2019`.

```
deadline return book /by 2019-10-15
```

```
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Oct 15 2019)
 Now you have 2 tasks in the list.
____________________________________________________________
```

`deadline return book /by Sunday` is rejected. Use `deadline return book /by 2019-10-15` instead.

A deadline that is not done, and whose date is before today, is printed with `overdue`. Marking it done removes that word. A deadline due today is not overdue.

## Adding an event

The start and end are stored as you type them.

```
event project meeting /from Mon 2pm /to 4pm
```

```
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
```

## Marking, unmarking, and deleting

`mark 1` marks task 1 as done. `unmark 1` marks it as not done. `delete 2` removes task 2 and renumbers the tasks after it.

```
delete 2
```

```
____________________________________________________________
 Noted. I've removed this task:
   [D][ ] return book (by: Oct 15 2019)
 Now you have 2 tasks in the list.
____________________________________________________________
```

## Finding a task

`find` looks for the keyword anywhere in the description. Capital letters do not matter.

```
find book
```

```
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][X] read book
 2.[D][X] return book (by: Jun 06 2019)
____________________________________________________________
```

The numbers in this reply count the matches, not the numbers from `list`.

## Deadlines on a date

`on` shows only the deadlines due on that day. The numbers are the same ones `list` uses, so you can `mark` or `delete` them afterwards. Todos and events are not included.

```
on 2019-10-15
```

```
____________________________________________________________
 Here are the deadlines due on Oct 15 2019:
 2.[D][ ] return book (by: Oct 15 2019)
____________________________________________________________
```

If nothing is due that day, Hedy says so. `on Sunday` is rejected. Use `on 2019-10-15`.

## Saving

Hedy writes `data/duke.txt` after every add, delete, mark, and unmark. When you start Hedy again, that list is loaded. If a saved line is damaged, Hedy skips that line, tells you, and keeps the rest.

## When a command is wrong

Hedy stays running and tells you how to fix the command. For example, an unknown word, a missing description, a date that is not `yyyy-mm-dd`, or a task number that is not in the list all get a message starting with `OOPS!!!`.
