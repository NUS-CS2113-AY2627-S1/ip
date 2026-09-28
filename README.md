# GPT

```text
  ____ ____ _____
 / ___|  _ \_   _|
| |  _| |_) || |
| |_| |  __/ | |
 \____|_|    |_|
```

GPT is a command-line task manager written in Java. It manages todos,
deadlines, and events, supports keyword search, and automatically saves tasks
between sessions.

## Features

- Add todos, deadlines, and events
- List, mark, unmark, and delete tasks
- Find tasks using a case-insensitive keyword search
- Parse deadline dates in `yyyy-MM-dd` format and display readable dates
- Save tasks automatically to `data/gpt.txt`
- Handle invalid and incomplete commands without terminating

## Quick Start

1. Install Java 25 or later.
2. Download `Gpt.jar` from the
   [latest GitHub release](https://github.com/Darren50/ip/releases/latest).
3. Put the JAR file in a folder where GPT can create its `data` folder.
4. Open a terminal in that folder and run:

   ```text
   java -jar Gpt.jar
   ```

See the [GPT User Guide](https://darren50.github.io/ip/) for complete command
instructions and examples.

## Commands

| Action | Command |
| --- | --- |
| Add a todo | `todo DESCRIPTION` |
| Add a deadline | `deadline DESCRIPTION /by yyyy-MM-dd` |
| Add an event | `event DESCRIPTION /from START /to END` |
| List tasks | `list` |
| Find tasks | `find KEYWORD` |
| Mark a task | `mark NUMBER` |
| Unmark a task | `unmark NUMBER` |
| Delete a task | `delete NUMBER` |
| Exit GPT | `bye` |

Example:

```text
todo read book
deadline submit report /by 2026-10-15
event project meeting /from Monday 2pm /to Monday 4pm
list
mark 1
find report
bye
```

## Data Storage

GPT saves task changes automatically in `data/gpt.txt`, relative to the folder
from which the application is run. If the folder or file does not exist, GPT
creates it when needed.

## Development Setup

### Prerequisites

- JDK 25
- A recent version of IntelliJ IDEA

### Running from IntelliJ IDEA

1. Open this repository as an IntelliJ IDEA project.
2. Configure the project SDK as JDK 25 and use the SDK default language level.
3. Keep `src/main/java` configured as the source root.
4. Run the `main` method in `src/main/java/gpt/Gpt.java`.

## Project Structure

The application uses separate classes for user interaction, command parsing,
task-list operations, and file storage:

- `Gpt`: application entry point and coordinator
- `Ui`: console input and output
- `Parser`: command parsing and validation
- `TaskList`: task collection operations
- `Storage`: loading and saving tasks
- `Task`, `Todo`, `Deadline`, and `Event`: task model classes
- `GptException`: application-specific error handling

## Documentation

- [User Guide](https://darren50.github.io/ip/)
- [GitHub releases](https://github.com/Darren50/ip/releases)
