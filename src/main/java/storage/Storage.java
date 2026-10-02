package storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import constants.Constants;
import task.Deadline;
import task.Event;
import task.Task;
import task.Todo;
import tasklist.TaskList;

/** Saves and loads tasks using the application's comma-separated file format. */
public class Storage {

    private static final DateTimeFormatter STORAGE_DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yy HHmm");

    private String saveFilePath;
    private final Constants c;
    
    /** Creates storage for the supplied file path.
     * @param inputFilePath file used to save and load tasks
     * @param constants keywords and markers used in saved records
     */
    public Storage(String inputFilePath, Constants constants) {
        this.saveFilePath = inputFilePath;
        this.c = constants;
    }

    // ============================================== LOAD SAVE ============================================================
    /** Ensures that a save path exists and reports whether it is a regular file.
     * @param path path to check or create
     * @return true when the path is a regular file
     */
    private boolean fileExists(String path) {
        Path filePath = Paths.get(path);

        if (Files.isRegularFile(filePath)) {
            return true;
        }

        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
            return Files.isRegularFile(filePath);
        } catch (IOException e) {
            return false;
        }
    }

    /** Appends one task record; called after a task is added.
     * @param task task to save
     */
    public void saveData(Task task) {
        if (task == null) {
            return;
        }

        if (!fileExists(saveFilePath)) {
            return;
        }

        try (FileWriter writer = new FileWriter(saveFilePath, true)) {
            String saveString = formatSaveString(task);

            writer.write(saveString + System.lineSeparator());

        } catch (IOException e) {
            return;
        }
    }

    /** Rewrites the file with the current tasks; called after mark, unmark, or delete.
     * @param tasks current in-memory task list
     */
   public void updateData(TaskList tasks) {
    if (!fileExists(saveFilePath)) {
        return;
    }

    try (FileWriter writer = new FileWriter(saveFilePath)) {
        for (int i = 0; i < tasks.getSize(); i++) {
            writer.write(formatSaveString(tasks.getTask(i)) + System.lineSeparator());
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    /** Converts one task into the record format used by {@link #saveData(Task)}.
     * @param task task to convert
     * @return comma-separated task record
     */
    private String formatSaveString(Task task) {
        String saveString;
        String isDoneString = (task.isDone()) ? c.MARKDONE_CHAR : "";

        if (task instanceof Deadline deadline) {
            saveString = c.DEADLINE_CHAR
                    + "," + isDoneString
                    + "," + deadline.getName()
                    + "," + STORAGE_DATE_FORMAT.format(deadline.getDeadline());
        } else if (task instanceof Event event) {
            saveString = c.EVENT_CHAR
                    + "," + isDoneString
                    + "," + event.getName()
                    + "," + STORAGE_DATE_FORMAT.format(event.getFrom())
                    + "," + STORAGE_DATE_FORMAT.format(event.getTo());
        } else {
            saveString = c.TODO_CHAR
                    + "," + isDoneString
                    + "," + task.getName();
        }

        return saveString;
    }
    
    /** Loads saved records into the supplied task list; called when the app starts.
     * @param tasks task list to populate
     * @return the supplied list after loading saved tasks
     */
    public TaskList loadData(TaskList tasks) {
        // open path of ./data/SmartFella.txt
        if (!fileExists(saveFilePath)) {
            return tasks; // TODO: is it ok to just return? shld probably do something about this
        }

        //file exists. so load data from file
        File f = new File(saveFilePath);
        Task tempTask;

        try (Scanner s = new Scanner(f)) {
            while (s.hasNext()) {
                tempTask = parseSaveString(s.nextLine());
                tasks.addTaskObject(tempTask);
            }

            return tasks;
            
        } catch (FileNotFoundException e) {
            return tasks; // TODO: is it ok to just return? shld probably do something about this
        }
        
    }

    /** Converts one saved record into a task; called by {@link #loadData(TaskList)}.
     * @param saveString comma-separated record from the save file
     * @return task represented by the record
     */
    private Task parseSaveString(String saveString) {
        String[] temp = saveString.split(",");

        Task newTask;

        if (saveString.startsWith(c.DEADLINE_CHAR)){
            newTask = new Deadline(temp[2], LocalDateTime.parse(temp[3], STORAGE_DATE_FORMAT));

        } else if (saveString.startsWith(c.EVENT_CHAR)){
            newTask = new Event(temp[2],
                    LocalDateTime.parse(temp[3], STORAGE_DATE_FORMAT),
                    LocalDateTime.parse(temp[4], STORAGE_DATE_FORMAT));

        } else {
            newTask = new Todo(temp[2]);

        } 

        if (temp[1].equals(c.MARKDONE_CHAR)) {
            newTask.markDone();
        }

        return newTask;
    }

}
