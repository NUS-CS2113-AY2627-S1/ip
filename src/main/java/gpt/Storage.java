package gpt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Loads tasks from and saves tasks to the hard disk.
 */
public class Storage {
    private static final Path FILE_PATH = Path.of("data", "gpt.txt");
    private static final String FIELD_SEPARATOR = " | ";

    /**
     * Loads tasks from the save file, returning an empty list if the file does not exist.
     */
    public TaskList loadTasks() throws GptException {
        TaskList tasks = new TaskList();
        if (!Files.exists(FILE_PATH)) {
            return tasks;
        }

        try {
            for (String line : Files.readAllLines(FILE_PATH, StandardCharsets.UTF_8)) {
                Task task = parseTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            throw new GptException("OOPS!!! I could not load your saved tasks.");
        }
        return tasks;
    }

    /**
     * Saves the given tasks to the save file, creating the data folder if needed.
     */
    public void saveTasks(TaskList tasks) throws GptException {
        try {
            Path parent = FILE_PATH.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            ArrayList<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(formatTask(task));
            }
            Files.write(FILE_PATH, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new GptException("OOPS!!! I could not save your tasks.");
        }
    }

    /**
     * Returns the task represented by a saved line, or null if the line is corrupted.
     */
    private Task parseTask(String line) {
        String[] parts = line.split(" \\| ", -1);
        if (parts.length < 3) {
            return null;
        }
        if (!parts[1].equals("0") && !parts[1].equals("1")) {
            return null;
        }

        Task task;
        switch (parts[0]) {
        case "T":
            task = parseTodo(parts);
            break;
        case "D":
            task = parseDeadline(parts);
            break;
        case "E":
            task = parseEvent(parts);
            break;
        default:
            task = null;
        }

        if (task != null && parts[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    private Task parseTodo(String[] parts) {
        if (parts.length != 3 || parts[2].isBlank()) {
            return null;
        }
        return new Todo(parts[2]);
    }

    private Task parseDeadline(String[] parts) {
        if (parts.length != 4 || parts[2].isBlank() || parts[3].isBlank()) {
            return null;
        }
        try {
            return new Deadline(parts[2], LocalDate.parse(parts[3]));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private Task parseEvent(String[] parts) {
        if (parts.length != 5 || parts[2].isBlank() || parts[3].isBlank() || parts[4].isBlank()) {
            return null;
        }
        return new Event(parts[2], parts[3], parts[4]);
    }

    /**
     * Returns the save-file line for a task.
     */
    private String formatTask(Task task) {
        String status = task.isDone() ? "1" : "0";
        if (task instanceof Todo) {
            return "T" + FIELD_SEPARATOR + status + FIELD_SEPARATOR + task.getDescription();
        } else if (task instanceof Deadline deadline) {
            return "D" + FIELD_SEPARATOR + status + FIELD_SEPARATOR + deadline.getDescription()
                    + FIELD_SEPARATOR + deadline.getBy();
        } else if (task instanceof Event event) {
            return "E" + FIELD_SEPARATOR + status + FIELD_SEPARATOR + event.getDescription()
                    + FIELD_SEPARATOR + event.getFrom() + FIELD_SEPARATOR + event.getTo();
        } else {
            throw new IllegalArgumentException("Unsupported task type.");
        }
    }
}
