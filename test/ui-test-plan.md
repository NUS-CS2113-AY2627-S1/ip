# Ernest UI test plan

This plan exercises the stdin/console interface of
`src/main/java/ernest/Ernest.java`.

## Execution information

- Runtime and compiler: Java 25 (`java` and `javac`).
- Main class: `ernest.Ernest`.
- The test runner compiles all sources under `src/main/java` into a temporary
  directory before testing.
- Each test case runs in a fresh process and temporary working directory. Input
  lines are sent in order, one command per line, and `bye` ends the session.
- Each successful add, delete, mark, unmark, or clear command writes the
  current task list to `data/ernest.txt` as CSV. Ernest loads this file when it
  starts.
- A test case may include an optional `Initial data` CSV block. The runner
  writes it to `data/ernest.txt` before starting that case.
- A test case may include an optional `Expected saved data` CSV block. The
  runner compares it with `data/ernest.txt` after that case completes.
- A test case may include `Data directory is a file:` to create a file named
  `data`, which simulates a storage-directory creation failure.
- A test case may include `Data file is a directory:` to create a directory at
  `data/ernest.txt`, which simulates a storage-file read failure.
- Output is compared exactly after CRLF/CR line endings are normalized to LF.
  Extra output, missing output, ordering changes, and whitespace changes fail
  the case.
- The runner prints the console input and output for each case. It stops after
  the first failure and prints both the expected and actual output.

## Test case: Exit from welcome screen

Aim: Verify that Ernest displays its welcome screen and exits cleanly when the user enters `bye`.

Inputs:
```text
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Bye. See you again soon!
______________________________________
```

## Test case: Load saved tasks at startup

Aim: Verify that Ernest restores all supported task types and completion states from its CSV data file.

Initial data:
```csv
type,isDone,description,deadline,startTime,endTime
"todo","true","finish assignment","","",""
"deadline","false","submit report","2026-10-09T18:00","",""
"event","false","team meeting","","2026-10-09T10:00","2026-10-09T11:00"
```

Inputs:
```text
list
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Your to-do list is:
1. [T][X] finish assignment
2. [D][ ] submit report (by: Oct 9 2026, 6:00 PM)
3. [E][ ] team meeting (from: Oct 9 2026, 10:00 AM to: Oct 9 2026, 11:00 AM)
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Ignore malformed saved tasks

Aim: Verify that Ernest loads valid CSV records while ignoring malformed records instead of terminating.

Initial data:
```csv
type,isDone,description,deadline,startTime,endTime
"todo","false","keep this task","","",""
"deadline","maybe","invalid status","Friday","",""
"unknown","false","invalid type","","",""
"event","false","unfinished
"event","false","reversed event","","2026-12-04","2026-12-03"
"todo","true","also keep this task","","",""
```

Inputs:
```text
list
bye
```

Expected output:
```text
Warning: Some saved tasks could not be loaded.
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Your to-do list is:
1. [T][ ] keep this task
2. [T][X] also keep this task
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Reject an invalid saved-data header

Aim: Verify that Ernest rejects a saved-data file with an invalid CSV header.

Initial data:
```csv
invalid,header
"todo","false","do not load this task","","",""
```

Inputs:
```text
list
bye
```

Expected output:
```text
Warning: Saved tasks could not be loaded because the file header is invalid.
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Your to-do list is:
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Accept case-insensitive task commands

Aim: Verify that task types and task markers are case-insensitive while task descriptions preserve their entered casing.

Inputs:
```text
TODO Buy Milk
DEADLINE Submit Report /BY 2026-10-09 18:00
EVENT Team Meeting /FROM 2026-10-09 10:00 /TO 2026-10-09 11:00
todoist invalid
unmarkable 1
list
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Ok, I've added to the task list:
> [T][ ] Buy Milk
Current list size: 1/100
______________________________________
Ok, I've added to the task list:
> [D][ ] Submit Report (by: Oct 9 2026, 6:00 PM)
Current list size: 2/100
______________________________________
Ok, I've added to the task list:
> [E][ ] Team Meeting (from: Oct 9 2026, 10:00 AM to: Oct 9 2026, 11:00 AM)
Current list size: 3/100
______________________________________
Sorry, please insert a valid command.
______________________________________
Sorry, please insert a valid command.
______________________________________
Your to-do list is:
1. [T][ ] Buy Milk
2. [D][ ] Submit Report (by: Oct 9 2026, 6:00 PM)
3. [E][ ] Team Meeting (from: Oct 9 2026, 10:00 AM to: Oct 9 2026, 11:00 AM)
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Reject empty task details

