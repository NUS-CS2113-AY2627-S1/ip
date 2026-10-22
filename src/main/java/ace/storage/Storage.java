package ace.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import ace.exception.AceException;
import ace.task.Deadline;
import ace.task.Event;
import ace.task.Task;
import ace.task.TaskManager;
import ace.task.Todo;
import ace.ui.Messages;

/**
 * Loads and saves tasks in Ace's text file.
 */
public class Storage {
    private static final Path FILE_PATH = Path.of("data", "ace.txt");
    private static final String INCOMPATIBLE_DATE_MESSAGE =
            "Some saved tasks use an old date format. Back up data/ace.txt and "
                    + "convert dates to yyyy-MM-dd before restarting Ace.";

    /**
     * Reads all saved tasks, creating an empty data file if necessary.
     *
     * @return saved tasks
     * @throws AceException if the file cannot be read or contains invalid data
     */
    public ArrayList<Task> loadTasks() throws AceException {
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            Files.createDirectories(FILE_PATH.getParent());
            if (Files.notExists(FILE_PATH)) {
                Files.createFile(FILE_PATH);
                return tasks;
            }
            List<String> lines = Files.readAllLines(FILE_PATH);
            for (String line : lines) {
                if (!line.isBlank()) {
                    tasks.add(parseTask(line));
                }
            }
            return tasks;
        } catch (IOException exception) {
            throw new AceException(Messages.STORAGE_EXCEPTION);
        }
    }

    /**
     * Saves all tasks using ISO-format dates to preserve their exact values.
     *
     * @param taskManager task collection to save
     * @throws AceException if the file cannot be written or a task is invalid
     */
    public void saveTasks(TaskManager taskManager) throws AceException {
        ArrayList<String> lines = new ArrayList<>();
        for (int i = 0; i < taskManager.getTasksCount(); i++) {
            lines.add(taskToString(taskManager.getTask(i)));
        }
        try {
            Files.createDirectories(FILE_PATH.getParent());
            Files.write(FILE_PATH, lines);
        } catch (IOException exception) {
            throw new AceException(Messages.STORAGE_EXCEPTION);
        }
    }

    private String decodeDescription(String encoded) throws AceException {
        try {
            byte[] bytes = Base64.getDecoder().decode(encoded);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException exception) {
            throw new AceException(Messages.CORRUPTED_DATA_EXCEPTION);
        }
    }

    private String taskToString(Task task) {
        String status = task.isDone() ? "1" : "0";
        String description = Base64.getEncoder().encodeToString(
                task.getLabel().getBytes(StandardCharsets.UTF_8));

        if (task instanceof Deadline deadline) {
            return "D2 | " + status + " | " + description
                    + " | " + deadline.getBy();
        }

        if (task instanceof Event event) {
            return "E2 | " + status + " | " + description
                    + " | " + event.getFrom() + " | " + event.getTo();
        }

        return "T2 | " + status + " | " + description;
    }

    private Task parseTask(String line) throws AceException {
        String[] parts = line.split("\\s*\\|\\s*", -1);

        if (parts.length < 3) {
            throw new AceException(Messages.CORRUPTED_DATA_EXCEPTION);
        }

        boolean isDone;
        if (parts[1].equals("1")) {
            isDone = true;
        } else if (parts[1].equals("0")) {
            isDone = false;
        } else {
            throw new AceException(Messages.CORRUPTED_DATA_EXCEPTION);
        }

        String type = parts[0];
        String description = type.endsWith("2")
                ? decodeDescription(parts[2])
                : parts[2];

        switch (type) {
        case "T":
        case "T2":
            if (parts.length != 3) {
                throw new AceException(Messages.CORRUPTED_DATA_EXCEPTION);
            }
            return new Todo(description, isDone);

        case "D":
        case "D2":
            if (parts.length != 4) {
                throw new AceException(Messages.CORRUPTED_DATA_EXCEPTION);
            }
            return new Deadline(description, parseStoredDate(parts[3]), isDone);

        case "E":
        case "E2":
            if (parts.length != 5) {
                throw new AceException(Messages.CORRUPTED_DATA_EXCEPTION);
            }

            LocalDate from = parseStoredDate(parts[3]);
            LocalDate to = parseStoredDate(parts[4]);

            if (to.isBefore(from)) {
                throw new AceException(Messages.CORRUPTED_DATA_EXCEPTION);
            }

            return new Event(description, from, to, isDone);

        default:
            throw new AceException(Messages.CORRUPTED_DATA_EXCEPTION);
        }
    }

    private LocalDate parseStoredDate(String value) throws AceException {
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException exception) {
            throw new AceException(INCOMPATIBLE_DATE_MESSAGE);
        }
    }
}
