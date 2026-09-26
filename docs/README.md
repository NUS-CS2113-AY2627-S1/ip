# SamSquare User Guide

A task can be a small reminder, a piece of work with a due date, or an activity
that takes up part of your day. SamSquare keeps all three in one list. Type
what you want to do in its terminal window; it responds and saves your changes.

[Launch](#launch-samsquare) · [Walkthrough](#try-it-with-a-small-project) ·
[Command reference](#choose-your-next-action) · [Your data](#pick-up-where-you-left-off) ·
[Help](#if-you-get-stuck)

## Launch SamSquare

You need **Java 25** and the application JAR. Download the `.jar` asset from the
[latest release](https://github.com/Rixte/ip/releases/latest) and place it in a
folder you want to use for your tasks. The instructions below call it `ip.jar`;
rename your download to match, or use its actual filename in the launch command.

Open PowerShell on Windows, or Terminal on macOS/Linux. Change to the folder
containing the JAR with `cd` followed by your folder path in quotes. Then run:

```text
java -version
```

Check that the version starts with `25`, then launch the app:

```text
java -jar ip.jar
```

Keep the terminal open. Beneath the welcome artwork, you will see
`What can I do for you?`—that is your cue to enter a command. Press Enter after
each command. You do not need IntelliJ, and you should launch through the
terminal rather than double-clicking the JAR.

## Try it with a small project

Imagine you are preparing a project demo. This walkthrough starts with an
**empty task list**. If you already have tasks, use their current numbers from
`list` instead of the example numbers below. You can also try the walkthrough
with a copy of the JAR in a new, empty folder to keep it separate from your tasks.

### Capture the work

First, record something you can do whenever you have time:

```text
todo sketch demo screens
```

Next, give the work with a due date a deadline:

```text
deadline finish demo /by 2026-10-02
```

Finally, set aside time to practise:

```text
event demo rehearsal /from Thu 3pm /to 4pm
```

Each addition receives a confirmation and an updated task count. Enter `list`
to see the three tasks together:

```text
 Here are the tasks in your list:
 1.[T][ ] sketch demo screens
 2.[D][ ] finish demo (by: Oct 02 2026)
 3.[E][ ] demo rehearsal (from: Thu 3pm to: 4pm)
____________________________________________________________
```

Read an entry from left to right: its list number, its type, its completion
status, and its description. `T` means ToDo, `D` means deadline, and `E` means
event. The empty `[ ]` means the task is still waiting to be done.

### Record your progress

Once the sketches are ready, enter:

```text
mark 1
```

SamSquare confirms the change. The first task now appears as
`[T][X] sketch demo screens`; `[X]` means complete. It stays in your list so
you can still see what you have finished.

If you marked it too soon, `unmark 1` changes it back to `[ ]`. Repeating
`mark` on a completed task, or `unmark` on an incomplete task, keeps its status
as it is.

### Check what needs your attention

Enter `find demo` to find descriptions containing the word `demo`. All three
example tasks match, including completed ones.

For a particular due date, enter:

```text
on 2026-10-02
```

SamSquare responds:

```text
 Here are the deadlines on 2026-10-02:
 1.[D][ ] finish demo (by: Oct 02 2026)
____________________________________________________________
```

**The `1` here is a search-result number.** The deadline is still task `2` in
the full list. Always run `list` before using `mark`, `unmark`, or `delete`
after a search. Both `find` and `on` number their results afresh from 1.

### Change your plans and close the app

If the rehearsal is cancelled, run `list`, then `delete 3`. SamSquare removes
the event and tells you how many tasks remain. Deletion is permanent: there is
no undo command. Removing a task also shifts the numbers of tasks after it.

Enter `bye` when you are finished. Your successful changes are saved as you
make them. The next launch from the same folder restores the remaining tasks
and their completion statuses.

## Choose your next action

Use this reference for your own tasks. Replace the capitalised placeholders
with your text; the placeholders themselves are not part of the command.
Every argument shown is required.

| You want to… | Enter |
| --- | --- |
| Remember something without a date | `todo DESCRIPTION` |
| Give a task a due date | `deadline DESCRIPTION /by DATE` |
| Record an activity with a start and end | `event DESCRIPTION /from START /to END` |
| See the whole list and its task numbers | `list` |
| Search the text of your descriptions | `find TEXT` |
| See deadlines due on one date | `on DATE` |
| Record that a task is finished | `mark NUMBER` |
| Put a completed task back in progress | `unmark NUMBER` |
| Remove a task permanently | `delete NUMBER` |
| Close SamSquare | `bye` |

### Writing commands

Use lowercase command names and enter one command per line. Descriptions can
contain several words. Spaces at the beginning and end of a command are
ignored, while spaces within a description are retained.

Put spaces around `/by`, `/from`, and `/to`. For an event, `/from` must come
before `/to`, and both time values must be present. `list` and `bye` accept no
extra words.

Task numbers must be whole numbers from the latest `list`, starting at 1.
Missing arguments, empty descriptions, unknown commands, and invalid task
numbers produce an error without changing your task list.

### Working with dates and times

A deadline's `DATE` must be a real date in **`yyyy-MM-dd`** format: four digits
for the year, two for the month, and two for the day. For example, `2026-10-02`
is accepted and displayed as `Oct 02 2026`.

SamSquare rejects impossible dates such as `2026-02-30`, other formats such as
`02/10/2026`, words such as `Friday`, and dates followed by a time. Leap days
are accepted only in leap years, such as `2024-02-29`.

Event start and end values are free text, so `Thu 3pm` is allowed. SamSquare
does not validate event dates or check that the end is later than the start.

### Understanding the three views

`list` shows all tasks in their current order, including completed ones. It
is the view to use when choosing a task number for an operation.

`find TEXT` looks only at descriptions. It is case-sensitive and matches text
inside words: `demo` matches `demo rehearsal`, while `Demo` does not. You can
also search a phrase, such as `find demo rehearsal`. An empty search is
rejected; spaces surrounding the phrase are ignored. Dates, event times, and
status markers are not searched.

`on DATE` matches deadlines due on that exact date using the same date format
as `deadline`. It includes completed deadlines and excludes ToDos and events.
Missing or invalid dates are rejected.

Both searches preserve task order and leave your list and save file unchanged.
An empty list or a search with no matches displays its heading and separator
without task entries.

## Pick up where you left off

SamSquare creates `data/samsquare.txt` beneath the folder from which you launch
it. Additions, completion changes, and deletions are saved automatically after
each successful command. There is no separate save command, and `bye` does not
perform an additional save.

**Keep using the same launch folder.** Launching from another folder uses a
different save-file location and may give you an empty list.

To move your tasks to another location, close SamSquare and copy both the JAR
and its `data` folder. If you previously ran the app in IntelliJ, copy the
project's `data` folder into your new JAR folder before launching there. Avoid
overwriting an existing task file at the destination.

Keep a separate copy of `data/samsquare.txt` if you want a backup. Avoid editing
the file manually. Also avoid a vertical bar (`|`) surrounded by spaces in
descriptions and event times: it can prevent those tasks from loading again.

## If you get stuck

**The app will not start.** If the terminal cannot find `java`, install Java 25
and add its `bin` folder to your system's `PATH`, then open a new terminal.
`UnsupportedClassVersionError` means you are using an older Java version;
check again with `java -version`. For `Unable to access jarfile`, check the
current folder and filename, and put a filename containing spaces in quotes.

**A command was rejected.** Check its spelling and required arguments against
the reference above. For a number error, run `list` and choose a number that
currently exists. Correct the command and try again; rejected commands leave
your tasks unchanged.

**Your tasks appear to have disappeared.** Check your launch folder first.
Your existing tasks may be in the `data` folder at the previous location.

**SamSquare reports `Error loading tasks.` or `Error saving tasks.`** Check
that the data folder and file are readable and writable. Back up the file
before changing it. Restart after resolving a loading error. A saving error
means the latest changes may not survive a restart.

**Some older deadlines were skipped.** Free-text dates such as `Friday`
cannot be loaded as calendar dates. SamSquare reports this at startup and,
before the next save, copies the original file to a uniquely named
`data/samsquare-legacy-dates-*.txt` backup. Use the backup to re-enter the skipped
deadlines with explicit dates. If the backup cannot be made, saving is aborted
and the original file is preserved.
