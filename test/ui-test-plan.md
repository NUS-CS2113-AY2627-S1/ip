# Yanny UI Test Plan

Run these tests from the repository root after compiling with Java 25:

```text
mkdir -p out
javac -d out $(find src/main/java -name '*.java')
python3 .codex/skills/test-ui/scripts/run_ui_tests.py \
  test/ui-test-plan.md \
  --program "bash test/run-yanny-isolated.sh"
bash test/persistence-read-test.sh
bash test/persistence-error-test.sh
```

Each case runs in a fresh Yanny process. Expected output records stdout only;
the shell prompt and terminal echo are intentionally excluded.

## Test case: Add and list a Todo

### Aim

Verify that a Todo is added, counted, and displayed in the task list.

### Inputs

```text
todo borrow book
list
bye
```

### Expected output

```text
+------------------------------------------+
| YANNY_OS :: BOOT SEQUENCE COMPLETE
____    ____  ___      .__   __. .__   __. ____    ____
\   \  /   / /   \     |  \ |  | |  \ |  | \   \  /   /
 \   \/   / /  ^  \    |   \|  | |   \|  |  \   \/   /
  \_    _/ /  /_\  \   |  . `  | |  . `  |   \_    _/
    |  |  /  _____  \  |  |\   | |  |\   |     |  |
    |__| /__/     \__\ |__| \__| |__| \__|     |__|

| GREETINGS I'M YANNY.
| SYSTEM READY. AWAITING COMMAND...
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > todo borrow book
| OUTPUT > ADDED: [T][ ] borrow book
| OUTPUT > CURRENT TASK COUNT: 1
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: TASK LIST
| 1. [T][ ] borrow book
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: SHUTDOWN INITIATED
| OUTPUT > Bye. Hope to see you again!
+------------------------------------------+
```

## Test case: Delete tasks and renumber the list

### Aim

Verify that DELETE removes the selected task, REMOVE works as an alias, and
remaining tasks are renumbered after deletion.

### Inputs

```text
todo first task
todo second task
delete 1
list
remove 1
list
bye
```

### Expected output

```text
+------------------------------------------+
| YANNY_OS :: BOOT SEQUENCE COMPLETE
____    ____  ___      .__   __. .__   __. ____    ____
\   \  /   / /   \     |  \ |  | |  \ |  | \   \  /   /
 \   \/   / /  ^  \    |   \|  | |   \|  |  \   \/   /
  \_    _/ /  /_\  \   |  . `  | |  . `  |   \_    _/
    |  |  /  _____  \  |  |\   | |  |\   |     |  |
    |__| /__/     \__\ |__| \__| |__| \__|     |__|

| GREETINGS I'M YANNY.
| SYSTEM READY. AWAITING COMMAND...
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > todo first task
| OUTPUT > ADDED: [T][ ] first task
| OUTPUT > CURRENT TASK COUNT: 1
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > todo second task
| OUTPUT > ADDED: [T][ ] second task
| OUTPUT > CURRENT TASK COUNT: 2
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: DELETED TASK SUCCESSFULLY
| OUTPUT > [T][ ] first task
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: TASK LIST
| 1. [T][ ] second task
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: DELETED TASK SUCCESSFULLY
| OUTPUT > [T][ ] second task
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: TASK LIST
| OUTPUT > NO TASKS STORED
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: SHUTDOWN INITIATED
| OUTPUT > Bye. Hope to see you again!
+------------------------------------------+
```

## Test case: Reject invalid delete and remove commands

### Aim

Verify that DELETE and REMOVE reject missing, non-numeric, non-positive, and
out-of-range task numbers.

### Inputs

```text
delete
remove abc
delete 0
todo keep task
remove 2
delete 1
delete 1
bye
```

### Expected output

