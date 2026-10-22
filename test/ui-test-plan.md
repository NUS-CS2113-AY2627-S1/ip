# Neo UI Test Plan

## Test setup

Run all commands from the repository root. The build command uses Java 25 and
the program command starts a new Neo session for each test case. Each session
uses a temporary working directory under `out` with an empty `data/neo.txt`,
so tests start with no tasks and do not change the application's saved data.

### Storage regression checks

Before the UI tests, run these commands with Java 25 from the repository root:

```text
javac -encoding UTF-8 -d out -sourcepath src/main/java test/neo/StorageTest.java
java -cp out neo.StorageTest
```

These checks use temporary files under `out`. They verify that descriptions,
deadlines, and event times containing pipes survive saving and loading, along
with completion states, Unicode, backslashes, line breaks, and empty fields.
They also check loading readable records starting directly with the task type,
manual edits to descriptions, completion states, and dates, and skipping
malformed records while keeping valid tasks. Expect seven corrupted-record
notices followed by `PASS: 4 storage regression checks`.

Latest storage regression result (2026-09-29, Java 25.0.3): **PASS**, all four
checks completed with the seven expected corrupted-record notices.

Task-addition confirmations use seven leading spaces before the task details,
as confirmed by the intended UI format. Events display one time range, such as
`(from: 10am to: 11am)`. Error messages use four leading spaces; invalid-index
errors identify the requested task and list size, and command help includes
`delete` and `find`. End-of-input displays the farewell message once.

### Build command

```text
javac -d out -sourcepath src/main/java src/main/java/neo/Neo.java
```

### Program command

```text
python -c "from pathlib import Path; from tempfile import TemporaryDirectory; import subprocess, sys; classes = Path('out').resolve(); session = TemporaryDirectory(prefix='neo-ui-', dir=classes); data = Path(session.name) / 'data'; data.mkdir(); (data / 'neo.txt').write_text('', encoding='utf-8'); result = subprocess.run(['java', '-cp', str(classes), 'neo.Neo'], cwd=session.name); session.cleanup(); sys.exit(result.returncode)"
```

## Test case: Exit the application

### Aim

Verify that the `bye` command ends a new Neo session with a farewell message.

### Inputs

```text
bye
```

### Expected output

```text
    ____________________________________________________________
 _   _
| \ | | ___  ___
|  \| |/ _ \/ _ \
| |\  |  __/ (_) |
|_| \_|\___|\___/
     Hello! I'm Neo.
     What can I do for you?
    ____________________________________________________________

    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________
```

## Test case: Add and list a todo task

### Aim

Verify that Neo stores a todo task and displays it in the task list.

### Inputs

```text
todo borrow book
list
bye
```

### Expected output

```text
    ____________________________________________________________
 _   _
| \ | | ___  ___
|  \| |/ _ \/ _ \
| |\  |  __/ (_) |
|_| \_|\___|\___/
     Hello! I'm Neo.
     What can I do for you?
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] borrow book
     Now you have 1 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] borrow book
    ____________________________________________________________

    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________
```

## Test case: Manage deadline and event tasks

### Aim

Verify that Neo creates deadline and event tasks, changes task completion,
and preserves the resulting task states in the list.

### Inputs

```text
deadline submit report /by Monday
event team sync /from 10am /to 11am
mark 2
unmark 2
list
bye
```

### Expected output

```text
    ____________________________________________________________
 _   _
| \ | | ___  ___
|  \| |/ _ \/ _ \
| |\  |  __/ (_) |
|_| \_|\___|\___/
     Hello! I'm Neo.
     What can I do for you?
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] submit report (by: Monday)
     Now you have 1 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [E][ ] team sync (from: 10am to: 11am)
     Now you have 2 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Nice! I've marked this task as done:
       [E][X] team sync (from: 10am to: 11am)
    ____________________________________________________________

    ____________________________________________________________
     OK, I've marked this task as not done yet:
       [E][ ] team sync (from: 10am to: 11am)
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[D][ ] submit report (by: Monday)
     2.[E][ ] team sync (from: 10am to: 11am)
    ____________________________________________________________

    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________
```

