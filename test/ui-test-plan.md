# Dobby UI Test Plan

## Test configuration

- Application: `dobby.Dobby`.
- Java version: 25.
- Compile command: `javac -d <temporary-build-dir> src\main\java\dobby\Dobby.java src\main\java\dobby\task\*.java src\main\java\dobby\exception\*.java src\main\java\dobby\storage\*.java`.
- Run command: `java -cp <temporary-build-dir> dobby.Dobby`.
- Persistence file: `data\dobby.txt`, created relative to the application's working directory.
- Test isolation: start each independent test case with a clean working directory or without a saved data file.
- Input format: one command per line, in the order shown in each test case.
- Typed task syntax: `todo <description>`, `deadline <description> /by <date/time>`, and `event <description> /from <start> /to <end>`.
- Date query syntax: `on <date>` lists deadlines on that date and events whose recognized date range contains it.
- Unrecognized date/time text is kept as entered, while recognized dates are displayed as `MMM d yyyy` and recognized date-times as `MMM d yyyy, h:mm a`.
- Comparison: exact stdout, ignoring only CRLF versus LF line endings and a final newline.
- Execution order: top to bottom; stop immediately after the first failure.
- Session record: print the complete console input and output for every executed case.

## Test cases

### TC-001 — List tasks when the task pouch is empty

- Aim: Verify that `list` reports an empty task list in a new session.
- Inputs (one command per line):

```text
list
bye
```

- Expected output:

```text
____________________________________________________________
     *        .        *        .        *
      ____          _      _
     |  _ \   ___  | |__  | |__   _   _
     | | | | / _ \ | '_ \ | '_ \ | | | |
     | |_| || (_) || |_) || |_) || |_| |
     |____/  \___/ |_.__/ |_.__/  \__, |
                                  |___/
     .        *        .        *        .
Hello! I'm Dobby, your mildly magical command goblin.
What adventure shall we get into today?
    ____________________________________________________________
     Behold, brave adventurer! Here are your mighty quests:
     Your task pouch is empty - add a quest and let the adventure begin!
    ____________________________________________________________
    ____________________________________________________________
     Bye for now! Dobby is off to polish the quest scrolls. Stay mighty!
    ____________________________________________________________
```

### TC-002 — Add a task and list it

- Aim: Verify that a normal input is added and appears as an unfinished task.
- Inputs (one command per line):

```text
todo finish homework
list
bye
```

- Expected output:

```text
____________________________________________________________
     *        .        *        .        *
      ____          _      _
     |  _ \   ___  | |__  | |__   _   _
     | | | | / _ \ | '_ \ | '_ \ | | | |
     | |_| || (_) || |_) || |_) || |_| |
     |____/  \___/ |_.__/ |_.__/  \__, |
                                  |___/
     .        *        .        *        .
Hello! I'm Dobby, your mildly magical command goblin.
What adventure shall we get into today?
    ____________________________________________________________
     Huzzah! A new quest has joined your magical task scroll:
       [T][ ] finish homework
     The quest scroll now holds 1 quest. Keep adventuring!
    ____________________________________________________________
    ____________________________________________________________
     Behold, brave adventurer! Here are your mighty quests:
     1.[T][ ] finish homework
    ____________________________________________________________
    ____________________________________________________________
     Bye for now! Dobby is off to polish the quest scrolls. Stay mighty!
    ____________________________________________________________
```

### TC-003 — Mark and unmark a task

- Aim: Verify that `mark` changes the task status to done and `unmark` restores the unfinished status.
- Inputs (one command per line):

```text
todo pack lunch
mark 1
list
unmark 1
list
bye
```

- Expected output:

```text
____________________________________________________________
     *        .        *        .        *
      ____          _      _
     |  _ \   ___  | |__  | |__   _   _
     | | | | / _ \ | '_ \ | '_ \ | | | |
     | |_| || (_) || |_) || |_) || |_| |
     |____/  \___/ |_.__/ |_.__/  \__, |
                                  |___/
     .        *        .        *        .
Hello! I'm Dobby, your mildly magical command goblin.
What adventure shall we get into today?
    ____________________________________________________________
     Huzzah! A new quest has joined your magical task scroll:
       [T][ ] pack lunch
     The quest scroll now holds 1 quest. Keep adventuring!
    ____________________________________________________________
    ____________________________________________________________
     Nice! Quest progress unlocked - I've marked this task as done:
       [T][X] pack lunch
    ____________________________________________________________
    ____________________________________________________________
     Behold, brave adventurer! Here are your mighty quests:
     1.[T][X] pack lunch
    ____________________________________________________________
    ____________________________________________________________
     Plot twist! This quest is back on the adventure board -
     I've marked this task as not done yet:
       [T][ ] pack lunch
    ____________________________________________________________
    ____________________________________________________________
     Behold, brave adventurer! Here are your mighty quests:
     1.[T][ ] pack lunch
    ____________________________________________________________
    ____________________________________________________________
     Bye for now! Dobby is off to polish the quest scrolls. Stay mighty!
    ____________________________________________________________
```

