package moistbot.storage;

import moistbot.exception.MoistBotException;
import moistbot.task.Deadline;
import moistbot.task.Event;
import moistbot.task.Task;
import moistbot.task.TaskManager;
import moistbot.task.Todo;
import moistbot.util.DateTimeUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads and saves MoistBot tasks using a text file on the local disk.
 */
public final class Storage {
    private static final String FIELD_SEPARATOR = " | ";

    /** The task data file used by this storage instance. */
    private final Path dataFile;

    /**
     * Creates a storage service that reads and writes tasks at the specified path.
     *
     * @param filePath The path of the task data file
     */
    public Storage(String filePath) {
        dataFile = Path.of(filePath);
    }

    /**
     * Restores tasks from the data file when it exists.
     * A missing file represents a new user and therefore starts with an empty list.
     *
     * @return The complete list of tasks restored from storage
     * @throws MoistBotException if the file cannot be read or contains invalid task data
     */
    public List<Task> loadTasks() throws MoistBotException {
        List<String> taskLines;
        try {
            taskLines = Files.readAllLines(dataFile, StandardCharsets.UTF_8);
        } catch (NoSuchFileException e) {
            return new ArrayList<>();
        } catch (IOException | SecurityException e) {
            throw new MoistBotException("My apologies, but I could not read your saved task list. Please check "
                    + "that the save file is readable, then restart MoistBot.");
        }

        List<Task> tasks = new ArrayList<>();
        for (int lineNumber = 1; lineNumber <= taskLines.size(); lineNumber++) {
            String taskLine = taskLines.get(lineNumber - 1);
            if (lineNumber == 1 && taskLine.startsWith("\uFEFF")) {
                taskLine = taskLine.substring(1);
            }
            if (taskLine.isBlank()) {
                throw invalidDataException(lineNumber);
            }
            tasks.add(parseTask(taskLine, lineNumber));
        }

        return tasks;
    }

    /**
     * Writes the complete current task list, replacing the previous saved copy.
     *
     * @param taskManager The task list whose current contents should be saved
     * @throws MoistBotException if the data directory or file cannot be written
     */
    public void saveTasks(TaskManager taskManager) throws MoistBotException {
        List<String> taskLines = new ArrayList<>();
        for (int taskNumber = 1; taskNumber <= taskManager.getSize(); taskNumber++) {
            taskLines.add(formatTask(taskManager.getTask(taskNumber)));
        }

        Path dataDirectory = dataFile.toAbsolutePath().getParent();
        Path temporaryFile = null;
        try {
            Files.createDirectories(dataDirectory);
            temporaryFile = Files.createTempFile(dataDirectory, "moistbot-", ".tmp");
            Files.write(temporaryFile, taskLines, StandardCharsets.UTF_8);
            replaceDataFile(temporaryFile);
            temporaryFile = null;
        } catch (IOException | SecurityException e) {
            throw new MoistBotException("My apologies, but I could not save your task list. Please check that "
                    + "the data folder is writable, then try your command again.");
        } finally {
            deleteTemporaryFile(temporaryFile);
        }
    }

    /**
     * Converts a task to a stable, pipe-separated representation for future loading.
     */
    private static String formatTask(Task task) throws MoistBotException {
        if (task == null) {
            throw unsupportedTaskException();
        }

        String completionStatus = task.isCompleted() ? "1" : "0";
        String commonFields = task.getTaskType() + FIELD_SEPARATOR + completionStatus + FIELD_SEPARATOR
                + escapeField(task.getDescription());

        switch (task.getTaskType()) {
            case Task.TYPE_DEADLINE:
                if (!(task instanceof Deadline deadline)) {
                    throw unsupportedTaskException();
                }
                return commonFields + FIELD_SEPARATOR + escapeField(DateTimeUtil.formatForStorage(
                        deadline.getDeadlineDate(), deadline.getDeadlineTime()));
            case Task.TYPE_EVENT:
                if (!(task instanceof Event event)) {
                    throw unsupportedTaskException();
                }
                return commonFields + FIELD_SEPARATOR + escapeField(DateTimeUtil.formatForStorage(
                        event.getStartDate(), event.getStartTime()))
                        + FIELD_SEPARATOR + escapeField(DateTimeUtil.formatForStorage(
                        event.getEndDate(), event.getEndTime()));
            case Task.TYPE_TODO:
                return commonFields;
            default:
                throw unsupportedTaskException();
        }
    }

