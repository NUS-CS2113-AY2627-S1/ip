package luke.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import luke.task.Deadline;
import luke.task.Event;
import luke.task.Task;
import luke.task.TaskList;
import luke.task.Todo;
import luke.util.DateTimeUtil;
/**
 * Handles saving and loading tasks from the data file.
 */
public class Storage {
    private final String filePath;

    /**
     * Creates a storage object using the specified file path.
     *
     * @param filePath Path of the data file.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves the tasks to the data file.
     *
     * @param tasks Tasks to save.
     * @param taskCount Number of tasks to save.
     * @throws IOException If the data file cannot be written.
     */
    public void save(Task[] tasks, int taskCount) throws IOException {
        Path path = Path.of(filePath);
        Path parentDirectory = path.getParent();

        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        FileWriter writer = new FileWriter(filePath);

        for (int i = 0; i < taskCount; i++) {
            writer.write(tasks[i].toDataString());
            writer.write(System.lineSeparator());
        }

        writer.close();
    }

    /**
     * Loads tasks from the data file into the task list.
     *
     * @param tasks Task list to populate.
     * @throws IOException If the data file cannot be read.
     */
    public void load(TaskList tasks) throws IOException {
        Path path = Path.of(filePath);

        if (!Files.exists(path)) {
            return;
        }

        Scanner scanner = new Scanner(path);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            try {
                Task task = createTaskFromData(line);
                tasks.add(task);
            } catch (RuntimeException e) {
                scanner.close();
                throw new IOException("Invalid task data: " + line, e);
            }
        }

        scanner.close();
    }

    /**
     * Creates a task from one line of stored data.
     *
     * @param line Stored task data.
     * @return Task represented by the stored data.
     */
    private Task createTaskFromData(String line) {
        String[] parts = line.split("\\s*\\|\\s*");

        String taskType = parts[0];
        String status = parts[1];
        String description = parts[2];

        Task task;

        switch (taskType) {
        case "T":
            task = new Todo(description);
            break;

        case "D":
            task = new Deadline(description, DateTimeUtil.parseDate(parts[3]));
            break;

        case "E":
            task = new Event(
                    description,
                    DateTimeUtil.parseDateTime(parts[3]),
                    DateTimeUtil.parseDateTime(parts[4]));
            break;

        default:
            throw new IllegalArgumentException("Unknown task type: " + taskType);
        }

        if (status.equals("1")) {
            task.markAsDone();
        }

        return task;
    }
}
