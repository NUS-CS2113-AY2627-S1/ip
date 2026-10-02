package slay69.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import slay69.Slay69Exception;
import slay69.task.Deadline;
import slay69.task.Event;
import slay69.task.Task;
import slay69.task.Todo;

/**
 * Saves and loads tasks from a text file in the data folder.
 */
public class Storage {
    private static final String DEFAULT_FILE_PATH = "data/slay69.txt";

    private final File file;

    /**
     * Creates storage that uses the application's default data file.
     */
    public Storage() {
        this(DEFAULT_FILE_PATH);
    }

    /**
     * Creates storage that uses the specified file path.
     *
     * @param filePath path to the saved task file
     */
    public Storage(String filePath) {
        file = new File(filePath);
    }

    /**
     * Loads and returns the saved tasks.
     * A missing file means there are no saved tasks yet.
     *
     * @return tasks reconstructed from the file
     * @throws IOException if reading fails
     * @throws Slay69Exception if saved data is invalid
     */
    public ArrayList<Task> load()
            throws IOException, Slay69Exception {
        ArrayList<Task> tasks = new ArrayList<>();

        if (!file.exists()) {
            return tasks;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.isBlank()) {
                    continue;
                }

                tasks.add(parseTask(line));
            }

            // Scanner stores read errors instead of throwing them directly.
            if (scanner.ioException() != null) {
                throw scanner.ioException();
            }
        }

        return tasks;
    }

    /**
     * Saves the current tasks, creating the data folder if needed.
     *
     * @param tasks tasks to write in their current order
     * @throws IOException if the folder cannot be created or writing fails
     */
    public void save(List<Task> tasks) throws IOException {
        File folder = file.getParentFile();

        if (!folder.isDirectory() && !folder.mkdirs()) {
            throw new IOException("Could not create the data folder.");
        }

        try (FileWriter writer = new FileWriter(file)) {
            for (Task task : tasks) {
                writer.write(task.toStorageString());
                writer.write(System.lineSeparator());
            }
        }
    }

    /**
     * Converts one saved line into a task, including its completion status.
     *
     * @throws Slay69Exception if the saved line has an invalid format
     */
    private Task parseTask(String line) throws Slay69Exception {
        String[] parts = line.split("\\|", -1);

        if (parts.length < 3) {
            throw new Slay69Exception("Invalid saved task: " + line);
        }

        if (!parts[1].equals("0") && !parts[1].equals("1")) {
            throw new Slay69Exception("Invalid saved status: " + line);
        }

        for (int i = 2; i < parts.length; i++) {
            if (parts[i].isBlank()) {
                throw new Slay69Exception("Empty saved task field: " + line);
            }
        }

        Task task;

        if (parts[0].equals("T") && parts.length == 3) {
            task = new Todo(parts[2]);
        } else if (parts[0].equals("D") && parts.length == 4) {
            task = Deadline.fromStorage(parts[2], parts[3]);
        } else if (parts[0].equals("E") && parts.length == 5) {
            task = new Event(parts[2], parts[3], parts[4]);
        } else {
            throw new Slay69Exception("Invalid saved task: " + line);
        }

        if (parts[1].equals("1")) {
            task.markAsDone();
        }

        return task;
    }
}
