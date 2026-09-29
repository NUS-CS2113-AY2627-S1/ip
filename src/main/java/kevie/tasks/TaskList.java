package kevie.tasks;

import kevie.UserInterface;
import kevie.exceptions.FileBadRawException;
import kevie.exceptions.FileCorruptionException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

import java.util.ArrayList;

/**
 * Represents a todo list consisting of a lot of tasks.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Creates a todo list.
     */
    public TaskList(){
        tasks = new ArrayList<Task>();
    }

    /**
     * Add a task into the todo list.
     *
     * @param newTask The task to add.
     */
    public void addTask(Task newTask){

        if (newTask == null){
            return;
        }
        tasks.add(newTask);
    }

    /**
     * Delete task with index from the todo list.
     *
     * @param index Index of task to delete (Task index = Task number in <code>TaskList.printAll()</code> - 1)
     */
    public void deleteTask(int index){

        if (index >= tasks.size() || index < 0) {
            return;
        }
        tasks.remove(index);
    }

    /**
     * Get task with index from the todo list.
     *
     * @param index Index of task to get (Task index = Task number in <code>TaskList.printAll()</code> - 1)
     * @return Task with task index indicated. Null if the task index is invalid.
     */
    public Task getTask(int index){

        if (index >= tasks.size() || index < 0) {
            return null;
        }
        return tasks.get(index);
    }

    /**
     * Get the number of tasks in the todo list.
     *
     * @return The number of tasks in the todo list.
     */
    public int getLength() {
        return tasks.size();
    }

    /**
     * Prints out the entire todo list.
     *
     * @param ui User interface to print the list in.
     */
    public void printAll(UserInterface ui){
        for (int i = 0; i < getLength(); i++) {
            ui.botSpeak((i + 1) + ". " + tasks.get(i).toString(), true);
        }
    }

    /**
     * Gets all indexes of tasks in the todo list with a certain keyword.
     *
     * @param keyword Keyword to find
     * @return List of integer indexes of tasks with the keyword.
     */
    public ArrayList<Integer> keywordFindTasks(String keyword){
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < getLength(); i++){
            if (tasks.get(i).getDescription().toLowerCase().contains(keyword.toLowerCase())){
                result.add(i);
            }
        }
        return result;
    }
}
