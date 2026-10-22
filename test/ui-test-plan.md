# Dawn UI test plan

Run these tests from the repository root after compiling with Java 25:

```powershell
$javaSources = @(Get-ChildItem -Path src\main\java -Recurse -Filter *.java | Select-Object -ExpandProperty FullName)
javac -d out\production\ip $javaSources
powershell -ExecutionPolicy Bypass -File .codex\skills\test-ui\scripts\run-ui-tests.ps1 -ProgramCommand 'java "-Dstdout.encoding=UTF-8" -cp out/production/ip dawn.Dawn'
```

The messages use Piplup's voice. Task entries and the command table stay factual. `${STORAGE_BYE}` replaces `${BYE}` after any storage problem, including skipped damaged records; ordinary validation errors do not change the goodbye.

Each case is a new console session. The listed inputs are entered in order, and the expected output is the complete session transcript. The runner expands the following output tokens before it compares output exactly, apart from platform line endings, final newlines, and trailing padding on a line.

## Output tokens

### INTRO
```text
____________________________________________________________

                             ..#+++.-+++##+.                    
                         .#...##..............+-                
                      .+.....##+....................            
                    ..................................          
                  -............................   ..            
                ..     ...................... +##########       
               .########. .......  ........ -#############+     
              ###########+  ... .##. ... . -################    
             ###### #######.#. ###### .##-.#######. #########   
            ######  . #####.+#####+###### #####. ..   #######.  
            #####  +###+####.#- +###+..#++##### ####   #######  
           -####.  +###+####  #########  #####- ####   -######  
           +####.   ##+ #### ########### #####.   .    .######. 
           +####.       ####.##########..#####.        +######. 
   ##      .####.      .####.-#######-.#.######        #######. 
  ####      #####      #####+-#+---+###.########      ########  
##+#+###    ######+   ######## +###### ######################.  
  ##+#       ####################...#########################   
    #         ##############################################    
              .###########################################+     
                #########################################       
                 .######################-       .######         
                   .####-  .....####-.+++-......-.. .           
                      .#+........+.+#.................###.      
                ###.+#................................######.   
            +#####.++.............  ............... .##..#####. 
          .####### #............. #- ............ .##..+####### 
          ########+.#.......... .####+   ....   +##+ +#########. 
         .########+   ......  .########+.#####.###+ ########### 
          #########-.## ..-##+.#######.+######++##..##########  
          .######### #+-###### ####### ########.##..########.   
            .######+ ##.###### ######## ######.#### ######.     
              .####.-##+.#### ##########- .- .###### .###.      
              .++. #######++##########################.         
                   ###################################...       
                   -################################## .        
                    #################################.          
                     ###############################.           
                      #############################             
                      . ########################+..+.           
                   -###-.##.                 .+##.-###.         
                 .########+++                -+++#######        
               .-.+#######+.                  .########+#.      
               -#+.+###+.                       .+####+#+..     
                 .-                                 +##..#+     

Piplup here! Ready when you are!

Here are the commands you can use:
+---------------------------------------------+-----------------------+
| Command / format                            | Description           |
+---------------------------------------------+-----------------------+
| todo [description]                          | Add a todo            |
| deadline [description] /by [due date]       | Add a deadline        |
| event [description] /from [start] /to [end] | Add an event          |
| list                                        | List all tasks        |
| mark [task number]                          | Mark a task done      |
| unmark [task number]                        | Mark a task not done  |
| delete [task number]                        | Delete a task         |
| view [date]                                 | View tasks on a date  |
| find [keyword]                              | Find tasks by keyword |
| help                                        | Show commands         |
| bye                                         | Exit Dawn             |
+---------------------------------------------+-----------------------+
____________________________________________________________
```

### GUIDE
```text
Here are the commands you can use:
+---------------------------------------------+-----------------------+
| Command / format                            | Description           |
+---------------------------------------------+-----------------------+
| todo [description]                          | Add a todo            |
| deadline [description] /by [due date]       | Add a deadline        |
| event [description] /from [start] /to [end] | Add an event          |
| list                                        | List all tasks        |
| mark [task number]                          | Mark a task done      |
| unmark [task number]                        | Mark a task not done  |
| delete [task number]                        | Delete a task         |
| view [date]                                 | View tasks on a date  |
| find [keyword]                              | Find tasks by keyword |
| help                                        | Show commands         |
| bye                                         | Exit Dawn             |
+---------------------------------------------+-----------------------+
```

### BYE
```text
Pip! No need to worry, everything is saved. See you next time!
____________________________________________________________
```

### STORAGE_BYE
```text
Pip... I ran into a storage problem this session. Please check your tasks before you go. See you next time!
____________________________________________________________
```

### LINE
```text
____________________________________________________________
```

## Test cases

### TC-01: Exit cleanly
**Aim:** Confirm that `bye` ends a newly started session with the farewell message.
**Inputs:**
```text
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

${BYE}
```

### TC-02: Add every task type and list them
**Aim:** Confirm that todo, deadline, event, and list commands show the created task details in insertion order.
**Inputs:**
```text
todo read book
deadline submit report /by 2019-10-15
event team meeting /from 2pm /to 3pm
list
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Pip! Added this task: read book

${LINE}

${LINE}

Pip! Added this task: submit report

${LINE}

${LINE}

Pip! Added this task: team meeting

${LINE}

${LINE}

Here's your task list:
1.[T][ ] read book
2.[D][ ] submit report (by: 15 Oct 2019)
3.[E][ ] team meeting (from: 2pm to: 3pm)
${LINE}

${LINE}

${BYE}
```

### TC-03: Mark and unmark a task
**Aim:** Confirm that task status changes are acknowledged and reflected by `list`.
**Inputs:**
```text
todo revise notes
mark 1
unmark 1
list
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Pip! Added this task: revise notes

${LINE}

${LINE}

Nice! Marked this task as done:
	[T][X] revise notes

${LINE}

${LINE}

Back on the list. Marked this task as not done yet:
	[T][ ] revise notes

${LINE}

${LINE}

Here's your task list:
1.[T][ ] revise notes
${LINE}

${LINE}

${BYE}
```

### TC-04: Reject missing todo details and unavailable task numbers
**Aim:** Confirm that specific validation feedback is shown and that the session continues after each error.
**Inputs:**
```text
todo
mark 1
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Pip?! I need a description for your todo. Use: todo [description]

${LINE}

${LINE}

Pip?! I couldn't find that task number. Use: mark [task number]

${LINE}

${LINE}

${BYE}
```

