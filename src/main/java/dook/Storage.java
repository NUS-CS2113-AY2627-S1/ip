package dook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Handles saving tasks to and loading tasks from the task data file.
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates a Storage instance for the specified file.
     *
     * @param filePath the path to the file used to store tasks
     */
    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    /**
     * Saves all tasks to the task data file.
     *
     * @param tasks the task list to save
     * @throws DookException if the tasks cannot be saved
     */
    public void saveTasks(TaskList tasks) throws DookException {
        StringBuilder data = new StringBuilder();
        for (Task task : tasks) {
            if (task instanceof Todo) {
                data.append("T | ")
                    .append(task.isDone() ? "1" : "0")
                    .append(" | ")
                    .append(task.getDescription())
                    .append("\n");
            } else if (task instanceof Deadline) {
                Deadline deadline = (Deadline) task;
                data.append("D | ")
                    .append(task.isDone() ? "1" : "0")
                    .append(" | ")
                    .append(task.getDescription())
                    .append(" | ")
                    .append(deadline.getBy())
                    .append("\n");
            } else if (task instanceof Event) {
                Event event = (Event) task;
                data.append("E | ")
                    .append(task.isDone() ? "1" : "0")
                    .append(" | ")
                    .append(task.getDescription())
                    .append(" | ")
                    .append(event.getFrom())
                    .append(" | ")
                    .append(event.getTo())
                    .append("\n");
            }
        }
        try {
            Files.createDirectories(filePath.getParent());
            Files.writeString(filePath, data.toString());
        } catch (IOException e) {
            throw new DookException("Could not save tasks.");
        }
    }

    /**
     * Loads tasks from the task data file.
     *
     * @return the loaded task list, or an empty task list if the file does not exist
     * @throws DookException if the file cannot be read or contains invalid task data
     */
    public TaskList loadTasks() throws DookException {
        if (!Files.exists(filePath)) {
            return new TaskList();
        }
        String data;
        try {
            data = Files.readString(filePath);
        } catch (IOException e) {
            throw new DookException("Could not load tasks.");
        }
        if (data.isBlank()) {
            return new TaskList();
        }
        String[] lines = data.split("\\R");
        TaskList tasks = new TaskList();
        for (String line : lines) {
            // Split each saved line into its task type, completion status, and task details.
            String[] parts = line.split(" \\| ");
            if (parts.length < 3) {
                throw new DookException("The data file contains invalid task data.");
            }
            String type = parts[0];
            String status = parts[1];
            if (!status.equals("0") && !status.equals("1")) {
                throw new DookException("The data file contains an invalid task status.");
            }
            Task task;
            if (type.equals("T")) {
                if (parts.length != 3) {
                    throw new DookException("The data file contains an invalid todo.");
                }
                task = new Todo(parts[2]);
            } else if (type.equals("D")) {
                if (parts.length != 4) {
                    throw new DookException("The data file contains an invalid deadline.");
                }
                task = new Deadline(parts[2], parts[3]);
            } else if (type.equals("E")) {
                if (parts.length != 5) {
                    throw new DookException("The data file contains an invalid event.");
                }
                task = new Event(parts[2], parts[3], parts[4]);
            } else {
                throw new DookException("The data file contains an invalid task type.");
            }
            if (status.equals("1")) {
                task.markAsDone();
            }
            tasks.addTask(task);
        }
        return tasks;
    }
}
