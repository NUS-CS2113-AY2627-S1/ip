# UI Test Plan

This plan tests SamSquare through its actual console interface. Run the cases in
order. Test Case 1 starts with an empty task list. Cases 2–17 continue in
the same process; Cases 18–39 explicitly restart the process with the saved
state or replacement fixture specified in each case. Stop at the first failure.
Every `bye`, including one surrounded by spaces, must terminate its process
after the response separator.

Run the process in a fresh working directory with no `data/samsquare.txt`, so
saved personal tasks do not affect the starting state. Compile and run using
Java 25, preserving UTF-8 output for the banner.

For a packaged release, run the same cases with
`python test/run-ui-tests.py jar-tests --jar out/artifacts/ip_jar2/ip.jar`,
replacing the JAR path if necessary. The runner copies that exact JAR into the
fresh working directory and launches it with `java -jar ip.jar`; it does not
rebuild the source. Expected responses and restart checks remain the same.

The greeting appears exactly once before Test Case 1.

Expected greeting:

```text
      __________
    /            \
   /   •     •    \
  |      ᴥ         |
   \    _____     /
    \____________/
      ||    ||
      ||____||
HELLO!! I am SamSquare :D
What can I do for you?
```

After the greeting and every command response, SamSquare prints this separator:

```text
____________________________________________________________
```

The separator is part of the expected output for every case, although it is
shown once here to keep the cases readable. Leading spaces inside output blocks
are significant. Input markers `<one trailing space>` and `<three spaces>`
stand for literal spaces, including when placed before or after a command.
Leading and trailing command spaces are ignored; internal description spaces
are preserved. Padded commands with missing arguments have the same errors as
their unpadded equivalents.

## Test Case 1: List an empty collection

Input: `list`

Expected response:

```text
 Here are the tasks in your list:
```

The task list remains empty.

## Test Case 2: Reject an empty ToDo without changing state

Input:

```text
todo
list
```

Expected responses:

```text
 WAIT PAUSE!! HEY!! The description of a todo cannot be empty!
```

```text
 Here are the tasks in your list:
```

The follow-up `list` confirms that the rejected command added nothing.

## Test Case 3: Add all task types

Input:

```text
todo borrow book
deadline return book /by 2019-10-15
event project meeting /from Mon 2pm /to 4pm
list
```

Expected responses:

```text
 Got it!! I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list :)
```

```text
 Got it! I've added this task:
   [D][ ] return book (by: Oct 15 2019)
 Now you have 2 tasks in the list :)
```

```text
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 3 tasks in the list.
```

```text
 Here are the tasks in your list:
 1.[T][ ] borrow book
 2.[D][ ] return book (by: Oct 15 2019)
 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
```

This verifies formatted deadline dates and unchanged arbitrary event time strings.

## Test Case 4: Reject malformed Deadline commands without changing state

Input:

```text
deadline submit report
deadline  /by Friday
deadline submit report /by<one trailing space>
list
```

Expected responses:

```text
 WAIT PAUSE!! Hey... a deadline needs the format: deadline <task> /by <date>.
```

```text
 WAIT PAUSE!! Hey... a deadline needs the format: deadline <task> /by <date>.
```

```text
 WAIT PAUSE!! Hey... a deadline needs the format: deadline <task> /by <date>.
```

```text
 Here are the tasks in your list:
 1.[T][ ] borrow book
 2.[D][ ] return book (by: Oct 15 2019)
 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
```

## Test Case 5: Reject malformed Event commands without changing state

Input:

```text
event team meeting
event team meeting /from Monday
event  /from Monday /to Tuesday
event team meeting /from  /to Tuesday
event team meeting /from Monday /to<one trailing space>
list
```

Expected responses:

```text
 WAIT PAUSE!! An event needs the format: event <task> /from <time> /to <time>.
```

```text
 WAIT PAUSE!! An event needs both a starting and ending time.
```

```text
 WAIT PAUSE!! An event needs the format: event <task> /from <time> /to <time>.
```

```text
 WAIT PAUSE!! Broski.. an event must have a starting time!
```

```text
 WAIT PAUSE!! An event needs both a starting and ending time.
```

```text
 Here are the tasks in your list:
 1.[T][ ] borrow book
 2.[D][ ] return book (by: Oct 15 2019)
 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
```

## Test Case 6: Reject invalid mark arguments without changing state

Input:

```text
mark
mark abc
mark 0
mark -1
mark 4
list
```

Expected responses:

```text
 WAIT PAUSE!! Specify the task number to mark please!
```

```text
 WAIT PAUSE!! The task number must be a valid number.
```

```text
 WAIT PAUSE!! There is no task numbered 0.
```

```text
 WAIT PAUSE!! There is no task numbered -1.
```

```text
 WAIT PAUSE!! There is no task numbered 4.
```

```text
 Here are the tasks in your list:
 1.[T][ ] borrow book
 2.[D][ ] return book (by: Oct 15 2019)
 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
```

## Test Case 7: Mark the first and last tasks

Input:

```text
mark 1
mark 3
```

Expected responses:

```text
 WELL DONE!! I've marked this task as done:
   [T][X] borrow book
```

```text
 WELL DONE!! I've marked this task as done:
   [E][X] project meeting (from: Mon 2pm to: 4pm)
```