```text
+------------------------------------------+
| YANNY_OS :: BOOT SEQUENCE COMPLETE
____    ____  ___      .__   __. .__   __. ____    ____
\   \  /   / /   \     |  \ |  | |  \ |  | \   \  /   /
 \   \/   / /  ^  \    |   \|  | |   \|  |  \   \/   /
  \_    _/ /  /_\  \   |  . `  | |  . `  |   \_    _/
    |  |  /  _____  \  |  |\   | |  |\   |     |  |
    |__| /__/     \__\ |__| \__| |__| \__|     |__|

| GREETINGS I'M YANNY.
| SYSTEM READY. AWAITING COMMAND...
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND REJECTED
| ERROR > DELETE COMMAND REQUIRES A TASK NUMBER. USE: DELETE <NUMBER>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND REJECTED
| ERROR > REMOVE TASK NUMBER MUST BE AN INTEGER. USE: REMOVE <NUMBER>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND REJECTED
| ERROR > DELETE TASK NUMBER MUST BE POSITIVE. USE: DELETE <NUMBER>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > todo keep task
| OUTPUT > ADDED: [T][ ] keep task
| OUTPUT > CURRENT TASK COUNT: 1
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND REJECTED
| ERROR > TASK NUMBER OUT OF RANGE. USE A NUMBER FROM 1 TO 1.
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: DELETED TASK SUCCESSFULLY
| OUTPUT > [T][ ] keep task
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND REJECTED
| ERROR > NO TASKS AVAILABLE. ADD A TASK BEFORE USING DELETE.
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: SHUTDOWN INITIATED
| OUTPUT > Bye. Hope to see you again!
+------------------------------------------+
```

## Test case: Mark and unmark a Todo

### Aim

Verify that the existing completion commands update the Todo status and that
the final list shows the task as incomplete after unmarking it.

### Inputs

```text
todo return book
mark 1
unmark 1
list
bye
```

### Expected output

```text
+------------------------------------------+
| YANNY_OS :: BOOT SEQUENCE COMPLETE
____    ____  ___      .__   __. .__   __. ____    ____
\   \  /   / /   \     |  \ |  | |  \ |  | \   \  /   /
 \   \/   / /  ^  \    |   \|  | |   \|  |  \   \/   /
  \_    _/ /  /_\  \   |  . `  | |  . `  |   \_    _/
    |  |  /  _____  \  |  |\   | |  |\   |     |  |
    |__| /__/     \__\ |__| \__| |__| \__|     |__|

| GREETINGS I'M YANNY.
| SYSTEM READY. AWAITING COMMAND...
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > todo return book
| OUTPUT > ADDED: [T][ ] return book
| OUTPUT > CURRENT TASK COUNT: 1
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: MARKED TASK SUCCESSFULLY
| OUTPUT > [X] return book
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: UNMARKED TASK SUCCESFULLY
| OUTPUT > [ ] return book
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: TASK LIST
| 1. [T][ ] return book
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: SHUTDOWN INITIATED
| OUTPUT > Bye. Hope to see you again!
+------------------------------------------+
```

## Test case: Add deadline and event tasks

### Aim

Verify that a deadline date and time is parsed and displayed in a different
format, while event values keep their existing text behavior.

### Inputs

```text
deadline return book /by 2/12/2019 1800
event project meeting /from Mon 2pm /to 4pm
list
bye
```

### Expected output

```text
+------------------------------------------+
| YANNY_OS :: BOOT SEQUENCE COMPLETE
____    ____  ___      .__   __. .__   __. ____    ____
\   \  /   / /   \     |  \ |  | |  \ |  | \   \  /   /
 \   \/   / /  ^  \    |   \|  | |   \|  |  \   \/   /
  \_    _/ /  /_\  \   |  . `  | |  . `  |   \_    _/
    |  |  /  _____  \  |  |\   | |  |\   |     |  |
    |__| /__/     \__\ |__| \__| |__| \__|     |__|

| GREETINGS I'M YANNY.
| SYSTEM READY. AWAITING COMMAND...
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > deadline return book /by 2/12/2019 1800
| OUTPUT > ADDED: [D][ ] return book (by: Dec 02 2019 6:00 PM)
| OUTPUT > CURRENT TASK COUNT: 1
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > event project meeting /from Mon 2pm /to 4pm
| OUTPUT > ADDED: [E][ ] project meeting (from: Mon 2pm to: 4pm)
| OUTPUT > CURRENT TASK COUNT: 2
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: TASK LIST
| 1. [D][ ] return book (by: Dec 02 2019 6:00 PM)
| 2. [E][ ] project meeting (from: Mon 2pm to: 4pm)
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: SHUTDOWN INITIATED
| OUTPUT > Bye. Hope to see you again!
+------------------------------------------+
```

## Test case: Reject incomplete and unknown commands

### Aim

Verify that an empty todo description and an unrecognized command are rejected
without adding tasks.

### Inputs

```text
todo
blah
list
bye
```

### Expected output

