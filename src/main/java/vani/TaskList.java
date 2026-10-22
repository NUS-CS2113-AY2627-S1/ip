package vani;

import java.util.ArrayList;
import java.util.List;

import vani.exception.VaniException;
import vani.task.Task;

/**
 * Owns the tasks and provides operations using the task numbers shown to the user.
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
     * Creates a task list containing the loaded tasks in their original order.
     * The supplied list is copied so that other classes cannot change its structure.
     *
     * @param loadedTasks the tasks loaded from storage
     */
    public TaskList(List<Task> loadedTasks) {
        tasks = new ArrayList<>(loadedTasks);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task the task to add
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Finds tasks whose descriptions contain the keyword, preserving their order.
     * Matching is case-sensitive and leaves the original task list unchanged.
     *
     * @param keyword the text to search for in task descriptions
     * @return the matching tasks
     */
    public List<Task> findTasks(String keyword) {
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    /**
     * Marks a task as completed.
     *
     * @param taskNumber the task's number, starting from 1
     * @return the task that was marked
     * @throws VaniException if the task number is outside the list
     */
    public Task markTask(int taskNumber) throws VaniException {
        validateTaskNumber(taskNumber);
        Task task = tasks.get(taskNumber - 1);
        task.markAsDone();
        return task;
    }

    /**
     * Marks a task as incomplete.
     *
     * @param taskNumber the task's number, starting from 1
     * @return the task that was unmarked
     * @throws VaniException if the task number is outside the list
     */
    public Task unmarkTask(int taskNumber) throws VaniException {
        validateTaskNumber(taskNumber);
        Task task = tasks.get(taskNumber - 1);
        task.markAsNotDone();
        return task;
    }

    /**
     * Removes a task and shifts subsequent task numbers down by one.
     *
     * @param taskNumber the task's number, starting from 1
     * @return the removed task
     * @throws VaniException if the task number is outside the list
     */
    public Task deleteTask(int taskNumber) throws VaniException {
        validateTaskNumber(taskNumber);
        return tasks.remove(taskNumber - 1);
    }

    /**
     * Returns whether the list has no tasks.
     *
     * @return true if the list is empty
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return the task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns a read-only copy of the list in its current order.
     * The task objects themselves are not copied.
     *
     * @return the tasks for display or saving
     */
    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }

    /**
     * Checks a user-facing task number before converting it to a list index.
     *
     * @param taskNumber the task's number, starting from 1
     * @throws VaniException if the task number is outside the list
     */
    private void validateTaskNumber(int taskNumber) throws VaniException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new VaniException("Invalid task number. >:[");
        }
    }
}
