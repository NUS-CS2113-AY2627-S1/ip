package ted;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import ted.task.Deadline;
import ted.task.Event;
import ted.task.Task;
import ted.task.Todo;

/**
 * Reads and writes Ted's task list in a file relative to the working directory.
 */
public class TedStorage {
    private static final Path DATA_FILE = Path.of("data", "ted.txt");
    private static final String FIELD_SEPARATOR = "|";
    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder DECODER = Base64.getUrlDecoder();

    /**
     * Loads saved tasks, returning an empty list when the data file does not exist.
     *
     * @return The tasks saved during the previous run.
     * @throws IOException If the data file cannot be read.
     */
    public List<Task> load() throws IOException {
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(DATA_FILE)) {
            return tasks;
        }

        List<String> lines = Files.readAllLines(DATA_FILE, StandardCharsets.UTF_8);
        for (int i = 0; i < lines.size(); i++) {
            try {
                tasks.add(decodeTask(lines.get(i)));
            } catch (IllegalArgumentException | DateTimeParseException exception) {
                System.out.println("     OOPS!!! Skipping invalid saved task on line " + (i + 1) + ".");
            }
        }
        return tasks;
    }

    /**
     * Saves the current tasks, creating the data directory when needed.
     *
     * @param tasks The tasks to save.
     * @throws IOException If the data directory or file cannot be written.
     */
    public void save(List<Task> tasks) throws IOException {
        Files.createDirectories(DATA_FILE.getParent());
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(encodeTask(task));
        }
        Files.write(DATA_FILE, lines, StandardCharsets.UTF_8);
    }

    private static String encodeTask(Task task) {
        String type;
        List<String> fields = new ArrayList<>();
        if (task instanceof Todo) {
            type = "T";
        } else if (task instanceof Deadline deadline) {
            type = "D";
            fields.add(encode(deadline.getBy().toString()));
        } else if (task instanceof Event event) {
            type = "E";
            fields.add(encode(event.getFrom()));
            fields.add(encode(event.getTo()));
        } else {
            throw new IllegalArgumentException("Unsupported task type.");
        }
        fields.add(0, encode(task.getDescription()));
        fields.add(0, task.isDone() ? "1" : "0");
        fields.add(0, type);
        return String.join(FIELD_SEPARATOR, fields);
    }

    private static Task decodeTask(String line) {
        String[] fields = line.split("\\|", -1);
        if (fields.length < 3 || !(fields[1].equals("0") || fields[1].equals("1"))) {
            throw new IllegalArgumentException("Invalid task record.");
        }
        String description = decode(fields[2]);
        Task task;
        switch (fields[0]) {
        case "T":
            if (fields.length != 3) {
                throw new IllegalArgumentException("Invalid todo record.");
            }
            task = new Todo(description);
            break;
        case "D":
            if (fields.length != 4) {
                throw new IllegalArgumentException("Invalid deadline record.");
            }
            task = new Deadline(description, LocalDate.parse(decode(fields[3])));
            break;
        case "E":
            if (fields.length != 5) {
                throw new IllegalArgumentException("Invalid event record.");
            }
            task = new Event(description, decode(fields[3]), decode(fields[4]));
            break;
        default:
            throw new IllegalArgumentException("Unknown task type.");
        }
        if (fields[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    private static String encode(String value) {
        return ENCODER.encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private static String decode(String value) {
        return new String(DECODER.decode(value), StandardCharsets.UTF_8);
    }
}
