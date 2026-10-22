/**
 * Represents an exception specific to the Irri chatbot.
 * Thrown when user input is invalid or an operation cannot be completed.
 */
public class IrriException extends Exception{

    /**
     * Constructs an IrriException with the given error message.
     *
     * @param message The error message.
     */
    public IrriException(String message) {
        super(message);
    }

    /**
     * Constructs an IrriException with the given error message and cause.
     *
     * @param message The error message.
     * @param cause The underlying cause.
     */
    public IrriException (String message, Throwable cause) {
        super(message, cause);
    }
}
