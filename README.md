# Aglio

Aglio is a command-line task manager that helps you keep track of todos, deadlines, and events.

```text
    _         _ _       
   / \   __ _| (_) ___  
  / _ \ / _` | | |/ _ \ 
 / ___ \ (_| | | | (_) |
/_/   \_\__, |_|_|\___/ 
        |___/           
```

## Quick Start

1. Ensure you have **Java 25** installed.
2. Download the latest `aglio.jar` from the releases page.
3. Run the application:

   ```shell
   java -jar aglio.jar
   ```

## Features

- **Todos** — tasks with no date: `todo read book`
- **Deadlines** — tasks with a due date: `deadline return book /by 2019-10-15`
- **Events** — tasks with a start and end time: `event meeting /from Mon 2pm /to 4pm`
- **Mark / Unmark** — toggle task completion: `mark 1`, `unmark 1`
- **Delete** — remove a task: `delete 2`
- **Find** — search tasks by keyword: `find book`
- **Auto-save** — tasks are saved automatically to `data/aglio.txt`

For the full command reference, see the [User Guide](docs/README.md).
