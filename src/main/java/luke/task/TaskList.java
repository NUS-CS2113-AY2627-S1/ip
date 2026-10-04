package luke.task;

import java.util.ArrayList;

import luke.exception.LukeException;

/**
 * Stores and manages the user's tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Adds a task to the end of the task list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Deletes the task at the specified one-based position.
     *
     * @param taskNumber One-based position of the task to delete.
     * @return Deleted task.
     * @throws LukeException If the task number does not identify a stored task.
     */
    public Task delete(int taskNumber) throws LukeException {
        validateTaskNumber(taskNumber);

        return tasks.remove(taskNumber - 1);
    }

    /**
     * Marks the task at the specified one-based position as completed.
     *
     * @param taskNumber One-based position of the task to mark.
     * @return Marked task.
     * @throws LukeException If the task number does not identify a stored task.
     */
    public Task mark(int taskNumber) throws LukeException {
        validateTaskNumber(taskNumber);

        Task task = tasks.get(taskNumber - 1);
        task.markAsDone();
        return task;
    }

    /**
     * Marks the task at the specified one-based position as incomplete.
     *
     * @param taskNumber One-based position of the task to unmark.
     * @return Unmarked task.
     * @throws LukeException If the task number does not identify a stored task.
     */
    public Task unmark(int taskNumber) throws LukeException {
        validateTaskNumber(taskNumber);

        Task task = tasks.get(taskNumber - 1);
        task.markAsNotDone();
        return task;
    }

    /**
     * Returns tasks whose descriptions contain the specified keyword.
     * Matching is case-insensitive and preserves task order.
     *
     * @param keyword Keyword to find in task descriptions.
     * @return Matching tasks in their original order.
     */
    public Task[] find(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();

        for (Task task : tasks) {
            if (task.containsKeyword(keyword)) {
                matchingTasks.add(task);
            }
        }

        return matchingTasks.toArray(new Task[0]);
    }

    /**
     * Validates that a one-based task number identifies a stored task.
     *
     * @param taskNumber One-based task number to validate.
     * @throws LukeException If the task list is empty or the number is out of range.
     */
    private void validateTaskNumber(int taskNumber) throws LukeException {
        if (tasks.isEmpty()) {
            throw new LukeException("There are no tasks in the list.");
        }

        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new LukeException(
                    "That task number does not exist. Please choose a number between 1 and "
                            + tasks.size() + ".");
        }
    }

    /**
     * Returns a snapshot of the stored tasks.
     *
     * @return Tasks in their current order.
     */
    public Task[] getTasks() {
        return tasks.toArray(new Task[0]);
    }

    /**
     * Returns the number of stored tasks.
     *
     * @return Number of stored tasks.
     */
    public int getTaskCount() {
        return tasks.size();
    }

}
