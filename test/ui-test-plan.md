# CortisolBot text-UI test plan

Every test case below is run by the `test-ui` skill. Run them all with:

```powershell
python test/run-ui-tests.py
```

## How a test case works

Each test case starts a fresh copy of the program in an empty working folder, types the lines of its
**Input** block one after another, and compares everything the program printed with its **Expected
output** block. A test case may also supply a **Data file** block, which is written to
`data/cortisolbot.txt` before the program starts, and an **Expected data file** block, which is
compared with that file after the program exits.

Rules for writing a test case:

* The last input line must be `bye`. Without it the program waits forever for more input.
* `{{greeting}}` and `{{farewell}}` on a line of their own stand for the blocks under **Snippets**,
  so that the banner need not be repeated in every test case.
* Indentation is compared exactly, so every task line -- the one printed by an add, a `mark`, an
  `unmark` or a `delete`, and each numbered line printed by `list` -- must carry its leading **tab**.
  Trailing spaces and trailing blank lines are ignored, since they cannot be seen on screen and most
  editors strip them.
* Each test case runs in its own folder under `_temp/ui-test-runs/`, so the task list saved by one
  test case can never reach another, and the real `data/cortisolbot.txt` is never touched.

## Known deviations recorded here on purpose

This is current behaviour, deliberately captured so that the day it is changed, the test session
fails and this plan must be updated to match. It is **not** an endorsement.

1. A data file edited by hand can still carry the separator, `|`, inside a field, and such a line
   comes back truncated at the separator rather than being reported as unreadable. Typing one in is
   now refused outright (TC-17), so this is reachable only by editing `data/cortisolbot.txt`
   directly; TC-18 records what happens when someone does. Escaping the separator as it is written
   would close it properly, and belongs in whichever increment touches `Storage` next.

When it is fixed, update the affected expected output here in the same commit as the code change.

## A note on trailing spaces

Because trailing spaces are ignored when outputs are compared, a bug that left a stray space at the
**end** of a line would slip past. Test cases therefore check whitespace where something follows it:
a deadline's description is followed by ` (by: ...)` on screen and by ` | ` in the data file, so a
description that kept a trailing space shows up as a doubled space mid-line, which is compared
exactly. TC-19 exists for that reason -- prefer a deadline or an event over a todo when what is being
checked is where a description begins and ends.

## Snippets

### greeting

```text
  ____           _   _           _ ____        _
 / ___|___  _ __| |_(_)___  ___ | | __ )  ___ | |_
| |   / _ \| '__| __| / __|/ _ \| |  _ \ / _ \| __|
| |__| (_) | |  | |_| \__ \ (_) | | |_) | (_) | |_
 \____\___/|_|   \__|_|___/\___/|_|____/ \___/ \__|
-------------------------------------------------------
Greetings sir/madam, CortisolBot humbly at your service.
How may I serve you this evening?
-------------------------------------------------------
```

### farewell

```text
-------------------------------------------------------
Tonight has been an honour, sir/madam. I shall bid you good evening.
-------------------------------------------------------
```

## TC-01 Starting and leaving

**Aim:** Check that the program greets the user, survives a session in which nothing is asked of it,
and takes its leave on `bye`.

### Input

```text
bye
```

### Expected output

```text
{{greeting}}
{{farewell}}
```

## TC-02 Listing an empty list

**Aim:** Check that `list` on a fresh start reports an empty list rather than an empty heading.

### Input

```text
list
bye
```

### Expected output

```text
{{greeting}}
-------------------------------------------------------
Your list is presently empty, sir/madam. A rare luxury.
-------------------------------------------------------
{{farewell}}
```

## TC-03 Adding one task of each type

**Aim:** Check that `todo`, `deadline` and `event` each add a task, report the growing count, display
in their own format, and are written to the data file in the encoding `Storage` expects.

### Input

```text
todo read book
deadline return book /by 2019-06-06
event project meeting /from Aug 6th 2pm /to 4pm
list
bye
```

### Expected output

