# UI Test Plan

This file is the source of truth for scripted acceptance tests of the interactive console UI. Add concrete test cases before running `$test-ui`.

## Test setup

- **Required Java version:** Java 25
- **Working directory:** Repository root
- **Build command:** `javac -d src/main/java/out -sourcepath src/main/java src/main/java/alfred/Alfred.java`
- **Launch command:** `java -cp src/main/java/out alfred.Alfred`
- **State setup:** Delete `data/alfred.txt` before each test case unless its preconditions specify seeded file data.
  Without a data file, each application process starts with an empty task list.
- **State cleanup:** Close standard input after the assertions so the application exits normally, then delete
  `data/alfred.txt` if it exists.
- **Comparison:** Compare exact application output after normalizing only `CRLF` and `LF` line endings. Terminal input echo is included in the transcript but excluded from comparison.
- **Data-file comparison:** Where an expected data file is specified, compare `data/alfred.txt` after the command,
  normalizing only `CRLF` and `LF` line endings.
- **Timeouts:** Allow 10 seconds for startup and 5 seconds for each command response.

## Test cases

Each test case runs in a fresh application process unless its preconditions say otherwise.

### TEST-01: Add and list all task types

**Aim:** Verify that todos, deadlines, and events are added with their type-specific details, listed in order, and
saved to the data file after each addition.

**Preconditions:** The application has just started and the in-memory task list is empty.

#### Step 1

**Input**

```text
todo borrow book
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [T][ ] borrow book
Now you have 1 tasks in the list.
____________________________________________________________
```

**Expected data file**

```text
T | 0 | borrow book
```

#### Step 2

**Input**

```text
deadline return book /by 2019-10-15
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [D][ ] return book (by: Oct 15 2019)
Now you have 2 tasks in the list.
____________________________________________________________
```

**Expected data file**

```text
T | 0 | borrow book
D | 0 | return book | 2019-10-15
```

#### Step 3

**Input**

```text
event project meeting /from 2019-10-15 /to 2019-10-16
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [E][ ] project meeting (from: Oct 15 2019 to: Oct 16 2019)
Now you have 3 tasks in the list.
____________________________________________________________
```

**Expected data file**

```text
T | 0 | borrow book
D | 0 | return book | 2019-10-15
E | 0 | project meeting | 2019-10-15 | 2019-10-16
```

#### Step 4

**Input**

```text
list
```

**Expected output**

```text
____________________________________________________________
These are your tasks Master Wayne:
1.[T][ ] borrow book
2.[D][ ] return book (by: Oct 15 2019)
3.[E][ ] project meeting (from: Oct 15 2019 to: Oct 16 2019)
____________________________________________________________
```

After Step 4 passes, close standard input so the application exits normally.

### TEST-02: Reject a deadline without a description

**Aim:** Verify that a deadline without a task description explains the exact problem and shows how to fix it.

**Preconditions:** The application has just started and the in-memory task list is empty.

#### Step 1

**Input**

```text
deadline /by tomorrow
```

**Expected output**

```text
____________________________________________________________
Unable to add the deadline: the task description is missing.
Use either: deadline <description> /by <yyyy-MM-dd>
        or: deadline <description> /by <yyyy-MM-dd HH:mm>
Examples: deadline return book /by 2019-10-15
          deadline submit report /by 2019-10-15 17:30
____________________________________________________________
```

After Step 1 passes, close standard input so the application exits normally.

### TEST-03: Reject an event without a description

**Aim:** Verify that an event without a task description explains the exact problem and shows how to fix it.

**Preconditions:** The application has just started and the in-memory task list is empty.

#### Step 1

**Input**

```text
event /from 2019-10-15 /to 2019-10-16
```

**Expected output**

```text
____________________________________________________________
Unable to add the event: the task description is missing.
Use either: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>
        or: event <description> /from <yyyy-MM-dd HH:mm> /to <yyyy-MM-dd HH:mm>
Examples: event conference /from 2019-10-15 /to 2019-10-16
          event meeting /from 2019-10-15 14:00 /to 2019-10-15 16:00
____________________________________________________________
```

After Step 1 passes, close standard input so the application exits normally.

### TEST-04: Mark and unmark a task

**Aim:** Verify that a task can be marked as done and then restored to not done, with each status saved to the data
file.

**Preconditions:** The application has just started and the in-memory task list is empty.

#### Step 1

**Input**

```text
todo borrow book
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [T][ ] borrow book
Now you have 1 tasks in the list.
____________________________________________________________
```