```text
+------------------------------------------+
| YANNY_OS :: BOOT SEQUENCE COMPLETE
____    ____  ___      .__   __. .__   __. ____    ____
\   \  /   / /   \     |  \ |  | |  \ |  | \   \  /   /
 \   \/   / /  ^  \    |   \|  | |   \|  |  \   \/   /
  \_    _/ /  /_\  \   |  . `  | |  . `  |   \_    _/
    |  |  /  _____  \  |  |\   | |  |\   |     |  |
    |__| /__/     \__\ |__| \__| |__| \__|     |__|

| GREETINGS I'M YANNY.
| SYSTEM READY. AWAITING COMMAND...
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > todo
| YANNY_OS :: COMMAND REJECTED
| ERROR > TODO DESCRIPTION CANNOT BE EMPTY. USE: TODO <DESCRIPTION>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > blah
| YANNY_OS :: COMMAND REJECTED
| ERROR > UNKNOWN COMMAND DETECTED. USE: TODO, DEADLINE, EVENT, LIST, FIND, MARK, UNMARK, DELETE, REMOVE, OR BYE
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: TASK LIST
| OUTPUT > NO TASKS STORED
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: SHUTDOWN INITIATED
| OUTPUT > Bye. Hope to see you again!
+------------------------------------------+
```

## Test case: Reject an empty command

### Aim

Verify that a blank input line is rejected with guidance and that Yanny continues running.

### Inputs

```text

bye
```

### Expected output

```text
+------------------------------------------+
| YANNY_OS :: BOOT SEQUENCE COMPLETE
____    ____  ___      .__   __. .__   __. ____    ____
\   \  /   / /   \     |  \ |  | |  \ |  | \   \  /   /
 \   \/   / /  ^  \    |   \|  | |   \|  |  \   \/   /
  \_    _/ /  /_\  \   |  . `  | |  . `  |   \_    _/
    |  |  /  _____  \  |  |\   | |  |\   |     |  |
    |__| /__/     \__\ |__| \__| |__| \__|     |__|

| GREETINGS I'M YANNY.
| SYSTEM READY. AWAITING COMMAND...
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  >
| YANNY_OS :: COMMAND REJECTED
| ERROR > COMMAND CANNOT BE EMPTY. ENTER A SUPPORTED COMMAND.
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: SHUTDOWN INITIATED
| OUTPUT > Bye. Hope to see you again!
+------------------------------------------+
```

## Test case: Reject invalid deadline and event commands

### Aim

Verify that missing, blank, and incomplete deadline and event values produce specific correction messages.

### Inputs

```text
deadline buy milk
deadline /by tomorrow
deadline buy milk /by
event meeting
event meeting /from Monday
event /from Monday /to Tuesday
event meeting /from /to Tuesday
event meeting /from Monday /to
bye
```

### Expected output

```text
+------------------------------------------+
| YANNY_OS :: BOOT SEQUENCE COMPLETE
____    ____  ___      .__   __. .__   __. ____    ____
\   \  /   / /   \     |  \ |  | |  \ |  | \   \  /   /
 \   \/   / /  ^  \    |   \|  | |   \|  |  \   \/   /
  \_    _/ /  /_\  \   |  . `  | |  . `  |   \_    _/
    |  |  /  _____  \  |  |\   | |  |\   |     |  |
    |__| /__/     \__\ |__| \__| |__| \__|     |__|

| GREETINGS I'M YANNY.
| SYSTEM READY. AWAITING COMMAND...
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > deadline buy milk
| YANNY_OS :: COMMAND REJECTED
| ERROR > DEADLINE COMMAND REQUIRES: DEADLINE <DESCRIPTION> /BY <YYYY-MM-DD OR D/M/YYYY HHMM>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > deadline /by tomorrow
| YANNY_OS :: COMMAND REJECTED
| ERROR > DEADLINE DESCRIPTION CANNOT BE EMPTY. USE: DEADLINE <DESCRIPTION> /BY <YYYY-MM-DD OR D/M/YYYY HHMM>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > deadline buy milk /by
| YANNY_OS :: COMMAND REJECTED
| ERROR > DEADLINE /BY VALUE CANNOT BE EMPTY. USE: DEADLINE <DESCRIPTION> /BY <YYYY-MM-DD OR D/M/YYYY HHMM>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > event meeting
| YANNY_OS :: COMMAND REJECTED
| ERROR > EVENT COMMAND REQUIRES: EVENT <DESCRIPTION> /FROM <START> /TO <END>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > event meeting /from Monday
| YANNY_OS :: COMMAND REJECTED
| ERROR > EVENT COMMAND REQUIRES: EVENT <DESCRIPTION> /FROM <START> /TO <END>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > event /from Monday /to Tuesday
| YANNY_OS :: COMMAND REJECTED
| ERROR > EVENT DESCRIPTION CANNOT BE EMPTY. USE: EVENT <DESCRIPTION> /FROM <START> /TO <END>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > event meeting /from /to Tuesday
| YANNY_OS :: COMMAND REJECTED
| ERROR > EVENT /FROM VALUE CANNOT BE EMPTY. USE: EVENT <DESCRIPTION> /FROM <START> /TO <END>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > event meeting /from Monday /to
| YANNY_OS :: COMMAND REJECTED
| ERROR > EVENT /TO VALUE CANNOT BE EMPTY. USE: EVENT <DESCRIPTION> /FROM <START> /TO <END>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: SHUTDOWN INITIATED
| OUTPUT > Bye. Hope to see you again!
+------------------------------------------+
```

