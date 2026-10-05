package bigbrother.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import bigbrother.exception.BigBrotherException;

/**
 * Owns the tasks and provides operations that act on the task list.
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
     * Creates a task list containing tasks loaded from storage.
     *
     * @param loadedTasks tasks to copy into the list
     */
    public TaskList(List<Task> loadedTasks) {
        tasks = new ArrayList<>(loadedTasks);
    }

    /**
     * Returns a snapshot for display or storage without exposing the mutable list.
     *
     * @return the current tasks in list order
     */
    public List<Task> getTasks() {
        return List.copyOf(tasks);
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
     * Adds a task at the end of the list.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes a task by its one-based number displayed to the user.
     *
     * @param taskNumber the number shown by the list command
     * @return the removed task
     * @throws BigBrotherException if the number does not identify a task
     */
    public Task delete(int taskNumber) throws BigBrotherException {
        return tasks.remove(getTaskIndex(taskNumber));
    }

    /**
     * Marks a numbered task as completed.
     *
     * @param taskNumber the number shown by the list command
     * @return the updated task
     * @throws BigBrotherException if the number does not identify a task
     */
    public Task markAsDone(int taskNumber) throws BigBrotherException {
        Task task = tasks.get(getTaskIndex(taskNumber));
        task.markAsDone();
        return task;
    }

    /**
     * Marks a numbered task as incomplete.
     *
     * @param taskNumber the number shown by the list command
     * @return the updated task
     * @throws BigBrotherException if the number does not identify a task
     */
    public Task markAsUndone(int taskNumber) throws BigBrotherException {
        Task task = tasks.get(getTaskIndex(taskNumber));
        task.markAsUndone();
        return task;
    }

    /**
     * Finds tasks containing a keyword in their descriptions, ignoring case.
     *
     * @param keyword the text to search for
     * @return matching tasks in their original order
     */
    public List<Task> find(String keyword) {
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase(Locale.ROOT).contains(normalizedKeyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    /**
     * Converts a one-based task number to an index after checking its range.
     *
     * @param taskNumber the number shown by the list command
     * @return the corresponding zero-based index
     * @throws BigBrotherException if the number does not identify a task
     */
    private int getTaskIndex(int taskNumber) throws BigBrotherException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new BigBrotherException("Task " + taskNumber + " does not exist. Choose a number from the list.");
        }
        return taskNumber - 1;
    }
}