Aim: Verify that deadline and event commands reject missing descriptions, dates, and times.

Inputs:
```text
deadline /by 2026-10-09 1800
deadline submit report /by
event /from 2026-10-09 10:00 /to 2026-10-09 11:00
event meeting /from /to 2026-10-09 11:00
event meeting /from 2026-10-09 10:00 /to
todo valid task
list
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Missing deadline description. Please try again.
______________________________________
Missing deadline date. Please try again.
______________________________________
Missing event description. Please try again.
______________________________________
Missing event start date. Please try again.
______________________________________
Missing event end date. Please try again.
______________________________________
Ok, I've added to the task list:
> [T][ ] valid task
Current list size: 1/100
______________________________________
Your to-do list is:
1. [T][ ] valid task
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Reject missing arguments

Aim: Verify that commands without required arguments print a specific message and that Ernest continues processing later commands.

Inputs:
```text
mark
unmark
todo
deadline
event
todo keep working
list
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Missing task number. Please refer to the task list and try again.
______________________________________
Missing task number. Please refer to the task list and try again.
______________________________________
Missing task description. Please try again.
______________________________________
Missing task description. Please try again.
______________________________________
Missing task description. Please try again.
______________________________________
Ok, I've added to the task list:
> [T][ ] keep working
Current list size: 1/100
______________________________________
Your to-do list is:
1. [T][ ] keep working
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Add and list mixed task types

Aim: Verify that todo, deadline, and event commands can be interleaved while preserving their insertion order and details.

Inputs:
```text
todo review the lecture
deadline submit the assignment /by 2026-10-09 1800
event project discussion /from 2026-10-09 14:00 /to 2026-10-09 15:00
list
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Ok, I've added to the task list:
> [T][ ] review the lecture
Current list size: 1/100
______________________________________
Ok, I've added to the task list:
> [D][ ] submit the assignment (by: Oct 9 2026, 6:00 PM)
Current list size: 2/100
______________________________________
Ok, I've added to the task list:
> [E][ ] project discussion (from: Oct 9 2026, 2:00 PM to: Oct 9 2026, 3:00 PM)
Current list size: 3/100
______________________________________
Your to-do list is:
1. [T][ ] review the lecture
2. [D][ ] submit the assignment (by: Oct 9 2026, 6:00 PM)
3. [E][ ] project discussion (from: Oct 9 2026, 2:00 PM to: Oct 9 2026, 3:00 PM)
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Ignore invalid task numbers

Aim: Verify that marking and unmarking task numbers outside the list leaves the existing task incomplete and in the same position.

Inputs:
```text
todo keep this task
mark 0
mark 2
unmark 0
unmark -1
list
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Ok, I've added to the task list:
> [T][ ] keep this task
Current list size: 1/100
______________________________________
Invalid task number. Please refer to the task list and try again.
______________________________________
Invalid task number. Please refer to the task list and try again.
______________________________________
Invalid task number. Please refer to the task list and try again.
______________________________________
Invalid task number. Please refer to the task list and try again.
______________________________________
Your to-do list is:
1. [T][ ] keep this task
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Add and list a todo

Aim: Verify that a todo command adds a task and `list` displays it as incomplete.

