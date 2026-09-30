# UI Test Plan

This plan is run by the `test-ui` skill. Each case lists the aim, the
inputs fed through stdin, and the expected output. Expected output is
compared after trimming trailing blank lines and trailing whitespace
on each line.

## Test cases

### TC01 Welcome and exit

Aim: Verifies that starting Maple prints the welcome banner and that
`bye` prints the farewell message.

Inputs:

```
bye
```

Expected output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

### TC02 Add a todo and list it

Aim: Verifies that `todo` adds a task with the `[T]` marker and that
`list` shows it with its position.

Inputs:

```
todo borrow book
list
bye
```

Expected output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
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

### TC03 Add a deadline and list it

Aim: Verifies that `deadline` with `/by` adds a task with the `[D]`
marker and shows the deadline.

Inputs:

```
deadline return book /by Sunday
list
bye
```

Expected output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Sunday)
 Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[D][ ] return book (by: Sunday)
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

### TC04 Add an event and list it

Aim: Verifies that `event` with `/from` and `/to` adds a task with the
`[E]` marker and shows both times.

Inputs:

```
event project meeting /from Mon 2pm /to 4pm
list
bye
```

Expected output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

### TC05 Deadline accepts arbitrary text

Aim: Verifies that the deadline field is treated as a plain string and
accepts arbitrary text without parsing it.

Inputs:

```
deadline do homework /by no idea :-p
list
bye
```

Expected output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [D][ ] do homework (by: no idea :-p)
 Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[D][ ] do homework (by: no idea :-p)
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

### TC06 Mark and unmark a task

Aim: Verifies that `mark` and `unmark` flip the done status shown in
the list.

Inputs:

```
todo read book
mark 1
list
unmark 1
list
bye
```

Expected output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

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
 Here are the tasks in your list:
 1.[T][X] read book
____________________________________________________________

____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] read book
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

### TC07 All three task types together

Aim: Verifies that todo, deadline, and event tasks coexist in one
list, keep their types, and that the added-task message shows the
running count.

Inputs:

```
todo visit new theme park
deadline submit report /by 11/10/2019 5pm
event team project meeting /from 2/10/2019 2pm /to 4pm
list
bye
```

Expected output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [T][ ] visit new theme park
 Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [D][ ] submit report (by: 11/10/2019 5pm)
 Now you have 2 tasks in the list.
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [E][ ] team project meeting (from: 2/10/2019 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] visit new theme park
 2.[D][ ] submit report (by: 11/10/2019 5pm)
 3.[E][ ] team project meeting (from: 2/10/2019 2pm to: 4pm)
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

### TC08 Empty todo and unknown command

Aim: Verifies that Maple reports an empty todo description and an
unknown command, then continues accepting commands.

Inputs:

```
todo
blah
bye
```

Expected output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Oops! A todo needs a description.
____________________________________________________________

____________________________________________________________
 Oops! I don't recognize that command.
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

### TC09 Invalid deadlines

Aim: Verifies that deadlines require both a description and a value
after `/by`.

Inputs:

```
deadline
deadline /by Sunday
deadline return book
deadline return book /by
bye
```

Expected output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Oops! A deadline needs a description.
____________________________________________________________

____________________________________________________________
 Oops! A deadline needs a description.
____________________________________________________________

____________________________________________________________
 Oops! A deadline needs a date or time after /by.
____________________________________________________________

____________________________________________________________
 Oops! A deadline needs a date or time after /by.
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

### TC10 Invalid events

Aim: Verifies that events require a description and nonempty values
after both `/from` and `/to`.

Inputs:

```
event
event /from Monday /to Tuesday
event meeting
event meeting /from Monday
event meeting /from /to Tuesday
event meeting /from Monday /to
bye
```

Expected output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Oops! An event needs a description.
____________________________________________________________

____________________________________________________________
 Oops! An event needs a description.
____________________________________________________________

____________________________________________________________
 Oops! An event needs a start after /from.
____________________________________________________________

____________________________________________________________
 Oops! An event needs an end after /to.
____________________________________________________________

____________________________________________________________
 Oops! An event needs a start after /from.
____________________________________________________________

____________________________________________________________
 Oops! An event needs an end after /to.
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

### TC11 Invalid mark and unmark indices

Aim: Verifies missing, nonnumeric, and out-of-range task numbers and
confirms that valid mark and unmark commands still work afterward.

Inputs:

```
mark
mark one
mark 1
todo read book
mark 0
mark 2
mark 1
unmark -1
unmark 1
bye
```

Expected output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Oops! Specify the number of the task to mark.
____________________________________________________________

____________________________________________________________
 Oops! A task number must be a whole number.
____________________________________________________________

