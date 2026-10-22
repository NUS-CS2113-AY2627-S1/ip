package ace.task;

import ace.exception.AceException;
import ace.ui.Messages;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Owns Ace's task list and provides operations to manage its tasks.
 */
public class TaskManager {
    private final List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskManager() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied saved tasks.
     *
     * @param savedTasks tasks to include in the task list
     * @throws AceException if a supplied task is null or has an empty description
     */
    public TaskManager(List<Task> savedTasks) throws AceException {
        this();
        for (Task task : savedTasks) {
            addTask(task);
        }
    }

    /**
     * Returns the task at the specified zero-based index.
     *
     * @param taskNumber zero-based index of the task
     * @return task at the specified index
     * @throws AceException if the index is outside the task list
     */
    public Task getTask(int taskNumber) throws AceException {
        if (taskNumber < 0 || taskNumber >= tasks.size()) {
            throw new AceException(Messages.UNKNOWN_TASK_EXCEPTION);
        }
        return tasks.get(taskNumber);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return the task count
     */
    public int getTasksCount() {
        return tasks.size();
    }

    /**
     * Adds a nonempty task to the list.
     *
     * @param task task to add
     * @throws AceException if the task is null or has an empty description
     */
    public void addTask(Task task) throws AceException {
        if (task == null) {
            throw new AceException(Messages.INVALID_TASK_EXCEPTION);
        }
        if (task.isEmpty()) {
            throw new AceException(Messages.EMPTY_TASK_EXCEPTION);
        }
        tasks.add(task);
    }

    /**
     * Finds original task indices whose descriptions contain the keyword.
     * Matching ignores case and does not change the task list.
     *
     * @param keyword nonempty text to search for
     * @return zero-based indices of matching tasks in their original order
     */
    public List<Integer> findTaskIndices(String keyword) {
        String searchTerm = keyword.toLowerCase(Locale.ROOT);
        List<Integer> matches = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getLabel().toLowerCase(Locale.ROOT).contains(searchTerm)) {
                matches.add(i);
            }
        }
        return matches;
    }

    /**
     * Removes and returns the task at the specified zero-based index.
     *
     * @param taskNumber zero-based index of the task to remove
     * @return the removed task
     * @throws AceException if the index is outside the task list
     */
    public Task deleteTask(int taskNumber) throws AceException {
        Task task = getTask(taskNumber);
        tasks.remove(taskNumber);
        return task;
    }

    /**
     * Marks a task as completed.
     *
     * @param taskNumber zero-based index of the task
     * @throws AceException if the index is outside the task list
     */
    public void markAsDone(int taskNumber) throws AceException {
        getTask(taskNumber).markAsDone();
    }

    /**
     * Marks a task as not completed.
     *
     * @param taskNumber zero-based index of the task
     * @throws AceException if the index is outside the task list
     */
    public void markAsUndone(int taskNumber) throws AceException {
        getTask(taskNumber).markAsUndone();
    }
}
