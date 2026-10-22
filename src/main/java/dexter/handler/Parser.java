package dexter.handler;

import dexter.classifications.Type;
import dexter.exceptions.InvalidTaskException;
import dexter.exceptions.MissingDescriptionException;
import dexter.tasks.Deadline;
import dexter.tasks.Event;
import dexter.tasks.Task;

/** Converts task-creation commands into tasks without changing the task list. */
public class Parser {

    /**
     * Identifies a task command and extracts the details needed to create its task.
     *
     * @param line the complete command entered by the user
     * @return a new task, ready for the caller to add to the list
     * @throws InvalidTaskException if the command is blank or unrecognized
     * @throws MissingDescriptionException if required details are missing
     */
    public static Task parseTask(String line) throws MissingDescriptionException, InvalidTaskException {
        Type type = inputCommand(line);
        String description;
        switch (type) {
            case TODO:
                description = line.substring(5);
                return new Task(description);
            case DEADLINE:
                String[] parts = line.substring(9).split("\\s+/?by\\s+", 2);
                if (parts.length < 2 || parts[0].isBlank() || parts[1].isBlank()) {
                    throw new MissingDescriptionException();
                }
                return new Deadline(parts[0].trim(), parts[1].trim());
            case EVENT:
                if (!line.contains("from ") || !line.contains("to ")) {
                    throw new MissingDescriptionException();
                }
                int fromIndex = line.indexOf("from ");
                if (fromIndex == 6) {
                    throw new MissingDescriptionException();
                }
                description = line.substring(6, fromIndex - 1);
                fromIndex += 5;
                int toIndex = line.indexOf("to ");
                if (toIndex == fromIndex) {
                    throw new MissingDescriptionException();
                }
                String startDate = line.substring(fromIndex, toIndex - 1);
                toIndex += 3;
                if (line.substring(toIndex).isEmpty()) {
                    throw new MissingDescriptionException();
                }
                String endDate = line.substring(toIndex);
                return new Event(description, startDate, endDate);
            default:
                throw new InvalidTaskException();
        }
    }

    /**
     * Checks the command keyword and ensures it is followed by a description.
     *
     * @param line the complete command entered by the user
     * @return the type of task requested by the command
     * @throws MissingDescriptionException if the command has no description
     * @throws InvalidTaskException if the command is blank or unrecognized
     */
    private static Type inputCommand(String line) throws MissingDescriptionException, InvalidTaskException {
        if (line == null || line.isBlank()) {
            throw new InvalidTaskException();
        }
        String[] parts = line.split(" ");
        Type type;
        switch (parts[0]) {
            case "todo":
                type = Type.TODO;
                break;
            case "deadline":
                type = Type.DEADLINE;
                break;
            case "event":
                type = Type.EVENT;
                break;
            default:
                throw new InvalidTaskException();
        }
        if (parts.length < 2 || parts[1].isBlank()) {
            throw new MissingDescriptionException();
        }
        return type;
    }
}
