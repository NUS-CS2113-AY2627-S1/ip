package bobby.taskmanager;

import bobby.exception.InvalidTaskException;
import bobby.exception.TaskNotFoundException;
import bobby.task.Task;

import java.util.ArrayList;

/**
 * Manages the list of tasks used by Bobby.
 */
public class TaskManager {
    private static final int MAX_TASK_COUNT = 100;
    private final ArrayList<Task> tasks;

    /**
     * Creates a TaskManager with an empty list of tasks.
     */
    public TaskManager() {
        this(new ArrayList<>());
    }

    /**
     * Creates a TaskManager with a given list of tasks.
     *
     * @param tasks the list of tasks to start with
     */
    public TaskManager(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Returns the list of tasks managed by this TaskManager.
     *
     * @return the list of tasks
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Returns the task with the specified task number.
     *
     * @param taskNumber the task number
     * @return the task corresponding to the number
     * @throws TaskNotFoundException if the task number is invalid
     */
    public Task getTask(int taskNumber) {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new TaskNotFoundException("You have " + tasks.size()
                    + " tasks. Please pick within the limits...");
        }

        return tasks.get(taskNumber - 1);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return an integer denoting the number of tasks
     */
    public int getTaskCount() {
        return tasks.size();
    }

    /**
     * Adds a task to the list of tasks.
     *
     * @param task the task to be added
     * @throws InvalidTaskException if the task limit is reached
     */
    public void addTask(Task task) {
        if (tasks.size() >= MAX_TASK_COUNT) {
            throw new InvalidTaskException("You have reached the limit on number of tasks.");
        }

        tasks.add(task);
    }

    /**
     * Deletes the task corresponding to the task number
     * and returns the deleted task.
     *
     * @param taskNumber the task number of the task to be deleted
     * @return the deleted task
     */
    public Task deleteTask(int taskNumber) {
        Task task = getTask(taskNumber);
        tasks.remove(taskNumber - 1);

        return task;
    }

    /**
     * Marks the task corresponding to the task number as done.
     *
     * @param taskNumber the task number of the task
     * @return task the task which was marked done
     */
    public Task markTask(int taskNumber) {
        Task task = getTask(taskNumber);
        task.markAsDone();
        return task;
    }

    /**
     * Marks the task corresponding to the task number as undone.
     *
     * @param taskNumber the task number of the task
     * @return task the task which was marked undone
     */
    public Task unmarkTask(int taskNumber) {
        Task task = getTask(taskNumber);
        task.markAsNotDone();
        return task;
    }

    /**
     * Finds tasks whose description contains the given keyword.
     *
     * @param keyword the keyword to search for
     * @return a list containing all tasks that match the keyword
     */
    public ArrayList<Task> findTasks(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        String keywordInLowerCase = keyword.toLowerCase();

        for (Task task : tasks) {
            if (task.getTaskDescription().toLowerCase().contains(keywordInLowerCase)) {
                matchingTasks.add(task);
            }
        }

        return matchingTasks;
    }
}
