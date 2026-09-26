# UI Test Plan

This file records the command-line UI test cases for the project. Run the cases from top to bottom. A test session stops at the first failure.

## Test cases

### TC-001: Save added and updated tasks

- **Aim:** Verify that invalid tasks are rejected and that adding and marking valid tasks saves the latest state.
- **Precondition:** `data/mimimeow.txt` does not exist.
- **Allowed differences:** Platform-specific line endings and trailing whitespace in the decorative banner or blank output lines may be ignored.
- **Inputs:**
  ```text
  todo
  todo compare A | B \ C
  deadline return book /by 6/6/2026 1800
  event backwards /from 6/8/2026 1600 /to 6/8/2026 1400
  event project meeting /from 6/8/2026 1400 /to 6/8/2026 1600
  mark 1
  bye
  ```
- **Expected console output:**
  ```text
        ( o.o ) | | (^._.^) | | (｡♥‿♥｡) | | (^._.^) | | ( o.o )
   ███╗   ███╗██╗███╗   ███╗██╗███╗   ███╗███████╗ ██████╗ ██╗    ██╗
   ████╗ ████║██║████╗ ████║██║████╗ ████║██╔════╝██╔═══██╗██║    ██║
   ██╔████╔██║██║██╔████╔██║██║██╔████╔██║█████╗  ██║   ██║██║ █╗ ██║
   ██║╚██╔╝██║██║██║╚██╔╝██║██║██║╚██╔╝██║██╔══╝  ██║   ██║██║███╗██║
   ██║ ╚═╝ ██║██║██║ ╚═╝ ██║██║██║ ╚═╝ ██║███████╗╚██████╔╝╚███╔███╔╝
   ╚═╝     ╚═╝╚═╝╚═╝     ╚═╝╚═╝╚═╝     ╚═╝╚══════╝ ╚═════╝  ╚══╝╚══╝
        ( o.o ) | | (^._.^) | | (｡♥‿♥｡) | | (^._.^) | | ( o.o )

      Hello! I'm MimiMeow.
      What can I do for you?
      ────────────────────────────────────────────────────────────

      ────────────────────────────────────────────────────────────
      Miiiision impossible! A task needs a description.
      ────────────────────────────────────────────────────────────
      ────────────────────────────────────────────────────────────
      (^._.^) meows: Got it! Meow'hv added this task:
      [T][ ] compare A | B \ C
      NOW you have 1 task in the list.
      ────────────────────────────────────────────────────────────
      ────────────────────────────────────────────────────────────
      (^._.^) meows: Got it! Meow'hv added this task:
      [D][ ] return book (by: 6 Jun 2026, 6:00 PM)
      NOW you have 2 tasks in the list.
      ────────────────────────────────────────────────────────────
      ────────────────────────────────────────────────────────────
      Miiiision impossible! An event's end time cannot be before its start time.
      ────────────────────────────────────────────────────────────
      ────────────────────────────────────────────────────────────
      (^._.^) meows: Got it! Meow'hv added this task:
      [E][ ] project meeting (from: 6 Aug 2026, 2:00 PM to: 6 Aug 2026, 4:00 PM)
      NOW you have 3 tasks in the list.
      ────────────────────────────────────────────────────────────
      ────────────────────────────────────────────────────────────
      Nice! Meow've marked this task as done:
      [T][X] compare A | B \ C
      ────────────────────────────────────────────────────────────
      ────────────────────────────────────────────────────────────
      Bye. Hope to see you again soon!
      ────────────────────────────────────────────────────────────
  ```

- **Expected `data/mimimeow.txt` contents:**
  ```text
  T | 1 | compare A \| B \\ C
  D | 0 | return book | 6/6/2026 1800
  E | 0 | project meeting | 6/8/2026 1400 | 6/8/2026 1600
  ```

### TC-002: Load saved tasks after restart

