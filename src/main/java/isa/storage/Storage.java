package isa.storage;

import isa.exception.StorageException;
import isa.task.Deadline;
import isa.task.Event;
import isa.task.Task;
import isa.task.TaskList;
import isa.task.Todo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads and writes task data on the hard disk.
 */
public class Storage {
    private static final String FIELD_SEPARATOR = " | ";

    private final Path filePath;

    /**
     * Creates storage that uses the specified file.
     *
     * @param filePath Path of the data file.
     */
    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    /**
     * Loads valid tasks and reports malformed records as warnings.
     *
     * @return Loaded tasks and warnings for skipped records.
     * @throws StorageException If the data file cannot be read.
     */
    public LoadResult load() throws StorageException {
        TaskList taskList = new TaskList();
        List<String> warnings = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return new LoadResult(taskList, warnings);
        }
        if (!Files.isRegularFile(filePath)) {
            throw new StorageException("the data path is not a regular file");
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new StorageException("the data file could not be read", e);
        }

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isBlank()) {
                continue;
            }
            try {
                taskList.add(parseTask(line));
            } catch (StorageException e) {
                warnings.add("line " + (i + 1) + ": " + e.getMessage());
            }
        }
        return new LoadResult(taskList, warnings);
    }

    /**
     * Saves all tasks to the data file.
     *
     * @param taskList Tasks to save.
     * @throws StorageException If the data file cannot be written.
     */
    public void save(TaskList taskList) throws StorageException {
        Path parentDirectory = filePath.getParent();
        try {
            if (parentDirectory != null) {
                Files.createDirectories(parentDirectory);
            }
            List<String> taskData = new ArrayList<>();
            for (int i = 0; i < taskList.size(); i++) {
                taskData.add(taskList.get(i).toDataString());
            }
            Files.write(filePath, taskData, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new StorageException("the task list could not be saved", e);
        }
    }

    /**
     * Converts one data-file record into a task.
     *
     * @param line Data-file record to parse.
     * @return Task represented by the record.
     * @throws StorageException If the record is malformed.
     */
    private Task parseTask(String line) throws StorageException {
        List<String> fields = splitFields(line);
        if (fields.size() < 2) {
            throw new StorageException("record does not contain a type and status");
        }

        boolean isDone = parseStatus(fields.get(1));
        Task task;
        switch (fields.get(0)) {
        case "T":
            requireFieldCount(fields, 3);
            task = new Todo(requireNonEmpty(fields.get(2), "description"));
            break;
        case "D":
            requireFieldCount(fields, 4);
            String description = requireNonEmpty(fields.get(2), "description");
            String savedDate = requireNonEmpty(fields.get(3), "due date");

            try {
                task = new Deadline(description, LocalDate.parse(savedDate));
            } catch (DateTimeParseException e) {
                throw new StorageException("invalid deadline date '" + savedDate + "'");
            }
            break;
        case "E":
            requireFieldCount(fields, 5);
            String eventDescription = requireNonEmpty(fields.get(2), "description");
            String startText = requireNonEmpty(fields.get(3), "start time");
            String endText = requireNonEmpty(fields.get(4), "end time");

            try {
                task = new Event(eventDescription,
                        Event.parseTime(startText), Event.parseTime(endText));
            } catch (DateTimeParseException e) {
                throw new StorageException("invalid event time; use yyyy-MM-ddTHH:mm");
            } catch (IllegalArgumentException e) {
                throw new StorageException(e.getMessage());
            }
            break;
        default:
            throw new StorageException("unknown task type '" + fields.get(0) + "'");
        }
        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Splits a record at unescaped field separators.
     *
     * @param line Record to split.
     * @return Unescaped fields in the record.
     * @throws StorageException If the record contains an incomplete escape.
     */
    private List<String> splitFields(String line) throws StorageException {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean isEscaped = false;

        for (int i = 0; i < line.length(); i++) {
            char current = line.charAt(i);
            if (isEscaped) {
                field.append('\\').append(current);
                isEscaped = false;
            } else if (current == '\\') {
                isEscaped = true;
            } else if (line.startsWith(FIELD_SEPARATOR, i)) {
                fields.add(unescapeDataField(field.toString()));
                field.setLength(0);
                i += FIELD_SEPARATOR.length() - 1;
            } else {
                field.append(current);
            }
        }
        if (isEscaped) {
            throw new StorageException("field ends with an incomplete escape");
        }
        fields.add(unescapeDataField(field.toString()));
        return fields;
    }

    /**
     * Converts escaped file characters back to their original values.
     *
     * @param value Escaped field value.
     * @return Unescaped field value.
     * @throws StorageException If an escape sequence is invalid.
     */
    private String unescapeDataField(String value) throws StorageException {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char current = value.charAt(i);
            if (current != '\\') {
                result.append(current);
                continue;
            }
            if (i + 1 >= value.length()) {
                throw new StorageException("field ends with an incomplete escape");
            }
            char escaped = value.charAt(++i);
            switch (escaped) {
            case '\\':
            case '|':
                result.append(escaped);
                break;
            case 'n':
                result.append('\n');
                break;
            case 'r':
                result.append('\r');
                break;
            default:
                throw new StorageException("field contains an invalid escape");
            }
        }
        return result.toString();
    }

    /**
     * Converts a stored status into a boolean.
     *
     * @param status Stored status value.
     * @return Whether the task is done.
     * @throws StorageException If the status is not {@code 0} or {@code 1}.
     */
    private boolean parseStatus(String status) throws StorageException {
        if (status.equals("0")) {
            return false;
        } else if (status.equals("1")) {
            return true;
        }
        throw new StorageException("status must be 0 or 1");
    }

    /**
     * Checks that a record contains the expected number of fields.
     *
     * @param fields Fields in the record.
     * @param expectedCount Required number of fields.
     * @throws StorageException If the field count is incorrect.
     */
    private void requireFieldCount(List<String> fields, int expectedCount)
            throws StorageException {
        if (fields.size() != expectedCount) {
            throw new StorageException(
                    "expected " + expectedCount + " fields but found " + fields.size());
        }
    }

    /**
     * Checks that a required field contains non-whitespace text.
     *
     * @param value Field value.
     * @param fieldName Name used in an error message.
     * @return Validated field value.
     * @throws StorageException If the field is blank.
     */
    private String requireNonEmpty(String value, String fieldName)
            throws StorageException {
        if (value.isBlank()) {
            throw new StorageException(fieldName + " cannot be empty");
        }
        return value;
    }
}
