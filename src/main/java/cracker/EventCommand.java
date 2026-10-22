package cracker;

/**
 * Represents a command that adds an event task.
 */
public class EventCommand extends AddCommand {
    /**
     * Creates a command that adds an event task.
     *
     * @param description event description
     * @param from event start time
     * @param to event end time
     */
    public EventCommand(String description, String from, String to) {
        super(new Event(description, from, to));
    }
}