## Test Case 8: Reject invalid unmark arguments and preserve state

Input:

```text
unmark
unmark xyz
unmark 0
unmark 4
list
```

Expected responses:

```text
 WAIT PAUSE!! Specify the task number to unmark please!
```

```text
 WAIT PAUSE!! The task number must be a valid number.
```

```text
 WAIT PAUSE!! There is no task numbered 0.
```

```text
 WAIT PAUSE!! There is no task numbered 4.
```

```text
 Here are the tasks in your list:
 1.[T][X] borrow book
 2.[D][ ] return book (by: Oct 15 2019)
 3.[E][X] project meeting (from: Mon 2pm to: 4pm)
```

## Test Case 9: Unmark the first and last tasks

Input:

```text
unmark 1
unmark 3
```

Expected responses:

```text
 OK, I've marked this task as not done yet:
   [T][ ] borrow book
```

```text
 OK, I've marked this task as not done yet:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
```

## Test Case 10: Reject invalid delete arguments and preserve state

Input:

```text
delete
delete nope
delete 0
delete -1
delete 4
list
```

Expected responses:

```text
 WAIT PAUSE!! Specify the task number to delete please!
```

```text
 WAIT PAUSE!! HEY!! The task number must be a valid number!
```

```text
 WAIT PAUSE!! There is no task numbered 0.
```

```text
 WAIT PAUSE!! There is no task numbered -1.
```

```text
 WAIT PAUSE!! There is no task numbered 4.
```

```text
 Here are the tasks in your list:
 1.[T][ ] borrow book
 2.[D][ ] return book (by: Oct 15 2019)
 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
```

## Test Case 11: Remove from the collection and verify reindexing

Input:

```text
delete 2
list
todo buy groceries
delete 1
list
```

Expected responses:

```text
 Ahh noted! I've removed this task:
   [D][ ] return book (by: Oct 15 2019)
 Now you have 2 tasks in the list.
```

```text
 Here are the tasks in your list:
 1.[T][ ] borrow book
 2.[E][ ] project meeting (from: Mon 2pm to: 4pm)
```

```text
 Got it!! I've added this task:
   [T][ ] buy groceries
 Now you have 3 tasks in the list :)
```

```text
 Ahh noted! I've removed this task:
   [T][ ] borrow book
 Now you have 2 tasks in the list.
```

```text
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 2.[T][ ] buy groceries
```

This is the collection-specific regression case. It verifies removal from the
middle and front, automatic index shifting, and adding after a removal.

## Test Case 12: Reject whitespace and unknown commands

The first input line is `<three spaces>`: enter three literal spaces, not the
angle-bracketed text.

Input:

```text
<three spaces>
hello
list
```

Expected responses:

```text
 WAIT PAUSE!! Please don't leave your input empty D:
```

```text
 WAIT PAUSE!! Hold up... I don't recognise that command. Please use todo, deadline, event, mark, unmark, delete, find, on, list or bye.
```

```text
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 2.[T][ ] buy groceries
```

## Test Case 13: Reject empty arguments and oversized task numbers

Continues the state left by Test Case 12.

Input:

```text
mark<one trailing space>
list
unmark<three spaces>
list
delete<one trailing space>
list
mark 2147483648
list
unmark 2147483648
list
delete 2147483648
list
unmark -1
list
todo<three spaces>
list
deadline
list
event
list
```

Expected responses:

```text
 WAIT PAUSE!! Specify the task number to mark please!
```

```text
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 2.[T][ ] buy groceries
```

```text
 WAIT PAUSE!! Specify the task number to unmark please!
```

```text
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 2.[T][ ] buy groceries
```

```text
 WAIT PAUSE!! Specify the task number to delete please!
```

```text
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 2.[T][ ] buy groceries
```

```text
 WAIT PAUSE!! The task number must be a valid number.
```

```text
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 2.[T][ ] buy groceries
```

```text
 WAIT PAUSE!! The task number must be a valid number.
```

```text
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 2.[T][ ] buy groceries
```

```text
 WAIT PAUSE!! HEY!! The task number must be a valid number!
```

```text
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 2.[T][ ] buy groceries
```

```text
 WAIT PAUSE!! There is no task numbered -1.
```

```text
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 2.[T][ ] buy groceries
```

```text
 WAIT PAUSE!! HEY!! The description of a todo cannot be empty!
```

```text
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 2.[T][ ] buy groceries
```

```text
 WAIT PAUSE!! Hey... a deadline needs the format: deadline <task> /by <date>.
```

```text
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 2.[T][ ] buy groceries
```

```text
 WAIT PAUSE!! An event needs the format: event <task> /from <time> /to <time>.
```

```text
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 2.[T][ ] buy groceries
```

## Test Case 14: Accept surrounding spaces but reject unknown names and extra words

Continues the state left by Test Case 13.

Input:

```text
todoist read
list
list extra
list
bye extra
list
 list
list
list<one trailing space>
list
bye extra<one trailing space>
list
```

Expected responses:

```text
 WAIT PAUSE!! Hold up... I don't recognise that command. Please use todo, deadline, event, mark, unmark, delete, find, on, list or bye.
```

```text
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 2.[T][ ] buy groceries
```

