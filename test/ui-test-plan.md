# UI Test Plan

This file records console UI test cases for this project. Update it whenever a code change affects commands, console inputs, or expected output.

For A-MoreOOP, run all existing cases unchanged to check that extracting `Ui`, `Parser`, and
`TaskList` preserves command validation, exact messages, saved data, and rollback after failed saves.

Run all cases with Java 25 and Python 3 using `python test/run-ui-tests.py`. The runner compiles the app,
builds the fat JAR with the Gradle wrapper (`.\gradlew.bat --console=plain shadowJar` on Windows,
`sh gradlew --console=plain shadowJar` on macOS/Linux), compares exact output and stderr,
stops at the first failure, and writes `test/ui-test-transcript.md`.
The first build requires internet access to download Gradle and plugin dependencies.

The runner starts each case in a disposable working directory under `_temp/`, using the absolute
path to the repository's `out` directory as the classpath, or the absolute path to
`build/libs/chillguy-all.jar` for JAR cases. This explicitly overrides the default
repository-root working directory so tests cannot overwrite personal `data/chillguy.txt` files.
The displayed command is the equivalent command for a manual run from the repository root.
The runner sets Java stdout/stderr encoding to UTF-8 for reproducible Unicode output.

Each case starts with no data folder unless its storage setup or initial saved tasks specify otherwise.
Cases with the same `Storage session` share only their temporary data directory, each in a new Java process.
`Saved tasks after command N` checks disk contents while the app is still running; `Expected saved tasks`
checks the file after the session. `Expected storage unchanged` compares all storage paths and bytes.
`Storage block` applies a test-owned blocker after command 1 and removes it after the specified restore command.
The runner checks that temporary save files have been cleaned up and deletes its sandboxes when finished.

## Test Case Format

Each test case should include:

- Aim: What behavior the test verifies.
- Command: The command to run from the repository root.
- Input: Console input to provide, or `None`.
- Expected output: The exact expected console output.
- Notes: Setup, cleanup, assumptions, or intentionally ignored output.

## Test Cases

### Run the Fat JAR

- Aim: Verifies that the Shadow JAR launches through its manifest without an external classpath,
  includes the task and storage classes, and supports adding, listing, and saving a task.
- Command: `java -jar build/libs/chillguy-all.jar`
- Input:

```text
todo read book
list
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

- Expected saved tasks:

```text
T | 0 | read book
```

- Notes: The runner builds the JAR first and launches it in an isolated working directory with
  no existing data folder. Expect exit code 0, empty stderr, and no temporary save files.
  For distribution verification, copy only `build/libs/chillguy-all.jar` into an empty temporary
  folder, open the terminal in that folder, and run `java -jar "chillguy-all.jar"` with the same
  input and expected output above. The saved tasks must be created in that folder's
  `data/chillguy.txt`. This explicitly overrides the repository-root working directory.

### Delete Task

- Aim: Verifies that `delete INDEX` removes the selected task and the remaining tasks shift up in the list.
- Command: `java -cp out chillguy.Chillguy`
- Input:

```text
todo read book
todo return book
delete 1
list
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] return book
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [T][ ] read book
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] return book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

- Notes: The runner compiles all Java files recursively under `src/main/java`, including subpackages, before running the cases.

### Delete Mixed Task Types

- Aim: Verifies the Level 6 example removes the middle event, preserves task order and status, and supports deleting the last task and a completed deadline using the updated numbering.
- Command: `java -cp out chillguy.Chillguy`
- Input:

```text
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
todo join sports club
todo borrow book
mark 1
mark 2
mark 4
list
delete 3
list
unmark 3
delete 4
delete 2
list
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: June 6th)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] join sports club
Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] borrow book
Now you have 5 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read book
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] return book (by: June 6th)
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] join sports club
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
2.[D][X] return book (by: June 6th)
3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
4.[T][X] join sports club
5.[T][ ] borrow book
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
2.[D][X] return book (by: June 6th)
3.[T][X] join sports club
4.[T][ ] borrow book
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [T][ ] join sports club
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [T][ ] borrow book
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [D][X] return book (by: June 6th)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
2.[T][ ] join sports club
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

- Notes: The initial list and `delete 3` reproduce the supplied Level 6 example. Every subsequent index refers to the current list.

### Add Level 4 Task Types

- Aim: Verifies that `todo`, `deadline`, and `event` commands create tasks with the correct type labels and details.
- Command: `java -cp out chillguy.Chillguy`
- Input:

```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
mark 1
list
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] borrow book
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] borrow book
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] borrow book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

- Notes: Dates and times are treated as plain strings.

