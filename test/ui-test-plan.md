# UI Test Plan

This file is the source of truth for command-driven console UI tests. Run the
cases in document order and stop at the first mismatch.

## Test environment

- Required JDK: Java 25
- Build command (PowerShell):

  ```powershell
  javac -d out (Get-ChildItem -Recurse src/main/java -Filter *.java).FullName
  ```

- Run command:

  ```powershell
  java -cp out bond.Bond
  ```

- Session isolation: Start a fresh process for every test case.
- Data isolation: Run each case in a clean temporary working directory so that
  `./data/bond.txt` starts absent, except when a case specifies initial data.
- Output comparison: Compare exactly, except that CRLF and LF line endings are
  equivalent and terminal-generated input echo is ignored. Spaces, blank lines,
  case, punctuation, and program-generated output remain significant.
- Data-file comparison: When a case specifies expected file contents, read
  `data/bond.txt` immediately after the paired response. Treat CRLF and LF as
  equivalent and require the file to end with a line ending.

## Expected startup output

Every fresh process must first print:

```text
    ____________________________________________________________
    ____                  __
   / __ )____  ____  ____/ /
  / __  / __ \/ __ \/ __  /
 / /_/ / /_/ / / / / /_/ /
/_____/\____/_/ /_/\__,_/
    Good day! I'm Bond, James Bond.
    Agent 007 at your service, what can I do for you?
    ____________________________________________________________
```

## UI-1: Exit immediately

**Aim:** Verify that `bye` ends a new session with the farewell message.

### Input 1

```text
bye
```

### Expected output 1

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

## UI-2: Add and list a to-do

**Aim:** Verify that a to-do is stored, counted, and displayed as not done.

### Input 1

```text
todo read book
```

### Expected output 1

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] read book
    Now you have 1 mission in the list.
    ____________________________________________________________
```

### Input 2

```text
list
```

### Expected output 2

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][ ] read book
    ____________________________________________________________
```

### Input 3

```text
bye
```

### Expected output 3

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

## UI-3: Mark and unmark a to-do

**Aim:** Verify that completion status can be set and cleared within one session.

### Input 1

```text
todo submit report
```

### Expected output 1

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] submit report
    Now you have 1 mission in the list.
    ____________________________________________________________
```

### Input 2

```text
mark 1
```

### Expected output 2

```text
    ____________________________________________________________
    Nice work, agent! Another mission accomplished!:
      [T][X] submit report
    ____________________________________________________________
```

### Input 3

```text
unmark 1
```

### Expected output 3

```text
    ____________________________________________________________
    OK, I've marked this mission as not accomplished yet:
      [T][ ] submit report
    ____________________________________________________________
```

### Input 4

```text
bye
```

### Expected output 4

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

## UI-4: Reject unknown commands without storing them

**Aim:** Verify that invalid commands between valid operations produce helpful
errors, do not add tasks, and do not prevent later commands from running.

### Input 1

```text
todo secure files
```

### Expected output 1

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] secure files
    Now you have 1 mission in the list.
    ____________________________________________________________
```

### Input 2

```text
blah
```

### Expected output 2

```text
    ____________________________________________________________
    Mission error: I don't recognize that command.
    Try: todo, deadline, event, find, list, mark, unmark, delete, or bye.
    ____________________________________________________________
```

### Input 3

```text
list
```

### Expected output 3

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][ ] secure files
    ____________________________________________________________
```

### Input 4

```text
todo retrieve intel
```

### Expected output 4

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] retrieve intel
    Now you have 2 missions in the list.
    ____________________________________________________________
```

### Input 5

```text
list now
```

### Expected output 5

```text
    ____________________________________________________________
    Mission error: I don't recognize that command.
    Try: todo, deadline, event, find, list, mark, unmark, delete, or bye.
    ____________________________________________________________
```

### Input 6

```text
list
```

### Expected output 6

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][ ] secure files
    2.[T][ ] retrieve intel
    ____________________________________________________________
```

### Input 7

```text
bye
```

### Expected output 7

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

## UI-5: Reject empty and case-mismatched commands

**Aim:** Verify that edge-case commands do not change tasks and that Bond
continues to accept valid commands after each error.

### Input 1

```text
todo inspect equipment
```

### Expected output 1

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] inspect equipment
    Now you have 1 mission in the list.
    ____________________________________________________________
```

### Input 2 (empty line)

```text

```

### Expected output 2

```text
    ____________________________________________________________
    Mission error: I don't recognize that command.
    Try: todo, deadline, event, find, list, mark, unmark, delete, or bye.
    ____________________________________________________________
```

### Input 3

```text
list
```

### Expected output 3

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][ ] inspect equipment
    ____________________________________________________________
```

### Input 4

```text
LIST
```

### Expected output 4

```text
    ____________________________________________________________
    Mission error: I don't recognize that command.
    Try: todo, deadline, event, find, list, mark, unmark, delete, or bye.
    ____________________________________________________________
```

### Input 5

```text
list
```

### Expected output 5

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][ ] inspect equipment
    ____________________________________________________________
```

### Input 6 (one leading space)

```text
 list
```

### Expected output 6

```text
    ____________________________________________________________
    Mission error: I don't recognize that command.
    Try: todo, deadline, event, find, list, mark, unmark, delete, or bye.
    ____________________________________________________________
```

### Input 7

```text
list
```

### Expected output 7

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][ ] inspect equipment
    ____________________________________________________________
```

### Input 8

```text
bye
```

### Expected output 8

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

## UI-6: Reject a todo without a description

**Aim:** Verify that a missing todo description does not add a task and that
valid todo commands before and after the error remain correct.

### Input 1

```text
todo survey perimeter
```

### Expected output 1

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] survey perimeter
    Now you have 1 mission in the list.
    ____________________________________________________________
```

### Input 2

```text
todo
```

### Expected output 2

