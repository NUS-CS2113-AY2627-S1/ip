package twitchchat.tasks;

/** Represents a task that occurs during a specified time interval. */
public class Event extends Task {
    private final String startTime;
    private final String endTime;
    private static final String ICON = "E";

    /** Creates an event task.
     *
     * @param taskName task description
     * @param startTime event start time
     * @param endTime event end time
     */
    public Event(String taskName, String startTime, String endTime) {
        super(taskName);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    /** Returns the icon representing an event task.
     *
     * @return event task icon
     */
    public String getIcon() {
        return ICON;
    }

    /**
     * Returns the event start time.
     *
     * @return event start time
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Returns the event end time.
     *
     * @return event end time
     */
    public String getEndTime() {
        return endTime;
    }

    @Override
    /** Prints this event in the user-facing task-list format. */
    public void printTask() {
        String taskDetails = this.getTaskName() + " (from: " + startTime + " to: " + endTime + ")";
        System.out.printf("[%s][%s] %s\n", getIcon(), this.getStatusIcon(), taskDetails);
    }
}
