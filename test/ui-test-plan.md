# Command-line UI test plan

The `test-ui` skill runs each case in a new temporary working directory. This
keeps task storage isolated, so a case must include every command needed to
set up its own state.

## Exit after startup
Aim: Verify that the application welcomes the user and exits politely.
Input:
```text
bye
```
Expected output:
```text
____________________________________________________________
__   __  ___  ____    _
\ \ / / / _ \|  _ \  / \
 \ V / | | | | | | |/ _ \
  | |  | |_| | |_| / ___ \
  |_|   \___/|____/_/   \_\
____________________________________________________________
Hello! I'm YODA.
What can I do for you?
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Add and list a task
Aim: Verify that a new task is retained and displayed with its task number.
Input:
```text
todo read book
list
bye
```
Expected output:
```text
____________________________________________________________
__   __  ___  ____    _
\ \ / / / _ \|  _ \  / \
 \ V / | | | | | | |/ _ \
  | |  | |_| | |_| / ___ \
  |_|   \___/|____/_/   \_\
____________________________________________________________
Hello! I'm YODA.
What can I do for you?
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
Bye. Hope to see you again soon!
____________________________________________________________
```

## Add and display a dated deadline
Aim: Verify that an ISO deadline date is stored and displayed in a readable format.
Input:
```text
deadline return book /by 2019-12-02
list
bye
```
Expected output:
```text
____________________________________________________________
__   __  ___  ____    _
\ \ / / / _ \|  _ \  / \
 \ V / | | | | | | |/ _ \
  | |  | |_| | |_| / ___ \
  |_|   \___/|____/_/   \_\
____________________________________________________________
Hello! I'm YODA.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Dec 02 2019)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[D][ ] return book (by: Dec 02 2019)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Reject an incorrectly formatted deadline date
Aim: Verify that an invalid deadline date produces a helpful format error.
Input:
```text
deadline return book /by 02-12-2019
bye
```
Expected output:
```text
____________________________________________________________
__   __  ___  ____    _
\ \ / / / _ \|  _ \  / \
 \ V / | | | | | | |/ _ \
  | |  | |_| | |_| / ___ \
  |_|   \___/|____/_/   \_\
____________________________________________________________
Hello! I'm YODA.
What can I do for you?
____________________________________________________________
____________________________________________________________
OOPS! Please use a date in yyyy-MM-dd format.
  Example: deadline Submit report /by 2019-10-15
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Find tasks by keyword
Aim: Verify that find returns matching task descriptions regardless of letter case.
Input:
```text
todo read book
todo borrow BOOK
find book
bye
```
Expected output:
```text
____________________________________________________________
__   __  ___  ____    _
\ \ / / / _ \|  _ \  / \
 \ V / | | | | | | |/ _ \
  | |  | |_| | |_| / ___ \
  |_|   \___/|____/_/   \_\
____________________________________________________________
Hello! I'm YODA.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] borrow BOOK
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the matching tasks in your list:
1.[T][ ] read book
2.[T][ ] borrow BOOK
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