```text
    ____________________________________________________________
    Mission error: This todo mission has no description.
    Brief me with: todo <description>.
    ____________________________________________________________
```

### Input 3

```text
list
```

### Expected output 3

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][ ] survey perimeter
    ____________________________________________________________
```

### Input 4

```text
todo prepare escape route
```

### Expected output 4

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] prepare escape route
    Now you have 2 missions in the list.
    ____________________________________________________________
```

### Input 5

```text
list
```

### Expected output 5

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][ ] survey perimeter
    2.[T][ ] prepare escape route
    ____________________________________________________________
```

### Input 6

```text
bye
```

### Expected output 6

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

## UI-7: Validate deadline mission details

**Aim:** Verify that each required deadline component is reported specifically,
invalid deadlines are not stored, and valid deadlines remain correct.

### Input 1

```text
deadline file report /by 2019-10-15
```

### Expected output 1

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [D][ ] file report (by: Oct 15 2019)
    Now you have 1 mission in the list.
    ____________________________________________________________
```

### Input 2

```text
deadline
```

### Expected output 2

```text
    ____________________________________________________________
    Mission error: This deadline mission has no description.
    Brief me with: deadline <description> /by <yyyy-MM-dd>.
    ____________________________________________________________
```

### Input 3

```text
list
```

### Expected output 3

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[D][ ] file report (by: Oct 15 2019)
    ____________________________________________________________
```

### Input 4

```text
deadline secure documents
```

### Expected output 4

```text
    ____________________________________________________________
    Mission error: This deadline mission is missing its /by marker.
    Brief me with: deadline <description> /by <yyyy-MM-dd>.
    ____________________________________________________________
```

### Input 5

```text
deadline secure documents /by
```

### Expected output 5

```text
    ____________________________________________________________
    Mission error: This deadline mission has no date.
    Brief me with: deadline <description> /by <yyyy-MM-dd>.
    ____________________________________________________________
```

### Input 6

```text
deadline /by 2019-10-21
```

### Expected output 6

```text
    ____________________________________________________________
    Mission error: This deadline mission has no description.
    Brief me with: deadline <description> /by <yyyy-MM-dd>.
    ____________________________________________________________
```

### Input 7

```text
deadline contact M /by 2019-10-21
```

### Expected output 7

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [D][ ] contact M (by: Oct 21 2019)
    Now you have 2 missions in the list.
    ____________________________________________________________
```

### Input 8

```text
list
```

### Expected output 8

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[D][ ] file report (by: Oct 15 2019)
    2.[D][ ] contact M (by: Oct 21 2019)
    ____________________________________________________________
```

### Input 9

```text
deadline check report /by 2019-02-29
```

### Expected output 9

```text
    ____________________________________________________________
    Mission error: This deadline mission needs a valid date in yyyy-MM-dd format.
    Brief me with: deadline <description> /by <yyyy-MM-dd>.
    ____________________________________________________________
```

### Input 10

```text
list
```

### Expected output 10

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[D][ ] file report (by: Oct 15 2019)
    2.[D][ ] contact M (by: Oct 21 2019)
    ____________________________________________________________
```

### Input 11

```text
bye
```

### Expected output 11

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

## UI-8: Validate event mission details

**Aim:** Verify that each required event component is reported specifically,
invalid events are not stored, and valid commands still update state correctly.

### Input 1

```text
event gala /from 8pm /to 10pm
```

### Expected output 1

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [E][ ] gala (from: 8pm to: 10pm)
    Now you have 1 mission in the list.
    ____________________________________________________________
```

### Input 2

```text
event
```

### Expected output 2

```text
    ____________________________________________________________
    Mission error: This event mission has no description.
    Brief me with: event <description> /from <start> /to <end>.
    ____________________________________________________________
```

### Input 3

```text
list
```

### Expected output 3

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[E][ ] gala (from: 8pm to: 10pm)
    ____________________________________________________________
```

### Input 4

```text
event surveillance
```

### Expected output 4

```text
    ____________________________________________________________
    Mission error: This event mission is missing its /from marker.
    Brief me with: event <description> /from <start> /to <end>.
    ____________________________________________________________
```

### Input 5

```text
event surveillance /from
```

### Expected output 5

```text
    ____________________________________________________________
    Mission error: This event mission has no starting date or time.
    Brief me with: event <description> /from <start> /to <end>.
    ____________________________________________________________
```

### Input 6

```text
event surveillance /from 8pm
```

### Expected output 6

```text
    ____________________________________________________________
    Mission error: This event mission is missing its /to marker.
    Brief me with: event <description> /from <start> /to <end>.
    ____________________________________________________________
```

### Input 7

```text
event surveillance /from /to 10pm
```

### Expected output 7

```text
    ____________________________________________________________
    Mission error: This event mission has no starting date or time.
    Brief me with: event <description> /from <start> /to <end>.
    ____________________________________________________________
```

### Input 8

```text
event surveillance /from 8pm /to
```

### Expected output 8

```text
    ____________________________________________________________
    Mission error: This event mission has no ending date or time.
    Brief me with: event <description> /from <start> /to <end>.
    ____________________________________________________________
```

### Input 9

```text
event /from 8pm /to 10pm
```

### Expected output 9

```text
    ____________________________________________________________
    Mission error: This event mission has no description.
    Brief me with: event <description> /from <start> /to <end>.
    ____________________________________________________________
```

### Input 10

```text
todo file findings
```

### Expected output 10

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] file findings
    Now you have 2 missions in the list.
    ____________________________________________________________
```

### Input 11

```text
list
```

### Expected output 11

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[E][ ] gala (from: 8pm to: 10pm)
    2.[T][ ] file findings
    ____________________________________________________________
```

### Input 12

```text
bye
```

### Expected output 12

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

## UI-9: Validate mark mission numbers

**Aim:** Verify that malformed and unavailable mark selections leave mission
state unchanged and that a later valid selection still succeeds.

