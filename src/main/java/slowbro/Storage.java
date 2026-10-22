package slowbro;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;

/** Handles saving and loading tasks from the hard disk. */
public class Storage {
    private static final Path DATA_FILE = Path.of("data", "slowbro.txt");
    private static final String FIELD_SEPARATOR = "|";
    private static final String TODO_TYPE = "T";
    private static final String DEADLINE_TYPE = "D";
    private static final String EVENT_TYPE = "E";
    private static final String DONE_VALUE = "true";
    private static final String UNDONE_VALUE = "false";
    private static final String DONE_STATUS_ICON = "X";

    /** Loads all saved tasks, returning an empty array when no file exists. */
    public TaskList load() {
        TaskList tasks = new TaskList();
        if (!Files.exists(DATA_FILE)) {
            return tasks;
        }
        try {
            List<String> lines = Files.readAllLines(DATA_FILE);
            for (String line : lines) {
                if (!line.isBlank()) {
                    Task task = loadTask(line);
                    if (task != null) {
                        tasks.add(task);
                    }
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Warning: Unable to load saved tasks.");
        }
        return tasks;
    }

    /** Parses one saved line and reports malformed data without stopping the load. */
    private Task loadTask(String line) {
        try {
            return parseTask(line);
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            System.out.println("Warning: Skipping malformed saved task.");
            return null;
        }
    }

    /** Saves the given tasks to the hard disk. */
    public void save(TaskList tasks) {
        try {
            Files.createDirectories(DATA_FILE.getParent());
            StringBuilder data = new StringBuilder();
            for (Task task : tasks.getTasks()) {
                data.append(serializeTask(task)).append(System.lineSeparator());
            }
            Files.writeString(DATA_FILE, data.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Warning: Unable to save tasks.");
        }
    }

    /**
     * Converts a task into the encoded format used by the save file.
     *
     * @param task the task to serialize
     * @return the encoded representation of the task
     */
    private String serializeTask(Task task) {
        String type;
        String[] fields;
        if (task instanceof Deadline deadline) {
            type = DEADLINE_TYPE;
            fields = new String[] { deadline.getDescription(), deadline.getBy() };
        } else if (task instanceof Event event) {
            type = EVENT_TYPE;
            fields = new String[] { event.getDescription(), event.getFrom(), event.getTo() };
        } else {
            type = TODO_TYPE;
            fields = new String[] { task.getDescription() };
        }
        StringBuilder result = new StringBuilder(type + FIELD_SEPARATOR
                + (task.getStatusIcon().equals(DONE_STATUS_ICON)) + FIELD_SEPARATOR);
        for (String field : fields) {
            result.append(encode(field)).append(FIELD_SEPARATOR);
        }
        return result.toString();
    }

    /**
     * Converts one saved line into a task.
     *
     * @param line the encoded task line
     * @return the parsed task, or {@code null} for an unknown task type
     */
    private Task parseTask(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 3) {
            return null;
        }

        boolean isDone;
        if (DONE_VALUE.equals(parts[1])) {
            isDone = true;
        } else if (UNDONE_VALUE.equals(parts[1])) {
            isDone = false;
        } else {
            return null;
        }

        Task task;
        switch (parts[0]) {
        case TODO_TYPE:
            if (parts.length != 4) {
                return null;
            }
            task = new Todo(decode(parts[2]));
            break;
        case DEADLINE_TYPE:
            if (parts.length != 5) {
                return null;
            }
            task = new Deadline(decode(parts[2]), decode(parts[3]));
            break;
        case EVENT_TYPE:
            if (parts.length != 6) {
                return null;
            }
            task = new Event(decode(parts[2]), decode(parts[3]), decode(parts[4]));
            break;
        default:
            return null;
        }
        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

    private String encode(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String decode(String value) {
        return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
    }
}