### TC-05: Require exact command words and suggest intended commands
**Aim:** Confirm that recognizable command fragments suggest the matching syntax and unrelated commands point to `help`.
**Inputs:**
```text
todoadd read a book
addtodo read a book
deadlineurgent submit /by Friday
eventnow camp /from 2pm /to 4pm
marking 1
unmarking 1
deleteitem 1
viewtasks 2019-10-15
schedule 2019-10-15
findtasks book
add todo read a book
food
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Pip?! I don't recognize that command.
Did you mean: todo [description]?

${LINE}

${LINE}

Pip?! I don't recognize that command.
Did you mean: todo [description]?

${LINE}

${LINE}

Pip?! I don't recognize that command.
Did you mean: deadline [description] /by [due date]?

${LINE}

${LINE}

Pip?! I don't recognize that command.
Did you mean: event [description] /from [start] /to [end]?

${LINE}

${LINE}

Pip?! I don't recognize that command.
Did you mean: mark [task number]?

${LINE}

${LINE}

Pip?! I don't recognize that command.
Did you mean: unmark [task number]?

${LINE}

${LINE}

Pip?! I don't recognize that command.
Did you mean: delete [task number]?

${LINE}

${LINE}

Pip?! I don't recognize that command.
Did you mean: view [date]?

${LINE}

${LINE}

Pip?! I don't recognize that command. Type 'help' to see the available commands.

${LINE}

${LINE}

Pip?! I don't recognize that command.
Did you mean: find [keyword]?

${LINE}

${LINE}

Pip?! I don't recognize that command. Type 'help' to see the available commands.

${LINE}

${LINE}

Pip?! I don't recognize that command. Type 'help' to see the available commands.

${LINE}

${LINE}

${BYE}
```

### TC-06: Explain malformed deadline commands
**Aim:** Explain missing /by, description, and due date, while accepting freeform date strings.
**Inputs:**
```text
deadline watch lecture /by
deadline watch lecture by 2019-10-15
deadline /by 2019-10-15
deadline watch lecture /by Friday
deadline watch lecture /by 2019-10-15
list
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Pip?! I need a due date. Use: deadline [description] /by [due date]

${LINE}

${LINE}

Pip?! I need the /by keyword for your deadline. Use: deadline [description] /by [due date]

${LINE}

${LINE}

Pip?! I need a description for your deadline. Use: deadline [description] /by [due date]

${LINE}

${LINE}

Pip! Added this task: watch lecture

${LINE}

${LINE}

Pip! Added this task: watch lecture

${LINE}

${LINE}

Here's your task list:
1.[D][ ] watch lecture (by: Friday)
2.[D][ ] watch lecture (by: 15 Oct 2019)
${LINE}

${LINE}

${BYE}
```

### TC-07: Validate task numbers and commands without arguments
**Aim:** Confirm that task numbers must be positive integers without overflowing, and that `list` and `bye` reject arguments.
**Inputs:**
```text
todo read book
mark
unmark
delete
mark abc12
unmark 0
delete -1
delete 0
delete abc
mark 99999999999999999999
unmark 99999999999999999999
delete 99999999999999999999
mark 2
delete 2
list extra
bye now
list
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Pip! Added this task: read book

${LINE}

${LINE}

Pip?! I need a task number. Use: mark [task number]

${LINE}

${LINE}

Pip?! I need a task number. Use: unmark [task number]

${LINE}

${LINE}

Pip?! I need a task number. Use: delete [task number]

${LINE}

${LINE}

Pip?! I need a positive whole number for the task number. Use: mark [task number]

${LINE}

${LINE}

Pip?! I need a positive whole number for the task number. Use: unmark [task number]

${LINE}

${LINE}

Pip?! I need a positive whole number for the task number. Use: delete [task number]

${LINE}

${LINE}

Pip?! I need a positive whole number for the task number. Use: delete [task number]

${LINE}

${LINE}

Pip?! I need a positive whole number for the task number. Use: delete [task number]

${LINE}

${LINE}

Pip?! I need a positive whole number for the task number. Use: mark [task number]

${LINE}

${LINE}

Pip?! I need a positive whole number for the task number. Use: unmark [task number]

${LINE}

${LINE}

Pip?! I need a positive whole number for the task number. Use: delete [task number]

${LINE}

${LINE}

Pip?! I couldn't find that task number. Use: mark [task number]

${LINE}

${LINE}

Pip?! I couldn't find that task number. Use: delete [task number]

${LINE}

${LINE}

Pip?! I don't need arguments for list. Use: list

${LINE}

${LINE}

Pip?! I don't need arguments for bye. Use: bye

${LINE}

${LINE}

Here's your task list:
1.[T][ ] read book
${LINE}

${LINE}

${BYE}
```

### TC-08: Explain malformed event commands
**Aim:** Confirm that event errors identify the missing or misplaced field without adding an invalid task.
**Inputs:**
```text
event /from 10am /to 11am
event meeting /from /to 11am
event meeting /from 10am /to
event meeting from 10am /to 11am
event meeting /from 10am
event meeting /to 11am /from 10am
event meeting /from 10am /to 11am
list
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Pip?! I need a description for your event. Use: event [description] /from [start] /to [end]

${LINE}

${LINE}

Pip?! I need a start for your event. Use: event [description] /from [start] /to [end]

${LINE}

${LINE}

Pip?! I need an end for your event. Use: event [description] /from [start] /to [end]

${LINE}

${LINE}

Pip?! I need the /from keyword for your event. Use: event [description] /from [start] /to [end]

${LINE}

${LINE}

Pip?! I need the /to keyword for your event. Use: event [description] /from [start] /to [end]

${LINE}

${LINE}

Pip?! I need /from before /to. Use: event [description] /from [start] /to [end]

${LINE}

${LINE}

Pip! Added this task: meeting

${LINE}

${LINE}

Here's your task list:
1.[E][ ] meeting (from: 10am to: 11am)
${LINE}

${LINE}

${BYE}
```

