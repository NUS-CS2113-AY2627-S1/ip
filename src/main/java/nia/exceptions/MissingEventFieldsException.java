package nia.exceptions;

/** Thrown when an "event" command is missing its description, "/from", and/or "/to". */
public class MissingEventFieldsException extends NiaParserException {
    /** Creates the exception with a fixed debug message and voiceline. */
    public MissingEventFieldsException() {
        super(
                "[Debug] Event needs a description, a /from, and a /to, e.g. \"event exam /from Mon /to Tue\"",
                "Description, /from, /to. All three, or don't bother.");
    }
}