**Expected data file**

```text
T | 0 | borrow book
```

#### Step 2

**Input**

```text
mark 1
```

**Expected output**

```text
____________________________________________________________
Excellent work Master Wayne! I've marked this task as done:
  [T][X] borrow book
____________________________________________________________
```

**Expected data file**

```text
T | 1 | borrow book
```

#### Step 3

**Input**

```text
unmark 1
```

**Expected output**

```text
____________________________________________________________
Alright Master Wayne, I have unmarked this task as requested:
  [T][ ] borrow book
____________________________________________________________
```

**Expected data file**

```text
T | 0 | borrow book
```

After Step 3 passes, close standard input so the application exits normally.

### TEST-05: Reject invalid additions without changing the task list

**Aim:** Verify that invalid task commands do not add partial tasks or prevent later valid commands from succeeding.

**Preconditions:** The application has just started and the in-memory task list is empty.

#### Step 1

**Input**

```text
todo prepare slides
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [T][ ] prepare slides
Now you have 1 tasks in the list.
____________________________________________________________
```

#### Step 2

**Input**

```text
deadline /by 2026-10-02
```

**Expected output**

```text
____________________________________________________________
Unable to add the deadline: the task description is missing.
Use either: deadline <description> /by <yyyy-MM-dd>
        or: deadline <description> /by <yyyy-MM-dd HH:mm>
Examples: deadline return book /by 2019-10-15
          deadline submit report /by 2019-10-15 17:30
____________________________________________________________
```

#### Step 3

**Input**

```text
deadline submit report /by 2026-10-02
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [D][ ] submit report (by: Oct 02 2026)
Now you have 2 tasks in the list.
____________________________________________________________
```

#### Step 4

**Input**

```text
event team sync /from /to 2026-10-03
```

**Expected output**

```text
____________________________________________________________
Unable to add the event: the start date and optional time are missing.
Use either: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>
        or: event <description> /from <yyyy-MM-dd HH:mm> /to <yyyy-MM-dd HH:mm>
Examples: event conference /from 2019-10-15 /to 2019-10-16
          event meeting /from 2019-10-15 14:00 /to 2019-10-15 16:00
____________________________________________________________
```

#### Step 5

**Input**

```text
event team sync /from 2026-10-02 /to 2026-10-03
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [E][ ] team sync (from: Oct 02 2026 to: Oct 03 2026)
Now you have 3 tasks in the list.
____________________________________________________________
```

#### Step 6

**Input**

```text
todo
```

**Expected output**

```text
____________________________________________________________
The description of a todo cannot be empty.
____________________________________________________________
```

#### Step 7

**Input**

```text
list
```

**Expected output**

```text
____________________________________________________________
These are your tasks Master Wayne:
1.[T][ ] prepare slides
2.[D][ ] submit report (by: Oct 02 2026)
3.[E][ ] team sync (from: Oct 02 2026 to: Oct 03 2026)
____________________________________________________________
```

After Step 7 passes, close standard input so the application exits normally.

### TEST-06: Reject invalid status commands without changing task states

**Aim:** Verify that malformed, out-of-range, and command-prefix inputs do not mark or unmark unintended tasks.

**Preconditions:** The application has just started and the in-memory task list is empty.

#### Step 1

**Input**

```text
todo first task
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [T][ ] first task
Now you have 1 tasks in the list.
____________________________________________________________
```

#### Step 2

**Input**

```text
todo second task
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [T][ ] second task
Now you have 2 tasks in the list.
____________________________________________________________
```

#### Step 3

**Input**

```text
mark 1
```

**Expected output**

```text
____________________________________________________________
Excellent work Master Wayne! I've marked this task as done:
  [T][X] first task
____________________________________________________________
```

#### Step 4

**Input**

```text
mark abc
```

**Expected output**

```text
____________________________________________________________
Please provide a valid task number.
____________________________________________________________
```

#### Step 5

**Input**

```text
unmark 3
```

**Expected output**

```text
____________________________________________________________
Invalid task number.
____________________________________________________________
```

#### Step 6

**Input**

```text
market 2
```

**Expected output**

```text
____________________________________________________________
Pardon me Master Wayne, I did not quite get that.
____________________________________________________________
```

#### Step 7

**Input**

```text
list
```

**Expected output**

```text
____________________________________________________________
These are your tasks Master Wayne:
1.[T][X] first task
2.[T][ ] second task
____________________________________________________________
```

#### Step 8

**Input**

```text
unmark 1
```

**Expected output**