### TC-09: Load tasks from persistent storage and skip corrupted lines
**Aim:** Confirm that Dawn loads saved tasks from data/dawn.txt, handles various corrupted line formats with warnings, and displays valid tasks.
**File input:**
```text
T | 1 | revise notes
corrupted line without delimiters
D | 0 | submit report
E | 1 | project sync | 2pm
X | 0 | unknown type
E | 0 | hackathon | Friday 6pm | Sunday 6pm
```
**Inputs:**
```text
list
bye
```
**Expected output:**
```text
Pip?! I skipped a damaged saved task: [corrupted line without delimiters] - Missing essential task components.
Pip?! I skipped a damaged saved task: [D | 0 | submit report] - Deadline is missing the due date.
Pip?! I skipped a damaged saved task: [E | 1 | project sync | 2pm] - Event is missing start or end dates.
Pip?! I skipped a damaged saved task: [X | 0 | unknown type] - Unknown task type identifier: X
${INTRO}

${LINE}

Here's your task list:
1.[T][X] revise notes
2.[E][ ] hackathon (from: Friday 6pm to: Sunday 6pm)
${LINE}

${LINE}

${STORAGE_BYE}
```

### TC-10: Delete a task loaded from storage
**Aim:** Confirm that delete removes the selected stored task and keeps the remaining task correctly numbered.
**File input:**
```text
T | 0 | read book
D | 1 | submit report | 2019-12-02 1800
```
**Inputs:**
```text
delete 1
list
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Removed this task. You have 1 task left:
	[T][ ] read book

${LINE}

${LINE}

Here's your task list:
1.[D][X] submit report (by: 02 Dec 2019 18:00)
${LINE}

${LINE}

${BYE}
```

### TC-11: Empty list display, blank input handling, and whitespace padding
**Aim:** Confirm that an empty list shows Piplup's empty-list message, blank lines suggest `help`, and whitespace around commands is trimmed.
**Inputs:**
```text
list

   
todo buy groceries
list
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Your list is empty. I was starting to get bored.
${LINE}

${LINE}

Pip?! I don't recognize that command. Type 'help' to see the available commands.

${LINE}

${LINE}

Pip?! I don't recognize that command. Type 'help' to see the available commands.

${LINE}

${LINE}

Pip! Added this task: buy groceries

${LINE}

${LINE}

Here's your task list:
1.[T][ ] buy groceries
${LINE}

${LINE}

${BYE}
```

### TC-12: Delete all tasks until list is empty and deletion boundaries
**Aim:** Confirm that deleting all tasks updates the count down to 0, subsequent list displays an empty list, and deleting from an empty list reports that the task number was not found.
**Inputs:**
```text
todo temporary task
delete 1
list
delete 1
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Pip! Added this task: temporary task

${LINE}

${LINE}

Removed this task. You have 0 tasks left:
	[T][ ] temporary task

${LINE}

${LINE}

Your list is empty. I was starting to get bored.
${LINE}

${LINE}

Pip?! I couldn't find that task number. Use: delete [task number]

${LINE}

${LINE}

${BYE}
```

### TC-13: Standalone marker boundary validation for deadline and event
**Aim:** Confirm that /by, /from, and /to must have whitespace boundaries to be recognized as markers, and multiple /by markers in the description/date are handled deterministically.
**Inputs:**
```text
deadline submit work/by tomorrow
deadline submit work /bytomorrow
event camp/from Monday /to Tuesday
event camp /from Monday/to Tuesday
deadline read book by/author /by 2019-10-15
list
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Pip?! I need the /by keyword for your deadline. Use: deadline [description] /by [due date]

${LINE}

${LINE}

Pip?! I need the /by keyword for your deadline. Use: deadline [description] /by [due date]

${LINE}

${LINE}

Pip?! I need the /from keyword for your event. Use: event [description] /from [start] /to [end]

${LINE}

${LINE}

Pip?! I need the /to keyword for your event. Use: event [description] /from [start] /to [end]

${LINE}

${LINE}

Pip! Added this task: read book by/author

${LINE}

${LINE}

Here's your task list:
1.[D][ ] read book by/author (by: 15 Oct 2019)
${LINE}

${LINE}

${BYE}
```

### TC-14: Mark and unmark idempotency
**Aim:** Confirm that repeatedly marking a completed task or unmarking an incomplete task preserves the status and outputs feedback without error.
**Inputs:**
```text
todo practice coding
mark 1
mark 1
unmark 1
unmark 1
list
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Pip! Added this task: practice coding

${LINE}

${LINE}

Nice! Marked this task as done:
	[T][X] practice coding

${LINE}

${LINE}

Nice! Marked this task as done:
	[T][X] practice coding

${LINE}

${LINE}

Back on the list. Marked this task as not done yet:
	[T][ ] practice coding

${LINE}

${LINE}

Back on the list. Marked this task as not done yet:
	[T][ ] practice coding

${LINE}

${LINE}

Here's your task list:
1.[T][ ] practice coding
${LINE}

${LINE}

${BYE}
```

### TC-15: Maximum task list capacity boundary
**Aim:** Confirm that TaskList rejects additions when the 100-task limit is reached, and allows new tasks after deleting an item.
**File input:**
```text
T | 0 | task 1
T | 0 | task 2
T | 0 | task 3
T | 0 | task 4
T | 0 | task 5
T | 0 | task 6
T | 0 | task 7
T | 0 | task 8
T | 0 | task 9
T | 0 | task 10
T | 0 | task 11
T | 0 | task 12
T | 0 | task 13
T | 0 | task 14
T | 0 | task 15
T | 0 | task 16
T | 0 | task 17
T | 0 | task 18
T | 0 | task 19
T | 0 | task 20
T | 0 | task 21
T | 0 | task 22
T | 0 | task 23
T | 0 | task 24
T | 0 | task 25
T | 0 | task 26
T | 0 | task 27
T | 0 | task 28
T | 0 | task 29
T | 0 | task 30
T | 0 | task 31
T | 0 | task 32
T | 0 | task 33
T | 0 | task 34
T | 0 | task 35
T | 0 | task 36
T | 0 | task 37
T | 0 | task 38
T | 0 | task 39
T | 0 | task 40
T | 0 | task 41
T | 0 | task 42
T | 0 | task 43
T | 0 | task 44
T | 0 | task 45
T | 0 | task 46
T | 0 | task 47
T | 0 | task 48
T | 0 | task 49
T | 0 | task 50
T | 0 | task 51
T | 0 | task 52
T | 0 | task 53
T | 0 | task 54
T | 0 | task 55
T | 0 | task 56
T | 0 | task 57
T | 0 | task 58
T | 0 | task 59
T | 0 | task 60
T | 0 | task 61
T | 0 | task 62
T | 0 | task 63
T | 0 | task 64
T | 0 | task 65
T | 0 | task 66
T | 0 | task 67
T | 0 | task 68
T | 0 | task 69
T | 0 | task 70
T | 0 | task 71
T | 0 | task 72
T | 0 | task 73
T | 0 | task 74
T | 0 | task 75
T | 0 | task 76
T | 0 | task 77
T | 0 | task 78
T | 0 | task 79
T | 0 | task 80
T | 0 | task 81
T | 0 | task 82
T | 0 | task 83
T | 0 | task 84
T | 0 | task 85
T | 0 | task 86
T | 0 | task 87
T | 0 | task 88
T | 0 | task 89
T | 0 | task 90
T | 0 | task 91
T | 0 | task 92
T | 0 | task 93
T | 0 | task 94
T | 0 | task 95
T | 0 | task 96
T | 0 | task 97
T | 0 | task 98
T | 0 | task 99
T | 0 | task 100
```
**Inputs:**
```text
todo task 101
delete 100
todo replacement task
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Pip?! I can keep at most 100 tasks in your list.

${LINE}

${LINE}

Removed this task. You have 99 tasks left:
	[T][ ] task 100

${LINE}

${LINE}

Pip! Added this task: replacement task

${LINE}

${LINE}

${BYE}
```