    /**
     * Reconstructs one task while rejecting data that cannot be interpreted safely.
     */
    private static Task parseTask(String taskLine, int lineNumber) throws MoistBotException {
        String[] fields = splitFields(taskLine);
        if (fields.length < 3) {
            throw invalidDataException(lineNumber);
        }

        boolean isCompleted;
        if ("1".equals(fields[1])) {
            isCompleted = true;
        } else if ("0".equals(fields[1])) {
            isCompleted = false;
        } else {
            throw invalidDataException(lineNumber);
        }

        String description = fields[2];
        Task task;
        if (fields.length == 3 && "T".equals(fields[0])) {
            task = new Todo(description);
        } else if (fields.length == 4 && "D".equals(fields[0])) {
            task = parseDeadline(description, fields[3], lineNumber);
        } else if (fields.length == 5 && "E".equals(fields[0])) {
            task = parseEvent(description, fields[3], fields[4], lineNumber);
        } else {
            throw invalidDataException(lineNumber);
        }

        if (description.isBlank() || hasBlankTaskDetail(fields)) {
            throw invalidDataException(lineNumber);
        }
        task.setCompleted(isCompleted);
        return task;
    }

    /**
     * Restores a deadline while ensuring saved date-time data is valid.
     */
    private static Deadline parseDeadline(String description, String storedDeadline, int lineNumber)
            throws MoistBotException {
        try {
            DateTimeUtil.ParsedDateTime deadline = DateTimeUtil.parse(storedDeadline);
            return new Deadline(description, deadline.date(), deadline.time());
        } catch (DateTimeParseException e) {
            throw invalidDataException(lineNumber);
        }
    }

    /**
     * Restores an event while ensuring both saved endpoints form a valid range.
     */
    private static Event parseEvent(String description, String storedStart, String storedEnd, int lineNumber)
            throws MoistBotException {
        try {
            DateTimeUtil.ParsedDateTime start = DateTimeUtil.parse(storedStart);
            DateTimeUtil.ParsedDateTime end = DateTimeUtil.parse(storedEnd);
            if (isInvalidEventRange(start, end)) {
                throw invalidDataException(lineNumber);
            }
            return new Event(description, start.date(), start.time(), end.date(), end.time());
        } catch (DateTimeParseException e) {
            throw invalidDataException(lineNumber);
        }
    }

    /**
     * Returns whether event endpoints have inconsistent precision or run backwards.
     */
    private static boolean isInvalidEventRange(DateTimeUtil.ParsedDateTime start, DateTimeUtil.ParsedDateTime end) {
        if ((start.time() == null) != (end.time() == null)) {
            return true;
        }
        return start.date().isAfter(end.date()) || start.date().equals(end.date()) && start.time() != null
                && start.time().isAfter(end.time());
    }

    /**
     * Returns whether a deadline or event is missing one of its required time fields.
     */
    private static boolean hasBlankTaskDetail(String[] fields) {
        for (int fieldIndex = 3; fieldIndex < fields.length; fieldIndex++) {
            if (fields[fieldIndex].isBlank()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a consistent, actionable error for malformed saved data.
     */
    private static MoistBotException invalidDataException(int lineNumber) {
        return new MoistBotException("My apologies, but line " + lineNumber + " in the save file is invalid. "
                + "I have started with an empty task list instead. Please add your tasks again; MoistBot will "
                + "replace the save file when the task list next changes.");
    }

    /**
     * Escapes characters that otherwise have structural meaning in the storage format.
     */
    private static String escapeField(String field) throws MoistBotException {
        if (field == null || field.isBlank()) {
            throw unsupportedTaskException();
        }
        return field.replace("\\", "\\\\").replace("|", "\\|");
    }

    /**
     * Splits fields at unescaped pipe characters and restores escaped field content.
     */
    private static String[] splitFields(String taskLine) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();

        for (int characterIndex = 0; characterIndex < taskLine.length(); characterIndex++) {
            char currentCharacter = taskLine.charAt(characterIndex);
            if (currentCharacter == '\\' && characterIndex + 1 < taskLine.length()) {
                char nextCharacter = taskLine.charAt(characterIndex + 1);
                if (nextCharacter == '\\' || nextCharacter == '|') {
                    currentField.append(nextCharacter);
                    characterIndex++;
                    continue;
                }
            }
            if (currentCharacter == '|') {
                fields.add(currentField.toString().trim());
                currentField.setLength(0);
            } else {
                currentField.append(currentCharacter);
            }
        }

        fields.add(currentField.toString().trim());
        return fields.toArray(String[]::new);
    }

    /**
     * Replaces the save file atomically when supported by the host file system.
     */
    private void replaceDataFile(Path temporaryFile) throws IOException {
        try {
            Files.move(temporaryFile, dataFile, StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(temporaryFile, dataFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * Removes a leftover temporary file without masking the original save error.
     */
    private static void deleteTemporaryFile(Path temporaryFile) {
        if (temporaryFile == null) {
            return;
        }
        try {
            Files.deleteIfExists(temporaryFile);
        } catch (IOException | SecurityException e) {
            // The actionable save error has already been reported; cleanup must not replace it.
        }
    }

    /**
     * Creates a courteous error when an in-memory task cannot be represented safely.
     */
    private static MoistBotException unsupportedTaskException() {
        return new MoistBotException("My apologies, but the task list contains unsupported data and could not be "
                + "saved. Please restart MoistBot and try again.");
    }
}