## Test case: Reject invalid commands without corrupting task state

### Aim

Verify that invalid commands report errors without changing stored tasks,
while valid commands before and after the errors update task state correctly.

### Inputs

```text
todo alpha
mark 2
list
todo
deadline report /by Friday
event planning /from 9am
mark 1
unmark 3
list
bye
```

### Expected output

```text
    ____________________________________________________________
 _   _
| \ | | ___  ___
|  \| |/ _ \/ _ \
| |\  |  __/ (_) |
|_| \_|\___|\___/
     Hello! I'm Neo.
     What can I do for you?
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] alpha
     Now you have 1 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
    Error: Task number 2 does not exist. There are 1 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] alpha
    ____________________________________________________________

    ____________________________________________________________
    Error: Unrecognized command. Try typing: todo <description>, deadline <description> <date>, event <description> <date>, list, mark <index>, unmark <index>, delete <index>, find <keyword> or bye.
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] report (by: Friday)
     Now you have 2 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
    Error: The event end date is missing. Try typing: event <description> /from <start date> /to <end date>
    ____________________________________________________________

    ____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] alpha
    ____________________________________________________________

    ____________________________________________________________
    Error: Task number 3 does not exist. There are 2 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] alpha
     2.[D][ ] report (by: Friday)
    ____________________________________________________________

    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________
```

## Test case: Reject blank event fields and trim valid fields

### Aim

Verify that empty or whitespace-only event fields do not add tasks, and that
a subsequent valid event has trimmed description and times. Preserve the spaces
in the inputs, including the trailing spaces after /to.

### Inputs

```text
event meeting /from  /to 11am
event meeting /from     /to 11am
event meeting /from 9am /to
event /from 9am /to 11am
list
event   planning   /from   9am   /to   10am
list
bye
```

### Expected output

```text
    ____________________________________________________________
 _   _
| \ | | ___  ___
|  \| |/ _ \/ _ \
| |\  |  __/ (_) |
|_| \_|\___|\___/
     Hello! I'm Neo.
     What can I do for you?
    ____________________________________________________________

    ____________________________________________________________
    Error: The event start date is missing. Try typing: event <description> /from <start date> /to <end date>
    ____________________________________________________________

    ____________________________________________________________
    Error: The event start date is missing. Try typing: event <description> /from <start date> /to <end date>
    ____________________________________________________________

    ____________________________________________________________
    Error: The event end date is missing. Try typing: event <description> /from <start date> /to <end date>
    ____________________________________________________________

    ____________________________________________________________
    Error: The event description is missing. Try typing: event <description> /from <start date> /to <end date>
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [E][ ] planning (from: 9am to: 10am)
     Now you have 1 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[E][ ] planning (from: 9am to: 10am)
    ____________________________________________________________

    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________
```

## Test case: Exit cleanly on an empty input stream

### Aim

Verify that immediate EOF produces the welcome and one farewell without an
exception. The input block is deliberately empty.

### Inputs

```text
```

### Expected output

```text
    ____________________________________________________________
 _   _
| \ | | ___  ___
|  \| |/ _ \/ _ \
| |\  |  __/ (_) |
|_| \_|\___|\___/
     Hello! I'm Neo.
     What can I do for you?
    ____________________________________________________________

    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________
```

## Test case: Exit cleanly after commands without bye

### Aim

Verify that commands before EOF are processed and followed by exactly one
farewell, without requiring a bye command.

### Inputs

```text
todo finish report
list
```

### Expected output

```text
    ____________________________________________________________
 _   _
| \ | | ___  ___
|  \| |/ _ \/ _ \
| |\  |  __/ (_) |
|_| \_|\___|\___/
     Hello! I'm Neo.
     What can I do for you?
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] finish report
     Now you have 1 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] finish report
    ____________________________________________________________

    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________
```

## Test case: Handle blank and invalid commands before EOF

### Aim

