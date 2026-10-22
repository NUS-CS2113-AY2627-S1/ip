# Console UI Test Plan

Program command: `java -cp out isa.ui.Isa`
Data file: `data/isa.txt`

Run the tests from the repository root using the `test-ui` skill. Each test
starts with a new in-memory task list.

## Test case: Exit the program

Aim: Verify that `bye` displays the farewell message and exits.

### Inputs
```text
bye
```

### Expected output
```text
Helloo! I'm Isa
How can I help you?
____________________________________________________________
____________________________________________________________
Bye. Hope you have a nice day!
____________________________________________________________
```

## Test case: Save tasks after changes

Aim: Verify that saving added and marked tasks does not change the console output.

### Inputs
```text
todo read book
deadline return book /by 2019-10-15
event project meeting /from 2026-08-06T14:00 /to 2026-08-06T16:00
mark 1
bye
```

### Expected output
```text
Helloo! I'm Isa
How can I help you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Oct 15 2019)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Aug 06 2026 2pm to: Aug 06 2026 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] read book
____________________________________________________________
____________________________________________________________
Bye. Hope you have a nice day!
____________________________________________________________
```

## Test case: Add and list todos

Aim: Verify that `todo` adds typed tasks and `list` displays them in order.

### Inputs
```text
todo read book
todo join sports club
list
bye
```

### Expected output
```text
Helloo! I'm Isa
How can I help you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] join sports club
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[T][ ] join sports club
____________________________________________________________
____________________________________________________________
Bye. Hope you have a nice day!
____________________________________________________________
```

## Test case: Mark and unmark a todo

Aim: Verify that `mark` and `unmark` reverse a todo's done status.

### Inputs
```text
todo read book
mark 1
list
unmark 1
list
bye
```

### Expected output
```text
Helloo! I'm Isa
How can I help you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] read book
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] read book
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] read book
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
____________________________________________________________
____________________________________________________________
Bye. Hope you have a nice day!
____________________________________________________________
```

## Test case: Add deadlines with calendar dates

Aim: Verify that ISO dates are displayed in a readable format and listed in order.

### Inputs
```text
deadline return book /by 2019-10-15
deadline do homework /by 2020-02-29
list
bye
```

### Expected output
```text
Helloo! I'm Isa
How can I help you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Oct 15 2019)
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] do homework (by: Feb 29 2020)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[D][ ] return book (by: Oct 15 2019)
 2.[D][ ] do homework (by: Feb 29 2020)
____________________________________________________________
____________________________________________________________
Bye. Hope you have a nice day!
____________________________________________________________
```

## Test case: Add an event

Aim: Verify that event times with minutes display in 12-hour format.

### Inputs
```text
event project meeting /from 2026-08-06T14:30 /to 2026-08-06T16:45
list
bye
```

### Expected output
```text
Helloo! I'm Isa
How can I help you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Aug 06 2026 2:30pm to: Aug 06 2026 4:45pm)
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Aug 06 2026 2:30pm to: Aug 06 2026 4:45pm)
____________________________________________________________
____________________________________________________________
Bye. Hope you have a nice day!
____________________________________________________________
```

## Test case: List deadlines and events on a date

Aim: Verify that a multi-day event appears on both dates, a deadline appears
only on its due date, and task numbers still refer to the full list.

### Inputs
```text
todo read book
event conference /from 2026-08-06T23:00 /to 2026-08-07T01:00
deadline submit work /by 2026-08-07
on 2026-08-06
on 2026-08-07
on 2026-08-08
bye
```

### Expected output
```text
Helloo! I'm Isa
How can I help you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] conference (from: Aug 06 2026 11pm to: Aug 07 2026 1am)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] submit work (by: Aug 07 2026)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Tasks on Aug 06 2026:
 2.[E][ ] conference (from: Aug 06 2026 11pm to: Aug 07 2026 1am)
____________________________________________________________
____________________________________________________________
 Tasks on Aug 07 2026:
 2.[E][ ] conference (from: Aug 06 2026 11pm to: Aug 07 2026 1am)
 3.[D][ ] submit work (by: Aug 07 2026)
____________________________________________________________
____________________________________________________________
 Tasks on Aug 08 2026:
 No deadlines or events on this date.
____________________________________________________________
____________________________________________________________
Bye. Hope you have a nice day!
____________________________________________________________
```

## Test case: Reject invalid event times without changing tasks

Aim: Verify that malformed times, reversed ranges, and missing fields report
errors while the existing list remains intact.

### Inputs
```text
todo read book
event meeting /from Mon 2pm /to 4pm
event meeting /from 2026-08-07T16:00 /to 2026-08-07T14:00
event meeting /from 2026-08-07T14:00 /to
list
bye
```

