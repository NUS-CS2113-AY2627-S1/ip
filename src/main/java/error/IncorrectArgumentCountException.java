package error;

/** Signals that a command has a different number of arguments than required. */
public class IncorrectArgumentCountException extends Exception {
    /** Creates an exception for an incorrect argument count. */
    public IncorrectArgumentCountException() {
        super();
    }
}
