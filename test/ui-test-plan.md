# UI Test Plan

Each test case runs GLaDOS with a fixed sequence of typed commands (the
**Input**) and checks the full console output against the **Expected
output** block, character for character (line-ending differences between
Windows and Unix are ignored).

Each test runs in a fresh, empty folder, so it starts with no saved tasks.
A test can optionally add a **Data file before:** block (written to
`data/glados.txt` before the run, to test loading) and a **Data file
after:** block (the exact contents `data/glados.txt` must have once the run
ends, to test saving).

Update this file whenever a command's input format or reply wording
changes, so the test-ui skill keeps checking against current behaviour.

## Test 1: Add a todo, deadline, and event, then mark and unmark

**Aim:** Verify that all three task types can be added, that `list` shows
each one formatted with its type letter and status box, and that `mark`
and `unmark` both update the status shown afterwards. Also verify that the
saved data file holds every task, in order, with its latest done status.
The deadline uses the date and time `2/12/2019 1800`, which is shown as
`Dec 02 2019 6:00 PM` and saved as `2019-12-02 1800`, and the event's start
and end are understood the same way.

**Input:**
```text
todo read book
deadline return book /by 2/12/2019 1800
event project meeting /from 6/8/2019 1400 /to 6/8/2019 1600
mark 1
list
unmark 1
list
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] return book (by: Dec 02 2019 6:00 PM)
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [E][ ] project meeting (from: Aug 06 2019 2:00 PM to: Aug 06 2019 4:00 PM)
     Now you have 3 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] read book
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] read book
     2.[D][ ] return book (by: Dec 02 2019 6:00 PM)
     3.[E][ ] project meeting (from: Aug 06 2019 2:00 PM to: Aug 06 2019 4:00 PM)
    ____________________________________________________________
    ____________________________________________________________
     OK, I've marked this task as not done yet:
       [T][ ] read book
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read book
     2.[D][ ] return book (by: Dec 02 2019 6:00 PM)
     3.[E][ ] project meeting (from: Aug 06 2019 2:00 PM to: Aug 06 2019 4:00 PM)
    ____________________________________________________________
    ____________________________________________________________
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

**Data file after:**
```text
T | 0 | read book
D | 0 | return book | 2019-12-02 1800
E | 0 | project meeting | 2019-08-06 1400 | 2019-08-06 1600
```

## Test 2: Reject a todo with no description

**Aim:** Verify that a bare `todo` command is rejected with an error message
instead of adding a blank task, and that a normal todo entered afterwards is
still added correctly.

**Input:**
```text
todo
todo read book
list
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     A todo with no description. Try again, with words this time.
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
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

## Test 3: Reject an unrecognised command, then keep working normally

**Aim:** Verify that an unrecognised command and an empty todo are each
rejected with their own error message, and that a valid todo entered
afterwards is still added correctly, so a rejected command leaves the task
list untouched.

**Input:**
```text
blah
todo
todo read book
list
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     I have no idea what that was. Try one of: list, todo, deadline, event, mark, unmark, delete, find, on, bye.
    ____________________________________________________________
    ____________________________________________________________
     A todo with no description. Try again, with words this time.
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
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

## Test 4: Reject invalid mark arguments, then mark successfully

**Aim:** Verify that `mark` with no number, a non-numeric argument, and an
out-of-range number are each rejected with their own message rather than
crashing, and that a valid `mark` still works afterwards and is saved as
done (`1`) in the data file.

**Input:**
```text
todo read book
mark
mark abc
mark 999
mark 1
list
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Which task? Give me a number, like mark 2.
    ____________________________________________________________
    ____________________________________________________________
     "abc" is not a task number.
    ____________________________________________________________
    ____________________________________________________________
     There is no task 999. Your list has 1.
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
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

**Data file after:**
```text
T | 1 | read book
```

## Test 5: Reject malformed deadline and event input

**Aim:** Verify that a deadline missing its `/by` and an event missing its
`/to` are each rejected with a message naming the correct format, rather
than crashing. Also verify that a deadline whose `/by` is not a date, is a
date that does not exist, or has a time not in 24-hour form is rejected,
that an event whose `/from` or `/to` is not a date, or which ends before it
starts, is rejected, and that a well-formed deadline entered afterwards is
still added correctly.

