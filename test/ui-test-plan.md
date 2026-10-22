# UI Test Plan

## Test configuration

- Working directory: repository root
- Required JDK: Java 25
- Setup command (PowerShell):

  ```powershell
  $uiTestSources = Get-ChildItem -Path "src/main/java" -Filter "*.java" -Recurse
  New-Item -ItemType Directory -Force -Path "_temp/ui-test-classes" | Out-Null
  javac -d "_temp/ui-test-classes" $uiTestSources.FullName
  ```

- Launch command (PowerShell):

  ```powershell
  java -cp "_temp/ui-test-classes" seedu.potato.Potato
  ```

- Comparison: exact text after normalizing `CRLF` and `LF` line endings; terminal input echo is excluded.

### Expected startup output

```text
____________________________________________________________
 ____   ___    _____     _     _____   ___
|  _ \ / _ \  |_   _|   / \   |_   _| / _ \
| |_) | | | |   | |    / _ \    | |  | | | |
|  __/| |_| |   | |   / ___ \   | |  | |_| |
|_|    \___/    |_|  /_/   \_\  |_|   \___/
Hello! I'm Potato.
What can I do for you?
____________________________________________________________
```

## Test cases

## TC-1: Baseline command workflow

- Aim: Verify all currently supported commands in one stateful workflow: create a to-do, deadline, and event; list tasks; mark, unmark, and delete a task; save each change; and exit the application.
- Setup: Ensure `data/potato.txt` does not exist before launching the application.
- Inputs and expected outputs:

  1. Input

     ```text
     todo read book
     ```

     Expected output

     ```text
     ____________________________________________________________
      Got it. I've added this task:
        [T][ ] read book
      Now you have 1 task in the list.
     ____________________________________________________________
     ```

  2. Input

     ```text
     deadline return book /by 31-10-2026
     ```

     Expected output

     ```text
     ____________________________________________________________
      Got it. I've added this task:
        [D][ ] return book (by: 31 Oct 2026)
      Now you have 2 tasks in the list.
     ____________________________________________________________
     ```

  3. Input

     ```text
     event project meeting /from 01/11/2026 /to 02/11/2026
     ```

     Expected output

     ```text
     ____________________________________________________________
      Got it. I've added this task:
        [E][ ] project meeting (from: 01 Nov 2026 to: 02 Nov 2026)
      Now you have 3 tasks in the list.
     ____________________________________________________________
     ```

  4. Input

     ```text
     list
     ```

     Expected output

     ```text
     ____________________________________________________________
      Here are the tasks in your list:
      1.[T][ ] read book
      2.[D][ ] return book (by: 31 Oct 2026)
      3.[E][ ] project meeting (from: 01 Nov 2026 to: 02 Nov 2026)
     ____________________________________________________________
     ```

  5. Input

     ```text
     mark 2
     ```

     Expected output

     ```text
     ____________________________________________________________
      Nice! I've marked this task as done:
        [D][X] return book (by: 31 Oct 2026)
     ____________________________________________________________
     ```

  6. Input

     ```text
     unmark 2
     ```

     Expected output

     ```text
     ____________________________________________________________
      OK, I've marked this task as not done yet:
        [D][ ] return book (by: 31 Oct 2026)
     ____________________________________________________________
     ```

  7. Input

     ```text
     delete 2
     ```

     Expected output

     ```text
     ____________________________________________________________
      Noted. I've removed this task:
        [D][ ] return book (by: 31 Oct 2026)
      Now you have 2 tasks in the list.
     ____________________________________________________________
     ```

  8. Input

     ```text
     list
     ```

     Expected output

     ```text
     ____________________________________________________________
      Here are the tasks in your list:
      1.[T][ ] read book
      2.[E][ ] project meeting (from: 01 Nov 2026 to: 02 Nov 2026)
     ____________________________________________________________
     ```

  9. Input

     ```text
     bye
     ```

     Expected output

     ```text
     ____________________________________________________________
     Bye. Hope to see you again soon!
     ____________________________________________________________
     ```