Verify that a blank line is treated as a command rather than EOF, processing
continues after errors, and the eventual EOF exits cleanly. The first input
line is deliberately blank.

### Inputs

```text

unknown
```

### Expected output

```text
    ____________________________________________________________
 _   _
| \ | | ___  ___
|  \| |/ _ \/ _ \
| |\  |  __/ (_) |
|_| \_|\___|\___/
     Hello! I'm Neo.
     What can I do for you?
    ____________________________________________________________

    ____________________________________________________________
    Error: Unrecognized command. Try typing: todo <description>, deadline <description> <date>, event <description> <date>, list, mark <index>, unmark <index>, delete <index>, find <keyword> or bye.
    ____________________________________________________________

    ____________________________________________________________
    Error: Unrecognized command. Try typing: todo <description>, deadline <description> <date>, event <description> <date>, list, mark <index>, unmark <index>, delete <index>, find <keyword> or bye.
    ____________________________________________________________

    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________
```

## Latest test session

Run started: 2026-09-29T10:53:06+08:00

### Exit the application

**Result:** PASS

**Aim:** Verify that the `bye` command ends a new Neo session with a farewell message.

#### Console input
```text
bye
```

#### Console output
```text
    ____________________________________________________________
 _   _
| \ | | ___  ___
|  \| |/ _ \/ _ \
| |\  |  __/ (_) |
|_| \_|\___|\___/
     Hello! I'm Neo.
     What can I do for you?
    ____________________________________________________________

    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________
```

### Add and list a todo task

**Result:** PASS

**Aim:** Verify that Neo stores a todo task and displays it in the task list.

#### Console input
```text
todo borrow book
list
bye
```

#### Console output
```text
    ____________________________________________________________
 _   _
| \ | | ___  ___
|  \| |/ _ \/ _ \
| |\  |  __/ (_) |
|_| \_|\___|\___/
     Hello! I'm Neo.
     What can I do for you?
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] borrow book
     Now you have 1 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] borrow book
    ____________________________________________________________

    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________
```

### Manage deadline and event tasks

**Result:** PASS

**Aim:** Verify that Neo creates deadline and event tasks, changes task completion,
and preserves the resulting task states in the list.

#### Console input
```text
deadline submit report /by Monday
event team sync /from 10am /to 11am
mark 2
unmark 2
list
bye
```

#### Console output
```text
    ____________________________________________________________
 _   _
| \ | | ___  ___
|  \| |/ _ \/ _ \
| |\  |  __/ (_) |
|_| \_|\___|\___/
     Hello! I'm Neo.
     What can I do for you?
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] submit report (by: Monday)
     Now you have 1 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [E][ ] team sync (from: 10am to: 11am)
     Now you have 2 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Nice! I've marked this task as done:
       [E][X] team sync (from: 10am to: 11am)
    ____________________________________________________________

    ____________________________________________________________
     OK, I've marked this task as not done yet:
       [E][ ] team sync (from: 10am to: 11am)
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[D][ ] submit report (by: Monday)
     2.[E][ ] team sync (from: 10am to: 11am)
    ____________________________________________________________

    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________
```

### Reject invalid commands without corrupting task state

**Result:** PASS

**Aim:** Verify that invalid commands report errors without changing stored tasks,
while valid commands before and after the errors update task state correctly.

#### Console input
```text
todo alpha
mark 2
list
todo
deadline report /by Friday
event planning /from 9am
mark 1
unmark 3
list
bye
```

#### Console output
```text
    ____________________________________________________________
 _   _
| \ | | ___  ___
|  \| |/ _ \/ _ \
| |\  |  __/ (_) |
|_| \_|\___|\___/
     Hello! I'm Neo.
     What can I do for you?
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] alpha
     Now you have 1 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
    Error: Task number 2 does not exist. There are 1 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] alpha
    ____________________________________________________________

    ____________________________________________________________
    Error: Unrecognized command. Try typing: todo <description>, deadline <description> <date>, event <description> <date>, list, mark <index>, unmark <index>, delete <index>, find <keyword> or bye.
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] report (by: Friday)
     Now you have 2 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
    Error: The event end date is missing. Try typing: event <description> /from <start date> /to <end date>
    ____________________________________________________________

    ____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] alpha
    ____________________________________________________________

    ____________________________________________________________
    Error: Task number 3 does not exist. There are 2 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] alpha
     2.[D][ ] report (by: Friday)
    ____________________________________________________________

    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________
```