- **Aim:** Verify that a new MimiMeow session loads every saved task and restores its completion status.
- **Precondition:** Run TC-001 first and leave its generated `data/mimimeow.txt` unchanged.
- **Allowed differences:** Platform-specific line endings and trailing whitespace in the decorative banner or blank output lines may be ignored.
- **Inputs:**
  ```text
  list
  bye
  ```
- **Expected console output:**
  ```text
        ( o.o ) | | (^._.^) | | (｡♥‿♥｡) | | (^._.^) | | ( o.o )
   ███╗   ███╗██╗███╗   ███╗██╗███╗   ███╗███████╗ ██████╗ ██╗    ██╗
   ████╗ ████║██║████╗ ████║██║████╗ ████║██╔════╝██╔═══██╗██║    ██║
   ██╔████╔██║██║██╔████╔██║██║██╔████╔██║█████╗  ██║   ██║██║ █╗ ██║
   ██║╚██╔╝██║██║██║╚██╔╝██║██║██║╚██╔╝██║██╔══╝  ██║   ██║██║███╗██║
   ██║ ╚═╝ ██║██║██║ ╚═╝ ██║██║██║ ╚═╝ ██║███████╗╚██████╔╝╚███╔███╔╝
   ╚═╝     ╚═╝╚═╝╚═╝     ╚═╝╚═╝╚═╝     ╚═╝╚══════╝ ╚═════╝  ╚══╝╚══╝
        ( o.o ) | | (^._.^) | | (｡♥‿♥｡) | | (^._.^) | | ( o.o )

      Hello! I'm MimiMeow.
      What can I do for you?
      ────────────────────────────────────────────────────────────

      ────────────────────────────────────────────────────────────
      Here are the tasks in your list:
      1. [T][X] compare A | B \ C
      2. [D][ ] return book (by: 6 Jun 2026, 6:00 PM)
      3. [E][ ] project meeting (from: 6 Aug 2026, 2:00 PM to: 6 Aug 2026, 4:00 PM)
      ────────────────────────────────────────────────────────────
      ────────────────────────────────────────────────────────────
      Bye. Hope to see you again soon!
      ────────────────────────────────────────────────────────────
  ```

### TC-003: Delete a task from saved data

- **Aim:** Verify that deleting a task removes it from both the in-memory list and the saved data file.
- **Precondition:** Run TC-001 and TC-002 first, leaving their generated `data/mimimeow.txt` unchanged.
- **Allowed differences:** Platform-specific line endings and trailing whitespace in the decorative banner or blank output lines may be ignored.
- **Inputs:**
  ```text
  delete 2
  bye
  ```
- **Expected console output:**
  ```text
        ( o.o ) | | (^._.^) | | (｡♥‿♥｡) | | (^._.^) | | ( o.o )
   ███╗   ███╗██╗███╗   ███╗██╗███╗   ███╗███████╗ ██████╗ ██╗    ██╗
   ████╗ ████║██║████╗ ████║██║████╗ ████║██╔════╝██╔═══██╗██║    ██║
   ██╔████╔██║██║██╔████╔██║██║██╔████╔██║█████╗  ██║   ██║██║ █╗ ██║
   ██║╚██╔╝██║██║██║╚██╔╝██║██║██║╚██╔╝██║██╔══╝  ██║   ██║██║███╗██║
   ██║ ╚═╝ ██║██║██║ ╚═╝ ██║██║██║ ╚═╝ ██║███████╗╚██████╔╝╚███╔███╔╝
   ╚═╝     ╚═╝╚═╝╚═╝     ╚═╝╚═╝╚═╝     ╚═╝╚══════╝ ╚═════╝  ╚══╝╚══╝
        ( o.o ) | | (^._.^) | | (｡♥‿♥｡) | | (^._.^) | | ( o.o )

      Hello! I'm MimiMeow.
      What can I do for you?
      ────────────────────────────────────────────────────────────

      ────────────────────────────────────────────────────────────
      Noted! Meow've removed this task:
      [D][ ] return book (by: 6 Jun 2026, 6:00 PM)
      Now you have 2 tasks in the list.
      ────────────────────────────────────────────────────────────
      ────────────────────────────────────────────────────────────
      Bye. Hope to see you again soon!
      ────────────────────────────────────────────────────────────
  ```
