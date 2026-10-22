package neo;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import neo.task.Deadline;
import neo.task.Event;
import neo.task.Task;
import neo.task.Todo;

/** Checks storage round trips and manual edits using an isolated temporary file. */
public class StorageTest {
    /**
     * Runs the storage checks and removes their temporary data afterward.
     *
     * @param args Unused command-line arguments.
     * @throws Exception If a storage operation or regression check fails.
     */
    public static void main(String[] args) throws Exception {
        Path directory = Files.createTempDirectory(Path.of("out"), "neo-storage-");
        Path file = directory.resolve("neo.txt");
        try {
            checkReadableRoundTrip(file);
            checkReadableInput(file);
            checkManualEdits(file);
            checkCorruptedRecords(file);
            System.out.println("PASS: 4 storage regression checks");
        } finally {
            Files.deleteIfExists(file);
            Files.delete(directory);
        }
    }

    /** Checks all text fields and completion states after loading through a new Storage instance. */
    private static void checkReadableRoundTrip(Path file) throws Exception {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("read A | B \\ notes\nnext line: café 雪"));
        tasks.add(new Deadline("compare A | B", "Friday | Saturday"));
        tasks.add(new Event("meet A | B", "9am | 10am", "11am | noon"));
        tasks.add(new Todo(""));
        tasks.add(new Deadline("empty deadline", ""));
        tasks.add(new Event("empty times", "", ""));
        tasks.add(new Todo("  literal \\n and \\r, carriage\rreturn, trailing slash\\"));
        tasks.add(new Todo("trailing spaces  "));
        tasks.get(0).markAsDone();
        tasks.get(1).markAsDone();
        tasks.get(2).markAsDone();

        new Storage(file.toString()).save(tasks);
        checkReadableRecords(file, tasks.size());
        checkTasks(tasks, new Storage(file.toString()).load());
    }

    /** Checks plain records with literal separators in the last field and escaped pipes elsewhere. */
    private static void checkReadableInput(Path file) throws Exception {
        String readableData = "T | 1 | read | literal text\n"
                + "D | 0 | submit report | Friday | evening\n"
                + "E | 1 | team sync | 10am | 11am\n"
                + "D | 0 | read A \\| B | Friday\n"
                + "E | 0 | meet A \\| B | 9am \\| 10am | noon\n";
        Files.writeString(file, readableData, StandardCharsets.UTF_8);
        ArrayList<Task> expected = new ArrayList<>();
        expected.add(new Todo("read | literal text"));
        expected.add(new Deadline("submit report", "Friday | evening"));
        expected.add(new Event("team sync", "10am", "11am"));
        expected.add(new Deadline("read A | B", "Friday"));
        expected.add(new Event("meet A | B", "9am | 10am", "noon"));
        expected.get(0).markAsDone();
        expected.get(2).markAsDone();

        ArrayList<Task> loadedTasks = new Storage(file.toString()).load();
        checkTasks(expected, loadedTasks);
        new Storage(file.toString()).save(loadedTasks);
        checkReadableRecords(file, expected.size());
        checkTasks(expected, new Storage(file.toString()).load());
    }

    /** Checks readable output and simulates a user editing descriptions, status, and dates. */
    private static void checkManualEdits(Path file) throws Exception {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("read book"));
        tasks.add(new Deadline("return book", "June 6th"));
        tasks.add(new Event("meet friends", "10 am", "9 pm"));
        new Storage(file.toString()).save(tasks);
        List<String> expectedLines = List.of("T | 0 | read book",
                "D | 0 | return book | June 6th", "E | 0 | meet friends | 10 am | 9 pm");
        if (!Files.readAllLines(file, StandardCharsets.UTF_8).equals(expectedLines)) {
            throw new AssertionError("Saved fields are not human-readable.");
        }

        String editedData = Files.readString(file, StandardCharsets.UTF_8)
                .replace("T | 0 | read book", "T | 1 | read newspaper")
                .replace("June 6th", "June 7th").replace("10 am", "11 am");
        Files.writeString(file, editedData, StandardCharsets.UTF_8);
        tasks.set(0, new Todo("read newspaper"));
        tasks.get(0).markAsDone();
        tasks.set(1, new Deadline("return book", "June 7th"));
        tasks.set(2, new Event("meet friends", "11 am", "9 pm"));
        checkTasks(tasks, new Storage(file.toString()).load());
    }

    /** Checks that malformed records are skipped without discarding valid following tasks. */
    private static void checkCorruptedRecords(Path file) throws Exception {
        String corruptedData = "D | 0 | missing deadline\n"
                + "T | 0\n"
                + "T | 2 | invalid status\n"
                + "Z | 0 | unknown type\n"
                + "T | 0 | bad\\q\n"
                + "T | 0 | bad\\\n"
                + "E | 0 | missing times\n"
                + "T | 1 | safe\n";
        Files.writeString(file, corruptedData, StandardCharsets.UTF_8);
        ArrayList<Task> expected = new ArrayList<>();
        expected.add(new Todo("safe"));
        expected.get(0).markAsDone();
        checkTasks(expected, new Storage(file.toString()).load());
    }

    /** Checks task types, descriptions, completion states, and displayed date/time values. */
    private static void checkTasks(ArrayList<Task> expected, ArrayList<Task> actual) {
        if (expected.size() != actual.size()) {
            throw new AssertionError("Task count changed after loading.");
        }
        for (int i = 0; i < expected.size(); i++) {
            Task expectedTask = expected.get(i);
            Task actualTask = actual.get(i);
            if (!expectedTask.getClass().equals(actualTask.getClass())
                    || !expectedTask.getDescription().equals(actualTask.getDescription())
                    || !expectedTask.getStatus().equals(actualTask.getStatus())
                    || !expectedTask.toString().equals(actualTask.toString())) {
                throw new AssertionError("Task " + (i + 1) + " changed after loading: " + actualTask);
            }
        }
    }

    /** Checks that every task occupies one line starting directly with its task type. */
    private static void checkReadableRecords(Path file, int taskCount) throws Exception {
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        if (lines.size() != taskCount) {
            throw new AssertionError("Expected one storage line per task.");
        }
        for (String line : lines) {
            if (!line.matches("[TDE] \\| [01] \\| .*")) {
                throw new AssertionError("Saved task does not start with its type and completion state.");
            }
        }
    }
}