```text
 WAIT PAUSE!! Hold up... I don't recognise that command. Please use todo, deadline, event, mark, unmark, delete, find, on, list or bye.
```

```text
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 2.[T][ ] buy groceries
```

```text
 WAIT PAUSE!! Hold up... I don't recognise that command. Please use todo, deadline, event, mark, unmark, delete, find, on, list or bye.
```

```text
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 2.[T][ ] buy groceries
```

```text
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 2.[T][ ] buy groceries
```

```text
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 2.[T][ ] buy groceries
```

```text
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 2.[T][ ] buy groceries
```

```text
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 2.[T][ ] buy groceries
```

```text
 WAIT PAUSE!! Hold up... I don't recognise that command. Please use todo, deadline, event, mark, unmark, delete, find, on, list or bye.
```

```text
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 2.[T][ ] buy groceries
```

## Test Case 15: Accept whitespace around task arguments

Continues the state left by Test Case 14.

Input:

```text
<three spaces>todo   check parser<three spaces>
<three spaces>mark   3<three spaces>
list
<three spaces>unmark   3<three spaces>
list
<three spaces>delete   3<three spaces>
list
```

Expected responses:

```text
 Got it!! I've added this task:
   [T][ ] check parser
 Now you have 3 tasks in the list :)
```

```text
 WELL DONE!! I've marked this task as done:
   [T][X] check parser
```

```text
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 2.[T][ ] buy groceries
 3.[T][X] check parser
```

```text
 OK, I've marked this task as not done yet:
   [T][ ] check parser
```

```text
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 2.[T][ ] buy groceries
 3.[T][ ] check parser
```

```text
 Ahh noted! I've removed this task:
   [T][ ] check parser
 Now you have 2 tasks in the list.
```

```text
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 2.[T][ ] buy groceries
```

## Test Case 16: Empty the list and reject stale task numbers

Continues the state left by Test Case 15.

Input:

```text
delete 2
list
mark 2
list
delete 1
list
mark 1
list
unmark 1
list
delete 1
list
todo revived
mark 1
deadline persist deadline /by 2024-02-29
event persist event /from noon /to evening
mark 3
list
```

Expected responses:

```text
 Ahh noted! I've removed this task:
   [T][ ] buy groceries
 Now you have 1 tasks in the list.
```

```text
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
```

```text
 WAIT PAUSE!! There is no task numbered 2.
```

```text
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
```

```text
 Ahh noted! I've removed this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 0 tasks in the list.
```

```text
 Here are the tasks in your list:
```

```text
 WAIT PAUSE!! There is no task numbered 1.
```

```text
 Here are the tasks in your list:
```

```text
 WAIT PAUSE!! There is no task numbered 1.
```

```text
 Here are the tasks in your list:
```

```text
 WAIT PAUSE!! There is no task numbered 1.
```

```text
 Here are the tasks in your list:
```

```text
 Got it!! I've added this task:
   [T][ ] revived
 Now you have 1 tasks in the list :)
```

```text
 WELL DONE!! I've marked this task as done:
   [T][X] revived
```

```text
 Got it! I've added this task:
   [D][ ] persist deadline (by: Feb 29 2024)
 Now you have 2 tasks in the list :)
```

```text
 Got it. I've added this task:
   [E][ ] persist event (from: noon to: evening)
 Now you have 3 tasks in the list.
```

```text
 WELL DONE!! I've marked this task as done:
   [E][X] persist event (from: noon to: evening)
```

```text
 Here are the tasks in your list:
 1.[T][X] revived
 2.[D][ ] persist deadline (by: Feb 29 2024)
 3.[E][X] persist event (from: noon to: evening)
```

## Test Case 17: Exit with mixed saved tasks

Continues the state left by Test Case 16.

Input:

```text
bye<one trailing space>
```

Expected responses:

```text
Byebye hope to see you again soon!
```

## Test Case 18: Reload all task types and update loaded tasks

Starts a new process in the same isolated working directory, retaining
the task file from Test Case 17. Compare the greeting above
before sending the first command.

Input:

```text
list
unmark 1
delete 2
list
bye
```

Expected responses:

```text
 Here are the tasks in your list:
 1.[T][X] revived
 2.[D][ ] persist deadline (by: Feb 29 2024)
 3.[E][X] persist event (from: noon to: evening)
```

```text
 OK, I've marked this task as not done yet:
   [T][ ] revived
```

```text
 Ahh noted! I've removed this task:
   [D][ ] persist deadline (by: Feb 29 2024)
 Now you have 2 tasks in the list.
```

```text
 Here are the tasks in your list:
 1.[T][ ] revived
 2.[E][X] persist event (from: noon to: evening)
```

```text
Byebye hope to see you again soon!
```

## Test Case 19: Reload updated tasks and save an empty list

Starts a new process in the same isolated working directory, retaining
the task file from Test Case 18. Compare the greeting above
before sending the first command.

Input:

```text
list
delete 2
delete 1
list
bye
```

Expected responses:

```text
 Here are the tasks in your list:
 1.[T][ ] revived
 2.[E][X] persist event (from: noon to: evening)
```

```text
 Ahh noted! I've removed this task:
   [E][X] persist event (from: noon to: evening)
 Now you have 1 tasks in the list.
```

```text
 Ahh noted! I've removed this task:
   [T][ ] revived
 Now you have 0 tasks in the list.
```