```text
{{greeting}}
-------------------------------------------------------
Very good, sir/madam. I have added the following:
	[T][ ] read book
 That makes 1 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
Very good, sir/madam. I have added the following:
	[D][ ] return book (by: Jun 06 2019)
 That makes 2 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
Very good, sir/madam. I have added the following:
	[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
 That makes 3 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
Your tasks, sir/madam, as they presently stand:
	1.[T][ ] read book
	2.[D][ ] return book (by: Jun 06 2019)
	3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
-------------------------------------------------------
{{farewell}}
```

### Expected data file

```text
T | 0 | read book
D | 0 | return book | 2019-06-06
E | 0 | project meeting | Aug 6th 2pm | 4pm
```

## TC-04 Marking and unmarking a task

**Aim:** Check that `mark` and `unmark` change the status icon, that the change shows up in a later
`list`, and that the done-status reaches the data file.

### Input

```text
todo read book
mark 1
list
unmark 1
list
bye
```

### Expected output

```text
{{greeting}}
-------------------------------------------------------
Very good, sir/madam. I have added the following:
	[T][ ] read book
 That makes 1 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
Consider it done, sir/madam:
	[T][X] read book
-------------------------------------------------------
-------------------------------------------------------
Your tasks, sir/madam, as they presently stand:
	1.[T][X] read book
-------------------------------------------------------
-------------------------------------------------------
Very well, sir/madam. I have returned it to the undone:
	[T][ ] read book
-------------------------------------------------------
-------------------------------------------------------
Your tasks, sir/madam, as they presently stand:
	1.[T][ ] read book
-------------------------------------------------------
{{farewell}}
```

### Expected data file

```text
T | 0 | read book
```

## TC-05 Deleting a task

**Aim:** Check that `delete` removes the task it names, reports the task it removed along with the
new count, renumbers what is left, and shrinks the data file.

### Input

```text
todo read book
todo return book
delete 1
list
bye
```

### Expected output

```text
{{greeting}}
-------------------------------------------------------
Very good, sir/madam. I have added the following:
	[T][ ] read book
 That makes 1 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
Very good, sir/madam. I have added the following:
	[T][ ] return book
 That makes 2 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
Consider it forgotten, sir/madam:
	[T][ ] read book
 That leaves 1 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
Your tasks, sir/madam, as they presently stand:
	1.[T][ ] return book
-------------------------------------------------------
{{farewell}}
```

### Expected data file

```text
T | 0 | return book
```

## TC-06 Commands the bot does not know

**Aim:** Check that an unrecognised word, and an empty line, are both refused in character and
followed by the list of commands the bot does understand, without ending the session.

### Input

```text
sing

bye
```

### Expected output

```text
{{greeting}}
-------------------------------------------------------
 I do beg your pardon, sir/madam, but that instruction is not in my repertoire.
 I can manage: todo, deadline, event, list, find, mark, unmark, delete, bye.
-------------------------------------------------------
-------------------------------------------------------
 I do beg your pardon, sir/madam, but that instruction is not in my repertoire.
 I can manage: todo, deadline, event, list, find, mark, unmark, delete, bye.
-------------------------------------------------------
{{farewell}}
```

## TC-07 Add commands missing their parts

**Aim:** Check each way an add command can be incomplete: no description at all, a deadline with no
`/by`, a deadline with no description, and an event with no `/to`. Each must explain the
misunderstanding and offer the correct form.

The third line keeps a free-text date, `/by June 6th`, on purpose. A missing description must be
reported before the date is so much as looked at, so this line checks the order of the two
complaints: the answer must be about the description, not about the date.

### Input

```text
todo
deadline return book
deadline /by June 6th
event project meeting /from Aug 6th 2pm
bye
```

### Expected output

```text
{{greeting}}
-------------------------------------------------------
 A todo without a description is rather like tea without leaves, sir/madam.
 Do try: todo <description>
-------------------------------------------------------
-------------------------------------------------------
 A deadline is of little use without a date, sir/madam.
 Do try: deadline <description> /by 2019-12-02
-------------------------------------------------------
-------------------------------------------------------
 You have not told me what is due, sir/madam.
 Do try: deadline <description> /by 2019-12-02
-------------------------------------------------------
-------------------------------------------------------
 An event requires both a start and an end, sir/madam.
 Do try: event <description> /from <start> /to <end>
-------------------------------------------------------
{{farewell}}
```

## TC-08 Task numbers that refer to nothing

