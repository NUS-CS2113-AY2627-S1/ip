package floppy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import floppy.task.Deadline;
import floppy.task.Event;
import floppy.task.Task;
import floppy.task.Todo;

/**
 * Keeps Floppy's tasks on the hard disk so that they survive between runs.
 * Each task is written as one line of text in the data file.
 */
public class Storage {

    /**
     * Matches the separator between fields on a line of the data file. Spaces around the
     * bar are optional, so a line that was edited by hand still reads back correctly.
     */
    private static final String FIELD_SEPARATOR_REGEX = "\\s*\\|\\s*";

    /** Number of fields on a todo's line: type, status and description. */
    private static final int FIELD_COUNT_TODO = 3;

    /** Number of fields on a deadline's line: a todo's fields plus the due time. */
    private static final int FIELD_COUNT_DEADLINE = 4;

    /** Number of fields on an event's line: a todo's fields plus the start and end. */
    private static final int FIELD_COUNT_EVENT = 5;

    /** Added to the data file's name to name the copy kept when unreadable lines are found. */
    private static final String BACKUP_SUFFIX = ".bak";

    /** Where the tasks are kept, relative to the folder Floppy is run from. */
    private final Path filePath;

    /** Line numbers, counting from 1, that the most recent load could not read. */
    private final List<Integer> skippedLineNumbers = new ArrayList<>();

    /**
     * Constructs a storage that keeps tasks in the given file.
     *
     * @param filePath where to keep the tasks.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Returns the tasks kept in the data file, in the order they were saved.
     * Returns no tasks if nothing has been saved yet. Lines that cannot be read are
     * skipped rather than failing the whole load; their numbers are available from
     * {@link #getSkippedLineNumbers()}, and the original file is first copied to
     * {@link #getBackupPath()} so that the skipped lines are not lost on the next save.
     *
     * @return the saved tasks that could be read.
     * @throws FloppyException if the file exists but cannot be read, or cannot be backed up.
     */
    public List<Task> load() throws FloppyException {
        skippedLineNumbers.clear();
        if (Files.notExists(filePath)) {
            // Nothing has been saved yet, for example the first time Floppy runs in this folder.
            return new ArrayList<>();
        }

        List<String> lines = readLines();
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isBlank()) {
                continue;
            }
            try {
                tasks.add(decode(line));
            } catch (FloppyException e) {
                skippedLineNumbers.add(i + 1);
            }
        }

        if (!skippedLineNumbers.isEmpty()) {
            backUpOriginal();
        }
        return tasks;
    }

    /**
     * Returns the line numbers, counting from 1, that the most recent {@link #load()} skipped.
     *
     * @return the skipped line numbers; empty if every line was read.
     */
    public List<Integer> getSkippedLineNumbers() {
        return skippedLineNumbers;
    }

    /**
     * Returns where the original data file is copied when it contains unreadable lines.
     *
     * @return the backup file's path, next to the data file.
     */
    public Path getBackupPath() {
        return filePath.resolveSibling(filePath.getFileName() + BACKUP_SUFFIX);
    }

    /**
     * Returns every line of the data file.
     *
     * @return the lines of the data file.
     * @throws FloppyException if the file cannot be read.
     */
    private List<String> readLines() throws FloppyException {
        try {
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            throw new FloppyException("I couldn't read " + filePath + ": " + e.getMessage());
        }
    }

    /**
     * Copies the data file to the backup path, replacing any older backup.
     *
     * @throws FloppyException if the copy cannot be made.
     */
    private void backUpOriginal() throws FloppyException {
        try {
            Files.copy(filePath, getBackupPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FloppyException("I couldn't back up " + filePath + ": " + e.getMessage());
        }
    }

    /**
     * Returns the task described by one line of the data file.
     *
     * @param line a line written by {@link Task#toFileFormat()}.
     * @return the task the line describes, marked done if it was saved as done.
     * @throws FloppyException if the line is not a valid task.
     */
    private static Task decode(String line) throws FloppyException {
        // A limit of -1 keeps empty trailing fields, so a line ending in "|" is caught as incomplete.
        String[] fields = line.split(FIELD_SEPARATOR_REGEX, -1);
        requireNoBlankFields(fields);
        if (fields.length < FIELD_COUNT_TODO) {
            throw new FloppyException("A line has too few fields.");
        }

        String typeCode = fields[0];
        String description = fields[2];
        Task task = switch (typeCode) {
        case Todo.FILE_TYPE_CODE -> {
            requireFieldCount(fields, FIELD_COUNT_TODO);
            yield new Todo(description);
        }
        case Deadline.FILE_TYPE_CODE -> {
            requireFieldCount(fields, FIELD_COUNT_DEADLINE);
            yield new Deadline(description, Parser.parseDate(fields[3]));
        }
        case Event.FILE_TYPE_CODE -> {
            requireFieldCount(fields, FIELD_COUNT_EVENT);
            yield new Event(description, fields[3], fields[4]);
        }
        default -> throw new FloppyException("Unknown task type '" + typeCode + "'.");
        };

        if (isSavedAsDone(fields[1])) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Returns whether a saved status field marks its task as done.
     *
     * @param status the status field of a line.
     * @return true if the status marks the task as done.
     * @throws FloppyException if the status is neither the done nor the not-done marker.
     */
    private static boolean isSavedAsDone(String status) throws FloppyException {
        if (status.equals(Task.FILE_DONE)) {
            return true;
        }
        if (status.equals(Task.FILE_NOT_DONE)) {
            return false;
        }
        throw new FloppyException("Unknown status '" + status + "'.");
    }

    /**
     * Checks that a line split into the number of fields its task type needs.
     *
     * @param fields        the fields of the line.
     * @param expectedCount how many fields the task type needs.
     * @throws FloppyException if the line has a different number of fields.
     */
    private static void requireFieldCount(String[] fields, int expectedCount) throws FloppyException {
        if (fields.length != expectedCount) {
            throw new FloppyException("Expected " + expectedCount + " fields but found "
                    + fields.length + ".");
        }
    }

    /**
     * Checks that no field of a line is empty.
     *
     * @param fields the fields of the line.
     * @throws FloppyException if any field is empty.
     */
    private static void requireNoBlankFields(String[] fields) throws FloppyException {
        for (String field : fields) {
            if (field.isBlank()) {
                throw new FloppyException("A line has an empty field.");
            }
        }
    }

    /**
     * Writes every task to the data file, replacing whatever the file held before.
     *
     * @param tasks the tasks to keep.
     * @throws FloppyException if the file cannot be written.
     */
    public void save(List<Task> tasks) throws FloppyException {
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(task.toFileFormat());
        }

        try {
            // A fresh copy of Floppy has no data folder yet, so create it before writing.
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new FloppyException("I couldn't write to " + filePath + ": " + e.getMessage());
        }
    }
}
