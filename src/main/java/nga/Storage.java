package nga;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/** Writes and loads Nga's task list in the project data directory. */
public class Storage {
    private static final Path FILE_PATH = Path.of("data/nga.txt");
    private int invalidTaskCount;

    /** Creates a storage component using Nga's default data file. */
    public Storage() {
    }

    /**
     * Saves the current tasks, replacing the previous contents of the file.
     *
     * @param taskList the tasks to serialize
     * @return {@code true} if the file was written successfully
     */
    public boolean save(TaskList taskList) {
        if (taskList == null) {
            return false;
        }
        List<String> lines = new ArrayList<>();
        for (int taskNumber = 1; taskNumber <= taskList.size(); taskNumber++) {
            Task task = taskList.getTask(taskNumber);
            if (task != null) {
                lines.add(formatTask(task));
            }
        }
        try {
            Files.createDirectories(FILE_PATH.getParent());
            Files.write(FILE_PATH, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException | SecurityException exception) {
            return false;
        }
    }

    /**
     * Loads saved tasks, returning an empty list when no data file exists.
     * Invalid rows are skipped so one malformed row does not prevent the rest from loading.
     * The number of skipped rows is available through {@link #getInvalidTaskCount()}.
     *
     * @return the tasks read from the data file, or an empty list when loading fails
     */
    public List<Task> load() {
        invalidTaskCount = 0;
        try {
            if (!Files.exists(FILE_PATH)) {
                return new ArrayList<>();
            }
            List<Task> tasks = new ArrayList<>();
            for (String line : Files.readAllLines(FILE_PATH)) {
                if (line == null || line.isBlank()) {
                    continue;
                }
                Task task = parseTask(line);
                if (task != null) {
                    tasks.add(task);
                } else {
                    invalidTaskCount++;
                }
            }
            return tasks;
        } catch (IOException | SecurityException exception) {
            return new ArrayList<>();
        }
    }

    /**
     * Returns the number of non-blank rows rejected during the most recent load.
     *
     * @return the number of invalid task rows skipped during the most recent load
     */
    public int getInvalidTaskCount() {
        return invalidTaskCount;
    }

    /**
     * Converts a task into the pipe-separated format used in the data file.
     *
     * @param task the task to serialize
     * @return one storage row representing the task
     */
    private String formatTask(Task task) {
        String status = task.isDone() ? "1" : "0";
        if (task instanceof Deadline deadline) {
            String storedDate = deadline.hasExplicitTime()
                    ? deadline.getBy().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    : deadline.getBy().toLocalDate().toString();
            return String.join(" | ", "D", status, deadline.getDescription(), storedDate);
        }
        if (task instanceof Event event) {
            return String.join(" | ", "E", status, event.getDescription(), event.getFrom(), event.getTo());
        }
        return String.join(" | ", "T", status, task.getDescription());
    }

    /**
     * Converts one storage row into a task when the row is valid.
     *
     * @param line the raw row read from the data file
     * @return the parsed task, or {@code null} for an invalid row
     */
    private Task parseTask(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }
        String[] fields = line.split("\\s*\\|\\s*", -1);
        for (int index = 0; index < fields.length; index++) {
            fields[index] = fields[index].trim();
        }
        if (fields.length < 3 || !isValidStatus(fields[1])) {
            return null;
        }
        Task task = switch (fields[0]) {
        case "T" -> fields.length == 3 && hasContent(fields[2]) ? new Todo(fields[2]) : null;
        case "D" -> parseDeadline(fields);
        case "E" -> fields.length == 5 && hasContent(fields[2]) && hasContent(fields[3]) && hasContent(fields[4])
                ? new Event(fields[2], fields[3], fields[4]) : null;
        default -> null;
        };
        if (task != null && fields[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Parses the fields specific to a stored deadline.
     *
     * @param fields the split storage row, including its type and status
     * @return the parsed deadline, or {@code null} when its date is invalid
     */
    private Task parseDeadline(String[] fields) {
        if (fields.length != 4 || !hasContent(fields[2]) || !hasContent(fields[3])) {
            return null;
        }
        try {
            if (fields[3].contains("T")) {
                return new Deadline(fields[2], LocalDateTime.parse(fields[3]), true);
            }
            return new Deadline(fields[2], LocalDate.parse(fields[3]).atStartOfDay(), false);
        } catch (DateTimeParseException exception) {
            return null;
        }
    }

    /**
     * Checks whether a stored status is one of the supported values.
     *
     * @param status the status field from a storage row
     * @return {@code true} for an unfinished ({@code 0}) or completed ({@code 1}) status
     */
    private boolean isValidStatus(String status) {
        return "0".equals(status) || "1".equals(status);
    }

    /**
     * Checks whether a storage field contains non-whitespace text.
     *
     * @param value the field to check
     * @return {@code true} when the field is not null and is not blank
     */
    private boolean hasContent(String value) {
        return value != null && !value.isBlank();
    }
}