**Aim:** Check each way a task number can be wrong: missing, not a number, given while the list is
empty, and beyond the end of a list that does have tasks.

### Input

```text
mark
delete abc
mark 1
todo read book
unmark 5
bye
```

### Expected output

```text
{{greeting}}
-------------------------------------------------------
 Which task shall I mark, sir/madam?
 Do try: mark <task number>
-------------------------------------------------------
-------------------------------------------------------
 'abc' is not a number I recognise, sir/madam.
 Do try: delete <task number>
-------------------------------------------------------
-------------------------------------------------------
 Your list is presently empty, sir/madam. There is nothing to mark just yet.
-------------------------------------------------------
-------------------------------------------------------
Very good, sir/madam. I have added the following:
	[T][ ] read book
 That makes 1 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
 I keep no task numbered 5, sir/madam.
 Your list runs from 1 to 1.
-------------------------------------------------------
{{farewell}}
```

## TC-09 Tasks survive to the next session

**Aim:** Check that a data file left by an earlier session is read back at startup, that the
retrieval is announced, and that every task type and the done-status come back intact.

### Data file

```text
T | 1 | read book
D | 0 | return book | 2019-06-06
E | 0 | project meeting | Aug 6th 2pm | 4pm
```

### Input

```text
list
bye
```

### Expected output

```text
{{greeting}}
 I have retrieved 3 of your tasks, sir/madam.
-------------------------------------------------------
-------------------------------------------------------
Your tasks, sir/madam, as they presently stand:
	1.[T][X] read book
	2.[D][ ] return book (by: Jun 06 2019)
	3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
-------------------------------------------------------
{{farewell}}
```

## TC-10 A damaged data file

**Aim:** Check that lines the data file cannot explain -- an unknown type letter, and a deadline with
no date -- are counted and set aside rather than costing the user the readable tasks around them.

### Data file

```text
T | 1 | read book
X | 0 | mystery parcel
D | 0 | missing date |
```

### Input

```text
list
bye
```

### Expected output

```text
{{greeting}}
 I could not read 2 of my records, sir/madam. I have set them aside.
 I have retrieved 1 of your tasks, sir/madam.
-------------------------------------------------------
-------------------------------------------------------
Your tasks, sir/madam, as they presently stand:
	1.[T][X] read book
-------------------------------------------------------
{{farewell}}
```

## TC-11 Untidy typing

**Aim:** Check that spaces before the command word, and extra spaces between the command word and
its description, do not reach the stored description.

### Input

```text
   todo    walk the dog
list
bye
```

### Expected output

```text
{{greeting}}
-------------------------------------------------------
Very good, sir/madam. I have added the following:
	[T][ ] walk the dog
 That makes 1 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
Your tasks, sir/madam, as they presently stand:
	1.[T][ ] walk the dog
-------------------------------------------------------
{{farewell}}
```

### Expected data file

```text
T | 0 | walk the dog
```

## TC-12 A refused command changes nothing

**Aim:** Check that commands the bot refuses leave the task list exactly as it was. The list is
displayed after every refusal, so a command that corrupted the list before noticing the input was
wrong would be caught at the next `list` rather than going unnoticed.

### Input

```text
todo read book
deadline return book /by 2019-06-06
todo
list
mark 9
list
delete 0
list
bye
```

### Expected output

```text
{{greeting}}
-------------------------------------------------------
Very good, sir/madam. I have added the following:
	[T][ ] read book
 That makes 1 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
Very good, sir/madam. I have added the following:
	[D][ ] return book (by: Jun 06 2019)
 That makes 2 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
 A todo without a description is rather like tea without leaves, sir/madam.
 Do try: todo <description>
-------------------------------------------------------
-------------------------------------------------------
Your tasks, sir/madam, as they presently stand:
	1.[T][ ] read book
	2.[D][ ] return book (by: Jun 06 2019)
-------------------------------------------------------
-------------------------------------------------------
 I keep no task numbered 9, sir/madam.
 Your list runs from 1 to 2.
-------------------------------------------------------
-------------------------------------------------------
Your tasks, sir/madam, as they presently stand:
	1.[T][ ] read book
	2.[D][ ] return book (by: Jun 06 2019)
-------------------------------------------------------
-------------------------------------------------------
 I keep no task numbered 0, sir/madam.
 Your list runs from 1 to 2.
-------------------------------------------------------
-------------------------------------------------------
Your tasks, sir/madam, as they presently stand:
	1.[T][ ] read book
	2.[D][ ] return book (by: Jun 06 2019)
-------------------------------------------------------
{{farewell}}
```