### Expected output
```text
Helloo! I'm Isa
How can I help you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 enter event times in yyyy-MM-ddTHH:mm format
____________________________________________________________
____________________________________________________________
 event end time must not be before start time
____________________________________________________________
____________________________________________________________
 use event DESCRIPTION /from yyyy-MM-ddTHH:mm /to yyyy-MM-ddTHH:mm
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
____________________________________________________________
____________________________________________________________
Bye. Hope you have a nice day!
____________________________________________________________
```

## Test case: Reject an invalid date lookup

Aim: Verify that a malformed on-date command reports an error and later
commands still work.

### Inputs
```text
on 2026-02-30
list
bye
```

### Expected output
```text
Helloo! I'm Isa
How can I help you?
____________________________________________________________
____________________________________________________________
 enter a valid date in yyyy-MM-dd format
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Bye. Hope you have a nice day!
____________________________________________________________
```

## Test case: Find tasks by description

Aim: Verify that search ignores case, keeps original task numbers, ignores date
fields, and does not change the task list.

### Inputs
```text
todo read book
todo buy pens
deadline return BOOK /by 2019-10-15
find bOoK
find 2019
list
bye
```

### Expected output
```text
Helloo! I'm Isa
How can I help you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] buy pens
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return BOOK (by: Oct 15 2019)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] read book
 3.[D][ ] return BOOK (by: Oct 15 2019)
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 No matching tasks found.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[T][ ] buy pens
 3.[D][ ] return BOOK (by: Oct 15 2019)
____________________________________________________________
____________________________________________________________
Bye. Hope you have a nice day!
____________________________________________________________
```

## Test case: Reject find without a keyword

Aim: Verify that a missing keyword reports an error and leaves the list intact.

### Inputs
```text
todo read book
find
list
bye
```

### Expected output
```text
Helloo! I'm Isa
How can I help you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 please enter a keyword to find!
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
____________________________________________________________
____________________________________________________________
Bye. Hope you have a nice day!
____________________________________________________________
```

## Test case: Find a phrase without renumbering tasks

Aim: Verify that a mixed-case phrase matches a substring of the description
and the result retains its number from the full task list.

### Inputs
```text
todo buy groceries
todo join the CS book club
todo run errands
find BoOk ClUb
list
bye
```

### Expected output
```text
Helloo! I'm Isa
How can I help you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] buy groceries
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] join the CS book club
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] run errands
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 2.[T][ ] join the CS book club
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] buy groceries
 2.[T][ ] join the CS book club
 3.[T][ ] run errands
____________________________________________________________
____________________________________________________________
Bye. Hope you have a nice day!
____________________________________________________________
```

## Test case: Reject empty todos without changing the task list

Aim: Verify that empty todo descriptions are rejected and do not add tasks,
including when the command contains trailing spaces. Valid list operations are
interleaved to check the task list after each invalid input.

### Inputs
```text
todo read book
todo
list
todo    
list
bye
```

### Expected output
```text
Helloo! I'm Isa
How can I help you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 please enter a todo!
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
____________________________________________________________
____________________________________________________________
 please enter a todo!
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
____________________________________________________________
____________________________________________________________
Bye. Hope you have a nice day!
____________________________________________________________
```

## Test case: Reject unknown commands without changing the task list

Aim: Verify that unknown commands and commands that merely begin with a known
command name are rejected. Valid additions and list operations are interleaved
to confirm that rejected commands do not alter existing tasks.

### Inputs
```text
todo first task
blah
list
todolist
list
todo second task
list
bye
```

### Expected output
```text
Helloo! I'm Isa
How can I help you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] first task
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 i don't understand :((
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] first task
____________________________________________________________
____________________________________________________________
 i don't understand :((
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] first task
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] second task
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] first task
 2.[T][ ] second task
____________________________________________________________
____________________________________________________________
Bye. Hope you have a nice day!
____________________________________________________________
```

## Test case: Reject invalid deadline dates without adding tasks

Aim: Verify that impossible or missing dates do not change the task list.

### Inputs
```text
todo read book
deadline return book /by 2019-02-30
list
deadline return book /by
list
bye
```

### Expected output
```text
Helloo! I'm Isa
How can I help you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 enter a valid deadline date in yyyy-MM-dd format
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
____________________________________________________________
____________________________________________________________
 use deadline DESCRIPTION /by yyyy-MM-dd
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
____________________________________________________________
____________________________________________________________
Bye. Hope you have a nice day!
____________________________________________________________
```

## Test case: Load saved tasks

