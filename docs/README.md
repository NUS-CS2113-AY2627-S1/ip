# Chre User Guide

Chre is a friendly task management chatbot that helps you stay organized. Add tasks, set deadlines, track events, and search your task list—all from the command line!

## Getting Started

### Running Chre

```bash
java -jar chre.jar
```

Once started, Chre will greet you and wait for your commands. Type your commands and press Enter to execute them.

---

## Quick Reference

| Command | Format | Example |
|---------|--------|---------|
| **Add Todo** | `todo <description>` | `todo Buy groceries` |
| **Add Deadline** | `deadline <description> /by yyyy-MM-dd` | `deadline Finish project /by 2025-10-15` |
| **Add Event** | `event <description> /from yyyy-MM-dd /to yyyy-MM-dd` | `event Team meeting /from 2025-11-01 /to 2025-11-02` |
| **List Tasks** | `list` | `list` |
| **Mark Done** | `mark <task number>` | `mark 1` |
| **Unmark** | `unmark <task number>` | `unmark 1` |
| **Delete Task** | `delete <task number>` | `delete 2` |
| **Find Tasks** | `find <keyword>` | `find book` |
| **Exit** | `bye` | `bye` |

---

## Features

### Adding Tasks

#### Todo Tasks
Use this for simple tasks without a specific date.

```
todo Buy groceries
```

**Output:**
```
Got it. I've added this task:
  [T][ ] Buy groceries
Now you have 1 tasks in the list.
```

#### Deadline Tasks
Set a task with a due date. Use the format `yyyy-MM-dd` (e.g., `2025-10-15`).

```
deadline Submit essay /by 2025-10-15
```

**Output:**
```
Got it. I've added this task:
  [D][ ] Submit essay (by: Oct 15 2025)
Now you have 2 tasks in the list.
```

#### Event Tasks
Track events with a start and end date. Use the format `yyyy-MM-dd`.

```
event Company retreat /from 2025-11-15 /to 2025-11-17
```

**Output:**
```
Got it. I've added this task:
  [E][ ] Company retreat (from: Nov 15 2025 to: Nov 17 2025)
Now you have 3 tasks in the list.
```

---

### Listing All Tasks

View all your tasks at once.

```
list
```

**Output:**
```
 1. [T][ ] Buy groceries
 2. [D][ ] Submit essay (by: Oct 15 2025)
 3. [E][ ] Company retreat (from: Nov 15 2025 to: Nov 17 2025)
```

- **[T]** = Todo
- **[D]** = Deadline
- **[E]** = Event
- **[X]** = Task is done
- **[ ]** = Task is not done

---

### Marking Tasks as Done

Once you complete a task, mark it as done.

```
mark 1
```

**Output:**
```
Nice! I've marked this task as done:
[T][X] Buy groceries
```

---

### Unmarking Tasks

If you need to undo a completed task:

```
unmark 1
```

**Output:**
```
OK, I've marked this task as not done yet:
[T][ ] Buy groceries
```

---

### Deleting Tasks

Remove a task from your list.

```
delete 1
```

**Output:**
```
Noted. I've removed this task:
  [T][X] Buy groceries
Now you have 2 tasks in the list.
```

---

### Finding Tasks

Search for tasks by keyword. The search is case-insensitive.

```
find book
```

**Output:**
```
Here are the matching tasks in your list:
 1. [T][ ] Read book
 2. [D][ ] Return book (by: Dec 25 2025)
```

---

## Important Notes

### Date Format
Always use `yyyy-MM-dd` format for dates:
- ✅ Correct: `2025-10-15`
- ❌ Incorrect: `10/15/2025`, `October 15`

Dates will be displayed in a friendly format like "Oct 15 2025".

### Data Persistence
Chre automatically saves your tasks to a file. Your tasks will be loaded when you start Chre again.

### Exiting Chre
To close the application:

```
bye
```

---

## Tips & Tricks

- **Quick Search**: Use `find` to search by any part of the task name. For example, `find project` will find "Submit project", "Team project", etc.
- **Task Numbers Change**: When you delete a task, the numbers of remaining tasks may shift. Always check `list` to see current task numbers.
- **Careful with Dates**: Make sure deadlines and event dates are in `yyyy-MM-dd` format, or Chre will ask you to correct them.

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| "Invalid date format" error | Use `yyyy-MM-dd` format (e.g., `2025-10-15`) |
| Task number out of range | Run `list` to see current task numbers |
| Command not recognized | Check the Quick Reference table for correct command syntax |

---

Enjoy organizing your tasks with Chre! 🎉
