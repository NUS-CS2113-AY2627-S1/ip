package gigabot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles loading tasks from the hard disk and saving tasks to the hard disk.
 */
public class Storage {
    private String filePath;

    /**
     * Creates a new Storage instance.
     *
     * @param filePath The relative path to the storage file.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads the existing tasks from the storage file on disk.
     *
     * @return An ArrayList of the loaded Task objects.
     * @throws GigaBotException If the file cannot be found or the data is corrupted.
     */
    public ArrayList<Task> load() throws GigaBotException {
        ArrayList<Task> loadedTasks = new ArrayList<>();
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                return loadedTasks;
            }
            Scanner fileScanner = new Scanner(f);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(" \\| ");
                Task loadedTask = null;

                if (parts[0].equals("T")) {
                    loadedTask = new Todo(parts[2]);
                } else if (parts[0].equals("D")) {
                    loadedTask = new Deadline(parts[2], parts[3]);
                } else if (parts[0].equals("E")) {
                    loadedTask = new Event(parts[2], parts[3], parts[4]);
                }

                if (loadedTask != null) {
                    if (parts[1].equals("1")) {
                        loadedTask.markAsDone();
                    }
                    loadedTasks.add(loadedTask);
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            throw new GigaBotException("Data file not found.");
        } catch (Exception e) {
            throw new GigaBotException("Data file corrupted.");
        }
        return loadedTasks;
    }

    /**
     * Saves the current list of tasks to the storage file on disk.
     *
     * @param tasks The TaskList containing the tasks to be saved.
     * @throws GigaBotException If the file cannot be written to.
     */
    public void save(TaskList tasks) throws GigaBotException {
        try {
            File file = new File(filePath);
            File dir = file.getParentFile();
            if (dir != null && !dir.exists()) {
                dir.mkdirs();
            }
            FileWriter fw = new FileWriter(file);
            for (Task task : tasks.getTasks()) {
                fw.write(task.toSaveFormat() + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
            throw new GigaBotException("SYSTEM ERROR: Could not save tasks to disk.");
        }
    }
}