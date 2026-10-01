# Bastion User Guide

Bastion is a friendly chatbot that keeps track of your tasks in the terminal. Add to-dos, deadlines and events, find what you need, and mark tasks as done as you go.

## Quick start

1. Install **Java 25**.
2. Download [Bastion.jar](https://github.com/jinghaochua/ip/raw/refs/heads/master/Bastion.jar) and put it in a folder where you want to keep your tasks.
3. Open a terminal in that folder and run:

   ```sh
   java -jar Bastion.jar
   ```

4. Type `todo read a book` and press **Enter** to add your first task. Type `list` to see it, or `bye` to exit.

Always start Bastion from the same folder so it can find your saved tasks.

## Using commands

Type one command per line and press **Enter**. Commands are lowercase. Replace capitalised placeholders such as `DESCRIPTION` with your own text; do not type the placeholder itself. Keep spaces around `/by`, `/from` and `/to` as shown.

### Add a to-do: `todo`

Use a to-do for a task without a date.

**Format:** `todo DESCRIPTION`

**Example:** `todo read a book`

Bastion adds an unfinished task and shows the updated task count. Your task appears as:

```text
[T][ ] read a book
```

### Add a deadline: `deadline`

Use a deadline for something you need to finish by a particular date or time.

**Format:** `deadline DESCRIPTION /by DATE`

**Example:** `deadline submit report /by 2026-10-03 1800`

The new task appears as:

```text
[D][ ] submit report (by: Oct 03 2026 6:00 PM)
```

You can also enter just a date: `deadline return book /by 2026-10-05`.

### Add an event: `event`

Use an event for an activity with a start and end time.

**Format:** `event DESCRIPTION /from START /to END`

**Example:** `event study group /from 2026-10-03 1400 /to 2026-10-03 1600`

The new task appears as:

```text
[E][ ] study group (from: Oct 03 2026 2:00 PM to: Oct 03 2026 4:00 PM)
```

Enter the full date for both the start and end. Check that the end is after the start: Bastion does not currently check their order.

### Date and time formats

Deadlines and event start/end times accept these formats:

| Format | Example |
| --- | --- |
| `yyyy-mm-dd` | `2026-10-03` |
| `yyyy-mm-dd HHmm` | `2026-10-03 1800` |
| `d/m/yyyy HHmm` | `3/10/2026 1800` |

Use a four-digit, 24-hour time without a colon: `0930` means 9:30 AM and `1800` means 6:00 PM. A date without a time is treated as midnight at the start of that day. Words such as `tomorrow` are not supported.

### View all tasks: `list`

**Command:** `list`

Shows all tasks, including completed ones, with task numbers starting at 1. For example:

```text
Here are the tasks in your list:
1.[T][ ] read a book
2.[D][X] submit report (by: Oct 03 2026 6:00 PM)
```

`[T]` means to-do, `[D]` means deadline, and `[E]` means event. `[ ]` means unfinished; `[X]` means done.

### Find tasks: `find`

**Format:** `find TEXT`

**Example:** `find book`

Shows tasks whose descriptions contain your text, ignoring letter case. For example, `book` matches both `read a book` and `Book tickets`. Multiple words are searched as one phrase: `find study group` looks for the text `study group`. Dates are not searched. If nothing matches, Bastion displays `No matching tasks found.`

**Before marking, unmarking or deleting a search result, run `list` to get its full-list number.** Search results have their own numbering, but the commands below always use numbers from the full task list.

### Mark a task as done: `mark`

**Format:** `mark NUMBER`

**Example:** `mark 1`

Marks task 1 from `list` as done and displays the updated task with `[X]`. The task stays in your list.

### Mark a task as unfinished: `unmark`

**Format:** `unmark NUMBER`

**Example:** `unmark 1`

Changes task 1 back to unfinished, shown as `[ ]`.

### Delete a task: `delete`

**Format:** `delete NUMBER`

**Example:** `delete 1`

Removes task 1 immediately and shows the remaining task count. There is no undo command. Run `list` again before deleting another task because the remaining tasks are renumbered.

### Exit: `bye`

**Command:** `bye`

Closes Bastion with a goodbye message.

## Saving your tasks

Bastion automatically saves after you add, mark, unmark or delete a task, and loads your tasks when you next start it. No save command is needed.

Your tasks are stored in `data/bastion.txt`, relative to the folder from which you launch Bastion. To back up or transfer your tasks, close Bastion and copy this file, keeping it inside a `data` folder at the destination. Avoid editing the file by hand.

## If something goes wrong

- **Unknown command:** use lowercase commands exactly as shown above.
- **Missing description or date:** provide all required parts, including `/by` or `/from` and `/to` where needed.
- **Invalid date:** use one of the supported formats and a valid calendar date.
- **Invalid task number:** run `list`, then use an existing positive task number with one space after the command, such as `mark 2`.
- **Task list full:** Bastion supports up to 100 tasks. Delete tasks you no longer need; marking them done does not free space.
- **Tasks missing after restarting:** check that you launched Bastion from the same folder as before.

Avoid '|' (a vertical bar surrounded by spaces) in descriptions, as it is used to separate saved task fields.
