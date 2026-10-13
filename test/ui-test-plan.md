# UI Test Plan

## Test 1: Add a todo

**Aim:** Verify that the `todo` command creates a Todo task with the `[T]` prefix.

```input
todo borrow book
bye
```

```expected
____________________________________________________________
    _         _ _       
   / \   __ _| (_) ___  
  / _ \ / _` | | |/ _ \ 
 / ___ \ (_| | | | (_) |
/_/   \_\__, |_|_|\___/ 
        |___/           

Hello, I am Aglio.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 2: Add a deadline

**Aim:** Verify that the `deadline` command creates a Deadline task with the `[D]` prefix and a formatted date in the `(by: ...)` suffix.

```input
deadline return book /by 2019-10-15
bye
```

```expected
____________________________________________________________
    _         _ _       
   / \   __ _| (_) ___  
  / _ \ / _` | | |/ _ \ 
 / ___ \ (_| | | | (_) |
/_/   \_\__, |_|_|\___/ 
        |___/           

Hello, I am Aglio.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Oct 15 2019)
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 3: Add an event

**Aim:** Verify that the `event` command creates an Event task with the `[E]` prefix and `(from: ... to: ...)` suffix.

```input
event project meeting /from Mon 2pm /to 4pm
bye
```

```expected
____________________________________________________________
    _         _ _       
   / \   __ _| (_) ___  
  / _ \ / _` | | |/ _ \ 
 / ___ \ (_| | | | (_) |
/_/   \_\__, |_|_|\___/ 
        |___/           

Hello, I am Aglio.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 4: List multiple task types

**Aim:** Verify that `list` displays all task types with correct prefixes and numbering.

```input
todo borrow book
deadline return book /by 2019-10-15
event project meeting /from Mon 2pm /to 4pm
list
bye
```

```expected
____________________________________________________________
    _         _ _       
   / \   __ _| (_) ___  
  / _ \ / _` | | |/ _ \ 
 / ___ \ (_| | | | (_) |
/_/   \_\__, |_|_|\___/ 
        |___/           

Hello, I am Aglio.
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
 Here are the tasks in your list:
 1.[T][ ] borrow book
 2.[D][ ] return book (by: Oct 15 2019)
 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 5: Mark and unmark

**Aim:** Verify that `mark` and `unmark` toggle a task's done status and display the correct icon.

```input
todo borrow book
mark 1
unmark 1
bye
```

```expected
____________________________________________________________
    _         _ _       
   / \   __ _| (_) ___  
  / _ \ / _` | | |/ _ \ 
 / ___ \ (_| | | | (_) |
/_/   \_\__, |_|_|\___/ 
        |___/           

Hello, I am Aglio.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] borrow book
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] borrow book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 6: Invalid mark input

**Aim:** Verify that `mark` with a non-numeric argument shows an error instead of crashing.

```input
todo borrow book
mark abc
bye
```

```expected
____________________________________________________________
    _         _ _       
   / \   __ _| (_) ___  
  / _ \ / _` | | |/ _ \ 
 / ___ \ (_| | | | (_) |
/_/   \_\__, |_|_|\___/ 
        |___/           

Hello, I am Aglio.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! Please provide a valid task number. Usage: mark <task number>
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 7: Empty todo description

**Aim:** Verify that `todo` with no description shows an error instead of creating a task.

```input
todo
bye
```

```expected
____________________________________________________________
    _         _ _       
   / \   __ _| (_) ___  
  / _ \ / _` | | |/ _ \ 
 / ___ \ (_| | | | (_) |
/_/   \_\__, |_|_|\___/ 
        |___/           

Hello, I am Aglio.
What can I do for you?
____________________________________________________________
____________________________________________________________
 OOPS!!! The description of a todo cannot be empty.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 8: Unknown command

**Aim:** Verify that an unrecognized command shows an error instead of being added as a task.

```input
blah
bye
```

```expected
____________________________________________________________
    _         _ _       
   / \   __ _| (_) ___  
  / _ \ / _` | | |/ _ \ 
 / ___ \ (_| | | | (_) |
/_/   \_\__, |_|_|\___/ 
        |___/           

Hello, I am Aglio.
What can I do for you?
____________________________________________________________
____________________________________________________________
 OOPS!!! I'm sorry, but I don't know what that means :-(
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 9: Empty deadline description

**Aim:** Verify that `deadline` with no description shows an error instead of crashing.

```input
deadline
bye
```

```expected
____________________________________________________________
    _         _ _       
   / \   __ _| (_) ___  
  / _ \ / _` | | |/ _ \ 
 / ___ \ (_| | | | (_) |
/_/   \_\__, |_|_|\___/ 
        |___/           

Hello, I am Aglio.
What can I do for you?
____________________________________________________________
____________________________________________________________
 OOPS!!! The description of a deadline cannot be empty.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 10: Empty event description

**Aim:** Verify that `event` with no description shows an error instead of crashing.

```input
event
bye
```

```expected
____________________________________________________________
    _         _ _       
   / \   __ _| (_) ___  
  / _ \ / _` | | |/ _ \ 
 / ___ \ (_| | | | (_) |
/_/   \_\__, |_|_|\___/ 
        |___/           

Hello, I am Aglio.
What can I do for you?
____________________________________________________________
____________________________________________________________
 OOPS!!! The description of an event cannot be empty.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 11: Deadline missing /by

**Aim:** Verify that a `deadline` without the `/by` clause shows an error instead of crashing.

```input
deadline return book
bye
```

```expected
____________________________________________________________
    _         _ _       
   / \   __ _| (_) ___  
  / _ \ / _` | | |/ _ \ 
 / ___ \ (_| | | | (_) |
/_/   \_\__, |_|_|\___/ 
        |___/           

Hello, I am Aglio.
What can I do for you?
____________________________________________________________
____________________________________________________________
 OOPS!!! A deadline requires a /by clause.
 Usage: deadline <description> /by <date>
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 12: Event missing /from

**Aim:** Verify that an `event` without the `/from` clause shows an error instead of crashing.

```input
event project meeting
bye
```

```expected
____________________________________________________________
    _         _ _       
   / \   __ _| (_) ___  
  / _ \ / _` | | |/ _ \ 
 / ___ \ (_| | | | (_) |
/_/   \_\__, |_|_|\___/ 
        |___/           

Hello, I am Aglio.
What can I do for you?
____________________________________________________________
____________________________________________________________
 OOPS!!! An event requires /from and /to clauses.
 Usage: event <description> /from <start> /to <end>
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 13: Event missing /to

**Aim:** Verify that an `event` with `/from` but no `/to` shows an error instead of crashing.

```input
event project meeting /from Mon 2pm
bye
```

```expected
____________________________________________________________
    _         _ _       
   / \   __ _| (_) ___  
  / _ \ / _` | | |/ _ \ 
 / ___ \ (_| | | | (_) |
/_/   \_\__, |_|_|\___/ 
        |___/           

Hello, I am Aglio.
What can I do for you?
____________________________________________________________
____________________________________________________________
 OOPS!!! An event requires a /to clause.
 Usage: event <description> /from <start> /to <end>
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 14: Mark index out of range (too low)

**Aim:** Verify that `mark 0` shows an error instead of crashing.

```input
todo borrow book
mark 0
bye
```

```expected
____________________________________________________________
    _         _ _       
   / \   __ _| (_) ___  
  / _ \ / _` | | |/ _ \ 
 / ___ \ (_| | | | (_) |
/_/   \_\__, |_|_|\___/ 
        |___/           

Hello, I am Aglio.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! Task number 0 does not exist. You have 1 tasks.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 15: Mark index out of range (too high)

**Aim:** Verify that `mark 5` with only 1 task shows an error instead of crashing.

```input
todo borrow book
mark 5
bye
```

```expected
____________________________________________________________
    _         _ _       
   / \   __ _| (_) ___  
  / _ \ / _` | | |/ _ \ 
 / ___ \ (_| | | | (_) |
/_/   \_\__, |_|_|\___/ 
        |___/           

Hello, I am Aglio.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! Task number 5 does not exist. You have 1 tasks.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 16: Unmark index out of range

**Aim:** Verify that `unmark` with an out-of-range index shows an error instead of crashing.

```input
todo borrow book
unmark 3
bye
```

```expected
____________________________________________________________
    _         _ _       
   / \   __ _| (_) ___  
  / _ \ / _` | | |/ _ \ 
 / ___ \ (_| | | | (_) |
/_/   \_\__, |_|_|\___/ 
        |___/           

Hello, I am Aglio.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! Task number 3 does not exist. You have 1 tasks.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 17: Delete a task

**Aim:** Verify that `delete` removes a task, shows a confirmation, and renumbers the remaining tasks.

```input
todo borrow book
todo join sports club
todo read book
delete 2
list
bye
```

```expected
____________________________________________________________
    _         _ _       
   / \   __ _| (_) ___  
  / _ \ / _` | | |/ _ \ 
 / ___ \ (_| | | | (_) |
/_/   \_\__, |_|_|\___/ 
        |___/           

Hello, I am Aglio.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] join sports club
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] join sports club
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] borrow book
 2.[T][ ] read book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 18: Delete with invalid index

**Aim:** Verify that `delete` with a non-numeric argument shows an error.

```input
todo borrow book
delete abc
bye
```

```expected
____________________________________________________________
    _         _ _       
   / \   __ _| (_) ___  
  / _ \ / _` | | |/ _ \ 
 / ___ \ (_| | | | (_) |
/_/   \_\__, |_|_|\___/ 
        |___/           

Hello, I am Aglio.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! Please provide a valid task number. Usage: mark <task number>
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 19: Delete with out-of-range index

**Aim:** Verify that `delete 5` with only 1 task shows an error.

```input
todo borrow book
delete 5
bye
```

```expected
____________________________________________________________
    _         _ _       
   / \   __ _| (_) ___  
  / _ \ / _` | | |/ _ \ 
 / ___ \ (_| | | | (_) |
/_/   \_\__, |_|_|\___/ 
        |___/           

Hello, I am Aglio.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 OOPS!!! Task number 5 does not exist. You have 1 tasks.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 20: Delete the only task

**Aim:** Verify that deleting the sole task results in 0 tasks and an empty list.

```input
todo borrow book
delete 1
list
bye
```

```expected
____________________________________________________________
    _         _ _       
   / \   __ _| (_) ___  
  / _ \ / _` | | |/ _ \ 
 / ___ \ (_| | | | (_) |
/_/   \_\__, |_|_|\___/ 
        |___/           

Hello, I am Aglio.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] borrow book
 Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 21: Deadline with invalid date format

**Aim:** Verify that a deadline with an invalid date format (e.g. a plain word instead of `yyyy-MM-dd`) shows an error.

```input
deadline return book /by Sunday
bye
```

```expected
____________________________________________________________
    _         _ _       
   / \   __ _| (_) ___  
  / _ \ / _` | | |/ _ \ 
 / ___ \ (_| | | | (_) |
/_/   \_\__, |_|_|\___/ 
        |___/           

Hello, I am Aglio.
What can I do for you?
____________________________________________________________
____________________________________________________________
 OOPS!!! Invalid date format. Please use yyyy-MM-dd (e.g. 2019-10-15).
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 22: Find tasks by keyword

**Aim:** Verify that `find` returns only tasks whose descriptions contain the keyword.

```input
todo read book
todo return book
todo buy groceries
find book
bye
```

```expected
____________________________________________________________
    _         _ _       
   / \   __ _| (_) ___  
  / _ \ / _` | | |/ _ \ 
 / ___ \ (_| | | | (_) |
/_/   \_\__, |_|_|\___/ 
        |___/           

Hello, I am Aglio.
What can I do for you?
____________________________________________________________
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
 Got it. I've added this task:
   [T][ ] buy groceries
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] read book
 2.[T][ ] return book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 23: Find with no matches

**Aim:** Verify that `find` with a keyword that matches nothing shows an empty result.

```input
todo read book
find xyz
bye
```

```expected
____________________________________________________________
    _         _ _       
   / \   __ _| (_) ___  
  / _ \ / _` | | |/ _ \ 
 / ___ \ (_| | | | (_) |
/_/   \_\__, |_|_|\___/ 
        |___/           

Hello, I am Aglio.
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
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test 24: Find with empty keyword

**Aim:** Verify that `find` with no keyword shows an error.

```input
find
bye
```

```expected
____________________________________________________________
    _         _ _       
   / \   __ _| (_) ___  
  / _ \ / _` | | |/ _ \ 
 / ___ \ (_| | | | (_) |
/_/   \_\__, |_|_|\___/ 
        |___/           

Hello, I am Aglio.
What can I do for you?
____________________________________________________________
____________________________________________________________
 OOPS!!! The keyword for find cannot be empty.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
