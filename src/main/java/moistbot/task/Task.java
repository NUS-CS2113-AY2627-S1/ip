package moistbot.task;

import java.time.LocalDate;

/**
 * Represents a single task in the task list.
 * Each task has a description and a completion status that can be toggled.
 * Tasks can be of different types (todo, deadline, event) identified by a character code.
 */
public class Task {
    /** Type identifier used for basic todo tasks. */
    public static final char TYPE_TODO = 'T';
    /** Type identifier used for deadline tasks. */
    public static final char TYPE_DEADLINE = 'D';
    /** Type identifier used for event tasks. */
    public static final char TYPE_EVENT = 'E';

    private final String description;
    private boolean isCompleted;
    private final char taskType;

    /**
     * Constructs a Task with the given description and task type.
     * Tasks are initialized as not completed.
     *
     * @param description The description of the task
     * @param taskType The type of the task: T for todo, D for deadline, or E for event
     */
    public Task(String description, char taskType) {
        this.description = description;
        this.isCompleted = false;
        this.taskType = taskType;
    }

    /**
     * Constructs a generic todo task.
     * This is a convenience constructor that creates a Task of type TODO.
     *
     * @param description The description of the task
     */
    public Task(String description) {
        this(description, TYPE_TODO);
    }

    /**
     * Sets the completion status of this task.
     *
     * @param isCompleted True if the task is completed, false otherwise
     */
    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    /**
     * Returns the description of this task.
     *
     * @return The task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns a string representation of the task showing its description.
     * Subclasses may override to provide additional information.
     *
     * @return The task description
     */
    @Override
    public String toString() {
        return description;
    }

    /**
     * Returns whether this task has been completed.
     *
     * @return True if the task is completed, false otherwise
     */
    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * Returns the type character of this task.
     *
     * @return The task type: T, D, or E
     */
    public char getTaskType() {
        return taskType;
    }

    /**
     * Returns whether this task has a relevant date on or before a cutoff.
     * Undated task types are excluded from date-filtered lists by default.
     *
     * @param cutoffDate The inclusive date limit
     * @return True if the task is dated on or before the cutoff
     */
    public boolean isDatedOnOrBefore(LocalDate cutoffDate) {
        return false;
    }
}
