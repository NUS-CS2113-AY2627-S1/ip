# UI Test Transcript

```text
java -version
openjdk version "25.0.4" 2026-07-21 LTS
OpenJDK Runtime Environment Temurin-25.0.4+7 (build 25.0.4+7-LTS)
OpenJDK 64-Bit Server VM Temurin-25.0.4+7 (build 25.0.4+7-LTS, mixed mode, sharing)
```

```text
javac -version
javac 25.0.4
```

Build: `javac -d out src/main/java/chillguy/Chillguy.java src/main/java/chillguy/exception/ChillguyException.java src/main/java/chillguy/parser/Command.java src/main/java/chillguy/parser/Parser.java src/main/java/chillguy/storage/Storage.java src/main/java/chillguy/task/Deadline.java src/main/java/chillguy/task/Event.java src/main/java/chillguy/task/Task.java src/main/java/chillguy/task/TaskList.java src/main/java/chillguy/task/Todo.java src/main/java/chillguy/ui/Ui.java`

Fat JAR build: `"C:\Users\zepht\Documents\CS2113 iP\ip\gradlew.bat" --console=plain shadowJar`

```text
> Task :compileJava UP-TO-DATE
> Task :processResources NO-SOURCE
> Task :classes UP-TO-DATE
> Task :shadowJar UP-TO-DATE

BUILD SUCCESSFUL in 1s
2 actionable tasks: 2 up-to-date
Consider enabling configuration cache to speed up this build: https://docs.gradle.org/9.7.1/userguide/configuration_cache_enabling.html
```

Exit code: 0

## Run the Fat JAR: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -jar "C:\Users\zepht\Documents\CS2113 iP\ip\build\libs\chillguy-all.jar"`

Working directory: isolated sandbox `0`, storage session `0`.

Input:

```text
todo read book
list
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: saved file contents
- PASS: no temporary save files remain

## Delete Task: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `1`, storage session `1`.

Input:

```text
todo read book
todo return book
delete 1
list
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] return book
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [T][ ] read book
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] return book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: no temporary save files remain

## Delete Mixed Task Types: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `2`, storage session `2`.

Input:

```text
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
todo join sports club
todo borrow book
mark 1
mark 2
mark 4
list
delete 3
list
unmark 3
delete 4
delete 2
list
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: June 6th)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] join sports club
Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] borrow book
Now you have 5 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read book
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] return book (by: June 6th)
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] join sports club
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
2.[D][X] return book (by: June 6th)
3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
4.[T][X] join sports club
5.[T][ ] borrow book
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
2.[D][X] return book (by: June 6th)
3.[T][X] join sports club
4.[T][ ] borrow book
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [T][ ] join sports club
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [T][ ] borrow book
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [D][X] return book (by: June 6th)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
2.[T][ ] join sports club
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: no temporary save files remain

## Add Level 4 Task Types: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `3`, storage session `3`.

Input:

```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
mark 1
list
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] borrow book
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] borrow book
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] borrow book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: no temporary save files remain

## Reject Malformed Deadline: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `4`, storage session `4`.

Input:

```text
deadline byebye /today 6pm
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Sorry, deadline tasks need this format: deadline DESCRIPTION /by DATE
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: no temporary save files remain

## Reject Malformed Event: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `5`, storage session `5`.

Input:

```text
event meeting /from Monday 2pm
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: no temporary save files remain

## Reject Unknown and Empty Commands: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `6`, storage session `6`.

Input:

