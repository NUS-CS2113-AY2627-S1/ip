package cole;

/**
 * Signals an error caused by invalid user input, such as a missing description
 * or a badly formatted date. The message is meant to be shown to the user.
 */
public class ColeException extends Exception {

    /**
     * Creates an exception with a message that explains the problem to the user.
     *
     * @param message the error message to show
     */
    public ColeException(String message) {
        super(message);
    }

}
