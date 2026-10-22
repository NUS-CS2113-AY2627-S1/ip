package eric.task;

import java.util.regex.Pattern;

/**
 * Base class for tasks tracked by Eric. Every task has a description and a
 * done/not-done status. Concrete task types (Todo, Deadline, Event) supply
 * their own type tag and extend the displayed text with their extra details.
 */
public abstract class Task {
    /** Separates the columns of a task in the save file. Task text must not contain it. */
    public static final String FILE_SEPARATOR = " | ";

    private static final String FLAG_DONE = "1";
    private static final String FLAG_NOT_DONE = "0";

    private static final int COLUMN_TYPE = 0;
    private static final int COLUMN_IS_DONE = 1;
    private static final int COLUMN_DESCRIPTION = 2;
    private static final int COLUMN_FIRST_DETAIL = 3;
    private static final int COLUMN_SECOND_DETAIL = 4;
    private static final int COLUMNS_TODO = 3;
    private static final int COLUMNS_DEADLINE = 4;
    private static final int COLUMNS_EVENT = 5;

    protected String description;
    protected boolean isDone;

    /**
     * Creates a task that is not yet done.
     *
     * @param description What the task is about.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Returns what the task is about. */
    public String getDescription() {
        return description;
    }

    /** Returns "X" if the task is done, or a space otherwise. */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /** Marks this task as done. */
    public void markDone() {
        isDone = true;
    }

    /** Marks this task as not done. */
    public void markUndone() {
        isDone = false;
    }

    /** Returns the one-letter tag identifying the task type, e.g. "T", "D", "E". */
    public abstract String getTypeIcon();

    /** Returns this task as one line of the save file, e.g. "T | 1 | read book". */
    public String toFileString() {
        return getTypeIcon() + FILE_SEPARATOR + (isDone ? FLAG_DONE : FLAG_NOT_DONE)
                + FILE_SEPARATOR + description;
    }

    /**
     * Creates a task from one line of the save file, e.g. "D | 0 | return book | June 6th".
     *
     * @param line Line in the format written by {@link #toFileString()}.
     * @return The task described by {@code line}.
     * @throws IllegalArgumentException If {@code line} is not in the save file format. The message
     *         explains what is wrong.
     */
    public static Task fromFileString(String line) {
        String[] columns = line.split(Pattern.quote(FILE_SEPARATOR), -1);
        if (columns.length < COLUMNS_TODO) {
            throw new IllegalArgumentException("expected at least " + COLUMNS_TODO
                    + " columns but found " + columns.length);
        }

        boolean isDone = parseIsDone(columns[COLUMN_IS_DONE]);
        Task task = createTask(columns);
        if (isDone) {
            task.markDone();
        }
        return task;
    }

    /** Converts the done flag of a save file line to a boolean, or throws if it is not 0 or 1. */
    private static boolean parseIsDone(String flag) {
        if (flag.equals(FLAG_DONE)) {
            return true;
        }
        if (flag.equals(FLAG_NOT_DONE)) {
            return false;
        }
        throw new IllegalArgumentException("the done flag must be " + FLAG_NOT_DONE + " or "
                + FLAG_DONE + " but was \"" + flag + "\"");
    }

    /** Creates the task of the type named in the first column, checking that its columns are present. */
    private static Task createTask(String[] columns) {
        String type = columns[COLUMN_TYPE];
        return switch (type) {
        case Todo.TYPE_ICON -> {
            requireColumns(columns, COLUMNS_TODO);
            yield new Todo(columns[COLUMN_DESCRIPTION]);
        }
        case Deadline.TYPE_ICON -> {
            requireColumns(columns, COLUMNS_DEADLINE);
            yield new Deadline(columns[COLUMN_DESCRIPTION], columns[COLUMN_FIRST_DETAIL]);
        }
        case Event.TYPE_ICON -> {
            requireColumns(columns, COLUMNS_EVENT);
            yield new Event(columns[COLUMN_DESCRIPTION], columns[COLUMN_FIRST_DETAIL],
                    columns[COLUMN_SECOND_DETAIL]);
        }
        default -> throw new IllegalArgumentException("unknown task type \"" + type + "\"");
        };
    }

    /** Throws if there are not exactly {@code expected} columns, or if a text column is empty. */
    private static void requireColumns(String[] columns, int expected) {
        if (columns.length != expected) {
            throw new IllegalArgumentException("expected " + expected + " columns for a "
                    + columns[COLUMN_TYPE] + " task but found " + columns.length);
        }
        for (int i = COLUMN_DESCRIPTION; i < columns.length; i++) {
            if (columns[i].isEmpty()) {
                throw new IllegalArgumentException("column " + (i + 1) + " is empty");
            }
        }
    }

    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + description;
    }
}