Inputs:
```text
todo read the course notes
list
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Ok, I've added to the task list:
> [T][ ] read the course notes
Current list size: 1/100
______________________________________
Your to-do list is:
1. [T][ ] read the course notes
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Mark and unmark a task

Aim: Verify that `mark` changes a task to done and `unmark` changes it back to not done.

Inputs:
```text
todo submit the report
mark 1
unmark 1
list
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Ok, I've added to the task list:
> [T][ ] submit the report
Current list size: 1/100
______________________________________
Well done! Marked task 1 as done.
______________________________________
Ok, marked task 1 as not done yet.
______________________________________
Your to-do list is:
1. [T][ ] submit the report
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Reject an invalid command

Aim: Verify that Ernest reports an invalid command and remains available until `bye` is entered.

Inputs:
```text
launch rocket
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Sorry, please insert a valid command.
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Reject malformed task commands

Aim: Verify that Ernest reports a task-specific validation message when a deadline or event command omits its marker.

Inputs:
```text
deadline submit assignment by Friday
event team meeting from 10am to 11am
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Deadline must include a /by date.
______________________________________
Event must include a /from date.
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Add and list a deadline

Aim: Verify that a deadline command stores the description, due date, and due time, and displays all values.

Inputs:
```text
deadline submit assignment /by 2026-10-09 1800
list
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Ok, I've added to the task list:
> [D][ ] submit assignment (by: Oct 9 2026, 6:00 PM)
Current list size: 1/100
______________________________________
Your to-do list is:
1. [D][ ] submit assignment (by: Oct 9 2026, 6:00 PM)
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Add and list an event

Aim: Verify that an event command stores and displays the description and complete date-time range.

Inputs:
```text
event team meeting /from 2026-10-09 1000 /to 2026-10-09 1100
list
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Ok, I've added to the task list:
> [E][ ] team meeting (from: Oct 9 2026, 10:00 AM to: Oct 9 2026, 11:00 AM)
Current list size: 1/100
______________________________________
Your to-do list is:
1. [E][ ] team meeting (from: Oct 9 2026, 10:00 AM to: Oct 9 2026, 11:00 AM)
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Accept date-only deadlines and events

Aim: Verify that deadline and event dates can omit their optional times without adding a midnight time to the display.

Inputs:
```text
deadline submit outline /by 2026-10-09
event conference /from 2026-10-09 /to 2026-10-10
list
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Ok, I've added to the task list:
> [D][ ] submit outline (by: Oct 9 2026)
Current list size: 1/100
______________________________________
Ok, I've added to the task list:
> [E][ ] conference (from: Oct 9 2026 to: Oct 10 2026)
Current list size: 2/100
______________________________________
Your to-do list is:
1. [D][ ] submit outline (by: Oct 9 2026)
2. [E][ ] conference (from: Oct 9 2026 to: Oct 10 2026)
______________________________________
Bye. See you again soon!
______________________________________
```

Expected saved data:
```csv
type,isDone,description,deadline,startTime,endTime
"deadline","false","submit outline","2026-10-09","",""
"event","false","conference","","2026-10-09","2026-10-10"
```

## Test case: Reject invalid dates and times

Aim: Verify that Ernest rejects impossible values and values that do not use the documented date and time formats.

Inputs:
```text
deadline impossible date /by 2026-02-30 0900
deadline wrong date format /by 02/10/2026 0900
event impossible time /from 2026-10-09 2400 /to 2026-10-09 0900
event wrong time format /from 2026-10-09 9am /to 2026-10-09 10am
event missing dates /from 0900 /to 1000
event overly precise time /from 2026-10-09 09:00:30 /to 2026-10-09 10:00
list
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Invalid deadline date or time. Please use yyyy-MM-dd with an optional HHmm or HH:mm time.
______________________________________
Invalid deadline date or time. Please use yyyy-MM-dd with an optional HHmm or HH:mm time.
______________________________________
Invalid event date or time. Please use yyyy-MM-dd with an optional HHmm or HH:mm time.
______________________________________
Invalid event date or time. Please use yyyy-MM-dd with an optional HHmm or HH:mm time.
______________________________________
Invalid event date or time. Please use yyyy-MM-dd with an optional HHmm or HH:mm time.
______________________________________
Invalid event date or time. Please use yyyy-MM-dd with an optional HHmm or HH:mm time.
______________________________________
Your to-do list is:
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Handle mixed-case state commands

Aim: Verify that state-changing commands are recognized regardless of command-word casing and that the final state is correct.

Inputs:
```text
todo prepare slides
MaRk 1
LiSt
UnMaRk 1
list
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Ok, I've added to the task list:
> [T][ ] prepare slides
Current list size: 1/100
______________________________________
Well done! Marked task 1 as done.
______________________________________
Your to-do list is:
1. [T][X] prepare slides
______________________________________
Ok, marked task 1 as not done yet.
______________________________________
Your to-do list is:
1. [T][ ] prepare slides
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Preserve state after repeated state commands