## Test case: Reject invalid mark and unmark commands

### Aim

Verify that missing, non-numeric, non-positive, and out-of-range task numbers are rejected while valid commands still work.

### Inputs

```text
mark
unmark
mark abc
unmark abc
mark 0
unmark 0
mark 1
todo buy milk
mark 2
unmark 2
mark 1
unmark 1
list
bye
```

### Expected output

```text
+------------------------------------------+
| YANNY_OS :: BOOT SEQUENCE COMPLETE
____    ____  ___      .__   __. .__   __. ____    ____
\   \  /   / /   \     |  \ |  | |  \ |  | \   \  /   /
 \   \/   / /  ^  \    |   \|  | |   \|  |  \   \/   /
  \_    _/ /  /_\  \   |  . `  | |  . `  |   \_    _/
    |  |  /  _____  \  |  |\   | |  |\   |     |  |
    |__| /__/     \__\ |__| \__| |__| \__|     |__|

| GREETINGS I'M YANNY.
| SYSTEM READY. AWAITING COMMAND...
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND REJECTED
| ERROR > MARK COMMAND REQUIRES A TASK NUMBER. USE: MARK <NUMBER>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND REJECTED
| ERROR > UNMARK COMMAND REQUIRES A TASK NUMBER. USE: UNMARK <NUMBER>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND REJECTED
| ERROR > MARK TASK NUMBER MUST BE AN INTEGER. USE: MARK <NUMBER>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND REJECTED
| ERROR > UNMARK TASK NUMBER MUST BE AN INTEGER. USE: UNMARK <NUMBER>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND REJECTED
| ERROR > MARK TASK NUMBER MUST BE POSITIVE. USE: MARK <NUMBER>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND REJECTED
| ERROR > UNMARK TASK NUMBER MUST BE POSITIVE. USE: UNMARK <NUMBER>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND REJECTED
| ERROR > NO TASKS AVAILABLE. ADD A TASK BEFORE USING MARK.
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > todo buy milk
| OUTPUT > ADDED: [T][ ] buy milk
| OUTPUT > CURRENT TASK COUNT: 1
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND REJECTED
| ERROR > TASK NUMBER OUT OF RANGE. USE A NUMBER FROM 1 TO 1.
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND REJECTED
| ERROR > TASK NUMBER OUT OF RANGE. USE A NUMBER FROM 1 TO 1.
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: MARKED TASK SUCCESSFULLY
| OUTPUT > [X] buy milk
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: UNMARKED TASK SUCCESFULLY
| OUTPUT > [ ] buy milk
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: TASK LIST
| 1. [T][ ] buy milk
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: SHUTDOWN INITIATED
| OUTPUT > Bye. Hope to see you again!
+------------------------------------------+
```

## Test case: Reject values that would corrupt task storage

### Aim

Verify that the storage delimiter is rejected in task descriptions instead of
creating a task file that cannot be loaded on the next startup.

### Inputs

```text
todo contains | delimiter
list
bye
```

### Expected output