```text
 Here are the tasks in your list:
```

```text
Byebye hope to see you again soon!
```

## Test Case 20: Reload an empty saved list

Starts a new process in the same isolated working directory, retaining
the task file from Test Case 19. Compare the greeting above
before sending the first command.

Input:

```text
list
bye
```

Expected responses:

```text
 Here are the tasks in your list:
```

```text
Byebye hope to see you again soon!
```

## Test Case 21: Save immediately after adding a task

Starts a new process in the same isolated working directory, retaining
the task file from Test Case 20. Compare the greeting above
before sending the first command. No intervening mutation may save
the previous command's changes before this reload check.

Input:

```text
todo checkpoint
bye
```

Expected responses:

```text
 Got it!! I've added this task:
   [T][ ] checkpoint
 Now you have 1 tasks in the list :)
```

```text
Byebye hope to see you again soon!
```

## Test Case 22: Reload the addition and save a mark

Starts a new process in the same isolated working directory, retaining
the task file from Test Case 21. Compare the greeting above
before sending the first command. No intervening mutation may save
the previous command's changes before this reload check.

Input:

```text
list
mark 1
bye
```

Expected responses:

```text
 Here are the tasks in your list:
 1.[T][ ] checkpoint
```

```text
 WELL DONE!! I've marked this task as done:
   [T][X] checkpoint
```

```text
Byebye hope to see you again soon!
```

## Test Case 23: Reload the mark and save an unmark

Starts a new process in the same isolated working directory, retaining
the task file from Test Case 22. Compare the greeting above
before sending the first command. No intervening mutation may save
the previous command's changes before this reload check.

Input:

```text
list
unmark 1
bye
```

Expected responses:

```text
 Here are the tasks in your list:
 1.[T][X] checkpoint
```

```text
 OK, I've marked this task as not done yet:
   [T][ ] checkpoint
```

```text
Byebye hope to see you again soon!
```

## Test Case 24: Reload the unmark and save a deletion

Starts a new process in the same isolated working directory, retaining
the task file from Test Case 23. Compare the greeting above
before sending the first command. No intervening mutation may save
the previous command's changes before this reload check.

Input:

```text
list
delete 1
bye
```

Expected responses:

```text
 Here are the tasks in your list:
 1.[T][ ] checkpoint
```

```text
 Ahh noted! I've removed this task:
   [T][ ] checkpoint
 Now you have 0 tasks in the list.
```

```text
Byebye hope to see you again soon!
```

## Test Case 25: Reload the deletion

Starts a new process in the same isolated working directory, retaining
the task file from Test Case 24. Compare the greeting above
before sending the first command. No intervening mutation may save
the previous command's changes before this reload check.

Input:

```text
list
bye
```

Expected responses:

```text
 Here are the tasks in your list:
```

```text
Byebye hope to see you again soon!
```

## Test Case 26: Validate calendar dates and display formatting

Starts a new process in the same isolated working directory, retaining
the file from Test Case 25, unless a fixture below replaces it.
Compare the standard greeting before command input.

Input:

```text
deadline return book /by 2019-10-15
deadline rejected /by 2023-02-29
list
deadline rejected /by 2026-04-31
list
deadline rejected /by 2026-13-01
list
deadline rejected /by 2026-01-00
list
deadline rejected /by 2019-2-01
list
deadline rejected /by 15/10/2019
list
deadline rejected /by Friday
list
deadline rejected /by 2019-10-15 1800
list
deadline leap day /by   2024-02-29<three spaces>
list
bye
```

Expected responses:

```text
 Got it! I've added this task:
   [D][ ] return book (by: Oct 15 2019)
 Now you have 1 tasks in the list :)
```

```text
 WAIT PAUSE!! Use a valid deadline date in yyyy-MM-dd format (e.g., 2019-10-15).
```

```text
 Here are the tasks in your list:
 1.[D][ ] return book (by: Oct 15 2019)
```

```text
 WAIT PAUSE!! Use a valid deadline date in yyyy-MM-dd format (e.g., 2019-10-15).
```

```text
 Here are the tasks in your list:
 1.[D][ ] return book (by: Oct 15 2019)
```

```text
 WAIT PAUSE!! Use a valid deadline date in yyyy-MM-dd format (e.g., 2019-10-15).
```

```text
 Here are the tasks in your list:
 1.[D][ ] return book (by: Oct 15 2019)
```

```text
 WAIT PAUSE!! Use a valid deadline date in yyyy-MM-dd format (e.g., 2019-10-15).
```

```text
 Here are the tasks in your list:
 1.[D][ ] return book (by: Oct 15 2019)
```

```text
 WAIT PAUSE!! Use a valid deadline date in yyyy-MM-dd format (e.g., 2019-10-15).
```

```text
 Here are the tasks in your list:
 1.[D][ ] return book (by: Oct 15 2019)
```

```text
 WAIT PAUSE!! Use a valid deadline date in yyyy-MM-dd format (e.g., 2019-10-15).
```

```text
 Here are the tasks in your list:
 1.[D][ ] return book (by: Oct 15 2019)
```

```text
 WAIT PAUSE!! Use a valid deadline date in yyyy-MM-dd format (e.g., 2019-10-15).
```

