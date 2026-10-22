package cole;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles loading tasks from, and saving tasks to, a data file on disk.
 */
public class Storage {

    private final String filePath;

    /**
     * Creates a Storage that reads from and writes to the given file path.
     *
     * @param filePath relative path to the data file, e.g. "./data/cole.txt"
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the data file. If the file (or its parent folder)
     * does not exist yet, an empty list is returned instead of throwing.
     *
     * @return the list of tasks read from disk
     */
    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return tasks;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                Task task = parseTask(fileScanner.nextLine());
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            System.out.println("OOPS!!! Could not read the data file: " + e.getMessage());
        }

        return tasks;
    }

    /**
     * Saves the given tasks to the data file, overwriting any previous content.
     * Creates the parent folder first if it does not exist yet.
     *
     * @param tasks the list of tasks to persist
     */
    public void save(ArrayList<Task> tasks) {
        File file = new File(filePath);
        File parentFolder = file.getParentFile();

        if (parentFolder != null && !parentFolder.exists()) {
            parentFolder.mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {
            for (Task task : tasks) {
                writer.write(task.toSaveFormat() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("OOPS!!! Could not save the data file: " + e.getMessage());
        }
    }

    /**
     * Parses one line of the data file into a Task.
     * Returns null if the line is corrupted (missing fields, unknown type or an
     * invalid deadline date), so the caller can skip it instead of crashing.
     *
     * @param line one line of the data file, e.g. "D | 0 | return book | 2019-10-15"
     * @return the task, or null if the line cannot be read
     */
    private Task parseTask(String line) {
        String[] parts = line.split(" \\| ");

        if (parts.length < 3) {
            return null;
        }

        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task = null;
        if (type.equals("T")) {
            task = new ToDo(description);
        } else if (type.equals("D") && parts.length >= 4) {
            try {
                task = new Deadline(description, LocalDate.parse(parts[3]));
            } catch (DateTimeParseException e) {
                return null;
            }
        } else if (type.equals("E") && parts.length >= 5) {
            task = new Event(description, parts[3], parts[4]);
        }

        if (task != null && isDone) {
            task.markAsDone();
        }
        return task;
    }
}