package dawn.exception;

/**
 * Identifies storage failures so Dawn can avoid an inaccurate saving reassurance.
 */
public class StorageException extends DawnException {
    /**
     * Constructs a storage error with an explanation suitable for the console.
     */
    public StorageException(String message) {
        super(message);
    }
}
