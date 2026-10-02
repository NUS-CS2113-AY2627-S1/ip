package chre.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import chre.task.Task;
import chre.task.Todo;
import chre.task.Deadline;
import chre.task.Event;
import chre.exception.ChreException;
import chre.util.DateUtil;

/**
 * Storage handles loading and saving tasks from/to the hard disk.
 * Uses a pipe-separated format: TYPE | IS_DONE | task details
 */
public class Storage {
    private static final String DEFAULT_FILE_PATH = "data/chre.txt";

    /**
     * Constructs a Storage instance with the default file path.
     */
    public Storage() {
        this(DEFAULT_FILE_PATH);
    }

    /**
     * Constructs a Storage instance with a specified file path.
     *
     * @param filePath the path to the data file
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    private String filePath;

    /**
     * Loads tasks from the data file.
     *
     * @return a list of loaded tasks
     * @throws ChreException if an error occurs while reading the file
     */
    public List<Task> load() throws ChreException {
        List<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return tasks;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = parseTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            throw new ChreException("Error reading data file: " + e.getMessage());
        }

        return tasks;
    }

    /**
     * Saves all tasks to the data file.
     *
     * @param tasks the list of tasks to save
     * @throws ChreException if an error occurs while writing to the file
     */
    public void save(List<Task> tasks) throws ChreException {
        try {
            String[] pathParts = filePath.split("/");
            String dirPath = String.join(File.separator, pathParts);
            int lastSeparator = dirPath.lastIndexOf(File.separator);

            if (lastSeparator > 0) {
                String directory = dirPath.substring(0, lastSeparator);
                Files.createDirectories(Paths.get(directory));
            }

            try (FileWriter writer = new FileWriter(filePath)) {
                for (Task task : tasks) {
                    writer.write(taskToString(task) + "\n");
                }
            }
        } catch (IOException e) {
            throw new ChreException("Error saving data: " + e.getMessage());
        }
    }

    /**
     * Converts a task to its string representation for storage.
     *
     * @param task the task to convert
     * @return the string representation
     */
    private String taskToString(Task task) {
        String type = getTaskType(task);
        int isDone = task.isDone() ? 1 : 0;
        return type + " | " + isDone + " | " + task.getName() + getTaskDetails(task);
    }

    /**
     * Gets the type code for a task (T, D, or E).
     *
     * @param task the task
     * @return the type code
     */
    private String getTaskType(Task task) {
        if (task instanceof Todo) {
            return "T";
        } else if (task instanceof Deadline) {
            return "D";
        } else if (task instanceof Event) {
            return "E";
        }
        return "T";
    }

    /**
     * Gets additional details for a task (deadline or event times).
     *
     * @param task the task
     * @return the additional details
     */
    private String getTaskDetails(Task task) {
        if (task instanceof Deadline) {
            LocalDate by = ((Deadline) task).getBy();
            return " | " + by.toString();
        } else if (task instanceof Event) {
            LocalDate from = ((Event) task).getFrom();
            LocalDate to = ((Event) task).getTo();
            return " | " + from.toString() + " | " + to.toString();
        }
        return "";
    }

    /**
     * Parses a task from its string representation.
     *
     * @param line the line to parse
     * @return the parsed task, or null if the line is invalid
     */
    private Task parseTask(String line) {
        try {
            String[] parts = line.split(" \\| ");
            if (parts.length < 3) {
                return null;
            }

            String type = parts[0].trim();
            int isDone = Integer.parseInt(parts[1].trim());
            String name = parts[2].trim();

            Task task;
            if (type.equals("T")) {
                task = new Todo(name);
            } else if (type.equals("D")) {
                if (parts.length < 4) {
                    return null;
                }
                try {
                    LocalDate by = LocalDate.parse(parts[3].trim());
                    task = new Deadline(name, by);
                } catch (Exception e) {
                    return null;
                }
            } else if (type.equals("E")) {
                if (parts.length < 5) {
                    return null;
                }
                try {
                    LocalDate from = LocalDate.parse(parts[3].trim());
                    LocalDate to = LocalDate.parse(parts[4].trim());
                    task = new Event(name, from, to);
                } catch (Exception e) {
                    return null;
                }
            } else {
                return null;
            }

            if (isDone == 1) {
                task.setDone(true);
            }

            return task;
        } catch (Exception e) {
            return null;
        }
    }
}
