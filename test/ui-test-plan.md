# UI Test Plan

This file is maintained by the project-specific `$test-ui` skill.

## Test configuration

- Program build command: `javac --release 25 -d out <all Java files under src/main/java>`
- Program launch command: `java -cp out proton.Proton`
- Manual working directory: `E:\NUS\Academics\Year 2\CS2113\Individual_Project\ip`
- Java version: 25 (verified using `java -version`)
- Comparison: Exact complete stdout after normalizing CRLF/LF line endings
- Failure policy: Stop immediately after the first failed test case
- Automated Java launch: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp <absolute out path> proton.Proton`
- Automated runner: `python test/run_ui.py` (builds once with Java 25).
- Test working directory: A fresh temporary directory under `_temp/` for each case; compiled classes use an absolute path.
- Preconditions: Start each case with a fresh Proton process, an empty in-memory task list, and no data directory unless a case supplies initial_save. This isolates tests from real saved tasks.
- Save checks: UI-SAVE-01 checks exact UTF-8 file contents after every command response while the process remains running; `null` means the file must not exist. Normalize only line endings.
- Storage format: New saves use `PROTON 1` followed by tab-separated type, status (0/1), and Base64-encoded UTF-8 fields. Legacy fixtures remain unchanged until a successful mutation.
- Control-input notation: input_text is the exact JSON-decoded input; the transcript displays NUL as `\u0000` to keep this Markdown file readable.
- Failure fixtures: initial_hex supplies raw bytes; directory_save creates a directory at the save path; preserve_bytes verifies byte preservation. Per-command actions block/unblock the save path or replace it externally after startup. All fixture actions stay in the isolated temporary directory.
- Scope: Load tasks on startup and save after each mutation. JSON objects can specify initial_save, per-command snapshots, unchanged_save, and restart_expected. Restart checks launch a second process in the same isolated directory with exactly `list` and `bye`; compare its entire stdout, stderr, and exit code.

## Refactoring coverage

The Ui extraction preserves all recorded inputs and expected outputs. Run the full
suite to cover greetings, task responses, errors, separators, Unicode, and EOF,
including save failures where success messages must not be displayed.

The TaskList extraction also preserves the recorded cases. Run the full suite
to verify list growth, one-based task validation, deletion and renumbering,
loaded task order, and restoration of membership and completion flags after
failed additions, deletions, marking, and unmarking.

The Parser extraction preserves command recognition, argument validation, error
precedence, whitespace, and delimiter behavior. Run all cases to verify that
parsing remains independent of execution and persistence.

The first Command extraction moves list and exit behavior into executable
classes. Existing cases cover empty and populated lists, farewell output, EOF,
and unchanged save files for read-only commands.

The find feature is covered with saved tasks of different types. Its case checks
multiple matches in list order, description-only and case-sensitive matching,
empty-keyword validation, and unchanged storage for this read-only command.

## Test cases

### UI-TODO-01: Add and manage a ToDo task

Aim: Verify that `todo` adds a typed task that can be listed, marked, and unmarked.

Input:

```text
todo borrow book
list
mark 1
unmark 1
list
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
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
 Nice! I've marked this task as done:
   [T][X] borrow book
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] borrow book
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] borrow book
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

### UI-DEADLINE-01: Add and manage a deadline task

Aim: Verify that `deadline` stores and displays its due date/time as text.

Input:

```text
deadline return book /by Sunday
list
mark 1
unmark 1
list
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
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
 Nice! I've marked this task as done:
   [D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [D][ ] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[D][ ] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

### UI-EVENT-01: Add and manage an event task

Aim: Verify that `event` stores and displays its start and end date/times as text.

Input:

```text
event project meeting /from Mon 2pm /to 4pm
list
mark 1
unmark 1
list
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
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
 Nice! I've marked this task as done:
   [E][X] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

### UI-TODO-INVALID-01: Reject an empty todo

Aim: Verify that an empty todo reports an error and does not stop later commands.

Input:

```text
todo
todo borrow book
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Positive charge alert! A todo needs a description.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

### UI-DEADLINE-INVALID-01: Reject a malformed deadline

Aim: Verify that a deadline without `/by` reports its required format and does not stop later commands.

Input:

```text
deadline return book
deadline return book /by Sunday
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Positive charge alert! Use: deadline DESCRIPTION /by DATE
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Sunday)
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

### UI-EVENT-INVALID-01: Reject a malformed event

Aim: Verify that an event without `/to` reports its required format and does not stop later commands.

Input:

```text
event project meeting /from Mon 2pm
event project meeting /from Mon 2pm /to 4pm
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Positive charge alert! Use: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

### UI-TASK-NUMBER-INVALID-01: Reject invalid task references

Aim: Verify that invalid task references report specific errors and do not stop later commands.

Input:

```text
mark
unmark proton
mark 1
todo borrow book
mark 2
mark 1
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Positive charge alert! Use: mark TASK_NUMBER
____________________________________________________________
____________________________________________________________
 Positive charge alert! Use: unmark TASK_NUMBER
____________________________________________________________
____________________________________________________________
 Positive charge alert! There are no tasks in Proton's orbit yet.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Positive charge alert! Choose a task number from 1 to 1.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] borrow book
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

### UI-UNKNOWN-01: Reject an unknown command

Aim: Verify that an unknown command reports an error and does not add a task.

Input:

```text
dance
list
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Positive charge alert! That command is outside Proton's orbit.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

### UI-BLANK-01: Reject a blank command

Aim: Verify that a blank command reports an error and does not stop later commands.

Input:

```text

bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Positive charge alert! No command was detected. Please enter a command.
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

### UI-CAPACITY-01: Grow beyond the former capacity

Aim: Verify that Proton accepts task 101 and still accepts `bye`.

Input:

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
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
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
 Powering down for now, I'll see you next time!
