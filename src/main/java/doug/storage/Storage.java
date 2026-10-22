package doug.storage;

import doug.errors.DougException;
import doug.task.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads tasks from and saves tasks to a text file.
 */
public class Storage {
    /** Path of the file used to persist task data. */
    private Path dataPath;

    /**
     * Creates a storage manager for the specified file.
     *
     * @param filePath path to the task data file
     */
    public Storage(String filePath) {
        this.dataPath = Path.of(filePath);
    }

    /**
     * Loads all tasks from the data file, creating the file and its parent directories
     * when they do not yet exist.
     *
     * @return tasks reconstructed from the data file, in their saved order
     * @throws DougException if the file cannot be read or contains invalid task data
     */
    public ArrayList<Task> load() throws DougException {
        ArrayList<Task> taskList = new ArrayList<>();
        try {
            if (dataPath.getParent() != null)
                Files.createDirectories(dataPath.getParent());

            if (!Files.exists(dataPath))
                Files.createFile(dataPath);

            List<String> savedLines = Files.readAllLines(dataPath);
            for (int i = 0; i < savedLines.size(); ++i) {
                Task task = Task.fromSaveFormat(savedLines.get(i));
                taskList.add(task);
            }

            return taskList;

        } catch (IOException e) {
            throw new DougException("Could not read save file.");
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DougException("Save file corrupted.");
        }
    }

    /**
     * Replaces the contents of the data file with the supplied tasks.
     * The data file and its parent directories are created when necessary.
     *
     * @param taskList tasks to save, in the order they should be written
     * @throws DougException if the tasks cannot be written to the data file
     */
    public void save(ArrayList<Task> taskList) {
        try {
            if (dataPath.getParent() != null)
                Files.createDirectories(dataPath.getParent());

            if (!Files.exists(dataPath))
                Files.createFile(dataPath);

            ArrayList<String> saveLines = new ArrayList<>();
            for (int i = 0; i < taskList.size(); ++i) {
                String saveFormat = taskList.get(i).toSaveFormat();
                saveLines.add(saveFormat);
            }

            Files.write(dataPath, saveLines);

        } catch (IOException e) {
            throw new DougException("Failed to save tasks.");
        }

    }
}
