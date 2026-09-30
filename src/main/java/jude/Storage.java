package jude;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import jude.task.Deadline;
import jude.task.Event;
import jude.task.Task;
import jude.task.Todo;

/**
 * Handles the loading and saving of tasks to a file.
 */
public class Storage {
    private String filePath;

    private static final String TODO_TYPE = "T";
    private static final String DEADLINE_TYPE = "D";
    private static final String EVENT_TYPE = "E";
    private static final String FILE_DELIMITER = " \\| ";
    private static final String EVENT_TIME_DELIMITER = " - ";
    private static final String IS_DONE_FLAG = "1";

    /**
     * Constructs a Storage object with the specified file path.
     *
     * @param filePath The path to the file where tasks are saved and loaded from.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads the tasks from the file.
     *
     * @return A list of tasks loaded from the file.
     */
    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return tasks;
            }
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNext()) {
                String line = fileScanner.nextLine();
                try {
                    Task task = parseTaskFromString(line);
                    if (task != null) {
                        tasks.add(task);
                    }
                } catch (Exception e) {
                    System.out.println("Skipping corrupted data: " + line);
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        return tasks;
    }

    /**
     * Saves the given list of tasks to the file.
     *
     * @param taskList The list of tasks to be saved.
     */
    public void save(TaskList taskList) {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();

            FileWriter fw = new FileWriter(file);
            for (int i = 0; i < taskList.size(); i++) {
                Task task = taskList.get(i);
                fw.write(task.toFileFormat() + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("Something went wrong while saving: " + e.getMessage());
        }
    }

    /**
     * Parses a task from its string representation in the file.
     *
     * @param line The string representation of the task.
     * @return The parsed Task object.
     * @throws Exception If there is an error parsing the string.
     */
    private Task parseTaskFromString(String line) throws Exception {
        String[] parts = line.split(FILE_DELIMITER);
        String type = parts[0];
        boolean isDone = parts[1].equals(IS_DONE_FLAG);
        String description = parts[2];

        Task task = null;

        if (type.equals(TODO_TYPE)) {
            task = new Todo(description);
        } else if (type.equals(DEADLINE_TYPE)) {
            task = new Deadline(description, parts[3]);
        } else if (type.equals(EVENT_TYPE)) {
            String timeString = parts[3];
            String[] times = timeString.split(EVENT_TIME_DELIMITER);
            String start = times[0];
            String end = times[1];
            task = new Event(description, start, end);
        }

        if (task != null && isDone) {
            task.markAsDone();
        }

        return task;
    }
}