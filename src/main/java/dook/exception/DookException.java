package dook.exception;

/**
 * Represents the template for custom exceptions.
 */
public abstract class DookException extends RuntimeException {
    protected DookException(String message) {
        super(message);
    }
}