____________________________________________________________
 Oops! There are no tasks to update.
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________
 Oops! Choose a task number from 1 to 1.
____________________________________________________________

____________________________________________________________
 Oops! Choose a task number from 1 to 1.
____________________________________________________________

____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] read book
____________________________________________________________

____________________________________________________________
 Oops! Choose a task number from 1 to 1.
____________________________________________________________

____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] read book
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

### TC12 Invalid additions preserve task state

Aim: Verifies that rejected todos, deadlines, and events do not enter the list while valid additions before and after them remain correct.

Inputs:

```
todo valid todo
todo
deadline bad deadline
list
deadline valid deadline /by Friday
event bad event /from Monday
list
event valid event /from Mon /to Tue
list
bye
```

Expected output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [T][ ] valid todo
 Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________
 Oops! A todo needs a description.
____________________________________________________________

____________________________________________________________
 Oops! A deadline needs a date or time after /by.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] valid todo
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [D][ ] valid deadline (by: Friday)
 Now you have 2 tasks in the list.
____________________________________________________________

____________________________________________________________
 Oops! An event needs an end after /to.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] valid todo
 2.[D][ ] valid deadline (by: Friday)
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [E][ ] valid event (from: Mon to: Tue)
 Now you have 3 tasks in the list.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] valid todo
 2.[D][ ] valid deadline (by: Friday)
 3.[E][ ] valid event (from: Mon to: Tue)
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

### TC13 Invalid indices preserve completion state

Aim: Verifies that malformed and out-of-range mark and unmark commands do not change existing task statuses.

Inputs:

```
todo first
todo second
mark 1
mark two
mark 0
mark 3
list
unmark 2
unmark -1
list
unmark 1
list
bye
```

Expected output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [T][ ] first
 Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [T][ ] second
 Now you have 2 tasks in the list.
____________________________________________________________

____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] first
____________________________________________________________

____________________________________________________________
 Oops! A task number must be a whole number.
____________________________________________________________

____________________________________________________________
 Oops! Choose a task number from 1 to 2.
____________________________________________________________

____________________________________________________________
 Oops! Choose a task number from 1 to 2.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] first
 2.[T][ ] second
____________________________________________________________

____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] second
____________________________________________________________

____________________________________________________________
 Oops! Choose a task number from 1 to 2.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] first
 2.[T][ ] second
____________________________________________________________

____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] first
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] first
 2.[T][ ] second
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

### TC14 Unknown commands preserve task state

Aim: Verifies that unknown commands, blank input, extra list arguments, and invalid deadlines leave the task list unchanged.

Inputs:

```
todo keep me
blah

list extra
deadline
list
bye
```

Expected output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [T][ ] keep me
 Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________
 Oops! I don't recognize that command.
____________________________________________________________

____________________________________________________________
 Oops! I don't recognize that command.
____________________________________________________________

____________________________________________________________
 Oops! I don't recognize that command.
____________________________________________________________

____________________________________________________________
 Oops! A deadline needs a description.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] keep me
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

### TC15 Syntax markers require token boundaries

Aim: Verifies that marker-like text remains part of descriptions, while missing standalone markers are rejected without changing state.

Inputs:

```
todo anchor
deadline study /byte syntax
list
deadline study /byte syntax /by Friday
event cook /fromage recipes /to Tuesday
list
event cook /fromage recipes /from Monday /to Tuesday
list
bye
```

Expected output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [T][ ] anchor
 Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________
 Oops! A deadline needs a date or time after /by.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] anchor
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [D][ ] study /byte syntax (by: Friday)
 Now you have 2 tasks in the list.
____________________________________________________________

____________________________________________________________
 Oops! An event needs a start after /from.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] anchor
 2.[D][ ] study /byte syntax (by: Friday)
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [E][ ] cook /fromage recipes (from: Monday to: Tuesday)
 Now you have 3 tasks in the list.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] anchor
 2.[D][ ] study /byte syntax (by: Friday)
 3.[E][ ] cook /fromage recipes (from: Monday to: Tuesday)
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

### TC16 Whitespace and errors preserve state

Aim: Verifies surrounding and repeated whitespace while alternating valid commands with invalid list and mark commands.

Inputs:

```
   todo    spaced task
 list extra
 list
 mark    1
 mark one
 list
 bye
```

Expected output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [T][ ] spaced task
 Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________
 Oops! I don't recognize that command.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] spaced task
____________________________________________________________

____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] spaced task
____________________________________________________________

____________________________________________________________
 Oops! A task number must be a whole number.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] spaced task
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

### TC17 Find matching tasks

Aim: Verifies that `find` matches task descriptions case-insensitively
and lists only matching tasks.

Inputs:

```
todo read book
deadline return BOOK /by Friday
event project meeting /from Mon /to Tue
find book
bye
```

Expected output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [D][ ] return BOOK (by: Friday)
 Now you have 2 tasks in the list.
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon to: Tue)
 Now you have 3 tasks in the list.
____________________________________________________________

____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] read book
 2.[D][ ] return BOOK (by: Friday)
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

### TC18 Empty and unmatched find commands

Aim: Verifies that an unmatched keyword displays an empty result and
that a missing keyword produces an error without changing the task list.

Inputs:

```
todo read book
find homework
find
list
bye
```

Expected output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________
 Here are the matching tasks in your list:
____________________________________________________________

____________________________________________________________
 Oops! A find command needs a keyword.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## Test session

Run on 2026-10-01. All 18 cases passed.

### TC01 Welcome and exit

Console input:

```
bye
```

Console output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

Result: PASS

### TC02 Add a todo and list it

Console input:

```
todo borrow book
list
bye
```

Console output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
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

Result: PASS

### TC03 Add a deadline and list it

Console input:

```
deadline return book /by Sunday
list
bye
```

Console output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Sunday)
 Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[D][ ] return book (by: Sunday)
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

Result: PASS

### TC04 Add an event and list it

Console input:

```
event project meeting /from Mon 2pm /to 4pm
list
bye
```

Console output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

Result: PASS

### TC05 Deadline accepts arbitrary text

Console input:

```
deadline do homework /by no idea :-p
list
bye
```

Console output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [D][ ] do homework (by: no idea :-p)
 Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[D][ ] do homework (by: no idea :-p)
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

Result: PASS

### TC06 Mark and unmark a task

Console input:

```
todo read book
mark 1
list
unmark 1
list
bye
```

Console output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

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
 Here are the tasks in your list:
 1.[T][X] read book
____________________________________________________________

____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] read book
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

Result: PASS

### TC07 All three task types together

Console input:

```
todo visit new theme park
deadline submit report /by 11/10/2019 5pm
event team project meeting /from 2/10/2019 2pm /to 4pm
list
bye
```

Console output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [T][ ] visit new theme park
 Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [D][ ] submit report (by: 11/10/2019 5pm)
 Now you have 2 tasks in the list.
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [E][ ] team project meeting (from: 2/10/2019 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] visit new theme park
 2.[D][ ] submit report (by: 11/10/2019 5pm)
 3.[E][ ] team project meeting (from: 2/10/2019 2pm to: 4pm)
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

Result: PASS

### TC08 Empty todo and unknown command

Console input:

```
todo
blah
bye
```

Console output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Oops! A todo needs a description.
____________________________________________________________

____________________________________________________________
 Oops! I don't recognize that command.
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

Result: PASS

### TC09 Invalid deadlines

Console input:

```
deadline
deadline /by Sunday
deadline return book
deadline return book /by
bye
```

Console output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Oops! A deadline needs a description.
____________________________________________________________

____________________________________________________________
 Oops! A deadline needs a description.
____________________________________________________________

____________________________________________________________
 Oops! A deadline needs a date or time after /by.
____________________________________________________________

____________________________________________________________
 Oops! A deadline needs a date or time after /by.
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

Result: PASS

### TC10 Invalid events

Console input:

```
event
event /from Monday /to Tuesday
event meeting
event meeting /from Monday
event meeting /from /to Tuesday
event meeting /from Monday /to
bye
```

Console output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Oops! An event needs a description.
____________________________________________________________

____________________________________________________________
 Oops! An event needs a description.
____________________________________________________________

____________________________________________________________
 Oops! An event needs a start after /from.
____________________________________________________________

____________________________________________________________
 Oops! An event needs an end after /to.
____________________________________________________________

____________________________________________________________
 Oops! An event needs a start after /from.
____________________________________________________________

____________________________________________________________
 Oops! An event needs an end after /to.
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

Result: PASS

### TC11 Invalid mark and unmark indices

Console input:

```
mark
mark one
mark 1
todo read book
mark 0
mark 2
mark 1
unmark -1
unmark 1
bye
```

Console output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Oops! Specify the number of the task to mark.
____________________________________________________________

____________________________________________________________
 Oops! A task number must be a whole number.
____________________________________________________________

____________________________________________________________
 Oops! There are no tasks to update.
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________
 Oops! Choose a task number from 1 to 1.
____________________________________________________________

____________________________________________________________
 Oops! Choose a task number from 1 to 1.
____________________________________________________________

____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] read book
____________________________________________________________

____________________________________________________________
 Oops! Choose a task number from 1 to 1.
____________________________________________________________

____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] read book
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

Result: PASS

### TC12 Invalid additions preserve task state

Console input:

```
todo valid todo
todo
deadline bad deadline
list
deadline valid deadline /by Friday
event bad event /from Monday
list
event valid event /from Mon /to Tue
list
bye
```

Console output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [T][ ] valid todo
 Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________
 Oops! A todo needs a description.
____________________________________________________________

____________________________________________________________
 Oops! A deadline needs a date or time after /by.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] valid todo
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [D][ ] valid deadline (by: Friday)
 Now you have 2 tasks in the list.
____________________________________________________________

____________________________________________________________
 Oops! An event needs an end after /to.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] valid todo
 2.[D][ ] valid deadline (by: Friday)
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [E][ ] valid event (from: Mon to: Tue)
 Now you have 3 tasks in the list.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] valid todo
 2.[D][ ] valid deadline (by: Friday)
 3.[E][ ] valid event (from: Mon to: Tue)
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

Result: PASS

### TC13 Invalid indices preserve completion state

Console input:

```
todo first
todo second
mark 1
mark two
mark 0
mark 3
list
unmark 2
unmark -1
list
unmark 1
list
bye
```

Console output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [T][ ] first
 Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [T][ ] second
 Now you have 2 tasks in the list.
____________________________________________________________

____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] first
____________________________________________________________

____________________________________________________________
 Oops! A task number must be a whole number.
____________________________________________________________

____________________________________________________________
 Oops! Choose a task number from 1 to 2.
____________________________________________________________

____________________________________________________________
 Oops! Choose a task number from 1 to 2.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] first
 2.[T][ ] second
____________________________________________________________

____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] second
____________________________________________________________

____________________________________________________________
 Oops! Choose a task number from 1 to 2.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] first
 2.[T][ ] second
____________________________________________________________

____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] first
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] first
 2.[T][ ] second
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

Result: PASS

### TC14 Unknown commands preserve task state

Console input:

```
todo keep me
blah

list extra
deadline
list
bye
```

Console output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [T][ ] keep me
 Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________
 Oops! I don't recognize that command.
____________________________________________________________

____________________________________________________________
 Oops! I don't recognize that command.
____________________________________________________________

____________________________________________________________
 Oops! I don't recognize that command.
____________________________________________________________

____________________________________________________________
 Oops! A deadline needs a description.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] keep me
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

Result: PASS

### TC15 Syntax markers require token boundaries

Console input:

```
todo anchor
deadline study /byte syntax
list
deadline study /byte syntax /by Friday
event cook /fromage recipes /to Tuesday
list
event cook /fromage recipes /from Monday /to Tuesday
list
bye
```

Console output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [T][ ] anchor
 Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________
 Oops! A deadline needs a date or time after /by.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] anchor
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [D][ ] study /byte syntax (by: Friday)
 Now you have 2 tasks in the list.
____________________________________________________________

____________________________________________________________
 Oops! An event needs a start after /from.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] anchor
 2.[D][ ] study /byte syntax (by: Friday)
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [E][ ] cook /fromage recipes (from: Monday to: Tuesday)
 Now you have 3 tasks in the list.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] anchor
 2.[D][ ] study /byte syntax (by: Friday)
 3.[E][ ] cook /fromage recipes (from: Monday to: Tuesday)
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

Result: PASS

### TC16 Whitespace and errors preserve state

Console input:

```
   todo    spaced task
 list extra
 list
 mark    1
 mark one
 list
 bye
```

Console output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [T][ ] spaced task
 Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________
 Oops! I don't recognize that command.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] spaced task
____________________________________________________________

____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] spaced task
____________________________________________________________

____________________________________________________________
 Oops! A task number must be a whole number.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] spaced task
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

Result: PASS

### TC17 Find matching tasks

Console input:

```
todo read book
deadline return BOOK /by Friday
event project meeting /from Mon /to Tue
find book
bye
```

Console output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [D][ ] return BOOK (by: Friday)
 Now you have 2 tasks in the list.
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon to: Tue)
 Now you have 3 tasks in the list.
____________________________________________________________

____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] read book
 2.[D][ ] return BOOK (by: Friday)
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

Result: PASS

### TC18 Empty and unmatched find commands

Console input:

```
todo read book
find homework
find
list
bye
```

Console output:

```
____________________________________________________________
   __  ___          __
  /  |/  /__ ____  / /__
 / /|_/ / _ `/ _ \/ / -_)
/_/  /_/\_,_/ .__/_/\__/
           /_/
 Hello! I'm Maple.
 What can I do for you?
____________________________________________________________

____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________
 Here are the matching tasks in your list:
____________________________________________________________

____________________________________________________________
 Oops! A find command needs a keyword.
____________________________________________________________

____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
____________________________________________________________

____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

Result: PASS