### TC-16: Strict date-time validation for deadlines (leap year, midnight, noon, and display formatting)
**Aim:** Confirm that valid date-time formats (including leap day, midnight, noon, and standard formats) are accepted and displayed cleanly.
**Inputs:**
```text
deadline finish homework /by 2/12/2019 0000
deadline noon meeting /by 2/12/2019 1200
deadline leap day sprint /by 29/02/2020 0900
deadline standard date /by 2019-10-15
list
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Pip! Added this task: finish homework

${LINE}

${LINE}

Pip! Added this task: noon meeting

${LINE}

${LINE}

Pip! Added this task: leap day sprint

${LINE}

${LINE}

Pip! Added this task: standard date

${LINE}

${LINE}

Here's your task list:
1.[D][ ] finish homework (by: 02 Dec 2019 00:00)
2.[D][ ] noon meeting (by: 02 Dec 2019 12:00)
3.[D][ ] leap day sprint (by: 29 Feb 2020 09:00)
4.[D][ ] standard date (by: 15 Oct 2019)
${LINE}

${LINE}

${BYE}
```

### TC-17: Rejection of impossible calendar dates and out-of-range time values
**Aim:** Confirm that invalid leap days, impossible days, invalid months, hours, and minutes in numeric date-time input are rejected.
**Inputs:**
```text
deadline bad leap /by 29/02/2019 0900
deadline feb 31 /by 31/02/2019 1800
deadline month 13 /by 2019-13-02 1800
deadline day 32 /by 32/01/2019 1800
deadline hour 25 /by 2/12/2019 2500
deadline minute 60 /by 2/12/2019 1860
deadline feb 30 /by 2019-02-30
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Pip?! I couldn't understand that date or time. Use: yyyy-MM-dd, yyyy/MM/dd, d/M/yyyy, dd-MM-yyyy, or d MMM yyyy (optional HHmm time; e.g., 2026-10-12 or 12 Oct 2026 1800)

${LINE}

${LINE}

Pip?! I couldn't understand that date or time. Use: yyyy-MM-dd, yyyy/MM/dd, d/M/yyyy, dd-MM-yyyy, or d MMM yyyy (optional HHmm time; e.g., 2026-10-12 or 12 Oct 2026 1800)

${LINE}

${LINE}

Pip?! I couldn't understand that date or time. Use: yyyy-MM-dd, yyyy/MM/dd, d/M/yyyy, dd-MM-yyyy, or d MMM yyyy (optional HHmm time; e.g., 2026-10-12 or 12 Oct 2026 1800)

${LINE}

${LINE}

Pip?! I couldn't understand that date or time. Use: yyyy-MM-dd, yyyy/MM/dd, d/M/yyyy, dd-MM-yyyy, or d MMM yyyy (optional HHmm time; e.g., 2026-10-12 or 12 Oct 2026 1800)

${LINE}

${LINE}

Pip?! I couldn't understand that date or time. Use: yyyy-MM-dd, yyyy/MM/dd, d/M/yyyy, dd-MM-yyyy, or d MMM yyyy (optional HHmm time; e.g., 2026-10-12 or 12 Oct 2026 1800)

${LINE}

${LINE}

Pip?! I couldn't understand that date or time. Use: yyyy-MM-dd, yyyy/MM/dd, d/M/yyyy, dd-MM-yyyy, or d MMM yyyy (optional HHmm time; e.g., 2026-10-12 or 12 Oct 2026 1800)

${LINE}

${LINE}

Pip?! I couldn't understand that date or time. Use: yyyy-MM-dd, yyyy/MM/dd, d/M/yyyy, dd-MM-yyyy, or d MMM yyyy (optional HHmm time; e.g., 2026-10-12 or 12 Oct 2026 1800)

${LINE}

${LINE}

${BYE}
```

### TC-18: Load deadline dates and skip corrupted dates
**Aim:** Confirm that stored deadlines with valid dates are loaded and displayed correctly, while malformed dates are skipped with warnings and lead to the storage-problem goodbye.
**File input:**
```text
D | 0 | project submission | 2019-12-02 1800
D | 0 | corrupted deadline | 2019-13-02 1800
D | 1 | day only deadline | 2019-10-15
```
**Inputs:**
```text
list
bye
```
**Expected output:**
```text
Pip?! I skipped a damaged saved task: [D | 0 | corrupted deadline | 2019-13-02 1800] - I couldn't understand that date or time. Use: yyyy-MM-dd, yyyy/MM/dd, d/M/yyyy, dd-MM-yyyy, or d MMM yyyy (optional HHmm time; e.g., 2026-10-12 or 12 Oct 2026 1800)
${INTRO}

${LINE}

Here's your task list:
1.[D][ ] project submission (by: 02 Dec 2019 18:00)
2.[D][X] day only deadline (by: 15 Oct 2019)
${LINE}

${LINE}

${STORAGE_BYE}
```

