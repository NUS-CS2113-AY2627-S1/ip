package mimimeow.task;

import mimimeow.exception.MimiMeowException;

/** Represents an attempt to create a task with invalid task data. */
public class InvalidTaskException extends MimiMeowException {
    /**
     * Creates an exception with the specified validation message.
     *
     * @param message task validation error message
     */
    public InvalidTaskException(String message) {
        super(message);
    }
}
