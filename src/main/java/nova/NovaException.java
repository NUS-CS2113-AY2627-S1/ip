package nova;
/**
 * Signals that the user has entered invalid or malformed input, such as
 * a missing description, an unrecognized command, or an out-of-range task index.
 */

/**
 * Creates a NovaException with a message explaining what went wrong.
 */
public class NovaException extends Exception {
    public NovaException(String message) {
        super(message);
    }
}