### Input 1

```text
mark 1
```

### Expected output 1

```text
    ____________________________________________________________
    Mission error: The mission dossier is empty.
    Add a mission before trying to mark it.
    ____________________________________________________________
```

### Input 2

```text
todo inspect safe house
```

### Expected output 2

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] inspect safe house
    Now you have 1 mission in the list.
    ____________________________________________________________
```

### Input 3

```text
mark
```

### Expected output 3

```text
    ____________________________________________________________
    Mission error: I need a mission number for that mark order.
    Brief me with: mark <mission number>.
    ____________________________________________________________
```

### Input 4

```text
list
```

### Expected output 4

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][ ] inspect safe house
    ____________________________________________________________
```

### Input 5

```text
mark one
```

### Expected output 5

```text
    ____________________________________________________________
    Mission error: That mission number is not a valid whole number.
    Brief me with: mark <mission number>.
    ____________________________________________________________
```

### Input 6

```text
mark 0
```

### Expected output 6

```text
    ____________________________________________________________
    Mission error: Mission numbers start at 1, agent.
    Brief me with: mark <mission number>.
    ____________________________________________________________
```

### Input 7

```text
mark -3
```

### Expected output 7

```text
    ____________________________________________________________
    Mission error: Mission numbers start at 1, agent.
    Brief me with: mark <mission number>.
    ____________________________________________________________
```

### Input 8

```text
mark 2
```

### Expected output 8

```text
    ____________________________________________________________
    Mission error: Mission 2 is not in the dossier.
    Choose mission number 1.
    ____________________________________________________________
```

### Input 9

```text
mark 999999999999999999999999
```

### Expected output 9

```text
    ____________________________________________________________
    Mission error: That mission number is not a valid whole number.
    Brief me with: mark <mission number>.
    ____________________________________________________________
```

### Input 10

```text
mark 1
```

### Expected output 10

```text
    ____________________________________________________________
    Nice work, agent! Another mission accomplished!:
      [T][X] inspect safe house
    ____________________________________________________________
```

### Input 11

```text
list
```

### Expected output 11

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][X] inspect safe house
    ____________________________________________________________
```

### Input 12

```text
bye
```

### Expected output 12

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

## UI-10: Validate unmark mission numbers

**Aim:** Verify that malformed and unavailable unmark selections preserve a
completed mission and that a later valid selection can still clear its status.

### Input 1

```text
todo decode message
```

### Expected output 1

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] decode message
    Now you have 1 mission in the list.
    ____________________________________________________________
```

### Input 2

```text
mark 1
```

### Expected output 2

```text
    ____________________________________________________________
    Nice work, agent! Another mission accomplished!:
      [T][X] decode message
    ____________________________________________________________
```

### Input 3

```text
unmark
```

### Expected output 3

```text
    ____________________________________________________________
    Mission error: I need a mission number for that unmark order.
    Brief me with: unmark <mission number>.
    ____________________________________________________________
```

### Input 4

```text
list
```

### Expected output 4

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][X] decode message
    ____________________________________________________________
```

### Input 5

```text
unmark 1.5
```

### Expected output 5

```text
    ____________________________________________________________
    Mission error: That mission number is not a valid whole number.
    Brief me with: unmark <mission number>.
    ____________________________________________________________
```

### Input 6

```text
unmark 0
```

### Expected output 6

```text
    ____________________________________________________________
    Mission error: Mission numbers start at 1, agent.
    Brief me with: unmark <mission number>.
    ____________________________________________________________
```

### Input 7

```text
unmark 2
```

### Expected output 7

```text
    ____________________________________________________________
    Mission error: Mission 2 is not in the dossier.
    Choose mission number 1.
    ____________________________________________________________
```

### Input 8

```text
unmark 1 2
```

### Expected output 8

```text
    ____________________________________________________________
    Mission error: That mission number is not a valid whole number.
    Brief me with: unmark <mission number>.
    ____________________________________________________________
```

### Input 9

```text
unmark 1
```

### Expected output 9

```text
    ____________________________________________________________
    OK, I've marked this mission as not accomplished yet:
      [T][ ] decode message
    ____________________________________________________________
```

### Input 10

```text
list
```

### Expected output 10

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][ ] decode message
    ____________________________________________________________
```

### Input 11

```text
bye
```

### Expected output 11

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```


## UI-11: Store more than 100 missions

**Aim:** Verify that the mission collection grows beyond 100 entries and that
valid mark and unmark operations continue to work across that boundary.

### Input 1

```text
todo mission 1
```

### Expected output 1

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 1
    Now you have 1 mission in the list.
    ____________________________________________________________
```

### Input 2

```text
todo mission 2
```

### Expected output 2

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 2
    Now you have 2 missions in the list.
    ____________________________________________________________
```

### Input 3

```text
todo mission 3
```

### Expected output 3

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 3
    Now you have 3 missions in the list.
    ____________________________________________________________
```

### Input 4

```text
todo mission 4
```

### Expected output 4

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 4
    Now you have 4 missions in the list.
    ____________________________________________________________
```

### Input 5

```text
todo mission 5
```

### Expected output 5

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 5
    Now you have 5 missions in the list.
    ____________________________________________________________
```

### Input 6

```text
todo mission 6
```

### Expected output 6

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 6
    Now you have 6 missions in the list.
    ____________________________________________________________
```

### Input 7

```text
todo mission 7
```

### Expected output 7

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 7
    Now you have 7 missions in the list.
    ____________________________________________________________
```

### Input 8

```text
todo mission 8
```

### Expected output 8

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 8
    Now you have 8 missions in the list.
    ____________________________________________________________
```

### Input 9

```text
todo mission 9
```

### Expected output 9

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 9
    Now you have 9 missions in the list.
    ____________________________________________________________
```

### Input 10

```text
todo mission 10
```