### Reject Malformed Deadline

- Aim: Verifies that a deadline command without `/by` shows an error message instead of crashing.
- Command: `java -cp out chillguy.Chillguy`
- Input:

```text
deadline byebye /today 6pm
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Sorry, deadline tasks need this format: deadline DESCRIPTION /by DATE
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

- Notes: The app should keep running after the error and handle the next command.

### Reject Malformed Event

- Aim: Verifies that an event command without `/to` shows an error message instead of crashing.
- Command: `java -cp out chillguy.Chillguy`
- Input:

```text
event meeting /from Monday 2pm
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

- Notes: The app should keep running after the error and handle the next command.

### Reject Unknown and Empty Commands

- Aim: Verifies errors never create tasks, exact command names are required, extra arguments do not exit, and surrounding spaces and tabs are accepted.
- Command: `java -cp out chillguy.Chillguy`
- Input:

```text

   	  
todo
todo   
blah
read a book
todos book
TODO book
list
  todo	read book  
list extra
bye now
mark	1
blah
  list  
unmark  1 
delete 1
list
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Please enter a command, such as todo DESCRIPTION or list.
____________________________________________________________
____________________________________________________________
Please enter a command, such as todo DESCRIPTION or list.
____________________________________________________________
____________________________________________________________
ERROR: Description of todo cannot be empty.
____________________________________________________________
____________________________________________________________
ERROR: Description of todo cannot be empty.
____________________________________________________________
____________________________________________________________
ERROR: Unknown command.
____________________________________________________________
____________________________________________________________
ERROR: Unknown command.
____________________________________________________________
____________________________________________________________
ERROR: Unknown command.
____________________________________________________________
____________________________________________________________
ERROR: Unknown command.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
The list command takes no extra input. Use: list
____________________________________________________________
____________________________________________________________
The bye command takes no extra input. Use: bye
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read book
____________________________________________________________
____________________________________________________________
ERROR: Unknown command.
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
Noted. I've removed this task:
  [T][ ] read book
Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

- Notes: Literal tabs and spaces in the input are intentional. No errors may alter task count or status.

### Validate Task Numbers and Preserve State

- Aim: Verifies missing, nonnumeric, fractional, multiple, overflowing, zero, negative, and out-of-range indices for all three modifying commands, including empty and shifted lists.
- Command: `java -cp out chillguy.Chillguy`
- Input:

```text
mark
mark 1
unmark
unmark 1
delete
delete 1
todo first
todo second
mark 2
mark abc
mark 1.5
mark 1 2
mark 999999999999999999999
mark -2147483649
mark 0
mark -1
mark -2147483648
mark 3
mark 2147483647
list
unmark abc
unmark 1.5
unmark 1 2
unmark 999999999999999999999
unmark -2147483649
unmark 0
unmark -1
unmark -2147483648
unmark 3
unmark 2147483647
list
delete abc
delete 1.5
delete 1 2
delete 999999999999999999999
delete -2147483649
delete 0
delete -1
delete -2147483648
delete 3
delete 2147483647
list
unmark 2
delete 1
delete 2
mark 1
list
delete 1
delete 1
list
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Please include a task number. Use: mark INDEX
____________________________________________________________
____________________________________________________________
Your task list is empty. Add a task first with todo DESCRIPTION.
____________________________________________________________
____________________________________________________________
Please include a task number. Use: unmark INDEX
____________________________________________________________
____________________________________________________________
Your task list is empty. Add a task first with todo DESCRIPTION.
____________________________________________________________
____________________________________________________________
Please include a task number. Use: delete INDEX
____________________________________________________________
____________________________________________________________
Your task list is empty. Add a task first with todo DESCRIPTION.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] first
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] second
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] second
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: mark INDEX
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: mark INDEX
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: mark INDEX
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: mark INDEX
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: mark INDEX
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] first
2.[T][X] second
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: unmark INDEX
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: unmark INDEX
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: unmark INDEX
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: unmark INDEX
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: unmark INDEX
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] first
2.[T][X] second
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: delete INDEX
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: delete INDEX
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: delete INDEX
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: delete INDEX
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: delete INDEX
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] first
2.[T][X] second
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [T][ ] second
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [T][ ] first
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 1. Use list to see them.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] second
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] second
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [T][X] second
Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
Your task list is empty. Add a task first with todo DESCRIPTION.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

- Notes: List output after each invalid group must preserve both original tasks and their completion status.

### Validate Task Fields and Recover

- Aim: Verifies missing and blank descriptions and times, missing or repeated separators, reversed event separators, and successful recovery with all task types.
- Command: `java -cp out chillguy.Chillguy`
- Input:

