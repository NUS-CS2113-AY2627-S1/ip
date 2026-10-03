package turing.task;

import java.time.LocalDate;

/**
 * Represents a task that has to be done before a given date/time,
 * e.g. {@code return book (by: Oct 15 2019)}.
 */
public class Deadline extends Task {
    /** Icon identifying this kind of task, in both the display and the save file. */
    public static final String TYPE_ICON = "D";

    /** When the task is due. */
    protected final TaskTime by;

    /**
     * Creates a deadline that starts off as not done.
     *
     * @param description What the user has to do.
     * @param by When the task is due, as the user wrote it.
     */
    public Deadline(String description, String by) {
        super(description);
        // The due date arrives as text whether the user just typed it or it
        // came back from the save file, so a deadline reads its own date.
        this.by = TaskTime.of(by);
    }

    @Override
    public String getTypeIcon() {
        return TYPE_ICON;
    }

    @Override
    public boolean isOn(LocalDate date) {
        return by.isOn(date);
    }

    /**
     * Returns the deadline in save form, e.g. {@code D | 0 | return book | Sunday}.
     *
     * @return Save form of this deadline.
     */
    @Override
    public String toSaveFormat() {
        return super.toSaveFormat() + SAVE_SEPARATOR + by.toSaveFormat();
    }

    /**
     * Returns the deadline formatted for display, e.g.
     * {@code [D][ ] return book (by: Oct 15 2019)}. The shared part of the text comes
     * from the superclass, so only the due date is added here.
     *
     * @return Display form of this deadline.
     */
    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}
