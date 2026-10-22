package bob.storage;

import bob.exception.LoadingError;
import bob.exception.SavingError;
import bob.task.Deadline;
import bob.task.Event;
import bob.task.Task;
import bob.task.TaskList;
import bob.task.Todo;
import bob.ui.Ui;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads and saves tasks using a configured file on disk.
 */
public class Storage {
    private final Path taskFile;

    /**
     * Creates storage for the supplied file path.
     *
     * @param filePath path of the task file, relative to the working directory or absolute.
     */
    public Storage(String filePath) {
        taskFile = Path.of(filePath);
    }

    /**
     * Saves all tasks to the configured task file.
     *
     * @param tasks tasks to save.
     * @throws SavingError if the directory or file cannot be written.
     */
    public void save(TaskList tasks) throws SavingError {
        validateTaskList(tasks);
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            lines.add(formatTask(tasks.get(i)));
        }

        try {
            Path parent = taskFile.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.write(taskFile, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE);
        } catch (IOException | SecurityException e) {
            throw new SavingError(e);
        }
    }

    /**
     * Loads saved tasks, reporting a failure and using an empty list if loading fails.
     *
     * @param ui interface used to display the loading-error message.
     * @return loaded tasks, or an empty list if the file is missing or cannot be read.
     */
    public TaskList loadTasks(Ui ui) {
        try {
            return load();
        } catch (LoadingError e) {
            ui.showError(e.getMessage());
            return new TaskList();
        }
    }

    /**
     * Loads saved tasks, skipping malformed records.
     *
     * @return loaded tasks, or an empty list if the file does not exist.
     * @throws LoadingError if the task file cannot be read.
     */
    public TaskList load() throws LoadingError {
        try {
            if (!Files.exists(taskFile)) {
                return new TaskList();
            }
            if (!Files.isRegularFile(taskFile)) {
                throw new IOException("Task path is not a regular file");
            }

            List<Task> tasks = new ArrayList<>();
            for (String line : Files.readAllLines(taskFile, StandardCharsets.UTF_8)) {
                Task task = parseTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
            return new TaskList(tasks);
        } catch (IOException | SecurityException e) {
            throw new LoadingError(e);
        }
    }

    /**
     * Checks that the list and its task entries are non-null before saving.
     */
    private static void validateTaskList(TaskList tasks) {
        if (tasks == null) {
            throw new IllegalArgumentException("Task array cannot be null");
        }
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i) == null) {
                throw new IllegalArgumentException("Task list contains a null task");
            }
        }
    }

    /**
     * Parses a saved record, returning null for malformed records and restoring completion status.
     */
    private static Task parseTask(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }
        String[] parts = line.split("\\s*\\|\\s*", -1);
        if (parts.length < 3 || parts[2].isBlank()
                || (!parts[1].equals("0") && !parts[1].equals("1"))) {
            return null;
        }

        Task task;
        switch (parts[0]) {
            case "D":
                task = parts.length == 4 && !parts[3].isBlank() ? new Deadline(parts[2], parts[3]) : null;
                break;
            case "E":
                task = parts.length == 5 ? new Event(parts[2], parts[3], parts[4]) : null;
                break;
            case "T":
                task = parts.length == 3 ? new Todo(parts[2]) : null;
                break;
            default:
                task = null;
                break;
        }

        if (task != null && parts[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Formats a task as a saved record containing its type, completion status, and details.
     */
    private static String formatTask(Task task) {
        String status = task.isDone() ? "1" : "0";
        if (task instanceof Deadline deadline) {
            return "D | " + status + " | " + task.getDescription() + " | " + deadline.getBy();
        }
        if (task instanceof Event event) {
            return "E | " + status + " | " + task.getDescription()
                    + " | " + event.getFrom() + " | " + event.getTo();
        }
        return "T | " + status + " | " + task.getDescription();
    }
}
