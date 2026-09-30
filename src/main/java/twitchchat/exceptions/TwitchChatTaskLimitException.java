package twitchchat.exceptions;

/**
 * Represents an attempt to add a task after reaching the task-list limit.
 */
public class TwitchChatTaskLimitException extends TwitchChatCommandException {

    /**
     * Creates a task-limit exception.
     *
     * @param maximumTasks maximum number of tasks supported
     */
    public TwitchChatTaskLimitException(int maximumTasks) {
        super("Task list is full. The maximum is " + maximumTasks + " tasks.");
    }
}
