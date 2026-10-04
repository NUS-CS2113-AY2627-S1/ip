package dook.service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import dook.command.Command;
import dook.command.Response;
import dook.command.system.EmptyCommand;
import dook.command.system.ErrorCommand;
import dook.command.system.ExitCommand;
import dook.command.system.GreetCommand;
import dook.command.system.HelpCommand;
import dook.command.system.UndoCommand;
import dook.command.task.AddTaskCommand;
import dook.command.task.DeleteAllCommand;
import dook.command.task.DeleteCommand;
import dook.command.task.DeleteExpiredCommand;
import dook.command.task.FindCommand;
import dook.command.task.ListCommand;
import dook.command.task.MarkCommand;
import dook.command.task.UnmarkCommand;
import dook.command.time.CalendarCommand;
import dook.command.time.DateCommand;
import dook.command.time.TodayCommand;
import dook.exception.UnknownCommandException;
import dook.parser.Parser;

/**
 * Manages the processing of user inputs.
 */
public class CommandManager {
    private static final List<Parser<Command>> PARSERS = List.of(
        GreetCommand::parse,
        ExitCommand::parse,
        EmptyCommand::parse,
        ListCommand::parse,
        MarkCommand::parse,
        UnmarkCommand::parse,
        FindCommand::parse,
        DeleteCommand::parse,
        DeleteAllCommand::parse,
        DeleteExpiredCommand::parse,
        ErrorCommand::parse,
        HelpCommand::parse,
        UndoCommand::parse,
        CalendarCommand::parse,
        TodayCommand::parse,
        DateCommand::parse,
        AddTaskCommand::parse
    );

    private Deque<Command> commandLog = new ArrayDeque<>();
    private TaskManager taskManager;
    private Random random;

    /**
     * Constructs a new CommandManager with specified task manager and random generator.
     *
     * @param  taskManager The task manager, contains task list.
     * @param  random      The random number generator.
     */
    public CommandManager(TaskManager taskManager, Random random) {
        this.taskManager = taskManager;
        this.random = random;
    }

    /**
     * Parses the user input into a command.
     * Adds the command into the command log.
     * Executes the command and gives response.
     *
     * @param  userQuery Trimmed user input.
     * @return           The response asnwering user input.
     * @throws UnknownCommandException If parsing fails.
     */
    public Response processQuery(String userQuery) {
        for (Parser<Command> parser : PARSERS) {
            Optional<Command> maybeCommand = parser.parse(userQuery);
            if (maybeCommand.isPresent()) {
                Command command = maybeCommand.get();
                commandLog.push(command);
                return command.execute(taskManager, commandLog, random);
            }
        }

        throw new UnknownCommandException();
    }
}
