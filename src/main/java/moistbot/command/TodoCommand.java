package moistbot.command;

import moistbot.task.Task;
import moistbot.task.TaskManager;

/**
 * Adds a basic todo task to the task list.
 */
public final class TodoCommand extends AddCommand {
    /**
     * Creates a todo command for the supplied description.
     *
     * @param description The todo task description
     */
    public TodoCommand(String description) {
        super(description);
    }

    /**
     * Adds the todo represented by this command.
     *
     * @param tasks The task list to modify
     * @return The todo task that was added
     */
    @Override
    protected Task createTask(TaskManager tasks) {
        return tasks.addTodo(getDescription());
    }
}
