package nia.exceptions;

/** Thrown when a "todo" command has no description. */
public class EmptyTodoDescriptionException extends NiaParserException {
    /** Creates the exception with a fixed debug message and voiceline. */
    public EmptyTodoDescriptionException() {
        super(
                "[Debug] Todo description is empty. Add one after the command word, e.g. \"todo buy milk\"",
                "A todo with nothing in it? There's nothing for me to do.");
    }
}