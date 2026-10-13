package bobby.storage;

import bobby.exception.StorageException;
import bobby.task.Deadline;
import bobby.task.Event;
import bobby.task.Task;
import bobby.task.Todo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles creation and editing of the data file
 * containing saved tasks and their information.
 */
public class Storage {
    private final String filePath;

    /**
     * Creates a Storage object for the specified data file.
     * Creates the file if it does not already exist.
     *
     * @param filePath the path to the data file
     * @throws StorageException if the data file cannot be created
     */
    public Storage(String filePath) {
        this.filePath = filePath;

        try {
            createFileIfNeeded();
        } catch (IOException e) {
            throw new StorageException("Unable to create data file.");
        }
    }

    /**
     * Loads all saved tasks from the data file.
     *
     * @return a list containing the tasks loaded from the data file
     * @throws StorageException if the data file cannot be read
     */
    public ArrayList<Task> loadFile() {
        File file = new File(filePath);
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            readFileContentsIntoTaskList(file, tasks);
        } catch (FileNotFoundException e) {
            throw new StorageException("File not found.");
        }

        return tasks;
    }

    /**
     * Saves the current list of tasks to the data file
     *
     * @param tasks the list of tasks
     * @throws StorageException if the file cannot be written to
     */
    public void saveFile(ArrayList<Task> tasks) {
        try {
            FileWriter fw = new FileWriter(filePath);
            for (Task task : tasks) {
                fw.write(convertTaskToFileFormat(task));
            }

            fw.close();
        } catch (IOException e) {
            throw new StorageException("Something happened while saving file");
        }
    }

    /**
     * Creates the data file and its parent directory if they do not exist.
     *
     * @throws IOException if the file or parent directory cannot be created
     */
    private void createFileIfNeeded() throws IOException {
        File file = new File(filePath);
        File parentFile = file.getParentFile();

        if (parentFile != null && !parentFile.exists()) {
            parentFile.mkdirs();
        }

        if (!file.exists()) {
            file.createNewFile();
        }
    }

    /**
     * Reads the contents of the data file and converts
     * them into tasks that are added to the task list.
     *
     * @param file the data file to read
     * @param tasks the list where the tasks are added
     * @throws FileNotFoundException if the data file cannot be found
     */
    private void readFileContentsIntoTaskList(File file, ArrayList<Task> tasks) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            tasks.add(convertLineToTask(scanner.nextLine()));
        }
    }

    /**
     * Converts a line from the data file into a Task object.
     *
     * @param line the line containing the task information
     * @return the task obtained from the given line
     * @throws StorageException if the line contains invalid task data
     */
    private Task convertLineToTask(String line) {
        String[] args = line.split(" \\| ");
        Task task;

        try {
            boolean isDone = parseStatus(args[1]);
            switch (args[0]) {
                case "T" -> task = new Todo(args[2], isDone);
                case "D" -> task = new Deadline(args[2], isDone, args[3]);
                case "E" -> task = new Event(args[2], isDone, args[3], args[4]);
                default -> throw new StorageException("This line cannot be converted to a task.");
            }
        } catch (IndexOutOfBoundsException e) {
            throw new StorageException("Storage file contains invalid task data");
        }

        return task;
    }

    /**
     * Parses a task status from the data file
     *
     * @param arg the stored status, where '1' represents done
     *            and '0' represents not done
     * @return true if the task is done, otherwise false
     * @throws StorageException if the status is neither '1' nor '0'
     */
    private boolean parseStatus(String arg) {
        if (arg.equals("1")) {
            return true;
        }

        if (arg.equals("0")) {
            return false;
        }

        throw new StorageException("Invalid task status in storage file");
    }

    /**
     * Converts a Task object into its file storage format.
     *
     * @param task the task to convert
     * @return a string representing the task in file format
     * @throws StorageException if the task cannot be converted
     */
    private String convertTaskToFileFormat(Task task) {
        int status = (task.getStatus())? 1 : 0;
        String statusAndDescription = " | " + status + " | " + task.getTaskDescription();
        String line;

        if (task instanceof Todo) {
            line = "T"
                    + statusAndDescription;
        } else if (task instanceof Deadline deadline) {
            line = "D"
                    + statusAndDescription
                    + " | "
                    + deadline.getBy();
        } else if (task instanceof Event event) {
            line = "E"
                    + statusAndDescription
                    + " | "
                    + event.getStart()
                    + " | "
                    + event.getEnd();
        } else {
            throw new StorageException("Unable to convert task to file format");
        }

        return line + System.lineSeparator();
    }
}