```text
 Here are the tasks in your list:
 1.[D][ ] return book (by: Oct 15 2019)
```

```text
 WAIT PAUSE!! Use a valid deadline date in yyyy-MM-dd format (e.g., 2019-10-15).
```

```text
 Here are the tasks in your list:
 1.[D][ ] return book (by: Oct 15 2019)
```

```text
 Got it! I've added this task:
   [D][ ] leap day (by: Feb 29 2024)
 Now you have 2 tasks in the list :)
```

```text
 Here are the tasks in your list:
 1.[D][ ] return book (by: Oct 15 2019)
 2.[D][ ] leap day (by: Feb 29 2024)
```

```text
Byebye hope to see you again soon!
```

## Test Case 27: Reload dates and mark a leap-day deadline

Starts a new process in the same isolated working directory, retaining
the file from Test Case 26, unless a fixture below replaces it.
Compare the standard greeting before command input.

Input:

```text
list
mark 2
bye
```

Expected responses:

```text
 Here are the tasks in your list:
 1.[D][ ] return book (by: Oct 15 2019)
 2.[D][ ] leap day (by: Feb 29 2024)
```

```text
 WELL DONE!! I've marked this task as done:
   [D][X] leap day (by: Feb 29 2024)
```

```text
Byebye hope to see you again soon!
```

## Test Case 28: Reload the completed deadline

Starts a new process in the same isolated working directory, retaining
the file from Test Case 27, unless a fixture below replaces it.
Compare the standard greeting before command input.

Input:

```text
list
bye
```

Expected responses:

```text
 Here are the tasks in your list:
 1.[D][ ] return book (by: Oct 15 2019)
 2.[D][X] leap day (by: Feb 29 2024)
```

```text
Byebye hope to see you again soon!
```

## Test Case 29: Handle old or invalid saved dates without losing the original file

Starts a new process in the same isolated working directory, retaining
the file from Test Case 28, unless a fixture below replaces it.
Compare the standard greeting before command input.

Saved file before startup:

```text
T | 0 | before
D | 1 | valid saved date | 2024-02-29
D | 0 | legacy date | Friday
D | 0 | impossible date | 2023-02-29
E | 0 | after | noon | evening
```

Startup notice (after the greeting separator):

```text
Some saved deadlines have invalid dates and were not loaded. Original file will be backed up in data/samsquare-legacy-dates-*.txt before saving.
```

Verify legacy backup after session: exactly one backup must equal the fixture byte for byte.

Input:

```text
list
todo keep
bye
```

Expected responses:

```text
 Here are the tasks in your list:
 1.[T][ ] before
 2.[D][X] valid saved date (by: Feb 29 2024)
 3.[E][ ] after (from: noon to: evening)
```

```text
 Got it!! I've added this task:
   [T][ ] keep
 Now you have 4 tasks in the list :)
```

```text
Byebye hope to see you again soon!
```

## Test Case 30: Reload the repaired task file without another warning

Starts a new process in the same isolated working directory, retaining
the file from Test Case 29, unless a fixture below replaces it.
Compare the standard greeting before command input.

Input:

```text
list
bye
```

Expected responses:

```text
 Here are the tasks in your list:
 1.[T][ ] before
 2.[D][X] valid saved date (by: Feb 29 2024)
 3.[E][ ] after (from: noon to: evening)
 4.[T][ ] keep
```

```text
Byebye hope to see you again soon!
```

The runner also verifies that every saved deadline date uses ISO format and
that all expected output matches under a non-English default Java locale.

## Test Case 31: Find descriptions only and reject empty searches

Starts a new process in the same isolated working directory, retaining
the file from Test Case 30. Compare the standard greeting.

Input:

```text
find valid
find e
find noon
find Feb
find BEFORE
find
list
find<three spaces>
list
bye
```

Expected responses:

```text
 Here are the matching tasks in your list:
 1.[D][X] valid saved date (by: Feb 29 2024)
```

```text
 Here are the matching tasks in your list:
 1.[T][ ] before
 2.[D][X] valid saved date (by: Feb 29 2024)
 3.[E][ ] after (from: noon to: evening)
 4.[T][ ] keep
```

```text
 Here are the matching tasks in your list:
```

```text
 Here are the matching tasks in your list:
```

```text
 Here are the matching tasks in your list:
```

```text
 WAIT PAUSE!! Please specify a keyword to find.
```

```text
 Here are the tasks in your list:
 1.[T][ ] before
 2.[D][X] valid saved date (by: Feb 29 2024)
 3.[E][ ] after (from: noon to: evening)
 4.[T][ ] keep
```

```text
 WAIT PAUSE!! Please specify a keyword to find.
```

```text
 Here are the tasks in your list:
 1.[T][ ] before
 2.[D][X] valid saved date (by: Feb 29 2024)
 3.[E][ ] after (from: noon to: evening)
 4.[T][ ] keep
```

```text
Byebye hope to see you again soon!
```

## Test Case 32: Search mixed tasks and preserve original task numbering

Starts a new process in the same isolated working directory, retaining
the file from Test Case 31. Compare the standard greeting.

Input:

```text
todo read book
deadline return book /by 2019-10-15
event book club /from noon /to evening
mark 5
todo notebook
find book
find book
find   read book<three spaces>
find Book
delete 6
find book
list
bye
```

