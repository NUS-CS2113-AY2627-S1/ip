package jamal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import jamal.task.Deadline;
import jamal.task.Event;
import jamal.task.Task;
import jamal.task.Todo;

/**
 * Saves tasks to, and loads tasks from, a text file on the hard disk.
 *
 * Each task is stored as one line, with fields separated by {@code " | "}:
 * <pre>
 * T | 1 | read book
 * D | 0 | return book | June 6th
 * E | 0 | project meeting | Aug 6th 2pm | 4pm
 * </pre>
 * The second field is 1 if the task is done and 0 if not.
 */
public class Storage {
    /**
     * Separator between fields, as a regex for {@link String#split}.
     * "|" is escaped because it means "or" in regex.
     */
    private static final String SEPARATOR_REGEX = " \\| ";

    /** Location of the save file. */
    private final Path filePath;

    /**
     * Number of lines skipped during the most recent {@link #load()}
     * because they were not in the expected format.
     */
    private int corruptLineCount = 0;

    /**
     * Creates a storage that reads and writes the given file.
     *
     * @param filePath Path to the save file, relative to the folder the app is run from.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Creates a storage using the default file {@code ./data/jamal.txt}.
     * {@link Paths#get(String, String...)} joins the parts with the correct
     * separator for the current OS ("/" or "\"), so no separator is hard-coded.
     */
    public Storage() {
        this(Paths.get("data", "jamal.txt"));
    }

    /**
     * Reads every task from the save file.
     * If the file does not exist yet (e.g. first run), an empty list is returned.
     * Lines that cannot be understood are skipped rather than crashing the app;
     * how many were skipped is available from {@link #getCorruptLineCount()}.
     *
     * @return Tasks read from the file, in file order.
     * @throws JamaLException If the file exists but cannot be read.
     */
    public List<Task> load() throws JamaLException {
        List<Task> tasks = new ArrayList<>();
        corruptLineCount = 0;
        if (!Files.exists(filePath)) {
            return tasks;
        }
        List<String> lines;
        try {
            lines = Files.readAllLines(filePath);
        } catch (IOException e) {
            throw new JamaLException("couldn't read " + filePath + ". starting empty.");
        }
        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            try {
                tasks.add(parseLine(line));
            } catch (JamaLException e) {
                corruptLineCount++;
            }
        }
        return tasks;
    }

    /**
     * Returns how many lines the most recent {@link #load()} had to skip.
     *
     * @return Number of corrupted lines skipped.
     */
    public int getCorruptLineCount() {
        return corruptLineCount;
    }

    /**
     * Overwrites the save file with the given tasks.
     * Creates the parent folder (e.g. {@code data/}) first if it does not exist.
     *
     * @param tasks Tasks to save, in list order.
     * @throws JamaLException If the folder or file cannot be written.
     */
    public void save(List<Task> tasks) throws JamaLException {
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(task.toFileString());
        }
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                // Does nothing if the folder already exists.
                Files.createDirectories(parent);
            }
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new JamaLException("couldn't save to " + filePath + ".");
        }
    }

    /**
     * Turns one line of the save file back into a task.
     *
     * @param line One line from the save file.
     * @return The task described by the line.
     * @throws JamaLException If the line is not in the expected format.
     */
    private static Task parseLine(String line) throws JamaLException {
        // -1 keeps trailing empty fields, so "T | 1 | " is caught as corrupt
        // instead of silently losing the empty field.
        String[] fields = line.split(SEPARATOR_REGEX, -1);
        if (fields.length < 3) {
            throw new JamaLException("corrupt line: " + line);
        }
        String type = fields[0].trim();
        String doneFlag = fields[1].trim();
        String description = fields[2].trim();
        if (description.isEmpty() || !(doneFlag.equals("0") || doneFlag.equals("1"))) {
            throw new JamaLException("corrupt line: " + line);
        }

        Task task;
        if (type.equals("T") && fields.length == 3) {
            task = new Todo(description);
        } else if (type.equals("D") && fields.length == 4 && !fields[3].isBlank()) {
            task = new Deadline(description, fields[3].trim());
        } else if (type.equals("E") && fields.length == 5
                && !fields[3].isBlank() && !fields[4].isBlank()) {
            task = new Event(description, fields[3].trim(), fields[4].trim());
        } else {
            throw new JamaLException("corrupt line: " + line);
        }
        task.setDone(doneFlag.equals("1"));
        return task;
    }
}
