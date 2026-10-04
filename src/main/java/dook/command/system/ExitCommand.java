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
 * Represents the command for closing the CLI program.
 */
public class ExitCommand extends Command {
    private static final Pattern PATTERN = Pattern.compile("^bye");

    /**
     * Constructs a new ExitCommand with the specified user input.
     *
     * @param  userQuery Trimmed user input.
     */
    public ExitCommand(String userQuery) {
        super(userQuery);
    }

    /**
     * Attempts parsing user input into a new ExitCommand.
     *
     * @param  userQuery Trimmed user input.
     * @return           The new ExitCommand (optional).
     */
    public static Optional<Command> parse(String userQuery) {
        Matcher matcher = PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        return Optional.of(new ExitCommand(userQuery));
    }

    /**
     * Closes the program.
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
        System.out.print("\nGoodbye for now.\n");

        System.exit(0);
        return null;
    }
}