____________________________________________________________
```

### UI-DELETE-01: Delete tasks and use renumbered indices

Aim: Verify middle, last, first, and only-task deletion across task types, then add again.

Input:

```text
todo read book
deadline return book /by Sunday
event meeting /from 2pm /to 4pm
todo read book
delete 2
list
mark 2
unmark 2
delete 3
list
delete 1
list
delete 1
list
deadline return book /by Sunday
mark 1
delete 1
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
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
   [E][ ] meeting (from: 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [D][ ] return book (by: Sunday)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[E][ ] meeting (from: 2pm to: 4pm)
 3.[T][ ] read book
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [E][X] meeting (from: 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [E][ ] meeting (from: 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] read book
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[E][ ] meeting (from: 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[E][ ] meeting (from: 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [E][ ] meeting (from: 2pm to: 4pm)
 Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Sunday)
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [D][X] return book (by: Sunday)
 Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

### UI-DELETE-INVALID-01: Reject invalid deletion requests

Aim: Verify invalid requests leave tasks intact and allow later valid deletion.

Input:

```text
delete
delete 1
todo read book
delete 
delete abc
delete 1 2
delete 0
delete -1
delete 2
delete 2147483648
delete -2147483648
delete1
list
delete   1  
list
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Positive charge alert! Use: delete TASK_NUMBER
____________________________________________________________
____________________________________________________________
 Positive charge alert! There are no tasks in Proton's orbit yet.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Positive charge alert! Use: delete TASK_NUMBER
____________________________________________________________
____________________________________________________________
 Positive charge alert! Use: delete TASK_NUMBER
____________________________________________________________
____________________________________________________________
 Positive charge alert! Use: delete TASK_NUMBER
____________________________________________________________
____________________________________________________________
 Positive charge alert! Choose a task number from 1 to 1.
____________________________________________________________
____________________________________________________________
 Positive charge alert! Choose a task number from 1 to 1.
____________________________________________________________
____________________________________________________________
 Positive charge alert! Choose a task number from 1 to 1.
____________________________________________________________
____________________________________________________________
 Positive charge alert! Use: delete TASK_NUMBER
____________________________________________________________
____________________________________________________________
 Positive charge alert! Choose a task number from 1 to 1.
____________________________________________________________
____________________________________________________________
 Positive charge alert! That command is outside Proton's orbit.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] read book
 Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```


### UI-SAVE-01: Save every task-list change immediately

Aim: Verify directory creation, all task types, UTF-8 text, completion changes, overwrite after deletion, and a header-only file after deleting the last task. Check before exit; list and bye do not change the file.

Input:

```text
list
todo café | book
deadline submit /by Friday
event meeting /from 2pm /to 4pm
mark 2
unmark 2
delete 1
delete 2
delete 1
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] café | book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] submit (by: Friday)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] meeting (from: 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] submit (by: Friday)
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [D][ ] submit (by: Friday)
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] café | book
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [E][ ] meeting (from: 2pm to: 4pm)
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [D][ ] submit (by: Friday)
 Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Expected save file after each input line (JSON strings):

```json
[
  null,
  "PROTON 1\nT\t0\tY2Fmw6kgfCBib29r\n",
  "PROTON 1\nT\t0\tY2Fmw6kgfCBib29r\nD\t0\tc3VibWl0\tRnJpZGF5\n",
  "PROTON 1\nT\t0\tY2Fmw6kgfCBib29r\nD\t0\tc3VibWl0\tRnJpZGF5\nE\t0\tbWVldGluZw==\tMnBt\tNHBt\n",
  "PROTON 1\nT\t0\tY2Fmw6kgfCBib29r\nD\t1\tc3VibWl0\tRnJpZGF5\nE\t0\tbWVldGluZw==\tMnBt\tNHBt\n",
  "PROTON 1\nT\t0\tY2Fmw6kgfCBib29r\nD\t0\tc3VibWl0\tRnJpZGF5\nE\t0\tbWVldGluZw==\tMnBt\tNHBt\n",
  "PROTON 1\nD\t0\tc3VibWl0\tRnJpZGF5\nE\t0\tbWVldGluZw==\tMnBt\tNHBt\n",
  "PROTON 1\nD\t0\tc3VibWl0\tRnJpZGF5\n",
  "PROTON 1\n",
  "PROTON 1\n"
]
```

### UI-LOAD-01: Restore and modify saved tasks across restarts

Aim: Restore all task types, status, order, dates, and Unicode; modify the loaded list and verify another process restores the saved changes.

Input:

```text
list
unmark 1
mark 2
delete 3
event lunch /from noon /to 1pm
list
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] café | book
 2.[D][ ] submit (by: Friday)
 3.[E][X] meeting (from: 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] café | book
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] submit (by: Friday)
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [E][X] meeting (from: 2pm to: 4pm)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] lunch (from: noon to: 1pm)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] café | book
 2.[D][X] submit (by: Friday)
 3.[E][ ] lunch (from: noon to: 1pm)
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Storage configuration (exact UTF-8 contents and restart expectations):

