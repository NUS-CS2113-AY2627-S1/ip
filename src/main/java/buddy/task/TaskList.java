package buddy.task;

import java.util.ArrayList;
import java.util.List;

import buddy.exception.BuddyException;

/**
 * Stores and manages the tasks remembered by Buddy.
 *
 * <p>Tasks are kept in an {@link ArrayList} rather than a fixed-size array, so the
 * list can grow to hold any number of tasks and shrink again when a task is deleted.
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
     * Adds a task to the list.
     *
     * @param task task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the given one-based task number.
     *
     * @param taskNumber one-based task number
     * @return the task that was removed
     * @throws BuddyException if there is no task with that number
     */
    public Task remove(int taskNumber) throws BuddyException {
        checkTaskNumber(taskNumber);
        return tasks.remove(taskNumber - 1);
    }

    /**
     * Returns the task at the given one-based task number.
     *
     * @param taskNumber one-based task number
     * @return matching task
     * @throws BuddyException if there is no task with that number
     */
    public Task getTask(int taskNumber) throws BuddyException {
        checkTaskNumber(taskNumber);
        return tasks.get(taskNumber - 1);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return task count
     */
    public int size() {
        return tasks.size();
    }

    private void checkTaskNumber(int taskNumber) throws BuddyException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new BuddyException("There is no task number " + taskNumber
                    + ". You have " + tasks.size() + " task(s).");
        }
    }

    /**
     * Returns a snapshot of all tasks currently in the list, in order. Used
     * when saving the list to disk.
     *
     * @return a new list containing all tasks
     */
    public List<Task> getAll() {
        return new ArrayList<>(tasks);
    }

    /**
     * Returns every task whose description contains the given keyword, in order.
     * The match is case-insensitive.
     *
     * @param keyword text to search for within each task's description
     * @return matching tasks, or an empty list if none match
     */
    public List<Task> find(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        List<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(lowerKeyword)) {
                matches.add(task);
            }
        }
        return matches;
    }
}
