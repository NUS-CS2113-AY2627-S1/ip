package storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import tasks.Deadline;
import tasks.Event;
import tasks.Task;
import tasks.Todo;

/**
 * Loads and saves tasks using the application's existing text file format.
 */
public class Storage {
    private static final Path FILE_PATH = Paths.get("data", "samsquare.txt");

    /** Whether unparseable saved deadlines require a backup before the next write. */
    private static boolean needsDateBackup;

    /**
     * Saves tasks in their current order, reporting file errors to the console.
     * When loading skipped unparseable deadline dates, first preserves the original
     * file in a unique backup. A backup failure aborts the save.
     *
     * @param tasks Tasks to save, including their completion status.
     */
    public static void save(List<Task> tasks) {
        try {
            Files.createDirectories(FILE_PATH.getParent());

            if (needsDateBackup) {
                // A unique backup avoids overwriting an earlier migration backup.
                Path backup = Files.createTempFile(FILE_PATH.getParent(), "samsquare-legacy-dates-", ".txt");
                Files.copy(FILE_PATH, backup, StandardCopyOption.REPLACE_EXISTING);
                needsDateBackup = false;
            }

            BufferedWriter writer = Files.newBufferedWriter(FILE_PATH);

            for (Task task : tasks) {
                String status = task.isDone() ? "1" : "0";

                if (task instanceof Deadline) {
                    Deadline deadline = (Deadline) task;

                    writer.write("D | " + status + " | "
                            + deadline.getDescription() + " | "
                            + deadline.getBy());

                } else if (task instanceof Event) {
                    Event event = (Event) task;

                    writer.write("E | " + status + " | "
                            + event.getDescription() + " | "
                            + event.getFrom() + " | "
                            + event.getTo());
                } else {
                    writer.write("T | " + status + " | "
                            + task.getDescription());
                }

                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {
            System.out.println("Error saving tasks.");
        }
    }

    /**
     * Loads saved tasks, skipping malformed lines and reporting file errors.
     * Invalid saved deadline dates produce a notice and require a backup before
     * the next save, allowing the user to recover older free-text deadlines.
     *
     * @return Tasks read successfully, or an empty list if the file does not exist.
     */
    public static List<Task> load() {
        needsDateBackup = false;
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(FILE_PATH)) {
            return tasks;
        }

        try {
            BufferedReader reader = Files.newBufferedReader(FILE_PATH);

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(" \\| ");

                try {
                    String type = parts[0];
                    String status = parts[1];

                    Task loadedTask;

                    if (type.equals("T")) {
                        if (parts.length != 3
                                || parts[2].isEmpty())  {
                            continue;
                        }

                        loadedTask = new Todo(parts[2]);

                    } else if (type.equals("D")) {

                        if (parts.length != 4
                                || parts[2].isEmpty()
                                || parts[3].isEmpty()) {
                            continue;
                        }

                        loadedTask = new Deadline(parts[2], LocalDate.parse(parts[3]));

                    } else if (type.equals("E")) {

                        if (parts.length != 5
                                || parts[2].isEmpty()
                                || parts[3].isEmpty()
                                || parts[4].isEmpty()) {
                            continue;
                        }

                        loadedTask = new Event(parts[2], parts[3], parts[4]);

                    } else {
                        continue;
                    }

                    if (status.equals("1")) {
                        loadedTask.markAsDone();

                    } else if (!status.equals("0")) {
                        continue;
                    }

                    tasks.add(loadedTask);

                } catch (DateTimeParseException e) {
                    needsDateBackup = true;
                } catch (ArrayIndexOutOfBoundsException e) {
                    // this is to ignore malformed line and continue loading
                }
            }

            reader.close();

            if (needsDateBackup) {
                System.out.println("Some saved deadlines have invalid dates and were not loaded. "
                        + "Original file will be backed up in data/samsquare-legacy-dates-*.txt before saving.");
            }

        } catch (IOException e) {
            System.out.println("Error loading tasks.");
        }
        return tasks;
    }
}
