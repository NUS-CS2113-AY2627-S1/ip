# UI test plan

Use Java 25 to compile and run Ted. Use the test-ui skill's
`scripts/run_ui_tests.ps1` runner, with two separator lines per command.
Stop at the first failure and record the expected and actual output.
The latest complete console transcript is [ui-test-session.txt](ui-test-session.txt).

## Release JAR smoke test

Aim: verify the distributable JAR runs with Java 25 from a fresh folder
containing only that JAR, including a folder name with spaces. Check every
command, valid and invalid dates, search rules, Unicode text, automatic
saving and restored tasks. Stop at the first failed command.

Run from the project root with Java 25 available as `java`:

```text
pwsh -File test/run-release-smoke.ps1 -JarPath PATH_TO_JAR
```

Replace `PATH_TO_JAR` with the JAR's path. If `java` selects another version,
also supply `-JavaExecutable` with the full path to Java 25. The script copies
the JAR into a new folder under `_temp`, launches it with `java -jar`, and
records both sessions in `test/ui-test-session.txt`. Generated files and
task data stay under ignored `_temp` folders.

The manual **Release JAR smoke test** GitHub Actions workflow checks the
same prebuilt release asset on Windows, Linux and macOS with Java 25.
Supply the JAR's Base64-encoded bytes and SHA-256. Each job restores the
same bytes, verifies their checksum, runs this script and prints its
transcript. The workflow retains read-only repository permissions. The
encoded JAR and checksum must fit GitHub's 65,535-character input limit.

The script checks complete response blocks. Empty list/search responses
must contain the heading and no rows. Lists and searches below must contain
exactly the specified rows in order, followed by Ted's separator.

### Commands and saving

| # | Command | Expected output fragment |
| --- | --- | --- |
| 1 | `list` | `Here are the tasks in your list:` with no rows |
| 2 | `find book` | `Here are the matching tasks in your list:` with no rows |
| 3 | `todo borrow book` | `[T][ ] borrow book`; `Now you have 1 task in the list.` |
| 4 | `deadline return book /by 2026-10-15` | `[D][ ] return book (by: Oct 15 2026)`; total 2 tasks |
| 5 | `event project meeting /from Mon 2pm /to 4pm` | `[E][ ] project meeting (from: Mon 2pm to: 4pm)`; total 3 tasks |
| 6 | `todo notebook` | `[T][ ] notebook`; total 4 tasks |
| 7 | `todo Book launch` | `[T][ ] Book launch`; total 5 tasks |
| 8 | `todo café 読書` | `[T][ ] café 読書`; total 6 tasks |
| 9 | `deadline leap day /by 2024-02-29` | `[D][ ] leap day (by: Feb 29 2024)`; total 7 tasks |
| 10 | `deadline invalid date /by 2026-02-30` | `OOPS!!! Please provide a valid deadline date in yyyy-MM-dd format.` |
| 11 | `list` | The seven initial rows shown below |
| 12 | `mark 2` | `Nice! I've marked this task as done:` and `[D][X] return book (by: Oct 15 2026)` |
| 13 | `find book` | `1.[T][ ] borrow book`<br>`2.[D][X] return book (by: Oct 15 2026)`<br>`3.[T][ ] notebook` |
| 14 | `find Book` | `1.[T][ ] Book launch` only |
| 15 | `find return book` | `1.[D][X] return book (by: Oct 15 2026)` only |
| 16 | `find 読書` | `1.[T][ ] café 読書` only |
| 17 | `find Mon` | Matching heading with no rows |
| 18 | `find Oct` | Matching heading with no rows |
| 19 | `find missing` | Matching heading with no rows |
| 20 | `find` | `OOPS!!! The keyword for find cannot be empty.` |
| 21 | `unmark 2` | `OK, I've marked this task as not done yet:` and `[D][ ] return book (by: Oct 15 2026)` |
| 22 | `mark 6` | `Nice! I've marked this task as done:` and `[T][X] café 読書` |
| 23 | `mark abc` | `OOPS!!! Please provide a valid task number.` |
| 24 | `mark 99` | `OOPS!!! That task number is not in the list.` |
| 25 | `delete abc` | `OOPS!!! Please provide a valid task number.` |
| 26 | `delete 99` | `OOPS!!! That task number is not in the list.` |
| 27 | `delete 4` | `[T][ ] notebook`; `Now you have 6 tasks in the list.` |
| 28 | `list` | The six saved rows shown below |
| 29 | `find book` | `1.[T][ ] borrow book`<br>`2.[D][ ] return book (by: Oct 15 2026)` |
| 30 | `todo` | `OOPS!!! The description of a todo cannot be empty.` |
| 31 | `deadline missing date` | `OOPS!!! Use: deadline DESCRIPTION /by yyyy-MM-dd` |
| 32 | `event missing end /from Monday` | `OOPS!!! Use: event DESCRIPTION /from START /to END` |
| 33 | `unknown` | `OOPS!!! I'm sorry, but I don't know what that means :-(` |
| 34 | `bye` | `Bye. Hope to see you again soon!` |

