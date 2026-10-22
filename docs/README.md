---
layout: default
permalink: /index.html
---

# Isa User Guide

Isa is a command-line task manager for todos, deadlines, and events. Type one
command at a time and press Enter. Isa saves changes to your tasks automatically.

## Getting started

1. Install JDK 25 and open this project in IntelliJ IDEA.
2. Set the project SDK to JDK 25 and mark `src/main/java` as the source root.
3. Run the `main` method in `src/main/java/isa/ui/Isa.java`. Set the run
   configuration's working directory to the project root so Isa can find your
   saved tasks.
4. Try `todo read book`, then `list` to see your task.

Isa displays tasks with a type (`T` for todo, `D` for deadline, `E` for event)
and a status (`[ ]` for incomplete, `[X]` for done). The numbers shown by `list`
identify tasks for `mark`, `unmark`, and `delete`.

## Features

In the formats below, replace words in `UPPER_CASE` with your own values. Dates
use `yyyy-MM-dd` (for example, `2026-08-06`). Event times use
`yyyy-MM-ddTHH:mm` in 24-hour time (for example, `2026-08-06T14:00`).

### Add a todo: `todo`

Format: `todo DESCRIPTION`

Example: `todo read book` adds `[T][ ] read book`.

### Add a deadline: `deadline`

Format: `deadline DESCRIPTION /by yyyy-MM-dd`

Example: `deadline return book /by 2026-08-07` adds a task displayed as
`[D][ ] return book (by: Aug 07 2026)`. Isa rejects impossible dates such as
`2026-02-30`.

### Add an event: `event`

Format: `event DESCRIPTION /from yyyy-MM-ddTHH:mm /to yyyy-MM-ddTHH:mm`

Example: `event project meeting /from 2026-08-06T14:00 /to 2026-08-06T16:30`
adds a task displayed as
`[E][ ] project meeting (from: Aug 06 2026 2pm to: Aug 06 2026 4:30pm)`.
The end must not be earlier than the start. Events may span multiple days.

### View all tasks: `list`

Format: `list`

Isa shows every task in order with its task number. Use that number when
changing or deleting a task.

### Mark or unmark a task: `mark`, `unmark`

Formats: `mark NUMBER` and `unmark NUMBER`

Examples: `mark 1` marks the first task done; `unmark 1` makes it incomplete
again. Get the number from `list`.

### Delete a task: `delete`

Format: `delete NUMBER`

Example: `delete 2` removes the second task in the full task list. The remaining
tasks receive new numbers the next time you use `list`.

### Find tasks by description: `find`

Format: `find KEYWORD`

Example: `find book` finds tasks with `book` anywhere in their descriptions,
regardless of capitalization. A phrase such as `find project meeting` also
works. Results keep their numbers from the full task list; search does not
look at deadline dates or event times.

### View tasks on a date: `on`

Format: `on yyyy-MM-dd`

Example: `on 2026-08-06` lists deadlines due that day and events occurring
that day, including an event spanning multiple dates. Todos have no date and
do not appear. Results retain their numbers from `list`.

### Exit Isa: `bye`

Format: `bye`

Isa displays a farewell message and stops.

## Saving your tasks

Isa saves your tasks after you add, mark, unmark, or delete one. Saved tasks are
loaded when Isa starts again. The data file is `data/isa.txt` relative to the
working directory set when you run Isa. If Isa cannot read a saved record, it
shows a warning and loads the other valid records. Keep a backup before editing
the data file yourself.
