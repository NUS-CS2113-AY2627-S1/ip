# UI Test Plan

This file drives the `test-ui` skill (`.claude/skills/test-ui/`), which
replays these test cases against the compiled `Buddy` program and checks
the real console output against what's recorded here.

**Important — read before adding a test case:**
- All test cases run as **one continuous Buddy session**. Each test case's
  `Input` lines are appended to every earlier test case's input before
  Buddy is re-run, so task state (added tasks, marked/unmarked status)
  carries over from one test case to the next, exactly like a real user
  typing commands one after another.
- A test case's `Expected Output` is only the **new** console output
  produced by that test case's own input — not a repeat of earlier test
  cases' output.
- Exception: Buddy prints a one-time startup banner before it reads any
  input at all. Since Test Case 1 is the very first thing that happens in
  the session, its `Expected Output` must include that banner block too.
  Later test cases never repeat it.

See the `test-ui` skill (`.claude/skills/test-ui/SKILL.md`) for the exact
format template to copy when adding a new test case below.

## Add a todo (and see the startup banner)

**Aim:** Starting Buddy and adding a single todo shows the startup banner followed by an add-confirmation.

**Input:**
```text
todo read book
```

**Expected Output:**
```text
____________________________________________________________
 ____   _   _  ____   ____  __   __
| __ ) | | | ||  _ \ |  _ \ \ \ / /
|  _ \ | | | || | | || | | | \ V / 
| |_) || |_| || |_| || |_| |  | |  
|____/  \___/ |____/ |____/   |_|  

 Hello! I'm Buddy.
 What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
```

## Mark a task as done

**Aim:** Marking task 1 as done updates its status icon and confirms the change.

**Input:**
```text
mark 1
```

**Expected Output:**
```text
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] read book
____________________________________________________________
```

## Add a deadline

**Aim:** Adding a deadline task parses its yyyy-mm-dd HHmm due date/time, displays it reformatted, and reports the new task count.

**Input:**
```text
deadline return book /by 2019-12-02 1800
```

**Expected Output:**
```text
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Dec 02 2019, 6:00 PM)
 Now you have 2 tasks in the list.
____________________________________________________________
```

## Add an event

**Aim:** Adding an event task stores its from/to time span and reports the new task count.

**Input:**
```text
event project meeting /from Mon 2pm /to 4pm
```

**Expected Output:**
```text
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
```

## List all tasks

**Aim:** Listing shows all previously added tasks, in order, with correct type icons and statuses reflecting earlier commands in this session.

**Input:**
```text
list
```

**Expected Output:**
```text
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] read book
 2.[D][ ] return book (by: Dec 02 2019, 6:00 PM)
 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
```

## Find tasks matching a keyword

**Aim:** Finding tasks with a keyword shows only tasks whose description contains it, renumbered from 1.

**Input:**
```text
find book
```

**Expected Output:**
```text
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][X] read book
 2.[D][ ] return book (by: Dec 02 2019, 6:00 PM)
____________________________________________________________
```

## Find with a keyword that matches nothing

**Aim:** Finding with a keyword no task's description contains prints a "not found" message instead of an empty list.

**Input:**
```text
find xyzzy
```

**Expected Output:**
```text
____________________________________________________________
 Buddy couldnt find any tasks with that keyword.
____________________________________________________________
```

## Reject find with no keyword

**Aim:** A `find` command with no keyword throws a BuddyException and prints an error instead of crashing.

**Input:**
```text
find
```

**Expected Output:**
```text
____________________________________________________________
 Buddy you need to provide a keyword to search for.
____________________________________________________________
```

## Reject an empty todo description

**Aim:** A `todo` command with no description throws a BuddyException and prints an error instead of adding a task.

**Input:**
```text
todo
```

**Expected Output:**
```text
____________________________________________________________
 Buddy you cant leave the description of a todo empty.
____________________________________________________________
```

## Reject an empty deadline description

**Aim:** A `deadline` command with no description throws a BuddyException and prints an error instead of adding a task.

**Input:**
```text
deadline
```

**Expected Output:**
```text
____________________________________________________________
 Buddy you cant leave the description of a deadline empty.
____________________________________________________________
```

## Reject a deadline missing '/by'

**Aim:** A `deadline` command with a description but no `/by` throws a BuddyException and prints an error instead of adding a task.

**Input:**
```text
deadline return book
```

**Expected Output:**
```text
____________________________________________________________
 Buddy you need to specify the deadline using '/by'.
____________________________________________________________
```

## Reject a deadline with a badly-formatted date

**Aim:** A `deadline` command whose `/by` date isn't in yyyy-mm-dd HHmm format throws a BuddyException and prints an error instead of adding a task.

**Input:**
```text
deadline return book /by Sunday
```

**Expected Output:**
```text
____________________________________________________________
 Buddy needs the deadline date in yyyy-mm-dd HHmm format, e.g. 2019-12-02 1800, not Sunday.
____________________________________________________________
```

## Reject an empty event description

**Aim:** An `event` command with no description throws a BuddyException and prints an error instead of adding a task.

**Input:**
```text
event
```

**Expected Output:**
```text
____________________________________________________________
 Buddy you cant leave the description of an event empty.
____________________________________________________________
```

## Reject an event missing '/from'

**Aim:** An `event` command with a description but no `/from` throws a BuddyException and prints an error instead of adding a task.

