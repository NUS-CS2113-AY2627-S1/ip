# UI test plan

## Test configuration

- **Java version:** Java 25
- **Build command:** `javac -d out (Get-ChildItem -Path src/main/java -Filter *.java -Recurse).FullName`
- **Run command:** `java -cp out sandy.Sandy`
- **Comparison rule:** Exact output after line-ending normalization only.
- **Test isolation:** Remove `data/sandy.txt` before each case, then apply any case-specific data-file setup.

## Test cases

### Add, list, mark, and unmark a task

**Aim:** Verify that a task can be added, displayed, marked complete, and marked incomplete again within one UI session.

**Inputs:**

```text
list
todo plan presentation
list
mark 1
list
unmark 1
list
bye
```

**Expected output:**

```text
____________________________________________________________
 ____                  _       
/ ___|  __ _ _ __   __| |_   _ 
\___ \ / _` | '_ \ / _` | | | |
 ___) | (_| | | | | (_| | |_| |
|____/ \__,_|_| |_|\__,_|\__, |
                         |___/ 
Hello! I'm Sandy.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] plan presentation
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] plan presentation
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] plan presentation
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] plan presentation
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] plan presentation
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] plan presentation
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Find matching tasks

**Aim:** Verify that `find` displays all descriptions containing the keyword in task-list order and shows the matching-results heading when there are no matches.

**Inputs:**

```text
todo read book
deadline return book /by 2019-06-06
todo buy groceries
find book
find pen
bye
```

**Expected output:**

