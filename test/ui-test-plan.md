# Console UI Test Plan

This plan covers the user-visible task creation, modification, deletion,
search, display, and persistence behavior of BigBrother. Inputs are sent one
command per line. Expected output entries below are the important task-response
lines from the console transcript; startup and separator lines are also
captured and shown when the tests are run.

Unless a test states otherwise, run it from a fresh temporary working directory
that does not contain a `data` folder. This keeps saved tasks from one test from
affecting another test.

## Test 1: Reject an unknown command

### Aim

Verify that an unrecognized command reports a helpful error and is not stored.

### Input

```text
blah
list
bye
```

### Expected output

```text
     ERROR!!! Invalid command. Try todo, deadline, event, list, find, mark, unmark, delete, or bye.
     Displaying list of tasks:
```

## Test 2: Add a todo task

### Aim

Verify that a `todo` command creates a todo task and displays the `[T]`
marker.

### Input

```text
todo borrow book
list
bye
```

### Expected output

```text
     Understood, Creating Task:
       [T][ ] borrow book
     Now you have 1 tasks in the list.
     Displaying list of tasks:
     1.[T][ ] borrow book
```

## Test 3: Add a deadline task

### Aim

Verify that a deadline stores and displays its description and due date.

### Input

```text
deadline do homework /by Friday
list
bye
```

### Expected output

```text
     Understood, Creating Task with Deadline:
       [D][ ] do homework (by: Friday)
     Now you have 1 tasks in the list.
     Displaying list of tasks:
     1.[D][ ] do homework (by: Friday)
```

## Test 4: Reject an invalid deadline

### Aim

Verify that a deadline without `/by` reports the recommended format and does
not add a task.

### Input

```text
deadline do homework
list
bye
```

### Expected output

```text
     ERROR!!! A deadline must use: deadline <description> /by <date or time>.
     Displaying list of tasks:
```

## Test 5: Add an event task

### Aim

Verify that an event stores and displays its description, start time, and end
time.

### Input

```text
event project meeting /from Mon 2pm /to 4pm
list
bye
```

### Expected output

```text
     Understood, Created Event task:
       [E][ ] project meeting (from: Mon 2pm to: 4pm)
     Now you have 1 tasks in the list.
     Displaying list of tasks:
     1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
```

## Test 6: Reject an invalid event

### Aim

Verify that an event without `/from` and `/to` reports the recommended format
and does not add a task.

### Input

```text
event yes /2 /4
list
bye
```

### Expected output

```text
     ERROR!!! An event must use: event <description> /from <start> /to <end>.
     Displaying list of tasks:
```

## Test 7: Reject empty task descriptions

### Aim

Verify that todo, deadline, and event commands require descriptions and do not
add tasks when their descriptions are empty.

### Input

```text
todo
deadline /by Friday
event /from 2pm /to 4pm
list
bye
```

### Expected output

```text
     ERROR!!!      ERROR - Empty Todo task.
     ERROR!!! ERROR - Empty Deadline Task.
     ERROR!!! The description of an event cannot be empty.
     Displaying list of tasks:
```

## Test 8: Reject missing deadline and event times

### Aim

Verify that deadline and event commands explain which required time is missing.

### Input

```text
deadline submit report /by
event meeting /from
event meeting /from 2pm /to
list
bye
```

### Expected output

```text
     ERROR!!! A deadline must include a date or time after /by.
     ERROR!!! An event must include a start time after /from.
     ERROR!!! An event must include an end time after /to.
     Displaying list of tasks:
```

## Test 9: Reject invalid task numbers

### Aim

Verify that mark and unmark commands handle missing, non-numeric, and
out-of-range task numbers without stopping the chatbot.

### Input

```text
todo borrow book
mark
mark one
mark 0
unmark 2
list
bye
```

### Expected output

```text
     Understood, Creating Task:
       [T][ ] borrow book
     Now you have 1 tasks in the list.
     ERROR!!! Please provide a task number after mark.
     ERROR!!! The task number must be a whole number.
     ERROR!!! Task 0 does not exist. Choose a number from the list.
     ERROR!!! Task 2 does not exist. Choose a number from the list.
     Displaying list of tasks:
     1.[T][ ] borrow book
```

