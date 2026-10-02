# Dandelion UI test plan

Run the plan from the repository root with:

```text
python3 test-ui/scripts/run_ui_tests.py
```

The runner compiles the application before testing. Each expected-output line is a meaningful output fragment; fragments must appear in the listed order in the console output.

## Test case: Create and manage all task types

**Aim:** Verify that todo, deadline, and event commands create the correct task types, and that marking a task updates its status.

**Input:**

```text
todo read book
deadline return book /by 2019-12-01
event team meeting /from Monday 10am /to Monday 11am
list
mark 2
list
bye
```

**Expected output:**

```text
added: [T][ ] read book
added: [D][ ] return book (by: Dec 1 2019)
added: [E][ ] team meeting (from: Monday 10am to: Monday 11am)
1.[T][ ] read book
2.[D][ ] return book (by: Dec 1 2019)
3.[E][ ] team meeting (from: Monday 10am to: Monday 11am)
Nice! I've marked this task as done:
[D][X] return book (by: Dec 1 2019)
Bye, User.
```

## Test case: Persist all task types across a restart

**Aim:** Verify that todo, deadline, and event tasks, including a marked status, are saved and loaded.

**Input:**

```text
todo read book
deadline return book /by 2019-12-01
event team meeting /from Monday 10am /to Monday 11am
mark 2
bye
```

**Restart input:**

```text
list
bye
```

**Expected output:**

```text
added: [T][ ] read book
added: [D][ ] return book (by: Dec 1 2019)
added: [E][ ] team meeting (from: Monday 10am to: Monday 11am)
Nice! I've marked this task as done:
[D][X] return book (by: Dec 1 2019)
1.[T][ ] read book
2.[D][X] return book (by: Dec 1 2019)
3.[E][ ] team meeting (from: Monday 10am to: Monday 11am)
Bye, User.
```

## Test case: Persist unmarking and deletion across a restart

**Aim:** Verify that an unmarked task and a deleted task are saved correctly.

**Input:**

```text
todo keep this task
todo remove this task
mark 1
unmark 1
delete 2
bye
```

**Restart input:**

```text
list
bye
```

**Expected output:**

```text
added: [T][ ] keep this task
added: [T][ ] remove this task
Nice! I've marked this task as done:
OK, I've marked this task as not done yet:
deleted: [T][ ] remove this task
1.[T][ ] keep this task
Bye, User.
```

## Test case: Handle commands when no tasks exist

**Aim:** Verify that list, mark, unmark, and delete report an empty task list.

**Input:**

```text
list
mark 1
unmark 1
delete 1
bye
```

**Expected output:**

```text
No tasks added yet.
No tasks added yet.
No tasks added yet.
No tasks added yet.
Bye, User.
```

## Test case: Reject malformed task commands

**Aim:** Verify that every parser validation message is shown without ending the session.

**Input:**

```text
todo
deadline /by Friday
deadline return book /by
event meeting /to 11am
event meeting /from 10am
event /from 10am /to 11am
event meeting /from /to 11am
event meeting /from 10am /to
bye
```

**Expected output:**

```text
Todo description cannot be empty.
Both description and deadline are required.
Both description and deadline are required.
Event must include /from.
Event must include /to.
Description, from, and to cannot be empty.
Description, from, and to cannot be empty.
Description, from, and to cannot be empty.
Bye, User.
```

## Test case: Handle unknown and blank commands

**Aim:** Verify that unknown and blank commands produce an error and the session continues.

**Input:**

```text
unknown command

bye
```

**Expected output:**

```text
Unknown command.
Unknown command.
Bye, User.
```

## Test case: Reject an unknown saved task type

**Aim:** Verify that an unknown task type in saved data is reported at startup.

**Initial data:**

```text
X | 0 | unsupported task
```

**Input:**

```text
bye
```

**Expected output:**

```text
Unable to load saved tasks: Unknown saved task type: X.
Bye, User.
```

## Test case: Reject a saved task with missing fields

**Aim:** Verify that a saved task with too few fields is reported at startup.

**Initial data:**

```text
T | 0
```

**Input:**

```text
bye
```

**Expected output:**

```text
Unable to load saved tasks: Saved T task has an invalid format.
Bye, User.
```

## Test case: Reject a saved task with an invalid status

**Aim:** Verify that a saved task status other than 0 or 1 is reported at startup.

**Initial data:**

```text
T | 2 | read book
```

**Input:**

```text
bye
```

**Expected output:**

```text
Unable to load saved tasks: Saved task status must be 0 or 1.
Bye, User.
```

## Test case: Reject an invalid deadline and continue

**Aim:** Verify that a missing `/by` marker displays an error without terminating the session.

**Input:**

```text
deadline return book
todo recover after error
bye
```

**Expected output:**

```text
/by is missing.
added: [T][ ] recover after error
Bye, User.
```

## Test case: Reject an event with reversed time markers

**Aim:** Verify that `/from` must appear before `/to` and that the application continues afterward.

**Input:**

```text
event meeting /to 11am /from 10am
todo recover after event error
bye
```

**Expected output:**

```text
Event must place /from before /to.
added: [T][ ] recover after event error
Bye, User.
```

## Test case: Reject an invalid deadline date and continue

**Aim:** Verify that a deadline must use `yyyy-MM-dd` format and that the application continues afterward.

**Input:**

```text
deadline return book /by Friday
todo recover after date error
bye
```

**Expected output:**

```text
Deadline must use yyyy-MM-dd format.
added: [T][ ] recover after date error
Bye, User.
```

## Test case: Handle invalid mark and unmark arguments

**Aim:** Verify that invalid task numbers produce messages and valid unmark commands still work.

**Input:**

```text
todo submit assignment
mark abc
mark 2
unmark 1
bye
```

**Expected output:**

```text
added: [T][ ] submit assignment
Please enter a task number after the command.
Please enter a task number from 1 to 1.
OK, I've marked this task as not done yet:
[T][ ] submit assignment
Bye, User.
```

## Test case: Delete a task and validate its number

**Aim:** Verify that deleting a middle task removes it, renumbers the remaining tasks, and handles invalid task numbers.

**Input:**

```text
todo first task
deadline second task /by 2019-12-02
todo third task
delete 2
list
delete 3
delete abc
bye
```

**Expected output:**

```text
added: [T][ ] first task
added: [D][ ] second task (by: Dec 2 2019)
added: [T][ ] third task
deleted: [D][ ] second task (by: Dec 2 2019)
Number of tasks: 2
1.[T][ ] first task
2.[T][ ] third task
Please enter a task number from 1 to 2.
Number of tasks: 2
Please enter a task number after the command.
Number of tasks: 2
Bye, User.
```

## Test case: Find tasks by description keyword

**Aim:** Verify that find displays matching task descriptions with the requested formatting.

**Input:**

```text
todo read book
deadline return book /by 2019-06-06
todo attend class
mark 1
mark 2
find book
bye
```

**Expected output:**

```text
added: [T][ ] read book
added: [D][ ] return book (by: Jun 6 2019)
Nice! I've marked this task as done:
Nice! I've marked this task as done:
    ____________________________________________________________
     Here are the matching tasks in your list:
     1.[T][X] read book
     2.[D][X] return book (by: Jun 6 2019)
    ____________________________________________________________
Bye, User.
```
