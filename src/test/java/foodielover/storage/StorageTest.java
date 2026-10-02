package foodielover.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import foodielover.task.Deadline;
import foodielover.task.Event;
import foodielover.task.Task;
import foodielover.task.Todo;

public class StorageTest {

    @Test
    public void saveAndLoad_validTasks_roundTripSuccess(@TempDir Path tempDir) throws IOException {
        Path filePath = tempDir.resolve("tasks.txt");
        Storage storage = new Storage(filePath.toString());

        List<Task> tasksToSave = new ArrayList<>();
        tasksToSave.add(new Todo("read book"));
        tasksToSave.add(new Deadline("submit assignment", "2026-10-15"));
        tasksToSave.add(new Deadline("project deadline", "2026-10-15 1800"));
        tasksToSave.add(new Event("career fair", "2026-10-15 0900", "2026-10-15 1700"));
        tasksToSave.get(0).markAsDone();

        storage.save(tasksToSave);

        Storage reloadStorage = new Storage(filePath.toString());
        List<Task> loadedTasks = reloadStorage.load();

        assertEquals(4, loadedTasks.size());
        assertEquals("X", loadedTasks.get(0).getStatusIcon());
        assertEquals("read book", loadedTasks.get(0).getDescription());
        assertEquals(" ", loadedTasks.get(1).getStatusIcon());
        assertEquals("submit assignment", loadedTasks.get(1).getDescription());
        assertEquals("project deadline", loadedTasks.get(2).getDescription());
        assertEquals("career fair", loadedTasks.get(3).getDescription());
        assertTrue(reloadStorage.getCorruptedLines().isEmpty());
    }

    @Test
    public void load_corruptedLines_skipsAndRecordsWarnings(@TempDir Path tempDir) throws IOException {
        Path filePath = tempDir.resolve("corrupted_tasks.txt");
        List<String> rawLines = List.of(
                "T | 0 | valid todo",
                "INVALID | 0 | missing fields",
                "D | 1",
                "T | 1 | another valid todo"
        );
        Files.write(filePath, rawLines);

        Storage storage = new Storage(filePath.toString());
        List<Task> loadedTasks = storage.load();

        assertEquals(2, loadedTasks.size());
        assertEquals("valid todo", loadedTasks.get(0).getDescription());
        assertEquals("another valid todo", loadedTasks.get(1).getDescription());

        List<String> corrupted = storage.getCorruptedLines();
        assertEquals(2, corrupted.size());
        assertTrue(corrupted.contains("INVALID | 0 | missing fields"));
        assertTrue(corrupted.contains("D | 1"));
    }

    @Test
    public void save_multipleTimesInSession_backupPreservesInitialFile(@TempDir Path tempDir) throws IOException {
        Path filePath = tempDir.resolve("session_tasks.txt");
        Path backupPath = tempDir.resolve("session_tasks.txt.bak");

        List<String> initialLines = List.of(
                "T | 0 | initial task",
                "CORRUPTED_ENTRY"
        );
        Files.write(filePath, initialLines);

        Storage storage = new Storage(filePath.toString());
        List<Task> tasks = storage.load();
        assertEquals(1, tasks.size());
        assertEquals(1, storage.getCorruptedLines().size());

        // First save during session
        tasks.add(new Todo("second task"));
        storage.save(tasks);

        assertTrue(Files.exists(backupPath));
        List<String> backupLines = Files.readAllLines(backupPath);
        assertTrue(backupLines.contains("CORRUPTED_ENTRY"), "Backup should contain corrupted entry from initial file");

        // Second save during same session
        tasks.add(new Todo("third task"));
        storage.save(tasks);

        List<String> backupLinesAfterSecondSave = Files.readAllLines(backupPath);
        assertTrue(backupLinesAfterSecondSave.contains("CORRUPTED_ENTRY"),
                "Backup should still preserve initial state and not be overwritten by second save");
        assertFalse(backupLinesAfterSecondSave.contains("T | 0 | second task"),
                "Backup must not contain intermediate session saves");
    }
}
