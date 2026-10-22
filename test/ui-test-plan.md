# UI test plan

Tests the Eric command-line UI end to end. Run with the `test-ui` skill
(`.claude/skills/test-ui/SKILL.md`), which executes `test/run-ui-tests.py`.

## How the tests work

- Each test case starts a fresh `eric.Eric` process and feeds it the **Input** lines on standard input.
- The startup banner and greeting are stripped from the output before comparing, so **Expected output** starts right after the banner.
- The comparison is exact, line by line, ignoring trailing whitespace.
- Each test case runs in its own empty temporary folder, so `data/duke.txt` starts out missing and real data is never touched.
- A test case may add an `**Initial file (data/duke.txt):**` block after its aim; it is written to `data/duke.txt` before Eric starts. Without it, the file does not exist at startup.
- A test case may add an `**Expected file (data/duke.txt):**` block after its expected output; the saved file is then compared too. Use `(file not created)` when no file should exist.
- The session stops at the first failing test case and reports the actual and expected output.
- Every input block ends with `bye` so the program exits normally.
- To add a test case, append a section with the same shape: `## TCn: title`, `**Aim:**`, `**Input:**` code block, `**Expected output:**` code block.

## TC1: Add a todo

**Aim:** Check that `todo` adds a Todo task and reports the new task count.

**Input:**
```
todo read book
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC2: Add a deadline

**Aim:** Check that `deadline` adds a Deadline showing its `/by` date.

**Input:**
```
deadline return book /by Sunday
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Sunday)
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC3: Add an event

**Aim:** Check that `event` adds an Event showing its `/from` and `/to` times.

**Input:**
```
event meeting /from Mon 2pm /to 4pm
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [E][ ] meeting (from: Mon 2pm to: 4pm)
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC4: List an empty list

**Aim:** Check that `list` with no tasks prints an empty list without errors.

**Input:**
```
list
bye
```

**Expected output:**
```
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC5: List mixed task types

**Aim:** Check that `list` shows all task types, numbered, in insertion order (polymorphism).

**Input:**
```
todo read book
deadline return book /by Sunday
event meeting /from Mon 2pm /to 4pm
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Sunday)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] meeting (from: Mon 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[D][ ] return book (by: Sunday)
 3.[E][ ] meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC6: Mark and unmark a task

**Aim:** Check that `mark` and `unmark` change a task's done status.

**Input:**
```
todo read book
mark 1
unmark 1
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] read book
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] read book
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC7: Invalid task numbers

**Aim:** Check that `mark` rejects non-numeric, zero and out-of-range task numbers and says what is valid.

**Input:**
```
todo read book
mark 9
mark abc
mark 0
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! Task 9 doesn't exist.
 Choose a number from 1 to 1. Type list to see the tasks.
____________________________________________________________
____________________________________________________________
 OOPS!!! "abc" is not a valid task number.
 Use a plain whole number (no + sign or leading zeros), e.g. mark 2. Type list to see the task numbers.
____________________________________________________________
____________________________________________________________
 OOPS!!! Task 0 doesn't exist.
 Choose a number from 1 to 1. Type list to see the tasks.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC8: Malformed task commands

**Aim:** Check that an empty `todo` and incomplete `deadline` and `event` commands print an error and add nothing.

**Input:**
```
todo
deadline return book
event meeting /from Mon 2pm
list
bye
```

**Expected output:**
```
____________________________________________________________
 OOPS!!! The description of a todo is empty.
 Type a description after "todo", e.g. todo read book
____________________________________________________________
____________________________________________________________
 OOPS!!! A deadline needs a /by date, but I couldn't find one.
 Use the format: deadline DESCRIPTION /by DATE, e.g. deadline return book /by Sunday
____________________________________________________________
____________________________________________________________
 OOPS!!! An event needs a /to time, but I couldn't find one.
 Use the format: event DESCRIPTION /from START /to END, e.g. event project meeting /from Mon 2pm /to 4pm
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC9: Unknown command

**Aim:** Check that an unrecognised command is named in the error and the valid commands are listed.

**Input:**
```
blah
bye
```

**Expected output:**
```
____________________________________________________________
 OOPS!!! I don't know the command "blah".
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC10: Missing task number

**Aim:** Check that `mark` and `unmark` without a number explain what to type.

**Input:**
```
mark
unmark
bye
```

**Expected output:**
```
____________________________________________________________
 OOPS!!! The task number is missing.
 Type the number of a task after "mark", e.g. mark 2
____________________________________________________________
____________________________________________________________
 OOPS!!! The task number is missing.
 Type the number of a task after "unmark", e.g. unmark 2
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC11: Mark with no tasks

**Aim:** Check that marking when the list is empty says there is nothing to mark.

**Input:**
```
mark 1
unmark 1
bye
```

**Expected output:**
```
____________________________________________________________
 OOPS!!! There are no tasks to mark yet.
 Add a task first, e.g. todo read book
____________________________________________________________
____________________________________________________________
 OOPS!!! There are no tasks to unmark yet.
 Add a task first, e.g. todo read book
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC12: Deadline errors

**Aim:** Check each way a `deadline` can be invalid gets its own specific message.

**Input:**
```
deadline
deadline /by Sunday
deadline return book /by
bye
```

**Expected output:**
```
____________________________________________________________
 OOPS!!! A deadline needs a /by date, but I couldn't find one.
 Use the format: deadline DESCRIPTION /by DATE, e.g. deadline return book /by Sunday
____________________________________________________________
____________________________________________________________
 OOPS!!! The description of a deadline is empty.
 Type a description before /by, e.g. deadline return book /by Sunday
____________________________________________________________
____________________________________________________________
 OOPS!!! The date after /by is empty.
 Type when the task is due after /by, e.g. deadline return book /by Sunday
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC13: Event errors

**Aim:** Check each way an `event` can be invalid gets its own specific message.

**Input:**
```
event
event meeting /to 4pm
event meeting /to 4pm /from Mon 2pm
event /from Mon 2pm /to 4pm
event meeting /from /to 4pm
event meeting /from Mon 2pm /to
bye
```

**Expected output:**
```
____________________________________________________________
 OOPS!!! An event needs a /from time and a /to time, but I couldn't find either.
 Use the format: event DESCRIPTION /from START /to END, e.g. event project meeting /from Mon 2pm /to 4pm
____________________________________________________________
____________________________________________________________
 OOPS!!! An event needs a /from time, but I couldn't find one.
 Use the format: event DESCRIPTION /from START /to END, e.g. event project meeting /from Mon 2pm /to 4pm
____________________________________________________________
____________________________________________________________
 OOPS!!! /to comes before /from.
 Put /from first, then /to. Use the format: event DESCRIPTION /from START /to END, e.g. event project meeting /from Mon 2pm /to 4pm
____________________________________________________________
____________________________________________________________
 OOPS!!! The description of an event is empty.
 Type a description before /from, e.g. event project meeting /from Mon 2pm /to 4pm
____________________________________________________________
____________________________________________________________
 OOPS!!! The start time after /from is empty.
 Type when the event starts after /from, e.g. event project meeting /from Mon 2pm /to 4pm
____________________________________________________________
____________________________________________________________
 OOPS!!! The end time after /to is empty.
 Type when the event ends after /to, e.g. event project meeting /from Mon 2pm /to 4pm
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC14: Blank and padded input

**Aim:** Check that a blank line is reported and that spaces around a command are ignored.

**Input:**
```

   list  
bye
```

**Expected output:**
```
____________________________________________________________
 OOPS!!! You didn't type a command.
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC15: Keeps running after errors

**Aim:** Check that the chatbot stays up after errors and still accepts valid commands.

**Input:**
```
todo
blah
mark 3
todo read book
list
bye
```

**Expected output:**
```
____________________________________________________________
 OOPS!!! The description of a todo is empty.
 Type a description after "todo", e.g. todo read book
____________________________________________________________
____________________________________________________________
 OOPS!!! I don't know the command "blah".
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 OOPS!!! There are no tasks to mark yet.
 Add a task first, e.g. todo read book
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC16: Todo spacing and markers

**Aim:** Check that a todo keeps inner spaces, trims the ends and treats `/by` as plain text.

**Input:**
```
todo   read   book  
todo buy /by milk
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read   book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] buy /by milk
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC17: Deadline examples

**Aim:** Check deadlines with free-text, multi-word and slash-containing dates, and repeated `/by`.

