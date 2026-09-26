package mimimeow.task;

import java.util.Locale;

/** Represents a task in MimiMeow's task list. */
public abstract class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates a task with the specified non-blank description.
     *
     * @param description task description
     */
    protected Task(String description) {
        if (description == null || description.isBlank()) {
            throw new InvalidTaskException("A task needs a description.");
        }
        this.description = description.trim();
        this.isDone = false;
    }

    /**
     * Returns the completion status icon for this task.
     *
     * @return completion status icon
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Returns whether this task is marked as done.
     *
     * @return true if the task is complete
     */
    public boolean isDone() {
        return isDone;
    }


    /**
     * Returns this task's common fields in the format used for storage.
     *
     * @return serialized common task fields
     */
    /** Returns whether the description contains the specified keyword, ignoring letter case. */
    public boolean hasDescriptionContaining(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return false;
        }
        return description.toLowerCase(Locale.ROOT).contains(keyword.trim().toLowerCase(Locale.ROOT));
    }

    /** Returns this task's common fields in the format used for storage. */

    public String toFileString() {
        return (isDone ? "1" : "0") + " | " + escapeFileField(description);
    }

    /**
     * Escapes characters that otherwise have special meaning in the storage format.
     *
     * @param field field to escape
     * @return escaped field
     */
    protected String escapeFileField(String field) {
        return field.replace("\\", "\\\\").replace("|", "\\|");
    }

    /** Returns this task in the format used when displaying the task list. */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    /** Marks this task as done. */
    public void setAsDone() {
        isDone = true;
    }

    /** Marks this task as not done. */
    public void setAsNotDone() {
        isDone = false;
    }
}

