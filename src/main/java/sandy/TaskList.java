package sandy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import sandy.exception.SandyException;
import sandy.task.Task;

/**
 * Owns and manages the tasks in Sandy's task list.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied tasks.
     *
     * @param initialTasks The tasks to copy into the list.
     */
    public TaskList(List<Task> initialTasks) {
        tasks = new ArrayList<>(initialTasks);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task The task to add.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at the given one-based position.
     *
     * @param taskNumber The one-based task number.
     * @return The task at that position.
     * @throws SandyException If the task number is outside the list.
     */
    public Task getTask(int taskNumber) throws SandyException {
        int taskIndex = taskNumber - 1;
        validateTaskIndex(taskIndex);
        return tasks.get(taskIndex);
    }

    /**
     * Removes and returns the task at the given one-based position.
     *
     * @param taskNumber The one-based task number.
     * @return The removed task.
     * @throws SandyException If the task number is outside the list.
     */
    public Task deleteTask(int taskNumber) throws SandyException {
        int taskIndex = taskNumber - 1;
        validateTaskIndex(taskIndex);
        return tasks.remove(taskIndex);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return The number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns an unmodifiable view of the tasks.
     *
     * @return The tasks in their current order.
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Returns tasks whose descriptions contain the given keyword, ignoring case.
     *
     * @param keyword The keyword to search for.
     * @return Matching tasks in their original order.
     */
    public List<Task> findTasks(String keyword) {
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase(Locale.ROOT).contains(normalizedKeyword)) {
                matchingTasks.add(task);
            }
        }
        return Collections.unmodifiableList(matchingTasks);
    }

    /**
     * Checks that a zero-based task index refers to an existing task.
     *
     * @param taskIndex The zero-based task index to check.
     * @throws SandyException If the index is outside the list.
     */
    private void validateTaskIndex(int taskIndex) throws SandyException {
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new SandyException("That task number does not exist.");
        }
    }
}
