package foodielover.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import foodielover.FoodieloverException;
import foodielover.task.Deadline;
import foodielover.task.Event;
import foodielover.task.Task;
import foodielover.task.TaskList;
import foodielover.task.Todo;

/**
 * Manages storage of task data by reading from and writing to a local file.
 */
public class Storage {
    /** Target file path for task persistence. */
    private final String filePath;

    /** Indicates whether a backup has already been created during the current application session. */
    private boolean hasBackedUpThisSession;

    /** Corrupted lines encountered during the most recent load operation. */
    private final List<String> corruptedLines;

    /**
     * Constructs a new Storage instance with the specified file path.
     *
     * @param filePath Relative or absolute path to the task data file.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
        this.hasBackedUpThisSession = false;
        this.corruptedLines = new ArrayList<>();
    }

    /**
     * Loads and returns tasks saved in the storage file.
     * Corrupted lines are skipped and recorded, accessible via {@link #getCorruptedLines()}.
     *
     * @return List of tasks loaded from the storage file.
     */
    public List<Task> load() {
        List<Task> loadedTasks = new ArrayList<>();
        corruptedLines.clear();
        File file = new File(filePath);
        if (!file.exists()) {
            return loadedTasks;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                try {
                    Task task = parseTaskLine(line);
                    loadedTasks.add(task);
                } catch (FoodieloverException exception) {
                    corruptedLines.add(line);
                }
            }
        } catch (FileNotFoundException exception) {
            // File does not exist yet; return empty list.
        }
        return loadedTasks;
    }

    /**
     * Returns an unmodifiable list of corrupted lines encountered during loading.
     *
     * @return Unmodifiable list of corrupted file lines.
     */
    public List<String> getCorruptedLines() {
        return Collections.unmodifiableList(corruptedLines);
    }

    /**
     * Saves the provided list of tasks to the storage file.
     * Creates a backup copy before the first save of the session.
     *
     * @param tasks List of tasks to write to disk.
     * @throws IOException If writing to the file fails.
     */
    public void save(List<Task> tasks) throws IOException {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        if (file.exists() && !hasBackedUpThisSession) {
            File backupFile = new File(filePath + ".bak");
            Files.copy(file.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            hasBackedUpThisSession = true;
        }

        try (FileWriter writer = new FileWriter(file)) {
            for (Task task : tasks) {
                writer.write(task.toFileFormat() + System.lineSeparator());
            }
        }
    }

    /**
     * Saves the tasks in the provided TaskList to the storage file.
     *
     * @param tasks TaskList instance containing tasks to write to disk.
     * @throws IOException If writing to the file fails.
     */
    public void save(TaskList tasks) throws IOException {
        save(tasks.getAll());
    }

    /**
     * Parses a single line from the storage file into a Task instance.
     *
     * @param line Raw line text from the storage file.
     * @return Reconstructed Task instance.
     * @throws FoodieloverException If the line format is malformed or invalid.
     */
    private Task parseTaskLine(String line) throws FoodieloverException {
        String[] parts = line.split("\\s*\\|\\s*", -1);
        if (parts.length < 3) {
            throw new FoodieloverException("Malformed line: insufficient fields.");
        }

        String type = parts[0].trim();
        String isDoneStr = parts[1].trim();
        String description = parts[2].trim();

        if (!isDoneStr.equals("0") && !isDoneStr.equals("1")) {
            throw new FoodieloverException("Malformed line: invalid completion status.");
        }
        if (description.isEmpty()) {
            throw new FoodieloverException("Malformed line: empty task description.");
        }

        Task task;
        if (type.equals("T")) {
            task = new Todo(description);
        } else if (type.equals("D")) {
            if (parts.length < 4) {
                throw new FoodieloverException("Malformed line: deadline missing due date.");
            }
            String by = parts[3].trim();
            if (by.isEmpty()) {
                throw new FoodieloverException("Malformed line: deadline has empty due date.");
            }
            task = new Deadline(description, by);
        } else if (type.equals("E")) {
            if (parts.length < 5) {
                throw new FoodieloverException("Malformed line: event missing start or end time.");
            }
            String from = parts[3].trim();
            String to = parts[4].trim();
            if (from.isEmpty() || to.isEmpty()) {
                throw new FoodieloverException("Malformed line: event has empty start or end time.");
            }
            task = new Event(description, from, to);
        } else {
            throw new FoodieloverException("Malformed line: unknown task type '" + type + "'.");
        }

        if (isDoneStr.equals("1")) {
            task.markAsDone();
        }
        return task;
    }
}