### Expected output 10

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 10
    Now you have 10 missions in the list.
    ____________________________________________________________
```

### Input 11

```text
todo mission 11
```

### Expected output 11

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 11
    Now you have 11 missions in the list.
    ____________________________________________________________
```

### Input 12

```text
todo mission 12
```

### Expected output 12

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 12
    Now you have 12 missions in the list.
    ____________________________________________________________
```

### Input 13

```text
todo mission 13
```

### Expected output 13

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 13
    Now you have 13 missions in the list.
    ____________________________________________________________
```

### Input 14

```text
todo mission 14
```

### Expected output 14

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 14
    Now you have 14 missions in the list.
    ____________________________________________________________
```

### Input 15

```text
todo mission 15
```

### Expected output 15

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 15
    Now you have 15 missions in the list.
    ____________________________________________________________
```

### Input 16

```text
todo mission 16
```

### Expected output 16

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 16
    Now you have 16 missions in the list.
    ____________________________________________________________
```

### Input 17

```text
todo mission 17
```

### Expected output 17

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 17
    Now you have 17 missions in the list.
    ____________________________________________________________
```

### Input 18

```text
todo mission 18
```

### Expected output 18

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 18
    Now you have 18 missions in the list.
    ____________________________________________________________
```

### Input 19

```text
todo mission 19
```

### Expected output 19

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 19
    Now you have 19 missions in the list.
    ____________________________________________________________
```

### Input 20

```text
todo mission 20
```

### Expected output 20

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 20
    Now you have 20 missions in the list.
    ____________________________________________________________
```

### Input 21

```text
todo mission 21
```

### Expected output 21

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 21
    Now you have 21 missions in the list.
    ____________________________________________________________
```

### Input 22

```text
todo mission 22
```

### Expected output 22

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 22
    Now you have 22 missions in the list.
    ____________________________________________________________
```

### Input 23

```text
todo mission 23
```

### Expected output 23

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 23
    Now you have 23 missions in the list.
    ____________________________________________________________
```

### Input 24

```text
todo mission 24
```

### Expected output 24

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 24
    Now you have 24 missions in the list.
    ____________________________________________________________
```

### Input 25

```text
todo mission 25
```

### Expected output 25

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 25
    Now you have 25 missions in the list.
    ____________________________________________________________
```

### Input 26

```text
todo mission 26
```

### Expected output 26

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 26
    Now you have 26 missions in the list.
    ____________________________________________________________
```

### Input 27

```text
todo mission 27
```

### Expected output 27

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 27
    Now you have 27 missions in the list.
    ____________________________________________________________
```

### Input 28

```text
todo mission 28
```

### Expected output 28

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 28
    Now you have 28 missions in the list.
    ____________________________________________________________
```

### Input 29

```text
todo mission 29
```

### Expected output 29

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 29
    Now you have 29 missions in the list.
    ____________________________________________________________
```

### Input 30

```text
todo mission 30
```

### Expected output 30

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 30
    Now you have 30 missions in the list.
    ____________________________________________________________
```

### Input 31

```text
todo mission 31
```

### Expected output 31

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 31
    Now you have 31 missions in the list.
    ____________________________________________________________
```

### Input 32

```text
todo mission 32
```

### Expected output 32

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 32
    Now you have 32 missions in the list.
    ____________________________________________________________
```

### Input 33

```text
todo mission 33
```

### Expected output 33

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 33
    Now you have 33 missions in the list.
    ____________________________________________________________
```

### Input 34

```text
todo mission 34
```

### Expected output 34

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 34
    Now you have 34 missions in the list.
    ____________________________________________________________
```

### Input 35

```text
todo mission 35
```

### Expected output 35

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 35
    Now you have 35 missions in the list.
    ____________________________________________________________
```

### Input 36

```text
todo mission 36
```

### Expected output 36

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 36
    Now you have 36 missions in the list.
    ____________________________________________________________
```

### Input 37

```text
todo mission 37
```

### Expected output 37

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 37
    Now you have 37 missions in the list.
    ____________________________________________________________
```

### Input 38

```text
todo mission 38
```

### Expected output 38

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 38
    Now you have 38 missions in the list.
    ____________________________________________________________
```

### Input 39

```text
todo mission 39
```

### Expected output 39

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 39
    Now you have 39 missions in the list.
    ____________________________________________________________
```

### Input 40

```text
todo mission 40
```

### Expected output 40

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 40
    Now you have 40 missions in the list.
    ____________________________________________________________
```

### Input 41

```text
todo mission 41
```

### Expected output 41

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 41
    Now you have 41 missions in the list.
    ____________________________________________________________
```

### Input 42

```text
todo mission 42
```

### Expected output 42

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 42
    Now you have 42 missions in the list.
    ____________________________________________________________
```

### Input 43

```text
todo mission 43
```

### Expected output 43

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 43
    Now you have 43 missions in the list.
    ____________________________________________________________
```

### Input 44

```text
todo mission 44
```

### Expected output 44

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 44
    Now you have 44 missions in the list.
    ____________________________________________________________
```

### Input 45

```text
todo mission 45
```

### Expected output 45

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 45
    Now you have 45 missions in the list.
    ____________________________________________________________
```

### Input 46

```text
todo mission 46
```

### Expected output 46

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 46
    Now you have 46 missions in the list.
    ____________________________________________________________
```

### Input 47

```text
todo mission 47
```

### Expected output 47

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 47
    Now you have 47 missions in the list.
    ____________________________________________________________
```

### Input 48

```text
todo mission 48
```

### Expected output 48

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 48
    Now you have 48 missions in the list.
    ____________________________________________________________
```

### Input 49

```text
todo mission 49
```

### Expected output 49

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 49
    Now you have 49 missions in the list.
    ____________________________________________________________
```

### Input 50

```text
todo mission 50
```

### Expected output 50

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 50
    Now you have 50 missions in the list.
    ____________________________________________________________