## Test 10: Mark and unmark a typed task

### Aim

Verify that marking and unmarking a typed task preserves its type-specific
display.

### Input

```text
event project meeting /from Mon 2pm /to 4pm
mark 1
unmark 1
bye
```

### Expected output

```text
     Understood, Created Event task:
       [E][ ] project meeting (from: Mon 2pm to: 4pm)
     Now you have 1 tasks in the list.
     Nice! I've marked this task as done:
       [E][X] project meeting (from: Mon 2pm to: 4pm)
     I've marked this task as not done:
       [E][ ] project meeting (from: Mon 2pm to: 4pm)
```

## Test 11: Delete a task

### Aim

Verify that deleting a task displays the removed task, updates the task count,
and renumbers the remaining tasks.

### Input

```text
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
todo join sports club
todo borrow book
delete 3
list
bye
```

### Expected output

```text
     Understood, Creating Task:
       [T][ ] read book
     Now you have 1 tasks in the list.
     Understood, Creating Task with Deadline:
       [D][ ] return book (by: June 6th)
     Now you have 2 tasks in the list.
     Understood, Created Event task:
       [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
     Now you have 3 tasks in the list.
     Understood, Creating Task:
       [T][ ] join sports club
     Now you have 4 tasks in the list.
     Understood, Creating Task:
       [T][ ] borrow book
     Now you have 5 tasks in the list.
     Understood. I've removed this task:
       [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
     Now you have 4 tasks in the list.
     Displaying list of tasks:
     1.[T][ ] read book
     2.[D][ ] return book (by: June 6th)
     3.[T][ ] join sports club
     4.[T][ ] borrow book
```

## Test 12: Reject invalid delete task numbers

### Aim

Verify that delete commands handle missing, non-numeric, and out-of-range task
numbers without removing a valid task or stopping the chatbot.

### Input

```text
todo borrow book
delete
delete one
delete 0
delete 2
list
bye
```

### Expected output

```text
     Understood, Creating Task:
       [T][ ] borrow book
     Now you have 1 tasks in the list.
     ERROR!!! Please provide a task number after delete.
     ERROR!!! The task number must be a whole number.
     ERROR!!! Task 0 does not exist. Choose a number from the list.
     ERROR!!! Task 2 does not exist. Choose a number from the list.
     Displaying list of tasks:
     1.[T][ ] borrow book
```

## Test 13: Save, load, and delete tasks across restarts

### Aim

Verify that adding, marking, and deleting tasks saves all changes, and that new
BigBrother processes load them from `data/bigbrother.txt`. Run all sessions from
the same fresh temporary working directory.

### First session input

```text
todo borrow book
deadline return book /by Friday
event project meeting /from Mon 2pm /to 4pm
mark 2
bye
```

### First session expected output

```text
     Understood, Creating Task:
       [T][ ] borrow book
     Now you have 1 tasks in the list.
     Understood, Creating Task with Deadline:
       [D][ ] return book (by: Friday)
     Now you have 2 tasks in the list.
     Understood, Created Event task:
       [E][ ] project meeting (from: Mon 2pm to: 4pm)
     Now you have 3 tasks in the list.
     Nice! I've marked this task as done:
       [D][X] return book (by: Friday)
```

### Expected saved file

```text
T | 0 | borrow book
D | 1 | return book | Friday
E | 0 | project meeting | Mon 2pm | 4pm
```

### Second session input

```text
list
delete 1
bye
```

### Second session expected output

```text
     Displaying list of tasks:
     1.[T][ ] borrow book
     2.[D][X] return book (by: Friday)
     3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
     Understood. I've removed this task:
       [T][ ] borrow book
     Now you have 2 tasks in the list.
```

### Third session input

```text
list
bye
```

### Third session expected output

```text
     Displaying list of tasks:
     1.[D][X] return book (by: Friday)
     2.[E][ ] project meeting (from: Mon 2pm to: 4pm)
```

## Test 14: Handle a corrupted data file

### Aim

Verify that BigBrother reports a clear error instead of crashing when the saved
file contains an invalid task. Before starting the program, create
`data/bigbrother.txt` with the following content:

```text
T | invalid-status | damaged task
```

