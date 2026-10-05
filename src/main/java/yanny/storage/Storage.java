package yanny.storage;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import yanny.task.Deadline;
import yanny.task.DeadlineDateParser;
import yanny.task.Event;
import yanny.task.LegacyDeadline;
import yanny.task.Task;
import yanny.task.Todo;

/**
 * Reads and writes Yanny's task list in a local data file.
 */
public class Storage {
    private static final String DATA_FILE_PROPERTY = "yanny.data.file";
    private static final Path DEFAULT_DATA_FILE = Path.of("data", "yanny.txt");
    private final Path dataFile;

    /** Creates storage using the default path or a configured test path. */
    public Storage() {
        String configuredPath;
        try {
            configuredPath = System.getProperty(DATA_FILE_PROPERTY);
        } catch (SecurityException exception) {
            throw new IllegalArgumentException("The configured task data path is inaccessible.", exception);
        }
        if (configuredPath == null || configuredPath.isBlank()) {
            dataFile = DEFAULT_DATA_FILE;
            return;
        }

        try {
            dataFile = Path.of(configuredPath);
        } catch (InvalidPathException | SecurityException exception) {
            throw new IllegalArgumentException("The configured task data path is invalid.", exception);
        }
    }

    /**
     * Writes all supplied tasks to the data file, replacing its previous contents.
     *
     * @param tasks the tasks to serialize.
     * @throws IOException if the data directory or file cannot be written.
     */
    public void saveTasks(List<Task> tasks) throws IOException {
        if (tasks == null) {
            throw new IllegalArgumentException("The task list cannot be null.");
        }

        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(serializeTask(task));
        }

        Path dataDirectory = getDataDirectory();
        Files.createDirectories(dataDirectory);
        Path temporaryFile = Files.createTempFile(dataDirectory, "yanny-tasks-", ".tmp");
        try {
            Files.write(temporaryFile, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
            moveIntoPlace(temporaryFile);
        } finally {
            Files.deleteIfExists(temporaryFile);
        }
    }

    /**
     * Loads all tasks from the data file.
     *
     * @return the tasks stored in the file, or an empty list when it does not exist.
     * @throws IOException if the existing data file cannot be read.
     * @throws IllegalArgumentException if a stored line is not in the expected format.
     */
    public List<Task> loadTasks() throws IOException {
        if (Files.notExists(dataFile)) {
            return new ArrayList<>();
        }
        if (!Files.isRegularFile(dataFile)) {
            throw new IOException("The task data path is not a regular file.");
        }

        List<Task> tasks = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(dataFile, StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (!line.isBlank()) {
                    try {
                        tasks.add(parseTask(line));
                    } catch (IllegalArgumentException exception) {
                        throw new IllegalArgumentException("Invalid task data on line " + lineNumber
                                + ": " + exception.getMessage(), exception);
                    }
                }
            }
        }
        return tasks;
    }

    /**
     * Converts one task to its line-based storage representation.
     *
     * @param task the task to serialize.
     * @return the serialized task line.
     */
    private String serializeTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("The task list cannot contain null tasks.");
        }

        String status = task.isDone() ? "1" : "0";
        String description = requireStorageValue(task.getDescription(), "description");
        if (task instanceof Deadline deadline) {
            String storedDeadline = deadline.getTime()
                    .map(time -> LocalDateTime.of(deadline.getDate(), time).toString())
                    .orElseGet(() -> deadline.getDate().toString());
            return "D | " + status + " | " + description
                    + " | " + requireStorageValue(storedDeadline, "deadline");
        }
        if (task instanceof LegacyDeadline deadline) {
            return "D | " + status + " | " + description
                    + " | " + requireStorageValue(deadline.getDeadlineText(), "deadline");
        }
        if (task instanceof Event event) {
            return "E | " + status + " | " + description
                    + " | " + requireStorageValue(event.getStart(), "event start")
                    + " | " + requireStorageValue(event.getEnd(), "event end");
        }
        return "T | " + status + " | " + description;
    }

    /** Returns the directory in which temporary and final data files are stored. */
    private Path getDataDirectory() {
        Path absoluteDataFile = dataFile.toAbsolutePath().normalize();
        Path fileName = absoluteDataFile.getFileName();
        if (fileName == null || fileName.toString().isBlank()) {
            throw new IllegalArgumentException("The task data path must name a file.");
        }
        Path dataDirectory = absoluteDataFile.getParent();
        if (dataDirectory == null) {
            throw new IllegalArgumentException("The task data path must have a parent directory.");
        }
        return dataDirectory;
    }

    /** Replaces the target file atomically where the file system supports it. */
    private void moveIntoPlace(Path temporaryFile) throws IOException {
        try {
            Files.move(temporaryFile, dataFile, StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException exception) {
            Files.move(temporaryFile, dataFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * Parses one line from the task data file.
     *
     * @param line the serialized task line.
     * @return the parsed task.
     */
    private Task parseTask(String line) {
        String[] fields = line.split("\\s*\\|\\s*", -1);
        if (fields.length < 3) {
            throw new IllegalArgumentException("A task record must contain a type, status, and description.");
        }

        String type = fields[0].trim();
        String status = fields[1].trim();
        Task task;
        switch (type) {
        case "T" -> {
            requireFieldCount(fields, 3, type);
            task = new Todo(requireValue(fields[2], "description"));
        }
        case "D" -> {
            requireFieldCount(fields, 4, type);
            String description = requireValue(fields[2], "description");
            String deadlineValue = requireValue(fields[3], "deadline");
            try {
                task = DeadlineDateParser.parseStored(description, deadlineValue);
            } catch (IllegalArgumentException exception) {
                task = new LegacyDeadline(description, deadlineValue);
            }
        }
        case "E" -> {
            requireFieldCount(fields, 5, type);
            task = new Event(requireValue(fields[2], "description"),
                    requireValue(fields[3], "event start"), requireValue(fields[4], "event end"));
        }
        default -> throw new IllegalArgumentException("Unknown task type: " + type);
        }

        if ("1".equals(status)) {
            task.markAsDone();
        } else if (!"0".equals(status)) {
            throw new IllegalArgumentException("Task status must be 0 or 1.");
        }
        return task;
    }

    /** Validates the number of fields required by a task type. */
    private void requireFieldCount(String[] fields, int expectedCount, String type) {
        if (fields.length != expectedCount) {
            throw new IllegalArgumentException(type + " records contain the wrong number of fields.");
        }
    }

    /** Validates and returns a required stored value. */
    private String requireValue(String value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException("Task " + fieldName + " cannot be null.");
        }
        String trimmedValue = value.trim();
        if (trimmedValue.isBlank()) {
            throw new IllegalArgumentException("Task " + fieldName + " cannot be empty.");
        }
        if (trimmedValue.indexOf('|') >= 0 || trimmedValue.indexOf('\u0000') >= 0
                || trimmedValue.indexOf('\n') >= 0 || trimmedValue.indexOf('\r') >= 0) {
            throw new IllegalArgumentException("Task " + fieldName + " contains an unsupported character.");
        }
        return trimmedValue;
    }

    /** Validates a task value before serializing it into the delimiter-based format. */
    private String requireStorageValue(String value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException("Task " + fieldName + " cannot be null.");
        }
        return requireValue(value, fieldName);
    }
}
