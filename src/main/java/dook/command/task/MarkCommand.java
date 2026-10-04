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
 * Represents the command for marking a task as done.
 */
public class MarkCommand extends Command {
    private static final Pattern PATTERN = Pattern.compile("^mark\\s+(\\d+)$");
    private int taskIndex;
    private Task task;
    private boolean wasDone;

    /**
     * Constructs a new MarkCommand
     * with specified user input and task position.
     *
     * @param  userQuery Trimmed user input.
     * @param  taskIndex The position of the task in task list.
     */
    public MarkCommand(String userQuery, int taskIndex) {
        super(userQuery);
        this.taskIndex = taskIndex;
    }

    /**
     * Attempts parsing user input into a MarkCommand.
     * Looks for an integer to be task index.
     *
     * @param  userQuery Trimmed user input.
     * @return           The new MarkCommand (optional).
     */
    public static Optional<Command> parse(String userQuery) {
        Matcher matcher = PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        int taskIndex = Integer.parseInt(matcher.group(1)) - 1;
        return Optional.of(new MarkCommand(userQuery, taskIndex));
    }

    /**
     * Marks a task as done.
     * If the task status is already done, returns a special response message.
     * Else, saves the modified task list to local file.
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
        task = taskManager.getTask(taskIndex);
        wasDone = task.isDone();

        if (wasDone) {
            return new Response("The grave is already sealed. This task is finished:", task);
        }

        task.setDone(true);
        taskManager.save();
        return new Response("A debt is paid. Marked as done:", task);
    }

    /**
     * Marks the task status with its previous status.
     * Saves the modified task list to local file.
     *
     * @param taskManager {@inheritDoc}
     */
    @Override
    public void reverse(TaskManager taskManager) {
        task.setDone(wasDone);
        taskManager.save();
    }
}