### Expected data file

```text
T | 0 | read book
D | 0 | return book | 2019-06-06
```

## TC-13 The first and last task numbers

**Aim:** Check the three task numbers most likely to be off by one -- the first, the last, and the
one just past the last -- and check that what is left is renumbered after a deletion from either end.
`mark 4` against three tasks must be refused rather than reaching past the end of the list.

### Input

```text
todo read book
todo return book
todo buy milk
mark 4
mark 1
mark 3
list
delete 3
list
delete 1
list
bye
```

### Expected output

```text
{{greeting}}
-------------------------------------------------------
Very good, sir/madam. I have added the following:
	[T][ ] read book
 That makes 1 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
Very good, sir/madam. I have added the following:
	[T][ ] return book
 That makes 2 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
Very good, sir/madam. I have added the following:
	[T][ ] buy milk
 That makes 3 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
 I keep no task numbered 4, sir/madam.
 Your list runs from 1 to 3.
-------------------------------------------------------
-------------------------------------------------------
Consider it done, sir/madam:
	[T][X] read book
-------------------------------------------------------
-------------------------------------------------------
Consider it done, sir/madam:
	[T][X] buy milk
-------------------------------------------------------
-------------------------------------------------------
Your tasks, sir/madam, as they presently stand:
	1.[T][X] read book
	2.[T][ ] return book
	3.[T][X] buy milk
-------------------------------------------------------
-------------------------------------------------------
Consider it forgotten, sir/madam:
	[T][X] buy milk
 That leaves 2 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
Your tasks, sir/madam, as they presently stand:
	1.[T][X] read book
	2.[T][ ] return book
-------------------------------------------------------
-------------------------------------------------------
Consider it forgotten, sir/madam:
	[T][X] read book
 That leaves 1 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
Your tasks, sir/madam, as they presently stand:
	1.[T][ ] return book
-------------------------------------------------------
{{farewell}}
```

### Expected data file

```text
T | 0 | return book
```

## TC-14 Task numbers that are not task numbers

**Aim:** Check zero, a negative number, a number too large for `int`, and a second argument where a
number was expected. The `list` at the end proves none of the four touched the task that was there.

### Input

```text
todo read book
mark 0
mark -1
mark 2147483648
mark 1 2
list
bye
```

### Expected output

```text
{{greeting}}
-------------------------------------------------------
Very good, sir/madam. I have added the following:
	[T][ ] read book
 That makes 1 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
 I keep no task numbered 0, sir/madam.
 Your list runs from 1 to 1.
-------------------------------------------------------
-------------------------------------------------------
 I keep no task numbered -1, sir/madam.
 Your list runs from 1 to 1.
-------------------------------------------------------
-------------------------------------------------------
 '2147483648' is not a number I recognise, sir/madam.
 Do try: mark <task number>
-------------------------------------------------------
-------------------------------------------------------
 '1 2' is not a number I recognise, sir/madam.
 Do try: mark <task number>
-------------------------------------------------------
-------------------------------------------------------
Your tasks, sir/madam, as they presently stand:
	1.[T][ ] read book
-------------------------------------------------------
{{farewell}}
```

### Expected data file

```text
T | 0 | read book
```

## TC-15 Marking what is already marked

**Aim:** Check that `mark` and `unmark` set a status rather than toggling it, so that repeating
either one is harmless.

### Input

```text
todo read book
mark 1
mark 1
list
unmark 1
unmark 1
list
bye
```

### Expected output