Initial list after command 11:

```text
1.[T][ ] borrow book
2.[D][ ] return book (by: Oct 15 2026)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
4.[T][ ] notebook
5.[T][ ] Book launch
6.[T][ ] café 読書
7.[D][ ] leap day (by: Feb 29 2024)
```

Saved list after command 28:

```text
1.[T][ ] borrow book
2.[D][ ] return book (by: Oct 15 2026)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
4.[T][ ] Book launch
5.[T][X] café 読書
6.[D][ ] leap day (by: Feb 29 2024)
```

### Restart the same JAR

Aim: verify task order, dates, event details, completion state and Unicode
descriptions survive restarting in the same launch folder.

| # | Command | Expected output fragment |
| --- | --- | --- |
| 1 | `list` | The six saved rows above |
| 2 | `find book` | `1.[T][ ] borrow book`<br>`2.[D][ ] return book (by: Oct 15 2026)` |
| 3 | `find Book` | `1.[T][ ] Book launch` only |
| 4 | `find 読書` | `1.[T][X] café 読書` only |
| 5 | `bye` | `Bye. Hope to see you again soon!` |

## Commands, dates, and task operations

Aim: verify ISO dates display as `MMM dd yyyy`, a valid leap day is accepted,
invalid dates do not add tasks, and existing task operations still work.
Start in a new temporary working directory without `data/ted.txt`.
Send these commands in order. Each list must contain exactly the entries
shown, followed by the separator.

