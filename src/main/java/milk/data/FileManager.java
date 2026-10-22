package milk.data;

import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import milk.task.Deadline;
import milk.task.Event;
import milk.task.Task;
import milk.task.Todo;
import java.util.ArrayList;

/**
 * Contains the methods used for file management for saving and loading tasks.
 */
public class FileManager {
    public static String filePath = "milk.txt";

    /**
     * Checks if a save file exists.
     * If file does not exist, creates one.
     */
    public static void CreateFile() {
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                if (file.createNewFile()) {
                    System.out.println("Save file created");
                } else {
                    System.out.println("Save file not created");
                }
            } else {
                System.out.println("Save file exists");
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    /**
     * Loads task file.
     * @return ArrayList of tasks contained in file.
     */
    public static ArrayList<Task> LoadFile() {
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            int numTasks = 0;
            File file = new File(filePath);
            Scanner s = new Scanner(file);
            while (s.hasNext()) {
                String line = s.nextLine();
                switch (line.charAt(0)) {
                    case 'T':
                        tasks.add(new Todo(line.substring(4)));
                        break;
                    case 'D':
                        String[] deadlineParams = line.split(" /by ");
                        tasks.add(new Deadline(deadlineParams[0].substring(4), deadlineParams[1]));
                        break;
                    case 'E':
                        String[] eventParams = line.split("(?:/from |/to )");
                        tasks.add(new Event(eventParams[0].substring(4), eventParams[1], eventParams[2]));
                        break;
                    default:
                        break;
                }
                tasks.get(numTasks).setMarked(line.charAt(2) == 'M');
                numTasks++;
            }
        } catch (FileNotFoundException e) {
            CreateFile();
            LoadFile();
        }
        return tasks;
    }

    /**
     * Updates the task file on task changes.
     *
     * @param tasks The array of tasks to be updated.
     * @param numTasks The number of tasks in the array.
     * @throws IOException If there is an issue with IO when handling the file.
     */
    public static void UpdateFile(ArrayList<Task> tasks, int numTasks) throws IOException {
        FileWriter fw = new FileWriter(filePath);
        String taskList  = "";
        int i = 0;
        for (Task task : tasks) {
            if (i > numTasks - 1) {
                break;
            }
            char taskType = task.getTaskIcon().charAt(1);
            taskList += taskType + " ";
            taskList += (task.isMarked() ? "M " : "U ");
            switch (taskType) {
                case 'T':
                    taskList += task.getDescription();
                    break;
                case 'D':
                    taskList += task.getDescription() + " /by " + ((Deadline) task).getBy();
                    break;
                case 'E':
                    taskList += task.getDescription() + " /from " + ((Event) task).getFrom() + " /to " + ((Event) task).getTo();
                    break;
                default:
                    break;
            }
            taskList += "\n";
            i++;
        }
        fw.write(taskList);
        fw.close();
    }
}