Aim: Verify that repeated mark and unmark commands report the invalid transition without changing the task's state.

Inputs:
```text
todo check the answer
mark 1
mark 1
unmark 1
unmark 1
list
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Ok, I've added to the task list:
> [T][ ] check the answer
Current list size: 1/100
______________________________________
Well done! Marked task 1 as done.
______________________________________
Sorry, task 1 is already done.
______________________________________
Ok, marked task 1 as not done yet.
______________________________________
Sorry, task 1 is already marked as not done yet.
______________________________________
Your to-do list is:
1. [T][ ] check the answer
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Keep task order after an invalid task command

Aim: Verify that a malformed task command is rejected without consuming a task number or affecting later valid tasks.

Inputs:
```text
todo first task
event missing markers
deadline second task /by 2026-10-03 1700
list
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Ok, I've added to the task list:
> [T][ ] first task
Current list size: 1/100
______________________________________
Event must include a /from date.
______________________________________
Ok, I've added to the task list:
> [D][ ] second task (by: Oct 3 2026, 5:00 PM)
Current list size: 2/100
______________________________________
Your to-do list is:
1. [T][ ] first task
2. [D][ ] second task (by: Oct 3 2026, 5:00 PM)
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Reject reversed event markers

Aim: Verify that an event whose `/to` marker appears before `/from` is rejected and does not add an invalid task.

Inputs:
```text
event invalid order /to 11am /from 10am
list
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
The /to marker must come after /from.
______________________________________
Your to-do list is:
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Validate event chronology

Aim: Verify that events reject reversed dates and times while allowing equal endpoints and mixed precision.

Inputs:
```text
event reversed dates /from 2026-12-04 /to 2026-12-03
event reversed times /from 2026-12-04 1400 /to 2026-12-04 13:00
event equal dates /from 2026-12-04 /to 2026-12-04
event equal times /from 2026-12-04 1400 /to 2026-12-04 14:00
event mixed precision /from 2026-12-04 1400 /to 2026-12-04
list
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Event end cannot be before its start.
______________________________________
Event end cannot be before its start.
______________________________________
Ok, I've added to the task list:
> [E][ ] equal dates (from: Dec 4 2026 to: Dec 4 2026)
Current list size: 1/100
______________________________________
Ok, I've added to the task list:
> [E][ ] equal times (from: Dec 4 2026, 2:00 PM to: Dec 4 2026, 2:00 PM)
Current list size: 2/100
______________________________________
Ok, I've added to the task list:
> [E][ ] mixed precision (from: Dec 4 2026, 2:00 PM to: Dec 4 2026)
Current list size: 3/100
______________________________________
Your to-do list is:
1. [E][ ] equal dates (from: Dec 4 2026 to: Dec 4 2026)
2. [E][ ] equal times (from: Dec 4 2026, 2:00 PM to: Dec 4 2026, 2:00 PM)
3. [E][ ] mixed precision (from: Dec 4 2026, 2:00 PM to: Dec 4 2026)
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Reject nonnumeric state arguments

Aim: Verify that nonnumeric mark and unmark arguments are rejected without changing the state of a valid task.

