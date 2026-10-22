package dawn.exception;

/**
 * Reports a damaged saved record that can be skipped while loading other tasks.
 */
public class MalformedRecordException extends Exception {
    /**
     * Constructs a record error with the reason the saved line is invalid.
     */
    public MalformedRecordException(String message) {
        super(message);
    }
}
