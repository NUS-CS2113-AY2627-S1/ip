package yao;

/**
 * Signals an application-specific error occurring during Yao execution.
 */
public class YaoException extends Exception {

    /**
     * Constructs a YaoException with the specified error message.
     *
     * @param message Explanatory message for the error.
     */
    public YaoException(String message) {
        super(message);
    }
}
