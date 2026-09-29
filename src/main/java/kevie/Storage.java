package kevie;

import kevie.exceptions.FileBadRawException;
import kevie.exceptions.FileCorruptionException;
import kevie.tasks.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Handles file loading and savings.
 */
public class Storage {
    private String path;

    /**
     * Creates a storage handler.
     * @param path Relative file path to store the save file in.
     */
    public Storage(String path){
        this.path = path;
    }

    /**
     * Save a todo list in a save file.
     *
     * @param taskList Todo list to be saved.
     * @param ui User interface to print exceptions in.
     */
    public void save(TaskList taskList, UserInterface ui){
        try{
            FileWriter writer = new FileWriter(path);

            for (int i = 0; i < taskList.getLength(); i++) {
                writer.write(taskList.getTask(i).getRaw());
                if (i < taskList.getLength() - 1) writer.write(System.lineSeparator());
            }
            writer.close();

        } catch (IOException e) {
            ui.botSpeak("I cannot save the list of tasks because an IO error occurred.");
        }
    }

    /**
     * Loads todo list from a save file.
     *
     * @param ui User interface to print exceptions in.
     * @return The todo list loaded from save file.
     * @throws FileCorruptionException If there is an IO error occurred.
     */
    public TaskList load(UserInterface ui) throws FileCorruptionException {
        TaskList tasks = new TaskList();
        int lineNo = 0;

        try{
            File file = new File(path);
            file.getParentFile().mkdirs();
            file.createNewFile();
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                try{
                    String[] rawArgs = scanner.nextLine().split(Pattern.quote(Task.RAW_SEPERATOR));
                    lineNo++;
                    switch (rawArgs[0]){
                        case "T":
                            tasks.addTask(new Todo(rawArgs, lineNo, ui));
                            break;
                        case "D":
                            tasks.addTask(new Deadline(rawArgs, lineNo, ui));
                            break;
                        case "E":
                            tasks.addTask(new Event(rawArgs, lineNo, ui));
                            break;
                        default:
                            throw new FileBadRawException(lineNo, ui);
                    }
                }
                catch (FileBadRawException e){
                    e.printMessage();
                }
            }
            scanner.close();

        } catch (IOException e){
            throw new FileCorruptionException("Cannot open file.", ui);
        }

        save(tasks, ui);
        return tasks;
    }
}
