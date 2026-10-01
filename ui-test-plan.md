# Goober UI Test Plan

This plan tests the current Goober command-line user interface by entering commands
and checking the displayed output. Start the application from a fresh run for each
test case unless a sequence is shown.

## Test environment

- Application: Goober
- Interface: command line
- Tester: ____________________
- Date: ____________________

Record `Pass` or `Fail` in the final column after executing each test.

## 1. Startup and exit

| ID | Steps | Expected result | Result |
|---|---|---|---|
| UI-01 | Start the application. | The Goober banner, welcome message, and prompt separator are displayed. | |
| UI-02 | Enter `bye`. | Goober displays the goodbye message and terminates. | |
| UI-03 | Enter `bye now`. | Goober displays an error stating that `bye` does not accept additional input and continues running. | |

## 2. Help and listing

| ID | Steps | Expected result | Result |
|---|---|---|---|
| UI-04 | Enter `help`. | Goober displays the supported commands and their formats. | |
| UI-05 | Enter `list` immediately after startup. | Goober displays an empty task list and reports that there are 0 tasks. | |
| UI-06 | Enter `list now`. | Goober displays an error stating that `list` does not accept additional input. | |

## 3. Adding todo tasks

| ID | Steps | Expected result | Result |
|---|---|---|---|
| UI-07 | Enter `todo read book`. | Goober adds the task, displays `[T][ ] read book`, and reports 1 task. | |
| UI-08 | Enter `todo` with no description. | Goober displays an error that a todo description cannot be empty. | |
| UI-09 | Enter `todo    buy milk`. | Goober adds a todo with the trimmed description `buy milk`. | |
| UI-10 | Enter `TODO read book`. | Goober treats the command as `todo` and adds the task. | |

## 4. Adding deadline tasks

| ID | Steps | Expected result | Result |
|---|---|---|---|
| UI-11 | Enter `deadline return book /by June 6th`. | Goober adds `[D][ ] return book (by: June 6th)`. | |
| UI-12 | Enter `deadline return book`. | Goober displays an error requiring a description and `/by` value. | |
| UI-13 | Enter `deadline /by June 6th`. | Goober displays an error because the description is empty. | |
| UI-14 | Enter `deadline return book /by`. | Goober displays an error because the `/by` value is empty. | |

## 5. Adding event tasks

| ID | Steps | Expected result | Result |
|---|---|---|---|
| UI-15 | Enter `event project meeting /from Aug 6th 2pm /to 4pm`. | Goober adds `[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)`. | |
| UI-16 | Enter `event project meeting`. | Goober displays an error requiring description, `/from`, and `/to` values. | |
| UI-17 | Enter `event /from Aug 6th /to 4pm`. | Goober displays an error because the description is empty. | |
| UI-18 | Enter `event project meeting /from /to 4pm`. | Goober displays an error because the `/from` value is empty. | |
| UI-19 | Enter `event project meeting /from 4pm`. | Goober displays an error because the `/to` value is missing. | |

## 6. Marking and unmarking tasks

| ID | Steps | Expected result | Result |
|---|---|---|---|
| UI-20 | Add a todo, then enter `mark 1`. | The task is marked done and displayed with `[X]`. | |
| UI-21 | Enter `mark 1` again. | Goober reports that the task is already marked as done. | |
| UI-22 | Enter `unmark 1`. | The task is marked as not done and displayed with `[ ]`. | |
| UI-23 | Enter `unmark 1` again. | Goober reports that the task is already not done. | |
| UI-24 | Enter `mark abc`, `mark 0`, and `mark 99`. | Each input produces an appropriate invalid-number or task-not-found error; the application does not crash. | |
| UI-25 | Enter `unmark abc`, `unmark 0`, and `unmark 99`. | Each input produces an appropriate invalid-number or task-not-found error; the application does not crash. | |

## 7. Deleting tasks (Level-6)

These tests apply after the Level-6 implementation has been completed.

| ID | Steps | Expected result | Result |
|---|---|---|---|
| UI-26 | Add three tasks, then enter `delete 1`. | The first task is removed and Goober reports 2 remaining tasks. | |
| UI-27 | Add three tasks, then enter `delete 2`. | The middle task is removed; the former task 3 becomes task 2. | |
| UI-28 | Add three tasks, then enter `delete 3`. | The last task is removed and Goober reports 2 remaining tasks. | |
| UI-29 | Enter `delete abc`, `delete 0`, `delete -1`, and `delete 99`. | Goober displays an error and does not crash or remove an unintended task. | |
| UI-30 | Enter `delete` with no number. | Goober reports that a valid task number is required. | |
| UI-31 | Delete the only task, then enter `list`. | The list is empty and Goober reports 0 tasks. | |
| UI-32 | Add todo, deadline, and event tasks; delete each type. | The correct task and its complete formatted details are shown in the deletion confirmation. | |

## 8. Unknown commands and command suggestions

| ID | Steps | Expected result | Result |
|---|---|---|---|
| UI-33 | Enter `dance`. | Goober displays an unknown-command error and continues running. | |
| UI-34 | Enter a one-character misspelling such as `lst`. | Goober displays an unknown-command error and suggests `list`. | |
| UI-35 | Enter an empty line. | Goober does not crash and displays an unknown-command error. | |

## 9. End-to-end workflow

| ID | Steps | Expected result | Result |
|---|---|---|---|
| UI-36 | Enter `todo read book`, `deadline return book /by June 6th`, `event meeting /from 2pm /to 4pm`, `mark 1`, `delete 2`, `list`, then `bye`. | Tasks retain their correct types and statuses, deletion renumbers the remaining tasks, and the application exits normally. | |

## Notes

- Task numbers entered by the user are one-based: the first task is task 1.
- Each test should verify both the message and the continued application state.
- If the application does not yet support `delete`, mark UI-26 through UI-32 as `Blocked` until Level-6 is implemented.
