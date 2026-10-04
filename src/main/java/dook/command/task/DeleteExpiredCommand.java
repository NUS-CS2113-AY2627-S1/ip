package dook.command.task;

import java.util.Collection;
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
 * Represents the command for clearing expired tasks from the task list.
 */
public class DeleteExpiredCommand extends Command {
    private static final Pattern PATTERN = Pattern.compile("^delete expired$");
    private Collection<Task> tasks;

    /**
     * Constructs a new DeleteExpiredCommand with the specified user input.
     *
     * @param  userQuery Trimmed user input.
     */
    public DeleteExpiredCommand(String userQuery) {
        super(userQuery);
    }

    /**
     * Attempts parsing user input into a new DeleteExpiredCommand.
     *
     * @param  userQuery Trimmed user input.
     * @return           The new DeleteExpiredCommand (optional).
     */
    public static Optional<Command> parse(String userQuery) {
        Matcher matcher = PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        return Optional.of(new DeleteExpiredCommand(userQuery));
    }

    /**
     * Removes all expired tasks from the task list and saves the result.
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
        tasks = taskManager.getAllTasks();
        taskManager.deleteExpiredTasks();
        taskManager.save();
        return new Response("You run off into a new void and leave everything behind.");
    }

    /**
     * Adds back all the deleted tasks.
     * Saves the new task list to the local file.
     *
     * @param taskManager {@inheritDoc}
     */
    @Override
    public void reverse(TaskManager taskManager) {
        taskManager.deleteAllTasks();
        taskManager.addTasks(tasks);
        taskManager.save();
    }
}
