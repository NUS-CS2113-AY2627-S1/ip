package wenwen.parser;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import wenwen.command.AddCommand;
import wenwen.command.DeleteCommand;
import wenwen.command.ExitCommand;
import wenwen.command.FindCommand;
import wenwen.command.ListCommand;
import wenwen.command.MarkCommand;
import wenwen.command.OnDateCommand;
import wenwen.exception.WenwenException;

class ParserTest {
    @Test
    void parse_validCommands_returnsMatchingCommandTypes() throws WenwenException {
        assertInstanceOf(ExitCommand.class, Parser.parse("bye"));
        assertInstanceOf(ListCommand.class, Parser.parse("list"));
        assertInstanceOf(MarkCommand.class, Parser.parse("mark 1"));
        assertInstanceOf(MarkCommand.class, Parser.parse("unmark 1"));
        assertInstanceOf(DeleteCommand.class, Parser.parse("delete 1"));
        assertInstanceOf(AddCommand.class, Parser.parse("todo read book"));
        assertInstanceOf(AddCommand.class, Parser.parse("deadline return book /by 2026-10-02"));
        assertInstanceOf(AddCommand.class,
                Parser.parse("event meeting /from 2026-10-02 1400 /to 2026-10-02 1500"));
        assertInstanceOf(OnDateCommand.class, Parser.parse("on 2026-10-02"));
        assertInstanceOf(FindCommand.class, Parser.parse("find book"));
    }

    @Test
    void parse_malformedCommands_throwsWenwenException() {
        assertThrows(WenwenException.class, () -> Parser.parse("unknown"));
        assertThrows(WenwenException.class, () -> Parser.parse("todo"));
        assertThrows(WenwenException.class, () -> Parser.parse("mark one"));
        assertThrows(WenwenException.class, () -> Parser.parse("deadline return book"));
        assertThrows(WenwenException.class, () -> Parser.parse("event meeting /from 2pm"));
        assertThrows(WenwenException.class, () -> Parser.parse("deadline return book /by tomorrow"));
        assertThrows(WenwenException.class, () -> Parser.parse("deadline return book /by 2026-02-30"));
        assertThrows(WenwenException.class,
                () -> Parser.parse("event meeting /from 2026-10-02 1500 /to 2026-10-02 1400"));
        assertThrows(WenwenException.class, () -> Parser.parse("on 02/10/2026"));
        assertThrows(WenwenException.class, () -> Parser.parse("find"));
    }
}