**Input:**
```text
deadline return book
event meeting /from Mon
deadline return book /by Sunday
deadline return book /by 2019-02-30
deadline return book /by 2019-10-15 6pm
event meeting /from Mon /to Tue
event camp /from 2019-10-17 /to 2019-10-15
event meeting /from 2019-10-15 1600 /to 2019-10-15 1400
deadline return book /by 2019-10-15
list
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     A deadline needs a /by. Try: deadline return book /by 2019-10-15.
    ____________________________________________________________
    ____________________________________________________________
     An event needs both a /from and a /to. Try: event camp /from 2019-10-15 /to 2019-10-17.
    ____________________________________________________________
    ____________________________________________________________
     "Sunday" is not a date I understand. Try 2019-10-15 or 2/12/2019, with an optional time like 1800.
    ____________________________________________________________
    ____________________________________________________________
     "2019-02-30" is not a date I understand. Try 2019-10-15 or 2/12/2019, with an optional time like 1800.
    ____________________________________________________________
    ____________________________________________________________
     "2019-10-15 6pm" is not a date I understand. Try 2019-10-15 or 2/12/2019, with an optional time like 1800.
    ____________________________________________________________
    ____________________________________________________________
     "Mon" is not a date I understand. Try 2019-10-15 or 2/12/2019, with an optional time like 1800.
    ____________________________________________________________
    ____________________________________________________________
     This event ends before it starts. Check your /from and /to.
    ____________________________________________________________
    ____________________________________________________________
     This event ends before it starts. Check your /from and /to.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] return book (by: Oct 15 2019)
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[D][ ] return book (by: Oct 15 2019)
    ____________________________________________________________
    ____________________________________________________________
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

## Test 6: Delete tasks, including invalid delete arguments

**Aim:** Verify that `delete` with no number, a non-numeric argument, and an
out-of-range number are each rejected, that deleting a middle task removes
it and shifts later tasks up in `list`, and that deleting the remaining
tasks leaves an empty list, with the saved data file emptied to match.

**Input:**
```text
todo read book
deadline return book /by 2019-06-06
event project meeting /from 6/8/2019 1400 /to 6/8/2019 1600
delete
delete abc
delete 5
delete 2
list
delete 1
delete 1
list
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
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
       [E][ ] project meeting (from: Aug 06 2019 2:00 PM to: Aug 06 2019 4:00 PM)
     Now you have 3 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Which task? Give me a number, like delete 2.
    ____________________________________________________________
    ____________________________________________________________
     "abc" is not a task number.
    ____________________________________________________________
    ____________________________________________________________
     There is no task 5. Your list has 3.
    ____________________________________________________________
    ____________________________________________________________
     Noted. I've removed this task:
       [D][ ] return book (by: Jun 06 2019)
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read book
     2.[E][ ] project meeting (from: Aug 06 2019 2:00 PM to: Aug 06 2019 4:00 PM)
    ____________________________________________________________
    ____________________________________________________________
     Noted. I've removed this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Noted. I've removed this task:
       [E][ ] project meeting (from: Aug 06 2019 2:00 PM to: Aug 06 2019 4:00 PM)
     Now you have 0 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
    ____________________________________________________________
    ____________________________________________________________
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

**Data file after:**
```text
```

## Test 7: Load saved tasks at startup, then keep saving changes

**Aim:** Verify that tasks in an existing data file are loaded at startup
with their type, details, and done status intact, and that later changes
(unmarking a loaded task, adding a new one) are saved back to the file.

**Data file before:**
```text
T | 1 | read book
D | 0 | return book | 2019-06-06 1800
E | 0 | project meeting | 2019-08-06 1400 | 2019-08-06 1600
```

