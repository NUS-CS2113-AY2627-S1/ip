package lumi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import lumi.task.Deadline;
import lumi.task.Event;
import lumi.task.Task;
import lumi.task.Todo;

/**
 * Keeps the task list on disk so that it survives between runs.
 * The file is rewritten in full every time the list changes, which keeps the
 * saved copy and the in-memory list in step without tracking what changed.
 */
public class Storage {
    private final Path saveFile;

    private static final String SAVED_DONE = "1";
    private static final String SAVED_NOT_DONE = "0";

    private static final String TODO_TYPE = "T";
    private static final String DEADLINE_TYPE = "D";
    private static final String EVENT_TYPE = "E";

    /** How many fields each task type occupies on one saved line. */
    private static final int TODO_FIELDS = 3;
    private static final int DEADLINE_FIELDS = 4;
    private static final int EVENT_FIELDS = 5;

    private static final int TYPE_FIELD = 0;
    private static final int DONE_FIELD = 1;
    private static final int DESCRIPTION_FIELD = 2;
    private static final int FOURTH_FIELD = 3;
    private static final int FIFTH_FIELD = 4;

    /**
     * Creates a store backed by the given file.
     *
     * @param filePath Path to the save file, relative to where Lumi is run.
     */
    public Storage(String filePath) {
        saveFile = Path.of(filePath);
    }

    /**
     * Writes every task to the save file, replacing its previous contents.
     *
     * @param tasks The task list to record.
     * @throws LumiException If the file or its folder cannot be written.
     */
    public void save(List<Task> tasks) throws LumiException {
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(task.toSaveFormat());
        }
        try {
            Path folder = saveFile.getParent();
            if (folder != null) {
                Files.createDirectories(folder);
            }
            Files.write(saveFile, lines);
        } catch (IOException e) {
            throw new LumiException("I could not save your tasks to " + saveFile
                    + ". Check that the folder is writable.");
        }
    }

    /**
     * Returns the tasks recorded by a previous run, or an empty list on a first run.
     * A line that cannot be understood is left out and explained in {@code skipped}
     * rather than abandoning the whole file, so one damaged line never costs the
     * user the tasks that are still readable.
     *
     * @param skipped Collects one explanation per line that had to be left out.
     * @throws LumiException If the file exists but cannot be read at all.
     */
    public ArrayList<Task> load(List<String> skipped) throws LumiException {
        ArrayList<Task> tasks = new ArrayList<>();
        List<String> lines;
        try {
            lines = Files.readAllLines(saveFile);
        } catch (NoSuchFileException e) {
            // Nothing has been saved yet, which is the normal first run.
            return tasks;
        } catch (IOException e) {
            throw new LumiException("I could not read your saved tasks from " + saveFile
                    + ". Check that the file is readable.");
        }
        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            try {
                tasks.add(parseTask(line));
            } catch (LumiException e) {
                skipped.add(e.getMessage());
            }
        }
        return tasks;
    }

    /** Rebuilds one task from a saved line such as {@code D | 0 | return book | June 6th}. */
    private Task parseTask(String line) throws LumiException {
        String[] fields = line.split(Pattern.quote(Task.SEPARATOR_CHARACTER));
        if (fields.length < TODO_FIELDS) {
            throw new LumiException(rejected(line, "it has too few fields"));
        }
        Task task = createTask(fields, line);
        applyDoneFlag(task, fields[DONE_FIELD].trim(), line);
        return task;
    }

    /** Builds the right subclass for the saved type, checking that its fields are all present. */
    private Task createTask(String[] fields, String line) throws LumiException {
        String description = requireText(fields[DESCRIPTION_FIELD], line, "a description");
        switch (fields[TYPE_FIELD].trim()) {
        case TODO_TYPE:
            requireFieldCount(fields, TODO_FIELDS, line);
            return new Todo(description);
        case DEADLINE_TYPE:
            requireFieldCount(fields, DEADLINE_FIELDS, line);
            return new Deadline(description, requireText(fields[FOURTH_FIELD], line, "a due date"));
        case EVENT_TYPE:
            requireFieldCount(fields, EVENT_FIELDS, line);
            return new Event(description,
                    requireText(fields[FOURTH_FIELD], line, "a start time"),
                    requireText(fields[FIFTH_FIELD], line, "an end time"));
        default:
            throw new LumiException(rejected(line, "the task type is not one of T, D or E"));
        }
    }

    /** Marks the task done only for the exact saved flag; anything else means the line is bad. */
    private void applyDoneFlag(Task task, String flag, String line) throws LumiException {
        if (SAVED_DONE.equals(flag)) {
            task.markAsDone();
        } else if (!SAVED_NOT_DONE.equals(flag)) {
            throw new LumiException(rejected(line, "the done flag should be "
                    + SAVED_NOT_DONE + " or " + SAVED_DONE));
        }
    }

    /** A saved line must carry exactly the fields its type needs, no more and no fewer. */
    private void requireFieldCount(String[] fields, int expected, String line)
            throws LumiException {
        if (fields.length != expected) {
            throw new LumiException(rejected(line, "this task type needs exactly "
                    + expected + " fields"));
        }
    }

    /** Returns the trimmed field, refusing one that is empty or only spaces. */
    private String requireText(String field, String line, String what) throws LumiException {
        String text = field.trim();
        if (text.isEmpty()) {
            throw new LumiException(rejected(line, "it is missing " + what));
        }
        return text;
    }

    private String rejected(String line, String reason) {
        return "I skipped a line in " + saveFile + " because " + reason + ": " + line;
    }
}
