package floppy.task;

/**
 * Represents a single item that Floppy is keeping track of, together with
 * whether the user has finished it.
 *
 * <p>The fields are {@code protected} rather than {@code private} so that later
 * increments can introduce specialised kinds of tasks that inherit from this class.
 */
public class Task {

    /** Separates the fields of a task when it is written to the data file. */
    public static final String FILE_FIELD_SEPARATOR = " | ";

    /** Marks a finished task in the data file. */
    public static final String FILE_DONE = "1";

    /** Marks an unfinished task in the data file. */
    public static final String FILE_NOT_DONE = "0";

    /** What the user asked Floppy to remember. */
    protected String description;

    /** Whether the user has marked this task as finished. */
    protected boolean isDone;

    /**
     * Constructs a task that starts out not done.
     *
     * @param description what the user asked Floppy to remember.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns what the user asked Floppy to remember.
     *
     * @return this task's description.
     */
    public String getDescription() {
        return description;
    }

    /** Marks this task as finished. */
    public void markAsDone() {
        this.isDone = true;
    }

    /** Marks this task as not finished, undoing a previous {@link #markAsDone()}. */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns the single character shown inside the status brackets.
     *
     * @return "X" if this task is done, a space otherwise.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns this task rendered as a status box followed by its description,
     * for example {@code [X] read book}.
     *
     * @return the display form of this task.
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    /**
     * Returns the done status and description of this task in the data file format,
     * for example {@code 1 | read book}. Subclasses put their type code in front and
     * append any fields of their own.
     *
     * @return the fields every kind of task shares, ready to be written to the data file.
     */
    public String toFileFormat() {
        String status = isDone ? FILE_DONE : FILE_NOT_DONE;
        return status + FILE_FIELD_SEPARATOR + description;
    }
}
