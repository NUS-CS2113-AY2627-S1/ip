package ernest.storage;

import ernest.task.Deadline;
import ernest.task.Event;
import ernest.task.Task;
import ernest.task.TaskDateTime;
import ernest.task.ToDo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves and loads Ernest task lists from the local data file.
 */
public final class Storage {
    private static final String CSV_HEADER = "type,isDone,description,deadline,startTime,endTime";
    private static final int CSV_FIELD_COUNT = 6;

    /** Location of this storage instance's task data file. */
    private final Path dataFilePath;

    /**
     * Creates storage that reads and writes a specified data file.
     *
     * @param filePath path of the task data file.
     */
    public Storage(String filePath) {
        this.dataFilePath = Path.of(filePath);
    }

    /**
     * Writes all tasks to the data file in CSV format.
     *
     * @param tasks tasks to save.
     * @return true if the tasks were saved; otherwise false.
     */
    public boolean saveTasks(List<Task> tasks) {
        List<String> records = new ArrayList<>();
        records.add(CSV_HEADER);
        for (Task task : tasks) {
            records.add(toCsvRecord(task));
        }

        try {
            Path parentDirectory = dataFilePath.getParent();
            if (parentDirectory != null) {
                Files.createDirectories(parentDirectory);
            }
            Files.write(dataFilePath, records);
            return true;
        } catch (IOException | SecurityException exception) {
            return false;
        }
    }

    /**
     * Returns tasks loaded from the data file, or an empty list when no data file exists.
     *
     * @param maximumTasks maximum number of tasks to load.
     * @return tasks read from the data file.
     */
    public LoadResult loadTasks(int maximumTasks) {
        ArrayList<Task> tasks = new ArrayList<>();
        if (maximumTasks <= 0) {
            return new LoadResult(tasks, LoadStatus.SUCCESS);
        }

        try {
            if (!Files.exists(dataFilePath)) {
                return new LoadResult(tasks, LoadStatus.SUCCESS);
            }
            List<String> records = Files.readAllLines(dataFilePath);
            if (records.isEmpty()) {
                return new LoadResult(tasks, LoadStatus.SUCCESS);
            }
            if (!CSV_HEADER.equals(records.get(0))) {
                return new LoadResult(tasks, LoadStatus.INVALID_HEADER);
            }

            boolean hasInvalidRecord = false;
            for (int i = 1; i < records.size(); i++) {
                if (records.get(i).isBlank()) {
                    continue;
                }
                if (tasks.size() == maximumTasks) {
                    hasInvalidRecord = true;
                    break;
                }
                try {
                    tasks.add(toTask(parseCsvRecord(records.get(i))));
                } catch (IllegalArgumentException exception) {
                    hasInvalidRecord = true;
                }
            }
            LoadStatus status = hasInvalidRecord ? LoadStatus.PARTIAL_LOAD : LoadStatus.SUCCESS;
            return new LoadResult(tasks, status);
        } catch (IOException | SecurityException exception) {
            return new LoadResult(tasks, LoadStatus.LOAD_FAILURE);
        }
    }

    /**
     * Returns one task encoded as a CSV record.
     *
     * @param task task to encode.
     * @return CSV record for the task.
     */
    private static String toCsvRecord(Task task) {
        if (task instanceof Deadline deadline) {
            return toCsvRecord("deadline", deadline, deadline.getDueDateTime().toStorageString(), "", "");
        }
        if (task instanceof Event event) {
            return toCsvRecord("event", event, "", event.getStartDateTime().toStorageString(),
                    event.getEndDateTime().toStorageString());
        }
        return toCsvRecord("todo", task, "", "", "");
    }

