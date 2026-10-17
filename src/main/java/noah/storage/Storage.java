package noah.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import noah.exception.NoahException;
import noah.task.Deadline;
import noah.task.Event;
import noah.task.Task;
import noah.task.TaskDateTime;
import noah.task.Todo;

/**
 * Loads tasks from and saves tasks to a text file.
 */
public class Storage {
    private static final String FIELD_SEPARATOR = " | ";

    private final Path filePath;

    /**
     * Prevents saves from overwriting tasks that could not be loaded.
     */
    private boolean hasLoadingError;

    /**
     * Creates storage that uses the specified data file.
     *
     * @param filePath Relative or absolute path of the data file.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads saved tasks into a dynamic list.
     * Creates the data directory and file if either does not exist.
     * A loading failure blocks saves until the data can be loaded successfully.
     *
     * @return Tasks loaded from the data file.
     * @throws NoahException If the data file cannot be read or contains invalid data.
     */
    public ArrayList<Task> loadTasks() throws NoahException {
        try {
            ensureDataFileExists();
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            ArrayList<Task> tasks = new ArrayList<>();

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.isBlank()) {
                    continue;
                }
                tasks.add(parseTask(line, i + 1));
            }
            hasLoadingError = false;
            return tasks;
        } catch (IOException e) {
            hasLoadingError = true;
            throw new NoahException("Oops, I couldn't load your saved tasks from " + filePath + ".\n"
                    + "Please check the file path and permissions, then restart Noah.");
        } catch (NoahException e) {
            hasLoadingError = true;
            throw e;
        }
    }

    /**
     * Replaces the data file contents with the current task list.
     *
     * @param tasks Tasks to save.
     * @throws NoahException If loading failed or the tasks cannot be written to the data file.
     */
    public void saveTasks(List<Task> tasks) throws NoahException {
        if (hasLoadingError) {
            throw new NoahException("Your saved tasks need a little rescue first!\n"
                    + "Your latest changes are only in memory. Fix the loading error and restart Noah before saving.");
        }
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(task.toDataString());
        }

        try {
            ensureDataFileExists();
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new NoahException("Saving hit a bump! I couldn't save your tasks to " + filePath + ".\n"
                    + "Your latest changes are not saved. Please check the file path and permissions.");
        }
    }

    /**
     * Creates the data directory and an empty data file when they are missing.
     *
     * @throws IOException If the directory or file cannot be created.
     */
    private void ensureDataFileExists() throws IOException {
        Path parentDirectory = filePath.getParent();
        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }
        if (Files.notExists(filePath)) {
            Files.createFile(filePath);
        }
    }

    /**
     * Converts one line of saved data into a task.
     *
     * @param line Saved task data.
     * @param lineNumber One-based line number used in error messages.
     * @return Task represented by the line.
     * @throws NoahException If the line is not in a supported format.
     */
    private Task parseTask(String line, int lineNumber) throws NoahException {
        String[] fields = line.split(Pattern.quote(FIELD_SEPARATOR), -1);
        if (fields.length < 3 || fields[2].isBlank()) {
            throw invalidDataException(lineNumber);
        }

        Task task = switch (fields[0]) {
        case "T" -> parseTodo(fields, lineNumber);
        case "D" -> parseDeadline(fields, lineNumber);
        case "E" -> parseEvent(fields, lineNumber);
        default -> throw invalidDataException(lineNumber);
        };

        if (fields[1].equals("1")) {
            task.markAsDone();
        } else if (!fields[1].equals("0")) {
            throw invalidDataException(lineNumber);
        }
        return task;
    }

    /**
     * Parses a saved todo task.
     *
     * @param fields Fields from one data-file line.
     * @param lineNumber One-based line number used in error messages.
     * @return Parsed todo task.
     * @throws NoahException If the field count is invalid.
     */
    private Task parseTodo(String[] fields, int lineNumber) throws NoahException {
        if (fields.length != 3) {
            throw invalidDataException(lineNumber);
        }
        return new Todo(fields[2]);
    }

    /**
     * Parses a saved deadline task.
     *
     * @param fields Fields from one data-file line.
     * @param lineNumber One-based line number used in error messages.
     * @return Parsed deadline task.
     * @throws NoahException If the field count is invalid or the due date is blank.
     */
    private Task parseDeadline(String[] fields, int lineNumber) throws NoahException {
        if (fields.length == 3) {
            return new Deadline(fields[2]);
        }
        if (fields.length == 4 && !fields[3].isBlank()) {
            return new Deadline(fields[2], TaskDateTime.fromStorage(fields[3]));
        }
        throw invalidDataException(lineNumber);
    }

    /**
     * Parses a saved event task.
     *
     * @param fields Fields from one data-file line.
     * @param lineNumber One-based line number used in error messages.
     * @return Parsed event task.
     * @throws NoahException If the field count or event fields are invalid.
     */
    private Task parseEvent(String[] fields, int lineNumber) throws NoahException {
        if (fields.length == 3) {
            return new Event(fields[2]);
        }
        if (fields.length == 5 && !fields[3].isBlank() && !fields[4].isBlank()) {
            return new Event(fields[2], fields[3], fields[4]);
        }
        throw invalidDataException(lineNumber);
    }

    /**
     * Creates an exception that identifies an invalid data-file line.
     *
     * @param lineNumber One-based line number containing invalid data.
     * @return Exception describing the invalid line.
     */
    private NoahException invalidDataException(int lineNumber) {
        return new NoahException("Oops, I couldn't load your saved tasks: line "
                + lineNumber + " in " + filePath + " is invalid.\n"
                + "Please fix that line and restart Noah.");
    }
}