### TC-19: View command for date filtering
**Aim:** Confirm that view filters tasks occurring on a specific date, informs the user when a time is supplied, handles dates with no tasks, rejects blank arguments, and validates date formats.
**Inputs:**
```text
deadline assignment /by 2019-12-02 1800
deadline project /by 2/12/2019 2359
deadline other day /by 2019-12-03
event conference /from 2019-12-01 /to 2019-12-03
view 2019-12-02
view 2019-12-02 1800
view 2019-12-04
view
view invalid-date
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Pip! Added this task: assignment

${LINE}

${LINE}

Pip! Added this task: project

${LINE}

${LINE}

Pip! Added this task: other day

${LINE}

${LINE}

Pip! Added this task: conference

${LINE}

${LINE}

Here's your plan for 02 Dec 2019:
1.[D][ ] assignment (by: 02 Dec 2019 18:00)
2.[D][ ] project (by: 02 Dec 2019 23:59)
3.[E][ ] conference (from: 01 Dec 2019 to: 03 Dec 2019)
${LINE}

${LINE}

I'll check the entire day (02 Dec 2019).

Here's your plan for 02 Dec 2019:
1.[D][ ] assignment (by: 02 Dec 2019 18:00)
2.[D][ ] project (by: 02 Dec 2019 23:59)
3.[E][ ] conference (from: 01 Dec 2019 to: 03 Dec 2019)
${LINE}

${LINE}

I couldn't find any tasks on 04 Dec 2019.

${LINE}

${LINE}

Pip?! I need a date. Use: view [date]

${LINE}

${LINE}

Pip?! I couldn't understand that date or time. Use: yyyy-MM-dd, yyyy/MM/dd, d/M/yyyy, dd-MM-yyyy, or d MMM yyyy (optional HHmm time; e.g., 2026-10-12 or 12 Oct 2026 1800)

${LINE}

${LINE}

${BYE}
```

### TC-20: Event date validation, formatting, and impossible dates rejection
**Aim:** Confirm that events format valid dates and times consistently, accept freeform descriptions, and reject impossible dates or invalid time components without creating tasks.
**Inputs:**
```text
event retreat /from 2026-10-01 /to 2026-10-05
event final exam /from 2/12/2019 0900 /to 2/12/2019 1100
event orientation /from Monday /to Wednesday
event bad start /from 2019-13-01 /to 2019-10-05
event feb 30 /from 2019-02-30 /to 2019-03-01
event bad leap /from 29/02/2019 0900 /to 01/03/2019 0900
event bad hour /from 2/12/2019 2500 /to 2/12/2019 2600
event bad minute /from 2/12/2019 0960 /to 2/12/2019 1100
list
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Pip! Added this task: retreat

${LINE}

${LINE}

Pip! Added this task: final exam

${LINE}

${LINE}

Pip! Added this task: orientation

${LINE}

${LINE}

Pip?! I couldn't understand that date or time. Use: yyyy-MM-dd, yyyy/MM/dd, d/M/yyyy, dd-MM-yyyy, or d MMM yyyy (optional HHmm time; e.g., 2026-10-12 or 12 Oct 2026 1800)

${LINE}

${LINE}

Pip?! I couldn't understand that date or time. Use: yyyy-MM-dd, yyyy/MM/dd, d/M/yyyy, dd-MM-yyyy, or d MMM yyyy (optional HHmm time; e.g., 2026-10-12 or 12 Oct 2026 1800)

${LINE}

${LINE}

Pip?! I couldn't understand that date or time. Use: yyyy-MM-dd, yyyy/MM/dd, d/M/yyyy, dd-MM-yyyy, or d MMM yyyy (optional HHmm time; e.g., 2026-10-12 or 12 Oct 2026 1800)

${LINE}

${LINE}

Pip?! I couldn't understand that date or time. Use: yyyy-MM-dd, yyyy/MM/dd, d/M/yyyy, dd-MM-yyyy, or d MMM yyyy (optional HHmm time; e.g., 2026-10-12 or 12 Oct 2026 1800)

${LINE}

${LINE}

Pip?! I couldn't understand that date or time. Use: yyyy-MM-dd, yyyy/MM/dd, d/M/yyyy, dd-MM-yyyy, or d MMM yyyy (optional HHmm time; e.g., 2026-10-12 or 12 Oct 2026 1800)

${LINE}

${LINE}

Here's your task list:
1.[E][ ] retreat (from: 01 Oct 2026 to: 05 Oct 2026)
2.[E][ ] final exam (from: 02 Dec 2019 09:00 to: 02 Dec 2019 11:00)
3.[E][ ] orientation (from: Monday to: Wednesday)
${LINE}

${LINE}

${BYE}
```

### TC-21: Multi-case corrupted storage recovery and valid task preservation
**Aim:** Verify that storage loading skips various corrupted task lines with descriptive warnings, successfully loads valid tasks across types, supports further mutations, and still uses the storage-problem goodbye after successful saves.
**File input:**
```text
T | 1
D | 0 | submit paper
E | 0 | conference | 2026-10-01
E | 0 | hackathon | 2019-13-02 | 2019-13-05
Z | 0 | alien task
T | 0 | read book
D | 1 | submit assignment | 2026-10-02 1400
E | 0 | workshop | 2026-10-01 | 2026-10-03
```
**Inputs:**
```text
list
mark 1
delete 3
list
bye
```
**Expected output:**
```text
Pip?! I skipped a damaged saved task: [T | 1] - Missing essential task components.
Pip?! I skipped a damaged saved task: [D | 0 | submit paper] - Deadline is missing the due date.
Pip?! I skipped a damaged saved task: [E | 0 | conference | 2026-10-01] - Event is missing start or end dates.
Pip?! I skipped a damaged saved task: [E | 0 | hackathon | 2019-13-02 | 2019-13-05] - I couldn't understand that date or time. Use: yyyy-MM-dd, yyyy/MM/dd, d/M/yyyy, dd-MM-yyyy, or d MMM yyyy (optional HHmm time; e.g., 2026-10-12 or 12 Oct 2026 1800)
Pip?! I skipped a damaged saved task: [Z | 0 | alien task] - Unknown task type identifier: Z
${INTRO}

${LINE}

Here's your task list:
1.[T][ ] read book
2.[D][X] submit assignment (by: 02 Oct 2026 14:00)
3.[E][ ] workshop (from: 01 Oct 2026 to: 03 Oct 2026)
${LINE}

${LINE}

Nice! Marked this task as done:
	[T][X] read book

${LINE}

${LINE}

Removed this task. You have 2 tasks left:
	[E][ ] workshop (from: 01 Oct 2026 to: 03 Oct 2026)

${LINE}

${LINE}

Here's your task list:
1.[T][X] read book
2.[D][X] submit assignment (by: 02 Oct 2026 14:00)
${LINE}

${LINE}

${STORAGE_BYE}
```

