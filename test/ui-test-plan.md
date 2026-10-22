# UI Test Plan

Run with Java 25. Compare command responses exactly, including separators and blank lines. The unchanged startup banner and greeting are excluded from comparison; full actual sessions are recorded separately. Every session ends with bye and checks the farewell.

## Add a task

**Aim:** Accept and list a valid todo

**Inputs:**

```text
todo read book
list
bye
```

**Expected output (after greeting):**

```text
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

## Empty todos and unknown commands

**Aim:** Reject incorrect inputs without adding tasks

**Inputs:**

```text
todo
todo   
blah

marking 1
unmark1
list
bye
```

**Expected output (after greeting):**

```text
    ____________________________________________________________
     OOPS!!! The description of a todo cannot be empty.
    ____________________________________________________________

    ____________________________________________________________
     OOPS!!! The description of a todo cannot be empty.
    ____________________________________________________________

    ____________________________________________________________
     OOPS!!! I'm sorry, but I don't know what that means :-(
    ____________________________________________________________

    ____________________________________________________________
     OOPS!!! I'm sorry, but I don't know what that means :-(
    ____________________________________________________________

    ____________________________________________________________
     OOPS!!! I'm sorry, but I don't know what that means :-(
    ____________________________________________________________

    ____________________________________________________________
     OOPS!!! I'm sorry, but I don't know what that means :-(
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
    ____________________________________________________________

    ____________________________________________________________
 Bye. Hope to see you again soon!
   ____________________________________________________________
```

## Valid tasks and task numbers

**Aim:** Continue after invalid numbers and preserve task behavior

**Inputs:**

```text
deadline homework /by Friday
event lunch /from noon /to evening
mark
mark abc
mark 0
mark 3
mark 9999999999999999
unmark -1
mark 1
unmark 1
list
bye
```

**Expected output (after greeting):**

```text
    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] homework (by: Friday)
     Now you have 1 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [E][ ] lunch (from: noon to: evening)
     Now you have 2 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Invalid task number.
    ____________________________________________________________

    ____________________________________________________________
     Invalid task number.
    ____________________________________________________________

    ____________________________________________________________
     Invalid task number.
    ____________________________________________________________

    ____________________________________________________________
     Invalid task number.
    ____________________________________________________________

    ____________________________________________________________
     Invalid task number.
    ____________________________________________________________

    ____________________________________________________________
     Invalid task number.
    ____________________________________________________________

    ____________________________________________________________
     Nice! I've marked this task as done:
       [X] homework
    ____________________________________________________________

    ____________________________________________________________
     OK, I've marked this task as not done yet:
       [ ] homework
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[D][ ] homework (by: Friday)
     2.[E][ ] lunch (from: noon to: evening)
    ____________________________________________________________

    ____________________________________________________________
 Bye. Hope to see you again soon!
   ____________________________________________________________
```

## Find tasks by description

**Aim:** Match case-sensitive substrings across task types, preserve order and status, and handle empty searches

**Inputs:**

```text
find book
todo read book
todo unrelated
deadline return book /by June 6th
event book club /from noon /to evening
mark 1
  find	 book  
find ook
find return book
find Book
find June
find missing
find
find 	 
finder book
list
bye
```

**Expected output (after greeting):**

```text
    ____________________________________________________________
     Here are the matching tasks in your list:
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] unrelated
     Now you have 2 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] return book (by: June 6th)
     Now you have 3 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [E][ ] book club (from: noon to: evening)
     Now you have 4 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Nice! I've marked this task as done:
       [X] read book
    ____________________________________________________________

    ____________________________________________________________
     Here are the matching tasks in your list:
     1.[T][X] read book
     2.[D][ ] return book (by: June 6th)
     3.[E][ ] book club (from: noon to: evening)
    ____________________________________________________________

    ____________________________________________________________
     Here are the matching tasks in your list:
     1.[T][X] read book
     2.[D][ ] return book (by: June 6th)
     3.[E][ ] book club (from: noon to: evening)
    ____________________________________________________________

    ____________________________________________________________
     Here are the matching tasks in your list:
     1.[D][ ] return book (by: June 6th)
    ____________________________________________________________

    ____________________________________________________________
     Here are the matching tasks in your list:
    ____________________________________________________________

    ____________________________________________________________
     Here are the matching tasks in your list:
    ____________________________________________________________

    ____________________________________________________________
     Here are the matching tasks in your list:
    ____________________________________________________________

    ____________________________________________________________
     OOPS!!! The keyword for find cannot be empty.
    ____________________________________________________________

    ____________________________________________________________
     OOPS!!! The keyword for find cannot be empty.
    ____________________________________________________________

    ____________________________________________________________
     OOPS!!! I'm sorry, but I don't know what that means :-(
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] read book
     2.[T][ ] unrelated
     3.[D][ ] return book (by: June 6th)
     4.[E][ ] book club (from: noon to: evening)
    ____________________________________________________________

    ____________________________________________________________
 Bye. Hope to see you again soon!
   ____________________________________________________________
```

## Errors between additions and completion changes

**Aim:** Check counts, ordering and completion flags immediately after rejected inputs

**Inputs:**

```text
list
todo
todo first
blah
list
mark 1
todo
list
todo second
blah second
list
unmark 1

list
bye
```

**Expected output (after greeting):**

```text
    ____________________________________________________________
     Here are the tasks in your list:
    ____________________________________________________________

    ____________________________________________________________
     OOPS!!! The description of a todo cannot be empty.
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] first
     Now you have 1 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     OOPS!!! I'm sorry, but I don't know what that means :-(
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] first
    ____________________________________________________________

    ____________________________________________________________
     Nice! I've marked this task as done:
       [X] first
    ____________________________________________________________

    ____________________________________________________________
     OOPS!!! The description of a todo cannot be empty.
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] first
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] second
     Now you have 2 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     OOPS!!! I'm sorry, but I don't know what that means :-(
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] first
     2.[T][ ] second
    ____________________________________________________________

    ____________________________________________________________
     OK, I've marked this task as not done yet:
       [ ] first
    ____________________________________________________________

    ____________________________________________________________
     OOPS!!! I'm sorry, but I don't know what that means :-(
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

## Whitespace and command boundaries

**Aim:** Reject blank and misspelled commands while preserving descriptions and accepting whitespace around valid todos

**Inputs:**

```text
 	 
  todo	read book  
todo	  
list
todoish read
mark 1
TODO read
list
todo bye /by Friday
bye now
list
list extra
todo read book
list
bye
```

**Expected output (after greeting):**

```text
    ____________________________________________________________
     OOPS!!! I'm sorry, but I don't know what that means :-(
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     OOPS!!! The description of a todo cannot be empty.
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read book
    ____________________________________________________________

    ____________________________________________________________
     OOPS!!! I'm sorry, but I don't know what that means :-(
    ____________________________________________________________

    ____________________________________________________________
     Nice! I've marked this task as done:
       [X] read book
    ____________________________________________________________

    ____________________________________________________________
     OOPS!!! I'm sorry, but I don't know what that means :-(
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] read book
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] bye /by Friday
     Now you have 2 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     OOPS!!! I'm sorry, but I don't know what that means :-(
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] read book
     2.[T][ ] bye /by Friday
    ____________________________________________________________

    ____________________________________________________________
     OOPS!!! I'm sorry, but I don't know what that means :-(
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] read book
     Now you have 3 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] read book
     2.[T][ ] bye /by Friday
     3.[T][ ] read book
    ____________________________________________________________

    ____________________________________________________________
 Bye. Hope to see you again soon!
   ____________________________________________________________
```

## Invalid indexes between valid status updates

**Aim:** Verify missing, noninteger, out-of-range and overflowing indexes cannot change existing task flags

**Inputs:**

```text
mark 1
todo first
unmark
todo second
mark 1 2
mark 2
unmark 0
list
mark 3
mark 1
unmark -2147483648
list
unmark 2147483648
unmark 2
mark 1.5
list
unmark abc
unmark 2
list
bye
```

**Expected output (after greeting):**

```text
    ____________________________________________________________
     Invalid task number.
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] first
     Now you have 1 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Invalid task number.
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] second
     Now you have 2 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Invalid task number.
    ____________________________________________________________

    ____________________________________________________________
     Nice! I've marked this task as done:
       [X] second
    ____________________________________________________________

    ____________________________________________________________
     Invalid task number.
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] first
     2.[T][X] second
    ____________________________________________________________

    ____________________________________________________________
     Invalid task number.
    ____________________________________________________________

    ____________________________________________________________
     Nice! I've marked this task as done:
       [X] first
    ____________________________________________________________

    ____________________________________________________________
     Invalid task number.
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] first
     2.[T][X] second
    ____________________________________________________________

    ____________________________________________________________
     Invalid task number.
    ____________________________________________________________

    ____________________________________________________________
     OK, I've marked this task as not done yet:
       [ ] second
    ____________________________________________________________

    ____________________________________________________________
     Invalid task number.
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] first
     2.[T][ ] second
    ____________________________________________________________

    ____________________________________________________________
     Invalid task number.
    ____________________________________________________________

    ____________________________________________________________
     OK, I've marked this task as not done yet:
       [ ] second
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] first
     2.[T][ ] second
    ____________________________________________________________

    ____________________________________________________________
 Bye. Hope to see you again soon!
   ____________________________________________________________
```

## Delete tasks and reuse list positions

**Aim:** Delete middle, first and last tasks, preserve status and order, and add after deletion

**Inputs:**

```text
todo first
event project meeting /from Aug 6th 2pm /to 4pm
deadline last /by Friday
mark 3
delete 2
list
delete 1
  delete	1  
list
delete 1
todo new
delete
delete abc
delete 0
delete -1
delete 2
delete 2147483648
delete -2147483648
delete 1 2
delete1
list
bye
```

**Expected output (after greeting):**

```text
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] first
     Now you have 1 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
     Now you have 2 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] last (by: Friday)
     Now you have 3 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Nice! I've marked this task as done:
       [X] last
    ____________________________________________________________

    ____________________________________________________________
     Noted. I've removed this task:
       [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
     Now you have 2 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] first
     2.[D][X] last (by: Friday)
    ____________________________________________________________

    ____________________________________________________________
     Noted. I've removed this task:
       [T][ ] first
     Now you have 1 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Noted. I've removed this task:
       [D][X] last (by: Friday)
     Now you have 0 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
    ____________________________________________________________

    ____________________________________________________________
     Invalid task number.
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] new
     Now you have 1 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Invalid task number.
    ____________________________________________________________

    ____________________________________________________________
     Invalid task number.
    ____________________________________________________________

    ____________________________________________________________
     Invalid task number.
    ____________________________________________________________

    ____________________________________________________________
     Invalid task number.
    ____________________________________________________________

    ____________________________________________________________
     Invalid task number.
    ____________________________________________________________

    ____________________________________________________________
     Invalid task number.
    ____________________________________________________________

    ____________________________________________________________
     Invalid task number.
    ____________________________________________________________

    ____________________________________________________________
     Invalid task number.
    ____________________________________________________________

    ____________________________________________________________
     OOPS!!! I'm sorry, but I don't know what that means :-(
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] new
    ____________________________________________________________

    ____________________________________________________________
 Bye. Hope to see you again soon!
   ____________________________________________________________
```

## Collection grows beyond 100 tasks

**Aim:** Store more than 100 tasks and preserve indexes and order after deletion and addition

**Inputs:**

```text
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
delete 100
mark 100
todo replacement
list
bye
```

**Expected output (after greeting):**

```text
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
       [T][ ] task 100
     Now you have 100 tasks in the list.
    ____________________________________________________________

    ____________________________________________________________
     Nice! I've marked this task as done:
       [X] task 101
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] replacement
     Now you have 101 tasks in the list.
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
     10.[T][ ] task 10
     11.[T][ ] task 11
     12.[T][ ] task 12
     13.[T][ ] task 13
     14.[T][ ] task 14
     15.[T][ ] task 15
     16.[T][ ] task 16
     17.[T][ ] task 17
     18.[T][ ] task 18
     19.[T][ ] task 19
     20.[T][ ] task 20
     21.[T][ ] task 21
     22.[T][ ] task 22
     23.[T][ ] task 23
     24.[T][ ] task 24
     25.[T][ ] task 25
     26.[T][ ] task 26
     27.[T][ ] task 27
     28.[T][ ] task 28
     29.[T][ ] task 29
     30.[T][ ] task 30
     31.[T][ ] task 31
     32.[T][ ] task 32
     33.[T][ ] task 33
     34.[T][ ] task 34
     35.[T][ ] task 35
     36.[T][ ] task 36
     37.[T][ ] task 37
     38.[T][ ] task 38
     39.[T][ ] task 39
     40.[T][ ] task 40
     41.[T][ ] task 41
     42.[T][ ] task 42
     43.[T][ ] task 43
     44.[T][ ] task 44
     45.[T][ ] task 45
     46.[T][ ] task 46
     47.[T][ ] task 47
     48.[T][ ] task 48
     49.[T][ ] task 49
     50.[T][ ] task 50
     51.[T][ ] task 51
     52.[T][ ] task 52
     53.[T][ ] task 53
     54.[T][ ] task 54
     55.[T][ ] task 55
     56.[T][ ] task 56
     57.[T][ ] task 57
     58.[T][ ] task 58
     59.[T][ ] task 59
     60.[T][ ] task 60
     61.[T][ ] task 61
     62.[T][ ] task 62
     63.[T][ ] task 63
     64.[T][ ] task 64
     65.[T][ ] task 65
     66.[T][ ] task 66
     67.[T][ ] task 67
     68.[T][ ] task 68
     69.[T][ ] task 69
     70.[T][ ] task 70
     71.[T][ ] task 71
     72.[T][ ] task 72
     73.[T][ ] task 73
     74.[T][ ] task 74
     75.[T][ ] task 75
     76.[T][ ] task 76
     77.[T][ ] task 77
     78.[T][ ] task 78
     79.[T][ ] task 79
     80.[T][ ] task 80
     81.[T][ ] task 81
     82.[T][ ] task 82
     83.[T][ ] task 83
     84.[T][ ] task 84
     85.[T][ ] task 85
     86.[T][ ] task 86
     87.[T][ ] task 87
     88.[T][ ] task 88
     89.[T][ ] task 89
     90.[T][ ] task 90
     91.[T][ ] task 91
     92.[T][ ] task 92
     93.[T][ ] task 93
     94.[T][ ] task 94
     95.[T][ ] task 95
     96.[T][ ] task 96
     97.[T][ ] task 97
     98.[T][ ] task 98
     99.[T][ ] task 99
     100.[T][X] task 101
     101.[T][ ] replacement
    ____________________________________________________________

    ____________________________________________________________
 Bye. Hope to see you again soon!
   ____________________________________________________________
```

## Automatic file saving

**Aim:** Verify the file is created and replaced after each task change while the chatbot is still running.
Run `python test/test-storage.py` after the UI runner compiles the application. Each run uses an isolated
directory under `build/ui-test` to protect real task data.

**Inputs:** `todo read book`, `deadline homework /by Friday`, `event lunch /from noon /to evening`,
`mark 1`, `unmark 1`, `list`, `blah`, `bye` (one command at a time).

**Expected output:** Normal addition and completion confirmations, the three tasks in the list,
the existing unknown-command error, and the farewell. Full console input/output is recorded in
`test/storage-test-sessions.md`. Existing UI cases verify the exact response text.

**Expected file:** After each addition, `data/mybff.txt` contains all tasks added so far, one per line:

```text
T | 0 | read book
D | 0 | homework | Friday
E | 0 | lunch | noon | evening
```

Mark changes the first line to `T | 1 | read book`; unmark restores `T | 0 | read book`.
List, unknown commands, and bye leave the file unchanged. Startup alone creates no file.
New saves use a `MyBff storage v3` header and readable text fields with backslash escapes. The snapshots above
show decoded fields. Existing pipe-separated and Base64 v2 files remain readable.

## Storage errors and special text

**Aim:** Preserve existing data on failure and avoid crashes. Run `python test/test-storage-errors.py`.

**Inputs:** For each malformed file (invalid type/status/field count, empty description, bad UTF-8,
or bad Base64), send `todo replacement`, `bye`.

**Expected output:** `OOPS!!! Could not load data/mybff.txt. Your saved file has not been changed.
Check the file and restart.` The application stops without changing the file.
A directory at the file path also gives this error. A blocked data directory either gives the load
error or rejects an addition with `OOPS!!! Could not save data/mybff.txt. No changes were made.
Check the file and try again.`

**Additional inputs:** Load a BOM-prefixed file with blank lines and a completed task; send `list`,
`todo pipes | and \ paths`, `bye`, then restart and list again. Expect both tasks with exact text and status.
Load 101 numbered tasks and send `list`, `todo next`, `bye`: expect all tasks in order and an addition
confirmation reporting 102 tasks. Restart with `list`, `bye` and expect all 102 tasks in order.
Full inputs and actual outputs are recorded in `test/storage-error-sessions.md`.
After loading two tasks, the error runner also blocks file replacement, then sends `mark 1`,
`unmark 2`, `todo rejected`, `list`, `bye`. Expect three save errors, no success confirmations,
the original two statuses and count, and no leftover temporary files.

## Load saved tasks on startup

**Aim:** Restore all task types and dates in order, then preserve loaded tasks when editing and adding.

**Inputs:** Restart after the saving case with `list`, `mark 2`, `todo next`, `bye`.
Restart again with `list`, `bye`.

**Expected output:** The first list shows the three saved tasks with their original dates and incomplete
statuses. Mark confirms homework is done. Adding next reports four tasks. The second restart lists:

```text
     Here are the tasks in your list:
     1.[T][ ] read book
     2.[D][X] homework (by: Friday)
     3.[E][ ] lunch (from: noon to: evening)
     4.[T][ ] next
```

The runner checks exact responses, including separators and farewell, and records every session.

**Additional fixtures and inputs:** With `T | 1 | done`, `D | 0 | homework | `,
and `E | 1 | lunch |  | ` saved as separate lines, run `list`, `bye`.
Expect a completed todo, an incomplete deadline with an empty by field, and a completed event
with empty from/to fields. With an empty file, `list`, `bye` shows an empty list and leaves the file empty.
The original saving test also checks startup with no file.

## Readable storage migration

**Aim:** Read existing Base64 tasks and save readable text.

**Inputs:** With `MyBff storage v2` and `T | 1 | cmVhZCBib29r` as the saved file,
send `list`, `todo next`, `bye`.

**Expected output:** List shows `[T][X] read book`; addition confirms next and a count of two.
The file becomes `MyBff storage v3`, `T | 1 | read book`, `T | 0 | next` on separate lines.
Existing special-character restart tests verify pipes and backslashes survive the new format.

## Persistent deletion from the JAR

**Aim:** Save deletions across restarts, including an empty list, and restore the original order if saving fails.

**Inputs:** In an isolated folder, add `todo first`, `todo second`, `todo third`, then `delete 2`, `bye`.
Restart with `list`, `delete 2`, `delete 1`, `bye`; restart with `list`, `delete 1`, `bye`.

**Expected output:** The first deletion confirms `[T][ ] second` and two remaining tasks.
After restarting, list contains only first and third in that order. Deleting both leaves an empty list
on the next restart; deleting from it reports `Invalid task number.` The save file contains only its header.

**Save failure:** Start with three saved tasks, block replacement of the save file after startup,
then enter `delete 2`, `list`, `bye`. Expect the standard save error, no deletion confirmation,
and all three tasks in their original order.

## Temporary and persistent file locks on Windows

**Aim:** Allow a briefly locked save file to be replaced, and preserve rollback when access stays denied.
Run `python test/test-storage-retry.py` after compiling the application. Other platforms skip this case.

**Inputs:** Start with `T | 0 | original` saved under the v3 header. After the greeting, hold a Windows
file handle that prevents replacement, then send `todo next`, `list`, `bye`.

**Expected output:** If the handle is released 150 ms after the addition's opening separator, addition
succeeds with two tasks, and list shows original followed by next. If the handle stays open, addition
reports `OOPS!!! Could not save data/mybff.txt. No changes were made. Check the file and try again.`
and list shows only original. Both sessions end with the normal farewell. The runner checks complete
responses exactly and records the inputs and output in `test/storage-retry-sessions.md`.

**Expected file:** Temporary denial saves both tasks. Persistent denial preserves the original bytes.
Neither case leaves temporary save files. Access-denied replacement is attempted at most five times,
with 100 ms between attempts; the original file is never deleted as a fallback.