```text
{{greeting}}
-------------------------------------------------------
Very good, sir/madam. I have added the following:
	[T][ ] read book
 That makes 1 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
Consider it done, sir/madam:
	[T][X] read book
-------------------------------------------------------
-------------------------------------------------------
Consider it done, sir/madam:
	[T][X] read book
-------------------------------------------------------
-------------------------------------------------------
Your tasks, sir/madam, as they presently stand:
	1.[T][X] read book
-------------------------------------------------------
-------------------------------------------------------
Very well, sir/madam. I have returned it to the undone:
	[T][ ] read book
-------------------------------------------------------
-------------------------------------------------------
Very well, sir/madam. I have returned it to the undone:
	[T][ ] read book
-------------------------------------------------------
-------------------------------------------------------
Your tasks, sir/madam, as they presently stand:
	1.[T][ ] read book
-------------------------------------------------------
{{farewell}}
```

### Expected data file

```text
T | 0 | read book
```

## TC-16 Command words and markers inside a description

**Aim:** Check that a todo's description is taken literally -- the words `deadline` and `/by` inside
it stay part of the description and are not acted on -- while a real deadline alongside it is still
read as a date.

Until Level-8 this test case also checked that a date could itself contain `/by`, since only the
first `/by` separated the date. A date must now be a date, so that is no longer expressible; the
input that checked it has moved to TC-23, where it is refused.

### Input

```text
todo deadline the report /by tomorrow
deadline submit form /by 2019-12-02
list
bye
```

### Expected output

```text
{{greeting}}
-------------------------------------------------------
Very good, sir/madam. I have added the following:
	[T][ ] deadline the report /by tomorrow
 That makes 1 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
Very good, sir/madam. I have added the following:
	[D][ ] submit form (by: Dec 02 2019)
 That makes 2 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
Your tasks, sir/madam, as they presently stand:
	1.[T][ ] deadline the report /by tomorrow
	2.[D][ ] submit form (by: Dec 02 2019)
-------------------------------------------------------
{{farewell}}
```

### Expected data file

```text
T | 0 | deadline the report /by tomorrow
D | 0 | submit form | 2019-12-02
```

## TC-17 Text containing the file separator

**Aim:** Check that a `|` is refused wherever it would become a stored field -- a description, and an
event's start or end -- because the data file divides fields with `|` and cannot carry one inside
them. A valid deadline between the two refusals, and the `list` at the end, prove that only the
offending commands were turned away.

### Input

```text
todo read | book
deadline submit form /by 2019-12-02
event meeting /from 2pm /to 4 | pm
list
bye
```

### Expected output

```text
{{greeting}}
-------------------------------------------------------
 A task may not contain '|', sir/madam. I use it to rule the columns of my ledger.
 Do try the same instruction without it.
-------------------------------------------------------
-------------------------------------------------------
Very good, sir/madam. I have added the following:
	[D][ ] submit form (by: Dec 02 2019)
 That makes 1 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
 A task may not contain '|', sir/madam. I use it to rule the columns of my ledger.
 Do try the same instruction without it.
-------------------------------------------------------
-------------------------------------------------------
Your tasks, sir/madam, as they presently stand:
	1.[D][ ] submit form (by: Dec 02 2019)
-------------------------------------------------------
{{farewell}}
```

### Expected data file

```text
D | 0 | submit form | 2019-12-02
```

## TC-18 Reading back a description that contains the file separator

**Aim:** Record what happens to a data file that contains the separator inside a field. Since TC-17,
the bot refuses to write such a line itself, so this can only arise from editing
`data/cortisolbot.txt` by hand -- but when it does, the description is truncated at the `|` and
everything after it is lost without a word of warning.

### Data file

```text
T | 0 | read | book
```

### Input

```text
list
bye
```

### Expected output

```text
{{greeting}}
 I have retrieved 1 of your tasks, sir/madam.
-------------------------------------------------------
-------------------------------------------------------
Your tasks, sir/madam, as they presently stand:
	1.[T][ ] read
-------------------------------------------------------
{{farewell}}
```

## TC-19 Extra spaces around the parts of a command

**Aim:** Check that spaces around a description and around a date are removed, while spaces inside
the description are kept. A deadline is used so that the end of the description is followed by ` (by:`
on screen and by ` | ` in the data file, where a stray trailing space would show as a doubled space.

### Input

```text
deadline   return the book    /by   2019-06-06
list
bye
```

### Expected output

```text
{{greeting}}
-------------------------------------------------------
Very good, sir/madam. I have added the following:
	[D][ ] return the book (by: Jun 06 2019)
 That makes 1 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
Your tasks, sir/madam, as they presently stand:
	1.[D][ ] return the book (by: Jun 06 2019)
-------------------------------------------------------
{{farewell}}
```

