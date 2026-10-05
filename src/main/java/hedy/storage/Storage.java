package hedy.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hedy.exception.HedyException;
import hedy.task.Deadline;
import hedy.task.Event;
import hedy.task.Task;
import hedy.task.Todo;

/**
 * Loads tasks from a text file and saves them again after the list changes.
 * The path is relative to the folder where the program is run, so the same
 * code works on different computers and operating systems.
 */
public class Storage {
    private final Path filePath;
    private String loadWarning;

    /**
     * Saves tasks at the given path.
     * {@code data/duke.txt} uses a forward slash here; {@link Path} converts it
     * to the separator used by the operating system.
     *
     * @param filePath path relative to the folder where Hedy is run
     */
    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    /**
     * Reads the save file into a new list.
     * A missing file means this is a new list. A line that is not in the save
     * format is skipped, and {@link #getLoadWarning()} explains that afterwards.
     *
     * @return the tasks that could be read
     * @throws HedyException if the file exists but cannot be read
     */
    public ArrayList<Task> load() throws HedyException {
        ArrayList<Task> tasks = new ArrayList<>();
        loadWarning = null;
        if (!Files.exists(filePath)) {
            return tasks;
        }
        if (!Files.isRegularFile(filePath)) {
            throw new HedyException("I could not read " + filePath + ", so I am starting with an empty list.");
        }

        int skipped = 0;
        try (Scanner fileScanner = new Scanner(filePath)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.isBlank()) {
                    continue;
                }
                Task task = taskFromLine(line);
                if (task == null) {
                    skipped++;
                } else {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            throw new HedyException("I could not read " + filePath + ", so I am starting with an empty list.");
        }

        if (skipped > 0) {
            String lineWord = skipped == 1 ? "line" : "lines";
            loadWarning = "I skipped " + skipped + " " + lineWord + " in " + filePath
                    + " that were not in the expected format.";
        }
        return tasks;
    }

    /**
     * Returns a warning from the last {@link #load()}, or null if every line was usable.
     */
    public String getLoadWarning() {
        return loadWarning;
    }

    /**
     * Writes every task to the save file, creating parent folders if needed.
     *
     * @param tasks tasks to write, in list order
     * @throws HedyException if the file cannot be written
     */
    public void save(List<Task> tasks) throws HedyException {
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            StringBuilder content = new StringBuilder();
            for (Task task : tasks) {
                content.append(task.toFileFormat()).append(System.lineSeparator());
            }
            Files.writeString(filePath, content.toString());
        } catch (IOException e) {
            throw new HedyException("OOPS!!! I could not save your tasks to " + filePath + ".");
        }
    }

    /**
     * Builds one task from a saved line, or returns null if the line is unusable.
     * Expected forms:
     * {@code T | 1 | read book},
     * {@code D | 0 | return book | 2019-10-15},
     * {@code E | 0 | meeting | Mon 2pm | 4pm}.
     * The middle field must be {@code 0} or {@code 1}. A deadline date must be {@code yyyy-mm-dd}.
     */
    private Task taskFromLine(String line) {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            return null;
        }
        String type = parts[0];
        String doneFlag = parts[1];
        String description = parts[2];
        if ((!doneFlag.equals("0") && !doneFlag.equals("1")) || description.isEmpty()) {
            return null;
        }

        Task task;
        if (type.equals("T")) {
            task = new Todo(description);
        } else if (type.equals("D") && parts.length >= 4 && !parts[3].isEmpty()) {
            task = deadlineFromSavedDate(description, parts[3]);
        } else if (type.equals("E") && parts.length >= 5 && !parts[3].isEmpty() && !parts[4].isEmpty()) {
            task = new Event(description, parts[3], parts[4]);
        } else {
            return null;
        }

        if (task != null && doneFlag.equals("1")) {
            task.setDone(true);
        }
        return task;
    }

    /**
     * Builds a deadline from a saved {@code yyyy-mm-dd} date.
     * Returns null when the saved text is not a date, so that line is skipped.
     */
    private Deadline deadlineFromSavedDate(String description, String savedDate) {
        try {
            return new Deadline(description, LocalDate.parse(savedDate));
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
