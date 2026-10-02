package foodielover.parser;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import foodielover.FoodieloverException;
import foodielover.command.AddCommand;
import foodielover.command.Command;
import foodielover.command.DateFilterCommand;
import foodielover.command.DeleteCommand;
import foodielover.command.ExitCommand;
import foodielover.command.FindCommand;
import foodielover.command.ListCommand;
import foodielover.command.MarkCommand;
import foodielover.command.UnmarkCommand;

public class ParserTest {

    /**
     * Parses the input, expects a FoodieloverException, and returns its message.
     */
    private static String errorOf(String input) {
        FoodieloverException exception = assertThrows(FoodieloverException.class, () -> Parser.parse(input));
        return exception.getMessage();
    }

    // ------------------------------------------------------------------
    // Existing tests
    // ------------------------------------------------------------------

    @Test
    public void parse_validCommands_returnsCorrectCommandInstance() throws FoodieloverException {
        Command byeCmd = Parser.parse("bye");
        assertInstanceOf(ExitCommand.class, byeCmd);

        Command listCmd = Parser.parse("list");
        assertInstanceOf(ListCommand.class, listCmd);

        Command todoCmd = Parser.parse("todo read book");
        assertInstanceOf(AddCommand.class, todoCmd);
    }

    @Test
    public void parse_whitespaceAndCaseTolerance_success() throws FoodieloverException {
        assertInstanceOf(ExitCommand.class, Parser.parse("  BYE  "));
        assertInstanceOf(ListCommand.class, Parser.parse("  list  "));
        assertInstanceOf(AddCommand.class, Parser.parse("  TODO borrow book  "));
    }

    @Test
    public void parse_pipeInDescription_throwsException() {
        FoodieloverException e1 = assertThrows(FoodieloverException.class, () ->
                Parser.parse("todo read | book"));
        assertTrue(e1.getMessage().contains("pipe"));

        FoodieloverException e2 = assertThrows(FoodieloverException.class, () ->
                Parser.parse("deadline submit | report /by 2026-10-15"));
        assertTrue(e2.getMessage().contains("pipe"));

        FoodieloverException e3 = assertThrows(FoodieloverException.class, () ->
                Parser.parse("event carnival /from 2026-10-15 /to 2026-10-16 | party"));
        assertTrue(e3.getMessage().contains("pipe"));
    }

    @Test
    public void parse_impossibleDateInDeadline_throwsException() {
        FoodieloverException exception = assertThrows(FoodieloverException.class, () ->
                Parser.parse("deadline return book /by 2019-02-30"));
        assertTrue(exception.getMessage().contains("Invalid date or time"));
    }

    @Test
    public void parse_eventEndBeforeStart_throwsException() {
        FoodieloverException exception = assertThrows(FoodieloverException.class, () ->
                Parser.parse("event party /from 2019-12-02 1800 /to 2019-12-01 1800"));
        assertTrue(exception.getMessage().contains("cannot be earlier"));
    }

    @Test
    public void parse_emptyOrInvalidInput_throwsException() {
        assertThrows(FoodieloverException.class, () -> Parser.parse(""));
        assertThrows(FoodieloverException.class, () -> Parser.parse("   "));

        FoodieloverException exception = assertThrows(FoodieloverException.class, () ->
                Parser.parse("unknown-command"));
        assertTrue(exception.getMessage().contains("This is not a valid input"));
    }

    // ------------------------------------------------------------------
    // General input handling
    // ------------------------------------------------------------------

    @Test
    public void parse_nullInput_throwsException() {
        assertTrue(errorOf(null).contains("Please enter a command"));
    }

    @Test
    public void parse_unknownCommand_messageListsEveryCommand() {
        String message = errorOf("dance");
        for (String command : new String[] {"todo", "deadline", "event", "list", "mark", "unmark",
                "delete", "find", "date", "on", "bye"}) {
            assertTrue(message.contains(command), "Message should mention: " + command);
        }
    }

    @Test
    public void parse_commandWordsAreCaseInsensitive_success() throws FoodieloverException {
        assertInstanceOf(MarkCommand.class, Parser.parse("MARK 1"));
        assertInstanceOf(DeleteCommand.class, Parser.parse("Delete 2"));
        assertInstanceOf(FindCommand.class, Parser.parse("FIND book"));
        assertInstanceOf(AddCommand.class, Parser.parse("DEADLINE essay /by 2026-10-15"));
        assertInstanceOf(DateFilterCommand.class, Parser.parse("On 2026-10-15"));
    }

