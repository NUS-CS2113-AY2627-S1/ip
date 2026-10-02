# Dog UI test plan

Run the `test-ui` skill after each code update. Each case below specifies its aim,
inputs and exact expected output. The JSON block is executable test data, not a
record copied from a test run. Edit expectations only for an intended behavior
change. No third-party Python packages are required; use Python 3 and JDK 25.

Commands within a case share memory. Each case starts a fresh process and ends
with an explicit exit step. Expected text excludes the next `> ` prompt, which the
runner checks separately. Preserve all other spaces and line breaks. Dates stay
as strings. The existing `1 tasks` wording is recorded unchanged, not endorsed as
ideal grammar. Generated transcripts belong in ignored `_temp/`, not in commits.

```json
{
  "startup": " ____              \n|  _ \\  ___   __ _ \n| | | |/ _ \\ / _` |\n| |_| | (_) | (_| |\n|____/ \\___/ \\__, |\n             |___/ \n\nWoof! What can I do for you today?\n",
  "cases": [
    {
      "name": "Find tasks by description",
      "aim": "Check empty lists, missing keywords, case-insensitive substrings and phrases, all task types, description-only matching, result numbering, and unchanged list state.",
      "steps": [
        {
          "input": "find book",
          "expected": "____________________________________________________________\nHere are the matching tasks in your list:\nNo matching tasks found.\n____________________________________________________________\n"
        },
        {
          "input": "find   ",
          "expected": "____________________________________________________________\nPlease provide a keyword, for example: find book\n____________________________________________________________\n"
        },
        {
          "input": "todo walk dog",
          "expected": "____________________________________________________________\nWoof! I've added this task:\n  [T][ ] walk dog\nNow you have 1 tasks in the list.\n____________________________________________________________\n"
        },
        {
          "input": "todo read Book",
          "expected": "____________________________________________________________\nWoof! I've added this task:\n  [T][ ] read Book\nNow you have 2 tasks in the list.\n____________________________________________________________\n"
        },
        {
          "input": "deadline return book /by Sunday",
          "expected": "____________________________________________________________\nWoof! I've added this task:\n  [D][ ] return book (by: Sunday)\nNow you have 3 tasks in the list.\n____________________________________________________________\n"
        },
        {
          "input": "event book club /from Monday /to Tuesday",
          "expected": "____________________________________________________________\nWoof! I've added this task:\n  [E][ ] book club (from: Monday to: Tuesday)\nNow you have 4 tasks in the list.\n____________________________________________________________\n"
        },
        {
          "input": "mark 2",
          "expected": "Task 2 has been marked as done:\n[T][X] read Book\n"
        },
        {
          "input": "FIND BOO",
          "expected": "____________________________________________________________\nHere are the matching tasks in your list:\n1.[T][X] read Book\n2.[D][ ] return book (by: Sunday)\n3.[E][ ] book club (from: Monday to: Tuesday)\n____________________________________________________________\n"
        },
        {
          "input": "find read book",
          "expected": "____________________________________________________________\nHere are the matching tasks in your list:\n1.[T][X] read Book\n____________________________________________________________\n"
        },
        {
          "input": "find Sunday",
          "expected": "____________________________________________________________\nHere are the matching tasks in your list:\nNo matching tasks found.\n____________________________________________________________\n"
        },
        {
          "input": "find missing",
          "expected": "____________________________________________________________\nHere are the matching tasks in your list:\nNo matching tasks found.\n____________________________________________________________\n"
        },
        {
          "input": "list",
          "expected": "____________________________________________________________\nHere are the tasks in your list:\n1.[T][ ] walk dog\n2.[T][X] read Book\n3.[D][ ] return book (by: Sunday)\n4.[E][ ] book club (from: Monday to: Tuesday)\n____________________________________________________________\n"
        },
        {
          "input": "bye",
          "expected": "Woof! See you again!\n",
          "exit": true
        }
      ]
    },
    {
      "name": "Task types and completion",
      "aim": "Check typed task creation, free-form dates, numbered listing, and inherited completion behavior.",
      "steps": [
        {
          "input": "todo borrow book",
          "expected": "____________________________________________________________\nWoof! I've added this task:\n  [T][ ] borrow book\nNow you have 1 tasks in the list.\n____________________________________________________________\n"
        },
        {
          "input": "deadline homework /by no idea :-p",
          "expected": "____________________________________________________________\nWoof! I've added this task:\n  [D][ ] homework (by: no idea :-p)\nNow you have 2 tasks in the list.\n____________________________________________________________\n"
        },
        {
          "input": "event meeting /from Mon 2pm /to 4pm",
          "expected": "____________________________________________________________\nWoof! I've added this task:\n  [E][ ] meeting (from: Mon 2pm to: 4pm)\nNow you have 3 tasks in the list.\n____________________________________________________________\n"
        },
        {
          "input": "mark 1",
          "expected": "Task 1 has been marked as done:\n[T][X] borrow book\n"
        },
        {
          "input": "mark 2",
          "expected": "Task 2 has been marked as done:\n[D][X] homework (by: no idea :-p)\n"
        },
        {
          "input": "mark 3",
          "expected": "Task 3 has been marked as done:\n[E][X] meeting (from: Mon 2pm to: 4pm)\n"
        },
        {
          "input": "list",
          "expected": "____________________________________________________________\nHere are the tasks in your list:\n1.[T][X] borrow book\n2.[D][X] homework (by: no idea :-p)\n3.[E][X] meeting (from: Mon 2pm to: 4pm)\n____________________________________________________________\n"
        },
        {
          "input": "unmark 2",
          "expected": "Task 2 has been marked as not done:\n[D][ ] homework (by: no idea :-p)\n"
        },
        {
          "input": "delete 2",
          "expected": "____________________________________________________________\nNoted. I've removed this task:\n  [D][ ] homework (by: no idea :-p)\nNow you have 2 tasks in the list.\n____________________________________________________________\n"
        },
        {
          "input": "list",
          "expected": "____________________________________________________________\nHere are the tasks in your list:\n1.[T][X] borrow book\n2.[E][X] meeting (from: Mon 2pm to: 4pm)\n____________________________________________________________\n"
        },
        {
          "input": "bye",
          "expected": "Woof! See you again!\n",
          "exit": true
        }
      ]
    },
    {
      "name": "Invalid inputs do not create tasks",
      "aim": "Check missing descriptions, delimiters and invalid task numbers; verify the list remains empty.",
      "steps": [
        {
          "input": "todo",
          "expected": "____________________________________________________________\nOOPS!!! A todo needs a description leii. Woof!\n____________________________________________________________\n"
        },
        {
          "input": "deadline book /by",
          "expected": "____________________________________________________________\nOOPS!!! A deadline needs a description and follow this format: /by date or time. Woof!\n____________________________________________________________\n"
        },
        {
          "input": "event meeting /from Monday",
          "expected": "____________________________________________________________\nOOPS!!! An event needs a description and timestamp, /from time, and /to time. Woof!\n____________________________________________________________\n"
        },
        {
          "input": "mark",
          "expected": "____________________________________________________________\nPlease provide a task number, for example: mark 1\n____________________________________________________________\n"
        },
        {
          "input": "mark abc",
          "expected": "____________________________________________________________\nPlease provide a valid task number if not i will bite\n____________________________________________________________\n"
        },
        {
          "input": "unmark 0",
          "expected": "____________________________________________________________\nThat task number does not exist. *Growl*\n____________________________________________________________\n"
        },
        {
          "input": "delete",
          "expected": "____________________________________________________________\nPlease provide a task number, for example: delete 1\n____________________________________________________________\n"
        },
        {
          "input": "delete 1",
          "expected": "____________________________________________________________\nThat task number does not exist. *Growl*\n____________________________________________________________\n"
        },
        {
          "input": "blah",
          "expected": "____________________________________________________________\nOOPS!!! I don't know what that command means. WOOOOOOOOF!\n____________________________________________________________\n"
        },
        {
          "input": "list",
          "expected": "Your task list is empty :(\n"
        },
        {
          "input": "bye",
          "expected": "Woof! See you again!\n",
          "exit": true
        }
      ]
    },
    {
      "name": "Fresh process and case-insensitive commands",
      "aim": "Check memory is reset in a new session, casing is accepted, and bye terminates normally.",
      "steps": [
        {
          "input": "list",
          "expected": "Your task list is empty :(\n"
        },
        {
          "input": "TODO walk dog",
          "expected": "____________________________________________________________\nWoof! I've added this task:\n  [T][ ] walk dog\nNow you have 1 tasks in the list.\n____________________________________________________________\n"
        },
        {
          "input": "MARK 1",
          "expected": "Task 1 has been marked as done:\n[T][X] walk dog\n"
        },
        {
          "input": "UNMARK 1",
          "expected": "Task 1 has been marked as not done:\n[T][ ] walk dog\n"
        },
        {
          "input": "BYE",
          "expected": "Woof! See you again!\n",
          "exit": true
        }
      ]
    },
    {
      "name": "Task-field parsing boundaries",
      "aim": "Check missing descriptions, missing or reversed event fields, and preservation of free-form date text.",
      "steps": [
        {
          "input": "deadline /by Sunday",
          "expected": "____________________________________________________________\nOOPS!!! A deadline needs a description and follow this format: /by date or time. Woof!\n____________________________________________________________\n"
        },
        {
          "input": "event /from Monday /to Tuesday",
          "expected": "____________________________________________________________\nOOPS!!! An event needs a description and timestamp, /from time, and /to time. Woof!\n____________________________________________________________\n"
        },
        {
          "input": "event meeting /from Monday /to",
          "expected": "____________________________________________________________\nOOPS!!! An event needs a description and timestamp, /from time, and /to time. Woof!\n____________________________________________________________\n"
        },
        {
          "input": "event meeting /to Tuesday /from Monday",
          "expected": "____________________________________________________________\nOOPS!!! An event needs a description and timestamp, /from time, and /to time. Woof!\n____________________________________________________________\n"
        },
        {
          "input": "deadline report /by Sunday /by later",
          "expected": "____________________________________________________________\nWoof! I've added this task:\n  [D][ ] report (by: Sunday /by later)\nNow you have 1 tasks in the list.\n____________________________________________________________\n"
        },
        {
          "input": "event conference /from 4/10/2019 /to 11/10/2019",
          "expected": "____________________________________________________________\nWoof! I've added this task:\n  [E][ ] conference (from: 4/10/2019 to: 11/10/2019)\nNow you have 2 tasks in the list.\n____________________________________________________________\n"
        },
        {
          "input": "list",
          "expected": "____________________________________________________________\nHere are the tasks in your list:\n1.[D][ ] report (by: Sunday /by later)\n2.[E][ ] conference (from: 4/10/2019 to: 11/10/2019)\n____________________________________________________________\n"
        },
        {
          "input": "bye",
          "expected": "Woof! See you again!\n",
          "exit": true
        }
      ]
    },
    {
      "name": "Load saved tasks and skip corrupted data",
      "aim": "Check that saved task types and statuses load while a corrupted record is ignored.",
      "data_file": "T|1|cmVhZCBib29r\ncorrupted data\nD|0|cmV0dXJuIGJvb2s=|SnVuZSA2dGg=\nE|0|cHJvamVjdCBtZWV0aW5n|QXVnIDZ0aCAycG0=|NHBt\n",
      "startup": " ____              \n|  _ \\  ___   __ _ \n| | | |/ _ \\ / _` |\n| |_| | (_) | (_| |\n|____/ \\___/ \\__, |\n             |___/ \n\nWoof! What can I do for you today?\n____________________________________________________________\nOOPS!!! I skipped 1 corrupted data entry.\n____________________________________________________________\n",
      "steps": [
        {
          "input": "list",
          "expected": "____________________________________________________________\nHere are the tasks in your list:\n1.[T][X] read book\n2.[D][ ] return book (by: June 6th)\n3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)\n____________________________________________________________\n"
        },
        {
          "input": "bye",
          "expected": "Woof! See you again!\n",
          "exit": true
        }
      ]
    }
  ]
}
```

## Independent interactive check

Start `dog.Dog` from freshly compiled classes. Type a todo, mark it, list it,
unmark it and exit. Confirm the actual screen output matches the recorded status
format; do not describe the scripted tests as manual testing.

## End-of-input smoke check

Aim: verify that closing standard input exits cleanly instead of throwing an
exception. Run the release JAR in an empty folder with an empty input stream.
Expected: the normal greeting, followed by `> Woof! See you again!`, and exit
code 0. Record this check in the release smoke-test transcript.
