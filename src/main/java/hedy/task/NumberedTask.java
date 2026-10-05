package hedy.task;

/**
 * A task together with the number {@code list} shows for it.
 * That number is what {@code mark} and {@code delete} expect.
 */
public class NumberedTask {
    private final int number;
    private final Task task;

    /**
     * Pairs a list number with a task.
     *
     * @param number number shown by {@code list}, starting at 1
     * @param task the task at that position
     */
    public NumberedTask(int number, Task task) {
        this.number = number;
        this.task = task;
    }

    /** Returns the number shown by {@code list}. */
    public int getNumber() {
        return number;
    }

    /** Returns the task. */
    public Task getTask() {
        return task;
    }
}
