package task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * Stores tasks and provides operations for managing them as one collection.
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
     * Creates a task list containing the supplied tasks.
     *
     * @param tasks Tasks to copy into the new list.
     */
    public TaskList(Collection<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Adds a task at the specified zero-based position.
     *
     * @param index Zero-based position at which to add the task.
     * @param task Task to add.
     * @throws IndexOutOfBoundsException If the index is outside the valid insertion range.
     */
    public void add(int index, Task task) {
        tasks.add(index, task);
    }

    /**
     * Returns the task at the specified zero-based position.
     *
     * @param index Zero-based position of the task.
     * @return Task at the specified position.
     * @throws IndexOutOfBoundsException If the index is outside the list.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at the specified zero-based position.
     *
     * @param index Zero-based position of the task to remove.
     * @return Removed task.
     * @throws IndexOutOfBoundsException If the index is outside the list.
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return Number of stored tasks.
     */
    public int size() {
        return tasks.size();
    }

    /** Returns an iterator that does not support modifying the task list. */
    /**
     * Returns an iterator that does not support modifying the task list.
     *
     * @return Read-only iterator over the tasks.
     */
    @Override
    public Iterator<Task> iterator() {
        return Collections.unmodifiableList(tasks).iterator();
    }
}
