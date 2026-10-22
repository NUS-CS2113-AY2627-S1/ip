package yao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import yao.task.Deadline;
import yao.task.Event;
import yao.task.Task;
import yao.task.Todo;

/**
 * Handles persistent storage of tasks in the local filesystem.
 * Responsible for loading tasks from disk and writing task data to file.
 */
public class Storage {
    private final Path filePath;

    /**
     * Constructs a Storage instance with the specified file path.
     *
     * @param filePath Relative or absolute path to the data storage file.
     */
    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /**
     * Loads tasks from the file on disk.
     * If the file does not exist, an empty list is returned.
     *
     * @return An ArrayList containing all valid tasks loaded from storage.
     * @throws YaoException If an I/O error occurs while reading the file.
     */
    public ArrayList<Task> load() throws YaoException {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return tasks;
        }

        try {
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                Task task = parseTaskFromLine(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
            return tasks;
        } catch (IOException e) {
            throw new YaoException("Unable to read data file: " + e.getMessage());
        }
    }

    /**
     * Parses a single line from the storage file into a Task object.
     *
     * @param line A single serialized line from the file.
     * @return The parsed Task, or null if the line format is invalid or corrupted.
     */
    private Task parseTaskFromLine(String line) {
        String[] parts = line.split("\\|");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        if (parts.length < 3) {
            System.out.println("Warning: Skipping corrupted line in data file: " + line);
            return null;
        }

        String type = parts[0];
        String isDoneStr = parts[1];
        String description = parts[2];

        if (!isDoneStr.equals("0") && !isDoneStr.equals("1")) {
            System.out.println("Warning: Skipping corrupted status in data file: " + line);
            return null;
        }

        boolean isDone = isDoneStr.equals("1");
        Task task;

        switch (type) {
        case "T":
            task = new Todo(description);
            break;
        case "D":
            if (parts.length < 4) {
                System.out.println("Warning: Skipping corrupted deadline line: " + line);
                return null;
            }
            task = new Deadline(description, parts[3]);
            break;
        case "E":
            if (parts.length < 4) {
                System.out.println("Warning: Skipping corrupted event line: " + line);
                return null;
            }
            String from = parts[3];
            String to = parts.length > 4 ? parts[4] : "";
            task = new Event(description, from, to);
            break;
        case "TASK":
            task = new Task(description);
            break;
        default:
            System.out.println("Warning: Skipping unrecognized task type in data file: " + line);
            return null;
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Saves the current tasks from a TaskList to the storage file on disk.
     *
     * @param tasks The TaskList containing tasks to save.
     * @throws YaoException If an I/O error occurs while writing to the file.
     */
    public void save(TaskList tasks) throws YaoException {
        save(tasks.getTasks());
    }

    /**
     * Saves the current tasks to the storage file on disk.
     * Creates any missing parent directories automatically.
     *
     * @param tasks The list of tasks to save.
     * @throws YaoException If an I/O error occurs while writing to the file.
     */
    public void save(ArrayList<Task> tasks) throws YaoException {
        try {
            Path parentDir = filePath.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }

            List<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(task.toFileFormat());
            }
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new YaoException("Unable to save tasks to file: " + e.getMessage());
        }
    }
}
