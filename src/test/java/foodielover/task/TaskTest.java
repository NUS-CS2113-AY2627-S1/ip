package foodielover.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class TaskTest {

    @Test
    public void task_initialState_isNotDone() {
        Task task = new Task("read book");
        assertEquals("read book", task.getDescription());
        assertEquals(" ", task.getStatusIcon());
        assertEquals("0 | read book", task.toFileFormat());
        assertEquals("[ ] read book", task.toString());
        assertFalse(task.isOnDate(LocalDate.now()));
    }

    @Test
    public void markAndUnmark_updatesStatus() {
        Task task = new Task("borrow book");
        task.markAsDone();
        assertEquals("X", task.getStatusIcon());
        assertEquals("1 | borrow book", task.toFileFormat());
        assertEquals("[X] borrow book", task.toString());

        task.markAsUndone();
        assertEquals(" ", task.getStatusIcon());
        assertEquals("0 | borrow book", task.toFileFormat());
        assertEquals("[ ] borrow book", task.toString());
    }

    @Test
    public void containsKeyword_caseInsensitiveMatching() {
        Task task = new Task("Return library book");
        assertTrue(task.containsKeyword("book"));
        assertTrue(task.containsKeyword("BOOK"));
        assertTrue(task.containsKeyword("library"));
        assertFalse(task.containsKeyword("nonexistent"));
    }
}