### TC-004 — Reject an invalid mark command

- Aim: Verify that a non-numeric task selector produces a helpful error and does not terminate the session.
- Inputs (one command per line):

```text
mark nope
bye
```

- Expected output:

```text
____________________________________________________________
     *        .        *        .        *
      ____          _      _
     |  _ \   ___  | |__  | |__   _   _
     | | | | / _ \ | '_ \ | '_ \ | | | |
     | |_| || (_) || |_) || |_) || |_| |
     |____/  \___/ |_.__/ |_.__/  \__, |
                                  |___/
     .        *        .        *        .
Hello! I'm Dobby, your mildly magical command goblin.
What adventure shall we get into today?
    ____________________________________________________________
     The mark command needs a task number. Use mark <number>, for example mark 1.
    ____________________________________________________________
    ____________________________________________________________
     Bye for now! Dobby is off to polish the quest scrolls. Stay mighty!
    ____________________________________________________________
```

### TC-005 — Add ToDos, Deadlines, and Events

- Aim: Verify that all three explicit task commands are accepted, retain their date/time text, and display distinct type markers.
- Inputs (one command per line):

```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
bye
```

- Expected output:

```text
____________________________________________________________
     *        .        *        .        *
      ____          _      _
     |  _ \   ___  | |__  | |__   _   _
     | | | | / _ \ | '_ \ | '_ \ | | | |
     | |_| || (_) || |_) || |_) || |_| |
     |____/  \___/ |_.__/ |_.__/  \__, |
                                  |___/
     .        *        .        *        .
Hello! I'm Dobby, your mildly magical command goblin.
What adventure shall we get into today?
    ____________________________________________________________
     Huzzah! A new quest has joined your magical task scroll:
       [T][ ] borrow book
     The quest scroll now holds 1 quest. Keep adventuring!
    ____________________________________________________________
    ____________________________________________________________
     Huzzah! A new quest has joined your magical task scroll:
       [D][ ] return book (by: Sunday)
     The quest scroll now holds 2 quests. Keep adventuring!
    ____________________________________________________________
    ____________________________________________________________
     Huzzah! A new quest has joined your magical task scroll:
       [E][ ] project meeting (from: Mon 2pm to: 4pm)
     The quest scroll now holds 3 quests. Keep adventuring!
    ____________________________________________________________
    ____________________________________________________________
     Behold, brave adventurer! Here are your mighty quests:
     1.[T][ ] borrow book
     2.[D][ ] return book (by: Sunday)
     3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
    ____________________________________________________________
    ____________________________________________________________
     Bye for now! Dobby is off to polish the quest scrolls. Stay mighty!
    ____________________________________________________________
```

### TC-006 — Preserve numeric date and time ranges

- Aim: Verify that numeric dates and date ranges remain intact in Deadline and Event displays.
- Inputs (one command per line):

```text
deadline submit report /by 11/10/2019 5pm
event orientation week /from 4/10/2019 /to 11/10/2019
list
bye
```

- Expected output:

```text
____________________________________________________________
     *        .        *        .        *
      ____          _      _
     |  _ \   ___  | |__  | |__   _   _
     | | | | / _ \ | '_ \ | '_ \ | | | |
     | |_| || (_) || |_) || |_) || |_| |
     |____/  \___/ |_.__/ |_.__/  \__, |
                                  |___/
     .        *        .        *        .
Hello! I'm Dobby, your mildly magical command goblin.
What adventure shall we get into today?
    ____________________________________________________________
     Huzzah! A new quest has joined your magical task scroll:
       [D][ ] submit report (by: 11/10/2019 5pm)
     The quest scroll now holds 1 quest. Keep adventuring!
    ____________________________________________________________
    ____________________________________________________________
     Huzzah! A new quest has joined your magical task scroll:
       [E][ ] orientation week (from: 4/10/2019 to: 11/10/2019)
     The quest scroll now holds 2 quests. Keep adventuring!
    ____________________________________________________________
    ____________________________________________________________
     Behold, brave adventurer! Here are your mighty quests:
     1.[D][ ] submit report (by: 11/10/2019 5pm)
     2.[E][ ] orientation week (from: 4/10/2019 to: 11/10/2019)
    ____________________________________________________________
    ____________________________________________________________
     Bye for now! Dobby is off to polish the quest scrolls. Stay mighty!
    ____________________________________________________________
```

