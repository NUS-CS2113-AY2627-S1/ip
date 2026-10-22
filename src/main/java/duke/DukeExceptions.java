package duke;

/**
 * Represents an error specific to Duke, such as invalid or incomplete user commands.
 */
public class DukeExceptions extends Exception {
    /**
     * Creates a new DukeException with the given message.
     *
     * @param message What went wrong.
     */
    public DukeExceptions(String message) {
        super(message);
    }
}