import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import Task.Deadline;
import Task.Event;
import Task.TasksList;
import Ui.Parser;
import Ui.Storage;
import Ui.Ui;

/** Runs dependency-free persistence checks in an isolated working directory. */
public class StorageTest {
    private static final Path SAVE_FILE = Path.of("data", "tasks.txt");

    /** Checks saving immediately after commands, then exercises the console entry point. */
    public static void main(String[] args) throws Exception {
        Storage storage = new Storage();
        if (Files.exists(SAVE_FILE)) {
            throw new AssertionError("Run this check in an empty working directory");
        }
        if (!storage.loadTasks().isEmpty()) {
            throw new AssertionError("First use should load an empty list");
        }
        TasksList tasks = new TasksList();
        Ui command = new Ui(tasks);
        command.processCommand("list");
        if (Files.exists(SAVE_FILE)) {
            throw new AssertionError("Listing must not create a save file");
        }
        command.processCommand("todo read café book");
        expectLines("T\t0\tread café book");
        command.processCommand("deadline return book /by June 6th");
        command.processCommand("event meeting /from 2pm /to 4pm");
        command.processCommand("mark 2");
        command.processCommand("unmark 2");
        command.processCommand("delete 1");
        command.processCommand("mark 2");
        List<String> snapshot = List.of("D\t0\treturn book\tJune 6th", "E\t1\tmeeting\t2pm\t4pm");
        expectLines(snapshot.toArray(String[]::new));
        command.processCommand("mark 99");
        command.processCommand("todo");
        command.processCommand("deadline missing date");
        command.processCommand("event missing times");
        command.processCommand("delete invalid");
        command.processCommand("unknown");
        command.processCommand("list");
        command.processCommand("help");
        command.processCommand("bye");
        expectLines(snapshot.toArray(String[]::new));

        TasksList restored = storage.loadTasks();
        if (restored.size() != 2) {
            throw new AssertionError("Loading did not preserve deletion");
        }
        // Verify the task objects, including dates and completion status.
        if (!(restored.get(0) instanceof Deadline deadline)
                || !deadline.getDescription().equals("return book")
                || !deadline.getBy().equals("June 6th")
                || !deadline.getStatusIcon().equals(" ")) {
            throw new AssertionError("Loading did not restore the deadline and its unmarked status");
        }
        if (!(restored.get(1) instanceof Event event)
                || !event.getDescription().equals("meeting")
                || !event.getFrom().equals("2pm")
                || !event.getTo().equals("4pm")
                || !event.getStatusIcon().equals("X")) {
            throw new AssertionError("Loading did not restore the event and its marked status");
        }
        expectLines(snapshot.toArray(String[]::new));

        String input = "todo console task\nmark 3\nlist\nbye\n";
        String transcript = runConsole(input);
        Files.writeString(Path.of("ui-transcript.txt"), transcript);
        for (String expected : List.of("1.   [D][ ]return book (by: June 6th)",
                "2.   [E][X]meeting (from: 2pm to: 4pm)",
                "[T][X]console task", "Bye! See you soon")) {
            if (!transcript.contains(expected)) {
                throw new AssertionError("Missing console output: " + expected);
            }
        }
        expectLines("D\t0\treturn book\tJune 6th", "E\t1\tmeeting\t2pm\t4pm", "T\t1\tconsole task");

        String restart = runConsole("bye\n");
        if (!restart.contains("3.   [T][X]console task")) {
            throw new AssertionError("Startup must list tasks restored from the previous session");
        }
        if (restart.contains("Task added:") || restart.contains("Marked as")) {
            throw new AssertionError("Startup must load silently instead of replaying commands");
        }
        expectLines("D\t0\treturn book\tJune 6th", "E\t1\tmeeting\t2pm\t4pm", "T\t1\tconsole task");
        runConsole("delete 1\ndelete 1\ndelete 1\nbye\n");
        expectLines();
        if (!storage.loadTasks().isEmpty() || !runConsole("bye\n").contains("Add tasks first")) {
            throw new AssertionError("Deleting every task must remain empty after restart");
        }

        checkSpecialCharacters(storage);
        checkInvalidSave();
        System.out.println("PASS: snapshots, deletion, restart, plain text, and invalid-save checks");
    }

    /** Checks tab normalization and preserves Unicode, backslashes, and slash dates as plain text. */
    private static void checkSpecialCharacters(Storage storage) throws Exception {
        TasksList tasks = new TasksList();
        Parser parser = new Parser();
        String description = "café | notes\\today\\new\\raw\\q part two";
        tasks.add(parser.parseTask("todo café | notes\\today\\new\\raw\\q\tpart two"));
        tasks.add(parser.parseTask("deadline return\tbook /by 29/09/2026\t5pm"));
        tasks.add(parser.parseTask("event road\ttrip /from 29/09/2026\t9am /to 30/09/2026\t5pm"));
        storage.saveTasks(tasks);
        expectLines("T\t0\t" + description, "D\t0\treturn book\t29/09/2026 5pm",
                "E\t0\troad trip\t29/09/2026 9am\t30/09/2026 5pm");
        TasksList restored = storage.loadTasks();
        if (Files.readAllLines(SAVE_FILE).size() != 3
                || !restored.get(0).getDescription().equals(description)
                || !((Deadline) restored.get(1)).getBy().equals("29/09/2026 5pm")
                || !((Event) restored.get(2)).getTo().equals("30/09/2026 5pm")) {
            throw new AssertionError("Task fields did not survive saving and loading");
        }
    }

    /** Ensures a corrupt snapshot is reported and never overwritten by an incomplete session. */
    private static void checkInvalidSave() throws Exception {
        for (String invalid : List.of("T\t2\ttask", "E\t0\tmissing dates", "Q\t0\ttask",
                "T\t0\t", "T\t0\ttask\textra")) {
            Files.write(SAVE_FILE, List.of("T\t0\tkeep", invalid));
            String before = Files.readString(SAVE_FILE);
            String output = runConsole("todo overwrite\nbye\n");
            if (!output.contains("Could not load tasks:") || !Files.readString(SAVE_FILE).equals(before)) {
                throw new AssertionError("Invalid snapshot was not protected");
            }
        }
    }

    /** Runs a console session while capturing output and restoring standard streams. */
    private static String runConsole(String input) {
        var originalIn = System.in;
        var originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            Clanker.main(new String[0]);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
        return output.toString(StandardCharsets.UTF_8);
    }

    /** Checks that the file contains only the expected current tasks. */
    private static void expectLines(String... expected) throws Exception {
        List<String> actual = Files.readAllLines(SAVE_FILE);
        if (!actual.equals(List.of(expected))) {
            throw new AssertionError("Unexpected task snapshot: " + actual);
        }
    }
}
