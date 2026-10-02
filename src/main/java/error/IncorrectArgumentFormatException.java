package error;

/** Signals that command arguments do not begin in the required format. */
public class IncorrectArgumentFormatException extends Exception {
    /** Creates an exception for an invalid command format. */
    public IncorrectArgumentFormatException() {
        super();
    }
}