```json
{
  "initial_save": "[T][X] café | book\n[D][ ] submit (by: Friday)\n[E][X] meeting (from: 2pm to: 4pm)\n",
  "snapshots": [
    "[T][X] café | book\n[D][ ] submit (by: Friday)\n[E][X] meeting (from: 2pm to: 4pm)\n",
    "PROTON 1\nT\t0\tY2Fmw6kgfCBib29r\nD\t0\tc3VibWl0\tRnJpZGF5\nE\t1\tbWVldGluZw==\tMnBt\tNHBt\n",
    "PROTON 1\nT\t0\tY2Fmw6kgfCBib29r\nD\t1\tc3VibWl0\tRnJpZGF5\nE\t1\tbWVldGluZw==\tMnBt\tNHBt\n",
    "PROTON 1\nT\t0\tY2Fmw6kgfCBib29r\nD\t1\tc3VibWl0\tRnJpZGF5\n",
    "PROTON 1\nT\t0\tY2Fmw6kgfCBib29r\nD\t1\tc3VibWl0\tRnJpZGF5\nE\t0\tbHVuY2g=\tbm9vbg==\tMXBt\n",
    "PROTON 1\nT\t0\tY2Fmw6kgfCBib29r\nD\t1\tc3VibWl0\tRnJpZGF5\nE\t0\tbHVuY2g=\tbm9vbg==\tMXBt\n",
    "PROTON 1\nT\t0\tY2Fmw6kgfCBib29r\nD\t1\tc3VibWl0\tRnJpZGF5\nE\t0\tbHVuY2g=\tbm9vbg==\tMXBt\n"
  ],
  "restart_expected": " ____            _              \n|  _ \\ _ __ ___ | |_ ___  _ __ \n| |_) | '__/ _ \\| __/ _ \\| '_ \\\n|  __/| | | (_) | || (_) | | | |\n|_|   |_|  \\___/ \\__\\___/|_| |_|\n\n____________________________________________________________\nHey there! I'm Proton, your positively charged chatbot!\nI'm fired up and ready to help! What awesome thing shall we tackle today?\n____________________________________________________________\n____________________________________________________________\n Here are the tasks in your list:\n 1.[T][ ] café | book\n 2.[D][X] submit (by: Friday)\n 3.[E][ ] lunch (from: noon to: 1pm)\n____________________________________________________________\n____________________________________________________________\n Powering down for now, I'll see you next time!\n____________________________________________________________\n"
}
```

### UI-LOAD-EMPTY-01: Load an empty save file

Aim: Start with an empty list and preserve the zero-byte legacy file.

Input:

```text
list
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Storage configuration (exact UTF-8 contents and restart expectations):

```json
{
  "initial_save": "",
  "snapshots": [
    "",
    ""
  ],
  "restart_expected": " ____            _              \n|  _ \\ _ __ ___ | |_ ___  _ __ \n| |_) | '__/ _ \\| __/ _ \\| '_ \\\n|  __/| | | (_) | || (_) | | | |\n|_|   |_|  \\___/ \\__\\___/|_| |_|\n\n____________________________________________________________\nHey there! I'm Proton, your positively charged chatbot!\nI'm fired up and ready to help! What awesome thing shall we tackle today?\n____________________________________________________________\n____________________________________________________________\n Here are the tasks in your list:\n____________________________________________________________\n____________________________________________________________\n Powering down for now, I'll see you next time!\n____________________________________________________________\n"
}
```

### UI-LOAD-INVALID-01: Preserve a malformed save file

Aim: Stop before accepting a mutation when a later saved line is malformed, preserving the whole original file.

Input:

```text
todo must not overwrite
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Storage configuration (exact UTF-8 contents and restart expectations):

```json
{
  "initial_save": "[T][ ] valid task\nnot a saved task\n",
  "unchanged_save": "[T][ ] valid task\nnot a saved task\n"
}
```

### UI-INVALID-VERSION: Reject invalid saved data

Aim: Reject the invalid record without processing mutations or altering any saved bytes.

Input:

```text
todo ignored
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Storage configuration:

```json
{
  "initial_save": "PROTON 2\n",
  "preserve_bytes": true
}
```

### UI-INVALID-STATUS: Reject invalid saved data

Aim: Reject the invalid record without processing mutations or altering any saved bytes.

Input:

```text
todo ignored
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Storage configuration:

```json
{
  "initial_save": "PROTON 1\nT\t2\teA==\n",
  "preserve_bytes": true
}
```

### UI-INVALID-TYPE: Reject invalid saved data

Aim: Reject the invalid record without processing mutations or altering any saved bytes.

Input:

```text
todo ignored
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Storage configuration:

```json
{
  "initial_save": "PROTON 1\nQ\t0\teA==\n",
  "preserve_bytes": true
}
```

### UI-INVALID-FIELDS: Reject invalid saved data

Aim: Reject the invalid record without processing mutations or altering any saved bytes.

Input:

```text
todo ignored
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Storage configuration:

```json
{
  "initial_save": "PROTON 1\nD\t0\teA==\n",
  "preserve_bytes": true
}
```

### UI-INVALID-EXTRA: Reject invalid saved data

Aim: Reject the invalid record without processing mutations or altering any saved bytes.

Input:

```text
todo ignored
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Storage configuration:

```json
{
  "initial_save": "PROTON 1\nT\t0\teA==\teQ==\n",
  "preserve_bytes": true
}
```

### UI-INVALID-BASE64: Reject invalid saved data

Aim: Reject the invalid record without processing mutations or altering any saved bytes.

Input:

```text
todo ignored
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Storage configuration:

```json
{
  "initial_save": "PROTON 1\nT\t0\t%%%\n",
  "preserve_bytes": true
}
```

### UI-INVALID-BLANK: Reject invalid saved data

Aim: Reject the invalid record without processing mutations or altering any saved bytes.

Input:

```text
todo ignored
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Storage configuration:

```json
{
  "initial_save": "PROTON 1\nT\t0\tICAg\n",
  "preserve_bytes": true
}
```

### UI-INVALID-CONTROL: Reject invalid saved data

Aim: Reject the invalid record without processing mutations or altering any saved bytes.

Input:

```text
todo ignored
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Storage configuration:

```json
{
  "initial_save": "PROTON 1\nT\t0\tYQpi\n",
  "preserve_bytes": true
}
```

### UI-INVALID-PARTIAL: Reject invalid saved data

Aim: Reject the invalid record without processing mutations or altering any saved bytes.

Input:

```text
todo ignored
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Storage configuration:

```json
{
  "initial_save": "PROTON 1\nT\t0\tdmFsaWQ=\nT\t0\t\n",
  "preserve_bytes": true
}
```

### UI-INVALID-EMPTY-LINE: Reject invalid saved data

Aim: Reject the invalid record without processing mutations or altering any saved bytes.

Input:

```text
todo ignored
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Storage configuration:

```json
{
  "initial_save": "PROTON 1\n\n",
  "preserve_bytes": true
}
```

### UI-INVALID-LEGACY-BLANK: Reject invalid saved data

Aim: Reject the invalid record without processing mutations or altering any saved bytes.

Input:

```text
todo ignored
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Storage configuration:

```json
{
  "initial_save": "[T][ ]    \n",
  "preserve_bytes": true
}
```

### UI-INVALID-LEGACY-DEADLINE: Reject invalid saved data

Aim: Reject the invalid record without processing mutations or altering any saved bytes.

Input:

```text
todo ignored
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Storage configuration:

```json
{
  "initial_save": "[D][ ] task (by: )\n",
  "preserve_bytes": true
}
```

### UI-INVALID-LEGACY-EVENT: Reject invalid saved data

Aim: Reject the invalid record without processing mutations or altering any saved bytes.

Input:

```text
todo ignored
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Storage configuration:

```json
{
  "initial_save": "[E][ ] task (from: a to: )\n",
  "preserve_bytes": true
}
```

### UI-INVALID-LEGACY-AMBIGUOUS: Reject invalid saved data

Aim: Reject the invalid record without processing mutations or altering any saved bytes.

Input:

```text
todo ignored
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Storage configuration:

```json
{
  "initial_save": "[E][ ] task (from: a to: b to: c)\n",
  "preserve_bytes": true
}
```

### UI-INVALID-DECODED-UTF8: Reject invalid saved data

Aim: Reject the invalid record without processing mutations or altering any saved bytes.

Input:

```text
todo ignored
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Storage configuration:

```json
{
  "initial_save": "PROTON 1\nT\t0\t/w==\n",
  "preserve_bytes": true
}
```

### UI-INVALID-UTF8: Reject malformed UTF-8 file bytes

Aim: Preserve a file whose bytes are not valid UTF-8.

Input:

```text
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Storage configuration:

```json
{
  "initial_hex": "fffe00",
  "preserve_bytes": true
}
```

### UI-LOAD-DIRECTORY: Reject a directory at the save path

Aim: Report an unreadable save path without removing the directory.

Input:

```text
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Storage configuration:

```json
{
  "directory_save": true
}
```

### UI-SAVE-FAILURE: Roll back every failed mutation and retry

Aim: Block the save path after startup. Failed add, delete, mark, and unmark commands must preserve list order and status; after restoring the path, saving succeeds.

Input:

```text
todo new
mark 1
unmark 2
delete 1
list
todo recovered
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Could not save data/proton.txt. No tasks were changed. Check the path and permissions, then retry or restart.
____________________________________________________________
____________________________________________________________
 Could not save data/proton.txt. No tasks were changed. Check the path and permissions, then retry or restart.
____________________________________________________________
____________________________________________________________
 Could not save data/proton.txt. No tasks were changed. Check the path and permissions, then retry or restart.
____________________________________________________________
____________________________________________________________
 Could not save data/proton.txt. No tasks were changed. Check the path and permissions, then retry or restart.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] original
 2.[T][X] finished
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] recovered
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Storage configuration:

```json
{
  "initial_save": "[T][ ] original\n[T][X] finished\n",
  "actions": {
    "0": "block",
    "5": "unblock"
  },
  "snapshots": [
    "DIRECTORY",
    "DIRECTORY",
    "DIRECTORY",
    "DIRECTORY",
    "DIRECTORY",
    "PROTON 1\nT\t0\tb3JpZ2luYWw=\nT\t1\tZmluaXNoZWQ=\nT\t0\tcmVjb3ZlcmVk\n",
    "PROTON 1\nT\t0\tb3JpZ2luYWw=\nT\t1\tZmluaXNoZWQ=\nT\t0\tcmVjb3ZlcmVk\n"
  ],
  "restart_expected": " ____            _              \n|  _ \\ _ __ ___ | |_ ___  _ __ \n| |_) | '__/ _ \\| __/ _ \\| '_ \\\n|  __/| | | (_) | || (_) | | | |\n|_|   |_|  \\___/ \\__\\___/|_| |_|\n\n____________________________________________________________\nHey there! I'm Proton, your positively charged chatbot!\nI'm fired up and ready to help! What awesome thing shall we tackle today?\n____________________________________________________________\n____________________________________________________________\n Here are the tasks in your list:\n 1.[T][ ] original\n 2.[T][X] finished\n 3.[T][ ] recovered\n____________________________________________________________\n____________________________________________________________\n Powering down for now, I'll see you next time!\n____________________________________________________________\n"
}
```

### UI-EXTERNAL-CHANGE: Preserve externally edited saves

Aim: Refuse a mutation when the file has changed since startup; roll back memory and retain external data.

Input:

```text
mark 1
list
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Could not save data/proton.txt. No tasks were changed. Check the path and permissions, then retry or restart.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] original
 2.[T][X] finished
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Storage configuration:

```json
{
  "initial_save": "[T][ ] original\n[T][X] finished\n",
  "actions": {
    "0": {
      "replace": "[T][ ] changed elsewhere\n"
    }
  },
  "snapshots": [
    "[T][ ] changed elsewhere\n",
    "[T][ ] changed elsewhere\n",
    "[T][ ] changed elsewhere\n"
  ]
}
```

### UI-FIELD-ROUNDTRIP: Preserve delimiter text and Unicode

Aim: Load an unambiguous record containing display markers, tabs, Unicode, and backslashes; save and reload without changing fields.

Input:

```text
list
mark 2
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] café | tab	here \ [X]
 2.[E][ ] notes (from: a) (from: noon to: later to: 1pm (by: Friday))
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [E][X] notes (from: a) (from: noon to: later to: 1pm (by: Friday))
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Storage configuration:

