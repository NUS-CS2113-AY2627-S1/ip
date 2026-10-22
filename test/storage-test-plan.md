
## Automatic file saving

**Aim:** Verify the file is created and replaced after each task change while the chatbot is still running.
Run `python test/test-storage.py` after the UI runner compiles the application. Each run uses an isolated
directory under `build/ui-test` to protect real task data.

**Inputs:** `todo read book`, `deadline homework /by Friday`, `event lunch /from noon /to evening`,
`mark 1`, `unmark 1`, `list`, `blah`, `bye` (one command at a time).

**Expected output:** Normal addition and completion confirmations, the three tasks in the list,
the existing unknown-command error, and the farewell. Full console input/output is recorded in
`test/storage-test-sessions.md`. Existing UI cases verify the exact response text.

**Expected file:** After each addition, `data/mybff.txt` contains all tasks added so far, one per line:

```text
T | 0 | read book
D | 0 | homework | Friday
E | 0 | lunch | noon | evening
```

Mark changes the first line to `T | 1 | read book`; unmark restores `T | 0 | read book`.
List, unknown commands, and bye leave the file unchanged. Startup alone creates no file.
New saves use a `MyBff storage v3` header and readable text fields with backslash escapes. The snapshots above
show decoded fields. Existing pipe-separated and Base64 v2 files remain readable.

## Storage errors and special text

**Aim:** Preserve existing data on failure and avoid crashes. Run `python test/test-storage-errors.py`.

**Inputs:** For each malformed file (invalid type/status/field count, empty description, bad UTF-8,
or bad Base64), send `todo replacement`, `bye`.

**Expected output:** `OOPS!!! Could not load data/mybff.txt. Your saved file has not been changed.
Check the file and restart.` The application stops without changing the file.
A directory at the file path also gives this error. A blocked data directory either gives the load
error or rejects an addition with `OOPS!!! Could not save data/mybff.txt. No changes were made.
Check the file and try again.`

**Additional inputs:** Load a BOM-prefixed file with blank lines and a completed task; send `list`,
`todo pipes | and \ paths`, `bye`, then restart and list again. Expect both tasks with exact text and status.
Load 101 numbered tasks and send `list`, `todo next`, `bye`: expect all tasks in order and an addition
confirmation reporting 102 tasks. Restart with `list`, `bye` and expect all 102 tasks in order.
Full inputs and actual outputs are recorded in `test/storage-error-sessions.md`.
After loading two tasks, the error runner also blocks file replacement, then sends `mark 1`,
`unmark 2`, `todo rejected`, `list`, `bye`. Expect three save errors, no success confirmations,
the original two statuses and count, and no leftover temporary files.

## Load saved tasks on startup

**Aim:** Restore all task types and dates in order, then preserve loaded tasks when editing and adding.

**Inputs:** Restart after the saving case with `list`, `mark 2`, `todo next`, `bye`.
Restart again with `list`, `bye`.

**Expected output:** The first list shows the three saved tasks with their original dates and incomplete
statuses. Mark confirms homework is done. Adding next reports four tasks. The second restart lists:

```text
     Here are the tasks in your list:
     1.[T][ ] read book
     2.[D][X] homework (by: Friday)
     3.[E][ ] lunch (from: noon to: evening)
     4.[T][ ] next
```

The runner checks exact responses, including separators and farewell, and records every session.

**Additional fixtures and inputs:** With `T | 1 | done`, `D | 0 | homework | `,
and `E | 1 | lunch |  | ` saved as separate lines, run `list`, `bye`.
Expect a completed todo, an incomplete deadline with an empty by field, and a completed event
with empty from/to fields. With an empty file, `list`, `bye` shows an empty list and leaves the file empty.
The original saving test also checks startup with no file.

## Readable storage migration

**Aim:** Read existing Base64 tasks and save readable text.

**Inputs:** With `MyBff storage v2` and `T | 1 | cmVhZCBib29r` as the saved file,
send `list`, `todo next`, `bye`.

**Expected output:** List shows `[T][X] read book`; addition confirms next and a count of two.
The file becomes `MyBff storage v3`, `T | 1 | read book`, `T | 0 | next` on separate lines.
Existing special-character restart tests verify pipes and backslashes survive the new format.

## Persistent deletion from the JAR

**Aim:** Save deletions across restarts, including an empty list, and restore the original order if saving fails.

**Inputs:** In an isolated folder, add `todo first`, `todo second`, `todo third`, then `delete 2`, `bye`.
Restart with `list`, `delete 2`, `delete 1`, `bye`; restart with `list`, `delete 1`, `bye`.

**Expected output:** The first deletion confirms `[T][ ] second` and two remaining tasks.
After restarting, list contains only first and third in that order. Deleting both leaves an empty list
on the next restart; deleting from it reports `Invalid task number.` The save file contains only its header.

**Save failure:** Start with three saved tasks, block replacement of the save file after startup,
then enter `delete 2`, `list`, `bye`. Expect the standard save error, no deletion confirmation,
and all three tasks in their original order.

## Temporary and persistent file locks on Windows

**Aim:** Allow a briefly locked save file to be replaced, and preserve rollback when access stays denied.
Run `python test/test-storage-retry.py` after compiling the application. Other platforms skip this case.

**Inputs:** Start with `T | 0 | original` saved under the v3 header. After the greeting, hold a Windows
file handle that prevents replacement, then send `todo next`, `list`, `bye`.

**Expected output:** If the handle is released 150 ms after the addition's opening separator, addition
succeeds with two tasks, and list shows original followed by next. If the handle stays open, addition
reports `OOPS!!! Could not save data/mybff.txt. No changes were made. Check the file and try again.`
and list shows only original. Both sessions end with the normal farewell. The runner checks complete
responses exactly and records the inputs and output in `test/storage-retry-sessions.md`.

**Expected file:** Temporary denial saves both tasks. Persistent denial preserves the original bytes.
Neither case leaves temporary save files. Access-denied replacement is attempted at most five times,
with 100 ms between attempts; the original file is never deleted as a fallback.