- Expected save file after steps 1-3, 5, and 6: `data/potato.txt` exists and contains the complete current task list. After step 6, its exact contents are:

  ```text
  T | 0 | read book
  D | 0 | return book | 31-10-2026
  E | 0 | project meeting | 01-11-2026 | 02-11-2026
  ```

- Expected save file after step 7: `data/potato.txt` no longer contains the deleted deadline. Its exact contents are:

  ```text
  T | 0 | read book
  E | 0 | project meeting | 01-11-2026 | 02-11-2026
  ```

## TC-2: Invalid task input recovery

- Aim: Verify malformed task commands are ignored and the application continues accepting input.
- Setup: Ensure `data/potato.txt` does not exist before launching the application.
- Inputs and expected outputs:

  1. Input

     ```text
     todo
     ```

     Expected output

     ```text
     ____________________________________________________________
     Invalid to-do input. Format: todo [description]
     ____________________________________________________________
     ```

  2. Input

     ```text
     deadline return book
     ```

     Expected output

     ```text
     ____________________________________________________________
     Invalid deadline input. Format: deadline [description] /by [date] [optional time]
     ____________________________________________________________
     ```

  3. Input

     ```text
     event project meeting /from Monday 2pm
     ```

     Expected output

     ```text
     ____________________________________________________________
     Invalid event input. Format: event [description] /from [date] [optional time] /to [date] [optional time]
     ____________________________________________________________
     ```

  4. Input

     ```text
     deadline return book /by 2026-10-31
     ```

     Expected output

     ```text
     ____________________________________________________________
     Invalid deadline input. Format: deadline [description] /by [date] [optional time]
     [date formats: DD-MM-YYYY or DD/MM/YYYY; optional time format: HH:mm]
     ____________________________________________________________
     ```

  5. Input

     ```text
     event project meeting /from 31-10-2026 /to 31/02/2026
     ```

     Expected output

     ```text
     ____________________________________________________________
     Invalid event input. Format: event [description] /from [date] [optional time] /to [date] [optional time]
     [date formats: DD-MM-YYYY or DD/MM/YYYY; optional time format: HH:mm]
     ____________________________________________________________
     ```

  6. Input

     ```text
     todo read book
     ```

     Expected output

     ```text
     ____________________________________________________________
      Got it. I've added this task:
        [T][ ] read book
      Now you have 1 task in the list.
     ____________________________________________________________
     ```

  7. Input

     ```text
     bye
     ```

     Expected output

     ```text
     ____________________________________________________________
     Bye. Hope to see you again soon!
     ____________________________________________________________
     ```

## TC-3: Load tasks at startup

- Aim: Verify the application loads all supported task types and their completion states from an existing save file.
- Setup: Create `data/potato.txt` with these exact contents before launching the application:

  ```text
  T | 1 | read book
  D | 0 | return book | 31-10-2026
  E | 1 | project meeting | 01/11/2026 | 02/11/2026
  ```

- Inputs and expected outputs:

  1. Input

     ```text
     list
     ```

     Expected output

     ```text
     ____________________________________________________________
      Here are the tasks in your list:
      1.[T][X] read book
      2.[D][ ] return book (by: 31 Oct 2026)
      3.[E][X] project meeting (from: 01 Nov 2026 to: 02 Nov 2026)
     ____________________________________________________________
     ```

  2. Input

     ```text
     bye
     ```

     Expected output

     ```text
     ____________________________________________________________
     Bye. Hope to see you again soon!
     ____________________________________________________________
     ```

## TC-4: Skip malformed save-file lines

- Aim: Verify malformed save-file lines are reported verbatim and skipped while valid tasks still load.
- Setup: Create `data/potato.txt` with these exact contents before launching the application:

  ```text
  T | 1 | read book
  this line is not task data
  D | 0 | return book | 31-10-2026
  E | maybe | project meeting | 01-11-2026 | 02-11-2026
  ```

