# Dandelion

Dandelion is a command-line task manager for creating, tracking, searching, and
maintaining tasks. It supports todo, deadline, and event tasks, task status
updates, deletion, keyword search, and file-based persistence.

## Requirements

- JDK 25
- A terminal opened at the repository root

The Gradle build is configured to use Java 25 through its Java toolchain.

## Running the application

On macOS or Linux, run:

```bash
./gradlew run
```

On Windows, run:

```bat
gradlew.bat run
```

## Building an executable JAR

On macOS or Linux:

```bash
./gradlew shadowJar
java -jar build/libs/dandelion-1.0.0-all.jar
```

On Windows:

```bat
gradlew.bat shadowJar
java -jar build\libs\dandelion-1.0.0-all.jar
```

The generated `-all.jar` includes the application's runtime dependencies.

## Commands

| Command | Format | Description |
| --- | --- | --- |
| `todo` | `todo DESCRIPTION` | Adds a task without a deadline or time period. |
| `deadline` | `deadline DESCRIPTION /by yyyy-MM-dd` | Adds a task with a date deadline. |
| `event` | `event DESCRIPTION /from START /to END` | Adds a task with a time period. |
| `list` | `list` | Displays all tasks. |
| `mark` | `mark NUMBER` | Marks a task as done. |
| `unmark` | `unmark NUMBER` | Marks a task as not done. |
| `delete` | `delete NUMBER` | Deletes a task. |
| `find` | `find KEYWORD` | Finds tasks whose descriptions contain the keyword. |
| `bye` | `bye` | Exits the application. |

For complete command examples and validation messages, see the [Dandelion User
Guide](docs/README.md).

## Data persistence

Tasks are stored in `data/dandelion.txt` relative to the directory from which
the application is launched. Tasks are loaded at startup and saved after tasks
are added, marked, unmarked, or deleted.

## Testing

Run the console UI test plan from the repository root:

```bash
python3 test-ui/scripts/run_ui_tests.py
```

The test plan is available at [test/ui-test-plan.md](test/ui-test-plan.md).

## Project structure

The application is organized into classes with focused responsibilities:

- `dandelion.Dandelion` — application entry point and main loop.
- `dandelion.command.CommandHandler` — executes parsed commands.
- `dandelion.parser.Parser` — interprets user input.
- `dandelion.storage.Storage` — loads and saves tasks.
- `dandelion.task.TaskList` — manages the task collection.
- `dandelion.ui.Ui` — handles console interaction and output.