```text
todo keep
deadline
deadline   
deadline /by Sunday
deadline task
deadline task /by
deadline task /by   
deadline task /by Sunday /by Monday
deadline task /bySunday
list
deadline	return book	/by	Sunday  
mark 2
event
event   
event /from Monday /to Tuesday
event meeting
event meeting /from Monday
event meeting /to Tuesday
event meeting /from /to Tuesday
event meeting /from Monday /to
event meeting /from   /to Tuesday
event meeting /from Monday /to   
event meeting /to Tuesday /from Monday
event meeting /from Monday /from Tuesday /to Wednesday
event meeting /from Monday /to Tuesday /to Wednesday
event meeting /fromMonday /to Tuesday
list
event	meeting	/from	Mon 2pm	/to	4pm  
unmark 2
list
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] keep
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Sorry, deadline tasks need this format: deadline DESCRIPTION /by DATE
____________________________________________________________
____________________________________________________________
Sorry, deadline tasks need this format: deadline DESCRIPTION /by DATE
____________________________________________________________
____________________________________________________________
Sorry, deadline tasks need this format: deadline DESCRIPTION /by DATE
____________________________________________________________
____________________________________________________________
Sorry, deadline tasks need this format: deadline DESCRIPTION /by DATE
____________________________________________________________
____________________________________________________________
Sorry, deadline tasks need this format: deadline DESCRIPTION /by DATE
____________________________________________________________
____________________________________________________________
Sorry, deadline tasks need this format: deadline DESCRIPTION /by DATE
____________________________________________________________
____________________________________________________________
Sorry, deadline tasks need this format: deadline DESCRIPTION /by DATE
____________________________________________________________
____________________________________________________________
Sorry, deadline tasks need this format: deadline DESCRIPTION /by DATE
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] keep
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] keep
2.[D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [D][ ] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] keep
2.[D][ ] return book (by: Sunday)
3.[E][ ] meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

- Notes: Dates and times remain free-form strings. Each required separator must be a separate token and occur exactly once.

### Grow Task List Beyond 100 Tasks

- Aim: Verifies all task types can be added beyond 100 tasks, deletion renumbers a large list, and marking, unmarking, and adding still work after deletion.
- Command: `java -cp out chillguy.Chillguy`
- Input file: `test/fixtures/capacity.input.txt`
- Expected output file: `test/fixtures/capacity.expected.txt`

- Notes: Fixture files contain the complete literal session and exact output; no output is omitted from comparison.

### End of Input After Error

- Aim: Verifies the app exits cleanly when input ends after an error without a bye command.
- Command: `java -cp out chillguy.Chillguy`
- Input:

```text
todo
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
ERROR: Description of todo cannot be empty.
____________________________________________________________
```

- Notes: The process receives EOF after the last input line; no goodbye message is expected.

### End of Input Without Commands

- Aim: Verifies EOF at startup prints only the greeting and exits cleanly.
- Command: `java -cp out chillguy.Chillguy`
- Input:

```text
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
```

- Notes: Input is empty; no command or goodbye output is expected.

### Save Every Task Type Immediately

- Aim: Verifies folder creation and autosaving after every addition and status change, including Unicode, pipes, and backslashes.
- Command: `java -cp out chillguy.Chillguy`
- Storage session: `persistence`

- Input:

```text
todo read | book C:\notes\新书
deadline return book /by June | 6th
event planning /from Aug 6th 2pm /to 4pm \ UTC
mark 1
mark 2
mark 3
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read | book C:\notes\新书
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: June | 6th)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] planning (from: Aug 6th 2pm to: 4pm \ UTC)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read | book C:\notes\新书
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] return book (by: June | 6th)
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [E][X] planning (from: Aug 6th 2pm to: 4pm \ UTC)
____________________________________________________________
```

- Expected saved tasks:

```text
T | 1 | read \| book C:\\notes\\新书
D | 1 | return book | June \| 6th
E | 1 | planning | Aug 6th 2pm | 4pm \\ UTC
```

- Saved tasks after command 1:

```text
T | 0 | read \| book C:\\notes\\新书
```

- Saved tasks after command 2:

```text
T | 0 | read \| book C:\\notes\\新书
D | 0 | return book | June \| 6th
```

- Saved tasks after command 3:

```text
T | 0 | read \| book C:\\notes\\新书
D | 0 | return book | June \| 6th
E | 0 | planning | Aug 6th 2pm | 4pm \\ UTC
```

- Saved tasks after command 4:

```text
T | 1 | read \| book C:\\notes\\新书
D | 0 | return book | June \| 6th
E | 0 | planning | Aug 6th 2pm | 4pm \\ UTC
```

- Saved tasks after command 5:

```text
T | 1 | read \| book C:\\notes\\新书
D | 1 | return book | June \| 6th
E | 0 | planning | Aug 6th 2pm | 4pm \\ UTC
```

- Saved tasks after command 6:

```text
T | 1 | read \| book C:\\notes\\新书
D | 1 | return book | June \| 6th
E | 1 | planning | Aug 6th 2pm | 4pm \\ UTC
```

- Notes: Starts without a data folder. Disk checkpoints run after command responses while Java is still running. Ends at EOF without bye. The next five cases reuse this data directory in fresh Java processes.

### Reload Tasks and Unmark

- Aim: Verifies restoration of all task fields and statuses after restart, and immediate persistence of unmark.
- Command: `java -cp out chillguy.Chillguy`
- Storage session: `persistence`

- Input:

```text
list
unmark 1
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read | book C:\notes\新书
2.[D][X] return book (by: June | 6th)
3.[E][X] planning (from: Aug 6th 2pm to: 4pm \ UTC)
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [T][ ] read | book C:\notes\新书
____________________________________________________________
```

- Expected saved tasks:

```text
T | 0 | read \| book C:\\notes\\新书
D | 1 | return book | June \| 6th
E | 1 | planning | Aug 6th 2pm | 4pm \\ UTC
```

- Saved tasks after command 2:

```text
T | 0 | read \| book C:\\notes\\新书
D | 1 | return book | June \| 6th
E | 1 | planning | Aug 6th 2pm | 4pm \\ UTC
```

- Notes: Continues the persistence storage session; ends at EOF.

### Reload Unmarked Status and Mark

- Aim: Verifies the previous unmark survived restart, then saves a new mark.
- Command: `java -cp out chillguy.Chillguy`
- Storage session: `persistence`

- Input:

```text
list
mark 1
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read | book C:\notes\新书
2.[D][X] return book (by: June | 6th)
3.[E][X] planning (from: Aug 6th 2pm to: 4pm \ UTC)
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read | book C:\notes\新书
____________________________________________________________
```

- Expected saved tasks:

```text
T | 1 | read \| book C:\\notes\\新书
D | 1 | return book | June \| 6th
E | 1 | planning | Aug 6th 2pm | 4pm \\ UTC
```

- Saved tasks after command 2:

```text
T | 1 | read \| book C:\\notes\\新书
D | 1 | return book | June \| 6th
E | 1 | planning | Aug 6th 2pm | 4pm \\ UTC
```

- Notes: Continues the persistence storage session; ends at EOF.

### Reload and Delete the Middle Task

- Aim: Verifies deletion is immediately saved and preserves the other tasks and their order.
- Command: `java -cp out chillguy.Chillguy`
- Storage session: `persistence`

- Input:

```text
list
delete 2
list
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read | book C:\notes\新书
2.[D][X] return book (by: June | 6th)
3.[E][X] planning (from: Aug 6th 2pm to: 4pm \ UTC)
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [D][X] return book (by: June | 6th)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read | book C:\notes\新书
2.[E][X] planning (from: Aug 6th 2pm to: 4pm \ UTC)
____________________________________________________________
```

- Expected saved tasks:

```text
T | 1 | read \| book C:\\notes\\新书
E | 1 | planning | Aug 6th 2pm | 4pm \\ UTC
```

- Saved tasks after command 2:

```text
T | 1 | read \| book C:\\notes\\新书
E | 1 | planning | Aug 6th 2pm | 4pm \\ UTC
```

- Notes: Continues the persistence storage session; ends at EOF.

### Reload Deletion and Save an Empty List

- Aim: Verifies deletion survived restart and deleting the last task empties the saved file.
- Command: `java -cp out chillguy.Chillguy`
- Storage session: `persistence`

- Input:

```text
list
delete 2
delete 1
list
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read | book C:\notes\新书
2.[E][X] planning (from: Aug 6th 2pm to: 4pm \ UTC)
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [E][X] planning (from: Aug 6th 2pm to: 4pm \ UTC)
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [T][X] read | book C:\notes\新书
Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
```

- Expected saved tasks:

```text
```

- Saved tasks after command 2:

```text
T | 1 | read \| book C:\\notes\\新书
```

- Saved tasks after command 3:

```text
```

- Notes: Continues the persistence storage session; ends at EOF.

### Reload an Empty Saved List

- Aim: Verifies an empty saved file loads successfully and read-only commands leave it unchanged.
- Command: `java -cp out chillguy.Chillguy`
- Storage session: `persistence`
- Expected storage unchanged: `yes`

- Input:

```text
list
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

