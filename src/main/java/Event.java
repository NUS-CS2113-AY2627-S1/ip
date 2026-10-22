/** Represents a task occurring between a start and end time. */
public class Event extends Task {
    private String dateStart;
    private String dateEnd;

    /**
     * Creates a new event task with the given description and date.
     *
     * @param body Description of the event task.
     * @param dateStart Start date of the event task.
     * @param dateEnd End date of the event task.
     */
    public Event(String body, String dateStart, String dateEnd) {
        super(body);
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + dateStart + " to: " + dateEnd + ")";
    }

    /** Parses an event in the form {@code <body> /from <start> /to <end>}. */
    public static Event fromString(String args) throws IllegalEventException {
        if (args == null || !args.contains("/from") || !args.contains("/to")) {
            throw new IllegalEventException("Format invalid, do <body> /from <dateStart> /to <dateEnd>");
        }
        String[] parts = args.split("/from|/to");
        String body = parts[0].trim();
        String dateStart = parts[1].trim();
        String dateEnd = parts[2].trim();
        if (body.isEmpty() || dateStart.isEmpty() || dateEnd.isEmpty()) {
            throw new IllegalEventException("Format invalid, do <body> /from <dateStart> /to <dateEnd>");
        }
        return new Event(body, dateStart, dateEnd);
    }

    @Override 
    public char getStatusIcon() {
        return 'E';
    }
}
