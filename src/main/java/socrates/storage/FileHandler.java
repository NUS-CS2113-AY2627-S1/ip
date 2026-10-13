package socrates.storage;

import socrates.Task;
import socrates.CommandHandler;
import socrates.SocratesException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Saves the task list to, and loads it from, a plain-text file on disk.
 */
public class FileHandler {

    private static final String FILEPATH = "./data/socrates.txt";

    /**
     * Writes {@code textToAdd} to the save file, creating its parent
     * directory first if needed, overwriting any existing content.
     */
    public static void writeToFile(String textToAdd) throws IOException {
        File file = new File(FILEPATH);
        file.getParentFile().mkdirs();
        FileWriter fw = new FileWriter(file);
        fw.write(textToAdd);
        fw.close();
    }

    /**
     * Saves every task in {@code list} to disk, one per line, overwriting
     * any previous save. Prints a message instead of throwing if saving fails.
     */
    public static void saveFile(ArrayList<Task> list) {
        String fileString = "";
        for (Task i : list) {
            fileString = (fileString + "\n" + i.toSaveFormat());
        }
        try {
            writeToFile(fileString);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Loads the saved task list from disk, returning an empty list if no
     * save file exists yet. Skips any line that can't be parsed.
     */
    public static ArrayList<Task> loadFile() {
        ArrayList<Task> tasks = new ArrayList<>();
        File f = new File(FILEPATH);
        if (!f.exists()) {
            return tasks;
        }
        try {
            Scanner s = new Scanner(f);
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (!line.isBlank()) {
                    try {
                        tasks.add(parseLine(line));
                    } catch (SocratesException e) {
                        System.out.println("Skipping unreadable saved task: " + line);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not load saved tasks");
        }
        return tasks;
    }

    private static Task parseLine(String line) throws SocratesException {
        String[] parts = line.split(" \\| ");
        boolean isDone = parts[1].equals("1");
        String description = parts[2];
//        T | 1 | read book
//        D | 0 | return book | June 6th
//        E | 0 | project meeting | Aug 6th 2-4pm

        // parts[0] : command
        // parts[1] : checked / unchecked
        // parts[2] : description
        // parts[3] : deadline / from
        // parts[4] : to
        Task task = switch (parts[0]) {
            case "T" -> CommandHandler.buildToDo(description);
            case "D" -> CommandHandler.buildDeadlines(description, parts[3]);
            case "E" -> CommandHandler.buildEvents(description, parts[3], parts[4]);
            default -> throw new IllegalStateException("Invalid task type: " + parts[0]);
        };
        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