### TC-007 — Accept an arbitrary deadline description

- Aim: Verify that deadline text is treated as a string and does not need to be a recognized date or time.
- Inputs (one command per line):

```text
deadline do homework /by no idea :-p
bye
```

- Expected output:

```text
____________________________________________________________
     *        .        *        .        *
      ____          _      _
     |  _ \   ___  | |__  | |__   _   _
     | | | | / _ \ | '_ \ | '_ \ | | | |
     | |_| || (_) || |_) || |_) || |_| |
     |____/  \___/ |_.__/ |_.__/  \__, |
                                  |___/
     .        *        .        *        .
Hello! I'm Dobby, your mildly magical command goblin.
What adventure shall we get into today?
    ____________________________________________________________
     Huzzah! A new quest has joined your magical task scroll:
       [D][ ] do homework (by: no idea :-p)
     The quest scroll now holds 1 quest. Keep adventuring!
    ____________________________________________________________
    ____________________________________________________________
     Bye for now! Dobby is off to polish the quest scrolls. Stay mighty!
    ____________________________________________________________
```

### TC-008 — Reject a ToDo without a description

- Aim: Verify that an empty ToDo produces an error and does not terminate the session.
- Inputs (one command per line):

```text
todo
bye
```

- Expected output:

```text
____________________________________________________________
     *        .        *        .        *
      ____          _      _
     |  _ \   ___  | |__  | |__   _   _
     | | | | / _ \ | '_ \ | '_ \ | | | |
     | |_| || (_) || |_) || |_) || |_| |
     |____/  \___/ |_.__/ |_.__/  \__, |
                                  |___/
     .        *        .        *        .
Hello! I'm Dobby, your mildly magical command goblin.
What adventure shall we get into today?
    ____________________________________________________________
     A ToDo needs a description. Use todo <description>, for example todo read book.
    ____________________________________________________________
    ____________________________________________________________
     Bye for now! Dobby is off to polish the quest scrolls. Stay mighty!
    ____________________________________________________________
```

### TC-009 — Reject an unknown command

- Aim: Verify that an unsupported command produces a helpful error and does not terminate the session.
- Inputs (one command per line):

```text
blah
bye
```

- Expected output:

```text
____________________________________________________________
     *        .        *        .        *
      ____          _      _
     |  _ \   ___  | |__  | |__   _   _
     | | | | / _ \ | '_ \ | '_ \ | | | |
     | |_| || (_) || |_) || |_) || |_| |
     |____/  \___/ |_.__/ |_.__/  \__, |
                                  |___/
     .        *        .        *        .
Hello! I'm Dobby, your mildly magical command goblin.
What adventure shall we get into today?
    ____________________________________________________________
     I don't know that command. Try list, todo <description>, deadline <description> /by <date/time>, event <description> /from <start> /to <end>, mark <number>, unmark <number>, delete <number>, on <date>, find <keyword>, or bye.
    ____________________________________________________________
    ____________________________________________________________
     Bye for now! Dobby is off to polish the quest scrolls. Stay mighty!
    ____________________________________________________________
```

### TC-010 — Explain invalid task selectors

- Aim: Verify that invalid mark and unmark selectors explain the required number format.
- Inputs (one command per line):

```text
todo write report
mark 2
unmark nope
bye
```

- Expected output:

```text
____________________________________________________________
     *        .        *        .        *
      ____          _      _
     |  _ \   ___  | |__  | |__   _   _
     | | | | / _ \ | '_ \ | '_ \ | | | |
     | |_| || (_) || |_) || |_) || |_| |
     |____/  \___/ |_.__/ |_.__/  \__, |
                                  |___/
     .        *        .        *        .
Hello! I'm Dobby, your mildly magical command goblin.
What adventure shall we get into today?
    ____________________________________________________________
     Huzzah! A new quest has joined your magical task scroll:
       [T][ ] write report
     The quest scroll now holds 1 quest. Keep adventuring!
    ____________________________________________________________
    ____________________________________________________________
     Task 2 does not exist. Choose a number from 1 to 1.
    ____________________________________________________________
    ____________________________________________________________
     The unmark command needs a task number. Use unmark <number>, for example unmark 1.
    ____________________________________________________________
    ____________________________________________________________
     Bye for now! Dobby is off to polish the quest scrolls. Stay mighty!
    ____________________________________________________________
```

