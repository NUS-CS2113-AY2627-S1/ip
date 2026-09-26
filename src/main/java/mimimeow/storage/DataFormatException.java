package mimimeow.storage;

/** Represents malformed data in the saved task file. */
public class DataFormatException extends StorageException {
    /**
     * Creates an exception with the specified user-facing message.
     *
     * @param message user-facing error message
     */
    public DataFormatException(String message) {
        super(message);
    }
}