### TC-22: Task list deletion boundaries across positions and sequential re-indexing
**Aim:** Verify that deleting tasks from the middle, front, and end of the list properly re-indexes remaining tasks, and confirm that out-of-range and non-integer deletion indices are rejected.
**Inputs:**
```text
todo first task
todo second task
todo third task
todo fourth task
delete 2
list
delete 1
list
delete 2
list
delete 0
delete -2
delete 2
delete abc
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Pip! Added this task: first task

${LINE}

${LINE}

Pip! Added this task: second task

${LINE}

${LINE}

Pip! Added this task: third task

${LINE}

${LINE}

Pip! Added this task: fourth task

${LINE}

${LINE}

Removed this task. You have 3 tasks left:
	[T][ ] second task

${LINE}

${LINE}

Here's your task list:
1.[T][ ] first task
2.[T][ ] third task
3.[T][ ] fourth task
${LINE}

${LINE}

Removed this task. You have 2 tasks left:
	[T][ ] first task

${LINE}

${LINE}

Here's your task list:
1.[T][ ] third task
2.[T][ ] fourth task
${LINE}

${LINE}

Removed this task. You have 1 task left:
	[T][ ] fourth task

${LINE}

${LINE}

Here's your task list:
1.[T][ ] third task
${LINE}

${LINE}

Pip?! I need a positive whole number for the task number. Use: delete [task number]

${LINE}

${LINE}

Pip?! I need a positive whole number for the task number. Use: delete [task number]

${LINE}

${LINE}

Pip?! I couldn't find that task number. Use: delete [task number]

${LINE}

${LINE}

Pip?! I need a positive whole number for the task number. Use: delete [task number]

${LINE}

${LINE}

${BYE}
```

### TC-23: Multi-day event queries with view on populated and empty lists
**Aim:** Verify that date queries correctly detect events on start, middle, and end days, handle empty lists, and display the informational note when time is passed.
**Inputs:**
```text
view 2026-10-02
todo untimed task
deadline urgent /by 2026-10-02 1800
event conference /from 2026-10-01 /to 2026-10-03
deadline freeform /by tonight
view 2026-10-01
view 2026-10-02
view 2026-10-03
view 2026-10-04
view 2026-10-02 0900
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

I couldn't find any tasks on 02 Oct 2026.

${LINE}

${LINE}

Pip! Added this task: untimed task

${LINE}

${LINE}

Pip! Added this task: urgent

${LINE}

${LINE}

Pip! Added this task: conference

${LINE}

${LINE}

Pip! Added this task: freeform

${LINE}

${LINE}

Here's your plan for 01 Oct 2026:
1.[E][ ] conference (from: 01 Oct 2026 to: 03 Oct 2026)
${LINE}

${LINE}

Here's your plan for 02 Oct 2026:
1.[D][ ] urgent (by: 02 Oct 2026 18:00)
2.[E][ ] conference (from: 01 Oct 2026 to: 03 Oct 2026)
${LINE}

${LINE}

Here's your plan for 03 Oct 2026:
1.[E][ ] conference (from: 01 Oct 2026 to: 03 Oct 2026)
${LINE}

${LINE}

I couldn't find any tasks on 04 Oct 2026.

${LINE}

${LINE}

I'll check the entire day (02 Oct 2026).

Here's your plan for 02 Oct 2026:
1.[D][ ] urgent (by: 02 Oct 2026 18:00)
2.[E][ ] conference (from: 01 Oct 2026 to: 03 Oct 2026)
${LINE}

${LINE}

${BYE}
```


### TC-24: Find command for searching tasks by keyword
**Aim:** Verify that find searches task descriptions by case-insensitive keyword, handles single and multiple matches across task types, notifies when no matches are found, and explains missing search arguments.
**Inputs:**
```text
find book
todo read book
deadline return book /by 2019-12-02 1800
event book club meeting /from 2019-12-01 /to 2019-12-03
todo buy groceries
find book
find BOOK
find club
find non-existent
find
find    
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

I couldn't find any tasks matching 'book'.

${LINE}

${LINE}

Pip! Added this task: read book

${LINE}

${LINE}

Pip! Added this task: return book

${LINE}

${LINE}

Pip! Added this task: book club meeting

${LINE}

${LINE}

Pip! Added this task: buy groceries

${LINE}

${LINE}

I found these tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: 02 Dec 2019 18:00)
3.[E][ ] book club meeting (from: 01 Dec 2019 to: 03 Dec 2019)
${LINE}

${LINE}

I found these tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: 02 Dec 2019 18:00)
3.[E][ ] book club meeting (from: 01 Dec 2019 to: 03 Dec 2019)
${LINE}

${LINE}

I found these tasks in your list:
1.[E][ ] book club meeting (from: 01 Dec 2019 to: 03 Dec 2019)
${LINE}

${LINE}

I couldn't find any tasks matching 'non-existent'.

${LINE}

${LINE}

Pip?! I need a search keyword. Use: find [keyword]

${LINE}

${LINE}

Pip?! I need a search keyword. Use: find [keyword]

${LINE}

${LINE}

${BYE}
```

### TC-25: Load versioned tasks with escaped text
**Aim:** Confirm that saved todos, deadlines, and events preserve pipes and backslashes in their descriptions.
**File input:**
```text
V2 | T | 0 | read \| review
V2 | D | 1 | submit A\\B \| final | 2019-12-02 1800
V2 | E | 0 | plan \| demo | 2019-12-01 | 2019-12-03
```
**Inputs:**
```text
list
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Here's your task list:
1.[T][ ] read | review
2.[D][X] submit A\B | final (by: 02 Dec 2019 18:00)
3.[E][ ] plan | demo (from: 01 Dec 2019 to: 03 Dec 2019)
${LINE}

${LINE}

${BYE}
```