```text
____________________________________________________________
Alright Master Wayne, I have unmarked this task as requested:
  [T][ ] first task
____________________________________________________________
```

#### Step 9

**Input**

```text
unmark 0
```

**Expected output**

```text
____________________________________________________________
Invalid task number.
____________________________________________________________
```

#### Step 10

**Input**

```text
list
```

**Expected output**

```text
____________________________________________________________
These are your tasks Master Wayne:
1.[T][ ] first task
2.[T][ ] second task
____________________________________________________________
```

After Step 10 passes, close standard input so the application exits normally.

### TEST-07: Recover from malformed commands while preserving task order

**Aim:** Verify that Alfred continues accepting valid commands after errors and never inserts malformed tasks.

**Preconditions:** The application has just started and the in-memory task list is empty.

#### Step 1

**Input**

```text
deadline read book
```

**Expected output**

```text
____________________________________________________________
Unable to add the deadline: the /by marker is missing.
Use either: deadline <description> /by <yyyy-MM-dd>
        or: deadline <description> /by <yyyy-MM-dd HH:mm>
Examples: deadline return book /by 2019-10-15
          deadline submit report /by 2019-10-15 17:30
____________________________________________________________
```

#### Step 2

**Input**

```text
list
```

**Expected output**

```text
____________________________________________________________
You have no tasks, Master Wayne.
____________________________________________________________
```

#### Step 3

**Input**

```text
deadline read book /by 2019-12-01
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [D][ ] read book (by: Dec 01 2019)
Now you have 1 tasks in the list.
____________________________________________________________
```

#### Step 4

**Input**

```text
event product launch /from 2019-12-02
```

**Expected output**

```text
____________________________________________________________
Unable to add the event: the /to marker is missing.
Use either: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>
        or: event <description> /from <yyyy-MM-dd HH:mm> /to <yyyy-MM-dd HH:mm>
Examples: event conference /from 2019-10-15 /to 2019-10-16
          event meeting /from 2019-10-15 14:00 /to 2019-10-15 16:00
____________________________________________________________
```

#### Step 5

**Input**

```text
list
```

**Expected output**

```text
____________________________________________________________
These are your tasks Master Wayne:
1.[D][ ] read book (by: Dec 01 2019)
____________________________________________________________
```

#### Step 6

**Input**

```text
event product launch /from 2019-12-02 /to 2019-12-03
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [E][ ] product launch (from: Dec 02 2019 to: Dec 03 2019)
Now you have 2 tasks in the list.
____________________________________________________________
```

#### Step 7

**Input**

```text
todo
```

**Expected output**

```text
____________________________________________________________
The description of a todo cannot be empty.
____________________________________________________________
```

#### Step 8

**Input**

```text
todo pack bags
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [T][ ] pack bags
Now you have 3 tasks in the list.
____________________________________________________________
```

#### Step 9

**Input**

```text
list
```

**Expected output**

```text
____________________________________________________________
These are your tasks Master Wayne:
1.[D][ ] read book (by: Dec 01 2019)
2.[E][ ] product launch (from: Dec 02 2019 to: Dec 03 2019)
3.[T][ ] pack bags
____________________________________________________________
```

After Step 9 passes, close standard input so the application exits normally.

### TEST-08: Load saved tasks on startup

**Aim:** Verify that Alfred restores every task type and completion status from the data file, then preserves the
loaded tasks when a new task is added.

**Preconditions:** Before starting the application, create `data/alfred.txt` with exactly this content:

```text
T | 1 | read book
D | 0 | return book | 2019-10-15
E | 1 | project meeting | 2019-10-15T14:00 | 2019-10-15T16:00
```

#### Step 1

**Input**

```text
list
```

**Expected output**

```text
____________________________________________________________
These are your tasks Master Wayne:
1.[T][X] read book
2.[D][ ] return book (by: Oct 15 2019)
3.[E][X] project meeting (from: Oct 15 2019, 2:00 PM to: Oct 15 2019, 4:00 PM)
____________________________________________________________
```

#### Step 2

**Input**

```text
todo pack bags
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [T][ ] pack bags
Now you have 4 tasks in the list.
____________________________________________________________
```

**Expected data file**

```text
T | 1 | read book
D | 0 | return book | 2019-10-15
E | 1 | project meeting | 2019-10-15T14:00 | 2019-10-15T16:00
T | 0 | pack bags
```

After Step 2 passes, close standard input so the application exits normally.

### TEST-09: Skip corrupted lines when loading saved tasks

