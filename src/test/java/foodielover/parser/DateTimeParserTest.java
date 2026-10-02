package foodielover.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class DateTimeParserTest {

    @Test
    public void parseDate_validDates_success() {
        LocalDate date1 = DateTimeParser.parseDate("2019-10-15");
        assertNotNull(date1);
        assertEquals(LocalDate.of(2019, 10, 15), date1);

        LocalDate date2 = DateTimeParser.parseDate("2/12/2019");
        assertNotNull(date2);
        assertEquals(LocalDate.of(2019, 12, 2), date2);

        LocalDate date3 = DateTimeParser.parseDate("15-10-2019");
        assertNotNull(date3);
        assertEquals(LocalDate.of(2019, 10, 15), date3);
    }

    @Test
    public void parseDate_impossibleDates_returnsNull() {
        assertNull(DateTimeParser.parseDate("2019-02-30"));
        assertNull(DateTimeParser.parseDate("2021-04-31"));
        assertNull(DateTimeParser.parseDate("32/12/2019"));
        assertNull(DateTimeParser.parseDate("2019-13-01"));
    }

    @Test
    public void parseDateTime_validDateTimes_success() {
        LocalDateTime dt1 = DateTimeParser.parseDateTime("2019-10-15 0900");
        assertNotNull(dt1);
        assertEquals(LocalDateTime.of(2019, 10, 15, 9, 0), dt1);

        LocalDateTime dt2 = DateTimeParser.parseDateTime("2/12/2019 18:00");
        assertNotNull(dt2);
        assertEquals(LocalDateTime.of(2019, 12, 2, 18, 0), dt2);

        LocalDateTime dt3 = DateTimeParser.parseDateTime("2019-10-15 9:00");
        assertNotNull(dt3);
        assertEquals(LocalDateTime.of(2019, 10, 15, 9, 0), dt3);

        LocalDateTime dt4 = DateTimeParser.parseDateTime("2019-10-15 900");
        assertNotNull(dt4);
        assertEquals(LocalDateTime.of(2019, 10, 15, 9, 0), dt4);

        LocalDateTime dt5 = DateTimeParser.parseDateTime("15/10/2019 9:00");
        assertNotNull(dt5);
        assertEquals(LocalDateTime.of(2019, 10, 15, 9, 0), dt5);
    }

    @Test
    public void parseDateTime_impossibleDateTimes_returnsNull() {
        assertNull(DateTimeParser.parseDateTime("2019-02-30 1800"));
        assertNull(DateTimeParser.parseDateTime("2/12/2019 2500"));
        assertNull(DateTimeParser.parseDateTime("2/12/2019 18:61"));
    }

    @Test
    public void isDateLike_variousInputs_correctDetection() {
        assertTrue(DateTimeParser.isDateLike("2019-10-15"));
        assertTrue(DateTimeParser.isDateLike("2019-02-30"));
        assertTrue(DateTimeParser.isDateLike("2/12/2019 1800"));
        assertTrue(DateTimeParser.isDateLike("15/10/2019"));
        assertTrue(DateTimeParser.isDateLike("2019-10-15 9:00"));
        assertTrue(DateTimeParser.isDateLike("2019-10-15 900"));

        assertFalse(DateTimeParser.isDateLike("June 6th"));
        assertFalse(DateTimeParser.isDateLike("tonight"));
        assertFalse(DateTimeParser.isDateLike("tomorrow"));
        assertFalse(DateTimeParser.isDateLike(null));
    }

    @Test
    public void formatDateAndDateTime_validInputs_formattedCorrectly() {
        LocalDate date = LocalDate.of(2019, 10, 15);
        assertEquals("Oct 15 2019", DateTimeParser.formatDate(date));

        LocalDateTime dateTime = LocalDateTime.of(2019, 12, 2, 18, 0);
        assertEquals("Dec 02 2019, 6:00PM", DateTimeParser.formatDateTime(dateTime));
    }

    @Test
    public void resolveDate_variousInputs_resolvedCorrectly() {
        LocalDateTime dt = LocalDateTime.of(2026, 10, 15, 9, 0);
        assertEquals(LocalDate.of(2026, 10, 15), DateTimeParser.resolveDate("ignored", dt));

        assertEquals(LocalDate.of(2026, 10, 15), DateTimeParser.resolveDate("2026-10-15", null));
        assertNull(DateTimeParser.resolveDate("tonight", null));
    }

    @Test
    public void formatDisplay_variousInputs_formattedCorrectly() {
        LocalDateTime dt = LocalDateTime.of(2026, 12, 2, 18, 0);
        assertEquals("Dec 02 2026, 6:00PM", DateTimeParser.formatDisplay("raw", null, dt));

        LocalDate d = LocalDate.of(2026, 10, 15);
        assertEquals("Oct 15 2026", DateTimeParser.formatDisplay("raw", d, null));

        assertEquals("tonight", DateTimeParser.formatDisplay("tonight", null, null));
    }
}