Inputs:
```text
todo verify state
mark abc
unmark xyz
mark 1
unmark 1
list
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Ok, I've added to the task list:
> [T][ ] verify state
Current list size: 1/100
______________________________________
Task number must be an integer.
______________________________________
Task number must be an integer.
______________________________________
Well done! Marked task 1 as done.
______________________________________
Ok, marked task 1 as not done yet.
______________________________________
Your to-do list is:
1. [T][ ] verify state
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Handle surrounding whitespace

Aim: Verify that leading, trailing, and tab whitespace around commands does not change their meaning.

Inputs:
```text
  todo	spaced task
  mark	1
  list
  bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Ok, I've added to the task list:
> [T][ ] spaced task
Current list size: 1/100
______________________________________
Well done! Marked task 1 as done.
______________________________________
Your to-do list is:
1. [T][X] spaced task
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Delete tasks safely

Aim: Verify that deletion removes the requested task, renumbers the remaining
task, and rejects missing, nonnumeric, and out-of-range task numbers.

Inputs:
```text
todo first task
todo second task
delete 1
mark 1
delete
delete abc
delete 2
list
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Ok, I've added to the task list:
> [T][ ] first task
Current list size: 1/100
______________________________________
Ok, I've added to the task list:
> [T][ ] second task
Current list size: 2/100
______________________________________
Ok, I've deleted this task from the task list:
> [T][ ] first task
Current list size: 1/100
______________________________________
Well done! Marked task 1 as done.
______________________________________
Missing task number. Please refer to the task list and try again.
______________________________________
Task number must be an integer.
______________________________________
Invalid task number. Please refer to the task list and try again.
______________________________________
Your to-do list is:
1. [T][X] second task
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Clear the task list

Aim: Verify that `clear` removes all tasks and that the following list command shows an empty list.

Inputs:
```text
todo remove this task
clear
list
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Ok, I've added to the task list:
> [T][ ] remove this task
Current list size: 1/100
______________________________________
Task list cleared.
______________________________________
Your to-do list is:
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Create and populate storage on first save

Aim: Verify that a fresh working directory gains a data folder and populated CSV file after a task is added.

Inputs:
```text
todo persist this task
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Ok, I've added to the task list:
> [T][ ] persist this task
Current list size: 1/100
______________________________________
Bye. See you again soon!
______________________________________
```

Expected saved data:
```csv
type,isDone,description,deadline,startTime,endTime
"todo","false","persist this task","","",""
```

## Test case: Show command help

Aim: Verify that `help` displays command guidance and that Ernest continues accepting commands.

Inputs:
```text
help
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Available commands:
todo DESCRIPTION
deadline DESCRIPTION /by yyyy-MM-dd [HHmm|HH:mm]
event DESCRIPTION /from yyyy-MM-dd [HHmm|HH:mm] /to yyyy-MM-dd [HHmm|HH:mm]
find KEYWORD
list, delete NUMBER, mark NUMBER, unmark NUMBER, clear, help, bye
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Find tasks using an approximate case-insensitive keyword

Aim: Verify that `find` performs a case-insensitive partial-description search,
preserves matching task order and state, and displays an empty result when no
task matches.

Inputs:
```text
todo Read the Course Notes
deadline submit Final Report /by 2026-10-09 18:00
todo review REPORT outline
mark 2
find port
find COURSE
find final report
find absent
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Ok, I've added to the task list:
> [T][ ] Read the Course Notes
Current list size: 1/100
______________________________________
Ok, I've added to the task list:
> [D][ ] submit Final Report (by: Oct 9 2026, 6:00 PM)
Current list size: 2/100
______________________________________
Ok, I've added to the task list:
> [T][ ] review REPORT outline
Current list size: 3/100
______________________________________
Well done! Marked task 2 as done.
______________________________________
Here are the matching tasks in your list:
1. [D][X] submit Final Report (by: Oct 9 2026, 6:00 PM)
2. [T][ ] review REPORT outline
______________________________________
Here are the matching tasks in your list:
1. [T][ ] Read the Course Notes
______________________________________
Here are the matching tasks in your list:
1. [D][X] submit Final Report (by: Oct 9 2026, 6:00 PM)
______________________________________
Here are the matching tasks in your list:
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Reject a find command without a keyword