**Aim:** Verify that Alfred reports each corrupted line in the data file, skips it, and still loads every
well-formed task on startup.

**Preconditions:** Before starting the application, create `data/alfred.txt` with exactly this content:

```text
T | 0 | borrow book
T | 1

D | 0 | return book | 2019-10-15
D | 0 | impossible deadline | 2019-02-29
E | 0 | invalid event | Monday | Tuesday
E | 0 | reversed event | 2019-10-16 | 2019-10-15
X | 0 | mystery task
```

**Expected additional startup output immediately before the standard banner:**

```text
I could not make sense of this line, Master Wayne: a todo is missing its description.
Offending line: "T | 1"
I could not make sense of this line, Master Wayne: it does not match the expected "type | status | details" format.
Offending line: ""
I could not make sense of this line, Master Wayne: a deadline date and optional time are invalid.
Expected format: D | status | description | yyyy-MM-dd[THH:mm].
Offending line: "D | 0 | impossible deadline | 2019-02-29"
I could not make sense of this line, Master Wayne: event dates and optional times are invalid.
Expected format: E | status | description | yyyy-MM-dd[THH:mm] | yyyy-MM-dd[THH:mm].
Offending line: "E | 0 | invalid event | Monday | Tuesday"
I could not make sense of this line, Master Wayne: an event end cannot be before its start.
Expected format: E | status | description | yyyy-MM-dd[THH:mm] | yyyy-MM-dd[THH:mm].
Offending line: "E | 0 | reversed event | 2019-10-16 | 2019-10-15"
I could not make sense of this line, Master Wayne: unknown task type "X".
Offending line: "X | 0 | mystery task"
```

#### Step 1

**Input**

```text
list
```

**Expected output**

```text
____________________________________________________________
These are your tasks Master Wayne:
1.[T][ ] borrow book
2.[D][ ] return book (by: Oct 15 2019)
____________________________________________________________
```

After Step 1 passes, close standard input so the application exits normally.

### TEST-10: Survive a save/load failure instead of crashing

**Aim:** Verify that Alfred reports a friendly message and keeps the session alive when the data file cannot be
read or written, instead of crashing with an uncaught exception.

**Preconditions:** Before starting the application, delete `data/alfred.txt` if it exists, then create a
**directory** at that exact path (e.g. `mkdir -p data/alfred.txt`) so both reading and writing it as a file fail.

**Expected additional startup output immediately before the standard banner:**

```text
I could not load your saved tasks, Master Wayne.
Starting with an empty task list.
```

#### Step 1

**Input**

```text
todo borrow book
```

**Expected output**

```text
____________________________________________________________
I could not save that, Master Wayne.
____________________________________________________________
```

#### Step 2

**Input**

```text
list
```

**Expected output**

```text
____________________________________________________________
These are your tasks Master Wayne:
1.[T][ ] borrow book
____________________________________________________________
```

#### Step 3

**Input**

```text
bye
```

**Expected output**

```text
____________________________________________________________
Good luck Master Wayne.

              *         *      *         *               
          ***          **********          ***           
       *****           **********           *****        
     *******           **********           *******      
   **********         ************         **********    
  ****************************************************   
 ******************************************************  
******************************************************** 
******************************************************** 
******************************************************** 
 ******************************************************  
  ********      ************************      ********   
   *******       *     *********      *       *******    
     ******             *******              ******      
       *****             *****              *****        
          ***             ***              ***           
            **             *              **             

____________________________________________________________
```

**State cleanup for this test case:** Remove the `data/alfred.txt` **directory** (not a file) after the
application exits, since the default cleanup only expects a file at that path.

### TEST-11: Reject unsafe or ambiguous deadline/event/todo input

**Aim:** Verify that Alfred rejects a description or date/time containing the data-file delimiter " | ", rejects
a marker embedded inside a longer word (such as `/byte` or `/toy`), rejects a marker used more than once, and
gives a precise hint when `/to` appears before `/from` in an event.

**Preconditions:** The application has just started and the in-memory task list is empty.

#### Step 1

**Input**

```text
todo buy milk | eggs
```

**Expected output**

```text
____________________________________________________________
The description of a todo cannot contain " | ", as it is reserved for saving tasks to disk.
____________________________________________________________
```

#### Step 2

**Input**

```text
deadline submit report /byte 5pm
```

**Expected output**

```text
____________________________________________________________
Unable to add the deadline: the /by marker is missing.
Use either: deadline <description> /by <yyyy-MM-dd>
        or: deadline <description> /by <yyyy-MM-dd HH:mm>
Examples: deadline return book /by 2019-10-15
          deadline submit report /by 2019-10-15 17:30
____________________________________________________________
```

