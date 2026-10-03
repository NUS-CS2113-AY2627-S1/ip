package turing.task;

import java.time.LocalDate;

/**
 * Represents a single task in the user's list, along with whether it is done.
 * Every task has a type, so this class is abstract: the concrete subclasses
 * {@link Todo}, {@link Deadline} and {@link Event} each supply their own type
 * icon and, where they carry extra date/time information, their own display form.
 */
public abstract class Task {
    /** Separator written between the fields of a task in the save file. */
    public static final String SAVE_SEPARATOR = " | ";

    /** Field written in the save file for a task that is done. */
    public static final String DONE_FLAG = "1";

    /** Field written in the save file for a task that is not done. */
    public static final String NOT_DONE_FLAG = "0";

    /** What the user has to do, exactly as typed. */
    protected String description;

    /** True once the task has been marked as done. */
    protected boolean isDone;

    /**
     * Creates a task that starts off as not done.
     *
     * @param description What the user has to do.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns what the user has to do, without the type or status boxes.
     *
     * @return Description of this task.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the single-character icon identifying the kind of task, such as
     * "T" for a todo. Each subclass decides its own icon, which is why this
     * method has no body here.
     *
     * @return Single-character type icon.
     */
    public abstract String getTypeIcon();

    /**
     * Returns the icon shown inside the status box: "X" when done, a space otherwise.
     * Using a space keeps every task the same width, so the list stays aligned.
     *
     * @return Single-character status icon.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns whether this task falls on the given date. A task carrying no
     * date never does, so only the kinds of task that have one override this.
     *
     * @param date Date to test against.
     * @return True if this task is on that date.
     */
    public boolean isOn(LocalDate date) {
        return false;
    }

    /** Marks this task as done. */
    public void markAsDone() {
        this.isDone = true;
    }

    /** Marks this task as not done yet. */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns the task in the form written to the save file, e.g.
     * {@code T | 1 | read book}. The type icon comes first so that a saved
     * line can be read back into a task of the right kind, and a subclass
     * carrying extra information appends it in the order its constructor
     * expects. Display uses {@link #toString()} instead: the two forms are
     * kept apart so that reworded output cannot make an old save file
     * unreadable.
     *
     * @return Save form of this task.
     */
    public String toSaveFormat() {
        return getTypeIcon() + SAVE_SEPARATOR + (isDone ? DONE_FLAG : NOT_DONE_FLAG)
                + SAVE_SEPARATOR + description;
    }

    /**
     * Returns the task formatted for display, e.g. {@code [T][X] read book}.
     * Overriding toString lets the task decide how it looks, so the chatbot
     * does not need to know about the type and status boxes at all. Subclasses
     * that carry extra information append it to this text.
     *
     * @return Display form of this task.
     */
    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + description;
    }
}
