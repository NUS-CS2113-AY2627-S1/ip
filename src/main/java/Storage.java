import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads tasks from and saves tasks to the application's data file.
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates storage that uses the supplied data-file path.
     *
     * @param filePath the location of the task data file
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads the saved tasks, creating empty storage when it does not exist.
     *
     * @return the loaded task list, excluding corrupted saved entries
     */
    public TaskList load() {
        List<Task> loadedTasks = new ArrayList<>();
        try {
            ensureStorageExists();
            for (String line : Files.readAllLines(filePath)) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    loadedTasks.add(parseTaskFromLine(line));
                } catch (YodaException | DateTimeParseException exception) {
                    System.out.println("[Warning] Skipped corrupted data line: " + line);
                }
            }
        } catch (IOException exception) {
            System.out.println("[Warning] Failed to load data from " + filePath);
        }
        return new TaskList(loadedTasks);
    }

    /**
     * Saves all tasks in the supplied list to the data file.
     *
     * @param tasks the task list to save
     */
    public void save(TaskList tasks) {
        try {
            ensureStorageExists();
            StringBuilder contents = new StringBuilder();
            for (Task task : tasks.getTasks()) {
                contents.append(task.toFileFormat()).append(System.lineSeparator());
            }
            Files.writeString(filePath, contents.toString());
        } catch (IOException exception) {
            System.out.println("OOPS! Error occurred while saving tasks to file: "
                    + exception.getMessage());
        }
    }

    /**
     * Creates the parent directory and data file when either is missing.
     *
     * @throws IOException if the directory or file cannot be created
     */
    private void ensureStorageExists() throws IOException {
        if (filePath.getParent() != null && !Files.exists(filePath.getParent())) {
            Files.createDirectories(filePath.getParent());
        }
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }
    }

    /**
     * Recreates one task from its saved text representation.
     *
     * @param line one line from the data file
     * @return the recreated task
     * @throws YodaException if the saved representation is invalid
     */
    private Task parseTaskFromLine(String line) throws YodaException {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            throw new YodaException("Corrupted format");
        }

        String type = parts[0].trim();
        boolean isDone = parts[1].trim().equals("1");
        String description = parts[2].trim();
        Task task;
        switch (type) {
        case "T":
            task = new Todo(description);
            break;
        case "D":
            if (parts.length < 4) {
                throw new YodaException("Corrupted deadline format");
            }
            task = new Deadline(description, LocalDate.parse(parts[3].trim()));
            break;
        case "E":
            if (parts.length < 5) {
                throw new YodaException("Corrupted event format");
            }
            task = new Event(description, parts[3].trim(), parts[4].trim());
            break;
        default:
            throw new YodaException("Unknown task type");
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
