package foodielover.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class EventTest {

    @Test
    public void event_dates_isOnDateRangeCheck() {
        Event event = new Event("orientation camp", "2026-10-15", "2026-10-17");
        assertFalse(event.isOnDate(LocalDate.of(2026, 10, 14)));
        assertTrue(event.isOnDate(LocalDate.of(2026, 10, 15)));
        assertTrue(event.isOnDate(LocalDate.of(2026, 10, 16)));
        assertTrue(event.isOnDate(LocalDate.of(2026, 10, 17)));
        assertFalse(event.isOnDate(LocalDate.of(2026, 10, 18)));

        assertEquals("E | 0 | orientation camp | 2026-10-15 | 2026-10-17", event.toFileFormat());
        assertEquals("[E][ ] orientation camp (from: Oct 15 2026 to: Oct 17 2026)", event.toString());
    }

    @Test
    public void event_dateTimes_formattedCorrectly() {
        Event event = new Event("hackathon", "2026-10-15 900", "2026-10-15 1700");
        assertTrue(event.isOnDate(LocalDate.of(2026, 10, 15)));
        assertEquals("[E][ ] hackathon (from: Oct 15 2026, 9:00AM to: Oct 15 2026, 5:00PM)", event.toString());
    }

    @Test
    public void event_rawStrings_fallbackHandling() {
        Event event = new Event("food party", "noon", "midnight");
        assertFalse(event.isOnDate(LocalDate.now()));
        assertEquals("E | 0 | food party | noon | midnight", event.toFileFormat());
        assertEquals("[E][ ] food party (from: noon to: midnight)", event.toString());
    }
}
