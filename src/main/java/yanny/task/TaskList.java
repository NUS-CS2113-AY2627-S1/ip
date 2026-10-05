package yanny.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import yanny.exception.YannyException;

/**
 * Owns Yanny's ordered collection of tasks and validates task positions.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates a task list from previously loaded tasks.
     *
     * @param initialTasks the tasks to place in the list.
     */
    public TaskList(List<Task> initialTasks) {
        tasks = new ArrayList<>(initialTasks);
    }

    /**
     * Returns a snapshot of the tasks for display or saving.
     *
     * @return the tasks in list order.
     */
    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }

    /**
     * Finds task positions whose descriptions contain a keyword or phrase.
     *
     * @param keyword the case-insensitive text to search for.
     * @return zero-based positions in their original task-list order.
     */
    public List<Integer> findMatchingTaskIndices(String keyword) {
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        List<Integer> matchingIndices = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getDescription().toLowerCase(Locale.ROOT).contains(normalizedKeyword)) {
                matchingIndices.add(i);
            }
        }
        return List.copyOf(matchingIndices);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return the task count.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the task at a validated position.
     *
     * @param taskIndex the zero-based task position.
     * @param commandName the command used in validation messages.
     * @return the task at the requested position.
     * @throws YannyException if the list is empty or the position is out of range.
     */
    public Task getTask(int taskIndex, String commandName) throws YannyException {
        validateTaskIndex(taskIndex, commandName);
        return tasks.get(taskIndex);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task the task to add.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /** Removes the most recently added task from the list. */
    public void removeLastTask() {
        tasks.remove(tasks.size() - 1);
    }

    /**
     * Deletes and returns the task at a validated position.
     *
     * @param taskIndex the zero-based task position.
     * @param commandName the command used in validation messages.
     * @return the deleted task.
     * @throws YannyException if the list is empty or the position is out of range.
     */
    public Task deleteTask(int taskIndex, String commandName) throws YannyException {
        validateTaskIndex(taskIndex, commandName);
        return tasks.remove(taskIndex);
    }

    /**
     * Restores a deleted task at its original position.
     *
     * @param taskIndex the original zero-based task position.
     * @param task the task to restore.
     */
    public void restoreTask(int taskIndex, Task task) {
        tasks.add(taskIndex, task);
    }

    /**
     * Rejects a task position that does not refer to a stored task.
     *
     * @param taskIndex the zero-based task position.
     * @param commandName the command used in validation messages.
     * @throws YannyException if the list is empty or the position is out of range.
     */
    private void validateTaskIndex(int taskIndex, String commandName) throws YannyException {
        if (tasks.isEmpty()) {
            throw new YannyException("NO TASKS AVAILABLE. ADD A TASK BEFORE USING " + commandName + ".");
        }
        if (taskIndex >= tasks.size()) {
            throw new YannyException("TASK NUMBER OUT OF RANGE. USE A NUMBER FROM 1 TO " + tasks.size() + ".");
        }
    }
}
