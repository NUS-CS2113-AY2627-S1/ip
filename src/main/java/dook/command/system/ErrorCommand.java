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
 * Represents the command for intentionally triggering a runtime exception.
 */
public class ErrorCommand extends Command {
    private static final Pattern PATTERN = Pattern.compile("^error$");

    /**
     * Constructs a new ErrorCommand with the specified user input.
     *
     * @param  userQuery Trimmed user input.
     */
    public ErrorCommand(String userQuery) {
        super(userQuery);
    }

    /**
     * Attempts parsing user input into a new ErrorCommand.
     *
     * @param  userQuery Trimmed user input.
     * @return           The new ErrorCommand (optional).
     */
    public static Optional<Command> parse(String userQuery) {
        Matcher matcher = PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        return Optional.of(new ErrorCommand(userQuery));
    }

    /**
     * Throws a generic runtime exception intentionally.
     *
     * @param  taskManager {@inheritDoc}
     * @param  commandLog  {@inheritDoc}
     * @param  random      {@inheritDoc}
     * @return             {@inheritDoc}
     * @throws RuntimeException On purpose.
     */
    @Override
    public Response execute(
        TaskManager taskManager,
        Deque<Command> commandLog,
        Random random
    ) {
        throw new RuntimeException();
    }
}
