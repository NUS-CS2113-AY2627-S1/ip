/**
 * Represents an error caused by an invalid YODA command or task value.
 */
public class YodaException extends Exception {
    /**
     * Creates an exception with an explanation for the user.
     *
     * @param message the explanation of the error
     */
    public YodaException(String message) {
        super(message);
    }
}
