package foodielover;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * End to end tests that drive the whole application through simulated keyboard input.
 */
public class FoodieloverTest {

    @TempDir
    Path tempDir;

    private String fileText(Path file) throws IOException {
        return Files.readString(file).replace("\r\n", "\n");
    }

    @Test
    public void run_fullSession_handlesEveryCommandAndSaves() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        String input = String.join("\n",
                "todo read book",
                "deadline essay /by 2026-10-15",
                "event camp /from 2026-10-14 /to 2026-10-16",
                "mark 1",
                "list",
                "unmark 1",
                "find book",
                "on 2026-10-15",
                "delete 1",
                "bogus command",
                "delete 99",
                "bye",
                "todo never reached",
                "");
        try (ConsoleCapture console = new ConsoleCapture(input)) {
            new Foodielover(file.toString()).run();
            String output = console.output();
            assertTrue(output.contains("Hello! I'm Foodielover."));
            assertTrue(output.contains("Got it. I've added this task:"));
            assertTrue(output.contains("Nice! I've marked this task as done:"));
            assertTrue(output.contains("Here are the tasks in your list:"));
            assertTrue(output.contains("OK, I've marked this task as not done yet:"));
            assertTrue(output.contains("Here are the matching tasks in your list:"));
            assertTrue(output.contains("Here are the tasks occurring on Oct 15 2026:"));
            assertTrue(output.contains("Noted. I've removed this task:"));
            assertTrue(output.contains("This is not a valid input"));
            assertTrue(output.contains("This is not a valid task number"));
            assertTrue(output.contains("Bye. Hope to see you again soon!"));
            assertFalse(output.contains("never reached"));
        }
        String saved = fileText(file);
        assertFalse(saved.contains("read book"));
        assertTrue(saved.contains("D | 0 | essay"));
        assertTrue(saved.contains("E | 0 | camp"));
    }

    @Test
    public void run_tasksPersistAcrossSessions() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        try (ConsoleCapture first = new ConsoleCapture("todo read book\nmark 1\nbye\n")) {
            new Foodielover(file.toString()).run();
        }
        try (ConsoleCapture second = new ConsoleCapture("list\nbye\n")) {
            new Foodielover(file.toString()).run();
            assertTrue(second.output().contains("1.[T][X] read book"));
        }
    }

    @Test
    public void run_endsCleanlyWhenInputRunsOut() {
        Path file = tempDir.resolve("tasks.txt");
        try (ConsoleCapture console = new ConsoleCapture("todo read book\n")) {
            new Foodielover(file.toString()).run();
            assertTrue(console.output().contains("Got it. I've added this task:"));
            assertFalse(console.output().contains("Bye. Hope to see you again soon!"));
        }
    }

    @Test
    public void constructor_corruptedFile_warnsAndKeepsGoodTasks() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        Files.writeString(file, "T | 0 | good task\nGARBAGE LINE\n");
        try (ConsoleCapture console = new ConsoleCapture("list\nbye\n")) {
            new Foodielover(file.toString()).run();
            String output = console.output();
            assertTrue(output.contains("Skipping corrupted task entry"));
            assertTrue(output.contains("GARBAGE LINE"));
            assertTrue(output.contains("1.[T][ ] good task"));
        }
    }

    @Test
    public void defaultConstructor_andMain_runUsingDefaultFileWithoutWriting() {
        // Only "bye" is typed, so nothing is saved and the default data file is never modified.
        try (ConsoleCapture console = new ConsoleCapture("bye\n")) {
            new Foodielover().run();
            assertTrue(console.output().contains("Bye. Hope to see you again soon!"));
        }
        try (ConsoleCapture console = new ConsoleCapture("bye\n")) {
            Foodielover.main(new String[0]);
            assertEquals(1, console.output().split("Bye. Hope to see you again soon!", -1).length - 1);
        }
    }
}