- Expected saved tasks:

```text
```

- Notes: Final case in the persistence storage session.

### Create a Missing File in an Existing Folder

- Aim: Verifies the first save creates a missing data file when the folder already exists.
- Command: `java -cp out chillguy.Chillguy`
- Storage setup: `missing-file`

- Input:

```text
todo first task
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] first task
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

- Expected saved tasks:

```text
T | 0 | first task
```

- Saved tasks after command 1:

```text
T | 0 | first task
```

- Notes: Uses a fresh isolated working directory. Exit code must be zero and stderr empty.

### First Run Without Changes

- Aim: Verifies read-only and invalid commands do not create a data folder or file.
- Command: `java -cp out chillguy.Chillguy`
- Expected storage unchanged: `yes`

- Input:

```text
list
unknown
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
ERROR: Unknown command.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

- Notes: Uses a fresh isolated working directory. Exit code must be zero and stderr empty.

### Load Editor Formatting Without Rewriting

- Aim: Verifies blank lines, a UTF-8 BOM, surrounding field spaces, and no final newline load without rewriting the file.
- Command: `java -cp out chillguy.Chillguy`
- Expected storage unchanged: `yes`

- Initial saved tasks file: `test/fixtures/storage-editor.txt`

- Input:

```text
list
mark 1
unmark 2
unknown
delete 9
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
2.[D][ ] return book (by: June 6th)
3.[E][X] meeting (from: 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read book
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [D][ ] return book (by: June 6th)
____________________________________________________________
____________________________________________________________
ERROR: Unknown command.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 3. Use list to see them.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

- Expected saved tasks file: `test/fixtures/storage-editor.txt`

- Notes: The fixture has a UTF-8 BOM and no final newline. The runner writes the exact fixture bytes. No-op marks/unmarks and invalid commands must not rewrite storage.

### Reject Saved Unknown Task Type

- Aim: Verifies an invalid record on line 2 stops startup without changing any existing data.
- Command: `java -cp out chillguy.Chillguy`
- Expected storage unchanged: `yes`

- Initial saved tasks:

```text
T | 0 | keep before
Q | 0 | invalid
T | 1 | keep after
```

- Input:

```text
todo must not overwrite
list
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, data/chillguy.txt has invalid task data on line 2. Fix the file and restart Chillguy.
Your saved file has not been changed.
____________________________________________________________
```

- Expected saved tasks:

```text
T | 0 | keep before
Q | 0 | invalid
T | 1 | keep after
```

- Notes: The app must stop before processing input; it must not load or save a partial list.

### Reject Saved Invalid Completion Status

- Aim: Verifies an invalid record on line 2 stops startup without changing any existing data.
- Command: `java -cp out chillguy.Chillguy`
- Expected storage unchanged: `yes`

- Initial saved tasks:

```text
T | 0 | keep before
T | 2 | invalid
T | 1 | keep after
```

- Input:

```text
todo must not overwrite
list
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, data/chillguy.txt has invalid task data on line 2. Fix the file and restart Chillguy.
Your saved file has not been changed.
____________________________________________________________
```

- Expected saved tasks:

```text
T | 0 | keep before
T | 2 | invalid
T | 1 | keep after
```

- Notes: The app must stop before processing input; it must not load or save a partial list.

### Reject Saved Missing Description

- Aim: Verifies an invalid record on line 2 stops startup without changing any existing data.
- Command: `java -cp out chillguy.Chillguy`
- Expected storage unchanged: `yes`

- Initial saved tasks:

```text
T | 0 | keep before
T | 0 |
T | 1 | keep after
```

- Input:

```text
todo must not overwrite
list
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, data/chillguy.txt has invalid task data on line 2. Fix the file and restart Chillguy.
Your saved file has not been changed.
____________________________________________________________
```

- Expected saved tasks:

```text
T | 0 | keep before
T | 0 |
T | 1 | keep after
```

- Notes: The app must stop before processing input; it must not load or save a partial list.

### Reject Saved Missing Deadline Date

- Aim: Verifies an invalid record on line 2 stops startup without changing any existing data.
- Command: `java -cp out chillguy.Chillguy`
- Expected storage unchanged: `yes`

- Initial saved tasks:

```text
T | 0 | keep before
D | 0 | return book
T | 1 | keep after
```

- Input:

```text
todo must not overwrite
list
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, data/chillguy.txt has invalid task data on line 2. Fix the file and restart Chillguy.
Your saved file has not been changed.
____________________________________________________________
```

- Expected saved tasks:

```text
T | 0 | keep before
D | 0 | return book
T | 1 | keep after
```

- Notes: The app must stop before processing input; it must not load or save a partial list.

### Reject Saved Empty Deadline Date

- Aim: Verifies an invalid record on line 2 stops startup without changing any existing data.
- Command: `java -cp out chillguy.Chillguy`
- Expected storage unchanged: `yes`

- Initial saved tasks:

```text
T | 0 | keep before
D | 0 | return book |
T | 1 | keep after
```

- Input:

```text
todo must not overwrite
list
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, data/chillguy.txt has invalid task data on line 2. Fix the file and restart Chillguy.
Your saved file has not been changed.
____________________________________________________________
```

- Expected saved tasks:

```text
T | 0 | keep before
D | 0 | return book |
T | 1 | keep after
```

- Notes: The app must stop before processing input; it must not load or save a partial list.

### Reject Saved Missing Event End

- Aim: Verifies an invalid record on line 2 stops startup without changing any existing data.
- Command: `java -cp out chillguy.Chillguy`
- Expected storage unchanged: `yes`

- Initial saved tasks:

```text
T | 0 | keep before
E | 0 | meeting | 2pm
T | 1 | keep after
```

- Input:

```text
todo must not overwrite
list
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, data/chillguy.txt has invalid task data on line 2. Fix the file and restart Chillguy.
Your saved file has not been changed.
____________________________________________________________
```

- Expected saved tasks:

```text
T | 0 | keep before
E | 0 | meeting | 2pm
T | 1 | keep after
```

- Notes: The app must stop before processing input; it must not load or save a partial list.

### Reject Saved Empty Event Start

- Aim: Verifies an invalid record on line 2 stops startup without changing any existing data.
- Command: `java -cp out chillguy.Chillguy`
- Expected storage unchanged: `yes`

- Initial saved tasks:

```text
T | 0 | keep before
E | 0 | meeting | | 4pm
T | 1 | keep after
```

- Input:

```text
todo must not overwrite
list
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, data/chillguy.txt has invalid task data on line 2. Fix the file and restart Chillguy.
Your saved file has not been changed.
____________________________________________________________
```

- Expected saved tasks:

```text
T | 0 | keep before
E | 0 | meeting | | 4pm
T | 1 | keep after
```

- Notes: The app must stop before processing input; it must not load or save a partial list.

### Reject Saved Empty Event End

- Aim: Verifies an invalid record on line 2 stops startup without changing any existing data.
- Command: `java -cp out chillguy.Chillguy`
- Expected storage unchanged: `yes`

- Initial saved tasks:

```text
T | 0 | keep before
E | 0 | meeting | 2pm |
T | 1 | keep after
```

- Input:

```text
todo must not overwrite
list
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, data/chillguy.txt has invalid task data on line 2. Fix the file and restart Chillguy.
Your saved file has not been changed.
____________________________________________________________
```

- Expected saved tasks:

```text
T | 0 | keep before
E | 0 | meeting | 2pm |
T | 1 | keep after
```

- Notes: The app must stop before processing input; it must not load or save a partial list.

### Reject Saved Extra Fields

- Aim: Verifies an invalid record on line 2 stops startup without changing any existing data.
- Command: `java -cp out chillguy.Chillguy`
- Expected storage unchanged: `yes`

- Initial saved tasks:

```text
T | 0 | keep before
T | 0 | read book | extra
T | 1 | keep after
```

- Input:

```text
todo must not overwrite
list
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, data/chillguy.txt has invalid task data on line 2. Fix the file and restart Chillguy.
Your saved file has not been changed.
____________________________________________________________
```

- Expected saved tasks:

```text
T | 0 | keep before
T | 0 | read book | extra
T | 1 | keep after
```

- Notes: The app must stop before processing input; it must not load or save a partial list.

### Reject Saved Unknown Escape

- Aim: Verifies an invalid record on line 2 stops startup without changing any existing data.
- Command: `java -cp out chillguy.Chillguy`
- Expected storage unchanged: `yes`

- Initial saved tasks:

```text
T | 0 | keep before
T | 0 | bad\q
T | 1 | keep after
```

- Input:

```text
todo must not overwrite
list
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, data/chillguy.txt has invalid task data on line 2. Fix the file and restart Chillguy.
Your saved file has not been changed.
____________________________________________________________
```

- Expected saved tasks:

```text
T | 0 | keep before
T | 0 | bad\q
T | 1 | keep after
```

- Notes: The app must stop before processing input; it must not load or save a partial list.

### Reject Saved Trailing Backslash

- Aim: Verifies an invalid record on line 2 stops startup without changing any existing data.
- Command: `java -cp out chillguy.Chillguy`
- Expected storage unchanged: `yes`

- Initial saved tasks:

```text
T | 0 | keep before
T | 0 | bad\
T | 1 | keep after
```

- Input:

```text
todo must not overwrite
list
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, data/chillguy.txt has invalid task data on line 2. Fix the file and restart Chillguy.
Your saved file has not been changed.
____________________________________________________________
```

- Expected saved tasks:

```text
T | 0 | keep before
T | 0 | bad\
T | 1 | keep after
```

- Notes: The app must stop before processing input; it must not load or save a partial list.

### Reject Saved Truncated Record

- Aim: Verifies an invalid record on line 2 stops startup without changing any existing data.
- Command: `java -cp out chillguy.Chillguy`
- Expected storage unchanged: `yes`

- Initial saved tasks:

```text
T | 0 | keep before
T
T | 1 | keep after
```

- Input:

```text
todo must not overwrite
list
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, data/chillguy.txt has invalid task data on line 2. Fix the file and restart Chillguy.
Your saved file has not been changed.
____________________________________________________________
```

- Expected saved tasks:

```text
T | 0 | keep before
T
T | 1 | keep after
```

- Notes: The app must stop before processing input; it must not load or save a partial list.

### Handle Invalid UTF-8

- Aim: Verifies an unreadable storage path produces a friendly error and leaves existing storage untouched.
- Command: `java -cp out chillguy.Chillguy`
- Storage setup: `invalid-utf8`
- Expected storage unchanged: `yes`

- Input:

```text
todo must not overwrite
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, I couldn't load tasks from data/chillguy.txt. Check that the path is a readable UTF-8 file.
Your saved file has not been changed.
____________________________________________________________
```

- Notes: Uses a fresh isolated working directory. Exit code must be zero and stderr empty.

### Handle Directory at the Data File Path

- Aim: Verifies an unreadable storage path produces a friendly error and leaves existing storage untouched.
- Command: `java -cp out chillguy.Chillguy`
- Storage setup: `file-is-directory`
- Expected storage unchanged: `yes`

- Input:

```text
todo must not overwrite
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, I couldn't load tasks from data/chillguy.txt. Check that the path is a readable UTF-8 file.
Your saved file has not been changed.
____________________________________________________________
```

- Notes: Uses a fresh isolated working directory. Exit code must be zero and stderr empty.

### Handle File at the Data Folder Path

- Aim: Verifies an unreadable storage path produces a friendly error and leaves existing storage untouched.
- Command: `java -cp out chillguy.Chillguy`
- Storage setup: `parent-is-file`
- Expected storage unchanged: `yes`

- Input:

```text
todo must not overwrite
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, I couldn't load tasks from data/chillguy.txt. Check that the path is a readable UTF-8 file.
Your saved file has not been changed.
____________________________________________________________
```

- Notes: Uses a fresh isolated working directory. Exit code must be zero and stderr empty.

### Recover from Folder Creation Failure

- Aim: Verifies a failed save adds no task, prints no success message, and allows retry after the folder problem is fixed.
- Command: `java -cp out chillguy.Chillguy`
- Storage block: `folder`
- Restore storage after command: `3`

- Input:

```text
list
todo retry me
list
todo retry me
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Sorry, I couldn't save tasks to data/chillguy.txt. No changes were made. Check that the data folder is writable and the file is not in use.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] retry me
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

