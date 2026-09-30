package sandy.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import sandy.exception.SandyException;
import sandy.task.Deadline;
import sandy.task.Event;
import sandy.task.Task;
import sandy.task.Todo;

/**
 * Loads tasks from and saves tasks to a local text file.
 */
public class Storage {
    private static final String FIELD_DELIMITER = " | ";
    private static final String FIELD_DELIMITER_REGEX = " \\| ";

    private final Path dataFilePath;

    /**
     * Creates storage that uses the given data file path.
     *
     * @param dataFilePath The relative or absolute path of the data file.
     */
    public Storage(Path dataFilePath) {
        this.dataFilePath = dataFilePath;
    }

    /**
     * Loads tasks from the data file.
     *
     * @return The loaded tasks, or an empty list when the data file does not exist.
     * @throws IOException If the data file cannot be read.
     * @throws SandyException If the data file contains invalid task data.
     */
    public List<Task> loadTasks() throws IOException, SandyException {
        if (Files.notExists(dataFilePath)) {
            return new ArrayList<>();
        }

        List<String> dataLines = Files.readAllLines(dataFilePath, StandardCharsets.UTF_8);
        List<Task> loadedTasks = new ArrayList<>();
        for (int i = 0; i < dataLines.size(); i++) {
            loadedTasks.add(parseTask(dataLines.get(i), i + 1));
        }
        return loadedTasks;
    }

    /**
     * Saves the current tasks, creating the data directory when necessary.
     *
     * @param tasks The tasks to save.
     * @throws IOException If the tasks cannot be written.
     */
    public void saveTasks(List<Task> tasks) throws IOException {
        Path dataDirectory = dataFilePath.getParent();
        if (dataDirectory != null) {
            Files.createDirectories(dataDirectory);
        }

        List<String> dataLines = new ArrayList<>();
        for (Task task : tasks) {
            dataLines.add(formatTask(task));
        }
        Files.write(dataFilePath, dataLines, StandardCharsets.UTF_8);
    }

    /**
     * Converts one stored data line into a task.
     *
     * @param dataLine The stored representation of a task.
     * @param lineNumber The one-based line number used in error messages.
     * @return The task represented by the line.
     * @throws SandyException If the line is not in the expected format.
     */
    private Task parseTask(String dataLine, int lineNumber) throws SandyException {
        String[] fields = dataLine.split(FIELD_DELIMITER_REGEX, -1);
        if (fields.length < 3 || fields[2].isEmpty()) {
            throw corruptedDataException(lineNumber);
        }

        Task task;
        switch (fields[0]) {
        case "T":
            if (fields.length != 3) {
                throw corruptedDataException(lineNumber);
            }
            task = new Todo(fields[2]);
            break;
        case "D":
            if (fields.length != 4 || fields[3].isEmpty()) {
                throw corruptedDataException(lineNumber);
            }
            task = new Deadline(fields[2], parseDeadlineDate(fields[3], lineNumber));
            break;
        case "E":
            if (fields.length != 5 || fields[3].isEmpty() || fields[4].isEmpty()) {
                throw corruptedDataException(lineNumber);
            }
            task = new Event(fields[2], fields[3], fields[4]);
            break;
        default:
            throw corruptedDataException(lineNumber);
        }

        if (fields[1].equals("1")) {
            task.markAsDone();
        } else if (!fields[1].equals("0")) {
            throw corruptedDataException(lineNumber);
        }
        return task;
    }

    /**
     * Converts a task into its stored representation.
     *
     * @param task The task to convert.
     * @return The stored representation of the task.
     * @throws IOException If the task type is unsupported.
     */
    private String formatTask(Task task) throws IOException {
        String status = task.isDone() ? "1" : "0";
        if (task instanceof Todo) {
            return String.join(FIELD_DELIMITER, "T", status, task.getDescription());
        } else if (task instanceof Deadline deadline) {
            String dateTime = deadline.getDateTime().toLocalTime().equals(LocalTime.MIDNIGHT)
                    ? deadline.getDateTime().toLocalDate().toString() : deadline.getDateTime().toString();
            return String.join(FIELD_DELIMITER, "D", status, task.getDescription(), dateTime);
        } else if (task instanceof Event event) {
            return String.join(FIELD_DELIMITER, "E", status, task.getDescription(), event.getFrom(), event.getTo());
        }
        throw new IOException("Unsupported task type: " + task.getClass().getSimpleName());
    }

    /**
     * Creates a consistent error for a malformed data line.
     *
     * @param lineNumber The one-based line number containing invalid data.
     * @return An exception describing the corrupted line.
     */
    private SandyException corruptedDataException(int lineNumber) {
        return new SandyException("The data file is corrupted at line " + lineNumber + ".");
    }

    /**
     * Parses a stored ISO date or date-time and identifies malformed task data.
     *
     * @param dateText The stored date value.
     * @param lineNumber The one-based line containing the value.
     * @return The parsed deadline, with date-only values set to midnight.
     * @throws SandyException If the value is not a supported ISO date or date-time.
     */
    private LocalDateTime parseDeadlineDate(String dateText, int lineNumber) throws SandyException {
        try {
            return LocalDateTime.parse(dateText);
        } catch (DateTimeParseException exception) {
            try {
                return LocalDate.parse(dateText).atStartOfDay();
            } catch (DateTimeParseException secondException) {
                throw corruptedDataException(lineNumber);
            }
        }
    }
}