### Expected data file

```text
D | 0 | return the book | 2019-06-06
```

## TC-20 Blank lines in the data file

**Aim:** Check that blank lines in the data file are passed over in silence rather than counted
among the lines that could not be read.

### Data file

```text

T | 1 | read book


```

### Input

```text
list
bye
```

### Expected output

```text
{{greeting}}
 I have retrieved 1 of your tasks, sir/madam.
-------------------------------------------------------
-------------------------------------------------------
Your tasks, sir/madam, as they presently stand:
	1.[T][X] read book
-------------------------------------------------------
{{farewell}}
```

## TC-21 A data file in which nothing is readable

**Aim:** Check the case where every line is illegible: the count is still reported, no retrieval is
claimed, and the session carries on with an empty list rather than refusing to start.

### Data file

```text
X | 0 | mystery parcel
nonsense
```

### Input

```text
list
bye
```

### Expected output

```text
{{greeting}}
 I could not read 2 of my records, sir/madam. I have set them aside.
-------------------------------------------------------
-------------------------------------------------------
Your list is presently empty, sir/madam. A rare luxury.
-------------------------------------------------------
{{farewell}}
```

## TC-22 A tab between a command and its argument

**Aim:** Check that any run of whitespace separates the command word from the rest of the line, not
spaces alone -- a tab is easy to paste in and must work the same way. The done task is also saved
here, which is the only test case that writes a `1` done-status to the data file.

The two input lines below are separated by real tab characters. Keep them that way: replacing a tab
with spaces silently turns this into a copy of TC-11 and stops it checking anything.

### Input

```text
todo	read book
mark	1
list
bye
```

### Expected output

```text
{{greeting}}
-------------------------------------------------------
Very good, sir/madam. I have added the following:
	[T][ ] read book
 That makes 1 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
Consider it done, sir/madam:
	[T][X] read book
-------------------------------------------------------
-------------------------------------------------------
Your tasks, sir/madam, as they presently stand:
	1.[T][X] read book
-------------------------------------------------------
{{farewell}}
```

### Expected data file

```text
T | 1 | read book
```

## TC-23 Dates the bot cannot read

**Aim:** Check each way a deadline's date can fail to be a date: free text, a date that does not
exist, an hour that does not exist, and a second `/by` where the date should be. The `list` at the
end proves that none of the four left a task behind.

### Input

```text
deadline return book /by June 6th
deadline return book /by 2019-02-30
deadline return book /by 2019-12-02 2560
deadline submit form /by next /by week
list
bye
```

### Expected output

```text
{{greeting}}
-------------------------------------------------------
 I cannot make out 'June 6th' as a date, sir/madam.
 Do try: 2019-12-02, or 2019-12-02 1800 if an hour matters.
-------------------------------------------------------
-------------------------------------------------------
 I cannot make out '2019-02-30' as a date, sir/madam.
 Do try: 2019-12-02, or 2019-12-02 1800 if an hour matters.
-------------------------------------------------------
-------------------------------------------------------
 I cannot make out '2019-12-02 2560' as a date, sir/madam.
 Do try: 2019-12-02, or 2019-12-02 1800 if an hour matters.
-------------------------------------------------------
-------------------------------------------------------
 I cannot make out 'next /by week' as a date, sir/madam.
 Do try: 2019-12-02, or 2019-12-02 1800 if an hour matters.
-------------------------------------------------------
-------------------------------------------------------
Your list is presently empty, sir/madam. A rare luxury.
-------------------------------------------------------
{{farewell}}
```

## TC-24 A deadline with an hour

**Aim:** Check that an hour given after the date is kept and shown, that midnight is shown as an hour
rather than quietly dropped, and that both reach the data file in the shape they were typed.

### Input

```text
deadline submit form /by 2019-12-02 1800
deadline collect parcel /by 2019-12-02 0000
list
bye
```

### Expected output