**Input:**
```text
list
unmark 1
todo join sports club
list
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] read book
     2.[D][ ] return book (by: Jun 06 2019 6:00 PM)
     3.[E][ ] project meeting (from: Aug 06 2019 2:00 PM to: Aug 06 2019 4:00 PM)
    ____________________________________________________________
    ____________________________________________________________
     OK, I've marked this task as not done yet:
       [T][ ] read book
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] join sports club
     Now you have 4 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read book
     2.[D][ ] return book (by: Jun 06 2019 6:00 PM)
     3.[E][ ] project meeting (from: Aug 06 2019 2:00 PM to: Aug 06 2019 4:00 PM)
     4.[T][ ] join sports club
    ____________________________________________________________
    ____________________________________________________________
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

**Data file after:**
```text
T | 0 | read book
D | 0 | return book | 2019-06-06 1800
E | 0 | project meeting | 2019-08-06 1400 | 2019-08-06 1600
T | 0 | join sports club
```

## Test 8: Skip corrupted lines in the data file

**Aim:** Verify that lines in the data file with an unknown type letter, a
missing field, an invalid done flag, an empty description, a date that is
not in the data file's format, or an event that ends before it starts, and
lines that are not tasks at all, are skipped and reported, while blank
lines are ignored and valid lines still load. Also verify that the next
save rewrites the file with only the valid tasks.

**Data file before:**
```text
T | 1 | read book
X | 0 | unknown type
D | 0 | missing date
E | 2 | bad flag | 2019-08-06 | 2019-08-07
E | 0 | ends early | 2019-08-07 | 2019-08-06
T | 0 | 
D | 0 | bad date | June 6th

not a task at all
D | 0 | return book | 2019-06-06
```

**Input:**
```text
list
todo join sports club
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     Your save file is damaged. I skipped 7 unreadable line(s).
     They will be gone for good the next time I save.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] read book
     2.[D][ ] return book (by: Jun 06 2019)
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] join sports club
     Now you have 3 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

**Data file after:**
```text
T | 1 | read book
D | 0 | return book | 2019-06-06
T | 0 | join sports club
```

## Test 9: Reject the reserved | character in new tasks

**Aim:** Verify that a todo, deadline, or event containing `|` (the data
file's field separator) is rejected rather than saved in a form that could
not be loaded back, and that nothing is written to the data file.

**Input:**
```text
todo read | book
deadline return book /by June|6th
event meeting /from Mon /to Tue|Wed
list
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     The | character is reserved for my records. Leave it out.
    ____________________________________________________________
    ____________________________________________________________
     The | character is reserved for my records. Leave it out.
    ____________________________________________________________
    ____________________________________________________________
     The | character is reserved for my records. Leave it out.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
    ____________________________________________________________
    ____________________________________________________________
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

**Data file after:**
```text
(data file does not exist)
```

## Test 10: Understand dates and times in deadlines

**Aim:** Verify that a deadline's `/by` is understood in every accepted
form (`yyyy-mm-dd` and `d/m/yyyy`, each with or without a 24-hour time),
shown to the user as e.g. `Oct 15 2019` or `Dec 02 2019 6:00 PM`, and saved
in the data file as e.g. `2019-10-15` or `2019-12-02 1800`.

**Input:**
```text
deadline return book /by 2019-10-15
deadline submit report /by 2019-10-15 0930
deadline pay rent /by 2/12/2019
deadline call home /by 2/12/2019 1800
list
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] return book (by: Oct 15 2019)
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] submit report (by: Oct 15 2019 9:30 AM)
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] pay rent (by: Dec 02 2019)
     Now you have 3 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] call home (by: Dec 02 2019 6:00 PM)
     Now you have 4 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[D][ ] return book (by: Oct 15 2019)
     2.[D][ ] submit report (by: Oct 15 2019 9:30 AM)
     3.[D][ ] pay rent (by: Dec 02 2019)
     4.[D][ ] call home (by: Dec 02 2019 6:00 PM)
    ____________________________________________________________
    ____________________________________________________________
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

**Data file after:**
```text
D | 0 | return book | 2019-10-15
D | 0 | submit report | 2019-10-15 0930
D | 0 | pay rent | 2019-12-02
D | 0 | call home | 2019-12-02 1800
```

## Test 11: Understand dates and times in events

**Aim:** Verify that an event's `/from` and `/to` are understood in the same
forms as a deadline's `/by`, that an event may span several days or start
and end on the same day, that a start with a time and an end with only a
date on the same day is accepted (a date with no time counts as the whole
day), and that each is saved in the data file's date format.