### Input

```text
list
bye
```

### Expected output

```text
     ERROR!!! The data file is corrupted at line 1. Starting with no tasks.
     Displaying list of tasks:
```

## Test 15: Find tasks by description

### Aim

Verify that `find` searches task descriptions without regard to letter case,
numbers the matching results consecutively, and does not match deadline dates.

### Input

```text
todo read book
deadline submit report /by book launch
event Book club /from Monday /to Tuesday
todo return book
find book
bye
```

### Expected output

```text
     Understood, Creating Task:
       [T][ ] read book
     Now you have 1 tasks in the list.
     Understood, Creating Task with Deadline:
       [D][ ] submit report (by: book launch)
     Now you have 2 tasks in the list.
     Understood, Created Event task:
       [E][ ] Book club (from: Monday to: Tuesday)
     Now you have 3 tasks in the list.
     Understood, Creating Task:
       [T][ ] return book
     Now you have 4 tasks in the list.
     Displaying matched tasks in your list:
     1.[T][ ] read book
     2.[E][ ] Book club (from: Monday to: Tuesday)
     3.[T][ ] return book
```

## Test 16: Handle empty and unmatched searches

### Aim

Verify that `find` rejects a missing keyword and displays an empty matching
list when no task description contains the keyword.

### Input

```text
find
find missing
bye
```

### Expected output

```text
     ERROR!!! Please provide a keyword after find.
     Displaying matched tasks in your list:
```

## Test 17: Parse, display, and reload calendar deadlines

### Aim

Verify that ISO dates and optional 24-hour times are displayed in a different
format, saved without losing their values, and reconstructed after a restart.
Run both sessions from the same fresh temporary working directory.

### First session input

```text
deadline submit report /by 2026-10-15
deadline join call /by 2026-10-15 1800
list
bye
```

### First session expected output

```text
     Understood, Creating Task with Deadline:
       [D][ ] submit report (by: Oct 15 2026)
     Now you have 1 tasks in the list.
     Understood, Creating Task with Deadline:
       [D][ ] join call (by: Oct 15 2026 6:00 PM)
     Now you have 2 tasks in the list.
     Displaying list of tasks:
     1.[D][ ] submit report (by: Oct 15 2026)
     2.[D][ ] join call (by: Oct 15 2026 6:00 PM)
```

### Expected saved file

```text
D | 0 | submit report | 2026-10-15
D | 0 | join call | 2026-10-15 1800
```

### Second session input

```text
list
bye
```

### Second session expected output

```text
     Displaying list of tasks:
     1.[D][ ] submit report (by: Oct 15 2026)
     2.[D][ ] join call (by: Oct 15 2026 6:00 PM)
```

## Test 18: Reject invalid calendar dates and times

### Aim

Verify that invalid months, days, hours, and minutes produce errors that explain
the allowed ranges without adding tasks.

### Input

```text
deadline bad month /by 2026-13-10
deadline bad day /by 2026-02-30
deadline bad hour /by 2026-10-15 2460
deadline bad minute /by 2026-10-15 2360
list
bye
```

### Expected output

```text
     ERROR!!! Use a valid yyyy-MM-dd date (month 01-12, day valid for that month) and optional HHmm time (hours 00-23, minutes 00-59).
     ERROR!!! Use a valid yyyy-MM-dd date (month 01-12, day valid for that month) and optional HHmm time (hours 00-23, minutes 00-59).
     ERROR!!! Use a valid yyyy-MM-dd date (month 01-12, day valid for that month) and optional HHmm time (hours 00-23, minutes 00-59).
     ERROR!!! Use a valid yyyy-MM-dd date (month 01-12, day valid for that month) and optional HHmm time (hours 00-23, minutes 00-59).
     Displaying list of tasks:
```

## Test 19: Handle an invalid saved calendar date

### Aim

Verify that an impossible ISO date in the saved file reports corruption
instead of crashing. Before starting, create `data/bigbrother.txt` with:

```text
D | 0 | damaged deadline | 2026-02-30
```

### Input

```text
list
bye
```

### Expected output

```text
     ERROR!!! The data file is corrupted at line 1. Starting with no tasks.
     Displaying list of tasks:
```
