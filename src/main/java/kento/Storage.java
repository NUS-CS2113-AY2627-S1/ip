package kento;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;

import kento.commands.Deadline;
import kento.commands.Event;
import kento.commands.Task;
import kento.commands.Todo;

public class Storage {

    private static Path filepath;
    private static Task t;

    /**
     * Stores path when instance of storage is created
     * 
     * @param path
     */
    public Storage(Path path) {
        filepath = path;
    }

    /**
     * reads filepath file content
     * 
     * @return content of file
     */

    public static String readTasksFiles() {

        try {
            // Reads the entire file into a single String
            String content = Files.readString(filepath);

            return content; // Either string or empty

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * Create instance of ToDo Event and Deadline and adds them to tasks arraylist
     * 
     * @param tasks the arraylist that all tasks should be uploaded to
     */
    public static void loadTasksFiles(ArrayList<Task> tasks) {

        if (!filepath.toFile().exists()) {
            return;
        }

        String content = readTasksFiles();
        if (content.isEmpty()) {
            return;
        }
        String[] lines = content.split("\n");
        String[] taskArgs;

        for (String line : lines) {
            taskArgs = line.split("\\|");
            if (taskArgs.length < 2) {
                continue;
            }
            try {
                Task newTask = null;
                switch (taskArgs[0]) {
                    case "T":
                        newTask = new Todo(taskArgs[2]);
                        break;
                    case "D":
                        newTask = new Deadline(taskArgs[2], LocalDate.parse(taskArgs[3]));
                        break;
                    case "E":
                        newTask = new Event(taskArgs[2], LocalDate.parse(taskArgs[3]), LocalDate.parse(taskArgs[4]));
                        break;
                    default:
                        System.err.println("Unknown task type: " + taskArgs[0]);
                        continue;
                }
                if (taskArgs[1].equals("X")) {
                    newTask.setIsDone(true);
                }
                tasks.add(newTask);
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    /**
     * create a string with descriptions of all tasks
     * 
     * @param tasks arraylist of task
     * @return tasksFile
     */
    private static String getTasksFiles(ArrayList<Task> tasks) {

        String tasksFile = "";
        for (int i = 0; i < tasks.size(); i++) {
            t = tasks.get(i);
            // Make a custom in each command class
            tasksFile += t.getTaskFile() + "\n";
        }
        return tasksFile;

    }

    /**
     * write task files onto filepath file
     * 
     * @param tasks gets the tasks
     */
    public static void writeTasksFiles(ArrayList<Task> tasks) {

        FileWriter fw = null;
        try {
            File dir = filepath.getParent().toFile();
            dir.mkdirs();
            fw = new FileWriter(filepath.toFile());
            fw.write(getTasksFiles(tasks));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
