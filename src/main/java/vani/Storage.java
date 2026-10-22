package vani;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import vani.exception.VaniException;
import vani.task.Deadline;
import vani.task.Event;
import vani.task.Task;
import vani.task.Todo;

/**
 * Handles saving and loading tasks to and from the data file.
 */
public class Storage {
    /**
     * The path of the file used to store task data.
     */
    private final Path dataFile;

    /**
     * The number of corrupted lines skipped by the most recent load operation.
     */
    private int skippedLineCount;

    /**
     * Creates storage for the specified file.
     *
     * @param filePath the path to the task data file
     */
    public Storage(String filePath) {
        dataFile = Paths.get(filePath);
    }

    /**
     * Saves all tasks to the data file.
     *
     * <p>The data directory is created automatically if it does not exist.
     * Each task is stored in a format that records its type, completion status,
     * and task-specific information.</p>
     *
     * @param tasks the list of tasks to save
     * @throws VaniException if the file cannot be written
     */
    public void save(List<Task> tasks) throws VaniException {
        try {
            if (dataFile.getParent() != null) {
                Files.createDirectories(dataFile.getParent());
            }

            StringBuilder data = new StringBuilder();

            for (Task task : tasks) {
                if (task instanceof Todo) {
                    data.append("T | ")
                            .append(task.getStatusIcon().equals("X") ? "1" : "0")
                            .append(" | ")
                            .append(task.getDescription())
                            .append(System.lineSeparator());

                } else if (task instanceof Deadline) {
                    Deadline deadline = (Deadline) task;

                    data.append("D | ")
                            .append(task.getStatusIcon().equals("X") ? "1" : "0")
                            .append(" | ")
                            .append(task.getDescription())
                            .append(" | ")
                            .append(deadline.getBy())
                            .append(System.lineSeparator());

                } else if (task instanceof Event) {
                    Event event = (Event) task;

                    data.append("E | ")
                            .append(task.getStatusIcon().equals("X") ? "1" : "0")
                            .append(" | ")
                            .append(task.getDescription())
                            .append(" | ")
                            .append(event.getFrom())
                            .append(" | ")
                            .append(event.getTo())
                            .append(System.lineSeparator());
                }
            }

            Files.writeString(dataFile, data.toString());

        } catch (IOException e) {
            throw new VaniException("Error saving tasks.");
        }
    }

    /**
     * Loads tasks from the data file.
     *
     * <p>If the data file does not exist, an empty task list is returned.
     * Corrupted lines are skipped so that invalid data does not prevent
     * the remaining valid tasks from being loaded. Their count is available
     * through {@link #getSkippedLineCount()} for the UI to report.</p>
     *
     * @return a list of tasks loaded from the data file
     * @throws VaniException if an existing file cannot be read
     */
    public List<Task> load() throws VaniException {
        List<Task> tasks = new ArrayList<>();
        skippedLineCount = 0;

        if (!Files.exists(dataFile)) {
            return tasks;
        }

        try {
            List<String> lines = Files.readAllLines(dataFile);

            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                Task task = parseTask(line);
                if (task == null) {
                    skippedLineCount++;
                } else {
                    tasks.add(task);
                }
            }

        } catch (IOException e) {
            throw new VaniException("Error loading tasks.");
        }

        return tasks;
    }

    /**
     * Returns the number of corrupted lines skipped during the latest load.
     *
     * @return the number of skipped lines, excluding blank lines
     */
    public int getSkippedLineCount() {
        return skippedLineCount;
    }

    /**
     * Reconstructs a task from one line of the storage format.
     *
     * @param line the saved task data
     * @return the reconstructed task, or null if the line is corrupted
     */
    private Task parseTask(String line) {
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            return null;
        }

        String type = parts[0];
        String status = parts[1];
        if (!status.equals("0") && !status.equals("1")) {
            return null;
        }

        Task task;
        if (type.equals("T") && parts.length == 3) {
            task = new Todo(parts[2]);
        } else if (type.equals("D") && parts.length == 4) {
            task = new Deadline(parts[2], parts[3]);
        } else if (type.equals("E") && parts.length == 5) {
            task = new Event(parts[2], parts[3], parts[4]);
        } else {
            return null;
        }

        if (status.equals("1")) {
            task.markAsDone();
        }
        return task;
    }
}
