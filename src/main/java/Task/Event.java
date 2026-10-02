package Task;

/** A task that takes place between a start and an end date or time. */
public class Event extends ToDo {
    private final String from;
    private final String to;

    /** Creates an unfinished event with its start and end stored as text. */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /** Returns the event start text. */
    public String getFrom() {
        return this.from;
    }

    /** Returns the event end text. */
    public String getTo() {
        return this.to;
    }

    /** Returns the type marker used for displaying and saving an event. */
    @Override
    public String getTaskIcon() {
        return "E";
    }

    /** Prints the task details followed by its start and end times. */
    @Override
    public void printResponse() {
        System.out.printf("  [%s][%s]%s (from: %s to: %s)\n", this.getTaskIcon(), this.getStatusIcon(),
                this.description, this.from, this.to);
    }
}