**Input:**
```
deadline do homework /by no idea :-p
deadline submit report /by 11/10/2019 5pm
deadline a /by b /by c
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [D][ ] do homework (by: no idea :-p)
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] submit report (by: 11/10/2019 5pm)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] a (by: b /by c)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC18: Event examples

**Aim:** Check events with date ranges and a description containing a marker look-alike.

**Input:**
```
event team project meeting /from 2/10/2019 2pm /to 4pm
event orientation week /from 4/10/2019 /to 11/10/2019
event a /tomorrow /from 1 /to 2
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [E][ ] team project meeting (from: 2/10/2019 2pm to: 4pm)
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] orientation week (from: 4/10/2019 to: 11/10/2019)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] a /tomorrow (from: 1 to: 2)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC19: Extra spaces inside commands

**Aim:** Check that extra spaces around and between command parts are ignored.

**Input:**
```
  todo   read book  
deadline  return book   /by   Sunday  
event  meeting  /from  Mon 2pm  /to  4pm  
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Sunday)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] meeting (from: Mon 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[D][ ] return book (by: Sunday)
 3.[E][ ] meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC20: Mark each task type

**Aim:** Check that mark and unmark work on todos, deadlines and events, and that list shows the status.

**Input:**
```
todo read book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
mark 1
mark 3
list
unmark 3
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
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
   [T][X] read book
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [E][X] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] read book
 2.[D][ ] return book (by: Sunday)
 3.[E][X] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] read book
 2.[D][ ] return book (by: Sunday)
 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC21: Repeated mark and unmark

**Aim:** Check that marking a done task or unmarking a not-done task is harmless.

**Input:**
```
todo read book
mark 1
mark 1
unmark 1
unmark 1
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] read book
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] read book
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] read book
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] read book
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC22: Task number boundaries

**Aim:** Check the first and last task numbers work and numbers just outside the range are rejected.

**Input:**
```
todo a
todo b
todo c
mark 3
mark 4
mark -1
mark 1
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] b
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] c
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] c
____________________________________________________________
____________________________________________________________
 OOPS!!! Task 4 doesn't exist.
 Choose a number from 1 to 3. Type list to see the tasks.
____________________________________________________________
____________________________________________________________
 OOPS!!! Task -1 doesn't exist.
 Choose a number from 1 to 3. Type list to see the tasks.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] a
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] a
 2.[T][ ] b
 3.[T][X] c
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC23: Invalid task number formats

**Aim:** Check that decimals, several numbers and overflowing numbers are rejected, and spaces before a number are fine.

**Input:**
```
todo read book
mark 1.5
mark 1 2
mark 99999999999
mark   
mark   1
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! "1.5" is not a valid task number.
 Use a plain whole number (no + sign or leading zeros), e.g. mark 2. Type list to see the task numbers.
____________________________________________________________
____________________________________________________________
 OOPS!!! "1 2" is not a valid task number.
 Use a plain whole number (no + sign or leading zeros), e.g. mark 2. Type list to see the task numbers.
____________________________________________________________
____________________________________________________________
 OOPS!!! "99999999999" is not a valid task number.
 Use a plain whole number (no + sign or leading zeros), e.g. mark 2. Type list to see the task numbers.
____________________________________________________________
____________________________________________________________
 OOPS!!! The task number is missing.
 Type the number of a task after "mark", e.g. mark 2
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] read book
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC24: Unmark errors

**Aim:** Check that unmark reports bad, missing and out-of-range numbers using its own command name.

**Input:**
```
todo read book
unmark abc
unmark 0
unmark 9
unmark
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! "abc" is not a valid task number.
 Use a plain whole number (no + sign or leading zeros), e.g. unmark 2. Type list to see the task numbers.
____________________________________________________________
____________________________________________________________
 OOPS!!! Task 0 doesn't exist.
 Choose a number from 1 to 1. Type list to see the tasks.
____________________________________________________________
____________________________________________________________
 OOPS!!! Task 9 doesn't exist.
 Choose a number from 1 to 1. Type list to see the tasks.
____________________________________________________________
____________________________________________________________
 OOPS!!! The task number is missing.
 Type the number of a task after "unmark", e.g. unmark 2
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC25: Look-alike commands are unknown

**Aim:** Check that near-miss commands (wrong case, missing space, extra words) are rejected.

**Input:**
```
todoread book
TODO read book
listing
list extra
mark1
bye now
deadlines x /by y
events
bye
```

**Expected output:**
```
____________________________________________________________
 OOPS!!! I don't know the command "todoread book".
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 OOPS!!! I don't know the command "TODO read book".
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 OOPS!!! I don't know the command "listing".
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 OOPS!!! I don't know the command "list extra".
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 OOPS!!! I don't know the command "mark1".
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 OOPS!!! I don't know the command "bye now".
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 OOPS!!! I don't know the command "deadlines x /by y".
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 OOPS!!! I don't know the command "events".
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC26: Bye only

**Aim:** Check that `bye` alone exits with the goodbye message and nothing else.

**Input:**
```
bye
```

**Expected output:**
```
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC27: End of input without bye

**Aim:** Check that the chatbot exits normally when the input ends without `bye`.

**Input:**
```
todo read book
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC28: Bye stops processing

**Aim:** Check that `bye` (even with spaces) exits at once and ignores later input.

**Input:**
```
todo a
  bye  
todo b
list
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC29: Ten tasks numbering

**Aim:** Check that numbering and marking still work with two-digit task numbers.

**Input:**
```
todo task 1
todo task 2
todo task 3
todo task 4
todo task 5
todo task 6
todo task 7
todo task 8
todo task 9
todo task 10
mark 10
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 1
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 2
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 3
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 4
 Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 5
 Now you have 5 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 6
 Now you have 6 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 7
 Now you have 7 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 8
 Now you have 8 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 9
 Now you have 9 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 10
 Now you have 10 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] task 10
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] task 1
 2.[T][ ] task 2
 3.[T][ ] task 3
 4.[T][ ] task 4
 5.[T][ ] task 5
 6.[T][ ] task 6
 7.[T][ ] task 7
 8.[T][ ] task 8
 9.[T][ ] task 9
 10.[T][X] task 10
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC30: Errors do not change the task count

**Aim:** Check that failed adds leave the list and the task count unchanged.

**Input:**
```
todo a
deadline bad
event bad
todo
todo b
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! A deadline needs a /by date, but I couldn't find one.
 Use the format: deadline DESCRIPTION /by DATE, e.g. deadline return book /by Sunday
____________________________________________________________
____________________________________________________________
 OOPS!!! An event needs a /from time and a /to time, but I couldn't find either.
 Use the format: event DESCRIPTION /from START /to END, e.g. event project meeting /from Mon 2pm /to 4pm
____________________________________________________________
____________________________________________________________
 OOPS!!! The description of a todo is empty.
 Type a description after "todo", e.g. todo read book
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] b
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] a
 2.[T][ ] b
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC31: Repeated blank lines

**Aim:** Check that several blank or space-only lines each get an error and do not stop the program.

**Input:**
```


   
bye
```

**Expected output:**
```
____________________________________________________________
 OOPS!!! You didn't type a command.
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 OOPS!!! You didn't type a command.
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 OOPS!!! You didn't type a command.
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC32: Deadline with only a marker

**Aim:** Check that a deadline with `/by` but no description reports the empty description.

**Input:**
```
deadline /by
deadline   /by   
bye
```

**Expected output:**
```
____________________________________________________________
 OOPS!!! The description of a deadline is empty.
 Type a description before /by, e.g. deadline return book /by Sunday
____________________________________________________________
____________________________________________________________
 OOPS!!! The description of a deadline is empty.
 Type a description before /by, e.g. deadline return book /by Sunday
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC33: Event with only markers

**Aim:** Check that an event with `/from` and `/to` but nothing else reports the empty description.

**Input:**
```
event /from /to
bye
```

**Expected output:**
```
____________________________________________________________
 OOPS!!! The description of an event is empty.
 Type a description before /from, e.g. event project meeting /from Mon 2pm /to 4pm
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC34: Markers must be whole words

**Aim:** Check that `/bypass` and `x/by` are not treated as `/by`, but a date such as `bypass` is fine.

**Input:**
```
deadline read /bypass book
deadline x/by y
deadline read /by bypass
bye
```

**Expected output:**
```
____________________________________________________________
 OOPS!!! A deadline needs a /by date, but I couldn't find one.
 Use the format: deadline DESCRIPTION /by DATE, e.g. deadline return book /by Sunday
