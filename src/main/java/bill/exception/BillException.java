package bill.exception;

/**
 * Represents a user input error that Bill can explain and recover from.
 */
public class BillException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Creates an exception containing a user-friendly explanation.
     *
     * @param message Explanation shown to the user.
     */
    public BillException(String message) {
        super(message);
    }
}
