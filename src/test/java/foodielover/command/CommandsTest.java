package foodielover.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import foodielover.ConsoleCapture;
import foodielover.FoodieloverException;
import foodielover.storage.Storage;
import foodielover.task.Deadline;
import foodielover.task.Event;
import foodielover.task.TaskList;
import foodielover.task.Todo;
import foodielover.ui.Ui;

/**
 * Tests every Command subclass by executing it against a real TaskList, a real Ui whose output is
 * captured, and a Storage that points at a temporary file.
 */
public class CommandsTest {

    @TempDir
    Path tempDir;

    private Path dataFile;
    private Storage storage;
    private TaskList tasks;

    @BeforeEach
    public void setUp() {
        dataFile = tempDir.resolve("tasks.txt");
        storage = new Storage(dataFile.toString());
        tasks = new TaskList();
    }

    private String fileText() throws IOException {
        return Files.readString(dataFile).replace("\r\n", "\n");
    }

    // ---------------------------------------------------------------- AddCommand

    @Test
    public void add_validTask_addsSavesAndConfirms() throws Exception {
        try (ConsoleCapture console = new ConsoleCapture("")) {
            new AddCommand(new Todo("read book")).execute(tasks, new Ui(), storage);
            assertEquals(1, tasks.size());
            assertTrue(fileText().contains("T | 0 | read book"));
            assertTrue(console.output().contains("Got it. I've added this task:"));
            assertTrue(console.output().contains("Now you have 1 task in the list."));
        }
    }

    @Test
    public void add_saveFails_taskStillAddedAndErrorShown() throws Exception {
        // The parent of the data file is a regular file, so writing must fail.
        Path blocker = tempDir.resolve("blocker");
        Files.writeString(blocker, "not a folder");
        Storage brokenStorage = new Storage(blocker.resolve("tasks.txt").toString());
        try (ConsoleCapture console = new ConsoleCapture("")) {
            new AddCommand(new Todo("read book")).execute(tasks, new Ui(), brokenStorage);
            assertEquals(1, tasks.size());
            assertTrue(console.output().contains("An error occurred while saving tasks"));
        }
    }

    @Test
    public void add_isNotExitCommand() {
        assertFalse(new AddCommand(new Todo("read book")).isExit());
    }

    // ---------------------------------------------------------------- DeleteCommand

    @Test
    public void delete_validIndex_removesSavesAndConfirms() throws Exception {
        tasks.add(new Todo("first"));
        tasks.add(new Todo("second"));
        try (ConsoleCapture console = new ConsoleCapture("")) {
            new DeleteCommand(0).execute(tasks, new Ui(), storage);
            assertEquals(1, tasks.size());
            assertEquals("second", tasks.get(0).getDescription());
            assertTrue(fileText().contains("second"));
            assertFalse(fileText().contains("first"));
            assertTrue(console.output().contains("Noted. I've removed this task:"));
        }
    }

    @Test
    public void delete_lastTask_leavesEmptyList() throws Exception {
        tasks.add(new Todo("only"));
        try (ConsoleCapture console = new ConsoleCapture("")) {
            new DeleteCommand(0).execute(tasks, new Ui(), storage);
            assertTrue(tasks.isEmpty());
        }
    }

    @Test
    public void delete_emptyList_throwsFriendlyException() {
        FoodieloverException exception = assertThrows(FoodieloverException.class, () ->
                new DeleteCommand(0).execute(tasks, new Ui(), storage));
        assertTrue(exception.getMessage().contains("empty"));
    }

    @Test
    public void delete_indexOutOfRange_throwsExceptionWithValidRange() {
        tasks.add(new Todo("first"));
        tasks.add(new Todo("second"));
        FoodieloverException tooBig = assertThrows(FoodieloverException.class, () ->
                new DeleteCommand(5).execute(tasks, new Ui(), storage));
        assertTrue(tooBig.getMessage().contains("from 1 to 2"));
        FoodieloverException negative = assertThrows(FoodieloverException.class, () ->
                new DeleteCommand(-1).execute(tasks, new Ui(), storage));
        assertTrue(negative.getMessage().contains("from 1 to 2"));
        assertEquals(2, tasks.size());
    }

    // ---------------------------------------------------------------- MarkCommand

    @Test
    public void mark_validIndex_marksSavesAndConfirms() throws Exception {
        tasks.add(new Todo("read book"));
        try (ConsoleCapture console = new ConsoleCapture("")) {
            new MarkCommand(0).execute(tasks, new Ui(), storage);
            assertTrue(tasks.get(0).toString().contains("[X]"));
            assertTrue(fileText().contains("T | 1 | read book"));
            assertTrue(console.output().contains("Nice! I've marked this task as done:"));
        }
    }

    @Test
    public void mark_emptyList_throwsFriendlyException() {
        FoodieloverException exception = assertThrows(FoodieloverException.class, () ->
                new MarkCommand(0).execute(tasks, new Ui(), storage));
        assertTrue(exception.getMessage().contains("empty"));
    }

    @Test
    public void mark_indexOutOfRange_throwsException() {
        tasks.add(new Todo("read book"));
        FoodieloverException exception = assertThrows(FoodieloverException.class, () ->
                new MarkCommand(3).execute(tasks, new Ui(), storage));
        assertTrue(exception.getMessage().contains("from 1 to 1"));
        assertThrows(FoodieloverException.class, () ->
                new MarkCommand(-1).execute(tasks, new Ui(), storage));
    }

    // ---------------------------------------------------------------- UnmarkCommand

