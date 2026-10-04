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
 * Represents the command for printing the greeting on screen.
 */
public class GreetCommand extends Command {
    private static final Pattern PATTERN = Pattern.compile("^\\s+");
    private static final String BANNER =
        """
                        ▓█████▄  ▒█████   ▒█████   ██ ▄█▀
                        ▒██▀ ██▌▒██▒  ██▒▒██▒  ██▒ ██▄█▒
                        ░██   █▌▒██░  ██▒▒██░  ██▒▓███▄░
                        ░▓█▄   ▌▒██   ██░▒██   ██░▓██ █▄
                        ░▒████▓ ░ ████▓▒░░ ████▓▒░▒██▒ █▄
                         ▒▒▓  ▒ ░ ▒░▒░▒░ ░ ▒░▒░▒░ ▒ ▒▒ ▓▒
                         ░ ▒  ▒   ░ ▒ ▒░   ░ ▒ ▒░ ░ ░▒ ▒░
                         ░ ░  ░ ░ ░ ░ ▒  ░ ░ ░ ▒  ░ ░░ ░
                           ░        ░ ░      ░ ░  ░  ░
                         ░
        """;
    private static final String[] GREETINGS = {
        "The shadows just got a little colder.",
        "You should not have opened this chat.",
        "Are you alone in the room right now?"
    };

    /**
     * Constructs a new GreetCommand with the specified user input.
     *
     * @param  userQuery Trimmed user input.
     */
    public GreetCommand(String userQuery) {
        super(userQuery);
    }

    /**
     * Attempts parsing user input into a new GreetCommand.
     * Looks for untrimmed whitespaces.
     *
     * @param  userQuery User input.
     * @return           The new GreetCommand (optional).
     */
    public static Optional<Command> parse(String userQuery) {
        Matcher matcher = PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        return Optional.of(new GreetCommand(userQuery));
    }

    /**
     * Returns the program banner and a randomly selected greeting
     * as the response.
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
        String greeting = GREETINGS[random.nextInt(GREETINGS.length)];
        return new Response(BANNER + "\nI am Dook.\n" + greeting);
    }
}
