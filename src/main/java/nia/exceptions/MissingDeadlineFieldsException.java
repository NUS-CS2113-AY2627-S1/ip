package nia.exceptions;

/** Thrown when a "deadline" command is missing its description and/or its "/by". */
public class MissingDeadlineFieldsException extends NiaParserException {
    /** Creates the exception with a fixed debug message and voiceline. */
    public MissingDeadlineFieldsException() {
        super(
                "[Debug] Deadline needs a description and a /by, e.g. \"deadline return book /by Sunday\"",
                "I need a description and a /by, or I've got nothing to remember.");
    }
}