### TC-26: Recover from invalid input and preserve literal task text
**Aim:** Confirm that validation errors allow recovery, task text containing Piplup wording and storage separators stays literal, no-match searches are normal responses, and this session uses the normal goodbye.
**Inputs:**
```text
todo
todo Pip?! review A\B | notes
mark 2
mark 1
list
find missing
find pip?!
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Pip?! I need a description for your todo. Use: todo [description]

${LINE}

${LINE}

Pip! Added this task: Pip?! review A\B | notes

${LINE}

${LINE}

Pip?! I couldn't find that task number. Use: mark [task number]

${LINE}

${LINE}

Nice! Marked this task as done:
	[T][X] Pip?! review A\B | notes

${LINE}

${LINE}

Here's your task list:
1.[T][X] Pip?! review A\B | notes
${LINE}

${LINE}

I couldn't find any tasks matching 'missing'.

${LINE}

${LINE}

I found these tasks in your list:
1.[T][X] Pip?! review A\B | notes
${LINE}

${LINE}

${BYE}
```

### TC-27: Recover when every saved record is damaged
**Aim:** Confirm that all damaged rows are reported, the resulting empty list remains usable, and a subsequent successful save still leads to the storage-problem goodbye.
**File input:**
```text
damaged
D | 0 | missing date
```
**Inputs:**
```text
list
find book
todo new task
list
bye
```
**Expected output:**
```text
Pip?! I skipped a damaged saved task: [damaged] - Missing essential task components.
Pip?! I skipped a damaged saved task: [D | 0 | missing date] - Deadline is missing the due date.
${INTRO}

${LINE}

Your list is empty. I was starting to get bored.
${LINE}

${LINE}

I couldn't find any tasks matching 'book'.

${LINE}

${LINE}

Pip! Added this task: new task

${LINE}

${LINE}

Here's your task list:
1.[T][ ] new task
${LINE}

${LINE}

${STORAGE_BYE}
```

### TC-28: Skip malformed versioned records while preserving valid neighbors
**Aim:** Confirm that invalid status, unknown and incomplete escapes, surplus fields, and missing fields each produce a warning, while valid records before and after the damaged rows remain intact.
**File input:**
```text
V2 | T | 0 | first task
V2 | T | 2 | invalid status
V2 | T | 0 | bad\q
V2 | T | 0 | unfinished\
V2 | T | 0 | extra | field
V2 | D | 0 | missing date
V2 | T | 1 | last \| task
```
**Inputs:**
```text
list
bye
```
**Expected output:**
```text
Pip?! I skipped a damaged saved task: [V2 | T | 2 | invalid status] - Invalid task status.
Pip?! I skipped a damaged saved task: [V2 | T | 0 | bad\q] - Unknown escape sequence: \q
Pip?! I skipped a damaged saved task: [V2 | T | 0 | unfinished\] - Incomplete escape sequence.
Pip?! I skipped a damaged saved task: [V2 | T | 0 | extra | field] - Expected 4 fields, found 5.
Pip?! I skipped a damaged saved task: [V2 | D | 0 | missing date] - Expected 5 fields, found 4.
${INTRO}

${LINE}

Here's your task list:
1.[T][ ] first task
2.[T][X] last | task
${LINE}

${LINE}

${STORAGE_BYE}
```

### TC-29: Show the command guide on demand
**Aim:** Confirm that `help` repeats the complete startup command table without the banner, preserves the current task list, and rejects extra arguments.
**Inputs:**
```text
help
todo review notes
help extra
help
list
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

${GUIDE}
${LINE}

${LINE}

Pip! Added this task: review notes

${LINE}

${LINE}

Pip?! I don't need arguments for help. Use: help

${LINE}

${LINE}

${GUIDE}
${LINE}

${LINE}

Here's your task list:
1.[T][ ] review notes
${LINE}

${LINE}

${BYE}
```

### TC-30: End the session when input closes
**Aim:** Confirm that EOF after a command exits cleanly without an exception or an unsolicited goodbye. This session intentionally omits `bye`.
**Inputs:**
```text
list
```
**Expected output:**
```text
${INTRO}

${LINE}

Your list is empty. I was starting to get bored.
${LINE}
```

### TC-31: Find uppercase I using either letter case
**Aim:** Confirm that both lowercase and uppercase keywords find the same task. The focused code quality check also exercises this session with a Turkish default locale.
**Inputs:**
```text
todo FILE report
find file
find FILE
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Pip! Added this task: FILE report

${LINE}

${LINE}

I found these tasks in your list:
1.[T][ ] FILE report
${LINE}

${LINE}

I found these tasks in your list:
1.[T][ ] FILE report
${LINE}

${LINE}

${BYE}
```

### TC-32: Accept all calendar formats in deadlines and view
**Aim:** Verify each supported format parses in a deadline and in a view query, optional times display correctly, and named-month event dates span the queried day.
**Inputs:**
```text
deadline iso /by 2026-10-12
deadline year slash /by 2026/10/12 1800
deadline day slash /by 12/10/2026
deadline day hyphen /by 12-10-2026
deadline named month /by 12 Oct 2026 0900
event conference /from 11 Oct 2026 /to 13 Oct 2026
view 2026-10-12
view 2026/10/12
view 12/10/2026
view 12-10-2026
view 12 oct 2026
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Pip! Added this task: iso

${LINE}

${LINE}

Pip! Added this task: year slash

${LINE}

${LINE}

Pip! Added this task: day slash

${LINE}

${LINE}

Pip! Added this task: day hyphen

${LINE}

${LINE}

Pip! Added this task: named month

${LINE}

${LINE}

Pip! Added this task: conference

${LINE}

${LINE}

Here's your plan for 12 Oct 2026:
1.[D][ ] iso (by: 12 Oct 2026)
2.[D][ ] year slash (by: 12 Oct 2026 18:00)
3.[D][ ] day slash (by: 12 Oct 2026)
4.[D][ ] day hyphen (by: 12 Oct 2026)
5.[D][ ] named month (by: 12 Oct 2026 09:00)
6.[E][ ] conference (from: 11 Oct 2026 to: 13 Oct 2026)
${LINE}

${LINE}

Here's your plan for 12 Oct 2026:
1.[D][ ] iso (by: 12 Oct 2026)
2.[D][ ] year slash (by: 12 Oct 2026 18:00)
3.[D][ ] day slash (by: 12 Oct 2026)
4.[D][ ] day hyphen (by: 12 Oct 2026)
5.[D][ ] named month (by: 12 Oct 2026 09:00)
6.[E][ ] conference (from: 11 Oct 2026 to: 13 Oct 2026)
${LINE}

${LINE}

Here's your plan for 12 Oct 2026:
1.[D][ ] iso (by: 12 Oct 2026)
2.[D][ ] year slash (by: 12 Oct 2026 18:00)
3.[D][ ] day slash (by: 12 Oct 2026)
4.[D][ ] day hyphen (by: 12 Oct 2026)
5.[D][ ] named month (by: 12 Oct 2026 09:00)
6.[E][ ] conference (from: 11 Oct 2026 to: 13 Oct 2026)
${LINE}

${LINE}

Here's your plan for 12 Oct 2026:
1.[D][ ] iso (by: 12 Oct 2026)
2.[D][ ] year slash (by: 12 Oct 2026 18:00)
3.[D][ ] day slash (by: 12 Oct 2026)
4.[D][ ] day hyphen (by: 12 Oct 2026)
5.[D][ ] named month (by: 12 Oct 2026 09:00)
6.[E][ ] conference (from: 11 Oct 2026 to: 13 Oct 2026)
${LINE}

${LINE}

Here's your plan for 12 Oct 2026:
1.[D][ ] iso (by: 12 Oct 2026)
2.[D][ ] year slash (by: 12 Oct 2026 18:00)
3.[D][ ] day slash (by: 12 Oct 2026)
4.[D][ ] day hyphen (by: 12 Oct 2026)
5.[D][ ] named month (by: 12 Oct 2026 09:00)
6.[E][ ] conference (from: 11 Oct 2026 to: 13 Oct 2026)
${LINE}

${LINE}

${BYE}
```