____________________________________________________________
____________________________________________________________
 OOPS!!! A deadline needs a /by date, but I couldn't find one.
 Use the format: deadline DESCRIPTION /by DATE, e.g. deadline return book /by Sunday
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] read (by: bypass)
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC35: All commands in one session

**Aim:** Check a realistic session that uses every command, with errors in between.

**Input:**
```
list
todo borrow book
deadline return book /by Sunday
blah
event project meeting /from Mon 2pm /to 4pm
mark 2
mark 5
unmark 2
mark
list
bye
```

**Expected output:**
```
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Sunday)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! I don't know the command "blah".
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
 OOPS!!! Task 5 doesn't exist.
 Choose a number from 1 to 3. Type list to see the tasks.
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [D][ ] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
 OOPS!!! The task number is missing.
 Type the number of a task after "mark", e.g. mark 2
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] borrow book
 2.[D][ ] return book (by: Sunday)
 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC36: Interleaved valid and invalid adds

**Aim:** Check that failed adds between successful ones never change the count or the stored tasks.

**Input:**
```
todo a
todo
todo b
deadline
deadline c /by d
event
event e /from f /to g
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! The description of a todo is empty.
 Type a description after "todo", e.g. todo read book
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] b
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! A deadline needs a /by date, but I couldn't find one.
 Use the format: deadline DESCRIPTION /by DATE, e.g. deadline return book /by Sunday
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] c (by: d)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! An event needs a /from time and a /to time, but I couldn't find either.
 Use the format: event DESCRIPTION /from START /to END, e.g. event project meeting /from Mon 2pm /to 4pm
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] e (from: f to: g)
 Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] a
 2.[T][ ] b
 3.[D][ ] c (by: d)
 4.[E][ ] e (from: f to: g)
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC37: Failed marks leave other tasks unchanged

**Aim:** Check that invalid mark and unmark commands between valid ones never change any task's status.

**Input:**
```
todo a
todo b
todo c
mark 2
mark 4
mark abc
mark 0
list
unmark 4
unmark x
list
unmark 2
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] b
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] c
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] b
____________________________________________________________
____________________________________________________________
 OOPS!!! Task 4 doesn't exist.
 Choose a number from 1 to 3. Type list to see the tasks.
____________________________________________________________
____________________________________________________________
 OOPS!!! "abc" is not a valid task number.
 Use a plain whole number (no + sign or leading zeros), e.g. mark 2. Type list to see the task numbers.
____________________________________________________________
____________________________________________________________
 OOPS!!! Task 0 doesn't exist.
 Choose a number from 1 to 3. Type list to see the tasks.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] a
 2.[T][X] b
 3.[T][ ] c
____________________________________________________________
____________________________________________________________
 OOPS!!! Task 4 doesn't exist.
 Choose a number from 1 to 3. Type list to see the tasks.
____________________________________________________________
____________________________________________________________
 OOPS!!! "x" is not a valid task number.
 Use a plain whole number (no + sign or leading zeros), e.g. unmark 2. Type list to see the task numbers.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] a
 2.[T][X] b
 3.[T][ ] c
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] b
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] a
 2.[T][ ] b
 3.[T][ ] c
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC38: Errors between marks of one task

**Aim:** Check that an error between two marks of the same task leaves its status correct.

**Input:**
```
todo a
mark 1
unmark 9
mark 1
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] a
____________________________________________________________
____________________________________________________________
 OOPS!!! Task 9 doesn't exist.
 Choose a number from 1 to 1. Type list to see the tasks.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] a
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] a
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC39: No wrap-around of task numbers

**Aim:** Check that -1, 0 and count+1 do not select the last or first task.

**Input:**
```
todo a
todo b
todo c
mark -1
mark 0
mark 4
list
mark 3
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] b
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] c
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! Task -1 doesn't exist.
 Choose a number from 1 to 3. Type list to see the tasks.
____________________________________________________________
____________________________________________________________
 OOPS!!! Task 0 doesn't exist.
 Choose a number from 1 to 3. Type list to see the tasks.
____________________________________________________________
____________________________________________________________
 OOPS!!! Task 4 doesn't exist.
 Choose a number from 1 to 3. Type list to see the tasks.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] a
 2.[T][ ] b
 3.[T][ ] c
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] c
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] a
 2.[T][ ] b
 3.[T][X] c
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC40: Failed event does not leak fields

**Aim:** Check that a rejected event does not affect the fields of later events.

**Input:**
```
event a /from 1 /to 2
event b /to 4 /from 3
event c /from 5 /to 6
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [E][ ] a (from: 1 to: 2)
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! /to comes before /from.
 Put /from first, then /to. Use the format: event DESCRIPTION /from START /to END, e.g. event project meeting /from Mon 2pm /to 4pm
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] c (from: 5 to: 6)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[E][ ] a (from: 1 to: 2)
 2.[E][ ] c (from: 5 to: 6)
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC41: Failed deadline does not leak fields

**Aim:** Check that rejected deadlines do not affect the fields of later deadlines.

**Input:**
```
deadline a /by 1
deadline b /by
deadline /by 2
deadline c /by 3
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [D][ ] a (by: 1)
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! The date after /by is empty.
 Type when the task is due after /by, e.g. deadline return book /by Sunday
____________________________________________________________
____________________________________________________________
 OOPS!!! The description of a deadline is empty.
 Type a description before /by, e.g. deadline return book /by Sunday
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] c (by: 3)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[D][ ] a (by: 1)
 2.[D][ ] c (by: 3)
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC42: Command words as descriptions

**Aim:** Check that command words and markers inside a todo are stored as text and never executed.

**Input:**
```
todo list
todo bye
todo mark 1
todo event x /from a /to b
todo /by
list
mark 2
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] list
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] bye
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] mark 1
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] event x /from a /to b
 Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] /by
 Now you have 5 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] list
 2.[T][ ] bye
 3.[T][ ] mark 1
 4.[T][ ] event x /from a /to b
 5.[T][ ] /by
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] bye
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] list
 2.[T][X] bye
 3.[T][ ] mark 1
 4.[T][ ] event x /from a /to b
 5.[T][ ] /by
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC43: Special characters

**Aim:** Check that accents, symbols, quotes, backslashes and percent signs are stored and printed unchanged.

**Input:**
```
todo café ☕ ñ
todo "quoted" 'text'
todo back\slash 100% {braces} $dollar
deadline pay $5 /by 50% off
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] café ☕ ñ
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] "quoted" 'text'
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] back\slash 100% {braces} $dollar
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] pay $5 (by: 50% off)
 Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] café ☕ ñ
 2.[T][ ] "quoted" 'text'
 3.[T][ ] back\slash 100% {braces} $dollar
 4.[D][ ] pay $5 (by: 50% off)
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC44: Very long descriptions

**Aim:** Check that 200-character descriptions are handled and an error in between changes nothing.

**Input:**
```
todo xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
todo
deadline xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx /by xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! The description of a todo is empty.
 Type a description after "todo", e.g. todo read book
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx (by: xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
 2.[D][ ] xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx (by: xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC45: Only plain integers are task numbers

**Aim:** Check that `01`, `+2`, `-0` and `00` are rejected as invalid numbers without changing any task, `0` is out of range, and a plain number then works.

**Input:**
```
todo a
todo b
mark 01
mark +2
mark -0
mark 00
unmark 01
mark 0
list
mark 2
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] b
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! "01" is not a valid task number.
 Use a plain whole number (no + sign or leading zeros), e.g. mark 2. Type list to see the task numbers.
____________________________________________________________
____________________________________________________________
 OOPS!!! "+2" is not a valid task number.
 Use a plain whole number (no + sign or leading zeros), e.g. mark 2. Type list to see the task numbers.
____________________________________________________________
____________________________________________________________
 OOPS!!! "-0" is not a valid task number.
 Use a plain whole number (no + sign or leading zeros), e.g. mark 2. Type list to see the task numbers.
____________________________________________________________
____________________________________________________________
 OOPS!!! "00" is not a valid task number.
 Use a plain whole number (no + sign or leading zeros), e.g. mark 2. Type list to see the task numbers.
____________________________________________________________
____________________________________________________________
 OOPS!!! "01" is not a valid task number.
 Use a plain whole number (no + sign or leading zeros), e.g. unmark 2. Type list to see the task numbers.
____________________________________________________________
____________________________________________________________
 OOPS!!! Task 0 doesn't exist.
 Choose a number from 1 to 2. Type list to see the tasks.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] a
 2.[T][ ] b
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] b
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] a
 2.[T][X] b
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC46: Blank lines and unknown commands between valid ones

**Aim:** Check that blank lines and unknown commands between valid commands change nothing.

**Input:**
```
todo a

mark 1
blah
todo b
  
list
unmark 1
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! You didn't type a command.
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] a
____________________________________________________________
____________________________________________________________
 OOPS!!! I don't know the command "blah".
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] b
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! You didn't type a command.
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] a
 2.[T][ ] b
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] a
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] a
 2.[T][ ] b
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC47: Retry after each kind of error