- Expected startup output:

  ```text
  Warning: skipped malformed save-file line 2:
  this line is not task data
  Warning: skipped malformed save-file line 4:
  E | maybe | project meeting | 01-11-2026 | 02-11-2026
  ____________________________________________________________
   ____   ___    _____     _     _____   ___
  |  _ \ / _ \  |_   _|   / \   |_   _| / _ \
  | |_) | | | |   | |    / _ \    | |  | | | |
  |  __/| |_| |   | |   / ___ \   | |  | |_| |
  |_|    \___/    |_|  /_/   \_\  |_|   \___/
  Hello! I'm Potato.
  What can I do for you?
  ____________________________________________________________
  ```

- Inputs and expected outputs:

  1. Input

     ```text
     list
     ```

     Expected output

     ```text
     ____________________________________________________________
      Here are the tasks in your list:
      1.[T][X] read book
      2.[D][ ] return book (by: 31 Oct 2026)
     ____________________________________________________________
     ```

  2. Input

     ```text
     todo buy groceries
     ```

     Expected output

     ```text
     ____________________________________________________________
      Got it. I've added this task:
        [T][ ] buy groceries
      Now you have 3 tasks in the list.
     ____________________________________________________________
     ```

  3. Input

     ```text
     bye
     ```

     Expected output

     ```text
     ____________________________________________________________
     Bye. Hope to see you again soon!
     ____________________________________________________________
     ```

- Expected save file after step 2: `data/potato.txt` contains only the valid loaded tasks and the newly added task:

  ```text
  T | 1 | read book
  D | 0 | return book | 31-10-2026
  T | 0 | buy groceries
  ```

## TC-5: Load an entirely malformed save file

- Aim: Verify an entirely malformed save file produces warnings and an empty task list.
- Setup: Create `data/potato.txt` with these exact contents before launching the application:

  ```text
  unknown task type
  T | done | invalid status
  ```

- Expected startup output:

  ```text
  Warning: skipped malformed save-file line 1:
  unknown task type
  Warning: skipped malformed save-file line 2:
  T | done | invalid status
  ____________________________________________________________
   ____   ___    _____     _     _____   ___
  |  _ \ / _ \  |_   _|   / \   |_   _| / _ \
  | |_) | | | |   | |    / _ \    | |  | | | |
  |  __/| |_| |   | |   / ___ \   | |  | |_| |
  |_|    \___/    |_|  /_/   \_\  |_|   \___/
  Hello! I'm Potato.
  What can I do for you?
  ____________________________________________________________
  ```

- Inputs and expected outputs:

  1. Input

     ```text
     list
     ```

     Expected output

     ```text
     ____________________________________________________________
      Here are the tasks in your list:
      [List is currently empty]
     ____________________________________________________________
     ```

  2. Input

     ```text
     bye
     ```

     Expected output

     ```text
     ____________________________________________________________
     Bye. Hope to see you again soon!
     ____________________________________________________________
     ```

## TC-6: Find tasks by description and date

- Aim: Verify `find` creates and prints a temporary list of description matches and date matches without changing the stored task list.
- Setup: Create `data/potato.txt` with these exact contents before launching the application:

  ```text
  T | 0 | read project book
  D | 0 | return book | 31-10-2026
  E | 1 | project meeting | 01-11-2026 | 02-11-2026
  ```

