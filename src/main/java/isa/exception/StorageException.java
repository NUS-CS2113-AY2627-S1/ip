package isa.exception;

/**
 * Represents a problem while reading or writing task data.
 */
public class StorageException extends Exception {
    /**
     * Creates a storage exception with the specified message.
     *
     * @param message Error message to display.
     */
    public StorageException(String message) {
        super(message);
    }

    /**
     * Creates a storage exception caused by another exception.
     *
     * @param message Error message to display.
     * @param cause Cause of the storage problem.
     */
    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
