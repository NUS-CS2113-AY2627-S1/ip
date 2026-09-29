package kevie.exceptions;

import kevie.UserInterface;

/**
 * Exception thrown when the task number inputted exceeds the total number of tasks in todo list
 */
public class TaskNoExceedException extends TaskNoException {
    /**
     * Creates a Task number exceed task list length error.
     *
     * @param picked the task number inputted
     * @param max The lenght of the task list
     * @param ui User interface to print messages in
     */
    public TaskNoExceedException(int picked, int max, UserInterface ui) {
        super("List only have " + max + " tasks but task no." + picked + " is picked", ui);
    }
}