| # | Command | Expected output fragment |
| --- | --- | --- |
| 1 | `list` | `Here are the tasks in your list:` |
| 2 | `todo borrow book` | `[T][ ] borrow book` |
| 3 | `deadline return book /by 2026-10-15` | `[D][ ] return book (by: Oct 15 2026)` |
| 4 | `event project meeting /from Mon 2pm /to 4pm` | `[E][ ] project meeting (from: Mon 2pm to: 4pm)` |
| 5 | `mark 2` | `[D][X] return book (by: Oct 15 2026)` |
| 6 | `unmark 2` | `[D][ ] return book (by: Oct 15 2026)` |
| 7 | `mark 2` | `[D][X] return book (by: Oct 15 2026)` |
| 8 | `list` | `Here are the tasks in your list:`<br>`1.[T][ ] borrow book`<br>`2.[D][X] return book (by: Oct 15 2026)`<br>`3.[E][ ] project meeting (from: Mon 2pm to: 4pm)` |
| 9 | `delete 3` | `Now you have 2 tasks in the list.` |
| 10 | `list` | `Here are the tasks in your list:`<br>`1.[T][ ] borrow book`<br>`2.[D][X] return book (by: Oct 15 2026)` |
| 11 | `deadline do homework /by 2024-02-29` | `[D][ ] do homework (by: Feb 29 2024)` |
| 12 | `deadline missing date` | `OOPS!!! Use: deadline DESCRIPTION /by yyyy-MM-dd` |
| 13 | `deadline missing date /by ` | `OOPS!!! Use: deadline DESCRIPTION /by yyyy-MM-dd` |
| 14 | `deadline invalid date /by 2026-02-30` | `OOPS!!! Please provide a valid deadline date in yyyy-MM-dd format.` |
| 15 | `deadline invalid date /by 2025-02-29` | `OOPS!!! Please provide a valid deadline date in yyyy-MM-dd format.` |
| 16 | `deadline invalid date /by 2026-13-01` | `OOPS!!! Please provide a valid deadline date in yyyy-MM-dd format.` |
| 17 | `deadline invalid date /by Sunday` | `OOPS!!! Please provide a valid deadline date in yyyy-MM-dd format.` |
| 18 | `deadline invalid date /by 15/10/2026` | `OOPS!!! Please provide a valid deadline date in yyyy-MM-dd format.` |
| 19 | `deadline invalid date /by 2026-2-3` | `OOPS!!! Please provide a valid deadline date in yyyy-MM-dd format.` |
| 20 | `event missing end /from Monday` | `OOPS!!! Use: event DESCRIPTION /from START /to END` |
| 21 | `todo` | `OOPS!!! The description of a todo cannot be empty.` |
| 22 | `blah` | `OOPS!!! I'm sorry, but I don't know what that means :-(` |
| 23 | `mark abc` | `OOPS!!! Please provide a valid task number.` |
| 24 | `mark 9` | `OOPS!!! That task number is not in the list.` |
| 25 | `delete abc` | `OOPS!!! Please provide a valid task number.` |
| 26 | `delete 9` | `OOPS!!! That task number is not in the list.` |
| 27 | `mark 0` | `OOPS!!! That task number is not in the list.` |
| 28 | `delete 0` | `OOPS!!! That task number is not in the list.` |
| 29 | `unmark abc` | `OOPS!!! Please provide a valid task number.` |
| 30 | `unmark 9` | `OOPS!!! That task number is not in the list.` |
| 31 | `event project meeting /from Mon 2pm /to 4pm` | `[E][ ] project meeting (from: Mon 2pm to: 4pm)` |
| 32 | `list` | `Here are the tasks in your list:`<br>`1.[T][ ] borrow book`<br>`2.[D][X] return book (by: Oct 15 2026)`<br>`3.[D][ ] do homework (by: Feb 29 2024)`<br>`4.[E][ ] project meeting (from: Mon 2pm to: 4pm)` |
| 33 | `bye` | `Bye. Hope to see you again soon!` |

## Restart with saved tasks

Aim: verify all task types, dates, order, and completion state survive restart.
Restart Ted in the same temporary working directory.

| # | Command | Expected output fragment |
| --- | --- | --- |
| 1 | `list` | `Here are the tasks in your list:`<br>`1.[T][ ] borrow book`<br>`2.[D][X] return book (by: Oct 15 2026)`<br>`3.[D][ ] do homework (by: Feb 29 2024)`<br>`4.[E][ ] project meeting (from: Mon 2pm to: 4pm)` |
| 2 | `bye` | `Bye. Hope to see you again soon!` |

## Invalid saved dates

Aim: verify impossible dates and older free-text deadlines are reported and
skipped while subsequent valid records are loaded.

Append a deadline dated `2026-02-30` on line 5, a deadline dated `Sunday` on
line 6, and an incomplete todo named `saved after invalid records` on line 7.
Use the existing URL-safe Base64 encoding for string fields.
Restart Ted in the same working directory and send these commands in order.

| # | Command | Expected output fragment |
| --- | --- | --- |
| 1 | `list` | `OOPS!!! Skipping invalid saved task on line 5.`<br>`OOPS!!! Skipping invalid saved task on line 6.` |
| 2 | `list` | `Here are the tasks in your list:`<br>`1.[T][ ] borrow book`<br>`2.[D][X] return book (by: Oct 15 2026)`<br>`3.[D][ ] do homework (by: Feb 29 2024)`<br>`4.[E][ ] project meeting (from: Mon 2pm to: 4pm)`<br>`5.[T][ ] saved after invalid records` |
| 3 | `bye` | `Bye. Hope to see you again soon!` |

## Find tasks

Aim: verify searches work on empty and populated lists, include all task types
and completion states, preserve task order, and only match descriptions.
Check literal substring matching, case sensitivity, multiple words, whitespace,
missing keywords, no matches, and search results after deletion.
Use a separate new temporary working directory for this session.
Each search or list response must contain exactly the entries shown,
followed by the separator.

