package kento;

import kento.exception.CommandException;

public class Parser {

    /**
     * Extracts first word as command from input string
     * 
     * @param input cli input
     * @return command
     * @throws CommandException when the input is empty
     */
    public static String parseCommand(String input) throws CommandException {
        if (input == null || input.strip().isEmpty()) {
            throw new CommandException();
        }

        String[] inputList = input.strip().split(" ");
        String cmd = inputList[0];
        return cmd.toLowerCase();
    }

    /**
     * Parses the args string from the cli input
     * 
     * @param input cli text
     * @return args without command
     */
    public static String parseArguments(String input) {
        int spaceIdx = input.indexOf(" ");
        if (spaceIdx == -1) {
            return "";
        }
        return input.substring(spaceIdx + 1).strip();
    }

    /**
     * parses a index integer from argument string
     * 
     * @param args extracted with parseArguments
     * @return
     */
    public static int parseIndexArgument(String args) {
        return Integer.parseInt(args.strip()) - 1;
    }

    /**
     * parses task and by date from input arguments
     * 
     * @param args input arguments
     * @return task and by
     * @throws CommandException when by or task is empty
     */
    public static String[] parseDeadlineArguments(String args) throws CommandException {
        String[] argsList = args.split("/by");

        if (argsList.length < 1 || argsList[0].strip().isEmpty() || argsList[1].strip().isEmpty()) {
            System.out.print("deadline error");
            throw new CommandException();
        }

        String task = argsList[0].strip();
        String by = argsList[1].strip();

        return new String[] { task, by };
    }

    /**
     * parses task, from and to date from input arguments
     * 
     * @param args input arguments
     * @return task, from and to
     * @throws CommandException when from, to or task is empty
     */
    public static String parseTodoArguments(String args) throws CommandException {
        String task = args.strip();

        if (task.isEmpty()) {
            throw new CommandException();
        }

        return task;
    }

    public static String[] parseEventArguments(String args) throws CommandException {
        String[] fromSplit = args.split("/from");

        if (fromSplit.length < 2 || fromSplit[0].strip().isEmpty()) {
            throw new CommandException();
        }

        String task = fromSplit[0].strip();

        String[] toSplit = fromSplit[1].split("/to");

        if (toSplit.length < 2 || toSplit[0].strip().isEmpty() || toSplit[1].strip().isEmpty()) {

            System.out.print("event error");
            throw new CommandException();
        }

        String from = toSplit[0].strip();
        String to = toSplit[1].strip();

        return new String[] { task, from, to };
    }
}
