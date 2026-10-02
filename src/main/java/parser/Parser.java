package parser;

import constants.Constants;

import java.util.Scanner;

/** Reads commands from standard input for {@code AbstractFella}. */
public class Parser {
    private final Constants c;

    /** Creates a parser that uses the supplied input prompt.
     * @param c constants containing the input marker
     */
    public Parser(Constants c) {
        this.c = c;
    }


    /** Reads and returns one command; {@code AbstractFella.run()} calls this method.
     * @return the next line entered by the user
     */
    public String getInput() {
        String input;

        Scanner scanner = new Scanner(System.in); //should be closed at some point?
        System.out.print(c.INPUT_MARKER_STRING); // your inputs will be denoted by triple ">>>"
        input = scanner.nextLine();

        return input;
    }
}
