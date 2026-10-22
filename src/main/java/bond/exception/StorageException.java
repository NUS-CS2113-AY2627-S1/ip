package bond.exception;

/**
 * Represents a recoverable failure while loading or saving task data.
 */
public class StorageException extends BondException {

    /**
     * Creates a storage exception with an error message and recovery instruction.
     *
     * @param message Explanation of the storage failure.
     * @param correction Instruction for recovering from the failure.
     */
    public StorageException(String message, String correction) {
        super(message, correction);
    }

    /**
     * Creates a storage exception caused by another failure.
     *
     * @param message Explanation of the storage failure.
     * @param correction Instruction for recovering from the failure.
     * @param cause Underlying failure that prevented storage access.
     */
    public StorageException(String message, String correction, Throwable cause) {
        super(message, correction, cause);
    }
}
