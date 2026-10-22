package aigis;

import java.io.Serial;

/**
 * Represents a recoverable error encountered while processing a user command.
 */
public class AigisException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates an exception with the message shown to the user.
     *
     * @param message The user-facing error message.
     */
    public AigisException(String message) {
        super(message);
    }
}
