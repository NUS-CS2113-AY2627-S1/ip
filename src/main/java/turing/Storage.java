package turing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

import turing.task.Deadline;
import turing.task.Event;
import turing.task.Task;
import turing.task.TaskList;
import turing.task.Todo;

/**
 * Reads the task list from a file when the chatbot starts and writes it back
 * whenever the list changes, so that tasks survive a restart.
 * The file holds one task per line in the form written by
 * {@link Task#toSaveFormat()}, for example {@code D | 1 | return book | Sunday}.
 */
public class Storage {
    /**
     * Pattern matching the separator between saved fields. It is derived from
     * the separator the tasks write so that the two cannot drift apart, and
     * quoted because "|" means something else in a regular expression.
     */
    private static final String SAVE_SEPARATOR_PATTERN = Pattern.quote(Task.SAVE_SEPARATOR);

    /** Number of fields in a saved todo: the icon, the done flag and the description. */
    private static final int TODO_FIELD_COUNT = 3;

    /** Number of fields in a saved deadline, which adds the due date. */
    private static final int DEADLINE_FIELD_COUNT = 4;

    /** Number of fields in a saved event, which adds a start and an end. */
    private static final int EVENT_FIELD_COUNT = 5;

    /** Where the tasks are kept between runs. */
    private final Path filePath;

    /**
     * Creates storage backed by the given file. Neither the file nor the
     * folder holding it has to exist yet; both are created when tasks are
     * first saved.
     *
     * @param filePath Path to the save file, relative to where the chatbot is run.
     */
    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    /**
     * Adds every task in the save file to the given list. A line that cannot
     * be understood is skipped rather than abandoning the whole file, so one
     * damaged line does not cost the user the rest of their tasks.
     *
     * @param tasks List to add the saved tasks to.
     * @return Number of lines that could not be read and were skipped.
     * @throws TuringException If the file exists but cannot be read.
     */
    public int load(TaskList tasks) throws TuringException {
        // No file yet simply means this is the first run, which is not an error.
        if (!Files.exists(filePath)) {
            return 0;
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(filePath);
        } catch (IOException exception) {
            throw new TuringException("I could not read your saved tasks from " + filePath + ".",
                    "Starting with an empty list. Your file has been left as it is.");
        }

        int skippedLineCount = 0;
        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }

            Task task = parseSavedTask(line);
            if (task == null) {
                skippedLineCount++;
                continue;
            }
            tasks.add(task);
        }
        return skippedLineCount;
    }

    /**
     * Writes every task in the given list to the save file, replacing what was
     * there before, and creates the folder holding it if it is missing.
     *
     * @param tasks List to write.
     * @throws TuringException If the file cannot be written.
     */
    public void save(TaskList tasks) throws TuringException {
        StringBuilder savedTasks = new StringBuilder();
        for (int taskNumber = 1; taskNumber <= tasks.getTaskCount(); taskNumber++) {
            savedTasks.append(tasks.getTask(taskNumber).toSaveFormat())
                    .append(System.lineSeparator());
        }

        try {
            Path parentFolder = filePath.getParent();
            if (parentFolder != null) {
                Files.createDirectories(parentFolder);
            }
            Files.writeString(filePath, savedTasks.toString());
        } catch (IOException exception) {
            throw new TuringException("I could not save your tasks to " + filePath + ".",
                    "Your list is still correct here, but it may not survive a restart.");
        }
    }

    /**
     * Returns the task described by one line of the save file.
     *
     * @param line One non-blank line of the save file.
     * @return Matching task, or null if the line is not in the expected form.
     */
    private static Task parseSavedTask(String line) {
        // A limit of -1 keeps trailing empty fields, so a line that ends with a
        // separator is short of a field rather than quietly losing it.
        String[] fields = line.split(SAVE_SEPARATOR_PATTERN, -1);
        if (fields.length < TODO_FIELD_COUNT || fields[2].isBlank()) {
            return null;
        }

        Task task = createTask(fields);
        if (task == null) {
            return null;
        }

        String doneFlag = fields[1];
        if (doneFlag.equals(Task.DONE_FLAG)) {
            task.markAsDone();
        } else if (!doneFlag.equals(Task.NOT_DONE_FLAG)) {
            return null;
        }
        return task;
    }

    /**
     * Returns a task of the kind named by the saved type icon, provided the
     * line carries exactly the fields that kind of task needs.
     *
     * @param fields Fields of one saved line.
     * @return Matching task, or null if the icon is unknown or a field is missing or spare.
     */
    private static Task createTask(String[] fields) {
        String description = fields[2];
        return switch (fields[0]) {
        case Todo.TYPE_ICON -> fields.length == TODO_FIELD_COUNT ? new Todo(description) : null;
        case Deadline.TYPE_ICON -> fields.length == DEADLINE_FIELD_COUNT
                ? new Deadline(description, fields[3])
                : null;
        case Event.TYPE_ICON -> fields.length == EVENT_FIELD_COUNT
                ? new Event(description, fields[3], fields[4])
                : null;
        default -> null;
        };
    }
}