#### Step 3

**Input**

```text
deadline pay bills /by 5pm /by mistake
```

**Expected output**

```text
____________________________________________________________
Unable to add the deadline: the /by marker can only be used once.
Use either: deadline <description> /by <yyyy-MM-dd>
        or: deadline <description> /by <yyyy-MM-dd HH:mm>
Examples: deadline return book /by 2019-10-15
          deadline submit report /by 2019-10-15 17:30
____________________________________________________________
```

#### Step 4

**Input**

```text
event trip /to 2019-10-16 /from 2019-10-15
```

**Expected output**

```text
____________________________________________________________
Unable to add the event: the /to marker must come after the /from marker.
Use either: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>
        or: event <description> /from <yyyy-MM-dd HH:mm> /to <yyyy-MM-dd HH:mm>
Examples: event conference /from 2019-10-15 /to 2019-10-16
          event meeting /from 2019-10-15 14:00 /to 2019-10-15 16:00
____________________________________________________________
```

#### Step 5

**Input**

```text
event go /toy shop /from 2019-10-15 /to 2019-10-16
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [E][ ] go /toy shop (from: Oct 15 2019 to: Oct 16 2019)
Now you have 1 tasks in the list.
____________________________________________________________
```

#### Step 6

**Input**

```text
deadline pay | rent /by 5pm
```

**Expected output**

```text
____________________________________________________________
Unable to add the deadline: the task description cannot contain " | ".
Use either: deadline <description> /by <yyyy-MM-dd>
        or: deadline <description> /by <yyyy-MM-dd HH:mm>
Examples: deadline return book /by 2019-10-15
          deadline submit report /by 2019-10-15 17:30
____________________________________________________________
```

#### Step 7

**Input**

```text
list
```

**Expected output**

```text
____________________________________________________________
These are your tasks Master Wayne:
1.[E][ ] go /toy shop (from: Oct 15 2019 to: Oct 16 2019)
____________________________________________________________
```

**Expected data file**

```text
E | 0 | go /toy shop | 2019-10-15 | 2019-10-16
```

After Step 7 passes, close standard input so the application exits normally.

### TEST-12: Trim whitespace around top-level commands

**Aim:** Verify that a top-level command with leading or trailing whitespace still matches, instead of falling
through to the unrecognized-command message.

**Preconditions:** The application has just started and the in-memory task list is empty.

#### Step 1

**Input**

```text
 list
```

**Expected output**

```text
____________________________________________________________
You have no tasks, Master Wayne.
____________________________________________________________
```

#### Step 2

**Input**

```text
bye 
```

**Expected output**

```text
____________________________________________________________
Good luck Master Wayne.

              *         *      *         *               
          ***          **********          ***           
       *****           **********           *****        
     *******           **********           *******      
   **********         ************         **********    
  ****************************************************   
 ******************************************************  
******************************************************** 
******************************************************** 
******************************************************** 
 ******************************************************  
  ********      ************************      ********   
   *******       *     *********      *       *******    
     ******             *******              ******      
       *****             *****              *****        
          ***             ***              ***           
            **             *              **             

____________________________________________________________
```

### TEST-13: Distinguish missing vs. too-many-arguments for mark/unmark

**Aim:** Verify that `mark`/`unmark` give a different message for a missing task number than for too many
arguments.

**Preconditions:** The application has just started and the in-memory task list is empty.

#### Step 1

**Input**

```text
todo sample task
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [T][ ] sample task
Now you have 1 tasks in the list.
____________________________________________________________
```

#### Step 2

**Input**

```text
mark
```

**Expected output**

```text
____________________________________________________________
Please provide a task number to mark as done.
____________________________________________________________
```

#### Step 3

**Input**

```text
mark 1 2
```

**Expected output**

```text
____________________________________________________________
Please provide only one task number to mark as done.
____________________________________________________________
```

#### Step 4

**Input**

```text
unmark
```

**Expected output**

```text
____________________________________________________________
Please provide a task number to mark as not done.
____________________________________________________________
```

#### Step 5

**Input**

```text
unmark 1 2
```

**Expected output**

```text
____________________________________________________________
Please provide only one task number to mark as not done.
____________________________________________________________
```

After Step 5 passes, close standard input so the application exits normally.

### TEST-14: Delete a task

**Aim:** Verify that deleting a task removes it, updates the task count, and renumbers the remaining tasks.

