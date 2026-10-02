package foodielover.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class TodoTest {

    @Test
    public void toString_showsTypeAndStatus() {
        Todo todo = new Todo("read book");
        assertEquals("[T][ ] read book", todo.toString());
        todo.markAsDone();
        assertEquals("[T][X] read book", todo.toString());
    }

    @Test
    public void toFileFormat_usesTypeFlagAndDescription() {
        Todo todo = new Todo("read book");
        assertEquals("T | 0 | read book", todo.toFileFormat());
        todo.markAsDone();
        assertEquals("T | 1 | read book", todo.toFileFormat());
    }

    @Test
    public void isOnDate_isAlwaysFalse() {
        assertFalse(new Todo("read book").isOnDate(LocalDate.of(2026, 10, 15)));
    }
}
