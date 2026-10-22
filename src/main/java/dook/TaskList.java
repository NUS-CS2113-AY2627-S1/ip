package dook;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents a collection of tasks and provides operations for managing them.
 */
public class TaskList implements Iterable<Task> {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Returns an iterator over the tasks in this list.
     *
     * @return an iterator over the tasks
     */
    @Override
    public Iterator<Task> iterator() {
        return tasks.iterator();
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return the number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Marks the specified task as done.
     *
     * @param taskNumber the one-based number of the task to mark
     * @return the task that was marked as done
     * @throws DookException if the task number is out of range
     */
    public Task markTask(int taskNumber) throws DookException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new DookException("That task number is out of range.");
        }
        // Convert the user's one-based task number to the list's zero-based index.
        Task task = tasks.get(taskNumber - 1);
        task.markAsDone();
        return task;
    }

    /**
     * Marks the specified task as not done.
     *
     * @param taskNumber the one-based number of the task to unmark
     * @return the task that was marked as not done
     * @throws DookException if the task number is out of range
     */
    public Task unmarkTask(int taskNumber) throws DookException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new DookException("That task number is out of range.");
        }
        // Convert the user's one-based task number to the list's zero-based index.
        Task task = tasks.get(taskNumber - 1);
        task.markAsNotDone();
        return task;
    }

    /**
     * Adds a task to the end of the task list.
     *
     * @param task the task to add
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Deletes the specified task from the task list.
     *
     * @param taskNumber the one-based number of the task to delete
     * @return the deleted task
     * @throws DookException if the task number is out of range
     */
    public Task deleteTask(int taskNumber) throws DookException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new DookException("That task number is out of range.");
        }
        return tasks.remove(taskNumber - 1);
    }

    /**
     * Finds all tasks whose descriptions contain the specified keyword.
     *
     * @param keyword the keyword to search for
     * @return a task list containing all matching tasks
     */
    public TaskList findTasks(String keyword) {
        TaskList matchingTasks = new TaskList();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                matchingTasks.addTask(task);
            }
        }
        return matchingTasks;
    }
}
