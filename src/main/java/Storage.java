import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles reading from and writing to the storage data file.
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates storage that reads and writes at the given file path.
     *
     * @param filePathString path to the task data file
     */
    public Storage(String filePathString) {
        this.filePath = Path.of(filePathString);
    }

    /**
     * Loads tasks from storage, returning an empty list when no file exists.
     *
     * @return tasks reconstructed from the data file
     * @throws CS2113BotException if the file cannot be read or contains invalid task data
     */
    public ArrayList<Task> load() throws CS2113BotException {
        ArrayList<Task> loadedList = new ArrayList<>();
        try {
            if (!Files.exists(filePath)) {
                return loadedList;
            }
            if (!Files.isRegularFile(filePath)) {
                throw new CS2113BotException("Task data path is not a file: " + filePath + ".");
            }

            int lineNumber = 0;
            try (Scanner fileScanner = new Scanner(filePath, StandardCharsets.UTF_8)) {
                while (fileScanner.hasNextLine()) {
                    lineNumber++;
                    String line = fileScanner.nextLine().trim();
                    if (line.isEmpty()) {
                        continue;
                    }
                    loadedList.add(parseSavedTask(line, lineNumber));
                }
            }
        } catch (IOException | InvalidPathException | SecurityException e) {
            throw new CS2113BotException("Unable to read tasks from " + filePath + ".");
        }
        return loadedList;
    }

    /**
     * Saves all tasks to the data file, creating parent directories when required.
     *
     * @param taskList tasks to persist
     * @throws CS2113BotException if the tasks cannot be saved
     */
    public void save(ArrayList<Task> taskList) throws CS2113BotException {
        StringBuilder savedTasks = new StringBuilder();
        for (Task task : taskList) {
            savedTasks.append(formatTask(task)).append(System.lineSeparator());
        }

        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            Files.writeString(filePath, savedTasks.toString(), StandardCharsets.UTF_8);
        } catch (IOException | SecurityException e) {
            throw new CS2113BotException("Unable to save tasks to " + filePath + ".");
        }
    }

    /**
     * Recreates one task from its serialized storage representation.
     *
     * @param line serialized task data
     * @param lineNumber line number used in validation messages
     * @return reconstructed task
     * @throws CS2113BotException if the saved data is invalid
     */
    private Task parseSavedTask(String line, int lineNumber) throws CS2113BotException {
        String[] parts = line.split(" \\| ", -1);
        if (parts.length < 3 || (!parts[1].equals("0") && !parts[1].equals("1"))) {
            throw new CS2113BotException("Invalid task data on line " + lineNumber + ".");
        }

        Task task;
        if (parts[0].equals("T") && parts.length == 3) {
            validateTaskField(parts[2], "Task description on line " + lineNumber);
            task = new Todo(parts[2]);
        } else if (parts[0].equals("D") && parts.length == 4) {
            validateTaskField(parts[2], "Task description on line " + lineNumber);
            validateTaskField(parts[3], "Deadline date on line " + lineNumber);
            task = new Deadline(parts[2], parts[3]);
        } else if (parts[0].equals("E") && parts.length == 5) {
            validateTaskField(parts[2], "Task description on line " + lineNumber);
            validateTaskField(parts[3], "Event start time on line " + lineNumber);
            validateTaskField(parts[4], "Event end time on line " + lineNumber);
            task = new Event(parts[2], parts[3], parts[4]);
        } else {
            throw new CS2113BotException("Invalid task data on line " + lineNumber + ".");
        }
        task.setDone(parts[1].equals("1"));
        return task;
    }

    /**
     * Converts a task to the line format used by the storage file.
     *
     * @param task task to serialize
     * @return serialized task data
     * @throws CS2113BotException if the task cannot be stored safely
     */
    private String formatTask(Task task) throws CS2113BotException {
        if (task == null) {
            throw new CS2113BotException("Unable to save an empty task.");
        }

        String status = task.isDone ? "1" : "0";
        validateTaskField(task.description, "Task description");
        if (task instanceof Todo) {
            return "T | " + status + " | " + task.description;
        }
        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            validateTaskField(deadline.by, "Deadline date");
            return "D | " + status + " | " + deadline.description + " | " + deadline.by;
        }
        if (task instanceof Event) {
            Event event = (Event) task;
            validateTaskField(event.from, "Event start time");
            validateTaskField(event.to, "Event end time");
            return "E | " + status + " | " + event.description + " | " + event.from + " | " + event.to;
        }
        throw new CS2113BotException("Unable to save an unknown task type.");
    }

    /**
     * Checks that a task field can be stored without corrupting the file format.
     *
     * @param value value to validate
     * @param fieldName human-readable name used in validation messages
     * @throws CS2113BotException if the value is empty or contains reserved separators
     */
    public static void validateTaskField(String value, String fieldName) throws CS2113BotException {
        if (value == null || value.trim().isEmpty()) {
            throw new CS2113BotException(fieldName + " cannot be empty.");
        }
        if (value.contains(" | ") || value.contains("\n") || value.contains("\r")) {
            throw new CS2113BotException(fieldName + " cannot contain ' | ' or a line break.");
        }
    }
}

