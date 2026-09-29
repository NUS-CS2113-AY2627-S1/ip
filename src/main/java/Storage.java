import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads tasks from, and saves tasks to, a text file on disk.
 * Each task is stored on one line as pipe-separated fields,
 * e.g. "{@code D|1|return book|Sunday}": type, done flag (1 or 0), description,
 * then any type-specific times.
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates a storage that uses the file at {@code filePath}, e.g. "data/shaheer.txt".
     * Missing folders are created when saving.
     */
    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /**
     * Returns the saved tasks, skipping any malformed lines.
     * Returns an empty list if the file does not exist yet.
     *
     * @throws ShaheerException if the file exists but cannot be read.
     */
    public List<Task> load() throws ShaheerException {
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return tasks;
        }
        try {
            for (String line : Files.readAllLines(filePath)) {
                Task task = decodeTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            throw new ShaheerException("I could not load your saved tasks.");
        }
        return tasks;
    }

    /**
     * Writes {@code tasks} to the save file, replacing its previous contents.
     *
     * @throws ShaheerException if the file cannot be written.
     */
    public void save(List<Task> tasks) throws ShaheerException {
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(encodeTask(task));
        }
        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new ShaheerException("I could not save your tasks.");
        }
    }

    /** Converts a task into one line of the save file. */
    private static String encodeTask(Task task) {
        String commonFields = (task.isDone ? "1" : "0") + "|" + task.description;
        if (task instanceof Deadline deadline) {
            return "D|" + commonFields + "|" + deadline.by;
        } else if (task instanceof Event event) {
            return "E|" + commonFields + "|" + event.from + "|" + event.to;
        }
        return "T|" + commonFields;
    }

    /**
     * Converts one line of the save file back into a task, including its done status.
     *
     * @return the task, or null if the line is malformed and should be skipped.
     */
    private static Task decodeTask(String line) {
        String[] fields = line.split("\\|", -1);
        if (fields.length < 3) {
            return null;
        }
        Task task = createTask(fields);
        if (task != null && fields[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /** Creates a not-done task from the fields of a save-file line, or returns null if fields are missing. */
    private static Task createTask(String[] fields) {
        switch (fields[0]) {
        case "T":
            return new Todo(fields[2]);
        case "D":
            return fields.length < 4 ? null : new Deadline(fields[2], fields[3]);
        case "E":
            return fields.length < 5 ? null : new Event(fields[2], fields[3], fields[4]);
        default:
            return null;
        }
    }
}
