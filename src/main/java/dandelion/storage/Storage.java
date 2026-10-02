package dandelion.storage;

import dandelion.task.Deadline;
import dandelion.task.Event;
import dandelion.task.Task;
import dandelion.task.TaskList;
import dandelion.task.Todo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores tasks in and loads tasks from a local text file.
 */
public class Storage {
    /** Location of the task data file, relative to the project root. */
    private static final Path FILE_PATH = Path.of("data", "dandelion.txt");

    /**
     * Creates storage for the default task data file.
     */
    public Storage() {
    }

    /**
     * Loads all saved tasks from the data file.
     *
     * @return Loaded tasks, or an empty list when no data file exists.
     * @throws IOException If the data file cannot be read.
     * @throws IllegalArgumentException If a saved task has an invalid format.
     */
    public List<Task> loadTasks() throws IOException {
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(FILE_PATH)) {
            return tasks;
        }

        for (String line : Files.readAllLines(FILE_PATH, StandardCharsets.UTF_8)) {
            if (!line.isBlank()) {
                tasks.add(convertLineToTask(line));
            }
        }
        return tasks;
    }

    /**
     * Saves a task list to the data file.
     *
     * @param tasks Task list to save.
     * @throws IOException If the data directory or file cannot be written.
     */
    public void saveTasks(TaskList tasks) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(convertTaskToLine(task));
        }

        Files.createDirectories(FILE_PATH.getParent());
        Files.write(FILE_PATH, lines, StandardCharsets.UTF_8);
    }

    /**
     * Converts one task to its corresponding saved-file line.
     *
     * @param task Task to convert.
     * @return Saved-file representation of {@code task}.
     */
    private String convertTaskToLine(Task task) {
        String status = task.isDone() ? "1" : "0";
        if (task instanceof Todo) {
            return "T | " + status + " | " + task.getDescription();
        }
        if (task instanceof Deadline deadline) {
            return "D | " + status + " | " + deadline.getDescription()
                    + " | " + deadline.getBy();
        }
        if (task instanceof Event event) {
            return "E | " + status + " | " + event.getDescription()
                    + " | " + event.getFrom() + " | " + event.getTo();
        }
        throw new IllegalArgumentException("Cannot save an unknown task type.");
    }

    /**
     * Converts one saved-file line to a task.
     *
     * @param line Saved-file line to convert.
     * @return Task represented by {@code line}.
     * @throws IllegalArgumentException If {@code line} has an invalid format.
     */
    private Task convertLineToTask(String line) {
        String[] fields = line.split("\\|", -1);
        String taskType = fields[0].trim();
        Task task = switch (taskType) {
        case "T" -> {
            validateFieldCount(fields, 3, taskType);
            yield new Todo(fields[2].trim());
        }
        case "D" -> {
            validateFieldCount(fields, 4, taskType);
            yield new Deadline(fields[2].trim(), parseDeadlineDate(fields[3].trim()));
        }
        case "E" -> {
            validateFieldCount(fields, 5, taskType);
            yield new Event(fields[2].trim(), fields[3].trim(), fields[4].trim());
        }
        default -> throw new IllegalArgumentException("Unknown saved task type: " + taskType + ".");
        };

        String status = fields[1].trim();
        if (status.equals("1")) {
            task.markAsDone();
        } else if (!status.equals("0")) {
            throw new IllegalArgumentException("Saved task status must be 0 or 1.");
        }
        return task;
    }

    /**
     * Parses a saved deadline date in ISO local-date format.
     *
     * @param dateText Saved deadline date.
     * @return Parsed deadline date.
     * @throws IllegalArgumentException If the date is not in {@code yyyy-MM-dd} format.
     */
    private LocalDate parseDeadlineDate(String dateText) {
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Saved deadline must use yyyy-MM-dd format.", exception);
        }
    }

    /**
     * Verifies that a saved task line contains the expected number of fields.
     *
     * @param fields Fields extracted from a saved-file line.
     * @param expectedCount Expected number of fields.
     * @param taskType Type code from the saved-file line.
     * @throws IllegalArgumentException If the field count is incorrect.
     */
    private void validateFieldCount(String[] fields, int expectedCount, String taskType) {
        if (fields.length != expectedCount) {
            throw new IllegalArgumentException("Saved " + taskType + " task has an invalid format.");
        }
    }
}
