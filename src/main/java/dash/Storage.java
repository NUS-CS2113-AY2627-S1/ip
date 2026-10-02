package dash;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import dash.task.Deadline;
import dash.task.Event;
import dash.task.Task;
import dash.task.Todo;

/**
 * Deals with loading tasks from the file and saving tasks in the file.
 */
public class Storage {
    private final String filePath;

    /**
     * Constructs a {@code Storage} object with the specified file path.
     *
     * @param filePath The path of the file used to store task data.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the storage file.
     *
     * @return The list of tasks loaded from the file.
     * @throws DashException If an I/O error occurs while reading the file.
     */
    public ArrayList<Task> load() throws DashException {
        ArrayList<Task> loadedTasks = new ArrayList<>();
        Path path = Paths.get(filePath);
        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            return loadedTasks;
        }

        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (line.isBlank()) {
                    continue;
                }
                Task task = parseTaskFromFile(line);
                if (task != null) {
                    loadedTasks.add(task);
                }
            }
        } catch (IOException exception) {
            throw new DashException(exception.getMessage());
        }
        return loadedTasks;
    }

    /**
     * Saves the given list of tasks to the storage file.
     *
     * @param tasks The list of tasks to save.
     * @throws DashException If an I/O error occurs while writing to the file.
     */
    public void save(List<Task> tasks) throws DashException {
        try {
            Path path = Paths.get(filePath);
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            List<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(task.toFileFormat());
            }
            Files.write(path, lines);
        } catch (IOException exception) {
            throw new DashException(exception.getMessage());
        }
    }

    /**
     * Parses a single encoded line from the storage file into a corresponding {@code Task}.
     *
     * @param line The serialized task line from the file.
     * @return The reconstructed {@code Task}, or {@code null} if the line is corrupt or invalid.
     */
    private Task parseTaskFromFile(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }

        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            return null;
        }

        String type = parts[0].trim();
        String status = parts[1].trim();
        if (!status.equals("0") && !status.equals("1")) {
            return null;
        }
        boolean isDone = status.equals("1");
        String description = parts[2].trim();
        if (description.isEmpty()) {
            return null;
        }

        Task task;
        switch (type) {
        case "T":
            if (parts.length != 3) {
                return null;
            }
            task = new Todo(description);
            break;
        case "D":
            if (parts.length != 4) {
                return null;
            }
            String by = parts[3].trim();
            if (by.isEmpty()) {
                return null;
            }
            try {
                LocalDate byDate = LocalDate.parse(by);
                task = new Deadline(description, byDate);
            } catch (DateTimeParseException exception) {
                return null;
            }
            break;
        case "E":
            if (parts.length != 5) {
                return null;
            }
            String from = parts[3].trim();
            String to = parts[4].trim();
            if (from.isEmpty() || to.isEmpty()) {
                return null;
            }
            task = new Event(description, from, to);
            break;
        default:
            return null;
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
