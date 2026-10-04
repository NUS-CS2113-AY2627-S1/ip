package dook.command.system;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dook.command.Command;
import dook.command.Response;
import dook.service.TaskManager;

/**
 * Represents the command for showing what other commands do and their formats.
 */
public class HelpCommand extends Command {
    private record Pair(String command, String description) {}

    private static final Pattern PATTERN = Pattern.compile("^help$");
    private static final List<Pair> COMMAND_DESCRIPTIONS = new ArrayList<>(List.of(
        new Pair("help",                "Show this list of commands."),
        new Pair("bye",                 "Exit this nightmare."),
        new Pair("list",                "View your overwhelmingly long task list."),
        new Pair("todo DESCRIPTION",    "Add a to-do task on to your already long list."),

        new Pair("deadline DESCRIPTION /by DATE_TIME",
                    "Add a deadline task, then procrastinate."),

        new Pair("event DESCRIPTION /from START_DATE_TIME /to END_DATE_TIME",
                    "Add an event task that you will surely bail out last minute."),

        new Pair("mark INDEX",          "Lie to yourself that the task is done."),
        new Pair("unmark INDEX",        "Shamefully mark that task as unfinished."),
        new Pair("delete INDEX",        "Run away from the task, permanently."),
        new Pair("delete all",          "Relieve yourself from all burdens."),
        new Pair("delete expired",      "Let go of tasks whose time has passed."),
        new Pair("find SEARCH_PHRASE",  "Search for tasks matching the phrase."),
        new Pair("undo",                "Second chance for those haunted by past mistakes."),
        new Pair("calendar",            "View a calendar of your task density."),
        new Pair("today",               "See what awaits you today."),
        new Pair("date DATE",           "See what awaits you on a specific date.")
    ));
    private static String helpMessage;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("You hurriedly scan the book as the light grows dim:");

        for (Pair pair : COMMAND_DESCRIPTIONS) {
            sb.append("\n    " + pair.command())
              .append("\n      " + pair.description())
              .append("\n");
        }

        sb.setLength(sb.length() - 1);
        helpMessage = sb.toString();
    }

    /**
     * Constructs a new HelpCommand with the specified user input.
     *
     * @param  userQuery Trimmed user input.
     */
    public HelpCommand(String userQuery) {
        super(userQuery);
    }

    /**
     * Attempts parsing user input into a new HelpCommand.
     *
     * @param  userQuery Trimmed user input.
     * @return           The new HelpCommand (optional).
     */
    public static Optional<Command> parse(String userQuery) {
        Matcher matcher = PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        return Optional.of(new HelpCommand(userQuery));
    }

    /**
     * Returns command guides as a response.
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
        return new Response(helpMessage);
    }
}
