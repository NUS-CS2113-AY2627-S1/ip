package eric.storage;

/**
 * Signals that the save file cannot be used, e.g. because it is not a file, cannot be read, or is
 * not in the expected format. The message explains why, in words that can be shown to the user.
 */
public class StorageException extends Exception {
    /**
     * Creates an exception with a reason that can be shown to the user.
     *
     * @param message Why the save file cannot be used, e.g. "it is a folder, not a file.".
     */
    public StorageException(String message) {
        super(message);
    }
}
