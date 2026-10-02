package storage;

import list.TaskList;
import task.Deadline;
import task.Event;
import task.Task;
import task.Todo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles persistent storage of tasks to and from the local filesystem.
 */
public class Storage {
    private static final String DELIMITER_REGEX = " \\| ";
    private static final String TYPE_TODO = "T";
    private static final String TYPE_DEADLINE = "D";
    private static final String TYPE_EVENT = "E";
    private static final String STATUS_DONE = "1";

    private final String filePath;

    /**
     * Constructs a Storage instance with automatic project root path resolution.
     *
     * @param relativePath The relative path of the file to save/load.
     */
    public Storage(String relativePath) {
        Path userDirPath = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
        
        // If VS Code launches from inside src/main/java or src, find the project root containing build.gradle
        Path rootPath = userDirPath;
        while (rootPath != null && !rootPath.resolve("build.gradle").toFile().exists()) {
            if (rootPath.getParent() == null) {
                break;
            }
            rootPath = rootPath.getParent();
        }

        if (rootPath != null && rootPath.resolve("build.gradle").toFile().exists()) {
            this.filePath = rootPath.resolve(relativePath).toString();
        } else {
            this.filePath = userDirPath.resolve(relativePath).toString();
        }
    }

    /**
     * Loads saved tasks from the data file on disk.
     *
     * @return An ArrayList containing all valid tasks retrieved from disk.
     */
    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return tasks;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                Task task = parseLineToTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading storage file: " + e.getMessage());
        }

        return tasks;
    }

    private Task parseLineToTask(String line) {
        String[] parts = line.split(DELIMITER_REGEX);
        if (parts.length < 3) {
            return null;
        }

        String type = parts[0].trim();
        boolean isDone = parts[1].trim().equals(STATUS_DONE);
        String description = parts[2].trim();

        Task task = createTaskFromType(type, description, parts);
        if (task != null && isDone) {
            task.markAsDone();
        }
        return task;
    }

    private Task createTaskFromType(String type, String description, String[] parts) {
        try {
            switch (type) {
            case TYPE_TODO:
                return new Todo(description);
            case TYPE_DEADLINE:
                if (parts.length < 4) {
                    return null;
                }
                return new Deadline(description, LocalDate.parse(parts[3].trim()));
            case TYPE_EVENT:
                if (parts.length < 5) {
                    return null;
                }
                return new Event(description,
                        LocalDate.parse(parts[3].trim()),
                        LocalDate.parse(parts[4].trim()));
            default:
                return null;
            }
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Writes all tasks in the given TaskList to the storage file.
     * Creates parent directories if they do not already exist.
     *
     * @param list The TaskList containing tasks to be saved.
     */
    public void save(TaskList list) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {
            for (int i = 0; i < list.size(); i++) {
                writer.write(list.getItem(i).toFileFormat() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Error writing to storage file: " + e.getMessage());
        }
    }
}