### TC-011 — Explain malformed deadline and event commands

- Aim: Verify that malformed typed tasks identify the missing separators or time fields.
- Inputs (one command per line):

```text
deadline submit report
event team meeting /from 2pm
event team meeting /from  /to 4pm
bye
```

- Expected output:

```text
____________________________________________________________
     *        .        *        .        *
      ____          _      _
     |  _ \   ___  | |__  | |__   _   _
     | | | | / _ \ | '_ \ | '_ \ | | | |
     | |_| || (_) || |_) || |_) || |_| |
     |____/  \___/ |_.__/ |_.__/  \__, |
                                  |___/
     .        *        .        *        .
Hello! I'm Dobby, your mildly magical command goblin.
What adventure shall we get into today?
    ____________________________________________________________
     A deadline needs a description and a due time. Use deadline <description> /by <date/time>.
    ____________________________________________________________
    ____________________________________________________________
     An event needs an end time after /to. Add /to <end> after the start time.
    ____________________________________________________________
    ____________________________________________________________
     An event needs a start time after /from. Add a start time before /to.
    ____________________________________________________________
    ____________________________________________________________
     Bye for now! Dobby is off to polish the quest scrolls. Stay mighty!
    ____________________________________________________________
```

### TC-012 — Explain selectors when no tasks exist

- Aim: Verify that mark and unmark explain how to create a task when the list is empty.
- Inputs (one command per line):

```text
mark 1
unmark 1
bye
```

- Expected output:

```text
____________________________________________________________
     *        .        *        .        *
      ____          _      _
     |  _ \   ___  | |__  | |__   _   _
     | | | | / _ \ | '_ \ | '_ \ | | | |
     | |_| || (_) || |_) || |_) || |_| |
     |____/  \___/ |_.__/ |_.__/  \__, |
                                  |___/
     .        *        .        *        .
Hello! I'm Dobby, your mildly magical command goblin.
What adventure shall we get into today?
    ____________________________________________________________
     There are no tasks to mark. Add one with todo <description> first.
    ____________________________________________________________
    ____________________________________________________________
     There are no tasks to unmark. Add one with todo <description> first.
    ____________________________________________________________
    ____________________________________________________________
     Bye for now! Dobby is off to polish the quest scrolls. Stay mighty!
    ____________________________________________________________
```

### TC-013 — Delete a task and renumber the remaining list

- Aim: Verify that `delete <number>` removes the selected task, reports the new task count, and keeps the remaining list usable.
- Inputs (one command per line):

```text
todo first task
todo second task
delete 1
list
bye
```

- Expected output:

```text
____________________________________________________________
     *        .        *        .        *
      ____          _      _
     |  _ \   ___  | |__  | |__   _   _
     | | | | / _ \ | '_ \ | '_ \ | | | |
     | |_| || (_) || |_) || |_) || |_| |
     |____/  \___/ |_.__/ |_.__/  \__, |
                                  |___/
     .        *        .        *        .
Hello! I'm Dobby, your mildly magical command goblin.
What adventure shall we get into today?
    ____________________________________________________________
     Huzzah! A new quest has joined your magical task scroll:
       [T][ ] first task
     The quest scroll now holds 1 quest. Keep adventuring!
    ____________________________________________________________
    ____________________________________________________________
     Huzzah! A new quest has joined your magical task scroll:
       [T][ ] second task
     The quest scroll now holds 2 quests. Keep adventuring!
    ____________________________________________________________
    ____________________________________________________________
     Poof! This quest has vanished from the magical task scroll:
       [T][ ] first task
     The quest scroll now holds 1 quest. Onward, brave adventurer!
    ____________________________________________________________
    ____________________________________________________________
     Behold, brave adventurer! Here are your mighty quests:
     1.[T][ ] second task
    ____________________________________________________________
    ____________________________________________________________
     Bye for now! Dobby is off to polish the quest scrolls. Stay mighty!
    ____________________________________________________________
```

