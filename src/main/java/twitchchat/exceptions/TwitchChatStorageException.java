package twitchchat.exceptions;

/**
 * Represents an error while reading or writing TwitchChat data on disk.
 */
public class TwitchChatStorageException extends RuntimeException {

    /**
     * Creates a storage exception with the specified cause.
     *
     * @param cause underlying file-system error
     */
    public TwitchChatStorageException(Exception cause) {
        this("Unable to access tasks on disk", cause);
    }

    /**
     * Creates a storage exception with a specific operation message.
     *
     * @param message description of the failed storage operation
     * @param cause underlying file-system error
     */
    public TwitchChatStorageException(String message, Exception cause) {
        super(message, cause);
    }
}
