package Task;

import java.util.ArrayList;

import Exceptions.InvalidCommandException;

/** Owns the tasks and provides operations for managing them. */
public class TasksList{
    /** Tasks in display order; internal indexes begin at zero. */
    private final ArrayList<ToDo> tasks = new ArrayList<>();

    /** Appends a task to the end of the list. */
    public void add(ToDo task) {
        tasks.add(task);
    }

    /** Returns the task at a zero-based index. */
    public ToDo get(int index) {
        return tasks.get(index);
    }

    /** Removes the task at a zero-based index, shifting later tasks forward. */
    public void remove(int index) {
        tasks.remove(index);
    }

    /** Returns the number of tasks currently in the list. */
    public int size() {
        return tasks.size();
    }

    /** Returns whether the list contains no tasks. */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /** Returns the most recently appended task; the list must not be empty. */
    public ToDo getLast() {
        return tasks.getLast();
    }

    /** Returns description matches in list order using a case-sensitive substring search. */
    public ArrayList<ToDo> find(String keyword) {
        ArrayList<ToDo> matches = new ArrayList<>();
        for (ToDo task : tasks) {
            if (task.getDescription().contains(keyword)) {
                matches.add(task);
            }
        }
        return matches;
    }

    /** Processes the mark, unmark or delete command */
    public void targetingCommand(String command, int target) {

        if (target <= 0 || target > tasks.size()) {
            throw new InvalidCommandException("Error: out of bounds!");
        }

        // The displayed task numbers start at 1, list indexes start at 0.
        ToDo selectedTask = tasks.get(target - 1);

        if (command.equals("mark")) {
            System.out.println("OK! Marked as done: ");
            selectedTask.setDone(true);
        } else if (command.equals("unmark")) {
            System.out.println("OK! Marked as not done: " + target);
            selectedTask.setDone(false);
        } else {
            System.out.println("OK! deleted task: " + target);
        }

        selectedTask.printResponse();

        if (command.equals("delete")) {
            tasks.remove(target - 1);
        }
    }

    // List out current commands
    /** Prints every task in order with a one-based task number. */
    public void List() {
        System.out.println("Here is your list: ");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.printf("%d. ", i + 1);
            tasks.get(i).printResponse();
        }
    }
}
