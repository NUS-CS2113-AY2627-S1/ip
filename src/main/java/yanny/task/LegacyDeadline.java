package yanny.task;

/**
 * Preserves a free-text deadline loaded from an older task file.
 */
public class LegacyDeadline extends Task {
    private final String deadlineText;

    /**
     * Creates a deadline from an older, unstructured stored value.
     *
     * @param description the text describing the task.
     * @param deadlineText the original free-text deadline.
     */
    public LegacyDeadline(String description, String deadlineText) {
        super(description);
        this.deadlineText = deadlineText;
    }

    /**
     * Returns the deadline icon for an older free-text task.
     *
     * @return the deadline task type icon.
     */
    @Override
    public String getTypeIcon() {
        return "D";
    }

    /**
     * Returns the original deadline value for storage.
     *
     * @return the unstructured deadline text.
     */
    public String getDeadlineText() {
        return deadlineText;
    }

    /**
     * Returns the deadline task using its original free-text value.
     *
     * @return the formatted legacy deadline task.
     */
    @Override
    public String toString() {
        return super.toString() + " (by: " + deadlineText + ")";
    }
}
