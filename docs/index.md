# Exia

Exia is a command-line task manager that helps you record, manage, search, and save your tasks.

## Quick Start

1. Install Java 25.
2. Download `Exia.jar` from the [latest release](https://github.com/JamesZ1110/ip/releases).
3. Place the JAR file in an empty folder.
4. Open a terminal in that folder.
5. Run:

   ```text
   java -jar Exia.jar
   ```

Exia automatically saves your tasks in `data/exia.txt`.

## Commands

### Add a todo

```text
todo DESCRIPTION
```

Example:

```text
todo read book
```

### Add a deadline

```text
deadline DESCRIPTION /by TIME
```

Example:

```text
deadline return book /by Sunday
```

### Add an event

```text
event DESCRIPTION /from START /to END
```

Example:

```text
event project meeting /from Monday 2pm /to 4pm
```

### List tasks

```text
list
```

### Find tasks

```text
find KEYWORD
```

Example:

```text
find book
```

The search is not case-sensitive.

### Mark a task as done

```text
done TASK_NUMBER
```

Example:

```text
done 2
```

### Delete a task

```text
delete TASK_NUMBER
```

Example:

```text
delete 2
```

### Exit

```text
bye
```

Your tasks are saved automatically when you add, complete, or delete a task.