**Aim:** Check that every rejected command works once it is corrected.

**Input:**
```
todo
todo read book
mark
mark 1
deadline x
deadline x /by y
event x /from 1
event x /from 1 /to 2
list
bye
```

**Expected output:**
```
____________________________________________________________
 OOPS!!! The description of a todo is empty.
 Type a description after "todo", e.g. todo read book
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! The task number is missing.
 Type the number of a task after "mark", e.g. mark 2
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] read book
____________________________________________________________
____________________________________________________________
 OOPS!!! A deadline needs a /by date, but I couldn't find one.
 Use the format: deadline DESCRIPTION /by DATE, e.g. deadline return book /by Sunday
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] x (by: y)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! An event needs a /to time, but I couldn't find one.
 Use the format: event DESCRIPTION /from START /to END, e.g. event project meeting /from Mon 2pm /to 4pm
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] x (from: 1 to: 2)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] read book
 2.[D][ ] x (by: y)
 3.[E][ ] x (from: 1 to: 2)
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC48: Mark before and after the first task

**Aim:** Check that marking an empty list fails, and that adding a task then makes marking 1 work but 2 fail.

**Input:**
```
mark 1
todo a
mark 1
mark 2
list
bye
```

**Expected output:**
```
____________________________________________________________
 OOPS!!! There are no tasks to mark yet.
 Add a task first, e.g. todo read book
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] a
____________________________________________________________
____________________________________________________________
 OOPS!!! Task 2 doesn't exist.
 Choose a number from 1 to 1. Type list to see the tasks.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] a
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC49: Repeated markers in events

**Aim:** Check that with repeated `/to` or `/from` the first `/from` and first `/to` split the text, and a later error changes nothing.

**Input:**
```
event a /from 1 /to 2 /to 3
event b /from 1 /from 2 /to 3
event c /from 1
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [E][ ] a (from: 1 to: 2 /to 3)
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] b (from: 1 /from 2 to: 3)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! An event needs a /to time, but I couldn't find one.
 Use the format: event DESCRIPTION /from START /to END, e.g. event project meeting /from Mon 2pm /to 4pm
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[E][ ] a (from: 1 to: 2 /to 3)
 2.[E][ ] b (from: 1 /from 2 to: 3)
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC50: Status kept after an error

**Aim:** Check that a deadline's done status and fields survive a later failed command.

**Input:**
```
deadline a /by b
mark 1
deadline
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [D][ ] a (by: b)
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] a (by: b)
____________________________________________________________
____________________________________________________________
 OOPS!!! A deadline needs a /by date, but I couldn't find one.
 Use the format: deadline DESCRIPTION /by DATE, e.g. deadline return book /by Sunday
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[D][X] a (by: b)
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC51: Delete example from the specification

**Aim:** Check the specified example: delete the middle task of five and see the remaining tasks renumbered.

**Input:**
```
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
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
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
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC52: Delete first, last and only task

**Aim:** Check deleting the first task, the last task and the only task, ending with an empty list.

**Input:**
```
todo a
todo b
todo c
delete 1
delete 2
delete 1
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] b
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] c
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] a
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] c
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] b
 Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC53: Numbers shift after delete

**Aim:** Check that a deleted task's status does not move to another task and later numbers shift down.

**Input:**
```
todo a
todo b
todo c
mark 3
delete 1
list
mark 1
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] b
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] c
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] c
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] a
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] b
 2.[T][X] c
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] b
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] b
 2.[T][X] c
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC54: Add after delete

**Aim:** Check that the task count continues correctly after a delete.

**Input:**
```
todo a
todo b
delete 1
todo c
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] b
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] c
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] b
 2.[T][ ] c
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC55: Delete errors

**Aim:** Check that a missing, invalid or out-of-range number for `delete` is rejected and no task is removed.

**Input:**
```
todo a
delete
delete abc
delete 0
delete -1
delete 2
delete 01
delete +1
delete 1.5
delete 1 2
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! The task number is missing.
 Type the number of a task after "delete", e.g. delete 2
____________________________________________________________
____________________________________________________________
 OOPS!!! "abc" is not a valid task number.
 Use a plain whole number (no + sign or leading zeros), e.g. delete 2. Type list to see the task numbers.
____________________________________________________________
____________________________________________________________
 OOPS!!! Task 0 doesn't exist.
 Choose a number from 1 to 1. Type list to see the tasks.
____________________________________________________________
____________________________________________________________
 OOPS!!! Task -1 doesn't exist.
 Choose a number from 1 to 1. Type list to see the tasks.
____________________________________________________________
____________________________________________________________
 OOPS!!! Task 2 doesn't exist.
 Choose a number from 1 to 1. Type list to see the tasks.
____________________________________________________________
____________________________________________________________
 OOPS!!! "01" is not a valid task number.
 Use a plain whole number (no + sign or leading zeros), e.g. delete 2. Type list to see the task numbers.
____________________________________________________________
____________________________________________________________
 OOPS!!! "+1" is not a valid task number.
 Use a plain whole number (no + sign or leading zeros), e.g. delete 2. Type list to see the task numbers.
____________________________________________________________
____________________________________________________________
 OOPS!!! "1.5" is not a valid task number.
 Use a plain whole number (no + sign or leading zeros), e.g. delete 2. Type list to see the task numbers.
____________________________________________________________
____________________________________________________________
 OOPS!!! "1 2" is not a valid task number.
 Use a plain whole number (no + sign or leading zeros), e.g. delete 2. Type list to see the task numbers.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] a
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC56: Delete on an empty list

**Aim:** Check that deleting from an empty list, including after deleting everything, says there is nothing to delete.

**Input:**
```
delete 1
todo a
delete 1
delete 1
list
bye
```

**Expected output:**
```
____________________________________________________________
 OOPS!!! There are no tasks to delete yet.
 Add a task first, e.g. todo read book
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] a
 Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! There are no tasks to delete yet.
 Add a task first, e.g. todo read book
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC57: Interleaved valid and invalid deletes

**Aim:** Check that invalid deletes between valid ones never remove or shift tasks.

**Input:**
```
todo a
todo b
todo c
todo d
delete 2
delete 9
delete 2
delete x
delete 2
delete 2
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] b
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] c
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] d
 Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] b
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! Task 9 doesn't exist.
 Choose a number from 1 to 3. Type list to see the tasks.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] c
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! "x" is not a valid task number.
 Use a plain whole number (no + sign or leading zeros), e.g. delete 2. Type list to see the task numbers.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] d
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! Task 2 doesn't exist.
 Choose a number from 1 to 1. Type list to see the tasks.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] a
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC58: Delete each task type

**Aim:** Check that todos, deadlines and events can all be deleted and are shown with their details.

**Input:**
```
todo a
deadline b /by c
event d /from e /to f
delete 2
delete 2
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] b (by: c)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] d (from: e to: f)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [D][ ] b (by: c)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [E][ ] d (from: e to: f)
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] a
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC59: Delete look-alike commands

**Aim:** Check that near-miss delete commands are unknown and remove nothing.

**Input:**
```
todo a
deletes 1
delete1
DELETE 1
delete-1
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! I don't know the command "deletes 1".
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 OOPS!!! I don't know the command "delete1".
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 OOPS!!! I don't know the command "DELETE 1".
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 OOPS!!! I don't know the command "delete-1".
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] a
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC60: Delete with extra spaces

**Aim:** Check that spaces around and inside a delete command are ignored.

**Input:**
```
todo a
todo b
  delete   1  
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] b
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] b
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC61: More than 100 tasks

