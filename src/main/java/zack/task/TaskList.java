package zack.task;

import java.util.ArrayList;

/** Manages the ordered collection of tasks. */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates a task list by copying the supplied list structure.
     * The task objects themselves are shared, not copied.
     *
     * @param initialTasks tasks to include, in their existing order
     */
    public TaskList(ArrayList<Task> initialTasks) {
        tasks = new ArrayList<>(initialTasks);
    }

    /**
     * Returns the number of tasks currently stored.
     *
     * @return task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the task at the specified position.
     *
     * @param index zero-based index in the task list
     * @return task at that index
     * @throws IndexOutOfBoundsException if the index is outside the list
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Appends a task to the end of the list.
     *
     * @param task task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes a task and shifts subsequent tasks to fill its position.
     *
     * @param index zero-based index of the task to remove
     * @return removed task
     * @throws IndexOutOfBoundsException if the index is outside the list
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns a copy of the list structure in its current order.
     * Changes to task objects remain visible through both lists.
     *
     * @return a new list containing references to the same task objects
     */
    public ArrayList<Task> snapshot() {
        return new ArrayList<>(tasks);
    }
}