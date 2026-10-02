package error;

/** Signals that a required command argument is empty. */
public class EmptyArgumentException extends Exception {
    /** Creates an exception for a missing command argument. */
    public EmptyArgumentException() {
        super();
    }
}