Expected responses:

```text
 Got it!! I've added this task:
   [T][ ] read book
 Now you have 5 tasks in the list :)
```

```text
 Got it! I've added this task:
   [D][ ] return book (by: Oct 15 2019)
 Now you have 6 tasks in the list :)
```

```text
 Got it. I've added this task:
   [E][ ] book club (from: noon to: evening)
 Now you have 7 tasks in the list.
```

```text
 WELL DONE!! I've marked this task as done:
   [T][X] read book
```

```text
 Got it!! I've added this task:
   [T][ ] notebook
 Now you have 8 tasks in the list :)
```

```text
 Here are the matching tasks in your list:
 1.[T][X] read book
 2.[D][ ] return book (by: Oct 15 2019)
 3.[E][ ] book club (from: noon to: evening)
 4.[T][ ] notebook
```

```text
 Here are the matching tasks in your list:
 1.[T][X] read book
 2.[D][ ] return book (by: Oct 15 2019)
 3.[E][ ] book club (from: noon to: evening)
 4.[T][ ] notebook
```

```text
 Here are the matching tasks in your list:
 1.[T][X] read book
```

```text
 Here are the matching tasks in your list:
```

```text
 Ahh noted! I've removed this task:
   [D][ ] return book (by: Oct 15 2019)
 Now you have 7 tasks in the list.
```

```text
 Here are the matching tasks in your list:
 1.[T][X] read book
 2.[E][ ] book club (from: noon to: evening)
 3.[T][ ] notebook
```

```text
 Here are the tasks in your list:
 1.[T][ ] before
 2.[D][X] valid saved date (by: Feb 29 2024)
 3.[E][ ] after (from: noon to: evening)
 4.[T][ ] keep
 5.[T][X] read book
 6.[E][ ] book club (from: noon to: evening)
 7.[T][ ] notebook
```

```text
Byebye hope to see you again soon!
```

## Test Case 33: Find after reloading the modified list

Starts a new process in the same isolated working directory, retaining
the file from Test Case 32. Compare the standard greeting.

Input:

```text
find book
list
bye
```

Expected responses:

```text
 Here are the matching tasks in your list:
 1.[T][X] read book
 2.[E][ ] book club (from: noon to: evening)
 3.[T][ ] notebook
```

```text
 Here are the tasks in your list:
 1.[T][ ] before
 2.[D][X] valid saved date (by: Feb 29 2024)
 3.[E][ ] after (from: noon to: evening)
 4.[T][ ] keep
 5.[T][X] read book
 6.[E][ ] book club (from: noon to: evening)
 7.[T][ ] notebook
```

```text
Byebye hope to see you again soon!
```

## Test Case 34: Accept padding for every command while preserving task text

Starts a new process in the isolated working directory after Case 33.
Replace its saved file with the empty fixture below and compare the standard greeting.
The double space in `read  book` must remain in the task description.
After every rejected command, `list` must show the unchanged two tasks.

Saved file before startup:

```text
```

Input:

```text
<three spaces>todo read  book<three spaces>
<three spaces>deadline return book /by 2019-10-15<three spaces>
<three spaces>event book club /from Mon 2pm /to 4pm<three spaces>
<three spaces>find book<three spaces>
<three spaces>mark 2<three spaces>
<three spaces>unmark 2<three spaces>
<three spaces>delete 1<three spaces>
<three spaces>list<three spaces>
<three spaces>deadline rejected /by Friday<three spaces>
list
<three spaces>todo<three spaces>
list
<three spaces>find<three spaces>
list
<three spaces>mark 0<three spaces>
list
<three spaces>list extra<three spaces>
list
<three spaces>bye<three spaces>
```

Expected responses:

```text
 Got it!! I've added this task:
   [T][ ] read  book
 Now you have 1 tasks in the list :)
```

```text
 Got it! I've added this task:
   [D][ ] return book (by: Oct 15 2019)
 Now you have 2 tasks in the list :)
```

```text
 Got it. I've added this task:
   [E][ ] book club (from: Mon 2pm to: 4pm)
 Now you have 3 tasks in the list.
```

```text
 Here are the matching tasks in your list:
 1.[T][ ] read  book
 2.[D][ ] return book (by: Oct 15 2019)
 3.[E][ ] book club (from: Mon 2pm to: 4pm)
```

```text
 WELL DONE!! I've marked this task as done:
   [D][X] return book (by: Oct 15 2019)
```

```text
 OK, I've marked this task as not done yet:
   [D][ ] return book (by: Oct 15 2019)
```

```text
 Ahh noted! I've removed this task:
   [T][ ] read  book
 Now you have 2 tasks in the list.
```

```text
 Here are the tasks in your list:
 1.[D][ ] return book (by: Oct 15 2019)
 2.[E][ ] book club (from: Mon 2pm to: 4pm)
```

```text
 WAIT PAUSE!! Use a valid deadline date in yyyy-MM-dd format (e.g., 2019-10-15).
```

```text
 Here are the tasks in your list:
 1.[D][ ] return book (by: Oct 15 2019)
 2.[E][ ] book club (from: Mon 2pm to: 4pm)
```

```text
 WAIT PAUSE!! HEY!! The description of a todo cannot be empty!
```

