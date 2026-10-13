package socrates;

/**
 * Signals a user-facing error (bad command usage, invalid input, etc.)
 * that should be shown to the user rather than crashing the program.
 */
public class SocratesException extends Exception {
    public SocratesException(String message) {
        super(message);
    }
}
