import java.util.List;
import java.util.Locale;

import Exceptions.InvalidCommandException;
import Task.Deadline;
import Task.Event;
import Task.TasksList;
import Ui.Parser;
import Ui.Storage;

/** Runs dependency-free regression checks for parsing and saving accepted tasks. */
public class ParserTest {
    /** Checks Unicode whitespace, complete markers, and locale-independent commands. */
    public static void main(String[] args) throws Exception {
        Parser parser = new Parser();
        checkBlankFields(parser);
        checkMarkers(parser);
        checkLocale(parser);
        checkSaveAndReload(parser);
        System.out.println("PASS: Unicode fields, marker boundaries, command locale, and reload");
    }

    /** Rejects blank required fields before they can become unreadable saved tasks. */
    private static void checkBlankFields(Parser parser) {
        for (String input : List.of("todo \u2003", "deadline \u2003 /by tomorrow",
                "deadline task /by \u2003", "event \u2003 /from 2pm /to 4pm",
                "event task /from \u2003 /to 4pm", "event task /from 2pm /to \u2003")) {
            expectInvalidTask(parser, input);
        }
    }

    /** Ensures marker-like words and paths neither replace nor hide real markers. */
    private static void checkMarkers(Parser parser) {
        for (String input : List.of("deadline task /bye Friday", "deadline task/by Friday",
                "deadline task /byFriday", "event meeting /from 2pm /today 4pm",
                "event meeting /fromage 2pm /to 4pm", "event meeting/from 2pm /to 4pm",
                "event meeting /from2pm /to 4pm", "event meeting /from 2pm/to 4pm",
                "event meeting /from 2pm /to4pm", "event meeting /to 4pm /from 2pm",
                "deadline task /by", "event task /from 2pm /to")) {
            expectInvalidTask(parser, input);
        }
        Deadline deadline = (Deadline) parser.parseTask("deadline inspect /bypass /by Friday");
        if (!deadline.getDescription().equals("inspect /bypass") || !deadline.getBy().equals("Friday")) {
            throw new AssertionError("A marker prefix changed the deadline fields");
        }
        Event event = (Event) parser.parseTask("event review /tools /fromage /from 2pm /to 4pm");
        if (!event.getDescription().equals("review /tools /fromage")
                || !event.getFrom().equals("2pm") || !event.getTo().equals("4pm")) {
            throw new AssertionError("Marker prefixes changed the event fields");
        }
    }

    /** Commands must be recognised identically regardless of the machine's language. */
    private static void checkLocale(Parser parser) {
        Locale original = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"));
            for (String command : List.of("bye", "help", "list", "find", "mark", "unmark",
                    "delete", "todo", "deadline", "event")) {
                if (!parser.parseCommand(command.toUpperCase(Locale.ROOT)).equals(command)) {
                    throw new AssertionError("Command depends on locale: " + command);
                }
            }
        } finally {
            Locale.setDefault(original);
        }
    }

    /** Confirms valid Unicode input, tabs, and slash dates survive a save/load cycle. */
    private static void checkSaveAndReload(Parser parser) throws Exception {
        TasksList tasks = new TasksList();
        tasks.add(parser.parseTask("todo\u2003read book\u2003"));
        tasks.add(parser.parseTask("deadline\treturn book\u2003/by\u200329/09/2026 5pm"));
        tasks.add(parser.parseTask("event study\u2003/from\u20032pm\u2003/to\u20034pm"));
        tasks.get(1).setDone(true);
        Storage storage = new Storage();
        storage.saveTasks(tasks);
        TasksList restored = storage.loadTasks();
        if (restored.size() != 3 || !restored.get(0).getDescription().equals("read book")
                || !restored.get(1).getDescription().equals("return book")
                || !((Deadline) restored.get(1)).getBy().equals("29/09/2026 5pm")
                || !restored.get(1).getStatusIcon().equals("X")
                || !((Event) restored.get(2)).getFrom().equals("2pm")
                || !((Event) restored.get(2)).getTo().equals("4pm")) {
            throw new AssertionError("Valid parsed tasks did not survive saving and loading");
        }
    }

    /** Fails if invalid input produces a task rather than the expected user-facing error. */
    private static void expectInvalidTask(Parser parser, String input) {
        try {
            parser.parseTask(input);
        } catch (InvalidCommandException expected) {
            return;
        }
        throw new AssertionError("Expected invalid task: " + input);
    }
}
