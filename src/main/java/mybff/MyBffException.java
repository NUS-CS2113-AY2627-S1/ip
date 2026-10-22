package mybff;

/** Represents an invalid command or task description entered into MyBff. */
public class MyBffException extends Exception {
    /**
     * Creates an exception with an explanation to display to the user.
     *
     * @param message the explanation of the input error
     */
    public MyBffException(String message) {
        super(message);
    }
}