```json
{
  "initial_save": "PROTON 1\nT\t0\tY2Fmw6kgfCB0YWIJaGVyZSBcIFtYXQ==\nE\t0\tbm90ZXMgKGZyb206IGEp\tbm9vbiB0bzogbGF0ZXI=\tMXBtIChieTogRnJpZGF5KQ==\n",
  "snapshots": [
    "PROTON 1\nT\t0\tY2Fmw6kgfCB0YWIJaGVyZSBcIFtYXQ==\nE\t0\tbm90ZXMgKGZyb206IGEp\tbm9vbiB0bzogbGF0ZXI=\tMXBtIChieTogRnJpZGF5KQ==\n",
    "PROTON 1\nT\t0\tY2Fmw6kgfCB0YWIJaGVyZSBcIFtYXQ==\nE\t1\tbm90ZXMgKGZyb206IGEp\tbm9vbiB0bzogbGF0ZXI=\tMXBtIChieTogRnJpZGF5KQ==\n",
    "PROTON 1\nT\t0\tY2Fmw6kgfCB0YWIJaGVyZSBcIFtYXQ==\nE\t1\tbm90ZXMgKGZyb206IGEp\tbm9vbiB0bzogbGF0ZXI=\tMXBtIChieTogRnJpZGF5KQ==\n"
  ],
  "restart_expected": " ____            _              \n|  _ \\ _ __ ___ | |_ ___  _ __ \n| |_) | '__/ _ \\| __/ _ \\| '_ \\\n|  __/| | | (_) | || (_) | | | |\n|_|   |_|  \\___/ \\__\\___/|_| |_|\n\n____________________________________________________________\nHey there! I'm Proton, your positively charged chatbot!\nI'm fired up and ready to help! What awesome thing shall we tackle today?\n____________________________________________________________\n____________________________________________________________\n Here are the tasks in your list:\n 1.[T][ ] café | tab\there \\ [X]\n 2.[E][X] notes (from: a) (from: noon to: later to: 1pm (by: Friday))\n____________________________________________________________\n____________________________________________________________\n Powering down for now, I'll see you next time!\n____________________________________________________________\n"
}
```

### UI-BOM-CRLF: Read a UTF-8 BOM and CRLF lines

Aim: Accept a byte-order mark and Windows line endings while preserving original bytes on read-only commands.

Input:

```text
list
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] hello
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Storage configuration:

```json
{
  "initial_hex": "efbbbf50524f544f4e20310d0a54093009614756736247383d0d0a",
  "preserve_bytes": true
}
```

### UI-EOF: Exit cleanly at end of input

Aim: EOF terminates without requiring a bye command.

Input:

```text
list
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
```

### UI-CONTROL-INPUT: Reject control characters

Aim: Reject a NUL in command text without creating an unreadable saved task.

Input:

```text
todo bad\u0000text
list
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Positive charge alert! Commands cannot contain control characters.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Storage configuration:

```json
{
  "input_text": "todo bad\u0000text\nlist\nbye\n"
}
```

### UI-EXIT-01: Stop before commands after bye

Aim: Verify that exit displays its response and ignores later input.

Input:

```text
bye
todo ignored
list
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

### UI-FIND-01: Find tasks by description keyword

Aim: Verify that `find` searches task descriptions, preserves matching order, and requires a keyword.

Input:

```text
find book
find June
find Book
find
bye
```

Expected output:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] read book
 2.[D][X] return book (by: June 6th)
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
____________________________________________________________
____________________________________________________________
 Positive charge alert! Use: find KEYWORD
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Storage fixture and unchanged-file checks after every command:

```json
{
  "initial_save": "[T][ ] read book\n[D][X] return book (by: June 6th)\n[T][ ] call mom\n",
  "snapshots": [
    "[T][ ] read book\n[D][X] return book (by: June 6th)\n[T][ ] call mom\n",
    "[T][ ] read book\n[D][X] return book (by: June 6th)\n[T][ ] call mom\n",
    "[T][ ] read book\n[D][X] return book (by: June 6th)\n[T][ ] call mom\n",
    "[T][ ] read book\n[D][X] return book (by: June 6th)\n[T][ ] call mom\n",
    "[T][ ] read book\n[D][X] return book (by: June 6th)\n[T][ ] call mom\n"
  ]
}
```

## Latest test session

Timestamp: 2026-09-28T22:14:04.956750+08:00

Result: PASS

Java: java version "25.0.4" 2026-07-21 LTS

### UI-TODO-01: Add and manage a ToDo task

PASS; exit code: 0

Input:

```text
todo borrow book
list
mark 1
unmark 1
list
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
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
 Nice! I've marked this task as done:
   [T][X] borrow book
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] borrow book
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] borrow book
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Stderr:

```text
```

### UI-DEADLINE-01: Add and manage a deadline task

PASS; exit code: 0

Input:

```text
deadline return book /by Sunday
list
mark 1
unmark 1
list
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
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
 Nice! I've marked this task as done:
   [D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [D][ ] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[D][ ] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Stderr:

```text
```

### UI-EVENT-01: Add and manage an event task

PASS; exit code: 0

Input:

```text
event project meeting /from Mon 2pm /to 4pm
list
mark 1
unmark 1
list
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
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
 Nice! I've marked this task as done:
   [E][X] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Stderr:

```text
```

### UI-TODO-INVALID-01: Reject an empty todo

PASS; exit code: 0

Input:

```text
todo
todo borrow book
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Positive charge alert! A todo needs a description.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Stderr:

```text
```

### UI-DEADLINE-INVALID-01: Reject a malformed deadline

PASS; exit code: 0

Input:

```text
deadline return book
deadline return book /by Sunday
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Positive charge alert! Use: deadline DESCRIPTION /by DATE
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Sunday)
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Stderr:

```text
```

### UI-EVENT-INVALID-01: Reject a malformed event

PASS; exit code: 0

Input:

