package kevie.exceptions;

import kevie.UserInterface;

/**
 * Exception thrown when the task number inputted is non-positive
 */
public class TaskNoNonPosException extends TaskNoException{
    /**
     * Creates a Task number non-positive exception.
     *
     * @param ui User interface to print messages in
     */
    public TaskNoNonPosException(UserInterface ui) {
        super("Task number inputted is non positive", ui);
    }
}