- Expected saved tasks:

```text
T | 0 | retry me
```

- Saved tasks after command 4:

```text
T | 0 | retry me
```

- Notes: After command 1 completes, create a file named data. Remove that blocker after command 3 completes. All paths are inside the disposable test sandbox.

### Undo Every Kind of Failed Task Change

- Aim: Verifies failed additions, marks, unmarks, and deletion preserve task order/status and the previous saved bytes.
- Command: `java -cp out chillguy.Chillguy`
- Storage block: `file`
- Restore storage after command: `8`

- Initial saved tasks:

```text
T | 1 | keep todo
D | 0 | keep deadline | Friday
E | 0 | keep event | 2pm | 4pm
```

- Input:

```text
list
todo rejected
deadline rejected /by Friday
event rejected /from 1 /to 2
mark 2
unmark 1
delete 2
list
mark 2
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] keep todo
2.[D][ ] keep deadline (by: Friday)
3.[E][ ] keep event (from: 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Sorry, I couldn't save tasks to data/chillguy.txt. No changes were made. Check that the data folder is writable and the file is not in use.
____________________________________________________________
____________________________________________________________
Sorry, I couldn't save tasks to data/chillguy.txt. No changes were made. Check that the data folder is writable and the file is not in use.
____________________________________________________________
____________________________________________________________
Sorry, I couldn't save tasks to data/chillguy.txt. No changes were made. Check that the data folder is writable and the file is not in use.
____________________________________________________________
____________________________________________________________
Sorry, I couldn't save tasks to data/chillguy.txt. No changes were made. Check that the data folder is writable and the file is not in use.
____________________________________________________________
____________________________________________________________
Sorry, I couldn't save tasks to data/chillguy.txt. No changes were made. Check that the data folder is writable and the file is not in use.
____________________________________________________________
____________________________________________________________
Sorry, I couldn't save tasks to data/chillguy.txt. No changes were made. Check that the data folder is writable and the file is not in use.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] keep todo
2.[D][ ] keep deadline (by: Friday)
3.[E][ ] keep event (from: 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] keep deadline (by: Friday)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

- Expected saved tasks:

```text
T | 1 | keep todo
D | 1 | keep deadline | Friday
E | 0 | keep event | 2pm | 4pm
```

- Saved tasks after command 8:

```text
T | 1 | keep todo
D | 0 | keep deadline | Friday
E | 0 | keep event | 2pm | 4pm
```

- Saved tasks after command 9:

```text
T | 1 | keep todo
D | 1 | keep deadline | Friday
E | 0 | keep event | 2pm | 4pm
```

- Notes: After command 1, temporarily rename the original file and put a nonempty directory at data/chillguy.txt to force replacement failure. Restore the original immediately after command 8, verify its bytes, then retry mark 2. No temporary save files may remain.

### Find Descriptions Across Task Types

- Aim: Checks loaded tasks, mixed completion states, result numbering, substring and phrase matching, case sensitivity, literal punctuation, and description-only searches. Searching preserves storage bytes and task order.
- Command: `java -jar build/libs/chillguy-all.jar`
- Initial saved tasks:

```text
T | 0 | buy milk
T | 1 | read book
D | 1 | return book | June 6th
E | 0 | book club | Monday | Tuesday
T | 0 | Book review
T | 0 | notebook
```

- Expected storage unchanged: `yes`

- Input:

```text
find book
  find   read book  
