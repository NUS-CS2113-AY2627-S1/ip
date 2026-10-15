package projectfolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * Handles loading tasks from and saving tasks to a data file.
 */
public class Storage {
    private static final String SEPARATOR = " | ";

    private final String filePath;

    /**
     * Creates a storage object that uses the specified file path.
     *
     * @param filePath path of the data file
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves the given task list to the data file.
     *
     * @param tasks task list to save
     * @throws DinoException if the tasks cannot be saved
     */
    public void save(TaskList tasks) throws DinoException {
        File dataFile = prepareDataFile();

        try (FileWriter writer = new FileWriter(dataFile)) {
            writeTasks(writer, tasks);
        } catch (IOException e) {
            throw new DinoException(
                    "OOPS!!! I could not save your tasks."
            );
        }
    }

    /**
     * Loads tasks from the data file.
     *
     * @return tasks loaded from the data file
     */
    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        File dataFile = new File(filePath);

        if (!dataFile.exists()) {
            return tasks;
        }

        loadTasksFromFile(dataFile, tasks);
        return tasks;
    }

    private File prepareDataFile() throws DinoException {
        File dataFile = new File(filePath);
        File directory = dataFile.getParentFile();

        if (directory != null
                && !directory.exists()
                && !directory.mkdirs()) {
            throw new DinoException(
                    "OOPS!!! I could not create the data folder."
            );
        }

        return dataFile;
    }

    private void writeTasks(FileWriter writer, TaskList tasks)
            throws IOException {
        for (int i = 0; i < tasks.size(); i++) {
            writer.write(convertTaskToData(tasks.get(i)));
            writer.write(System.lineSeparator());
        }
    }

    private String convertTaskToData(Task task) {
        String status = task.isDone() ? "1" : "0";

        if (task instanceof Todo) {
            return convertTodoToData(task, status);
        }

        if (task instanceof Deadline) {
            return convertDeadlineToData((Deadline) task, status);
        }

        return convertEventToData((Event) task, status);
    }

    private String convertTodoToData(Task task, String status) {
        return "T" + SEPARATOR
                + status + SEPARATOR
                + task.getDescription();
    }

    private String convertDeadlineToData(Deadline deadline, String status) {
        return "D" + SEPARATOR
                + status + SEPARATOR
                + deadline.getDescription() + SEPARATOR
                + deadline.getBy();
    }

    private String convertEventToData(Event event, String status) {
        return "E" + SEPARATOR
                + status + SEPARATOR
                + event.getDescription() + SEPARATOR
                + event.getFrom() + SEPARATOR
                + event.getTo();
    }

    private void loadTasksFromFile(File dataFile, ArrayList<Task> tasks) {
        try (Scanner fileScanner = new Scanner(dataFile)) {
            while (fileScanner.hasNextLine()) {
                addTaskFromData(fileScanner.nextLine(), tasks);
            }
        } catch (FileNotFoundException e) {
            return;
        }
    }

    private void addTaskFromData(String line, ArrayList<Task> tasks) {
        Task task = convertDataToTask(line);

        if (task != null) {
            tasks.add(task);
        }
    }

    private Task convertDataToTask(String line) {
        String[] taskData = line.split("\\s\\|\\s");

        if (taskData.length < 3) {
            return null;
        }

        Task task = createTaskFromData(taskData);

        if (task != null && taskData[1].equals("1")) {
            task.markAsDone();
        }

        return task;
    }

    private Task createTaskFromData(String[] taskData) {
        switch (taskData[0]) {
            case "T":
                return new Todo(taskData[2]);
            case "D":
                return createDeadlineFromData(taskData);
            case "E":
                return createEventFromData(taskData);
            default:
                return null;
        }
    }

    private Deadline createDeadlineFromData(String[] taskData) {
        if (taskData.length < 4) {
            return null;
        }

        LocalDate by = LocalDate.parse(taskData[3]);
        return new Deadline(taskData[2], by);
    }

    private Event createEventFromData(String[] taskData) {
        if (taskData.length < 5) {
            return null;
        }

        return new Event(taskData[2], taskData[3], taskData[4]);
    }
}