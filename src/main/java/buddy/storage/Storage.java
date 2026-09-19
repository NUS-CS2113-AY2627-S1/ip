package buddy.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import buddy.task.Deadline;
import buddy.task.Event;
import buddy.task.Task;
import buddy.task.Todo;

/**
 * Loads tasks from, and saves tasks to, a save file on disk so that Buddy
 * remembers tasks between runs.
 *
 * <p>The save file is plain text, one task per line, e.g.:
 * <pre>
 * T | 1 | read book
 * D | 0 | return book | June 6th
 * E | 0 | project meeting | Aug 6th 2pm | 4pm
 * </pre>
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates a storage backed by the given file path.
     *
     * @param filePath path to the save file, relative to the project root
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the save file.
     *
     * <p>If the save file does not exist yet (e.g. on the very first run),
     * an empty list is returned instead of failing, since there is simply
     * nothing saved yet. Lines that are not in the expected format are
     * skipped with a warning rather than aborting the whole load, so that
     * one corrupted line does not cost the user every other saved task.
     *
     * @return the tasks read from the save file, or an empty list if there is none
     */
    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return tasks;
        }

        try {
            for (String line : Files.readAllLines(filePath)) {
                if (line.isBlank()) {
                    continue;
                }
                Task task = parseLine(line);
                if (task == null) {
                    System.out.println("Skipping a corrupted line in the save file: " + line);
                } else {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read the save file (" + e.getMessage() + "). Starting with an empty list.");
        }
        return tasks;
    }

    /**
     * Parses a single save-file line into a task.
     *
     * @param line one line from the save file
     * @return the task described by the line, or null if the line is not in the expected format
     */
    private Task parseLine(String line) {
        String[] fields = line.split(" \\| ");
        try {
            String type = fields[0].trim();
            boolean isDone = fields[1].trim().equals("1");
            String description = fields[2].trim();

            Task task;
            switch (type) {
            case "T":
                task = new Todo(description);
                break;
            case "D":
                task = new Deadline(description, fields[3].trim());
                break;
            case "E":
                task = new Event(description, fields[3].trim(), fields[4].trim());
                break;
            default:
                return null;
            }
            if (isDone) {
                task.markAsDone();
            }
            return task;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Saves the given tasks to the save file, overwriting whatever was there
     * before. Creates the save file's parent folder first if it does not
     * exist yet (e.g. on the very first save).
     *
     * @param tasks tasks to save, in order
     */
    public void save(List<Task> tasks) {
        try {
            Path parentDir = filePath.getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }
            List<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(task.toFileFormat());
            }
            Files.write(filePath, lines);
        } catch (IOException e) {
            System.out.println("Could not save tasks to disk (" + e.getMessage() + ").");
        }
    }
}