```text
+------------------------------------------+
| YANNY_OS :: BOOT SEQUENCE COMPLETE
____    ____  ___      .__   __. .__   __. ____    ____
\   \  /   / /   \     |  \ |  | |  \ |  | \   \  /   /
 \   \/   / /  ^  \    |   \|  | |   \|  |  \   \/   /
  \_    _/ /  /_\  \   |  . `  | |  . `  |   \_    _/
    |  |  /  _____  \  |  |\   | |  |\   |     |  |
    |__| /__/     \__\ |__| \__| |__| \__|     |__|

| GREETINGS I'M YANNY.
| SYSTEM READY. AWAITING COMMAND...
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > todo contains | delimiter
| YANNY_OS :: COMMAND REJECTED
| ERROR > TODO DESCRIPTION CONTAINS AN UNSUPPORTED CHARACTER. REMOVE '|'.
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: TASK LIST
| OUTPUT > NO TASKS STORED
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: SHUTDOWN INITIATED
| OUTPUT > Bye. Hope to see you again!
+------------------------------------------+
```

## Test case: Parse and display ISO deadline dates

### Aim

Verify that ISO dates, including leap day, are stored as dates and displayed in a readable format.

### Inputs

```text
deadline submit report /by 2019-10-15
deadline renew pass /by 2020-02-29
list
bye
```

### Expected output

```text
+------------------------------------------+
| YANNY_OS :: BOOT SEQUENCE COMPLETE
____    ____  ___      .__   __. .__   __. ____    ____
\   \  /   / /   \     |  \ |  | |  \ |  | \   \  /   /
 \   \/   / /  ^  \    |   \|  | |   \|  |  \   \/   /
  \_    _/ /  /_\  \   |  . `  | |  . `  |   \_    _/
    |  |  /  _____  \  |  |\   | |  |\   |     |  |
    |__| /__/     \__\ |__| \__| |__| \__|     |__|

| GREETINGS I'M YANNY.
| SYSTEM READY. AWAITING COMMAND...
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > deadline submit report /by 2019-10-15
| OUTPUT > ADDED: [D][ ] submit report (by: Oct 15 2019)
| OUTPUT > CURRENT TASK COUNT: 1
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > deadline renew pass /by 2020-02-29
| OUTPUT > ADDED: [D][ ] renew pass (by: Feb 29 2020)
| OUTPUT > CURRENT TASK COUNT: 2
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: TASK LIST
| 1. [D][ ] submit report (by: Oct 15 2019)
| 2. [D][ ] renew pass (by: Feb 29 2020)
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: SHUTDOWN INITIATED
| OUTPUT > Bye. Hope to see you again!
+------------------------------------------+
```

## Test case: Reject invalid deadline dates and times

### Aim

Verify that impossible dates, invalid times, and old free-text input are rejected without adding tasks.

### Inputs

```text
deadline wrong leap day /by 2019-02-29
deadline wrong time /by 2/12/2019 2460
deadline old format /by Sunday
list
bye
```

### Expected output

```text
+------------------------------------------+
| YANNY_OS :: BOOT SEQUENCE COMPLETE
____    ____  ___      .__   __. .__   __. ____    ____
\   \  /   / /   \     |  \ |  | |  \ |  | \   \  /   /
 \   \/   / /  ^  \    |   \|  | |   \|  |  \   \/   /
  \_    _/ /  /_\  \   |  . `  | |  . `  |   \_    _/
    |  |  /  _____  \  |  |\   | |  |\   |     |  |
    |__| /__/     \__\ |__| \__| |__| \__|     |__|

| GREETINGS I'M YANNY.
| SYSTEM READY. AWAITING COMMAND...
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > deadline wrong leap day /by 2019-02-29
| YANNY_OS :: COMMAND REJECTED
| ERROR > DEADLINE /BY VALUE MUST BE A VALID DATE OR TIME. USE: DEADLINE <DESCRIPTION> /BY <YYYY-MM-DD OR D/M/YYYY HHMM>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > deadline wrong time /by 2/12/2019 2460
| YANNY_OS :: COMMAND REJECTED
| ERROR > DEADLINE /BY VALUE MUST BE A VALID DATE OR TIME. USE: DEADLINE <DESCRIPTION> /BY <YYYY-MM-DD OR D/M/YYYY HHMM>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > deadline old format /by Sunday
| YANNY_OS :: COMMAND REJECTED
| ERROR > DEADLINE /BY VALUE MUST BE A VALID DATE OR TIME. USE: DEADLINE <DESCRIPTION> /BY <YYYY-MM-DD OR D/M/YYYY HHMM>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: TASK LIST
| OUTPUT > NO TASKS STORED
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: SHUTDOWN INITIATED
| OUTPUT > Bye. Hope to see you again!
+------------------------------------------+
```

## Test case: Find tasks by description

