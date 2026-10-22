# Aigis UI test plan

This plan describes black-box console sessions for `aigis.Aigis`. Every input
line is one command sent to a fresh process with an isolated working directory.
Expected output is an exact stdout transcript, including the startup banner and
closing message.

Run the plan from the project root with:

```text
python .codex/skills/test-ui/scripts/run_ui_tests.py --plan test/ui-test-plan.md
```

- Main class: `aigis.Aigis`
- Java requirement: Java 25 (`java` and `javac`)
- Failure policy: stop immediately after the first failed test case

## Test case 1: Exit immediately

Aim: Verify that Aigis starts with no data file and closes cleanly when the user enters `bye`.

Inputs:

```text
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

_______
_______
 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```

## Test case 7: Reject empty fields without changing task count

Aim: Verify that empty task fields are rejected and valid tasks remain correctly positioned after invalid commands.

Inputs:

```text
list
todo   
todo valid todo
list
deadline /by 2019-10-15
deadline submit report /by 2019-10-15
list
event /from 9am /to 10am
event team meeting /from 9am /to 10am
list
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

_______
_______
_______
Task description cannot be null or blank.
_______
_______
New objective: valid todo
_______
_______
1. [T] [ ] valid todo
_______
_______
Please use: deadline <description> /by <date>
_______
_______
New objective: submit report ( by: Oct 15 2019 )
_______
_______
1. [T] [ ] valid todo
2. [D] [ ] submit report ( by: Oct 15 2019 )
_______
_______
Please use: event <description> /from <start> /to <end>
_______
_______
New objective: team meeting( from: 9am to: 10am )
_______
_______
1. [T] [ ] valid todo
2. [D] [ ] submit report ( by: Oct 15 2019 )
3. [E] [ ] team meeting( from: 9am to: 10am )
_______
_______
_______
 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```

## Test case 2: Handle an unknown command

Aim: Verify that an unrecognized command receives an explicit response and does not create a task.

Inputs:

```text
what
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

_______
I don't understand that command.
_______
_______
_______
 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```

## Test case 3: Manage a todo task

Aim: Verify adding a todo, listing it, marking it done, and unmarking it.

Inputs:

```text
todo read chapter
list
mark 1
list
unmark 1
list
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

_______
New objective: read chapter
_______
_______
1. [T] [ ] read chapter
_______
_______
Marked as done: [T] [X] read chapter
_______
_______
1. [T] [X] read chapter
_______
_______
Unmarked as done: [T] [ ] read chapter
_______
_______
1. [T] [ ] read chapter
_______
_______
_______
 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```

## Test case 4: Reject malformed deadline and event commands

Aim: Verify that incomplete deadline and event commands are rejected without throwing an exception or adding a task.

Inputs:

```text
deadline report
deadline report /by
deadline report /by Friday
event team meeting
event team meeting /from 10am
event team meeting /from 10am /to
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

_______
Please use: deadline <description> /by <date>
_______
_______
Please use: deadline <description> /by <date>
_______
_______
Please use: deadline <description> /by <date>
_______
_______
Please use: event <description> /from <start> /to <end>
_______
_______
Please use: event <description> /from <start> /to <end>
_______
_______
Please use: event <description> /from <start> /to <end>
_______
_______
_______
 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```

## Test case 5: Add deadline and event tasks

Aim: Verify that deadline and event commands parse their fields and display them in a list.

Inputs:

```text
deadline submit report /by 2019-10-15
event team meeting /from 10am /to 11am
list
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

_______
New objective: submit report ( by: Oct 15 2019 )
_______
_______
New objective: team meeting( from: 10am to: 11am )
_______
_______
1. [D] [ ] submit report ( by: Oct 15 2019 )
2. [E] [ ] team meeting( from: 10am to: 11am )
_______
_______
_______
 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```

## Test case 6: Reject invalid task numbers

Aim: Verify that invalid and out-of-range task numbers produce clear errors without changing the task list.

Inputs:

```text
todo task
mark nope
unmark 2
list
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

_______
New objective: task
_______
_______
Please provide a valid task number.
_______
_______
That task does not exist.
_______
_______
1. [T] [ ] task
_______
_______
_______
 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```

## Test case 8: Preserve completion state after invalid status commands

Aim: Verify that malformed and out-of-range mark commands do not alter valid task completion states or task counts.

Inputs:

