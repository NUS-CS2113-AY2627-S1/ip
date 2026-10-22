package aigis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import aigis.task.Deadline;
import aigis.task.Event;
import aigis.task.Task;
import aigis.task.Todo;

/**
 * Handles saving and loading Aigis tasks from the local data file.
 */
public final class Storage {
    private static final String RECORD_SEPARATOR = " / ";
    private final Path dataFile;

    /**
     * Creates storage backed by the specified file path.
     *
     * @param filePath The path of the file used to save and load tasks.
     */
    public Storage(String filePath) {
        dataFile = Path.of(filePath).toAbsolutePath().normalize();
    }

    /**
     * Saves the current tasks, replacing the previous contents of the data file.
     *
     * @param tasks The task list to save.
     */
    public void save(TaskList tasks) {
        List<String> records = serializeTasks(tasks);
        if (records == null) {
            return;
        }
        Path temporaryFile = null;
        try {
            temporaryFile = writeTemporaryFile(records);
            replaceDataFile(temporaryFile);
        } catch (IOException exception) {
            reportStorageError("Could not save tasks: " + exception.getMessage());
        } catch (SecurityException exception) {
            reportStorageError("Could not save tasks: access to the data file was denied.");
        } finally {
            if (temporaryFile != null) {
                try {
                    Files.deleteIfExists(temporaryFile);
                } catch (IOException | SecurityException exception) {
                    // The completed save is still usable even if temporary-file cleanup fails.
                }
            }
        }
    }

    /**
     * Converts every task into a validated storage record.
     *
     * @param tasks The task list to serialize.
     * @return The serialized records, or {@code null} when a task is invalid.
     */
    private static List<String> serializeTasks(TaskList tasks) {
        if (tasks == null) {
            reportStorageError("Could not save tasks: task storage is null.");
            return null;
        }

        List<String> records = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task == null) {
                reportStorageError("Could not save tasks: task " + (i + 1) + " is null.");
                return null;
            }
            String record = task.toStorageString();
            if (record == null || record.isBlank() || record.indexOf('\n') >= 0
                    || record.indexOf('\r') >= 0) {
                reportStorageError("Could not save tasks: task " + (i + 1)
                        + " has invalid storage data.");
                return null;
            }
            records.add(record);
        }
        return records;
    }

    /**
     * Writes serialized task records to a temporary file in the data directory.
     *
     * @param records The records to write.
     * @return The temporary file containing the records.
     * @throws IOException If the directory or temporary file cannot be created.
     */
    private Path writeTemporaryFile(List<String> records) throws IOException {
        Path dataDirectory = dataFile.getParent();
        Files.createDirectories(dataDirectory);
        Path temporaryFile = Files.createTempFile(dataDirectory, "aigis-", ".tmp");
        try (BufferedWriter writer = Files.newBufferedWriter(temporaryFile,
                StandardCharsets.UTF_8)) {
            for (String record : records) {
                writer.write(record);
                writer.newLine();
            }
        }
        return temporaryFile;
    }

    /**
     * Replaces the data file with the completed temporary file.
     *
     * @param temporaryFile The temporary file to move into place.
     * @throws IOException If the data file cannot be replaced.
     */
    private void replaceDataFile(Path temporaryFile) throws IOException {
        try {
            Files.move(temporaryFile, dataFile,
                    StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException exception) {
            Files.move(temporaryFile, dataFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * Loads saved tasks from the data file.
     *
     * @return A task list containing all valid tasks that were loaded.
     */
    public TaskList load() {
        TaskList tasks = new TaskList();

        try {
            if (!Files.exists(dataFile)) {
                return tasks;
            }
            if (!Files.isRegularFile(dataFile)) {
                reportStorageError("Could not load tasks: the data path is not a file.");
                return tasks;
            }
        } catch (SecurityException exception) {
            reportStorageError("Could not load tasks: access to the data file was denied.");
            return tasks;
        }

        try (BufferedReader reader = Files.newBufferedReader(dataFile, StandardCharsets.UTF_8)) {
            String line;
            while (!tasks.isFull() && (line = reader.readLine()) != null) {
                Task task = parseTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException exception) {
            reportStorageError("Could not load tasks: " + exception.getMessage());
        } catch (SecurityException exception) {
            reportStorageError("Could not load tasks: access to the data file was denied.");
        }
        return tasks;
    }

    /**
     * Parses one serialized task line.
     *
     * @param line The line read from the data file.
     * @return The parsed task, or {@code null} if the line is malformed.
     */
    private static Task parseTask(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }

        if (line.charAt(0) == '\ufeff') {
            line = line.substring(1);
        }

        int firstSeparator = line.indexOf(RECORD_SEPARATOR);
        int secondSeparator = firstSeparator < 0
                ? -1 : line.indexOf(RECORD_SEPARATOR, firstSeparator + RECORD_SEPARATOR.length());
        if (firstSeparator <= 0 || secondSeparator <= firstSeparator + RECORD_SEPARATOR.length()) {
            return null;
        }

        String type = line.substring(0, firstSeparator).trim();
        String status = line.substring(firstSeparator + RECORD_SEPARATOR.length(), secondSeparator).trim();
        String details = line.substring(secondSeparator + RECORD_SEPARATOR.length()).trim();
        if ((!status.equals("0") && !status.equals("1")) || details.isEmpty()) {
            return null;
        }

        try {
            Task task = createTask(type, details);
            task.setDone(status.equals("1"));
            return task;
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    /**
     * Creates a task from its serialized type and details.
     *
     * @param type The serialized task type.
     * @param details The serialized task details.
     * @return The created task.
     * @throws IllegalArgumentException If the serialized details are malformed.
     */
    private static Task createTask(String type, String details) {
        if (type.equals("T")) {
            return new Todo(details);
        }
        if (type.equals("Task")) {
            return new Task(details);
        }
        int openingParenthesis = details.lastIndexOf('(');
        if (openingParenthesis <= 0 || !details.endsWith(")")) {
            throw new IllegalArgumentException("Malformed task details.");
        }

        String description = details.substring(0, openingParenthesis).trim();
        String timing = details.substring(openingParenthesis + 1, details.length() - 1).trim();
        if (type.equals("D")) {
            try {
                LocalDate dueDate = DateFormats.parseDate(timing);
                return new Deadline(description, dueDate);
            } catch (DateTimeParseException exception) {
                throw new IllegalArgumentException("Malformed deadline date.", exception);
            }
        }
        if (type.equals("E")) {
            int separator = timing.lastIndexOf(' ');
            if (separator <= 0 || separator == timing.length() - 1) {
                throw new IllegalArgumentException("Malformed event details.");
            }
            String from = timing.substring(0, separator).trim();
            String till = timing.substring(separator + 1).trim();
            return new Event(description, from, till);
        }
        throw new IllegalArgumentException("Unknown task type.");
    }

    /** Reports a storage problem without allowing it to terminate the command loop. */
    private static void reportStorageError(String message) {
        System.err.println(message);
    }
}
