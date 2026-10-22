package ace.parser;

import java.time.LocalDate;

import ace.TestSupport;
import ace.parser.Parser.ParsedCommand;
import ace.task.Deadline;
import ace.task.Event;
import ace.task.Todo;

/** Regression tests for command parsing and date validation. */
public final class ParserTest {
    private ParserTest() {
    }

    public static void run() throws Exception {
        ParsedCommand todo = Parser.parse("todo read book");
        TestSupport.equal("todo", todo.getKeyword(), "todo command");
        TestSupport.check(todo.getTask() instanceof Todo, "todo produces a Todo");
        TestSupport.equal("read book", todo.getTask().getLabel(), "todo description");

        ParsedCommand deadline = Parser.parse("deadline submit ip /by 2026-10-15");
        TestSupport.check(deadline.getTask() instanceof Deadline, "deadline task type");
        TestSupport.equal(LocalDate.of(2026, 10, 15),
                ((Deadline) deadline.getTask()).getBy(), "deadline date");

        ParsedCommand event = Parser.parse("event workshop /from 2026-10-15 /to 2026-10-16");
        TestSupport.check(event.getTask() instanceof Event, "event task type");
        TestSupport.equal(LocalDate.of(2026, 10, 15),
                ((Event) event.getTask()).getFrom(), "event start date");
        TestSupport.equal(LocalDate.of(2026, 10, 16),
                ((Event) event.getTask()).getTo(), "event end date");

        TestSupport.equal(0, Parser.parse("mark 1").getTaskNumber(), "mark converts to zero-based index");
        TestSupport.equal(1, Parser.parse("unmark 2").getTaskNumber(), "unmark task number");
        TestSupport.equal(2, Parser.parse("delete 3").getTaskNumber(), "delete task number");
        TestSupport.equal("book", Parser.parse("find book").getSearchTerm(), "find search term");
        TestSupport.equal("book cover", Parser.parse("find   book cover  ").getSearchTerm(),
                "find trims spaces");
        TestSupport.equal("list", Parser.parse("list").getKeyword(), "list command");
        TestSupport.equal("help", Parser.parse("help").getKeyword(), "help command");
        TestSupport.equal("bye", Parser.parse("bye").getKeyword(), "bye command");

        TestSupport.rejects(() -> Parser.parse(""), "empty command");
        TestSupport.rejects(() -> Parser.parse("unknown"), "unknown command");
        TestSupport.rejects(() -> Parser.parse("find"), "missing search term");
        TestSupport.rejects(() -> Parser.parse("mark 0"), "zero task number");
        TestSupport.rejects(() -> Parser.parse("mark xyz"), "non-numeric task number");
        TestSupport.rejects(() -> Parser.parse("delete"), "missing delete number");
        TestSupport.rejects(() -> Parser.parse("deadline submit ip"), "missing /by");
        TestSupport.rejects(() -> Parser.parse("deadline submit ip /by 2026-02-30"),
                "invalid calendar date");
        TestSupport.rejects(() -> Parser.parse("event workshop /from 2026-10-15"),
                "missing /to");
        TestSupport.rejects(() -> Parser.parse(
                "event workshop /from 2026-10-16 /to 2026-10-15"), "reversed event dates");
        System.out.println("PASS ParserTest");
    }
}