### Aim

Verify case-insensitive keyword and phrase matches, original task numbers, status, and description-only search.

### Inputs

```text
todo read book
todo buy milk
deadline return book /by 2019-12-02
event meeting /from book room /to 4pm
mark 1
mark 3
find BOOK
find return book
list
bye
```

### Expected output

```text
+------------------------------------------+
| YANNY_OS :: BOOT SEQUENCE COMPLETE
____    ____  ___      .__   __. .__   __. ____    ____
\   \  /   / /   \     |  \ |  | |  \ |  | \   \  /   /
 \   \/   / /  ^  \    |   \|  | |   \|  |  \   \/   /
  \_    _/ /  /_\  \   |  . `  | |  . `  |   \_    _/
    |  |  /  _____  \  |  |\   | |  |\   |     |  |
    |__| /__/     \__\ |__| \__| |__| \__|     |__|

| GREETINGS I'M YANNY.
| SYSTEM READY. AWAITING COMMAND...
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > todo read book
| OUTPUT > ADDED: [T][ ] read book
| OUTPUT > CURRENT TASK COUNT: 1
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > todo buy milk
| OUTPUT > ADDED: [T][ ] buy milk
| OUTPUT > CURRENT TASK COUNT: 2
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > deadline return book /by 2019-12-02
| OUTPUT > ADDED: [D][ ] return book (by: Dec 02 2019)
| OUTPUT > CURRENT TASK COUNT: 3
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > event meeting /from book room /to 4pm
| OUTPUT > ADDED: [E][ ] meeting (from: book room to: 4pm)
| OUTPUT > CURRENT TASK COUNT: 4
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: MARKED TASK SUCCESSFULLY
| OUTPUT > [X] read book
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: MARKED TASK SUCCESSFULLY
| OUTPUT > [X] return book
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: SEARCH RESULTS
| OUTPUT > HERE ARE THE MATCHING TASKS IN YOUR LIST:
| 1. [T][X] read book
| 3. [D][X] return book (by: Dec 02 2019)
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: SEARCH RESULTS
| OUTPUT > HERE ARE THE MATCHING TASKS IN YOUR LIST:
| 3. [D][X] return book (by: Dec 02 2019)
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: TASK LIST
| 1. [T][X] read book
| 2. [T][ ] buy milk
| 3. [D][X] return book (by: Dec 02 2019)
| 4. [E][ ] meeting (from: book room to: 4pm)
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: SHUTDOWN INITIATED
| OUTPUT > Bye. Hope to see you again!
+------------------------------------------+
```

## Test case: Reject missing find keyword and show no matches

### Aim

Verify no-match feedback, blank-keyword errors, and the find command boundary.

### Inputs

```text
todo read book
find pen
find
FIND
findbook
bye
```

### Expected output

```text
+------------------------------------------+
| YANNY_OS :: BOOT SEQUENCE COMPLETE
____    ____  ___      .__   __. .__   __. ____    ____
\   \  /   / /   \     |  \ |  | |  \ |  | \   \  /   /
 \   \/   / /  ^  \    |   \|  | |   \|  |  \   \/   /
  \_    _/ /  /_\  \   |  . `  | |  . `  |   \_    _/
    |  |  /  _____  \  |  |\   | |  |\   |     |  |
    |__| /__/     \__\ |__| \__| |__| \__|     |__|

| GREETINGS I'M YANNY.
| SYSTEM READY. AWAITING COMMAND...
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > todo read book
| OUTPUT > ADDED: [T][ ] read book
| OUTPUT > CURRENT TASK COUNT: 1
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: SEARCH RESULTS
| OUTPUT > NO MATCHING TASKS FOUND
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND REJECTED
| ERROR > FIND KEYWORD CANNOT BE EMPTY. USE: FIND <KEYWORD>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND REJECTED
| ERROR > FIND KEYWORD CANNOT BE EMPTY. USE: FIND <KEYWORD>
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: COMMAND RECEIVED
| INPUT  > findbook
| YANNY_OS :: COMMAND REJECTED
| ERROR > UNKNOWN COMMAND DETECTED. USE: TODO, DEADLINE, EVENT, LIST, FIND, MARK, UNMARK, DELETE, REMOVE, OR BYE
+------------------------------------------+
+------------------------------------------+
| YANNY_OS :: SHUTDOWN INITIATED
| OUTPUT > Bye. Hope to see you again!
+------------------------------------------+
```