```text
todo first task
mark 1
mark 1 extra
todo second task
unmark 1
mark 0
list
deadline submit report /by 2019-10-16
mark 3
unmark nope
list
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

_______
New objective: first task
_______
_______
Marked as done: [T] [X] first task
_______
_______
Please provide a valid task number.
_______
_______
New objective: second task
_______
_______
Unmarked as done: [T] [ ] first task
_______
_______
That task does not exist.
_______
_______
1. [T] [ ] first task
2. [T] [ ] second task
_______
_______
New objective: submit report ( by: Oct 16 2019 )
_______
_______
Marked as done: [D] [X] submit report ( by: Oct 16 2019 )
_______
_______
Please provide a valid task number.
_______
_______
1. [T] [ ] first task
2. [T] [ ] second task
3. [D] [X] submit report ( by: Oct 16 2019 )
_______
_______
_______
 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```

## Test case 9: Reject event markers in the wrong order

Aim: Verify that reversed event markers are rejected without displacing valid tasks or altering completion state.

Inputs:

```text
event planning /to 11am /from 10am
deadline report /by
todo retained task
deadline submit report /by 2019-10-15
event team meeting /from 10am /to 11am
list
mark 2
event planning /to 11am /from 10am
list
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

_______
Please use: event <description> /from <start> /to <end>
_______
_______
Please use: deadline <description> /by <date>
_______
_______
New objective: retained task
_______
_______
New objective: submit report ( by: Oct 15 2019 )
_______
_______
New objective: team meeting( from: 10am to: 11am )
_______
_______
1. [T] [ ] retained task
2. [D] [ ] submit report ( by: Oct 15 2019 )
3. [E] [ ] team meeting( from: 10am to: 11am )
_______
_______
Marked as done: [D] [X] submit report ( by: Oct 15 2019 )
_______
_______
Please use: event <description> /from <start> /to <end>
_______
_______
1. [T] [ ] retained task
2. [D] [X] submit report ( by: Oct 15 2019 )
3. [E] [ ] team meeting( from: 10am to: 11am )
_______
_______
_______
 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```

## Test case 12: Skip malformed saved records

Aim: Verify that malformed records do not crash startup or prevent later valid records from loading.

Initial data:

```text
T / 1 / read / book
not a task record
D / 2 / invalid status
E / 0 / incomplete event (10am)
X / 0 / unknown type
D / 0 / send report  (Friday)
```

Inputs:

```text
list
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

_______
1. [T] [X] read / book
_______
_______
_______
 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```

## Test case 13: Reject repeated task markers

Aim: Verify that repeated deadline or event markers are rejected without adding malformed tasks.

Inputs:

```text
deadline report /by 2019-10-15 /by 2019-10-16
event meeting /from 10am /to 11am /to 12pm
todo retained task
list
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

_______
Please use: deadline <description> /by <date>
_______
_______
Please use: event <description> /from <start> /to <end>
_______
_______
New objective: retained task
_______
_______
1. [T] [ ] retained task
_______
_______
_______
 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```

## Test case 11: Load tasks from disk

Aim: Verify that saved todos, deadlines, and events are loaded with their completion state.

Initial data:

```text
T / 1 / read book
D / 0 / return book  (2019-06-06)
E / 0 / project meeting (Aug 6th 2-4pm)
```

Inputs:

```text
list
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

_______
1. [T] [X] read book
2. [D] [ ] return book ( by: Jun 06 2019 )
3. [E] [ ] project meeting( from: Aug 6th to: 2-4pm )
_______
_______
_______
 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```

## Test case 10: Save changed tasks to disk

Aim: Verify that successful additions and status changes are handled in one session for persistence.

Inputs:

```text
todo read book
deadline return book /by 2019-06-06
event project meeting /from Aug 6th /to 2-4pm
mark 1
list
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

_______
New objective: read book
_______
_______
New objective: return book ( by: Jun 06 2019 )
_______
_______
New objective: project meeting( from: Aug 6th to: 2-4pm )
_______
_______
Marked as done: [T] [X] read book
_______
_______
1. [T] [X] read book
2. [D] [ ] return book ( by: Jun 06 2019 )
3. [E] [ ] project meeting( from: Aug 6th to: 2-4pm )
_______
_______
_______
 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```

## Test case 14: Validate deadline calendar dates

Aim: Verify that valid leap days are accepted while invalid leap days and months are rejected.

Inputs:

```text
deadline leap day /by 2020-02-29
deadline invalid leap day /by 2019-02-29
deadline invalid month /by 2019-13-01
list
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

_______
New objective: leap day ( by: Feb 29 2020 )
_______
_______
Please use: deadline <description> /by <date>
_______
_______
Please use: deadline <description> /by <date>
_______
_______
1. [D] [ ] leap day ( by: Feb 29 2020 )
_______
_______
_______
 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```