**Aim:** Check that the list is not limited to 100 tasks and delete still works beyond that.

**Input:**
```
todo task 1
todo task 2
todo task 3
todo task 4
todo task 5
todo task 6
todo task 7
todo task 8
todo task 9
todo task 10
todo task 11
todo task 12
todo task 13
todo task 14
todo task 15
todo task 16
todo task 17
todo task 18
todo task 19
todo task 20
todo task 21
todo task 22
todo task 23
todo task 24
todo task 25
todo task 26
todo task 27
todo task 28
todo task 29
todo task 30
todo task 31
todo task 32
todo task 33
todo task 34
todo task 35
todo task 36
todo task 37
todo task 38
todo task 39
todo task 40
todo task 41
todo task 42
todo task 43
todo task 44
todo task 45
todo task 46
todo task 47
todo task 48
todo task 49
todo task 50
todo task 51
todo task 52
todo task 53
todo task 54
todo task 55
todo task 56
todo task 57
todo task 58
todo task 59
todo task 60
todo task 61
todo task 62
todo task 63
todo task 64
todo task 65
todo task 66
todo task 67
todo task 68
todo task 69
todo task 70
todo task 71
todo task 72
todo task 73
todo task 74
todo task 75
todo task 76
todo task 77
todo task 78
todo task 79
todo task 80
todo task 81
todo task 82
todo task 83
todo task 84
todo task 85
todo task 86
todo task 87
todo task 88
todo task 89
todo task 90
todo task 91
todo task 92
todo task 93
todo task 94
todo task 95
todo task 96
todo task 97
todo task 98
todo task 99
todo task 100
todo task 101
delete 1
mark 100
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 1
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 2
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 3
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 4
 Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 5
 Now you have 5 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 6
 Now you have 6 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 7
 Now you have 7 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 8
 Now you have 8 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 9
 Now you have 9 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 10
 Now you have 10 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 11
 Now you have 11 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 12
 Now you have 12 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 13
 Now you have 13 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 14
 Now you have 14 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 15
 Now you have 15 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 16
 Now you have 16 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 17
 Now you have 17 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 18
 Now you have 18 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 19
 Now you have 19 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 20
 Now you have 20 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 21
 Now you have 21 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 22
 Now you have 22 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 23
 Now you have 23 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 24
 Now you have 24 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 25
 Now you have 25 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 26
 Now you have 26 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 27
 Now you have 27 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 28
 Now you have 28 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 29
 Now you have 29 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 30
 Now you have 30 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 31
 Now you have 31 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 32
 Now you have 32 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 33
 Now you have 33 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 34
 Now you have 34 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 35
 Now you have 35 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 36
 Now you have 36 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 37
 Now you have 37 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 38
 Now you have 38 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 39
 Now you have 39 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 40
 Now you have 40 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 41
 Now you have 41 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 42
 Now you have 42 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 43
 Now you have 43 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 44
 Now you have 44 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 45
 Now you have 45 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 46
 Now you have 46 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 47
 Now you have 47 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 48
 Now you have 48 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 49
 Now you have 49 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 50
 Now you have 50 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 51
 Now you have 51 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 52
 Now you have 52 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 53
 Now you have 53 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 54
 Now you have 54 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 55
 Now you have 55 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 56
 Now you have 56 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 57
 Now you have 57 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 58
 Now you have 58 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 59
 Now you have 59 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 60
 Now you have 60 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 61
 Now you have 61 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 62
 Now you have 62 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 63
 Now you have 63 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 64
 Now you have 64 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 65
 Now you have 65 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 66
 Now you have 66 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 67
 Now you have 67 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 68
 Now you have 68 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 69
 Now you have 69 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 70
 Now you have 70 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 71
 Now you have 71 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 72
 Now you have 72 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 73
 Now you have 73 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 74
 Now you have 74 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 75
 Now you have 75 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 76
 Now you have 76 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 77
 Now you have 77 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 78
 Now you have 78 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 79
 Now you have 79 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 80
 Now you have 80 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 81
 Now you have 81 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 82
 Now you have 82 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 83
 Now you have 83 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 84
 Now you have 84 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 85
 Now you have 85 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 86
 Now you have 86 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 87
 Now you have 87 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 88
 Now you have 88 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 89
 Now you have 89 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 90
 Now you have 90 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 91
 Now you have 91 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 92
 Now you have 92 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 93
 Now you have 93 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 94
 Now you have 94 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 95
 Now you have 95 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 96
 Now you have 96 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 97
 Now you have 97 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 98
 Now you have 98 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 99
 Now you have 99 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 100
 Now you have 100 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 101
 Now you have 101 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] task 1
 Now you have 100 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] task 101
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC62: Delete combined with mark and unmark

**Aim:** Check mark, unmark and delete numbers stay consistent as tasks are removed.

**Input:**
```
todo a
todo b
delete 1
mark 2
mark 1
delete 1
mark 1
unmark 1
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] b
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! Task 2 doesn't exist.
 Choose a number from 1 to 1. Type list to see the tasks.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] b
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][X] b
 Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! There are no tasks to mark yet.
 Add a task first, e.g. todo read book
____________________________________________________________
____________________________________________________________
 OOPS!!! There are no tasks to unmark yet.
 Add a task first, e.g. todo read book
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC63: Save creates the data folder and file

**Aim:** Check that adding a task creates `data/duke.txt`, although the `data` folder does not exist beforehand.

**Input:**
```
todo read book
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
T | 0 | read book
```

## TC64: Save all task types

**Aim:** Check the file format for todos, deadlines and events (events use separate start and end columns) and done flags.

**Input:**
```
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
todo join sports club
mark 1
mark 4
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
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
 Nice! I've marked this task as done:
   [T][X] read book
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] join sports club
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
T | 1 | read book
D | 0 | return book | June 6th
E | 0 | project meeting | Aug 6th 2pm | 4pm
T | 1 | join sports club
```

## TC65: Save after mark and unmark

**Aim:** Check that mark and unmark update the file, and only the marked task's flag changes.

**Input:**
```
todo a
todo b
mark 1
mark 2
unmark 1
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] b
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] a
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] b
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] a
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
T | 0 | a
T | 1 | b
```

## TC66: Rejected commands do not change the file

**Aim:** Check that errors between valid commands leave the saved tasks unchanged.

**Input:**
```
todo a
todo
deadline x
blah
mark 9
mark abc
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! The description of a todo is empty.
 Type a description after "todo", e.g. todo read book
____________________________________________________________
____________________________________________________________
 OOPS!!! A deadline needs a /by date, but I couldn't find one.
 Use the format: deadline DESCRIPTION /by DATE, e.g. deadline return book /by Sunday
____________________________________________________________
____________________________________________________________
 OOPS!!! I don't know the command "blah".
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 OOPS!!! Task 9 doesn't exist.
 Choose a number from 1 to 1. Type list to see the tasks.
____________________________________________________________
____________________________________________________________
 OOPS!!! "abc" is not a valid task number.
 Use a plain whole number (no + sign or leading zeros), e.g. mark 2. Type list to see the task numbers.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
T | 0 | a
```

## TC67: No file is written when nothing changes

**Aim:** Check that commands that do not change the list (list, unknown, bad mark) do not create the file.

**Input:**
```
list
blah
mark 1
bye
```

**Expected output:**
```
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 OOPS!!! I don't know the command "blah".
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 OOPS!!! There are no tasks to mark yet.
 Add a task first, e.g. todo read book
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
(file not created)
```

## TC68: Save special characters

**Aim:** Check that accents, emoji and symbols are written to the file unchanged (UTF-8).

**Input:**
```
todo café ☕ ñ
deadline pay $5 /by 50% off
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] café ☕ ñ
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] pay $5 (by: 50% off)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
T | 0 | café ☕ ñ
D | 0 | pay $5 | 50% off
```

## TC69: Save trimmed text

**Aim:** Check that the file holds the trimmed text, with inner spaces and marker look-alikes kept.

**Input:**
```
todo   read   book  
event a /tomorrow /from 1 /to 2
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read   book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] a /tomorrow (from: 1 to: 2)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
T | 0 | read   book
E | 0 | a /tomorrow | 1 | 2
```

## TC70: Save ten tasks in order

**Aim:** Check that all tasks are saved in list order, including the marked last one.

