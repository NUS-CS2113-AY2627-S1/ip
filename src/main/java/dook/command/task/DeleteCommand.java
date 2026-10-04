package dook.command.task;

import java.util.Deque;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dook.command.Command;
import dook.command.Response;
import dook.service.TaskManager;
import dook.task.Task;

/**
 * Represents the command for deleting a task.
 */
public class DeleteCommand extends Command {
    private static final Pattern PATTERN = Pattern.compile("^delete\\s+(\\d+)$");
    private int taskIndex;
    private Task task;

    /**
     * Constructs a new DeleteCommand
     * with the specified user input and task position.
     *
     * @param  userQuery Trimmed user input.
     * @param  taskIndex The position of the task in task list.
     */
    public DeleteCommand(String userQuery, int taskIndex) {
        super(userQuery);
        this.taskIndex = taskIndex;
    }

    /**
     * Attempts parsing user input into a new DeleteCommand.
     * Looks for an integer to be task index.
     *
     * @param  userQuery Trimmed user input.
     * @return           The new DeleteCommand (optional).
     */
    public static Optional<Command> parse(String userQuery) {
        Matcher matcher = PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        int taskIndex = Integer.parseInt(matcher.group(1)) - 1;
        return Optional.of(new DeleteCommand(userQuery, taskIndex));
    }

    /**
     * Removes the task.
     * Saves the modified task list to local file.
     *
     * @param  taskManager {@inheritDoc}
     * @param  commandLog  {@inheritDoc}
     * @param  random      {@inheritDoc}
     * @return             {@inheritDoc}
     */
    @Override
    public Response execute(
        TaskManager taskManager,
        Deque<Command> commandLog,
        Random random
    ) {
        task = taskManager.deleteTask(taskIndex);
        taskManager.save();
        return new Response("Ripped off the ledger:", task);
    }

    /**
     * Adds back the deleted task to its previous position.
     * Saves the modified task list to local file.
     *
     * @param taskManager {@inheritDoc}
     */
    @Override
    public void reverse(TaskManager taskManager) {
        taskManager.addTask(taskIndex, task);
        taskManager.save();
    }
}