    // ------------------------------------------------------------------
    // mark, unmark, delete
    // ------------------------------------------------------------------

    @Test
    public void parse_taskNumberCommands_validNumber_returnsMatchingCommand() throws FoodieloverException {
        assertInstanceOf(MarkCommand.class, Parser.parse("mark 1"));
        assertInstanceOf(UnmarkCommand.class, Parser.parse("unmark 3"));
        assertInstanceOf(DeleteCommand.class, Parser.parse("delete 10"));
        assertInstanceOf(MarkCommand.class, Parser.parse("  mark   2  "));
    }

    @Test
    public void parse_taskNumberCommands_missingNumber_throwsException() {
        assertTrue(errorOf("mark").contains("Please enter something after 'mark'"));
        assertTrue(errorOf("unmark").contains("Please enter something after 'unmark'"));
        assertTrue(errorOf("delete").contains("Please enter something after 'delete'"));
        assertTrue(errorOf("delete   ").contains("Please enter something after 'delete'"));
    }

    @Test
    public void parse_taskNumberCommands_nonNumeric_throwsException() {
        assertTrue(errorOf("mark abc").contains("Please enter a number after 'mark'"));
        assertTrue(errorOf("unmark two").contains("Please enter a number after 'unmark'"));
        assertTrue(errorOf("delete 1.5").contains("Please enter a number after 'delete'"));
        assertTrue(errorOf("mark 1 2").contains("Please enter a number after 'mark'"));
        assertTrue(errorOf("delete 99999999999").contains("Please enter a number after 'delete'"));
    }

    // ------------------------------------------------------------------
    // todo
    // ------------------------------------------------------------------

    @Test
    public void parse_todo_emptyDescription_throwsException() {
        assertTrue(errorOf("todo").contains("Please enter a description after 'todo'"));
        assertTrue(errorOf("todo    ").contains("Please enter a description after 'todo'"));
    }

    // ------------------------------------------------------------------
    // deadline
    // ------------------------------------------------------------------

    @Test
    public void parse_deadline_validInputs_returnsAddCommand() throws FoodieloverException {
        assertInstanceOf(AddCommand.class, Parser.parse("deadline essay /by 2026-10-15"));
        assertInstanceOf(AddCommand.class, Parser.parse("deadline essay /by 2/12/2026"));
        assertInstanceOf(AddCommand.class, Parser.parse("deadline essay /by 2026-10-15 1800"));
        assertInstanceOf(AddCommand.class, Parser.parse("deadline essay /by 2026-10-15 9:00"));
        assertInstanceOf(AddCommand.class, Parser.parse("deadline essay /by 2026-10-15 900"));
        assertInstanceOf(AddCommand.class, Parser.parse("deadline essay /by tonight"));
        assertInstanceOf(AddCommand.class, Parser.parse("deadline essay /by June 6th"));
    }

    @Test
    public void parse_deadline_missingBy_throwsException() {
        assertTrue(errorOf("deadline").contains("'/by'"));
        assertTrue(errorOf("deadline return book").contains("'/by'"));
        assertTrue(errorOf("deadline return book 2026-10-15").contains("'/by'"));
    }

    @Test
    public void parse_deadline_emptyDescriptionOrValue_throwsException() {
        assertTrue(errorOf("deadline /by 2026-10-15").contains("both a deadline description"));
        assertTrue(errorOf("deadline return book /by").contains("both a deadline description"));
        assertTrue(errorOf("deadline return book /by    ").contains("both a deadline description"));
    }

    @Test
    public void parse_deadline_invalidTimeOfDay_throwsException() {
        assertTrue(errorOf("deadline essay /by 2026-10-15 25:00").contains("Invalid date or time"));
        assertTrue(errorOf("deadline essay /by 2026-10-15 960").contains("Invalid date or time"));
        assertTrue(errorOf("deadline essay /by 2026-13-45").contains("Invalid date or time"));
    }

    @Test
    public void parse_deadline_pipeInDateValue_throwsException() {
        assertTrue(errorOf("deadline essay /by 2026-10-15 | 1800").contains("pipe"));
    }

