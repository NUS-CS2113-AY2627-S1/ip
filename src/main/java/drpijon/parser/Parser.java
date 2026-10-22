package drpijon.parser;

import drpijon.command.Command;
import drpijon.command.DeadlineCommand;
import drpijon.command.DeleteCommand;
import drpijon.command.EventCommand;
import drpijon.command.ExitCommand;
import drpijon.command.FindCommand;
import drpijon.command.ListCommand;
import drpijon.command.MarkCommand;
import drpijon.command.OnCommand;
import drpijon.command.TodoCommand;
import drpijon.command.UnmarkCommand;
import drpijon.exception.DrPijonException;

/**
 * Interprets raw console input as a command and its arguments.
 */
public class Parser {
    /**
     * Creates a parser.
     */
    public Parser() {
    }

    /**
     * Parses one line of console input.
     *
     * @param inputLine raw console input
     * @return executable command represented by the input
     * @throws DrPijonException when the command keyword is not recognized
     */
    public static Command parse(String inputLine) throws DrPijonException {
        String[] inputParts = inputLine.trim().split("\\s+", 2);
        String command = inputParts[0];
        String arguments = inputParts.length > 1 ? inputParts[1] : "";

        switch (command) {
        case "bye":
            return new ExitCommand();
        case "list":
            return new ListCommand();
        case "on":
            return new OnCommand(arguments);
        case "find":
            return new FindCommand(arguments);
        case "mark":
            return new MarkCommand(arguments);
        case "unmark":
            return new UnmarkCommand(arguments);
        case "delete":
            return new DeleteCommand(arguments);
        case "todo":
            return new TodoCommand(arguments);
        case "deadline":
            return new DeadlineCommand(arguments);
        case "event":
            return new EventCommand(arguments);
        default:
            throw new DrPijonException("I DONT KNOW THAT COMMAND. Try: list, on, find, todo, deadline, event, "
                    + "mark, unmark, delete, or bye ^w^");
        }
    }
}
