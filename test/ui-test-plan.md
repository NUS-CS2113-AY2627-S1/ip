# Dash UI Test Plan

## Test 1: Add and list all task types

Aim: Verifies that to-dos, deadlines, and events are stored with the correct
type-specific display format and that completed tasks retain their status.

### Input

```text
todo borrow book
deadline return book /by 2019-10-15
event project meeting /from Mon 2pm /to 4pm
mark 1
list
bye
```

### Expected output

```text
 ____              _     
|  _ \  __ _ ___| |__  
| | | |/ _` / __| '_ \ 
| |_| | (_| \__ \ | | |
|____/ \__,_|___/_| |_|

____________________________________________________________
Hello! I'm Dash.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Oct 15 2019)
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
 2.[D][ ] return book (by: Oct 15 2019)
 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 2: Preserve free-form deadline text and persist loaded tasks

Aim: Verifies that tasks from the previous session are loaded on startup, a
new deadline's date is stored and formatted as a date, and marking and unmarking
continue to work.

### Input

```text
deadline do homework /by 2019-12-31
mark 4
unmark 4
list
bye
```

### Expected output

```text
 ____              _     
|  _ \  __ _ ___| |__  
| | | |/ _` / __| '_ \ 
| |_| | (_| \__ \ | | |
|____/ \__,_|___/_| |_|

____________________________________________________________
Hello! I'm Dash.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] do homework (by: Dec 31 2019)
 Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] do homework (by: Dec 31 2019)
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [D][ ] do homework (by: Dec 31 2019)
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] borrow book
 2.[D][ ] return book (by: Oct 15 2019)
 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 4.[D][ ] do homework (by: Dec 31 2019)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 3: Reject invalid user commands

Aim: Verifies that empty task details, unknown commands, missing deadline or
event markers, and invalid mark numbers produce error messages instead of
crashing or silently ignoring the input.

### Input

```text
todo
blah
deadline
deadline return book
deadline /by 2019-10-15
event project meeting
event project meeting /from Mon 2pm
mark 10
todo join sports club
mark abc
mark 10
list
bye
```

### Expected output

```text
 ____              _     
|  _ \  __ _ ___| |__  
| | | |/ _` / __| '_ \ 
| |_| | (_| \__ \ | | |
|____/ \__,_|___/_| |_|

____________________________________________________________
Hello! I'm Dash.
What can I do for you?
____________________________________________________________
____________________________________________________________
 A to-do needs a description. Try: todo borrow book
____________________________________________________________
____________________________________________________________
 I don't recognize that command. Try list, todo, deadline, event, mark, unmark, delete, on, find, or bye.
____________________________________________________________
____________________________________________________________
 A deadline needs a /by date. Try: deadline return book /by 2019-10-15
____________________________________________________________
____________________________________________________________
 A deadline needs a /by date. Try: deadline return book /by 2019-10-15
____________________________________________________________
____________________________________________________________
 A deadline needs a description before /by.
____________________________________________________________
____________________________________________________________
 An event needs /from and /to times. Try: event meeting /from Mon 2pm /to 4pm
____________________________________________________________
____________________________________________________________
 An event needs /from and /to times. Try: event meeting /from Mon 2pm /to 4pm
____________________________________________________________
____________________________________________________________
 There is no task numbered 10.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] join sports club
 Now you have 5 tasks in the list.
____________________________________________________________
____________________________________________________________
 That is not a valid task number. Try something like: mark 1
____________________________________________________________
____________________________________________________________
 There is no task numbered 10.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] borrow book
 2.[D][ ] return book (by: Oct 15 2019)
 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 4.[D][ ] do homework (by: Dec 31 2019)
 5.[T][ ] join sports club
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 4: Delete tasks and reject invalid delete inputs

Aim: Verifies that tasks can be removed by index, the remaining task count is
updated correctly, and invalid delete attempts show informative error messages.

### Input

```text
delete 5
delete 4
delete 2
list
delete
delete abc
delete 0
delete 10
delete 1
delete 1
list
bye
```

### Expected output

```text
 ____              _     
|  _ \  __ _ ___| |__  
| | | |/ _` / __| '_ \ 
| |_| | (_| \__ \ | | |
|____/ \__,_|___/_| |_|

____________________________________________________________
Hello! I'm Dash.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] join sports club
 Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [D][ ] do homework (by: Dec 31 2019)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [D][ ] return book (by: Oct 15 2019)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] borrow book
 2.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 Please give the task number to delete. Try: delete 1
____________________________________________________________
____________________________________________________________
 That is not a valid task number. Try something like: delete 1
____________________________________________________________
____________________________________________________________
 There is no task numbered 0.
____________________________________________________________
____________________________________________________________
 There is no task numbered 10.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][X] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 5: Filter tasks occurring on a specific date

Aim: Verifies that the on command filters deadlines and events occurring on a
given date, reports when no tasks match, and rejects missing or malformed dates.

### Input

```text
deadline return book /by 2019-10-15
event project conference /from 2019-10-15 /to 2019-10-17
on 2019-10-15
on 2019-10-16
on 2025-01-01
on
on not-a-date
delete 2
delete 1
bye
```

### Expected output

```text
 ____              _     
|  _ \  __ _ ___| |__  
| | | |/ _` / __| '_ \ 
| |_| | (_| \__ \ | | |
|____/ \__,_|___/_| |_|

____________________________________________________________
Hello! I'm Dash.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Oct 15 2019)
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project conference (from: 2019-10-15 to: 2019-10-17)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks occurring on Oct 15 2019:
 1.[D][ ] return book (by: Oct 15 2019)
 2.[E][ ] project conference (from: 2019-10-15 to: 2019-10-17)
____________________________________________________________
____________________________________________________________
 Here are the tasks occurring on Oct 16 2019:
 1.[E][ ] project conference (from: 2019-10-15 to: 2019-10-17)
____________________________________________________________
____________________________________________________________
 There are no tasks occurring on Jan 01 2025.
____________________________________________________________
____________________________________________________________
 Please specify a date in yyyy-mm-dd format. Try: on 2019-10-15
____________________________________________________________
____________________________________________________________
 Please provide a valid date in yyyy-mm-dd format. Try: on 2019-10-15
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [E][ ] project conference (from: 2019-10-15 to: 2019-10-17)
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [D][ ] return book (by: Oct 15 2019)
 Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 6: Find tasks by keyword

Aim: Verifies that the find command searches task descriptions for a keyword,
displays matching tasks with their current completion status, handles cases
with no matches, and rejects an empty keyword.

### Input

```text
todo read book
deadline return book /by 2019-10-15
event book club /from Mon 6pm /to 8pm
todo buy groceries
mark 1
find book
find groceries
find homework
find
bye
```

### Expected output

```text
 ____              _     
|  _ \  __ _ ___| |__  
| | | |/ _` / __| '_ \ 
| |_| | (_| \__ \ | | |
|____/ \__,_|___/_| |_|

____________________________________________________________
Hello! I'm Dash.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Oct 15 2019)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] book club (from: Mon 6pm to: 8pm)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] buy groceries
 Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] read book
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][X] read book
 2.[D][ ] return book (by: Oct 15 2019)
 3.[E][ ] book club (from: Mon 6pm to: 8pm)
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] buy groceries
____________________________________________________________
____________________________________________________________
 There are no matching tasks in your list.
____________________________________________________________
____________________________________________________________
 Please specify a keyword to search for. Try: find book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