**Input:**
```text
event camp /from 2019-10-15 /to 2019-10-17
event project meeting /from 6/8/2019 1400 /to 6/8/2019 1600
event party /from 2019-10-15 1800 /to 2019-10-15
list
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [E][ ] camp (from: Oct 15 2019 to: Oct 17 2019)
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [E][ ] project meeting (from: Aug 06 2019 2:00 PM to: Aug 06 2019 4:00 PM)
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [E][ ] party (from: Oct 15 2019 6:00 PM to: Oct 15 2019)
     Now you have 3 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[E][ ] camp (from: Oct 15 2019 to: Oct 17 2019)
     2.[E][ ] project meeting (from: Aug 06 2019 2:00 PM to: Aug 06 2019 4:00 PM)
     3.[E][ ] party (from: Oct 15 2019 6:00 PM to: Oct 15 2019)
    ____________________________________________________________
    ____________________________________________________________
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

**Data file after:**
```text
E | 0 | camp | 2019-10-15 | 2019-10-17
E | 0 | project meeting | 2019-08-06 1400 | 2019-08-06 1600
E | 0 | party | 2019-10-15 1800 | 2019-10-15
```

## Test 12: List the tasks that fall on a date

**Aim:** Verify that `on` lists, in list order, the deadlines due on a date
(with or without a time) and the events running across it, including the
first and last day of a multi-day event, while todos and tasks on other
dates are left out. Also verify that both date forms work, that a date with
nothing on it says so, that a missing date, a non-date, a date with a time,
and a date that does not exist are each rejected, and that `on` does not
change the data file.

**Data file before:**
```text
T | 0 | read book
D | 1 | return book | 2019-10-15
D | 0 | pay rent | 2019-10-16
E | 0 | camp | 2019-10-14 | 2019-10-17
E | 0 | dinner | 2019-10-15 1900 | 2019-10-15 2100
D | 0 | submit report | 2019-10-15 2359
E | 0 | trip | 2019-10-16 | 2019-10-18
```

**Input:**
```text
on 2019-10-15
on 15/10/2019
on 2019-10-14
on 2019-10-18
on 2019-10-20
on
on Sunday
on 2019-10-15 1800
on 2019-02-30
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks on Oct 15 2019:
     1.[D][X] return book (by: Oct 15 2019)
     2.[E][ ] camp (from: Oct 14 2019 to: Oct 17 2019)
     3.[E][ ] dinner (from: Oct 15 2019 7:00 PM to: Oct 15 2019 9:00 PM)
     4.[D][ ] submit report (by: Oct 15 2019 11:59 PM)
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks on Oct 15 2019:
     1.[D][X] return book (by: Oct 15 2019)
     2.[E][ ] camp (from: Oct 14 2019 to: Oct 17 2019)
     3.[E][ ] dinner (from: Oct 15 2019 7:00 PM to: Oct 15 2019 9:00 PM)
     4.[D][ ] submit report (by: Oct 15 2019 11:59 PM)
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks on Oct 14 2019:
     1.[E][ ] camp (from: Oct 14 2019 to: Oct 17 2019)
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks on Oct 18 2019:
     1.[E][ ] trip (from: Oct 16 2019 to: Oct 18 2019)
    ____________________________________________________________
    ____________________________________________________________
     You have nothing on Oct 20 2019. Enjoy it while it lasts.
    ____________________________________________________________
    ____________________________________________________________
     Which date? Try: on 2019-10-15.
    ____________________________________________________________
    ____________________________________________________________
     "Sunday" is not a date I understand. Try 2019-10-15 or 2/12/2019, without a time.
    ____________________________________________________________
    ____________________________________________________________
     "2019-10-15 1800" is not a date I understand. Try 2019-10-15 or 2/12/2019, without a time.
    ____________________________________________________________
    ____________________________________________________________
     "2019-02-30" is not a date I understand. Try 2019-10-15 or 2/12/2019, without a time.
    ____________________________________________________________
    ____________________________________________________________
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

