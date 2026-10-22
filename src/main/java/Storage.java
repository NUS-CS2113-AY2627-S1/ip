import task.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles saving and loading of tasks from the hard disk.
 * Uses a plain text file with pipe-separated fields.
 */
public class Storage {
    private final String filePath;

    /**
     * Constructs a Storage object with the given file path.
     *
     * @param filePath The path to the data file.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves all tasks to the file.
     * Creates the parent directory if it does not exist.
     *
     * @param tasks The list of tasks to save.
     */
    public void save(ArrayList<Task> tasks) {
        try {
            File file = new File(filePath);
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            FileWriter writer = new FileWriter(file);
            for (Task task: tasks) {
                writer.write(task.toFileFormat() + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println(" Warning: Unable to save tasks to file.");
        }
    }

    /**
     * Loads tasks from the file.
     * Returns an empty list if the file does not exist.
     *
     * @return The list of loaded tasks.
     * @throws IrriException If the file cannot be read.
     */
    public ArrayList<Task> load() throws IrriException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return tasks;
        }

        try {
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                Task task = parseTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            throw new IrriException(" Warning: Unable to load tasks from file.");
        }

        return tasks;
    }

    /**
     * Parses a single line from the data file into a Task object.
     *
     * @param line The line to parse.
     * @return The parsed Task, or null if the line is invalid.
     */
    private Task parseTask(String line) {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            return null;
        }
        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task;
        switch (type) {
            case "T":
                task = new ToDo(description);
                break;
            case "D":
                if (parts.length < 4) {
                    return null;
                }
                task = new Deadline(description, parts[3]);
                break;
            case "E":
                if (parts.length < 5) {
                    return null;
                }
                task = new Event(description, parts[3], parts[4]);
                break;
            default:
                return null;
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