Aim: Verify that valid todo, deadline, and event records are restored with
their saved completion statuses.

### Initial data
```text
T | 1 | read book
D | 0 | return book | 2019-10-15
E | 0 | project meeting | 2026-08-06T14:00 | 2026-08-06T16:00
```

### Inputs
```text
list
bye
```

### Expected output
```text
Helloo! I'm Isa
How can I help you?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] read book
 2.[D][ ] return book (by: Oct 15 2019)
 3.[E][ ] project meeting (from: Aug 06 2026 2pm to: Aug 06 2026 4pm)
____________________________________________________________
____________________________________________________________
Bye. Hope you have a nice day!
____________________________________________________________
```

## Test case: Accept leap-day deadlines and reject impossible leap days

Aim: Verify that a valid leap day is accepted, an impossible leap day is
rejected, and the rejected command does not change the task list.

### Inputs
```text
deadline leap-day report /by 2028-02-29
deadline invalid report /by 2027-02-29
list
bye
```

### Expected output
```text
Helloo! I'm Isa
How can I help you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] leap-day report (by: Feb 29 2028)
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 enter a valid deadline date in yyyy-MM-dd format
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[D][ ] leap-day report (by: Feb 29 2028)
____________________________________________________________
____________________________________________________________
Bye. Hope you have a nice day!
____________________________________________________________
```

## Test case: Skip legacy deadline dates

Aim: Verify that an old text deadline produces a warning while valid tasks load.

### Initial data
```text
T | 0 | read book
D | 0 | return book | Friday
D | 1 | finish homework | 2020-02-29
```

### Inputs
```text
list
bye
```

### Expected output
```text
Helloo! I'm Isa
How can I help you?
____________________________________________________________
 Warning: skipped saved task on line 2: invalid deadline date 'Friday'
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[D][X] finish homework (by: Feb 29 2020)
____________________________________________________________
____________________________________________________________
Bye. Hope you have a nice day!
____________________________________________________________
```

## Test case: Skip legacy and reversed saved events

Aim: Verify that invalid saved event times produce warnings while valid tasks
remain available.

### Initial data
```text
E | 0 | old meeting | Monday 2pm | 4pm
E | 0 | reversed meeting | 2026-08-07T16:00 | 2026-08-07T14:00
E | 1 | valid meeting | 2026-08-06T14:00 | 2026-08-06T16:00
```

### Inputs
```text
on 2026-08-06
bye
```

### Expected output
```text
Helloo! I'm Isa
How can I help you?
____________________________________________________________
 Warning: skipped saved task on line 1: invalid event time; use yyyy-MM-ddTHH:mm
 Warning: skipped saved task on line 2: event end time must not be before start time
____________________________________________________________
 Tasks on Aug 06 2026:
 1.[E][X] valid meeting (from: Aug 06 2026 2pm to: Aug 06 2026 4pm)
____________________________________________________________
____________________________________________________________
Bye. Hope you have a nice day!
____________________________________________________________
```

## Test case: Skip malformed saved tasks

Aim: Verify that malformed records produce warnings while valid records still
load and remain usable.

### Initial data
```text
T | 1 | valid task
T | 2 | wrong status
D | 0 | missing date
Z | 0 | unknown type
broken

```

### Inputs
```text
list
bye
```

### Expected output
```text
Helloo! I'm Isa
How can I help you?
____________________________________________________________
 Warning: skipped saved task on line 2: status must be 0 or 1
 Warning: skipped saved task on line 3: expected 4 fields but found 3
 Warning: skipped saved task on line 4: unknown task type 'Z'
 Warning: skipped saved task on line 5: record does not contain a type and status
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] valid task
____________________________________________________________
____________________________________________________________
Bye. Hope you have a nice day!
____________________________________________________________
```

## Test case: Load escaped task data

Aim: Verify that escaped separators and backslashes are restored correctly.

### Initial data
```text
T | 0 | use \| separator and \\ slash
```

### Inputs
```text
list
bye
```

### Expected output
```text
Helloo! I'm Isa
How can I help you?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] use | separator and \ slash
____________________________________________________________
____________________________________________________________
Bye. Hope you have a nice day!
____________________________________________________________
```

## Test case: Delete and save a task

Aim: Verify that deleting a task updates the displayed task list and its saved
data.

### Initial data
```text
T | 0 | read book
D | 0 | return book | 2026-09-25
```

### Inputs
```text
delete 1
list
bye
```

### Expected output
```text
Helloo! I'm Isa
How can I help you?
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[D][ ] return book (by: Sep 25 2026)
____________________________________________________________
____________________________________________________________
Bye. Hope you have a nice day!
____________________________________________________________
```
