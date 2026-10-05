---
layout: default
title: Ted User Guide
permalink: /
---

<p class="eyebrow">A little order, one command at a time</p>

# Ted User Guide

<p class="lede">Keep track of todos, deadlines and events from your terminal. Add a task, find it later, and mark it done.</p>

Ted saves your changes automatically and restores your tasks the next time you open it.

<div class="intro-links"><a class="primary-link" href="#quick-start">Get started <span aria-hidden="true">↗</span></a><a href="#command-summary">See all commands <span aria-hidden="true">↓</span></a></div>

<div class="terminal-example" aria-label="Example of a Ted task list">
<div class="terminal-label">A look inside Ted</div>
<pre><code><span class="terminal-command">list</span>
Here are the tasks in your list:
1.[T][X] borrow book
2.[D][ ] return book (by: Oct 15 2026)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)</code></pre>
</div>

## Quick start

1. Install **Java Development Kit (JDK) 25**. In a terminal, run `java --version` and check that the version begins with `25`.
2. [Download `ted-1.1.0.jar`](https://github.com/holsoma/ip/releases/download/v1.1.0/ted-1.1.0.jar) from the [latest release](https://github.com/holsoma/ip/releases/latest). Keep it as a `.jar` file; do not extract it.
3. Put the JAR in a folder of your choice, for example `Ted`, and open a terminal in that folder.
4. Start Ted with this terminal command:

   ```text
   java -jar ted-1.1.0.jar
   ```

5. When Ted greets you, enter `todo borrow book`, then `list`. Type `bye` to exit.

Launch Ted from the same folder each time to return to your saved tasks. Use the terminal command rather than double-clicking the JAR, so you can enter commands and see Ted's replies.

### Run from source instead

If you prefer to run the source, [download the source for v1.1.0](https://github.com/holsoma/ip/archive/refs/tags/v1.1.0.zip), extract it, and open a terminal in the folder containing `src` and `docs`. With JDK 25, run:

```text
java src/main/java/ted/Ted.java
```

Java compiles the source when you launch it this way.

## Reading your tasks

Each task has a number, a type and a completion status:

```text
2.[D][ ] return book (by: Oct 15 2026)
```

| Part | Meaning |
| --- | --- |
| `2.` | Task number in the list being displayed. |
| `[T]`, `[D]`, `[E]` | Todo, deadline or event. |
| `[ ]`, `[X]` | Not done or done. |
| `(by: ...)`, `(from: ... to: ...)` | A deadline's date or an event's start and end details. |

Tasks appear in the order you added them. Marking a task as done keeps it in the list.

## Commands

Enter one command per line and press **Enter**. Use lowercase command words and the spaces shown in each format. Do not add spaces before the command; enter `list` and `bye` exactly as shown.

In the formats below, uppercase words such as `DESCRIPTION` are placeholders. Replace them with your own text, without adding quotation marks. Enter these commands inside Ted, after its greeting. Examples use a small sample list; your task numbers and totals may differ.

### Add a todo

Create a task that does not need a date or time.

**Format:** `todo DESCRIPTION`

**Example:** `todo borrow book`

Ted adds `[T][ ] borrow book` and shows the new total number of tasks. The description must not be empty.

### Add a deadline

Create a task with a due date.

**Format:** `deadline DESCRIPTION /by yyyy-MM-dd`

**Example:** `deadline return book /by 2026-10-15`

Ted adds `[D][ ] return book (by: Oct 15 2026)`. Enter the date as a four-digit year, two-digit month and two-digit day. Ted displays it with an English month name.

Use a real calendar date. For example, `2026-02-30` is rejected and no task is added. Deadlines accept a date only, without a time.

### Add an event

Create a task with a start and an end.

**Format:** `event DESCRIPTION /from START /to END`

**Example:** `event project meeting /from Mon 2pm /to 4pm`

Ted adds `[E][ ] project meeting (from: Mon 2pm to: 4pm)`. The description, start and end must all contain text.

Event details are kept as you enter them. You can use dates, times or words such as `Monday`; Ted does not check whether the end is after the start.

### List all tasks

See every task, including completed tasks, with its current number.

**Format:** `list`

For example, after adding the todo and deadline above:

```text
Here are the tasks in your list:
1.[T][ ] borrow book
2.[D][ ] return book (by: Oct 15 2026)
```

An empty list shows the heading with no task rows.

### Find tasks

Search for text in task descriptions.

**Format:** `find KEYWORD`

**Example:** `find book`

```text
Here are the matching tasks in your list:
1.[T][ ] borrow book
2.[D][ ] return book (by: Oct 15 2026)
```

- Search is **case-sensitive**: `book` matches `notebook`, but not `Book`.
- You can search for a phrase, for example `find return book`.
- All task types and completed tasks are included. Dates and event times are not searched.
- If nothing matches, Ted shows the heading with no task rows.

<div class="note"><strong>Use <code>list</code> before changing a task.</strong> Search results are numbered from one within the results. The <code>mark</code>, <code>unmark</code> and <code>delete</code> commands always use numbers from the full task list.</div>

### Mark or unmark a task

Update a task's completion status using its number from `list`.

**Formats:** `mark TASK_NUMBER` · `unmark TASK_NUMBER`

**Example:** `mark 2`

```text
Nice! I've marked this task as done:
  [D][X] return book (by: Oct 15 2026)
```

To change it back, enter `unmark 2`. Ted changes `[X]` to `[ ]` and keeps the task in the same position.

### Delete a task

Remove a task using its number from `list`.

**Format:** `delete TASK_NUMBER`

**Example:** `delete 2`

```text
Noted. I've removed this task:
  [D][ ] return book (by: Oct 15 2026)
Now you have 1 task in the list.
```

Deletion happens immediately and has no undo command. Later tasks move up one number, so run `list` again before making another change.

### Exit Ted

**Format:** `bye`

Ted says goodbye and closes. Your task changes have already been saved.

## Saving your tasks

Ted saves after you add, mark, unmark or delete a task. It loads the saved list when you start it. There is no separate save command.

Your tasks are stored in **`data/ted.txt`**, relative to the folder where you ran the launch command. For example, launching from a folder named `Ted` saves them in `Ted/data/ted.txt`. Always launch from the same folder to use the same list. To move Ted to another folder, take its `data` folder with you.

A missing data file starts an empty list. If a saved record is invalid, Ted reports it and skips it; the next save keeps only the records that loaded successfully. Re-add skipped older deadlines containing text such as `Sunday` using `deadline DESCRIPTION /by yyyy-MM-dd`.

If Ted reports a save error, the change is still present for that session but may be lost after exit. Keep Ted open while you check that the folder is writable, then make a task change to try saving again.

## Troubleshooting

Ted prefixes command errors with `OOPS!!!` and lets you try again.

| If you see… | What to do |
| --- | --- |
| `java` is not recognised or not found | Install JDK 25 and add its `bin` folder to your system's `PATH`. Reopen your terminal and check `java --version`. |
| `Unable to access jarfile ted-1.1.0.jar` | Check that the JAR is downloaded and your terminal is open in the folder containing it. Use its exact filename. |
| `UnsupportedClassVersionError` | Run `java --version` in the same terminal and make sure it uses Java 25. |
| Java cannot find `src/main/java/ted/Ted.java` | Open your terminal in the extracted project folder before running the launch command. |
| `Please provide a valid deadline date in yyyy-MM-dd format.` | Use a valid date, for example `2026-10-15`. |
| `Use: deadline DESCRIPTION /by yyyy-MM-dd` | Include a description, `/by` and a date, with spaces as shown. |
| `Use: event DESCRIPTION /from START /to END` | Include a description, `/from`, a start, `/to` and an end. |
| `Please provide a valid task number.` | Enter a whole number after `mark`, `unmark` or `delete`. |
| `That task number is not in the list.` | Run `list` and choose a number from that full list. Numbers start at 1. |
| `I'm sorry, but I don't know what that means :-(` | Check the command's spelling, lowercase letters and spacing. |
| A load error | Check that `data/ted.txt` and its folder are readable. Exit without changing tasks, fix the file access, and restart Ted to reload your list. |
| A save error | Check that `data/ted.txt` and its folder are writable. Keep Ted open and make a task change to try saving again. |

Example output in this guide omits Ted's separator lines and leading indentation for readability.

## Command summary

| Action | Command |
| --- | --- |
| Add a todo | `todo DESCRIPTION` |
| Add a deadline | `deadline DESCRIPTION /by yyyy-MM-dd` |
| Add an event | `event DESCRIPTION /from START /to END` |
| List all tasks | `list` |
| Find tasks | `find KEYWORD` |
| Mark as done | `mark TASK_NUMBER` |
| Mark as not done | `unmark TASK_NUMBER` |
| Delete a task | `delete TASK_NUMBER` |
| Exit | `bye` |
