package eric.exception;

/**
 * Signals that a command typed by the user could not be carried out, for example because it could
 * not be understood or refers to a task that does not exist. It says what is wrong, in the
 * message, and how the user can correct it, so both can be shown to the user.
 */
public class EricException extends Exception {
    private final String fix;

    /**
     * Creates an exception that explains why a command failed and how to fix it.
     *
     * @param problem Why the command could not be carried out.
     * @param fix What the user should do or type instead.
     */
    public EricException(String problem, String fix) {
        super(problem);
        this.fix = fix;
    }

    /** Returns what the user should do or type instead. */
    public String getFix() {
        return fix;
    }
}
