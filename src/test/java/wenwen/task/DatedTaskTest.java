package wenwen.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class DatedTaskTest {
    @Test
    void deadline_toString_formatsDateForDisplay() {
        Deadline deadline = new Deadline("return book", LocalDate.of(2026, 10, 2));

        assertEquals("[D][ ] return book (by: Oct 02 2026)", deadline.toString());
    }

    @Test
    void event_occursOn_checksEntireDateRange() {
        Event event = new Event("conference",
                LocalDateTime.of(2026, 10, 2, 9, 0),
                LocalDateTime.of(2026, 10, 4, 17, 0));

        assertTrue(event.occursOn(LocalDate.of(2026, 10, 2)));
        assertTrue(event.occursOn(LocalDate.of(2026, 10, 3)));
        assertTrue(event.occursOn(LocalDate.of(2026, 10, 4)));
        assertFalse(event.occursOn(LocalDate.of(2026, 10, 5)));
    }
}
