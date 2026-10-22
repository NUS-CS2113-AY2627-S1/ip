package yuwei;

import yuwei.task.Deadline;
import yuwei.task.Event;
import yuwei.task.Task;
import yuwei.task.ToDo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves tasks to, and loads them from, a text file on the hard disk.
 *
 * <p>Each line of the file holds one task, in the format produced by
 * {@link Task#toFileString()}, e.g. {@code D | 0 | return book | 2019-10-15}.
 */
public class Storage {
    /** Separator between fields; {@code split()} takes a regex, so {@code |} must be escaped. */
    private static final String FIELD_SEPARATOR_REGEX = " \\| ";

    private final Path filePath;

    /**
     * Creates a Storage that reads and writes the given file.
     *
     * @param filePath path to the data file, preferably relative so the app works on any computer
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads the saved tasks. Returns an empty list if nothing has been saved yet.
     * Lines that are not in the expected format are skipped with a warning, so one
     * corrupted line does not lose the rest of the user's tasks.
     *
     * @throws YuWeiException if the file exists but cannot be read
     */
    public List<Task> load() throws YuWeiException {
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return tasks; // First run: no data file yet.
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(filePath);
        } catch (IOException e) {
            throw new YuWeiException("I couldn't read your saved tasks: " + e.getMessage());
        }

        for (String line : lines) {
            try {
                tasks.add(parseTask(line));
            } catch (YuWeiException e) {
                System.out.println("     " + e.getMessage());
            }
        }
        return tasks;
    }

    /**
     * Overwrites the data file with the given tasks, creating its folder if needed.
     *
     * @throws YuWeiException if the file cannot be written
     */
    public void save(List<Task> tasks) throws YuWeiException {
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(task.toFileString());
        }
        try {
            Path folder = filePath.getParent();
            if (folder != null) {
                Files.createDirectories(folder); // Does nothing if the folder already exists.
            }
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new YuWeiException("I couldn't save your tasks: " + e.getMessage());
        }
    }

    /**
     * Converts one line of the data file back into a Task.
     *
     * @throws YuWeiException if the line is not in the expected format
     */
    private static Task parseTask(String line) throws YuWeiException {
        String[] fields = line.split(FIELD_SEPARATOR_REGEX);
        if (fields.length < 3 || !(fields[1].equals("0") || fields[1].equals("1"))) {
            throw corruptedLine(line);
        }

        String description = fields[2];
        Task task;
        if (fields[0].equals("T") && fields.length == 3) {
            task = new ToDo(description);
        } else if (fields[0].equals("D") && fields.length == 4) {
            task = new Deadline(description, parseSavedDate(fields[3], line));
        } else if (fields[0].equals("E") && fields.length == 5) {
            task = new Event(description, fields[3], fields[4]);
        } else {
            throw corruptedLine(line);
        }

        if (fields[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Reads a date saved in {@code yyyy-mm-dd} format.
     *
     * @throws YuWeiException if {@code text} is not such a date, e.g. a line saved before
     *     deadlines were stored as dates
     */
    private static LocalDate parseSavedDate(String text, String line) throws YuWeiException {
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException e) {
            throw corruptedLine(line);
        }
    }

    private static YuWeiException corruptedLine(String line) {
        return new YuWeiException("Skipping a corrupted line in your data file: " + line);
    }
}