```text
event project meeting /from Mon 2pm
event project meeting /from Mon 2pm /to 4pm
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Positive charge alert! Use: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Stderr:

```text
```

### UI-TASK-NUMBER-INVALID-01: Reject invalid task references

PASS; exit code: 0

Input:

```text
mark
unmark proton
mark 1
todo borrow book
mark 2
mark 1
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Positive charge alert! Use: mark TASK_NUMBER
____________________________________________________________
____________________________________________________________
 Positive charge alert! Use: unmark TASK_NUMBER
____________________________________________________________
____________________________________________________________
 Positive charge alert! There are no tasks in Proton's orbit yet.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Positive charge alert! Choose a task number from 1 to 1.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] borrow book
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Stderr:

```text
```

### UI-UNKNOWN-01: Reject an unknown command

PASS; exit code: 0

Input:

```text
dance
list
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Positive charge alert! That command is outside Proton's orbit.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Stderr:

```text
```

### UI-BLANK-01: Reject a blank command

PASS; exit code: 0

Input:

```text

bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Positive charge alert! No command was detected. Please enter a command.
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Stderr:

```text
```

### UI-CAPACITY-01: Grow beyond the former capacity

PASS; exit code: 0

Input:

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
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
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
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Stderr:

```text
```

### UI-DELETE-01: Delete tasks and use renumbered indices

PASS; exit code: 0

Input:

```text
todo read book
deadline return book /by Sunday
event meeting /from 2pm /to 4pm
todo read book
delete 2
list
mark 2
unmark 2
delete 3
list
delete 1
list
delete 1
list
deadline return book /by Sunday
mark 1
delete 1
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
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
   [E][ ] meeting (from: 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [D][ ] return book (by: Sunday)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[E][ ] meeting (from: 2pm to: 4pm)
 3.[T][ ] read book
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [E][X] meeting (from: 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [E][ ] meeting (from: 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] read book
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[E][ ] meeting (from: 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[E][ ] meeting (from: 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [E][ ] meeting (from: 2pm to: 4pm)
 Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Sunday)
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [D][X] return book (by: Sunday)
 Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Stderr:

```text
```

### UI-DELETE-INVALID-01: Reject invalid deletion requests

PASS; exit code: 0

Input:

```text
delete
delete 1
todo read book
delete 
delete abc
delete 1 2
delete 0
delete -1
delete 2
delete 2147483648
delete -2147483648
delete1
list
delete   1  
list
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Positive charge alert! Use: delete TASK_NUMBER
____________________________________________________________
____________________________________________________________
 Positive charge alert! There are no tasks in Proton's orbit yet.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Positive charge alert! Use: delete TASK_NUMBER
____________________________________________________________
____________________________________________________________
 Positive charge alert! Use: delete TASK_NUMBER
____________________________________________________________
____________________________________________________________
 Positive charge alert! Use: delete TASK_NUMBER
____________________________________________________________
____________________________________________________________
 Positive charge alert! Choose a task number from 1 to 1.
____________________________________________________________
____________________________________________________________
 Positive charge alert! Choose a task number from 1 to 1.
____________________________________________________________
____________________________________________________________
 Positive charge alert! Choose a task number from 1 to 1.
____________________________________________________________
____________________________________________________________
 Positive charge alert! Use: delete TASK_NUMBER
____________________________________________________________
____________________________________________________________
 Positive charge alert! Choose a task number from 1 to 1.
____________________________________________________________
____________________________________________________________
 Positive charge alert! That command is outside Proton's orbit.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] read book
 Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Stderr:

```text
```

### UI-SAVE-01: Save every task-list change immediately

PASS; exit code: 0

Input:

```text
list
todo café | book
deadline submit /by Friday
event meeting /from 2pm /to 4pm
mark 2
unmark 2
delete 1
delete 2
delete 1
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] café | book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] submit (by: Friday)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] meeting (from: 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] submit (by: Friday)
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [D][ ] submit (by: Friday)
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] café | book
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [E][ ] meeting (from: 2pm to: 4pm)
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [D][ ] submit (by: Friday)
 Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Stderr:

```text
```

Save checks:

```text
list: PASS; save file = null
todo café | book: PASS; save file = "PROTON 1\nT\t0\tY2Fmw6kgfCBib29r\n"
deadline submit /by Friday: PASS; save file = "PROTON 1\nT\t0\tY2Fmw6kgfCBib29r\nD\t0\tc3VibWl0\tRnJpZGF5\n"
event meeting /from 2pm /to 4pm: PASS; save file = "PROTON 1\nT\t0\tY2Fmw6kgfCBib29r\nD\t0\tc3VibWl0\tRnJpZGF5\nE\t0\tbWVldGluZw==\tMnBt\tNHBt\n"
mark 2: PASS; save file = "PROTON 1\nT\t0\tY2Fmw6kgfCBib29r\nD\t1\tc3VibWl0\tRnJpZGF5\nE\t0\tbWVldGluZw==\tMnBt\tNHBt\n"
unmark 2: PASS; save file = "PROTON 1\nT\t0\tY2Fmw6kgfCBib29r\nD\t0\tc3VibWl0\tRnJpZGF5\nE\t0\tbWVldGluZw==\tMnBt\tNHBt\n"
delete 1: PASS; save file = "PROTON 1\nD\t0\tc3VibWl0\tRnJpZGF5\nE\t0\tbWVldGluZw==\tMnBt\tNHBt\n"
delete 2: PASS; save file = "PROTON 1\nD\t0\tc3VibWl0\tRnJpZGF5\n"
delete 1: PASS; save file = "PROTON 1\n"
bye: PASS; save file = "PROTON 1\n"
```

### UI-LOAD-01: Restore and modify saved tasks across restarts

PASS; exit code: 0

Input:

```text
list
unmark 1
mark 2
delete 3
event lunch /from noon /to 1pm
list
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] café | book
 2.[D][ ] submit (by: Friday)
 3.[E][X] meeting (from: 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] café | book
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] submit (by: Friday)
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [E][X] meeting (from: 2pm to: 4pm)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] lunch (from: noon to: 1pm)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] café | book
 2.[D][X] submit (by: Friday)
 3.[E][ ] lunch (from: noon to: 1pm)
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Stderr:

