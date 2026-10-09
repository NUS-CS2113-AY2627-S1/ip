# Nimbus

**Sunglasses on. Briefcase loaded. Let's do business.**

Nimbus is a command-line task manager with main character energy.
Manage todos, deadlines, and events; find tasks; and track completed
business. Your tasks are saved between sessions.

## Getting started

1. Install Java 25.
2. Download `ip.jar` from the
   [latest release](https://github.com/Rohita-29/ip/releases/latest).
3. Place the JAR in a folder where you have permission to create files.
4. Open a terminal in that folder and run:

   ```bash
   java -jar ip.jar
   ```

5. Enter a command. Type `bye` to exit.

## Commands

| Action | Format |
|--------|--------|
| Add a todo | `todo <description>` |
| Add a deadline | `deadline <description> /by <date/time>` |
| Add an event | `event <description> /from <start> /to <end>` |
| List tasks | `list` |
| Find tasks | `find <keyword>` |
| Mark completed | `mark <task number>` |
| Mark incomplete | `unmark <task number>` |
| Delete a task | `delete <task number>` |
| Exit | `bye` |

Replace the placeholders with your own values.
New dates accept `yyyy-MM-dd` or `yyyy-MM-dd HHmm`.

See the [User Guide](docs/README.md) for examples, input restrictions,
and information about saving and loading.

## Running from source

1. Clone this repository.
2. Open the project in IntelliJ IDEA.
3. Set the project SDK to Java 25.
4. Ensure `src/main/java` is marked as a Sources Root.
5. Run `nimbus.Nimbus` from
   `src/main/java/nimbus/Nimbus.java`.

## Task storage

Nimbus stores tasks in `data/nimbus.txt`, relative to the folder
from which it runs. Use the same working folder between sessions
to continue using your existing task list.

## Acknowledgements

Nimbus was developed from the
[Duke project template](https://github.com/se-edu/duke)
for the NUS CS2113 individual project.