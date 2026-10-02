package buddy.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds Buddy's list of tasks and the operations that can be performed
 * on it, so callers don't need to manipulate the underlying list
 * directly.
 */
public class TaskList {
    private final List<Task> tasks;

    /** Creates a new, empty task list. */
    public TaskList() {

        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list pre-populated with the given tasks, e.g. ones
     * just read back from the save file.
     *
     * @param tasks the tasks to start with, in order
     */
    public TaskList(List<Task> tasks) {

        this.tasks = new ArrayList<>(tasks);
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
     * Removes and returns the task at the given 0-based index.
     *
     * @param index the 0-based position of the task to remove
     * @return the task that was removed
     */
    public Task remove(int index) {

        return tasks.remove(index);
    }

    /**
     * Returns the task at the given 0-based index.
     *
     * @param index the 0-based position of the task
     * @return the task at that position
     */
    public Task get(int index) {

        return tasks.get(index);
    }

    /**
     * Returns how many tasks are currently in the list.
     *
     * @return the number of tasks
     */
    public int size() {

        return tasks.size();
    }

    /**
     * Returns the tasks as a plain list, e.g. for handing to
     * {@link buddy.storage.Storage#save}.
     *
     * @return the current tasks, in order
     */
    public List<Task> asList() {

        return tasks;
    }

    /**
     * Returns the tasks whose description contains the given keyword.
     * The match is case-sensitive.
     *
     * @param keyword the text to search for within each task's description
     * @return the matching tasks, in their original order
     */
    public List<Task> find(String keyword) {
        List<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                matches.add(task);
            }
        }
        return matches;
    }
}