**Input:**
```
todo task 1
todo task 2
todo task 3
todo task 4
todo task 5
todo task 6
todo task 7
todo task 8
todo task 9
todo task 10
mark 10
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 1
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 2
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 3
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 4
 Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 5
 Now you have 5 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 6
 Now you have 6 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 7
 Now you have 7 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 8
 Now you have 8 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 9
 Now you have 9 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 10
 Now you have 10 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] task 10
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
T | 0 | task 1
T | 0 | task 2
T | 0 | task 3
T | 0 | task 4
T | 0 | task 5
T | 0 | task 6
T | 0 | task 7
T | 0 | task 8
T | 0 | task 9
T | 1 | task 10
```

## TC71: Load all task types

**Aim:** Check that every task type and done flag in the save file is loaded and shown by `list`, and the file is left unchanged.

**Initial file (data/duke.txt):**
```
T | 1 | read book
D | 0 | return book | June 6th
E | 0 | project meeting | Aug 6th 2pm | 4pm
T | 1 | join sports club
```

**Input:**
```
list
bye
```

**Expected output:**
```
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] read book
 2.[D][ ] return book (by: June 6th)
 3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
 4.[T][X] join sports club
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
T | 1 | read book
D | 0 | return book | June 6th
E | 0 | project meeting | Aug 6th 2pm | 4pm
T | 1 | join sports club
```

## TC72: Loaded tasks can be changed and extended

**Aim:** Check that loaded tasks can be marked and unmarked, new tasks continue the numbering, and the file is rewritten.

**Initial file (data/duke.txt):**
```
T | 1 | a
D | 0 | b | c
```

**Input:**
```
todo d
mark 2
list
unmark 1
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] d
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] b (by: c)
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] a
 2.[D][X] b (by: c)
 3.[T][ ] d
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] a
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
T | 0 | a
D | 1 | b | c
T | 0 | d
```

## TC73: Load an empty file

**Aim:** Check that an empty save file gives an empty list and can be added to.

**Initial file (data/duke.txt):**
```

```

**Input:**
```
list
todo a
bye
```

**Expected output:**
```
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
T | 0 | a
```

## TC74: Blank lines in the file are ignored

**Aim:** Check that blank or space-only lines are skipped silently, without a warning.

**Initial file (data/duke.txt):**
```
T | 0 | a

   
T | 1 | b
```

**Input:**
```
list
bye
```

**Expected output:**
```
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] a
 2.[T][X] b
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
T | 0 | a

   
T | 1 | b
```

## TC75: A file with invalid lines is rejected as a whole

**Aim:** Check that every invalid line is reported with its line number and reason, that even the valid lines are not loaded, and that the file is replaced by the next save.

**Initial file (data/duke.txt):**
```
T | 0 | good one
X | 0 | unknown type
T | 2 | bad flag
D | 0 | no date
E | 0 | only one time | Mon
D | 0 |  | June 6th
just some text
T | 1 | good two
```

**Input:**
```
list
todo new
bye
```

**Expected output:**
```
____________________________________________________________
 OOPS!!! I couldn't load your saved tasks from data/duke.txt because it is not in the expected format:
   line 2: unknown task type "X"
   line 3: the done flag must be 0 or 1 but was "2"
   line 4: expected 4 columns for a D task but found 3
   line 5: expected 5 columns for a E task but found 4
   line 6: column 3 is empty
   line 7: expected at least 3 columns but found 1
 Starting with an empty list. The file will be replaced the next time your tasks change. To keep it, close Eric, then fix or move the file.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] new
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
T | 0 | new
```

## TC76: Load special characters and spacing

**Aim:** Check that UTF-8 text, inner spaces, marker look-alikes and symbols load unchanged.

**Initial file (data/duke.txt):**
```
T | 0 | café ☕ ñ
T | 0 | read   book
E | 0 | a /tomorrow | 1 | 2
D | 1 | pay $5 | 50% off
```

**Input:**
```
list
bye
```

**Expected output:**
```
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] café ☕ ñ
 2.[T][ ] read   book
 3.[E][ ] a /tomorrow (from: 1 to: 2)
 4.[D][X] pay $5 (by: 50% off)
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
T | 0 | café ☕ ñ
T | 0 | read   book
E | 0 | a /tomorrow | 1 | 2
D | 1 | pay $5 | 50% off
```

## TC77: Loaded task numbers are validated

**Aim:** Check that task numbers are checked against the loaded tasks, not against an empty list.

**Initial file (data/duke.txt):**
```
T | 0 | a
T | 0 | b
```

**Input:**
```
mark 3
mark 1
list
bye
```

**Expected output:**
```
____________________________________________________________
 OOPS!!! Task 3 doesn't exist.
 Choose a number from 1 to 2. Type list to see the tasks.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] a
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] a
 2.[T][ ] b
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
T | 1 | a
T | 0 | b
```

## TC78: Data folder exists but the file does not

**Aim:** Check that Eric starts with an empty list when the data folder exists without a save file, and then creates the file.

**Initial file (data/duke.txt):**
```
(data folder only)
```

**Input:**
```
list
todo a
bye
```

**Expected output:**
```
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
T | 0 | a
```

## TC79: A folder where the file should be

**Aim:** Check that Eric reports a folder in place of the save file, starts with an empty list and does not crash.

**Initial file (data/duke.txt):**
```
(folder)
```

**Input:**
```
list
bye
```

**Expected output:**
```
____________________________________________________________
 OOPS!!! I couldn't load your saved tasks from data/duke.txt because it is a folder, not a file.
 Starting with an empty list. The file will be replaced the next time your tasks change. To keep it, close Eric, then fix or move the file.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC80: A file that is not valid text

**Aim:** Check that a file with bytes that are not valid UTF-8 text is rejected with a clear reason.

**Initial file (data/duke.txt):**
```
(invalid utf-8)
```

**Input:**
```
list
bye
```

**Expected output:**
```
____________________________________________________________
 OOPS!!! I couldn't load your saved tasks from data/duke.txt because it is not valid UTF-8 text.
 Starting with an empty list. The file will be replaced the next time your tasks change. To keep it, close Eric, then fix or move the file.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC81: A file with Windows line endings

**Aim:** Check that a file saved with CRLF line endings, as on Windows, loads correctly.

**Initial file (data/duke.txt):**
```
(windows line endings)
T | 1 | a
D | 0 | b | c
```

**Input:**
```
list
bye
```

**Expected output:**
```
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] a
 2.[D][ ] b (by: c)
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
T | 1 | a
D | 0 | b | c
```

## TC82: A file with only blank lines

**Aim:** Check that a file with nothing but blank or space-only lines gives an empty list without a warning.

**Initial file (data/duke.txt):**
```

   

```

**Input:**
```
list
bye
```

**Expected output:**
```
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC83: More kinds of invalid lines

**Aim:** Check the reasons for a lower-case type, a leading space, a done flag that is not 0 or 1, too many columns and an empty time.

**Initial file (data/duke.txt):**
```
t | 0 | a
 T | 0 | a
T | true | a
T | 0 | a | b
D | 0 | a | b | c
E | 0 | a |  | 4pm
```

**Input:**
```
list
bye
```

**Expected output:**
```
____________________________________________________________
 OOPS!!! I couldn't load your saved tasks from data/duke.txt because it is not in the expected format:
   line 1: unknown task type "t"
   line 2: unknown task type " T"
   line 3: the done flag must be 0 or 1 but was "true"
   line 4: expected 3 columns for a T task but found 4
   line 5: expected 4 columns for a D task but found 5
   line 6: column 4 is empty
 Starting with an empty list. The file will be replaced the next time your tasks change. To keep it, close Eric, then fix or move the file.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC84: Pipes without spaces are ordinary text

**Aim:** Check that a | that is not surrounded by spaces is part of the text, not a column separator.

**Initial file (data/duke.txt):**
```
T | 0 | a|b
T | 1 | | leading pipe
```

**Input:**
```
list
bye
```

**Expected output:**
```
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] a|b
 2.[T][X] | leading pipe
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
T | 0 | a|b
T | 1 | | leading pipe
```

## TC85: A rejected file is not modified by starting Eric

**Aim:** Check that a rejected file is left exactly as it was while no task is added, marked or unmarked.

**Initial file (data/duke.txt):**
```
X | 0 | a
```

**Input:**
```
list
blah
mark 1
todo
bye
```

