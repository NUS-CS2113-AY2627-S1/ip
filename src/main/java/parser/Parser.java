package parser;

import command.AddCommand;
import command.Command;
import command.DeleteCommand;
import command.ExitCommand;
import command.FindCommand;
import command.ListCommand;
import command.MarkCommand;
import command.UnmarkCommand;

/**
 * Converts raw user input into executable commands.
 */
public final class Parser {
    private Parser() {
    }

    /**
     * Returns the command represented by the user's input.
     *
     * @param input Raw command entered by the user.
     * @return Command corresponding to the user's input.
     * @throws ParserException If the input is blank or starts with an unsupported command word.
     */
    public static Command parse(String input) {
        String fullCommand = input.trim();
        if (fullCommand.isEmpty()) {
            throw new ParserException("please fill in something");
        }

        String commandWord = fullCommand.split("\\s+", 2)[0];
        switch (commandWord) {
            case "todo":
            case "deadline":
            case "event":
                return new AddCommand(fullCommand);
            case "delete":
                return new DeleteCommand(fullCommand);
            case "find":
                return new FindCommand(fullCommand);
            case "list":
                return new ListCommand(fullCommand);
            case "mark":
                return new MarkCommand(fullCommand);
            case "unmark":
                return new UnmarkCommand(fullCommand);
            case "bye":
                if (fullCommand.equals("bye")) {
                    return new ExitCommand(fullCommand);
                }
                break;
            default:
                break;
        }
        throw new ParserException("I dont understand what you want me to do, please start with deadline, "
                + "todo, event, mark, unmark, delete, find, list or bye");
    }
}
