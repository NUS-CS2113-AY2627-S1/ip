package nia.exceptions;

/** Thrown when a task index refers to a task that doesn't exist. */
public class InvalidTaskIndexException extends NiaParserException {
    /** Creates the exception for taskNumber, an out-of-range or nonexistent index into a list of size listSize. */
    public InvalidTaskIndexException(int taskNumber, int listSize) {
        super(
                String.format(
                        "[Debug] Task number %d is out of range (1-%d) or does not exist. Run \"list\" to see valid task numbers",
                        taskNumber, listSize),
                "That task doesn't exist... did you make it up?");
    }
}