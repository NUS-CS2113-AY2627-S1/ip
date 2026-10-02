package foodielover.task;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class EventDateMatchingTest {

    @Test
    public void isOnDate_onlyStartIsARealDate_matchesStartDateOnly() {
        Event event = new Event("trip", "2026-12-01", "sometime later");
        assertTrue(event.isOnDate(LocalDate.of(2026, 12, 1)));
        assertFalse(event.isOnDate(LocalDate.of(2026, 12, 2)));
    }

    @Test
    public void isOnDate_onlyEndIsARealDate_matchesEndDateOnly() {
        Event event = new Event("trip", "from the start", "2026-12-03");
        assertTrue(event.isOnDate(LocalDate.of(2026, 12, 3)));
        assertFalse(event.isOnDate(LocalDate.of(2026, 12, 2)));
    }

    @Test
    public void isOnDate_neitherIsARealDate_neverMatches() {
        Event event = new Event("trip", "Monday", "Friday");
        assertFalse(event.isOnDate(LocalDate.of(2026, 12, 1)));
    }

    @Test
    public void isOnDate_bothDates_matchesInsideRangeInclusive() {
        Event event = new Event("camp", "2026-12-01", "2026-12-03");
        assertTrue(event.isOnDate(LocalDate.of(2026, 12, 1)));
        assertTrue(event.isOnDate(LocalDate.of(2026, 12, 2)));
        assertTrue(event.isOnDate(LocalDate.of(2026, 12, 3)));
        assertFalse(event.isOnDate(LocalDate.of(2026, 11, 30)));
        assertFalse(event.isOnDate(LocalDate.of(2026, 12, 4)));
    }
}
