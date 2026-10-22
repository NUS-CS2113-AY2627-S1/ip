import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Saves and loads the current task list to disk.
 *
 * <p>Each task is stored on one line using the task type, completion status,
 * and display text.</p>
 */
public class Storage {
    private final Path filePath;

    /** Creates storage backed by the supplied file path. */
    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /**
     *
     * Writes all current tasks to the data file, creating its parent directory
     * if necessary.
     *
     * @param tasks the task list
     * @param taskCount number of valid tasks in the list
     */
    public void save(TaskList tasks, int taskCount) {
        StringBuilder contents = new StringBuilder();
        for (int i = 0; i < taskCount; i++) {
            Task task = tasks.get(i);
            contents.append(task.getStatusIcon())
                    .append(" | ")
                    .append(task.getDone() ? 1 : 0)
                    .append(" | ")
                    .append(task)
                    .append(System.lineSeparator());
        }

        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.writeString(filePath, contents.toString());
        } catch (IOException e) {
            System.out.println("Unable to save tasks: " + e.getMessage());
        }
    }

    /**
     * Loads tasks from disk. Invalid lines are ignored so one bad record does
     * not prevent the chatbot from starting.
     *
     * @param tasks list to populate
     * @param maximumTasks maximum number of tasks to load
     * @return number of tasks loaded
     */
    public int load(TaskList tasks, int maximumTasks) {
        if (!Files.exists(filePath)) {
            return 0;
        }

        int loaded = 0;
        try {
            for (String line : Files.readAllLines(filePath)) {
                if (loaded == maximumTasks) {
                    System.out.println("Warning: task limit reached; remaining tasks were skipped.");
                    break;
                }
                Task task = parseLine(line);
                if (task != null) {
                    tasks.addLoaded(task);
                    loaded++;
                } else if (!line.trim().isEmpty()) {
                    System.out.println("Warning: skipped malformed task: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to load tasks: " + e.getMessage());
        }
        return loaded;
    }

    private static Task parseLine(String line) {
        String[] parts = line.split("\\s*\\|\\s*", 3);
        if (parts.length != 3 || !parts[1].equals("0") && !parts[1].equals("1")) {
            return null;
        }

        Task task;
        String text = parts[2].trim();
        try {
            switch (parts[0].trim()) {
            case "T":
                task = new Todo(text);
                break;
            case "D":
                int deadlineMarker = text.indexOf(" (by: ");
                if (!text.endsWith(")") || deadlineMarker < 0) return null;
                task = new Deadline(text.substring(0, deadlineMarker),
                        text.substring(deadlineMarker + 6, text.length() - 1));
                break;
            case "E":
                int eventMarker = text.indexOf(" (from: ");
                int eventSeparator = text.indexOf(" to: ", eventMarker);
                if (!text.endsWith(")") || eventMarker < 0 || eventSeparator < 0) return null;
                task = new Event(text.substring(0, eventMarker),
                        text.substring(eventMarker + 8, eventSeparator),
                        text.substring(eventSeparator + 5, text.length() - 1));
                break;
            default:
                return null;
            }
        } catch (RuntimeException e) {
            return null;
        }
        task.setDone(parts[1].equals("1"));
        return task;
    }
}
