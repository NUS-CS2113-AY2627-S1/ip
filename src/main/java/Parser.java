/** Interprets raw user commands and creates tasks from task commands. */
public class Parser {

    /** Splits a raw command into its keyword and remaining argument text. */
    public String[] split(String input) {
        return input.trim().split(" ", 2);
    }

    /** Converts the first token into a supported command type. */
    public CommandType getKeyword(String[] tokens) throws IllegalKeywordException {
        CommandType output;
        switch (tokens[0].toLowerCase())
                {   
                case "bye":
                    output = CommandType.BYE;
                    break;
                case "delete":
                    output = CommandType.DELETE;
                    break;
                case "find":
                    output = CommandType.FIND;
                    break;
                case "list":
                    output = CommandType.LIST;
                    break;
                case "mark":
                    output = CommandType.MARK;
                    break;
                case "unmark":
                    output = CommandType.UNMARK;
                    break;
                case "todo":
                    output = CommandType.TODO;
                    break;
                case "deadline":
                    output = CommandType.DEADLINE;
                    break;
                case "event":
                    output = CommandType.EVENT;
                    break;
                default:
                    throw new IllegalKeywordException();
                }                

        return output;
    }

    /** Converts a one-based user task number into a zero-based list index. */
    public int parseAsIndex(CommandType keyword, String[] tokens) {
        if (tokens.length < 2 || tokens[1].isBlank()) {
            throw new NumberFormatException("Missing task number");
        }
        int index = Integer.parseInt(tokens[1]) - 1;
        if (index < 0) {
            throw new NumberFormatException("Task numbers start at 1");
        }
        return index;
    }

    /** Creates a task from a task command. */
    /** Creates a task from the argument of a task-creation command. */
    public Task parseAsTask(CommandType keyword, String[] tokens) throws IllegalEventException {
        if (tokens.length < 2 || tokens[1].isBlank()) {
            throw switch (keyword) {
            case TODO -> new IllegalEventException("Usage: todo <description>");
            case DEADLINE -> new IllegalEventException(
                    "Usage: deadline <body> /by <date>");
            case EVENT -> new IllegalEventException(
                    "Usage: event <body> /from <start> /to <end>");
            default -> new IllegalEventException("Not a task command: " + keyword);
            };
        }
        return switch (keyword) {
        case TODO -> Todo.fromString(tokens[1]);
        case DEADLINE -> Deadline.fromString(tokens[1]);
        case EVENT -> Event.fromString(tokens[1]);
        default -> throw new IllegalEventException("Not a task command: " + keyword);
        };
    }
}
