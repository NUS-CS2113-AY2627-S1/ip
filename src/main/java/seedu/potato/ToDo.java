package seedu.potato;

/**
 * Represents a task without an attached date or time.
 */
public class ToDo extends Task {
    static final int DATA_FIELD_COUNT = 3;

    private static final String TODO_SYMBOL = "[T]";

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description Description of the task.
     */
    public ToDo(String description) {
        super(description);
    }

    /**
     * Checks whether input contains a non-blank to-do description.
     *
     * @param taskInput Raw input following the to-do command word.
     * @return {@code true} if the input can be used to create a to-do.
     */
    public static boolean isValidInput(String taskInput) {
        return taskInput != null && !taskInput.isBlank();
    }

    /**
     * Creates a to-do from input that has already been validated.
     *
     * @param taskInput Raw input following the to-do command word.
     * @return To-do represented by the input.
     * @throws IllegalArgumentException If the input is not valid for a to-do.
     */
    static ToDo createFromInput(String taskInput) {
        if (!isValidInput(taskInput)) {
            throw new IllegalArgumentException("Invalid to-do input. Format: todo [description]");
        }
        return new ToDo(taskInput);
    }

    /**
     * Returns this to-do in the format used for persistent storage.
     *
     * @return Save-file representation of this to-do.
     */
    @Override
    public String toDataString() {
        return "T | " + getDataStatus() + " | " + description;
    }

    /**
     * Returns the to-do description with its type and completion icons.
     *
     * @return Display-ready task text.
     */
    @Override
    public String toString() {
        return TODO_SYMBOL + "[" + getStatusIcon() + "] " + description;
    }
}