```text

   	  
todo
todo   
blah
read a book
todos book
TODO book
list
  todo	read book  
list extra
bye now
mark	1
blah
  list  
unmark  1 
delete 1
list
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Please enter a command, such as todo DESCRIPTION or list.
____________________________________________________________
____________________________________________________________
Please enter a command, such as todo DESCRIPTION or list.
____________________________________________________________
____________________________________________________________
ERROR: Description of todo cannot be empty.
____________________________________________________________
____________________________________________________________
ERROR: Description of todo cannot be empty.
____________________________________________________________
____________________________________________________________
ERROR: Unknown command.
____________________________________________________________
____________________________________________________________
ERROR: Unknown command.
____________________________________________________________
____________________________________________________________
ERROR: Unknown command.
____________________________________________________________
____________________________________________________________
ERROR: Unknown command.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
The list command takes no extra input. Use: list
____________________________________________________________
____________________________________________________________
The bye command takes no extra input. Use: bye
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read book
____________________________________________________________
____________________________________________________________
ERROR: Unknown command.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [T][ ] read book
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
Bye. Hope to see you again soon!
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: no temporary save files remain

## Validate Task Numbers and Preserve State: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `7`, storage session `7`.

Input:

```text
mark
mark 1
unmark
unmark 1
delete
delete 1
todo first
todo second
mark 2
mark abc
mark 1.5
mark 1 2
mark 999999999999999999999
mark -2147483649
mark 0
mark -1
mark -2147483648
mark 3
mark 2147483647
list
unmark abc
unmark 1.5
unmark 1 2
unmark 999999999999999999999
unmark -2147483649
unmark 0
unmark -1
unmark -2147483648
unmark 3
unmark 2147483647
list
delete abc
delete 1.5
delete 1 2
delete 999999999999999999999
delete -2147483649
delete 0
delete -1
delete -2147483648
delete 3
delete 2147483647
list
unmark 2
delete 1
delete 2
mark 1
list
delete 1
delete 1
list
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Please include a task number. Use: mark INDEX
____________________________________________________________
____________________________________________________________
Your task list is empty. Add a task first with todo DESCRIPTION.
____________________________________________________________
____________________________________________________________
Please include a task number. Use: unmark INDEX
____________________________________________________________
____________________________________________________________
Your task list is empty. Add a task first with todo DESCRIPTION.
____________________________________________________________
____________________________________________________________
Please include a task number. Use: delete INDEX
____________________________________________________________
____________________________________________________________
Your task list is empty. Add a task first with todo DESCRIPTION.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] first
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] second
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] second
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: mark INDEX
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: mark INDEX
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: mark INDEX
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: mark INDEX
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: mark INDEX
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] first
2.[T][X] second
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: unmark INDEX
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: unmark INDEX
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: unmark INDEX
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: unmark INDEX
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: unmark INDEX
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] first
2.[T][X] second
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: delete INDEX
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: delete INDEX
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: delete INDEX
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: delete INDEX
____________________________________________________________
____________________________________________________________
Please enter one whole-number task index. Use: delete INDEX
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 2. Use list to see them.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] first
2.[T][X] second
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [T][ ] second
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [T][ ] first
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 1. Use list to see them.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] second
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] second
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [T][X] second
Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
Your task list is empty. Add a task first with todo DESCRIPTION.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: no temporary save files remain

## Validate Task Fields and Recover: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `8`, storage session `8`.

Input:

```text
todo keep
deadline
deadline   
deadline /by Sunday
deadline task
deadline task /by
deadline task /by   
deadline task /by Sunday /by Monday
deadline task /bySunday
list
deadline	return book	/by	Sunday  
mark 2
event
event   
event /from Monday /to Tuesday
event meeting
event meeting /from Monday
event meeting /to Tuesday
event meeting /from /to Tuesday
event meeting /from Monday /to
event meeting /from   /to Tuesday
event meeting /from Monday /to   
event meeting /to Tuesday /from Monday
event meeting /from Monday /from Tuesday /to Wednesday
event meeting /from Monday /to Tuesday /to Wednesday
event meeting /fromMonday /to Tuesday
list
event	meeting	/from	Mon 2pm	/to	4pm  
unmark 2
list
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] keep
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Sorry, deadline tasks need this format: deadline DESCRIPTION /by DATE
____________________________________________________________
____________________________________________________________
Sorry, deadline tasks need this format: deadline DESCRIPTION /by DATE
____________________________________________________________
____________________________________________________________
Sorry, deadline tasks need this format: deadline DESCRIPTION /by DATE
____________________________________________________________
____________________________________________________________
Sorry, deadline tasks need this format: deadline DESCRIPTION /by DATE
____________________________________________________________
____________________________________________________________
Sorry, deadline tasks need this format: deadline DESCRIPTION /by DATE
____________________________________________________________
____________________________________________________________
Sorry, deadline tasks need this format: deadline DESCRIPTION /by DATE
____________________________________________________________
____________________________________________________________
Sorry, deadline tasks need this format: deadline DESCRIPTION /by DATE
____________________________________________________________
____________________________________________________________
Sorry, deadline tasks need this format: deadline DESCRIPTION /by DATE
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] keep
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Sorry, event tasks need this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] keep
2.[D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [D][ ] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] keep
2.[D][ ] return book (by: Sunday)
3.[E][ ] meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: no temporary save files remain

## Grow Task List Beyond 100 Tasks: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `9`, storage session `9`.

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
todo extra todo
deadline extra deadline /by Sunday
event extra event /from Mon /to Tue
mark 100
mark 103
list
delete 50
unmark 102
deadline replacement /by Friday
list
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] task 1
Now you have 1 task in the list.
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
  [T][ ] extra todo
Now you have 101 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] extra deadline (by: Sunday)
Now you have 102 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] extra event (from: Mon to: Tue)
Now you have 103 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] task 100
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [E][X] extra event (from: Mon to: Tue)
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
100.[T][X] task 100
101.[T][ ] extra todo
102.[D][ ] extra deadline (by: Sunday)
103.[E][X] extra event (from: Mon to: Tue)
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [T][ ] task 50
Now you have 102 tasks in the list.
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [E][ ] extra event (from: Mon to: Tue)
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] replacement (by: Friday)
Now you have 103 tasks in the list.
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
50.[T][ ] task 51
51.[T][ ] task 52
52.[T][ ] task 53
53.[T][ ] task 54
54.[T][ ] task 55
55.[T][ ] task 56
56.[T][ ] task 57
57.[T][ ] task 58
58.[T][ ] task 59
59.[T][ ] task 60
60.[T][ ] task 61
61.[T][ ] task 62
62.[T][ ] task 63
63.[T][ ] task 64
64.[T][ ] task 65
65.[T][ ] task 66
66.[T][ ] task 67
67.[T][ ] task 68
68.[T][ ] task 69
69.[T][ ] task 70
70.[T][ ] task 71
71.[T][ ] task 72
72.[T][ ] task 73
73.[T][ ] task 74
74.[T][ ] task 75
75.[T][ ] task 76
76.[T][ ] task 77
77.[T][ ] task 78
78.[T][ ] task 79
79.[T][ ] task 80
80.[T][ ] task 81
81.[T][ ] task 82
82.[T][ ] task 83
83.[T][ ] task 84
84.[T][ ] task 85
85.[T][ ] task 86
86.[T][ ] task 87
87.[T][ ] task 88
88.[T][ ] task 89
89.[T][ ] task 90
90.[T][ ] task 91
91.[T][ ] task 92
92.[T][ ] task 93
93.[T][ ] task 94
94.[T][ ] task 95
95.[T][ ] task 96
96.[T][ ] task 97
97.[T][ ] task 98
98.[T][ ] task 99
99.[T][X] task 100
100.[T][ ] extra todo
101.[D][ ] extra deadline (by: Sunday)
102.[E][ ] extra event (from: Mon to: Tue)
103.[D][ ] replacement (by: Friday)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: no temporary save files remain

## End of Input After Error: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `10`, storage session `10`.

Input:

```text
todo
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
ERROR: Description of todo cannot be empty.
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: no temporary save files remain

## End of Input Without Commands: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `11`, storage session `11`.

Input:

```text
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: no temporary save files remain

## Save Every Task Type Immediately: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `12`, storage session `persistence`.

Input:

```text
todo read | book C:\notes\新书
deadline return book /by June | 6th
event planning /from Aug 6th 2pm /to 4pm \ UTC
mark 1
mark 2
mark 3
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read | book C:\notes\新书
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: June | 6th)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] planning (from: Aug 6th 2pm to: 4pm \ UTC)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read | book C:\notes\新书
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] return book (by: June | 6th)
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [E][X] planning (from: Aug 6th 2pm to: 4pm \ UTC)
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: saved data after command 1 (app still running)
- PASS: saved data after command 2 (app still running)
- PASS: saved data after command 3 (app still running)
- PASS: saved data after command 4 (app still running)
- PASS: saved data after command 5 (app still running)
- PASS: saved data after command 6 (app still running)
- PASS: saved file contents
- PASS: no temporary save files remain

