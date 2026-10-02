package glados;

/**
 * Represents an error specific to GLaDOS, such as a command the user entered
 * incorrectly.
 *
 * <p>The message carried by this exception is the one shown to the user, so it
 * should explain what went wrong.
 */
public class GLaDOSException extends Exception {

    /**
     * Creates an exception carrying the message to show the user.
     *
     * @param message explanation of what went wrong.
     */
    public GLaDOSException(String message) {
        super(message);
    }
}
