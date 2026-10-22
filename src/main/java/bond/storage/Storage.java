package bond.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import bond.exception.StorageException;
import bond.task.Deadline;
import bond.task.Event;
import bond.task.Task;
import bond.task.TaskList;
import bond.task.Todo;

/**
 * Loads and saves task data in a file on the hard disk.
 */
public class Storage {

    private static final String FIELD_DELIMITER = " | ";
    private static final int MAX_STORED_FIELDS = 5;
    private static final int TODO_FIELD_COUNT = 3;
    private static final int DEADLINE_FIELD_COUNT = 4;
    private static final int EVENT_FIELD_COUNT = 5;
    private static final String TASK_TYPE_TODO = "T";
    private static final String TASK_TYPE_DEADLINE = "D";
    private static final String TASK_TYPE_EVENT = "E";
    private static final String STATUS_NOT_DONE = "0";
    private static final String STATUS_DONE = "1";

    private final String filePathText;
    private final Path filePath;

    /**
     * Creates storage that writes to the specified file path.
     *
     * @param filePath Relative path of the task data file.
     */
    public Storage(Path filePath) {
        if (filePath.isAbsolute()) {
            throw new IllegalArgumentException("Task data file path must be relative");
        }

        this.filePathText = "./" + filePath.toString().replace('\\', '/');
        this.filePath = filePath;
    }

    /**
     * Loads all tasks from the data file in their stored order.
     *
     * @return Loaded tasks, or an empty task list when no data file exists.
     * @throws StorageException If the data file cannot be read or contains invalid data.
     */
    public TaskList loadTasks() throws StorageException {
        TaskList taskList = new TaskList();
        if (!Files.exists(filePath)) {
            return taskList;
        }

        if (!Files.isRegularFile(filePath)) {
            throw createReadException(null);
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String taskData;
            int lineNumber = 0;
            while ((taskData = reader.readLine()) != null) {
                lineNumber++;
                Task task = parseTask(taskData, lineNumber);
                taskList.addTask(task);
            }
        } catch (IOException e) {
            throw createReadException(e);
        }
        return taskList;
    }

    /**
     * Writes every task to the data file, replacing its previous contents.
     *
     * @param taskList Tasks to write in their stored order.
     * @throws StorageException If the directory or data file cannot be written.
     */
    public void saveTasks(TaskList taskList) throws StorageException {
        Path parentDirectory = filePath.getParent();
        if (parentDirectory == null) {
            parentDirectory = Path.of(".");
        }
        Path temporaryFilePath = null;

        try {
            Files.createDirectories(parentDirectory);
            temporaryFilePath = Files.createTempFile(parentDirectory, "bond-", ".tmp");

            try (BufferedWriter writer = Files.newBufferedWriter(temporaryFilePath)) {
                for (int i = 0; i < taskList.getSize(); i++) {
                    writer.write(taskList.getTask(i).toDataString());
                    writer.newLine();
                }
            }

            replaceDataFile(temporaryFilePath, filePath);
            temporaryFilePath = null;
        } catch (IOException e) {
            throw new StorageException(
                    "I couldn't save the mission archive.",
                    "Check that " + filePathText + " is writable, then try again.",
                    e);
        } finally {
            deleteTemporaryFile(temporaryFilePath);
        }
    }

