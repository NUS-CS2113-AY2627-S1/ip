package lumi.task;

/**
 * Represents a single item of work tracked by Lumi.
 * A task carries a description and a completion status; each concrete
 * subclass decides the icon that identifies its type in the task listing.
 */
public abstract class Task {
    /** The character that may not appear in task text, because the save file uses it. */
    public static final String SEPARATOR_CHARACTER = "|";

    /** Separates the fields of one task when it is written to the save file. */
    public static final String FIELD_SEPARATOR = " " + SEPARATOR_CHARACTER + " ";

    private static final String ICON_DONE = "X";
    private static final String ICON_NOT_DONE = " ";
    private static final String SAVED_DONE = "1";
    private static final String SAVED_NOT_DONE = "0";

    protected final String description;
    protected boolean isDone;

    /**
     * Creates a task that starts out not done.
     *
     * @param description What has to be done.
     */
    protected Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Returns the single-character icon that identifies this task's type. */
    public abstract String getTypeIcon();

    /** Returns the icon shown in the status box: marked when done, blank otherwise. */
    public String getStatusIcon() {
        return isDone ? ICON_DONE : ICON_NOT_DONE;
    }

    /**
     * Returns this task as one line of the save file, shaped as
     * {@code T | 1 | read book}. Subclasses append their own fields to this.
     */
    public String toSaveFormat() {
        return getTypeIcon() + FIELD_SEPARATOR
                + (isDone ? SAVED_DONE : SAVED_NOT_DONE) + FIELD_SEPARATOR
                + description;
    }

    /**
     * Returns true if the given text appears in this task's description.
     * Only the description is searched, so looking for "d" does not match
     * every deadline through its type icon.
     *
     * @param keyword Text to look for.
     */
    public boolean hasKeyword(String keyword) {
        return description.contains(keyword);
    }

    /** Records that this task has been completed. */
    public void markAsDone() {
        isDone = true;
    }

    /** Records that this task is outstanding again. */
    public void markAsNotDone() {
        isDone = false;
    }

    /** Returns the listing form, shaped as {@code [T][X] description}. */
    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + description;
    }
}
