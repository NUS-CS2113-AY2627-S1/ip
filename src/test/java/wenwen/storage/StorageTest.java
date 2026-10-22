package wenwen.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import wenwen.task.Deadline;
import wenwen.task.Event;
import wenwen.task.Task;

class StorageTest {
    @TempDir
    private Path temporaryFolder;

    @Test
    void saveAndLoadTasks_datedTasks_preservesDateValues() throws Exception {
        Storage storage = new Storage(temporaryFolder.resolve("data/wenwen.txt"));
        Deadline deadline = new Deadline("submit work", LocalDate.of(2026, 10, 2));
        Event event = new Event("presentation",
                LocalDateTime.of(2026, 10, 3, 10, 0),
                LocalDateTime.of(2026, 10, 3, 11, 30));

        storage.saveTasks(List.of(deadline, event));
        List<Task> loadedTasks = storage.loadTasks();

        assertEquals(LocalDate.of(2026, 10, 2), ((Deadline) loadedTasks.get(0)).getBy());
        assertEquals(LocalDateTime.of(2026, 10, 3, 10, 0), ((Event) loadedTasks.get(1)).getFrom());
        assertEquals(LocalDateTime.of(2026, 10, 3, 11, 30), ((Event) loadedTasks.get(1)).getTo());
    }
}