```

### Input 51

```text
todo mission 51
```

### Expected output 51

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 51
    Now you have 51 missions in the list.
    ____________________________________________________________
```

### Input 52

```text
todo mission 52
```

### Expected output 52

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 52
    Now you have 52 missions in the list.
    ____________________________________________________________
```

### Input 53

```text
todo mission 53
```

### Expected output 53

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 53
    Now you have 53 missions in the list.
    ____________________________________________________________
```

### Input 54

```text
todo mission 54
```

### Expected output 54

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 54
    Now you have 54 missions in the list.
    ____________________________________________________________
```

### Input 55

```text
todo mission 55
```

### Expected output 55

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 55
    Now you have 55 missions in the list.
    ____________________________________________________________
```

### Input 56

```text
todo mission 56
```

### Expected output 56

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 56
    Now you have 56 missions in the list.
    ____________________________________________________________
```

### Input 57

```text
todo mission 57
```

### Expected output 57

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 57
    Now you have 57 missions in the list.
    ____________________________________________________________
```

### Input 58

```text
todo mission 58
```

### Expected output 58

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 58
    Now you have 58 missions in the list.
    ____________________________________________________________
```

### Input 59

```text
todo mission 59
```

### Expected output 59

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 59
    Now you have 59 missions in the list.
    ____________________________________________________________
```

### Input 60

```text
todo mission 60
```

### Expected output 60

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 60
    Now you have 60 missions in the list.
    ____________________________________________________________
```

### Input 61

```text
todo mission 61
```

### Expected output 61

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 61
    Now you have 61 missions in the list.
    ____________________________________________________________
```

### Input 62

```text
todo mission 62
```

### Expected output 62

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 62
    Now you have 62 missions in the list.
    ____________________________________________________________
```

### Input 63

```text
todo mission 63
```

### Expected output 63

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 63
    Now you have 63 missions in the list.
    ____________________________________________________________
```

### Input 64

```text
todo mission 64
```

### Expected output 64

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 64
    Now you have 64 missions in the list.
    ____________________________________________________________
```

### Input 65

```text
todo mission 65
```

### Expected output 65

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 65
    Now you have 65 missions in the list.
    ____________________________________________________________
```

### Input 66

```text
todo mission 66
```

### Expected output 66

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 66
    Now you have 66 missions in the list.
    ____________________________________________________________
```

### Input 67

```text
todo mission 67
```

### Expected output 67

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 67
    Now you have 67 missions in the list.
    ____________________________________________________________
```

### Input 68

```text
todo mission 68
```

### Expected output 68

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 68
    Now you have 68 missions in the list.
    ____________________________________________________________
```

### Input 69

```text
todo mission 69
```

### Expected output 69

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 69
    Now you have 69 missions in the list.
    ____________________________________________________________
```

### Input 70

```text
todo mission 70
```

### Expected output 70

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 70
    Now you have 70 missions in the list.
    ____________________________________________________________
```

### Input 71

```text
todo mission 71
```

### Expected output 71

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 71
    Now you have 71 missions in the list.
    ____________________________________________________________
```

### Input 72

```text
todo mission 72
```

### Expected output 72

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 72
    Now you have 72 missions in the list.
    ____________________________________________________________
```

### Input 73

```text
todo mission 73
```

### Expected output 73

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 73
    Now you have 73 missions in the list.
    ____________________________________________________________
```

### Input 74

```text
todo mission 74
```

### Expected output 74

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 74
    Now you have 74 missions in the list.
    ____________________________________________________________
```

### Input 75

```text
todo mission 75
```

### Expected output 75

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 75
    Now you have 75 missions in the list.
    ____________________________________________________________
```

### Input 76

```text
todo mission 76
```

### Expected output 76

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 76
    Now you have 76 missions in the list.
    ____________________________________________________________
```

### Input 77

```text
todo mission 77
```

### Expected output 77

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 77
    Now you have 77 missions in the list.
    ____________________________________________________________
```

### Input 78

```text
todo mission 78
```

### Expected output 78

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 78
    Now you have 78 missions in the list.
    ____________________________________________________________
```

### Input 79

```text
todo mission 79
```

### Expected output 79

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 79
    Now you have 79 missions in the list.
    ____________________________________________________________
```

### Input 80

```text
todo mission 80
```

### Expected output 80

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 80
    Now you have 80 missions in the list.
    ____________________________________________________________
```

### Input 81

```text
todo mission 81
```

### Expected output 81

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 81
    Now you have 81 missions in the list.
    ____________________________________________________________
```

### Input 82

```text
todo mission 82
```

### Expected output 82

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 82
    Now you have 82 missions in the list.
    ____________________________________________________________
```

### Input 83

```text
todo mission 83
```

### Expected output 83

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 83
    Now you have 83 missions in the list.
    ____________________________________________________________
```

### Input 84

```text
todo mission 84
```

### Expected output 84

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 84
    Now you have 84 missions in the list.
    ____________________________________________________________
```

### Input 85

```text
todo mission 85
```

### Expected output 85

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 85
    Now you have 85 missions in the list.
    ____________________________________________________________
```

### Input 86

```text
todo mission 86
```

### Expected output 86

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 86
    Now you have 86 missions in the list.
    ____________________________________________________________
```

### Input 87

```text
todo mission 87
```

### Expected output 87

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 87
    Now you have 87 missions in the list.
    ____________________________________________________________
```

### Input 88

```text
todo mission 88
```

### Expected output 88

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 88
    Now you have 88 missions in the list.
    ____________________________________________________________
```

### Input 89

```text
todo mission 89
```

### Expected output 89

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 89
    Now you have 89 missions in the list.
    ____________________________________________________________
```

### Input 90

```text
todo mission 90
```

### Expected output 90

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 90
    Now you have 90 missions in the list.
    ____________________________________________________________
