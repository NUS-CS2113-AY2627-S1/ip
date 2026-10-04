package dobby.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dobby.exception.DobbyException;

/**
 * Stores the tasks that Dobby manages during a session.
 */
public class TaskList {

    /** The maximum number of tasks that can be stored in one session. */
    public static final int MAX_TASKS = 100;

    /** The tasks currently stored by Dobby. */
    private final List<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing a defensive copy of the supplied tasks.
     *
     * @param initialTasks the tasks to place in the new list.
     */
    public TaskList(List<Task> initialTasks) {
        tasks = new ArrayList<>(initialTasks);
    }

    /**
     * Returns the number of tasks currently stored.
     *
     * @return the number of stored tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the task at the supplied zero-based index.
     *
     * @param index the zero-based position of the requested task.
     * @return the task at the requested position.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns an unmodifiable view of the stored tasks.
     *
     * @return the tasks currently stored.
     */
    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Adds a task when the list has not reached its capacity.
     *
     * @param task the task to add.
     * @throws DobbyException when the task list is full.
     */
    public void add(Task task) throws DobbyException {
        if (tasks.size() >= MAX_TASKS) {
            throw new DobbyException("Your task pouch is full at 100 quests. Start a new Dobby session before "
                    + "adding another quest.");
        }

        tasks.add(task);
    }

    /**
     * Deletes and returns the task at the supplied zero-based index.
     *
     * @param index the zero-based position of the task to delete.
     * @return the deleted task.
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }
}
