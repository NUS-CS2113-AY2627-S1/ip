package foodielover.parser;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class DateTimeParserNullTest {

    @Test
    public void parseDateTime_nullInput_returnsNull() {
        assertNull(DateTimeParser.parseDateTime(null));
    }

    @Test
    public void parseDate_nullInput_returnsNull() {
        assertNull(DateTimeParser.parseDate(null));
    }
}
