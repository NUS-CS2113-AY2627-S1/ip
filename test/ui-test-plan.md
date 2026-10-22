# UI Test Plan

Text-UI test cases for the GPT chatbot. Each scenario feeds a list of commands
into the program on standard input and compares the console output against the
expected output recorded here.

Run these with the `test-ui` skill.

## How a scenario is checked

* The commands under **Input** are sent to the program one per line, in order.
* Before each scenario except TC-08, delete `data/gpt.txt` if it exists, so the
  scenario starts with an empty task list.
* TC-08 starts with a prepared `data/gpt.txt` file to verify loading saved data.
* The text under **Expected output** is compared against the **end** of the
  program's console output. The startup banner is a fixed prefix and is only
  checked explicitly by TC-01, so the other scenarios do not repeat it.
* Trailing whitespace at the end of each line is ignored, because it is
  invisible in this file and easy to lose when editing.
* Everything else is compared exactly, including blank lines and the separator
  lines.

## Conventions

* `LINE` in prose refers to the 60-character separator the program prints:
  `____________________________________________________________`
* Every command produces a `LINE` before its response and a `LINE` after it, so
  consecutive commands show two separator lines in a row.

---

## TC-01: Startup banner and exit

**Aim:** Verify the greeting is printed on startup and the farewell on `bye`.
This is the only scenario that checks the banner, so the expected output below
is the program's complete console output.

**Input**

```
bye
```

**Expected output**

```
____________________________________________________________
  ____ ____ _____
 / ___|  _ \_   _|
| |  _| |_) || |
| |_| |  __/ | |
 \____|_|    |_|

Beep Boop, I'm GPT.
What's cooking, I'm gonna use all your tokens today.
____________________________________________________________

____________________________________________________________
Bye. Make sure to buy more tokens
____________________________________________________________
```

---

## TC-02: Empty list and unknown command

**Aim:** Verify that `list` on an empty task list prints only the header, and
that an unrecognized command is reported rather than stored as a task.

**Input**

```
blah
list
bye
```

**Expected output**

```
____________________________________________________________
OOPS!!! I don't know what that command means.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Bye. Make sure to buy more tokens
____________________________________________________________
```

---

## TC-03: Add one task of each type

**Aim:** Verify that `todo`, `deadline`, and `event` each create a task with the
correct type icon and date fields, that the running count is correct, and that
`list` numbers the tasks from 1.

**Input**

```
todo borrow book
deadline return book /by 2019-10-15
event project meeting /from Mon 2pm /to 4pm
list
bye
```

**Expected output**

```
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
Bye. Make sure to buy more tokens
____________________________________________________________
```

---

## TC-04: Mark and unmark

**Aim:** Verify that `mark` and `unmark` change the status icon of the correct
task, that the change is reflected in a later `list`, and that the type icon is
preserved.

**Input**

```
todo read book
mark 1
list
unmark 1
list
bye
```

**Expected output**

```
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Beep boop, task has been marked.
  [T][X] read book
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
____________________________________________________________
____________________________________________________________
Beep boop, task has been unmarked.
  [T][ ] read book
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
____________________________________________________________
____________________________________________________________
Bye. Make sure to buy more tokens
____________________________________________________________
```

---

## TC-05: Parse and display deadline dates

**Aim:** Verify that deadline dates in `yyyy-MM-dd` format are displayed in a
readable format, while multi-word event descriptions and dates remain intact.

**Input**

```
deadline do homework /by 2019-12-02
event orientation week /from 4/10/2019 /to 11/10/2019
list
bye
```

**Expected output**

```
____________________________________________________________
Got it. I've added this task:
  [D][ ] do homework (by: Dec 2 2019)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] orientation week (from: 4/10/2019 to: 11/10/2019)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[D][ ] do homework (by: Dec 2 2019)
2.[E][ ] orientation week (from: 4/10/2019 to: 11/10/2019)
____________________________________________________________
____________________________________________________________
Bye. Make sure to buy more tokens
____________________________________________________________
```

---

## TC-06: Invalid task creation commands

**Aim:** Verify that incomplete `todo`, `deadline`, and `event` commands show
friendly error messages instead of crashing the chatbot.

**Input**