**Expected output:**
```
____________________________________________________________
 OOPS!!! I couldn't load your saved tasks from data/duke.txt because it is not in the expected format:
   line 1: unknown task type "X"
 Starting with an empty list. The file will be replaced the next time your tasks change. To keep it, close Eric, then fix or move the file.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 OOPS!!! I don't know the command "blah".
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 OOPS!!! There are no tasks to mark yet.
 Add a task first, e.g. todo read book
____________________________________________________________
____________________________________________________________
 OOPS!!! The description of a todo is empty.
 Type a description after "todo", e.g. todo read book
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
X | 0 | a
```

## TC86: Text with spaced pipes is rejected

**Aim:** Check that a | with spaces around it is rejected in a todo description, and in each text part of a deadline and an event, and that nothing is saved.

**Input:**
```
todo a | b
deadline x | y /by z
deadline x /by y | z
event e | f /from 1 /to 2
event e /from 1 | f /to 2
event e /from 1 /to 2 | f
list
bye
```

**Expected output:**
```
____________________________________________________________
 OOPS!!! A task can't contain " | ", because that separates the columns of the save file.
 Remove the " | " from your command, e.g. use a comma instead.
____________________________________________________________
____________________________________________________________
 OOPS!!! A task can't contain " | ", because that separates the columns of the save file.
 Remove the " | " from your command, e.g. use a comma instead.
____________________________________________________________
____________________________________________________________
 OOPS!!! A task can't contain " | ", because that separates the columns of the save file.
 Remove the " | " from your command, e.g. use a comma instead.
____________________________________________________________
____________________________________________________________
 OOPS!!! A task can't contain " | ", because that separates the columns of the save file.
 Remove the " | " from your command, e.g. use a comma instead.
____________________________________________________________
____________________________________________________________
 OOPS!!! A task can't contain " | ", because that separates the columns of the save file.
 Remove the " | " from your command, e.g. use a comma instead.
____________________________________________________________
____________________________________________________________
 OOPS!!! A task can't contain " | ", because that separates the columns of the save file.
 Remove the " | " from your command, e.g. use a comma instead.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
(file not created)
```

## TC87: Pipes without spaces can be saved

**Aim:** Check that a | at the start, in the middle or at the end of the text is accepted and saved in a form that can be read back.

**Input:**
```
todo a|b
todo | a
todo a |
deadline x|y /by |z
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a|b
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] | a
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a |
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] x|y (by: |z)
 Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] a|b
 2.[T][ ] | a
 3.[T][ ] a |
 4.[D][ ] x|y (by: |z)
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
T | 0 | a|b
T | 0 | | a
T | 0 | a |
D | 0 | x|y | |z
```

## TC88: Text ending with a pipe loads

**Aim:** Check that a saved line whose text ends with a | is read back correctly.

**Initial file (data/duke.txt):**
```
T | 0 | a |
D | 1 | x|y | |z
```

**Input:**
```
list
bye
```

**Expected output:**
```
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] a |
 2.[D][X] x|y (by: |z)
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
T | 0 | a |
D | 1 | x|y | |z
```

## TC89: Rejected text does not disturb valid tasks

**Aim:** Check that rejected commands between valid ones change neither the list nor the saved file.

**Input:**
```
todo a
todo b | c
todo d
deadline x /by y | z
mark 2
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! A task can't contain " | ", because that separates the columns of the save file.
 Remove the " | " from your command, e.g. use a comma instead.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] d
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! A task can't contain " | ", because that separates the columns of the save file.
 Remove the " | " from your command, e.g. use a comma instead.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] d
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] a
 2.[T][X] d
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
T | 0 | a
T | 1 | d
```

## TC90: Save after delete

**Aim:** Check that deleting a task rewrites the save file without it.

**Input:**
```
todo a
todo b
todo c
delete 2
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] b
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] c
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] b
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
T | 0 | a
T | 0 | c
```

## TC91: Save after deleting the last task

**Aim:** Check that deleting the only task leaves an empty save file, not a missing one.

**Input:**
```
todo a
delete 1
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] a
 Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```

```

## TC92: Delete a loaded task

**Aim:** Check that a task loaded from the save file can be deleted and the file is rewritten.

**Initial file (data/duke.txt):**
```
T | 1 | a
D | 0 | b | c
T | 0 | d
```

**Input:**
```
delete 1
list
bye
```

**Expected output:**
```
____________________________________________________________
 Noted. I've removed this task:
   [T][X] a
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[D][ ] b (by: c)
 2.[T][ ] d
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
D | 0 | b | c
T | 0 | d
```

## TC93: Save more than 100 tasks

**Aim:** Check that there is no limit of 100 tasks, and that all of them are saved.

**Input:**
```
todo task 1
todo task 2
todo task 3
todo task 4
todo task 5
todo task 6
todo task 7
todo task 8
todo task 9
todo task 10
todo task 11
todo task 12
todo task 13
todo task 14
todo task 15
todo task 16
todo task 17
todo task 18
todo task 19
todo task 20
todo task 21
todo task 22
todo task 23
todo task 24
todo task 25
todo task 26
todo task 27
todo task 28
todo task 29
todo task 30
todo task 31
todo task 32
todo task 33
todo task 34
todo task 35
todo task 36
todo task 37
todo task 38
todo task 39
todo task 40
todo task 41
todo task 42
todo task 43
todo task 44
todo task 45
todo task 46
todo task 47
todo task 48
todo task 49
todo task 50
todo task 51
todo task 52
todo task 53
todo task 54
todo task 55
todo task 56
todo task 57
todo task 58
todo task 59
todo task 60
todo task 61
todo task 62
todo task 63
todo task 64
todo task 65
todo task 66
todo task 67
todo task 68
todo task 69
todo task 70
todo task 71
todo task 72
todo task 73
todo task 74
todo task 75
todo task 76
todo task 77
todo task 78
todo task 79
todo task 80
todo task 81
todo task 82
todo task 83
todo task 84
todo task 85
todo task 86
todo task 87
todo task 88
todo task 89
todo task 90
todo task 91
todo task 92
todo task 93
todo task 94
todo task 95
todo task 96
todo task 97
todo task 98
todo task 99
todo task 100
todo task 101
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 1
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 2
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 3
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 4
 Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 5
 Now you have 5 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 6
 Now you have 6 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 7
 Now you have 7 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 8
 Now you have 8 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 9
 Now you have 9 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 10
 Now you have 10 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 11
 Now you have 11 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 12
 Now you have 12 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 13
 Now you have 13 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 14
 Now you have 14 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 15
 Now you have 15 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 16
 Now you have 16 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 17
 Now you have 17 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 18
 Now you have 18 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 19
 Now you have 19 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 20
 Now you have 20 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 21
 Now you have 21 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 22
 Now you have 22 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 23
 Now you have 23 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 24
 Now you have 24 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 25
 Now you have 25 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 26
 Now you have 26 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 27
 Now you have 27 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 28
 Now you have 28 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 29
 Now you have 29 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 30
 Now you have 30 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 31
 Now you have 31 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 32
 Now you have 32 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 33
 Now you have 33 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 34
 Now you have 34 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 35
 Now you have 35 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 36
 Now you have 36 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 37
 Now you have 37 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 38
 Now you have 38 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 39
 Now you have 39 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 40
 Now you have 40 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 41
 Now you have 41 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 42
 Now you have 42 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 43
 Now you have 43 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 44
 Now you have 44 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 45
 Now you have 45 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 46
 Now you have 46 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 47
 Now you have 47 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 48
 Now you have 48 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 49
 Now you have 49 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 50
 Now you have 50 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 51
 Now you have 51 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 52
 Now you have 52 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 53
 Now you have 53 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 54
 Now you have 54 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 55
 Now you have 55 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 56
 Now you have 56 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 57
 Now you have 57 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 58
 Now you have 58 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 59
 Now you have 59 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 60
 Now you have 60 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 61
 Now you have 61 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 62
 Now you have 62 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 63
 Now you have 63 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 64
 Now you have 64 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 65
 Now you have 65 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 66
 Now you have 66 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 67
 Now you have 67 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 68
 Now you have 68 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 69
 Now you have 69 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 70
 Now you have 70 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 71
 Now you have 71 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 72
 Now you have 72 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 73
 Now you have 73 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 74
 Now you have 74 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 75
 Now you have 75 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 76
 Now you have 76 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 77
 Now you have 77 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 78
 Now you have 78 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 79
 Now you have 79 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 80
 Now you have 80 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 81
 Now you have 81 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 82
 Now you have 82 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 83
 Now you have 83 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 84
 Now you have 84 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 85
 Now you have 85 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 86
 Now you have 86 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 87
 Now you have 87 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 88
 Now you have 88 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 89
 Now you have 89 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 90
 Now you have 90 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 91
 Now you have 91 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 92
 Now you have 92 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 93
 Now you have 93 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 94
 Now you have 94 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 95
 Now you have 95 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 96
 Now you have 96 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 97
 Now you have 97 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 98
 Now you have 98 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 99
 Now you have 99 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 100
 Now you have 100 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 101
 Now you have 101 tasks in the list.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
