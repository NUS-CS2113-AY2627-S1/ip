# Proton User Guide

Proton is a console chatbot that helps you keep track of todos, deadlines, and events.
Type a command and press **Enter** to manage your tasks.

## Getting started

1. Install JDK **25** and open the project in IntelliJ IDEA with JDK 25 selected.
2. Open `src/main/java/proton/Proton.java` and run `Proton.main()`.
   Set the run configuration's working directory to the project root so Proton uses the same saved tasks each time.
3. In the console, enter `todo read book`, then `list` to see your first task.

## Command format

- Use lowercase command words exactly as shown, without leading spaces.
- Replace uppercase placeholders such as `DESCRIPTION` with your own text; all shown arguments are required.
- Keep spaces around `/by`, `/from`, and `/to`, and put event times in the order shown.
- Dates and times are free text, such as `Friday` or `28 Sep 2026 6pm`. Proton does not validate dates or send reminders.

## Features

### Add a todo: `todo`

Adds a task without a date or time.

Format: `todo DESCRIPTION`

Example: `todo read book`

### Add a deadline: `deadline`

Adds a task with a due date or time.

Format: `deadline DESCRIPTION /by DATE`

Example: `deadline return book /by Friday 5pm`

### Add an event: `event`

Adds a task with a start and end date or time.

Format: `event DESCRIPTION /from START /to END`

Example: `event study group /from Monday 2pm /to Monday 4pm`

Each successful addition displays the new task and the total number of tasks.
New tasks start as incomplete.

### View all tasks: `list`

Enter `list` to display all tasks in their current order. For example, after adding the three tasks above:

```text
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[D][ ] return book (by: Friday 5pm)
 3.[E][ ] study group (from: Monday 2pm to: Monday 4pm)
```

`[T]` means todo, `[D]` means deadline, and `[E]` means event.
`[ ]` means incomplete; `[X]` means done. An empty list shows only the heading.

### Find tasks: `find`

Displays tasks whose descriptions contain the supplied text.
Matching is case-sensitive: `book` matches `read book`, but `Book` does not.
Dates and times are not searched. Multiple words are matched as one continuous phrase.

Format: `find KEYWORD`

Example: `find book`

If no tasks match, Proton displays the matching-tasks heading without any tasks below it.

**Run `list` before marking, unmarking, or deleting a task.** Search results are numbered separately;
the commands below always use the task number from the full list.

### Mark or unmark a task: `mark` / `unmark`

Use `mark TASK_NUMBER` to mark a task as done, or `unmark TASK_NUMBER` to make it incomplete again.
Proton displays the task with its updated status.

Examples: `mark 1`, then `unmark 1`.

The task number must be a positive integer shown by `list`.

### Delete a task: `delete`

Removes the task and displays it along with the number of remaining tasks.
There is no undo command. Later tasks are renumbered, so run `list` again before your next change.

Format: `delete TASK_NUMBER`

Example: `delete 2`

### Exit: `bye`

Enter `bye` to close Proton. Your successfully saved tasks will be loaded the next time you start it.

## Saving and troubleshooting

Proton automatically saves after each successful addition, mark, unmark, or deletion to
`data/proton.txt` under its working directory. No manual save command is needed.
A missing save file starts an empty list. Use one Proton instance at a time.

- **Invalid command or task number:** read the error, check the formats above, and use `list` to check task numbers.
- **Cannot load tasks:** check that `data/proton.txt` is readable and valid, or restore a backup, then restart Proton.
- **Cannot save tasks:** the attempted change is cancelled. Check the save path, permissions, and available disk space,
  then retry. If the save file was changed elsewhere, restart Proton to load it before trying again.
