package wenwen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import wenwen.storage.Storage;
import wenwen.task.Task;
import wenwen.ui.Ui;

class WenwenTest {
    @TempDir
    private Path temporaryFolder;

    @Test
    void run_typicalSession_updatesStorageAndShowsResponses() throws Exception {
        String commands = String.join("\n",
                "todo read book",
                "deadline return book /by 2026-10-02",
                "mark 1",
                "find BOOK",
                "on 2026-10-02",
                "list",
                "delete 2",
                "bye") + "\n";
        ByteArrayInputStream input = new ByteArrayInputStream(commands.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Path dataFile = temporaryFolder.resolve("data/wenwen.txt");
        Storage storage = new Storage(dataFile);
        Ui ui = new Ui(input, new PrintStream(output, true, StandardCharsets.UTF_8));

        new Wenwen(storage, ui).run();

        String transcript = output.toString(StandardCharsets.UTF_8);
        assertTrue(transcript.contains("Got it. I've added this task:"));
        assertTrue(transcript.contains("[T][X] read book"));
        assertTrue(transcript.contains("Here are the matching tasks in your list:"));
        assertTrue(transcript.contains("Here are the tasks on Oct 02 2026:"));
        assertTrue(transcript.contains("[D][ ] return book (by: Oct 02 2026)"));
        assertTrue(transcript.contains("Noted. I've removed this task:"));
        assertTrue(transcript.contains("Bye. Hope to see you again soon!"));

        List<Task> savedTasks = storage.loadTasks();
        assertEquals(1, savedTasks.size());
        assertEquals("read book", savedTasks.get(0).getDescription());
        assertTrue(savedTasks.get(0).isDone());
    }
}