### TC-014 — Format an ISO deadline date

- Aim: Verify that an ISO deadline date is understood as a date and displayed in a readable format.
- Inputs (one command per line):

```text
deadline return book /by 2019-10-15
list
bye
```

- Expected output:

```text
____________________________________________________________
     *        .        *        .        *
      ____          _      _
     |  _ \   ___  | |__  | |__   _   _
     | | | | / _ \ | '_ \ | '_ \ | | | |
     | |_| || (_) || |_) || |_) || |_| |
     |____/  \___/ |_.__/ |_.__/  \__, |
                                  |___/
     .        *        .        *        .
Hello! I'm Dobby, your mildly magical command goblin.
What adventure shall we get into today?
    ____________________________________________________________
     Huzzah! A new quest has joined your magical task scroll:
       [D][ ] return book (by: Oct 15 2019)
     The quest scroll now holds 1 quest. Keep adventuring!
    ____________________________________________________________
    ____________________________________________________________
     Behold, brave adventurer! Here are your mighty quests:
     1.[D][ ] return book (by: Oct 15 2019)
    ____________________________________________________________
    ____________________________________________________________
     Bye for now! Dobby is off to polish the quest scrolls. Stay mighty!
    ____________________________________________________________
```

### TC-015 — List deadlines and events on a date

- Aim: Verify that `on <date>` lists a deadline on that date and an event spanning that date.
- Inputs (one command per line):

```text
deadline submit report /by 2019-10-15
event orientation week /from 2019-10-14 /to 2019-10-16
on 2019-10-15
bye
```

- Expected output:

```text
____________________________________________________________
     *        .        *        .        *
      ____          _      _
     |  _ \   ___  | |__  | |__   _   _
     | | | | / _ \ | '_ \ | '_ \ | | | |
     | |_| || (_) || |_) || |_) || |_| |
     |____/  \___/ |_.__/ |_.__/  \__, |
                                  |___/
     .        *        .        *        .
Hello! I'm Dobby, your mildly magical command goblin.
What adventure shall we get into today?
    ____________________________________________________________
     Huzzah! A new quest has joined your magical task scroll:
       [D][ ] submit report (by: Oct 15 2019)
     The quest scroll now holds 1 quest. Keep adventuring!
    ____________________________________________________________
    ____________________________________________________________
     Huzzah! A new quest has joined your magical task scroll:
       [E][ ] orientation week (from: 2019-10-14 to: 2019-10-16)
     The quest scroll now holds 2 quests. Keep adventuring!
    ____________________________________________________________
    ____________________________________________________________
     Quests scheduled for Oct 15 2019:
     1.[D][ ] submit report (by: Oct 15 2019)
     2.[E][ ] orientation week (from: 2019-10-14 to: 2019-10-16)
    ____________________________________________________________
    ____________________________________________________________
     Bye for now! Dobby is off to polish the quest scrolls. Stay mighty!
    ____________________________________________________________
```

### TC-016 — Find tasks by a keyword

- Aim: Verify that `find <keyword>` displays tasks whose descriptions contain the keyword and preserves their original task numbers.
- Inputs (one command per line):

```text
todo read book
deadline return book /by June 6th
find book
bye
```

- Expected output:

```text
____________________________________________________________
     *        .        *        .        *
      ____          _      _
     |  _ \   ___  | |__  | |__   _   _
     | | | | / _ \ | '_ \ | '_ \ | | | |
     | |_| || (_) || |_) || |_) || |_| |
     |____/  \___/ |_.__/ |_.__/  \__, |
                                  |___/
     .        *        .        *        .
Hello! I'm Dobby, your mildly magical command goblin.
What adventure shall we get into today?
    ____________________________________________________________
     Huzzah! A new quest has joined your magical task scroll:
       [T][ ] read book
     The quest scroll now holds 1 quest. Keep adventuring!
    ____________________________________________________________
    ____________________________________________________________
     Huzzah! A new quest has joined your magical task scroll:
       [D][ ] return book (by: June 6th)
     The quest scroll now holds 2 quests. Keep adventuring!
    ____________________________________________________________
    ____________________________________________________________
     Aha! I found these quests hiding in your magical task scroll:
     1.[T][ ] read book
     2.[D][ ] return book (by: June 6th)
    ____________________________________________________________
    ____________________________________________________________
     Bye for now! Dobby is off to polish the quest scrolls. Stay mighty!
    ____________________________________________________________
```
