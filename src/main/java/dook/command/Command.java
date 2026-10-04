package dook.command;

import java.util.Deque;
import java.util.Random;

import dook.service.TaskManager;

/**
 * Represents the template for commands.
 */
public abstract class Command {
    /**
     * Represents the user input parsed into this command.
     * Uses for recording in command log.
     */
    protected String userQuery;

    /**
     * Represents the template constructors of the command
     * with specified user input.
     *
     * @param  userQuery Trimmed user input.
     */
    protected Command(String userQuery) {
        this.userQuery = userQuery;
    }

    /**
     * Retrieves the user input parsed into this command.
     *
     * @return Corresponding user input.
     */
    public String getUserQuery() {
        return userQuery;
    }

    /**
     * Represents the default undoing of commands.
     * Does nothing.
     *
     * @param taskManager The task manager containing the task list.
     */
    public void reverse(TaskManager taskManager) {}

    /**
     * Represents the template for execution of the command.
     *
     * @param  taskManager The task manager containing the task list.
     * @param  commandLog  The history log of previously executed commands.
     * @param  random      The shared random number generator.
     * @return             The response, to be printed on screen.
     */
    public abstract Response execute(
        TaskManager taskManager,
        Deque<Command> commandLog,
        Random random
    );
}
