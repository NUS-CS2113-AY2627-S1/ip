# Dr. Pijon User Guide

Dr. Pijon is a friendly command-line task manager. It helps you record todos,
deadlines, and events, then stores them in `data/drpijon.txt` so they are
available the next time you start the application.

## Getting started

### Requirements

- JDK 25
- A terminal or IntelliJ IDEA

From the project root, start Dr. Pijon with:

```text
./gradlew run
```

On Windows, use:

```text
.\gradlew.bat run
```

### Building and running the packaged JAR

1. Open a command window in the project root.
2. Build the packaged JAR with:

```text
.\gradlew.bat shadowJar
```

Gradle creates the JAR at `build/libs/drpijon.jar`.

3. Copy `build/libs/drpijon.jar` into an empty folder.
4. Open a command window in the folder containing the copied JAR file.
5. Run `java -jar "drpijon.jar"`.

Dr. Pijon greets you and waits for one command per line. Enter `bye` when you
are finished.

### IntelliJ IDEA setup

1. Open this project directory in IntelliJ IDEA.
2. Configure the project SDK and language level to JDK 25.
3. Run `src/main/java/drpijon/Main.java`.

If IntelliJ reports stale compile errors, reload the Gradle project or restart
the IDE. Keep `src/main/java` as the Java source root because the Gradle build
expects that directory layout.

## Commands

| Command | Format | What it does |
| --- | --- | --- |
| `todo` | `todo <description>` | Adds a task without a date. |
| `deadline` | `deadline <description> /by <date>` | Adds a task due on a date or at a date and time. |
| `event` | `event <description> /from <date> /to <date>` | Adds an event between two dates or date-times. |
| `list` | `list` | Displays every task and its current status. |
| `mark` | `mark <number>` | Marks a task as complete. |
| `unmark` | `unmark <number>` | Marks a task as incomplete. |
| `delete` | `delete <number>` | Removes a task from the list. |
| `on` | `on <date>` | Displays deadlines and events occurring on a date. |
| `find` | `find <keyword>` | Finds tasks whose descriptions contain a keyword. |
| `bye` | `bye` | Exits the application; changes are saved immediately. |

Task numbers are the one-based numbers shown by `list`. Use them with
`mark`, `unmark`, and `delete`.

## Dates and times

Use one of these formats for deadline and event dates:

- Date only: `yyyy-MM-dd`, such as `2019-10-15`
- Date and time: `yyyy-MM-dd HHmm`, such as `2019-10-15 1800`

Times use the 24-hour clock. Dr. Pijon displays `2019-10-15 1800` as
`Oct 15 2019, 6:00 PM`.

For an event spanning multiple dates, `on <date>` includes the event on every
date from its start date through its end date. Date searches use typed ISO
dates; older free-text event dates cannot be matched reliably.

## Example session

```text
todo read book
deadline return book /by 2019-10-15 1800
event project meeting /from 2026-08-06 1400 /to 2026-08-06 1600
list
find book
on 2026-08-06
mark 1
unmark 1
delete 1
bye
```

`find` searches descriptions without regard to letter case. For example,
`find BOOK` matches both `read book` and `return book`. If no task matches, Dr.
Pijon reports that no matching tasks were found.

## Common mistakes

- Include `/by` when adding a deadline.
- Include both `/from` and `/to` when adding an event.
- Use `yyyy-MM-dd` or `yyyy-MM-dd HHmm` for typed dates.
- Provide a task number for `mark`, `unmark`, and `delete`.
- Provide a keyword after `find` and a date after `on`.

Invalid commands or arguments produce an error message, but the application
continues waiting for the next command.