```text
{{greeting}}
-------------------------------------------------------
Very good, sir/madam. I have added the following:
	[D][ ] submit form (by: Dec 02 2019, 6:00pm)
 That makes 1 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
Very good, sir/madam. I have added the following:
	[D][ ] collect parcel (by: Dec 02 2019, 12:00am)
 That makes 2 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
Your tasks, sir/madam, as they presently stand:
	1.[D][ ] submit form (by: Dec 02 2019, 6:00pm)
	2.[D][ ] collect parcel (by: Dec 02 2019, 12:00am)
-------------------------------------------------------
{{farewell}}
```

### Expected data file

```text
D | 0 | submit form | 2019-12-02 1800
D | 0 | collect parcel | 2019-12-02 0000
```

## TC-25 Dates read back from the data file

**Aim:** Check the loading side of Level-8. A deadline saved with an hour and one saved without must
come back showing exactly what they showed when they were saved, and a deadline left behind by an
older version of the bot -- when free text was still accepted -- must be set aside as illegible
rather than stopping the session.

### Data file

```text
D | 1 | submit form | 2019-12-02 1800
D | 0 | return book | 2019-06-06
D | 0 | old habit | June 6th
```

### Input

```text
list
bye
```

### Expected output

```text
{{greeting}}
 I could not read 1 of my records, sir/madam. I have set them aside.
 I have retrieved 2 of your tasks, sir/madam.
-------------------------------------------------------
-------------------------------------------------------
Your tasks, sir/madam, as they presently stand:
	1.[D][X] submit form (by: Dec 02 2019, 6:00pm)
	2.[D][ ] return book (by: Jun 06 2019)
-------------------------------------------------------
{{farewell}}
```

## TC-26 Finding tasks by keyword

**Aim:** Check that `find` shows every task whose description contains the keyword and no others,
that the search ignores case, and that searching leaves the list exactly as it was -- the `list` at
the end and the saved file both still hold all three tasks.

### Input

```text
todo read book
deadline return book /by 2019-06-06
todo buy milk
find book
find BOOK
find milk
list
bye
```

### Expected output

```text
{{greeting}}
-------------------------------------------------------
Very good, sir/madam. I have added the following:
	[T][ ] read book
 That makes 1 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
Very good, sir/madam. I have added the following:
	[D][ ] return book (by: Jun 06 2019)
 That makes 2 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
Very good, sir/madam. I have added the following:
	[T][ ] buy milk
 That makes 3 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
The following mention 'book', sir/madam:
	1.[T][ ] read book
	2.[D][ ] return book (by: Jun 06 2019)
-------------------------------------------------------
-------------------------------------------------------
The following mention 'BOOK', sir/madam:
	1.[T][ ] read book
	2.[D][ ] return book (by: Jun 06 2019)
-------------------------------------------------------
-------------------------------------------------------
The following mention 'milk', sir/madam:
	1.[T][ ] buy milk
-------------------------------------------------------
-------------------------------------------------------
Your tasks, sir/madam, as they presently stand:
	1.[T][ ] read book
	2.[D][ ] return book (by: Jun 06 2019)
	3.[T][ ] buy milk
-------------------------------------------------------
{{farewell}}
```

### Expected data file

```text
T | 0 | read book
D | 0 | return book | 2019-06-06
T | 0 | buy milk
```

## TC-27 Searching for what is not there

**Aim:** Check the three ways a search can come up short -- an empty list, a keyword nothing matches,
and no keyword at all -- interleaved with an add, so that a search that quietly disturbed the list
would show up in the `list` at the end.

### Input

```text
find book
todo read book
find newspaper
find
list
bye
```

### Expected output

```text
{{greeting}}
-------------------------------------------------------
Nothing in your list mentions 'book', sir/madam.
-------------------------------------------------------
-------------------------------------------------------
Very good, sir/madam. I have added the following:
	[T][ ] read book
 That makes 1 in your keeping.
-------------------------------------------------------
-------------------------------------------------------
Nothing in your list mentions 'newspaper', sir/madam.
-------------------------------------------------------
-------------------------------------------------------
 You have not said what to look for, sir/madam.
 Do try: find <keyword>
-------------------------------------------------------
-------------------------------------------------------
Your tasks, sir/madam, as they presently stand:
	1.[T][ ] read book
-------------------------------------------------------
{{farewell}}
```

### Expected data file

```text
T | 0 | read book
```
