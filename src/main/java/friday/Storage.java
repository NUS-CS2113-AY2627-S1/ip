package friday;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Deals with loading tasks from the save file and saving tasks back to it.
 * Each {@code Storage} is bound to one file path for its lifetime.
 */
public class Storage {

    private final String filePath;

    /**
     * Creates a storage bound to the given file.
     *
     * @param filePath path of the save file (it need not exist yet)
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the save file into a new list. If the file doesn't
     * exist yet (e.g. first run), returns an empty list. Lines that can't
     * be parsed are skipped, with a warning reported via {@code ui}.
     */
    public ArrayList<Task> load(Ui ui) {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return tasks;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();

                try {
                    tasks.add(parseTaskFromFile(line));
                } catch (RuntimeException e) {
                    ui.showMessage("Warning: skipping corrupted line in save file: " + line);
                }
            }
        } catch (IOException e) {
            ui.showMessage("Warning: could not load tasks from disk (" + e.getMessage() + ").");
        }

        return tasks;
    }

    /**
     * Writes the given tasks to the save file, one task per line,
     * overwriting whatever was there before. Creates the parent folder
     * (e.g. "data") if it doesn't already exist. Reports any failure
     * via {@code ui}.
     */
    public void save(ArrayList<Task> tasks, Ui ui) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();

        if (parentDir != null) {
            parentDir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {
            for (Task task : tasks) {
                writer.write(task.toFileFormat() + System.lineSeparator());
            }
        } catch (IOException e) {
            ui.showMessage("Warning: could not save tasks to disk (" + e.getMessage() + ").");
        }
    }

    /**
     * Parses one line of the save file (e.g. "D | 0 | return book | 2019-12-02")
     * back into a Task. Throws an unchecked exception if the line is
     * malformed (including a deadline date that isn't valid ISO
     * yyyy-MM-dd), which the caller treats as a corrupted line to skip.
     */
    private Task parseTaskFromFile(String line) {
        String[] parts = line.split(" \\| ");
        String type = parts[0];
        boolean isDone = "1".equals(parts[1]);
        String description = parts[2];

        Task task;

        if ("T".equals(type)) {
            task = new Todo(description);
        } else if ("D".equals(type)) {
            task = new Deadline(description, LocalDate.parse(parts[3]));
        } else if ("E".equals(type)) {
            task = new Event(description, parts[3], parts[4]);
        } else {
            throw new IllegalArgumentException("Unknown task type: " + type);
        }

        if (isDone) {
            task.changeStatus("mark");
        }

        return task;
    }
}
