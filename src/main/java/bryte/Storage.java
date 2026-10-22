package bryte;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import bryte.exception.BryteException;
import bryte.task.Deadline;
import bryte.task.Event;
import bryte.task.Task;
import bryte.task.Todo;

/**
 * Handles loading tasks from the file and saving tasks to the file.
 */
public class Storage {
    private String dirPath;
    private String filePath;

    /**
     * Constructs a Storage object.
     *
     * @param dirPath  The directory where the data file is stored.
     * @param filePath The name of the file to store the data.
     */
    public Storage(String dirPath, String filePath) {
        this.dirPath = dirPath;
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the file.
     *
     * @return An ArrayList of tasks loaded from the file.
     * @throws BryteException If the file is not found or corrupted.
     */
    public ArrayList<Task> load() throws BryteException {
        ArrayList<Task> loadedTasks = new ArrayList<>();
        File file = new File(dirPath, filePath);

        if (!file.exists()) {
            return loadedTasks;
        }

        try {
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNext()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(" \\| ");
                if (parts.length < 3) {
                    continue;
                }

                String type = parts[0];
                boolean isDone = parts[1].equals("1");
                String description = parts[2];

                Task task = null;
                switch (type) {
                    case "T":
                        task = new Todo(description);
                        break;
                    case "D":
                        if (parts.length >= 4) {
                            task = new Deadline(description, parts[3]);
                        }
                        break;
                    case "E":
                        if (parts.length >= 5) {
                            task = new Event(description, parts[3], parts[4]);
                        }
                        break;
                    default:
                        continue; // Unknown task type
                }

                if (task != null) {
                    task.setDone(isDone);
                    loadedTasks.add(task);
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            throw new BryteException("Data file not found: " + e.getMessage());
        } catch (Exception e) {
            throw new BryteException("Data file is corrupted. Starting with an empty task list.");
        }

        return loadedTasks;
    }

    /**
     * Saves tasks to the file.
     *
     * @param tasks The list of tasks to be saved.
     * @throws BryteException If there is an error writing to the file.
     */
    public void save(ArrayList<Task> tasks) throws BryteException {
        try {
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dirPath, filePath);
            FileWriter fw = new FileWriter(file);
            for (Task task : tasks) {
                fw.write(task.toFileFormat() + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
            throw new BryteException("Error saving tasks to file: " + e.getMessage());
        }
    }
}
