package foodielover.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import foodielover.task.Task;
import foodielover.task.TaskList;
import foodielover.task.Todo;

public class StorageEdgeCasesTest {

    @TempDir
    Path tempDir;

    @Test
    public void load_missingFile_returnsEmptyList() {
        Storage storage = new Storage(tempDir.resolve("does-not-exist.txt").toString());
        assertTrue(storage.load().isEmpty());
        assertTrue(storage.getCorruptedLines().isEmpty());
    }

    @Test
    public void load_pathIsADirectory_returnsEmptyList() throws IOException {
        Path directory = Files.createDirectory(tempDir.resolve("folder"));
        Storage storage = new Storage(directory.toString());
        assertTrue(storage.load().isEmpty());
    }

    @Test
    public void load_blankLines_areIgnoredNotCorrupted() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        Files.writeString(file, "\n   \nT | 0 | read book\n\n");
        Storage storage = new Storage(file.toString());
        assertEquals(1, storage.load().size());
        assertTrue(storage.getCorruptedLines().isEmpty());
    }

    @Test
    public void load_everyKindOfMalformedLine_isRecordedAsCorrupted() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        String[] badLines = {
            "T | 0",                          // too few fields
            "T | 2 | bad completion flag",    // flag is not 0 or 1
            "T | 0 |",                        // empty description
            "D | 0 | essay",                  // deadline without a due date
            "D | 0 | essay |",                // deadline with an empty due date
            "E | 0 | camp | 2026-12-01",      // event without an end
            "E | 0 | camp | | 2026-12-03",    // event with an empty start
            "E | 0 | camp | 2026-12-01 |",    // event with an empty end
            "X | 0 | unknown type"            // unknown task type
        };
        Files.writeString(file, String.join("\n", badLines) + "\n");
        Storage storage = new Storage(file.toString());
        assertTrue(storage.load().isEmpty());
        assertEquals(badLines.length, storage.getCorruptedLines().size());
    }

    @Test
    public void load_doneFlagOne_marksTaskAsDone() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        Files.writeString(file, "T | 1 | read book\n");
        List<Task> loaded = new Storage(file.toString()).load();
        assertTrue(loaded.get(0).toString().contains("[X]"));
    }

    @Test
    public void load_calledTwice_doesNotDuplicateCorruptedLines() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        Files.writeString(file, "GARBAGE\n");
        Storage storage = new Storage(file.toString());
        storage.load();
        storage.load();
        assertEquals(1, storage.getCorruptedLines().size());
    }

    @Test
    public void save_missingParentFolders_areCreated() throws IOException {
        Path file = tempDir.resolve("nested").resolve("deeper").resolve("tasks.txt");
        Storage storage = new Storage(file.toString());
        storage.save(List.of(new Todo("read book")));
        assertTrue(Files.exists(file));
        assertTrue(Files.readString(file).contains("T | 0 | read book"));
    }

    @Test
    public void save_taskListOverload_writesAllTasks() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        TaskList tasks = new TaskList();
        tasks.add(new Todo("first"));
        tasks.add(new Todo("second"));
        new Storage(file.toString()).save(tasks);
        String saved = Files.readString(file);
        assertTrue(saved.contains("first"));
        assertTrue(saved.contains("second"));
    }

    @Test
    public void save_bareFileName_hasNoParentFolderAndStillWorks() throws IOException {
        // A bare file name such as "x.txt" has no parent folder, so folder creation is skipped.
        // The file is created in the working directory, so it is deleted again afterwards.
        String bareName = "storage-edge-case-test-" + System.nanoTime() + ".txt";
        Path file = Path.of(bareName);
        try {
            new Storage(bareName).save(List.of(new Todo("read book")));
            assertTrue(Files.exists(file));
            assertTrue(Files.readString(file).contains("T | 0 | read book"));
        } finally {
            Files.deleteIfExists(file);
        }
    }
}
