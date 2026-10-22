package bill.storage;

import bill.exception.BillException;
import bill.task.Deadline;
import bill.task.Event;
import bill.task.Task;
import bill.task.Todo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads and saves tasks from Bill's local data file.
 */
public class Storage {
    private static final String FIELD_SEPARATOR = " | ";
    private static final String FIELD_SEPARATOR_REGEX = "\\s*\\|\\s*";
    private static final Path DEFAULT_FILE_PATH = Path.of("data", "bill.txt");

    private final Path filePath;

    /**
     * Creates storage that uses Bill's default data file.
     */
    public Storage() {
        this(DEFAULT_FILE_PATH);
    }

    /**
     * Creates storage that uses the given data file.
     *
     * @param filePath Path to the data file.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the data file. A missing file is treated as an empty list.
     *
     * @return Tasks loaded from disk.
     * @throws BillException If the file exists but cannot be read or parsed.
     */
    public List<Task> loadTasks() throws BillException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        try {
            List<Task> tasks = new ArrayList<>();
            List<String> lines = Files.readAllLines(filePath);
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (!line.isBlank()) {
                    tasks.add(parseTask(line, i + 1));
                }
            }
            return tasks;
        } catch (IOException exception) {
            throw new BillException("I couldn't read saved tasks from " + filePath + ".");
        }
    }

    /**
     * Saves tasks to the data file.
     *
     * @param tasks Tasks to save.
     * @throws BillException If the tasks cannot be written to disk.
     */
    public void saveTasks(List<Task> tasks) throws BillException {
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            List<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(formatTask(task));
            }
            Files.write(filePath, lines);
        } catch (IOException exception) {
            throw new BillException("I couldn't save your tasks to " + filePath + ".");
        }
    }

    private Task parseTask(String line, int lineNumber) throws BillException {
        String[] fields = line.split(FIELD_SEPARATOR_REGEX, -1);
        if (fields.length < 3) {
            throw new BillException("Saved task line " + lineNumber + " is incomplete.");
        }

        String taskType = fields[0];
        boolean isDone = parseDoneStatus(fields[1], lineNumber);
        String description = fields[2];

        switch (taskType) {
        case "T":
            requireFieldCount(fields, 3, lineNumber, "todo");
            return new Todo(description, isDone);
        case "D":
            requireFieldCount(fields, 4, lineNumber, "deadline");
            return new Deadline(description, fields[3], isDone);
        case "E":
            requireFieldCount(fields, 5, lineNumber, "event");
            return new Event(description, fields[3], fields[4], isDone);
        default:
            throw new BillException("Saved task line " + lineNumber + " has an unknown task type.");
        }
    }

    private boolean parseDoneStatus(String status, int lineNumber) throws BillException {
        if ("1".equals(status)) {
            return true;
        }
        if ("0".equals(status)) {
            return false;
        }
        throw new BillException("Saved task line " + lineNumber + " has an invalid done status.");
    }

    private void requireFieldCount(String[] fields, int expectedCount, int lineNumber, String taskType)
            throws BillException {
        if (fields.length != expectedCount) {
            throw new BillException("Saved task line " + lineNumber + " is not a valid " + taskType + ".");
        }
    }

    private String formatTask(Task task) {
        String doneStatus = task.isDone() ? "1" : "0";

        if (task instanceof Todo) {
            return String.join(FIELD_SEPARATOR, "T", doneStatus, task.getDescription());
        }
        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return String.join(FIELD_SEPARATOR, "D", doneStatus, deadline.getDescription(), deadline.getBy());
        }

        Event event = (Event) task;
        return String.join(FIELD_SEPARATOR, "E", doneStatus, event.getDescription(), event.getFrom(), event.getTo());
    }
}