**Input:**
```text
event project meeting
```

**Expected Output:**
```text
____________________________________________________________
 Buddy you need to specify the start of an event using '/from'.
____________________________________________________________
```

## Reject an event missing '/to'

**Aim:** An `event` command with a description and `/from` but no `/to` throws a BuddyException and prints an error instead of adding a task.

**Input:**
```text
event project meeting /from Mon 2pm
```

**Expected Output:**
```text
____________________________________________________________
 Buddy you need to specify the end of an event using '/to'.
____________________________________________________________
```

## Reject mark with no task number

**Aim:** A `mark` command with no task number throws a BuddyException and prints an error instead of crashing.

**Input:**
```text
mark
```

**Expected Output:**
```text
____________________________________________________________
 Buddy you need to provide a task number to mark.
____________________________________________________________
```

## Reject mark with a non-numeric task number

**Aim:** A `mark` command whose task number isn't an integer throws a BuddyException and prints an error instead of crashing.

**Input:**
```text
mark abc
```

**Expected Output:**
```text
____________________________________________________________
 Buddy you need to provide an int for the task number, not abc.
____________________________________________________________
```

## Reject mark with an out-of-range task number

**Aim:** A `mark` command whose task number is beyond the current task list throws a BuddyException and prints an error instead of crashing.

**Input:**
```text
mark 99
```

**Expected Output:**
```text
____________________________________________________________
 Buddy task number 99 does not exist.
____________________________________________________________
```

## Reject mark with a task number below 1

**Aim:** A `mark` command with a task number of 0 or lower throws a BuddyException and prints an error instead of crashing.

**Input:**
```text
mark 0
```

**Expected Output:**
```text
____________________________________________________________
 Buddy task number 0 does not exist.
____________________________________________________________
```

## Unmark a task

**Aim:** Unmarking task 1 clears its status icon and confirms the change.

**Input:**
```text
unmark 1
```

**Expected Output:**
```text
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] read book
____________________________________________________________
```

## Reject unmark with no task number

**Aim:** An `unmark` command with no task number throws a BuddyException and prints an error instead of crashing.

**Input:**
```text
unmark
```

**Expected Output:**
```text
____________________________________________________________
 Buddy you need to provide a task number to unmark.
____________________________________________________________
```

## Reject unmark with a non-numeric task number

**Aim:** An `unmark` command whose task number isn't an integer throws a BuddyException and prints an error instead of crashing.

**Input:**
```text
unmark abc
```

**Expected Output:**
```text
____________________________________________________________
 Buddy you need to provide an int for the task number, not abc.
____________________________________________________________
```

## Reject unmark with an out-of-range task number

**Aim:** An `unmark` command whose task number is beyond the current task list throws a BuddyException and prints an error instead of crashing.

**Input:**
```text
unmark 99
```

**Expected Output:**
```text
____________________________________________________________
 Buddy task number 99 does not exist.
____________________________________________________________
```

## Reject unmark with a task number below 1

**Aim:** An `unmark` command with a task number of 0 or lower throws a BuddyException and prints an error instead of crashing.

**Input:**
```text
unmark 0
```

**Expected Output:**
```text
____________________________________________________________
 Buddy task number 0 does not exist.
____________________________________________________________
```

## Delete a task

**Aim:** Deleting task 2 removes it from the list, shows the removed task, and reports the new task count.

**Input:**
```text
delete 2
```

**Expected Output:**
```text
____________________________________________________________
 Noted. I've removed this task:
   [D][ ] return book (by: Dec 02 2019, 6:00 PM)
 Now you have 2 tasks in the list.
____________________________________________________________
```

## Reject delete with no task number

**Aim:** A `delete` command with no task number throws a BuddyException and prints an error instead of crashing.

**Input:**
```text
delete
```

**Expected Output:**
```text
____________________________________________________________
 Buddy you need to provide a task number to delete.
____________________________________________________________
```

## Reject delete with a non-numeric task number

**Aim:** A `delete` command whose task number isn't an integer throws a BuddyException and prints an error instead of crashing.

**Input:**
```text
delete abc
```

**Expected Output:**
```text
____________________________________________________________
 Buddy you need to provide an int for the task number, not abc.
____________________________________________________________
```

## Reject delete with an out-of-range task number

**Aim:** A `delete` command whose task number is beyond the current task list throws a BuddyException and prints an error instead of crashing.

**Input:**
```text
delete 99
```

**Expected Output:**
```text
____________________________________________________________
 Buddy task number 99 does not exist.
____________________________________________________________
```

## Reject delete with a task number below 1

**Aim:** A `delete` command with a task number of 0 or lower throws a BuddyException and prints an error instead of crashing.

**Input:**
```text
delete 0
```

**Expected Output:**
```text
____________________________________________________________
 Buddy task number 0 does not exist.
____________________________________________________________
```

## Reject an unknown command

**Aim:** A command word Buddy doesn't recognize throws a BuddyException and prints an error instead of being silently added as a task.

**Input:**
```text
blah
```

**Expected Output:**
```text
____________________________________________________________
 Buddy there is no such command.
____________________________________________________________
```

## Exit with bye

**Aim:** The `bye` command prints a goodbye message and ends the session.

**Input:**
```text
bye
```

**Expected Output:**
```text
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```