**Preconditions:** The application has just started and the in-memory task list is empty.

#### Step 1

**Input**

```text
todo read book
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
```

#### Step 2

**Input**

```text
event project meeting /from 2019-08-06 /to 2019-08-06
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [E][ ] project meeting (from: Aug 06 2019 to: Aug 06 2019)
Now you have 2 tasks in the list.
____________________________________________________________
```

#### Step 3

**Input**

```text
todo borrow book
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [T][ ] borrow book
Now you have 3 tasks in the list.
____________________________________________________________
```

#### Step 4

**Input**

```text
delete 2
```

**Expected output**

```text
____________________________________________________________
Will do Master Wayne. I've removed this task:
  [E][ ] project meeting (from: Aug 06 2019 to: Aug 06 2019)
Now you have 2 tasks in the list.
____________________________________________________________
```

#### Step 5

**Input**

```text
list
```

**Expected output**

```text
____________________________________________________________
These are your tasks Master Wayne:
1.[T][ ] read book
2.[T][ ] borrow book
____________________________________________________________
```

**Expected data file**

```text
T | 0 | read book
T | 0 | borrow book
```

After Step 5 passes, close standard input so the application exits normally.

### TEST-15: Show scheduled tasks on a date

**Aim:** Verify that `schedule` shows deadlines due on a date and events whose inclusive range contains that
date, while excluding todos and tasks outside the date.

**Preconditions:** The application has just started and the in-memory task list is empty.

#### Step 1

**Input**

```text
todo buy snacks
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [T][ ] buy snacks
Now you have 1 tasks in the list.
____________________________________________________________
```

#### Step 2

**Input**

```text
deadline submit report /by 2019-10-16
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [D][ ] submit report (by: Oct 16 2019)
Now you have 2 tasks in the list.
____________________________________________________________
```

#### Step 3

**Input**

```text
event conference /from 2019-10-15 /to 2019-10-17
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [E][ ] conference (from: Oct 15 2019 to: Oct 17 2019)
Now you have 3 tasks in the list.
____________________________________________________________
```

#### Step 4

**Input**

```text
event interview /from 2019-10-16 /to 2019-10-16
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [E][ ] interview (from: Oct 16 2019 to: Oct 16 2019)
Now you have 4 tasks in the list.
____________________________________________________________
```

#### Step 5

**Input**

```text
schedule 2019-10-16
```

**Expected output**

```text
____________________________________________________________
These are your deadlines and events on Oct 16 2019:
1.[D][ ] submit report (by: Oct 16 2019)
2.[E][ ] conference (from: Oct 15 2019 to: Oct 17 2019)
3.[E][ ] interview (from: Oct 16 2019 to: Oct 16 2019)
____________________________________________________________
```

#### Step 6

**Input**

```text
schedule 2019-10-14
```

**Expected output**

```text
____________________________________________________________
There are no deadlines or events on Oct 14 2019, Master Wayne.
____________________________________________________________
```

After Step 6 passes, close standard input so the application exits normally.

### TEST-16: Reject invalid schedule and event dates

**Aim:** Verify that schedule commands require one valid ISO date and events reject invalid or reversed date
ranges without changing the task list.

**Preconditions:** The application has just started and the in-memory task list is empty.

#### Step 1

**Input**

```text
schedule
```

**Expected output**

```text
____________________________________________________________
Unable to show the schedule: the date is missing.
Use this format: schedule <yyyy-MM-dd>
Example: schedule 2019-10-15
____________________________________________________________
```

#### Step 2

**Input**

```text
schedule 2019-02-29
```

**Expected output**

```text
____________________________________________________________
Unable to show the schedule: the date must use yyyy-MM-dd.
Use this format: schedule <yyyy-MM-dd>
Example: schedule 2019-10-15
____________________________________________________________
```

#### Step 3

**Input**

```text
schedule 2019-10-15 extra
```

**Expected output**

```text
____________________________________________________________
Unable to show the schedule: only one date can be provided.
Use this format: schedule <yyyy-MM-dd>
Example: schedule 2019-10-15
____________________________________________________________
```

#### Step 4

**Input**

```text
event impossible /from 2019-02-29 /to 2019-03-01
```

**Expected output**

```text
____________________________________________________________
Unable to add the event: the dates and optional times are invalid.
Use either: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>
        or: event <description> /from <yyyy-MM-dd HH:mm> /to <yyyy-MM-dd HH:mm>
Examples: event conference /from 2019-10-15 /to 2019-10-16
          event meeting /from 2019-10-15 14:00 /to 2019-10-15 16:00
____________________________________________________________
```

