# Zack User Guide
Zack is a command-line task manager for todos, deadlines, and events. Tasks are saved automatically between sessions.

## Getting Started
1. Install Java 25 and check it with `java -version`.
2. Download `Zack.jar` from [GitHub Releases](https://github.com/zackyesten/ip/releases).
3. Place it in a writable folder. Open a terminal in that folder and run `java -jar Zack.jar`.
4. Enter one command per line. Use `bye` to exit.

## Commands
Command words are lowercase. Replace the example descriptions, dates, and task numbers with your own values.

| Action | Example | Result |
|---|---|---|
| Add a todo | `todo read book` | Adds a task without a date. |
| Add a deadline | `deadline submit UG /by 2026-10-02` | Adds a task due on Oct 02 2026. |
| Add an event | `event meeting /from Monday 14:00 /to Monday 15:00` | Adds an event with textual start and end values. |
| List tasks | `list` | Displays all tasks and their current numbers. |
| Mark completed | `mark 2` | Marks task 2 as completed. |
| Mark incomplete | `unmark 2` | Marks task 2 as not completed. |
| Delete a task | `delete 2` | Removes task 2. There is no undo command. |
| Search descriptions | `find book` | Finds descriptions containing this text, ignoring case. |
| Query deadlines | `due 2026-10-02` | Lists deadlines on that date, including completed ones. |
| Exit | `bye` | Displays a farewell message and exits. |

## Dates, Searches, and Task Numbers
Deadline dates and `due` queries require a valid date in `yyyy-MM-dd` format. For example, `2026-02-30` is rejected. Deadlines display dates as `Oct 02 2026`; time-of-day values are not supported for deadlines.

Event start and end values remain text: Zack does not validate their dates or chronological order. The `due` command searches deadlines only.

Searches match a continuous phrase in the description: `find read book` matches `read Book`, but not `read a book`. Searches do not inspect dates or completion status. Empty search text is rejected.

`[T]`, `[D]`, and `[E]` identify todos, deadlines, and events. `[X]` means completed; `[ ]` means incomplete. Search results retain numbers from the full task list. Deletion changes subsequent numbers, so run `list` before choosing another task.

## Saving and Troubleshooting
Zack uses `data/zack.properties` relative to the terminal's working directory. It creates the folder and file when missing. Always launch from the same folder to use the same task list.

Successful add, mark, unmark, and delete commands automatically save changes. If saving fails, changes remain in memory but may be lost on exit. Check folder permissions and available disk space before continuing.

Invalid commands display an error without closing the app. If the saved file cannot be read or contains invalid data, Zack reports the problem and stops without overwriting that file.

Close Zack before editing or restoring saved data, and make a backup first. Older deadline values such as `Sunday` must be replaced with the intended date in `yyyy-MM-dd` format. Alternatively, rename the old file to preserve it and start Zack with a new empty list.