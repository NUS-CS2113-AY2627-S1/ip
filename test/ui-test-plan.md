# UI Test Plan

This file is the source of truth for scripted acceptance tests of the interactive console UI. Add concrete test cases before running `$test-ui`.

## Test setup

- **Required Java version:** Java 25
- **Working directory:** Repository root
- **Build command:** `javac -d src/main/java/out -sourcepath src/main/java src/main/java/alfred/Alfred.java`
- **Launch command:** `java -cp src/main/java/out alfred.Alfred`
- **State setup:** None. Tasks are stored only in memory and each application process starts with an empty list.
- **State cleanup:** Close standard input after the assertions so the application exits normally.
- **Comparison:** Compare exact application output after normalizing only `CRLF` and `LF` line endings. Terminal input echo is included in the transcript but excluded from comparison.
- **Timeouts:** Allow 10 seconds for startup and 5 seconds for each command response.

## Test cases

Each test case runs in a fresh application process unless its preconditions say otherwise.

### TEST-01: Add and list all task types

**Aim:** Verify that todos, deadlines, and events are added with their type-specific details and listed in order.

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

#### Step 2

**Input**

```text
deadline return book /by Sunday
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
```

#### Step 3

**Input**

```text
event project meeting /from Mon 2pm /to 4pm
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
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
These are your tasks Master Wayne:
1.[T][ ] borrow book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
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
Use this format: deadline <description> /by <date or time>
Example: deadline return book /by tomorrow
____________________________________________________________
```

After Step 1 passes, close standard input so the application exits normally.

### TEST-03: Reject an event without a description

**Aim:** Verify that an event without a task description explains the exact problem and shows how to fix it.

**Preconditions:** The application has just started and the in-memory task list is empty.

#### Step 1

**Input**

```text
event /from Mon 2pm /to 4pm
```

**Expected output**

```text
____________________________________________________________
Unable to add the event: the task description is missing.
Use this format: event <description> /from <start> /to <end>
Example: event project meeting /from Mon 2pm /to 4pm
____________________________________________________________
```

After Step 1 passes, close standard input so the application exits normally.

### TEST-04: Mark and unmark a task

**Aim:** Verify that a task can be marked as done and then restored to not done.

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
deadline /by Friday
```

**Expected output**

```text
____________________________________________________________
Unable to add the deadline: the task description is missing.
Use this format: deadline <description> /by <date or time>
Example: deadline return book /by tomorrow
____________________________________________________________
```

#### Step 3

**Input**

```text
deadline submit report /by Friday
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [D][ ] submit report (by: Friday)
Now you have 2 tasks in the list.
____________________________________________________________
```

#### Step 4

**Input**

```text
event team sync /from /to 4pm
```

**Expected output**

```text
____________________________________________________________
Unable to add the event: the start date or time is missing.
Use this format: event <description> /from <start> /to <end>
Example: event project meeting /from Mon 2pm /to 4pm
____________________________________________________________
```

#### Step 5

**Input**

```text
event team sync /from 3pm /to 4pm
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [E][ ] team sync (from: 3pm to: 4pm)
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
2.[D][ ] submit report (by: Friday)
3.[E][ ] team sync (from: 3pm to: 4pm)
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
Use this format: deadline <description> /by <date or time>
Example: deadline return book /by tomorrow
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
____________________________________________________________
```

#### Step 3

**Input**

```text
deadline read book /by Sunday
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [D][ ] read book (by: Sunday)
Now you have 1 tasks in the list.
____________________________________________________________
```

#### Step 4

**Input**

```text
event product launch /from Monday
```

**Expected output**

```text
____________________________________________________________
Unable to add the event: the /to marker is missing.
Use this format: event <description> /from <start> /to <end>
Example: event project meeting /from Mon 2pm /to 4pm
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
1.[D][ ] read book (by: Sunday)
____________________________________________________________
```

#### Step 6

**Input**

```text
event product launch /from Monday /to Tuesday
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [E][ ] product launch (from: Monday to: Tuesday)
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
1.[D][ ] read book (by: Sunday)
2.[E][ ] product launch (from: Monday to: Tuesday)
3.[T][ ] pack bags
____________________________________________________________
```

After Step 9 passes, close standard input so the application exits normally.

### TEST-08: Delete a task

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
event project meeting /from Aug 6th 2pm /to 4pm
```

**Expected output**

```text
____________________________________________________________
Understood Master Wayne, I've added this task:
  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
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
  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
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

After Step 5 passes, close standard input so the application exits normally.

### TEST-09: Reject invalid delete commands

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
