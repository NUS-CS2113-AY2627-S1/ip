package gizmo;

import gizmo.task.Deadline;
import gizmo.task.Event;
import gizmo.task.Task;
import gizmo.task.Todo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads tasks from and saves tasks to Gizmo's data file.
 */
public class Storage {
    private final Path dataFile;

    /** Creates storage backed by the supplied file path. */
    public Storage(String filePath) {
        dataFile = Path.of(filePath);
    }

    /**
     * Saves all tasks to the data file.
     *
     * @throws GizmoException if the file cannot be written
     */
    public void save(List<Task> tasks) throws GizmoException {
        try {
            Path parent = dataFile.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            try (BufferedWriter writer = Files.newBufferedWriter(
                    dataFile, StandardCharsets.UTF_8)) {
                for (Task task : tasks) {
                    writer.write(serializeTask(task));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new GizmoException("Unable to save tasks: " + e.getMessage());
        }
    }

    /**
     * Loads all tasks from the data file.
     * If the file does not exist, an empty list is returned.
     *
     * @return tasks loaded from the data file
     * @throws GizmoException if the file cannot be read or is malformed
     */
    public ArrayList<Task> load() throws GizmoException {
        ArrayList<Task> loadedTasks = new ArrayList<>();
        if (Files.notExists(dataFile)) {
            return loadedTasks;
        }

        try (BufferedReader reader = Files.newBufferedReader(
                dataFile, StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (!line.isBlank()) {
                    loadedTasks.add(deserializeTask(line, lineNumber));
                }
            }
        } catch (IOException e) {
            throw new GizmoException("Unable to load tasks: " + e.getMessage());
        }
        return loadedTasks;
    }

    /** Converts a task to the line format used by the data file. */
    private String serializeTask(Task task) throws GizmoException {
        String completionStatus = task.isDone() ? "1" : "0";
        if (task instanceof Todo) {
            return "T | " + completionStatus + " | " + task.getDescription();
        }
        if (task instanceof Deadline deadline) {
            return "D | " + completionStatus + " | " + task.getDescription()
                    + " | " + deadline.getCompleteBy();
        }
        if (task instanceof Event event) {
            return "E | " + completionStatus + " | " + task.getDescription()
                    + " | " + event.getEventStart() + " | " + event.getEventEnd();
        }
        throw new GizmoException("Unsupported task type in storage.");
    }

    /** Converts one data-file line back into a task. */
    private Task deserializeTask(String line, int lineNumber) throws GizmoException {
        String[] parts = line.split("\\s*\\|\\s*", -1);
        if (parts.length < 3) {
            throw new GizmoException("Invalid data on line " + lineNumber + ".");
        }
        if (!parts[1].equals("0") && !parts[1].equals("1")) {
            throw new GizmoException("Invalid completion status on line " + lineNumber + ".");
        }

        boolean isDone = parts[1].equals("1");
        Task task;
        switch (parts[0]) {
        case "T":
            if (parts.length != 3) {
                throw new GizmoException("Invalid todo on line " + lineNumber + ".");
            }
            task = new Todo(parts[2]);
            break;
        case "D":
            if (parts.length != 4) {
                throw new GizmoException("Invalid deadline on line " + lineNumber + ".");
            }
            task = new Deadline(parts[2], parts[3]);
            break;
        case "E":
            if (parts.length != 5) {
                throw new GizmoException("Invalid event on line " + lineNumber + ".");
            }
            task = new Event(parts[2], parts[3], parts[4]);
            break;
        default:
            throw new GizmoException("Unknown task type on line " + lineNumber + ".");
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
