package dook.command.task;

import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dook.command.Command;
import dook.command.Response;
import dook.service.TaskManager;
import dook.task.Task;

import dook.exception.EmptyTaskListException;

/**
 * Represents the command for viewing the current task list in pretty format.
 */
public class ListCommand extends Command {
    private static final Pattern PATTERN = Pattern.compile("^list$");

    /**
     * Constructs a new ListCommand with specified user input.
     *
     * @param  userQuery Trimmed user input.
     */
    public ListCommand(String userQuery) {
        super(userQuery);
    }

    /**
     * Attempts parsing user input into a new ListCommand.
     *
     * @param  userQuery Trimmed user input.
     * @return           The new ListCommand (optional).
     */
    public static Optional<Command> parse(String userQuery) {
        Matcher matcher = PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        return Optional.of(new ListCommand(userQuery));
    }

    /**
     * Returns the pretty-print format of the task list as a response.
     *
     * @param  taskManager {@inheritDoc}
     * @param  commandLog  {@inheritDoc}
     * @param  random      {@inheritDoc}
     * @return             {@inheritDoc}
     * @throws EmptyTaskListException
     *         If the task list is empty.
     */
    @Override
    public Response execute(
        TaskManager taskManager,
        Deque<Command> commandLog,
        Random random
    ) {
        List<Task> tasks = taskManager.getAllTasks();
        if (tasks.isEmpty()) {
            throw new EmptyTaskListException();
        }

        String format = "%" + (tasks.size() / 10 + 1) + "d.";

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(format, 1))
          .append(tasks.get(0));

        for (int i = 1; i < tasks.size(); i++) {
            sb.append("\n")
              .append(String.format(format, i+1))
              .append(tasks.get(i));
        }

        return new Response(sb.toString());
    }
}