Aim: Verify that `find` requires non-empty search text and that Ernest remains
available after reporting the missing keyword.

Inputs:
```text
find
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Missing search keyword. Please try again.
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Handle end of input

Aim: Verify that Ernest exits cleanly without printing a farewell when standard input ends without `bye`.

Inputs:
```text
list
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Your to-do list is:
______________________________________
```

## Test case: Warn when saving fails

Aim: Verify that Ernest informs the user when each task-list mutation cannot be saved.

Data directory is a file:

Inputs:
```text
todo unsaved task
mark 1
unmark 1
delete 1
clear
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Warning: Task changes could not be saved.
Ok, I've added to the task list:
> [T][ ] unsaved task
Current list size: 1/100
______________________________________
Warning: Task changes could not be saved.
Well done! Marked task 1 as done.
______________________________________
Warning: Task changes could not be saved.
Ok, marked task 1 as not done yet.
______________________________________
Warning: Task changes could not be saved.
Ok, I've deleted this task from the task list:
> [T][ ] unsaved task
Current list size: 0/100
______________________________________
Warning: Task changes could not be saved.
Task list cleared.
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Reject empty and overlong fixed commands

Aim: Verify that empty input and fixed commands with extra arguments are rejected without exiting or changing state.

Inputs:
```text

bye now
list now
clear now
help now
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Sorry, please insert a valid command.
______________________________________
Sorry, please insert a valid command.
______________________________________
Sorry, please insert a valid command.
______________________________________
Sorry, please insert a valid command.
______________________________________
Sorry, please insert a valid command.
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Reject incomplete and duplicate task markers

Aim: Verify that missing or repeated deadline and event markers produce specific errors without adding tasks.

Inputs:
```text
deadline submit report /by Friday /by Saturday
event team meeting /from 10am
event team meeting /from 10am /from 10:30am /to 11am
event team meeting /from 10am /to 11am /to noon
list
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Deadline may contain only one /by marker.
______________________________________
Event must include a /to date.
______________________________________
Event may only contain one /from and one /to marker.
______________________________________
Event may only contain one /from and one /to marker.
______________________________________
Your to-do list is:
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Preserve quoted CSV task details

Aim: Verify that storage loads and saves commas and quotation marks in task fields without corrupting them.

Initial data:
```csv
type,isDone,description,deadline,startTime,endTime
"todo","false","buy milk, eggs and ""bread""","","",""
"deadline","true","submit ""final"", report","2026-10-09T18:00","",""
"event","false","team ""sync"", weekly","","2026-10-09T10:00","2026-10-09T11:00"
```

Inputs:
```text
mark 1
list
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Well done! Marked task 1 as done.
______________________________________
Your to-do list is:
1. [T][X] buy milk, eggs and "bread"
2. [D][X] submit "final", report (by: Oct 9 2026, 6:00 PM)
3. [E][ ] team "sync", weekly (from: Oct 9 2026, 10:00 AM to: Oct 9 2026, 11:00 AM)
______________________________________
Bye. See you again soon!
______________________________________
```

Expected saved data:
```csv
type,isDone,description,deadline,startTime,endTime
"todo","true","buy milk, eggs and ""bread""","","",""
"deadline","true","submit ""final"", report","2026-10-09T18:00","",""
"event","false","team ""sync"", weekly","","2026-10-09T10:00","2026-10-09T11:00"
```

## Test case: Warn when loading fails

Aim: Verify that Ernest warns the user and starts with an empty task list when the storage file cannot be read.

Data file is a directory:

Inputs:
```text
list
bye
```

