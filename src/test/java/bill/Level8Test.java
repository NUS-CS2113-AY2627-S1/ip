package bill;

import bill.storage.Storage;
import bill.task.Deadline;
import bill.task.Task;
import bill.ui.Ui;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Checks ISO deadline dates through the command flow and saved task file.
 */
class Level8Test {
    @TempDir
    Path temporaryDirectory;

    @Test
    void isoDeadlineDisplaysInFriendlyFormatAndReloadsFromItsCanonicalDate() throws Exception {
        Path dataFile = temporaryDirectory.resolve("tasks.txt");
        Storage storage = new Storage(dataFile);

        String firstSession = runBill(storage, "deadline return book /by 2019-10-15\nlist\nbye\n");

        assertTrue(firstSession.contains("[D][ ] return book (by: Oct 15 2019)"));
        List<Task> savedTasks = storage.loadTasks();
        assertEquals(1, savedTasks.size());
        Deadline savedDeadline = assertInstanceOf(Deadline.class, savedTasks.get(0));
        assertEquals("2019-10-15", savedDeadline.getBy());
        assertEquals(LocalDate.of(2019, 10, 15), savedDeadline.getByDate().orElseThrow());
        assertTrue(Files.readString(dataFile).contains("2019-10-15"));
        assertFalse(Files.readString(dataFile).contains("Oct 15 2019"));

        String secondSession = runBill(storage, "list\nbye\n");
        assertTrue(secondSession.contains("1.[D][ ] return book (by: Oct 15 2019)"));
    }

    @Test
    void impossibleIsoDateShowsAnErrorWithoutAddingOrSavingATask() throws Exception {
        Storage storage = new Storage(temporaryDirectory.resolve("tasks.txt"));

        String output = runBill(storage, String.join("\n",
                "todo keep this",
                "deadline impossible /by 2026-02-30",
                "list",
                "bye") + "\n");

        assertTrue(output.contains("Hmm, I couldn't do that:"));
        assertTrue(output.contains("valid date"));
        assertTrue(output.contains("1.[T][ ] keep this"));
        assertFalse(output.contains("[D][ ] impossible"));
        List<Task> savedTasks = storage.loadTasks();
        assertEquals(1, savedTasks.size());
        assertEquals("[T][ ] keep this", savedTasks.get(0).toString());
    }

    @Test
    void leapDayIsAcceptedButAnInvalidNonLeapDayIsRejected() throws Exception {
        Storage storage = new Storage(temporaryDirectory.resolve("tasks.txt"));

        String output = runBill(storage, String.join("\n",
                "deadline valid leap day /by 2024-02-29",
                "deadline invalid leap day /by 2023-02-29",
                "list",
                "bye") + "\n");

        assertTrue(output.contains("[D][ ] valid leap day (by: Feb 29 2024)"));
        assertTrue(output.contains("valid date"));
        assertFalse(output.contains("[D][ ] invalid leap day"));
        List<Task> savedTasks = storage.loadTasks();
        assertEquals(1, savedTasks.size());
        assertEquals("2024-02-29", assertInstanceOf(Deadline.class, savedTasks.get(0)).getBy());
    }

    @Test
    void existingFreeFormDeadlinesAndEventTimesRemainUnchanged() throws Exception {
        Storage storage = new Storage(temporaryDirectory.resolve("tasks.txt"));

        String output = runBill(storage, String.join("\n",
                "deadline weekly quiz /by Monday 9pm",
                "deadline evening call /by 2026-10-02 18:00",
                "event lecture /from Friday 4pm /to Friday 6pm",
                "list",
                "bye") + "\n");

        assertTrue(output.contains("1.[D][ ] weekly quiz (by: Monday 9pm)"));
        assertTrue(output.contains("2.[D][ ] evening call (by: 2026-10-02 18:00)"));
        assertTrue(output.contains("3.[E][ ] lecture (from: Friday 4pm to: Friday 6pm)"));
        List<Task> savedTasks = storage.loadTasks();
        Deadline oldDeadline = assertInstanceOf(Deadline.class, savedTasks.get(0));
        assertEquals("Monday 9pm", oldDeadline.getBy());
        assertTrue(oldDeadline.getByDate().isEmpty());
        Deadline oldDateTime = assertInstanceOf(Deadline.class, savedTasks.get(1));
        assertEquals("2026-10-02 18:00", oldDateTime.getBy());
        assertTrue(oldDateTime.getByDate().isEmpty());
        assertEquals("[E][ ] lecture (from: Friday 4pm to: Friday 6pm)", savedTasks.get(2).toString());
    }

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
