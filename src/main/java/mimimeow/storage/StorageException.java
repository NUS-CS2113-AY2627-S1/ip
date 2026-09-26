package mimimeow.storage;

import mimimeow.exception.MimiMeowException;

/** Represents a failure to load or save task data. */
public class StorageException extends MimiMeowException {
    /**
     * Creates an exception with the specified user-facing message.
     *
     * @param message user-facing error message
     */
    public StorageException(String message) {
        super(message);
    }

    /**
     * Creates an exception with a user-facing message and its underlying cause.
     *
     * @param message user-facing error message
     * @param cause underlying cause
     */
    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
