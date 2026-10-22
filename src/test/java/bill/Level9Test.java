package bill;

import bill.storage.Storage;
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
import java.util.Locale;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Checks the find command through Bill's console and saved task file.
 */
class Level9Test {
    @TempDir
    Path temporaryDirectory;

    @Test
    void findMatchesDescriptionRegardlessOfCaseAndRenumbersWithoutChangingTasks() throws Exception {
        Path dataFile = temporaryDirectory.resolve("tasks.txt");
        Storage storage = new Storage(dataFile);
        storage.saveTasks(List.of(
                new Todo("get milk"),
                new Todo("Read BOOK"),
                new Todo("borrow book", true),
                new Todo("cook lunch")));
        String savedBeforeSearch = Files.readString(dataFile);

        String output = runBill(storage, "find bOoK\nbye\n");

        assertTrue(output.contains("1.[T][ ] Read BOOK"));
        assertTrue(output.contains("2.[T][X] borrow book"));
        assertFalse(output.contains("get milk"));
        assertFalse(output.contains("cook lunch"));
        assertFalse(output.contains("3.[T]"));
        assertEquals(savedBeforeSearch, Files.readString(dataFile));
    }

    @Test
    void findDoesNotMatchDeadlineDateWhenDescriptionDoesNotMatch() throws Exception {
        Storage storage = new Storage(temporaryDirectory.resolve("tasks.txt"));
        runBill(storage, "deadline submit report /by 2026-10-02\nbye\n");
        assertEquals(1, storage.loadTasks().size());

        String output = runBill(storage, "find 2026\nbye\n");

        assertFalse(output.contains("submit report"));
        assertTrue(indicatesNoMatches(output));
    }

    @Test
    void findWithNoMatchesExplainsTheResult() throws Exception {
        Storage storage = new Storage(temporaryDirectory.resolve("tasks.txt"));
        storage.saveTasks(List.of(new Todo("read a book")));

        String output = runBill(storage, "find bicycle\nbye\n");

        assertFalse(output.contains("1.[T]"));
        assertTrue(indicatesNoMatches(output));
    }

    @Test
    void findRequiresANonBlankKeyword() throws Exception {
        Path dataFile = temporaryDirectory.resolve("tasks.txt");
        String output = runBill(new Storage(dataFile), "find\nfind   \nbye\n");

        assertEquals(2, output.split("Hmm, I couldn't do that:", -1).length - 1);
        String lowerCaseOutput = output.toLowerCase(Locale.ROOT);
        assertTrue(lowerCaseOutput.contains("what to find") || lowerCaseOutput.contains("keyword")
                || lowerCaseOutput.contains("search term"));
        assertFalse(Files.exists(dataFile));
    }

    /**
     * Accepts equivalent friendly explanations without depending on one exact sentence.
     */
    private boolean indicatesNoMatches(String output) {
        Pattern explanation = Pattern.compile(
                "no\\s+(?:matching\\s+)?tasks|no\\s+matches|couldn.t\\s+find|nothing\\s+(?:matches|found)",
                Pattern.CASE_INSENSITIVE);
        return explanation.matcher(output).find();
    }

    /**
     * Runs a conversation using temporary input and output streams.
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