    // ------------------------------------------------------------------
    // event
    // ------------------------------------------------------------------

    @Test
    public void parse_event_validInputs_returnsAddCommand() throws FoodieloverException {
        assertInstanceOf(AddCommand.class,
                Parser.parse("event camp /from 2026-12-01 /to 2026-12-03"));
        assertInstanceOf(AddCommand.class,
                Parser.parse("event party /from 2026-12-02 9:00 /to 2026-12-02 18:00"));
        assertInstanceOf(AddCommand.class,
                Parser.parse("event meeting /from Mon 2pm /to 4pm"));
    }

    @Test
    public void parse_event_sameStartAndEnd_isAllowed() throws FoodieloverException {
        assertInstanceOf(AddCommand.class,
                Parser.parse("event launch /from 2026-12-02 1800 /to 2026-12-02 1800"));
        assertInstanceOf(AddCommand.class,
                Parser.parse("event holiday /from 2026-12-02 /to 2026-12-02"));
    }

    @Test
    public void parse_event_missingDelimiters_throwsException() {
        assertTrue(errorOf("event").contains("'/from'"));
        assertTrue(errorOf("event party").contains("'/from'"));
        assertTrue(errorOf("event party /from 2026-12-02").contains("'/to'"));
        assertTrue(errorOf("event party /to 2026-12-02").contains("'/from'"));
    }

    @Test
    public void parse_event_delimitersInWrongOrder_throwsException() {
        assertTrue(errorOf("event party /to 2026-12-02 /from 2026-12-01").contains("'/from'"));
    }

    @Test
    public void parse_event_emptyFields_throwsException() {
        assertTrue(errorOf("event /from 2026-12-01 /to 2026-12-02").contains("event description"));
        assertTrue(errorOf("event party /from /to 2026-12-02").contains("event description"));
        assertTrue(errorOf("event party /from 2026-12-01 /to").contains("event description"));
    }

    @Test
    public void parse_event_invalidStartOrEndDate_throwsException() {
        assertTrue(errorOf("event party /from 2026-02-30 /to 2026-03-01")
                .contains("Invalid start date or time"));
        assertTrue(errorOf("event party /from 2026-03-01 /to 2026-02-30")
                .contains("Invalid end date or time"));
    }

    @Test
    public void parse_event_endDateBeforeStartDate_throwsException() {
        assertTrue(errorOf("event camp /from 2026-12-03 /to 2026-12-01")
                .contains("end date cannot be earlier"));
    }

    @Test
    public void parse_event_endTimeBeforeStartTimeSameDay_throwsException() {
        assertTrue(errorOf("event party /from 2026-12-02 1800 /to 2026-12-02 0900")
                .contains("end time cannot be earlier"));
    }

    // ------------------------------------------------------------------
    // find
    // ------------------------------------------------------------------

    @Test
    public void parse_find_validKeyword_returnsFindCommand() throws FoodieloverException {
        assertInstanceOf(FindCommand.class, Parser.parse("find book"));
        assertInstanceOf(FindCommand.class, Parser.parse("find read the book"));
    }

    @Test
    public void parse_find_missingKeyword_throwsException() {
        assertTrue(errorOf("find").contains("Please enter a keyword after 'find'"));
        assertTrue(errorOf("find    ").contains("Please enter a keyword after 'find'"));
    }

    // ------------------------------------------------------------------
    // date and on
    // ------------------------------------------------------------------

    @Test
    public void parse_dateFilter_validDate_returnsDateFilterCommand() throws FoodieloverException {
        assertInstanceOf(DateFilterCommand.class, Parser.parse("date 2026-10-15"));
        assertInstanceOf(DateFilterCommand.class, Parser.parse("on 2026-10-15"));
        assertInstanceOf(DateFilterCommand.class, Parser.parse("date 2/12/2026"));
    }

    @Test
    public void parse_dateFilter_missingDate_throwsException() {
        assertTrue(errorOf("date").contains("Please enter a date after 'date'"));
        assertTrue(errorOf("on").contains("Please enter a date after 'on'"));
    }

    @Test
    public void parse_dateFilter_invalidDate_throwsException() {
        assertTrue(errorOf("date 2019-02-30").contains("valid date"));
        assertTrue(errorOf("on tomorrow").contains("valid date"));
        assertTrue(errorOf("date 2026-13-01").contains("valid date"));
    }
}
