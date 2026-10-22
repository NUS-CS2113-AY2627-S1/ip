package anson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Deals with loading tasks from the data file and saving tasks to it.
 */
public class Storage {
    private static final String SPLIT_REGEX = " \\| ";

    private final Path filePath;
    private int skippedLineCount = 0;

    /**
     * Creates a storage that reads from and writes to the given file.
     * A relative path with forward slashes (e.g., "data/anson.txt") also
     * works on Windows, so the path stays OS-independent.
     *
     * @param filePath Path of the data file, relative to the working directory.
     */
    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /**
     * Returns how many lines were skipped as unreadable during the last load.
     *
     * @return Number of skipped lines.
     */
    public int getSkippedLineCount() {
        return skippedLineCount;
    }

    /**
     * Loads tasks from the data file. A missing file is treated as an empty
     * task list. Lines in an unexpected format are skipped and counted.
     *
     * @return The loaded tasks.
     * @throws AnsonException If the file exists but cannot be read.
     */
    public ArrayList<Task> load() throws AnsonException {
        ArrayList<Task> tasks = new ArrayList<>();
        skippedLineCount = 0;

        if (!Files.exists(filePath)) {
            return tasks;
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(filePath);
        } catch (IOException e) {
            throw new AnsonException("I couldn't read your saved tasks: " + e.getMessage());
        }

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            try {
                tasks.add(parseTask(line));
            } catch (AnsonException e) {
                skippedLineCount++;
            }
        }
        return tasks;
    }

    /**
     * Writes all tasks to the data file, creating the data folder and file
     * if they do not exist yet.
     *
     * @param tasks Current task list.
     * @throws AnsonException If the file cannot be written.
     */
    public void save(TaskList tasks) throws AnsonException {
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            lines.add(tasks.get(i).toFileString());
        }

        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new AnsonException("I couldn't save your tasks: " + e.getMessage());
        }
    }

    /**
     * Parses one line of the data file into a task.
     *
     * @param line A line such as "D | 0 | return book | June 6th".
     * @return The corresponding task.
     * @throws AnsonException If the line is not in the expected format.
     */
    private Task parseTask(String line) throws AnsonException {
        String[] parts = line.split(SPLIT_REGEX, -1);
        if (parts.length < 3) {
            throw new AnsonException("Malformed line: " + line);
        }

        String type = parts[0];
        String doneFlag = parts[1];
        String description = parts[2];

        if (!doneFlag.equals("0") && !doneFlag.equals("1")) {
            throw new AnsonException("Malformed line: " + line);
        }
        if (description.trim().isEmpty()) {
            throw new AnsonException("Malformed line: " + line);
        }

        Task task;
        switch (type) {
            case "T":
                if (parts.length != 3) {
                    throw new AnsonException("Malformed line: " + line);
                }
                task = new Todo(description);
                break;
            case "D":
                if (parts.length != 4 || parts[3].trim().isEmpty()) {
                    throw new AnsonException("Malformed line: " + line);
                }
                task = new Deadline(description, parts[3]);
                break;
            case "E":
                if (parts.length != 5 || parts[3].trim().isEmpty() || parts[4].trim().isEmpty()) {
                    throw new AnsonException("Malformed line: " + line);
                }
                task = new Event(description, parts[3], parts[4]);
                break;
            default:
                throw new AnsonException("Malformed line: " + line);
        }

        if (doneFlag.equals("1")) {
            task.markAsDone();
        }
        return task;
    }
}