```text
```

Save checks:

```text
list: PASS; save file = "[T][X] café | book\n[D][ ] submit (by: Friday)\n[E][X] meeting (from: 2pm to: 4pm)\n"
unmark 1: PASS; save file = "PROTON 1\nT\t0\tY2Fmw6kgfCBib29r\nD\t0\tc3VibWl0\tRnJpZGF5\nE\t1\tbWVldGluZw==\tMnBt\tNHBt\n"
mark 2: PASS; save file = "PROTON 1\nT\t0\tY2Fmw6kgfCBib29r\nD\t1\tc3VibWl0\tRnJpZGF5\nE\t1\tbWVldGluZw==\tMnBt\tNHBt\n"
delete 3: PASS; save file = "PROTON 1\nT\t0\tY2Fmw6kgfCBib29r\nD\t1\tc3VibWl0\tRnJpZGF5\n"
event lunch /from noon /to 1pm: PASS; save file = "PROTON 1\nT\t0\tY2Fmw6kgfCBib29r\nD\t1\tc3VibWl0\tRnJpZGF5\nE\t0\tbHVuY2g=\tbm9vbg==\tMXBt\n"
list: PASS; save file = "PROTON 1\nT\t0\tY2Fmw6kgfCBib29r\nD\t1\tc3VibWl0\tRnJpZGF5\nE\t0\tbHVuY2g=\tbm9vbg==\tMXBt\n"
bye: PASS; save file = "PROTON 1\nT\t0\tY2Fmw6kgfCBib29r\nD\t1\tc3VibWl0\tRnJpZGF5\nE\t0\tbHVuY2g=\tbm9vbg==\tMXBt\n"
Restart restored the saved list: PASS
```

Restart input:

```text
list
bye
```

Restart stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] café | book
 2.[D][X] submit (by: Friday)
 3.[E][ ] lunch (from: noon to: 1pm)
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Restart stderr:

```text
```

Restart exit code: 0

### UI-LOAD-EMPTY-01: Load an empty save file

PASS; exit code: 0

Input:

```text
list
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Stderr:

```text
```

Save checks:

```text
list: PASS; save file = ""
bye: PASS; save file = ""
Restart restored the saved list: PASS
```

Restart input:

```text
list
bye
```

Restart stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Restart stderr:

```text
```

Restart exit code: 0

### UI-LOAD-INVALID-01: Preserve a malformed save file

PASS; exit code: 0

Input:

```text
todo must not overwrite
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Stderr:

```text
```

Save checks:

```text

Startup preserved the save file: PASS
```

### UI-INVALID-VERSION: Reject invalid saved data

PASS; exit code: 0

Input:

```text
todo ignored
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Stderr:

```text
```

### UI-INVALID-STATUS: Reject invalid saved data

PASS; exit code: 0

Input:

```text
todo ignored
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Stderr:

```text
```

### UI-INVALID-TYPE: Reject invalid saved data

PASS; exit code: 0

Input:

```text
todo ignored
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Stderr:

```text
```

### UI-INVALID-FIELDS: Reject invalid saved data

PASS; exit code: 0

Input:

```text
todo ignored
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Stderr:

```text
```

### UI-INVALID-EXTRA: Reject invalid saved data

PASS; exit code: 0

Input:

```text
todo ignored
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Stderr:

```text
```

### UI-INVALID-BASE64: Reject invalid saved data

PASS; exit code: 0

Input:

```text
todo ignored
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Stderr:

```text
```

### UI-INVALID-BLANK: Reject invalid saved data

PASS; exit code: 0

Input:

```text
todo ignored
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Stderr:

```text
```

### UI-INVALID-CONTROL: Reject invalid saved data

PASS; exit code: 0

Input:

```text
todo ignored
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Stderr:

```text
```

### UI-INVALID-PARTIAL: Reject invalid saved data

PASS; exit code: 0

Input:

```text
todo ignored
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Stderr:

```text
```

### UI-INVALID-EMPTY-LINE: Reject invalid saved data

PASS; exit code: 0

Input:

```text
todo ignored
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Stderr:

```text
```

### UI-INVALID-LEGACY-BLANK: Reject invalid saved data

PASS; exit code: 0

Input:

```text
todo ignored
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Stderr:

```text
```

### UI-INVALID-LEGACY-DEADLINE: Reject invalid saved data

PASS; exit code: 0

Input:

```text
todo ignored
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Stderr:

```text
```

### UI-INVALID-LEGACY-EVENT: Reject invalid saved data

PASS; exit code: 0

Input:

```text
todo ignored
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Stderr:

```text
```

### UI-INVALID-LEGACY-AMBIGUOUS: Reject invalid saved data

PASS; exit code: 0

Input:

```text
todo ignored
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Stderr:

```text
```

### UI-INVALID-DECODED-UTF8: Reject invalid saved data

PASS; exit code: 0

Input:

```text
todo ignored
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Stderr:

```text
```

### UI-INVALID-UTF8: Reject malformed UTF-8 file bytes

PASS; exit code: 0

Input:

```text
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Stderr:

```text
```

### UI-LOAD-DIRECTORY: Reject a directory at the save path

PASS; exit code: 0

Input:

```text
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
 Proton could not load data/proton.txt. Check the file and restart.
```

Stderr:

```text
```

### UI-SAVE-FAILURE: Roll back every failed mutation and retry

PASS; exit code: 0

Input:

