package chatbot;

import chatbot.exception.AgentCTException;
import chatbot.task.Deadline;
import chatbot.task.Event;
import chatbot.task.Todo;

/** Interprets user input and constructs executable chatbot commands. */
public class Parser {
    /** Parses a user command into an executable command.
     *
     * @param command raw user command
     * @return executable command
     * @throws AgentCTException when the command is unsupported or incomplete
     */
    public Command parseCommand(String command) throws AgentCTException {
        if (command.equals("bye")) {
            return new ExitCommand();
        }
        if (command.equals("list")) {
            return new ListCommand();
        }
        if (command.startsWith("find ")) {
            String keyword = command.substring(5).trim();
            if (keyword.isEmpty()) {
                throw new AgentCTException("Please provide a keyword to search for!\n"
                        + "Format: find <keyword>");
            }
            return new FindCommand(keyword);
        }
        if (command.matches("mark \\d+")) {
            return new MarkCommand(Integer.parseInt(command.substring(5)));
        }
        if (command.matches("unmark \\d+")) {
            return new UnmarkCommand(Integer.parseInt(command.substring(7)));
        }
        if (command.matches("delete \\d+")) {
            return new DeleteCommand(Integer.parseInt(command.substring(7)));
        }
        if (command.equals("todo") || command.startsWith("todo ")) {
            String description = command.substring(4).trim();
            if (description.isEmpty()) {
                throw new AgentCTException(missingDescriptionMessage());
            }
            return new AddCommand(new Todo(description));
        }
        if (command.startsWith("deadline ") && command.contains(" /by ")) {
            int markerIndex = command.indexOf(" /by ");
            String description = command.substring(9, markerIndex).trim();
            if (description.isEmpty()) {
                throw new AgentCTException(missingDescriptionMessage());
            }
            return new AddCommand(new Deadline(description,
                    command.substring(markerIndex + 5).trim()));
        }
        if (command.startsWith("event ") && command.contains(" /from ")
                && command.contains(" /to ")) {
            int fromIndex = command.indexOf(" /from ");
            int toIndex = command.indexOf(" /to ", fromIndex);
            String description = command.substring(6, fromIndex).trim();
            if (description.isEmpty()) {
                throw new AgentCTException(missingDescriptionMessage());
            }
            return new AddCommand(new Event(description,
                    command.substring(fromIndex + 7, toIndex).trim(),
                    command.substring(toIndex + 5).trim()));
        }
        throw new AgentCTException(invalidCommandMessage());
    }

    /** Builds the error shown when a task description is missing.
     *
     * @return missing-description error message
     */
    private String missingDescriptionMessage() {
        return "Please provide a task description!\n"
                + "Format: todo <description>\n"
                + "Example: todo Play Video Games";
    }

    /** Builds the error shown for an unsupported command.
     *
     * @return invalid-command error message
     */
    private String invalidCommandMessage() {
        return "Please enter a valid command!\n"
                + "Examples:\n"
                + "  todo <description>\n"
                + "  deadline <description> /by <time>\n"
                + "  event <description> /from <time> /to <time>\n"
                + "  list\n"
                + "  find <keyword>\n"
                + "  mark <number>\n"
                + "  unmark <number>\n"
                + "  delete <number>";
    }
}
