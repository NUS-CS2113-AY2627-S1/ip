package doug.task;

import java.util.ArrayList;

import doug.errors.DougException;

/**
 * Manages the collection of tasks used by the application.
 */
public class TaskList {
    /** Mutable collection containing the tasks in display order. */
    private ArrayList<Task> taskList;

    /**
     * Creates a task list backed by the supplied collection.
     *
     * @param loadedTaskList tasks loaded when the application starts
     */
    public TaskList(ArrayList<Task> loadedTaskList) {
        this.taskList = loadedTaskList;
    }

    /**
     * Appends a task to the end of the list.
     *
     * @param task task to add
     */
    public void addTask(Task task) {
        taskList.add(task);
    }

    /**
     * Changes the completion state of the task at the specified index.
     *
     * @param index zero-based index of the task to update
     * @param isMarked completion state to assign to the task
     * @return the updated task
     * @throws DougException if the index does not identify a task in the list
     */
    public Task markTask(int index, boolean isMarked) {
        if (index < 0 || index >= taskList.size())
            throw new DougException("What? That doesn't exist yet!");
        Task toMark = taskList.get(index);
        toMark.setDone(isMarked);
        return toMark;
    }

    /**
     * Removes the task at the specified index.
     *
     * @param index zero-based index of the task to remove
     * @return the removed task
     * @throws DougException if the index does not identify a task in the list
     */
    public Task deleteTask(int index) {
        if (index < 0 || index >= taskList.size())
            throw new DougException("Delete something thats already in!");
        Task toDelete = taskList.get(index);
        taskList.remove(index);
        return toDelete;
    }

    /**
     * Finds tasks whose descriptions contain a keyword, ignoring letter case.
     *
     * @param keyword text to search for in task descriptions
     * @return matching tasks in their original list order
     */
    public ArrayList<Task> findTasks(String keyword) {
        ArrayList<Task> matchedTasks = new ArrayList<>();
        String lowerCaseKeyword = keyword.toLowerCase();

        for (Task task : taskList) {
            if (task.getDescription().toLowerCase().contains(lowerCaseKeyword)) {
                matchedTasks.add(task);
            }
        }
        return matchedTasks;
    }

    /**
     * Returns the task at the specified index.
     *
     * @param index zero-based index of the task to return
     * @return task at the specified index
     * @throws DougException if the index does not identify a task in the list
     */
    public Task getTask(int index) {
        if (index < 0 || index >= taskList.size())
            throw new DougException("Can't get that. Not a thing.");
        return taskList.get(index);
    }

    /**
     * Returns the collection backing this task list.
     *
     * @return mutable collection of tasks
     */
    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    /**
     * Returns the number of tasks currently in the list.
     *
     * @return number of tasks
     */
    public int size() {
        return taskList.size();
    }
}