```

### Input 91

```text
todo mission 91
```

### Expected output 91

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 91
    Now you have 91 missions in the list.
    ____________________________________________________________
```

### Input 92

```text
todo mission 92
```

### Expected output 92

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 92
    Now you have 92 missions in the list.
    ____________________________________________________________
```

### Input 93

```text
todo mission 93
```

### Expected output 93

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 93
    Now you have 93 missions in the list.
    ____________________________________________________________
```

### Input 94

```text
todo mission 94
```

### Expected output 94

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 94
    Now you have 94 missions in the list.
    ____________________________________________________________
```

### Input 95

```text
todo mission 95
```

### Expected output 95

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 95
    Now you have 95 missions in the list.
    ____________________________________________________________
```

### Input 96

```text
todo mission 96
```

### Expected output 96

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 96
    Now you have 96 missions in the list.
    ____________________________________________________________
```

### Input 97

```text
todo mission 97
```

### Expected output 97

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 97
    Now you have 97 missions in the list.
    ____________________________________________________________
```

### Input 98

```text
todo mission 98
```

### Expected output 98

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 98
    Now you have 98 missions in the list.
    ____________________________________________________________
```

### Input 99

```text
todo mission 99
```

### Expected output 99

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 99
    Now you have 99 missions in the list.
    ____________________________________________________________
```

### Input 100

```text
todo
```

### Expected output 100

```text
    ____________________________________________________________
    Mission error: This todo mission has no description.
    Brief me with: todo <description>.
    ____________________________________________________________
```

### Input 101

```text
todo mission 100
```

### Expected output 101

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] mission 100
    Now you have 100 missions in the list.
    ____________________________________________________________
```

### Input 102

```text
todo overflow mission
```

### Expected output 102

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] overflow mission
    Now you have 101 missions in the list.
    ____________________________________________________________
```

### Input 103

```text
mark 100
```

### Expected output 103

```text
    ____________________________________________________________
    Nice work, agent! Another mission accomplished!:
      [T][X] mission 100
    ____________________________________________________________
```

### Input 104

```text
mark 101
```

### Expected output 104

```text
    ____________________________________________________________
    Nice work, agent! Another mission accomplished!:
      [T][X] overflow mission
    ____________________________________________________________
```

### Input 105

```text
unmark 100
```

### Expected output 105

```text
    ____________________________________________________________
    OK, I've marked this mission as not accomplished yet:
      [T][ ] mission 100
    ____________________________________________________________
```

### Input 106

```text
bye
```

### Expected output 106

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

## UI-12: Save every task-list change

**Aim:** Verify that additions and completion-status changes rewrite the data
file immediately using the storage format for each task type.

### Input 1

```text
todo read book
```

### Expected output 1

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] read book
    Now you have 1 mission in the list.
    ____________________________________________________________
```

### Expected data file 1

```text
T | 0 | read book
```

### Input 2

```text
deadline return book /by 2019-06-06
```

### Expected output 2

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [D][ ] return book (by: Jun 06 2019)
    Now you have 2 missions in the list.
    ____________________________________________________________
```

### Expected data file 2

```text
T | 0 | read book
D | 0 | return book | 2019-06-06
```

### Input 3

```text
event project meeting /from Aug 6th 2pm /to 4pm
```

### Expected output 3

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
    Now you have 3 missions in the list.
    ____________________________________________________________
```

### Expected data file 3

```text
T | 0 | read book
D | 0 | return book | 2019-06-06
E | 0 | project meeting | Aug 6th 2pm | 4pm
```

### Input 4

```text
mark 2
```

### Expected output 4

```text
    ____________________________________________________________
    Nice work, agent! Another mission accomplished!:
      [D][X] return book (by: Jun 06 2019)
    ____________________________________________________________
```

### Expected data file 4

```text
T | 0 | read book
D | 1 | return book | 2019-06-06
E | 0 | project meeting | Aug 6th 2pm | 4pm
```

### Input 5

```text
unmark 2
```

### Expected output 5

```text
    ____________________________________________________________
    OK, I've marked this mission as not accomplished yet:
      [D][ ] return book (by: Jun 06 2019)
    ____________________________________________________________
```

### Expected data file 5

```text
T | 0 | read book
D | 0 | return book | 2019-06-06
E | 0 | project meeting | Aug 6th 2pm | 4pm
```

### Input 6

```text
bye
```

### Expected output 6

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

### Expected data file 6

```text
T | 0 | read book
D | 0 | return book | 2019-06-06
E | 0 | project meeting | Aug 6th 2pm | 4pm
```

## UI-13: Load saved tasks at startup

**Aim:** Verify that Bond restores every task type and its completion status
from the data file when a new process starts.

Run both sessions in the same otherwise-clean temporary working directory so
that the second process reads the file written by the first process.

### Session 1, input 1

```text
todo read book
```

### Session 1, expected output 1

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] read book
    Now you have 1 mission in the list.
    ____________________________________________________________
```

### Session 1, input 2

```text
deadline return book /by 2019-06-06
```

### Session 1, expected output 2

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [D][ ] return book (by: Jun 06 2019)
    Now you have 2 missions in the list.
    ____________________________________________________________
```

### Session 1, input 3

```text
event project meeting /from Aug 6th 2pm /to 4pm
```

### Session 1, expected output 3

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
    Now you have 3 missions in the list.
    ____________________________________________________________
```

### Session 1, input 4

```text
mark 1
```

### Session 1, expected output 4

```text
    ____________________________________________________________
    Nice work, agent! Another mission accomplished!:
      [T][X] read book
    ____________________________________________________________
```

### Session 1, input 5

```text
mark 3
```

### Session 1, expected output 5

```text
    ____________________________________________________________
    Nice work, agent! Another mission accomplished!:
      [E][X] project meeting (from: Aug 6th 2pm to: 4pm)
    ____________________________________________________________
```

### Session 1, input 6

```text
bye
```

### Session 1, expected output 6

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