### Reject blank event fields and trim valid fields

**Result:** PASS

**Aim:** Verify that empty or whitespace-only event fields do not add tasks, and that
a subsequent valid event has trimmed description and times. Preserve the spaces
in the inputs, including the trailing spaces after /to.

#### Console input
```text
event meeting /from  /to 11am
event meeting /from     /to 11am
event meeting /from 9am /to
event /from 9am /to 11am
list
event   planning   /from   9am   /to   10am
list
bye
```

#### Console output
```text
    ____________________________________________________________
 _   _
| \ | | ___  ___
|  \| |/ _ \/ _ \
| |\  |  __/ (_) |
|_| \_|\___|\___/
     Hello! I'm Neo.
     What can I do for you?
    ____________________________________________________________

    ____________________________________________________________
    Error: The event start date is missing. Try typing: event <description> /from <start date> /to <end date>
    ____________________________________________________________

    ____________________________________________________________
    Error: The event start date is missing. Try typing: event <description> /from <start date> /to <end date>
    ____________________________________________________________

    ____________________________________________________________
    Error: The event end date is missing. Try typing: event <description> /from <start date> /to <end date>
    ____________________________________________________________

    ____________________________________________________________
    Error: The event description is missing. Try typing: event <description> /from <start date> /to <end date>
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [E][ ] planning (from: 9am to: 10am)
     Now you have 1 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[E][ ] planning (from: 9am to: 10am)
    ____________________________________________________________

    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________
```

### Exit cleanly on an empty input stream

**Result:** PASS

**Aim:** Verify that immediate EOF produces the welcome and one farewell without an
exception. The input block is deliberately empty.

#### Console input
```text
```

#### Console output
```text
    ____________________________________________________________
 _   _
| \ | | ___  ___
|  \| |/ _ \/ _ \
| |\  |  __/ (_) |
|_| \_|\___|\___/
     Hello! I'm Neo.
     What can I do for you?
    ____________________________________________________________

    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________
```

### Exit cleanly after commands without bye

**Result:** PASS

**Aim:** Verify that commands before EOF are processed and followed by exactly one
farewell, without requiring a bye command.

#### Console input
```text
todo finish report
list
```

#### Console output
```text
    ____________________________________________________________
 _   _
| \ | | ___  ___
|  \| |/ _ \/ _ \
| |\  |  __/ (_) |
|_| \_|\___|\___/
     Hello! I'm Neo.
     What can I do for you?
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] finish report
     Now you have 1 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] finish report
    ____________________________________________________________

    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________
```

### Handle blank and invalid commands before EOF

**Result:** PASS

**Aim:** Verify that a blank line is treated as a command rather than EOF, processing
continues after errors, and the eventual EOF exits cleanly. The first input
line is deliberately blank.

#### Console input
```text

unknown
```

#### Console output
```text
    ____________________________________________________________
 _   _
| \ | | ___  ___
|  \| |/ _ \/ _ \
| |\  |  __/ (_) |
|_| \_|\___|\___/
     Hello! I'm Neo.
     What can I do for you?
    ____________________________________________________________

    ____________________________________________________________
    Error: Unrecognized command. Try typing: todo <description>, deadline <description> <date>, event <description> <date>, list, mark <index>, unmark <index>, delete <index>, find <keyword> or bye.
    ____________________________________________________________

    ____________________________________________________________
    Error: Unrecognized command. Try typing: todo <description>, deadline <description> <date>, event <description> <date>, list, mark <index>, unmark <index>, delete <index>, find <keyword> or bye.
    ____________________________________________________________

    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________
```
