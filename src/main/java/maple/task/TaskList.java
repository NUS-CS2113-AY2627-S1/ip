package maple.task;

import java.util.ArrayList;
import java.util.List;

import maple.exception.MapleException;

/**
 * Manages the tasks stored by Maple.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Constructs an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a task list containing the given tasks.
     *
     * @param tasks the initial tasks.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the list.
     *
     * @param task the task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at the given one-based position.
     *
     * @param taskNumber the one-based task number.
     * @return the task at the requested position.
     * @throws MapleException if the task list is empty or the number is out of range.
     */
    public Task get(int taskNumber) throws MapleException {
        if (tasks.isEmpty()) {
            throw new MapleException("There are no tasks to update.");
        }
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new MapleException("Choose a task number from 1 to " + tasks.size() + ".");
        }
        return tasks.get(taskNumber - 1);
    }

    /**
     * Deletes and returns the task at the given one-based position.
     *
     * @param taskNumber the one-based task number.
     * @return the deleted task.
     * @throws MapleException if the task list is empty or the number is out of range.
     */
    public Task delete(int taskNumber) throws MapleException {
        Task task = get(taskNumber);
        tasks.remove(taskNumber - 1);
        return task;
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return the number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Finds tasks whose descriptions contain the given keyword.
     *
     * @param keyword the keyword to find.
     * @return the matching tasks in their current order.
     */
    public List<Task> find(String keyword) {
        List<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.matches(keyword)) {
                matches.add(task);
            }
        }
        return matches;
    }

    /**
     * Returns a snapshot of the tasks in their current order.
     *
     * @return the tasks in their current order.
     */
    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }
}
