package nico;

import nico.tasks.Deadline;
import nico.tasks.Event;
import nico.tasks.Task;
import nico.tasks.TaskDateTime;
import nico.tasks.Todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Saves tasks to, and loads tasks from, a text file on disk.
 * Each task is stored on its own line, e.g. <code>D | 0 | return book | Sunday</code>.
 */
public class Storage {
    private static final int MIN_FIELD_COUNT = 3;
    private static final int DEADLINE_FIELD_COUNT = 4;
    private static final int EVENT_FIELD_COUNT = 5;

    private final File saveFile;

    /**
     * Creates a Storage that reads from and writes to the given file path.
     * The path is relative to the folder the program is run from.
     *
     * @param filePath Location of the save file, e.g. <code>data/nico.txt</code>.
     */
    public Storage(String filePath) {
        this.saveFile = new File(filePath);
    }

    /**
     * Loads the saved tasks. Returns an empty list if there is no save file yet.
     * Lines that cannot be understood (e.g. a corrupted file) are skipped.
     *
     * @return The tasks read from the save file.
     * @throws NicoException If the save file exists but cannot be read.
     */
    public List<Task> load() throws NicoException {
        List<Task> tasks = new ArrayList<>();
        if (!saveFile.exists()) {
            return tasks;
        }

        try (Scanner fileScanner = new Scanner(saveFile)) {
            while (fileScanner.hasNextLine()) {
                Task task = extractTask(fileScanner.nextLine());
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (FileNotFoundException e) {
            throw new NicoException("I couldn't read the save file, starting with an empty list.");
        }
        return tasks;
    }

    /**
     * Writes all tasks to the save file, replacing its previous contents.
     * Creates the folder for the save file if it does not exist yet.
     *
     * @param taskList The tasks to save.
     * @throws NicoException If the file cannot be written.
     */
    public void save(TaskList taskList) throws NicoException {
        File parentFolder = saveFile.getParentFile();
        if (parentFolder != null && !parentFolder.exists()) {
            parentFolder.mkdirs();
        }

        try (FileWriter writer = new FileWriter(saveFile)) {
            for (Task task : taskList.getAllTasks()) {
                writer.write(task.toSaveFormat() + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new NicoException("I couldn't save your tasks to disk: " + e.getMessage());
        }
    }

    /**
     * Converts one line of the save file back into a Task.
     * Returns null if the line is not in a recognised format, or holds an invalid date.
     */
    private Task extractTask(String line) {
        // Pattern.quote is needed because "|" is a special character in regex.
        String[] fields = line.split(Pattern.quote(Task.SAVE_DELIMITER));
        if (fields.length < MIN_FIELD_COUNT) {
            return null;
        }

        String type = fields[0];
        String description = fields[2];
        Task task;
        try {
            if (type.equals("T")) {
                task = new Todo(description);
            } else if (type.equals("D") && fields.length >= DEADLINE_FIELD_COUNT) {
                task = new Deadline(description, TaskDateTime.parse(fields[3]));
            } else if (type.equals("E") && fields.length >= EVENT_FIELD_COUNT) {
                task = new Event(description, TaskDateTime.parse(fields[3]), TaskDateTime.parse(fields[4]));
            } else {
                return null;
            }
        } catch (NicoException e) {
            return null;
        }

        if (Task.isDoneFlag(fields[1])) {
            task.markAsDone();
        }
        return task;
    }
}