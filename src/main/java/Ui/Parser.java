package Ui;

import java.util.Locale;

import Exceptions.InvalidCommandException;
import Task.Deadline;
import Task.Event;
import Task.ToDo;

/** Interprets command text without printing, changing the task list, or saving history. */
public class Parser {

    /** Splits once so spaces within a task description are preserved. */
    private String[] separateInput(String input) {
        // Tabs are normalized because the save file uses them to separate fields.
        input = input.replace("\t", " ");
        return input.strip().split("\\p{javaWhitespace}+", 2);
    }

    /** Returns a recognised command in lowercase, or reports an unknown command. */
    public String parseCommand(String input) {
        String originalCommand = separateInput(input)[0];
        String command = originalCommand.toLowerCase(Locale.ROOT);
        switch (command) {
        case "bye", "help", "list", "find", "mark", "unmark", "delete", "todo", "deadline", "event":
            return command;
        default:
            throw new InvalidCommandException(originalCommand + " is not a valid command! -_-");
        }
    }

    /** Reads the search text, preserving spaces within a phrase. */
    public String parseKeyword(String input) {
        String[] separatedInput = separateInput(input);
        if (separatedInput.length < 2 || separatedInput[1].isBlank()) {
            throw new InvalidCommandException("Please provide a keyword! Use: find keyword");
        }
        return separatedInput[1].strip();
    }

    /** Reads a displayed task number; the caller checks whether that task exists. */
    public int parseTaskNumber(String input) {
        String[] separatedInput = separateInput(input);
        if (separatedInput.length != 2) {
            throw new InvalidCommandException("Please provide a task number!");
        }
        try {
            return Integer.parseInt(separatedInput[1].strip());
        } catch (NumberFormatException e) {
            throw new InvalidCommandException("Please provide a valid task number!");
        }
    }

    /** Creates a task from an add command, preserving the existing slash-separated date format. */
    public ToDo parseTask(String input) {
        String command = parseCommand(input);
        String[] separatedInput = separateInput(input);
        if (separatedInput.length < 2 || separatedInput[1].isBlank()) {
            throw new InvalidCommandException("Error! Please use the right format!");
        }
        String taskToAdd = separatedInput[1].strip();
        switch (command) {
        case "todo":
            return new ToDo(taskToAdd);
        case "deadline":
            return parseDeadline(taskToAdd);
        case "event":
            return parseEvent(taskToAdd);
        default:
            throw new InvalidCommandException("Error! Please use todo, deadline or event command!");
        }
    }

    /** Finds a complete marker, ignoring occurrences inside words or paths. */
    private int findMarker(String input, String marker) {
        int index = input.indexOf(marker);
        while (index != -1) {
            int end = index + marker.length();
            boolean startsAtBoundary = index == 0 || Character.isWhitespace(input.charAt(index - 1));
            boolean endsAtBoundary = end == input.length() || Character.isWhitespace(input.charAt(end));
            if (startsAtBoundary && endsAtBoundary) {
                return index;
            }
            index = input.indexOf(marker, index + 1);
        }
        return -1;
    }

    /** Splits a deadline at its /by marker, keeping the remaining due-date text intact. */
    private Deadline parseDeadline(String taskToAdd) {
        int idxBy = findMarker(taskToAdd, "/by");
        if (idxBy == -1) {
            throw new InvalidCommandException("Error! Try this format: deadline task /by date");
        }
        String description = taskToAdd.substring(0, idxBy).strip();
        String due = taskToAdd.substring(idxBy + "/by".length()).strip();
        if (description.isBlank() || due.isBlank()) {
            throw new InvalidCommandException("Error! Please provide valid task and due date!");
        }
        return new Deadline(description, due);
    }

    /** Reads the description, start, and end using complete /from and /to markers. */
    private Event parseEvent(String taskToAdd) {
        int fromIndex = findMarker(taskToAdd, "/from");
        int toIndex = findMarker(taskToAdd, "/to");

        if (fromIndex == -1 || toIndex == -1 || toIndex < fromIndex) {
            throw new InvalidCommandException("Use: event description /from start /to end");
        }

        String description = taskToAdd.substring(0, fromIndex).strip();
        String start = taskToAdd.substring(fromIndex + "/from".length(), toIndex).strip();
        String end = taskToAdd.substring(toIndex + "/to".length()).strip();

        if (description.isBlank() || start.isBlank() || end.isBlank()) {
            throw new InvalidCommandException("Please provide a description, start, and end!");
        }

        return new Event(description, start, end);
    }
}
