package list;

import java.util.ArrayList;
import java.util.List;
import task.Task;

/**
 * Manages the in-memory list of tasks and provides operations to query and modify it.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Initializes an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Initializes a TaskList populated with an existing list of tasks.
     *
     * @param initialTasks Tasks to populate the list with.
     */
    public TaskList(ArrayList<Task> initialTasks) {
        this.tasks = initialTasks;
    }

    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the specified index.
     *
     * @param index The zero-based index of the task to remove.
     * @return The Task that was removed.
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    public int size() {
        return tasks.size();
    }

    public Task getItem(int index) {
        return tasks.get(index);
    }

    /**
     * Finds and returns all tasks containing the specified keyword in their description.
     *
     * @param keyword The search term to match against task descriptions.
     * @return A list of matching Task objects.
     */
    public ArrayList<Task> findTasks(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(lowerKeyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }
}