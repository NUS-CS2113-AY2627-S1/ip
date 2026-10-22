package dexter.data;

import dexter.tasks.Deadline;
import dexter.tasks.Event;
import dexter.tasks.Task;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/** Reads tasks from and writes tasks to Dexter's local data file. */
public class Storage {
    private static final File FILE = new File("data/dexter.txt");

    /** Creates a storage handler that uses Dexter's default data file. */
    public Storage() {
    }

    /**
     * Loads tasks from the local data file.
     *
     * <p>If the file does not exist, it is created and an empty task list is returned.
     * Malformed or unsupported records are skipped.</p>
     *
     * @return the tasks stored in the local data file
     * @throws IOException if the file cannot be created or read
     */
    public ArrayList<Task> readFromDatabase() throws IOException {
        File file = FILE;
        ArrayList<Task> itemList = new ArrayList<>();
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            return new ArrayList<>();
        }
        ArrayList<String> items = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                items.add(scanner.nextLine());
            }
        }
        for (String item : items) {
            String[] parts = item.split("\\|", -1);
            if (parts.length < 3) {
                continue;
            }
            boolean mark = parts[1].equals("1");
            Task task;
            switch (parts[0]) {
                case "T":
                    task = new Task(parts[2]);
                    break;
                case "D":
                    if (parts.length < 4) {
                        continue;
                    }
                    task = new Deadline(parts[2], parts[3]);
                    break;
                case "E":
                    if (parts.length < 5) {
                        continue;
                    }
                    task = new Event(parts[2], parts[3], parts[4]);
                    break;
                default:
                    continue;
            }
            if (mark) {
                task.markAsDone();
            }
            itemList.add(task);
        }
        return itemList;
    }

    /**
     * Saves the given tasks to the local data file, replacing its previous contents.
     *
     * @param itemList the tasks to save
     * @throws IOException if the data file cannot be created or written
     */
    public void writeToDatabase(ArrayList<Task> itemList) throws IOException {
        FILE.getParentFile().mkdirs();
        try (FileWriter fileWriter = new FileWriter(FILE)) {
            for (Task item : itemList) {
                String itemType = item.getType();
                String status;
                switch (itemType) {
                    case "T":
                        status = (item.getStatusIcon().equals("X")) ? "1" : "0";
                        fileWriter.write("T|" + status + "|" + item.getDescription());
                        break;
                    case "D":
                        status = (item.getStatusIcon().equals("X")) ? "1" : "0";
                        if (item instanceof Deadline deadline) {
                            fileWriter.write("D|" + status + "|" + deadline.getDescription() + "|" + deadline.getDueDate());
                        }
                        break;
                    case "E":
                        status = (item.getStatusIcon().equals("X")) ? "1" : "0";
                        if (item instanceof Event event) {
                            fileWriter.write("E|" + status + "|" + event.getDescription() + "|" + event.getStartDate() + "|" + event.getEndDate());
                        }
                        break;
                }
                fileWriter.write(System.lineSeparator());
            }
        }
    }
}
