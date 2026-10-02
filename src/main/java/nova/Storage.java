package nova;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import nova.task.Task;
import nova.task.Todo;
import nova.task.Deadline;
import nova.task.Event;

/**
 * Handles reading and writing the task list to a file on disk, so tasks
 * continue between runs of Nova.
 */
public class Storage {

    private final String filePath;

    /**
     * Creates a Storage pointing at the given file path.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from disk. Returns an empty list if the file doesn't exist
     * Throws NovaException if the file exists but can't be read.
     */
    public List<Task> load() throws NovaException {
        List<Task> tasks = new ArrayList<>();
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            return tasks;
        }

        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                Task task = parseLine(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            throw new NovaException("Unable to read " + filePath);
        }

        return tasks;
    }

    /**
     * Saves the given tasks to disk, or creates the folder and file
     * if they don't already exist.
     * Throws NovaException if the file can't be written.
     */
    public void save(TaskList tasks) throws NovaException {
        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            try (FileWriter writer = new FileWriter(file)) {
                for (Task task : tasks.getAll()) {
                    writer.write(task.toSaveString() + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            throw new NovaException("Unable to write " + filePath);
        }
    }

    /**
     * Parses a single saved line back into a Task. Returns null if the
     * line is wrong
     */
    private Task parseLine(String line) {
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            return null;
        }

        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task;
        switch (type) {
            case "T":
                task = new Todo(description);
                break;
            case "D":
                if (parts.length < 4) {
                    return null;
                }
                try {
                    task = new Deadline(description, LocalDate.parse(parts[3]));
                } catch (DateTimeParseException e) {
                    return null; // Skip lines whose date can't be read
                }
                break;
            case "E":
                if (parts.length < 5) {
                    return null;
                }
                task = new Event(description, parts[3], parts[4]);
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