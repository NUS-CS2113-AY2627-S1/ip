package moistbot.command;

import moistbot.exception.MoistBotException;
import moistbot.storage.Storage;
import moistbot.task.Task;
import moistbot.task.TaskManager;
import moistbot.ui.UserInterface;

/**
 * Provides the shared persistence and feedback workflow for commands that add tasks.
 */
public abstract class AddCommand extends Command {
    private final String description;

    /**
     * Creates an add command containing a description only.
     *
     * @param description The task description
     */
    protected AddCommand(String description) {
        this.description = description;
    }

    /**
     * Adds and saves a task, restoring the previous list if saving fails.
     *
     * @param tasks The task list to modify
     * @param ui The user interface used to confirm the addition
     * @param storage The persistence service used to save the updated list
     * @throws MoistBotException if the updated task list cannot be saved
     */
    @Override
    public void execute(TaskManager tasks, UserInterface ui, Storage storage) throws MoistBotException {
        Task task = createTask(tasks);
        try {
            storage.saveTasks(tasks);
        } catch (MoistBotException e) {
            tasks.removeLastTask();
            throw e;
        }
        ui.printAddTask(task, tasks.getSize());
    }

    /**
     * Creates and adds the task represented by this command.
     *
     * @param tasks The task list to modify
     * @return The task that was added
     */
    protected abstract Task createTask(TaskManager tasks);

    /**
     * Returns the description of the task to add.
     *
     * @return The task description
     */
    protected String getDescription() {
        return description;
    }
}
