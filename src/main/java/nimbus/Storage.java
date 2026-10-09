package nimbus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves and loads Nimbus tasks using a local text file.
 */
public class Storage {
    private static final Path FILE_PATH = Path.of("data", "nimbus.txt");

    /**
     * Saves tasks through a temporary file before replacing the saved data.
     *
     * @param tasks Tasks to save.
     * @throws IllegalStateException If saving fails.
     */
    public static void saveTasks(ArrayList<Task> tasks) {
        Path temporaryFile = null;

        try {
            Files.createDirectories(FILE_PATH.getParent());

            List<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(toRecord(task));
            }

            temporaryFile = Files.createTempFile(
                    FILE_PATH.getParent(), "nimbus-", ".tmp");
            Files.write(temporaryFile, lines, StandardCharsets.UTF_8);
            Files.move(temporaryFile, FILE_PATH,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Filing system down. Your latest change is in memory only.\n"
                            + "Check that data/nimbus.txt and its folder are writable.\n"
                            + "A later successful task change will save the current list.",
                    exception);
        } finally {
            if (temporaryFile != null) {
                try {
                    Files.deleteIfExists(temporaryFile);
                } catch (IOException exception) {
                    // A leftover temporary file does not affect task loading.
                }
            }
        }
    }

    /**
     * Converts a task into a record for the data file.
     */
    private static String toRecord(Task task) {
        String record = task.getTypeIcon()
                + " | " + (task.isDone ? "1" : "0")
                + " | " + task.getDescription();

        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return record + " | " + deadline.by;
        }

        if (task instanceof Event) {
            Event event = (Event) task;
            return record + " | " + event.from + " | " + event.to;
        }

        return record;
    }

    /**
     * Loads tasks, returning an empty list when no data file exists.
     * Rejects malformed records without modifying the original file.
     *
     * @return Previously saved tasks.
     * @throws IllegalStateException If the file cannot be read or is malformed.
     */
    public static ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();

        if (Files.notExists(FILE_PATH)) {
            return tasks;
        }

        try {
            List<String> lines = Files.readAllLines(
                    FILE_PATH, StandardCharsets.UTF_8);

            for (int index = 0; index < lines.size(); index++) {
                String line = lines.get(index);

                if (line.isBlank()) {
                    continue;
                }

                try {
                    tasks.add(parseRecord(line));
                } catch (IllegalArgumentException exception) {
                    throw new IllegalStateException(
                            "Wait a second... saved task data is damaged at line "
                                    + (index + 1) + ".\n"
                                    + "Nimbus stopped to protect your saved tasks.\n"
                                    + "Back up data/nimbus.txt, then repair that line "
                                    + "or restore a working backup.",
                            exception);
                }
            }
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "I couldn't read data/nimbus.txt.\n"
                            + "Check the file permissions before starting Nimbus again.",
                    exception);
        }

        return tasks;
    }

    /**
     * Validates a saved record and restores its task and completion status.
     */
    private static Task parseRecord(String line) {
        String[] parts = line.split(" \\| ", -1);
        int expectedFields;

        switch (parts[0]) {
            case "T":
                expectedFields = 3;
                break;
            case "D":
                expectedFields = 4;
                break;
            case "E":
                expectedFields = 5;
                break;
            default:
                throw new IllegalArgumentException("Unknown task type.");
        }

        if (parts.length != expectedFields) {
            throw new IllegalArgumentException("Incorrect number of fields.");
        }

        if (!parts[1].equals("0") && !parts[1].equals("1")) {
            throw new IllegalArgumentException("Invalid completion status.");
        }

        for (int index = 2; index < parts.length; index++) {
            if (parts[index].isBlank() || parts[index].contains("|")) {
                throw new IllegalArgumentException("Invalid task field.");
            }
        }

        Task task;
        switch (parts[0]) {
            case "T":
                task = new Todo(parts[2]);
                break;
            case "D":
                task = new Deadline(parts[2], parts[3]);
                break;
            case "E":
                task = new Event(parts[2], parts[3], parts[4]);
                break;
            default:
                throw new IllegalArgumentException("Unknown task type.");
        }

        if (parts[1].equals("1")) {
            task.markAsDone();
        }

        return task;
    }
}