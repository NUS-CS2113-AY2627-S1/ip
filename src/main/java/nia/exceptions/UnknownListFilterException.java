package nia.exceptions;

/** Thrown when "list" is given a filter token that isn't a recognized todo/deadline/event filter. */
public class UnknownListFilterException extends NiaParserException {
    /** Creates the exception for filterToken, the unrecognized list filter that was given. */
    public UnknownListFilterException(String filterToken) {
        super(
                String.format(
                        "[Debug] Unrecognized list filter \"%s\". Valid filters: t/todo, d/dl/deadline, e/event",
                        filterToken),
                "Filter by what now? I only know t/todo, d/dl/deadline, and e/event.");
    }
}
