package cole;

import java.util.ArrayList;

/**
 * Stores the user's tasks and provides operations to add, remove and access them.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<Task>();
    }

    /**
     * Creates a task list containing the given tasks, e.g. those loaded from the data file.
     *
     * @param tasks the initial tasks
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes the task at the given position.
     *
     * @param index 0-based position of the task
     * @return the task that was removed
     * @throws IndexOutOfBoundsException if the index is not in the list
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the task at the given position without removing it.
     *
     * @param index 0-based position of the task
     * @return the task at that position
     * @throws IndexOutOfBoundsException if the index is not in the list
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return how many tasks there are
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the tasks whose description contains the given keyword, ignoring case.
     * The tasks keep the same order as in this list.
     *
     * @param keyword the text to search for
     * @return the matching tasks, or an empty list if none match
     */
    public ArrayList<Task> find(String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(lowerCaseKeyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    /**
     * Returns the underlying list, for saving to file or displaying to the user.
     *
     * @return all tasks in their current order
     */
    public ArrayList<Task> getAll() {
        return tasks;
    }
}