#### Step 5

**Input**

```text
event backwards /from 2019-10-16 /to 2019-10-15
```

**Expected output**

```text
____________________________________________________________
Unable to add the event: the end cannot be before the start.
Use either: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>
        or: event <description> /from <yyyy-MM-dd HH:mm> /to <yyyy-MM-dd HH:mm>
Examples: event conference /from 2019-10-15 /to 2019-10-16
          event meeting /from 2019-10-15 14:00 /to 2019-10-15 16:00
____________________________________________________________
```

#### Step 6

**Input**

```text
list
```

**Expected output**

```text
____________________________________________________________
You have no tasks, Master Wayne.
____________________________________________________________
```

After Step 6 passes, close standard input so the application exits normally.

### TEST-17: Validate and format deadline dates

**Aim:** Verify that deadlines reject invalid ISO dates, accept valid calendar dates, display them in
`MMM dd yyyy` format, and save them in reloadable ISO format.

**Preconditions:** The application has just started and the in-memory task list is empty.

#### Step 1

**Input**

```text
deadline submit report /by 2019-02-29
```

**Expected output**

```text
____________________________________________________________
Unable to add the deadline: the date and optional time are invalid.
Use either: deadline <description> /by <yyyy-MM-dd>
        or: deadline <description> /by <yyyy-MM-dd HH:mm>
Examples: deadline return book /by 2019-10-15
          deadline submit report /by 2019-10-15 17:30
____________________________________________________________
```

#### Step 2

**Input**

```text
deadline submit report /by 2020-02-29
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [D][ ] submit report (by: Feb 29 2020)
Now you have 1 tasks in the list.
____________________________________________________________
```

**Expected data file**

```text
D | 0 | submit report | 2020-02-29
```

#### Step 3

**Input**

```text
list
```

**Expected output**

```text
____________________________________________________________
These are your tasks Master Wayne:
1.[D][ ] submit report (by: Feb 29 2020)
____________________________________________________________
```

After Step 3 passes, close standard input so the application exits normally.

### TEST-18: Reject invalid delete commands

**Aim:** Verify that invalid delete commands report the problem without changing the task list.

**Preconditions:** The application has just started and the in-memory task list is empty.

#### Step 1

**Input**

```text
todo read book
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
```

#### Step 2

**Input**

```text
delete
```

**Expected output**

```text
____________________________________________________________
Please provide a task number to delete.
____________________________________________________________
```

#### Step 3

**Input**

```text
delete abc
```

**Expected output**

```text
____________________________________________________________
Please provide a valid task number.
____________________________________________________________
```

#### Step 4

**Input**

```text
delete 2
```

**Expected output**

```text
____________________________________________________________
Invalid task number.
____________________________________________________________
```

#### Step 5

**Input**

```text
list
```

**Expected output**

```text
____________________________________________________________
These are your tasks Master Wayne:
1.[T][ ] read book
____________________________________________________________
```

After Step 5 passes, close standard input so the application exits normally.

### TEST-19: Store and query optional task times

**Aim:** Verify that deadlines and events accept times when supplied, display them clearly, store them in ISO
date-time format, and still appear in a date-based schedule.

**Preconditions:** The application has just started and the in-memory task list is empty.

#### Step 1

**Input**

```text
deadline submit report /by 2026-10-02 17:30
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [D][ ] submit report (by: Oct 02 2026, 5:30 PM)
Now you have 1 tasks in the list.
____________________________________________________________
```

**Expected data file**

```text
D | 0 | submit report | 2026-10-02T17:30
```

#### Step 2

**Input**

```text
event team meeting /from 2026-10-02 14:00 /to 2026-10-02 16:00
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [E][ ] team meeting (from: Oct 02 2026, 2:00 PM to: Oct 02 2026, 4:00 PM)
Now you have 2 tasks in the list.
____________________________________________________________
```

**Expected data file**

```text
D | 0 | submit report | 2026-10-02T17:30
E | 0 | team meeting | 2026-10-02T14:00 | 2026-10-02T16:00
```

#### Step 3

**Input**

```text
schedule 2026-10-02
```

**Expected output**

```text
____________________________________________________________
These are your deadlines and events on Oct 02 2026:
1.[D][ ] submit report (by: Oct 02 2026, 5:30 PM)
2.[E][ ] team meeting (from: Oct 02 2026, 2:00 PM to: Oct 02 2026, 4:00 PM)
____________________________________________________________
```

