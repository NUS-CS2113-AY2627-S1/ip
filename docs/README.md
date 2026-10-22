# MyBff User Guide

MyBff is a terminal chatbot for your to-dos, deadlines, and events. Type a command and press **Enter** to manage your tasks.

## Quick start

1. Install **Java 25**. Check your version with `java -version`.
2. Open [A-Release](https://github.com/shengyang2010/ip/releases/tag/A-Release) and download the **`.jar`** file under **Assets**, not a source-code archive.
3. Save it in a folder of your choice and rename it to `mybff.jar` if needed.
4. Open a terminal in that folder and run:

   ```text
   java -jar mybff.jar
   ```

5. Try `todo read book`, then `list`. Type `bye` to exit.

Start MyBff from the same folder each time to load your saved tasks. No build is needed to use the release JAR.

## Features

Use lowercase command words. Replace uppercase placeholders with your own text, keeping the order and spaces around `/by`, `/from`, and `/to` shown below. Dates and times are plain text: MyBff does not validate them or send reminders.

### Add a to-do: `todo`

Adds a task without a date or time.

**Format:** `todo DESCRIPTION`

**Example:** `todo read book`

### Add a deadline: `deadline`

Adds a task with a due date or time.

**Format:** `deadline DESCRIPTION /by WHEN`

**Example:** `deadline submit report /by Friday 18:00`

### Add an event: `event`

Adds a task with a start and end time.

**Format:** `event DESCRIPTION /from START /to END`

**Example:** `event team meeting /from Monday 14:00 /to Monday 15:00`

Each addition confirms the new task and total task count.

### View all tasks: `list`

Enter `list` to show all tasks, including completed ones, in the order you added them. After adding the examples above:

```text
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] submit report (by: Friday 18:00)
3.[E][ ] team meeting (from: Monday 14:00 to: Monday 15:00)
```

`[T]` means to-do, `[D]` deadline, and `[E]` event. `[ ]` means not done; `[X]` means done.

### Find tasks: `find`

**Format:** `find KEYWORD`

Searches task descriptions only. Matching is **case-sensitive** and includes parts of words: `find book` matches `read book` and `buy books`, but not `read Book`. Multiple words are matched as one exact phrase. No matches means only the results heading is shown.

> Search results have separate numbering. Before using `mark`, `unmark`, or `delete`, run `list` and use the task's number from the full list.

### Change completion status: `mark` / `unmark`

**Formats:** `mark NUMBER` and `unmark NUMBER`

**Examples:** `mark 1` marks the first task as done (`[X]`). `unmark 1` changes it back to not done (`[ ]`). The task stays in your list.

### Delete a task: `delete`

**Format:** `delete NUMBER`

**Example:** `delete 2` removes the second task and confirms the remaining count.

Deletion is immediate, with no undo. Remaining tasks are renumbered, so run `list` before your next change. Task numbers must be positive and refer to an existing task.

### Exit: `bye`

Enter `bye` to close MyBff.

### Automatic saving

Every successful addition, deletion, or status change is saved to `data/mybff.txt` in the folder you run MyBff from. Tasks reload on startup; no save command is needed.

To back up or move your tasks, close MyBff and copy the `data` folder along with the JAR.

## Troubleshooting

- **Java not found:** Check your Java installation and `PATH`, then reopen the terminal.
- **Unable to access jarfile:** Check the JAR's name and open a terminal in its folder.
- **Unknown command:** Check spelling and lowercase letters. Enter `list` and `bye` on their own.
- **Invalid task number:** Run `list` and use an existing number, starting at 1.
- **Empty to-do or search:** Supply a description after `todo` or text after `find`.
- **Could not save:** The change was cancelled. Check folder permissions and file locks, then retry.
- **Could not load:** MyBff stops without changing your file. Check that it is readable or restore a backup, then restart.

## Optional: build from source

1. Install **JDK 25**. Both `java -version` and `javac -version` should report 25. If set, `JAVA_HOME` must point to that JDK.
2. Open the [repository](https://github.com/shengyang2010/ip), select **Code > Download ZIP**, and extract it. Alternatively, with Git installed:

   ```sh
   git clone https://github.com/shengyang2010/ip.git
   cd ip
   ```

3. Open a terminal in the project folder containing `build.gradle`, not `docs`. Build using the command for your system:

   **Windows PowerShell:**

   ```powershell
   .\gradlew.bat shadowJar
   ```

   **macOS/Linux:**

   ```sh
   sh ./gradlew shadowJar
   ```

4. Wait for `BUILD SUCCESSFUL`, then run:

   ```text
   java -jar build/libs/mybff.jar
   ```

The first build needs internet access to download dependencies. No separate Gradle installation is needed. The output is `build/libs/mybff.jar`; rebuild after changing the source.
