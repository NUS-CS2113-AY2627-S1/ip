package mybff;

import java.util.ArrayList;

/** Stores tasks in insertion order using a dynamically sized collection. */
public class TaskList {
    private final ArrayList<Task> tasks = new ArrayList<>();

    /** Returns the number of tasks currently in the list. */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the task at a zero-based index.
     *
     * @param index the position of the task to retrieve
     * @return the task at the specified position
     * @throws IndexOutOfBoundsException if the index does not identify a stored task
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /** Returns tasks whose descriptions contain the case-sensitive keyword, in insertion order. */
    public TaskList find(String keyword) {
        TaskList matches = new TaskList();
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                matches.add(task);
            }
        }
        return matches;
    }

    /** Reports whether the zero-based index identifies a stored task. */
    public boolean isValidIndex(int index) {
        return index >= 0 && index < tasks.size();
    }

    /**
     * Removes and returns the task at a zero-based index, preserving the remaining order.
     *
     * @throws IndexOutOfBoundsException if the index does not identify a stored task
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /** Appends a task to the list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /** Inserts a task at a zero-based index, for restoring a failed deletion. */
    public void insert(int index, Task task) {
        tasks.add(index, task);
    }
}
