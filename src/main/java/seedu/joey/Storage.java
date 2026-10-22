package seedu.joey;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;


import seedu.joey.task.Deadline;
import seedu.joey.task.Event;
import seedu.joey.task.Task;
import seedu.joey.task.Todo;
/**
 * Loads tasks from, and saves tasks to, a text file on the hard disk.
 * The save file lives at {@code data/joey.txt} relative to the current
 * working directory and uses the format
 * {@code TYPE | DONE | DESCRIPTION [| extra fields]}.
 */
public class Storage {
    private static final String FOLDER_PATH = "data";
    private static final String FILE_PATH = FOLDER_PATH + File.separator + "joey.txt";

    /**
     * Reads the saved tasks from disk.
     *
     * @return The saved tasks, or an empty list if there is no save file yet
     *         or the file cannot be read.
     */
    public static ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return tasks;
        }
        try (Scanner fileScanner = new Scanner(file)) {
            while(fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                Task task = parseLine(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read " + FILE_PATH + ". Starting with an empty list." );
        }
        return tasks;
    }

    /**
     * Writes all tasks to disk, creating the data folder if it does not exist.
     */
    public static void save(ArrayList<Task> tasks) {
        try {
            File folder = new File(FOLDER_PATH);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            FileWriter writer = new FileWriter(FILE_PATH);
            for (Task task : tasks) {
                writer.write(task.toFileFormat() + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Sorry, I could not save your tasks: " + e.getMessage());
        }  
    }

    /**
     * Converts one line of the save file back into a Task.
     * The line is expected in the form
     * {@code TYPE | DONE | DESCRIPTION [| extra fields]}.
     *
     * @param line A single line read from the save file.
     * @return The reconstructed task, or {@code null} if the line is unreadable.
     */
    private static Task parseLine(String line) {
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            return reportCorruptedLine(line);
        }
        boolean isDone = parts[1].equals("1");
        String description = parts[2];
        Task task;

        switch (parts[0]) {
            case "T":
                task = new Todo(description);
                break;
            case "D":
                if (parts.length < 4) {
                    return reportCorruptedLine(line);
                }
                try {
                    task = new Deadline(description, LocalDate.parse(parts[3]));
                } catch (DateTimeParseException e) {
                    return reportCorruptedLine(line);
                }
                break;
            case "E":
                if (parts.length < 5) {
                    return reportCorruptedLine(line);
                }
                task = new Event(description, parts[3], parts[4]);
                break;
            default:
                return reportCorruptedLine(line);
        }
        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

    private static Task reportCorruptedLine(String line){
        System.out.println("Skipping a line I could not understand in " + FILE_PATH + ": " + line);
        return null;
    }

    
}
