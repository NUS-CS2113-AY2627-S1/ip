import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import task.Deadline;
import task.Event;
import task.Task;
import task.TaskList;
import task.ToDo;

/**
 * Reads and writes tasks using ATHENA's persistent file format.
 */
final class Storage {
    private static final Path DATA_FILE_PATH = Path.of("data", "athena.txt");
    private static final Path TEMPORARY_FILE_PATH = Path.of("data", "athena.txt.tmp");

    private Storage() {
    }

    /** Returns the tasks stored in the data file, or an empty list when no data file exists. */
    /**
     * Returns the tasks stored in the data file, or an empty list if the file does not exist.
     *
     * @return Tasks loaded from persistent storage.
     * @throws AthenaException If the data file cannot be read or contains invalid data.
     */
    static TaskList loadTasks() {
        if (Files.notExists(DATA_FILE_PATH)) {
            return new TaskList();
        }

        try {
            ArrayList<Task> loadedTasks = new ArrayList<>();
            ArrayList<String> lines = new ArrayList<>(
                    Files.readAllLines(DATA_FILE_PATH, StandardCharsets.UTF_8));
            for (int i = 0; i < lines.size(); i++) {
                if (!lines.get(i).isBlank()) {
                    loadedTasks.add(parseTask(lines.get(i), i + 1));
                }
            }
            return new TaskList(loadedTasks);
        } catch (IOException | SecurityException exception) {
            throw new AthenaException("Unable to load tasks from " + DATA_FILE_PATH, exception);
        }
    }

    /** Saves all tasks to the data file, replacing any previously stored tasks. */
    /**
     * Saves all tasks to the data file, replacing its previous contents.
     *
     * @param tasks Tasks to save.
     * @throws AthenaException If the tasks cannot be saved.
     */
    static void saveTasks(TaskList tasks) {
        ArrayList<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(task.toFileString());
        }

        try {
            Files.createDirectories(DATA_FILE_PATH.getParent());
            Files.write(TEMPORARY_FILE_PATH, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
            replaceDataFile();
        } catch (IOException | SecurityException exception) {
            deleteTemporaryFile(exception);
            throw new AthenaException("Unable to save tasks to " + DATA_FILE_PATH, exception);
        }
    }

    /** Replaces the data file with the completed temporary file as safely as the file system allows. */
    /**
     * Replaces the data file with the completed temporary file.
     *
     * @throws IOException If neither an atomic nor a regular replacement succeeds.
     */
    private static void replaceDataFile() throws IOException {
        try {
            Files.move(TEMPORARY_FILE_PATH, DATA_FILE_PATH,
                    StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException exception) {
            Files.move(TEMPORARY_FILE_PATH, DATA_FILE_PATH, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /** Deletes an incomplete temporary file and attaches any cleanup failure to the original exception. */
    /**
     * Deletes an incomplete temporary file after a failed save.
     * Any cleanup error is added to the original exception as a suppressed exception.
     *
     * @param originalException Exception that caused the save to fail.
     */
    private static void deleteTemporaryFile(Exception originalException) {
        try {
            Files.deleteIfExists(TEMPORARY_FILE_PATH);
        } catch (IOException | SecurityException cleanupException) {
            originalException.addSuppressed(cleanupException);
        }
    }

    /** Returns the task represented by one line of the persistent data file. */
    /**
     * Returns the task represented by one line of stored data.
     *
     * @param line Stored task data to parse.
     * @param lineNumber One-based line number used in error messages.
     * @return Task represented by the stored data.
     * @throws AthenaException If the stored data is invalid.
     */
    private static Task parseTask(String line, int lineNumber) {
        ArrayList<String> fields = splitFields(line);
        if (fields.size() < 3) {
            throw invalidData(lineNumber, "not enough fields");
        }

        boolean isDone = parseDoneState(fields.get(1), lineNumber);
        String description = requireText(fields.get(2), lineNumber, "description");
        Task task;
        switch (fields.get(0)) {
            case "T":
                requireFieldCount(fields, 3, lineNumber);
                task = new ToDo(description);
                break;
            case "D":
                requireFieldCount(fields, 4, lineNumber);
                task = new Deadline(description, requireText(fields.get(3), lineNumber, "deadline"));
                break;
            case "E":
                requireFieldCount(fields, 5, lineNumber);
                String from = requireText(fields.get(3), lineNumber, "start time");
                String to = requireText(fields.get(4), lineNumber, "end time");
                task = new Event(description, from, to);
                break;
            default:
                throw invalidData(lineNumber, "unknown task type");
        }
        task.setDone(isDone);
        return task;
    }

    /** Returns the unescaped fields from one line of the persistent data file. */
    /**
     * Returns the unescaped fields from one line of stored data.
     *
     * @param line Stored task data to split.
     * @return Fields extracted from the line.
     */
    private static ArrayList<String> splitFields(String line) {
        ArrayList<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean isEscaping = false;
        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);
            if (isEscaping) {
                if (character != '\\' && character != '|') {
                    field.append('\\');
                }
                field.append(character);
                isEscaping = false;
            } else if (character == '\\') {
                isEscaping = true;
            } else if (character == '|') {
                fields.add(field.toString().trim());
                field.setLength(0);
            } else {
                field.append(character);
            }
        }
        if (isEscaping) {
            field.append('\\');
        }
        fields.add(field.toString().trim());
        return fields;
    }

    /** Returns the completion state represented by a stored numeric value. */
    /**
     * Returns the completion state represented by a stored numeric value.
     *
     * @param value Stored completion value.
     * @param lineNumber One-based line number used in error messages.
     * @return {@code true} for {@code 1}, or {@code false} for {@code 0}.
     * @throws AthenaException If the value is neither {@code 0} nor {@code 1}.
     */
    private static boolean parseDoneState(String value, int lineNumber) {
        if (value.equals("1")) {
            return true;
        }
        if (value.equals("0")) {
            return false;
        }
        throw invalidData(lineNumber, "completion state must be 0 or 1");
    }

    /** Returns a required text field after verifying that it is not blank. */
    /**
     * Returns a required text field after verifying that it is not blank.
     *
     * @param value Field value to validate.
     * @param lineNumber One-based line number used in error messages.
     * @param fieldName Field name used in error messages.
     * @return Validated field value.
     * @throws AthenaException If the value is blank.
     */
    private static String requireText(String value, int lineNumber, String fieldName) {
        if (value.isBlank()) {
            throw invalidData(lineNumber, fieldName + " cannot be blank");
        }
        return value;
    }

    /** Verifies that a stored task contains the expected number of fields. */
    /**
     * Verifies that a stored task contains the expected number of fields.
     *
     * @param fields Fields belonging to the stored task.
     * @param expectedCount Required number of fields.
     * @param lineNumber One-based line number used in error messages.
     * @throws AthenaException If the field count differs from the expected count.
     */
    private static void requireFieldCount(ArrayList<String> fields, int expectedCount, int lineNumber) {
        if (fields.size() != expectedCount) {
            throw invalidData(lineNumber, "incorrect number of fields");
        }
    }

    /** Returns an exception that identifies an invalid line in the data file. */
    /**
     * Returns an exception describing invalid data at a particular file line.
     *
     * @param lineNumber One-based line number containing the invalid data.
     * @param reason Explanation of why the data is invalid.
     * @return Exception containing the data file, line number, and reason.
     */
    private static AthenaException invalidData(int lineNumber, String reason) {
        return new AthenaException("Invalid data in " + DATA_FILE_PATH + " at line " + lineNumber + ": " + reason);
    }
}
