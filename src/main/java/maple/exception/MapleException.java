package maple.exception;

/**
 * Represents an error caused by an invalid Maple command.
 */
public class MapleException extends Exception {
    /** Serialization version identifier. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a Maple-specific exception with the given message.
     *
     * @param message the message that explains the error.
     */
    public MapleException(String message) {
        super(message);
    }
}
