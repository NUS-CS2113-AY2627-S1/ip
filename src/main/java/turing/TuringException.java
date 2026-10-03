package turing;

/**
 * Signals a problem the user can put right by typing a different command, such
 * as a missing task description or a task number that does not exist.
 * Errors of this kind are part of a normal conversation rather than faults in
 * the chatbot, so they are reported to the user instead of ending the program.
 * The exception is checked so that the compiler points out any command that
 * forgets to deal with it.
 */
public class TuringException extends Exception {
    /**
     * Lines to show the user: what went wrong, and usually an example of the
     * command written correctly. They are kept separate rather than joined into
     * one long line so that a reply can show them one below the other.
     */
    private final String[] messageLines;

    /**
     * Creates an exception carrying an explanation for the user.
     *
     * @param messageLines What went wrong, and how to correct it.
     */
    public TuringException(String... messageLines) {
        super(String.join(" ", messageLines));
        this.messageLines = messageLines;
    }

    /**
     * Returns the explanation as separate lines, ready to be shown to the user.
     *
     * @return One line per sentence of the explanation.
     */
    public String[] getMessageLines() {
        return messageLines;
    }
}
