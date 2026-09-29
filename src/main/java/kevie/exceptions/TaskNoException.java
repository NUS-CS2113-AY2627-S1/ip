package kevie.exceptions;

import kevie.Kevie;
import kevie.UserInterface;

/**
 * Exception thrown when the task number inputted is invalid
 */
public class TaskNoException extends KevieException {

    /**
     * Creates a Task number invalid exception.
     *
     * @param type Reason of why task number is invalid
     * @param ui User interface to print messages in
     */
    public TaskNoException(String type, UserInterface ui) {
        super("You inputted an invalid task number", type, ui);
    }

    @Override
    public void printMessage() {
        super.printMessage();
        ui.botSpeak("To see which number correspond to your task, do \"list\".", true);
    }
}
