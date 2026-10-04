package dook.command.system;

import java.util.Deque;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dook.command.Command;
import dook.command.Response;
import dook.exception.EmptyCommandLogException;
import dook.service.TaskManager;

/**
 * Represents the command for undoing the previous commands.
 */
public class UndoCommand extends Command {
    private static final Pattern PATTERN = Pattern.compile("^undo$");

    /**
     * Constructs a new UndoCommand with the specified user input.
     *
     * @param  userQuery Trimmed user input.
     */
    public UndoCommand(String userQuery) {
        super(userQuery);
    }

    /**
     * Attempts parsing user input into a new UndoCommand.
     *
     * @param  userQuery Trimmed user input.
     * @return           The new UndoCommand (optional).
     */
    public static Optional<Command> parse(String userQuery) {
        Matcher matcher = PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        return Optional.of(new UndoCommand(userQuery));
    }

    /**
     * Undoes the previous command by removing it from the command log
     * and reversing its effects.
     *
     * @param  taskManager {@inheritDoc}
     * @param  commandLog  {@inheritDoc}
     * @param  random      {@inheritDoc}
     * @return             {@inheritDoc}
     * @throws EmptyCommandLogException If there is no previous command to undo.
     */
    @Override
    public Response execute(
        TaskManager taskManager,
        Deque<Command> commandLog,
        Random random
    ) {
        commandLog.pop();
        if (commandLog.size() == 0) {
            throw new EmptyCommandLogException();
        }

        Command lastCommand = commandLog.pop();
        lastCommand.reverse(taskManager);
        return new Response("History was rewritten. Past mistake was amended:\n  "
                            + lastCommand.getUserQuery());
    }
}