```text
todo new
mark 1
unmark 2
delete 1
list
todo recovered
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Could not save data/proton.txt. No tasks were changed. Check the path and permissions, then retry or restart.
____________________________________________________________
____________________________________________________________
 Could not save data/proton.txt. No tasks were changed. Check the path and permissions, then retry or restart.
____________________________________________________________
____________________________________________________________
 Could not save data/proton.txt. No tasks were changed. Check the path and permissions, then retry or restart.
____________________________________________________________
____________________________________________________________
 Could not save data/proton.txt. No tasks were changed. Check the path and permissions, then retry or restart.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] original
 2.[T][X] finished
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] recovered
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Stderr:

```text
```

Save checks:

```text
todo new: PASS; save file = "DIRECTORY"
mark 1: PASS; save file = "DIRECTORY"
unmark 2: PASS; save file = "DIRECTORY"
delete 1: PASS; save file = "DIRECTORY"
list: PASS; save file = "DIRECTORY"
todo recovered: PASS; save file = "PROTON 1\nT\t0\tb3JpZ2luYWw=\nT\t1\tZmluaXNoZWQ=\nT\t0\tcmVjb3ZlcmVk\n"
bye: PASS; save file = "PROTON 1\nT\t0\tb3JpZ2luYWw=\nT\t1\tZmluaXNoZWQ=\nT\t0\tcmVjb3ZlcmVk\n"
Restart restored the saved list: PASS
```

Restart input:

```text
list
bye
```

Restart stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] original
 2.[T][X] finished
 3.[T][ ] recovered
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Restart stderr:

```text
```

Restart exit code: 0

### UI-EXTERNAL-CHANGE: Preserve externally edited saves

PASS; exit code: 0

Input:

```text
mark 1
list
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Could not save data/proton.txt. No tasks were changed. Check the path and permissions, then retry or restart.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] original
 2.[T][X] finished
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Stderr:

```text
```

Save checks:

```text
mark 1: PASS; save file = "[T][ ] changed elsewhere\n"
list: PASS; save file = "[T][ ] changed elsewhere\n"
bye: PASS; save file = "[T][ ] changed elsewhere\n"
```

### UI-FIELD-ROUNDTRIP: Preserve delimiter text and Unicode

PASS; exit code: 0

Input:

```text
list
mark 2
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] café | tab	here \ [X]
 2.[E][ ] notes (from: a) (from: noon to: later to: 1pm (by: Friday))
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [E][X] notes (from: a) (from: noon to: later to: 1pm (by: Friday))
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Stderr:

```text
```

Save checks:

```text
list: PASS; save file = "PROTON 1\nT\t0\tY2Fmw6kgfCB0YWIJaGVyZSBcIFtYXQ==\nE\t0\tbm90ZXMgKGZyb206IGEp\tbm9vbiB0bzogbGF0ZXI=\tMXBtIChieTogRnJpZGF5KQ==\n"
mark 2: PASS; save file = "PROTON 1\nT\t0\tY2Fmw6kgfCB0YWIJaGVyZSBcIFtYXQ==\nE\t1\tbm90ZXMgKGZyb206IGEp\tbm9vbiB0bzogbGF0ZXI=\tMXBtIChieTogRnJpZGF5KQ==\n"
bye: PASS; save file = "PROTON 1\nT\t0\tY2Fmw6kgfCB0YWIJaGVyZSBcIFtYXQ==\nE\t1\tbm90ZXMgKGZyb206IGEp\tbm9vbiB0bzogbGF0ZXI=\tMXBtIChieTogRnJpZGF5KQ==\n"
Restart restored the saved list: PASS
```

Restart input:

```text
list
bye
```

Restart stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] café | tab	here \ [X]
 2.[E][X] notes (from: a) (from: noon to: later to: 1pm (by: Friday))
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Restart stderr:

```text
```

Restart exit code: 0

### UI-BOM-CRLF: Read a UTF-8 BOM and CRLF lines

PASS; exit code: 0

Input:

```text
list
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] hello
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Stderr:

```text
```

### UI-EOF: Exit cleanly at end of input

PASS; exit code: 0

Input:

```text
list
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
```

Stderr:

```text
```

### UI-CONTROL-INPUT: Reject control characters

PASS; exit code: 0

Input:

```text
todo bad\u0000text
list
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Positive charge alert! Commands cannot contain control characters.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Stderr:

```text
```

### UI-EXIT-01: Stop before commands after bye

PASS; exit code: 0

Input:

```text
bye
todo ignored
list
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Stderr:

```text
```

### UI-FIND-01: Find tasks by description keyword

PASS; exit code: 0

Input:

```text
find book
find June
find Book
find
bye
```

Actual stdout:

```text
 ____            _              
|  _ \ _ __ ___ | |_ ___  _ __ 
| |_) | '__/ _ \| __/ _ \| '_ \
|  __/| | | (_) | || (_) | | | |
|_|   |_|  \___/ \__\___/|_| |_|

____________________________________________________________
Hey there! I'm Proton, your positively charged chatbot!
I'm fired up and ready to help! What awesome thing shall we tackle today?
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] read book
 2.[D][X] return book (by: June 6th)
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
____________________________________________________________
____________________________________________________________
 Positive charge alert! Use: find KEYWORD
____________________________________________________________
____________________________________________________________
 Powering down for now, I'll see you next time!
____________________________________________________________
```

Stderr:

```text
```

Save checks:

```text
find book: PASS; save file = "[T][ ] read book\n[D][X] return book (by: June 6th)\n[T][ ] call mom\n"
find June: PASS; save file = "[T][ ] read book\n[D][X] return book (by: June 6th)\n[T][ ] call mom\n"
find Book: PASS; save file = "[T][ ] read book\n[D][X] return book (by: June 6th)\n[T][ ] call mom\n"
find: PASS; save file = "[T][ ] read book\n[D][X] return book (by: June 6th)\n[T][ ] call mom\n"
bye: PASS; save file = "[T][ ] read book\n[D][X] return book (by: June 6th)\n[T][ ] call mom\n"
```