- **Expected `data/mimimeow.txt` contents:**
  ```text
  T | 1 | compare A \| B \\ C
  E | 0 | project meeting | 6/8/2026 1400 | 6/8/2026 1600
  ```

### TC-004: Reject corrupted saved data safely

- **Aim:** Verify that malformed saved data produces a clear error with its line number and does not partially load tasks.
- **Precondition:** Copy `test/fixtures/corrupted-tasks.txt` to `data/mimimeow.txt`.
- **Allowed differences:** Platform-specific line endings and trailing whitespace in the decorative banner or blank output lines may be ignored.
- **Inputs:**
  ```text
  todo do not overwrite
  list
  bye
  ```
- **Expected console output:**
  ```text
        ( o.o ) | | (^._.^) | | (｡♥‿♥｡) | | (^._.^) | | ( o.o )
   ███╗   ███╗██╗███╗   ███╗██╗███╗   ███╗███████╗ ██████╗ ██╗    ██╗
   ████╗ ████║██║████╗ ████║██║████╗ ████║██╔════╝██╔═══██╗██║    ██║
   ██╔████╔██║██║██╔████╔██║██║██╔████╔██║█████╗  ██║   ██║██║ █╗ ██║
   ██║╚██╔╝██║██║██║╚██╔╝██║██║██║╚██╔╝██║██╔══╝  ██║   ██║██║███╗██║
   ██║ ╚═╝ ██║██║██║ ╚═╝ ██║██║██║ ╚═╝ ██║███████╗╚██████╔╝╚███╔███╔╝
   ╚═╝     ╚═╝╚═╝╚═╝     ╚═╝╚═╝╚═╝     ╚═╝╚══════╝ ╚═════╝  ╚══╝╚══╝
        ( o.o ) | | (^._.^) | | (｡♥‿♥｡) | | (^._.^) | | ( o.o )

      Hello! I'm MimiMeow.
      What can I do for you?
      ────────────────────────────────────────────────────────────

      Miiiision impossible! Mimi could not understand saved task on line 3: status must be 0 or 1.
      ────────────────────────────────────────────────────────────
      ────────────────────────────────────────────────────────────
      Miiiision impossible! Mimi cannot save changes because the existing data could not be loaded. Fix the data file and restart MimiMeow first.
      ────────────────────────────────────────────────────────────
      ────────────────────────────────────────────────────────────
      There are no tasks in your list yet!
      ────────────────────────────────────────────────────────────
      ────────────────────────────────────────────────────────────
      Bye. Hope to see you again soon!
      ────────────────────────────────────────────────────────────
  ```
- **Expected `data/mimimeow.txt` contents:** The file remains identical to `test/fixtures/corrupted-tasks.txt`.

### TC-005: Query tasks by their schedule

- **Aim:** Verify that date-based commands filter incomplete tasks and order upcoming tasks chronologically.
- **Precondition:** Copy `test/fixtures/scheduled-tasks.txt` to `data/mimimeow.txt`.
- **Allowed differences:** Platform-specific line endings and trailing whitespace in the decorative banner or blank output lines may be ignored.
- **Inputs:**
  ```text
  today
  upcoming
  overdue
  bye
  ```