T | 0 | task 1
T | 0 | task 2
T | 0 | task 3
T | 0 | task 4
T | 0 | task 5
T | 0 | task 6
T | 0 | task 7
T | 0 | task 8
T | 0 | task 9
T | 0 | task 10
T | 0 | task 11
T | 0 | task 12
T | 0 | task 13
T | 0 | task 14
T | 0 | task 15
T | 0 | task 16
T | 0 | task 17
T | 0 | task 18
T | 0 | task 19
T | 0 | task 20
T | 0 | task 21
T | 0 | task 22
T | 0 | task 23
T | 0 | task 24
T | 0 | task 25
T | 0 | task 26
T | 0 | task 27
T | 0 | task 28
T | 0 | task 29
T | 0 | task 30
T | 0 | task 31
T | 0 | task 32
T | 0 | task 33
T | 0 | task 34
T | 0 | task 35
T | 0 | task 36
T | 0 | task 37
T | 0 | task 38
T | 0 | task 39
T | 0 | task 40
T | 0 | task 41
T | 0 | task 42
T | 0 | task 43
T | 0 | task 44
T | 0 | task 45
T | 0 | task 46
T | 0 | task 47
T | 0 | task 48
T | 0 | task 49
T | 0 | task 50
T | 0 | task 51
T | 0 | task 52
T | 0 | task 53
T | 0 | task 54
T | 0 | task 55
T | 0 | task 56
T | 0 | task 57
T | 0 | task 58
T | 0 | task 59
T | 0 | task 60
T | 0 | task 61
T | 0 | task 62
T | 0 | task 63
T | 0 | task 64
T | 0 | task 65
T | 0 | task 66
T | 0 | task 67
T | 0 | task 68
T | 0 | task 69
T | 0 | task 70
T | 0 | task 71
T | 0 | task 72
T | 0 | task 73
T | 0 | task 74
T | 0 | task 75
T | 0 | task 76
T | 0 | task 77
T | 0 | task 78
T | 0 | task 79
T | 0 | task 80
T | 0 | task 81
T | 0 | task 82
T | 0 | task 83
T | 0 | task 84
T | 0 | task 85
T | 0 | task 86
T | 0 | task 87
T | 0 | task 88
T | 0 | task 89
T | 0 | task 90
T | 0 | task 91
T | 0 | task 92
T | 0 | task 93
T | 0 | task 94
T | 0 | task 95
T | 0 | task 96
T | 0 | task 97
T | 0 | task 98
T | 0 | task 99
T | 0 | task 100
T | 0 | task 101
```

## TC94: Find example from the specification

**Aim:** Check the specified example: finding a keyword lists the matching tasks, with their done status and details, numbered from 1.

**Input:**
```
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
todo join sports club
mark 1
mark 2
find book
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
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
 Nice! I've marked this task as done:
   [T][X] read book
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] return book (by: June 6th)
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][X] read book
 2.[D][X] return book (by: June 6th)
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC95: Find ignores upper and lower case

**Aim:** Check that the keyword matches regardless of case, both in the keyword and in the description.

**Input:**
```
todo Read BOOK
todo bOoKs
todo other
find book
find READ
find BoOk
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] Read BOOK
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] bOoKs
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] other
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] Read BOOK
 2.[T][ ] bOoKs
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] Read BOOK
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] Read BOOK
 2.[T][ ] bOoKs
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC96: Find with a keyword of several words

**Aim:** Check that everything after find is the keyword, so a phrase must appear as typed.

**Input:**
```
todo read book
todo book read
todo read a book
find read book
find book read
find read
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] book read
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read a book
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] read book
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] book read
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] read book
 2.[T][ ] book read
 3.[T][ ] read a book
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC97: Find with no match

**Aim:** Check that a keyword that is in no description says there are no matching tasks.

**Input:**
```
todo a
find b
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 There are no matching tasks in your list.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC98: Find on an empty list

**Aim:** Check that finding in an empty list says there are no matching tasks, and creates no file.

**Input:**
```
find book
bye
```

**Expected output:**
```
____________________________________________________________
 There are no matching tasks in your list.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
(file not created)
```

## TC99: Find searches only the description

**Aim:** Check that the dates and times of deadlines and events are not searched.

**Input:**
```
deadline return /by book
event party /from book /to book
todo y
find book
find return
find party
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return (by: book)
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] party (from: book to: book)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] y
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 There are no matching tasks in your list.
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[D][ ] return (by: book)
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[E][ ] party (from: book to: book)
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC100: Find without a keyword

**Aim:** Check that find on its own, or with only spaces, explains that a keyword is needed.

**Input:**
```
find
find   
bye
```

**Expected output:**
```
____________________________________________________________
 OOPS!!! The keyword to search for is missing.
 Type a keyword after "find", e.g. find book
____________________________________________________________
____________________________________________________________
 OOPS!!! The keyword to search for is missing.
 Type a keyword after "find", e.g. find book
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
(file not created)
```

## TC101: Look-alike find commands are unknown

**Aim:** Check that near-miss commands (extra letters, no space, wrong case) are rejected.

**Input:**
```
todo book
finds book
findbook
FIND book
Find book
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! I don't know the command "finds book".
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 OOPS!!! I don't know the command "findbook".
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 OOPS!!! I don't know the command "FIND book".
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 OOPS!!! I don't know the command "Find book".
 Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
T | 0 | book
```

## TC102: The keyword is plain text, not a pattern

**Aim:** Check that characters such as . * and [ are searched for literally.

**Input:**
```
todo a.b
todo axb
todo a*b
todo [x]
find .
find *
find [x]
find a.b
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a.b
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] axb
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a*b
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] [x]
 Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] a.b
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] a*b
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] [x]
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] a.b
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC103: Find does not change the list or the file

**Aim:** Check that finding, with or without matches, leaves the tasks and the saved file as they were.

**Input:**
```
todo a
todo b
find a
find z
find
list
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] b
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] a
____________________________________________________________
____________________________________________________________
 There are no matching tasks in your list.
____________________________________________________________
____________________________________________________________
 OOPS!!! The keyword to search for is missing.
 Type a keyword after "find", e.g. find book
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] a
 2.[T][ ] b
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
T | 0 | a
T | 0 | b
```

## TC104: Find reflects the current list

**Aim:** Check that results follow additions, deletions and status changes.

**Input:**
```
todo read book
todo return book
find book
delete 1
find book
mark 1
find book
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] return book
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] read book
 2.[T][ ] return book
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] return book
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] return book
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][X] return book
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC105: Find numbers results from 1

**Aim:** Check that results are numbered from 1 in result order, not by their number in the list.

**Input:**
```
todo a
todo target one
todo target two
find target
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] a
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] target one
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] target two
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] target one
 2.[T][ ] target two
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC106: Find in loaded tasks

**Aim:** Check that tasks loaded from the save file can be found.

**Initial file (data/duke.txt):**
```
T | 1 | read book
D | 0 | return book | June 6th
E | 0 | party | Mon | Tue
```

**Input:**
```
find book
find party
bye
```

**Expected output:**
```
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][X] read book
 2.[D][ ] return book (by: June 6th)
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[E][ ] party (from: Mon to: Tue)
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected file (data/duke.txt):**
```
T | 1 | read book
D | 0 | return book | June 6th
E | 0 | party | Mon | Tue
```

## TC107: Find with accents and symbols

**Aim:** Check that accented letters ignore case and that symbols are found.

**Input:**
```
todo Café ☕
todo plain
find CAFÉ
find ☕
find café ☕
bye
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] Café ☕
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] plain
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] Café ☕
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] Café ☕
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] Café ☕
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```
