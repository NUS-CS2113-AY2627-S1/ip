package usagi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds the user's tasks, in the order they were added, and provides the
 * operations Usagi's commands need on them.
 *
 * Tasks are identified by their index (starting from 0). Every method that
 * takes an index checks it first, so that a bad task number typed by the user
 * is reported as a {@link UsagiException} rather than crashing the program.
 */
public class TaskList {
    /**
     * Tasks in the list.
     *
     * An {@code ArrayList} is used rather than a fixed-size array so that the
     * list can grow as needed and so that removing a task automatically shifts
     * the tasks after it, which is what the "delete" command needs.
     */
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a task list holding the given tasks, e.g. those loaded from disk.
     *
     * @param tasks Tasks to start with, in order.
     */
    public TaskList(List<Task> tasks) {
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
     * Removes the task at the given index, shifting later tasks up by one.
     *
     * @param index Index of the task to remove (starting from 0).
     * @return The task that was removed.
     * @throws UsagiException If no task has that index.
     */
    public Task delete(int index) throws UsagiException {
        checkIndex(index);
        return tasks.remove(index);
    }

    /**
     * Returns the task at the given index.
     *
     * @param index Index of the task (starting from 0).
     * @return The task at that index.
     * @throws UsagiException If no task has that index.
     */
    public Task get(int index) throws UsagiException {
        checkIndex(index);
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in the list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the tasks whose description contains the given keyword.
     *
     * The match ignores upper and lower case, so that "book" also finds
     * "Book club", as a user searching is unlikely to remember the exact case.
     *
     * @param keyword Text to look for.
     * @return The matching tasks, in list order. Empty if none match.
     */
    public List<Task> find(String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(lowerCaseKeyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    /**
     * Returns a read-only view of the tasks, e.g. for saving them.
     *
     * The view is read-only so that callers cannot change the list without
     * going through this class.
     *
     * @return The tasks, in order.
     */
    public List<Task> getAll() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Checks that a task exists at the given index.
     *
     * The error message counts tasks from 1, as the user does.
     *
     * @param index Index to check (starting from 0).
     * @throws UsagiException If no task has that index.
     */
    private void checkIndex(int index) throws UsagiException {
        if (index < 0 || index >= tasks.size()) {
            throw new UsagiException("uNAAA->(There is no task " + (index + 1) + ". You have "
                    + tasks.size() + " task(s) so far.)");
        }
    }
}