    /**
     * Converts one line of stored task data into its corresponding task.
     *
     * @param taskData Stored task data to parse.
     * @param lineNumber One-based line number in the data file.
     * @return Task represented by the stored data.
     * @throws StorageException If the stored data does not represent a valid task.
     */
    private Task parseTask(String taskData, int lineNumber) throws StorageException {
        if (taskData.isBlank()) {
            throw createInvalidDataException(lineNumber, "the line is empty", null);
        }

        String[] fields = splitDataFields(taskData);
        String taskType = fields[0];
        int expectedFieldCount = getExpectedFieldCount(taskType, lineNumber);
        if (fields.length != expectedFieldCount) {
            throw createInvalidDataException(
                    lineNumber, "the number of fields does not match the task type", null);
        }
        if (!fields[1].equals(STATUS_NOT_DONE) && !fields[1].equals(STATUS_DONE)) {
            throw createInvalidDataException(
                    lineNumber, "the completion status must be 0 or 1", null);
        }
        for (int i = 2; i < fields.length; i++) {
            if (fields[i].isBlank()) {
                throw createInvalidDataException(
                        lineNumber, "a required task field is empty", null);
            }
        }

        boolean isDone = fields[1].equals(STATUS_DONE);
        String description = fields[2];

        Task task;
        try {
            task = switch (taskType) {
                case TASK_TYPE_TODO -> new Todo(description);
                case TASK_TYPE_DEADLINE -> new Deadline(description, fields[3]);
                case TASK_TYPE_EVENT -> new Event(description, fields[3], fields[4]);
                default -> throw new IllegalStateException("Validated task type was not handled");
            };
        } catch (DateTimeParseException e) {
            throw createInvalidDataException(
                    lineNumber, "the deadline date must be a valid yyyy-MM-dd date", e);
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Returns the number of fields required by a stored task type.
     *
     * @param taskType Stored task type identifier.
     * @param lineNumber One-based line number in the data file.
     * @return Required field count for the task type.
     * @throws StorageException If the task type is unknown.
     */
    private int getExpectedFieldCount(String taskType, int lineNumber) throws StorageException {
        return switch (taskType) {
            case TASK_TYPE_TODO -> TODO_FIELD_COUNT;
            case TASK_TYPE_DEADLINE -> DEADLINE_FIELD_COUNT;
            case TASK_TYPE_EVENT -> EVENT_FIELD_COUNT;
            default -> throw createInvalidDataException(
                    lineNumber, "the task type '" + taskType + "' is unknown", null);
        };
    }

    /**
     * Splits stored data fields while decoding escaped pipes and backslashes.
     *
     * @param taskData Stored task line to split.
     * @return Decoded fields from the task line.
     */
    private String[] splitDataFields(String taskData) {
        String[] fields = new String[MAX_STORED_FIELDS + 1];
        StringBuilder field = new StringBuilder();
        int fieldCount = 0;

        for (int i = 0; i < taskData.length(); i++) {
            char currentCharacter = taskData.charAt(i);
            if (currentCharacter == '\\' && i + 1 < taskData.length()) {
                char nextCharacter = taskData.charAt(i + 1);
                if (nextCharacter == '\\' || nextCharacter == '|') {
                    field.append(nextCharacter);
                    i++;
                    continue;
                }
            }

            if (taskData.startsWith(FIELD_DELIMITER, i)) {
                if (fieldCount == MAX_STORED_FIELDS) {
                    fields[fieldCount] = field.toString();
                    return fields;
                }
                fields[fieldCount] = field.toString();
                fieldCount++;
                field.setLength(0);
                i += FIELD_DELIMITER.length() - 1;
            } else {
                field.append(currentCharacter);
            }
        }

        if (fieldCount == MAX_STORED_FIELDS) {
            fields[fieldCount] = field.toString();
            return fields;
        }
        fields[fieldCount] = field.toString();
        fieldCount++;
        return Arrays.copyOf(fields, fieldCount);
    }

    /**
     * Replaces the data file atomically when the file system supports it.
     *
     * @param temporaryFilePath Fully written temporary file.
     * @param dataFilePath Destination data file.
     * @throws IOException If the data file cannot be replaced.
     */
    private void replaceDataFile(Path temporaryFilePath, Path dataFilePath)
            throws IOException {
        try {
            Files.move(
                    temporaryFilePath,
                    dataFilePath,
                    StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(
                    temporaryFilePath,
                    dataFilePath,
                    StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * Deletes an unfinished temporary file without hiding the original save error.
     *
     * @param temporaryFilePath Temporary file to delete, or null if none remains.
     */
    private void deleteTemporaryFile(Path temporaryFilePath) {
        if (temporaryFilePath == null) {
            return;
        }

        try {
            Files.deleteIfExists(temporaryFilePath);
        } catch (IOException ignored) {
            // The save failure already gives the user the relevant recovery action.
        }
    }

    /**
     * Creates a user-facing error for invalid stored data.
     *
     * @param lineNumber One-based invalid line number.
     * @param reason Explanation of why the line is invalid.
     * @param cause Underlying cause, or null when validation found the error directly.
     * @return Error containing the archive repair instructions.
     */
    private StorageException createInvalidDataException(
            int lineNumber, String reason, Throwable cause) {
        String message = "Mission archive line " + lineNumber + " is invalid: " + reason + ".";
        String correction = "Repair or remove " + filePathText + ", then restart Bond.";
        if (cause == null) {
            return new StorageException(message, correction);
        }
        return new StorageException(message, correction, cause);
    }

    /**
     * Creates a user-facing error for an unreadable data file.
     *
     * @param cause Underlying read failure, or null if the path is not a file.
     * @return Error containing the archive access instructions.
     */
    private StorageException createReadException(Throwable cause) {
        String message = "I couldn't read the mission archive.";
        String correction = "Check that " + filePathText
                + " is a readable file, then restart Bond.";
        if (cause == null) {
            return new StorageException(message, correction);
        }
        return new StorageException(message, correction, cause);
    }
}
