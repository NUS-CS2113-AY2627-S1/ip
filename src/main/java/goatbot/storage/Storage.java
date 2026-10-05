package goatbot.storage;

import goatbot.command.FileParser;
import goatbot.exception.GoatBotException;
import goatbot.task.Task;
import goatbot.task.TaskList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Saves and loads Goat Bot tasks on the hard disk.
 */
public class Storage {

    private final File file;

    /**
     * Creates a storage manager that writes to the specified file path.
     *
     * @param filePath path of the file used to store tasks
     */
    public Storage(String filePath) {
        file = new File(filePath);
    }

    /**
     * Overwrites the storage file with the tasks currently in the task list.
     *
     * @param tasks list containing the tasks
     * @throws IOException if the directory or file cannot be written
     */
    public void saveTasks(TaskList tasks) throws IOException {
        file.getParentFile().mkdirs();
        try (FileWriter fw = new FileWriter(file)) {
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                fw.write(task.toFileString());
                fw.write(System.lineSeparator());
            }
        }
    }

    /**
     * Loads stored tasks into the supplied task list.
     *
     * @param tasks list that receives the loaded tasks
     * @throws IOException if the storage file cannot be read
     * @throws GoatBotException if a stored task cannot be parsed
     */
    public void loadTasks(TaskList tasks) throws IOException, GoatBotException {
        if (!file.exists()) {
            return;
        }
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.startsWith("T")) {
                    tasks.add(FileParser.parseTodo(line));
                } else if (line.startsWith("D")) {
                    tasks.add(FileParser.parseDeadline(line));
                } else if (line.startsWith("E")) {
                    tasks.add(FileParser.parseEvent(line));
                } else {
                    throw new GoatBotException("Invalid task entry in storage");
                }
            }
        }
    }
}
