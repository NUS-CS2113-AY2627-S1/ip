import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    public int size() {
        return tasks.size();
    }

    public Task get(int index) throws FunkyException {
        checkIndex(index);
        return tasks.get(index);
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task delete(int index) throws FunkyException {
        checkIndex(index);
        return tasks.remove(index);
    }

    public List<Task> asList() {
        return new ArrayList<>(tasks);
    }

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

    private void checkIndex(int index) throws FunkyException {
        if (index < 0 || index >= tasks.size()) {
            throw new FunkyException("Task number " + (index + 1) + " does not exist.");
        }
    }
}