| # | Command | Expected output fragment |
| --- | --- | --- |
| 1 | `find book` | `Here are the matching tasks in your list:` |
| 2 | `todo wash dishes` | `[T][ ] wash dishes` |
| 3 | `todo read book` | `[T][ ] read book` |
| 4 | `deadline return book /by 2026-10-15` | `[D][ ] return book (by: Oct 15 2026)` |
| 5 | `event book club /from Monday /to Tuesday` | `[E][ ] book club (from: Monday to: Tuesday)` |
| 6 | `todo notebook` | `[T][ ] notebook` |
| 7 | `todo Book launch` | `[T][ ] Book launch` |
| 8 | `todo version 1.0` | `[T][ ] version 1.0` |
| 9 | `mark 2` | `[T][X] read book` |
| 10 | `find book` | `Here are the matching tasks in your list:`<br>`1.[T][X] read book`<br>`2.[D][ ] return book (by: Oct 15 2026)`<br>`3.[E][ ] book club (from: Monday to: Tuesday)`<br>`4.[T][ ] notebook` |
| 11 | `find Book` | `Here are the matching tasks in your list:`<br>`1.[T][ ] Book launch` |
| 12 | `find book club` | `Here are the matching tasks in your list:`<br>`1.[E][ ] book club (from: Monday to: Tuesday)` |
| 13 | `find  book  ` | `Here are the matching tasks in your list:`<br>`1.[T][X] read book`<br>`2.[D][ ] return book (by: Oct 15 2026)`<br>`3.[E][ ] book club (from: Monday to: Tuesday)`<br>`4.[T][ ] notebook` |
| 14 | `find Monday` | `Here are the matching tasks in your list:` |
| 15 | `find Oct` | `Here are the matching tasks in your list:` |
| 16 | `find [T]` | `Here are the matching tasks in your list:` |
| 17 | `find .` | `Here are the matching tasks in your list:`<br>`1.[T][ ] version 1.0` |
| 18 | `find missing` | `Here are the matching tasks in your list:` |
| 19 | `find` | `OOPS!!! The keyword for find cannot be empty.` |
| 20 | `find   ` | `OOPS!!! The keyword for find cannot be empty.` |
| 21 | `findbook` | `OOPS!!! I'm sorry, but I don't know what that means :-(` |
| 22 | `list` | `Here are the tasks in your list:`<br>`1.[T][ ] wash dishes`<br>`2.[T][X] read book`<br>`3.[D][ ] return book (by: Oct 15 2026)`<br>`4.[E][ ] book club (from: Monday to: Tuesday)`<br>`5.[T][ ] notebook`<br>`6.[T][ ] Book launch`<br>`7.[T][ ] version 1.0` |
| 23 | `delete 3` | `Now you have 6 tasks in the list.` |
| 24 | `find book` | `Here are the matching tasks in your list:`<br>`1.[T][X] read book`<br>`2.[E][ ] book club (from: Monday to: Tuesday)`<br>`3.[T][ ] notebook` |
| 25 | `list` | `Here are the tasks in your list:`<br>`1.[T][ ] wash dishes`<br>`2.[T][X] read book`<br>`3.[E][ ] book club (from: Monday to: Tuesday)`<br>`4.[T][ ] notebook`<br>`5.[T][ ] Book launch`<br>`6.[T][ ] version 1.0` |
| 26 | `bye` | `Bye. Hope to see you again soon!` |

## Find tasks after restart

Aim: verify searching restored tasks preserves descriptions, dates, completion
states, and order. Restart Ted in the search test's working directory.

| # | Command | Expected output fragment |
| --- | --- | --- |
| 1 | `find book` | `Here are the matching tasks in your list:`<br>`1.[T][X] read book`<br>`2.[E][ ] book club (from: Monday to: Tuesday)`<br>`3.[T][ ] notebook` |
| 2 | `find Book` | `Here are the matching tasks in your list:`<br>`1.[T][ ] Book launch` |
| 3 | `list` | `Here are the tasks in your list:`<br>`1.[T][ ] wash dishes`<br>`2.[T][X] read book`<br>`3.[E][ ] book club (from: Monday to: Tuesday)`<br>`4.[T][ ] notebook`<br>`5.[T][ ] Book launch`<br>`6.[T][ ] version 1.0` |
| 4 | `bye` | `Bye. Hope to see you again soon!` |
