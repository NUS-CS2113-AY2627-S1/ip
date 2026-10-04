# UI Test Plan

This file is the source of truth for command-line UI regression tests.
Each test case runs in a fresh process. Output comparisons are exact except
that CRLF and LF line endings are treated as equivalent. Test execution
stops immediately after the first failure.

## Environment

- Java version: 25
- Main class: `luke.Luke`
- Source directory: `src/main/java`
- Runner: `.agents/skills/test-ui/scripts/run_ui_tests.py`
- Per-test timeout: 10 seconds

## Test cases

### UI-001: Exit immediately

**Aim:** Verify that Luke starts and exits cleanly without adding tasks.

**Inputs:**
```text
bye
```

**Expected output:**
```text
____________________________________________________________
Hello! I'm Luke
 _          _        
| |   _   _| | _____ 
| |  | | | | |/ / _ \
| |__| |_| |   <  __/
|_____\__,_|_|\_\___|

What can I do for you?
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### UI-002: Add a Todo

**Aim:** Verify that a Todo is added and displayed with the correct type and status.

**Inputs:**
```text
todo borrow book
bye
```

**Expected output:**
```text
____________________________________________________________
Hello! I'm Luke
 _          _        
| |   _   _| | _____ 
| |  | | | | |/ / _ \
| |__| |_| |   <  __/
|_____\__,_|_|\_\___|

What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] borrow book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### UI-003: Add a Deadline

**Aim:** Verify that a Deadline is added and displayed with its due date.

**Inputs:**
```text
deadline return book /by 2026-10-02
bye
```

**Expected output:**
```text
____________________________________________________________
Hello! I'm Luke
 _          _        
| |   _   _| | _____ 
| |  | | | | |/ / _ \
| |__| |_| |   <  __/
|_____\__,_|_|\_\___|

What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Oct 02 2026)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### UI-004: Add an Event

**Aim:** Verify that an Event is added and displayed with its start and end times.

**Inputs:**
```text
event project meeting /from 2026-10-01 14:00 /to 2026-10-01 15:00
bye
```

**Expected output:**
```text
____________________________________________________________
Hello! I'm Luke
 _          _        
| |   _   _| | _____ 
| |  | | | | |/ / _ \
| |__| |_| |   <  __/
|_____\__,_|_|\_\___|

What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Oct 01 2026 14:00 to: Oct 01 2026 15:00)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### UI-005: List mixed task types

**Aim:** Verify that Todo, Deadline, and Event tasks retain their order and type-specific details when listed.

**Inputs:**
```text
todo borrow book
deadline return book /by 2026-10-02
event project meeting /from 2026-10-01 14:00 /to 2026-10-01 15:00
list
bye
```

**Expected output:**
```text
____________________________________________________________
Hello! I'm Luke
 _          _        
| |   _   _| | _____ 
| |  | | | | |/ / _ \
| |__| |_| |   <  __/
|_____\__,_|_|\_\___|

What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] borrow book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Oct 02 2026)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Oct 01 2026 14:00 to: Oct 01 2026 15:00)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] borrow book
2. [D][ ] return book (by: Oct 02 2026)
3. [E][ ] project meeting (from: Oct 01 2026 14:00 to: Oct 01 2026 15:00)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### UI-006: Mark and unmark a typed task

**Aim:** Verify that a Deadline can be marked done, unmarked, and listed as incomplete again without losing its type details.

**Inputs:**
```text
deadline return book /by 2026-10-02
mark 1
unmark 1
list
bye
```

**Expected output:**
```text
____________________________________________________________
Hello! I'm Luke
 _          _        
| |   _   _| | _____ 
| |  | | | | |/ / _ \
| |__| |_| |   <  __/
|_____\__,_|_|\_\___|

What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Oct 02 2026)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] return book (by: Oct 02 2026)
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [D][ ] return book (by: Oct 02 2026)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [D][ ] return book (by: Oct 02 2026)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### UI-007: Reject malformed structured commands

**Aim:** Verify that malformed Deadline and Event commands show specific errors, preserve the task list, and allow later commands to run.

**Inputs:**
```text
todo existing task
deadline submit report
deadline submit report /by
deadline /by Friday
event meeting
event meeting /from 2pm
event meeting /to 3pm
list
bye
```

**Expected output:**
```text
____________________________________________________________
Hello! I'm Luke
 _          _        
| |   _   _| | _____ 
| |  | | | | |/ / _ \
| |__| |_| |   <  __/
|_____\__,_|_|\_\___|

What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] existing task
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
[WARNING] The deadline must include a /by date or time.
____________________________________________________________
____________________________________________________________
[WARNING] The deadline must include a /by date or time.
____________________________________________________________
____________________________________________________________
[WARNING] The description of a deadline cannot be empty.
____________________________________________________________
____________________________________________________________
[WARNING] The event must include a /from start time.
____________________________________________________________
____________________________________________________________
[WARNING] The event must include a /to end time.
____________________________________________________________
____________________________________________________________
[WARNING] The event must include a /from start time.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] existing task
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### UI-008: Reject invalid mark task numbers

**Aim:** Verify that zero, negative, out-of-range, and non-numeric mark arguments leave the task incomplete and do not stop the application.

**Inputs:**
```text
todo existing task
mark 0
mark -1
mark 999
mark abc
list
bye
```

**Expected output:**
```text
____________________________________________________________
Hello! I'm Luke
 _          _        
