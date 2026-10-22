package neo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import neo.exception.NeoException;
import neo.task.Deadline;
import neo.task.Event;
import neo.task.Task;
import neo.task.Todo;

/**
 * Handles the loading and saving of task data to a persistent text file.
 * Creates the necessary directories and files if they do not exist, and parses
 * the stored text back into recognizable Task objects.
 */
public class Storage {
    private String filePath;

    /**
     * Constructs a Storage instance with the specified file path.
     *
     * @param filePath The relative or absolute file path where task data is saved.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads the tasks from the storage file into an ArrayList.
     * Reads the file line by line, identifies the task type (Todo, Deadline, Event),
     * and reconstructs the objects from readable, pipe-separated records.
     * Corrupted lines are skipped automatically.
     *
     * @return An {@code ArrayList<Task>} containing the tasks parsed from the file.
     * @throws NeoException If the data file does not exist at the specified path.
     */
    public ArrayList<Task> load() throws NeoException {
        ArrayList<Task> loadedTasks = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            throw new NeoException("Data file not found.");
        }

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.isBlank()) {
                    continue;
                }

                try {
                    loadedTasks.add(parseTask(line));
                } catch (IllegalArgumentException e) {
                    System.out.println("Skipping corrupted data line: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Data file not found.");
        }
        return loadedTasks;
    }

    /**
     * Reconstructs one task from a readable, pipe-separated record.
     *
     * @param line The complete stored record.
     * @return The task with its stored completion state.
     * @throws IllegalArgumentException If the record type, fields, or escape sequences are invalid.
     */
    private Task parseTask(String line) {
        String type = line.split(" \\| ", 2)[0];
        int fieldCount = switch (type) {
        case "T" -> 3;
        case "D" -> 4;
        case "E" -> 5;
        default -> throw new IllegalArgumentException("Unknown task type.");
        };

        // Allow literal separators inside the last field and preserve empty fields.
        String[] parts = line.split(" \\| ", fieldCount);
        if (parts.length != fieldCount || (!parts[1].equals("0") && !parts[1].equals("1"))) {
            throw new IllegalArgumentException("Invalid task fields.");
        }
        for (int i = 2; i < parts.length; i++) {
            parts[i] = unescapeField(parts[i]);
        }

        Task task = switch (type) {
        case "T" -> new Todo(parts[2]);
        case "D" -> new Deadline(parts[2], parts[3]);
        case "E" -> new Event(parts[2], parts[3], parts[4]);
        default -> throw new IllegalArgumentException("Unknown task type.");
        };
        if (parts[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /** Restores escaped characters in a readable field, rejecting incomplete or unknown escapes. */
    private String unescapeField(String text) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);
            if (character != '\\') {
                result.append(character);
                continue;
            }
            i++;
            if (i == text.length()) {
                throw new IllegalArgumentException("Incomplete escape sequence.");
            }
            result.append(switch (text.charAt(i)) {
            case '\\' -> '\\';
            case '|' -> '|';
            case 'n' -> '\n';
            case 'r' -> '\r';
            default -> throw new IllegalArgumentException("Unknown escape sequence.");
            });
        }
        return result.toString();
    }

    /**
     * Saves the provided list of tasks to the storage file.
     * Overwrites the existing file content with the string representations of the
     * tasks using pipe-separated records with escaped, readable text fields. Automatically creates
     * the parent directories if they are missing.
     *
     * @param tasks The {@code ArrayList<Task>} containing the current tasks to save.
     */
    public void save(ArrayList<Task> tasks) {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            for (int i = 0; i < tasks.size(); i++) {
                writer.write(tasks.get(i).toSaveFormat() + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }
}
