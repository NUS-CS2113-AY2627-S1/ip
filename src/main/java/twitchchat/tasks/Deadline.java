package twitchchat.tasks;

/** Represents a task that must be completed by a specified time. */
public class Deadline extends Task {

    private final String endTime;
    private static final String ICON = "D";

    /** Creates a deadline task.
     *
     * @param taskName task description
     * @param endTime task deadline
     */
    public Deadline(String taskName, String endTime) {
        super(taskName);
        this.endTime = endTime;
    }

    /** Returns the icon representing a deadline task.
     *
     * @return deadline task icon
     */
    public String getIcon() {
        return ICON;
    }

    /**
     * Returns the deadline details for this task.
     *
     * @return deadline details
     */
    public String getEndTime() {
        return endTime;
    }

    @Override
    /** Prints this deadline in the user-facing task-list format. */
    public void printTask() {
        String taskDetails = this.getTaskName() + " (by: " + endTime + ")";
        System.out.printf("[%s][%s] %s\n", getIcon(), this.getStatusIcon(), taskDetails);
    }
}
