package duke;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles loading tasks from, and saving tasks to, the data file on disk.
 */
public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the data file, returning an empty list if the
     * file does not exist yet (e.g. first run on a new machine).
     *
     * @return The list of tasks read from disk.
     */
    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return tasks;
        }
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                Task t = parseLine(fileScanner.nextLine());
                if (t != null) {
                    tasks.add(t);
                }
            }
        } catch (FileNotFoundException e) {
            // Already checked existence above; nothing further to do.
        }
        return tasks;
    }

    /**
     * Saves the given tasks to the data file, creating the parent
     * folder first if it does not already exist.
     *
     * @param tasks The tasks to write to disk.
     */
    public void save(ArrayList<Task> tasks) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        try (FileWriter writer = new FileWriter(file)) {
            for (Task t : tasks) {
                writer.write(t.toSaveFormat() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("OOPS!!! Could not save tasks to disk: " + e.getMessage());
        }
    }

    /**
     * Parses one line of the data file into a Task. Returns null if the
     * line is corrupted or in an unrecognized format, so a bad line is
     * skipped rather than crashing the whole load.
     *
     * @param line One line from the data file.
     * @return The parsed task, or null if the line could not be parsed.
     */
    private Task parseLine(String line) {
        try {
            String[] parts = line.split(" \\| ");
            String type = parts[0].trim();
            boolean isDone = parts[1].trim().equals("1");
            String description = parts[2].trim();

            Task t;
            switch (type) {
            case "T":
                t = new Todo(description);
                break;
            case "D":
                t = new Deadline(description, parts[3].trim());
                break;
            case "E":
                t = new Event(description, parts[3].trim(), parts[4].trim());
                break;
            default:
                return null;
            }
            if (isDone) {
                t.markAsDone();
            }
            return t;
        } catch (RuntimeException e) {
            return null;
        }
    }
}
