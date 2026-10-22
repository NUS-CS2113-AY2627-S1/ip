package bill;

import bill.storage.Storage;
import bill.task.Task;
import bill.task.Todo;
import bill.ui.Ui;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Checks Bill's command flow through its user interface and a temporary data file.
 */
class BillTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void completeWorkflowKeepsTaskOrderAndSavesFinalState() throws Exception {
        Storage storage = new Storage(temporaryDirectory.resolve("tasks.txt"));
        String output = runBill(storage, String.join("\n",
                "todo read a book",
                "deadline weekly quiz /by Monday 9pm",
                "event lecture /from Friday 4pm /to Friday 6pm",
                "list",
                "mark 2",
                "stats",
                "unmark 2",
                "delete 1",
                "list",
                "help",
                "bye",
                "todo this must not run") + "\n");

        assertTrue(output.contains("1.[T][ ] read a book"));
        assertTrue(output.contains("2.[D][ ] weekly quiz (by: Monday 9pm)"));
        assertTrue(output.contains("3.[E][ ] lecture (from: Friday 4pm to: Friday 6pm)"));
        assertTrue(output.contains("[D][X] weekly quiz (by: Monday 9pm)"));
        assertTrue(output.contains("Task stats: 3 total, 1 done, 2 remaining."));
        assertTrue(output.contains("1.[D][ ] weekly quiz (by: Monday 9pm)"));
        assertTrue(output.contains("2.[E][ ] lecture (from: Friday 4pm to: Friday 6pm)"));
        assertTrue(output.contains("Here are the commands I understand:"));
        assertTrue(output.contains("Bye. Have a good day mate!"));
        assertFalse(output.contains("this must not run"));

        List<Task> savedTasks = storage.loadTasks();
        assertEquals(2, savedTasks.size());
        assertEquals("[D][ ] weekly quiz (by: Monday 9pm)", savedTasks.get(0).toString());
        assertEquals("[E][ ] lecture (from: Friday 4pm to: Friday 6pm)", savedTasks.get(1).toString());
    }

    @Test
    void invalidCommandsLeaveExistingTaskUntouched() throws Exception {
        Storage storage = new Storage(temporaryDirectory.resolve("tasks.txt"));
        String output = runBill(storage, String.join("\n",
                "todo keep this",
                "mark",
                "mark abc",
                "mark 2",
                "delete 0",
                "unknown command",
                "todo",
                "list",
                "bye") + "\n");

        assertTrue(output.contains("Tell me which task number to mark. Try: mark 1"));
        assertTrue(output.contains("'abc' is not a valid task number. Try: mark 1"));
        assertTrue(output.contains("Task 2 does not exist. Choose a number from 1 to 1."));
        assertTrue(output.contains("Task 0 does not exist. Choose a number from 1 to 1."));
        assertTrue(output.contains("I don't recognise that command."));
        assertTrue(output.contains("A todo needs a description."));
        assertTrue(output.contains("1.[T][ ] keep this"));

        List<Task> savedTasks = storage.loadTasks();
        assertEquals(1, savedTasks.size());
        assertEquals("[T][ ] keep this", savedTasks.get(0).toString());
    }

    @Test
    void startupLoadsSavedTasksAndEndOfInputExitsCleanly() throws Exception {
        Storage storage = new Storage(temporaryDirectory.resolve("tasks.txt"));
        storage.saveTasks(List.of(new Todo("saved before restart", true)));

        String output = runBill(storage, "list\n");

        assertTrue(output.contains("1.[T][X] saved before restart"));
        assertTrue(output.contains("Bye. Have a good day mate!"));
        assertEquals(1, storage.loadTasks().size());
    }

    @Test
    void unreadableSavedTasksAreNotOverwrittenOnStartup() throws Exception {
        Path dataFile = temporaryDirectory.resolve("tasks.txt");
        Files.writeString(dataFile, "not a valid saved task\n");
        Storage storage = new Storage(dataFile);

        String output = runBill(storage, "list\nbye\n");

        assertTrue(output.contains("Saved task line 1 is incomplete."));
        assertTrue(output.contains("I'll start with an empty task list for this session."));
        assertTrue(output.contains("Your task list is empty."));
        assertEquals("not a valid saved task\n", Files.readString(dataFile));
    }

    /**
     * Runs one complete conversation without using the real console or task file.
     */
    private String runBill(Storage storage, String input) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (PrintStream printStream = new PrintStream(outputStream, true, StandardCharsets.UTF_8);
                Ui ui = new Ui(inputStream, printStream)) {
            new Bill(ui, storage).run();
        }
        return outputStream.toString(StandardCharsets.UTF_8);
    }
}