```text
____________________________________________________________
 ____                  _       
/ ___|  __ _ _ __   __| |_   _ 
\___ \ / _` | '_ \ / _` | | | |
 ___) | (_| | | | | (_| | |_| |
|____/ \__,_|_| |_|\__,_|\__, |
                         |___/ 
Hello! I'm Sandy.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Jun 06 2019)
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
 2.[D][ ] return book (by: Jun 06 2019)
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Create and display all task types

**Aim:** Verify that deadlines parse ISO dates and date-times, display formatted dates and times, persist ISO values, and that all task types display correctly.

**Inputs:**

```text
todo borrow book
deadline return book /by 2/12/2019 1800
event project meeting /from Mon 2pm /to 4pm
deadline do homework /by 2020-02-03
mark 2
list
bye
```

**Expected output:**

```text
____________________________________________________________
 ____                  _       
/ ___|  __ _ _ __   __| |_   _ 
\___ \ / _` | '_ \ / _` | | | |
 ___) | (_| | | | | (_| | |_| |
|____/ \__,_|_| |_|\__,_|\__, |
                         |___/ 
Hello! I'm Sandy.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Dec 02 2019 6:00 PM)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] do homework (by: Feb 03 2020)
 Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] return book (by: Dec 02 2019 6:00 PM)
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] borrow book
 2.[D][X] return book (by: Dec 02 2019 6:00 PM)
 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 4.[D][ ] do homework (by: Feb 03 2020)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected data file:**

```text
T | 0 | borrow book
D | 1 | return book | 2019-12-02T18:00
E | 0 | project meeting | Mon 2pm | 4pm
D | 0 | do homework | 2020-02-03
```

### Load tasks from an existing data file

**Aim:** Verify that Sandy loads all task types and their completion statuses when it starts.

**Data-file setup:**

```text
T | 1 | read book
D | 0 | return book | 2019-06-06T18:00
E | 0 | project meeting | Aug 6th 2pm | 4pm
```

**Inputs:**

```text
list
bye
```

**Expected output:**

```text
____________________________________________________________
 ____                  _       
/ ___|  __ _ _ __   __| |_   _ 
\___ \ / _` | '_ \ / _` | | | |
 ___) | (_| | | | | (_| | |_| |
|____/ \__,_|_| |_|\__,_|\__, |
                         |___/ 
Hello! I'm Sandy.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] read book
 2.[D][ ] return book (by: Jun 06 2019 6:00 PM)
 3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Delete a task from the middle of the list

**Aim:** Verify that deleting a task reports the removed task, updates the task count, and renumbers the remaining tasks.

**Inputs:**

```text
todo borrow book
deadline return book /by 2019-12-02
event project meeting /from Mon 2pm /to 4pm
delete 2
list
bye
```

**Expected output:**

```text
____________________________________________________________
 ____                  _       
/ ___|  __ _ _ __   __| |_   _ 
\___ \ / _` | '_ \ / _` | | | |
 ___) | (_| | | | | (_| | |_| |
|____/ \__,_|_| |_|\__,_|\__, |
                         |___/ 
Hello! I'm Sandy.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Dec 02 2019)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [D][ ] return book (by: Dec 02 2019)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] borrow book
 2.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected data file:**

```text
T | 0 | borrow book
E | 0 | project meeting | Mon 2pm | 4pm
```

### Handle invalid commands and continue

**Aim:** Verify that invalid commands show useful errors without stopping the command loop.

**Inputs:**

```text
todo
blah
find
deadline submit tax return /by 2019-02-30
list
mark abc
delete abc
delete 1
bye
```

**Expected output:**

```text
____________________________________________________________
 ____                  _       
/ ___|  __ _ _ __   __| |_   _ 
\___ \ / _` | '_ \ / _` | | | |
 ___) | (_| | | | | (_| | |_| |
|____/ \__,_|_| |_|\__,_|\__, |
                         |___/ 
Hello! I'm Sandy.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Oops! The description of a todo cannot be empty.
____________________________________________________________
____________________________________________________________
 Oops! I do not recognize that command.
____________________________________________________________
____________________________________________________________
 Oops! Please provide a keyword to search for.
____________________________________________________________
____________________________________________________________
 Oops! Please provide a deadline date in yyyy-MM-dd or d/M/yyyy HHmm format.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Oops! Please provide a valid task number.
____________________________________________________________
____________________________________________________________
 Oops! Please provide a valid task number.
____________________________________________________________
____________________________________________________________
 Oops! That task number does not exist.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Handle a corrupted data file

**Aim:** Verify that Sandy reports corrupted saved data, starts with an empty list, and remains usable.

**Data-file setup:**

```text
D | 0 | return book | 2019-02-30
```

**Inputs:**

```text
list
bye
```

**Expected output:**

```text
____________________________________________________________
 ____                  _       
/ ___|  __ _ _ __   __| |_   _ 
\___ \ / _` | '_ \ / _` | | | |
 ___) | (_| | | | | (_| | |_| |
|____/ \__,_|_| |_|\__,_|\__, |
                         |___/ 
Hello! I'm Sandy.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Oops! I could not load saved tasks: The data file is corrupted at line 1.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Latest test session

- **Java version:** 25.0.4
- **Last run:** 2026-09-27
- **Result:** Passed - 7 of 7 cases after Javadoc additions.

### Add, list, mark, and unmark a task

**Console input:**

```text
list
todo plan presentation
list
mark 1
list
unmark 1
list
bye
```

**Console output:**

```text
____________________________________________________________
 ____                  _       
/ ___|  __ _ _ __   __| |_   _ 
\___ \ / _` | '_ \ / _` | | | |
 ___) | (_| | | | | (_| | |_| |
|____/ \__,_|_| |_|\__,_|\__, |
                         |___/ 
Hello! I'm Sandy.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] plan presentation
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] plan presentation
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] plan presentation
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] plan presentation
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] plan presentation
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] plan presentation
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Find matching tasks

**Console input:**

```text
todo read book
deadline return book /by 2019-06-06
todo buy groceries
find book
find pen
bye
```

**Console output:**

```text
____________________________________________________________
 ____                  _       
/ ___|  __ _ _ __   __| |_   _ 
\___ \ / _` | '_ \ / _` | | | |
 ___) | (_| | | | | (_| | |_| |
|____/ \__,_|_| |_|\__,_|\__, |
                         |___/ 
Hello! I'm Sandy.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Jun 06 2019)
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
 2.[D][ ] return book (by: Jun 06 2019)
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Create and display all task types

**Console input:**

```text
todo borrow book
deadline return book /by 2/12/2019 1800
event project meeting /from Mon 2pm /to 4pm
deadline do homework /by 2020-02-03
mark 2
list
bye
```

**Console output:**

```text
____________________________________________________________
 ____                  _       
/ ___|  __ _ _ __   __| |_   _ 
\___ \ / _` | '_ \ / _` | | | |
 ___) | (_| | | | | (_| | |_| |
|____/ \__,_|_| |_|\__,_|\__, |
                         |___/ 
Hello! I'm Sandy.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Dec 02 2019 6:00 PM)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] do homework (by: Feb 03 2020)
 Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] return book (by: Dec 02 2019 6:00 PM)
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] borrow book
 2.[D][X] return book (by: Dec 02 2019 6:00 PM)
 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 4.[D][ ] do homework (by: Feb 03 2020)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

**Data file after run:**

```text
T | 0 | borrow book
D | 1 | return book | 2019-12-02T18:00
E | 0 | project meeting | Mon 2pm | 4pm
D | 0 | do homework | 2020-02-03
```

### Load tasks from an existing data file

**Data-file setup:**

```text
T | 1 | read book
D | 0 | return book | 2019-06-06T18:00
E | 0 | project meeting | Aug 6th 2pm | 4pm
```

**Console input:**

```text
list
bye
```

**Console output:**

```text
____________________________________________________________
 ____                  _       
/ ___|  __ _ _ __   __| |_   _ 
\___ \ / _` | '_ \ / _` | | | |
 ___) | (_| | | | | (_| | |_| |
|____/ \__,_|_| |_|\__,_|\__, |
                         |___/ 
Hello! I'm Sandy.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] read book
 2.[D][ ] return book (by: Jun 06 2019 6:00 PM)
 3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Delete a task from the middle of the list

**Console input:**

```text
todo borrow book
deadline return book /by 2019-12-02
event project meeting /from Mon 2pm /to 4pm
delete 2
list
bye
```

**Console output:**

```text
____________________________________________________________
 ____                  _       
/ ___|  __ _ _ __   __| |_   _ 
\___ \ / _` | '_ \ / _` | | | |
 ___) | (_| | | | | (_| | |_| |
|____/ \__,_|_| |_|\__,_|\__, |
                         |___/ 
Hello! I'm Sandy.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Dec 02 2019)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [D][ ] return book (by: Dec 02 2019)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] borrow book
 2.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

**Data file after run:**

```text
T | 0 | borrow book
E | 0 | project meeting | Mon 2pm | 4pm
```

### Handle invalid commands and continue

**Console input:**

```text
todo
blah
find
deadline submit tax return /by 2019-02-30
list
mark abc
delete abc
delete 1
bye
```

**Console output:**

```text
____________________________________________________________
 ____                  _       
/ ___|  __ _ _ __   __| |_   _ 
\___ \ / _` | '_ \ / _` | | | |
 ___) | (_| | | | | (_| | |_| |
|____/ \__,_|_| |_|\__,_|\__, |
                         |___/ 
Hello! I'm Sandy.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Oops! The description of a todo cannot be empty.
____________________________________________________________
____________________________________________________________
 Oops! I do not recognize that command.
____________________________________________________________
____________________________________________________________
 Oops! Please provide a keyword to search for.
____________________________________________________________
____________________________________________________________
 Oops! Please provide a deadline date in yyyy-MM-dd or d/M/yyyy HHmm format.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Oops! Please provide a valid task number.
____________________________________________________________
____________________________________________________________
 Oops! Please provide a valid task number.
____________________________________________________________
____________________________________________________________
 Oops! That task number does not exist.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Handle a corrupted data file

**Data-file setup:**

```text
D | 0 | return book | 2019-02-30
```

**Console input:**

```text
list
bye
```

**Console output:**

```text
____________________________________________________________
 ____                  _       
/ ___|  __ _ _ __   __| |_   _ 
\___ \ / _` | '_ \ / _` | | | |
 ___) | (_| | | | | (_| | |_| |
|____/ \__,_|_| |_|\__,_|\__, |
                         |___/ 
Hello! I'm Sandy.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Oops! I could not load saved tasks: The data file is corrupted at line 1.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