    /**
     * Returns CSV columns for a task.
     *
     * @param type task type.
     * @param task task containing the shared fields.
     * @param deadline deadline date and optional time, if applicable.
     * @param startTime event start date and optional time, if applicable.
     * @param endTime event end date and optional time, if applicable.
     * @return CSV record for the supplied columns.
     */
    private static String toCsvRecord(String type, Task task, String deadline, String startTime, String endTime) {
        return String.join(",", escapeCsv(type), escapeCsv(Boolean.toString(task.isDone())),
                escapeCsv(task.getTaskName()), escapeCsv(deadline), escapeCsv(startTime), escapeCsv(endTime));
    }

    /**
     * Escapes one CSV field using double quotes.
     *
     * @param value field value.
     * @return escaped CSV field.
     */
    private static String escapeCsv(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    /**
     * Returns a task reconstructed from a CSV record.
     *
     * @param fields fields in one CSV record.
     * @return task represented by the record.
     */
    private static Task toTask(List<String> fields) {
        if (fields.size() != CSV_FIELD_COUNT) {
            throw new IllegalArgumentException("Task record has an invalid number of fields.");
        }

        boolean isDone = switch (fields.get(1)) {
            case "true" -> true;
            case "false" -> false;
            default -> throw new IllegalArgumentException("Task record has an invalid completion status.");
        };

        Task task;
        try {
            switch (fields.get(0)) {
                case "todo":
                    task = new ToDo(fields.get(2));
                    break;
                case "deadline":
                    task = new Deadline(fields.get(2), parseTaskDateTime(fields.get(3)));
                    break;
                case "event":
                    task = new Event(fields.get(2), parseTaskDateTime(fields.get(4)),
                            parseTaskDateTime(fields.get(5)));
                    break;
                default:
                    throw new IllegalArgumentException("Task record has an unknown type.");
            }
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Task record contains an invalid date or time.", exception);
        }
        task.setDone(isDone);
        return task;
    }

    /**
     * Returns a stored date with its optional time.
     *
     * @param value ISO date or date-time text.
     * @return parsed task date and optional time.
     */
    private static TaskDateTime parseTaskDateTime(String value) {
        return value.contains("T")
                ? new TaskDateTime(LocalDateTime.parse(value))
                : new TaskDateTime(LocalDate.parse(value));
    }

    /**
     * Returns fields parsed from a CSV record written by this class.
     *
     * @param record CSV record to parse.
     * @return fields in the CSV record.
     */
    private static List<String> parseCsvRecord(String record) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean isQuoted = false;
        boolean hasClosedQuote = false;

        for (int i = 0; i < record.length(); i++) {
            char character = record.charAt(i);
            if (isQuoted) {
                if (character == '"' && i + 1 < record.length() && record.charAt(i + 1) == '"') {
                    field.append(character);
                    i++;
                } else if (character == '"') {
                    isQuoted = false;
                    hasClosedQuote = true;
                } else {
                    field.append(character);
                }
            } else if (character == ',') {
                fields.add(field.toString());
                field.setLength(0);
                hasClosedQuote = false;
            } else if (character == '"') {
                if (field.length() != 0 || hasClosedQuote) {
                    throw new IllegalArgumentException("Task record has an invalid quoted field.");
                }
                isQuoted = true;
            } else {
                if (hasClosedQuote) {
                    throw new IllegalArgumentException("Task record has an invalid quoted field.");
                }
                field.append(character);
            }
        }
        if (isQuoted) {
            throw new IllegalArgumentException("Task record has an unclosed quoted field.");
        }
        fields.add(field.toString());
        return fields;
    }

    /**
     * Describes the outcome of loading tasks from storage.
     */
    public enum LoadStatus {
        SUCCESS,
        INVALID_HEADER,
        PARTIAL_LOAD,
        LOAD_FAILURE
    }

    /**
     * Stores tasks loaded from storage and the loading outcome.
     *
     * @param tasks tasks that were loaded successfully.
     * @param status outcome of the loading operation.
     */
    public record LoadResult(List<Task> tasks, LoadStatus status) {
        /**
         * Creates an immutable snapshot of a storage loading result.
         */
        public LoadResult {
            tasks = List.copyOf(tasks);
        }
    }
}
