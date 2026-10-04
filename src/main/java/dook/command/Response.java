package dook.command;

import dook.task.Task;

/**
 * Represents the output answering the user input, to be printed on screen.
 *
 * @param  message The main message.
 * @param  task    The task attached.
 */
public record Response(String message, Task task) {

    /**
     * Constructs a new Response without a task attached.
     *
     * @param  message The main message.
     */
    public Response(String message) {
        this(message, null);
    }

    /**
     * Returns whether a task is attached in this response.
     *
     * @return Whether this response has an attachment.
     */
    public boolean hasTask() {
        return (task != null);
    }
}
