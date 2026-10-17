# Noah User Guide

> Hey, Traveler! I'm Noah, your trusted companion.
> Ready for an adventure?

Noah keeps your todos, deadlines, and events in one place, right in your terminal.
Add a quest, tick it off, and pick up where you left off next time.

[Download Noah](https://github.com/littlecrawler/ip/releases/latest) ·
[View the source](https://github.com/littlecrawler/ip)

[Quick start](#quick-start) · [Command guide](#command-guide) ·
[Your saved tasks](#your-saved-tasks) · [Troubleshooting](#troubleshooting)

## Quick start

1. Install **Java 25**. Run `java -version` in a terminal to check your version.
2. Download **Noah.jar** from the release's **Assets** section.
3. Put it in its own folder. Open a terminal in that folder and run:

   ```text
   java -jar "Noah.jar"
   ```

4. Type a command and press **Enter**. Try these one at a time:

   ```text
   todo read book
   deadline return book /by 2026-10-04
   event study group /from Mon 2pm /to 4pm
   list
   ```

On a fresh quest board, `list` shows:

```text
Quest board, coming right up! Here are your tasks:
1.[T][ ] read book
2.[D][ ] return book (by: Oct 4 2026)
3.[E][ ] study group (from: Mon 2pm to: 4pm)
```

`[T]` means todo, `[D]` deadline, and `[E]` event. `[ ]` means unfinished;
`[X]` means done. Type `bye` when you are ready to leave.

## Command guide

Use lowercase command words and spaces as shown. Enter `list`, `clear`, and
`bye` on their own. Replace the example descriptions and numbers with your own.

| What you want to do | Example command |
| --- | --- |
| Add a todo | `todo read book` |
| Add a deadline | `deadline return book /by 2026-10-04` |
| Add an event | `event meeting /from 2pm /to 4pm` |
| See all tasks | `list` |
| Search descriptions | `find book` |
| Mark task 1 as done | `mark 1` |
| Mark task 1 as unfinished | `unmark 1` |
| Delete task 1 | `delete 1` |
| Remove every task | `clear` |
| Exit Noah | `bye` |

### Adding tasks

Every task needs a description containing more than just whitespace.
A todo needs nothing else:

```text
todo prepare presentation
```

Deadlines and events can also start without timing details:

```text
deadline submit report
event team meeting
```

If you use `/by`, supply a due value. For an event with timing details, supply
both `/from` and `/to`, in that order. Write these markers as separate words.
Text such as `/byte` or a URL stays part of the description.

### Deadline dates and times

Use either `yyyy-MM-dd` or `d/M/yyyy`. Slash dates are **day/month/year**.
Time is optional; add a 24-hour time as `HH:mm` or `HHmm`.

| After `/by` | Shown in the task list |
| --- | --- |
| `2026-10-04` | `Oct 4 2026` |
| `4/10/2026` | `Oct 4 2026` |
| `2019-12-02 18:00` | `Dec 2 2019 18:00` |
| `2/12/2019 1800` | `Dec 2 2019 18:00` |

For example:

```text
deadline return book /by 2/12/2019 1800
```

Noah adds `[D][ ] return book (by: Dec 2 2019 18:00)`. Numeric dates and times
must be valid: `2026-02-29` and `2026-10-04 24:00` are rejected. A date without a time
stays date-only.

Prefer a text label? That works too:

```text
deadline buy groceries /by Sunday
deadline send slides /by Mon 2pm
```

Labels such as `Sunday`, `June 6th`, and `after dinner` are kept as written;
Noah does not interpret them as calendar dates. Input beginning with a numeric
date pattern, such as `2026-` or `4/10/`, follows the numeric date rules above.

### Event times

```text
event project meeting /from Mon 2pm /to 4pm
```

Event start and end values are text labels. Noah displays them as entered;
it does not check whether the end is later than the start.

### Finding a task

```text
find book
```

Search matches text anywhere in a description, ignoring letter case.
For example, it finds both `Read BOOK` and `buy notebook`. It searches all task
types, including completed tasks, but not their dates or status markers.

Use `find read book` to search for that whole phrase. Punctuation is literal.
Results keep their **original list numbers**, so you can use those numbers
directly with `mark`, `unmark`, or `delete`.

### Completing and deleting tasks

Check `list` or `find` for the current number, then use:

```text
mark 1
unmark 1
delete 1
```

`mark` changes `[ ]` to `[X]`; `unmark` reverses it. `delete` removes the task
immediately. Remaining tasks are renumbered after deletion, so check `list`
before your next numbered command. These commands take numbers, not task names.

### Adding a duplicate

If a task with the same details already exists, Noah asks first:

```text
Deja vu! A task with the same details is already on the board:
1.[T][ ] read book
Add another copy? Type yes/no (y/n), or use another command to cancel.
```

- `yes` or `y` adds one new, unfinished copy.
- `no` or `n` cancels the addition.
- Another recognized command, such as `list` or `bye`, cancels the addition
  and runs normally. An invalid command leaves the confirmation pending.

Replies ignore letter case and surrounding spaces. A duplicate has the same
task type, exact description, and timing details, even if the existing task is
done. Text comparison is case-sensitive; equivalent numeric deadline formats
match. A date-only deadline differs from one with a time.

### Clearing the board

```text
clear
```

**This immediately deletes all tasks, including completed ones, and saves the
empty list. There is no confirmation or undo.** It also cancels a pending
duplicate addition. If the list is already empty, Noah tells you; your next
new task will be number 1.

## Your saved tasks

Noah saves automatically after additions, completion changes, deletions, and
clearing. You do not need a separate save command.

Tasks live in `data/noah.txt` inside the folder you **launch Noah from**.
Missing folders and files are created automatically. Launch from the same
folder next time to load the same tasks. Keep a copy of this file if you want
a backup, and move the `data` folder with your JAR when changing locations.

The `|` character is reserved for saving tasks. Noah rejects it in new task
descriptions and time labels; replace it with a word or another punctuation
mark. Descriptions and supplied time labels cannot contain only whitespace.

## Troubleshooting

| What happened? | What to do |
| --- | --- |
| The JAR will not start | Check `java -version` for Java 25. Open the terminal in the JAR's folder and use its exact filename. |
| Noah does not recognize a command | Check the lowercase spelling and the examples above. Type one command per line. |
| A task number is rejected | Run `list` again and use a whole number from 1 to the current task count. |
| A numeric date is rejected | Check the calendar date and 24-hour time. Use `yyyy-MM-dd` or day/month/year, with the full year. |
| Tasks seem to be missing | Check that you launched Noah from the folder containing your original `data/noah.txt`. |
| Loading or saving fails | Follow the reported file path and check its contents and permissions. Keep a backup before editing saved data. |

After a loading error, Noah uses an empty temporary board and blocks saving
to protect the original file. Repair the reported problem or restore a backup,
then restart. Tasks added in that failed session are not saved.

If a save fails, the latest changes remain only in memory. Resolve the file
problem before continuing; exiting does not save them automatically.

Ready for your next quest? Start with `todo` and make it happen. :)