    @Test
    public void unmark_validIndex_unmarksSavesAndConfirms() throws Exception {
        Todo todo = new Todo("read book");
        todo.markAsDone();
        tasks.add(todo);
        try (ConsoleCapture console = new ConsoleCapture("")) {
            new UnmarkCommand(0).execute(tasks, new Ui(), storage);
            assertTrue(tasks.get(0).toString().contains("[ ]"));
            assertTrue(fileText().contains("T | 0 | read book"));
            assertTrue(console.output().contains("OK, I've marked this task as not done yet:"));
        }
    }

    @Test
    public void unmark_emptyList_throwsFriendlyException() {
        FoodieloverException exception = assertThrows(FoodieloverException.class, () ->
                new UnmarkCommand(0).execute(tasks, new Ui(), storage));
        assertTrue(exception.getMessage().contains("empty"));
    }

    @Test
    public void unmark_indexOutOfRange_throwsException() {
        tasks.add(new Todo("read book"));
        FoodieloverException exception = assertThrows(FoodieloverException.class, () ->
                new UnmarkCommand(3).execute(tasks, new Ui(), storage));
        assertTrue(exception.getMessage().contains("from 1 to 1"));
        assertThrows(FoodieloverException.class, () ->
                new UnmarkCommand(-1).execute(tasks, new Ui(), storage));
    }

    // ---------------------------------------------------------------- FindCommand

    @Test
    public void find_matchingKeyword_listsOnlyMatches() throws Exception {
        tasks.add(new Todo("read book"));
        tasks.add(new Todo("write essay"));
        try (ConsoleCapture console = new ConsoleCapture("")) {
            new FindCommand("BOOK").execute(tasks, new Ui(), storage);
            String output = console.output();
            assertTrue(output.contains("Here are the matching tasks in your list:"));
            assertTrue(output.contains("read book"));
            assertFalse(output.contains("write essay"));
        }
    }

    @Test
    public void find_noMatch_printsNoMatchMessage() throws Exception {
        tasks.add(new Todo("read book"));
        try (ConsoleCapture console = new ConsoleCapture("")) {
            new FindCommand("zebra").execute(tasks, new Ui(), storage);
            assertTrue(console.output().contains("No matching tasks found in your list."));
        }
    }

    // ---------------------------------------------------------------- ListCommand

    @Test
    public void list_printsEveryTaskNumbered() throws Exception {
        tasks.add(new Todo("read book"));
        tasks.add(new Todo("write essay"));
        try (ConsoleCapture console = new ConsoleCapture("")) {
            new ListCommand().execute(tasks, new Ui(), storage);
            String output = console.output();
            assertTrue(output.contains("1.[T][ ] read book"));
            assertTrue(output.contains("2.[T][ ] write essay"));
        }
    }

    // ---------------------------------------------------------------- DateFilterCommand

    @Test
    public void dateFilter_matchingDeadlineAndEvent_listsBoth() throws Exception {
        tasks.add(new Deadline("essay", "2026-10-15"));
        tasks.add(new Event("camp", "2026-10-14", "2026-10-16"));
        tasks.add(new Todo("unrelated"));
        try (ConsoleCapture console = new ConsoleCapture("")) {
            new DateFilterCommand(LocalDate.of(2026, 10, 15)).execute(tasks, new Ui(), storage);
            String output = console.output();
            assertTrue(output.contains("Here are the tasks occurring on Oct 15 2026:"));
            assertTrue(output.contains("essay"));
            assertTrue(output.contains("camp"));
            assertFalse(output.contains("unrelated"));
        }
    }

    @Test
    public void dateFilter_noMatch_printsNoTasksMessage() throws Exception {
        tasks.add(new Deadline("essay", "2026-10-15"));
        try (ConsoleCapture console = new ConsoleCapture("")) {
            new DateFilterCommand(LocalDate.of(2030, 1, 1)).execute(tasks, new Ui(), storage);
            assertTrue(console.output().contains("No tasks found occurring on Jan 01 2030."));
        }
    }

    // ---------------------------------------------------------------- ExitCommand

    @Test
    public void exit_isExitAndSaysGoodbye() {
        try (ConsoleCapture console = new ConsoleCapture("")) {
            ExitCommand exit = new ExitCommand();
            exit.execute(tasks, new Ui(), storage);
            assertTrue(exit.isExit());
            assertTrue(console.output().contains("Bye. Hope to see you again soon!"));
        }
    }

    @Test
    public void nonExitCommands_reportNotExit() {
        assertFalse(new ListCommand().isExit());
        assertFalse(new FindCommand("x").isExit());
        assertFalse(new DeleteCommand(0).isExit());
    }

    // ---------------------------------------------------------------- boundary checks

    @Test
    public void taskNumberCommands_indexEqualToSize_isRejectedNotCrashed() {
        tasks.add(new Todo("first"));
        tasks.add(new Todo("second"));
        // Index 2 is the first number past the end of a two task list (the user typed 3).
        assertThrows(FoodieloverException.class, () -> new DeleteCommand(2).execute(tasks, new Ui(), storage));
        assertThrows(FoodieloverException.class, () -> new MarkCommand(2).execute(tasks, new Ui(), storage));
        assertThrows(FoodieloverException.class, () -> new UnmarkCommand(2).execute(tasks, new Ui(), storage));
        assertEquals(2, tasks.size());
    }

    @Test
    public void taskNumberCommands_lastValidIndex_works() throws Exception {
        tasks.add(new Todo("first"));
        tasks.add(new Todo("second"));
        try (ConsoleCapture console = new ConsoleCapture("")) {
            Ui ui = new Ui();
            new MarkCommand(1).execute(tasks, ui, storage);
            new UnmarkCommand(1).execute(tasks, ui, storage);
            new DeleteCommand(1).execute(tasks, ui, storage);
            assertEquals(1, tasks.size());
            assertEquals("first", tasks.get(0).getDescription());
        }
    }
}