| |   _   _| | _____ 
| |  | | | | |/ / _ \
| |__| |_| |   <  __/
|_____\__,_|_|\_\___|

What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] existing task
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
[WARNING] That task number does not exist. Please choose a number between 1 and 1.
____________________________________________________________
____________________________________________________________
[WARNING] That task number does not exist. Please choose a number between 1 and 1.
____________________________________________________________
____________________________________________________________
[WARNING] That task number does not exist. Please choose a number between 1 and 1.
____________________________________________________________
____________________________________________________________
[WARNING] The task number must be a valid number.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] existing task
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### UI-009: Reject invalid unmark task numbers

**Aim:** Verify that invalid unmark arguments leave a completed task unchanged and do not stop the application.

**Inputs:**
```text
todo existing task
mark 1
unmark 0
unmark 999
unmark abc
list
bye
```

**Expected output:**
```text
____________________________________________________________
Hello! I'm Luke
 _          _        
| |   _   _| | _____ 
| |  | | | | |/ / _ \
| |__| |_| |   <  __/
|_____\__,_|_|\_\___|

What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] existing task
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] existing task
____________________________________________________________
____________________________________________________________
[WARNING] That task number does not exist. Please choose a number between 1 and 1.
____________________________________________________________
____________________________________________________________
[WARNING] That task number does not exist. Please choose a number between 1 and 1.
____________________________________________________________
____________________________________________________________
[WARNING] The task number must be a valid number.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [T][X] existing task
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### UI-010: Reject invalid dates and event ranges

**Aim:** Verify that invalid calendar values, formats, and event ranges do not add tasks.

**Inputs:**
```text
deadline invalid date /by 2026-02-30
deadline wrong format /by 30-02-2026
event equal times /from 2026-10-01 14:00 /to 2026-10-01 14:00
event backwards /from 2026-10-01 15:00 /to 2026-10-01 14:00
event invalid time /from 2026-10-01 25:00 /to 2026-10-01 26:00
list
bye
```

**Expected output:**
```text
____________________________________________________________
Hello! I'm Luke
 _          _        
| |   _   _| | _____ 
| |  | | | | |/ / _ \
| |__| |_| |   <  __/
|_____\__,_|_|\_\___|

What can I do for you?
____________________________________________________________
____________________________________________________________
[WARNING] The deadline date must be in yyyy-MM-dd format.
____________________________________________________________
____________________________________________________________
[WARNING] The deadline date must be in yyyy-MM-dd format.
____________________________________________________________
____________________________________________________________
[WARNING] The event end time must be after its start time.
____________________________________________________________
____________________________________________________________
[WARNING] The event end time must be after its start time.
____________________________________________________________
____________________________________________________________
[WARNING] The event date and time must be in yyyy-MM-dd HH:mm format.
____________________________________________________________
____________________________________________________________
There are no tasks in your list.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### UI-011: Find matching tasks

**Aim:** Verify that find performs case-insensitive matching, preserves task order, and does not modify tasks.

**Inputs:**
```text
todo Read Book
deadline return book /by 2026-10-02
event project meeting /from 2026-10-01 14:00 /to 2026-10-01 15:00
find BOOK
find meeting
list
bye
```

**Expected output:**
```text
____________________________________________________________
Hello! I'm Luke
 _          _        
| |   _   _| | _____ 
| |  | | | | |/ / _ \
| |__| |_| |   <  __/
|_____\__,_|_|\_\___|

What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] Read Book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Oct 02 2026)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Oct 01 2026 14:00 to: Oct 01 2026 15:00)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the matching tasks in your list:
1. [T][ ] Read Book
2. [D][ ] return book (by: Oct 02 2026)
____________________________________________________________
____________________________________________________________
Here are the matching tasks in your list:
1. [E][ ] project meeting (from: Oct 01 2026 14:00 to: Oct 01 2026 15:00)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] Read Book
2. [D][ ] return book (by: Oct 02 2026)
3. [E][ ] project meeting (from: Oct 01 2026 14:00 to: Oct 01 2026 15:00)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### UI-012: Handle unsuccessful find commands

**Aim:** Verify that find reports no matches and rejects a missing keyword without changing the task list.

**Inputs:**
```text
todo existing task
find absent
find
list
bye
```

**Expected output:**
```text
____________________________________________________________
Hello! I'm Luke
 _          _        
| |   _   _| | _____ 
| |  | | | | |/ / _ \
| |__| |_| |   <  __/
|_____\__,_|_|\_\___|

What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] existing task
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
There are no matching tasks in your list.
____________________________________________________________
____________________________________________________________
[WARNING] Please specify a search keyword.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] existing task
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
