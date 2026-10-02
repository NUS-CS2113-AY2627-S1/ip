import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads tasks from a file and saves tasks to a file.
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates storage that uses the specified file.
     *
     * @param filePath path of the task data file
     */
    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    /**
     * Loads all tasks from the data file.
     *
     * @return tasks stored in the file, or an empty list if it does not exist
     * @throws IOException if the file cannot be read or contains invalid data
     */
    public ArrayList<Task> loadTasks() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return tasks;
        }

        for (String line : Files.readAllLines(filePath)) {
            String[] parts = line.split(" \\| ", -1);
            Task task;

            try {
                task = switch (parts[0]) {
                    case "T" -> new ToDo(parts[2]);
                    case "D" -> new Deadline(parts[2], parts[3]);
                    case "E" -> new Event(parts[2], parts[3], parts[4]);
                    default -> throw new IllegalArgumentException();
                };

                if (parts[1].equals("1")) {
                    task.markAsDone();
                }
            } catch (RuntimeException e) {
                throw new IOException("The task data file is invalid.");
            }

            tasks.add(task);
        }

        return tasks;
    }

    /**
     * Saves all tasks to the data file.
     *
     * @param tasks tasks to save
     * @throws IOException if the tasks cannot be written
     */
    public void saveTasks(ArrayList<Task> tasks) throws IOException {
        Files.createDirectories(filePath.getParent());
        List<String> lines = new ArrayList<>();

        for (Task task : tasks) {
            String status = task.isDone() ? "1" : "0";

            if (task instanceof Deadline deadline) {
                lines.add("D | " + status + " | " + task.getDescription()
                        + " | " + deadline.getBy());
            } else if (task instanceof Event event) {
                lines.add("E | " + status + " | " + task.getDescription()
                        + " | " + event.getFrom() + " | " + event.getTo());
            } else {
                lines.add("T | " + status + " | " + task.getDescription());
            }
        }

        Files.write(filePath, lines);
    }
}