### TC-33: Reject invalid calendar dates without losing free-form text
**Aim:** Confirm malformed month-name and numeric dates fail validation, while a natural-language deadline remains valid.
**Inputs:**
```text
deadline impossible month /by 31 Feb 2026
deadline misspelled month /by 12 Fob 2026
deadline impossible slash /by 2026/02/30
deadline impossible hyphen /by 31-02-2026
deadline natural /by Friday
view 31 Feb 2026
list
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Pip?! I couldn't understand that date or time. Use: yyyy-MM-dd, yyyy/MM/dd, d/M/yyyy, dd-MM-yyyy, or d MMM yyyy (optional HHmm time; e.g., 2026-10-12 or 12 Oct 2026 1800)

${LINE}

${LINE}

Pip?! I couldn't understand that date or time. Use: yyyy-MM-dd, yyyy/MM/dd, d/M/yyyy, dd-MM-yyyy, or d MMM yyyy (optional HHmm time; e.g., 2026-10-12 or 12 Oct 2026 1800)

${LINE}

${LINE}

Pip?! I couldn't understand that date or time. Use: yyyy-MM-dd, yyyy/MM/dd, d/M/yyyy, dd-MM-yyyy, or d MMM yyyy (optional HHmm time; e.g., 2026-10-12 or 12 Oct 2026 1800)

${LINE}

${LINE}

Pip?! I couldn't understand that date or time. Use: yyyy-MM-dd, yyyy/MM/dd, d/M/yyyy, dd-MM-yyyy, or d MMM yyyy (optional HHmm time; e.g., 2026-10-12 or 12 Oct 2026 1800)

${LINE}

${LINE}

Pip! Added this task: natural

${LINE}

${LINE}

Pip?! I couldn't understand that date or time. Use: yyyy-MM-dd, yyyy/MM/dd, d/M/yyyy, dd-MM-yyyy, or d MMM yyyy (optional HHmm time; e.g., 2026-10-12 or 12 Oct 2026 1800)

${LINE}

${LINE}

Here's your task list:
1.[D][ ] natural (by: Friday)
${LINE}

${LINE}

${BYE}
```

### TC-34: Load saved dates in new formats
**Aim:** Confirm saved deadlines using named-month and year-first slash dates load and can be found by view.
**File input:**
```text
D | 0 | saved month | 12 Oct 2026
D | 1 | saved slash | 2026/10/12 1800
```
**Inputs:**
```text
view 2026-10-12
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Here's your plan for 12 Oct 2026:
1.[D][ ] saved month (by: 12 Oct 2026)
2.[D][X] saved slash (by: 12 Oct 2026 18:00)
${LINE}

${LINE}

${BYE}
```

## Focused storage failure verification

The companion Java checks exercise failures that cannot be represented by a
plain saved-file fixture in the console runner:

- Reject the first file replacement, verify the attempted task is rolled back,
  then save another task successfully and still show the storage-problem goodbye.
- Attempt to load a directory as the saved file, report the load error, and use
  the storage-problem goodbye without claiming that everything is saved.
- Verify failed replacement leaves the original file intact, failed saves roll
  back add/mark/unmark/delete, and escaped fields survive a save/load round trip.

Run this after the full console plan with Java 25:

```powershell
$javaSources = @(Get-ChildItem src/main/java -Recurse -Filter *.java | Select-Object -ExpandProperty FullName)
javac -d out/test $javaSources test/dawn/PiplupStorageTest.java test/StorageRegressionTest.java
java -cp out/test dawn.PiplupStorageTest
java -cp out/test StorageRegressionTest
```

## Focused code quality verification

`test/dawn/CodeQualityRegressionTest.java` complements the console cases with:

- EOF on an entirely empty input stream and after saving a task.
- The session `todo FILE report`, `find file`, `find FILE`, `bye` under a Turkish
  default locale. Both searches must show `1.[T][ ] FILE report` as a match.
- Loading 100 valid saved tasks followed by an invalid-status record and a valid
  101st task. The loader must retain the first 100 tasks, report both warnings,
  and leave the saved file unchanged.

Run with Java 25:

```powershell
$javaSources = @(Get-ChildItem src/main/java -Recurse -Filter *.java | Select-Object -ExpandProperty FullName)
javac -d out/test $javaSources test/dawn/CodeQualityRegressionTest.java
java -cp out/test dawn.CodeQualityRegressionTest
```

## Performance comparison

Compile the original and updated sources into separate directories using Java 25.
Then run `python test/compare-performance.py BEFORE_CLASSPATH AFTER_CLASSPATH`.
The script runs a 423-command session up to the 100-task limit, covering every
task type, mark/unmark, searches, date queries, deletion, list, help, and exit.
It compares the complete output and saved file, and reports median elapsed time
over five measured runs per version after a warm-up pair. Each run uses isolated
temporary storage under `_temp`; results are written to
`_temp/performance-comparison.json`. Timing includes JVM startup and file I/O and
is a smoke check for a regression in this workload, not proof of performance for
every environment or input.
