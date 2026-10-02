package foodielover.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class DeadlineTest {

    @Test
    public void deadline_dateOnly_parsedCorrectly() {
        Deadline deadline = new Deadline("submit report", "2026-10-15");
        assertEquals(LocalDate.of(2026, 10, 15), deadline.getDueDate());
        assertNull(deadline.getDueDateTime());
        assertTrue(deadline.isOnDate(LocalDate.of(2026, 10, 15)));
        assertFalse(deadline.isOnDate(LocalDate.of(2026, 10, 16)));
        assertEquals("D | 0 | submit report | 2026-10-15", deadline.toFileFormat());
        assertEquals("[D][ ] submit report (by: Oct 15 2026)", deadline.toString());
    }

    @Test
    public void deadline_dateTime_parsedCorrectly() {
        Deadline deadline = new Deadline("quiz submission", "2026-10-15 9:00");
        assertEquals(LocalDate.of(2026, 10, 15), deadline.getDueDate());
        assertEquals(LocalDateTime.of(2026, 10, 15, 9, 0), deadline.getDueDateTime());
        assertTrue(deadline.isOnDate(LocalDate.of(2026, 10, 15)));
        assertEquals("D | 0 | quiz submission | 2026-10-15 9:00", deadline.toFileFormat());
        assertEquals("[D][ ] quiz submission (by: Oct 15 2026, 9:00AM)", deadline.toString());
    }

    @Test
    public void deadline_naturalLanguageDate_storedAsRawText() {
        Deadline deadline = new Deadline("return book", "tonight");
        assertNull(deadline.getDueDate());
        assertNull(deadline.getDueDateTime());
        assertFalse(deadline.isOnDate(LocalDate.now()));
        assertEquals("D | 0 | return book | tonight", deadline.toFileFormat());
        assertEquals("[D][ ] return book (by: tonight)", deadline.toString());
    }
}
