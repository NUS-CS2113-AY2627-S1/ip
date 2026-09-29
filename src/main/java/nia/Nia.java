package nia;

import nia.content.VoicelineManager;
import nia.exceptions.NiaException;
import nia.parser.Parser;
import nia.processor.Processor;
import nia.storage.Storage;
import nia.tasks.TaskList;
import nia.ui.InputReader;
import nia.ui.Printer;

/**
 * Entry point for Nia, a CLI task-tracking assistant.
 * Runs a read-parse-process loop: InputReader reads a line, Parser sanitizes it into
 * canonical words, and Processor executes the corresponding command.
 */
public class Nia {

    /** Reads commands from stdin in a loop until a "bye"-aliased command is entered. */
    public static void main(String[] args) {
        printStarter();

        TaskList taskList = Storage.load();

        boolean running = true;
        while (running) {
            String rawInput = readInput();

            try {
                String[] words = Parser.parse(rawInput);
                if (words.length == 0) {
                    continue;
                }
                running = Processor.process(words, taskList);
                Storage.save(taskList);
            } catch (NiaException e) {
                Printer.printIndentedError(e.getMessage());
                Printer.printIndent(e.getVoiceline());
            }
        }

        printEnding();
    }

    /** Prints the startup banner, greeting, and a random opening voiceline. */
    private static void printStarter() {
        Printer.printBar();
        Printer.printIndent(VoicelineManager.getBanner());
        Printer.printIndent("Hi, my name's Nia.");
        Printer.printIndent(VoicelineManager.getStartingLine());
    }

    /** Prints a random farewell voiceline. */
    private static void printEnding() {
        Printer.printIndent(VoicelineManager.getEndingLine());
    }

    /** Reads one line of input, framed with bars and a prompt above/below it. */
    private static String readInput() {
        Printer.printBar();
        Printer.printPrompt();
        String input = InputReader.getUserInput();
        Printer.printBar();
        return input;
    }
}