```text
 Here are the tasks in your list:
 1.[D][ ] return book (by: Oct 15 2019)
 2.[E][ ] book club (from: Mon 2pm to: 4pm)
```

```text
 WAIT PAUSE!! Please specify a keyword to find.
```

```text
 Here are the tasks in your list:
 1.[D][ ] return book (by: Oct 15 2019)
 2.[E][ ] book club (from: Mon 2pm to: 4pm)
```

```text
 WAIT PAUSE!! There is no task numbered 0.
```

```text
 Here are the tasks in your list:
 1.[D][ ] return book (by: Oct 15 2019)
 2.[E][ ] book club (from: Mon 2pm to: 4pm)
```

```text
 WAIT PAUSE!! Hold up... I don't recognise that command. Please use todo, deadline, event, mark, unmark, delete, find, on, list or bye.
```

```text
 Here are the tasks in your list:
 1.[D][ ] return book (by: Oct 15 2019)
 2.[E][ ] book club (from: Mon 2pm to: 4pm)
```

```text
Byebye hope to see you again soon!
```

## Test Case 35: Reload padded commands and exit with a leading space

Starts a new process in the same isolated working directory, retaining
the task file from Case 34. Compare the standard greeting.

Input:

```text
list
<three spaces>find return book<three spaces>
 bye
```

Expected responses:

```text
 Here are the tasks in your list:
 1.[D][ ] return book (by: Oct 15 2019)
 2.[E][ ] book club (from: Mon 2pm to: 4pm)
```

```text
 Here are the matching tasks in your list:
 1.[D][ ] return book (by: Oct 15 2019)
```

```text
Byebye hope to see you again soon!
```

## Test Case 36: Validate date searches on an empty list

Starts a new process after Case 35 using the empty saved file below.
Compare the standard greeting. Every rejected search must leave the list empty.

Saved file before startup:

```text
```

Verify task file unchanged after session: searches must not change the empty file.

Input:

```text
on 2024-02-29
on
list
<three spaces>on<three spaces>
list
on Friday
list
on 2023-02-29
list
on 2024-13-01
list
on 2024-04-31
list
on 2024-01-00
list
on 29/02/2024
list
on 2024-2-29
list
on 2024-02-29 1800
list
on 2024-02-29 extra
list
only 2024-02-29
list
bye
```

Expected responses:

```text
 Here are the deadlines on 2024-02-29:
```

```text
 WAIT PAUSE!! Please specify a date: on yyyy-MM-dd.
```

```text
 Here are the tasks in your list:
```

```text
 WAIT PAUSE!! Please specify a date: on yyyy-MM-dd.
```

```text
 Here are the tasks in your list:
```

```text
 WAIT PAUSE!! Use a valid date in yyyy-MM-dd format (e.g., 2019-10-15).
```

```text
 Here are the tasks in your list:
```

```text
 WAIT PAUSE!! Use a valid date in yyyy-MM-dd format (e.g., 2019-10-15).
```

```text
 Here are the tasks in your list:
```

```text
 WAIT PAUSE!! Use a valid date in yyyy-MM-dd format (e.g., 2019-10-15).
```

```text
 Here are the tasks in your list:
```

```text
 WAIT PAUSE!! Use a valid date in yyyy-MM-dd format (e.g., 2019-10-15).
```

```text
 Here are the tasks in your list:
```

```text
 WAIT PAUSE!! Use a valid date in yyyy-MM-dd format (e.g., 2019-10-15).
```

```text
 Here are the tasks in your list:
```

```text
 WAIT PAUSE!! Use a valid date in yyyy-MM-dd format (e.g., 2019-10-15).
```

```text
 Here are the tasks in your list:
```

```text
 WAIT PAUSE!! Use a valid date in yyyy-MM-dd format (e.g., 2019-10-15).
```

```text
 Here are the tasks in your list:
```

```text
 WAIT PAUSE!! Use a valid date in yyyy-MM-dd format (e.g., 2019-10-15).
```

```text
 Here are the tasks in your list:
```

```text
 WAIT PAUSE!! Use a valid date in yyyy-MM-dd format (e.g., 2019-10-15).
```

```text
 Here are the tasks in your list:
```

```text
 WAIT PAUSE!! Hold up... I don't recognise that command. Please use todo, deadline, event, mark, unmark, delete, find, on, list or bye.
```

```text
 Here are the tasks in your list:
```

```text
Byebye hope to see you again soon!
```

## Test Case 37: Match exact due dates in a mixed saved list without saving

Starts a new process after Case 36 with the fixture below. Compare the standard greeting.
The blank line in the fixture must survive: an unintended save would remove it.
The date-looking ToDo and event must not match. Both completed and incomplete
deadlines must match in their original order, with independent result numbering.

Saved file before startup:

```text
T | 0 | note 2024-02-29

D | 0 | before leap | 2024-02-28
D | 1 | first leap | 2024-02-29
E | 0 | date event | 2024-02-29 | 2024-02-29
D | 0 | second leap | 2024-02-29
D | 0 | after leap | 2024-03-01
D | 0 | other year | 2020-02-29
D | 0 | other month | 2024-03-29
```

Verify task file unchanged after session: preserve the entire fixture byte for byte.

Input:

```text
on 2024-02-29
<three spaces>on   2024-02-29<three spaces>
on 2020-02-29
on 2024-03-29
on 2030-01-01
<three spaces>on 2023-02-29<three spaces>
list
on
list
bye
```

Expected responses:

```text
 Here are the deadlines on 2024-02-29:
 1.[D][X] first leap (by: Feb 29 2024)
 2.[D][ ] second leap (by: Feb 29 2024)
```

```text
 Here are the deadlines on 2024-02-29:
 1.[D][X] first leap (by: Feb 29 2024)
 2.[D][ ] second leap (by: Feb 29 2024)
```

```text
 Here are the deadlines on 2020-02-29:
 1.[D][ ] other year (by: Feb 29 2020)
```

```text
 Here are the deadlines on 2024-03-29:
 1.[D][ ] other month (by: Mar 29 2024)
```

```text
 Here are the deadlines on 2030-01-01:
```

```text
 WAIT PAUSE!! Use a valid date in yyyy-MM-dd format (e.g., 2019-10-15).
```

```text
 Here are the tasks in your list:
 1.[T][ ] note 2024-02-29
 2.[D][ ] before leap (by: Feb 28 2024)
 3.[D][X] first leap (by: Feb 29 2024)
 4.[E][ ] date event (from: 2024-02-29 to: 2024-02-29)
 5.[D][ ] second leap (by: Feb 29 2024)
 6.[D][ ] after leap (by: Mar 01 2024)
 7.[D][ ] other year (by: Feb 29 2020)
 8.[D][ ] other month (by: Mar 29 2024)
```

```text
 WAIT PAUSE!! Please specify a date: on yyyy-MM-dd.
```

```text
 Here are the tasks in your list:
 1.[T][ ] note 2024-02-29
 2.[D][ ] before leap (by: Feb 28 2024)
 3.[D][X] first leap (by: Feb 29 2024)
 4.[E][ ] date event (from: 2024-02-29 to: 2024-02-29)
 5.[D][ ] second leap (by: Feb 29 2024)
 6.[D][ ] after leap (by: Mar 01 2024)
 7.[D][ ] other year (by: Feb 29 2020)
 8.[D][ ] other month (by: Mar 29 2024)
```

```text
Byebye hope to see you again soon!
```

## Test Case 38: Refresh date results after deletion and completion changes

Starts a new process in the same isolated working directory, retaining the
fixture from Case 37. Compare the standard greeting. Mutation commands use
full-list numbers, not the numbers displayed by `on`.

Input:

```text
on 2024-02-29
delete 3
on 2024-02-29
mark 4
on 2024-02-29
list
bye
```

Expected responses:

```text
 Here are the deadlines on 2024-02-29:
 1.[D][X] first leap (by: Feb 29 2024)
 2.[D][ ] second leap (by: Feb 29 2024)
```

```text
 Ahh noted! I've removed this task:
   [D][X] first leap (by: Feb 29 2024)
 Now you have 7 tasks in the list.
```

```text
 Here are the deadlines on 2024-02-29:
 1.[D][ ] second leap (by: Feb 29 2024)
```

```text
 WELL DONE!! I've marked this task as done:
   [D][X] second leap (by: Feb 29 2024)
```

```text
 Here are the deadlines on 2024-02-29:
 1.[D][X] second leap (by: Feb 29 2024)
```

```text
 Here are the tasks in your list:
 1.[T][ ] note 2024-02-29
 2.[D][ ] before leap (by: Feb 28 2024)
 3.[E][ ] date event (from: 2024-02-29 to: 2024-02-29)
 4.[D][X] second leap (by: Feb 29 2024)
 5.[D][ ] after leap (by: Mar 01 2024)
 6.[D][ ] other year (by: Feb 29 2020)
 7.[D][ ] other month (by: Mar 29 2024)
```

```text
Byebye hope to see you again soon!
```

## Test Case 39: Reload filtered tasks and remove the final match

Starts a new process in the same isolated working directory, retaining the
saved list from Case 38. Compare the standard greeting.

Input:

```text
on 2024-02-29
unmark 4
on 2024-02-29
delete 4
on 2024-02-29
on 2024-03-01
list
bye
```

Expected responses:

```text
 Here are the deadlines on 2024-02-29:
 1.[D][X] second leap (by: Feb 29 2024)
```

```text
 OK, I've marked this task as not done yet:
   [D][ ] second leap (by: Feb 29 2024)
```

```text
 Here are the deadlines on 2024-02-29:
 1.[D][ ] second leap (by: Feb 29 2024)
```

```text
 Ahh noted! I've removed this task:
   [D][ ] second leap (by: Feb 29 2024)
 Now you have 6 tasks in the list.
```

```text
 Here are the deadlines on 2024-02-29:
```

```text
 Here are the deadlines on 2024-03-01:
 1.[D][ ] after leap (by: Mar 01 2024)
```

```text
 Here are the tasks in your list:
 1.[T][ ] note 2024-02-29
 2.[D][ ] before leap (by: Feb 28 2024)
 3.[E][ ] date event (from: 2024-02-29 to: 2024-02-29)
 4.[D][ ] after leap (by: Mar 01 2024)
 5.[D][ ] other year (by: Feb 29 2020)
 6.[D][ ] other month (by: Mar 29 2024)
```

```text
Byebye hope to see you again soon!
```
