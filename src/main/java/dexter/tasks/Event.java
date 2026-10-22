package dexter.tasks;

/** A task that occurs between a specified start date and end date. */
public class Event extends Task {
    protected String startDate;
    protected String endDate;

    /** Creates an event with a description, start date, and end date. */
    public Event(String description, String startDate, String endDate) {
        super(description);
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = "E";
    }

    /**
     * Returns the event's start date text.
     *
     * @return the event start date
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Returns the event's end date text.
     *
     * @return the event end date
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Formats this event for display in the user interface.
     *
     * @return the event details in display format
     */
    @Override
    public String toString() {
        return super.toString() + " (from: " + startDate + " to: " + endDate + ")";
    }
}