- **Expected console output:**
  ```text
        ( o.o ) | | (^._.^) | | (｡♥‿♥｡) | | (^._.^) | | ( o.o )
   ███╗   ███╗██╗███╗   ███╗██╗███╗   ███╗███████╗ ██████╗ ██╗    ██╗
   ████╗ ████║██║████╗ ████║██║████╗ ████║██╔════╝██╔═══██╗██║    ██║
   ██╔████╔██║██║██╔████╔██║██║██╔████╔██║█████╗  ██║   ██║██║ █╗ ██║
   ██║╚██╔╝██║██║██║╚██╔╝██║██║██║╚██╔╝██║██╔══╝  ██║   ██║██║███╗██║
   ██║ ╚═╝ ██║██║██║ ╚═╝ ██║██║██║ ╚═╝ ██║███████╗╚██████╔╝╚███╔███╔╝
   ╚═╝     ╚═╝╚═╝╚═╝     ╚═╝╚═╝╚═╝     ╚═╝╚══════╝ ╚═════╝  ╚══╝╚══╝
        ( o.o ) | | (^._.^) | | (｡♥‿♥｡) | | (^._.^) | | ( o.o )

      Hello! I'm MimiMeow.
      What can I do for you?
      ────────────────────────────────────────────────────────────

      ────────────────────────────────────────────────────────────
      There are no tasks scheduled for today.
      ────────────────────────────────────────────────────────────
      ────────────────────────────────────────────────────────────
      Here are your upcoming tasks:
      - [D][ ] future deadline (by: 1 Jan 9999, 9:00 AM)
      - [E][ ] future event (from: 2 Jan 9999, 10:00 AM to: 2 Jan 9999, 11:00 AM)
      ────────────────────────────────────────────────────────────
      ────────────────────────────────────────────────────────────
      Here are your overdue deadlines:
      - [D][ ] old deadline (by: 1 Jan 2000, 9:00 AM)
      ────────────────────────────────────────────────────────────
      ────────────────────────────────────────────────────────────
      Bye. Hope to see you again soon!
      ────────────────────────────────────────────────────────────
  ```

### TC-006: Find tasks by description

- **Aim:** Verify that finding tasks is case-insensitive, searches descriptions only, and validates the keyword.
- **Precondition:** Copy `test/fixtures/scheduled-tasks.txt` to `data/mimimeow.txt`.
- **Allowed differences:** Platform-specific line endings and trailing whitespace in the decorative banner or blank output lines may be ignored.
- **Inputs:**
  ```text
  find FUTURE
  find 9999
  find
  bye
  ```
- **Expected console output:**
  ```text
        ( o.o ) | | (^._.^) | | (｡♥‿♥｡) | | (^._.^) | | ( o.o )
   ███╗   ███╗██╗███╗   ███╗██╗███╗   ███╗███████╗ ██████╗ ██╗    ██╗
   ████╗ ████║██║████╗ ████║██║████╗ ████║██╔════╝██╔═══██╗██║    ██║
   ██╔████╔██║██║██╔████╔██║██║██╔████╔██║█████╗  ██║   ██║██║ █╗ ██║
   ██║╚██╔╝██║██║██║╚██╔╝██║██║██║╚██╔╝██║██╔══╝  ██║   ██║██║███╗██║
   ██║ ╚═╝ ██║██║██║ ╚═╝ ██║██║██║ ╚═╝ ██║███████╗╚██████╔╝╚███╔███╔╝
   ╚═╝     ╚═╝╚═╝╚═╝     ╚═╝╚═╝╚═╝     ╚═╝╚══════╝ ╚═════╝  ╚══╝╚══╝
        ( o.o ) | | (^._.^) | | (｡♥‿♥｡) | | (^._.^) | | ( o.o )

      Hello! I'm MimiMeow.
      What can I do for you?
      ────────────────────────────────────────────────────────────

      ────────────────────────────────────────────────────────────
      Here are the matching tasks in your list:
      1. [D][ ] future deadline (by: 1 Jan 9999, 9:00 AM)
      2. [E][ ] future event (from: 2 Jan 9999, 10:00 AM to: 2 Jan 9999, 11:00 AM)
      ────────────────────────────────────────────────────────────
      ────────────────────────────────────────────────────────────
      There are no matching tasks in your list.
      ────────────────────────────────────────────────────────────
      ────────────────────────────────────────────────────────────
      Miiiision impossible! Mimi needs a keyword to find tasks.
      ────────────────────────────────────────────────────────────
      ────────────────────────────────────────────────────────────
      Bye. Hope to see you again soon!
      ────────────────────────────────────────────────────────────
  ```