After Step 3 passes, close standard input so the application exits normally.

### TEST-20: Reject invalid or inconsistent task times

**Aim:** Verify that invalid times, mixed event precision, and reversed timed ranges are rejected without adding
tasks, with errors that show both supported input formats.

**Preconditions:** The application has just started and the in-memory task list is empty.

#### Step 1

**Input**

```text
deadline submit report /by 2026-10-02 25:00
```

**Expected output**

```text
____________________________________________________________
Unable to add the deadline: the date and optional time are invalid.
Use either: deadline <description> /by <yyyy-MM-dd>
        or: deadline <description> /by <yyyy-MM-dd HH:mm>
Examples: deadline return book /by 2019-10-15
          deadline submit report /by 2019-10-15 17:30
____________________________________________________________
```

#### Step 2

**Input**

```text
event mixed /from 2026-10-02 /to 2026-10-02 16:00
```

**Expected output**

```text
____________________________________________________________
Unable to add the event: both endpoints must either include a time or omit it.
Use either: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>
        or: event <description> /from <yyyy-MM-dd HH:mm> /to <yyyy-MM-dd HH:mm>
Examples: event conference /from 2019-10-15 /to 2019-10-16
          event meeting /from 2019-10-15 14:00 /to 2019-10-15 16:00
____________________________________________________________
```

#### Step 3

**Input**

```text
event backwards /from 2026-10-02 17:00 /to 2026-10-02 16:00
```

**Expected output**

```text
____________________________________________________________
Unable to add the event: the end cannot be before the start.
Use either: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>
        or: event <description> /from <yyyy-MM-dd HH:mm> /to <yyyy-MM-dd HH:mm>
Examples: event conference /from 2019-10-15 /to 2019-10-16
          event meeting /from 2019-10-15 14:00 /to 2019-10-15 16:00
____________________________________________________________
```

#### Step 4

**Input**

```text
list
```

**Expected output**

```text
____________________________________________________________
You have no tasks, Master Wayne.
____________________________________________________________
```

After Step 4 passes, close standard input so the application exits normally.

### TEST-21: Find tasks by description

**Aim:** Verify that `find` performs a case-insensitive substring search over descriptions, preserves task order
and status, supports multi-word search text, excludes date fields, and handles missing or unmatched searches.

**Preconditions:** The application has just started and the in-memory task list is empty.

#### Step 1

**Input**

```text
todo read book
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
```

#### Step 2

**Input**

```text
deadline return book /by 2026-10-02 17:30
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [D][ ] return book (by: Oct 02 2026, 5:30 PM)
Now you have 2 tasks in the list.
____________________________________________________________
```

#### Step 3

**Input**

```text
event book club /from 2026-10-03 /to 2026-10-03
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [E][ ] book club (from: Oct 03 2026 to: Oct 03 2026)
Now you have 3 tasks in the list.
____________________________________________________________
```

#### Step 4

**Input**

```text
todo buy milk
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [T][ ] buy milk
Now you have 4 tasks in the list.
____________________________________________________________
```

#### Step 5

**Input**

```text
mark 1
```

**Expected output**

```text
____________________________________________________________
Excellent work Master Wayne! I've marked this task as done:
  [T][X] read book
____________________________________________________________
```

#### Step 6

**Input**

```text
mark 2
```

**Expected output**

```text
____________________________________________________________
Excellent work Master Wayne! I've marked this task as done:
  [D][X] return book (by: Oct 02 2026, 5:30 PM)
____________________________________________________________
```

#### Step 7

**Input**

```text
find BOOK
```

**Expected output**

```text
____________________________________________________________
Here are the matching tasks in your list:
1.[T][X] read book
2.[D][X] return book (by: Oct 02 2026, 5:30 PM)
3.[E][ ] book club (from: Oct 03 2026 to: Oct 03 2026)
____________________________________________________________
```

#### Step 8

**Input**

```text
find return book
```

**Expected output**

```text
____________________________________________________________
Here are the matching tasks in your list:
1.[D][X] return book (by: Oct 02 2026, 5:30 PM)
____________________________________________________________
```

#### Step 9

**Input**

```text
find 2026
```

**Expected output**

```text
____________________________________________________________
There are no matching tasks in your list, Master Wayne.
____________________________________________________________
```

#### Step 10

**Input**

```text
find
```

**Expected output**

```text
____________________________________________________________
Please provide a keyword using this format: find <keyword>.
____________________________________________________________
```

After Step 10 passes, close standard input so the application exits normally.
