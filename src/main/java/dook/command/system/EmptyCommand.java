package dook.command.system;

import java.util.Deque;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dook.command.Command;
import dook.command.Response;
import dook.service.TaskManager;

/**
 * Represents the command for empty user input.
 */
public class EmptyCommand extends Command {
    private static final Pattern PATTERN = Pattern.compile("^$");

    /**
     * Constructs a new EmptyCommand with the specified user input.
     *
     * @param  userQuery Trimmed user input.
     */
    public EmptyCommand(String userQuery) {
        super(userQuery);
    }

    /**
     * Attempts parsing user input into a new EmptyCommand.
     *
     * @param  userQuery Trimmed user input.
     * @return           The new EmptyCommand (optional).
     */
    public static Optional<Command> parse(String userQuery) {
        Matcher matcher = PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        return Optional.of(new EmptyCommand(userQuery));
    }

    /**
     * Returns a special message as a response for empty user input.
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
        return new Response("Silence won't keep you safe.");
    }
}
