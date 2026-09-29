package kento;

import java.util.ArrayList;

import kento.commands.Task;

public class TaskList {

    private static ArrayList<Task> tasks = new ArrayList<>();

    /**
     * @return tasks arraylist
     */
    public static ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Add task to arraylist
     * 
     * @param task task you want to add
     */
    public static void addTask(Task task) {
        tasks.add(task);
    }

    public static Task getTaskByIndex(int index) {
        return tasks.get(index);
    }

    public static void removeTaskByIndex(int index) {
        tasks.remove(index);
    }

    public static int getTasksSize() {
        return tasks.size();
    }

}
