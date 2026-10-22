package wenwen.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import wenwen.exception.WenwenException;
import wenwen.task.Deadline;
import wenwen.task.Event;
import wenwen.task.Task;
import wenwen.task.Todo;

/**
 * Loads and saves the task list in a local data file.
 */
public class Storage {
    private static final String FIELD_SEPARATOR = "|";
    private static final String FIELD_SEPARATOR_REGEX = "\\|";
    private static final String TODO_CODE = "T";
    private static final String DEADLINE_CODE = "D";
    private static final String EVENT_CODE = "E";

    private final Path dataFile;

    /**
     * Creates storage that reads from and writes to the given path.
     *
     * @param dataFile The path of the task data file.
     */
    public Storage(Path dataFile) {
        this.dataFile = dataFile;
    }

    /**
     * Loads all tasks, returning an empty list when the data file does not exist yet.
     *
     * @return The tasks loaded from disk.
     * @throws WenwenException If the file cannot be read or contains invalid data.
     */
    public List<Task> loadTasks() throws WenwenException {
        if (Files.notExists(dataFile)) {
            return new ArrayList<>();
        }

        try {
            List<String> lines = Files.readAllLines(dataFile, StandardCharsets.UTF_8);
            List<Task> tasks = new ArrayList<>();
            for (int i = 0; i < lines.size(); i++) {
                tasks.add(parseTask(lines.get(i), i + 1));
            }
            return tasks;
        } catch (IOException exception) {
            throw new WenwenException("I couldn't read saved tasks from " + dataFile + ".");
        }
    }

    /**
     * Saves all tasks, creating the data folder when necessary.
     *
     * @param tasks The tasks to save.
     * @throws WenwenException If the data cannot be written.
     */
    public void saveTasks(List<Task> tasks) throws WenwenException {
        Path parent = dataFile.getParent();
        Path temporaryFile = dataFile.resolveSibling(dataFile.getFileName() + ".tmp");

        try {
            if (parent != null) {
                Files.createDirectories(parent);
            }
            List<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(formatTask(task));
            }
            Files.write(temporaryFile, lines, StandardCharsets.UTF_8);
            replaceDataFile(temporaryFile);
        } catch (IOException exception) {
            throw new WenwenException("I couldn't save tasks to " + dataFile + ".");
        }
    }

    /**
     * Replaces the data file atomically when the file system supports it.
     */
    private void replaceDataFile(Path temporaryFile) throws IOException {
        try {
            Files.move(temporaryFile, dataFile, StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException exception) {
            Files.move(temporaryFile, dataFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * Converts one task to a separator-safe storage record.
     */
    private String formatTask(Task task) throws WenwenException {
        String status = task.isDone() ? "1" : "0";
        String description = encode(task.getDescription());

        if (task instanceof Todo) {
            return String.join(FIELD_SEPARATOR, TODO_CODE, status, description);
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return String.join(FIELD_SEPARATOR, DEADLINE_CODE, status, description,
                    encode(deadline.getBy().toString()));
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return String.join(FIELD_SEPARATOR, EVENT_CODE, status, description,
                    encode(event.getFrom().toString()), encode(event.getTo().toString()));
        }

        throw new WenwenException("I couldn't save an unknown task type.");
    }

    /**
     * Parses one storage record and reports its line number when it is corrupted.
     */
    private Task parseTask(String line, int lineNumber) throws WenwenException {
        String[] fields = line.split(FIELD_SEPARATOR_REGEX, -1);

        try {
            if (fields.length < 3) {
                throw new IllegalArgumentException();
            }
            boolean isDone = parseStatus(fields[1]);
            String description = decode(fields[2]);

            switch (fields[0]) {
                case TODO_CODE:
                    requireFieldCount(fields, 3);
                    return new Todo(description, isDone);
                case DEADLINE_CODE:
                    requireFieldCount(fields, 4);
                    return new Deadline(description, LocalDate.parse(decode(fields[3])), isDone);
                case EVENT_CODE:
                    requireFieldCount(fields, 5);
                    return new Event(description, LocalDateTime.parse(decode(fields[3])),
                            LocalDateTime.parse(decode(fields[4])), isDone);
                default:
                    throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException exception) {
            throw new WenwenException("Saved task data is corrupted at line " + lineNumber + ".");
        }
    }

    /**
     * Converts a stored completion marker to a boolean.
     */
    private boolean parseStatus(String status) {
        if (status.equals("1")) {
            return true;
        } else if (status.equals("0")) {
            return false;
        }
        throw new IllegalArgumentException();
    }

    /**
     * Ensures a record contains exactly the fields required by its task type.
     */
    private void requireFieldCount(String[] fields, int expectedCount) {
        if (fields.length != expectedCount) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Encodes arbitrary user text so separators cannot corrupt the file format.
     */
    private String encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Decodes user text stored in a task record.
     */
    private String decode(String text) {
        byte[] bytes = Base64.getDecoder().decode(text);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
