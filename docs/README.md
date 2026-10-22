# B.O.B.  USER GUIDE

B.O.B. — Best OpenAI Bot is a terminal chatbot for keeping track of tasks, deadlines, and events.

## Quick start: run the JAR

1. Install **Java 25**. Run `java -version` in a terminal to check your version.
2. Put `bob.jar` in a folder where you want to keep your tasks. If you have the source code instead, build the JAR using the instructions below.
3. Open a terminal in that folder and run:

   ```shell
   java -jar bob.jar
   ```

4. You should see B.O.B.'s greeting:

   ```text
   ____________________________________________________________
    ____     ___    ____
   | |_) )  / _ \  | |_) )
   |  _ \  | | | | |  _ \
   | |_) | | |_| | | |_) |
   |____/   \___/  |____/
   Hello! I'm B.O.B. (Best OpenAI Bot)
   What can I do for you?
   ____________________________________________________________
   ```


### Command format

- Replace words in `UPPER_CASE` with your own values; do not type the placeholders.
- Use lowercase command words and keep spaces around `/by`, `/from`, and `/to` as shown.
- Descriptions and dates can contain spaces. Dates and times are stored as text, so `Friday`, `June 6th`, and `2pm` are all accepted; B.O.B. does not validate dates or send reminders.
- You can keep up to **100 tasks**, including completed tasks. Delete a task to make room when the list is full.

### Adding a todo: `todo`

Adds a task without a date or time.

**Format:** `todo DESCRIPTION`

**Example:** `todo read book`

### Adding a deadline: `deadline`

Adds a task with a due date. Both the description and deadline are required.

**Format:** `deadline DESCRIPTION /by WHEN`

**Example:** `deadline return book /by June 6th`

### Adding an event: `event`

Adds an event with optional start and end times. To give an end time, include a start time first.

**Formats:**

```text
event DESCRIPTION
event DESCRIPTION /from START
event DESCRIPTION /from START /to END
```

**Example:** `event book club /from 2pm /to 4pm`

### Viewing all tasks: `list`

Shows every task with its current number. `[T]`, `[D]`, and `[E]` mean todo, deadline, and event. `[X]` means completed; `[ ]` means incomplete.

**Format:** `list`

### Marking or unmarking a task: `mark`, `unmark`

Use a positive task number from the full `list` to change its completion status.

**Formats:** `mark NUMBER`, `unmark NUMBER`

**Examples:** `mark 1`, `unmark 1`

### Finding tasks: `find`

Searches task descriptions for a keyword or phrase. Matching is **case-sensitive** and includes partial words: `book` matches `notebook`, but not `Book`. Dates and times are not searched.

**Format:** `find KEYWORD`

**Examples:** `find book`, `find read book`

If nothing matches, B.O.B. displays `No tasks found matching that keyword.` A keyword is required.

Search results are numbered separately from the full task list. **Run `list` before marking, unmarking, or deleting a task, and use its number from that full list.**

### Deleting a task: `delete`

Permanently removes the task at the given number from the full list. Remaining tasks are renumbered, so check `list` again before another deletion. There is no undo command.

**Format:** `delete NUMBER`

**Example:** `list`, then `delete 2`

### Exiting: `bye`

Closes B.O.B. with a farewell message.

**Format:** `bye`

### Saving and loading tasks

B.O.B. automatically saves after adding, marking, unmarking, or deleting a task. It loads saved tasks when it starts. No save command is needed.

Tasks are stored in `data/bob.txt` relative to the folder where you run the application. The file is created on the first successful save. To back up or transfer tasks, close B.O.B. and copy this file with its `data` folder.

Avoid the `|` character in descriptions and dates: it separates fields in the saved file. Malformed saved records are skipped when loading.

### If something goes wrong

- **Invalid command or missing details:** Check the format above and try again. B.O.B. stays open after an input error.
- **Invalid task number:** Run `list` and choose an existing positive number.
- **Could not load tasks:** B.O.B. starts with an empty list. Check that `data/bob.txt` is readable and back it up before adding tasks, as a later save can replace it.
- **Could not save tasks:** The change remains in memory but may be lost when you exit. Check that your chosen folder is writable. Once fixed, another add, mark, unmark, or delete command retries saving the entire list.
