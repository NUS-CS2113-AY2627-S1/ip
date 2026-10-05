# Ted

Ted is a command-line task manager for keeping track of todos, deadlines, and
events. It is an individual project (iP) for CS2113 and is implemented in
Java using object-oriented design.

## Features

- Add todos with a description.
- Add deadlines with a due date in `yyyy-MM-dd` format.
- Add events with a start and end date or time.
- List all tasks in the order they were added.
- Find tasks by searching their descriptions for a keyword.
- Mark tasks as done or not done.
- Delete tasks you no longer need.
- Save tasks automatically in `data/ted.txt` and restore them when Ted starts.
- Reject incomplete deadline and event commands with a usage message.
- Report invalid commands and task numbers with clear error messages.

## Quick start

### Prerequisites

- Java Development Kit (JDK) 25, with `java` available in your terminal.

### Run Ted

Download [ted-1.1.0.jar](https://github.com/holsoma/ip/releases/download/v1.1.0/ted-1.1.0.jar)
from the [latest release](https://github.com/holsoma/ip/releases/latest).
Put it in a folder of your choice, open a terminal there, and check that
`java --version` reports Java 25. Then run:

```text
java -jar ted-1.1.0.jar
```

Tasks are stored in `data/ted.txt`, relative to the folder where you run
the command. Use the terminal rather than double-clicking the JAR.
See the [User Guide](https://holsoma.github.io/ip/) for setup details and
command examples.

To run from source instead, download or clone the project, open a terminal
in the project root, and use JDK 25:

```text
java src/main/java/ted/Ted.java
```

## Usage

| Command | Example | Description |
| --- | --- | --- |
| `todo DESCRIPTION` | `todo borrow book` | Adds a todo. |
| `deadline DESCRIPTION /by yyyy-MM-dd` | `deadline return book /by 2026-10-15` | Adds a deadline. |
| `event DESCRIPTION /from START /to END` | `event project meeting /from Mon 2pm /to 4pm` | Adds an event. |
| `list` | `list` | Displays all tasks. |
| `find KEYWORD` | `find book` | Displays tasks with the keyword in their description. |
| `mark TASK_NUMBER` | `mark 2` | Marks a task as done. |
| `unmark TASK_NUMBER` | `unmark 2` | Marks a task as not done. |
| `delete TASK_NUMBER` | `delete 2` | Removes a task. |
| `bye` | `bye` | Exits Ted. |

Deadline dates are stored as `LocalDate` values and displayed as `MMM dd yyyy`
in English. Invalid dates, such as `2026-02-30`, are rejected without adding a
task. Event details are kept as entered. For example:

```text
todo borrow book
    Got it. I've added this task:
      [T][ ] borrow book

deadline return book /by 2026-10-15
    Got it. I've added this task:
      [D][ ] return book (by: Oct 15 2026)

event project meeting /from Mon 2pm /to 4pm
    Got it. I've added this task:
      [E][ ] project meeting (from: Mon 2pm to: 4pm)
```

Saved deadlines use `yyyy-MM-dd` dates. Older deadline records containing
free text, such as `Sunday`, are reported as invalid and skipped when loading.

Search matches a literal, case-sensitive substring in the description.
For example, `find book` matches `borrow book` and `return book`, but
`find Book` does not match those descriptions. Dates and event times are
not searched. A missing keyword produces an error, and a search with no
matches displays an empty result list.

Search results are numbered from one in their existing task order. Use
`list` to obtain the full task-list numbers before marking or deleting a task.

## Documentation and testing

- [Ted User Guide](docs/README.md)
- [UI test plan](test/ui-test-plan.md)
- [Latest UI test transcript](test/ui-test-session.txt)

## Acknowledgements

OpenAI Codex was used extensively during development to review and refactor
command handling, draft documentation, and run UI regression checks. The
resulting changes were checked with Java 25 compilation and the documented UI
test cases.
