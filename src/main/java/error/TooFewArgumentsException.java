package error;

/** Signals that a mark or unmark command has a non-numeric task number. */
public class TooFewArgumentsException extends Exception {
    /** Creates an exception for a command missing required arguments. */
    public TooFewArgumentsException() {
        super();
    }
}
