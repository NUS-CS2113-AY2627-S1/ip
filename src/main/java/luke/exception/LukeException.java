package luke.exception;

/**
 * Represents errors caused by invalid user commands.
 */
public class LukeException extends Exception {

    /**
     * Creates a Luke-specific exception with the given message.
     *
     * @param message Explanation of the error.
     */
    public LukeException(String message) {
        super(message);
    }
}
