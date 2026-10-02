package foodielover.parser;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import foodielover.FoodieloverException;
import foodielover.command.AddCommand;

/**
 * Covers the remaining decision branches in Parser's event handling.
 */
public class ParserBranchTest {

    private static String errorOf(String input) {
        return assertThrows(FoodieloverException.class, () -> Parser.parse(input)).getMessage();
    }

    @Test
    public void parse_event_pipeInDescription_isRejected() {
        assertTrue(errorOf("event a | b /from 2026-12-01 /to 2026-12-02").contains("pipe"));
    }

    @Test
    public void parse_event_pipeInStart_isRejected() {
        assertTrue(errorOf("event camp /from 2026-12-01 | x /to 2026-12-02").contains("pipe"));
    }

    @Test
    public void parse_event_pipeInEnd_isRejected() {
        assertTrue(errorOf("event camp /from 2026-12-01 /to 2026-12-02 | x").contains("pipe"));
    }

    @Test
    public void parse_event_startWithTimeAndEndWithoutTime_isAccepted() throws FoodieloverException {
        assertInstanceOf(AddCommand.class,
                Parser.parse("event camp /from 2026-12-02 1800 /to 2026-12-03"));
    }

    @Test
    public void parse_event_startWithoutTimeAndEndWithTime_isAccepted() throws FoodieloverException {
        assertInstanceOf(AddCommand.class,
                Parser.parse("event camp /from 2026-12-02 /to 2026-12-03 1800"));
    }

    @Test
    public void parse_event_startIsPlainTextAndEndIsDate_isAccepted() throws FoodieloverException {
        assertInstanceOf(AddCommand.class,
                Parser.parse("event camp /from next week /to 2026-12-03"));
    }

    @Test
    public void parse_event_startIsDateAndEndIsPlainText_isAccepted() throws FoodieloverException {
        assertInstanceOf(AddCommand.class,
                Parser.parse("event camp /from 2026-12-03 /to after exams"));
    }

    @Test
    public void parse_event_bothPlainText_isAccepted() throws FoodieloverException {
        assertInstanceOf(AddCommand.class,
                Parser.parse("event meeting /from Monday 2pm /to Monday 4pm"));
    }
}
