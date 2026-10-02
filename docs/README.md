# Nova User Guide

Nova is a task manager you use from the command line. It keeps track of your todos, deadlines and events, remembers them between sessions, and has opinions about all of them.

- [Quick start](#quick-start)
- [Features](#features)
  - [Adding a todo: `todo`](#adding-a-todo-todo)
  - [Adding a deadline: `deadline`](#adding-a-deadline-deadline)
  - [Adding an event: `event`](#adding-an-event-event)
  - [Listing all tasks: `list`](#listing-all-tasks-list)
  - [Marking a task as done: `mark`](#marking-a-task-as-done-mark)
  - [Unmarking a task: `unmark`](#unmarking-a-task-unmark)
  - [Deleting a task: `delete`](#deleting-a-task-delete)
  - [Finding tasks: `find`](#finding-tasks-find)
  - [Exiting: `bye`](#exiting-bye)
  - [Where your tasks are saved](#where-your-tasks-are-saved)
- [Command summary](#command-summary)

## Quick start

1. Check that you have Java 25 installed (`java -version` will tell you).
2. Download `nova.jar` from the [Releases](https://github.com/anannya-mk/ip/releases) page.
3. Put it in an empty folder. Nova creates a `data` folder next to it to store your tasks.
4. Open a terminal in that folder and run `java -jar nova.jar`.
5. Try `todo read book`, then `list`.

## Features

A few things that apply to every command:

- Words in `UPPER_CASE` are what you fill in. In `todo DESCRIPTION`, you might type `todo read book`.
- Task numbers are the ones `list` shows, starting from 1.
- Commands aren't case-sensitive, so `TODO` works as well as `todo`.

### Adding a todo: `todo`

The simplest kind of task: just a description.

Format: `todo DESCRIPTION`

Example: `todo read book`

```
____________________________________________________________
I've added this to your ever-growing pile:
  [T][ ] read book
You now have 1 tasks. Do try to keep up.
____________________________________________________________
```

Leave out the description and Nova will point that out, not kindly.

### Adding a deadline: `deadline`

For something that needs to be done by a certain date.

Format: `deadline DESCRIPTION /by DATE`

Write the date as `yyyy-mm-dd`. Nova shows it back in a friendlier form.

Example: `deadline return book /by 2026-10-15`

```
____________________________________________________________
I've added this to your ever-growing pile:
  [D][ ] return book (by: Oct 15 2026)
You now have 2 tasks. Do try to keep up.
____________________________________________________________
```

Anything else, like `Sunday` or `October 20`, gets rejected, with commentary.```

### Adding an event: `event`

For something with a start and an end.

Format: `event DESCRIPTION /from START /to END`

Example: `event project meeting /from Mon 2pm /to 4pm`

```
____________________________________________________________
I've added this to your ever-growing pile:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
You now have 3 tasks. Do try to keep up.
____________________________________________________________
```

You need both `/from` and `/to`, in that order.

### Listing all tasks: `list`

Format: `list`

```
____________________________________________________________
1.[T][ ] read book
2.[D][ ] return book (by: Oct 15 2026)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
```

The first bracket is the task type (`T`, `D` or `E`). The second has an `X` once the task is done.

### Marking a task as done: `mark`

Format: `mark TASK_NUMBER`

Example: `mark 1`

```
Huh. I'm almost proud. Almost.
Your task has been marked.
1.[T][X] read book
```

Marking something that's already done works too, but Nova won't let it slide.

### Unmarking a task: `unmark`

Changed your mind? This sets a task back to not done.

Format: `unmark TASK_NUMBER`

Example: `unmark 1`

```
Very well, your task is unmarked.
1.[T][ ] read book
```

### Deleting a task: `delete`

Format: `delete TASK_NUMBER`

Example: `delete 2`

```
____________________________________________________________
Noted. I've removed this task:
   [D][ ] return book (by: Oct 15 2026)
Now you have 2 tasks in the list.
____________________________________________________________
```

There's no undo. Everything below the deleted task moves up one number, so run `list` again before your next `mark` or `delete`.

### Finding tasks: `find`

Format: `find KEYWORD`

Example: `find book`

```
____________________________________________________________
Here are the matching tasks in your list:
1.[T][ ] read book
____________________________________________________________
```

Capitals don't matter, and part of a word is enough, so `find book` also picks up `Bookshelf`.

One catch: the numbers here only count the matches. They're not the numbers `mark` and `delete` use, so check `list` first.

### Exiting: `bye`

Format: `bye`

### Where your tasks are saved

You don't have to save anything yourself. Nova writes your tasks to `data/nova.txt` after every command and loads them again next time.

You can edit that file by hand, but any line Nova can't make sense of gets skipped when it loads.

## Command summary

| Action   | Format                                  | Example                                       |
|----------|-----------------------------------------|-----------------------------------------------|
| Todo     | `todo DESCRIPTION`                      | `todo read book`                              |
| Deadline | `deadline DESCRIPTION /by TIME`         | `deadline return book /by Sunday`             |
| Event    | `event DESCRIPTION /from START /to END` | `event project meeting /from Mon 2pm /to 4pm` |
| List     | `list`                                  | `list`                                        |
| Mark     | `mark TASK_NUMBER`                      | `mark 1`                                      |
| Unmark   | `unmark TASK_NUMBER`                    | `unmark 1`                                    |
| Deadline | `deadline DESCRIPTION /by DATE`         | `deadline return book /by 2026-10-15`         |
| Find     | `find KEYWORD`                          | `find book`                                   |
| Exit     | `bye`                                   | `bye`                                         |