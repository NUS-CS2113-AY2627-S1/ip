package nico.tasks;

import java.time.LocalDate;

/**
 * Represents a task that the user wants to keep track of.
 * Every task has a description and can be marked as done or not done.
 * Subclasses decide their own type icon and any extra details they store.
 */
public abstract class Task {
    /** Separator placed between fields when a task is written to the save file. */
    public static final String SAVE_DELIMITER = " | ";

    private static final String DONE_FLAG = "1";
    private static final String NOT_DONE_FLAG = "0";

    protected String description;
    protected boolean isDone;

    /**
     * Creates a task with the given description. The task starts out not done.
     *
     * @param description What the task is about.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the description of this task.
     *
     * @return The task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether this task has been marked as done.
     *
     * @return True if the task is done, false otherwise.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the one-letter icon that identifies the type of this task, e.g. "T" for a todo.
     *
     * @return The type icon of this task.
     */
    public abstract String getTypeIcon();

    /**
     * Returns whether this task takes place on, or is due on, the given date.
     * Tasks without dates (e.g. todos) never match.
     *
     * @param date The date to check.
     * @return True if the task falls on that date.
     */
    public boolean occursOn(LocalDate date) {
        return false;
    }

    /**
     * Returns this task in the format used by the save file,
     * e.g. <code>T | 1 | read book</code>.
     * Subclasses with extra details append them to this base format.
     *
     * @return The task as a single line of save-file text.
     */
    public String toSaveFormat() {
        String doneFlag = isDone ? DONE_FLAG : NOT_DONE_FLAG;
        return getTypeIcon() + SAVE_DELIMITER + doneFlag + SAVE_DELIMITER + description;
    }

    /**
     * Returns whether the given save-file flag means the task is done.
     *
     * @param flag The done flag read from the save file.
     * @return True if the flag represents a done task.
     */
    public static boolean isDoneFlag(String flag) {
        return DONE_FLAG.equals(flag);
    }

    @Override
    public String toString() {
        String statusIcon = isDone ? "X" : " ";
        return "[" + getTypeIcon() + "][" + statusIcon + "] " + description;
    }
}