find Book
find June
find Monday
find [X]
find .*
list
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the matching tasks in your list:
1.[T][X] read book
2.[D][X] return book (by: June 6th)
3.[E][ ] book club (from: Monday to: Tuesday)
4.[T][ ] notebook
____________________________________________________________
____________________________________________________________
Here are the matching tasks in your list:
1.[T][X] read book
____________________________________________________________
____________________________________________________________
Here are the matching tasks in your list:
1.[T][ ] Book review
____________________________________________________________
____________________________________________________________
No matching tasks found.
____________________________________________________________
____________________________________________________________
No matching tasks found.
____________________________________________________________
____________________________________________________________
No matching tasks found.
____________________________________________________________
____________________________________________________________
No matching tasks found.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] buy milk
2.[T][X] read book
3.[D][X] return book (by: June 6th)
4.[E][ ] book club (from: Monday to: Tuesday)
5.[T][ ] Book review
6.[T][ ] notebook
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

- Notes: Uses the isolated storage and cleanup rules above.

### Find Empty List and Reject Missing Keyword

- Aim: Checks empty results, missing and whitespace-only keywords, and recovery without creating storage.
- Command: `java -cp out chillguy.Chillguy`
- Expected storage unchanged: `yes`

- Input:

```text
find book
find
find   
find book
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
No matching tasks found.
____________________________________________________________
____________________________________________________________
Please include a search keyword. Use: find KEYWORD
____________________________________________________________
____________________________________________________________
Please include a search keyword. Use: find KEYWORD
____________________________________________________________
____________________________________________________________
No matching tasks found.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

- Notes: Uses the isolated storage and cleanup rules above.

### Find Reflects Task Changes

- Aim: Checks that searches reflect additions, status changes, and deletions in the current session.
- Command: `java -cp out chillguy.Chillguy`
- Expected saved tasks:

```text
```

- Input:

```text
todo read book
find book
mark 1
find book
unmark 1
find book
delete 1
find book
bye
```

- Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Here are the matching tasks in your list:
1.[T][ ] read book
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read book
____________________________________________________________
____________________________________________________________
Here are the matching tasks in your list:
1.[T][X] read book
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [T][ ] read book
____________________________________________________________
____________________________________________________________
Here are the matching tasks in your list:
1.[T][ ] read book
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [T][ ] read book
Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
No matching tasks found.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

- Notes: Uses the isolated storage and cleanup rules above.
