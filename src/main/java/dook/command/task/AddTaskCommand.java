package dook.command.task;

import java.util.Deque;
import java.util.Optional;
import java.util.Random;

import dook.command.Command;
import dook.command.Response;
import dook.service.TaskManager;
import dook.task.Task;

/**
 * Represents the command for adding new task.
 */
public class AddTaskCommand extends Command {
    private boolean wasAdded = false;

    /**
     * Constructs a new AddTaskCommand with the specified user input.
     *
     * @param  userQuery Trimmed user input.
     */
    public AddTaskCommand(String userQuery) {
        super(userQuery);
    }

    /**
     * Returns a new AddTaskCommand for the given user input.
     * This parser always succeeds.
     *
     * @param  userQuery Trimmed user input.
     * @return           The new AddTaskCommand, always present.
     */
    public static Optional<Command> parse(String userQuery) {
        return Optional.of(new AddTaskCommand(userQuery));
    }

    /**
     * Attempts parsing user input into a task.
     * Saves the modified task list to local file.
     *
     * @param  taskManager {@inheritDoc}
     * @param  commandLog  {@inheritDoc}
     * @param  random      {@inheritDoc}
     * @return             {@inheritDoc}
     * @see dook.service.TaskManager#addTask(String)
     */
    @Override
    public Response execute(
        TaskManager taskManager,
        Deque<Command> commandLog,
        Random random
    ) {
        Task newTask = taskManager.addTask(userQuery);
        taskManager.save();
        wasAdded = true;
        return new Response("Scratched into the ledger:", newTask);
    }

    /**
     * Removes the newly created task,
     * which should be at the end of the task list.
     * Saves the modified task list to local file.
     *
     * @param taskManager {@inheritDoc}
     */
    @Override
    public void reverse(TaskManager taskManager) {
        if (wasAdded) {
            taskManager.deleteLastTask();
            taskManager.save();
        }
    }
}
