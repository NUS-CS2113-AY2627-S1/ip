import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Reads the task list from disk when the bot starts and writes it back
 * whenever the list changes.
 * <p>
 * Each task occupies one line, with its fields separated by " | ". For example:
 * <pre>
 * T | 1 | read book
 * D | 0 | return book | 2019-12-02
 * D | 0 | submit form | 2019-12-02 1800
 * E | 0 | project meeting | Aug 6th 2pm | 4pm
 * </pre>
 * The first field is the task type, the second is 1 when the task is done and
 * 0 otherwise, and the rest are the task's own fields. A deadline's date is
 * written in the same shape the user types it, so a line of this file can be
 * read back by the very parsing that accepted it; an event's start and end are
 * still free text, and become dates in a later increment.
 * <p>
 * Each Storage object is tied to one file, named when the object is
 * constructed, and reports trouble by throwing {@link CortisolException}
 * rather than by printing. What the user is told, and when, is the caller's
 * business; this class only knows about the file.
 */
public class Storage {

    /** File this object reads from and writes to. */
    private final Path dataFile;

    /** Lines the most recent call to {@link #load()} could not understand. */
    private int skippedLineCount;

    /**
     * Constructs a Storage object for one data file.
     *
     * @param filePath path to the data file, relative to the folder the bot is
     *                 run from, so that it works on any computer. It may be
     *                 written with "/" separators whatever the operating
     *                 system, since {@link Paths#get(String, String...)}
     *                 converts them to the local form.
     */
    public Storage(String filePath) {
        this.dataFile = Paths.get(filePath);
    }

    /**
     * Loads the saved tasks.
     * <p>
     * A missing file is treated as an empty list rather than an error, since
     * that is simply what the first run looks like. Lines that cannot be
     * understood are skipped so that one damaged line does not cost the user
     * the rest of their list; how many were skipped is available afterwards
     * from {@link #getSkippedLineCount()}.
     *
     * @return the tasks that were loaded, or an empty list if there were none.
     * @throws CortisolException if the file exists but cannot be read.
     */
    public ArrayList<Task> load() throws CortisolException {
        skippedLineCount = 0;

        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(dataFile)) {
            return tasks;
        }

        try {
            for (String line : Files.readAllLines(dataFile)) {
                if (line.isBlank()) {
                    continue;
                }

                try {
                    tasks.add(parseTask(line));
                } catch (CortisolException e) {
                    skippedLineCount++;
                }
            }
        } catch (IOException e) {
            throw new CortisolException("I could not open my records, sir/madam. "
                    + "I shall begin the evening with an empty list.");
        }

        return tasks;
    }

    /**
     * Returns how many lines the most recent load could not understand.
     * <p>
     * Reported separately rather than thrown, because a damaged line is not a
     * reason to abandon the load: the caller needs the tasks that were
     * understood <em>and</em> the number that were not.
     *
     * @return number of lines skipped by the most recent call to load.
     */
    public int getSkippedLineCount() {
        return skippedLineCount;
    }

    /**
     * Rebuilds a single task from one line of the data file.
     *
     * @param line one line of the data file.
     * @return the task that line describes.
     * @throws CortisolException if the line does not have the expected shape.
     */
    private static Task parseTask(String line) throws CortisolException {
        // The separator is a literal "|", which must be escaped because
        // split() treats its argument as a regular expression.
        String[] fields = line.split("\\|");
        if (fields.length < 3) {
            throw new CortisolException("Too few fields.");
        }

        String type = fields[0].trim();
        String status = fields[1].trim();
        String description = fields[2].trim();

        if (description.isEmpty()) {
            throw new CortisolException("Missing description.");
        }
        if (!status.equals("0") && !status.equals("1")) {
            throw new CortisolException("Unrecognised done-status.");
        }

        Task task;
        switch (type) {
        case "T":
            task = new ToDo(description);
            break;
        case "D":
            if (fields.length < 4 || fields[3].isBlank()) {
                throw new CortisolException("Deadline is missing its due date.");
            }
            task = new Deadline(description, TaskDateTime.parse(fields[3].trim()));
            break;
        case "E":
            if (fields.length < 5 || fields[3].isBlank() || fields[4].isBlank()) {
                throw new CortisolException("Event is missing its start or end.");
            }
            task = new Event(description, fields[3].trim(), fields[4].trim());
            break;
        default:
            throw new CortisolException("Unrecognised task type.");
        }

        if (status.equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Writes the whole task list to disk, replacing whatever was there before.
     *
     * @param tasks list containing the tasks.
     * @throws CortisolException if the list cannot be written to the file.
     */
    public void save(ArrayList<Task> tasks) throws CortisolException {
        StringBuilder content = new StringBuilder();
        for (Task task : tasks) {
            content.append(task.toFileFormat()).append(System.lineSeparator());
        }

        try {
            Path parentFolder = dataFile.getParent();
            if (parentFolder != null) {
                // Creates the folder on the first run; does nothing if it exists.
                Files.createDirectories(parentFolder);
            }
            Files.writeString(dataFile, content.toString());
        } catch (IOException e) {
            throw new CortisolException("I was unable to write to my records, sir/madam. "
                    + "This change may not survive the evening.");
        }
    }
}