**Data file after:**
```text
T | 0 | read book
D | 1 | return book | 2019-10-15
D | 0 | pay rent | 2019-10-16
E | 0 | camp | 2019-10-14 | 2019-10-17
E | 0 | dinner | 2019-10-15 1900 | 2019-10-15 2100
D | 0 | submit report | 2019-10-15 2359
E | 0 | trip | 2019-10-16 | 2019-10-18
```

## Test 13: Find tasks by a keyword in their description

**Aim:** Verify that `find` lists, in list order and numbered from 1, every
task whose description contains the keyword, whatever its type or done
status, ignoring upper and lower case. Also verify that a keyword may
contain spaces, that spaces around it are ignored, that a keyword matching
nothing says so, that a missing keyword is rejected, that only the
description is searched (not dates), and that `find` does not change the
data file.

**Data file before:**
```text
T | 1 | read book
D | 1 | return book | 2019-06-06
E | 0 | project meeting | 2019-08-06 1400 | 2019-08-06 1600
T | 1 | join sports club
T | 0 | borrow book
```

**Input:**
```text
find book
find BOOK
find return book
find   meeting  
find xyz
find
find 2019
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     Here are the matching tasks in your list:
     1.[T][X] read book
     2.[D][X] return book (by: Jun 06 2019)
     3.[T][ ] borrow book
    ____________________________________________________________
    ____________________________________________________________
     Here are the matching tasks in your list:
     1.[T][X] read book
     2.[D][X] return book (by: Jun 06 2019)
     3.[T][ ] borrow book
    ____________________________________________________________
    ____________________________________________________________
     Here are the matching tasks in your list:
     1.[D][X] return book (by: Jun 06 2019)
    ____________________________________________________________
    ____________________________________________________________
     Here are the matching tasks in your list:
     1.[E][ ] project meeting (from: Aug 06 2019 2:00 PM to: Aug 06 2019 4:00 PM)
    ____________________________________________________________
    ____________________________________________________________
     No tasks match "xyz". Perhaps it never existed.
    ____________________________________________________________
    ____________________________________________________________
     Find what? Give me a keyword, like find book.
    ____________________________________________________________
    ____________________________________________________________
     No tasks match "2019". Perhaps it never existed.
    ____________________________________________________________
    ____________________________________________________________
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

**Data file after:**
```text
T | 1 | read book
D | 1 | return book | 2019-06-06
E | 0 | project meeting | 2019-08-06 1400 | 2019-08-06 1600
T | 1 | join sports club
T | 0 | borrow book
```

## Test 14: Say goodbye when input ends without bye

**Aim:** Verify that when the input ends before `bye` is entered (e.g. the
user presses Ctrl+D, or commands are read from a file), GLaDOS shows the
goodbye message and exits normally instead of crashing, and that tasks
added before then are still saved.

**Input:**
```text
todo read book
list
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
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
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

**Data file after:**
```text
T | 0 | read book
```

## Test 15: Keep readable tasks when a line has characters that are not UTF-8

**Aim:** Verify that a data file saved in another encoding (here
Windows-1252, as by older Windows text editors), where one line contains a
character that is not valid UTF-8, has only that line skipped and reported,
while every other task still loads and is kept the next time GLaDOS saves.

**Data file before (Windows-1252):**
```text
T | 0 | important saved task
T | 0 | café
D | 0 | return book | 2019-06-06
```

**Input:**
```text
list
todo new task
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     Your save file is damaged. I skipped 1 unreadable line(s).
     They will be gone for good the next time I save.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] important saved task
     2.[D][ ] return book (by: Jun 06 2019)
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] new task
     Now you have 3 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

**Data file after:**
```text
T | 0 | important saved task
D | 0 | return book | 2019-06-06
T | 0 | new task
```

## Test 16: Load a data file saved as UTF-8 with a byte order mark

**Aim:** Verify that a data file starting with a byte order mark (as saved
by e.g. Notepad's "UTF-8 with BOM" option) loads every task, including the
first one, with no lines reported as damaged.

**Data file before (UTF-8 with BOM):**
```text
T | 0 | first task
T | 1 | second task
```

**Input:**
```text
list
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] first task
     2.[T][X] second task
    ____________________________________________________________
    ____________________________________________________________
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```