### Session 2, input 1

```text
list
```

### Session 2, expected output 1

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][X] read book
    2.[D][ ] return book (by: Jun 06 2019)
    3.[E][X] project meeting (from: Aug 6th 2pm to: 4pm)
    ____________________________________________________________
```

### Session 2, input 2

```text
bye
```

### Session 2, expected output 2

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

## UI-14: Preserve storage-format characters in task fields

**Aim:** Verify that pipes and backslashes in user-entered task fields survive
saving and loading without being mistaken for storage delimiters.

Run both sessions in the same otherwise-clean temporary working directory.

### Session 1, input 1

```text
todo inspect A | B \ archive
```

### Session 1, expected output 1

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] inspect A | B \ archive
    Now you have 1 mission in the list.
    ____________________________________________________________
```

### Expected data file 1

```text
T | 0 | inspect A \| B \\ archive
```

### Session 1, input 2

```text
bye
```

### Session 1, expected output 2

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

### Session 2, input 1

```text
list
```

### Session 2, expected output 1

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][ ] inspect A | B \ archive
    ____________________________________________________________
```

### Session 2, input 2

```text
bye
```

### Session 2, expected output 2

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

## UI-15: Reject malformed mission archives

**Aim:** Verify that each malformed storage record produces a specific recovery
message instead of a Java exception or partially loaded mission list.

For each subcase, copy the named fixture to `data/bond.txt` in a separate clean
temporary working directory, launch Bond, and provide no console input. Bond
must print the shared startup output, print the indicated response, and exit.

### Subcase A initial data

Copy `test/fixtures/storage/invalid-status.txt`.

### Subcase A input

None; Bond exits after detecting the startup error.

### Subcase A expected output after startup

```text
    Mission error: Mission archive line 1 is invalid: the completion status must be 0 or 1.
    Repair or remove ./data/bond.txt, then restart Bond.
    ____________________________________________________________
```

### Subcase B initial data

Copy `test/fixtures/storage/unknown-type.txt`.

### Subcase B input

None; Bond exits after detecting the startup error.

### Subcase B expected output after startup

```text
    Mission error: Mission archive line 1 is invalid: the task type 'Z' is unknown.
    Repair or remove ./data/bond.txt, then restart Bond.
    ____________________________________________________________
```

### Subcase C initial data

Copy `test/fixtures/storage/wrong-field-count.txt`.

### Subcase C input

None; Bond exits after detecting the startup error.

### Subcase C expected output after startup

```text
    Mission error: Mission archive line 1 is invalid: the number of fields does not match the task type.
    Repair or remove ./data/bond.txt, then restart Bond.
    ____________________________________________________________
```

### Subcase D initial data

Copy `test/fixtures/storage/extra-field.txt`.

### Subcase D input

None; Bond exits after detecting the startup error.

### Subcase D expected output after startup

```text
    Mission error: Mission archive line 1 is invalid: the number of fields does not match the task type.
    Repair or remove ./data/bond.txt, then restart Bond.
    ____________________________________________________________
```

### Subcase E initial data

Copy `test/fixtures/storage/empty-field.txt`.

### Subcase E input

None; Bond exits after detecting the startup error.

### Subcase E expected output after startup

```text
    Mission error: Mission archive line 1 is invalid: a required task field is empty.
    Repair or remove ./data/bond.txt, then restart Bond.
    ____________________________________________________________
```

### Subcase F initial data

Copy `test/fixtures/storage/blank-line.txt`.

### Subcase F input

None; Bond exits after detecting the startup error.

### Subcase F expected output after startup

```text
    Mission error: Mission archive line 2 is invalid: the line is empty.
    Repair or remove ./data/bond.txt, then restart Bond.
    ____________________________________________________________
```

### Subcase G initial data

Copy `test/fixtures/storage/invalid-deadline-date.txt`.

### Subcase G input

None; Bond exits after detecting the startup error.

### Subcase G expected output after startup

```text
    Mission error: Mission archive line 1 is invalid: the deadline date must be a valid yyyy-MM-dd date.
    Repair or remove ./data/bond.txt, then restart Bond.
    ____________________________________________________________
```

## UI-16: Reject an invalid archive path

**Aim:** Verify that a directory at the configured data-file path produces a
clear read error instead of a Java exception.

Before launch, create the directory `data/bond.txt` in an otherwise-clean
temporary working directory.

### Input

None; Bond exits after detecting the startup error.

### Expected output after startup

```text
    Mission error: I couldn't read the mission archive.
    Check that ./data/bond.txt is a readable file, then restart Bond.
    ____________________________________________________________
```

## UI-17: Roll back a mission when saving fails

**Aim:** Verify that a failed write produces a recoverable error and does not
leave the unsaved mission in memory.

Start Bond in a clean temporary working directory. After the shared startup
output appears, create the directory `data/bond.txt` before sending input 1.

### Input 1

```text
todo secure archive
```

### Expected output 1

```text
    ____________________________________________________________
    Mission error: I couldn't save the mission archive.
    Check that ./data/bond.txt is writable, then try again.
    ____________________________________________________________
```

### Input 2

```text
list
```

### Expected output 2

```text
    ____________________________________________________________
    Here are the missions in your list:
    ____________________________________________________________
```

### Input 3

```text
bye
```

### Expected output 3

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

## UI-18: Roll back status changes when saving fails

**Aim:** Verify that failed mark and unmark writes restore the completion states
that were loaded from disk.

Copy `test/fixtures/storage/rollback-statuses.txt` to `data/bond.txt`, then
launch Bond. After the shared startup output appears, move the data file aside
and create the directory `data/bond.txt` before sending input 1.

### Input 1

```text
mark 1
```

### Expected output 1

```text
    ____________________________________________________________
    Mission error: I couldn't save the mission archive.
    Check that ./data/bond.txt is writable, then try again.
    ____________________________________________________________
