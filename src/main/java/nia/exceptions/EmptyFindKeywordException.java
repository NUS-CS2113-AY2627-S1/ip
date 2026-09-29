package nia.exceptions;

/** Thrown when a "find" command has no keyword to search for. */
public class EmptyFindKeywordException extends NiaParserException {
    /** Creates the exception with a fixed debug message and voiceline. */
    public EmptyFindKeywordException() {
        super(
                "[Debug] Find keyword is empty. Add one after the command word, e.g. \"find book\"",
                "Find what, exactly? You have to give me something to look for.");
    }
}