- Inputs and expected outputs:

  1. Input

     ```text
     find project
     ```

     Expected output

     ```text
     ____________________________________________________________
      Here are the matching tasks in your list:
      1.[T][ ] read project book
      2.[E][X] project meeting (from: 01 Nov 2026 to: 02 Nov 2026)
     ____________________________________________________________
     ```

  2. Input

     ```text
     find 31-10-2026
     ```

     Expected output

     ```text
     ____________________________________________________________
      Here are the matching tasks in your list:
      1.[D][ ] return book (by: 31 Oct 2026)
     ____________________________________________________________
     ```

  3. Input

     ```text
     find 02/11/2026
     ```

     Expected output

     ```text
     ____________________________________________________________
      Here are the matching tasks in your list:
      1.[E][X] project meeting (from: 01 Nov 2026 to: 02 Nov 2026)
     ____________________________________________________________
     ```

  4. Input

     ```text
     find holiday
     ```

     Expected output

     ```text
     ____________________________________________________________
      Here are the matching tasks in your list:
      [No matching tasks found]
     ____________________________________________________________
     ```

  5. Input

     ```text
     list
     ```

     Expected output

     ```text
     ____________________________________________________________
      Here are the tasks in your list:
      1.[T][ ] read project book
      2.[D][ ] return book (by: 31 Oct 2026)
      3.[E][X] project meeting (from: 01 Nov 2026 to: 02 Nov 2026)
     ____________________________________________________________
     ```

  6. Input

     ```text
     bye
     ```

     Expected output

     ```text
     ____________________________________________________________
     Bye. Hope to see you again soon!
     ____________________________________________________________
     ```

- Expected save file after all steps: `data/potato.txt` remains unchanged:

  ```text
  T | 0 | read project book
  D | 0 | return book | 31-10-2026
  E | 1 | project meeting | 01-11-2026 | 02-11-2026
  ```

## TC-7: Optional deadline and event times

- Aim: Verify times may appear to the right of date fields, use 24-hour `HH:mm`, persist across restarts,
  and remain independently optional for event dates.
- Setup: Ensure `data/potato.txt` does not exist before launching the application.
- Inputs and expected outputs:

  1. Input

     ```text
     deadline submit report /by 15/11/2026 23:59
     ```

     Expected output

     ```text
     ____________________________________________________________
      Got it. I've added this task:
        [D][ ] submit report (by: 15 Nov 2026 23:59)
      Now you have 1 task in the list.
     ____________________________________________________________
     ```

  2. Input

     ```text
     event project meeting /from 01-11-2026 09:00 /to 01-11-2026 10:30
     ```

     Expected output

     ```text
     ____________________________________________________________
      Got it. I've added this task:
        [E][ ] project meeting (from: 01 Nov 2026 09:00 to: 01 Nov 2026 10:30)
      Now you have 2 tasks in the list.
     ____________________________________________________________
     ```

  3. Input

     ```text
     event submission window /from 10-12-2026 /to 12-12-2026 18:00
     ```

     Expected output

     ```text
     ____________________________________________________________
      Got it. I've added this task:
        [E][ ] submission window (from: 10 Dec 2026 to: 12 Dec 2026 18:00)
      Now you have 3 tasks in the list.
     ____________________________________________________________
     ```

  4. Input

     ```text
     deadline invalid time /by 15-11-2026 24:00
     ```

     Expected output

     ```text
     ____________________________________________________________
     Invalid deadline input. Format: deadline [description] /by [date] [optional time]
     [date formats: DD-MM-YYYY or DD/MM/YYYY; optional time format: HH:mm]
     ____________________________________________________________
     ```

  5. Input

     ```text
     bye
     ```

     Expected output

     ```text
     ____________________________________________________________
     Bye. Hope to see you again soon!
     ____________________________________________________________
     ```

- Expected save file after steps 1-4:

  ```text
  D | 0 | submit report | 15-11-2026 23:59
  E | 0 | project meeting | 01-11-2026 09:00 | 01-11-2026 10:30
  E | 0 | submission window | 10-12-2026 | 12-12-2026 18:00
  ```

- Restart Potato without changing the save file and enter `list` followed by `bye`. The list output must be:

  ```text
  ____________________________________________________________
   Here are the tasks in your list:
   1.[D][ ] submit report (by: 15 Nov 2026 23:59)
   2.[E][ ] project meeting (from: 01 Nov 2026 09:00 to: 01 Nov 2026 10:30)
   3.[E][ ] submission window (from: 10 Dec 2026 to: 12 Dec 2026 18:00)
  ____________________________________________________________
  ```
