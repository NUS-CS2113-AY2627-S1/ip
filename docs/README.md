---
layout: default
title: Bill User Guide
permalink: /index.html
---

# Bill User Guide

Bill is a friendly command-line task tracker. Give Bill a to-do, deadline, or event, and it will keep your list and progress together. Your tasks are saved automatically between sessions.

## Get started

You need **Java 25**. Download `bill.jar` from the [latest GitHub release](https://github.com/Bealng/ip/releases/latest), open a terminal in the folder containing the JAR, and run `java -jar bill.jar`. Run it from the same folder each time so Bill can find your saved tasks.

If you have the source code instead, run `./gradlew run` on macOS or Linux, or `./gradlew.bat run` on Windows, from the project folder. You can also open the project in IntelliJ IDEA with JDK 25 and run `bill.Bill.main()`.

Type one command per line. Try this short session:

```text
todo Read a book
deadline Return library book /by 2026-10-02
event Study group /from Friday 4pm /to Friday 6pm
list
mark 1
find book
stats
bye
```

Type `help` in Bill at any time for a command reminder.

## Commands

| What you want to do | Command | Example |
| --- | --- | --- |
| Add a to-do | `todo DESCRIPTION` | `todo Read a book` |
| Add a deadline | `deadline DESCRIPTION /by WHEN` | `deadline Return library book /by 2026-10-02` |
| Add an event | `event DESCRIPTION /from START /to END` | `event Study group /from Friday 4pm /to Friday 6pm` |
| See all tasks | `list` | `list` |
| Find tasks by description | `find KEYWORD` | `find book` |
| Mark a task done | `mark NUMBER` | `mark 1` |
| Mark a task not done | `unmark NUMBER` | `unmark 1` |
| Remove a task | `delete NUMBER` | `delete 1` |
| See your progress | `stats` | `stats` |
| Show command help | `help` | `help` |
| Leave Bill | `bye` | `bye` |

Descriptions can contain spaces; you do not need quotation marks. Task numbers start at **1** and are shown by `list`. After deleting a task, use `list` again because the remaining tasks are renumbered.

### Dates and events

For a deadline, an ISO date such as `2026-10-02` is checked as a real date and displayed as **Oct 02 2026**. Bill also accepts free-form deadline text such as `Monday 9pm` and shows that text as entered. Event start and end values are free-form text, for example `Friday 4pm` and `Friday 6pm`; Bill does not turn them into calendar appointments or send reminders.

### Finding and updating tasks

`find` searches task **descriptions** without caring about letter case. It does not search deadline dates or event times. Matching tasks are numbered from 1 *within the search results*; those numbers might differ from the full list. Use the number shown by `list` when you `mark`, `unmark`, or `delete` a task.

There is no edit command yet. To correct a task, use `list`, `delete NUMBER`, and then add it again with the corrected details.

### Saving your work

Bill stores tasks in `data/bill.txt`, relative to the folder from which you run it. It saves after you add, mark, unmark, or delete a task, then reloads that file next time. If there is no saved file, Bill starts with an empty list. Keep the file if you want to keep your tasks; avoid editing it by hand.

In `list`, `[T]` means to-do, `[D]` deadline, and `[E]` event. `[X]` means done; `[ ]` means not done.

---

[View Bill's source code](https://github.com/Bealng/ip) · [Report an issue](https://github.com/Bealng/ip/issues)