## Reload Tasks and Unmark: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `13`, storage session `persistence`.

Input:

```text
list
unmark 1
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read | book C:\notes\新书
2.[D][X] return book (by: June | 6th)
3.[E][X] planning (from: Aug 6th 2pm to: 4pm \ UTC)
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [T][ ] read | book C:\notes\新书
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: saved data after command 2 (app still running)
- PASS: saved file contents
- PASS: no temporary save files remain

## Reload Unmarked Status and Mark: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `14`, storage session `persistence`.

Input:

```text
list
mark 1
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read | book C:\notes\新书
2.[D][X] return book (by: June | 6th)
3.[E][X] planning (from: Aug 6th 2pm to: 4pm \ UTC)
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read | book C:\notes\新书
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: saved data after command 2 (app still running)
- PASS: saved file contents
- PASS: no temporary save files remain

## Reload and Delete the Middle Task: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `15`, storage session `persistence`.

Input:

```text
list
delete 2
list
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read | book C:\notes\新书
2.[D][X] return book (by: June | 6th)
3.[E][X] planning (from: Aug 6th 2pm to: 4pm \ UTC)
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [D][X] return book (by: June | 6th)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read | book C:\notes\新书
2.[E][X] planning (from: Aug 6th 2pm to: 4pm \ UTC)
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: saved data after command 2 (app still running)
- PASS: saved file contents
- PASS: no temporary save files remain

## Reload Deletion and Save an Empty List: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `16`, storage session `persistence`.

Input:

```text
list
delete 2
delete 1
list
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read | book C:\notes\新书
2.[E][X] planning (from: Aug 6th 2pm to: 4pm \ UTC)
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [E][X] planning (from: Aug 6th 2pm to: 4pm \ UTC)
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [T][X] read | book C:\notes\新书
Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: saved data after command 2 (app still running)
- PASS: saved data after command 3 (app still running)
- PASS: saved file contents
- PASS: no temporary save files remain

## Reload an Empty Saved List: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `17`, storage session `persistence`.

Input:

```text
list
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: saved file contents
- PASS: storage bytes and paths unchanged
- PASS: no temporary save files remain

## Create a Missing File in an Existing Folder: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `18`, storage session `18`.

Input:

```text
todo first task
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] first task
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: saved data after command 1 (app still running)
- PASS: saved file contents
- PASS: no temporary save files remain

## First Run Without Changes: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `19`, storage session `19`.

Input:

```text
list
unknown
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
ERROR: Unknown command.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: storage bytes and paths unchanged
- PASS: no temporary save files remain

## Load Editor Formatting Without Rewriting: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `20`, storage session `20`.

Input:

```text
list
mark 1
unmark 2
unknown
delete 9
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
2.[D][ ] return book (by: June 6th)
3.[E][X] meeting (from: 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read book
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [D][ ] return book (by: June 6th)
____________________________________________________________
____________________________________________________________
ERROR: Unknown command.
____________________________________________________________
____________________________________________________________
Please choose a task number from 1 to 3. Use list to see them.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: saved file contents
- PASS: storage bytes and paths unchanged
- PASS: no temporary save files remain

## Reject Saved Unknown Task Type: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `21`, storage session `21`.

Input:

```text
todo must not overwrite
list
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, data/chillguy.txt has invalid task data on line 2. Fix the file and restart Chillguy.
Your saved file has not been changed.
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: saved file contents
- PASS: storage bytes and paths unchanged
- PASS: no temporary save files remain

## Reject Saved Invalid Completion Status: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `22`, storage session `22`.

Input:

```text
todo must not overwrite
list
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, data/chillguy.txt has invalid task data on line 2. Fix the file and restart Chillguy.
Your saved file has not been changed.
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: saved file contents
- PASS: storage bytes and paths unchanged
- PASS: no temporary save files remain

## Reject Saved Missing Description: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `23`, storage session `23`.

Input:

```text
todo must not overwrite
list
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, data/chillguy.txt has invalid task data on line 2. Fix the file and restart Chillguy.
Your saved file has not been changed.
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: saved file contents
- PASS: storage bytes and paths unchanged
- PASS: no temporary save files remain

## Reject Saved Missing Deadline Date: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `24`, storage session `24`.

Input:

```text
todo must not overwrite
list
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, data/chillguy.txt has invalid task data on line 2. Fix the file and restart Chillguy.
Your saved file has not been changed.
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: saved file contents
- PASS: storage bytes and paths unchanged
- PASS: no temporary save files remain

## Reject Saved Empty Deadline Date: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `25`, storage session `25`.

Input:

```text
todo must not overwrite
list
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, data/chillguy.txt has invalid task data on line 2. Fix the file and restart Chillguy.
Your saved file has not been changed.
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: saved file contents
- PASS: storage bytes and paths unchanged
- PASS: no temporary save files remain

## Reject Saved Missing Event End: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `26`, storage session `26`.

Input:

```text
todo must not overwrite
list
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, data/chillguy.txt has invalid task data on line 2. Fix the file and restart Chillguy.
Your saved file has not been changed.
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: saved file contents
- PASS: storage bytes and paths unchanged
- PASS: no temporary save files remain

## Reject Saved Empty Event Start: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `27`, storage session `27`.

Input:

```text
todo must not overwrite
list
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, data/chillguy.txt has invalid task data on line 2. Fix the file and restart Chillguy.
Your saved file has not been changed.
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: saved file contents
- PASS: storage bytes and paths unchanged
- PASS: no temporary save files remain

## Reject Saved Empty Event End: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `28`, storage session `28`.

Input:

```text
todo must not overwrite
list
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, data/chillguy.txt has invalid task data on line 2. Fix the file and restart Chillguy.
Your saved file has not been changed.
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: saved file contents
- PASS: storage bytes and paths unchanged
- PASS: no temporary save files remain

## Reject Saved Extra Fields: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `29`, storage session `29`.

Input:

```text
todo must not overwrite
list
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, data/chillguy.txt has invalid task data on line 2. Fix the file and restart Chillguy.
Your saved file has not been changed.
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: saved file contents
- PASS: storage bytes and paths unchanged
- PASS: no temporary save files remain

## Reject Saved Unknown Escape: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `30`, storage session `30`.

Input:

```text
todo must not overwrite
list
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, data/chillguy.txt has invalid task data on line 2. Fix the file and restart Chillguy.
Your saved file has not been changed.
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: saved file contents
- PASS: storage bytes and paths unchanged
- PASS: no temporary save files remain

## Reject Saved Trailing Backslash: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `31`, storage session `31`.

Input:

```text
todo must not overwrite
list
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, data/chillguy.txt has invalid task data on line 2. Fix the file and restart Chillguy.
Your saved file has not been changed.
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: saved file contents
- PASS: storage bytes and paths unchanged
- PASS: no temporary save files remain

## Reject Saved Truncated Record: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `32`, storage session `32`.

Input:

```text
todo must not overwrite
list
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, data/chillguy.txt has invalid task data on line 2. Fix the file and restart Chillguy.
Your saved file has not been changed.
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: saved file contents
- PASS: storage bytes and paths unchanged
- PASS: no temporary save files remain

## Handle Invalid UTF-8: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `33`, storage session `33`.

Input:

```text
todo must not overwrite
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, I couldn't load tasks from data/chillguy.txt. Check that the path is a readable UTF-8 file.
Your saved file has not been changed.
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: storage bytes and paths unchanged
- PASS: no temporary save files remain

## Handle Directory at the Data File Path: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `34`, storage session `34`.

Input:

```text
todo must not overwrite
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, I couldn't load tasks from data/chillguy.txt. Check that the path is a readable UTF-8 file.
Your saved file has not been changed.
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: storage bytes and paths unchanged
- PASS: no temporary save files remain

## Handle File at the Data Folder Path: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `35`, storage session `35`.

Input:

```text
todo must not overwrite
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, I couldn't load tasks from data/chillguy.txt. Check that the path is a readable UTF-8 file.
Your saved file has not been changed.
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: storage bytes and paths unchanged
- PASS: no temporary save files remain

## Recover from Folder Creation Failure: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `36`, storage session `36`.

Input:

```text
list
todo retry me
list
todo retry me
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Sorry, I couldn't save tasks to data/chillguy.txt. No changes were made. Check that the data folder is writable and the file is not in use.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] retry me
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: saved data after command 4 (app still running)
- PASS: saved file contents
- PASS: no temporary save files remain

## Undo Every Kind of Failed Task Change: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `37`, storage session `37`.

Input:

```text
list
todo rejected
deadline rejected /by Friday
event rejected /from 1 /to 2
mark 2
unmark 1
delete 2
list
mark 2
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] keep todo
2.[D][ ] keep deadline (by: Friday)
3.[E][ ] keep event (from: 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Sorry, I couldn't save tasks to data/chillguy.txt. No changes were made. Check that the data folder is writable and the file is not in use.
____________________________________________________________
____________________________________________________________
Sorry, I couldn't save tasks to data/chillguy.txt. No changes were made. Check that the data folder is writable and the file is not in use.
____________________________________________________________
____________________________________________________________
Sorry, I couldn't save tasks to data/chillguy.txt. No changes were made. Check that the data folder is writable and the file is not in use.
____________________________________________________________
____________________________________________________________
Sorry, I couldn't save tasks to data/chillguy.txt. No changes were made. Check that the data folder is writable and the file is not in use.
____________________________________________________________
____________________________________________________________
Sorry, I couldn't save tasks to data/chillguy.txt. No changes were made. Check that the data folder is writable and the file is not in use.
____________________________________________________________
____________________________________________________________
Sorry, I couldn't save tasks to data/chillguy.txt. No changes were made. Check that the data folder is writable and the file is not in use.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] keep todo
2.[D][ ] keep deadline (by: Friday)
3.[E][ ] keep event (from: 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] keep deadline (by: Friday)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: saved data after command 8 (app still running)
- PASS: saved data after command 9 (app still running)
- PASS: saved file contents
- PASS: no temporary save files remain

## Find Descriptions Across Task Types: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -jar "C:\Users\zepht\Documents\CS2113 iP\ip\build\libs\chillguy-all.jar"`

Working directory: isolated sandbox `38`, storage session `38`.

Input:

```text
find book
  find   read book  
find Book
find June
find Monday
find [X]
find .*
list
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the matching tasks in your list:
1.[T][X] read book
2.[D][X] return book (by: June 6th)
3.[E][ ] book club (from: Monday to: Tuesday)
4.[T][ ] notebook
____________________________________________________________
____________________________________________________________
Here are the matching tasks in your list:
1.[T][X] read book
____________________________________________________________
____________________________________________________________
Here are the matching tasks in your list:
1.[T][ ] Book review
____________________________________________________________
____________________________________________________________
No matching tasks found.
____________________________________________________________
____________________________________________________________
No matching tasks found.
____________________________________________________________
____________________________________________________________
No matching tasks found.
____________________________________________________________
____________________________________________________________
No matching tasks found.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] buy milk
2.[T][X] read book
3.[D][X] return book (by: June 6th)
4.[E][ ] book club (from: Monday to: Tuesday)
5.[T][ ] Book review
6.[T][ ] notebook
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: storage bytes and paths unchanged
- PASS: no temporary save files remain

## Find Empty List and Reject Missing Keyword: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `39`, storage session `39`.

Input:

```text
find book
find
find   
find book
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
No matching tasks found.
____________________________________________________________
____________________________________________________________
Please include a search keyword. Use: find KEYWORD
____________________________________________________________
____________________________________________________________
Please include a search keyword. Use: find KEYWORD
____________________________________________________________
____________________________________________________________
No matching tasks found.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: storage bytes and paths unchanged
- PASS: no temporary save files remain

## Find Reflects Task Changes: PASS

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `40`, storage session `40`.

Input:

```text
todo read book
find book
mark 1
find book
unmark 1
find book
delete 1
find book
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Here are the matching tasks in your list:
1.[T][ ] read book
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read book
____________________________________________________________
____________________________________________________________
Here are the matching tasks in your list:
1.[T][X] read book
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [T][ ] read book
____________________________________________________________
____________________________________________________________
Here are the matching tasks in your list:
1.[T][ ] read book
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [T][ ] read book
Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
No matching tasks found.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: saved file contents
- PASS: no temporary save files remain
