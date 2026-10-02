package error;

/** Signals that a mark or unmark command refers to a task outside the list. */
public class InvalidRangeException extends Exception {
    /** Creates an exception for a task number outside the list. */
    public InvalidRangeException() {
        super();
    }
}
