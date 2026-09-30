package twitchchat.tasks;

/** Represents a task without a deadline or event time. */
public class Todo extends Task {
    private static final String ICON = "T";

    /** Creates a todo task.
     *
     * @param taskName task description
     */
    public Todo(String taskName) {
        super(taskName);
    }

    @Override
    public String getIcon() {
        return ICON;
    }

    @Override
    public void printTask() {
        System.out.printf("[%s][%s] %s\n", getIcon(), this.getStatusIcon(), this.getTaskName());
    }
}
