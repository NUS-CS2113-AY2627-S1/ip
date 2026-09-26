package mimimeow.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import mimimeow.task.Deadline;
import mimimeow.task.Event;
import mimimeow.task.InvalidTaskException;
import mimimeow.task.Task;
import mimimeow.task.TaskList;
import mimimeow.task.Todo;

/** Saves and loads MimiMeow's task data on the hard disk. */
public class Storage {
    private final Path filePath;
    private boolean canSave = true;

    /**
     * Creates storage that writes task data to the specified path.
     *
     * @param filePath path of the task data file
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads all tasks from the data file, or returns an empty list when the file does not exist.
     *
     * @return loaded task list
     */
    public TaskList load() {
        TaskList taskList = new TaskList();
        try {
            if (Files.notExists(filePath)) {
                return taskList;
            }

            List<String> taskLines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            for (int i = 0; i < taskLines.size(); i++) {
                String taskLine = taskLines.get(i);
                if (!taskLine.isBlank()) {
                    try {
                        taskList.add(parseTask(taskLine, i + 1));
                    } catch (InvalidTaskException exception) {
                        throw createCorruptedDataException(i + 1, exception.getMessage());
                    }
                }
            }
            return taskList;
        } catch (DataFormatException exception) {
            canSave = false;
            throw exception;
        } catch (IOException | SecurityException exception) {
            canSave = false;
            throw new StorageException("Mimi could not read the saved tasks.", exception);
        }
    }

    /**
     * Saves all tasks, replacing the previous contents of the data file.
     *
     * @param taskList task list to save
     */
    public void save(TaskList taskList) {
        if (!canSave) {
            throw new StorageException("Mimi cannot save changes because the existing data could not be loaded. "
                    + "Fix the data file and restart MimiMeow first.");
        }

        ArrayList<String> taskLines = new ArrayList<>();
        for (int i = 0; i < taskList.size(); i++) {
            taskLines.add(taskList.get(i).toFileString());
        }

        Path temporaryFile = filePath.resolveSibling(filePath.getFileName() + ".tmp");
        try {
            Path parentDirectory = filePath.getParent();
            if (parentDirectory != null) {
                Files.createDirectories(parentDirectory);
            }
            Files.write(temporaryFile, taskLines, StandardCharsets.UTF_8);
            replaceDataFile(temporaryFile);
        } catch (IOException | SecurityException exception) {
            deleteTemporaryFile(temporaryFile);
            throw new StorageException("Mimi could not save the task list.", exception);
        }
    }

    /** Converts one line from the data file into its corresponding task. */
    private Task parseTask(String taskLine, int lineNumber) {
        List<String> taskFields = parseFields(taskLine);
        if (taskFields.size() < 2) {
            throw createCorruptedDataException(lineNumber, "task type or status is missing");
        }

        String taskType = taskFields.get(0);
        validateFieldCount(taskType, taskFields.size(), lineNumber);
        validateStatus(taskFields.get(1), lineNumber);
        validateRequiredFields(taskFields, lineNumber);

        Task task;
        switch (taskType) {
        case "T":
            task = new Todo(taskFields.get(2));
            break;
        case "D":
            task = new Deadline(taskFields.get(2), taskFields.get(3));
            break;
        case "E":
            task = new Event(taskFields.get(2), taskFields.get(3), taskFields.get(4));
            break;
        default:
            throw createCorruptedDataException(lineNumber, "task type must be T, D, or E");
        }

        if (taskFields.get(1).equals("1")) {
            task.setAsDone();
        }
        return task;
    }

    /** Splits a stored line while restoring escaped pipes and backslashes in task fields. */
    private List<String> parseFields(String taskLine) {
        ArrayList<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        for (int i = 0; i < taskLine.length(); i++) {
            char currentCharacter = taskLine.charAt(i);
            if (currentCharacter == '\\' && i + 1 < taskLine.length()
                    && (taskLine.charAt(i + 1) == '\\' || taskLine.charAt(i + 1) == '|')) {
                currentField.append(taskLine.charAt(i + 1));
                i++;
            } else if (currentCharacter == '|') {
                fields.add(currentField.toString().strip());
                currentField.setLength(0);
            } else {
                currentField.append(currentCharacter);
            }
        }
        fields.add(currentField.toString().strip());
        return fields;
    }

    /** Verifies that a stored task has the number of fields required by its type. */
    private void validateFieldCount(String taskType, int fieldCount, int lineNumber) {
        int expectedFieldCount;
        switch (taskType) {
        case "T":
            expectedFieldCount = 3;
            break;
        case "D":
            expectedFieldCount = 4;
            break;
        case "E":
            expectedFieldCount = 5;
            break;
        default:
            throw createCorruptedDataException(lineNumber, "task type must be T, D, or E");
        }
        if (fieldCount != expectedFieldCount) {
            throw createCorruptedDataException(lineNumber, "task has the wrong number of fields");
        }
    }

    /** Verifies that a stored completion status is either zero or one. */
    private void validateStatus(String status, int lineNumber) {
        if (!status.equals("0") && !status.equals("1")) {
            throw createCorruptedDataException(lineNumber, "status must be 0 or 1");
        }
    }

    /** Verifies that the description and any date or time fields are not empty. */
    private void validateRequiredFields(List<String> taskFields, int lineNumber) {
        for (int i = 2; i < taskFields.size(); i++) {
            if (taskFields.get(i).isBlank()) {
                throw createCorruptedDataException(lineNumber, "a required field is empty");
            }
        }
    }

    /** Replaces the previous data file with the completely written temporary file. */
    private void replaceDataFile(Path temporaryFile) throws IOException {
        try {
            Files.move(temporaryFile, filePath, StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException exception) {
            Files.move(temporaryFile, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /** Removes an incomplete temporary file without hiding the original save failure. */
    private void deleteTemporaryFile(Path temporaryFile) {
        try {
            Files.deleteIfExists(temporaryFile);
        } catch (IOException | SecurityException ignored) {
            // The original save failure gives the user the actionable error.
        }
    }

    /** Creates a consistent user-facing error for malformed stored data. */
    private DataFormatException createCorruptedDataException(int lineNumber, String reason) {
        return new DataFormatException("Mimi could not understand saved task on line "
                + lineNumber + ": " + reason + ".");
    }
}
