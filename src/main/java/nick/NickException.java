package nick;

/**
 * Signals an error caused by invalid user input, such as an unknown command
 * or a command with a missing or malformed argument.
 * The message is meant to be shown directly to the user.
 */
public class NickException extends Exception {

    /**
     * Creates an exception with a message describing the input error.
     *
     * @param message The error message to show the user.
     */
    public NickException(String message) {
        super(message);
    }
}
