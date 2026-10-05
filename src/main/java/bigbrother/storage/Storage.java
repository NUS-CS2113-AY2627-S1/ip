package bigbrother.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import bigbrother.exception.BigBrotherException;
import bigbrother.task.Deadline;
import bigbrother.task.Event;
import bigbrother.task.Task;
import bigbrother.task.ToDo;

/**
 * Loads and saves BigBrother tasks in a text file.
 */
public class Storage {
    private static final String FIELD_SEPARATOR = " | ";
    private static final String TYPE_TODO = "T";
    private static final String TYPE_DEADLINE = "D";
    private static final String TYPE_EVENT = "E";
    private static final String STATUS_DONE = "1";
    private static final String STATUS_NOT_DONE = "0";

    private final Path filePath;

    /**
     * Creates a storage manager for the given data file.
     *
     * @param filePath the relative path of the data file
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the data file.
     *
     * @return the loaded tasks, or an empty list when the file does not exist
     * @throws BigBrotherException if the file cannot be read or contains invalid data
     */
    public List<Task> loadTasks() throws BigBrotherException {
        createDataDirectory();
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            List<Task> loadedTasks = new ArrayList<>();
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (!line.isBlank()) {
                    loadedTasks.add(parseTask(line, i + 1));
                }
            }

            return loadedTasks;
        } catch (IOException exception) {
            throw new BigBrotherException("Unable to read " + filePath + ".");
        }
    }

    /**
     * Saves all current tasks to the data file.
     *
     * @param tasks the task list to save
     * @throws BigBrotherException if the data file cannot be written
     */
    public void saveTasks(List<Task> tasks) throws BigBrotherException {
        createDataDirectory();
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(formatTask(task));
        }

        try {
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new BigBrotherException("Unable to save tasks to " + filePath + ".");
        }
    }

    /**
     * Creates the folder containing the data file when it does not exist.
     *
     * @throws BigBrotherException if the folder cannot be created
     */
    private void createDataDirectory() throws BigBrotherException {
        Path parentPath = filePath.getParent();
        if (parentPath == null) {
            return;
        }

        try {
            Files.createDirectories(parentPath);
        } catch (IOException exception) {
            throw new BigBrotherException("Unable to create the data folder " + parentPath + ".");
        }
    }

    /**
     * Converts a task into one line of the storage format.
     *
     * @param task the task to convert
     * @return the task's storage representation
     * @throws BigBrotherException if the task has an unsupported type
     */
    private String formatTask(Task task) throws BigBrotherException {
        String status = task.isDone() ? STATUS_DONE : STATUS_NOT_DONE;
        String description = escapeField(task.getDescription());

        if (task instanceof ToDo) {
            return TYPE_TODO + FIELD_SEPARATOR + status + FIELD_SEPARATOR + description;
        }
        if (task instanceof Deadline deadline) {
            return TYPE_DEADLINE + FIELD_SEPARATOR + status + FIELD_SEPARATOR + description
                    + FIELD_SEPARATOR + escapeField(deadline.getBy());
        }
        if (task instanceof Event event) {
            return TYPE_EVENT + FIELD_SEPARATOR + status + FIELD_SEPARATOR + description
                    + FIELD_SEPARATOR + escapeField(event.getFrom())
                    + FIELD_SEPARATOR + escapeField(event.getTo());
        }
        throw new BigBrotherException("Unable to save an unknown task type.");
    }

    /**
     * Reconstructs a task from one line of the data file.
     *
     * @param line the stored task data
     * @param lineNumber the line's position in the data file
     * @return the reconstructed task
     * @throws BigBrotherException if the line does not follow the storage format
     */
    private Task parseTask(String line, int lineNumber) throws BigBrotherException {
        List<String> fields = splitFields(line, lineNumber);
        if (fields.size() < 3) {
            throw corruptedDataException(lineNumber);
        }

        String type = fields.get(0);
        String status = fields.get(1);
        String description = fields.get(2);
        if (description.isEmpty() || !status.equals(STATUS_DONE) && !status.equals(STATUS_NOT_DONE)) {
            throw corruptedDataException(lineNumber);
        }

        Task task;
        switch (type) {
        case TYPE_TODO:
            if (fields.size() != 3) {
                throw corruptedDataException(lineNumber);
            }
            task = new ToDo(description);
            break;
        case TYPE_DEADLINE:
            if (fields.size() != 4 || fields.get(3).isEmpty()) {
                throw corruptedDataException(lineNumber);
            }
            try {
                task = new Deadline(description, fields.get(3));
            } catch (BigBrotherException exception) {
                throw corruptedDataException(lineNumber);
            }
            break;
        case TYPE_EVENT:
            if (fields.size() != 5 || fields.get(3).isEmpty() || fields.get(4).isEmpty()) {
                throw corruptedDataException(lineNumber);
            }
            task = new Event(description, fields.get(3), fields.get(4));
            break;
        default:
            throw corruptedDataException(lineNumber);
        }

        if (status.equals(STATUS_DONE)) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Splits a stored line while preserving escaped pipes and backslashes.
     *
     * @param line the stored line to split
     * @param lineNumber the line's position in the data file
     * @return the unescaped fields from the line
     * @throws BigBrotherException if the line contains an invalid escape sequence
     */
    private List<String> splitFields(String line, int lineNumber) throws BigBrotherException {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean isEscaped = false;

        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);
            if (isEscaped) {
                if (character != '\\' && character != '|') {
                    throw corruptedDataException(lineNumber);
                }
                currentField.append(character);
                isEscaped = false;
            } else if (character == '\\') {
                isEscaped = true;
            } else if (character == '|') {
                fields.add(currentField.toString().trim());
                currentField.setLength(0);
            } else {
                currentField.append(character);
            }
        }

        if (isEscaped) {
            throw corruptedDataException(lineNumber);
        }
        fields.add(currentField.toString().trim());
        return fields;
    }

    /**
     * Escapes characters that have special meaning in the storage format.
     *
     * @param field the field to escape
     * @return the escaped field
     */
    private String escapeField(String field) {
        return field.replace("\\", "\\\\").replace("|", "\\|");
    }

    /**
     * Creates a consistent error for malformed saved data.
     *
     * @param lineNumber the malformed line's position in the data file
     * @return an exception that identifies the malformed line
     */
    private BigBrotherException corruptedDataException(int lineNumber) {
        return new BigBrotherException("The data file is corrupted at line " + lineNumber
                + ". Starting with no tasks.");
    }
}