Expected output:
```text
Warning: Saved tasks could not be loaded.
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Your to-do list is:
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Reject tasks beyond storage capacity

Aim: Verify that oversized saved data is truncated with a warning and that no task can be added beyond capacity.

Initial data:
```csv
type,isDone,description,deadline,startTime,endTime
"todo","false","task 1","","",""
"todo","false","task 2","","",""
"todo","false","task 3","","",""
"todo","false","task 4","","",""
"todo","false","task 5","","",""
"todo","false","task 6","","",""
"todo","false","task 7","","",""
"todo","false","task 8","","",""
"todo","false","task 9","","",""
"todo","false","task 10","","",""
"todo","false","task 11","","",""
"todo","false","task 12","","",""
"todo","false","task 13","","",""
"todo","false","task 14","","",""
"todo","false","task 15","","",""
"todo","false","task 16","","",""
"todo","false","task 17","","",""
"todo","false","task 18","","",""
"todo","false","task 19","","",""
"todo","false","task 20","","",""
"todo","false","task 21","","",""
"todo","false","task 22","","",""
"todo","false","task 23","","",""
"todo","false","task 24","","",""
"todo","false","task 25","","",""
"todo","false","task 26","","",""
"todo","false","task 27","","",""
"todo","false","task 28","","",""
"todo","false","task 29","","",""
"todo","false","task 30","","",""
"todo","false","task 31","","",""
"todo","false","task 32","","",""
"todo","false","task 33","","",""
"todo","false","task 34","","",""
"todo","false","task 35","","",""
"todo","false","task 36","","",""
"todo","false","task 37","","",""
"todo","false","task 38","","",""
"todo","false","task 39","","",""
"todo","false","task 40","","",""
"todo","false","task 41","","",""
"todo","false","task 42","","",""
"todo","false","task 43","","",""
"todo","false","task 44","","",""
"todo","false","task 45","","",""
"todo","false","task 46","","",""
"todo","false","task 47","","",""
"todo","false","task 48","","",""
"todo","false","task 49","","",""
"todo","false","task 50","","",""
"todo","false","task 51","","",""
"todo","false","task 52","","",""
"todo","false","task 53","","",""
"todo","false","task 54","","",""
"todo","false","task 55","","",""
"todo","false","task 56","","",""
"todo","false","task 57","","",""
"todo","false","task 58","","",""
"todo","false","task 59","","",""
"todo","false","task 60","","",""
"todo","false","task 61","","",""
"todo","false","task 62","","",""
"todo","false","task 63","","",""
"todo","false","task 64","","",""
"todo","false","task 65","","",""
"todo","false","task 66","","",""
"todo","false","task 67","","",""
"todo","false","task 68","","",""
"todo","false","task 69","","",""
"todo","false","task 70","","",""
"todo","false","task 71","","",""
"todo","false","task 72","","",""
"todo","false","task 73","","",""
"todo","false","task 74","","",""
"todo","false","task 75","","",""
"todo","false","task 76","","",""
"todo","false","task 77","","",""
"todo","false","task 78","","",""
"todo","false","task 79","","",""
"todo","false","task 80","","",""
"todo","false","task 81","","",""
"todo","false","task 82","","",""
"todo","false","task 83","","",""
"todo","false","task 84","","",""
"todo","false","task 85","","",""
"todo","false","task 86","","",""
"todo","false","task 87","","",""
"todo","false","task 88","","",""
"todo","false","task 89","","",""
"todo","false","task 90","","",""
"todo","false","task 91","","",""
"todo","false","task 92","","",""
"todo","false","task 93","","",""
"todo","false","task 94","","",""
"todo","false","task 95","","",""
"todo","false","task 96","","",""
"todo","false","task 97","","",""
"todo","false","task 98","","",""
"todo","false","task 99","","",""
"todo","false","task 100","","",""
"todo","false","task 101","","",""
```

Inputs:
```text
todo overflow task
bye
```

Expected output:
```text
Warning: Some saved tasks could not be loaded.
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
The list is full (100/100).
______________________________________
Bye. See you again soon!
______________________________________
```
