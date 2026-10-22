package clanky.task;

/**
 * Represents a To-Do task.
 * A {@code Event} extends {@link Task} and requires a description.
 */
public class Todo extends Task{

    /**
     * Creates a new To-Do task with the given description
     *
     * @param  description a description of the task to-do
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the single-letter type icon used to identify this task type
     * in list and save-file output
     *
     * @return "T", representing a To-Do task
     */
    @Override
    public String getTaskType(){
        return "T";
    }
}
