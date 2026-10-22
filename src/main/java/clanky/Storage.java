package clanky;

import clanky.task.Task;
import clanky.task.Todo;
import clanky.task.Deadline;
import clanky.task.Event;

import java.io.*;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.time.LocalDate;

/**
 * Handles reading and writing tasks to a save file on disk,
 * so tasks persist between application runs.
 */
public class Storage {
    private final String filePath;

    /**
     * Creates a Storage bound to the given file path.
     *
     * @param filePath the path to the save file.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Writes the given tasks to the save file, creating parent directories
     * if needed.
     *
     * @param list the tasks to save.
     * @throws ClankyException if the file can't be written.
     */
    public void save(ArrayList<Task> list) throws ClankyException {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            for (Task task : list) {
                writer.write(task.toSaveFormat() + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e){
            throw new ClankyException("Failed to save tasks: " + e.getMessage());
        }
    }

    /**
     * Loads tasks from the save file, or returns an empty list if no save
     * file exists yet.
     *
     * @return the loaded tasks.
     * @throws ClankyException if the file exists but can't be read or parsed.
     */
    public ArrayList<Task> load() throws ClankyException {
        ArrayList<Task> list = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return list;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(parseLine(line));
            }
            reader.close();
        } catch (IOException e) {
            throw new ClankyException("Failed to load tasks: " + e.getMessage());
        }
        return list;
    }

    /**
     * Parses a single save-file line back into a {@link Task}.
     *
     * @param line one pipe-delimited line from the save file.
     * @return the reconstructed task.
     * @throws ClankyException if the line is corrupted, incomplete, or has an unknown task type.
     */
    private Task parseLine(String line) throws ClankyException {
        String[] fields = line.split("\\|");
        for (int i = 0; i < fields.length; i ++) {
            fields[i] = fields[i].trim();
        }
        String type = fields[0];
        boolean isDone = fields[1].equals("1");

        Task task;
        try {
            switch (type) {
                case "T":
                    task = new Todo(fields[2]);
                    break;
                case "D":
                    task = new Deadline(fields[2], LocalDate.parse(fields[3]));
                    break;
                case "E":
                    task = new Event(fields[2], LocalDate.parse(fields[3]), LocalDate.parse(fields[4]));
                    break;
                default:
                    throw new ClankyException("Corrupted Save File: Unkown Task Type '" + type + "'");
            }
        } catch (DateTimeParseException e) {
            throw new ClankyException("Corrupted save file: Invalid date on line: " + line);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ClankyException("Corrupted save file: Incomplete line: " + line);
        }
        
        if (isDone) {
            task.setMark();
        }
        return task;
    }
}
