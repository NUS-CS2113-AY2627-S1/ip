package hedy.exception;

/**
 * An error caused by a user command that Hedy can explain.
 * Checked so every command handler must either fix the problem or report it.
 */
public class HedyException extends Exception {

    /**
     * Creates an exception whose message is shown to the user.
     *
     * @param message a short explanation of what went wrong and how to fix it
     */
    public HedyException(String message) {
        super(message);
    }
}
