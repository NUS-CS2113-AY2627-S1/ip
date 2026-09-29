import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The user's list of tasks. Tasks are accessed by zero-based index; an index
 * outside the list is reported to the user using its one-based task number.
 */
public class TaskList {
    private final List<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this(new ArrayList<>());
    }

    /** Creates a task list holding {@code tasks}, e.g. the tasks loaded from the save file. */
    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    /** Adds {@code task} to the end of the list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at {@code index}.
     *
     * @throws ShaheerException if there is no task at {@code index}.
     */
    public Task get(int index) throws ShaheerException {
        checkIndex(index);
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at {@code index}.
     *
     * @throws ShaheerException if there is no task at {@code index}.
     */
    public Task delete(int index) throws ShaheerException {
        checkIndex(index);
        return tasks.remove(index);
    }

    /** Returns the tasks whose description contains {@code keyword}, ignoring case. */
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

    /** Returns the number of tasks in the list. */
    public int size() {
        return tasks.size();
    }

    /** Returns a read-only view of the tasks, for displaying or saving them. */
    public List<Task> getAll() {
        return Collections.unmodifiableList(tasks);
    }

    private void checkIndex(int index) throws ShaheerException {
        if (index < 0 || index >= tasks.size()) {
            throw new ShaheerException("There is no task number " + (index + 1) + " in your list.");
        }
    }
}