```
todo
deadline return book
deadline /by Sunday
deadline return book /by
deadline return book /by Sunday
event project meeting
event /from Mon 2pm /to 4pm
event project meeting /from Mon 2pm
event project meeting /from /to 4pm
event project meeting /from Mon 2pm /to
bye
```

**Expected output**

```
____________________________________________________________
OOPS!!! The description of a todo cannot be empty.
____________________________________________________________
____________________________________________________________
OOPS!!! A deadline needs a /by date or time.
____________________________________________________________
____________________________________________________________
OOPS!!! The description of a deadline cannot be empty.
____________________________________________________________
____________________________________________________________
OOPS!!! The deadline date or time cannot be empty.
____________________________________________________________
____________________________________________________________
OOPS!!! Please enter the deadline date as yyyy-MM-dd.
____________________________________________________________
____________________________________________________________
OOPS!!! An event needs a /from date or time.
____________________________________________________________
____________________________________________________________
OOPS!!! The description of an event cannot be empty.
____________________________________________________________
____________________________________________________________
OOPS!!! An event needs a /to date or time.
____________________________________________________________
____________________________________________________________
OOPS!!! The event start date or time cannot be empty.
____________________________________________________________
____________________________________________________________
OOPS!!! The event end date or time cannot be empty.
____________________________________________________________
____________________________________________________________
Bye. Make sure to buy more tokens
____________________________________________________________
```

---

## TC-07: Invalid task status commands

**Aim:** Verify that invalid `mark` and `unmark` commands show friendly error
messages instead of crashing the chatbot.

**Input**

```
todo read book
mark
mark abc
mark 2
unmark
unmark abc
unmark 0
bye
```

**Expected output**

```
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
OOPS!!! Please tell me which task number to update.
____________________________________________________________
____________________________________________________________
OOPS!!! Task numbers must be whole numbers.
____________________________________________________________
____________________________________________________________
OOPS!!! That task number does not exist.
____________________________________________________________
____________________________________________________________
OOPS!!! Please tell me which task number to update.
____________________________________________________________
____________________________________________________________
OOPS!!! Task numbers must be whole numbers.
____________________________________________________________
____________________________________________________________
OOPS!!! That task number does not exist.
____________________________________________________________
____________________________________________________________
Bye. Make sure to buy more tokens
____________________________________________________________
```

---

## TC-08: Load saved tasks

**Aim:** Verify that saved todo, deadline, and event tasks are loaded when the
chatbot starts.

**Setup file: `data/gpt.txt`**

```
T | 1 | read book
D | 0 | return book | 2019-10-15
E | 0 | project meeting | Mon 2pm | 4pm
```

**Input**

```
list
bye
```

**Expected output**

```
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
2.[D][ ] return book (by: Oct 15 2019)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Bye. Make sure to buy more tokens
____________________________________________________________
```

---

## TC-09: Delete task

**Aim:** Verify that `delete` removes the correct task, prints the removed task,
updates the task count, and leaves the remaining tasks in order.

**Input**

```
todo read book
deadline return book /by 2019-10-15
event project meeting /from Mon 2pm /to 4pm
delete 2
list
delete abc
delete 3
bye
```

**Expected output**

```
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
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've removed this task:
  [D][ ] return book (by: Oct 15 2019)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
OOPS!!! Task numbers must be whole numbers.
____________________________________________________________
____________________________________________________________
OOPS!!! That task number does not exist.
____________________________________________________________
____________________________________________________________
Bye. Make sure to buy more tokens
____________________________________________________________
```

---

## TC-10: Find tasks by keyword

**Aim:** Verify that `find` displays tasks whose descriptions contain the
keyword, ignores letter case, and reports a missing keyword without crashing.

**Input**

```
todo read book
deadline return book /by 2019-10-15
todo write essay
find BOOK
find
bye
```

**Expected output**

```
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
  [T][ ] write essay
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the matching tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Oct 15 2019)
____________________________________________________________
____________________________________________________________
OOPS!!! Please enter a keyword to find.
____________________________________________________________
____________________________________________________________
Bye. Make sure to buy more tokens
____________________________________________________________
```

---

## Known limitations locked in by this plan

These are current behaviors the tests deliberately record, not defects to fix
before the tests pass:

* The count message reads `Now you have 1 tasks in the list.` even for a single
  task.