```

### Input 2

```text
list
```

### Expected output 2

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][ ] inspect archive
    2.[T][X] secure archive
    ____________________________________________________________
```

### Input 3

```text
unmark 2
```

### Expected output 3

```text
    ____________________________________________________________
    Mission error: I couldn't save the mission archive.
    Check that ./data/bond.txt is writable, then try again.
    ____________________________________________________________
```

### Input 4

```text
list
```

### Expected output 4

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][ ] inspect archive
    2.[T][X] secure archive
    ____________________________________________________________
```

### Input 5

```text
bye
```

### Expected output 5

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

## UI-19: Persist a deleted mission

**Aim:** Verify that deleting a mission rewrites the data file, closes the gap
in the numbered list, and remains deleted after Bond restarts.

Run both sessions in the same otherwise-clean temporary working directory.

### Session 1, input 1

```text
todo read book
```

### Session 1, expected output 1

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] read book
    Now you have 1 mission in the list.
    ____________________________________________________________
```

### Session 1, expected data file 1

```text
T | 0 | read book
```

### Session 1, input 2

```text
deadline return book /by 2019-06-06
```

### Session 1, expected output 2

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [D][ ] return book (by: Jun 06 2019)
    Now you have 2 missions in the list.
    ____________________________________________________________
```

### Session 1, expected data file 2

```text
T | 0 | read book
D | 0 | return book | 2019-06-06
```

### Session 1, input 3

```text
delete 1
```

### Session 1, expected output 3

```text
    ____________________________________________________________
    Noted. I've removed this mission:
      [T][ ] read book
    Now you have 1 mission in the list.
    ____________________________________________________________
```

### Session 1, expected data file 3

```text
D | 0 | return book | 2019-06-06
```

### Session 1, input 4

```text
list
```

### Session 1, expected output 4

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[D][ ] return book (by: Jun 06 2019)
    ____________________________________________________________
```

### Session 1, input 5

```text
bye
```

### Session 1, expected output 5

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

### Session 2, input 1

```text
list
```

### Session 2, expected output 1

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[D][ ] return book (by: Jun 06 2019)
    ____________________________________________________________
```

### Session 2, input 2

```text
bye
```

### Session 2, expected output 2

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

## UI-20: Roll back a deletion when saving fails

**Aim:** Verify that a failed delete write restores the removed mission at its
original position and reports a recoverable storage error.

Copy `test/fixtures/storage/rollback-statuses.txt` to `data/bond.txt`, then
launch Bond. After the shared startup output appears, move the data file aside
and create the directory `data/bond.txt` before sending input 1.

### Input 1

```text
delete 1
```

### Expected output 1

```text
    ____________________________________________________________
    Mission error: I couldn't save the mission archive.
    Check that ./data/bond.txt is writable, then try again.
    ____________________________________________________________
```

### Input 2

```text
list
```

### Expected output 2

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][ ] inspect archive
    2.[T][X] secure archive
    ____________________________________________________________
```

### Input 3

```text
bye
```

### Expected output 3

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

## UI-21: Find missions by description

**Aim:** Verify that `find` searches descriptions without regard to case,
shows original mission numbers, ignores dates outside descriptions, rejects an
empty keyword, and does not change the stored list.

### Input 1

```text
todo read book
```

### Expected output 1

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] read book
    Now you have 1 mission in the list.
    ____________________________________________________________
```

### Input 2

```text
todo prepare kit
```

### Expected output 2

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] prepare kit
    Now you have 2 missions in the list.
    ____________________________________________________________
```

### Input 3

```text
deadline return Book /by 2019-06-06
```

### Expected output 3

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [D][ ] return Book (by: Jun 06 2019)
    Now you have 3 missions in the list.
    ____________________________________________________________
```

### Input 4

```text
event book club /from Friday /to Saturday
```

### Expected output 4

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [E][ ] book club (from: Friday to: Saturday)
    Now you have 4 missions in the list.
    ____________________________________________________________
```

### Input 5

```text
find BOOK
```

### Expected output 5

```text
    ____________________________________________________________
    Here are the matching missions in your list:
    1.[T][ ] read book
    3.[D][ ] return Book (by: Jun 06 2019)
    4.[E][ ] book club (from: Friday to: Saturday)
    ____________________________________________________________
```

### Expected data file 5

```text
T | 0 | read book
T | 0 | prepare kit
D | 0 | return Book | 2019-06-06
E | 0 | book club | Friday | Saturday
```

### Input 6

```text
find Jun
```

### Expected output 6

```text
    ____________________________________________________________
    Here are the matching missions in your list:
    No missions match that keyword.
    ____________________________________________________________
```

### Input 7

```text
find book club
```

### Expected output 7

```text
    ____________________________________________________________
    Here are the matching missions in your list:
    4.[E][ ] book club (from: Friday to: Saturday)
    ____________________________________________________________
```

### Input 8

```text
find
```

### Expected output 8

```text
    ____________________________________________________________
    Mission error: This find order needs a keyword.
    Brief me with: find <keyword>.
    ____________________________________________________________
```

### Input 9

```text
list
```

### Expected output 9

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][ ] read book
    2.[T][ ] prepare kit
    3.[D][ ] return Book (by: Jun 06 2019)
    4.[E][ ] book club (from: Friday to: Saturday)
    ____________________________________________________________
```

### Input 10

```text
bye
```

### Expected output 10

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

## UI-22: Exit when input ends

**Aim:** Verify that Bond exits normally after standard input closes without a
`bye` command, instead of printing a Java exception.

Run in a clean temporary working directory. Send the single line below through
a finite input stream, then close standard input.

### Input 1

```text
list
```

### Expected output 1

```text
    ____________________________________________________________
    Here are the missions in your list:
    ____________________________________________________________
```

### Input 2 (end of input)

Close standard input without entering another command.

### Expected output 2

```text
```

Bond exits with status code 0.
