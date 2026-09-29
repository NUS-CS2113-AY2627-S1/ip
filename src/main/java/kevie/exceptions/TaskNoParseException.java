package kevie.exceptions;

import kevie.UserInterface;

/**
 * Exception thrown when the task number inputted is not an integer
 */
public class TaskNoParseException extends TaskNoException{

    /**
     * Creates a Task number parsing exception.
     *
     * @param ui User interface to print messages in
     */
    public TaskNoParseException(UserInterface ui) {
        super("Task no. argument given not an integer", ui);
    }
}
