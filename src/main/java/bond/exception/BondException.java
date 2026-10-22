package bond.exception;

/**
 * Represents a recoverable error that can be explained to the user.
 */
public class BondException extends Exception {

    private final String correction;

    /**
     * Creates an exception with the error cause and its correction.
     *
     * @param message Explanation of the error.
     * @param correction Instruction for correcting the error.
     */
    public BondException(String message, String correction) {
        super(message);
        this.correction = correction;
    }

    /**
     * Creates an exception caused by another failure.
     *
     * @param message Explanation of the error.
     * @param correction Instruction for correcting the error.
     * @param cause Underlying failure that caused this error.
     */
    public BondException(String message, String correction, Throwable cause) {
        super(message, cause);
        this.correction = correction;
    }

    /**
     * Returns the instruction for correcting the error.
     *
     * @return Instruction for correcting the error.
     */
    public String getCorrection() {
        return correction;
    }
}
