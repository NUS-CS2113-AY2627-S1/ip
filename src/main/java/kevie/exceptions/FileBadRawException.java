package kevie.exceptions;

import kevie.Kevie;
import kevie.UserInterface;

/**
 * Exception thrown when a save file line cannot be read.
 */
public class FileBadRawException extends FileCorruptionException{
    /**
     * Creates a Save file raw format parsing error exception.
     *
     * @param line Number of line in save file that is corrupted
     * @param ui User interface to print messages in
     */
    public FileBadRawException(int line, UserInterface ui) {
        super("Format incorrect on line " + line, ui);
    }

    @Override
    public void printMessage() {
        super.printMessage();
        ui.botSpeak("I will delete the corrupted line as a result.", true);
    }
}
