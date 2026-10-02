# Slay69 User Guide

Slay69 is a command-line chatbot that helps you keep track of tasks. You can add to-dos, dated deadlines, and events, then mark them complete or search for them.

## Getting started

Install Java 25. Download the JAR from the latest GitHub release, open a terminal in a folder where you want your task data stored, and run:

```text
java -jar ip.jar
```

If you renamed the downloaded JAR, use its new filename instead. Enter one command per line. Type `bye` to exit. Slay69 automatically saves changes to `data/slay69.txt` in the folder where you run it and loads them next time.

## Commands

| What you want to do | Command | Example |
| --- | --- | --- |
| Add a to-do | `todo DESCRIPTION` | `todo read book` |
| Add a deadline | `deadline DESCRIPTION /by yyyy-MM-dd` | `deadline return book /by 2019-12-02` |
| Add an event | `event DESCRIPTION /from START /to END` | `event project meeting /from 2pm /to 3pm` |
| Show all tasks | `list` | `list` |
| Search descriptions | `find KEYWORD` | `find book` |
| Mark a task complete | `mark NUMBER` | `mark 1` |
| Mark a task incomplete | `unmark NUMBER` | `unmark 1` |
| Remove a task | `delete NUMBER` | `delete 1` |
| Exit | `bye` | `bye` |

Deadline dates must use `yyyy-MM-dd` when entered; for example, `2019-12-02` is displayed as `Dec 2 2019`. Event start and end values are displayed as entered.

Use `list` to see each task’s number before using `mark`, `unmark`, or `delete`. A completed task shows `[X]`; an incomplete task shows `[ ]`. `find` ignores letter case, searches task descriptions, and keeps the task numbers from the full list.

For example:

```text
todo read book
deadline return book /by 2019-12-02
list
find book
mark 1
bye
```
