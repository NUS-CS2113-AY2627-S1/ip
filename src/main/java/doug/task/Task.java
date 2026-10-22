package doug.task;

import doug.errors.DougException;

/**
 * Represents a task with a type, description, and completion state.
 */
public abstract class Task {
    /**
     * Identifies the supported kinds of tasks.
     */
    public enum Type {
        TODO, DEADLINE, EVENT
    };

    /** Type-specific category of this task. */
    protected Type taskType;
    /** Whether this task has been completed. */
    protected boolean isDone;
    /** User-supplied description of this task. */
    protected String description;

    /**
     * Creates an incomplete task of the specified type.
     *
     * @param taskType category of task to create
     * @param description description of the task
     */
    public Task(Type taskType, String description) {
        this.taskType = taskType;
        this.isDone = false;
        this.description = description;
    }

    /**
     * Reconstructs a task with an existing completion state.
     *
     * @param taskType category of task to create
     * @param isDone whether the task has been completed
     * @param description description of the task
     */
    public Task(Type taskType, Boolean isDone, String description) {
        this.taskType = taskType;
        this.isDone = isDone;
        this.description = description;
    }

    /**
     * Resolves the one-character task code used in the save file.
     *
     * @param taskChar saved task-type code
     * @return task type represented by the code
     * @throws DougException if the code does not represent a supported task type
     */
    private static Type resolveTaskType(String taskChar) throws DougException {
        switch (taskChar) {
            case "T":
                return Task.Type.TODO;
            case "D":
                return Task.Type.DEADLINE;
            case "E":
                return Task.Type.EVENT;
            default:
                throw new DougException("Corrupted TaskType");
        }
    }

    /**
     * Resolves the completion-state code used in the save file.
     *
     * @param isDoneChar saved completion-state code
     * @return {@code true} for {@code "1"}; {@code false} for {@code "0"}
     * @throws DougException if the code is neither {@code "0"} nor {@code "1"}
     */
    private static boolean resolveIsDone(String isDoneChar) {
        if (!isDoneChar.equals("0") && !isDoneChar.equals("1"))
            throw new DougException("Corrupted isDone");
        return isDoneChar.equals("1");
    }

    /**
     * Reconstructs a task from its pipe-delimited storage representation.
     *
     * @param input serialized task data
     * @return task represented by the saved data
     * @throws DougException if the data is incomplete or contains an invalid task type
     *                       or completion state
     */
    public static Task fromSaveFormat(String input) {
        String[] taskParams = input.split("\\|");
        if (taskParams.length < 3)
            throw new DougException("Save file corrupted: Not enough arguments.");

        Type parsedTaskType = resolveTaskType(taskParams[0]);
        boolean parsedIsDone = resolveIsDone(taskParams[1]);
        String parsedDescription = taskParams[2];

        try {
            switch (parsedTaskType) {
                case TODO:
                    return new Todo(parsedDescription, parsedIsDone);
                case DEADLINE:
                    return new Deadline(parsedDescription, taskParams[3], parsedIsDone);
                case EVENT:
                    return new Event(parsedDescription, taskParams[3], taskParams[4], parsedIsDone);
                default:
                    throw new DougException("Unknown task type.");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DougException("Save file corrupted: Missing date arguments.");
        }
    }

    /**
     * Converts this task to its pipe-delimited storage representation.
     *
     * @return serialized form of this task
     */
    public abstract String toSaveFormat();

    /**
     * Returns this task's description.
     *
     * @return task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns this task's type.
     *
     * @return task type
     */
    public Type getTaskType() {
        return taskType;
    }

    /**
     * Returns whether this task has been completed.
     *
     * @return {@code true} if the task is complete; {@code false} otherwise
     */
    public boolean getIsDone() {
        return isDone;
    }

    /**
     * Changes this task's completion state.
     *
     * @param input completion state to assign
     */
    public void setDone(boolean input) {
        isDone = input;
    }

    /**
     * Returns a display representation containing the task type, completion marker,
     * and description.
     *
     * @return formatted task for display to the user
     */
    @Override
    public String toString() {
        char taskTypeChar = taskType.toString().charAt(0);
        char isDoneChar = (isDone) ? 'X' : ' ';
        return String.format("[%c][%c] %s", taskTypeChar, isDoneChar, getDescription());
    }
}
