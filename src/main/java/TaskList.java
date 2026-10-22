import java.util.ArrayList;

/**
 * The tasks of one session.
 * <p>
 * This is the only place that knows how a task number typed by the user maps
 * onto a position in the list. The numbers the user sees start at 1 while the
 * list beneath starts at 0, so every conversion between the two happens here --
 * the off-by-one has one home rather than one per command -- and a number that
 * refers to no task is refused here as well.
 */
public class TaskList {

    /** The tasks, in the order the user gave them. */
    private final ArrayList<Task> tasks;

    /**
     * Constructs an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task the task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Adds every task of another collection to the end of the list.
     *
     * @param newTasks the tasks to add, such as those just read from the data file.
     */
    public void addAll(ArrayList<Task> newTasks) {
        tasks.addAll(newTasks);
    }

    /**
     * Returns how many tasks the list holds.
     *
     * @return number of tasks in the list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the task the user refers to by number.
     *
     * @param taskNumber number as the user typed it, counting from 1.
     * @param commandWord command the user typed, used to phrase the error message.
     * @return the task that number refers to.
     * @throws CortisolException if the number refers to no task in the list.
     */
    public Task get(int taskNumber, String commandWord) throws CortisolException {
        checkTaskNumber(taskNumber, commandWord);
        return tasks.get(taskNumber - 1);
    }

    /**
     * Removes the task the user refers to by number.
     *
     * @param taskNumber number as the user typed it, counting from 1.
     * @param commandWord command the user typed, used to phrase the error message.
     * @return the task that was removed, so that it can still be reported after
     *         it has left the list.
     * @throws CortisolException if the number refers to no task in the list.
     */
    public Task remove(int taskNumber, String commandWord) throws CortisolException {
        checkTaskNumber(taskNumber, commandWord);
        return tasks.remove(taskNumber - 1);
    }

    /**
     * Returns every task whose description contains the given keyword.
     * <p>
     * The matches are a new list rather than a view, so the task list itself is
     * untouched by a search, and they keep the order they have here.
     *
     * @param keyword word or phrase to look for.
     * @return the matching tasks, in list order, or an empty list if none match.
     */
    public ArrayList<Task> find(String keyword) {
        ArrayList<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.matchesKeyword(keyword)) {
                matches.add(task);
            }
        }
        return matches;
    }

    /**
     * Returns the tasks themselves, for printing and for saving.
     * <p>
     * Callers are expected to read the list and not to change it: adding and
     * removing belong to this class, which is where the bounds are checked.
     *
     * @return the tasks, in order.
     */
    public ArrayList<Task> asArrayList() {
        return tasks;
    }

    /**
     * Refuses a task number that refers to no task.
     * <p>
     * An empty list is reported differently from a number out of range, since
     * "your list runs from 1 to 0" would be no help at all.
     *
     * @param taskNumber number as the user typed it, counting from 1.
     * @param commandWord command the user typed, used to phrase the error message.
     * @throws CortisolException if the number refers to no task in the list.
     */
    private void checkTaskNumber(int taskNumber, String commandWord)
            throws CortisolException {
        if (tasks.isEmpty()) {
            throw new CortisolException("Your list is presently empty, sir/madam. "
                    + "There is nothing to " + commandWord + " just yet.");
        }
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new CortisolException("I keep no task numbered " + taskNumber + ", sir/madam.\n"
                    + " Your list runs from 1 to " + tasks.size() + ".");
        }
    }
}
